package com.freshdirect.webapp.ajax;

import java.util.ArrayList;
import java.util.Collections;
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
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
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
import com.freshdirect.webapp.ajax.recommendation.RecommendationRequestObject;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;
import com.freshdirect.webapp.ajax.viewcart.data.ViewCartCarouselData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.smartstore.Impression;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.ProductRecommenderUtil;
import com.freshdirect.webapp.util.RecommendationsCache;

public abstract class AbstractCarouselService {

    private static final Logger LOGGER = LoggerFactory.getInstance(AbstractCarouselService.class);

    protected static final String PARENT_IMPRESSION_ID_POSTFIX = "_parentImpressionId";
    protected static final String SELECTED_SITE_FEATURE_POSTFIX = "_selectedSiteFeature";

    /**
     * Return tab recommendation variant
     * 
     * @return
     */
    protected abstract Variant getTabVariant();

	/**
	 * Maximum number of tabs
	 * 
	 * @return
	 */
	protected abstract int getMaxTabs();

	/**
	 * Maximum number of recommended items to show
	 * 
	 * @return
	 */
	protected abstract int getMaxRecommendations();

	/**
	 * Name of facility (or sub-site feature name)
	 * 
	 * @return
	 */
	protected abstract String getSmartStoreFacilityName();

	/**
	 * Return true when empty tabs should not be shown
	 * 
	 * @return
	 */
	protected abstract boolean shouldConsolidateEmptyTabs();

    protected abstract int getSelectedTab(TabRecommendation tabs, HttpSession session, ServletRequest request, FDUserI user);

    protected abstract List<String> getSiteFeatures(FDUserI user);

    protected abstract TabRecommendation getTabRecommendation(HttpServletRequest request, FDUserI user, SessionInput input);

    protected abstract String getEventSource(String siteFeature, FDUserI user);

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
     * 
     * @throws FDResourceException
     */
    public CarouselData doGenericRecommendation(HttpSession session, HttpServletRequest request, FDSessionUser user, Variant variant,
            String parentImpressionId,
            String parentVariantId) throws FDResourceException {
        Recommendations recommendations = getRecommendations(variant, request, session, parentImpressionId, user);

        if (recommendations == null || recommendations.getProducts().isEmpty()) {
            if (FDStoreProperties.isLogRecommenderResults()) {
                LOGGER.debug("Return empty result");
            }
        }

        if (recommendations != null && !recommendations.isLogged()) {
            logImpressions(recommendations, user, request, parentImpressionId, parentVariantId);
            logRecommenderResults(recommendations.getProducts());
        }

        EnumSiteFeature siteFeature = variant.getSiteFeature();
        CarouselData carousel = null;
        if (recommendations != null) {
            carousel = CarouselService.defaultService().createCarouselData(null, siteFeature.getName(), recommendations.getAllProducts(), user,
                    getEventSource(siteFeature.getName(), user), recommendations.getVariant().getId());
        } else {
            carousel = CarouselService.defaultService().createCarouselData(null, siteFeature.getName(), Collections.<ProductModel> emptyList(), user,
                    getEventSource(siteFeature.getName(), user), variant.getId());
        }
        return carousel;
	}

