package com.freshdirect.webapp.ajax.viewcart.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
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

public class CheckoutCarouselService extends AbstractCarouselService {

    private static final CheckoutCarouselService INSTANCE = new CheckoutCarouselService();

    private static final String EX_CHECKOUT_VARIANT_ID = "xc_checkout";

    public static final CheckoutCarouselService getDefaultService() {
        return INSTANCE;
    }

    @Override
    protected int getMaxTabs() {
        return 5;
    }

    @Override
    protected int getMaxRecommendations() {
        return 3;
    }

    @Override
    protected String getSmartStoreFacilityName() {
        return "default";
    }

    @Override
    protected boolean shouldConsolidateEmptyTabs() {
        return false;
    }

    @Override
    protected String getSelectedTabName() {
        return "";
    }

    @Override
    protected String getSelectedVariantName() {
        return "";
    }

    @Override
    protected String getEventSource(String siteFeature) {
        EnumEventSource eventSource = null;
        if ("PRODUCT_SAMPLE".equals(siteFeature)) {
            eventSource = EnumEventSource.ps_carousel_checkout;
        } else if ("DONATION_SAMPLE".equals(siteFeature)) {
            eventSource = EnumEventSource.dn_carousel_checkout;
        } else {
            eventSource = EnumEventSource.checkout;
        }
        return eventSource.getName();
    }

    @Override
    protected TabRecommendation getTabRecommendation(HttpServletRequest request, FDUserI user, SessionInput input) {
        final RecommendationServiceConfig cfg = new RecommendationServiceConfig(EX_CHECKOUT_VARIANT_ID, RecommendationServiceType.NIL);
        final Variant tabVariant = new Variant(EX_CHECKOUT_VARIANT_ID, EnumSiteFeature.EX_CHECKOUT_CAROUSEL, cfg);

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

        TabRecommendation tabs = new TabRecommendation(tabVariant, variants);
        tabs.setError(input.isError());
        return tabs;
    }

    protected List<String> getSiteFeatures(FDUserI user) {
        boolean isCurrentUser = isUserAlreadyOrdered(user);
        return isCurrentUser ? FDStoreProperties.getCheckoutCurrentCustomerCarouselSiteFeatures() : FDStoreProperties.getCheckoutNewCustomerCarouselSiteFeatures();
    }

    protected String getDefaultSiteFeature(FDUserI user) {
        boolean isCurrentUser = isUserAlreadyOrdered(user);
        return isCurrentUser ? "DYF" : "FAVORITES";
    }

}
