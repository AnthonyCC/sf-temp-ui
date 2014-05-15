package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.smartstore.CmsRecommenderService;
import com.freshdirect.cms.util.ProductPromotionUtil;
import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.ejb.FDProductPromotionManager;
import com.freshdirect.erp.ejb.ProductPromotionInfoManager;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDProductPromotionPreviewInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ProductModelPromotionAdapter;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.fdstore.content.grabber.GrabberServiceI;
import com.freshdirect.fdstore.ecoupon.FDCouponFactory;
import com.freshdirect.fdstore.ecoupon.model.FDCouponUPCInfo;
import com.freshdirect.fdstore.productpromotion.FDProductAssortmentPromotionFactory;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CategoryModel extends ProductContainer {
	
	private static final long serialVersionUID = 5787890004203680537L;

	private static final Logger LOGGER = LoggerFactory.getInstance(CategoryModel.class);

	private static final Executor threadPool = new ThreadPoolExecutor(5, 5, 60,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
			new ThreadPoolExecutor.DiscardPolicy());
	
	private static final long FIVE_MINUTES = 5l * 60l * 1000l;
	
	private final class RecommendedProductsRef extends BalkingExpiringReference<List<ProductModel>> {
	    String zoneId;

        private RecommendedProductsRef(Executor executor, String zoneId) {
            super(FIVE_MINUTES, executor);
            this.zoneId = zoneId;
            // synchronous load
            try {
                set(load());
            } catch (RuntimeException e) {
                LOGGER.error("failed to initialize smart products for category " + CategoryModel.this.getContentName() + " and zone " + zoneId
                        + " synchronously");
            }
        }

		@Override
		protected List<ProductModel> load() {
			CmsRecommenderService recommenderService = getRecommenderService();
			ContentKey categoryId = CategoryModel.this.getContentKey();
			final Recommender recommender = CategoryModel.this.getRecommender();
            ContentKey recommenderId = recommender != null ? recommender.getContentKey() : null;
			LOGGER.debug("loader started for category " + categoryId + ", zone " + zoneId + ", recommender: " + recommenderId);
			
			if ( recommenderService == null || recommenderId == null ) {
				LOGGER.warn("recommender service ("
						+ recommenderId
						+ ") not found, returning previous recommendations for category "
						+ categoryId);
				// fallback
				throw new RuntimeException();
			}
			
			List<String> prodIds = recommenderService.recommendNodes(recommenderId, categoryId, zoneId);
			List<ProductModel> products = new ArrayList<ProductModel>( prodIds.size() );
			for ( String productId : prodIds ) {
				ContentNodeModel product = ContentFactory.getInstance().getContentNode( productId );
				if ( product instanceof ProductModel && !products.contains( product ) )
					products.add( (ProductModel)product );
			}
			LOGGER.debug( "found " + products.size() + " products for category " + categoryId + ", zone " + zoneId);
			return products;
		}
		
		private CmsRecommenderService getRecommenderService() {
			try {
				return (CmsRecommenderService) FDRegistry.getInstance()
						.getService("com.freshdirect.fdstore.CmsRecommenderService", CmsRecommenderService.class);
			} catch (RuntimeException e) {
				LOGGER.error("failed to initialize CMS recommender service", e);
			}
			return null;
		}
		
		@SuppressWarnings( "unused" )
		public String getZoneId() {
			return zoneId;
		}
	}

	private CategoryAlias categoryAlias;

	/**
	 * map of zoneId --> recommended products reference
	 */
	private final class ProductPromotionDataRef extends BalkingExpiringReference<ProductPromotionData> {

		private String productPromotitonType; 
		private String zoneId;
		
		public ProductPromotionDataRef(Executor executor, String zoneId, String productPromotitonType) {
			super(FIVE_MINUTES, executor);
			this.zoneId = zoneId;
			this.productPromotitonType = productPromotitonType;
			
			// synchronous load
            try {
                set(load());
            } catch (RuntimeException e) {
                LOGGER.error("failed to initialize promo products for category " + CategoryModel.this.getContentName() + " synchronously");
            }
		}	
		

		@Override
		protected ProductPromotionData load() {
			ProductPromotionData productPromotionData = new ProductPromotionData();
			
			loadProductPromotionData(productPromotionData);
			
			return productPromotionData;
		}

		public void loadProductPromotionData(
				ProductPromotionData productPromotionData) {
			try {
				Map<String,List<FDProductPromotionInfo>> productPromoInfoMap ;
				String ppType =productPromotitonType;
				if(null !=ppType){
					if("PRESIDENTS_PICKS_PREVIEW".equals(ppType)||"PRESIDENTS_PICKS".equals(ppType)){
						ppType = "PRESIDENTS_PICKS";
					}
					/*if("PRODUCTS_ASSORTMENTS_PREVIEW".equals(ppType)||"PRODUCTS_ASSORTMENTS".equals(ppType)){
						ppType = "PRODUCTS_ASSORTMENTS";
					}*/
				}
				if(!"E_COUPONS".equals(ppType)){
					synchronized (FDProductPromotionManager.getInstance()) {					
						productPromoInfoMap = FDProductPromotionManager.getProductPromotion(ppType);				
					}
					populateProductPromotionData(productPromotionData,productPromoInfoMap,zoneId,false);
				}else{										
					Map<String, Set<FDCouponUPCInfo>> fdUpcCouponMap =FDCouponFactory.getInstance().getFdUpcCouponMap();
					populateProductPromotionData(productPromotionData,fdUpcCouponMap);					
				}

			} catch (FDResourceException e) {
				LOGGER.error("failed to load promo products for category " + CategoryModel.this.getContentName(), e);
				throw new RuntimeException(e);
			}
		}
		
	}
	
	/**
	 * Method to fetch products by coupon's upcs.
	 * @param productPromotionData
	 * @param upcCouponMap
	 * @return
	 * @throws FDResourceException
	 */
	private ProductPromotionData populateProductPromotionData(
			ProductPromotionData productPromotionData,
			Map<String, Set<FDCouponUPCInfo>> upcCouponMap)
			throws FDResourceException {
		List<ProductModel> products = new ArrayList<ProductModel>();
		if(null !=upcCouponMap && !upcCouponMap.isEmpty()){
			Set<String> skuCodes = new HashSet<String>();
			for (Iterator<String> iterator = upcCouponMap.keySet().iterator(); iterator.hasNext();) {
				String couponUPC = (String) iterator.next();				
				FDProductInfo cachedProductInfo = FDCachedFactory.getProductInfoByUpc(couponUPC);
				if(cachedProductInfo != null && cachedProductInfo.getSkuCode() != null && cachedProductInfo.getSkuCode().length() > 0) {
//					LOGGER.info("Product Found in UPCCache:"+couponUPC+"->"+cachedProductInfo.getSkuCode());
					skuCodes.add(cachedProductInfo.getSkuCode());
				} 
			}
			if(skuCodes.isEmpty()){
				LOGGER.info("No valid coupons found in fdUpcCouponMap cache of "+upcCouponMap.size());
			}else{
				for (String skuCode : skuCodes) {
					SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(FDContentTypes.SKU, skuCode);
					if (sku == null)
						continue;
					ProductModel product = sku.getProductModel();
					if (product != null)
						products.add(product);
				}
				LOGGER.info("Found "+products.size()+" products with coupons");
			}
				
			
		}else{
			LOGGER.info("No coupons found in fdUpcCouponMap cache, for the promotion type:"+this.getProductPromotionType());
		}
		productPromotionData.setProductModels(products);
		return productPromotionData;
	}
	
	public ProductPromotionData populateProductPromotionData(
			ProductPromotionData productPromotionData,
			Map<String, List<FDProductPromotionInfo>> productPromoInfoMap,String zoneId,boolean isPreview)
			throws FDResourceException {
		Map<String, ProductModel> skuProductMap = new HashMap<String, ProductModel>();
		List<ProductModel> productModels = new ArrayList<ProductModel>();
		if(null !=productPromoInfoMap){
			List<FDProductPromotionInfo> fDProductPromotionSkus = productPromoInfoMap.get(zoneId);				
			if(null ==fDProductPromotionSkus){
				fDProductPromotionSkus = productPromoInfoMap.get(ProductPromotionData.DEFAULT_ZONE);
				productPromoInfoMap.put(zoneId,fDProductPromotionSkus);
			}
			if(null != fDProductPromotionSkus)
			for (FDProductPromotionInfo fDProductPromotionSku : fDProductPromotionSkus){
				String sku = fDProductPromotionSku.getSkuCode();
				if(!isPreview){
					FDCachedFactory.refreshProductPromotionSku(sku);
				}
				ProductModel productModel = null;
				ProductModel prodModel = null;
				try {
					SkuModel skuModel = ((SkuModel) ContentFactory.getInstance().getContentNodeByKey(new ContentKey(FDContentTypes.SKU, sku)));
					if(null !=skuModel){
						prodModel = skuModel.getProductModel();
						if(isPreview){
							ProductModel prdModelClone = (ProductModel)prodModel.clone();
							productModel = new ProductModelPromotionAdapter(prdModelClone,fDProductPromotionSku.isFeatured(),fDProductPromotionSku.getFeaturedHeader(),sku,fDProductPromotionSku.getErpCategory(),fDProductPromotionSku.getErpCatPosition());
							((ProductModelPromotionAdapter)productModel).setPreview(isPreview);
							((ProductModelPromotionAdapter)productModel).setFdProdInfo(((FDProductPromotionPreviewInfo)fDProductPromotionSku).getFdProductInfo());
							((ProductModelPromotionAdapter)productModel).setFdProduct(((FDProductPromotionPreviewInfo)fDProductPromotionSku).getFdProduct());
						}else{
							productModel = new ProductModelPromotionAdapter(prodModel,fDProductPromotionSku.isFeatured(),fDProductPromotionSku.getFeaturedHeader(),sku,fDProductPromotionSku.getErpCategory(),fDProductPromotionSku.getErpCatPosition());
						}
					}
				} catch (Exception e){
					LOGGER.warn("Failed to get product for sku: " + sku +", promo products for category " + CategoryModel.this.getContentName(), e);				}
				
				if (productModel != null && !productModel.isOrphan() && !productModel.isHidden() && !productModel.isInvisible()){					
					skuProductMap.put(sku, productModel);
					if (!productModels.contains(productModel)) {
						productModels.add(productModel);
					}
				}
			}
//			}
		}
		productPromotionData.setSkuProductMap(skuProductMap);
		productPromotionData.setProductModels(productModels);
//		productPromotionData.setZoneProductModelsMap(ProductPromotionUtil.populatePromotionPageProducts(productPromoInfoMap,skuProductMap));
		
		return productPromotionData;
	}
	
	
	private final class ProductAssortmentPromotionDataRef extends BalkingExpiringReference<ProductPromotionData> {

		private String productPromotitonType; 
		private String zoneId;
		private String promotionId;
		
		public ProductAssortmentPromotionDataRef(Executor executor, String zoneId, String promotionId, String productPromotitonType) {
			super(FIVE_MINUTES, executor);
			this.zoneId = zoneId;
			this.productPromotitonType = productPromotitonType;
			this.promotionId = promotionId;
			
			// synchronous load
            try {
                set(load());
            } catch (RuntimeException e) {
                LOGGER.error("failed to initialize promo products for category " + CategoryModel.this.getContentName() + " synchronously");
            }
		}	
		

		@Override
		protected ProductPromotionData load() {
			ProductPromotionData productPromotionData = new ProductPromotionData();
			productPromotionData.setProductPromotitonType(productPromotitonType);
			productPromotionData.setPromotionId(promotionId);
			
			loadProductPromotionData(productPromotionData);
			
			return productPromotionData;
		}

		public void loadProductPromotionData(
				ProductPromotionData productPromotionData) {
			try {
				Map<String,List<FDProductPromotionInfo>> productPromoInfoMap ;
				String ppType =productPromotionData.getProductPromotitonType();
				String promotionId =productPromotionData.getPromotionId();
				if(null !=ppType){					
					if("PRODUCTS_ASSORTMENTS_PREVIEW".equals(ppType)||"PRODUCTS_ASSORTMENTS".equals(ppType)){
						ppType = "PRODUCTS_ASSORTMENTS";
					}
				}
				synchronized (FDProductAssortmentPromotionFactory.getInstance()) {					
						productPromoInfoMap = FDProductAssortmentPromotionFactory.getInstance().getProductPromotion(ppType,promotionId);				
				}
				populateProductPromotionData(productPromotionData,productPromoInfoMap,zoneId,false);
				

			} catch (FDResourceException e) {
				LOGGER.error("failed to load promo products for category " + CategoryModel.this.getContentName(), e);
				throw new RuntimeException(e);
			}
		}
		
	}
	
	private Map<String, RecommendedProductsRef> recommendedProductsRefMap = new HashMap<String, RecommendedProductsRef>();
	
	private Map<String, ProductPromotionDataRef> productPromotionDataRefMap = new HashMap<String, ProductPromotionDataRef>();
	
	private Map<String, ProductAssortmentPromotionDataRef> productAssortmentPromotionDataRefMap = new HashMap<String, ProductAssortmentPromotionDataRef>();
	
	private String promotionPageType;
	
	private Object recommendedProductsSync = new Object();
	
	private int smartCategoryVersion;
	
	private static int globalSmartCategoryVersion = 1;

	private List<CategoryModel> subcategoriesModels = new ArrayList<CategoryModel>();

	private List<ProductModel> productModels = new ArrayList<ProductModel>();

	private List<ProductModel> featuredProductModels = new ArrayList<ProductModel>();
	
	private final List<BrandModel> featuredBrandModels = new ArrayList<BrandModel>();
	
	private List<ContentNodeModel> featuredNewProdBrands = new ArrayList<ContentNodeModel>();

	/**
	 * List of ProductModels and CategoryModels.
	 */
	private List<ContentNodeModel> candidateList = new ArrayList<ContentNodeModel>();
	
	// New Wine Store 

	private List wineSortCriteriaList = new ArrayList();
	
	private List wineFilterCriteriaList = new ArrayList();
	
	private List wineSideNavSectionsList = new ArrayList();
	
	private List wineSideNavFullsList = new ArrayList();
	
	private List<ProductModel> howToCookItProducts = new ArrayList<ProductModel> ();
	
    final private List<CategoryModel> virtualGroups = new ArrayList<CategoryModel>();
	
	private List<ProductGrabberModel> productGrabbers = new ArrayList<ProductGrabberModel>();
    
    private List<ProductModel> catMerchantRecommenderProducts = new ArrayList<ProductModel>();

	public CategoryModel(com.freshdirect.cms.ContentKey cKey) {
		super(cKey);
		categoryAlias = null;
		this.smartCategoryVersion = globalSmartCategoryVersion;
	}

	public DepartmentModel getDepartment() {
		ContentNodeModel start = this;

		while (!(start instanceof DepartmentModel) && (start != null)) {
			start = start.getParentNode();
		}
		return (DepartmentModel) start;
	}
	
	public CategoryModel getTopCategory() {
	    ContentNodeModel parentNode2 = getParentNode();
	    if (parentNode2 instanceof CategoryModel) {
	        return ((CategoryModel)parentNode2).getTopCategory();
	    }
	    return this;
	}

	public boolean isUseAlternateImages() {
		return getAttribute( "USE_ALTERNATE_IMAGES", false );
	}

	public boolean getSideNavBold() {
		return getAttribute("SIDENAV_BOLD", false);
	}

	public boolean getSideNavLink() {
		return getAttribute("SIDENAV_LINK", false);
	}

	public int getSideNavPriority() {
		return getAttribute("SIDENAV_PRIORITY", 1);
	}

	public int getColumnSpan(int defaultValue) {
		return getAttribute("COLUMN_SPAN", defaultValue);
	}

	public boolean getFakeAllFolders() {
		return getAttribute("FAKE_ALL_FOLDER", false);
	}
	
	/**
	 * 
	 * @return CAT_LABEL
	 */
	public Image getCategoryLabel() {
	    return FDAttributeFactory.constructImage(this, "CAT_LABEL");
	}
	
    /**
     * Return the CAT_LABEL image, if the attribute is null, then it returns an
     * empty image object.
     * 
     * @return
     */
    public final Image getCategoryLabelNotNull() {
        Image img = getCategoryLabel();
        if (img != null) {
            return img;
        } else {
            return new Image();
        }
    }	
	
	
	public Image getCategoryNavBar() {
	    return FDAttributeFactory.constructImage(this, "CATEGORY_NAVBAR");
	}
	
	/**
	 * Use getTopMedia
	 * @return
	 */
	@Deprecated
	public List<Html> getCategoryTopMedia() {
		return getTopMedia();
	}

	
	public EnumShowChildrenType getShowChildren() {
		return EnumShowChildrenType.getShowChildrenType(getAttribute("SHOWCHILDREN", EnumShowChildrenType.ALWAYS_FOLDERS.getId()));
	}

	public boolean isShowSelf() {
		return getAttribute("SHOWSELF", true);
	}


	/**
	 * @deprecated use isShowSelf() instead
	 * @return
	 */
	@Deprecated
	public boolean getShowSelf() {
		return isShowSelf();
	}

	public boolean isSecondaryCategory() {
		return getAttribute("SECONDARY_CATEGORY", false);
	}

	public boolean isFeatured() {
		return getAttribute("FEATURED", false);
	}
	
	
	public Html getSeparatorMedia() {
	    return FDAttributeFactory.constructHtml(this, "SEPARATOR_MEDIA");
	}

	public List<CategoryModel> getSubcategories() {
		ContentNodeModelUtil.refreshModels(this, "subcategories", subcategoriesModels, true);
		return new ArrayList<CategoryModel>(subcategoriesModels);
	}
	
    public List<ContentNodeModel> getCandidateList() {
        ContentNodeModelUtil.refreshModels(this, "CANDIDATE_LIST", candidateList, false);
        return new ArrayList<ContentNodeModel>(candidateList);
    }
    
    public int getManualSelectionSlots() {
        return getAttribute("MANUAL_SELECTION_SLOTS", 0);
    }

	/* This does not traverse the alias list */
	public List<ProductModel> getPrivateProducts() {
		ContentNodeModelUtil.refreshModels(this, "products", productModels, true);
		return new ArrayList<ProductModel>(productModels);
	}
	
	public List<ProductModel> getHowToCookItProducts() {
	    ContentNodeModelUtil.refreshModels(this, "HOW_TO_COOK_IT_PRODUCTS", this.howToCookItProducts, false);
	    return new ArrayList<ProductModel> (howToCookItProducts);
	}
	
	/* [APPREQ-160] SmartStore, Category Level Aggregation */
	public boolean isDYFAggregated() {
		return getAttribute("SS_LEVEL_AGGREGATION", false);
	}

	public boolean isHavingBeer() {
		return getAttribute("CONTAINS_BEER", false);
	}

	/* [NEW WINE STORE CHANGES] */  
	
	
	
	public final Image getCategoryDetailImage() {
	    return FDAttributeFactory.constructImage(this, "CATEGORY_DETAIL_IMAGE");
	}
	 	
	public List getWineSortCriteria() {
		ContentNodeModelUtil.refreshModels(this, "WINE_SORTING", wineSortCriteriaList, false,true);
		return new ArrayList(wineSortCriteriaList);		
	}
	
	public List getWineFilterCriteria() {
		ContentNodeModelUtil.refreshModels(this, "WINE_FILTER", wineFilterCriteriaList, false,true);
		return new ArrayList(wineFilterCriteriaList);		
	}

	public DomainValue getWineFilterValue() {
        return FDAttributeFactory.lookup(this, "WINE_FILTER_VALUE", (DomainValue) null);
	}
	
	public List<DomainValue> getWineSideNavSections() {
		ContentNodeModelUtil.refreshModels(this, "SIDE_NAV_SECTIONS", wineSideNavSectionsList, false);
		return new ArrayList(wineSideNavSectionsList);		
	}

	public List<Domain> getWineSideNavFullList() {
		ContentNodeModelUtil.refreshModels(this, "SIDE_NAV_FULL_LIST", wineSideNavFullsList, false);
		return new ArrayList(wineSideNavFullsList);		
	}
	
	
	public String getContentTemplatePath(){		
		return this.getAttribute("TEMPLATE_PATH", null);		
	}
	
	public Recommender getRecommender() {
        return FDAttributeFactory.lookup(this, "recommender", (Recommender) null);
	}
	
	/** @return List of {@link CategoryModel} */
	public List<CategoryModel> getVirtualGroupRefs() {
        ContentNodeModelUtil.refreshModels(this, "VIRTUAL_GROUP", virtualGroups, false);
        return new ArrayList<CategoryModel>(virtualGroups);
	}

	private ContentKey recommenderKey;

    public List<ProductModel> getProducts() {
    	
    	List<ProductModel> prodList = new ArrayList<ProductModel>();
    	String zoneId = ContentFactory.getInstance().getCurrentPricingContext().getZoneId();
    	String currentProductPromotionType = getProductPromotionType();
    	if(!"E_COUPONS".equalsIgnoreCase(currentProductPromotionType) && (currentProductPromotionType == null || !ContentFactory.getInstance().isEligibleForDDPP())){
    		prodList =getStaticProducts();
	
	        Recommender recommender = getRecommender();
	        if ( recommender == null ) {
	        	recommenderKey = null;
	        } 
	        
	        if (recommender != null) {	        	
	            boolean recommenderChanged = !recommender.getContentKey().equals( recommenderKey ); 
	        	recommenderKey = recommender.getContentKey();  	
	        	
	        	if ( recommenderChanged ) {
	        		LOGGER.warn( this.getContentKey().getEncoded() + ": recommender changed!" );
	        	}
	        	
	            LOGGER.info("Category[id=\"" + this.getContentKey().getId() + "\"].getSmartProducts(\"" + zoneId + "\")");
	            synchronized (CategoryModel.class) {
	                if (globalSmartCategoryVersion > smartCategoryVersion) {
	                    LOGGER.info("forced smart category recalculation : " + smartCategoryVersion + " -> " + globalSmartCategoryVersion + " for category : "
	                            + this.getContentKey().getId());
	                    smartCategoryVersion = globalSmartCategoryVersion;
	                    recommendedProductsRefMap.clear();
	                }
	            }
	            
	            synchronized (recommendedProductsSync) {
	                if ( recommenderChanged || recommendedProductsRefMap.get(zoneId) == null )
	                    recommendedProductsRefMap.put(zoneId, new RecommendedProductsRef(threadPool, zoneId));
	            }
	
	            try {
	//                List<ProductModel> recProds = recommendedProductsRefMap.get(zoneId).get();
	                addDynamicProducts(recommendedProductsRefMap.get(zoneId).get(), prodList,true);
	            } catch (Exception e) {
	                LOGGER.warn("exception during smart category recommendation", e);
	            }
	        }
    	}else{
//	        String currentProductPromotionType = getProductPromotionType();
	        prodList = getPromotionPageProducts(prodList, zoneId,currentProductPromotionType);
    	}


    	// == [APPDEV-2910] add products picked by new product grabbers ==
		{
			if (getProductGrabbers() != null) { //refreshes this.productGrabbers
				GrabberServiceI grabber = ContentFactory.getInstance()
						.getProductGrabberService();
				if (grabber != null) {
					Collection<ProductModel> grabbedProducts;
					for (ProductGrabberModel productGrabber : productGrabbers) {
						grabbedProducts = grabber.getProducts(productGrabber);
						addDynamicProducts(grabbedProducts, prodList, false);
					}
				}
			}
		}

    	
    	
        return prodList;
    }

	private List<ProductModel> getPromotionPageProducts(
			List<ProductModel> prodList, String zoneId,
			String currentProductPromotionType) {
		if (currentProductPromotionType != null) {
			loadProductPromotion(zoneId, currentProductPromotionType);
			try {
				if(null !=productPromotionDataRefMap.get(zoneId).get()) {
					prodList = new ArrayList<ProductModel>();
					if("E_COUPONS".equals(currentProductPromotionType)){
						addDynamicProducts(productPromotionDataRefMap.get(zoneId).get().getProductModels(), prodList,false);
					}else{
						addDynamicProductsForPromotion(productPromotionDataRefMap.get(zoneId).get().getProductModels(), prodList);
					}
				}
		    } catch (Exception e) {
		        LOGGER.warn("exception during promo category product assignment", e);
		    }
		}
		return prodList;
	}

    private void addDynamicProducts(Collection<ProductModel> srcProducts, Collection<ProductModel> destProducts,boolean overrideParentNode){
    	if (srcProducts != null) {
            for ( ProductModel prod : srcProducts ) {
            	ProductModel newProd = (ProductModel)prod.clone();
            	if(overrideParentNode){
            		( (ContentNodeModelImpl)newProd ).setParentNode(this);
            	}
                if (!destProducts.contains(newProd)) {
                    ( (ContentNodeModelImpl)newProd ).setPriority(destProducts.size());
                    destProducts.add(newProd);
                }
            }
        }
    }
    
    private void addDynamicProductsForPromotion(Collection<ProductModel> srcProducts, Collection<ProductModel> destProducts, boolean overrideParentNode){
    	if (srcProducts != null) {
            for ( ProductModel prod : srcProducts ) {
            	ProductModel newProdModel = (ProductModel)prod.clone();
            	ProductModel newProd = ((ProductModelPromotionAdapter)newProdModel).getProductModel();
            	if(overrideParentNode){
            		( (ContentNodeModelImpl)newProd ).setParentNode(this);
            	}
                if (!destProducts.contains(newProdModel)) {
                    ( (ContentNodeModelImpl)newProd ).setPriority(destProducts.size());
                    destProducts.add(newProdModel);
                }
            }
        }
    }
    
    private void addDynamicProductsForPromotion(Collection<ProductModel> srcProducts, Collection<ProductModel> destProducts){
    	addDynamicProductsForPromotion(srcProducts,destProducts, true);
    }
    /**
     * @return all, non smart products.
     */
    public List<ProductModel> getStaticProducts() {
        List<ProductModel> prodList = getPrivateProducts();

        List<CategoryModel> l = getVirtualGroupRefs();
        if ( l != null ) {
            this.categoryAlias = new CategoryAlias(l, getFilterList());
        }

        if (categoryAlias != null) {
            try {
                Collection<ProductModel> aliasProds = this.categoryAlias.processCategoryAlias();
                if (aliasProds != null) { // if we had an error..then we get null, cause empty list is valid
                    for ( ProductModel prod : aliasProds ) {
                    	ProductModel newProd = (ProductModel)prod.clone();
                        ( (ContentNodeModelImpl)newProd ).setParentNode(this);
                        ( (ContentNodeModelImpl)newProd ).setPriority(prodList.size());
                        if (!prodList.contains(newProd)) { // don't put a duplicate product in there
                            prodList.add(newProd);
                            // LOGGER.debug(" ##### added aliased product: " +
                            // newProd.getContentName());
                        } else {
                            // LOGGER.debug(" #### "
                            // + newProd.getContentName()
                            // + " already in list, not adding product: "
                            // + newProd.getContentName());
                        }
                    }
                }
            } catch (Exception ex) {
                LOGGER.warn("exception during category aliasing", ex);
            }
        }
        return prodList;
    }

    private List<String> getFilterList() {
        List<String> filterList;

        String filters = (String) getCmsAttributeValue("FILTER_LIST");
        if (filters != null) {
            filterList = new ArrayList<String>();
            StringTokenizer stFilterNames = new StringTokenizer(filters, ",");
            while ( stFilterNames.hasMoreTokens() ) {
                String tok = stFilterNames.nextToken();
                if (!filterList.contains(tok)) {
                    filterList.add(tok);
                }
            }
        } else {
            filterList = Collections.emptyList();
        }
        return filterList;
    }

	//** ContentFactory.getProductByName() is the main user..
	public ProductModel getProductByName(String productId) {
		for ( ProductModel pm : getProducts() ) {
			if (pm.getContentName().equals(productId)) {
				return pm;
			}
		}
		return null;
	}

	//** setNearestParentForProduct() is the main user..
	public ProductModel getPrivateProductByName(String productId) {
		for ( ProductModel pm : getPrivateProducts() ) {
			if (pm.getContentName().equals(productId)) {
				return pm;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return CategoryModel, same as getAliasCategory
	 */
	public CategoryModel getAlias() {
	    return getAliasCategory();
	}
	
	@Override
	public List<ProductModel> getFeaturedProducts() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(
			this,
			"FEATURED_PRODUCTS",
			featuredProductModels,
			false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(this, featuredProductModels);
		}
		return new ArrayList<ProductModel>(featuredProductModels);
	}

	/** Getter for property featured brands.
	 * @return List of BrandModels that are referenced by the  property brands.
	 */
	public List<BrandModel> getFeaturedBrands() {
		ContentNodeModelUtil.refreshModels(this, "FEATURED_BRANDS", featuredBrandModels, false);

		return new ArrayList<BrandModel>(featuredBrandModels);
	}

	/* get Featured New Items (Products/Brands) */
	public List<ContentNodeModel> getFeaturedNewProdBrands() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "FEATURED_NEW_PRODBRANDS", featuredNewProdBrands, false);

		if (bRefreshed) {
			List<ProductModel> tempList = new ArrayList<ProductModel>();
	
			//loop through and get only products
			for (ContentNodeModel item : featuredNewProdBrands) {
				if (item instanceof ProductModel) {
					tempList.add((ProductModel) item);
				}
			}
			//send over to have parents adjusted
			ContentNodeModelUtil.setNearestParentForProducts(this, tempList);
		}
		
		return new ArrayList<ContentNodeModel>(featuredNewProdBrands);
	}

	/**
	 * @return all the brands of available products within the category, recursively
	 */
	public Set<BrandModel> getAllBrands() throws FDResourceException {
		Set<BrandModel> brands = new TreeSet<BrandModel>(FULL_NAME_COMPARATOR);
		this.getAllBrands(brands, this);
		return brands;
	}
	
	/**
	 * @return content key of the category/department this alias category
	 * points to. If it this category is not a alias category , return null.
	 */
	public ContentKey getAliasAttributeValue() {
	    return (ContentKey) this.getCmsAttributeValue("ALIAS");
	}

	/**
	 * Returns alias category if has
	 *
	 * @return category<CategoryModel>
	 */
    public CategoryModel getAliasCategory() {
        ContentKey key = getAliasAttributeValue();
        if (key != null) {
            return (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key);
        }
        return null;
    }
	
	public boolean getTreatAsProduct() {
	    return getAttribute("TREAT_AS_PRODUCT", false);
	}
	
	/**
	 * Recursive method to get all brands.
	 */
	private void getAllBrands(Set<BrandModel> brands, CategoryModel category) throws FDResourceException {
		if (category == null) {
			return;
		}

		List<CategoryModel> subFolders = category.getSubcategories();
		if (subFolders != null && subFolders.size() != 0) {
			for ( CategoryModel c : subFolders ) {
				getAllBrands( brands, c );
			}
		}

		Collection<ProductModel> products = category.getProducts();
		for ( ProductModel product : products ) {
			if (product.isDiscontinued()) {
				continue;
			}
			brands.addAll(product.getBrands());
		}
	}

	public boolean hasCategoryAlias() {
		return categoryAlias != null;
	}

	private class CategoryAlias implements Serializable {
		
		private static final long	serialVersionUID	= -2035449126009749044L;
		
		private final List<CategoryModel> categoryRefs;
		private final List<String> productFilterNames;

		public CategoryAlias(List<CategoryModel> categoryRefs, List<String> prodFilterNames) {
			this.categoryRefs = categoryRefs;
			this.productFilterNames = prodFilterNames;
			// LOGGER.debug("Filter Name = " + productFilterNames);
		}

		public Collection<ProductModel> processCategoryAlias() throws FDResourceException {
			List<ProductModel> products = new ArrayList<ProductModel>();
			try {
				for ( CategoryModel cm : categoryRefs ) {
					products.addAll(cm.getStaticProducts());	
				}
				return filterProducts(products);
			} catch (FDResourceException fdre) {
				LOGGER.error("caught resource exception in CategoryAlias.processCategory()", fdre);
				throw fdre;
			}
		}

		private List<ProductModel> filterProducts(List<ProductModel> nodes) throws FDResourceException {
			List<ProductFilterI> productFilters = null;

			if (this.productFilterNames != null && !this.productFilterNames.isEmpty()) {
				productFilters = ProductFilterFactory.getInstance().getFilters(this.productFilterNames);
				// LOGGER.debug(" List of Filters = " + productFilters.size());
			} else {
				productFilters = Collections.emptyList();
			}
			List<ProductModel> nodeList = nodes;
			try {
				for (Iterator<ProductFilterI> fi = productFilters.iterator(); fi.hasNext() && !nodeList.isEmpty();) {
					ProductFilterI prodFilter = fi.next();
					// LOGGER.debug(" about to apply filter:" + prodFilter.getClass().getName());
					nodeList = new ArrayList<ProductModel>(prodFilter.apply(nodeList));
				}
			} catch (FDResourceException fdre) {
				LOGGER.error("caught resource exception atttempting to get products from category", fdre);
				throw fdre;
			}
			return nodeList;
		}

	}

	/**
	 * a category is active, if there is at least one item in it regardless of availability. Discontinued items are already filtered out.
	 * @return
	 */
	public boolean isActive() {	//legacy support
		return isActive(false);
	}
	
	private boolean isActive(boolean filterNonDisplayableProducts) {
	    List<ProductModel> products = getProducts();
		
	    if (filterNonDisplayableProducts){
	    	Iterator<ProductModel> itr = products.iterator();
	        while(itr.hasNext()) {
	        	ProductModel prod = itr.next();

	        	if (prod.isInvisible() || prod.isHidden() || prod.isDiscontinued()) {
	        		itr.remove();
	        	}
	        }
	    }
	    		
		if (!products.isEmpty()) {
	        return true;
	    }
	    for (CategoryModel subCat : getSubcategories()) {
	        if (subCat.isActive(filterNonDisplayableProducts)) {
	            return true;
	        }
	    }
	    return false;
	}

	public boolean isDisplayable() {
		return getSpecialLayout()!=null || isActive(true);
	}
	
	/**
     * 
     * @return CategoryModel looked up by RATING_HOME
     */
    public CategoryModel getRatingHome() {
        ContentKey key = (ContentKey) getCmsAttributeValue("RATING_HOME");
        
        return key != null ? (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key) : null;
    }
    
    @SuppressWarnings("unchecked")
    public List<Html> getArticles() {
        return FDAttributeFactory.constructWrapperList(this, "ARTICLES");
    }
    
    @SuppressWarnings("unchecked")
    public List<Html> getTopMedia() {
        return FDAttributeFactory.constructWrapperList(this, "CATEGORY_TOP_MEDIA");
    }

    @SuppressWarnings("unchecked")
    public List<Html> getMiddleMedia() {
        return FDAttributeFactory.constructWrapperList(this, "CATEGORY_MIDDLE_MEDIA");
    }

    @SuppressWarnings("unchecked")
    public List<Html> getBottomMedia() {
        return FDAttributeFactory.constructWrapperList(this, "CATEGORY_BOTTOM_MEDIA");
    }
    
    public Html getPreviewMedia() {
        return FDAttributeFactory.constructHtml(this, "CATEGORY_PREVIEW_MEDIA");
    }
    
    public String getMaterialCharacteristic() {
        return (String) getCmsAttributeValue("MATERIAL_CHARACTERISTIC");
    }
    
    public Html getAlternateContent() {
        return FDAttributeFactory.constructHtml(this, "ALTERNATE_CONTENT");
    }
    
    public Html getCategoryStorageGuideMedia() {
        return FDAttributeFactory.constructHtml(this, "CAT_STORAGE_GUIDE_MEDIA");
    }
    
    public Set<ContentKey> getAllChildProductKeys() {
    	Set<ContentKey> keys = new HashSet<ContentKey>();
    	for (ProductModel p : getPrivateProducts())
    		keys.add(p.getContentKey());
    	for (CategoryModel c : getSubcategories())
    		for (ProductModel p : c.getPrivateProducts())
    			keys.add(p.getContentKey());
    	return keys;
    }

    public Set<ProductModel> getAllChildProducts() {
    	Set<ProductModel> products = new HashSet<ProductModel>();
    	for (ProductModel p : getProducts())
    		products.add(p);
    	for (CategoryModel c : getSubcategories())
    		for (ProductModel p : c.getProducts())
    			products.add(p);
    	return products;
    }
    
    // Another version of the above method, this time returning a list.
    // Using a set like above only loses the natural ordering (CMS order) 
    // which was previously in the lists. There is no point in that.
    // Unless it was meant to filter out duplicates. 
    // Which will not work anyway using this approach. 
    // The same products in different subcategories will have different context,
    // and so they will be different objects, with possibly different values for
    // inheritable properties/relationships.
    // Using a simple HashSet will not resolve that they are the same products after all ...
    public List<ProductModel> getAllChildProductsAsList() {
    	List<ProductModel> products = new ArrayList<ProductModel>();
    	for (ProductModel p : getProducts())
    		products.add(p);
    	for (CategoryModel c : getSubcategories())
    		for (ProductModel p : c.getProducts())
    			products.add(p);
    	return products;
    }
    
    
	public boolean isHideWineRatingPricing() {
		return getAttribute("HIDE_WINE_RATING", false);
	}
	
	public static synchronized void forceSmartCategoryRecalculation() {
	    globalSmartCategoryVersion ++;
	}

	@Override
	public Image getPhoto() {
		return getCategoryPhoto();
	}

	public String getProductPromotionType() {
		String attr = getAttribute("PRODUCT_PROMOTION_TYPE", null);
		if (attr == null || "NONE".equals(attr) ){
			return null;
		} else {
			return attr;
		}
	}
	
	private synchronized boolean loadProductPromotion(String zoneId, String promotionPageType){
		String currentProductPromotionType = getProductPromotionType();
		
		if (currentProductPromotionType == null ){
			promotionPageType = currentProductPromotionType;
			return false;
			
		} else {			
			if (productPromotionDataRefMap.get(zoneId) == null || !currentProductPromotionType.equals(promotionPageType)){
				promotionPageType = currentProductPromotionType;
				productPromotionDataRefMap.put(zoneId, new ProductPromotionDataRef(threadPool,  zoneId,promotionPageType));
			}
			return true;
        }
	}
		
	public List<ProductModel> getPromotionPageProductsForPreview(String ppPreviewId) {	
		if(getProductPromotionType()!=null){
			String zoneId = ContentFactory.getInstance().getCurrentPricingContext().getZoneId();
			List<ProductModel> productModelList = null;
			try {				
//				if(!SapProperties.isBlackhole()){
					ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo = ProductPromotionInfoManager.getProductPromotionPreviewInfo(ppPreviewId);
					Map<String, List<FDProductPromotionInfo>> productPromotionPreviewInfoMap = ProductPromotionUtil.formatProductPromotionPreviewInfo(erpProductPromotionPreviewInfo);
					ProductPromotionData ppData = new ProductPromotionData();
					ppData = populateProductPromotionData(ppData, productPromotionPreviewInfoMap, zoneId,true);
					productModelList = ppData.getProductModels();
//				}
				if(null == productModelList || productModelList.isEmpty()){
					productModelList =  this.getPromotionPageProducts(productModelList,zoneId,getProductPromotionType());//get products from cache.
				}
				return productModelList;
//				return ppData.getProductModels();
			} catch(FDResourceException e){
				LOGGER.error(e);
			}
		}
		return null;
	}
	
	/* APPDEV-2259 */
	public boolean isHideFeaturedItems() {
		return getAttribute("HIDE_FEATURED_ITEMS", false);
	}
	
	public List<ProductGrabberModel> getProductGrabbers() {
		ContentNodeModelUtil.refreshModels(this, "productGrabbers", productGrabbers, true);
		return new ArrayList<ProductGrabberModel>(productGrabbers);
	}

	public boolean isPreferenceCategory() {
		return getAttribute("preferenceCategory", false);
	}
	
	public Html getDescription() {
		return FDAttributeFactory.constructHtml(this, "description");
	}

	public final Image getNameImage() {
		return FDAttributeFactory.constructImage(this, "nameImage");
	}
	
	public EnumLayoutType getSpecialLayout() {
		EnumLayoutType specialLayout = getLayout();
		switch (specialLayout){
			case HOW_TO_COOK_IT:
			case TRANSAC_MULTI_PAIRED_ITEMS:
			case TEMPLATE_LAYOUT:
			case PRESIDENTS_PICKS:
				return specialLayout;
			default:
				return null;
		}
	}
	
	public EnumLayoutType getFullWidthLayout() {
		EnumLayoutType specialLayout = getLayout();
		switch (specialLayout){
			case TEMPLATE_LAYOUT:
			case PRESIDENTS_PICKS:
				return specialLayout;
			default:
				return null;
		}
	}

	public String getCatMerchantRecommenderTitle() {
		return getAttribute("catMerchantRecommenderTitle", "");
	}

	public List<ProductModel> getCatMerchantRecommenderProducts() {
		ContentNodeModelUtil.refreshModels(this, "catMerchantRecommenderProducts", catMerchantRecommenderProducts, false, true);
		return new ArrayList<ProductModel>(catMerchantRecommenderProducts);
	}

	public boolean isCatMerchantRecommenderRandomizeProducts() {
	    return getAttribute("catMerchantRecommenderRandomizeProducts", false);
	}

	public boolean isHideIfFilteringIsSupported() {
	    return getAttribute("hideIfFilteringIsSupported", false);
	}
	
	/*public List<ProductModel> getPromotionPageProductsById(String ppId) {	
		if(getProductPromotionType()!=null){
			String zoneId = ContentFactory.getInstance().getCurrentPricingContext().getZoneId();
			List<ProductModel> productModelList = null;
			try {	
				Map<String,List<FDProductPromotionInfo>> productPromotionInfoMap =FDProductAssortmentPromotionFactory.getInstance().getProductPromotion(getProductPromotionType(),ppId);			
				ProductPromotionData ppData = new ProductPromotionData();
				ppData = populateProductPromotionData(ppData, productPromotionInfoMap, zoneId,false);
				productModelList = new ArrayList<ProductModel>();
				addDynamicProductsForPromotion(ppData.getProductModels(), productModelList,false);
				// If we want to show the default promotion type products when the products by given promotion id is empty.
				if(null == productModelList || productModelList.isEmpty()){
					productModelList =  this.getPromotionPageProducts(productModelList,zoneId,getProductPromotionType());//get products from cache.
				}
				return productModelList;
			} catch(FDResourceException e){
				LOGGER.error(e);
			}
		}
		return null;
	}*/
	
	public List<ProductModel> getAssortmentPromotionPageProducts(String promotionId) {
		List<ProductModel> prodList =new ArrayList<ProductModel>();
		String zoneId = ContentFactory.getInstance().getCurrentPricingContext().getZoneId();
		if (EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName().equalsIgnoreCase(getProductPromotionType())) {
			loadProductAssortmentPromotion(zoneId, promotionId);
			try {
				if(null != productAssortmentPromotionDataRefMap.get(promotionId) && null !=productAssortmentPromotionDataRefMap.get(promotionId).get()) {
					prodList = new ArrayList<ProductModel>();
					addDynamicProductsForPromotion(productAssortmentPromotionDataRefMap.get(promotionId).get().getProductModels(), prodList,false);
				}
		    } catch (Exception e) {
		        LOGGER.warn("exception during promo category product assignment", e);
		    }
		}
		return prodList;
	}
	
	private synchronized boolean loadProductAssortmentPromotion(String zoneId, String promotionId){
		String currentProductPromotionType = getProductPromotionType();		
		if (currentProductPromotionType == null ){
			return false;			
		} else {			
			if (productAssortmentPromotionDataRefMap.get(promotionId) == null && isValidAssortmentPromotion(currentProductPromotionType,promotionId)){
				productAssortmentPromotionDataRefMap.put(promotionId, new ProductAssortmentPromotionDataRef(threadPool,  zoneId, promotionId, currentProductPromotionType));
			}
			return true;
        }
	}
	
	public Image getGlobalNavPostNameImage() {
        return FDAttributeFactory.constructImage(this, "globalNavPostNameImage");
	}

	private boolean isValidAssortmentPromotion(String productPromotionType, String promotionId){		
		return (null !=FDProductAssortmentPromotionFactory.getInstance().getProductPromotion(productPromotionType,promotionId))?true:false;
	}


	/**
	 * Return the level of category deepness
	 * 
	 * @return
	 */
	public int getCategoryLevel() {
		if (isOrphan())
			return 0;
		
		ContentNodeModel m = getParentNode();
		if (m instanceof CategoryModel) {
			return ((CategoryModel) m).getCategoryLevel() + 1;
		}
		return 0;
	}
	
	public boolean isTopLevelCategory(){
		return getParentNode() instanceof DepartmentModel;
	}

}