    public RecommendationTab getRecommendationTab(HttpServletRequest request, FDUserI user, RecommendationRequestObject requestData)
            throws FDResourceException {
        HttpSession session = request.getSession();
        String siteFeature = requestData.getFeature();
        String parentImpressionId = requestData.getParentImpressionId();
        session.setAttribute(requestData.getParentVariantId() + SELECTED_SITE_FEATURE_POSTFIX, siteFeature);
        session.setAttribute(requestData.getParentVariantId() + PARENT_IMPRESSION_ID_POSTFIX, parentImpressionId);
        EnumSiteFeature enumSiteFeature = EnumSiteFeature.getEnum(siteFeature);
        Variant variant = VariantSelectorFactory.getSelector(enumSiteFeature).select(user, false);
        String impressionId = requestData.getImpressionId();
        String parentVariantId = requestData.getParentVariantId();

        if (impressionId != null) {
            Impression.tabClick(impressionId);
        }

        RecommendationTab recommendationTab = new RecommendationTab(getTitleForVariant(variant), enumSiteFeature.getName()).setParentImpressionId(parentVariantId)
                .setImpressionId(impressionId).setParentVariantId(parentVariantId).setDescription(getDescription(variant)).setSelected(requestData.isSelected())
                .setProductSamplesReacedMaximumItemQuantity(user.getShoppingCart().isMaxSampleReached());
        if (requestData.isSelected()) {
            recommendationTab.setCarouselData(doGenericRecommendation(session, request, (FDSessionUser) user, variant, parentImpressionId, parentVariantId));
        }
        return recommendationTab;
    }

