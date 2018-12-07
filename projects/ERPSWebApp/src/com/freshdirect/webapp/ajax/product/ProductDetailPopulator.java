package com.freshdirect.webapp.ajax.product;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.freshdirect.WineUtil;
import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsContext;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsDTO;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.util.DYFUtil;
import com.freshdirect.fdstore.util.UnitPriceUtil;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.storeapi.content.ComponentGroupModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ContentNodeModelUtil;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductReference;
import com.freshdirect.storeapi.content.ProductReferenceImpl;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.storeapi.util.ProductInfoUtil;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.cart.CartOperations;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.holidaymealbundle.service.HolidayMealBundleService;
import com.freshdirect.webapp.ajax.mealkit.service.MealkitService;
import com.freshdirect.webapp.ajax.product.data.BasicProductData;
import com.freshdirect.webapp.ajax.product.data.CartLineData;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.VarItem;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Variation;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.SkuData;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.taglib.fdstore.display.ProductSavingTag;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.ProductRecommenderUtil;
import com.freshdirect.webapp.util.RestrictionUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class ProductDetailPopulator {

    private static final Logger LOG = LoggerFactory.getInstance(ProductDetailPopulator.class);

    /**
     * Create generic product data from product & category ID-s
     *
     * Mainly for potato diggers.
     *
     * @param user
     * @param productId
     * @param categoryId
     * @return
     * @throws HttpErrorResponse
     * @throws FDResourceException
     * @throws FDSkuNotFoundException
     */
    public static ProductData createProductData(FDUserI user, String productId, String categoryId) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (productId == null) {
            BaseJsonServlet.returnHttpError(400, "productId not specified"); // 400 Bad Request
        }

        // Get the ProductModel
        ProductModel product = PopulatorUtil.getProduct(productId, categoryId);

        return createProductData(user, product);
    }

    public static CartLineData createCartLineData(FDUserI user, FDCartLineI cartLine) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (cartLine == null) {
            BaseJsonServlet.returnHttpError(400, "missing cartline"); // 400 Bad Request
        }

        CartLineData cartLineData = new CartLineData();
        cartLineData.setRandomId(cartLine.getRandomId());
        cartLineData.setProduct(createProductData(user, cartLine, false));
        cartLineData.setPrice(JspMethods.formatPrice(cartLine.getPrice()));
        String label = cartLine.getLabel();
        if (!"".equals(label)) {
            label = " " + label;
        }
        cartLineData.setQuantity(new DecimalFormat("0.##").format(cartLine.getQuantity()) + label);
        cartLineData.setDescription(cartLine.getDescription());
        cartLineData.setConfigurationDescription(cartLine.getConfigurationDesc());

        ContentNodeModel recipe = ContentFactory.getInstance().getContentNode(cartLine.getRecipeSourceId());
        if (recipe != null) {
            cartLineData.setRecipeName(recipe.getFullName());
        }

        return cartLineData;
    }

    /**
     * Create generic product data from a cartline object
     *
     * Mainly for potato diggers.
     *
     * @param user
     * @param cartLine
     * @return
     * @throws HttpErrorResponse
     * @throws FDResourceException
     * @throws FDSkuNotFoundException
     */
    public static ProductData createProductData(FDUserI user, FDCartLineI cartLine, boolean showCouponStatus)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (cartLine == null) {
            BaseJsonServlet.returnHttpError(400, "missing cartline"); // 400 Bad Request
        }

        // Note : these are product & category IDs !!!
        String productId = cartLine.getProductName();
        String categoryId = cartLine.getCategoryName();

        // Get the models
        ProductModel product = PopulatorUtil.getProduct(productId, categoryId);
        SkuModel sku = product.getSku(cartLine.getSkuCode());

        return createProductData(user, product, sku, cartLine, showCouponStatus);
    }

    /**
     * Create generic product data from ProductModel and SkuModel
     *
     * Mainly for potato diggers.
     *
     * @param user
     * @param product
     * @return
     * @throws HttpErrorResponse
     * @throws FDResourceException
     * @throws FDSkuNotFoundException
     */
    public static ProductData createProductData(FDUserI user, ProductModel product, SkuModel sku, FDProductSelectionI lineData, boolean showCouponStatus)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (product == null) {
            BaseJsonServlet.returnHttpError(500, "product not found");
        }

        if (sku == null) {
            BaseJsonServlet.returnHttpError(500, "sku not found");
        }

        if (!(product instanceof ProductModelPricingAdapter)) {
            // wrap it into a pricing adapter if naked
            product = ProductPricingFactory.getInstance().getPricingAdapter(product);
        }

        // Create response data object
        ProductData data = new ProductData();

        populateDetailProductData(user, product, sku, lineData, showCouponStatus, data);

        return data;
    }

    private static void populateDetailProductData(FDUserI user, ProductModel product, SkuModel sku, FDProductSelectionI lineData, boolean showCouponStatus, ProductData data)
            throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse {
        FDProductInfo productInfo = sku.getProductInfo();
        if (productInfo == null) {
            BaseJsonServlet.returnHttpError(500, "productInfo does not exist for this product");
        }

        FDProduct fdProduct = sku.getProduct();
        if (fdProduct == null) {
            BaseJsonServlet.returnHttpError(500, "fdProduct does not exist for this product");
        }

        PriceCalculator priceCalculator = product.getPriceCalculator();
        if (priceCalculator == null) {
            BaseJsonServlet.returnHttpError(500, "priceCalculator does not exist for this product");
        }

        if (lineData == null) {
            lineData = new FDProductSelection(fdProduct, product, getProductConfiguration(product, fdProduct), user.getUserContext());
            try {
                lineData.refreshConfiguration();
            } catch (FDInvalidConfigurationException e) {
                LOG.warn("Invalid configuration" + e.getMessage());
            }
        }
        // Populate product basic-level data
        populateBasicProductData(data, user, product);

        // Populate product level data
        populateProductData(data, user, product, sku, fdProduct, priceCalculator, lineData, true, false);

        // Populate pricing data
        populatePricing(data, fdProduct, productInfo, priceCalculator, user);

        // Populate sku-level data for the default sku only
        populateSkuData(data, user, product, sku, fdProduct);

        // Populate MealKit specific properties
        MealkitService.defaultService().populateData(data, product);

        // Populate transient-data
        postProcessPopulate(user, data, sku.getSkuCode(), showCouponStatus, lineData);
    }

    /**
     * Carousel Product -- APPDEV-4251
     *
     * @param user
     * @param product
     * @return
     * @throws HttpErrorResponse
     * @throws FDResourceException
     * @throws FDSkuNotFoundException
     */
    public static ProductData createProductDataForCarousel(FDUserI user, ProductModel product, SkuModel sku, FDProductSelectionI lineData, boolean showCouponStatus)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (product == null) {
            BaseJsonServlet.returnHttpError(500, "product not found");
        }

        if (sku == null) {
            BaseJsonServlet.returnHttpError(500, "sku not found");
        }

        if (!(product instanceof ProductModelPricingAdapter)) {
            // wrap it into a pricing adapter if naked
            product = ProductPricingFactory.getInstance().getPricingAdapter(product);
        }

        FDProductInfo productInfo = sku.getProductInfo();
        if (productInfo == null) {
            BaseJsonServlet.returnHttpError(500, "productInfo does not exist for this product");
        }

        FDProduct fdProduct = sku.getProduct();
        if (fdProduct == null) {
            BaseJsonServlet.returnHttpError(500, "fdProduct does not exist for this product");
        }

        PriceCalculator priceCalculator = product.getPriceCalculator();
        if (priceCalculator == null) {
            BaseJsonServlet.returnHttpError(500, "priceCalculator does not exist for this product");
        }

        if (lineData == null) {
            lineData = new FDProductSelection(fdProduct, product, getProductConfiguration(product, fdProduct), user.getUserContext());
            try {
                lineData.refreshConfiguration();
            } catch (FDInvalidConfigurationException e) {
                LOG.warn("Invalid configuration" + e.getMessage());
            }
        }

        // Create response data object
        ProductData data = new ProductData();

        // Populate product basic-level data
        populateBasicProductData(data, user, product);

        // Populate product level data
        populateProductData(data, user, product, sku, fdProduct, priceCalculator, lineData, false, false);

        // Populate sku-level data for the default sku only
        populateSkuData(data, user, product, sku, fdProduct);

        // Populate transient-data
        postProcessPopulate(user, data, sku.getSkuCode(), showCouponStatus, lineData);

        return data;
    }

    /**
     * Create generic product data from ProductModel using default sku
     *
     * Mainly for potato diggers.
     *
     * Will populate pricing data based on default sku!
     *
     * @param user
     * @param product
     * @return
     * @throws HttpErrorResponse
     * @throws FDResourceException
     * @throws FDSkuNotFoundException
     */
    public static ProductData createProductData(FDUserI user, ProductModel product) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
        return createProductData(user, product, FDStoreProperties.getPreviewMode());
    }

    public static ProductData createProductData(FDUserI user, ProductModel product, boolean enableProductIncomplete)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (product == null) {
            BaseJsonServlet.returnHttpError(500, "product not found");
        }

        if (!(product instanceof ProductModelPricingAdapter)) {
            // wrap it into a pricing adapter if naked
            product = ProductPricingFactory.getInstance().getPricingAdapter(product);
        }

        if (enableProductIncomplete && PopulatorUtil.isProductIncomplete(product) && !PopulatorUtil.isNodeArchived(product)) {
            return createProductDataLight(user, product);
        }

        SkuModel sku = PopulatorUtil.getDefSku(product);
        if (sku == null) {
            BaseJsonServlet.returnHttpError(500, "default sku does not exist for this product: " + product.getContentName());
        }

        return createProductData(user, product, sku, null, false);
    }

    // APPDEV-4251
    public static ProductData createProductDataForCarousel(FDUserI user, ProductModel product, boolean enableProductIncomplete)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (product == null) {
            BaseJsonServlet.returnHttpError(500, "product not found");
        }

        if (!(product instanceof ProductModelPricingAdapter)) {
            // wrap it into a pricing adapter if naked
            product = ProductPricingFactory.getInstance().getPricingAdapter(product);
        }

        if (enableProductIncomplete && PopulatorUtil.isProductIncomplete(product) && !PopulatorUtil.isNodeArchived(product)) {
            return createProductDataLight(user, product);
        }

        SkuModel sku = PopulatorUtil.getDefSku(product);
        /* if this is not populated, then product name won't be displayed on pdp */
        try {
            // if product is disc, then getDefSku returns null, but on PDP we need the prods anyway, so get first sku
            if (sku == null && product.getSkuCodes().size() > 0) {
                sku = product.getSku(0);
            }
        } catch (Exception e) {
            LOG.warn("Exception while populating defaultSku: ", e);
        }

        if (sku == null) {
            BaseJsonServlet.returnHttpError(500, "default sku does not exist for this product: " + product.getContentName());
        }

        return createProductDataForCarousel(user, product, sku, null, false);
    }

    public static void populateBrowseProductData(ProductData productData, FDUserI user, CmsFilteringNavigator nav, boolean enableProductIncomplete)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        if (productData == null || !productData.isRequirePopulation()) {
            return;
        }
        ProductModel product = productData.getProductModel();
        if (product == null) {
            BaseJsonServlet.returnHttpError(500, "product not found");
        }

        if (!(product instanceof ProductModelPricingAdapter)) {
            // wrap it into a pricing adapter if naked
            product = ProductPricingFactory.getInstance().getPricingAdapter(product);
        }

        if (enableProductIncomplete && PopulatorUtil.isProductIncomplete(product) && !PopulatorUtil.isNodeArchived(product)) {
            populateProductDataLight(user, product, productData);
            productData.setRequirePopulation(false);
            return;
        }

        SkuModel sku = PopulatorUtil.getDefSku(product);
        if (sku == null) {
            BaseJsonServlet.returnHttpError(500, "default sku does not exist for this product: " + product.getContentName());
        }

        populateDetailProductData(user, product, sku, null, false, productData);
        if (!nav.populateSectionsOnly()) {
            productData = ProductDetailPopulator.populateBrowseRecommendation(user, productData, product);
            if (!productData.isIncomplete()) {
                productData = ProductDetailPopulator.populateSelectedNutritionFields(user, productData, sku.getProduct(), nav.getErpNutritionTypeType());
            }
        }
        FilteringFlowType pageType = nav.getPageType();
        if (pageType != null) {
            productData.setPageType(pageType.toString());
        }

        productData.setRequirePopulation(false);
    }

    public static ProductData populateBrowseRecommendation(FDUserI user, ProductData data, ProductModel product)
            throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse {
        if (data == null) {
            data = new ProductData();
        }

        // APPDEV-4034

        boolean isAllowedCohort = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.browseflyoutrecommenders, user);
        if (!isAllowedCohort) {
            return data;
        }

        ProductModel browseRecommendation = ProductRecommenderUtil.getBrowseRecommendation(product);

        if (browseRecommendation != null) {
            data.setBrowseRecommandation(createProductData(user, browseRecommendation));
        }

        return data;
    }

    public static ProductData populateSelectedNutritionFields(FDUserI user, ProductData data, FDProduct fdProduct, ErpNutritionType.Type erpsNutritionTypeType) {
        if (data == null) {
            data = new ProductData();
        }

        if (erpsNutritionTypeType != null) {

            // format nutrition title based on i_grocery_product_separator.jspf
            String nutritionSortTitle = StringUtils.replace(erpsNutritionTypeType.getDisplayName(), " quantity", "");

            if ("Total Carbohydrate".equalsIgnoreCase(nutritionSortTitle)) {
                nutritionSortTitle = "Total Carbs";
            }
            if ("%".equals(erpsNutritionTypeType.getUom())) {
                nutritionSortTitle += ", %DV";
            }
            data.setNutritionSortTitle(nutritionSortTitle);
            data.setNutritionSortValue("no data"); // fallback

            FDNutrition selectedNutrition = fdProduct.getNutritionItemByType(erpsNutritionTypeType);
            ErpNutritionType.Type servingSizeType = ErpNutritionType.getType(ErpNutritionType.SERVING_SIZE);
            NumberFormat nf = NumberFormat.getNumberInstance();

            if (selectedNutrition != null) {

                // format nutrition value based on i_grocery_product_line.jspf
                double nurtitionSortValue = selectedNutrition.getValue();
                String nurtitionSortUom = selectedNutrition.getUnitOfMeasure();
                String nutritionSortValueText = (nurtitionSortValue < 0 ? "<1" : nf.format(nurtitionSortValue)) + ("%".equals(nurtitionSortUom) ? "" : " ") + nurtitionSortUom;
                String nutritionServingSizeValueText = null; // this field is only populated if nutritionSortValueText does not contain serving size info

                double servingSizeValue = 0;
                if (servingSizeType == erpsNutritionTypeType) {
                    servingSizeValue = nurtitionSortValue;

                } else { // add serving size info
                    FDNutrition servingSizeNutrition = fdProduct.getNutritionItemByType(servingSizeType);
                    if (servingSizeNutrition != null) {
                        servingSizeValue = servingSizeNutrition.getValue();
                        nutritionServingSizeValueText = nf.format(servingSizeValue) + " " + servingSizeNutrition.getUnitOfMeasure();
                    }
                }

                if (servingSizeValue != 0) {
                    FDNutrition servingWeightNutrition = fdProduct.getNutritionItemByType(ErpNutritionType.getType(ErpNutritionType.SERVING_WEIGHT));
                    if (servingWeightNutrition != null) {
                        String servingWeightText = " (" + nf.format(servingWeightNutrition.getValue()) + "g)";
                        if (nutritionServingSizeValueText == null) {
                            nutritionSortValueText += servingWeightText;
                        } else {
                            nutritionServingSizeValueText += servingWeightText;
                        }
                    }

                    data.setNutritionSortValue(nutritionSortValueText);
                    data.setNutritionServingSizeValue(nutritionServingSizeValueText);
                }
            }
        }

        // add nutrition fields to try recommendation as well
        ProductData browseRecommendationData = data.getBrowseRecommandation();
        if (browseRecommendationData != null) {
            try {
                FDProduct browseRecommendationFDProduct = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(browseRecommendationData.getSkuCode()));
                ;
                data.setBrowseRecommandation(populateSelectedNutritionFields(user, browseRecommendationData, browseRecommendationFDProduct, erpsNutritionTypeType));

            } catch (FDSkuNotFoundException e) {
                LOG.error("Couldnt't add nutrion fields to browseRecommendationData", e);
            } catch (FDResourceException e) {
                LOG.error("Couldnt't add nutrion fields to browseRecommendationData", e);
            }
        }

        return data;
    }

    /**
     * Populates product basic-level data. Does not populate any sku level attributes.
     *
     * @param data
     * @param user
     * @param product
     * @return
     * @throws HttpErrorResponse
     * @throws FDResourceException
     */
    public static BasicProductData populateBasicProductData(BasicProductData data, FDUserI user, ProductModel product) throws FDResourceException {
        if (data == null) {
            data = new ProductData();
        }

        data.setProductId(product.getContentKey().getId());

        data.setCMSKey(product.getContentKey().getEncoded());

        // Product & brand name - we need to separate them if applicable
        populateProductAndBrandName(data, product);

        // APPDEV - 4270 Optimize the webpage changes to append the domain
        String domains = FDStoreProperties.getSubdomains();
        data.setAkaName(product.getAka());
        if (product.getProdImage() != null)
            data.setProductImage(domains + product.getProdImage().getPathWithPublishId());
        if (product.getDetailImage() != null)
            data.setProductDetailImage(domains + product.getDetailImage().getPathWithPublishId());
        if (product.getZoomImage() != null)
            data.setProductZoomImage(domains + product.getZoomImage().getPathWithPublishId());
        if (product.getJumboImage() != null)
            data.setProductJumboImage(domains + product.getJumboImage().getPathWithPublishId());
        if (product.getAlternateImage() != null) {
            data.setProductAlternateImage(domains + product.getAlternateImage().getPathWithPublishId());
        }
        if (product.getPackageImage() != null) {
            data.setProductImagePackage(domains + product.getPackageImage().getPathWithPublishId());
        }

        data.setProductPageUrl(FDURLUtil.getNewProductURI(product));
        data.setProductPagePrimaryHomeUrl(FDURLUtil.getNewProductURI(product.getPrimaryProductModel()));

        data.setQuantityText(product.getQuantityText());
        data.setPackageDescription(product.getPackageDescription());
        data.setSoldBySalesUnit(product.isSoldBySalesUnits());
        data.setHasTerms(product.hasTerms());
        if (StandingOrderHelper.isEligibleForSo3_0(user)) {
            // data.setSoData(StandingOrderHelper.getAllSoData(user,true,false));
            data.setSoData(StandingOrderHelper.getValidSO3DataForProducts(user));
        }
        // alcoholic & usq flags
        try {
            // For alcoholic and usq flags check the default sku
            FDProduct fdProduct = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(product.getDefaultSkuCode()));
            data.setAlcoholic(isAlcoholic(product, fdProduct));
            data.setUsq(isUsq(product, fdProduct));
        } catch (Exception ignore) {
            LOG.debug("Failed to set alcoholic and usq flags " + ignore.getMessage());
            // ignore any errors
        }

        // bazaar-voice flag
        data.setBazaarVoice(false);

        return data;
    }

    /**
     * Set the following attributes on product potato
     *
     * Product Name Brand Name Product Name without Brand Name
     *
     * @param data
     * @param product
     * @return
     */
    public static BasicProductData populateProductAndBrandName(BasicProductData data, ProductModel product) {

        // Product & brand name - we need to separate them if applicable
        final String fullName = product.getFullName();
        String productNameNoBrand = fullName;

        String brandName = product.getPrimaryBrandName();

        // remove brand name from product full name if possible
        if (brandName != null && brandName.length() > 0 && fullName.length() >= brandName.length() && fullName.substring(0, brandName.length()).equalsIgnoreCase(brandName)) {
            productNameNoBrand = fullName.substring(brandName.length()).trim();
        }

        // set potato attributes
        data.setProductName(fullName);
        data.setProductNameNoBrand(productNameNoBrand);
        data.setBrandName(brandName);
        data.setProductAltText(fullName.replace("\"", "").replace("'", ""));

        return data;
    }

    /**
     * Populates product level data.
     *
     * @param item
     * @param user
     * @param productModel
     * @param sku
     * @param fdProduct
     * @param priceCalculator
     * @param orderLine
     * @param useFavBurst
     * @param useFavBurst
     */
    public static void populateProductData(ProductData item, FDUserI user, ProductModel productModel, SkuModel sku, FDProduct fdProduct, PriceCalculator priceCalculator,
            FDProductSelectionI orderLine, boolean useFavBurst, boolean usePrimaryHome) {
        populateSimpleProductData(item, productModel, sku, usePrimaryHome);
        populateAvailable(item, user, productModel);
        populateRatings(item, user, productModel, sku.getSkuCode());
        populateBursts(item, user, productModel, priceCalculator, useFavBurst);
        populateQuantity(item, user, productModel, orderLine);
        populateSalesUnits(item, fdProduct, orderLine);
        populateScores(item, user, productModel);
        populateLineData(item, orderLine, fdProduct, productModel, sku);
        populateAvailabilityMessages(item, productModel, fdProduct, sku);
    }
    
    private static void populateSimpleProductData(ProductData item, ProductModel productModel, SkuModel sku, boolean usePrimaryHome) {
        if (productModel != null && productModel.getCategory() != null && productModel.getCategory().getDepartment() != null) { // this can happen if the product is orphan
            item.setCatId(usePrimaryHome ? productModel.getParentNode().getContentKey().getId() : productModel.getCategory().getContentName());
            item.setDepartmentId(productModel.getCategory().getDepartment().getContentKey().getId());
            item.setSkuCode(sku.getSkuCode());
            item.setCustomizePopup(!productModel.isAutoconfigurable());
            item.setHasTerms(productModel.hasTerms());
            item.setDiscontinued(productModel.isDiscontinued());
            item.setOutOfSeason(productModel.isOutOfSeason());
            item.setNewProduct(productModel.isNew());
        }
    }

    private static void populateAvailable(ProductData item, FDUserI user, ProductModel productModel) {
        boolean available = true;
        if (productModel.isUnavailable()) {
            available = false;
            // if unavailable add product replacements
            if (item instanceof QuickShopLineItem) {
                QuickShopHelper.populateReplacements((QuickShopLineItem) item, productModel, user);
            }
        }
        item.setAvailable(available);

        item.setAvailableQty(productModel.getAvailabileQtyForDate(null));
        if (item.getAvailableQty() == 0 && item.isAvailable()) {
            // LOG.warn( " INCONSISTENCY BETWEEN AVAILABILITY AND INVENTORY: productid:" + item.getProductId() );
        }
    }

    // ==============
    // Messaging
    // ==============
    private static void populateAvailabilityMessages(ProductData item, ProductModel productModel, FDProduct fdProduct, SkuModel sku) {
        // Party platter cancellation notice
        if (productModel.isPlatter()) {
            /*
             * COMMENTED OUT FOR APPDEV-4014
             *
             * item.setMsgCancellation(
             * "* Orders for this item cancelled after 3PM the day before delivery (or Noon on Friday/Saturday/Sunday and major holidays) will be subject to a 50% fee." );
             */
        }

        // Party platter cutoff notice (header+text)
        try {
            TimeOfDay cutoffTime = RestrictionUtil.getPlatterRestrictionStartTime();
            if (productModel.isPlatter() && cutoffTime != null) {
                String headerTime;
                // String bodyTime;

                SimpleDateFormat headerTimeFormat = new SimpleDateFormat("h:mm a");
                // SimpleDateFormat bodyTimeFormat = new SimpleDateFormat("ha");

                headerTime = headerTimeFormat.format(cutoffTime.getAsDate());
                // bodyTime = bodyTimeFormat.format(cutoffTime.getAsDate());
                if(null !=sku){
//                	String earliestDate=new SimpleDateFormat("EEE M/dd").format(productModel.getEarliestAvailability());
                	String earliestAvailabilityDate=sku.getEarliestAvailabilityMessage();
                	if(null !=earliestAvailabilityDate){
	                	item.setMsgCutoffHeader("Order by " + headerTime + " for Delivery " + earliestAvailabilityDate );
	                	item.setMsgCutoffNotice("");
                	}
                	else{
                		item.setMsgCutoffHeader("Order by " + headerTime + " for Delivery Tomorrow");
                    	item.setMsgCutoffNotice("");
                	}
                }else{
                	item.setMsgCutoffHeader("Order by " + headerTime + " for Delivery Tomorrow");
                	item.setMsgCutoffNotice("");
                }

            }
        } catch (Exception e) {
            LOG.debug("Exception will figuring out Platter Cutoff Found only in Table APP, not sure about the rootcause");
        }

        // Limited availability messaging

        // msgDayOfWeek - Blocked days of the week notice
        // msgDeliveryNote - Another blocked days of the week notice

        DayOfWeekSet blockedDays = productModel.getBlockedDays();
        if (!blockedDays.isEmpty()) {
            int numOfDays = 0;
            StringBuffer daysStringBuffer = null;
            boolean isInverted = true;

            if (blockedDays.size() > 3) {
                numOfDays = (7 - blockedDays.size());
                daysStringBuffer = new StringBuffer(blockedDays.inverted().format(true));
            } else {
                isInverted = false;
                daysStringBuffer = new StringBuffer(blockedDays.format(true));
                numOfDays = blockedDays.size();
            }

            if (numOfDays > 1) {
                // ** make sundays the last day, if more than one in the list
                if (daysStringBuffer.indexOf("Sundays, ") != -1) {
                    daysStringBuffer.delete(0, 9);
                    daysStringBuffer.append(" ,Sundays");
                }

                // replace final comma with "and" or "or"
                int li = daysStringBuffer.lastIndexOf(",");
                daysStringBuffer.replace(li, li + 1, (isInverted ? " and " : " or "));
            }

            // item.setMsgDayOfWeekHeader( "Limited Availability" );
            item.setMsgDayOfWeekHeader("<b>" + (isInverted ? "Only" : "Not") + "</b> available for delivery on <b>" + daysStringBuffer.toString() + "</b>");
            item.setMsgDayOfWeek("");

            item.setMsgDeliveryNote("Only available for delivery on " + blockedDays.inverted().format(true) + ".");
        }

        // Lead time message
        if (fdProduct != null) {
            int leadTime = fdProduct.getMaterial().getLeadTime();
            if (leadTime > 0 && FDStoreProperties.isLeadTimeOasAdTurnedOff()) {
                // item.setMsgLeadTimeHeader( JspMethods.convertNumToWord(leadTime) + "-Day Lead Time" );
                item.setMsgLeadTimeHeader("Please complete checkout at least two days in advance. (Order by Thursday for Saturday).");
                item.setMsgLeadTime("");
            }
        }
        // String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
        String plantID = null;

        if (fdProduct != null) {
            try {
                plantID = ProductInfoUtil.getPickingPlantId(FDCachedFactory.getProductInfo(fdProduct.getSkuCode()));
            } catch (FDResourceException ex) {
                LOG.debug("Exception while getting the plantId " + ex);
                // TODO Auto-generated catch block
            } catch (FDSkuNotFoundException exsku) {
                // TODO Auto-generated catch block
                LOG.debug("Exception while getting the plantId" + exsku);
            }
        }
        // Kosher restrictions
        if (fdProduct != null && fdProduct.getKosherInfo(plantID) != null && fdProduct.getKosherInfo(plantID).isKosherProduction()) {
            try {
                DlvRestrictionsList globalRestrictions = FDDeliveryManager.getInstance().getDlvRestrictions();

                StringBuilder buf = new StringBuilder("* Not available for delivery on Friday, Saturday, or Sunday morning.");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date startDate = cal.getTime();

                cal.add(Calendar.DATE, 7);
                Date endDate = cal.getTime();

                DateRange dateRange = new DateRange(startDate, endDate);

                List<RestrictionI> kosherRestrictions = globalRestrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY, EnumDlvRestrictionReason.KOSHER,
                        EnumDlvRestrictionType.ONE_TIME_RESTRICTION, dateRange);

                if (kosherRestrictions.size() > 0) {
                    buf.append("<br/>Also unavailable during ");
                    int s = kosherRestrictions.size();
                    for (int i = 0; i < s; i++) {
                        RestrictionI r = kosherRestrictions.get(i);
                        buf.append("<b>" + r.getName() + "</b>, " + r.getDisplayDate() + ((i == s - 1) ? "." : "; "));
                    }
                }

                item.setMsgKosherRestriction(buf.toString());
            } catch (FDResourceException e) {
            }

        }

        // earliest availability - product not yet available but will in the near future
        if (sku != null) {
            item.setMsgEarliestAvailability(sku.getEarliestAvailabilityMessage());
        }

    }

    /**
     * Populates the default sku for the product. Returns a ProductData object.
     *
     * As multiple sku products are not used anymore, this is the combined product+sku data for any future use.
     *
     * @param data
     * @param user
     * @param product
     * @param fdProduct
     * @param sku
     * @return
     * @throws HttpErrorResponse
     * @throws FDResourceException
     * @throws FDSkuNotFoundException
     */
    public static ProductData populateSkuData(ProductData data, FDUserI user, ProductModel product, SkuModel sku, FDProduct fdProduct)
            throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

        // Get the sku
        if (sku == null) {
            sku = product.getDefaultSku();
        }

        // Get the fdproduct
        if (fdProduct == null) {
            fdProduct = sku.getProduct();
        }

        // Automatic configuration
        Map<String, String> currentConfig = null;
        if (fdProduct.isAutoconfigurable(product.isSoldBySalesUnits())) {
            // try to autoconfigure
            FDConfigurableI autoConfigured = fdProduct.getAutoconfiguration(product.isSoldBySalesUnits(), product.getQuantityMinimum());
            if (autoConfigured != null) {
                currentConfig = autoConfigured.getOptions();
            }
        }

        // Set data

        populateSkuVariations(data, getVariations(fdProduct, product, currentConfig, user.getUserContext().getPricingContext()));
        data.setLabel(getLabel(sku));

        data.setSalesUnitLabel(product.getSalesUnitLabel());

        if (product.isHasSalesUnitDescription()) {
            data.setHasSalesUnitDescription(true);

            String parentCat = product.getParentNode().getContentName();

            StringBuilder popupUrl = new StringBuilder();
            popupUrl.append("/shared/popup.jsp?catId=");
            popupUrl.append(parentCat);
            popupUrl.append("&prodId=");
            popupUrl.append(product.getContentName());
            popupUrl.append("&attrib=SALES_UNIT_DESCRIPTION&tmpl=");

            // Strange stuff copied from i_product.jspf
            if (parentCat.equalsIgnoreCase("fwhl") || parentCat.equalsIgnoreCase("fstk") || parentCat.equalsIgnoreCase("fflt") || parentCat.equalsIgnoreCase("prchp")
                    || parentCat.equalsIgnoreCase("bovnrst") || parentCat.equalsIgnoreCase("bpotrst") || parentCat.equalsIgnoreCase("kosher_meat_beef_roast")
                    || parentCat.indexOf("kosher_seafood") > -1) {
                popupUrl.append("small");
            } else {
                popupUrl.append("large");
            }

            data.setSalesUnitDescrPopup(popupUrl.toString());
        }

        data.setHolidayMealBundleContainer(HolidayMealBundleService.defaultService().populateHolidayMealBundleData(sku, user));

        return data;
    }

    private static void populateRatings(ProductData item, FDUserI user, ProductModel product, String skuCode) {

        int wineRating = 0;
        int expertRating = 0;
        int customerRating = 0;
        int customerRatingReviewCount = 0;
        String sustainabilityRating = null;

        // Wine Rating
        final String deptName = product.getDepartment() != null ? product.getDepartment().getContentName() : "";
        if (WineUtil.getWineAssociateId().toLowerCase().equalsIgnoreCase(deptName)) {
            try {
                EnumOrderLineRating r = product.getProductRatingEnum(skuCode);
                if (r != null) {
                    wineRating = r.getValue();
                }
            } catch (FDResourceException ignore) {
            }
        }

        // Expert Rating
        try {
            EnumOrderLineRating r = product.getProductRatingEnum(skuCode);
            if (r != null) {
                expertRating = r.getValue();
            }
        } catch (FDResourceException ignore) {
        }

        // Customer Rating
        CustomerRatingsDTO customerRatingsDTO = CustomerRatingsContext.getInstance().getCustomerRatingByProductId(product.getContentKey().getId());
        if (customerRatingsDTO != null) {
            BigDecimal averageRating = customerRatingsDTO.getAverageOverallRating();
            customerRating = ((int) Math.ceil(averageRating.doubleValue())) * 2;
            customerRatingReviewCount = customerRatingsDTO.getTotalReviewCount();
        }

        // Sustainability Rating
        if (user.isProduceRatingEnabled()) {
            try {
                sustainabilityRating = product.getSustainabilityRating(skuCode);
            } catch (FDResourceException ignore) {
            }
        }

        // check if sku should show ratings at all
        item.setShowRatings(PopulatorUtil.isShowRatings(skuCode));

        // Now set only(!) the appropriate values on the item

        // 1. always(!) add sustainability, regardless of others
        if (sustainabilityRating != null && sustainabilityRating.trim().length() > 0) {
            item.setSustainabilityRating(sustainabilityRating);
        }

        // 2. wine rating is the strongest of all
        if (wineRating > 0) {
            item.setWineRating(wineRating);
        } else

        // 3. expert rating comes next
        if (expertRating > 0) {
            item.setExpertRating(expertRating);
        } else

        // 4. customer rating is the last resort
        if (customerRating > 0) {
            item.setCustomerRating(customerRating);
            item.setCustomerRatingReviewCount(customerRatingReviewCount);
        }

        // 5. heat rating
        item.setHeatRating(product.getHeatRating());
        if (item.getHeatRating() > 0) {
            String heatRatingMediaPath = "/media/brands/fd_heatscale/fd_heatscale.html";
            try {
                item.setHeatRatingScale(fetchMedia(heatRatingMediaPath, user, false));
            } catch (IOException e) {
                LOG.error("Failed to fetch Heat Scale Legend Media " + heatRatingMediaPath + " " + e.getMessage());
            } catch (TemplateException e) {
                LOG.error("Failed to fetch Heat Scale Legend Media " + heatRatingMediaPath + " " + e.getMessage());
            }
        }
    }

    private static void populateBursts(ProductData item, FDUserI user, ProductModel product, PriceCalculator priceCalculator, boolean useFavBurst) {
        int deal = priceCalculator.getHighestGroupDealPercentage();
        boolean isNew = product.isNew();
        boolean isYourFave = DYFUtil.isFavorite(product, user);
        boolean isBackInStock = product.isBackInStock();
        boolean isGoingOutOfStock = product.isGoingOutOfStock();
        ProductReference prodRef = new ProductReferenceImpl(product);
        boolean isFree = user.isProductSample(prodRef);

        /* compare against prop limits */
        // determine what to display
        if ((FDStoreProperties.getBurstsLowerLimit() <= deal) && (FDStoreProperties.getBurstUpperLimit() >= deal)) {
            item.setDeal(deal);
        } else if (isFree) {
            item.setBadge("free");
            item.setFreeSamplePromoProduct(isFree);
        } else if (useFavBurst && isYourFave) {
            item.setBadge("fav");
        } /*else if (isGoingOutOfStock) {
            item.setBadge("going");
        }*/ else if (isNew) {
            item.setBadge("new");
        } else if (isBackInStock) {
            item.setBadge("back");
        }
    }

    private static final float calculateSafeMaximumQuantity(FDUserI user, ProductModel product) {
        float min = product.getQuantityMinimum();
        float max = user.getQuantityMaximum(product);
        float inc = product.getQuantityIncrement();

        while (min <= max - inc)
            min += inc;
        return min;
    }

    private static void populateSalesUnits(ProductData item, FDProduct fdProduct, FDProductSelectionI orderLine) {
        // Sales units
        List<SalesUnit> sus = new ArrayList<SalesUnit>();
        String selectedSu = null;
        if (orderLine != null) {
            selectedSu = orderLine.getSalesUnit();
        } else {
            FDSalesUnit fdsu = fdProduct.getDefaultSalesUnit();
            if (fdsu != null) {
                selectedSu = fdsu.getName();
            }
        }

        for (FDSalesUnit fdsu : fdProduct.getSalesUnits()) {
            SalesUnit sue = new SalesUnit();
            String id = fdsu.getName();
            sue.setId(id);
            sue.setName(fdsu.getDescription());
            sue.setSelected(id.equals(selectedSu));
            sus.add(sue);
        }
        item.setSalesUnit(sus);
    }

    private static void populateQuantity(ProductData item, FDUserI user, ProductModel productModel, FDProductSelectionI orderLine) {
        // Numeric quantity
        Quantity quantity = new Quantity();
        quantity.setqMin(productModel.getQuantityMinimum());
        quantity.setqMax(calculateSafeMaximumQuantity(user, productModel));
        quantity.setqInc(productModel.getQuantityIncrement());
        quantity.setQuantity(orderLine != null ? orderLine.getQuantity() : quantity.getqMin());
        item.setQuantity(quantity);

        // populate in cart amount
        FDCartModel cart = user.getShoppingCart();
        item.setMaxProductSampleQuantityReached(cart.isMaxProductSampleQuantityReached(productModel.getContentName()) || cart.isMaxSampleReached());
        item.setInCartAmount(cart.getTotalQuantity(productModel, false));
    }

    private static void populateScores(ProductData item, FDUserI user, ProductModel productModel) {
        // get frequency and recency values from smartstore
        String[] variables = new String[2];
        variables[0] = ScoreProvider.USER_FREQUENCY;
        variables[1] = ScoreProvider.RECENCY_DISCRETIZED;
        String userId = user.getIdentity() != null ? user.getIdentity().getErpCustomerPK() : null;
        double[] scores = ScoreProvider.getInstance().getVariables(userId, productModel.getUserContext().getPricingContext(), productModel.getContentKey(), variables);
        item.setFrequency(scores[0]);
        item.setRecency(scores[1]);
    }

    private static void populateLineData(ProductData item, FDProductSelectionI lineItem, FDProduct product, ProductModel productModel, SkuModel sku) {

        // orderline/cartline/productselection data
        if (lineItem != null) {

            item.setConfigInvalid(lineItem.isInvalidConfig());

            // APPDEV-4123 START
            FDProductInfo defaultProductInfo = null;
            FDProduct defaultProduct = null;
            int sizeFDVar = 0;
            try {
                defaultProductInfo = FDCachedFactory.getProductInfo(lineItem.getSkuCode());
                defaultProduct = FDCachedFactory.getProduct(defaultProductInfo);
                for (FDVariation fdVariation : defaultProduct.getVariations()) {
                    sizeFDVar++;
                }
            } catch (FDResourceException e) {
                LOG.error("Error in post-process populate. Skipping item.");
                return;
            } catch (FDSkuNotFoundException e) {
                LOG.warn("Sku not found in post-process populate. This is unexpected. Skipping item.");
                return;
            }

            int lineItemSizeConfiguration = 0;
            if (null != lineItem.getConfiguration()) {
                lineItemSizeConfiguration = lineItem.getConfiguration().getOptions().size();
            }

            if (sizeFDVar != lineItemSizeConfiguration) {
                item.setShowMsg(true);
            }

            // APPDEV-4123 END

            Map<String, String> config = lineItem.getConfiguration().getOptions();
            if (config != null && !config.isEmpty()) {
                item.setConfiguration(config);
            }

            item.setConfigDescr(lineItem.getConfigurationDesc());

            item.setSalesUnitDescrPDP(
                    generateSalesUnitDescrPDP(product, lineItem, productModel, sku, (item.getSalesUnit() != null && item.getSalesUnit().size() > 1) ? true : false));

            item.setDescription(lineItem.getDescription());

            item.setDepartmentDesc(lineItem.getDepartmentDesc());

            item.setConfiguredPrice(lineItem.getConfiguredPrice());

        }
    }

    private static String generateSalesUnitDescrPDP(FDProduct product, FDProductSelectionI theProduct, ProductModel productModel, SkuModel sku, boolean isMultiChoice) {

        FDSalesUnit unit = product.getSalesUnit(theProduct.getConfiguration().getSalesUnit());
        String salesUnitDescr = unit != null ? unit.getDescription() : null;

        StringBuffer PDPsalesUnitDescr = new StringBuffer();

        // clean sales unit description
        if (salesUnitDescr != null) {
            salesUnitDescr = salesUnitDescr.trim();

            String salesUnitDescrHead = salesUnitDescr; // if the part before '('
            if (salesUnitDescr.indexOf("(") > -1) {
                salesUnitDescrHead = salesUnitDescr.substring(0, salesUnitDescr.indexOf("("));
                salesUnitDescrHead = salesUnitDescrHead.trim();
            }
            if ((!"".equals(salesUnitDescr)) // original is empty

                    // if the part before '(' is "nm" and "ea", it should be ignored
                    && (!"nm".equalsIgnoreCase(salesUnitDescrHead)) && (!isMultiChoice) // this is different compared to OrderLineUtil.createConfigurationDescription()
                    && (!"ea".equalsIgnoreCase(salesUnitDescrHead))) {
                if (!productModel.getSellBySalesunit().equals("SALES_UNIT")) {
                    PDPsalesUnitDescr.append(salesUnitDescr); // append original
                } else if ((productModel.getPrimarySkus().size() == 1) && (productModel.getVariationMatrix().isEmpty()) && (product.getSalesUnits().length == 1)
                        && (product.getSalesUnits()[0].getName().equalsIgnoreCase("EA"))) {
                    PDPsalesUnitDescr.append(salesUnitDescr); // append original
                }
            }
        }

        return PDPsalesUnitDescr.toString();
    }

    public static void populatePricing(ProductData item, FDProduct fdProduct, FDProductInfo productInfo, PriceCalculator priceCalculator) throws FDResourceException {
        populatePricing(item, fdProduct, productInfo, priceCalculator, null);
    }

    public static void populatePricing(ProductData item, FDProduct fdProduct, FDProductInfo productInfo, PriceCalculator priceCalculator, FDUserI user) throws FDResourceException {

        ZonePriceInfoModel zpi;
        try {
            zpi = priceCalculator.getZonePriceInfoModel();

            if (zpi != null) {
                item.setPrice(zpi.getDefaultPrice());
                item.setScaleUnit(productInfo.getDisplayableDefaultPriceUnit().toLowerCase());

                // APPDEV-4357 -Price display by default sales unit.
                boolean isPriceConfigConversionRequired = isPriceConfigConversionRequired(item, user);
                if (isPriceConfigConversionRequired) {
                    populateConvertedPriceByDefaultSalesUnit(item, fdProduct, productInfo);
                }
            }
        } catch (FDSkuNotFoundException e) {
            // No sku (cannot happen) - don't even try the pricing
            return;
        }

        // Tax and Deposit

        StringBuilder taxAndDepositBuilder = new StringBuilder();
        boolean hasTax = fdProduct.isTaxable();
        boolean hasDeposit = fdProduct.hasDeposit();
        if (hasTax || hasDeposit) {
            taxAndDepositBuilder.append("plus ");
            if (hasTax) {
                taxAndDepositBuilder.append("tax ");
            }
            if (hasTax && hasDeposit) {
                taxAndDepositBuilder.append("& ");
            }
            if (hasDeposit) {
                taxAndDepositBuilder.append("deposit");
            }
        }
        item.setTaxAndDeposit(taxAndDepositBuilder.toString());
        item.setAboutPriceText(priceCalculator.getAboutPriceFormatted(priceCalculator.getDealPercentage()));

        populateSubtotalInfo(item, fdProduct, productInfo, priceCalculator);
        populateSaving(item, productInfo, priceCalculator);

        // [APPDEV-3438] unit price thingy
        if (FDStoreProperties.isUnitPriceDisplayEnabled()) {
            FDSalesUnit su = fdProduct.getDefaultSalesUnit();
            if (su != null) {
                String unitPrice = UnitPriceUtil.getUnitPrice(su, item.getPrice());
                if (unitPrice != null) {
                    item.setUtPrice(unitPrice);
                    item.setUtSalesUnit(su.getUnitPriceUOM());
                }
            }
        }
    }

    private static void populateConvertedPriceByDefaultSalesUnit(ProductData item, FDProduct fdProduct, FDProductInfo productInfo) {
        FDSalesUnit su = fdProduct.getDefaultSalesUnit();
        if (null == su) {
            su = fdProduct.getSalesUnits()[0];
        }
        if (null != su) {
            final int n = su.getNumerator();
            final int d = su.getDenominator();
            if (n > 0 && d > 0 && ((double) n) / d <= FDStoreProperties.getPriceConfigConversionLimit()) {
                final double p = (item.getPrice() * n) / d;
                item.setPricePerDefaultSalesUnit(UnitPriceUtil.formatDecimalToString(p));
                item.setDispDefaultSalesUnit(UnitPriceUtil.formatDecimalToString((double) n / d) + productInfo.getDisplayableDefaultPriceUnit().toLowerCase());
            }
        }
    }

    private static boolean isPriceConfigConversionRequired(ProductData item, FDUserI user) {
        boolean doPriceConfigConversion = false;
        if (null != user && FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.priceconfigdisplay2016, user) && "lb".equalsIgnoreCase(item.getScaleUnit())
                && null != item.getDepartmentId()) {
            String deparmentIds = FDStoreProperties.getPriceConfigDepartments();
            if (null != deparmentIds && !"".equalsIgnoreCase(deparmentIds.trim())) {
                StringTokenizer st = new StringTokenizer(deparmentIds.trim(), ",");
                while (st.hasMoreElements()) {
                    String departmentId = st.nextToken();
                    if (item.getDepartmentId().equalsIgnoreCase(departmentId)) {
                        doPriceConfigConversion = true;
                        break;
                    }
                }
            } else {
                doPriceConfigConversion = true;
            }
        }
        return doPriceConfigConversion;
    }

    private static void populateSubtotalInfo(ProductData item, FDProduct fdProduct, FDProductInfo productInfo, PriceCalculator priceCalculator) throws FDResourceException {

        ZoneInfo pricingZone = priceCalculator != null && priceCalculator.getPricingContext() != null ? priceCalculator.getPricingContext().getZoneInfo() : null;
        MaterialPrice[] availMatPrices = fdProduct.getPricing().getZonePrice(pricingZone).getMaterialPrices();
        MaterialPrice[] matPrices = null;
        List<MaterialPrice> matPriceList = new ArrayList<MaterialPrice>();

        FDGroup group = productInfo.getGroup(pricingZone);
        // if ( productInfo.isGroupExists(pricingZone.getSalesOrg(),pricingZone.getDistributionChanel()) ) {
        if (null != group) {
            // Has a Group Scale associated with it. Check if there is GS price defined for current pricing zone.
            // FDGroup group = productInfo.getGroup(pricingZone.getSalesOrg(),pricingZone.getDistributionChanel());
            MaterialPrice[] grpPrices = null;
            try {
                grpPrices = GroupScaleUtil.getGroupScalePrices(group, pricingZone);
            } catch (FDResourceException fe) {
                // Never mind. Show regular price for the material.
            }
            if (grpPrices != null && grpPrices.length > 0) {
                // Group scale price applicable to this material. So modify material prices array to accomodate GS price.
                MaterialPrice regularPrice = availMatPrices[0];// Get the regular price/single unit price first.

                // Get the first group scale price and set the lower bound to be upper bound of regular price.
                MaterialPrice newRegularPrice = new MaterialPrice(regularPrice.getPrice(), regularPrice.getPricingUnit(), regularPrice.getScaleLowerBound(),
                        grpPrices[0].getScaleLowerBound(), grpPrices[0].getScaleUnit(), regularPrice.getPromoPrice());
                // Add the modified regular price.
                matPriceList.add(newRegularPrice);
                // Add the remaining group scale prices.
                for (int i = 0; i < grpPrices.length; i++) {
                    matPriceList.add(grpPrices[i]);
                }
                matPrices = matPriceList.toArray(new MaterialPrice[0]);
            }
        }

        if (matPrices == null) {
            // Set the default prices defined for the material.
            matPrices = availMatPrices;
        }

        if (fdProduct.getPricing() != null) {
            item.setAvailMatPrices(matPrices);
            item.setCvPrices(fdProduct.getPricing().getCharacteristicValuePrices(priceCalculator.getPricingContext()));
            item.setSuRatios(fdProduct.getPricing().getSalesUnitRatios());
        }
        if ( /* productInfo.isGroupExists(pricingZone.getSalesOrg(),pricingZone.getDistributionChanel()) && */productInfo.getGroup(pricingZone) != null) {
            item.setGrpPrices(GroupScaleUtil.getGroupScalePrices(productInfo.getGroup(pricingZone), pricingZone));
        }

        if (null != group) {
            item.setGrpPrices(GroupScaleUtil.getGroupScalePrices(group, pricingZone));
        }

        ZonePriceInfoModel zone = productInfo.getZonePriceInfo(pricingZone);
        if (zone != null && zone.isItemOnSale()) {
            item.setWasPrice(zone.getSellingPrice());
        }
    }

    private static void populateSaving(BasicProductData item, FDProductInfo productInfo, PriceCalculator priceCalculator) throws FDResourceException {

        MaterialPrice matPrice = null;
        GroupScalePricing grpPricing = null;

        // FDGroup group = productInfo.getGroup(priceCalculator.getPricingContext().getZoneInfo().getSalesOrg(),
        // priceCalculator.getPricingContext().getZoneInfo().getDistributionChanel());

        FDGroup group = productInfo.getGroup(priceCalculator.getPricingContext().getZoneInfo());

        if (group != null) {
            grpPricing = GroupScaleUtil.lookupGroupPricing(group);
            matPrice = GroupScaleUtil.getGroupScalePrice(group, priceCalculator.getPricingContext().getZoneInfo());
        }

        StringBuilder buf = new StringBuilder();
        if (grpPricing != null && matPrice != null) {

            // Group scale pricing (a.k.a. mix'n'match)
            item.setMixNMatch(true);
            item.setDealInfo(ProductSavingTag.getGroupPrice(group, priceCalculator.getPricingContext().getZoneInfo()));

            item.setGrpShortDesc(grpPricing.getShortDesc());
            item.setGrpLongDesc(grpPricing.getLongDesc());
            // change url for APPDEV-4060
            item.setGrpLink(item.getProductPageUrl().replace("&amp;", "&") + "&grpId=" + grpPricing.getGroupId() + "&version=" + grpPricing.getVersion());
            item.setGrpId(grpPricing.getGroupId());
            item.setGrpVersion(grpPricing.getVersion());

            // Group Scale Pricing - price string
            StringBuilder priceStr = new StringBuilder();
            NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
            DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");

            boolean isSaleUnitDiff = false;
            double displayPrice = 0.0;
            if (matPrice.getPricingUnit().equals(matPrice.getScaleUnit()))
                displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
            else {
                displayPrice = matPrice.getPrice();
                isSaleUnitDiff = true;
            }

            priceStr.append(FORMAT_QUANTITY.format(matPrice.getScaleLowerBound()));

            if (matPrice.getScaleUnit().equals("LB")) {
                priceStr.append(matPrice.getScaleUnit().toLowerCase()).append("s");
            }

            priceStr.append(" for ");
            priceStr.append(FORMAT_CURRENCY.format(displayPrice));

            if (isSaleUnitDiff) {
                priceStr.append("/").append(matPrice.getPricingUnit().toLowerCase());
            }

            item.setGrpPrice(priceStr.toString());

        } else {
            // Regular deal
            String scaleString = priceCalculator.getTieredPrice(0, null);
            if (scaleString != null) {
                // buf.append( "Save! " );
                buf.append(scaleString);
            } /*
               * else if ( priceCalculator.isOnSale() ) { buf.append( "Save " ); buf.append( priceCalculator.getDealPercentage() ); buf.append( "%" ); }
               */ else {
                // no sales, do nothing
            }
        }
        item.setSavingString(buf.toString());

    }

    public static void postProcessPopulate(FDUserI user, BasicProductData item, String skuCode) {
        postProcessPopulate(user, item, skuCode, false, null);
    }

    public static void postProcessPopulate(FDUserI user, BasicProductData item, String skuCode, boolean showCouponStatus, FDProductSelectionI lineData) {

        // lookup product data
        ProductModel productModel;
        FDProductInfo productInfo;
        try {
            productModel = ContentFactory.getInstance().getProduct(skuCode);
            productInfo = FDCachedFactory.getProductInfo(skuCode);
        } catch (FDSkuNotFoundException e) {
            LOG.warn("Sku not found in post-process populate. This is unexpected. Skipping item.");
            return;
        } catch (FDResourceException e) {
            LOG.error("Error in post-process populate. Skipping item.");
            return;
        }
        postProcessPopulate(user, item, productModel, productInfo, showCouponStatus, lineData);
        // post-process replacement item too, if any
        if (item instanceof QuickShopLineItem) {
            QuickShopLineItem replItem = ((QuickShopLineItem) item).getReplacement();
            if (replItem != null) {
                postProcessPopulate(user, replItem, replItem.getSkuCode(), showCouponStatus, lineData);
            }
        }

    }

    public static void postProcessPopulate(FDUserI user, BasicProductData item, ProductModel productModel, FDProductInfo productInfo, boolean showCouponStatus,
            FDProductSelectionI lineData) {

        // populate Ecoupons data
        FDCustomerCoupon coupon = null;
        if (!showCouponStatus) {
            coupon = user.getCustomerCoupon(productInfo, EnumCouponContext.PRODUCT, productModel.getParentId(), productModel.getContentName());
        } else if (lineData instanceof FDCartLineI) {
            coupon = user.getCustomerCoupon((FDCartLineI) lineData, EnumCouponContext.VIEWCART);
        }
        item.setCoupon(coupon);

        if (coupon != null) {
            EnumCouponStatus status = coupon.getStatus();
            item.setCouponDisplay(status != EnumCouponStatus.COUPON_CLIPPED_REDEEMED && status != EnumCouponStatus.COUPON_CLIPPED_EXPIRED);
            item.setCouponClipped(status != EnumCouponStatus.COUPON_ACTIVE);
            if (showCouponStatus) {
                item.setCouponStatusText(CartOperations.generateFormattedCouponMessage(coupon, status));
            } else {
                item.setCouponStatusText("");
            }
        } else {
            item.setCouponDisplay(false);
            item.setCouponClipped(false);
        }

        // populate in cart amount if it hasn't been populated yet
        if (item.getInCartAmount() == 0) {
            FDCartModel cart = user.getShoppingCart();
            item.setInCartAmount(cart.getTotalQuantity(productModel));
        }
    }

    public static boolean isUsq(ProductModel productModel, FDProduct fdProduct) {
        return fdProduct.isWine() && ContentNodeModelUtil.hasWineDepartment(productModel.getContentKey());
    }

    public static boolean isAlcoholic(ProductModel productModel, FDProduct fdProduct) {
        return fdProduct.isAlcohol() || productModel.getPrimaryHome().isHavingBeer();
    }

    public static FDConfigurableI getProductConfiguration(ProductModel product, FDProduct fdProduct) {
        FDConfigurableI config = null;
        if (fdProduct.isAutoconfigurable(product.isSoldBySalesUnits())) {
            // try to autoconfigure
            config = fdProduct.getAutoconfiguration(product.isSoldBySalesUnits(), product.getQuantityMinimum());
        }
        if (config == null) {
            String salesUnit = getDefaultSalesUnit(fdProduct);
            config = new FDConfiguration(product.getQuantityMinimum(), salesUnit);
        }
        return config;
    }

    public static String getDefaultSalesUnit(FDProduct fdProduct) {

        FDSalesUnit su = fdProduct.getDefaultSalesUnit();
        if (su == null && null != fdProduct.getSalesUnits() && fdProduct.getSalesUnits().length > 0) {

            su = fdProduct.getSalesUnits()[0];
        }
        if (null == su) {
            LOG.warn("Salesunit missing for " + fdProduct.getSkuCode() + " version:" + fdProduct.getVersion());
        }
        String salesUnit = su != null ? su.getName() : "unknown salesunit";

        return salesUnit;
    }

    public static String getLabel(SkuModel sku) {
        StringBuilder sb = new StringBuilder();
        List<DomainValue> varMx = sku.getVariationMatrix();
        boolean first = true;
        for (DomainValue dv : varMx) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(dv.getLabel());
        }
        String s = sb.toString();
        return s;
    }

    public static List<Variation> getVariations(FDProduct fdProd, ProductModel product, Map<String, String> currentConfig, PricingContext pCtx) {
        FDVariation[] variations = fdProd.getVariations();
        List<Variation> varList = new ArrayList<Variation>();

        List<FDVariation> orderedVariationsList = new ArrayList<FDVariation>();
        List<ComponentGroupModel> componentGroups = product.getComponentGroups();

        if (!componentGroups.isEmpty()) {
            for (ComponentGroupModel componentGroup : componentGroups) {
                List<String> chars = componentGroup.getCharacteristicNames();

                for (String curChar : chars) {
                    FDVariation tempVar = fdProd.getVariation(curChar);
                    orderedVariationsList.add(tempVar);
                }
            }

        } else {
            for (FDVariation fdVar : variations) {
                orderedVariationsList.add(fdVar);
            }
        }

        for (FDVariation fdVar : orderedVariationsList) {
            String varName = fdVar.getName();
            Variation var = new Variation();
            var.setName(varName);
            var.setLabel(fdVar.getDescription());
            var.setOptional(fdVar.isOptional());
            var.setDisplay(fdVar.getDisplayFormat());
            var.setUnderLabel(fdVar.getUnderLabel());

            // Bizarre help popup link generation, copied from i_product.jspf
            String charFileName = "media/editorial/fd_defs/characteristics/" + varName.toLowerCase() + ".html";
            if (MediaUtils.checkMedia(charFileName)) {

                // Media file for popup does exist, generate data
                StringBuilder popupUrl = new StringBuilder();
                popupUrl.append("/shared/fd_def_popup.jsp?charName=");
                popupUrl.append(varName);
                popupUrl.append("&tmpl=");
                popupUrl.append((fdVar.getDescription().equalsIgnoreCase("MARINADE/RUB")) ? "large_pop" : "small_pop");
                popupUrl.append("&title=");
                popupUrl.append(fdVar.getDescription());

                var.setDescrPopup(popupUrl.toString());
            }

            Map<FDVariationOption, ProductModel> varOptProductModels = collectAvailableVariations(fdVar, ContentFactory.getInstance());

            List<VarItem> varOpts = new ArrayList<VarItem>();
            for (FDVariationOption varOpt : varOptProductModels.keySet()) {
                VarItem varItem = new VarItem();
                String vName = varOpt.getName();
                varItem.setLabelValue(varOpt.isLabelValue());
                varItem.setName(vName);
                varItem.setLabel(varOpt.getDescription());
                varItem.setSelected(currentConfig == null ? false : vName.equals(currentConfig.get(varName)));
                CharacteristicValuePrice cvp = fdProd.getPricing().findCharacteristicValuePrice(varName, vName, pCtx);
                varItem.setCvp(cvp == null ? "0" : JspMethods.formatPrice(cvp.getPrice()));
                ProductModel productModel = varOptProductModels.get(varOpt);
                if (productModel != null) {
                    Image featureImage = varOptProductModels.get(varOpt).getDetailImage();
                    if (featureImage != null) {
                        varItem.setImagePath(featureImage.getPath());
                    }
                }
                varOpts.add(varItem);
            }
            var.setValues(varOpts);
            varList.add(var);
        }
        return varList;
    }

    private static Map<FDVariationOption, ProductModel> collectAvailableVariations(FDVariation fdVar, ContentFactory contentFactory) {
        Map<FDVariationOption, ProductModel> availableProductModels = new LinkedHashMap<FDVariationOption, ProductModel>();
        FDVariationOption[] varOpts = fdVar.getVariationOptions();
        for (FDVariationOption varOpt : varOpts) {
            String optSkuCode = varOpt.getSkuCode();

            // sometimes skucode attrib in erps may be missing..so handle
            // it, so we don't get SkuNotFoundException
            if (optSkuCode != null && !"".equals(optSkuCode.trim())) {
                ProductModel productModel = null;
                try {
                    productModel = contentFactory.getProduct(optSkuCode);
                } catch (FDSkuNotFoundException e) {
                    LOG.error(e);
                }
                if (productModel == null) {
                    LOG.debug("Variation has orphan sku with no ProductModel): " + varOpt + " (Sku: " + optSkuCode + ")");
                } else {
                    SkuModel sku = productModel.getSku(optSkuCode);
                    if (sku != null && !sku.isUnavailable()) {
                        availableProductModels.put(varOpt, productModel);
                    } else {
                        if (sku.isUnavailable()) {
                            LOG.debug("Skipping as unavailable: " + varOpt + " (Sku: " + optSkuCode + ")");
                        }
                    }
                }
            } else {
                // not a product, add to list
                availableProductModels.put(varOpt, null);
            }
        }

        return availableProductModels;
    }

    /**
     * This utility method not just populates {@link SkuData#variations} field but also the {@link SkuData#variationDisplay} indicator
     *
     * @param data
     * @param variations
     */
    public static void populateSkuVariations(SkuData data, List<Variation> variations) {
        if (data == null || variations == null)
            return;

        data.setVariations(variations);

        for (Variation v : variations) {
            if (v.getValues() != null && v.getValues().size() > 1) {
                data.setVariationDisplay(true);
                break;
            }
        }
    }

    /**
     * Produce lightweight potato data
     *
     * @param user
     * @param product
     *
     * @return
     *
     * @throws HttpErrorResponse
     * @throws FDResourceException
     * @throws FDSkuNotFoundException
     */
    public static ProductData createProductDataLight(FDUserI user, ProductModel product) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
        // Create response data object
        ProductData data = new ProductData();
        populateProductDataLight(user, product, data);

        return data;
    }

    public static void populateProductDataLight(FDUserI user, ProductModel product, ProductData data) throws FDResourceException, HttpErrorResponse, FDSkuNotFoundException {
        // Episode I - DO THE MAGIC / PREPARATIONS

        if (!(product instanceof ProductModelPricingAdapter)) {
            // wrap it into a pricing adapter if naked
            product = ProductPricingFactory.getInstance().getPricingAdapter(product);
        }

        PriceCalculator priceCalculator = product.getPriceCalculator();
        if (priceCalculator == null) {
            BaseJsonServlet.returnHttpError(500, "priceCalculator does not exist for this product");
        }

        SkuModel sku = null;
        if (product.getSkus() != null && product.getSkus().size() > 0) {
            // just pick the first
            sku = product.getSku(0);
        }

        // Episode II - POPULATE DATA
        LOG.debug("Product[" + product.getContentKey().getId() + "] with SKU[" + (sku != null ? sku.getContentKey().getId() : "null") + "] is considered incomplete");

        data.setIncomplete(true);

        // Populate product basic-level data
        populateBasicProductData(data, user, product);

        if (sku != null) {
            try {
                FDProductInfo productInfo_fam = sku.getProductInfo();
                FDProduct fdProduct = sku.getProduct();
                populateProductData(data, user, product, sku, fdProduct, priceCalculator, null, true, true);
                populatePricing(data, fdProduct, productInfo_fam, priceCalculator, user);
            } catch (FDResourceException exc) {
                LOG.debug("Pricing and ERPS parts of " + product.getContentKey() + " are not populated due to missing resource", exc);
            } catch (FDSkuNotFoundException exc) {
                if (FDStoreProperties.getPreviewMode()) {
                    LOG.debug("Populating partial product data for preview (always available) for " + product.getContentKey() + ", as ERPS data is missing.");
                    
                    populateSimpleProductData(data, product, sku, true);
                    populateRatings(data, user, product, sku.getSkuCode());
                    populateBursts(data, user, product, priceCalculator, true);
                    populateScores(data, user, product);
                    populateQuantity(data, user, product, null);

                    // Minimal set of properties for preview mode, that makes the product look like available without breaking the page
                    data.setAvailable(true);
                    data.setAvailableQty(0);
                    data.setSalesUnit(Collections.<SalesUnit>emptyList());
                    data.setSalesUnitDescrPDP("");
                } else {
                    LOG.debug("Pricing and ERPS parts of " + product.getContentKey() + " are not populated due to missing ERPS data", exc);
                }
            }

            // Populate transient-data
            postProcessPopulate(user, data, sku.getSkuCode());
        }
    }

    private static String fetchMedia(String mediaPath, FDUserI user, boolean quoted) throws IOException, TemplateException {
        if (mediaPath == null)
            return null;

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

    public static void populateAvailabilityMessagesForMobile(ProductData item, ProductModel productModel, FDProduct fdProduct, SkuModel sku) {
        populateAvailabilityMessages(item, productModel, fdProduct, sku);
    }
}
