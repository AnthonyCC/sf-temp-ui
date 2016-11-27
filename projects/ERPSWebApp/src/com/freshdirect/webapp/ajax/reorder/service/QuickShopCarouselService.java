package com.freshdirect.webapp.ajax.reorder.service;

import java.util.ArrayList;
import java.util.List;

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

	private static QuickShopCarouselService INSTANCE = new QuickShopCarouselService();

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

	/**
	 * Tabs
	 */
	public static final String[] QS_SITE_FEATURES = { "DEALS_QS", "EXPRATED_QS", "CUSTRATED_QS" };

	private static final List<EnumSiteFeature> _QS_FEATURES = new ArrayList<EnumSiteFeature>();
	static {
		for (final String n : QS_SITE_FEATURES) {
			EnumSiteFeature sf = EnumSiteFeature.getEnum(n);
			if (sf != null) {
				_QS_FEATURES.add(sf);
			}
		}
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
	protected TabRecommendation getTabRecommendation(final FDUserI user, final SessionInput input) {
		// TODO finalize
		final RecommendationServiceConfig cfg = new RecommendationServiceConfig("qs_bottom_general", RecommendationServiceType.NIL);
		final Variant tabVariant = new Variant("qs_bottom_general", EnumSiteFeature.QS_BOTTOM_CAROUSEL, cfg);

		// variants
		List<Variant> variants = new ArrayList<Variant>();
		for (final EnumSiteFeature sf : _QS_FEATURES) {
			// pick right variant
			VariantSelector selector = VariantSelectorFactory.getSelector(sf);
			Variant v = selector.select(user);

			variants.add(v);
		}

		TabRecommendation tabs = new TabRecommendation(tabVariant, variants);
		return tabs;
	}
}