    // TODO remove this if with APPDEV-6161 is released
    public ViewCartCarouselData populateViewCartTabsRecommendationsAndCarouselSampleCarousel(HttpServletRequest request, FDSessionUser user, SessionInput input)
            throws FDResourceException {
        ViewCartCarouselData result = new ViewCartCarouselData();
        if (!FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user)) {
        if (FDStoreProperties.isPropDonationProductSamplesEnabled()) {
            try {
                result.addRecommendationTab(populateViewCartPageDonationProductSampleCarousel(request));
            } catch (Exception e) {
                LOGGER.error("Error while populating sample carousels", e);
            }
        } else {
            try {
                result.addRecommendationTab(populateViewCartPageProductSampleCarousel(request));
            } catch (Exception e) {
                LOGGER.error("Error while populating donation carousels", e);
            }
        }
        return result;
    } else {
        return populateViewCartTabsRecommendationsAndCarousel(request, user, input);
    }
    }

	/**
     * Populate view cart carousel tabs, gets recommendations and generate carousels.
     * 
     * @param request
     * @return view cart specific JSON compatible carousel definition
     * @throws InvalidContentKeyException
     * @throws FDResourceException
     */
    public ViewCartCarouselData populateViewCartTabsRecommendationsAndCarousel(HttpServletRequest request, FDSessionUser user, SessionInput input)
            throws FDResourceException {
        ViewCartCarouselData result = new ViewCartCarouselData();

        final int maxTabs = getMaxTabs();
        HttpSession session = request.getSession();

        TabRecommendation tabs = getTabRecommendation(request, user, input);

		if (input.getPreviousRecommendations() != null) {
			session.setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, input.getPreviousRecommendations());
		}

		if (tabs.size() > 0) {
			user.logTabImpression(tabs.getTabVariant().getId(), tabs.size());
			if (tabs.size() < maxTabs) {
				LOGGER.warn("not enough variants (" + tabs.size() + ") for " + maxTabs + " tabs.");
			}

			Impression impression = Impression.get(user, request, getSmartStoreFacilityName());
			String impressionId = impression.logFeatureImpression(null, null, tabs.getTabVariant(), input.getCategory(), input.getCurrentNode(), input.getYmalSource());
			for (int i = 0; i < tabs.size(); i++) {
				String tabImpression = impression.logTab(impressionId, i, tabs.get(i).getSiteFeature().getName());
				tabs.setFeatureImpressionId(i, tabImpression);
			}

            if (tabs.getParentImpressionId() == null) {
                tabs.setParentImpressionId(impressionId);
            }

            final int selectedTab = getSelectedTab(tabs, session, request, user);
			Variant selectedVariant = tabs.get(selectedTab);
            String parentImpressionId = tabs.getParentImpressionId();
			String parentVariantId = tabs.getTabVariant().getId();
			int tabIndex = 0;
			for (Variant variant : tabs.getVariants()) {
                String tabTitle = WordUtils.capitalizeFully(getTitleForVariant(variant));
				String siteFeatureName = variant.getSiteFeature().getName();
                RecommendationTab tab = new RecommendationTab(tabTitle, siteFeatureName).setParentImpressionId(parentImpressionId)
                        .setImpressionId(tabs.getFeatureImpressionId(tabIndex)).setParentVariantId(parentVariantId).setDescription(getDescription(variant))
                        .setSelected(variant.equals(selectedVariant)).setProductSamplesReacedMaximumItemQuantity(user.getShoppingCart().isMaxSampleReached());
				result.getRecommendationTabs().add(tab);
				tabIndex++;
			}
			final boolean consolidate = shouldConsolidateEmptyTabs();

			if (consolidate) {
				// APPBUG-2752 Get recommendations for all tabs in order to remove empty ones
				for (int i=0; i<tabs.size(); i++) {
                    result.getRecommendationTabs().get(i).setCarouselData(doGenericRecommendation(session, request, user, tabs.get(i), parentImpressionId, parentVariantId));
				}
			} else {
				// APPBUG-2752 Get recommendations for only the selected (visible) tab
                result.getRecommendationTabs().get(selectedTab)
                        .setCarouselData(doGenericRecommendation(session, request, user, selectedVariant, parentImpressionId, parentVariantId));
			}

            // post-process tabs
            if (consolidate) {
                result.setRecommendationTabs(removeEmptyTabs(result.getRecommendationTabs(), user));
            }

		}
		return result;
	}

    private List<RecommendationTab> removeEmptyTabs(List<RecommendationTab> tabs, FDUserI user) {
        List<RecommendationTab> recommendationTabs = new ArrayList<RecommendationTab>(tabs.size());

        for (RecommendationTab tab : tabs) {
            if (tab.getCarouselData() == null || tab.getCarouselData().getProducts().isEmpty()) {
                LOGGER.warn("Removing tab " + tab.getSiteFeature());
            } else {
                recommendationTabs.add(tab);
            }
        }

        boolean isAnyTabSelected = false;
        for (RecommendationTab recommendationTab : recommendationTabs) {
            if (recommendationTab.isSelected()) {
                isAnyTabSelected = true;
                break;
            }
        }
        if (!isAnyTabSelected && !recommendationTabs.isEmpty()) {
            recommendationTabs.get(0).setSelected(true);
        }
        return recommendationTabs;
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

    @SuppressWarnings("deprecation")
    public String getDescription(Variant variant) {
        String description = variant.getServiceConfig().getPresentationDescription();
        if (description == null) {
            EnumSiteFeature siteFeature = variant.getSiteFeature();
            description = siteFeature.getPresentationDescription();
        }
        return description;
    }

	// Utility methods

	private Variant getOverriddenVariant(Variant v, FDUserI user) throws FDResourceException {
		Variant v2 = VariantSelectorFactory.getSelector(v.getSiteFeature()).select(user);
		return v2 != null ? v2 : v;
	}

	private void collectRequestId(HttpServletRequest request, Recommendations recommendations, FDUserI user) {
		if (recommendations.getAllProducts().size() > 0) {
			Impression imp = Impression.get(user, request, getSmartStoreFacilityName());
			recommendations.setRequestId(imp.getRequestId());
		}
	}

	// recommendation methods
    public SessionInput createSessionInput(FDUserI user, HttpServletRequest request) {
		SessionInput sessionInput = new SessionInput(user);
        sessionInput.setPreviousRecommendations((Map) request.getSession().getAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS));
		FDStoreRecommender.initYmalSource(sessionInput, user, request);
		sessionInput.setCurrentNode(sessionInput.getYmalSource());
		sessionInput.setMaxRecommendations(getMaxRecommendations());
		return sessionInput;
	}

    private Recommendations extractRecommendations(HttpSession session, HttpServletRequest request, EnumSiteFeature siteFeature, FDUserI user) throws FDResourceException {
        SessionInput sessionInput = createSessionInput(user, request);
		Recommendations recommendations = ProductRecommenderUtil.doRecommend(user, siteFeature, sessionInput);
		collectRequestId(request, recommendations, user);
		return recommendations;
	}

    private Recommendations getCachedRecommendations(Variant oVariant, HttpSession session, HttpServletRequest request, String cacheId, FDUserI user) throws FDResourceException {
        RecommendationsCache cache = RecommendationsCache.getCache(session);
        Recommendations r = cache.get(cacheId, oVariant);
        if (r == null) {
            r = extractRecommendations(session, request, oVariant.getSiteFeature(), user);
            if (cacheId != null){
              cache.store(cacheId, oVariant, r);
            }
        }
		return r;
	}

    private Recommendations getRecommendations(Variant variant, HttpServletRequest request, HttpSession session, String cacheId, FDUserI user) throws FDResourceException {
		if (variant == null) {
			return null;
		}
        Recommendations recommendations = getCachedRecommendations(getOverriddenVariant(variant, user), session, request, cacheId, user);
		if (recommendations == null) {
			throw new FDResourceException("Recommendation cache not found!");
		}
		persistToSession(recommendations, session);
		return recommendations;
	}

	// log methods

	protected void logRecommenderResults(List<ProductModel> products) {
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

	protected void logImpressions(Recommendations recommendations, FDSessionUser user, HttpServletRequest request, String parentImpressionId, String parentVariantId) {
		if (recommendations.getProducts().size() > 0) {
			user.logImpression(recommendations.getVariant().getId(), recommendations.getProducts().size());
		}
		for (ProductModel p : recommendations.getProducts()) {
			FDEventUtil.logRecommendationImpression(recommendations.getVariant().getId(), p.getContentKey());
		}
		if (ImpressionLogger.isGlobalEnabled()) {
			Impression imp = Impression.get(user, request, getSmartStoreFacilityName());
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

	// misc

	protected void persistToSession(Recommendations recommendations, HttpSession session) {
		Map<String, List<ContentKey>> previousRecommendations = recommendations.getPreviousRecommendations();
		if (previousRecommendations != null) {
			session.setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, previousRecommendations);
		}
	}

    protected boolean isUserAlreadyOrdered(FDUserI user) {
        boolean currentUser = false;
        try {
            currentUser = user.getLevel() > FDUserI.RECOGNIZED && user.getAdjustedValidOrderCount() >= 3;
        } catch (FDResourceException e) {
            currentUser = false;
        }
        return currentUser;
    }

    protected boolean isMaxSampleReached(FDCartI cart) {
        int numberOfFreeSampleProducts = 0;
        int eligibleQuantity = FDStoreProperties.getProductSamplesMaxBuyProductsLimit();
        for (FDCartLineI orderLine : cart.getOrderLines()) {
            if (null != orderLine.getDiscount() && orderLine.getDiscount().getDiscountType().equals(EnumDiscountType.FREE)) {
                numberOfFreeSampleProducts++;
            }
        }
        return numberOfFreeSampleProducts >= eligibleQuantity;
    }

    protected int selectedTab(List<Variant> variants, String selectedSiteFeature) {
        int selected = 0;
        for (int i = 0; i < variants.size(); i++) {
            if (variants.get(i).getSiteFeature().getName().equals(selectedSiteFeature)) {
                selected = i;
                break;
            }
        }
        return selected;
    }

    // TODO remove this method with APPDEV-6161 is released
    public RecommendationTab populateViewCartPageProductSampleCarousel(HttpServletRequest request) throws Exception {
        CarouselData carouselData = new CarouselData();
        FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
        /* make title configurable */
        String prodSampelsTitle = FDStoreProperties.getProductSamplesTitle().replaceAll("%%N%%", Integer.toString(FDStoreProperties.getProductSamplesMaxBuyProductsLimit()))
                .replaceAll("%%Q%%", Integer.toString(FDStoreProperties.getProductSamplesMaxQuantityLimit()));
        RecommendationTab tab = new RecommendationTab(prodSampelsTitle, RecommendationTab.PRODUCT_SAMPLE_SITE_FEATURE).setSelected(true);
        tab.setCarouselData(carouselData);
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
        List<ProductReference> productSamples = user.getProductSamples();
        tab.setProductSamplesReacedMaximumItemQuantity(productSamplesInCart.size() >= FDStoreProperties.getProductSamplesMaxBuyProductsLimit());

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
                    // pd.getQuantity().setqMax(FDStoreProperties.getProductSamplesMaxQuantityLimit());
                    pd.getQuantity().setqMax(1);
                    populateCartAmountByProductSample(pd, orderLinesSkuCodeWithQuantity.get(skuCode));
                } catch (FDSkuNotFoundException e) {
                    LOGGER.warn("Sku not found: " + skuCode, e);
                }
                sampleProducts.add(pd);
            }
        }
        carouselData.setProducts(sampleProducts);
        tab.setCarouselData(carouselData);

        return tab;
    }

    // TODO remove this method with APPDEV-6161 is released
    private void populateCartAmountByProductSample(ProductData pd, Double quantity) {
        if (pd.getInCartAmount() > 1) {// FDStoreProperties.getProductSamplesMaxQuantityLimit()) {
            if (quantity <= 1) {// FDStoreProperties.getProductSamplesMaxQuantityLimit()) {
                pd.setInCartAmount(quantity);
            } else {
                pd.setInCartAmount(0.0);
            }
        }
    }

    // TODO remove this method with APPDEV-6161 is released
    public RecommendationTab populateViewCartPageDonationProductSampleCarousel(HttpServletRequest request) throws Exception {
        CarouselData carouselData = new CarouselData();
        RecommendationTab tab = new RecommendationTab("Donate to Grand Giving! For every $1 donated, 5 meals will be given to a family in need.",
                RecommendationTab.DONATION_SAMPLE_SITE_FEATURE).setSelected(true);
        tab.setCarouselData(carouselData);
        FDSessionUser user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
        List<ProductData> sampleProducts = new ArrayList<ProductData>();
        List<FDCartLineI> productSamplesInCart = new ArrayList<FDCartLineI>();
        Map<String, Double> orderLinesSkuCodeWithQuantity = new HashMap<String, Double>();
        FDCartModel cart = user.getShoppingCart();
        List<FDCartLineI> orderLines = cart.getOrderLines();
        List<ProductModel> productModels = new ArrayList<ProductModel>();
        productModels = getProductModelsFromSku();
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
                    // pd.getQuantity().setqMax(FDStoreProperties.getProductSamplesMaxQuantityLimit());
                    // pd.getQuantity().setqMax(1);
                    // populateCartAmountByProductSample(pd, orderLinesSkuCodeWithQuantity.get(skuCode));
                } catch (FDSkuNotFoundException e) {
                    LOGGER.warn("Sku not found: " + skuCode, e);
                }
                /*
                 * if (FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.checkout2_0, request.getCookies(), user)) { 547
                 * pd.setAvailable(!productSamplesMaxBuyProductsLimitReaced); 548 }
                 */
                sampleProducts.add(pd);
            }
        }
        carouselData.setProducts(sampleProducts);
        tab.setCarouselData(carouselData);

        return tab;
    }

    // TODO remove this method with APPDEV-6161 is released
    private List<ProductModel> getProductModelsFromSku()
            throws FDSkuNotFoundException {
        List<ProductModel> productModels = new ArrayList<ProductModel>();
        List<String> productIds = FDStoreProperties.getPropDonationProductSamplesId();
        // String[] productIds=null != FDStoreProperties.getPropDonationProductSamplesId() ? FDStoreProperties.getPropDonationProductSamplesId().split(",") : null;
        if (productIds != null) {
            for (String productId : productIds) {
                try {
                    ProductModel productModel = ContentFactory.getInstance().getProduct(productId);
                    productModels.add(productModel);
                } catch (FDSkuNotFoundException e) {
                    LOGGER.warn("Sku not found: " + productId, e);
                }
            }
        }
        return productModels;
    }

}
