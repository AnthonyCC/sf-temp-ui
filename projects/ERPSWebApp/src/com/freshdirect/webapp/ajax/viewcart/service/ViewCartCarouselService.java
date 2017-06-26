package com.freshdirect.webapp.ajax.viewcart.service;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.webapp.ajax.AbstractCarouselService;
import com.freshdirect.webapp.ajax.viewcart.data.RecommendationTab;

public class ViewCartCarouselService extends AbstractCarouselService {

    private static final ViewCartCarouselService INSTANCE = new ViewCartCarouselService();

    private static final String EX_VIEW_CART_VARIANT_ID = "xc_view_cart";

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
    protected String getEventSource(String siteFeature) {
        EnumEventSource eventSource = null;
        if (RecommendationTab.PRODUCT_SAMPLE_SITE_FEATURE.equals(siteFeature)) {
            eventSource = EnumEventSource.ps_carousel_view_cart;
        } else if (RecommendationTab.DONATION_SAMPLE_SITE_FEATURE.equals(siteFeature)) {
            eventSource = EnumEventSource.dn_carousel_view_cart;
        } else {
            eventSource = EnumEventSource.view_cart;
        }
        return eventSource.getName();
    }

    @Override
    protected Variant getTabVariant() {
        final RecommendationServiceConfig cfg = new RecommendationServiceConfig(EX_VIEW_CART_VARIANT_ID, RecommendationServiceType.NIL);
        return new Variant(EX_VIEW_CART_VARIANT_ID, EnumSiteFeature.EX_VIEW_CART_CAROUSEL, cfg);
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
        boolean isCurrentUser = isUserAlreadyOrdered(user);
        return isCurrentUser ? FDStoreProperties.getViewcartCurrentCustomerCarouselSiteFeatures() : FDStoreProperties.getViewcartNewCustomerCarouselSiteFeatures();
    }

    protected String getDefaultSiteFeature(FDUserI user) {
        boolean isCurrentUser = isUserAlreadyOrdered(user);
        return isCurrentUser ? "DYF" : "FAVORITES";
    }

}
