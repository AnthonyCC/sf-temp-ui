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
    public final static String GatewayActivityLog ="payment.ejb.GatewayActivityLogSB";
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
    public final static String FDCouponActivityLogSB = "fdstore.ecoupon.FDCouponActivityLogSB";
    public final static String EwalletActivityLogSB = "ewallet.ejb.EwalletActivityLogSB";
    public final static String FDExtoleManagerSB = "referral.extole.FDExtoleManagerSB";
    public final static String ErpEWalletSB = "erp.ejb.ErpEWalletSB";
    public final static String FDSurveySB = "survey.ejb.FDSurveySB";
    public final static String ExternalAccountManagerSB = "accounts.external.ExternalAccountManagerSB";
    public final static String FDCouponManagerSB = "fdstore.ecoupon.FDCouponManagerSB";
    public final static String FDGrpInfoSB = "grp.ejb.FDGrpInfoSB";
    public final static String ScoreFactorSB = "smartstore.ejb.ScoreFactorSB";
    public final static String DyfModelSB = "smartstore.ejb.DyfModelSB";
    public final static String SessionImpressionLogSB = "smartstore.ejb.SessionImpressionLogSB";
    public final static String RulesManagerSB = "rules.ejb.RulesManagerSB";
    public final static String ErpRoutingGatewaySB ="routing.ejb.ErpRoutingGatewaySB";
    public final static String FDXOrderPickEligibleSB ="erp.ejb.FDXOrderPickEligibleSB";//story SF17-64
    public final static String SAPLoaderSB ="sap.ejb.SAPLoaderSB";
    public final static String CmsFeedmanagerSB ="fdstore.cms.CMSFeedManagerSB";//story SF17-22
    public final static String FDReferralManagerSB = "fdstore.referral.ejb.FDReferralManagerSB";
	public final static String MailerGatewaySB ="mail.ejb.MailerGatewaySB";
	public final static String ErpProductFamilySB ="erp.ejb.ErpProductFamilySB";
	public final static String RestrictedPaymentMethodSB = "fraud.ejb.RestrictedPaymentMethodSB";
	public static final String ErpNutritionSB = "nutrition.ejb.ErpNutritionSB";
	public static final String ErpComplaintManagerSB = "customer.ejb.ErpComplaintManagerSB";
	public static final String DlvPassManagerSB = "deliverypass.ejb.DlvPassManagerSB";
	public static final String SmartStoreServiceConfigurationSB = "smartstore.ejb.SmartStoreServiceConfigurationSB";
	public static final String FDListManagerSB = "fdstore.lists.ejb.FDListManagerSB";
	public static final String PayPalServiceSB = "ewallet.ejb.PayPalServiceSB";
	public static final String MasterpassServiceSB = "ewallet.ejb.MasterpassServiceSB";
	public static final String CrmManagerSB = "crm.ejb.CrmManagerSB";
	public final static  String FDPromotionManagerNewSB = "fdstore.ejb.FDPromotionNewManagerSB";//story SF17-88
	public final static  String FDSFGatewayStatsLogging = "fdstore.promotion.FDSFGatewayStatsLogging";//introduced with story SF17-88
	public static final String CallCenterManagerSB = "com.freshdirect.fdstore.customer.CallCenterManagerSB"; 
	public static final String PaymentSB = "com.freshdirect.payment.PaymentSB";
	public static final String PaymentManagerSB = "com.freshdirect.payment.PaymentManagerSB";
	public static final String PaypalReconciliationSB = "payment.gateway.ewallet.impl.PaypalReconciliationSB";
	public static final String SitemapSB = "fdstore.sitemap.SitemapSB";
	public static final String GiftCardManagerSB = "giftcard.ejb.GiftCardManagerSB";
	public final static String PostSettlementNotifySB ="payment.ejb.PostSettlementNotifySB";
	
	public static final String ProductFeedSB = "com.freshdirect.fdstore.content.productfeed.ProductFeedSB";
	public static final String TEmailInfoSB = "com.freshdirect.fdstore.temails";
	public static final String ProfileCreatorSB = "com.freshdirect.dataloader.payment.ProfileCreatorSB";
	public static final String FDStandingOrderSB = "com.freshdirect.fdstore.standingorders.FDStandingOrdersSB"; 
	public static final String StandingOrder3CronSB = "com.freshdirect.fdstore.standingorders.service.StandingOrder3CronSB";
	public static final String StandingOrdersServiceSB = "com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceSB";
	public static final String FDCustomerManagerSB="com.freshdirect.customer.FDCustomerManagerSB";
	public static final String FDCustomerIdentity = "com.freshdirect.customer.identity";
	public static final String FDCustomerAddress = "com.freshdirect.customer.address";
	public static final String FDCustomerPayment = "com.freshdirect.customer.payment";
	public static final String FDCustomerPreference = "com.freshdirect.customer.preference";
	public static final String FDCustomerGiftCard = "com.freshdirect.customer.giftCard";
	public static final String FDCustomerComplaint = "com.freshdirect.customer.complaint";
	public static final String FDCustomerReport = "com.freshdirect.customer.report";
	public static final String FDCustomerNotification = "com.freshdirect.customer.notification";
	public static final String FDCustomerInfo = "com.freshdirect.customer.info";
	public static final String FDCustomerDeliveryPass = "com.freshdirect.customer.deliveryPass";
	public static final String FDCustomerOrder = "com.freshdirect.customer.order";
	public static final String FDCustomer = "com.freshdirect.customer";
	public static final String FDCustomerFactory = "com.freshdirect.fdstore.customer.FDCustomerFactory";
	public static final String Registration = "com.freshdirect.customer.registration";
	public static final String SaleCronSB = "payment.ejb.SaleCronSB";
	public static final String FDFactorySB_WarmUp ="fdstore.ejb.FDFactorySB_WarmUp";
	private static final String PROP_ECOM_SERVICE_CONNECTION_TIMEOUT = "ecom.service.conn.timeout";
	private static final String PROP_ECOM_SERVICE_CONNECTION_POOL = "ecom.service.conn.pool";
	private static final String PROP_ECOM_SERVICE_CONN_READ_TIMEOUT = "ecom.service.conn.read.timeout";
	private static final String PROP_ECOM_SERVICE_CONNECTION_REQUEST_TIMEOUT = "ecom.service.conn.request.timeout";
	public final static String PROP_JCO_CLIENT_LISTENER_COOL_INFO_ENABLED = "jco.client.listeners.coolinfo.enabled";
	public final static String PROP_JCO_CLIENT_LISTENER_PRODUCT_PROMOTION_ENABLED = "jco.client.listeners.productPromotion.enabled";
	public final static String PROP_JCO_CLIENT_LISTENER_INVENTORY_INFO_ENABLED = "jco.client.listeners.inventoryInfo.Enabled";
	public final static String PROP_JCO_CLIENT_LISTENER_RESTRICTED_AVAILABILITY_INFO_ENABLED = "jco.client.listeners.restrictedAvailabilityInfo.Enabled";
	public static final String ReconciliationSB="com.freshdirect.payment.ReconciliationSB";

   
    static {

        defaults.put(FDMonitorSB,FALSE);
        defaults.put(ProductFeedSB,FALSE);
        defaults.put(SaleCronSB,FALSE);
        defaults.put(GatewayActivityLog,FALSE);
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
        defaults.put(FDFactorySB_WarmUp ,FALSE);
        defaults.put(FDCouponActivityLogSB,FALSE);
        defaults.put(EwalletActivityLogSB ,FALSE);
        defaults.put(FDExtoleManagerSB ,FALSE);
        defaults.put(ErpEWalletSB ,FALSE);
        defaults.put(FDSurveySB ,FALSE);
        defaults.put(ExternalAccountManagerSB ,FALSE);
        defaults.put(FDGrpInfoSB ,FALSE);
        defaults.put(ScoreFactorSB ,FALSE);
        defaults.put(DyfModelSB,FALSE);
        defaults.put(SessionImpressionLogSB ,FALSE);
        defaults.put(RulesManagerSB ,FALSE);
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
        defaults.put(FDListManagerSB, FALSE);
        defaults.put(PayPalServiceSB, FALSE);
        defaults.put(MasterpassServiceSB, FALSE);
        defaults.put(CrmManagerSB, FALSE);
        defaults.put(FDPromotionManagerNewSB, FALSE);
        defaults.put(CallCenterManagerSB, FALSE);
        defaults.put(PaymentSB, FALSE);
        defaults.put(PaymentManagerSB, FALSE);
        defaults.put(TEmailInfoSB, FALSE);
        defaults.put(ProfileCreatorSB, FALSE);
        defaults.put(FDStandingOrderSB, FALSE);
        defaults.put(StandingOrder3CronSB, FALSE);
        defaults.put(FDCustomerManagerSB, FALSE);
        defaults.put(StandingOrdersServiceSB, FALSE);
        
        
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
            //LOGGER.info("Loaded configuration from FDStorePropertiesDB Database: " + config);
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

	 public static int getEcomServiceConnectionTimeout() {
	        try {
	            return Integer.parseInt(config.getProperty(PROP_ECOM_SERVICE_CONNECTION_TIMEOUT));
	        } catch (NumberFormatException e) {
	            return 60;
	        }
	    }

	    public static int getEcomServiceConnectionPool() {
	        try {
	            return Integer.parseInt(config.getProperty(PROP_ECOM_SERVICE_CONNECTION_POOL));
	        } catch (NumberFormatException e) {
	            return 12;
	        }
	    }

	    public static int getEcomServiceConnectionReadTimeout() {
	        try {
	            return Integer.parseInt(config.getProperty(PROP_ECOM_SERVICE_CONN_READ_TIMEOUT));
	        } catch (NumberFormatException e) {
	            return 360;
	        }
	    }

	    public static int getEcomServiceConnectionRequestTimeout() {
	        try {
	            return Integer.parseInt(config.getProperty(PROP_ECOM_SERVICE_CONNECTION_REQUEST_TIMEOUT));
	        } catch (NumberFormatException e) {
	            return 60;
	        }
	    }
	  
	
}
