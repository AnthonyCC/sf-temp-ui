package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.CreateException;

import org.apache.log4j.Category;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.ecommerce.data.enums.EnumPropertyType;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.monitor.ejb.ErpMonitorHome;
import com.freshdirect.monitor.ejb.ErpMonitorSB;

public class FDEcommProperties {
	
	

    private static final Category LOGGER = LoggerFactory.getInstance(FDEcommProperties.class);
    private static List<ConfigLoadedListener> listeners = new ArrayList<ConfigLoadedListener>();
    private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy-MM-dd");
   
    private static Properties config;
    private final static Properties defaults = new Properties();
  
    private static long lastRefresh = 0;
    private final static long REFRESH_PERIOD = 5 * 60 * 1000;
    
    private final static String FALSE="false";
    public final static String FDMonitorSB = "fdstore.monitor.FDMonitorSB";
    public final static String ErpCOOLManagerSB = "erp.ejb.ErpCOOLManagerSB";
    public final static String ErpGrpInfoSB = "erp.ejb.ErpGrpInfoSB";
    public static final String EnumManagerSB = "enums.ejb.EnumManagerSB";
    public final static String SapGrpInfoLoaderSB = "sap.ejb.SapGrpInfoLoaderSB";
    public final static String ErpInventoryManagerSB = "erp.ejb.ErpInventoryManagerSB";
    public final static String DlvManagerSB = "delivery.ejb.DlvManagerSB";
    public final static String ActivityLogSB = "customer.ejb.ActivityLogSB";
    public final static String FDBrandProductsAdManagerSB = "fdstore.brandads.FDBrandProductsAdManagerSB";
    public final static String BINInfoManagerSB = "payment.ejb.BINInfoManagerSB";
    public final static String GCGatewaySB = "giftcard.ejb.GCGatewaySB";
    public final static String SAPProductFamilyLoaderSB = "sap.ejb.SAPProductFamilyLoaderSB";
    public final static String SmsAlertsSB = "sms.ejb.SmsAlertsSB";
    public final static String SAPZoneInfoLoaderSB = "sap.ejb.SAPZoneInfoLoaderSB";
    public final static String BatchManagerSB = "erp.ejb.BatchManagerSB";
    public final static String AttributeFacadeSB = "attributes.ejb.AttributeFacadeSB";
    public final static String ErpProductPromotionInfoSB = "erp.ejb.ErpProductPromotionInfoSB";
    public final static String ErpZoneInfoSB = "erp.ejb.ErpZoneInfoSB";
    public final static String RecommendationEventLoggerSB = "event.ejb.RecommendationEventLoggerSB";
    public final static String EventLoggerSB = "event.ejb.EventLoggerSB";
    public final static String FDFactorySB = "fdstore.ejb.FDFactorySB";
    public final static String FDCouponActivityLogSB = "fdstore.ecoupon.FDCouponActivityLogSB";
    public final static String EwalletActivityLogSB = "ewallet.ejb.EwalletActivityLogSB";
    public final static String FDExtoleManagerSB = "referral.extole.FDExtoleManagerSB";
    public final static String ErpEWalletSB = "erp.ejb.ErpEWalletSB";
    public final static String FDSurveySB = "survey.ejb.FDSurveySB";
    public final static String ExternalAccountManagerSB = "accounts.external.ExternalAccountManagerSB";
    public final static String FDGrpInfoSB = "grp.ejb.FDGrpInfoSB";
    public final static String FDZoneInfoSB = "zone.ejb.FDZoneInfoSB";
    public final static String ScoreFactorSB = "smartstore.ejb.ScoreFactorSB";
    public final static String DyfModelSB = "smartstore.ejb.DyfModelSB";
    public final static String SessionImpressionLogSB = "smartstore.ejb.SessionImpressionLogSB";
    public final static String RulesManagerSB = "rules.ejb.RulesManagerSB";
    public final static String TestSupportSB = "test.ejb.TestSupportSB";
    public final static String ErpInfoSB = "erp.ejb.ErpInfoSB";
    public final static String ErpRoutingGatewaySB ="routing.ejb.ErpRoutingGatewaySB";
    public  final static String FDXOrderPickEligibleSB ="erp.ejb.FDXOrderPickEligibleSB";//story SF17-64
    public  final static String SAPLoaderSB ="sap.ejb.SAPLoaderSB";
    public  final static String CmsFeedmanagerSB ="fdstore.cms.CMSFeedManagerSB";//story SF17-22
    public final static  String FDReferralManagerSB = "fdstore.referral.ejb.FDReferralManagerSB";
	public final static String MailerGatewaySB ="mail.ejb.MailerGatewaySB";
	public final static String ErpProductFamilySB ="erp.ejb.ErpProductFamilySB";
	public final static String RestrictedPaymentMethodSB = "fraud.ejb.RestrictedPaymentMethodSB";
	public static final String ErpNutritionSB = "nutrition.ejb.ErpNutritionSB";
	public static final String ErpComplaintManagerSB = "customer.ejb.ErpComplaintManagerSB";
	public static final String EwalletNotifyStatusSB = "ewallet.ejb.EwalletNotifyStatusSB";
	public static final String DlvPassManagerSB = "deliverypass.ejb.DlvPassManagerSB";
	public static final String SmartStoreServiceConfigurationSB = "smartstore.ejb.SmartStoreServiceConfigurationSB";
	public static final String PaymentGatewaySB ="payment.ejb.PaymentGatewaySB";
	public static final String FDListManagerSB = "fdstore.lists.ejb.FDListManagerSB";
	public static final String EwalletServiceSB = "ewallet.ejb.EwalletServiceSB";
	public static final String CoremetricsCdfServiceSB = "coremetrics.service.CoremetricsCdfServiceSB";
	public static final String CrmManagerSB = "crm.ejb.CrmManagerSB";
	public final static  String FDPromotionManagerNewSB = "fdstore.ejb.FDPromotionNewManagerSB";//story SF17-88
	public final static  String FDSFGatewayStatsLogging = "fdstore.promotion.FDSFGatewayStatsLogging";//introduced with story SF17-88
	public static final String CallCenterManagerSB = "com.freshdirect.fdstore.customer.CallCenterManagerSB"; 
	public static final String PaymentSB = "com.freshdirect.payment.PaymentSB"; 
 
  
    
    
    static {

        defaults.put(FDMonitorSB,FALSE);
        defaults.put(ErpCOOLManagerSB ,FALSE);
        defaults.put(ErpGrpInfoSB,FALSE);
        defaults.put(EnumManagerSB,FALSE);
        defaults.put(SapGrpInfoLoaderSB,FALSE);
        defaults.put(ErpInventoryManagerSB ,FALSE);
        defaults.put(DlvManagerSB ,FALSE);
        defaults.put(ActivityLogSB ,FALSE);
        defaults.put(FDBrandProductsAdManagerSB ,FALSE);
        defaults.put(BINInfoManagerSB,FALSE);
        defaults.put(GCGatewaySB,FALSE);
        defaults.put(SAPProductFamilyLoaderSB,FALSE);
        defaults.put(SmsAlertsSB ,FALSE);
        defaults.put(SAPZoneInfoLoaderSB,FALSE);
        defaults.put(BatchManagerSB,FALSE);
        defaults.put(AttributeFacadeSB ,FALSE);
        defaults.put(ErpProductPromotionInfoSB,FALSE);
        defaults.put(ErpZoneInfoSB,FALSE);
        defaults.put(RecommendationEventLoggerSB ,FALSE);
        defaults.put(EventLoggerSB ,FALSE);
        defaults.put(FDFactorySB ,FALSE);
        defaults.put(FDCouponActivityLogSB,FALSE);
        defaults.put(EwalletActivityLogSB ,FALSE);
        defaults.put(FDExtoleManagerSB ,FALSE);
        defaults.put(ErpEWalletSB ,FALSE);
        defaults.put(FDSurveySB ,FALSE);
        defaults.put(ExternalAccountManagerSB ,FALSE);
        defaults.put(FDGrpInfoSB ,FALSE);
        defaults.put(FDZoneInfoSB ,FALSE);
        defaults.put(ScoreFactorSB ,FALSE);
        defaults.put(DyfModelSB,FALSE);
        defaults.put(SessionImpressionLogSB ,FALSE);
        defaults.put(RulesManagerSB ,FALSE);
        defaults.put(TestSupportSB,FALSE);
        defaults.put(ErpInfoSB ,FALSE);
        defaults.put(ErpRoutingGatewaySB ,FALSE);
        defaults.put(FDXOrderPickEligibleSB, FALSE);
        defaults.put(SAPLoaderSB, FALSE);
        defaults.put(CmsFeedmanagerSB, FALSE);
        defaults.put(FDReferralManagerSB, FALSE);
        defaults.put(ErpProductFamilySB,FALSE);
        defaults.put(RestrictedPaymentMethodSB,FALSE);
        defaults.put(ErpComplaintManagerSB,FALSE);
        defaults.put(DlvPassManagerSB, FALSE);
        defaults.put(SmartStoreServiceConfigurationSB, FALSE);
        defaults.put(PaymentGatewaySB, FALSE);
        defaults.put(FDListManagerSB, FALSE);
        defaults.put(EwalletNotifyStatusSB, FALSE);
        defaults.put(EwalletServiceSB, FALSE);
        defaults.put(CoremetricsCdfServiceSB, FALSE);
        defaults.put(CrmManagerSB, FALSE);
        defaults.put(CallCenterManagerSB, FALSE);
        defaults.put(PaymentSB, FALSE);
        refresh();
    }

