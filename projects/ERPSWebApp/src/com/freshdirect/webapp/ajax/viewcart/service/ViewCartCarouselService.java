package com.freshdirect.webapp.ajax.viewcart.service;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.webapp.ajax.AbstractCarouselService;

public class ViewCartCarouselService extends AbstractCarouselService {

    private static final ViewCartCarouselService INSTANCE = new ViewCartCarouselService();

    private static final String XC_VIEW_CART_VARIANT_ID = "xc_view_cart";

    public static final ViewCartCarouselService getDefaultService() {
        return INSTANCE;
    }

    @Override
    protected int getMaxRecommendations() {
        return 5;
    }

    @Override
    protected int getMaxTabs() {
        return 3;
    }

    @Override
    protected String getSmartStoreFacilityName() {
        return "default";
    }

    @Override
    protected String getEventSource(String siteFeature, FDUserI user) {
        EnumEventSource eventSource = null;

        if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user)) {
            if (PRODUCT_SAMPLE_SITE_FEATURE.equals(siteFeature)) {
                eventSource = EnumEventSource.ps_carousel_view_cart;
            } else if (DONATION_SAMPLE_SITE_FEATURE.equals(siteFeature)) {
                eventSource = EnumEventSource.dn_carousel_view_cart;
            } else {
                eventSource = EnumEventSource.view_cart;
            }
        } else {
            if (FDStoreProperties.isPropDonationProductSamplesEnabled()) {
                eventSource = EnumEventSource.dn_caraousal;
            } else {
                eventSource = EnumEventSource.ps_caraousal;
            }
        }

        return eventSource.getName();
    }

    @Override
    protected Variant getTabVariant() {
        final RecommendationServiceConfig cfg = new RecommendationServiceConfig(XC_VIEW_CART_VARIANT_ID, RecommendationServiceType.NIL);
        return new Variant(XC_VIEW_CART_VARIANT_ID, EnumSiteFeature.XC_VIEW_CART_CAROUSEL, cfg);
    }

    @Override
    protected TabRecommendation getTabRecommendation(HttpServletRequest request, FDUserI user, SessionInput input) {
        Variant tabVariant = getTabVariant();
        String selectedSiteFeature = getSelectedSiteFeatureAttribute(request.getSession(), tabVariant.getId());
        if (selectedSiteFeature == null) {
            selectedSiteFeature = getFreeProductSiteFeature();
            setSelectedSiteFeatureAttribute(request.getSession(), selectedSiteFeature);
        }
        String parentImpressionId = getParentImpressionIdAttribute(request.getSession(), tabVariant.getId());
        TabRecommendation tabs = new TabRecommendation(tabVariant, getVariants(user));
        tabs.setSelectedSiteFeature(selectedSiteFeature);
        tabs.setParentImpressionId(parentImpressionId);
        tabs.setOnlyTabHeader(input.isOnlyTabHeader());
        tabs.setError(input.isError());
        return tabs;
    }

    @Override
    protected int getSelectedTab(TabRecommendation tabs, HttpSession session, ServletRequest request, FDUserI user) {
        List<Variant> variants = tabs.getVariants();
        String selectedSiteFeature = tabs.getSelectedSiteFeature();
        int selected = selectedTab(variants, selectedSiteFeature);
        if (tabs.isError()) {
            selected = selectedTab(variants, getDefaultErrorSiteFeature(user));
            setSelectedSiteFeatureAttribute(session, variants.get(selected).getSiteFeature().getName());
        } else if (user.getShoppingCart().isMaxSampleReached() && variants.size() > 1 && PRODUCT_SAMPLE_SITE_FEATURE.equals(selectedSiteFeature)) {
            selected = (selected + 1) % variants.size();
            setSelectedSiteFeatureAttribute(session, variants.get(selected).getSiteFeature().getName());
        }
        return selected;
    }

    @Override
    protected List<String> getSiteFeatures(FDUserI user) {
        List<String> siteFeatures = null;
        if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user)) {
            boolean isCurrentUser = isUserAlreadyOrdered(user);
            siteFeatures = isCurrentUser ? FDStoreProperties.getViewcartCurrentCustomerCarouselSiteFeatures() : FDStoreProperties.getViewcartNewCustomerCarouselSiteFeatures();
            replaceSampleSiteFeatures(siteFeatures);
        } else {
            siteFeatures = Arrays.asList(getFreeProductSiteFeature());
        }
        return siteFeatures;
    }

    public String getDefaultErrorSiteFeature(FDUserI user) {
        boolean isCurrentUser = isUserAlreadyOrdered(user);
        return isCurrentUser ? "DYF" : "FAVORITES";
    }

}
