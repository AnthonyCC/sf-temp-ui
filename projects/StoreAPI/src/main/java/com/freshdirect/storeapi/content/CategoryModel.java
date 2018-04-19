package com.freshdirect.storeapi.content;

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

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.ejb.FDProductPromotionManager;
import com.freshdirect.erp.ejb.ProductPromotionInfoManager;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDProductPromotionPreviewInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ecoupon.FDCouponFactory;
import com.freshdirect.fdstore.ecoupon.model.FDCouponUPCInfo;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.GrabberServiceLocator;
import com.freshdirect.storeapi.ProductModelPromotionAdapter;
import com.freshdirect.storeapi.StoreServiceLocator;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;
import com.freshdirect.storeapi.content.grabber.GrabberServiceI;
import com.freshdirect.storeapi.smartstore.CmsRecommenderService;
import com.freshdirect.storeapi.util.ProductPromotionUtil;

public class CategoryModel extends ProductContainer {

	private static final long serialVersionUID = 5787890004203680537L;

	private static final Logger LOGGER = LoggerFactory.getInstance(CategoryModel.class);

	private static final long FIVE_MINUTES = 5l * 60l * 1000l;

	private final List<ImageBanner> heroCarousel = new ArrayList<ImageBanner>();

	private final class RecommendedProductsRef extends BalkingExpiringReference<List<ProductModel>> {
		ZoneInfo pricingZone;

        private RecommendedProductsRef(ZoneInfo pricingZone) {
            super(FIVE_MINUTES, false);
            this.pricingZone = pricingZone;
            // synchronous load
            try {
                set(load());
            } catch (RuntimeException e) {
                LOGGER.error("failed to initialize smart products for category " + CategoryModel.this.getContentName() + " and pricingZone " + pricingZone
                        + " synchronously");
            }
        }

		@Override
		protected List<ProductModel> load() {
			CmsRecommenderService recommenderService = getRecommenderService();
			ContentKey categoryId = CategoryModel.this.getContentKey();
			final Recommender recommender = CategoryModel.this.getRecommender();
            ContentKey recommenderId = recommender != null ? recommender.getContentKey() : null;
			LOGGER.debug("loader started for category " + categoryId + ", pricingZone " + pricingZone + ", recommender: " + recommenderId);

			if ( recommenderService == null || recommenderId == null ) {
				LOGGER.warn("recommender service ("
						+ recommenderId
						+ ") not found, returning previous recommendations for category "
						+ categoryId);
				// fallback
				throw new RuntimeException();
			}

			List<String> prodIds = recommenderService.recommendNodes(recommenderId, categoryId, pricingZone);
			List<ProductModel> products = new ArrayList<ProductModel>( prodIds.size() );
			for ( String productId : prodIds ) {
				ContentNodeModel product = ContentFactory.getInstance().getContentNode( productId );
				if ( product instanceof ProductModel && !products.contains( product ) )
					products.add( (ProductModel)product );
			}
			//LOGGER.debug( "found " + products.size() + " products for category " + categoryId + ", zone " + pricingZone);
			return products;
		}

		private CmsRecommenderService getRecommenderService() {
			try {
                return StoreServiceLocator.recommenderService();
			} catch (RuntimeException e) {
				LOGGER.error("failed to initialize CMS recommender service", e);
			}
			return null;
		}

		@SuppressWarnings( "unused" )
		public ZoneInfo getZoneInfo() {
			return pricingZone;
		}
	}

	private CategoryAlias categoryAlias;

	/**
	 * map of zoneId --> recommended products reference
	 */
	private final class ProductPromotionDataRef extends BalkingExpiringReference<ProductPromotionData> {

		private final String productPromotitonType;
		private final ZoneInfo pricingZone;

