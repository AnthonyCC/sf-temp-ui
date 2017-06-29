package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationServiceConfig;
import com.freshdirect.smartstore.RecommendationServiceType;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.VariantSelector;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.AbstractCarouselService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class QuickShopCarouselService extends AbstractCarouselService {

    public static final String QUICKSHOP_VIRTUAL_SITE_FEATURE = "CRAZY_QUICKSHOP";
    private static final String QS_BOTTOM_GENERAL_VARIANT_ID = "qs_bottom_general";
    private static final String SELECTED_SITE_FEATURE_ATTRIBUTE_KEY = QS_BOTTOM_GENERAL_VARIANT_ID + SELECTED_SITE_FEATURE_POSTFIX;
    private static final String PARENT_IMPRESSION_ID_ATTRIBUTE_KEY = QS_BOTTOM_GENERAL_VARIANT_ID + PARENT_IMPRESSION_ID_POSTFIX;
    private static final List<String> QS_SITE_FEATURES = Arrays.asList("DEALS_QS", "EXPRATED_QS", "CUSTRATED_QS");

    private static final QuickShopCarouselService INSTANCE = new QuickShopCarouselService();

    private QuickShopCarouselService() {
    }

    /**
     * Gives the default carousel service.
     * 
     * @return the default service instance
     */
    public static QuickShopCarouselService defaultService() {
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

	protected String getSelectedTabName() {
		return SessionName.QSB_SELECTED_TAB;
	}

	protected String getSelectedVariantName() {
		return SessionName.QSB_SELECTED_VARIANT;
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
        return siteFeature;
    }

	@Override
    protected List<String> getSiteFeatures(FDUserI user) {
        return QS_SITE_FEATURES;
    }

    @Override
    protected Variant getTabVariant() {
        final RecommendationServiceConfig cfg = new RecommendationServiceConfig(QS_BOTTOM_GENERAL_VARIANT_ID, RecommendationServiceType.NIL);
        return new Variant(QS_BOTTOM_GENERAL_VARIANT_ID, EnumSiteFeature.QS_BOTTOM_CAROUSEL, cfg);
    }

    @Override
    protected int getSelectedTab(TabRecommendation tabs, HttpSession session, ServletRequest request, FDUserI user) {
        final int numTabs = tabs.size();
        int selectedTab = 0; // default value
        Object selectedTabAttribute = session.getAttribute(getSelectedTabName());

        boolean shouldStoreTabPos = selectedTabAttribute == null; // true == not
        // stored yet
        if (selectedTabAttribute != null) {
            // get the stored one if exist
            selectedTab = ((Integer) selectedTabAttribute).intValue();
        }

        if (selectedTab == -1) {
            shouldStoreTabPos = true;
            // try to calculate a good tab index
            if (session.getAttribute(getSelectedVariantName()) != null) {
                String tabId = (String) session.getAttribute(getSelectedVariantName());

                selectedTab = tabs.getTabIndex(tabId);
                if (selectedTab == -1 && tabId.indexOf(',') != -1) {
                    String[] variants = tabId.split(",");
                    for (int i = 0; i < variants.length && selectedTab == -1; i++) {
                        selectedTab = tabs.getTabIndex(variants[i]);
                        if (selectedTab != -1) {
                            session.setAttribute(getSelectedVariantName(), variants[i]);
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
                || (session.getAttribute(getSelectedVariantName()) != null && !tabs.get(selectedTab).getId().equals(session.getAttribute(getSelectedVariantName())))) {
            // reset if selection is out of tab range or the variant of selected
            // tab has changed
            selectedTab = 0;
            shouldStoreTabPos = true;
        }

        Integer iSelectedTab = new Integer(selectedTab);
        if (shouldStoreTabPos) {
            // store changed tab position in session
            session.setAttribute(getSelectedTabName(), iSelectedTab);
            session.setAttribute(getSelectedVariantName(), tabs.get(selectedTab).getId());
        }

        return selectedTab;
    }
}
