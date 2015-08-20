package com.freshdirect;

/**
 *
 * @author  ksriram
 * @version
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDCouponProperties {

	private static Category LOGGER = LoggerFactory.getInstance(FDCouponProperties.class);
	private static List<CouponConfigLoadListener> listeners = new ArrayList<CouponConfigLoadListener>();
	
	private static long lastRefresh = 0;
    private final static long REFRESH_PERIOD = 5 * 60 * 1000;
    
	private static Properties config;
	private static Properties defaults;

	private final static String PROP_YT_GET_COUPONS_META_URL = "fdstore.getCouponsMeta.url";
	private final static String PROP_YT_GET_COUPONS_CUSTOMER_URL = "fdstore.YT.getCouponsCustomer.url";
	private final static String PROP_YT_CLIP_COUPON_URL = "fdstore.YT.clipCoupon.url";
	private final static String PROP_YT_PREVIEW_CART_COUPONS_URL = "fdstore.YT.previewCartCoupons.url";
	private final static String PROP_YT_PREVIEW_MODIFY_CART_COUPONS_URL = "fdstore.YT.previewModifyCartCoupons.url";
	private final static String PROP_YT_SUBMIT_CREATE_ORDER_COUPONS_URL = "fdstore.YT.submitCreateOrderCoupons.url";
	private final static String PROP_YT_SUBMIT_MODIFY_ORDER_COUPONS_URL = "fdstore.YT.submitModifyOrderCoupons.url";
	private final static String PROP_YT_CANCEL_ORDER_COUPONS_URL = "fdstore.YT.cancelOrderCoupons.url";
	private final static String PROP_YT_CONFIRM_ORDER_COUPONS_URL = "fdstore.YT.confirmOrderCoupons.url";

	private final static String PROP_YT_SITE_ID = "fdstore.YT.site.id";
	private final static String PROP_YT_RETAILER_NAME = "fdstore.YT.retailer.name";
	private final static String PROP_YT_RETAILER_ID = "fdstore.YT.retailer.id";
	private final static String PROP_YT_SIGNATURE = "fdstore.YT.signature";
	private final static String PROP_YT_PROVIDER_URL = "fdstore.YT.provider.url";
	private final static String PROP_YT_CONNECTION_TIMEOUT_PERIOD = "fdstore.YT.connection.timeout.period";
	private final static String PROP_YT_READ_TIMEOUT_PERIOD = "fdstore.YT.read.timeout.period";
	private final static String PROP_YT_PROVIDER_VERSION = "fdstore.YT.provider.version";
	private final static String PROP_COUPONS_BLACK_HOLE = "fdstore.coupons.blackhole.enabled";
	private final static String PROP_COUPONS_CACHE_DAYS_LIMIT_ENABLED="fdstore.coupons.cache.days.limit.enabled";
	private final static String PROP_COUPONS_CACHE_DAYS_LIMIT="fdstore.coupons.cache.days.limit";
	public final static String PROP_COUPONS_ENABLED="fdstore.ecoupons.enabled";
	private final static String PROP_COUPONS_CMS_CATEGORY="fdstore.ecoupons.cms.category";
	private final static String PROP_COUPONS_CMS_CATEGORY_FDX="fdstore.ecoupons.cms.fdx_category";
	private final static String PROP_COUPONS_CACHE_REFRESH_PERIOD = "fdstore.ecoupons.cache.refresh.period";
	private final static String PROP_CUSTOMER_COUPONS_USAGE_HISTORY_DAYS_LIMIT="fdstore.customer.coupons.history.days.limit";
	private final static String PROP_COUPONS_EXPIRE_GRACE_PERIOD = "fdstore.coupons.expire.grace.period";

	static {
		defaults = new Properties();
		defaults.put(PROP_YT_GET_COUPONS_META_URL, "/coupons/show.json?");
		defaults.put(PROP_YT_GET_COUPONS_CUSTOMER_URL,"/coupons/show/by_card_and_rule.json?");
		defaults.put(PROP_YT_CLIP_COUPON_URL,"/card/coupons/add.json?");
		defaults.put(PROP_YT_PREVIEW_CART_COUPONS_URL,"/coupons/transaction/preview.json?");
		defaults.put(PROP_YT_PREVIEW_MODIFY_CART_COUPONS_URL,"/coupons/transaction/preview/modify.json?");
		defaults.put(PROP_YT_SUBMIT_CREATE_ORDER_COUPONS_URL,"/coupons/transaction/apply.json?");
		defaults.put(PROP_YT_SUBMIT_MODIFY_ORDER_COUPONS_URL,"/coupons/transaction/apply/modify.json?");
		defaults.put(PROP_YT_CANCEL_ORDER_COUPONS_URL,"/coupons/transaction/cancel.json?");
		defaults.put(PROP_YT_CONFIRM_ORDER_COUPONS_URL,"/coupons/transaction/commit.json?");

		defaults.put(PROP_YT_SITE_ID, "8210");
		defaults.put(PROP_YT_RETAILER_NAME, "Fresh Direct");
		defaults.put(PROP_YT_RETAILER_ID, "481");
		defaults.put(PROP_YT_SIGNATURE, "AMcSlvpXMAAp0xgON9Jr");
		defaults.put(PROP_YT_PROVIDER_URL, "http://staging2.softcoin.com");
		defaults.put(PROP_YT_PROVIDER_VERSION, "/p/ws/1");

		defaults.put(PROP_COUPONS_BLACK_HOLE, "false");
		defaults.put(PROP_COUPONS_CACHE_DAYS_LIMIT_ENABLED, "true");
		defaults.put(PROP_COUPONS_CACHE_DAYS_LIMIT, "30");
		defaults.put(PROP_COUPONS_ENABLED, "true");
		defaults.put(PROP_COUPONS_CMS_CATEGORY, "ecoupons");
		defaults.put(PROP_COUPONS_CMS_CATEGORY_FDX, "ecoupons_fdx");
		defaults.put(PROP_COUPONS_CACHE_REFRESH_PERIOD, "5");//mins
		defaults.put(PROP_YT_CONNECTION_TIMEOUT_PERIOD, "10");//secs
		defaults.put(PROP_YT_READ_TIMEOUT_PERIOD, "10");//secs
		defaults.put(PROP_CUSTOMER_COUPONS_USAGE_HISTORY_DAYS_LIMIT, "90");//days
		defaults.put(PROP_COUPONS_EXPIRE_GRACE_PERIOD, "7");//days
		refresh();
	}

	/** Creates new FDCouponProperties */
	public FDCouponProperties() {
	}
	
	private static void refresh() {
        refresh(false);
    }
	
    public static void forceRefresh() {
        refresh(true);
    }
	
	public static interface CouponConfigLoadListener {
        void configLoaded();
    }
	
    private synchronized static void refresh(boolean force) {
        long t = System.currentTimeMillis();

        if (force || ((t - lastRefresh) > REFRESH_PERIOD)) {
            config = ConfigHelper.getPropertiesFromClassLoader("fdcoupon.properties", defaults);
            lastRefresh = t;
            LOGGER.info("Loaded configuration from fdcoupon.properties: " + config);
            fireEvent();
        }
    }
    
    private static void fireEvent() {
        synchronized (listeners) {
            for (CouponConfigLoadListener listener : listeners) {
                listener.configLoaded();
            }
        }
    }
    
	public static String get(String key) {
        refresh();

        return config.getProperty(key);
    }
	
	public static void set(String key, String value) {
		config.setProperty(key, value);
	}

	public static String getYTCouponsMetaURL() {
		return get(PROP_YT_GET_COUPONS_META_URL);
	}

	public static String getYTCustomerCouponsURL() {
		return get(PROP_YT_GET_COUPONS_CUSTOMER_URL);
	}

	public static String getYTClipCouponsURL() {
		return get(PROP_YT_CLIP_COUPON_URL);
	}

	public static String getYTPreviewCartCouponsURL() {
		return get(PROP_YT_PREVIEW_CART_COUPONS_URL);
	}

	public static String getYTPreviewModifyCartCouponsURL() {
		return get(PROP_YT_PREVIEW_MODIFY_CART_COUPONS_URL);
	}
	
	public static String getYTSubmitCreateOrderURL() {
		return get(PROP_YT_SUBMIT_CREATE_ORDER_COUPONS_URL);
	}

	public static String getYTSubmitModifyOrderURL() {
		return get(PROP_YT_SUBMIT_MODIFY_ORDER_COUPONS_URL);
	}
	
	public static String getYTCancelOrderURL() {
		return get(PROP_YT_CANCEL_ORDER_COUPONS_URL);
	}

	public static String getYTConfirmOrderURL() {
		return get(PROP_YT_CONFIRM_ORDER_COUPONS_URL);
	}

	public static String getYTSiteId() {
		return get(PROP_YT_SITE_ID);
	}

	public static String getYTRetailerName() {
		return get(PROP_YT_RETAILER_NAME);
	}

	public static String getYTRetailerId() {
		return get(PROP_YT_RETAILER_ID);
	}

	public static String getYTSignature() {
		return get(PROP_YT_SIGNATURE);
	}
	
	public static String getYTProviderUrl() {
		return get(PROP_YT_PROVIDER_URL);
	}
	
	public static String getYTProviderVersion() {
		return get(PROP_YT_PROVIDER_VERSION);
	}

	public static boolean isCouponsBlackHoleEnabled() {
    	return (Boolean.valueOf(get(PROP_COUPONS_BLACK_HOLE))).booleanValue();
    }
	
	public static boolean isCouponCacheDaysLimitEnabled() {
    	return (Boolean.valueOf(get(PROP_COUPONS_CACHE_DAYS_LIMIT_ENABLED))).booleanValue();
    }
	
	public static int getCouponCacheDaysLimit() { 
    	return (Integer.parseInt(get(PROP_COUPONS_CACHE_DAYS_LIMIT)));
    }
	
	public static boolean isCouponsEnabled() {
		return (new Boolean(get(PROP_COUPONS_ENABLED))).booleanValue();
	}
	
	public static String getCouponCMSCategory() {
		return get(PROP_COUPONS_CMS_CATEGORY);
	}
	
	public static String getCouponCMSCategoryFDX() {
		return get(PROP_COUPONS_CMS_CATEGORY_FDX);
	}

	public static Integer getCouponCacheRefreshPeriod() {
		try {
			return Integer.parseInt(get(PROP_COUPONS_CACHE_REFRESH_PERIOD));
		} catch (NumberFormatException e) {
			return 60;
		}
	}
	
	public static Integer getYTConnectionTimeoutPeriod() {
		try {
			return Integer.parseInt(get(PROP_YT_CONNECTION_TIMEOUT_PERIOD));
		} catch (NumberFormatException e) {
			return 10;
		}
	}
	
	public static Integer getYTReadTimeoutPeriod() {
		try {
			return Integer.parseInt(get(PROP_YT_READ_TIMEOUT_PERIOD));
		} catch (NumberFormatException e) {
			return 10;
		}
	}
	
	public static int getCustomerCouponUsageHistoryDaysLimit() { 
    	return (Integer.parseInt(get(PROP_CUSTOMER_COUPONS_USAGE_HISTORY_DAYS_LIMIT)));
    }
	
	public static int getExpiredCouponsGracePeriod() { 
    	return (Integer.parseInt(get(PROP_COUPONS_EXPIRE_GRACE_PERIOD)));
    }
	
}
