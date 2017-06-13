package com.freshdirect.fdstore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Category;

import com.freshdirect.ecommerce.data.enums.EnumPropertyType;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDEcommProperties {

    private static final Category LOGGER = LoggerFactory.getInstance(FDEcommProperties.class);
    private static List<ConfigLoadedListener> listeners = new ArrayList<ConfigLoadedListener>();
    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");
   
    private static Properties config;
    private final static Properties defaults = new Properties();
  
    private static long lastRefresh = 0;
    private final static long REFRESH_PERIOD = 5 * 60 * 1000;
    

    private final static String FDMonitorSB = "fdstore.monitor.FDMonitorSB";
    private final static String ErpCOOLManagerSB = "erp.ejb.ErpCOOLManagerSB";
    private final static String ErpGrpInfoSB = "erp.ejb.ErpGrpInfoSB";
    private final static String SapGrpInfoLoaderSB = "sap.ejb.SapGrpInfoLoaderSB";
    private final static String ErpInventoryManagerSB = "erp.ejb.ErpInventoryManagerSB";
    private final static String DlvManagerSB = "delivery.ejb.DlvManagerSB";
    private final static String ActivityLogSB = "customer.ejb.ActivityLogSB";
    private final static String FDBrandProductsAdManagerSB = "fdstore.brandads.FDBrandProductsAdManagerSB";
    private final static String BINInfoManagerSB = "payment.ejb.BINInfoManagerSB";
    private final static String GCGatewaySB = "giftcard.ejb.GCGatewaySB";
    private final static String SAPProductFamilyLoaderSB = "sap.ejb.SAPProductFamilyLoaderSB";
    private final static String SmsAlertsSB = "sms.ejb.SmsAlertsSB";
    private final static String SAPZoneInfoLoaderSB = "sap.ejb.SAPZoneInfoLoaderSB";
    private final static String BatchManagerSB = "erp.ejb.BatchManagerSB";
    private final static String AttributeFacadeSB = "attributes.ejb.AttributeFacadeSB";
    private final static String ErpProductPromotionInfoSB = "erp.ejb.ErpProductPromotionInfoSB";
    private final static String ErpZoneInfoSB = "erp.ejb.ErpZoneInfoSB";
    private final static String RecommendationEventLoggerSB = "event.ejb.RecommendationEventLoggerSB";
    private final static String EventLoggerSB = "event.ejb.EventLoggerSB";
    private final static String FDFactorySB = "fdstore.ejb.FDFactorySB";
    private final static String FDCouponActivityLogSB = "fdstore.ecoupon.FDCouponActivityLogSB";
    private final static String EwalletActivityLogSB = "ewallet.ejb.EwalletActivityLogSB";
    private final static String FDExtoleManagerSB = "referral.extole.FDExtoleManagerSB";
    private final static String ErpEWalletSB = "erp.ejb.ErpEWalletSB";
    private final static String FDSurveySB = "survey.ejb.FDSurveySB";
    private final static String ExternalAccountManagerSB = "accounts.external.ExternalAccountManagerSB";
    private final static String FDGrpInfoSB = "grp.ejb.FDGrpInfoSB";
    private final static String FDZoneInfoSB = "zone.ejb.FDZoneInfoSB";
    private final static String ScoreFactorSB = "smartstore.ejb.ScoreFactorSB";
    private final static String DyfModelSB = "smartstore.ejb.DyfModelSB";
    private final static String SessionImpressionLogSB = "smartstore.ejb.SessionImpressionLogSB";
    private final static String RulesManagerSB = "rules.ejb.RulesManagerSB";
    private final static String TestSupportSB = "test.ejb.TestSupportSB";
    private final static String ErpInfoSB = "erp.ejb.ErpInfoSB";
    private final static String ErpRoutingGatewaySB ="routing.ejb.ErpRoutingGatewaySB";
    
    
    static {

        defaults.put(FDMonitorSB, "false");
        defaults.put(ErpCOOLManagerSB , "false");
        defaults.put(ErpGrpInfoSB, "false");
        defaults.put(SapGrpInfoLoaderSB, "false");
        defaults.put(ErpInventoryManagerSB , "false");
        defaults.put(DlvManagerSB , "false");
        defaults.put(ActivityLogSB , "false");
        defaults.put(FDBrandProductsAdManagerSB , "false");
        defaults.put(BINInfoManagerSB, "false");
        defaults.put(GCGatewaySB, "false");
        defaults.put(SAPProductFamilyLoaderSB, "false");
        defaults.put(SmsAlertsSB , "false");
        defaults.put(SAPZoneInfoLoaderSB, "false");
        defaults.put(BatchManagerSB, "false");
        defaults.put(AttributeFacadeSB , "false");
        defaults.put(ErpProductPromotionInfoSB, "false");
        defaults.put(ErpZoneInfoSB, "false");
        defaults.put(RecommendationEventLoggerSB , "false");
        defaults.put(EventLoggerSB , "false");
        defaults.put(FDFactorySB , "false");
        defaults.put(FDCouponActivityLogSB, "false");
        defaults.put(EwalletActivityLogSB , "false");
        defaults.put(FDExtoleManagerSB , "false");
        defaults.put(ErpEWalletSB , "false");
        defaults.put(FDSurveySB , "false");
        defaults.put(ExternalAccountManagerSB , "false");
        defaults.put(FDGrpInfoSB , "false");
        defaults.put(FDZoneInfoSB , "false");
        defaults.put(ScoreFactorSB , "false");
        defaults.put(DyfModelSB, "false");
        defaults.put(SessionImpressionLogSB , "false");
        defaults.put(RulesManagerSB , "false");
        defaults.put(TestSupportSB, "false");
        defaults.put(ErpInfoSB , "false");
        defaults.put(ErpRoutingGatewaySB , "false");
        
        

        refresh();
    }

    private FDEcommProperties() {
    }

    private static void refresh() {
        refresh(false);
    }

    private synchronized static void refresh(boolean force) {
        long t = System.currentTimeMillis();

        if (true/*force || ((t - lastRefresh) > REFRESH_PERIOD)*/) {
            config = ConfigHelper.getPropertiesFromDB(EnumPropertyType.WEB.toString(),
            		FDStoreProperties.getClusterName(),
            		FDStoreProperties.getNodeName(), defaults);
            lastRefresh = t;
            LOGGER.info("Loaded configuration from FDStorePropertiesDB Database: " + config);
            fireEvent();
        }
    }



	public static String get(String key) {
        refresh();

        return config.getProperty(key);
    }

    /**
     * A method to set a specific property. Use with care - it's here for testing purposes only.
     *
     * @param key
     *            the name of the property to set.
     * @param value
     *            the new value of the property.
     */
    public static void set(String key, String value) {
        refresh();
        config.setProperty(key, value);
    }


    public static void forceRefresh() {
        refresh(true);
    }


    public static void addConfigLoadedListener(ConfigLoadedListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    private static void fireEvent() {
        synchronized (listeners) {
            for (ConfigLoadedListener listener : listeners) {
                listener.configLoaded();
            }
        }
    }

   

    public static interface ConfigLoadedListener {

        void configLoaded();
    }

    public static Properties getConfig() {
        return config;
    }

	public static boolean isServiceEnabled(String beanName) {
		refresh();
		if(config.containsKey(beanName))
    	return (Boolean.valueOf(config.getProperty(beanName))).booleanValue();
		return false;
	}
	
	
}
