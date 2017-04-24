package com.freshdirect.webapp.ajax.viewcart.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.event.ImpressionLogger;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.CartTabRecommender;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.data.CarouselData;
import com.freshdirect.webapp.ajax.browse.service.CarouselService;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.viewcart.data.ProductSamplesCarousel;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.smartstore.Impression;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.ProductRecommenderUtil;
import com.freshdirect.webapp.util.RecommendationsCache;

/**
 * View cart carousel service built on the following legacy components:
 * <ul>
 * <li>PIPTabTag</li>
 * <li>GenericRecommendationsTag</li>
 * <li>view_cart.jsp[i_recommender_tabs.jspf[i_generic_recommendations.jspf]]</li>
 * <li>refresh.jsp</li>
 * </ul>
 * 
 * Most of the methods copied into this service as is.
 * 
 * @author pkovacs
 *
 */
public class ViewCartCarouselService {

    private static ViewCartCarouselService INSTANCE = new ViewCartCarouselService();

    private static final Logger LOGGER = LoggerFactory.getInstance(ViewCartCarouselService.class);

    private static final int MAX_RECOMMENDATIONS = 5;
    private static final int MAX_TABS = 3;
    private static final String SMART_STORE_FACILILTY = "view_cart";
    
    public static final String CAROUSEL_PRODUCT_DONATIONS_SITE_FEATURE = "PRODUCT_DONATIONS";
    public static final String CAROUSEL_PRODUCT_DONATIONS_TAB_TITLE = "Donation Samples";
    public static final String CAROUSEL_PRODUCT_SAMPLES_SITE_FEATURE = "PRODUCT_SAMPLES";
    public static final String CAROUSEL_PRODUCT_SAMPLES_TAB_TITLE = "Free Sample!";

    private ViewCartCarouselService() {
    }

    /**
     * Gives the default view cart carousel service.
     * 
     * @return the default service instance
     */
    public static ViewCartCarouselService defaultService() {
        return INSTANCE;
    }

    /**
     * Calculates recommendations for variant-user pair and populate recommendation tab with carousel.
     * <p>
     * This is the endpoint for AJAX calls.
     * </p>
     * 
     * @param session
     * @param request
     * @param user
     * @param recommendationTab
     * @param variant
     * @param parentImpressionId
     * @param parentVariantId
     * @throws FDResourceException
     * @throws InvalidContentKeyException
     */
    public void doGenericRecommendation(HttpSession session, HttpServletRequest request, FDSessionUser user, RecommendationTab recommendationTab, Variant variant,
            String parentImpressionId, String parentVariantId) throws FDResourceException, InvalidContentKeyException {
        Recommendations recommendations = getRecommendations(variant, request, session, parentImpressionId);

        if (recommendations == null || recommendations.getProducts().isEmpty()) {
            if (FDStoreProperties.isLogRecommenderResults()) {
                LOGGER.debug("Return empty result");
            }
            recommendations = null;
        }

        if (recommendations != null && !recommendations.isLogged()) {
            logImpressions(recommendations, user, request, parentImpressionId, parentVariantId);
            logRecommenderResults(recommendations.getProducts());
        }

        EnumSiteFeature siteFeature = variant.getSiteFeature();
        CarouselData carousel = CarouselService.defaultService().createCarouselData(null, siteFeature.getName(), recommendations.getAllProducts(), user, "",
                recommendations.getVariant().getId());
        recommendationTab.setCarouselData(carousel);
    }

    /**
     * Calculates the title based on variant or site feature.
     * 
     * @param variant
     * @return title of site feature or variant
     */
    @SuppressWarnings("deprecation")
    public String getTitleForVariant(Variant variant) {
        String prezTitle = variant.getServiceConfig().getPresentationTitle();
        if (prezTitle == null) {
            EnumSiteFeature siteFeature = variant.getSiteFeature();
            prezTitle = siteFeature.getPresentationTitle();
            if (prezTitle == null)
                prezTitle = siteFeature.getTitle();
            if (prezTitle == null)
                prezTitle = siteFeature.getName();
        }
        if (!variant.isSmartSavings() || prezTitle.toLowerCase().startsWith("save on "))
            return prezTitle;

        return "Save on " + prezTitle;
    }

