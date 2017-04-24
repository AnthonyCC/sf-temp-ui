package com.freshdirect.webapp.ajax.backoffice.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ComponentGroupModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.DYFUtil;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.backoffice.data.ActionDataRequest;
import com.freshdirect.webapp.ajax.backoffice.data.BackOfficeResponse;
import com.freshdirect.webapp.ajax.backoffice.data.BackOfficeServiceAction;
import com.freshdirect.webapp.ajax.backoffice.data.CRMSmartStoreInfoResponse;
import com.freshdirect.webapp.ajax.backoffice.data.CategoryModelResponse;
import com.freshdirect.webapp.ajax.backoffice.data.DomainValueModelResponse;
import com.freshdirect.webapp.ajax.backoffice.data.OverriddenVariantsResponse;
import com.freshdirect.webapp.ajax.backoffice.data.ProductModelResponse;
import com.freshdirect.webapp.ajax.backoffice.data.SkuModelResponse;
import com.freshdirect.webapp.ajax.backoffice.data.VariantResponse;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.expresscheckout.content.service.ContentFactoryService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.location.service.DeliveryAddressService;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.service.ReceiptService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.service.TextMessageAlertService;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.checkout.RedirectToPage;


public class BackOfficeService {


    private static final Category LOGGER = LoggerFactory.getInstance(BackOfficeService.class);

    public static BackOfficeService defaultService() {
        return INSTANCE;
    }

    private static final BackOfficeService INSTANCE = new BackOfficeService();


