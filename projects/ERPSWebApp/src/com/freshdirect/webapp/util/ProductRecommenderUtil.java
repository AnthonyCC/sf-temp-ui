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
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductRecommenderUtil {
	private static Category LOGGER = LoggerFactory.getInstance(ProductRecommenderUtil.class);

	public static final int MAX_LIST_CONTENT_SIZE = 20;
	
	public static final int MAX_DEPT_FEATURED_RECOMMENDER_COUNT = 20;
	public static final int MAX_DEPT_MERCHANT_RECOMMENDER_COUNT = 5;
	public static final int MAX_CAT_MERCHANT_RECOMMENDER_COUNT = 10;
	public static final int MAX_CAT_SCARAB_RECOMMENDER_COUNT = 10;
	
	public static final int MAX_UPSELL_PRODS = 12;
	public static final int MAX_XSELL_PRODS = 12;
	

    public static Recommendations doRecommend( FDUserI user, HttpSession session, EnumSiteFeature siteFeat, int maxItems, Set<ContentKey> listContent, ContentNodeModel currentNode ) throws FDResourceException {
    	
		FDStoreRecommender recommender = FDStoreRecommender.getInstance();	    

		//listContent should not be larger than MAX_LIST_CONTENT_SIZE for scarab to work well (limit could be larger but wouldn't make more sense)
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

	
	public static List<ProductModel> getFeaturedRecommenderProducts(DepartmentModel deptModel, FDSessionUser user, HttpSession session) throws FDResourceException {
		List<ProductModel> products = new ArrayList<ProductModel>();
		
		CategoryModel sourceCat = deptModel.getFeaturedRecommenderSourceCategory();
		
		if (sourceCat == null){
			EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(deptModel.getFeaturedRecommenderSiteFeature());
			
			if (siteFeat!=null) {
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

	public static ProductModel getBrowseRecommendation (ProductModel product){
		List<ProductModel> browseRecommendations = null;
		
		String browseRecommenderType = product.getBrowseRecommenderType();
		if ("PDP_XSELL".equals(browseRecommenderType)) {
			browseRecommendations = product.getCrossSellProducts();
			
		} else if ("PDP_UPSELL".equals(browseRecommenderType)) {
			browseRecommendations = product.getUpSellProducts();
		}
		
		if (browseRecommendations !=null){
			cleanUpProducts(browseRecommendations, false, 1);
			if (browseRecommendations.size()>0) {
				return browseRecommendations.get(0);
			}
		}
				
		return null;
	}
	
	
	public static Recommendations getBrowseCategoryListingPageRecommendations(FDUserI user, ContentNodeModel contentNode) throws FDResourceException{
		Recommendations recommendations = null;

		if (user.getIdentity() != null){ //try personal if user is identified
			recommendations = doRecommend(user, null, EnumSiteFeature.getEnum("SCARAB_PERSONAL"), MAX_CAT_SCARAB_RECOMMENDER_COUNT, null, null);	
		}
		
		if (recommendations == null || recommendations.getAllProducts().size() == 0){ //fallback
			recommendations = doRecommend(user, null, EnumSiteFeature.getEnum("SCR_FEAT_ITEMS"), MAX_CAT_SCARAB_RECOMMENDER_COUNT, null, contentNode); //TODO verify site feature	
		}
		
		return recommendations;
	}

	public static Recommendations getBrowseProductListingPageRecommendations(FDUserI user, Set<ContentKey> keys) throws FDResourceException{
		return doRecommend(user, null, EnumSiteFeature.getEnum("SCARAB_CART"), MAX_CAT_SCARAB_RECOMMENDER_COUNT, keys, null);	
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

			try {
				// Use YMAL_PDTL recommender
				Recommendations r = ProductRecommenderUtil.doRecommend(
						user, null, EnumSiteFeature.getEnum("YMAL_PDTL"),
						MAX_XSELL_PRODS,
						Collections.<ContentKey> emptySet(), product);
				List<ProductModel> scarabProds = r.getAllProducts();

				LOGGER.debug("  [XSELL] Got " + scarabProds.size() + " recommendations ..");
				cleanUpProducts(scarabProds, false, MAX_XSELL_PRODS);
				LOGGER.debug("  .. " + scarabProds.size() + " are available");

				// complement static list with recomended items
				if (scarabProds.size() > 0) {
					int rem = MAX_XSELL_PRODS-products.size();
					int k = Math.min(rem, scarabProds.size());

					products.addAll(  scarabProds.subList(0, k) );
				}
			} catch (FDResourceException e) {
				LOGGER.error("Failed to invoke Scarab recommender", e);
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