    /**
     * Populate view cart carousel tabs, gets recommendations and generate carousels.
     * 
     * @param request
     * @return view cart specific JSON compatible carousel definition
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public ViewCartCarouselData populateViewCartTabsRecommendationsAndCarousel(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        FDSessionUser user = (FDSessionUser) getUserFromSession(session);
        ViewCartCarouselData result = new ViewCartCarouselData();
        SessionInput input = createSessionInput(session, request);
        TabRecommendation tabs = CartTabRecommender.recommendTabs(user, input);
        if (input.getPreviousRecommendations() != null) {
            session.setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, input.getPreviousRecommendations());
        }

        if (input.getSavingsVariantId() != null) {
            user.setSavingsVariantId(input.getSavingsVariantId());
        }
        if (tabs.size() > 0) {
            user.logTabImpression(tabs.getTabVariant().getId(), tabs.size());
            if (tabs.size() < MAX_TABS) {
                LOGGER.warn("not enough variants (" + tabs.size() + ") for " + MAX_TABS + " tabs.");
            }

            Impression impression = Impression.get(user, request, SMART_STORE_FACILILTY);
            String impressionId = impression.logFeatureImpression(null, null, tabs.getTabVariant(), input.getCategory(), input.getCurrentNode(), input.getYmalSource());
            tabs.setParentImpressionId(impressionId);
            for (int i = 0; i < tabs.size(); i++) {
                String tabImpression = impression.logTab(impressionId, i, tabs.get(i).getSiteFeature().getName());
                tabs.setFeatureImpressionId(i, tabImpression);
            }
            int selectedTab = getSelectedTab(tabs, session, request);
            tabs.setSelected(selectedTab);
            Variant genericRecommendationsVariant = tabs.get(selectedTab);
            String parentImpressionId = impressionId;
            String parentVariantId = tabs.getTabVariant().getId();
            int tabIndex = 0;
            for (Variant variant : tabs.getVariants()) {
                String tabTitle = tabs.getTabTitle(tabIndex);
                tabTitle = WordUtils.capitalizeFully(tabTitle);
                String siteFeatureName = variant.getSiteFeature().getName();
                RecommendationTab tab = new RecommendationTab(tabTitle, siteFeatureName, tabs.getParentImpressionId(), tabs.getFeatureImpressionId(tabIndex), parentVariantId);
                result.getRecommendationTabs().add(tab);
                tabIndex++;
            }
            doGenericRecommendation(session, request, user, result.getRecommendationTabs().get(selectedTab), genericRecommendationsVariant, parentImpressionId, parentVariantId);
        }
        return result;
    }

    public ProductSamplesCarousel populateViewCartPageProductSampleCarousel(HttpServletRequest request) throws Exception {
        CarouselData carouselData = new CarouselData();
        /* make title configurable */
        String prodSampelsTitle = FDStoreProperties.getProductSamplesTitle()
        		.replaceAll("%%N%%", Integer.toString( FDStoreProperties.getProductSamplesMaxBuyProductsLimit() ))
        		.replaceAll("%%Q%%", Integer.toString( FDStoreProperties.getProductSamplesMaxQuantityLimit() ));
        ProductSamplesCarousel tab = new ProductSamplesCarousel(prodSampelsTitle, ViewCartCarouselService.CAROUSEL_PRODUCT_SAMPLES_SITE_FEATURE.toString(), "", "", "");
        tab.setCarouselData(carouselData);
        FDSessionUser user = (FDSessionUser) getUserFromSession(request.getSession());
        List<ProductData> sampleProducts = new ArrayList<ProductData>();
        List<FDCartLineI> productSamplesInCart = new ArrayList<FDCartLineI>();
        Map<String, Double> orderLinesSkuCodeWithQuantity = new HashMap<String, Double>();
        FDCartModel cart = user.getShoppingCart();
        List<FDCartLineI> orderLines = cart.getOrderLines();
        if (null != orderLines && !orderLines.isEmpty()) {
            for (FDCartLineI orderLine : orderLines) {
                if (orderLine.getSkuCode() != null) {
                    // TODO: THE NPE POSSIBILITY NEEDS TO BE CHECKED IN CODE AND DATABASE! IT SEEMS TO BE A DATA ISSUE
                    // original line: orderLinesSkuCodeWithQuantity.put(orderLine.getProductRef().lookupProductModel().getDefaultSku().getSkuCode(), orderLine.getQuantity());
                    // issue with PROD user: johannarfarina@gmail.com
                    orderLinesSkuCodeWithQuantity.put(orderLine.getSkuCode(), orderLine.getQuantity());
                }
                if (null != orderLine.getDiscount() && orderLine.getDiscount().getDiscountType().equals(EnumDiscountType.FREE)) {
                    productSamplesInCart.add(orderLine);
                    orderLine.setErpOrderLineSource(EnumEventSource.ps_caraousal);
                    carouselData.setCmEventSource(EnumEventSource.ps_caraousal.toString());
                }
            }
        }
        List<ProductReference> productSamples = new ArrayList<ProductReference>();
        boolean productSamplesMaxBuyProductsLimitReaced = productSamplesInCart.size() >= FDStoreProperties.getProductSamplesMaxBuyProductsLimit();
        if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user)) {
            tab.setProductSamplesReacedMaximumItemQuantity(productSamplesMaxBuyProductsLimitReaced);
            productSamples = user.getProductSamples();
        } else {
            if (!productSamplesMaxBuyProductsLimitReaced) {
                productSamples = user.getProductSamples();
            }
        }
        for (ProductReference productReference : productSamples) {
            ProductModel productModel = productReference.lookupProductModel();
            ProductData pd = new ProductData();
            SkuModel skuModel = null;
            if (!(productModel instanceof ProductModelPricingAdapter)) {
                // wrap it into a pricing adapter if naked
                productModel = ProductPricingFactory.getInstance().getPricingAdapter(productModel, user.getPricingContext());
            }
            if (skuModel == null) {
                skuModel = productModel.getDefaultSku();
            }
            if (skuModel != null) {
                String skuCode = skuModel.getSkuCode();
                try {
                    FDProductInfo productInfo_fam = skuModel.getProductInfo();
                    FDProduct fdProduct = skuModel.getProduct();
                    PriceCalculator priceCalculator = productModel.getPriceCalculator();
                    ProductDetailPopulator.populateBasicProductData(pd, user, productModel);
                    ProductDetailPopulator.populateProductData(pd, user, productModel, skuModel, fdProduct, priceCalculator, null, true, true);
                    ProductDetailPopulator.populatePricing(pd, fdProduct, productInfo_fam, priceCalculator, user);
                    try {
                        ProductDetailPopulator.populateSkuData(pd, user, productModel, skuModel, fdProduct);
                    } catch (FDSkuNotFoundException e) {
                        LOGGER.error("Failed to populate sku data", e);
                    } catch (HttpErrorResponse e) {
                        LOGGER.error("Failed to populate sku data", e);
                    }
                    ProductDetailPopulator.postProcessPopulate(user, pd, pd.getSkuCode());
//                    pd.getQuantity().setqMax(FDStoreProperties.getProductSamplesMaxQuantityLimit());
                    pd.getQuantity().setqMax(1);
                    populateCartAmountByProductSample(pd, orderLinesSkuCodeWithQuantity.get(skuCode));
                } catch (FDSkuNotFoundException e) {
                    LOGGER.warn("Sku not found: " + skuCode, e);
                }
                if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user)) {
                    pd.setAvailable(!productSamplesMaxBuyProductsLimitReaced);
                }
                sampleProducts.add(pd);
            }
        }
        carouselData.setProducts(sampleProducts);
        tab.setCarouselData(carouselData);

        return tab;
    }

    private void populateCartAmountByProductSample(ProductData pd, Double quantity) {
        if (pd.getInCartAmount() > 1){//FDStoreProperties.getProductSamplesMaxQuantityLimit()) {
            if (quantity <= 1){//FDStoreProperties.getProductSamplesMaxQuantityLimit()) {
                pd.setInCartAmount(quantity);
            } else {
                pd.setInCartAmount(0.0);
            }
        }
    }

    private void collectRequestId(HttpServletRequest request, Recommendations recommendations, FDUserI user) {
        if (recommendations.getAllProducts().size() > 0) {
            Impression imp = Impression.get(user, request, SMART_STORE_FACILILTY);
            recommendations.setRequestId(imp.getRequestId());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private SessionInput createSessionInput(HttpSession session, HttpServletRequest request) {
        FDUserI user = getUserFromSession(session);
        SessionInput sessionInput = new SessionInput(user);
        sessionInput.setPreviousRecommendations((Map) session.getAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS));
        FDStoreRecommender.initYmalSource(sessionInput, user, request);
        sessionInput.setCurrentNode(sessionInput.getYmalSource());
        sessionInput.setMaxRecommendations(MAX_RECOMMENDATIONS);
        return sessionInput;
    }

    private Recommendations extractRecommendations(HttpSession session, HttpServletRequest request, EnumSiteFeature siteFeature) throws FDResourceException {
        FDUserI user = getUserFromSession(session);
        SessionInput sessionInput = createSessionInput(session, request);
        Recommendations recommendations = ProductRecommenderUtil.doRecommend(user, siteFeature, sessionInput);
        collectRequestId(request, recommendations, user);
        return recommendations;
    }

    private Recommendations getCachedRecommendations(Variant oVariant, HttpSession session, HttpServletRequest request, String cacheId) throws FDResourceException {
        RecommendationsCache cache = RecommendationsCache.getCache(session);
        Recommendations r = cache.get(cacheId, oVariant);
        if (r == null) {
            r = extractRecommendations(session, request, oVariant.getSiteFeature());
            cache.store(cacheId, oVariant, r);
        }
        return r;
    }

    private Variant getOverriddenVariant(Variant v, FDUserI user) throws FDResourceException {
        Variant v2 = VariantSelectorFactory.getSelector(v.getSiteFeature()).select(user);
        return v2 != null ? v2 : v;
    }

    private Recommendations getRecommendations(Variant variant, HttpServletRequest request, HttpSession session, String cacheId) throws FDResourceException,
            InvalidContentKeyException {
        if (variant == null) {
            return null;
        }
        FDUserI user = getUserFromSession(session);
        Recommendations recommendations = getCachedRecommendations(getOverriddenVariant(variant, user), session, request, cacheId);
        if (recommendations == null) {
            throw new FDResourceException("Recommendation cache not found!");
        }
        persistToSession(recommendations, session);
        return recommendations;
    }

    private int getSelectedTab(TabRecommendation tabs, HttpSession session, ServletRequest request) {
        final int numTabs = tabs.size();
        int selectedTab = 0; // default value
        Object selectedTabAttribute = session.getAttribute(SessionName.SS_SELECTED_TAB);

        boolean shouldStoreTabPos = selectedTabAttribute == null; // true == not
        // stored yet
        if (selectedTabAttribute != null) {
            // get the stored one if exist
            selectedTab = ((Integer) selectedTabAttribute).intValue();
        }

        if (selectedTab == -1) {
            shouldStoreTabPos = true;
            // try to calculate a good tab index
            if (session.getAttribute(SessionName.SS_SELECTED_VARIANT) != null) {
                String tabId = (String) session.getAttribute(SessionName.SS_SELECTED_VARIANT);

                selectedTab = tabs.getTabIndex(tabId);
                if (selectedTab == -1 && tabId.indexOf(',') != -1) {
                    String[] variants = tabId.split(",");
                    for (int i = 0; i < variants.length && selectedTab == -1; i++) {
                        selectedTab = tabs.getTabIndex(variants[i]);
                        if (selectedTab != -1) {
                            session.setAttribute(SessionName.SS_SELECTED_VARIANT, variants[i]);
                        }
                    }
                }
            }
            // no success, fallback to 0
            if (selectedTab == -1) {
                selectedTab = 0;
            }
            // store in the session
        }

        // tab explicitly set
        String value = request.getParameter("tab");
        if (value != null && !"".equals(value)) {
            selectedTab = Integer.parseInt(value);
            shouldStoreTabPos = true;
        }

        if (selectedTab >= numTabs
                || (session.getAttribute(SessionName.SS_SELECTED_VARIANT) != null && !tabs.get(selectedTab).getId().equals(session.getAttribute(SessionName.SS_SELECTED_VARIANT)))) {
            // reset if selection is out of tab range or the variant of selected
            // tab has changed
            selectedTab = 0;
            shouldStoreTabPos = true;
        }

        Integer iSelectedTab = new Integer(selectedTab);
        if (shouldStoreTabPos) {
            // store changed tab position in session
            session.setAttribute(SessionName.SS_SELECTED_TAB, iSelectedTab);
            session.setAttribute(SessionName.SS_SELECTED_VARIANT, tabs.get(selectedTab).getId());
        }

        return selectedTab;
    }

    private FDUserI getUserFromSession(HttpSession session) {
        return (FDUserI) session.getAttribute(SessionName.USER);
    }

    private void logImpressions(Recommendations recommendations, FDSessionUser user, HttpServletRequest request, String parentImpressionId, String parentVariantId) {
        if (recommendations.getProducts().size() > 0) {
            user.logImpression(recommendations.getVariant().getId(), recommendations.getProducts().size());
        }
        for (ProductModel p : recommendations.getProducts()) {
            FDEventUtil.logRecommendationImpression(recommendations.getVariant().getId(), p.getContentKey());
        }
        if (ImpressionLogger.isGlobalEnabled()) {
            Impression imp = Impression.get(user, request, SMART_STORE_FACILILTY);
            int rank = 1;
            Map<ContentKey, String> map = new HashMap<ContentKey, String>();
            String featureImpId = imp.logFeatureImpression(parentImpressionId, parentVariantId, recommendations);
            for (ProductModel p : recommendations.getProducts()) {
                String imp_id = imp.logProduct(featureImpId, rank, p.getContentKey(), recommendations.getRecommenderIdForProduct(p.getContentName()),
                        recommendations.getRecommenderStrategyIdForProduct(p.getContentName()));
                map.put(p.getContentKey(), imp_id);
                rank++;
            }
            recommendations.addImpressionIds(map);
        }
    }

    private void logRecommenderResults(List<ProductModel> products) {
        if (FDStoreProperties.isLogRecommenderResults()) {
            StringBuilder buf = new StringBuilder();
            buf.append("Result: [");
            int k = products.size();
            for (ProductModel obj : products) {
                buf.append(obj.getContentName());
                if (--k > 0)
                    buf.append(", ");
            }
            buf.append("]");
            LOGGER.debug(buf.toString());
        }
    }

    private void persistToSession(Recommendations recommendations, HttpSession session) {
        Map<String, List<ContentKey>> previousRecommendations = recommendations.getPreviousRecommendations();
        if (previousRecommendations != null) {
            session.setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, previousRecommendations);
        }
    }
    
    public ProductSamplesCarousel populateViewCartPageDonationProductSampleCarousel(HttpServletRequest request) throws Exception {
        CarouselData carouselData = new CarouselData();
        ProductSamplesCarousel tab = new ProductSamplesCarousel("Donate to Grand Giving! For every $1 donated, 5 meals will be given to a family in need.", ViewCartCarouselService.CAROUSEL_PRODUCT_DONATIONS_SITE_FEATURE.toString(), "", "", "");
        tab.setCarouselData(carouselData);
        FDSessionUser user = (FDSessionUser) getUserFromSession(request.getSession());
        List<ProductData> sampleProducts = new ArrayList<ProductData>();
        List<FDCartLineI> productSamplesInCart = new ArrayList<FDCartLineI>();
        Map<String, Double> orderLinesSkuCodeWithQuantity = new HashMap<String, Double>();
        FDCartModel cart = user.getShoppingCart();
        List<FDCartLineI> orderLines = cart.getOrderLines();
    /*    if (null != orderLines && !orderLines.isEmpty()) {
            for (FDCartLineI orderLine : orderLines) {
                if (orderLine.getSkuCode() != null) {
                    // TODO: THE NPE POSSIBILITY NEEDS TO BE CHECKED IN CODE AND DATABASE! IT SEEMS TO BE A DATA ISSUE
                    // original line: orderLinesSkuCodeWithQuantity.put(orderLine.getProductRef().lookupProductModel().getDefaultSku().getSkuCode(), orderLine.getQuantity());
                    // issue with PROD user: johannarfarina@gmail.com
                    orderLinesSkuCodeWithQuantity.put(orderLine.getSkuCode(), orderLine.getQuantity());
                    
                }
                if (null != orderLine.getDiscount() && orderLine.getDiscount().getDiscountType().equals(EnumDiscountType.FREE)) {
                    productSamplesInCart.add(orderLine);
                    orderLine.setErpOrderLineSource(EnumEventSource.ps_caraousal);
                }
            }
        }
*/    
        List<ProductModel> productModels=new ArrayList<ProductModel>();
        productModels = getProductModelsFromSku();   
        /*boolean productSamplesMaxBuyProductsLimitReaced = productSamplesInCart.size() >= FDStoreProperties.getPropDonationProductSamplesMaxBuyProductsLimit();
        if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user)) {
            tab.setProductSamplesReacedMaximumItemQuantity(productSamplesMaxBuyProductsLimitReaced);
            productModels = getProductModelsFromSku();       
        } else {
            if (!productSamplesMaxBuyProductsLimitReaced) {
            	productModels = getProductModelsFromSku();
            }
        }*/
        for (ProductModel productModel : productModels) {
            ProductData pd = new ProductData();
            SkuModel skuModel = null;
            if (!(productModel instanceof ProductModelPricingAdapter)) {
                // wrap it into a pricing adapter if naked
                productModel = ProductPricingFactory.getInstance().getPricingAdapter(productModel, user.getPricingContext());
            }
            if (skuModel == null) {
                skuModel = productModel.getDefaultSku();
            }
            if (skuModel != null) {
                String skuCode = skuModel.getSkuCode();
                try {
                    FDProductInfo productInfo_fam = skuModel.getProductInfo();
                    FDProduct fdProduct = skuModel.getProduct();
                    PriceCalculator priceCalculator = productModel.getPriceCalculator();
                    ProductDetailPopulator.populateBasicProductData(pd, user, productModel);
                    ProductDetailPopulator.populateProductData(pd, user, productModel, skuModel, fdProduct, priceCalculator, null, true, true);
                    ProductDetailPopulator.populatePricing(pd, fdProduct, productInfo_fam, priceCalculator, user);
                    try {
                        ProductDetailPopulator.populateSkuData(pd, user, productModel, skuModel, fdProduct);
                    } catch (FDSkuNotFoundException e) {
                        LOGGER.error("Failed to populate sku data", e);
                    } catch (HttpErrorResponse e) {
                        LOGGER.error("Failed to populate sku data", e);
                    }
                    ProductDetailPopulator.postProcessPopulate(user, pd, pd.getSkuCode());
                  //  pd.getQuantity().setqMax(FDStoreProperties.getProductSamplesMaxQuantityLimit());
                 //   pd.getQuantity().setqMax(1);
                  //  populateCartAmountByProductSample(pd, orderLinesSkuCodeWithQuantity.get(skuCode));
                } catch (FDSkuNotFoundException e) {
                    LOGGER.warn("Sku not found: " + skuCode, e);
                }
                /*if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user)) {
                    pd.setAvailable(!productSamplesMaxBuyProductsLimitReaced);
                }*/
                sampleProducts.add(pd);
            }
        }
        carouselData.setProducts(sampleProducts);
        tab.setCarouselData(carouselData);

        return tab;
    }

	private List<ProductModel> getProductModelsFromSku()
			throws FDSkuNotFoundException {
		List<ProductModel> productModels =  new ArrayList<ProductModel>();
		String[] productIds = FDStoreProperties.getPropDonationProductSamplesId().split(",");
		//String[] productIds=null != FDStoreProperties.getPropDonationProductSamplesId() ? FDStoreProperties.getPropDonationProductSamplesId().split(",") : null;
		if (productIds != null){
			for (String productId : productIds) {
				try{
				ProductModel productModel = ContentFactory.getInstance()
						.getProduct(productId);
				productModels.add(productModel);
			} catch (FDSkuNotFoundException e) {
				LOGGER.warn("Sku not found: " + productId, e);
				}
			} 
		}
		return productModels;
	}

}

