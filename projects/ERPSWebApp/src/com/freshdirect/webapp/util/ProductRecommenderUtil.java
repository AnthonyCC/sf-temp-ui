package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.browse.sorter.ProductItemSorterFactory;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.ValueHolder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.fdstore.VariantSelector;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.storeapi.content.SortOptionModel;
import com.freshdirect.storeapi.content.SortStrategyType;
import com.freshdirect.storeapi.content.SuperDepartmentModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.filtering.ProductItemFilterUtil;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductRecommenderUtil {

    private static final Category LOGGER = LoggerFactory.getInstance(ProductRecommenderUtil.class);

	public static final int MAX_LIST_CONTENT_SIZE = 20;

	public static final int MAX_DEPT_FEATURED_RECOMMENDER_COUNT = 20;
	public static final int MAX_DEPT_MERCHANT_RECOMMENDER_COUNT = 20;
	public static final int MAX_CAT_MERCHANT_RECOMMENDER_COUNT = 10;
	public static final int MAX_CAT_SCARAB_RECOMMENDER_COUNT = 10;
	public static final int MAX_UNAVAILABLE_REPLACEMENTS_COUNT = 9;

	public static final int MAX_UPSELL_PRODS = 12;
	public static final int MAX_XSELL_PRODS = 12;

    private static final int CATEGORY_TOP_ITEM_CACHE_MAXIMAL_SIZE = FDStoreProperties.getCategoryTopItemCacheMaximalSize();
    private static final String MERCHANT_RECOMMENDATION_VARIANT = "merch";

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

    public static List<ProductModel> getAggregatedSuperDepartmentFeaturedRecommenderProducts(SuperDepartmentModel superDeptModel, FDUserI user, HttpSession session,
            ValueHolder<Variant> outVariant) throws FDResourceException {
		List<ProductModel> products = new ArrayList<ProductModel>();

		CategoryModel sourceCat = superDeptModel.getSdFeaturedRecommenderSourceCategory();

		if (sourceCat == null){
			EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(superDeptModel.getSdFeaturedRecommenderSiteFeature());

			if (siteFeat!=null) {
				for (DepartmentModel deptModel : superDeptModel.getDepartments()) {

					//TODO: hide department from this functionality
					Recommendations results = doRecommend(user, session, siteFeat, MAX_DEPT_FEATURED_RECOMMENDER_COUNT, new HashSet<ContentKey>(), deptModel);
					products.addAll(results.getAllProducts()); //TODO de we need to provide site feature id for CM?
					cleanUpProducts(products, superDeptModel.isSdFeaturedRecommenderRandomizeProducts(), MAX_DEPT_FEATURED_RECOMMENDER_COUNT);

					if (outVariant != null) {
						getUserVariant(user, siteFeat, outVariant);
					}
				}
			}

		} else {
            products = fetchProductsFromCategory(user, sourceCat, superDeptModel.isSdFeaturedRecommenderRandomizeProducts());
		}

		return products;
	}

	// FIXME
    public static List<ProductModel> getFeaturedRecommenderProducts(ProductContainer productContainer, FDUserI user, HttpSession session, ValueHolder<Variant> outVariant)
            throws FDResourceException {
		List<ProductModel> products = new ArrayList<ProductModel>();

		CategoryModel sourceCat = productContainer.getFeaturedRecommenderSourceCategory();

		if (sourceCat == null){
			EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(productContainer.getFeaturedRecommenderSiteFeature());

			if (siteFeat!=null) {
				Recommendations results = doRecommend(user, session, siteFeat, MAX_DEPT_FEATURED_RECOMMENDER_COUNT, new HashSet<ContentKey>(), productContainer);
				products = results.getAllProducts(); //TODO de we need to provide site feature id for CM?

				if (outVariant != null) {
					getUserVariant(user, siteFeat, outVariant);
				}
			}

		} else {
            products = fetchProductsFromCategory(user, sourceCat, productContainer.isFeaturedRecommenderRandomizeProducts());
		}

		return products;
	}

    private static List<ProductModel> fetchProductsFromCategory(FDUserI user, CategoryModel category, boolean isProductRandomize) {
        List<ProductModel> products = category.getAllChildProductsAsList();
        cleanUpProducts(products, isProductRandomize, MAX_DEPT_FEATURED_RECOMMENDER_COUNT);
        return sortProducts(user, products, getSortStrategy(category), false);
    }

    private static SortStrategyType getSortStrategy(CategoryModel category) {
        SortStrategyType strategy = null;
        List<SortOptionModel> sortOptions = category.getSortOptions();
        if (!sortOptions.isEmpty()) {
            strategy = sortOptions.get(0).getSortStrategyType();
        } else {
            // strategy = SortStrategyType.NAME;
        }
        return strategy;
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

	public static Recommendations getBrowseCategoryListingPageRecommendations(FDUserI user, ContentNodeModel contentNode) throws FDResourceException{
		return doRecommend(user, null,
			EnumSiteFeature.getEnum("BRWS_CAT_LST"),
			MAX_CAT_SCARAB_RECOMMENDER_COUNT, null, null);
	}

	public static Recommendations getBrowseProductListingPageRecommendations(FDUserI user, Set<ContentKey> keys) throws FDResourceException {
		return doRecommend(user, null,
			EnumSiteFeature.getEnum("BRWS_PRD_LST"),
			MAX_CAT_SCARAB_RECOMMENDER_COUNT, null, null);
	}

	public static Recommendations getSearchPageRecommendations(FDUserI user, String productId) throws FDResourceException {

		Recommendations recommendations = null;
		if (user.getIdentity() != null){ //try personal if user is identified
			// Round #1 - Get personalized recommendations (Scarab 'Personalized Items')
			recommendations = doRecommend(user, null, EnumSiteFeature.getEnum("SRCH"), 16, null, null);
		}

		if ((recommendations == null || recommendations.getAllProducts().size() == 0) && productId != null && !productId.isEmpty()) {
			// Round #2 - Get YMAL recommendations triggered by the first product from the selection
			//   (Scarab 'Also Viewed' recommender backfilled with local SmartYMAL)

			SessionInput si = new SessionInput.Builder()
					.setUser(user)
					.setExcludeAlcoholicContent(false)
					.setMaxRecommendations(16)
					.setCurrentNode(ContentFactory.getInstance().getContentNode(productId))
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
						user, null, EnumSiteFeature.YMAL,
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

    public static List<ProductData> createProductData(FDUserI user, List<ProductModel> products, String variantId) {
        List<ProductData> productDatas = new ArrayList<ProductData>();

        for (ProductModel product : products) {
            try {
                ProductData productData = ProductDetailPopulator.createProductData(user, product);
                productData = ProductDetailPopulator.populateBrowseRecommendation(user, productData, product);
                productData.setVariantId(variantId);
                productData.setProductPageUrl(FDURLUtil.getNewProductURI(product, variantId));
                productDatas.add(productData);
            } catch (FDResourceException e) {
                LOGGER.error("failed to create ProductData", e);
            } catch (FDSkuNotFoundException e) {
                LOGGER.error("failed to create ProductData", e);
            } catch (HttpErrorResponse e) {
                LOGGER.error("failed to create ProductData", e);
            }
        }
        return productDatas;
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

	public static void initTopItemCategoriesCache() {
		for (DepartmentModel departmentModel : ContentFactory.getInstance().getStore().getDepartments()) {
			for (CategoryModel category : departmentModel.getCategories()) {
                List<ProductModel> products = ProductRecommenderUtil.sortProducts(null, category.getProducts(), SortStrategyType.POPULARITY, true,
                        CATEGORY_TOP_ITEM_CACHE_MAXIMAL_SIZE);
                CmsServiceLocator.ehCacheUtil().putListToCache(CmsCaches.BR_CATEGORY_TOP_ITEM_CACHE.cacheName, category.getContentKey().getId(), products);
			}
		}
	}

	public static List<ProductModel> getCategoryTopItem(String key) {
		List<ProductModel> availableCategoryTopItems = new ArrayList<ProductModel>();

        List<ProductModel> categoryTopItemCaches = CmsServiceLocator.ehCacheUtil().getListFromCache(CmsCaches.BR_CATEGORY_TOP_ITEM_CACHE.cacheName, key);

		if (categoryTopItemCaches != null) {
			for (ProductModel productModel : categoryTopItemCaches) {
				if (!productModel.isUnavailable()) {
					availableCategoryTopItems.add(productModel);
				}
			}
		}

		int availableCategoryTopItemsSize = availableCategoryTopItems.size();
		int categoryTopItemCacheSize = FDStoreProperties.getCategoryTopItemCacheSize();
		int availableCategoryCacheCurrentSize = availableCategoryTopItemsSize >= categoryTopItemCacheSize ? categoryTopItemCacheSize : availableCategoryTopItemsSize;

		return availableCategoryTopItems.subList(0, availableCategoryCacheCurrentSize);
	}

    private static Variant getUserVariant(FDUserI user, EnumSiteFeature siteFeature, ValueHolder<Variant> out) {
        final VariantSelector selector = VariantSelectorFactory.getSelector(siteFeature);

        final Variant variant = selector.select(user);

        if (out != null) {
                out.setValue(variant);
        }

        return variant;
    }

    public static List<ProductModel> sortProducts(FDUserI user, List<ProductModel> products, SortStrategyType sortStrategy, boolean reverseOrder) {
        return sortProducts(user, products, sortStrategy, reverseOrder, products.size());
    }

    public static List<ProductModel> sortProducts(FDUserI user, List<ProductModel> products, SortStrategyType sortStrategy, boolean reverseOrder, int maxProductSize) {
        Comparator<FilteringProductItem> comparator = ProductItemSorterFactory.createComparator(sortStrategy, user, reverseOrder);

        List<FilteringProductItem> filteringProducts = ProductItemFilterUtil.createFilteringProductItems(products);

        Collections.sort(filteringProducts, comparator);

        List<ProductModel> sortedProducts = new ArrayList<ProductModel>();
        for (int i = 0; i < Math.min(filteringProducts.size(), maxProductSize); i++) {
            sortedProducts.add(filteringProducts.get(i).getProductModel());
        }
        return sortedProducts;
    }
    
    //Method added as part of FDLabs of Aggregated Customer and Global Favorites page
    public static List<List<ProductModel>> getYouLoveWeLoveProducts(FDUserI user, double ratingBaseLine
    		, double dealsBaseLine, double popularityBaseLine
    		, boolean considerNew , boolean considerBackInStock, boolean sortProducts, int maxNoOfProducts) throws FDResourceException {

    	List<List<ProductModel>> result = new ArrayList<List<ProductModel>>();
    	ProductModel productModel = null;
    	String customerId = null;
    	
    	if(user.getIdentity() != null) {
			FDCustomerModel fdCustomerModel = user.getFDCustomer();
			if(fdCustomerModel != null) {
				customerId = fdCustomerModel.getErpCustomerPK();
			}
		}
    	
    	List<ProductModel> youLove = new ArrayList<ProductModel>();
    	if(customerId != null) {
	    	Map<ContentKey,Float> customerPersonalizedProductScores = ScoreProvider.getInstance().getUserProductScores(customerId);	//2149848491
	    	for (Map.Entry<ContentKey,Float> entry : customerPersonalizedProductScores.entrySet()) {
	    		productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(entry.getKey());
	    		if(isYouLoveWeLoveProduct(productModel, ratingBaseLine, dealsBaseLine, considerNew, considerBackInStock)) {
	    			youLove.add(productModel);
	    		}
	    	}	    	
    	}
    	
    	if(youLove.size() > 0) {
    		youLove = ProductRecommenderUtil.sortProducts(user, youLove, (sortProducts ? SortStrategyType.CUSTOMER_POPULARITY : SortStrategyType.SALE), false, maxNoOfProducts);
    	}
    	result.add(youLove);

    	List<ProductModel> weLove = new ArrayList<ProductModel>();
    	Map<ContentKey, double[]> globalProductScores = ScoreProvider.getInstance().getGlobalScores();
    	int indexOfPopularity = ScoreProvider.getInstance().getGlobalIndexes().get("Popularity");
    	for (Map.Entry<ContentKey, double[]> entry : globalProductScores.entrySet()) {			
    		double[] value = entry.getValue();

    		if( value[indexOfPopularity] >= popularityBaseLine) {
    			productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(entry.getKey());
    			if(isYouLoveWeLoveProduct(productModel, ratingBaseLine, dealsBaseLine, considerNew, considerBackInStock)) {
    				weLove.add(productModel);
    			}
    		}
    	}
    	
    	if(weLove.size() > 0) {
    		weLove = ProductRecommenderUtil.sortProducts(user, weLove, (sortProducts ? SortStrategyType.POPULARITY : SortStrategyType.SALE), false, maxNoOfProducts);
    	}
    	result.add(weLove);

    	return result;
    }

    private static boolean isYouLoveWeLoveProduct(ProductModel productModel, double ratingBaseLine, double dealsBaseLine
    		, boolean considerNew , boolean considerBackInStock) {
    	boolean result = false;
    	try {
    		result = (!productModel.isUnavailable() && 
    				((considerNew && productModel.isNew()) || productModel.getPriceCalculator().getHighestDealPercentage() > dealsBaseLine 
    						|| (considerBackInStock && productModel.isBackInStock()) 
    						|| (productModel.getProductRatingEnum() != null && (productModel.getProductRatingEnum().getValue())/2  >= ratingBaseLine)));
    				
    	} catch (Exception e) {
    		//System.out.println("isYouLoveWeLoveProduct..failed...."+productModel.getContentKey().getId());			
    	}



    	return result;
    }

}