    private FDEcommProperties() {
    }

    private static void refresh() {
        refresh(false);
    }

    private synchronized static void refresh(boolean force) {
        long t = System.currentTimeMillis();

        if (force || ((t - lastRefresh) > REFRESH_PERIOD)) {
        	ErpMonitorHome monitorHome = ERPServiceLocator.getInstance().getErpMonitorHome();
             	ErpMonitorSB sb;
			try {
				sb = monitorHome.create();
          
				config = sb.monitorAndLoadProperties(EnumPropertyType.WEB.toString(),
						FDStoreProperties.getClusterName(),
						FDStoreProperties.getNodeName(), defaults);
			} catch (FDResourceException e) {
				e.printStackTrace();
				 LOGGER.error("Error loading FDStorePropertiesDB Database FDResourceException: " + config);
			} catch (RemoteException e) {
				 LOGGER.error("Error loading FDStorePropertiesDB Database RemoteException: " + config);
				e.printStackTrace();
			}catch (CreateException e) {
				 LOGGER.error("Error loading FDStorePropertiesDB Database CreationException: " + config);
				e.printStackTrace();
			}
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
	
	/** similar to the mechanism for checking if a SB service is enabled <BR>
	 * but instead is a general feature by feature name
	 * @param featureName String 
	 * @return
	 */
	public static boolean isFeatureEnabled(String featureName) {
		refresh();
		if(config.containsKey(featureName))
    	return (Boolean.valueOf(config.getProperty(featureName))).booleanValue();
		return false;
	}


	
}