    private BackOfficeService() {  }

  
    public BackOfficeResponse submitPostRequest(FDUserI user, ActionDataRequest actionDataRequest,  HttpServletRequest  request, HttpServletResponse  response) throws FDResourceException,
            IOException,  JspException,  HttpErrorResponse {
    	BackOfficeResponse result = new BackOfficeResponse();
        String actionName = getActionData(actionDataRequest, "action");
        ProductModel productModel = null;

        Map<String,DomainValueModelResponse> templateWithDomVal = new HashMap<String,DomainValueModelResponse>();
        BackOfficeServiceAction ast = BackOfficeServiceAction.parse(actionName);
        switch (ast) {
            case GET_CATEGORY:
            	String catId= getActionData(actionDataRequest, "categoryId");
            	final CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, catId);
            	CategoryModelResponse categoryModelResponse = buildCategoryModel(categoryModel);
        		result.setCategoryModelResponse(categoryModelResponse);
            	break;	
            case GET_CONTENT_TYPE:
            	String contentId= getActionData(actionDataRequest, "categoryId");
            	final Map<String, String> contentMap = getContentMap();
        		String ContentType = contentMap.get(contentId);
        		break;
            case GET_DEPT:
            	String deptId= getActionData(actionDataRequest, "departmentId");
            	final DepartmentModel departmentModel = (DepartmentModel) ContentFactory.getInstance().getContentNode(FDContentTypes.DEPARTMENT, deptId);
            	break;
            case GET_DOMAIN_VALUES:
            	List<String> templateIds= getActionObject(actionDataRequest, "templateIds");
            	
            	for (ListIterator<String> li = templateIds.listIterator(); li.hasNext();) {
            		String temId=li.next();
            	final DomainValue domainValue = (DomainValue) ContentFactory.getInstance().getContentNode(FDContentTypes.DOMAINVALUE, temId);
            	DomainValueModelResponse domainValueModelResponse = buildDomainValueModel(domainValue);
            	if(domainValue!=null)
            		templateWithDomVal.put(temId, domainValueModelResponse);
            	}
            	result.setDomainValueModelResponse(templateWithDomVal);
            	break;
            case GET_DOMAIN_VALUE_RESPONSE:
            	String domainValId= getActionData(actionDataRequest, "domainValueId");
            	final DomainValue domainVal = (DomainValue) ContentFactory.getInstance().getContentNode(FDContentTypes.DOMAINVALUE, domainValId);
            	templateWithDomVal.put(domainValId, buildDomainValueModel(domainVal));
            	result.setDomainValueModelResponse(templateWithDomVal);
            	break;
            case GET_PRODUCT:
            	String prodId= getActionData(actionDataRequest, "productId");
            	productModel = (ProductModel) ContentFactory.getInstance().getContentNode(FDContentTypes.PRODUCT, prodId);
         		ProductModelResponse prodModelResponse = buildProductModel(productModel);
         		result.setProductModelResponse(prodModelResponse);
            	 break;
            case GET_PRODUCT_BY_SKU:
            	String skuCode= getActionData(actionDataRequest, "skuCode");
        		final SkuModel skuModel = (SkuModel) ContentFactory.getInstance().getContentNode(FDContentTypes.SKU, skuCode);
        		productModel = skuModel.getProductModel();
        		break;
            case GET_SKU:
            	String skuCodeOnly= getActionData(actionDataRequest, "skuCode");
        		final SkuModel skuModelOnly = (SkuModel) ContentFactory.getInstance().getContentNode(FDContentTypes.SKU, skuCodeOnly);
        		SkuModelResponse skuModelResponse = buildSkuProductModel(skuModelOnly);
        		result.setSkuModelResponse(skuModelResponse);
        		break;
            case GET_LIST_SKU:
            	List<String> skuCodeList= getActionObject(actionDataRequest, "skuCodes");
            	List<SkuModelResponse> listSku = new ArrayList<SkuModelResponse>();
            	 for (ListIterator<String> li = skuCodeList.listIterator(); li.hasNext();) {
            		 final SkuModel skuMod = (SkuModel) ContentFactory.getInstance().getContentNode(FDContentTypes.SKU, li.next());
            		 SkuModelResponse skuModelResp = buildSkuProductModel(skuMod);
            		 if(skuModelResp!=null)
            		 listSku.add(skuModelResp);
            	 }
            	result.setSkuModelResponseList(listSku);
        		break;
            case GET_SMART_STORE_INFO:
            	CRMSmartStoreInfoResponse cRMSmartStoreInfoResponse = getCRMSmartStoreInfo(user);
            	break;
            case GET_SUPER_DEPT:
            	String supDeptId= getActionData(actionDataRequest, "supDeptId");
        		final SuperDepartmentModel superDepartmentModel = (SuperDepartmentModel) ContentFactory.getInstance().getContentNode(FDContentTypes.SUPER_DEPARTMENT, supDeptId);
                break;
            case GET_SKU_INFO:
            	String skuCodeDetails= getActionData(actionDataRequest, "skuCode");
            	String skuProdCatInfo = getCatProdInfoBySku(request,skuCodeDetails); 
            	result.setSkuInfo(skuProdCatInfo);
        		break;
            default:
                break;
        }
        return result;
    }
    
    private static final String PATH = "/test/freemarker_testing/all_info.jsp?sku2url=true&sku=";
    private String getCatProdInfoBySku(HttpServletRequest  request, String skuCodeDetails) throws FDResourceException {
    	
    	String basePath = "";//request.getScheme()+"://"+request.getServerName();
		 if(FDStoreProperties.isLocalDeployment()){
			 basePath = "http" + "://" + request.getServerName() + ":" + request.getServerPort();
		 }else{
			 basePath = "https" + "://" + request.getServerName();
		 }
		 basePath=basePath + request.getContextPath();
		 LOGGER.info("The BasePath : "+basePath);
			StringBuffer sb = null;
			BufferedReader br = null;
			
			
			try {
				URL url = new URL(basePath+PATH + skuCodeDetails);
				LOGGER.info("The BasePath url : "+url);
				URLConnection conn = url.openConnection();

				conn.setDoInput(true);
				conn.setDoOutput(false);
				                                    
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				sb = new StringBuffer();
				                                    
				String inputLine;
				                                    
				while ((inputLine = br.readLine()) != null)
				    sb.append(inputLine);
				                                                    
				br.close();
			}catch (MalformedURLException me) {
				LOGGER.error(me);
				throw new FDResourceException("Not able to Process Requrest at this time, please retry again");
			} catch (IOException ie) {
				LOGGER.error(ie);
				throw new FDResourceException("Not able to Process Requrest at this time, please try again");
			}finally {
				if(br!=null){
					try {
						br.close();
					} catch (IOException e) {
						LOGGER.error(e);
					}
				}
			}
    	return sb.toString();
    	
    }


	private DomainValueModelResponse buildDomainValueModel(
			DomainValue domainValue) {		
    	
    	LOGGER.debug("Beginning createDomainValueResponse :: ResponseConverter");
			if (domainValue==null) {
				return null;
			}
			DomainValueModelResponse domainValueModelResponse = new DomainValueModelResponse();
			
			domainValueModelResponse.setLabel(domainValue.getLabel());
			domainValueModelResponse.setValue(domainValue.getValue());
			domainValueModelResponse.setDomainName(domainValue.getDomainName());
			domainValueModelResponse.setTheValue(domainValue.getTheValue());
			LOGGER.debug("Ending buildDomainValueModel :: BackOfficeServie");
			return domainValueModelResponse;
			}


	private ProductModelResponse buildProductModel(ProductModel productModel) {
    	if (productModel==null) {
			return null;
		}

		final ProductModelResponse productResponse = new ProductModelResponse();
		productResponse.setHasTerms(productModel.hasTerms());
		if (productModel.getPreferredSku() != null)
			productResponse.setPreferredSku(productModel.getPreferredSku().getContentName());
		productResponse.setHasActiveYmalSets(productModel.hasActiveYmalSets());

	
		if (productModel.getPerfectPair() != null)
			productResponse.setPerfectPair(productModel.getPerfectPair().getContentName());
		if (productModel.getCategory() != null)
			productResponse.setParentCategory(productModel.getCategory().getContentName());
		if (productModel.getSkus() != null)
			productResponse.setSkus(createSkuListResponse(productModel.getSkus()));
		if (productModel.getPrimarySkus() != null)
			productResponse.setPrimarySkus(createSkuListResponse(productModel.getPrimarySkus()));

		if (productModel.getProductDescription() != null)
			productResponse.setProductDescription(productModel.getProductDescription().getPath());
		productResponse.setAka(productModel.getAka());
		productResponse.setIncrementMaxEnforce(productModel.isIncrementMaxEnforce());
		productResponse.setExcludedRecommendation(productModel.isExcludedRecommendation());
		productResponse.setExcludedForEBTPayment(productModel.isExcludedForEBTPayment());
		productResponse.setQuantityText(productModel.getQuantityText());
		productResponse.setQuantityTextSecondary(productModel.getQuantityTextSecondary());
		productResponse.setSoldBySalesUnits(productModel.isSoldBySalesUnits());
		productResponse.setPerishable(productModel.isPerishable());
		productResponse.setPreconfigured(productModel.isPreconfigured());
		productResponse.setYmalHeader(productModel.getYmalHeader());
		productResponse.setSeasonText(productModel.getSeasonText());
		productResponse.setFrozen(productModel.isFrozen());
		productResponse.setShowTopTenImage(productModel.isShowTopTenImage());
		productResponse.setShowSalesUnitImage(productModel.isShowSalesUnitImage());
		productResponse.setHeatRating(productModel.getHeatRating());
		productResponse.setGrocery(productModel.isGrocery());
		productResponse.setFullyAvailable(productModel.isFullyAvailable());
		productResponse.setDisplayableBasedOnCms(productModel.isDisplayableBasedOnCms());
		productResponse.setTemporaryUnavailableOrAvailable(productModel.isTemporaryUnavailableOrAvailable());
		productResponse.setBrowseRecommenderType(productModel.getBrowseRecommenderType());
		if (productModel.getProductLayout() != null)
			productResponse.setProductLayout(productModel.getProductLayout().getId());
		productResponse.setSubtitle(productModel.getSubtitle());

		if (productModel.getLayout() != null)
		productResponse.setLayout(productModel.getLayout().getId());
		productResponse.setRedirectUrl(productModel.getRedirectUrl());
		productResponse.setSellBySalesunit(productModel.getSellBySalesunit());
		productResponse.setSalesUnitLabel(productModel.getSalesUnitLabel());
		productResponse.setServingSuggestion(productModel.getServingSuggestion());
		productResponse.setPackageDescription(productModel.getPackageDescription());
		productResponse.setSeafoodOrigin(productModel.getSeafoodOrigin());
		productResponse.setNutritionMultiple(productModel.isNutritionMultiple());
		productResponse.setNotSearchable(productModel.isNotSearchable());
		productResponse.setProdPageRatings(productModel.getProdPageRatings());
		productResponse.setProdPageTextRatings(productModel.getProdPageTextRatings());
		productResponse.setRatingProdName(productModel.getRatingProdName());
		if (productModel.getPrimaryHome() != null)
			productResponse.setPrimaryHome(productModel.getPrimaryHome().getContentName());
		productResponse.setRetainOriginalSkuOrder(productModel.isRetainOriginalSkuOrder());
		productResponse.setShowDefaultSustainabilityRating(productModel.showDefaultSustainabilityRating());
		productResponse.setEnforceQuantityMax(productModel.enforceQuantityMax());
		productResponse.setHasSalesUnitDescription(productModel.isHasSalesUnitDescription());
		productResponse.setHasPartiallyFrozen(productModel.isHasPartiallyFrozen());
		productResponse.setExpertWeight(productModel.getExpertWeight());
		productResponse.setAlsoSoldAsName(productModel.getAlsoSoldAsName());
		productResponse.setContainerWeightHalfPint(productModel.getContainerWeightHalfPint());
		productResponse.setContainerWeightPint(productModel.getContainerWeightPint());
		productResponse.setContainerWeightQuart(productModel.getContainerWeightQuart());
			if (productModel.getTemplateType() != null)
			productResponse.setTemplateType(productModel.getTemplateType().getId());
		
		productResponse.setPriority(productModel.getPriority());
		productResponse.setWineRegion(productModel.getWineRegion());
			productResponse.setDisabledRecommendations(productModel.isDisabledRecommendations());
		productResponse.setHasActiveYmalSets(productModel.hasActiveYmalSets());
		if (productModel.getDepartment() != null) {
			productResponse.setDepartment(productModel.getDepartment().getContentName());
		}
		if(productModel.getSpecialLayout()!=null)
		productResponse.setSpecialLayout(productModel.getSpecialLayout().getId());
		productResponse.setPageTitle(productModel.getPageTitle());
		
		LOGGER.debug("Ending createProductResponse :: ResponseConverter");
		return productResponse;
	}


	public List<String> createSkuListResponse(List<SkuModel> skus) {
		List<String> skuResponses = null;
		if (skus != null && !skus.isEmpty()) {
			skuResponses = new ArrayList<String>(skus.size());
			for (SkuModel skuModel : skus) {
				skuResponses.add(skuModel.getContentName());
			}
		}
		return skuResponses;
	}


	private CategoryModelResponse buildCategoryModel(CategoryModel categoryModel) {
		if (categoryModel==null) {
			return null;
		}

		final CategoryModelResponse categoryResponse = new CategoryModelResponse();
		categoryResponse.setDepartmentId(categoryModel.getDepartment().getContentName());
		categoryResponse.setHavingBeer(categoryModel.isHavingBeer());

		categoryResponse.setTopLevelCategory(categoryModel.isTopLevelCategory());
		categoryResponse.setHideIfFilteringIsSupported(categoryModel.isHideIfFilteringIsSupported());
		categoryResponse.setPreferenceCategory(categoryModel.isPreferenceCategory());
		categoryResponse.setHavingBeer(categoryModel.isHavingBeer());
		
		categoryResponse.setShowSelf(categoryModel.isShowSelf());
		if (categoryModel.getAliasCategory() != null)
			categoryResponse.setAliasCategory(categoryModel.getAliasCategory().getContentName());
		categoryResponse.setCatMerchantRecommenderTitle(categoryModel.getCatMerchantRecommenderTitle());
		categoryResponse.setColumnSpan(categoryModel.getColumnSpan());
		categoryResponse.setContentTemplatePath(categoryModel.getContentTemplatePath());
		categoryResponse.setUseAlternateImages(categoryModel.isUseAlternateImages());
		categoryResponse.setSideNavLink(categoryModel.getSideNavLink());
		categoryResponse.setFakeAllFolder(categoryModel.getFakeAllFolders());
		categoryResponse.setSecondaryCategory(categoryModel.isSecondaryCategory());
		categoryResponse.setFeatured(categoryModel.isFeatured());
		categoryResponse.setHideFeaturedItems(categoryModel.isHideFeaturedItems());
		categoryResponse.setCatMerchantRecommenderRandomizeProducts(categoryModel.isCatMerchantRecommenderRandomizeProducts());
		categoryResponse.setContainsBeer(categoryModel.isHavingBeer());
		categoryResponse.setdYFAggregated(categoryModel.isDYFAggregated());
		categoryResponse.setDisableCategoryYmalRecommender(categoryModel.isDisableCategoryYmalRecommender());
		categoryResponse.setShowSideNav(categoryModel.isShowSideNav());
		categoryResponse.setSideNavBold(categoryModel.getSideNavBold());
		categoryResponse.setHideWineRating(categoryModel.isHideWineRatingPricing());
		
		categoryResponse.setMaterialCharacteristic(categoryModel.getMaterialCharacteristic());
		categoryResponse.setManualSelectionSlots(categoryModel.getManualSelectionSlots());
		categoryResponse.setProductPromotionType(categoryModel.getProductPromotionType());
		if (categoryModel.getShowChildren() != null)
			categoryResponse.setShowChildren(categoryModel.getShowChildren().getId());
		categoryResponse.setSideNavPriority(categoryModel.getSideNavPriority());
		categoryResponse.setTreatAsProduct(categoryModel.getTreatAsProduct());
		categoryResponse.setFeaturedNewProdBrands(createNewProdBrands(categoryModel.getFeaturedNewProdBrands()));
		categoryResponse.setFeaturedProducts(createProductListResponse(categoryModel.getFeaturedProducts()));
		categoryResponse.setPrivateProducts(createProductListResponse(categoryModel.getPrivateProducts()));
					if (categoryModel.getRatingHome() != null)
			categoryResponse.setRatingHome(categoryModel.getRatingHome().getContentName());
			categoryResponse.setShowAllByDefault(categoryModel.isShowAllByDefault());

		categoryResponse.setSubcategories(createCategoryListResponse(categoryModel.getSubcategories()));
		categoryResponse.setPopularCategories(createCategoryListResponse(categoryModel.getPopularCategories()));

		categoryResponse.setActive(categoryModel.isActive());
		if (categoryModel.getAliasAttributeValue() != null)
			categoryResponse.setAliasAttributeValue(categoryModel.getAliasAttributeValue().getId());
		categoryResponse.setHideWineRatingPricing(categoryModel.isHideWineRatingPricing());
		categoryResponse.setPageTitle(categoryModel.getPageTitle());
		categoryResponse.setPriority(categoryModel.getPriority());
		categoryResponse.setRedirectUrl(categoryModel.getRedirectUrl());
		categoryResponse.setRedirectUrlClean(categoryModel.getRedirectUrlClean());
		categoryResponse.setSeoMetaDescription(categoryModel.getSEOMetaDescription());

		categoryResponse.setFeaturedProductIds(categoryModel.getFeaturedProductIds());
		categoryResponse.setShowSideNav(categoryModel.isShowSideNav());
		categoryResponse.setFavoriteShowPrice(categoryModel.isFavoriteShowPrice());
		if (categoryModel.getDepartmentManagerBio() != null)
			categoryResponse.setDepartmentManagerBio(categoryModel.getDepartmentManagerBio().getPath());
		if (categoryModel.getTemplateType() != null)
			categoryResponse.setTemplateType(categoryModel.getTemplateType().intValue());
		categoryResponse.setRatingBreakOnSubfolders(categoryModel.isRatingBreakOnSubfolders());
		categoryResponse.setShowRatingRelatedImage(categoryModel.isShowRatingRelatedImage());
		categoryResponse.setRatingGroupNames(categoryModel.getRatingGroupNames());
		categoryResponse.setSeasonText(categoryModel.getSeasonText());
		categoryResponse.setNutritionSort(categoryModel.isNutritionSort());
		categoryResponse.setDefaultGrocerySort(categoryModel.getDefaultGrocerySort());
		if (categoryModel.getSideNavShowChildren() != null)
			categoryResponse.setSideNavShowChildren(categoryModel.getSideNavShowChildren().getId());
		categoryResponse.setColumnNum(categoryModel.getColumnNum());
		categoryResponse.setColumnSpan(categoryModel.getColumnSpan());
		categoryResponse.setHideIphone(categoryModel.isHideIphone());
		categoryResponse.setGlobalMenuTitleLabel(categoryModel.getGlobalMenuTitleLabel());
		categoryResponse.setGlobalMenuLinkLabel(categoryModel.getGlobalMenuLinkLabel());
		categoryResponse.setExcludedForEBTPayment(categoryModel.isExcludedForEBTPayment());
		categoryResponse.setNoGroupingByCategory(categoryModel.isNoGroupingByCategory());
		categoryResponse.setExpandSecondLowestNavigationBox(categoryModel.isExpandSecondLowestNavigationBox());
		categoryResponse.setShowPopularCategories(categoryModel.isShowPopularCategories());
		categoryResponse.setDisableCategoryYmalRecommender(categoryModel.isDisableCategoryYmalRecommender());

		LOGGER.debug("Ending BuildCategoryModel :: BackOfficeSerrvice");
		return categoryResponse;
	}


	public List<String> createCategoryListResponse(List<CategoryModel> selectedCategories) {
		List<String> categoryResponses = null;
		if (selectedCategories != null && !selectedCategories.isEmpty()) {
			categoryResponses = new ArrayList<String>(selectedCategories.size());
			for (CategoryModel categoryModel : selectedCategories) {
				categoryResponses.add(categoryModel.getContentName());
			}
		}
		return categoryResponses;
	}


	public List<String> createProductListResponse(List<ProductModel> featuredProducts) {
		List<String> productResponses = new ArrayList<String>(featuredProducts.size());
		for (ProductModel productModel : featuredProducts) {
			productResponses.add(productModel.getContentName());
		}
		return productResponses;
	}


	public List<String> createNewProdBrands(List featuredNewProdBrands) {
		List<String> productModelss = null;
		if (featuredNewProdBrands != null && !featuredNewProdBrands.isEmpty()) {
			productModelss = new ArrayList<String>(featuredNewProdBrands.size());
			for (Object contentNodeModel : featuredNewProdBrands) {
				if (contentNodeModel instanceof ProductModel) {
					ProductModel productModel = (ProductModel) contentNodeModel;
					productModelss.add(productModel.getContentName());
				} else if (contentNodeModel instanceof BrandModel) {
					BrandModel brandModel = (BrandModel) contentNodeModel;
					productModelss.add(brandModel.getContentName());
				}
			}
		}
		return productModelss;
	}


	private SkuModelResponse buildSkuProductModel(SkuModel skuModelOnly) {
    	SkuModelResponse skuModelResponse = new SkuModelResponse();
		ProductModelResponse productModelResponse = new ProductModelResponse();
		skuModelResponse.setProductModelResponse(productModelResponse);

		
		if (skuModelOnly==null) {
			return null;
		}
		LOGGER.debug("Beginning buildSkuProductModel :: ResponseConverter :: input - skuModel: " + skuModelOnly);
		skuModelResponse.setSkuName(skuModelOnly.getFullName());
		skuModelResponse.setContentKey(skuModelOnly.getContentKey().getId());
		skuModelResponse.setContentName(skuModelOnly.getContentName());
		skuModelResponse.setContentType(skuModelOnly.getContentType());
		skuModelResponse.setFullName(skuModelOnly.getFullName());
		skuModelResponse.setParentId(skuModelOnly.getParentId());
		skuModelResponse.setSkuCodeId(skuModelOnly.getSkuCode());
		skuModelResponse.setProductModelResponse(buildProductModel(skuModelOnly.getProductModel()));
		LOGGER.debug("Ending buildSkuProductModel :: ResponseConverter");

		return skuModelResponse;
	}


	public String getActionData(ActionDataRequest data, String key) {
        return (String) (data.getRequestData() != null ? data.getRequestData().get(key) : null);
	}
	public <T> T getActionObject(ActionDataRequest data, String key) {
        return  (T) (data.getRequestData() != null ? data.getRequestData().get(key) : null);
	}
	
    public Boolean getBoolean(ActionDataRequest data, String key) {
        return (Boolean) (data.getRequestData() != null ? data.getRequestData().get(key) : null);
    }


	public Map<String, String> getContentMap() {
		final Map<String, String> map = new HashMap<String, String>();
		Set<ContentKey> contentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.SUPER_DEPARTMENT);
		for (final ContentKey contentKey : contentKeys) {
			map.put(contentKey.getId(), contentKey.getType().getName());
		}
		contentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.DEPARTMENT);
		for (final ContentKey contentKey : contentKeys) {
			map.put(contentKey.getId(), contentKey.getType().getName());
		}
		contentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.CATEGORY);
		for (final ContentKey contentKey : contentKeys) {
			map.put(contentKey.getId(), contentKey.getType().getName());
		}
		contentKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCT);
		for (final ContentKey contentKey : contentKeys) {
			map.put(contentKey.getId(), contentKey.getType().getName());
		}
		return map;
	}
	public CRMSmartStoreInfoResponse getCRMSmartStoreInfo(FDUserI  context) {
		  final String INVALID_STATUS="invalid";
		  final String DUPLICATE_STATUS="duplicate";
		  final String OK_STATUS="OK";
		CRMSmartStoreInfoResponse crmSmartStoreInfoResponse = new CRMSmartStoreInfoResponse();
		List<VariantResponse> variantResponses = new ArrayList<VariantResponse>();
		
		List<String> ovariants = OverriddenVariantsHelper.getOverriddenVariantIds(context);
		OverriddenVariantsHelper.VariantInfoList vInfoList = OverriddenVariantsHelper.consolidateVariantsList(ovariants);
		List<EnumSiteFeature> enumSiteFeatures = EnumSiteFeature.getSmartStoreEnumList();
		if(enumSiteFeatures.size()>0){
			
		for (Iterator<EnumSiteFeature> it = EnumSiteFeature.getSmartStoreEnumList().iterator();
					it.hasNext();) {
				VariantResponse variantResponse = new VariantResponse();
				EnumSiteFeature feature = (EnumSiteFeature) it.next();
				
				OverriddenVariantsHelper.VariantInfo vi = vInfoList.get(feature);
				String variantId = null;
				if (vi != null) {
					variantId = vi.variant;
				} else {
					Variant variant = VariantSelectorFactory.getSelector(feature).getVariant(context.getCohortName());
					if (variant != null) {
						variantId = variant.getId();
					} else {
						variantId = "not assigned";
					}
				}
				variantResponse.setVariant(variantId);
				variantResponse.setFeatureName(feature.getName());
				boolean flag=false;
				String status = vi != null ? "overridden" : "";
				try {
					status = status + (EnumSiteFeature.DYF.equals(feature) && DYFUtil.isCustomerEligible(context) ? "eligible":"");
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				variantResponse.setOptions(status);
				variantResponses.add(variantResponse);
			}
		}
		
		List<OverriddenVariantsResponse> overriddenVariantsResponses = new ArrayList<OverriddenVariantsResponse>(); 
		
		for (Iterator it=vInfoList.iterator(); it.hasNext();) {
			OverriddenVariantsHelper.VariantInfo vi = (OverriddenVariantsHelper.VariantInfo) it.next();
			OverriddenVariantsResponse overriddenVariantsResponse  = new OverriddenVariantsResponse();
			overriddenVariantsResponse.setVariant(vi.variant);
			overriddenVariantsResponse.setFeatureName(vi.feature != null ? vi.feature.getName() : "-");
			if (!vi.exists) {
				overriddenVariantsResponse.setOptions(INVALID_STATUS);
			} else if (vi.duplicate) {
				overriddenVariantsResponse.setOptions(DUPLICATE_STATUS);
			} else {
				overriddenVariantsResponse.setOptions(OK_STATUS);
			}
			overriddenVariantsResponses.add(overriddenVariantsResponse);
		}
		
		crmSmartStoreInfoResponse.setOverriddenVariants(overriddenVariantsResponses);
		crmSmartStoreInfoResponse.setVariants(variantResponses);
		Collections.sort(variantResponses, new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return (((VariantResponse) o1).getFeatureName()).compareTo(((VariantResponse) o2).getFeatureName());
			}
			
		});
		crmSmartStoreInfoResponse.setCohortName(context.getCohortName());
		return crmSmartStoreInfoResponse;
	}


	public BackOfficeResponse submitGetRequest(FDUserI user,
			HttpServletRequest request, HttpServletResponse response) throws FDResourceException,
            IOException, JspException,  HttpErrorResponse {
		// TODO Auto-generated method stub
		return null;
	}

}
