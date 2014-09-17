package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.CertonaTransitionUtil;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.smartstore.Impression;

public class ProductRecommenderUtil {
	
	private static final String MERCHANT_RECOMMENDATION_VARIANT = "merch";

	private static Category LOGGER = LoggerFactory.getInstance(ProductRecommenderUtil.class);

	public static final int MAX_LIST_CONTENT_SIZE = 20;
	
	public static final int MAX_DEPT_FEATURED_RECOMMENDER_COUNT = 20;
	public static final int MAX_DEPT_MERCHANT_RECOMMENDER_COUNT = 5;
	public static final int MAX_CAT_MERCHANT_RECOMMENDER_COUNT = 10;
	public static final int MAX_CAT_SCARAB_RECOMMENDER_COUNT = 10;
	public static final int MAX_UNAVAILABLE_REPLACEMENTS_COUNT = 9;
	
	public static final int MAX_UPSELL_PRODS = 12;
	public static final int MAX_XSELL_PRODS = 12;
	
	/**
	 * 
	 * @param user Actual user
	 * @param session HTTP session (optional). Currently used to cache previous result.
	 * @param siteFeat Smart Store Site Feature
	 * @param maxItems Maximum number of recommended items
	 * @param listContent List of content keys. They are required for certain type of recommenders such as ones offering items for cart content.
	 * @param currentNode Triggering content node (optional)
	 * 
	 * @return 
	 * 
	 * @throws FDResourceException
	 */
    public static Recommendations doRecommend( FDUserI user, HttpSession session, EnumSiteFeature siteFeat, int maxItems, Set<ContentKey> listContent, ContentNodeModel currentNode ) throws FDResourceException {
    	
		FDStoreRecommender recommender = FDStoreRecommender.getInstance();	    

		//listContent should not be larger than MAX_LIST_CONTENT_SIZE for scarab to work well (limit could be larger but wouldn't make more sense)
		Recommendations results = recommender.getRecommendations(siteFeat, user, createSessionInput( session, user, maxItems, currentNode, listContent ) );
		
		// FIXME is this required to do?
		persistToSession(session, results);
		return results;
    }


