package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

/**
 * A copy-paste clone of {@see ViewCartCarouselService}
 * 
 * @author segabor
 *
 */
public class QuickShopCarouselService extends AbstractCarouselService {

    public static final String QUICKSHOP_VIRTUAL_SITE_FEATURE = "CRAZY_QUICKSHOP";
    private static final List<String> QS_SITE_FEATURES = Arrays.asList("DEALS_QS", "EXPRATED_QS", "CUSTRATED_QS");
    private static final String QS_BOTTOM_GENERAL_VARIANT_ID = "qs_bottom_general";

    private static final QuickShopCarouselService INSTANCE = new QuickShopCarouselService();

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
	protected String getSelectedTabName() {
		return SessionName.QSB_SELECTED_TAB;
	}

	@Override
	protected String getSelectedVariantName() {
		return SessionName.QSB_SELECTED_VARIANT;
	}

    @Override
    protected String getEventSource(String siteFeature) {
        return "";
    }

	private QuickShopCarouselService() {
	}

	/**
	 * Gives the default view cart carousel service.
	 * 
	 * @return the default service instance
	 */
	public static QuickShopCarouselService defaultService() {
		return INSTANCE;
	}

	@Override
    protected TabRecommendation getTabRecommendation(HttpServletRequest request, final FDUserI user, SessionInput input) {
		final RecommendationServiceConfig cfg = new RecommendationServiceConfig(QS_BOTTOM_GENERAL_VARIANT_ID, RecommendationServiceType.NIL);
		final Variant tabVariant = new Variant(QS_BOTTOM_GENERAL_VARIANT_ID, EnumSiteFeature.QS_BOTTOM_CAROUSEL, cfg);

		// variants
		List<Variant> variants = new ArrayList<Variant>();
        for (final String siteFeature : QS_SITE_FEATURES) {
            EnumSiteFeature enumSiteFeature = EnumSiteFeature.getEnum(siteFeature);
            VariantSelector selector = VariantSelectorFactory.getSelector(enumSiteFeature);
			variants.add(selector.select(user));
		}

		TabRecommendation tabs = new TabRecommendation(tabVariant, variants);
		return tabs;
	}
}
