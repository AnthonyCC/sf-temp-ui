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
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.browse.data.CarouselData;
import com.freshdirect.webapp.ajax.browse.service.CarouselService;
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

    protected String getEventSource(String siteFeature, FDUserI user) {
        // TODO create site feature >> cm event source mapping
        return siteFeature;
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
		final int maxTabs = getMaxTabs();
		ViewCartCarouselData result = new ViewCartCarouselData();
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
					// LOGGER.warn(">>> TAB["+i+"], SF="+result.getRecommendationTabs().get(i).getSiteFeature()+", V="+tabs.get(i).getId() + ": " + result.getRecommendationTabs().get(i).getCarouselData() );
				}
			} else {
				// APPBUG-2752 Get recommendations for only the selected (visible) tab
                result.getRecommendationTabs().get(selectedTab)
                        .setCarouselData(doGenericRecommendation(session, request, user, selectedVariant, parentImpressionId, parentVariantId));
			}

			// post-process tabs
			if (consolidate) {
                result.setRecommendationTabs(removeEmptyTabs(result.getRecommendationTabs()));
			}

		}
		return result;
	}

    private List<RecommendationTab> removeEmptyTabs(List<RecommendationTab> tabs) {
        List<RecommendationTab> recommendationTabs = new ArrayList<RecommendationTab>(tabs.size());

        for (RecommendationTab tab : tabs) {
            if (tab.getCarouselData() == null || tab.getCarouselData().getProducts().isEmpty()) {
                LOGGER.warn("Removing tab " + tab.getSiteFeature() + " ...");
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

}