    /**
     * Streamlined method for custom SessionInput object.
     * Note, this method does not persist result in session (as it is not needed in most cases).
     * 
     * @param user
     * @param siteFeat
     * @param si
     * 
     * @return Recommended items
     * 
     * @throws FDResourceException
     */
    public static Recommendations doRecommend( FDUserI user, EnumSiteFeature siteFeat, SessionInput si ) throws FDResourceException {
    	
		FDStoreRecommender recommender = FDStoreRecommender.getInstance();	    

		//listContent should not be larger than MAX_LIST_CONTENT_SIZE for scarab to work well (limit could be larger but wouldn't make more sense)
		Recommendations results = recommender.getRecommendations(siteFeat, user, si );
		
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

	public static List<ProductModel> getAggregatedSuperDepartmentFeaturedRecommenderProducts(SuperDepartmentModel superDeptModel, HttpSession session) throws FDResourceException {
		List<ProductModel> products = new ArrayList<ProductModel>();
		
		CategoryModel sourceCat = superDeptModel.getSdFeaturedRecommenderSourceCategory();
		
		if (sourceCat == null){
			EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(superDeptModel.getSdFeaturedRecommenderSiteFeature());
			
			if (siteFeat!=null) {
				for (DepartmentModel deptModel : superDeptModel.getDepartments()) {

					//TODO: hide department from this functionality
					FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
					Recommendations results = doRecommend(user, session, siteFeat, MAX_DEPT_FEATURED_RECOMMENDER_COUNT, new HashSet<ContentKey>(), deptModel);
					products.addAll(results.getAllProducts()); //TODO de we need to provide site feature id for CM?
					cleanUpProducts(products, superDeptModel.isSdFeaturedRecommenderRandomizeProducts(), MAX_DEPT_FEATURED_RECOMMENDER_COUNT);
				}
			}
			
		} else {
			products = sourceCat.getAllChildProductsAsList();
			cleanUpProducts(products, superDeptModel.isSdFeaturedRecommenderRandomizeProducts(), MAX_DEPT_FEATURED_RECOMMENDER_COUNT);
		}
		
		return products;
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

	public static List<ProductModel> getSuperDepartmentMerchantRecommenderProducts (SuperDepartmentModel superDeptModel){
		List<ProductModel> products = superDeptModel.getSdMerchantRecommenderProducts();
		cleanUpProducts(products, superDeptModel.isSdMerchantRecommenderRandomizeProducts(), MAX_DEPT_MERCHANT_RECOMMENDER_COUNT);
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
	
	// figure out customer belongs to Certona cohort
	public static boolean isEligibleForCertona(final FDUserI user, final EnumSiteFeature sf) {
		if (user == null || sf == null) {
			return false;
		}

		final String cohortName = user.getCohortName();

		if (CertonaTransitionUtil.TRANSITIONAL_PERIOD) {
			return CertonaTransitionUtil.isCertonaBasedCohort(cohortName, sf);
		}

		// transition period is over, only certona should serve
		return true;
	}

	public static Recommendations getBrowseCategoryListingPageRecommendations(FDUserI user, ContentNodeModel contentNode) throws FDResourceException{
		final EnumSiteFeature siteFeature = EnumSiteFeature.getEnum("BRWS_CAT_LST");

		if (isEligibleForCertona(user, siteFeature)) {
			// single-step recommender for Certona customers
			return doRecommend(user, null, siteFeature, MAX_CAT_SCARAB_RECOMMENDER_COUNT, null, null);	
		}

		// recommenders for Scarab customers
		Recommendations recommendations = doRecommend(user, null, EnumSiteFeature.getEnum("SCR_FEAT_ITEMS"), MAX_CAT_SCARAB_RECOMMENDER_COUNT, null, contentNode);	
		
		return recommendations;
	}

	public static Recommendations getBrowseProductListingPageRecommendations(FDUserI user, Set<ContentKey> keys) throws FDResourceException {
		final EnumSiteFeature siteFeature = EnumSiteFeature.getEnum("BRWS_PRD_LST");

		if (isEligibleForCertona(user, siteFeature)) {
			// single-step recommender for Certona customers
			return doRecommend(user, null,
				siteFeature,
				MAX_CAT_SCARAB_RECOMMENDER_COUNT, null, null);
		}


		//   Get YMAL recommendations triggered by the first product from the selection
		//   (Scarab 'Also Viewed' recommender backfilled with local SmartYMAL)
		ContentNodeModel currentNode = null;
		if (keys.size() > 0) {
			currentNode = ContentFactory.getInstance().getContentNodeByKey(keys.iterator().next());
		}

		SessionInput si = new SessionInput.Builder()
				.setUser(user)
				.setExcludeAlcoholicContent(false)
				.setMaxRecommendations(MAX_CAT_SCARAB_RECOMMENDER_COUNT)
				.setCurrentNode(currentNode)
				.setCartContents(keys)
				.setExcludeCartContent(true)
				.build();

		/* recommendations = doRecommend(user, null,
				EnumSiteFeature.getEnum("SRCH_RLTD"),
				MAX_CAT_SCARAB_RECOMMENDER_COUNT, null, currentNode); */
		Recommendations recommendations = doRecommend(user, EnumSiteFeature.getEnum("SRCH_RLTD"), si);

		return recommendations;
	}
	
	public static Recommendations getSearchPageRecommendations(FDUserI user, ProductData product) throws FDResourceException {

		Recommendations recommendations = null;
		if (user.getIdentity() != null){ //try personal if user is identified
			// Round #1 - Get personalized recommendations (Scarab 'Personalized Items')
			recommendations = doRecommend(user, null, EnumSiteFeature.getEnum("SRCH"), 16, null, null);
		}
		
		if ((recommendations == null || recommendations.getAllProducts().size() == 0) && product != null) {
			// Round #2 - Get YMAL recommendations triggered by the first product from the selection
			//   (Scarab 'Also Viewed' recommender backfilled with local SmartYMAL)

			SessionInput si = new SessionInput.Builder()
					.setUser(user)
					.setExcludeAlcoholicContent(false)
					.setMaxRecommendations(16)
					.setCurrentNode(ContentFactory.getInstance().getContentNode(product.getProductId()))
					.build();

			recommendations = doRecommend(user, EnumSiteFeature.getEnum("SRCH_RLTD"), si);
		}

		return recommendations;
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
	public static List<ProductData> getCrossSellProducts(ProductModel product, FDUserI user) {
		
		List<ProductData> result = new ArrayList<ProductData>();
		// pick list from CMS
		List<ProductModel> products = product.getCrossSellProducts();
		LOGGER.debug("  [XSELL] Found " + products.size() + " cross-sell products in CMS ..");
		
		final int MERCHANT_PROD_NUMBER = FDStoreProperties.getMaxXsellProds();
		
		
		cleanUpProducts(products, false, MAX_XSELL_PRODS > MERCHANT_PROD_NUMBER ? MERCHANT_PROD_NUMBER : MAX_XSELL_PRODS);
		LOGGER.debug("  .. " + products.size() + " are available");
		
		result.addAll(createData(products, MERCHANT_RECOMMENDATION_VARIANT, user));
		
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
					
					//transform into product data and set the source
					result.addAll(createData(scarabProds.subList(0, k), r.getVariant().getId(), user));
				}
				
			} catch (FDResourceException e) {
				LOGGER.error("Failed to invoke Scarab recommender", e);
			}
		}
		
		return result;
	}
	
	private static List<ProductData> createData(List<ProductModel> products, String variantId, FDUserI user){
		
		List<ProductData> data = new ArrayList<ProductData>();
		
		for(ProductModel p : products){
			try{
				ProductData productData = ProductDetailPopulator.createProductData( user, p );
				productData.setVariantId(variantId);
				data.add(productData);					
			} catch ( FDResourceException e ) {
				LOGGER.error( "Failed to get product info.", e );
				continue;
			} catch ( HttpErrorResponse e ) {
				LOGGER.error( "Failed to get product info.", e );
				continue;
			} catch ( FDSkuNotFoundException e ) {
				LOGGER.error( "Failed to get product info.", e );
				continue;
			}
		}
		
		return data;
	}


	public static void cleanUpProducts (List<ProductModel> products, boolean randomize, int maxCnt){
		if (products == null || products.size() == 0)
			return;

		if (randomize){
			Collections.shuffle(products);
		}

		int keptItemCount=0;
		Iterator<ProductModel> it = products.iterator();
		while (it.hasNext()) {
			ProductModel product = it.next();
		    if (keptItemCount >= maxCnt || !product.isFullyAvailable()) {
		        it.remove();
		    } else {
		    	keptItemCount++;
		    }
		}
	}
	

	public static List<ProductModel> getUnavailableReplacementProducts (ProductModel originalProduct, Set<ContentKey> excludedProductKeys){
		List<ProductModel> replacementProducts = new ArrayList<ProductModel>();
		if (originalProduct!=null && !originalProduct.isDisableAtpFailureRecommendation()) {

			//based on QuickShopHelper.populateReplacements()
			for (ContentNodeModel node : originalProduct.getRecommendedAlternatives()) {
				if (node instanceof ProductModel) {
					replacementProducts.add((ProductModel)node);				
				} else if (node instanceof SkuModel) {			
					replacementProducts.add(((SkuModel)node).getProductModel());
				}
			}
			removeProductsByKeys(replacementProducts, excludedProductKeys);
			cleanUpProducts(replacementProducts, false, MAX_UNAVAILABLE_REPLACEMENTS_COUNT);
			
			
			//append list with products in same category
			CategoryModel parentCategory = originalProduct.getPrimaryHome();
			if (replacementProducts.size() < MAX_UNAVAILABLE_REPLACEMENTS_COUNT){
				List<ProductModel> siblingProducts = parentCategory.getProducts();
				siblingProducts.removeAll(replacementProducts);
				removeProductsByKeys(siblingProducts, excludedProductKeys);
				
				cleanUpProducts(siblingProducts, true, MAX_UNAVAILABLE_REPLACEMENTS_COUNT-replacementProducts.size());
				replacementProducts.addAll(siblingProducts);
			}
			
			//append list with products under the category's parent category
			ContentNodeModel grandpaNode = parentCategory.getParentNode(); 
			if (replacementProducts.size() < MAX_UNAVAILABLE_REPLACEMENTS_COUNT && grandpaNode instanceof CategoryModel){
				List<ProductModel> productsUnderGrandpa = ((CategoryModel) grandpaNode).getAllChildProductsAsList();
				productsUnderGrandpa.removeAll(replacementProducts);
				removeProductsByKeys(productsUnderGrandpa, excludedProductKeys);
				
				cleanUpProducts(productsUnderGrandpa, true, MAX_UNAVAILABLE_REPLACEMENTS_COUNT-replacementProducts.size());
				replacementProducts.addAll(productsUnderGrandpa);
			}
		}
		return replacementProducts;
	}
	
	
	public static void removeProductsByKeys(List<ProductModel> products,  Set<ContentKey> excludedProducts){
	    for (Iterator<ProductModel> productIt = products.iterator(); productIt.hasNext();){
	        ProductModel product = productIt.next();
	    	if (product!=null && excludedProducts.contains(product.getContentKey())){
	        	productIt.remove();
	        }
	    }
	}
}
