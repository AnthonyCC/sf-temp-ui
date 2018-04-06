package com.freshdirect.webapp.ajax.product;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.freshdirect.WineUtil;
import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.cache.CmsCaches;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.content.nutrition.EnumAllergenValue;
import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.content.nutrition.EnumOrganicValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDNutritionPanelCache;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.content.browse.sorter.ProductItemSorterFactory;
import com.freshdirect.fdstore.content.view.WebHowToCookIt;
import com.freshdirect.fdstore.content.view.WebProductRating;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.HowToCookItUtil;
import com.freshdirect.fdstore.util.RatingUtil;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.AbstractProductModelImpl;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ComponentGroupModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.Domain;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.EnumProductLayout;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.Html;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.MediaI;
import com.freshdirect.storeapi.content.MediaModel;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.storeapi.content.SortStrategyType;
import com.freshdirect.storeapi.content.TitledMedia;
import com.freshdirect.storeapi.util.ProductInfoUtil;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.BrandInfo;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.GroupScaleData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.LabelAndLink;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.RecipeData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.SourceData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.WineData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData.WineRating;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.NutritionInfoPanelRendererUtil;

public class ProductExtraDataPopulator {
	private static final Logger LOG = LoggerFactory.getInstance( ProductExtraDataPopulator.class );
	private static final java.text.DecimalFormat QTY_FORMATTER = new java.text.DecimalFormat("0");
	private static final java.text.DecimalFormat TOTAL_FORMATTER = new java.text.DecimalFormat("0.00");
    private static final String POPUP_PAGE = "/shared/popup.jsp";
	
