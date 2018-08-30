package com.freshdirect.webapp.ajax.product;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;
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

    private static final Logger LOG = LoggerFactory.getInstance(ProductExtraDataPopulator.class);
    private static final java.text.DecimalFormat QUANTITY_FORMATTER = new java.text.DecimalFormat("0");
    private static final java.text.DecimalFormat TOTAL_FORMATTER = new java.text.DecimalFormat("0.00");
    private static final String POPUP_PAGE = "/shared/popup.jsp";

    public static ProductExtraData createExtraData(FDUserI user, ProductModel product, String grpId, String grpVersion, boolean includeProductAboutMedia)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
        return createExtraData(user, product, grpId, grpVersion, includeProductAboutMedia, null);
    }

    public static ProductExtraData createExtraData(FDUserI user, ProductModel product, String grpId, String grpVersion, boolean includeProductAboutMedia, String cssValue)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (product == null) {
            BaseJsonServlet.returnHttpError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "product not found");
        }

        ProductExtraData data = new ProductExtraData();
        populateData(data, user, product, grpId, grpVersion, includeProductAboutMedia, cssValue);

        return data;
    }

    public static ProductExtraData createExtraData(FDUserI user, String productId, String categoryId, String grpId, String grpVersion)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (productId == null) {
            BaseJsonServlet.returnHttpError(HttpStatus.SC_BAD_REQUEST, "productId not specified");
        }

        ProductModel product = PopulatorUtil.getProduct(productId, categoryId);

        return createExtraData(user, product, grpId, grpVersion, true, null);
    }

    public static ProductExtraData createLightExtraData(FDUserI user, ProductModel product) throws HttpErrorResponse {

        if (product == null) {
            BaseJsonServlet.returnHttpError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "product not found");
        }

        ProductExtraData data = new ProductExtraData();
        populateLightExtraData(data, user, product);

        return data;
    }

    public static ProductExtraData populateClaimsData(ProductExtraData data, ProductModel productNode) throws FDResourceException, FDSkuNotFoundException {
        List<String> claims = collectClaims(productNode);
        List<String> organicClaims = collectOrganicClaims(productNode);

        if (!organicClaims.isEmpty()) {
            data.setOrganicClaims(organicClaims);
        }

        if (!claims.isEmpty()) {
            data.setClaims(claims);
        }
        return data;
    }

    private static void populateLightExtraData(ProductExtraData data, FDUserI user, ProductModel product) {
        final DepartmentModel departmentNode = product.getDepartment();
        final String departmentFullName = departmentNode.getFullName();

        SkuModel defaultSku = null;
        try {
            defaultSku = fetchDefaultSkuForProductModel(product);
        } catch (FDSkuNotFoundException e) {
            LOG.debug("Could not find sku for product" + product.getContentKey(), e);
        }

        populateProductDescription(data, user, product.getProductDescription());
        populateSeasonTextAndServingSuggestions(data, product);
        populateCustomerServiceContact(data, user);
        populateDeliBuyingGuide(data, product);
        populateRelatedRecipes(data, product);
        if (defaultSku != null) {
            populateOriginData(data, product, defaultSku);
        }
        populateStorageGuideData(data, product, user, departmentNode);
        populateUsageList(data, product);
        populateFreshTips(data, product);
        populateHowToCookItFolders(data, product);
        populateDonenessGuides(data, product);
        populateCheeseData(data, departmentNode.getContentName());
        populatePartiallyFrozenBakeryHack(data, product, departmentFullName);
        populateProductMedias(data, product, user, false);
        populatePageTitle(data, product);
        populateWineData(data, user, product);

        try {
            populateWebRatings(data, product);
        } catch (FDResourceException e1) {
            LOG.debug("Couldn't populate web ratings for productExtraData light for product: " + product.getContentKey(), e1);
        }
    }

    private static void populateData(ProductExtraData data, FDUserI user, ProductModel productNode, String groupId, String groupVersion, boolean includeProductAboutMedia,
            String cssValue) throws FDResourceException, FDSkuNotFoundException {

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
        populateClaimsData(data, productNode);
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
        populateWineData(data, user, productNode);
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
            // if product is disc, then getDefSku returns null, but on PDP we need the products anyway, so get first sku
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

        if (containerWeightHalfPint != null) {
            Map<String, Double> buyerGuide = new HashMap<String, Double>();
            buyerGuide.put(ProductExtraData.KEY_CT_PINT05, containerWeightHalfPint);
            if (containerWeightPint != null) {
                buyerGuide.put(ProductExtraData.KEY_CT_PINT, containerWeightPint);
                if (containerWeightQuart != null) {
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
            final String recipeId = recipe.getContentName();
            final String recipeName = recipe.getName();

            RecipeData recipeData = new RecipeData(recipeId, recipeCategoryId, recipeName);
            recipeDatas.add(recipeData);
        }
        if (recipeDatas.size() > 0) {
            data.setRelatedRecipes(recipeDatas);
        }
    }

    private static void populateOriginData(ProductExtraData data, ProductModel productNode, SkuModel defaultSku) {
        if (defaultSku.getVariationMatrix() != null && defaultSku.getVariationMatrix().size() > 0) {
            Map<String, SourceData> sourceToName = new HashMap<String, SourceData>();

            // media
            Html fdDefGradeMedia = productNode.getFddefGrade();
            Html fdDefSourceMedia = productNode.getFddefSource();

            TitledMedia titleMedia = null;
            for (DomainValue domainValue : defaultSku.getVariationMatrix()) {
                Domain domain = domainValue.getDomain();
                final String domainName = domain.getContentName();

                if ("grade".equalsIgnoreCase(domainName)) {
                    titleMedia = (TitledMedia) fdDefGradeMedia;
                } else if ("source".equalsIgnoreCase(domainName)) {
                    titleMedia = (TitledMedia) fdDefSourceMedia;
                } else {
                    continue;
                }

                SourceData sourceData = new SourceData();
                sourceData.label = domainValue.getValue();
                if (titleMedia != null) {
                    sourceData.path = titleMedia.getPath();
                }

                sourceToName.put(domainName, sourceData);
            }

            if (sourceToName.size() > 0) {
                data.setSource(sourceToName);
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
        final int maxBrandsToShow = isWineLayout ? 1 : 2;

        @SuppressWarnings("unchecked")
        List<BrandModel> productBrands = productNode.getDisplayableBrands(maxBrandsToShow);

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
                if (skuCode != null && skuCode.startsWith("MEA")) {
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
            TitledMedia titleMedia = (TitledMedia) productNode.getProductDescriptionNote();
            try {
                data.setProductDescriptionNote(fetchMedia(titleMedia.getPath(), user, false));
            } catch (IOException e) {
                LOG.error("Failed to fetch product description note media " + titleMedia.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch product description note media " + titleMedia.getPath(), e);
            }
        }

    }

    private static void populateProductAboutMedia(ProductExtraData data, ProductModel productNode, FDUserI user, boolean includeProductAboutMedia) {
        if (productNode.getProductAbout() != null) {
            TitledMedia titleMedia = (TitledMedia) productNode.getProductAbout();
            try {
                if (includeProductAboutMedia) {
                    data.setProductAboutMedia(fetchMedia(titleMedia.getPath(), user, false));
                }
                data.setProductAboutMediaPath(titleMedia.getPath());
            } catch (IOException e) {
                LOG.error("Failed to fetch product about media " + titleMedia.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch product about media " + titleMedia.getPath(), e);
            }
        }

    }

    private static void populateProductTermsMedia(ProductExtraData data, ProductModel productNode, FDUserI user) {
        if (productNode.getProductTerms() != null) {
            TitledMedia titleMedia = (TitledMedia) productNode.getProductTerms();
            try {
                data.setProductTermsMedia(fetchMedia(titleMedia.getPath(), user, false));
            } catch (IOException e) {
                LOG.error("Failed to fetch product term media " + titleMedia.getPath(), e);
            } catch (TemplateException e) {
                LOG.error("Failed to fetch product term media " + titleMedia.getPath(), e);
            }
        }
    }

    private static void populateAllergenData(ProductExtraData data, ProductModel productNode) throws FDResourceException {
        @SuppressWarnings("unchecked")
        Set<EnumAllergenValue> common = productNode.getCommonNutritionInfo(ErpNutritionInfoType.ALLERGEN);
        if (!common.isEmpty()) {
            List<String> allergens = new ArrayList<String>(common.size());
            for (EnumAllergenValue allergen : common) {
                if (!EnumAllergenValue.getValueForCode("NONE").equals(allergen)) {
                    allergens.add(allergen.toString());
                }
            }
            data.setAllergens(allergens);
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
        GroupScaleData groupScaleData = new GroupScaleData(); // make sure this gets added, even if it's all nulls

        if (groupId != null && !"".equals(groupId)) {
            groupScaleData.grpId = groupId;
            groupScaleData.version = groupVersion;

            try {
                List<String> skus = null;
                final String prioritySku = defaultSku.getSkuCode();
                List<ProductModel> productModels = new ArrayList<ProductModel>();

                if (groupId != null && !"".equals(groupId)) {
                    FDGroup group = null;

                    if (groupVersion == null || (groupVersion != null && "".equals(groupVersion.trim()))) {
                        group = GroupScaleUtil.getLatestActiveGroup(groupId);
                        groupScaleData.version = Integer.toString(group.getVersion());
                    } else {
                        group = new FDGroup(groupId, Integer.parseInt(groupVersion));
                    }

                    MaterialPrice materialPrice = GroupScaleUtil.getGroupScalePrice(group, user.getUserContext().getPricingContext().getZoneInfo());

                    if (materialPrice != null) {
                        String groupQuantity = "0";
                        String groupTotalPrice = "0";
                        boolean saleUnitDifferent = false;
                        double displayPrice = 0.0;

                        if (materialPrice.getPricingUnit().equals(materialPrice.getScaleUnit())) {
                            displayPrice = materialPrice.getPrice() * materialPrice.getScaleLowerBound();
                        } else {
                            displayPrice = materialPrice.getPrice();
                            saleUnitDifferent = true;
                        }

                        groupQuantity = QUANTITY_FORMATTER.format(materialPrice.getScaleLowerBound());

                        if (materialPrice.getScaleUnit().equals("LB")) {
                            groupQuantity = groupQuantity + (materialPrice.getScaleUnit().toLowerCase());
                        }
                        groupTotalPrice = "$" + TOTAL_FORMATTER.format(displayPrice);

                        if (saleUnitDifferent) {
                            groupTotalPrice = groupTotalPrice + "/" + (materialPrice.getPricingUnit().toLowerCase());
                        }

                        GroupScalePricing groupScalePricing = GroupScaleUtil.lookupGroupPricing(group);

                        groupScaleData.grpQty = groupQuantity;
                        groupScaleData.grpTotalPrice = groupTotalPrice;
                        groupScaleData.grpShortDesc = groupScalePricing.getShortDesc();
                        groupScaleData.grpLongDesc = groupScalePricing.getLongDesc();

                        skus = groupScalePricing.getSkuList();
                    }
                }

                if (skus == null) {
                    LOG.info("skuList is empty for groupId" + groupId + " " + groupVersion);
                }

                if (skus != null) {
                    for (String currentSku : skus) {
                        if (prioritySku != null && prioritySku.equals(currentSku)) {
                            continue;
                        }
                        try {
                            ProductModel productForSku = ContentFactory.getInstance().getProduct(currentSku);
                            if (productForSku != null) {
                                productModels.add(productForSku);
                            }
                        } catch (FDSkuNotFoundException se) {
                            // Ignore this sku. move to next
                            LOG.debug("Sku not found: " + currentSku, se);
                        }
                    }
                }
                groupScaleData.groupProducts = processProductModels(productModels, user);

            } catch (FDResourceException fe) {
                LOG.warn("Resource not found", fe);
            } catch (FDGroupNotFoundException e) {
                LOG.warn("Group not found!", e);
            }
        }
        data.setGroupScaleData(groupScaleData);
    }

    private static void populateComponentGroupsAndOptionalProducts(ProductExtraData data, ProductModel productNode, FDUserI user, FDProductInfo productInfo)
            throws FDResourceException {
        List<ComponentGroupModel> componentGroups = productNode.getComponentGroups();
        List<ProductModel> optionalProducts = new ArrayList<ProductModel>();

        for (ComponentGroupModel componentGroup : componentGroups) {
            if ((componentGroup.isUnavailable() && !(ContentFactory.getInstance().getPreviewMode())) || (componentGroup.isShowInPopupOnly())) {
                continue;
            }
            if (componentGroup.isShowOptions()) {
                optionalProducts = componentGroup.getOptionalProducts();
            }
        }

        data.setOptionalProducts(processProductModels(optionalProducts, user));
    }

    private static void populateFamilyProducts(ProductExtraData data, ProductModel productNode, FDUserI user, FDProductInfo productInfo, SkuModel defaultSku)
            throws FDResourceException, FDSkuNotFoundException {

        if (FDStoreProperties.isProductFamilyEnabled() && EnumEStoreId.FDX != CmsManager.getInstance().getEStoreEnum()) {
            List<FilteringProductItem> filteringProductItems = new ArrayList<FilteringProductItem>();

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
                for (String skuCode : skuCodes) {
                    if (selectedProdSku.equalsIgnoreCase(skuCode)) {
                        continue;
                    }
                    ProductModel productModel = PopulatorUtil.getProduct(skuCode);
                    FilteringProductItem filteringProductItem = new FilteringProductItem(productModel);
                    filteringProductItems.add(filteringProductItem);
                }

                Comparator<FilteringProductItem> comparator = ProductItemSorterFactory.createComparator(SortStrategyType.toEnum("POPULARITY"), user, true);
                Collections.sort(filteringProductItems, comparator);
            }

            data.setFamilyProducts(processFilteringProductItems(filteringProductItems, user));
        }
    }

    private static List<ProductData> processFilteringProductItems(List<FilteringProductItem> filteringProductItems, FDUserI user) throws FDResourceException {
        List<ProductData> resultData = new ArrayList<ProductData>();
        for (FilteringProductItem filteringProductItem : filteringProductItems) {
            ProductModel productModel = filteringProductItem.getProductModel();
            ProductData productData = processProductModel(productModel, user);
            if (productData != null) {
                resultData.add(productData);
            }
        }
        return resultData;
    }

    private static List<ProductData> processProductModels(List<ProductModel> productModels, FDUserI user) throws FDResourceException {
        List<ProductData> result = new ArrayList<ProductData>();
        for (ProductModel productModelToProcess : productModels) {
            ProductData processed = processProductModel(productModelToProcess, user);
            if (processed != null) {
                result.add(processed);
            }
        }
        return result;
    }

    private static ProductData processProductModel(ProductModel productModel, FDUserI user) throws FDResourceException {
        ProductData resultProductData = null;
        SkuModel skuModel = null;
        ProductModel productModelToWorkWith = productModel;
        if (!(productModel instanceof ProductModelPricingAdapter)) {
            productModelToWorkWith = ProductPricingFactory.getInstance().getPricingAdapter(productModel);
        }

        if (skuModel == null) {
            skuModel = productModelToWorkWith.getDefaultSku();
        }

        if (skuModel != null) {
            resultProductData = new ProductData();
            String skuCode = skuModel.getSkuCode();
            try {
                FDProductInfo productInfoForPricing = skuModel.getProductInfo();
                FDProduct fdProduct = skuModel.getProduct();

                PriceCalculator priceCalculator = productModelToWorkWith.getPriceCalculator();
                ProductDetailPopulator.populateBasicProductData(resultProductData, user, productModelToWorkWith);
                ProductDetailPopulator.populateProductData(resultProductData, user, productModelToWorkWith, skuModel, fdProduct, priceCalculator, null, true, true);
                ProductDetailPopulator.populatePricing(resultProductData, fdProduct, productInfoForPricing, priceCalculator, user);

                try {
                    ProductDetailPopulator.populateSkuData(resultProductData, user, productModelToWorkWith, skuModel, fdProduct);
                } catch (FDSkuNotFoundException e) {
                    LOG.error("Failed to populate sku data", e);
                } catch (HttpErrorResponse e) {
                    LOG.error("Failed to populate sku data", e);
                }

                ProductDetailPopulator.postProcessPopulate(user, resultProductData, resultProductData.getSkuCode());

            } catch (FDSkuNotFoundException e) {
                LOG.warn("Sku not found: " + skuCode, e);
            }
        }

        return resultProductData;
    }

    public static ProductExtraData populateWineData(ProductExtraData data, FDUserI user, ProductModel productNode) {
        WineData wineData = new WineData();

        DomainValue wineCountry = productNode.getWineCountry();
        List<DomainValue> wineRegion = productNode.getNewWineRegion();
        String wineCity = productNode.getWineCity();
        if (wineCity.trim().length() == 0) {
            wineCity = null;
        }
        List<DomainValue> wineVintageList = productNode.getWineVintage();
        DomainValue wineVintage = wineVintageList.size() > 0 ? wineVintageList.get(0) : null;
        if (wineVintage != null && "vintage_nv".equals(wineVintage.getContentKey().getId())) {
            wineVintage = null;
        }

        List<DomainValue> wineTypes = productNode.getNewWineType();
        List<String> typeValues = new ArrayList<String>();
        List<String> wineTypeIconPaths = new ArrayList<String>();
        for (DomainValue wineTypeValue : wineTypes) {
            final String type = wineTypeValue.getValue();
            typeValues.add(type);
            wineTypeIconPaths.add("/media/editorial/win_" + WineUtil.getWineAssociateId().toLowerCase() + "/icons/" + wineTypeValue.getContentName().toLowerCase() + ".gif");
        }
        wineData.types = typeValues;
        wineData.typeIconPaths = wineTypeIconPaths;

        List<String> varietals = new ArrayList<String>();
        for (DomainValue wvv : productNode.getWineVarietal()) {
            varietals.add(wvv.getValue());
        }
        wineData.varietals = varietals;

        if (wineCountry != null) {
            wineData.country = wineCountry.getValue();
        }
        if (!wineRegion.isEmpty()) {
            wineData.region = wineRegion.get(0).getLabel();
        }
        if (wineCity != null) {
            wineData.city = wineCity;
        }
        if (wineVintage != null) {
            wineData.vintage = wineVintage.getValue();
        }

        wineData.classification = productNode.getWineClassification();

        // grape type / blending
        wineData.grape = productNode.getWineType();

        wineData.importer = productNode.getWineImporter();

        wineData.agingNotes = productNode.getWineAging();

        wineData.alcoholGrade = productNode.getWineAlchoholContent();

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
            wineData.ratings = ratings;
        }

        data.setWineData(wineData);
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
        if (wineRatingDomainValues == null || wineRatingDomainValues.size() == 0) {
            return null;
        }
        WineRating wineRating = new WineRating();
        DomainValue domainValue = wineRatingDomainValues.get(0);
        wineRating.reviewer = domainValue.getDomainName();
        wineRating.rating = domainValue.getValue();
        wineRating.ratingKey = domainValue.getContentName();
        wineRating.iconPath = "/media/editorial/win_" + WineUtil.getWineAssociateId().toLowerCase() + "/icons/rating_small/" + domainValue.getContentName() + ".gif";

        if (reviewMedia != null) {
            TitledMedia tm = (TitledMedia) reviewMedia;
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
        if (mediaPath == null) {
            return null;
        }

        Map<String, Object> parameters = new HashMap<String, Object>();

        /* pass user/sessionUser by default, so it doesn't need to be added every place this tag is used. */
        parameters.put("user", user);
        parameters.put("sessionUser", user);

        StringWriter out = new StringWriter();

        MediaUtils.render(mediaPath, out, parameters, false,
                user != null && user.getUserContext().getPricingContext() != null ? user.getUserContext().getPricingContext() : PricingContext.DEFAULT);

        String outString = out.toString();

        // fix media if needed
        outString = MediaUtils.fixMedia(outString);

        return quoted ? JSONObject.quote(outString) : outString;
    }

    private static List<String> collectOrganicClaims(ProductModel productNode) throws FDResourceException {
        @SuppressWarnings("unchecked")
        Set<EnumOrganicValue> organicNutritionInfos = productNode.getCommonNutritionInfo(ErpNutritionInfoType.ORGANIC);
        List<String> organicClaims = new ArrayList<String>(organicNutritionInfos.size());
        for (EnumOrganicValue claim : organicNutritionInfos) {
            if (!EnumOrganicValue.getValueForCode("NONE").equals(claim)) {
                if (EnumOrganicValue.getValueForCode("CERT_ORGN").equals(claim)) {
                    organicClaims.add("Organic");
                } else {
                    if (!"".equals(claim.getName())) {
                        organicClaims.add(claim.getName());
                    }
                }
            }
        }
        return organicClaims;
    }

    private static List<String> collectClaims(ProductModel productNode) throws FDResourceException {
        @SuppressWarnings("unchecked")
        Set<EnumClaimValue> claimNutritionInfos = productNode.getCommonNutritionInfo(ErpNutritionInfoType.CLAIM);
        List<String> claims = new ArrayList<String>(claimNutritionInfos.size());
        for (EnumClaimValue claim : claimNutritionInfos) {
            if (!EnumClaimValue.getValueForCode("NONE").equals(claim) && !EnumClaimValue.getValueForCode("OAN").equals(claim)) {
                if (EnumClaimValue.getValueForCode("FR_ANTI").equals(claim)) {
                    claims.add("Raised Without Antibiotics");
                } else {
                    claims.add(claim.toString());
                }
            }
        }
        return claims;
    }
}
