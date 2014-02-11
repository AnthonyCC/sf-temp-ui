package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.external.ExternalRecommender;
import com.freshdirect.smartstore.external.ExternalRecommenderCommunicationException;
import com.freshdirect.smartstore.external.ExternalRecommenderRegistry;
import com.freshdirect.smartstore.external.ExternalRecommenderRequest;
import com.freshdirect.smartstore.external.ExternalRecommenderType;
import com.freshdirect.smartstore.external.NoSuchExternalRecommenderException;
import com.freshdirect.smartstore.external.RecommendationItem;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductRecommenderUtil {
	private static Category LOGGER = LoggerFactory.getInstance(ProductRecommenderUtil.class);

	public static final int MAX_DEPT_FEATURED_RECOMMENDER_COUNT = 20;
	public static final int MAX_DEPT_MERCHANT_RECOMMENDER_COUNT = 5;
	public static final int MAX_CAT_MERCHANT_RECOMMENDER_COUNT = 10;
	
	public static final int MAX_UPSELL_PRODS = 12;
	public static final int MAX_XSELL_PRODS = 12;
	
    public static Recommendations doRecommend( FDUserI user, HttpSession session, EnumSiteFeature siteFeat, int maxItems, Set<ContentKey> listContent, ContentNodeModel currentNode ) throws FDResourceException {
    	
		FDStoreRecommender recommender = FDStoreRecommender.getInstance();	    
		Recommendations results = recommender.getRecommendations(siteFeat, user, createSessionInput( session, user, maxItems, currentNode, listContent ) );
		persistToSession(session, results);
		return results;
    }

    @SuppressWarnings("unchecked")
	public static SessionInput createSessionInput(HttpSession session, FDUserI user, int maxItems, ContentNodeModel currentNode, Set<ContentKey> listContent ) {
    	
		SessionInput si = new SessionInput(user);		
		
		if ( session != null ) {
			si.setPreviousRecommendations((Map<String, List<ContentKey>>) session.getAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS));
		}
		
		si.setMaxRecommendations(maxItems);
		si.setExcludeAlcoholicContent(false);
		si.setCurrentNode( currentNode );
		
		if ( listContent != null && listContent.size() > 0 ) {
			si.setCartContents( listContent );
		}
		
		return si;
    }
    
	public static void persistToSession(HttpSession session, Recommendations r) {
		if ( session != null ) {
	        Map<String, List<ContentKey>> previousRecommendations = r.getPreviousRecommendations();
	        if (previousRecommendations!=null) {
	            session.setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, previousRecommendations);
	        }
		}
    }    

	
	public static List<ProductModel> getFeaturedRecommenderProducts(DepartmentModel deptModel, HttpSession session) throws FDResourceException {
		List<ProductModel> products = new ArrayList<ProductModel>();
		
		CategoryModel sourceCat = deptModel.getFeaturedRecommenderSourceCategory();
		
		if (sourceCat == null){
			EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(deptModel.getFeaturedRecommenderSiteFeature());
			
			if (siteFeat!=null) {
				FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
				Recommendations results = doRecommend(user, session, siteFeat, MAX_DEPT_FEATURED_RECOMMENDER_COUNT, new HashSet<ContentKey>(), deptModel);
				products = results.getAllProducts(); //TODO de we need to provide site feature id for CM?
			}
			
		} else {
			products = sourceCat.getAllChildProductsAsList();
			cleanUpProducts(products, deptModel.isFeaturedRecommenderRandomizeProducts(), MAX_DEPT_FEATURED_RECOMMENDER_COUNT);
		}
		
		return products;
	}

	public static List<ProductModel> getMerchantRecommenderProducts (DepartmentModel deptModel){
		List<ProductModel> products = deptModel.getMerchantRecommenderProducts();
		cleanUpProducts(products, deptModel.isMerchantRecommenderRandomizeProducts(), MAX_DEPT_MERCHANT_RECOMMENDER_COUNT);
		return products;
	}

	public static List<ProductModel> getMerchantRecommenderProducts (CategoryModel catModel){
		List<ProductModel> products = catModel.getCatMerchantRecommenderProducts();
		cleanUpProducts(products, catModel.isCatMerchantRecommenderRandomizeProducts(), MAX_CAT_MERCHANT_RECOMMENDER_COUNT);
		return products;
	}
	
	/**
	 * Provide upsell product list for a given products.
	 * It also ensures no invalid or unavailable product is yielded.
	 * 
	 * @param product
	 * @return
	 */
	public static List<ProductModel> getUpsellProducts(ProductModel product) {
		// pick list from CMS
		List<ProductModel> products = product.getUpSellProducts();
		LOGGER.debug("  [UPSELL] Found " + products.size() + " upsell products in CMS ..");
		cleanUpProducts(products, false, MAX_UPSELL_PRODS);
		LOGGER.debug("  .. " + products.size() + " are available");
		return products;
	}


	/**
	 * Provide cross-sell product list for a given products.
	 * It also ensures no invalid or unavailable product is yielded.
	 * 
	 * @param product
	 * @return
	 */
	public static List<ProductModel> getCrossSellProducts(ProductModel product, FDUserI user) {
		// pick list from CMS
		List<ProductModel> products = product.getCrossSellProducts();
		LOGGER.debug("  [XSELL] Found " + products.size() + " cross-sell products in CMS ..");
		cleanUpProducts(products, false, MAX_XSELL_PRODS);
		LOGGER.debug("  .. " + products.size() + " are available");
		
		if (products.size() < MAX_XSELL_PRODS) {
			// back-fill with scarab recommender

//			final SessionInput si = new SessionInput(user);
//			si.setCurrentNode(product);
//			si.setMaxRecommendations(MAX_XSELL_PRODS*2);
			
			// invoke scarab recommender
			List<ContentNodeModel> nodez = new ArrayList<ContentNodeModel>();
			nodez.add(product);
			
			
			
			// List<ContentNodeModel> cnodes = HelperFunctions.getRelatedExternalRecommendations(nodez, "scarab1", si);
			try {
				final String providerName = "scarab1";
				List<ContentNodeModel> cnodes = new ArrayList<ContentNodeModel>();
				
				ExternalRecommender recommender = ExternalRecommenderRegistry.getInstance(providerName, ExternalRecommenderType.RELATED);
				List<RecommendationItem> requestItems = new ArrayList<RecommendationItem>();
				for (ContentNodeModel node : nodez) {
					requestItems.add(new RecommendationItem(node.getContentKey().getId()));
				}

				List<RecommendationItem> items = recommender.recommendItems(new ExternalRecommenderRequest(requestItems));
				for (RecommendationItem item : items) {
					ContentNodeModel node = ContentFactory.getInstance().getContentNode(item.getId());
					if (node != null) {
						cnodes.add(node);
					}
				}

				// APPDEV-1633 trace nodes
				// input.traceContentNodes(providerName, nodes);

				// return nodes;

			
				// post works
				List<ProductModel> prds2 = new ArrayList<ProductModel>();
				for (ContentNodeModel cnode : cnodes) {
					if (cnode instanceof ProductModel) {
						prds2.add( (ProductModel) cnode);
					}
				}
				LOGGER.debug("  [XSELL] Got " + prds2.size() + " recommendations ..");
				cleanUpProducts(prds2, false, MAX_XSELL_PRODS);
				LOGGER.debug("  .. " + prds2.size() + " are available");

				// complement list
				if (prds2.size() > 0) {
					int rem = MAX_XSELL_PRODS-products.size();
					int k = Math.min(rem, prds2.size());

					products.addAll(  prds2.subList(0, k) );
				}
			} catch (IllegalArgumentException e) {
				LOGGER.error(e);
			} catch (NoSuchExternalRecommenderException e) {
				LOGGER.error(e);
			} catch (ExternalRecommenderCommunicationException e) {
				LOGGER.error(e);
			}
		}
		
		return products;
	}


	public static void cleanUpProducts (List<ProductModel> products, boolean randomize, int maxCnt){
		if (products == null || products.size() == 0)
			return;
		
		Iterator<ProductModel> it = products.iterator();
		while (it.hasNext()) {
		    if (!it.next().isFullyAvailable()) {
		        it.remove();
		    }
		}
		
		if (randomize){
			Collections.shuffle(products);
		}
		
		if (products.size() > maxCnt){
			products = products.subList(0, maxCnt-1);
		}
	}
}