	public static ProductExtraData createExtraData( FDUserI user, ProductModel product, String grpId, String grpVersion, boolean includeProductAboutMedia)
			throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
	return  createExtraData( user,  product, grpId, grpVersion, includeProductAboutMedia, null);
	}
	//appdev 6259 nutrition panel from soy required including a css to go with it to build the page/viewport.

	public static ProductExtraData createExtraData( FDUserI user, ProductModel product, String grpId, String grpVersion, boolean includeProductAboutMedia, String cssValue) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( product == null ) {
			BaseJsonServlet.returnHttpError( 500, "product not found" );
		}
		
		// Create response data object
		ProductExtraData data = new ProductExtraData();
		
		// First populate product-level data
		populateData( data, user, product, grpId, grpVersion, includeProductAboutMedia, cssValue );
		
		return data;
	}
	
	public static ProductExtraData createExtraData( FDUserI user, String productId, String categoryId, String grpId, String grpVersion ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( productId == null ) {
			BaseJsonServlet.returnHttpError( 400, "productId not specified" );	// 400 Bad Request
		}
	
		// Get the ProductModel
		ProductModel product = PopulatorUtil.getProduct( productId, categoryId );
		
		return createExtraData( user, product, grpId, grpVersion, true, null );
	}

    public static ProductExtraData createLightExtraData(FDUserI user, ProductModel product) throws HttpErrorResponse {

        if (product == null) {
            BaseJsonServlet.returnHttpError(500, "product not found");
        }

        // Create response data object
        ProductExtraData data = new ProductExtraData();

        // First populate product-level data
        populateLightExtraData(data, user, product);

        return data;
    }

    private static void populateLightExtraData(ProductExtraData data, FDUserI user, ProductModel product) {
        populateProductDescription(data, user, product.getProductDescription());
    }
	
    private static void populateData(ProductExtraData data, FDUserI user, ProductModel productNode, String groupId, String groupVersion, boolean includeProductAboutMedia, String cssValue)
            throws FDResourceException, FDSkuNotFoundException {

        final DepartmentModel departmentNode = productNode.getDepartment();
        final String departmentName = departmentNode.getContentName();
        final String departmentFullName = departmentNode.getFullName();
        final SkuModel defaultSku = fetchDefaultSkuForProductModel(productNode);
        final FDProductInfo fdProductInfo = fetchFDProductInfoForSkuModel(defaultSku);
        final FDProduct fdProduct = fetchFDProductForFDProductInfo(fdProductInfo);
        final String plantID = ProductInfoUtil.getPickingPlantId(fdProductInfo);

        populateAllergenData(data, productNode);
        populateProductDescription(data, user, productNode.getProductDescription());
        populateProductMedias(data, productNode, user, includeProductAboutMedia);
        populateClaimsData(data, user, productNode, groupId, groupVersion);
        populateProductKosherData(data, productNode);
        populateHeatingInstructions(data, fdProduct);
        populatePartiallyFrozenBakeryHack(data, productNode, departmentFullName);
        populateCountryOfOriginData(data, fdProductInfo, plantID);
        populateDeliBuyingGuide(data, productNode);
        populateCheeseData(data, departmentName);
        populateProductBrands(data, productNode, defaultSku, fdProductInfo);
        populateNutritions(data, defaultSku, cssValue, fdProduct);
        populateDonenessGuides(data, productNode);
        populateHowToCookItFolders(data, productNode);
        populateFreshTips(data, productNode);
        populateWebRatings(data, productNode);
        populateUsageList(data, productNode);
        populateStorageGuideData(data, productNode, user, departmentNode);
        populateOriginData(data, productNode, defaultSku);
        populateRelatedRecipes(data, productNode);

        if (departmentNode != null) {
            populateWineData(data, user, productNode);
        }
        populatePerishableProductData(data, fdProductInfo, plantID);
        populateGroupScaleData(data, productNode, user, fdProductInfo, groupId, groupVersion, defaultSku);
        populateComponentGroupsAndOptionalProducts(data, productNode, user, fdProductInfo);
        populateFamilyProducts(data, productNode, user, fdProductInfo, defaultSku);
        populateSeasonTextAndServingSuggestions(data, productNode);
        populatePageTitle(data, productNode);
        populateCustomerServiceContact(data, user);
    }

    private static FDProductInfo fetchFDProductInfoForSkuModel(SkuModel skuModel) {
        FDProductInfo productInfo = null;
        if (skuModel != null) {
            try {
                productInfo = FDCachedFactory.getProductInfo(skuModel.getSkuCode());
            } catch (FDResourceException e) {
                LOG.debug(e);
            } catch (FDSkuNotFoundException e) {
                LOG.debug(e);
            }   
        }
        return productInfo;
    }

    private static FDProduct fetchFDProductForFDProductInfo(FDProductInfo productInfo) {
        FDProduct fdProduct = null;
        try {
            fdProduct = FDCachedFactory.getProduct(productInfo);
        } catch (FDResourceException e) {
            LOG.debug(e);
        } catch (FDSkuNotFoundException e) {
            LOG.debug(e);
        }
        return fdProduct;
    }

    private static SkuModel fetchDefaultSkuForProductModel(ProductModel productNode) throws FDSkuNotFoundException {
        SkuModel defaultSku = PopulatorUtil.getDefSku(productNode);
        try {
            // if product is disc, then getDefSku returns null, but on PDP we need the prods anyway, so get first sku
            if (defaultSku == null && productNode.getSkuCodes().size() > 0) {
                defaultSku = productNode.getSku(0);
            }
        } catch (Exception e) {
            LOG.warn("Exception while populating defaultSku: ", e);
        }

        if (defaultSku == null) {
            throw new FDSkuNotFoundException("No default SKU found for product " + productNode.getContentName());
        }

        return defaultSku;
    }

    private static void populateSeasonTextAndServingSuggestions(ProductExtraData data, ProductModel productNode) {
        data.setSeasonText(productNode.getSeasonText());
        data.setServingSuggestions(productNode.getServingSuggestion());
    }

    private static void populateCustomerServiceContact(ProductExtraData data, FDUserI user) {
        data.setCustomerServiceContact(user.getCustomerServiceContact());
    }

    private static void populateDeliBuyingGuide(ProductExtraData data, ProductModel productNode) {
        final Double containerWeightHalfPint = productNode.getContainerWeightHalfPint();
        final Double containerWeightPint = productNode.getContainerWeightPint();
        final Double containerWeightQuart = productNode.getContainerWeightQuart();

        if ((containerWeightHalfPint != null)) {
            Map<String, Double> buyerGuide = new HashMap<String, Double>();
            buyerGuide.put(ProductExtraData.KEY_CT_PINT05, containerWeightHalfPint);
            if ((containerWeightPint != null)) {
                buyerGuide.put(ProductExtraData.KEY_CT_PINT, containerWeightPint);
                if ((containerWeightQuart != null)) {
                    buyerGuide.put(ProductExtraData.KEY_CT_QUART, containerWeightQuart);
                }
            }
            data.setBuyerGuide(buyerGuide);
        }
    }

    private static void populatePerishableProductData(ProductExtraData data, FDProductInfo productInfo, String plantID) {
        if (FDStoreProperties.IsFreshnessGuaranteedEnabled() && productInfo.getFreshness(plantID) != null) {
            data.setFreshness(Integer.parseInt(productInfo.getFreshness(plantID)));
        }
    }

    private static void populateRelatedRecipes(ProductExtraData data, ProductModel productNode) {
        final String recipeCategoryId = productNode.getParentId();
        List<RecipeData> recipeDatas = new ArrayList<RecipeData>();
        for (Recipe recipe : productNode.getRelatedRecipes()) {
            if (recipe.isAvailable()) {
                final String recipeId = recipe.getContentName();
                final String recipeName = recipe.getName();

                RecipeData recipeData = new RecipeData(recipeId, recipeCategoryId, recipeName);
                recipeDatas.add(recipeData);
            }
        }
        if (recipeDatas.size() > 0) {
            data.setRelatedRecipes(recipeDatas);
        }
    }

    private static void populateOriginData(ProductExtraData data, ProductModel productNode, SkuModel defaultSku) {
        if (defaultSku.getVariationMatrix() != null && defaultSku.getVariationMatrix().size() > 0) {
            Map<String, SourceData> map = new HashMap<String, SourceData>();

            // media
            Html _fdDefGrade = productNode.getFddefGrade();
            Html _fdDefSource = productNode.getFddefSource();

            TitledMedia tm = null;
            for (DomainValue dv : defaultSku.getVariationMatrix()) {
                Domain d = dv.getDomain();
                final String dName = d.getContentName();

                if ("grade".equalsIgnoreCase(dName)) {
                    tm = (TitledMedia) _fdDefGrade;
                } else if ("source".equalsIgnoreCase(dName)) {
                    tm = (TitledMedia) _fdDefSource;
                } else {
                    continue;
                }

                SourceData sd = new SourceData();
                sd.label = dv.getValue();
                if (tm != null) {
                    sd.path = tm.getPath();
                }

                map.put(dName, sd);
            }

            if (map.size() > 0) {
                data.setSource(map);
            }
        }
    }

    private static void populateStorageGuideData(ProductExtraData data, ProductModel productNode, FDUserI user, DepartmentModel department) {
        CategoryModel parentCategory = productNode.getCategory();
        MediaModel categoryStorage = parentCategory.getCategoryStorageGuideMedia();
        MediaModel departmentStorage = department.getDeptStorageGuideMedia();

        if (parentCategory != null && categoryStorage != null) {
            data.setCategoryStorageGuideLabel(department.getFullName() + " Storage Guide");
            data.setCategoryStorageGuideLink(POPUP_PAGE + "?catId=" + parentCategory.getContentName() + "&attrib=CAT_STORAGE_GUIDE_MEDIA&tmpl=large");

            TitledMedia tm = (TitledMedia) parentCategory.getCategoryStorageGuideMedia();
            try {
                data.setCategoryStorageGuide(fetchMedia(tm.getPath(), user, false));
            } catch (IOException e) {
                LOG.error("Failed to fetch media " + tm.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch media " + tm.getPath(), e);
            }
        }

        if (categoryStorage != null) {
            data.setStorageGuideCat(categoryStorage.getPath());
        }
        if (departmentStorage != null) {
            data.setStorageGuideDept(departmentStorage.getPath());
        }
        data.setStorageGuideTitle(department.getFullName() + " Storage Guide");
    }

    private static void populateUsageList(ProductExtraData data, ProductModel productNode) {
        if (productNode.getUsageList() != null && productNode.getUsageList().size() > 0) {
            List<String> usageList = new ArrayList<String>();
            for (Domain domain : productNode.getUsageList()) {
                usageList.add(domain.getLabel());
            }
            data.setUsageList(usageList);
        }
    }

    private static void populateWebRatings(ProductExtraData data, ProductModel productNode) throws FDResourceException {
        WebProductRating webProductRating = RatingUtil.getRatings(productNode);
        data.setWebRating(webProductRating);
    }

    private static void populateFreshTips(ProductExtraData data, ProductModel productNode) {
        final Html freshTipsMedia = productNode.getFreshTips();
        if (freshTipsMedia != null) {
            LabelAndLink labelAndLink = new LabelAndLink((TitledMedia) freshTipsMedia);
            data.setFreshTips(labelAndLink);
        }
    }

    private static void populateHowToCookItFolders(ProductExtraData data, ProductModel productNode) {
        List<WebHowToCookIt> howToCookItResult = HowToCookItUtil.getHowToCookIt(productNode);
        data.setHowToCookItList(howToCookItResult);
    }

    private static void populateDonenessGuides(ProductExtraData data, ProductModel productNode) {
        List<Html> donenessGuides = productNode.getDonenessGuide();
        if (donenessGuides != null && donenessGuides.size() > 0) {
            List<LabelAndLink> labelAndLinks = new ArrayList<LabelAndLink>();
            for (Html media : donenessGuides) {
                LabelAndLink labelAndLink = new LabelAndLink((TitledMedia) media);
                labelAndLinks.add(labelAndLink);
            }
            data.setDonenessGuideList(labelAndLinks);
        }
    }

    private static void populateNutritions(ProductExtraData data, SkuModel defaultSku, String cssValue, FDProduct fdProduct) {
        final String skuCode = defaultSku.getSkuCode();
        NutritionPanel nutritionPanel = FDNutritionPanelCache.getInstance().getNutritionPanel(skuCode);

        if (cssValue != null && !cssValue.isEmpty()) {
            nutritionPanel.setViewportCss(cssValue);
        }

        if (nutritionPanel != null) {
            data.setNutritionPanel(nutritionPanel);
        } else if (fdProduct != null && fdProduct.hasNutritionFacts()) {
            // old style

            ErpNutritionModel nutritionModel = FDNutritionCache.getInstance().getNutrition(defaultSku.getSkuCode());

            if (nutritionModel != null) {
                try {
                    StringWriter wr = new StringWriter();
                    if (NutritionInfoPanelRendererUtil.renderClassicPanel(nutritionModel, false, wr)) {
                        data.setOldNutritionPanel(wr.toString());
                    }
                } catch (IOException e) {
                    LOG.error("Failed to render old nutrition panel", e);
                }
            }

        } else {
            // generate classic nutrition panel
            LOG.warn("Not found new nutrition info data for SKU " + skuCode);
        }

        if (fdProduct != null) {
            data.setIngredients(fdProduct.getIngredients());
        }
    }

    private static void populateProductBrands(ProductExtraData data, ProductModel productNode, SkuModel defaultSku, FDProductInfo productInfo) {
            final boolean isWineLayout = EnumProductLayout.NEW_WINE_PRODUCT.equals(productNode.getProductLayout());
            final int MAX_BRANDS_TO_SHOW = isWineLayout ? 1 : 2;

            @SuppressWarnings("unchecked")
            List<BrandModel> productBrands = productNode.getDisplayableBrands(MAX_BRANDS_TO_SHOW);

            if (defaultSku != null && FDStoreProperties.isSeafoodSustainEnabled()) {
                EnumSustainabilityRating enumRating = productInfo.getSustainabilityRating(ProductInfoUtil.getPickingPlantId(productInfo));
                if (enumRating != null) {
                    if (enumRating != null && enumRating.isEligibleToDisplay() && (enumRating.getId() == 4 || enumRating.getId() == 5)) {
                        ContentNodeModel ssBrandCheck = ContentFactory.getInstance().getContentNode("bd_ocean_friendly");
                        if (ssBrandCheck instanceof BrandModel) {
                            productBrands.add((BrandModel) ssBrandCheck);
                        }
                    }
                }
            }

            List<BrandInfo> brandInfos = new ArrayList<BrandInfo>(productBrands.size());
            for (final BrandModel brandModel : productBrands) {
                Html brandAttrib = brandModel.getPopupContent();

                BrandInfo brandInfo = new BrandInfo();
                brandInfo.id = brandModel.getContentName();
                brandInfo.name = brandModel.getFullName();
                brandInfo.alt = brandModel.getAltText();

                Image blogo = brandModel.getLogoSmall();
                if (blogo != null) {
                    brandInfo.logoPath = blogo.getPath();
                    brandInfo.logoWidth = blogo.getWidth();
                    brandInfo.logoHeight = blogo.getHeight();
                }
                if (brandAttrib != null) {
                    brandInfo.contentPath = "/shared/brandpop.jsp?brandId=" + brandModel.getContentKey().getId();
                }
                brandInfos.add(brandInfo);
            }
            data.setBrands(brandInfos);
    }

    private static void populateCheeseData(ProductExtraData data, String departmentName) {
        if ("CHE".equalsIgnoreCase(departmentName)) {
            data.setCheese101(true);
            data.setCheeseText("Learn the essentials &#151; from buying to serving");
            data.setCheesePopupPath("/departments/cheese/101_selecting.jsp");
        } else {
            data.setCheese101(false);
        }
    }

    private static void populateCountryOfOriginData(ProductExtraData data, FDProductInfo productInfo, String plantID) {
        // origin (or Country of Origin Label, a.k.a. COOL)
        if (productInfo != null) {
            final String skuCode = productInfo.getSkuCode();
            final List<String> coolList = productInfo.getCountryOfOrigin(plantID);
            if (coolList != null && !coolList.isEmpty()) {
                if (skuCode != null && (skuCode).startsWith("MEA")) {
                    data.setOriginTitle("Born, Raised and Harvested in");
                } else {
                    data.setOriginTitle("Origin");
                }
            }
        }
        if (productInfo != null) {
            final List<String> coolList = productInfo.getCountryOfOrigin(plantID);
            if (coolList != null && !coolList.isEmpty()) {
                data.setOrigin(AbstractProductModelImpl.getCOOLText(coolList));
            }
        }
    }

    private static void populatePartiallyFrozenBakeryHack(ProductExtraData data, ProductModel productNode, String departmentFullName) {
        // Deprecated due to APPBUG-1705, preserved because you never know ...
        if (productNode.isHasPartiallyFrozen()) {
            if ("SEAFOOD".equalsIgnoreCase(departmentFullName)) {
                // seafood department
                data.setFrozenSeafood(true);
            } else if ("BAKERY".equalsIgnoreCase(departmentFullName)) {
                // bakery dept
                data.setFrozenBakery(true);
            }

        }
    }

    private static void populateHeatingInstructions(ProductExtraData data, FDProduct fdProduct) {
        if (fdProduct != null && fdProduct.hasNutritionInfo(ErpNutritionInfoType.HEATING)) {
            data.setHeatingInstructions(fdProduct.getNutritionInfoString(ErpNutritionInfoType.HEATING));
        }
    }

    private static void populateProductKosherData(ProductExtraData data, ProductModel productNode) throws FDResourceException {
        PriceCalculator priceCalculator = productNode.getPriceCalculator();

        final String kosherType = priceCalculator.getKosherType();
        final String kosherSymbol = priceCalculator.getKosherSymbol();

        if (!"".equalsIgnoreCase(kosherType)) {
            data.setKosherType(kosherType);
        }
        if (!"".equalsIgnoreCase(kosherSymbol)) {
            data.setKosherSymbol(kosherSymbol);
            data.setKosherIconPath("/media/editorial/kosher/symbols/" + kosherSymbol.toLowerCase() + "_s.gif");
            data.setKosherPopupPath(POPUP_PAGE + "?attrib=KOSHER&amp;spec=" + kosherSymbol.toLowerCase() + "&amp;tmpl=small");
        }
    }

    private static void populateProductMedias(ProductExtraData data, ProductModel productNode, FDUserI user, boolean includeProductAboutMedia) {
        populateProductDescriptionMedia(data, productNode, user);
        populateProductAboutMedia(data, productNode, user, includeProductAboutMedia);
        populateProductTermsMedia(data, productNode, user);
        populatePartiallyFrozenMedia(data, productNode, user);
    }

    private static void populatePartiallyFrozenMedia(ProductExtraData data, ProductModel productNode, FDUserI user) {
        Html frozenMedia = productNode.getPartallyFrozen();
        if (frozenMedia != null) {
            try {
                data.setPartiallyFrozenMedia(fetchMedia(frozenMedia.getPath(), user, false));
                data.setFrozen(true);
            } catch (IOException e) {
                LOG.error("Failed to fetch partially frozen media " + frozenMedia.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch partially frozen media " + frozenMedia.getPath(), e);
            }
        }
    }

    private static void populateProductDescriptionMedia(ProductExtraData data, ProductModel productNode, FDUserI user) {
        if (productNode.getProductDescriptionNote() != null) {
            TitledMedia tm = (TitledMedia) productNode.getProductDescriptionNote();
            try {
                data.setProductDescriptionNote(fetchMedia(tm.getPath(), user, false));
            } catch (IOException e) {
                LOG.error("Failed to fetch product description note media " + tm.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch product description note media " + tm.getPath(), e);
            }
        }

    }

    private static void populateProductAboutMedia(ProductExtraData data, ProductModel productNode, FDUserI user, boolean includeProductAboutMedia) {
        if (productNode.getProductAbout() != null) {
            TitledMedia tm = (TitledMedia) productNode.getProductAbout();
            try {
                if (includeProductAboutMedia) {
                    data.setProductAboutMedia(fetchMedia(tm.getPath(), user, false));
                }
                data.setProductAboutMediaPath(tm.getPath());
            } catch (IOException e) {
                LOG.error("Failed to fetch product about media " + tm.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch product about media " + tm.getPath(), e);
            }
        }

    }

    private static void populateProductTermsMedia(ProductExtraData data, ProductModel productNode, FDUserI user) {
        if (productNode.getProductTerms() != null) {
            TitledMedia tm = (TitledMedia) productNode.getProductTerms();
            try {
                data.setProductTermsMedia(fetchMedia(tm.getPath(), user, false));
            } catch (IOException e) {
                LOG.error("Failed to fetch product term media " + tm.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch product term media " + tm.getPath(), e);
            }
        }
    }

    private static void populateAllergenData(ProductExtraData data, ProductModel productNode) throws FDResourceException {
        @SuppressWarnings("unchecked")
        Set<EnumAllergenValue> common = productNode.getCommonNutritionInfo(ErpNutritionInfoType.ALLERGEN);
        if (!common.isEmpty()) {
            List<String> aList = new ArrayList<String>(common.size());
            for (EnumAllergenValue allergen : common) {
                if (!EnumAllergenValue.getValueForCode("NONE").equals(allergen)) {
                    aList.add(allergen.toString());
                }
            }
            data.setAllergens(aList);
        }
    }

    private static void populatePageTitle(ProductExtraData data, ProductModel productNode) {
        if (EnumEStoreId.FDX == CmsManager.getInstance().getEStoreEnum()) {
            data.setPageTitle(productNode.getFdxPageTitle());
            data.setSeoMetaDescription(productNode.getFdxSEOMetaDescription());
        } else {
            data.setPageTitle(productNode.getPageTitle());
            data.setSeoMetaDescription(productNode.getSEOMetaDescription());
        }
    }

    private static void populateGroupScaleData(ProductExtraData data, ProductModel productNode, FDUserI user, FDProductInfo productInfo, String groupId, String groupVersion,
            SkuModel defaultSku) {
        GroupScaleData gsData = new GroupScaleData(); // make sure this gets added, even if it's all nulls

        if (groupId != null && !"".equals(groupId)) {

            List<ProductData> groupProductsList = new ArrayList<ProductData>();

            /* set initial values */
            gsData.grpId = groupId;
            gsData.version = groupVersion;

            try {

                List<String> skuList = null; /* we'll use this to get the other products */
                final String prioritySku = defaultSku.getSkuCode(); /* this is the sku for the product we're starting with, we skip adding it to the product list */
                List<ProductModel> productModelList = new ArrayList<ProductModel>();

                if (groupId != null && !"".equals(groupId)) {
                    FDGroup group = null;

                    if (groupVersion == null || (groupVersion != null && "".equals(groupVersion.trim()))) {
                        group = GroupScaleUtil.getLatestActiveGroup(groupId);
                        gsData.version = Integer.toString(group.getVersion());
                    } else {
                        group = new FDGroup(groupId, Integer.parseInt(groupVersion));
                    }

                    MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group, user.getUserContext().getPricingContext().getZoneInfo());

                    if (matPrice != null) {
                        String grpQty = "0";
                        String grpTotalPrice = "0";
                        boolean isSaleUnitDiff = false;
                        double displayPrice = 0.0;

                        if (matPrice.getPricingUnit().equals(matPrice.getScaleUnit()))
                            displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
                        else {
                            displayPrice = matPrice.getPrice();
                            isSaleUnitDiff = true;
                        }

                        grpQty = QTY_FORMATTER.format(matPrice.getScaleLowerBound());

                        if (matPrice.getScaleUnit().equals("LB"))// Other than eaches append the /pricing unit for clarity.
                            grpQty = grpQty + (matPrice.getScaleUnit().toLowerCase());

                        grpTotalPrice = "$" + TOTAL_FORMATTER.format(displayPrice);

                        if (isSaleUnitDiff)
                            grpTotalPrice = grpTotalPrice + "/" + (matPrice.getPricingUnit().toLowerCase());

                        GroupScalePricing gsp = GroupScaleUtil.lookupGroupPricing(group);

                        gsData.grpQty = grpQty;
                        gsData.grpTotalPrice = grpTotalPrice;
                        gsData.grpShortDesc = gsp.getShortDesc();
                        gsData.grpLongDesc = gsp.getLongDesc();

                        skuList = gsp.getSkuList();
                    }
                }

                if (skuList == null) {
                    LOG.info("skuList is empty for groupId" + groupId + " " + groupVersion);
                }

                if (skuList != null) {
                    /* iterate over list of skus to get productModels */
                    Iterator<String> skuListIt = skuList.iterator();
                    while (skuListIt.hasNext()) {
                        String curSku = skuListIt.next();
                        if (prioritySku != null && prioritySku.equals(curSku))
                            // as it is already processed
                            continue;
                        try {
                            ProductModel pm = ContentFactory.getInstance().getProduct(curSku);
                            if (pm != null) {
                                productModelList.add(pm);
                            }
                        } catch (FDSkuNotFoundException se) {
                            // Ignore this sku. move to next
                        }

                    }
                }
                /* now iterate over productModels to get ProductDatas */
                Iterator<ProductModel> productModelListIt = productModelList.iterator();
                while (productModelListIt.hasNext()) {
                    ProductModel curPm = productModelListIt.next();
                    ProductData pd = new ProductData();
                    SkuModel skuModel = null;

                    if (!(curPm instanceof ProductModelPricingAdapter)) {
                        // wrap it into a pricing adapter if naked
                        curPm = ProductPricingFactory.getInstance().getPricingAdapter(curPm, user.getUserContext().getPricingContext());
                    }

                    if (skuModel == null) {
                        skuModel = curPm.getDefaultSku();
                    }

                    if (skuModel == null) {
                        LOG.warn("Default Sku not found for product: " + curPm);
                        continue;
                    }

                    String skuCode = skuModel.getSkuCode();

                    try {
                        FDProductInfo productInfo_fam = skuModel.getProductInfo();
                        FDProduct fdProduct = skuModel.getProduct();

                        PriceCalculator priceCalculator = curPm.getPriceCalculator();

                        ProductDetailPopulator.populateBasicProductData(pd, user, curPm);
                        ProductDetailPopulator.populateProductData(pd, user, curPm, skuModel, fdProduct, priceCalculator, null, true, true);
                        ProductDetailPopulator.populatePricing(pd, fdProduct, productInfo_fam, priceCalculator, user);

                        try {
                            ProductDetailPopulator.populateSkuData(pd, user, curPm, skuModel, fdProduct);
                        } catch (FDSkuNotFoundException e) {
                            LOG.error("Failed to populate sku data", e);
                        } catch (HttpErrorResponse e) {
                            LOG.error("Failed to populate sku data", e);
                        }

                        ProductDetailPopulator.postProcessPopulate(user, pd, pd.getSkuCode());

                    } catch (FDSkuNotFoundException e) {
                        LOG.warn("Sku not found: " + skuCode, e);
                    }

                    groupProductsList.add(pd);

                }

                /* and finally set to Data */
                gsData.groupProducts = groupProductsList;

            } catch (FDResourceException fe) {
                // throw new JspException(fe);
            } catch (FDGroupNotFoundException e) {
                // TODO Auto-generated catch block
                // throw new JspException(e);
            }

        }
        data.setGroupScaleData(gsData);
    }

    private static void populateComponentGroupsAndOptionalProducts(ProductExtraData data, ProductModel productNode, FDUserI user, FDProductInfo productInfo)
            throws FDResourceException {
        List<ComponentGroupModel> componentGroups = productNode.getComponentGroups();
        List<ProductData> optProducts = new ArrayList<ProductData>();
        List<FilteringProductItem> optProdModels = new ArrayList<FilteringProductItem>();

        int compGrpIdx = 0;
        for (Iterator<ComponentGroupModel> cgItr = componentGroups.iterator(); cgItr.hasNext(); compGrpIdx++) {
            ComponentGroupModel compGroup = cgItr.next();

            if (compGroup.isUnavailable() && !(ContentFactory.getInstance().getPreviewMode()))
                continue;

            if (compGroup.isShowInPopupOnly())
                continue;

            if (compGroup.isShowOptions()) {
                for (ProductModel pm : compGroup.getOptionalProducts()) {
                    FilteringProductItem fpt = new FilteringProductItem(pm);
                    optProdModels.add(fpt);
                }

                for (FilteringProductItem fpt : optProdModels) {

                    ProductModel productModel = fpt.getProductModel();

                    ProductData pd = new ProductData();
                    SkuModel skuModel = null;

                    if (!(productModel instanceof ProductModelPricingAdapter)) {
                        productModel = ProductPricingFactory.getInstance().getPricingAdapter(productModel, user.getUserContext().getPricingContext());
                    }

                    if (skuModel == null) {
                        skuModel = productModel.getDefaultSku();
                    }

                    try {
                        if (skuModel == null) {
                            continue;
                        }

                        FDProductInfo productInfo_fam = skuModel.getProductInfo();
                        FDProduct fdProduct = skuModel.getProduct();

                        PriceCalculator priceCalculator = productModel.getPriceCalculator();

                        ProductDetailPopulator.populateBasicProductData(pd, user, productModel);

                        ProductDetailPopulator.populateProductData(pd, user, productModel, skuModel, fdProduct, priceCalculator, null, true, true);
                        ProductDetailPopulator.populatePricing(pd, fdProduct, productInfo_fam, priceCalculator, user);

                        try {
                            ProductDetailPopulator.populateSkuData(pd, user, productModel, skuModel, fdProduct);
                        } catch (FDSkuNotFoundException e) {
                            LOG.error("Failed to populate sku data", e);
                        } catch (HttpErrorResponse e) {
                            LOG.error("Failed to populate sku data", e);
                        }

                        ProductDetailPopulator.postProcessPopulate(user, pd, pd.getSkuCode());

                    } catch (FDSkuNotFoundException e) {
                        LOG.warn("Sku not found: " + pd.getSkuCode(), e);
                    }
                    optProducts.add(pd);
                }
            }
        }

        data.setOptionalProducts(optProducts);
    }

    // TODO: this could be even smaller - if you see this todo during review, I forgot to come back here - please open an issue
    private static void populateFamilyProducts(ProductExtraData data, ProductModel productNode, FDUserI user, FDProductInfo productInfo, SkuModel defaultSku)
            throws FDResourceException, FDSkuNotFoundException {

        if (FDStoreProperties.isProductFamilyEnabled() && EnumEStoreId.FDX != CmsManager.getInstance().getEStoreEnum()) {
            List<ProductData> familyProducts = new ArrayList<ProductData>();
            List<FilteringProductItem> modelList = new ArrayList<FilteringProductItem>();

            String selectedProdSku = defaultSku.getSkuCode();

            String familyID = productInfo.getFamilyID();
            ErpProductFamilyModel products = null;
            List<String> skuCodes = null;
            if (familyID == null && productInfo.getMaterialNumber() != null) {
                try {
                    products = FDFactory.getSkuFamilyInfo(productInfo.getMaterialNumber());
                } catch (FDGroupNotFoundException e) {
                    LOG.warn("No product family group exists for material: " + productInfo, e);
                }
                familyID = products.getFamilyId();
                if (familyID != null) {
                    CmsServiceLocator.ehCacheUtil().putListToCache(CmsCaches.FD_FAMILY_PRODUCT_CACHE.cacheName, familyID, products.getSkuList());
                }
            }
            if (null != familyID) {
                skuCodes = CmsServiceLocator.ehCacheUtil().getListFromCache(CmsCaches.FD_FAMILY_PRODUCT_CACHE.cacheName, familyID);
            }

            if (skuCodes == null && familyID != null) {
                try {
                    products = FDFactory.getFamilyInfo(familyID);
                } catch (FDGroupNotFoundException e) {
                    LOG.warn("No product family group exists for material: " + productInfo, e);
                }
                skuCodes = products.getSkuList();
                CmsServiceLocator.ehCacheUtil().putListToCache(CmsCaches.FD_FAMILY_PRODUCT_CACHE.cacheName, familyID, products.getSkuList());
            }

            if (skuCodes != null && selectedProdSku != null) {
                duplicateSku: for (String skuCode : skuCodes) {

                    if (selectedProdSku.equalsIgnoreCase(skuCode)) {
                        continue duplicateSku;
                    }

                    ProductModel productModel = PopulatorUtil.getProduct(skuCode);
                    FilteringProductItem fpt = new FilteringProductItem(productModel);
                    modelList.add(fpt);

                }

                Comparator<FilteringProductItem> comparator = ProductItemSorterFactory.createComparator(SortStrategyType.toEnum("POPULARITY"), user, true);
                Collections.sort(modelList, comparator);

                for (FilteringProductItem fpt : modelList) {

                    ProductModel productModel = fpt.getProductModel();
                    ProductData pd = new ProductData();
                    SkuModel skuModel = null;

                    if (!(productModel instanceof ProductModelPricingAdapter)) {
                        productModel = ProductPricingFactory.getInstance().getPricingAdapter(productModel, user.getUserContext().getPricingContext());
                    }

                    if (skuModel == null) {
                        skuModel = productModel.getDefaultSku();
                    }

                    try {
                        if (skuModel == null) {
                            continue;
                        }

                        FDProductInfo productInfo_fam = skuModel.getProductInfo();
                        FDProduct fdProduct = skuModel.getProduct();

                        PriceCalculator priceCalculator = productModel.getPriceCalculator();

                        ProductDetailPopulator.populateBasicProductData(pd, user, productModel);

                        ProductDetailPopulator.populateProductData(pd, user, productModel, skuModel, fdProduct, priceCalculator, null, true, true);
                        ProductDetailPopulator.populatePricing(pd, fdProduct, productInfo_fam, priceCalculator, user);

                        try {
                            ProductDetailPopulator.populateSkuData(pd, user, productModel, skuModel, fdProduct);
                        } catch (FDSkuNotFoundException e) {
                            LOG.error("Failed to populate sku data", e);
                        } catch (HttpErrorResponse e) {
                            LOG.error("Failed to populate sku data", e);
                        }

                        ProductDetailPopulator.postProcessPopulate(user, pd, pd.getSkuCode());

                    } catch (FDSkuNotFoundException e) {
                        LOG.warn("Sku not found: " + pd.getSkuCode(), e);
                    }

                    familyProducts.add(pd);

                }
            }
            data.setFamilyProducts(familyProducts);
        }
    }

	public static ProductExtraData populateWineData(ProductExtraData data, FDUserI user,
			ProductModel productNode) {
		WineData wd = new WineData();

		/** code snipped from  WineRegionLabel#doStart method */
		DomainValue wineCountry = productNode.getWineCountry();
		List<DomainValue> wineRegion = productNode.getNewWineRegion();
		String wineCity = productNode.getWineCity();
		if (wineCity.trim().length() == 0)
			wineCity = null;
		List<DomainValue> wineVintageList = productNode.getWineVintage();
		DomainValue wineVintage = wineVintageList.size() > 0 ? wineVintageList.get(0) : null;
		if (wineVintage != null && "vintage_nv".equals(wineVintage.getContentKey().getId())) {
			wineVintage = null;
		}

		List<DomainValue> wTypes = productNode.getNewWineType();
		List<String> typesList = new ArrayList<String>();
		List<String> wTypeIconPaths = new ArrayList<String>();
		for (DomainValue wtv : wTypes) {
			final String type = wtv.getValue();
			typesList.add(type);
			wTypeIconPaths.add("/media/editorial/win_"+WineUtil.getWineAssociateId().toLowerCase()+"/icons/"+wtv.getContentName().toLowerCase()+".gif");
		}
		wd.types = typesList;
		wd.typeIconPaths = wTypeIconPaths;
		
		List<String> varietals = new ArrayList<String>();
		for (DomainValue wvv : productNode.getWineVarietal()) {
			varietals.add(wvv.getValue());
		}
		wd.varietals = varietals;
		
		if (wineCountry != null) {
			wd.country = wineCountry.getValue();
		}
		if (!wineRegion.isEmpty()) {
			wd.region = wineRegion.get(0).getLabel();
		}
		if (wineCity != null) {
			wd.city = wineCity;
		}
		if (wineVintage != null) {
			wd.vintage = wineVintage.getValue();
		}

		wd.classification = productNode.getWineClassification();

		// grape type / blending
		wd.grape = productNode.getWineType();

		wd.importer = productNode.getWineImporter();
		
		wd.agingNotes = productNode.getWineAging();
		
		wd.alcoholGrade = productNode.getWineAlchoholContent();

		// reviews
		List<WineRating> ratings = new ArrayList<WineRating>();
		if (productNode.getWineRating1() != null && productNode.getWineRating1().size() > 0) {
			WineRating r = processWineRating(productNode.getWineRating1(), user, productNode.getWineReview1());
			if (r != null) {
				ratings.add(r);
			}
		}
		if (productNode.getWineRating2() != null && productNode.getWineRating2().size() > 0) {
			WineRating r = processWineRating(productNode.getWineRating2(), user, productNode.getWineReview2());
			if (r != null) {
				ratings.add(r);
			}
		}
		if (productNode.getWineRating3() != null && productNode.getWineRating3().size() > 0) {
			WineRating r = processWineRating(productNode.getWineRating3(), user, productNode.getWineReview3());
			if (r != null) {
				ratings.add(r);
			}
		}

		if (ratings.size() > 0) {
			wd.ratings = ratings;
		}

		if (productNode.hasWineOtherRatings()) {
			// ??
		}
		
		data.setWineData(wd);
		return data;
	}

    private static void populateProductDescription(ProductExtraData data, FDUserI user, MediaI media) {
        String productDescription = null;
        if (media != null) {
            try {
                productDescription = fetchMedia(media.getPath(), user, false);
            } catch (IOException e) {
                LOG.error("Failed to fetch product description media " + media.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch product description media " + media.getPath(), e);
            }
        }
        data.setProductDescription(productDescription);
    }


	private static WineRating processWineRating(List<DomainValue> wineRatingDomainValues, FDUserI user, Html reviewMedia) {
		if (wineRatingDomainValues == null || wineRatingDomainValues.size() == 0)
			return null;
		
		WineRating wineRating = new WineRating();
		// pick only the first rating
		DomainValue domainValue = wineRatingDomainValues.get(0);
		wineRating.reviewer = domainValue.getDomainName();
		wineRating.rating = domainValue.getValue();
		wineRating.ratingKey = domainValue.getContentName();
		wineRating.iconPath = "/media/editorial/win_"+WineUtil.getWineAssociateId().toLowerCase()+"/icons/rating_small/" + domainValue.getContentName() + ".gif";

		if (reviewMedia != null) {
			TitledMedia tm = (TitledMedia)reviewMedia;
			try {
				wineRating.media = fetchMedia(tm.getPath(), user, false);
			} catch (IOException e) {
				LOG.error("Failed to fetch wine rating media " + tm.getPath(), e);
			} catch (TemplateException e) {
				LOG.error("Failed to fetch wine rating media " + tm.getPath(), e);
			}
		}
		
		return wineRating;
	}

	private static String fetchMedia(String mediaPath, FDUserI user, boolean quoted) throws IOException, TemplateException {
		if (mediaPath == null)
			return null;

		Map<String,Object> parameters = new HashMap<String,Object>();
		
		/* pass user/sessionUser by default, so it doesn't need to be added every place this tag is used. */
		parameters.put("user", user);
		parameters.put("sessionUser", user);
		
		StringWriter out = new StringWriter();
				
		MediaUtils.render(mediaPath, out, parameters, false, 
				user != null && user.getUserContext().getPricingContext() != null ? user.getUserContext().getPricingContext() : PricingContext.DEFAULT);

		String outString = out.toString();
		
		//fix media if needed
		outString = MediaUtils.fixMedia(outString);
		
		return quoted ? JSONObject.quote( outString ) : outString;
	}
	
	public static  ProductExtraData populateClaimsDataForMobile(ProductExtraData data, FDUserI user,
			ProductModel productNode, String grpId, String grpVersion) throws FDResourceException, FDSkuNotFoundException {
			return 	populateClaimsData(data, user, productNode, grpId, grpVersion);
	}
	
	private static ProductExtraData populateClaimsData(ProductExtraData data, FDUserI user,
			ProductModel productNode, String grpId, String grpVersion) throws FDResourceException, FDSkuNotFoundException {
		
		// organic claims
		{
			@SuppressWarnings("unchecked")
			Set<EnumOrganicValue> commonOrgs = productNode.getCommonNutritionInfo(ErpNutritionInfoType.ORGANIC);
			if (!commonOrgs.isEmpty()) {
				List<String> aList = new ArrayList<String>(commonOrgs.size());
				for (EnumOrganicValue claim : commonOrgs) {
					if(!EnumOrganicValue.getValueForCode("NONE").equals(claim)) {
						//Changed for APPDEV-705

						//check for different text than what Enum has (Enum shows in ERPSy-Daisy)
						if(EnumOrganicValue.getValueForCode("CERT_ORGN").equals(claim)){
							// %><div>&bull; Organic</div><%
							aList.add("Organic");
						}else{
							//don't use empty
							if ( !"".equals(claim.getName()) ) {
								// %><div>&bull; <%= claim.getName() %></div><%
								aList.add(claim.getName());
							}
						}
					}
				}

				data.setOrganicClaims(aList);
			}
		}


		// claims
		{
			@SuppressWarnings("unchecked")
			Set<EnumClaimValue> common = productNode.getCommonNutritionInfo(ErpNutritionInfoType.CLAIM);
			if (!common.isEmpty()) {
				List<String> aList = new ArrayList<String>(common.size());
				for (EnumClaimValue claim : common) {
					if (!EnumClaimValue.getValueForCode("NONE").equals(claim) && !EnumClaimValue.getValueForCode("OAN").equals(claim)) {
						//Changed for APPDEV-705

						//check for different text than what Enum has (Enum shows in ERPSy-Daisy)
						if(EnumClaimValue.getValueForCode("FR_ANTI").equals(claim)){
							// %><div>&bull; Raised Without Antibiotics</div><%
							aList.add("Raised Without Antibiotics");
						}else{
							// %><div style="margin-left:8px; text-indent: -8px;">&bull; <%= claim %></div><%
							aList.add(claim.toString());
						}
					}
				}
				data.setClaims(aList);
			}
		}
		return data;
	}

}