        public ProductPromotionDataRef(ZoneInfo pricingZone, String productPromotitonType) {
            super(FIVE_MINUTES, false);
			this.pricingZone = pricingZone;
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
				Map<ZoneInfo,List<FDProductPromotionInfo>> productPromoInfoMap ;
				String ppType =productPromotitonType;
				if(null !=ppType){
					if("PRESIDENTS_PICKS_PREVIEW".equals(ppType)||"PRESIDENTS_PICKS".equals(ppType)){
						ppType = "PRESIDENTS_PICKS";
					}
					if("PRODUCTS_ASSORTMENTS_PREVIEW".equals(ppType)||"PRODUCTS_ASSORTMENTS".equals(ppType)){
						ppType = "PRODUCTS_ASSORTMENTS";
					}
				}
				if(!"E_COUPONS".equals(ppType)){
					synchronized (FDProductPromotionManager.getInstance()) {
						productPromoInfoMap = FDProductPromotionManager.getProductPromotion(ppType);
					}
					populateProductPromotionData(productPromotionData,productPromoInfoMap,pricingZone,false);
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
				String couponUPC = iterator.next();
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
					SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(ContentType.Sku, skuCode);
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
			Map<ZoneInfo, List<FDProductPromotionInfo>> productPromoInfoMap,ZoneInfo pricingZone,boolean isPreview)
			throws FDResourceException {
		Map<String, ProductModel> skuProductMap = new HashMap<String, ProductModel>();
		List<ProductModel> productModels = new ArrayList<ProductModel>();
		if(null !=productPromoInfoMap){
			List<FDProductPromotionInfo> fDProductPromotionSkus = productPromoInfoMap.get(pricingZone);
			if(null ==fDProductPromotionSkus){
				fDProductPromotionSkus = productPromoInfoMap.get(new ZoneInfo("0000100000","0001","01"));//ProductPromotionData.DEFAULT_ZONE);
				productPromoInfoMap.put(pricingZone,fDProductPromotionSkus);
			}
			if(null != fDProductPromotionSkus)
			for (FDProductPromotionInfo fDProductPromotionSku : fDProductPromotionSkus){
				String sku = fDProductPromotionSku.getSkuCode();
                //No need to refresh the productnfo.
                /*if(!isPreview){
                        FDCachedFactory.refreshProductPromotionSku(sku);
                }*/
				ProductModel productModel = null;
				ProductModel prodModel = null;
				try {
					SkuModel skuModel = ((SkuModel) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get(ContentType.Sku, sku)));
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

	private final Map<ZoneInfo, RecommendedProductsRef> recommendedProductsRefMap = new HashMap<ZoneInfo, RecommendedProductsRef>();

	private final Map<ZoneInfo, ProductPromotionDataRef> productPromotionDataRefMap = new HashMap<ZoneInfo, ProductPromotionDataRef>();

	//private final Map<String, ProductAssortmentPromotionDataRef> productAssortmentPromotionDataRefMap = new HashMap<String, ProductAssortmentPromotionDataRef>();
	private final Map<ZoneInfo, ProductPromotionDataRef> productAssortmentPromotionDataRefMap = new HashMap<ZoneInfo, ProductPromotionDataRef>();

	private String promotionPageType;

	private final Object recommendedProductsSync = new Object();

	private int smartCategoryVersion;

	private static int globalSmartCategoryVersion = 1;

	private final List<CategoryModel> subcategoriesModels = new ArrayList<CategoryModel>();

	private final List<ProductModel> productModels = new ArrayList<ProductModel>();

	private final List<ProductModel> featuredProductModels = new ArrayList<ProductModel>();

	private final List<BrandModel> featuredBrandModels = new ArrayList<BrandModel>();

	private final List<ContentNodeModel> featuredNewProdBrands = new ArrayList<ContentNodeModel>();

	/**
	 * List of ProductModels and CategoryModels.
	 */
	private final List<ContentNodeModel> candidateList = new ArrayList<ContentNodeModel>();

	// New Wine Store

	private final List wineSortCriteriaList = new ArrayList();

	private final List wineFilterCriteriaList = new ArrayList();

	private final List wineSideNavSectionsList = new ArrayList();

	private final List wineSideNavFullsList = new ArrayList();

	private final List<ProductModel> howToCookItProducts = new ArrayList<ProductModel> ();

    final private List<CategoryModel> virtualGroups = new ArrayList<CategoryModel>();

	private final List<ProductGrabberModel> productGrabbers = new ArrayList<ProductGrabberModel>();

    private final List<ProductModel> catMerchantRecommenderProducts = new ArrayList<ProductModel>();

    public CategoryModel(ContentKey cKey) {
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

	@Override
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
    	ZoneInfo pricingZone = ContentFactory.getInstance()!=null && ContentFactory.getInstance().getCurrentUserContext()!=null &&
    								ContentFactory.getInstance().getCurrentUserContext().getPricingContext()!=null?
    										ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo():null;
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
	        		LOGGER.debug( this.getContentKey().toString() + ": recommender changed!" );
	        	}

	            //LOGGER.info("Category[id=\"" + this.getContentKey().getId() + "\"].getSmartProducts(\"" + zoneId + "\")");
                if (!FDStoreProperties.isLocalDeployment()) { // refreshes this.productGrabbers
                    synchronized (CategoryModel.class) {
                        if (globalSmartCategoryVersion > smartCategoryVersion) {
                            LOGGER.debug("forced smart category recalculation : " + smartCategoryVersion + " -> " + globalSmartCategoryVersion + " for category : "
                                    + this.getContentKey().id);
                            smartCategoryVersion = globalSmartCategoryVersion;
                            recommendedProductsRefMap.clear();
                        }
                    }
                }

	            synchronized (recommendedProductsSync) {
	                if ( recommenderChanged || recommendedProductsRefMap.get(pricingZone) == null )
                        recommendedProductsRefMap.put(pricingZone, new RecommendedProductsRef(pricingZone));
	            }

	            try {
	//                List<ProductModel> recProds = recommendedProductsRefMap.get(zoneId).get();
	                addDynamicProducts(recommendedProductsRefMap.get(pricingZone).get(), prodList,true);
	            } catch (Exception e) {
	                LOGGER.error("exception during smart category recommendation", e);
	            }
	        }
    	}else{
//	        String currentProductPromotionType = getProductPromotionType();
	        prodList = getPromotionPageProducts(prodList, pricingZone,currentProductPromotionType);
    	}


    	// == [APPDEV-2910] add products picked by new product grabbers ==
		{
            if (!FDStoreProperties.isLocalDeployment() && getProductGrabbers() != null) { // refreshes this.productGrabbers
                GrabberServiceI grabber = GrabberServiceLocator.grabberService();
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
			List<ProductModel> prodList,ZoneInfo pricingZone,
			String currentProductPromotionType) {
		if (currentProductPromotionType != null) {
			loadProductPromotion(pricingZone, currentProductPromotionType);
			try {
				if(null !=productPromotionDataRefMap.get(pricingZone).get()) {
					prodList = new ArrayList<ProductModel>();
					if("E_COUPONS".equals(currentProductPromotionType)){
						addDynamicProducts(productPromotionDataRefMap.get(pricingZone).get().getProductModels(), prodList,false);
					}else{
						addDynamicProductsForPromotion(productPromotionDataRefMap.get(pricingZone).get().getProductModels(), prodList);
					}
				}
		    } catch (Exception e) {
                LOGGER.error("exception during promo category product assignment", e);
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
    @Override
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

	    if (filterNonDisplayableProducts&&products!=null&&!products.isEmpty()){
	    	Iterator<ProductModel> itr = products.iterator();
	        while(itr.hasNext()) {
	        	ProductModel prod = itr.next();

	        	if (prod.isInvisible() || prod.isHidden() || prod.isDiscontinued()) {
	        		itr.remove();
	        	} else {
	        	    return true;
	        	}
	        }
	    }

		if (products!=null&&!products.isEmpty()) {
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
        return checkSpecialLayoutDisplayRules() || isActive(true);
    }

    private boolean checkSpecialLayoutDisplayRules() {
        EnumLayoutType layout = getLayout();
        return !EnumLayoutType.RECIPE_MEALKIT_CATEGORY.equals(layout) && !EnumLayoutType.HOLIDAY_MEAL_BUNDLE_CATEGORY.equals(layout) && getSpecialLayout() != null;
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

	private synchronized boolean loadProductPromotion(ZoneInfo pricingZone, String promotionPageType){
		String currentProductPromotionType = getProductPromotionType();
		if (currentProductPromotionType == null ){
			promotionPageType = currentProductPromotionType;
			return false;

		} else {
			if (productPromotionDataRefMap.get(pricingZone) == null || !currentProductPromotionType.equals(promotionPageType)){
				promotionPageType = currentProductPromotionType;
                productPromotionDataRefMap.put(pricingZone, new ProductPromotionDataRef(pricingZone, promotionPageType));
			}
			return true;
        }
	}

	public List<ProductModel> getPromotionPageProductsForPreview(String ppPreviewId) {
		if(getProductPromotionType()!=null){
			ZoneInfo pricingZone = ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo();
			List<ProductModel> productModelList = null;
			try {
//				if(!SapProperties.isBlackhole()){
					ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo = ProductPromotionInfoManager.getProductPromotionPreviewInfo(ppPreviewId);
					Map<ZoneInfo, List<FDProductPromotionInfo>> productPromotionPreviewInfoMap = ProductPromotionUtil.formatProductPromotionPreviewInfo(erpProductPromotionPreviewInfo);
					ProductPromotionData ppData = new ProductPromotionData();
					ppData = populateProductPromotionData(ppData, productPromotionPreviewInfoMap, pricingZone,true);
					productModelList = ppData.getProductModels();
//				}
				if(null == productModelList || productModelList.isEmpty()){
					productModelList =  this.getPromotionPageProducts(productModelList,pricingZone,getProductPromotionType());//get products from cache.
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
                //$FALL-THROUGH$
			case TRANSAC_MULTI_PAIRED_ITEMS:
                //$FALL-THROUGH$
			case TEMPLATE_LAYOUT:
                //$FALL-THROUGH$
			case PRESIDENTS_PICKS:
                //$FALL-THROUGH$
			case PRODUCTS_ASSORTMENTS:
                //$FALL-THROUGH$
            case HOLIDAY_MEAL_BUNDLE_CATEGORY:
                //$FALL-THROUGH$
            case RECIPE_MEALKIT_CATEGORY:
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
			case PRODUCTS_ASSORTMENTS:
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

	public List<ProductModel> getAssortmentPromotionPageProducts(String promotionId) {
		List<ProductModel> prodList =new ArrayList<ProductModel>();
//		String zoneId = ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo().getPricingZoneId();
		ZoneInfo zoneInfo = ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo();
		if (EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getName().equalsIgnoreCase(getProductPromotionType())) {
			loadProductAssortmentPromotion(zoneInfo, promotionId);
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

	private synchronized boolean loadProductAssortmentPromotion(ZoneInfo pricingZone, String promotionPageType){
		String currentProductPromotionType = getProductPromotionType();
		if (currentProductPromotionType == null ){
		promotionPageType = currentProductPromotionType;
			return false;
		} else {
			if (productAssortmentPromotionDataRefMap.get(pricingZone) == null || !currentProductPromotionType.equals(promotionPageType)){
				promotionPageType = currentProductPromotionType;
                productAssortmentPromotionDataRefMap.put(pricingZone, new ProductPromotionDataRef(pricingZone, promotionPageType));
			}
			return true;
        }
	}

	public Image getGlobalNavPostNameImage() {
        return FDAttributeFactory.constructImage(this, "globalNavPostNameImage");
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

	@Override
    public boolean isTopLevelCategory(){
		return getParentNode() instanceof DepartmentModel;
	}

	public EnumBrowseEditorialLocation getBannerLocationCLP(String defaultValue) {
		return EnumBrowseEditorialLocation.valueOf(getAttribute("bannerLocationCLP", defaultValue));
	}

	public EnumBrowseEditorialLocation getBannerLocationPLP(String defaultValue) {
		return EnumBrowseEditorialLocation.valueOf(getAttribute("bannerLocationPLP", defaultValue));
	}

	public EnumBrowseEditorialLocation getCarouselPositionCLP(String defaultValue) {
		return EnumBrowseEditorialLocation.valueOf(getAttribute("carouselPositionCLP", defaultValue));
	}

	public EnumBrowseEditorialLocation getCarouselPositionPLP(String defaultValue) {
		return EnumBrowseEditorialLocation.valueOf(getAttribute("carouselPositionPLP", defaultValue));
	}

    public EnumBrowseCarouselRatio getCarouselRatioCLP(String defaultValue) {
		return EnumBrowseCarouselRatio.valueOf(getAttribute("carouselRatioCLP", defaultValue));
	}

    public EnumBrowseCarouselRatio getCarouselRatioPLP(String defaultValue) {
		return EnumBrowseCarouselRatio.valueOf(getAttribute("carouselRatioPLP", defaultValue));
	}

    public EnumBrandFilterLocation getBrandFilterLocation(String defaultValue) {
		return EnumBrandFilterLocation.valueOf(getAttribute("brandFilterLocation", defaultValue));
	}

	public final Image getTabletThumbnailImage() {
		return FDAttributeFactory.constructImage(this, "tabletThumbnailImage");
	}

	public List<ImageBanner> getHeroCarousel() {
		ContentNodeModelUtil.refreshModels(this, "heroCarousel", heroCarousel, false);
		return new ArrayList<ImageBanner>(heroCarousel);
	}

}
