package com.freshdirect.webapp.ajax.viewcart.service;

import java.util.ArrayList;
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
import com.freshdirect.smartstore.fdstore.VariantSelector;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.AbstractCarouselService;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;

public class ViewCartCarouselService extends AbstractCarouselService {

    private static final ViewCartCarouselService INSTANCE = new ViewCartCarouselService();

    private static final String XC_VIEW_CART_VARIANT_ID = "xc_view_cart";
    private static final String SELECTED_SITE_FEATURE_ATTRIBUTE_KEY = XC_VIEW_CART_VARIANT_ID + SELECTED_SITE_FEATURE_POSTFIX;
    private static final String PARENT_IMPRESSION_ID_ATTRIBUTE_KEY = XC_VIEW_CART_VARIANT_ID + PARENT_IMPRESSION_ID_POSTFIX;

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
    protected boolean shouldConsolidateEmptyTabs() {
        return true;
    }

    @Override
    protected TabRecommendation getTabRecommendation(HttpServletRequest request, FDUserI user, SessionInput input) {
        // variants
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

        TabRecommendation tabs = new TabRecommendation(getTabVariant(), variants);
        tabs.setError(input.isError());
        tabs.setSelectedSiteFeature((String) request.getSession().getAttribute(SELECTED_SITE_FEATURE_ATTRIBUTE_KEY));
        tabs.setParentImpressionId((String) request.getSession().getAttribute(PARENT_IMPRESSION_ID_ATTRIBUTE_KEY));
        return tabs;
    }

    @Override
    protected String getEventSource(String siteFeature, FDUserI user) {
        EnumEventSource eventSource = null;

        if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user)) {
            if (RecommendationTab.PRODUCT_SAMPLE_SITE_FEATURE.equals(siteFeature)) {
                eventSource = EnumEventSource.ps_carousel_view_cart;
            } else if (RecommendationTab.DONATION_SAMPLE_SITE_FEATURE.equals(siteFeature)) {
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
    protected int getSelectedTab(TabRecommendation tabs, HttpSession session, ServletRequest request, FDUserI user) {
        List<Variant> variants = tabs.getVariants();
        int selected = selectedTab(variants, tabs.getSelectedSiteFeature());

        if (tabs.isError()) {
            selected = selectedTab(variants, getDefaultSiteFeature(user));
        } else if (isMaxSampleReached(user.getShoppingCart()) && variants.size() > 1
                && RecommendationTab.isSample(tabs.getSelectedSiteFeature())) {
            selected = (selected + 1) % variants.size();
        }

        return selected;
    }

    @Override
    protected List<String> getSiteFeatures(FDUserI user) {
        List<String> siteFeatures = null;
        if (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.carttabcars, user)) {
            boolean isCurrentUser = isUserAlreadyOrdered(user);
            siteFeatures = isCurrentUser ? FDStoreProperties.getViewcartCurrentCustomerCarouselSiteFeatures() : FDStoreProperties.getViewcartNewCustomerCarouselSiteFeatures();
        } else {
            if (FDStoreProperties.isPropDonationProductSamplesEnabled()) {
                siteFeatures = Arrays.asList(RecommendationTab.DONATION_SAMPLE_SITE_FEATURE);
            } else {
                siteFeatures = Arrays.asList(RecommendationTab.PRODUCT_SAMPLE_SITE_FEATURE);
            }
        }
        return siteFeatures;
    }

    protected String getDefaultSiteFeature(FDUserI user) {
        boolean isCurrentUser = isUserAlreadyOrdered(user);
        return isCurrentUser ? "DYF" : "FAVORITES";
    }

}
