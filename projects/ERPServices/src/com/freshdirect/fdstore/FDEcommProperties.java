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
    public static final String EnumManagerSB = "enums.ejb.EnumManagerSB";
    public final static String DlvManagerSB = "delivery.ejb.DlvManagerSB";
    public final static String GCGatewaySB = "giftcard.ejb.GCGatewaySB";
    public final static String SAPProductFamilyLoaderSB = "sap.ejb.SAPProductFamilyLoaderSB";
    public final static String SAPZoneInfoLoaderSB = "sap.ejb.SAPZoneInfoLoaderSB";
    public final static String AttributeFacadeSB = "attributes.ejb.AttributeFacadeSB";
    public final static String FDExtoleManagerSB = "referral.extole.FDExtoleManagerSB";
    public final static String FDCouponManagerSB = "fdstore.ecoupon.FDCouponManagerSB";
    public final static String SAPLoaderSB ="sap.ejb.SAPLoaderSB";
    public final static String MailerGatewaySB ="mail.ejb.MailerGatewaySB";
    public final static String CmsFeedmanagerSB ="fdstore.cms.CMSFeedManagerSB";//story SF17-22
	public final static String RestrictedPaymentMethodSB = "fraud.ejb.RestrictedPaymentMethodSB";
	public static final String ErpComplaintManagerSB = "customer.ejb.ErpComplaintManagerSB";
	public static final String PayPalServiceSB = "ewallet.ejb.PayPalServiceSB";
	public static final String MasterpassServiceSB = "ewallet.ejb.MasterpassServiceSB";
	public static final String CrmManagerSB = "crm.ejb.CrmManagerSB";
	public final static  String FDPromotionManagerNewSB = "fdstore.ejb.FDPromotionNewManagerSB";//story SF17-88
	public final static  String FDSFGatewayStatsLogging = "fdstore.promotion.FDSFGatewayStatsLogging";//introduced with story SF17-88
	public static final String CallCenterManagerSB = "com.freshdirect.fdstore.customer.CallCenterManagerSB";
	public final static String PostSettlementNotifySB ="payment.ejb.PostSettlementNotifySB";
	
	public static final String TEmailInfoSB = "com.freshdirect.fdstore.temails";
	public static final String FDStandingOrderSB = "com.freshdirect.fdstore.standingorders.FDStandingOrdersSB"; 
	public static final String StandingOrdersServiceSB = "com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceSB";
	public static final String FDCustomerComplaint = "com.freshdirect.customer.complaint";
	public static final String FDCustomerMisc1 = "com.freshdirect.customer.misc1";
	public static final String FDCustomerMisc2 = "com.freshdirect.customer.misc2";
	public static final String FDCustomerFactory = "com.freshdirect.fdstore.customer.FDCustomerFactory";
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
        defaults.put(SaleCronSB,FALSE);
        defaults.put(EnumManagerSB,FALSE);
        defaults.put(DlvManagerSB ,FALSE);
        defaults.put(GCGatewaySB,FALSE);
        defaults.put(SAPProductFamilyLoaderSB,FALSE);
        defaults.put(SAPZoneInfoLoaderSB,FALSE);
        defaults.put(AttributeFacadeSB ,FALSE);
        defaults.put(FDFactorySB_WarmUp ,FALSE);
        defaults.put(FDExtoleManagerSB ,FALSE);
        defaults.put(SAPLoaderSB, FALSE);
        defaults.put(CmsFeedmanagerSB, FALSE);
        defaults.put(RestrictedPaymentMethodSB,FALSE);
        defaults.put(ErpComplaintManagerSB,FALSE);
        defaults.put(PayPalServiceSB, FALSE);
        defaults.put(MasterpassServiceSB, FALSE);
        defaults.put(CrmManagerSB, FALSE);
        defaults.put(FDPromotionManagerNewSB, FALSE);
        defaults.put(CallCenterManagerSB, FALSE);
        defaults.put(TEmailInfoSB, FALSE);
        defaults.put(FDStandingOrderSB, FALSE);
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
