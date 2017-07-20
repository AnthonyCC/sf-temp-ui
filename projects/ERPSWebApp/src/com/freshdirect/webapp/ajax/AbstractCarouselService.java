package com.freshdirect.webapp.ajax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.event.ImpressionLogger;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.VariantSelector;
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

    private static final String PARENT_IMPRESSION_ID_POSTFIX = "_parentImpressionId";
    public static final String SELECTED_SITE_FEATURE_POSTFIX = "_selectedSiteFeature";
    protected static final String PRODUCT_SAMPLE_SITE_FEATURE = "PRODUCT_SAMPLE";
    protected static final String DONATION_SAMPLE_SITE_FEATURE = "PRODUCT_DONATION";
    private static final List<String> PRODUCT_SAMPLE_GRID_SITE_FEAURES = Arrays.asList(PRODUCT_SAMPLE_SITE_FEATURE, DONATION_SAMPLE_SITE_FEATURE);

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

    protected abstract int getSelectedTab(TabRecommendation tabs, HttpSession session, ServletRequest request, FDUserI user);

    protected abstract List<String> getSiteFeatures(FDUserI user);

    protected abstract String getEventSource(String siteFeature, FDUserI user);

    protected abstract TabRecommendation getTabRecommendation(HttpServletRequest request, FDUserI user, SessionInput input);

    public void setSelectedSiteFeatureAttribute(HttpSession session, String siteFeature) {
        setSelectedSiteFeatureAttribute(session, getTabVariant().getId(), siteFeature);
    }

    public String getSelectedSiteFeatureAttribute(HttpSession session, String variantId) {
        return (String) session.getAttribute(variantId + SELECTED_SITE_FEATURE_POSTFIX);
    }

    public void setSelectedSiteFeatureAttribute(HttpSession session, String variantId, String siteFeature) {
        session.setAttribute(variantId + SELECTED_SITE_FEATURE_POSTFIX, siteFeature);
    }

    public String getParentImpressionIdAttribute(HttpSession session, String variantId) {
        return (String) session.getAttribute(variantId + PARENT_IMPRESSION_ID_POSTFIX);
    }

    public void setParentImpressionIdAttribute(HttpSession session, String variantId, String parentImpressionId) {
        session.setAttribute(variantId + PARENT_IMPRESSION_ID_POSTFIX, parentImpressionId);
    }

	/**
     * Populate recommendation tab with carousel.
     * 
     * @param session
     * @param request
     * @param user
     * @param variant
     * @param parentImpressionId
     * @param parentVariantId
     * @param recommendations
     * 
     * @throws FDResourceException
     */
    public CarouselData getCarouselData(HttpSession session, HttpServletRequest request, FDSessionUser user, Variant variant, String parentImpressionId,
            String parentVariantId, Recommendations recommendations) throws FDResourceException {

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
        setSelectedSiteFeatureAttribute(session, requestData.getParentVariantId(), siteFeature);
        setParentImpressionIdAttribute(session, requestData.getParentVariantId(), parentImpressionId);
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
            Recommendations recommendations = getRecommendations(variant, request, request.getSession(), parentImpressionId, user);
            recommendationTab.setCarouselData(getCarouselData(session, request, (FDSessionUser) user, variant, parentImpressionId, parentVariantId, recommendations));
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
    public ViewCartCarouselData populateTabsRecommendationsAndCarousel(HttpServletRequest request, FDSessionUser user, SessionInput input) throws FDResourceException {
        final int maxTabs = getMaxTabs();
        ViewCartCarouselData result = new ViewCartCarouselData();
        HttpSession session = request.getSession();

        TabRecommendation tabRecommendation = getTabRecommendation(request, user, input);

        if (input.getPreviousRecommendations() != null) {
            session.setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, input.getPreviousRecommendations());
        }

        if (tabRecommendation.size() > 0) {
            user.logTabImpression(tabRecommendation.getTabVariant().getId(), tabRecommendation.size());
            if (tabRecommendation.size() < maxTabs) {
                LOGGER.warn("not enough variants (" + tabRecommendation.size() + ") for " + maxTabs + " tabs.");
            }

            Impression impression = Impression.get(user, request, getSmartStoreFacilityName());
            String impressionId = impression.logFeatureImpression(null, null, tabRecommendation.getTabVariant(), input.getCategory(), input.getCurrentNode(), input.getYmalSource());
            for (int i = 0; i < tabRecommendation.size(); i++) {
                String tabImpression = impression.logTab(impressionId, i, tabRecommendation.get(i).getSiteFeature().getName());
                tabRecommendation.setFeatureImpressionId(i, tabImpression);
            }

            if (tabRecommendation.getParentImpressionId() == null) {
                tabRecommendation.setParentImpressionId(impressionId);
            }

            int selectedTab = getSelectedTab(tabRecommendation, session, request, user);

            String selectedSiteFeature = tabRecommendation.getSelectedSiteFeature();
            String updatedSiteFeature = tabRecommendation.get(selectedTab).getSiteFeature().getName();
            String parentImpressionId = tabRecommendation.getParentImpressionId();
            String parentVariantId = tabRecommendation.getTabVariant().getId();
            for (int tabIndex = 0; tabIndex < tabRecommendation.size(); tabIndex++) {
                Variant variant = tabRecommendation.get(tabIndex);
                String tabTitle = WordUtils.capitalizeFully(getTitleForVariant(variant));
                String siteFeatureName = variant.getSiteFeature().getName();
                Recommendations recommendations = getRecommendations(variant, request, request.getSession(), parentImpressionId, user);
                boolean selected = tabIndex == selectedTab;
                RecommendationTab tab = new RecommendationTab(tabTitle, siteFeatureName).setParentImpressionId(parentImpressionId)
                        .setImpressionId(tabRecommendation.getFeatureImpressionId(tabIndex)).setParentVariantId(parentVariantId).setDescription(getDescription(variant))
                        .setProductSamplesReacedMaximumItemQuantity(user.getShoppingCart().isMaxSampleReached()).setSelected(selected)
                        .setUpdateContent(!input.isOnlyTabHeader() || !updatedSiteFeature.equals(selectedSiteFeature) || isSample(siteFeatureName));
                    tab.setCarouselData(getCarouselData(session, request, user, variant, parentImpressionId, parentVariantId, recommendations));
                result.addRecommendationTab(tab);
            }
        }

        List<RecommendationTab> tabs = result.getRecommendationTabs();
        if (!isTabSelected(tabs)) {
            selectedFirstAvailableTab(tabs);
        }
        removeEmptyTabs(tabs);

        return result;
    }

    private void selectedFirstAvailableTab(List<RecommendationTab> tabs) {
        for (RecommendationTab tab : tabs) {
            if (tab.getCarouselData() != null && !tab.getCarouselData().getProducts().isEmpty()) {
                tab.setSelected(true);
                break;
            }
        }
    }

    private boolean isTabSelected(List<RecommendationTab> tabs) {
        boolean selected = false;
        for (RecommendationTab tab : tabs) {
            if (tab.isSelected() && tab.getCarouselData() != null && !tab.getCarouselData().getProducts().isEmpty()) {
                selected = true;
                break;
            }
        }
        return selected;
    }

    private void removeEmptyTabs(List<RecommendationTab> tabs) {
        for (Iterator<RecommendationTab> iterator = tabs.iterator(); iterator.hasNext();) {
            RecommendationTab tab = iterator.next();
            if (tab.getCarouselData() == null || tab.getCarouselData().getProducts().isEmpty()) {
                iterator.remove();
            } else if (!tab.isSelected()) {
                tab.setCarouselData(null);
            }
        }
    }

    protected List<Variant> getVariants(FDUserI user) {
        List<Variant> variants = new ArrayList<Variant>();
        for (final String siteFeature : getSiteFeatures(user)) {
            EnumSiteFeature enumSiteFeature = EnumSiteFeature.getEnum(siteFeature);
            if (EnumSiteFeature.NIL != enumSiteFeature) {
                VariantSelector selector = VariantSelectorFactory.getSelector(enumSiteFeature);
                Variant variant = selector.select(user, false);
                if (variant != null) {
                    variants.add(variant);
                }
            }
        }
        return variants;
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
        EnumSiteFeature siteFeature = variant.getSiteFeature();
        if (description == null) {
            description = siteFeature.getPresentationDescription();
        }
        if (PRODUCT_SAMPLE_SITE_FEATURE.equals(siteFeature.getName())) {
            description = description.replace("%%N%%", String.valueOf(FDStoreProperties.getProductSamplesMaxBuyProductsLimit()));
            description = description.replace("%%Q%%", String.valueOf(FDStoreProperties.getProductSamplesMaxQuantityLimit()));
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
            currentUser = user.getLevel() > FDUserI.RECOGNIZED && user.getAdjustedValidOrderCount(EnumEStoreId.FD) >= 3;
        } catch (FDResourceException e) {
            currentUser = false;
        }
        return currentUser;
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

    public void replaceSampleSiteFeatures(List<String> siteFeatures) {
        for (int i = 0; i < siteFeatures.size(); i++) {
            if (isSample(siteFeatures.get(i))) {
                siteFeatures.set(i, getFreeProductSiteFeature());
            }
        }
    }

    public String getFreeProductSiteFeature() {
        return FDStoreProperties.isPropDonationProductSamplesEnabled() ? DONATION_SAMPLE_SITE_FEATURE : PRODUCT_SAMPLE_SITE_FEATURE;
    }

    public static boolean isSample(String siteFeature) {
        return PRODUCT_SAMPLE_GRID_SITE_FEAURES.contains(siteFeature);
    }

}
