/*
 * DlvProperties.java
 *
 * Created on November 12, 2001, 3:00 PM
 */

package com.freshdirect.delivery;

/**
 *
 * @author  knadeem
 * @version 
 */
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.Properties;
import java.util.Hashtable;

import com.freshdirect.framework.util.ConfigHelper;


public class DlvProperties {
	
	private static Category LOGGER = LoggerFactory.getInstance( DlvProperties.class );
	
	private final static String PROP_PROVIDER_URL				= "delivery.providerURL";
	private final static String PROP_MAPMARKER_CONNECTION_TYPE 	= "mapmarker.connectiontype";
	private final static String PROP_MAPMARKER_IP 				= "mapmarker.ip";
	private final static String PROP_MAPMARKER_PORT 				= "mapmarker.port";
	private final static String PROP_INIT_CTX_FACTORY			= "delivery.initialContextFactory";
	private final static String PROP_DLVREGION_HOME				= "delivery.region.home";
	private final static String PROP_DLVSPECIALDATE_HOME 		= "delivery.specialDate.home";
	private final static String PROP_DLVTEMPLATEMANAGER_HOME 	= "delivery.templateManager.home";
	private final static String PROP_DLVDEPOT_HOME 				= "delivery.depot.home";
	private final static String PROP_DLVDEPOTMANAGER_HOME 		= "delivery.depotManager.home";
	private final static String PROP_DLVMANAGER_HOME 			= "delivery.manager.home";
	private final static String PROP_PAYMENTGATEWAY_HOME 		= "payment.gateway.home";
	private final static String PROP_CUSTOMER_MANAGER_HOME 		= "customer.manager.home";
	private final static String PROP_PAYMENT_HOME 				= "payment.home";
	private final static String PROP_PLAN_HOME					= "plan.home";
	private final static String PROP_RESOURCE_HOME 				= "resource.home";
	private final static String PROP_ADMIN_MANAGER_HOME 			= "adminmanager.home";
	private final static String PROP_SESSIONAUDITOR_HOME 			= "sessionauditor.home";
	private final static String PROP_MAPPER_HOME 				= "mapper.home";
	private final static String PROP_REPORT_HOME 				= "report.home";
	private final static String PROP_FREE_SPATIAL_ONLY		= "delivery.free.spatial.only";
	
	private final static Properties config;
	static {
		Properties defaults = new Properties();

		defaults.put(PROP_PROVIDER_URL, 	"t3://127.0.0.1:8080");
		defaults.put(PROP_MAPMARKER_CONNECTION_TYPE, "TCPIP");
		defaults.put(PROP_MAPMARKER_IP, "localhost");
		defaults.put(PROP_MAPMARKER_PORT, "4141");
		defaults.put(PROP_INIT_CTX_FACTORY,	"weblogic.jndi.WLInitialContextFactory");

		defaults.put(PROP_DLVREGION_HOME,	"freshdirect.delivery.Region");
		defaults.put(PROP_DLVSPECIALDATE_HOME, "freshdirect.delivery.SpecialDate");
		defaults.put(PROP_DLVTEMPLATEMANAGER_HOME, "freshdirect.delivery.TemplateManager");
		defaults.put(PROP_DLVDEPOT_HOME, "freshdirect.delivery.Depot");
		defaults.put(PROP_DLVDEPOTMANAGER_HOME, "freshdirect.delivery.DepotManager");
		defaults.put(PROP_DLVMANAGER_HOME, "freshdirect.delivery.DeliveryManager");
		defaults.put(PROP_PAYMENTGATEWAY_HOME, "freshdirect.payment.Gateway");
		defaults.put(PROP_CUSTOMER_MANAGER_HOME, "freshdirect.erp.CustomerManager");
		defaults.put(PROP_PAYMENT_HOME, "freshdirect.payment.Payment");
		defaults.put(PROP_PLAN_HOME, "freshdirect.delivery.Plan");
		defaults.put(PROP_RESOURCE_HOME, "freshdirect.delivery.Resource");
		defaults.put(PROP_ADMIN_MANAGER_HOME, "freshdirect.delivery.AdminManager");
		defaults.put(PROP_SESSIONAUDITOR_HOME, "freshdirect.SessionAuditor");
		defaults.put(PROP_MAPPER_HOME, "freshdirect.delivery.Mapper");
		defaults.put(PROP_REPORT_HOME, "freshdirect.delivery.Report");
		defaults.put(PROP_FREE_SPATIAL_ONLY, "true");
				
		config = ConfigHelper.getPropertiesFromClassLoader("erpservices.properties", defaults);
		LOGGER.info("Loaded configuration for Delivery: "+config);
	}
	/** Creates new DlvProperties */
    public DlvProperties() {
    }
	
	public static String getProviderURL() {
		return config.getProperty(PROP_PROVIDER_URL);
	}
	
	public static String getMapmarkerConnectionType(){
		return config.getProperty(PROP_MAPMARKER_CONNECTION_TYPE);
	}
	
	public static String getMapmarkerIp(){
		return config.getProperty(PROP_MAPMARKER_IP);
	}
	
	public static String getMapmarkerPort(){
		return config.getProperty(PROP_MAPMARKER_PORT);
	}

	public static String getInitialContextFactory() {
		return config.getProperty(PROP_INIT_CTX_FACTORY);
	}

	public static String getDlvRegionHome() {
		return config.getProperty(PROP_DLVREGION_HOME);	
	}
	
	public static String getDlvSpecialDateHome(){
		return config.getProperty(PROP_DLVSPECIALDATE_HOME);
	}
	
	public static String getDlvTemplateManagerHome(){
		return config.getProperty(PROP_DLVTEMPLATEMANAGER_HOME);
	}
	
	public static String getDlvDepotHome(){
		return config.getProperty(PROP_DLVDEPOT_HOME);
	}
	
	public static String getDlvDepotManagerHome(){
		return config.getProperty(PROP_DLVDEPOTMANAGER_HOME);
	}
	
	public static String getDlvManagerHome(){
		return config.getProperty(PROP_DLVMANAGER_HOME);
	}
	
	public static String getDlvPlanHome(){
		return config.getProperty(PROP_PLAN_HOME);
	}
	
	public static String getDlvResourceHome(){
		return config.getProperty(PROP_RESOURCE_HOME);
	}
	
	public static String getDlvAdminManagerHome(){
		return config.getProperty(PROP_ADMIN_MANAGER_HOME);
	}

	public static String getSessionAuditorHome(){
		return config.getProperty(PROP_SESSIONAUDITOR_HOME);
	}

	public static String getPaymentGatewayHome(){
		return config.getProperty(PROP_PAYMENTGATEWAY_HOME);
	}
	
	public static String getCustomerManagerHome(){
		return config.getProperty(PROP_CUSTOMER_MANAGER_HOME);
	}
	
	public static String getPaymentHome(){
		return config.getProperty(PROP_PAYMENT_HOME);
	}
	
	public static String getMapperHome(){
		return config.getProperty(PROP_MAPPER_HOME);
	}
	
	public static String getReportHome(){
		return config.getProperty(PROP_REPORT_HOME);
	}
	
	public static boolean useFreeSpatialOnly(){
		return Boolean.valueOf(config.getProperty(PROP_FREE_SPATIAL_ONLY)).booleanValue();
	}

	public static Context getInitialContext() throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL, getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory() );
		return new InitialContext(env);
	}
}
