package com.freshdirect.fdstore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Category;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author Aniwesh Vatsal
 * 
 */
public class PayPalData {

	private final static Category LOGGER = LoggerFactory.getInstance(PayPalData.class);
	
	private static String configFile = "";
	
	private String merchantId;
	private String privateKey;
	private String publicKey;
	private String environment;
	private String merchantFRESHDIRECT;
	private String merchantFDW;
	
	private static final String PAYPAL_ENVIRONMENT_PRODL = "PRODUCTION";
	
	private static final String ENV_PROP = "environment";
	private static final String MERCHANT_ID_PROP = "merchantId";
	private static final String PRIVATE_KEY_PROP = "privateKey";
	private static final String PUBLIC_KEY_PROP = "publicKey";
	private static final String MERCHANT_FD_PROP = "Merchant.FRESHDIRECT";
	private static final String MERCHANT_FDW_PROP = "Merchant.FDW";
	
	private final static Properties config;
	
	static {
		Properties defaults = new Properties();
		defaults.put(ENV_PROP, "SANDBOX");
		defaults.put(MERCHANT_ID_PROP, "r2gxdqrcpthptsym");
		defaults.put(PRIVATE_KEY_PROP, "c540c4a827a8732e880a01a9bc3272d1");
		defaults.put(PUBLIC_KEY_PROP, "mskc3nzddx5qz6qv");
		defaults.put(MERCHANT_FD_PROP, "mrunal.doddanavar-facilitator@igate.com");
		defaults.put(MERCHANT_FDW_PROP, "mrunal.doddanavar-facilitatorFDW@igate.com");
		
		config = ConfigHelper.getPropertiesFromClassLoader("PayPal-Profile.ini", defaults);
		LOGGER.info("Loaded configuration: "+config);
	}
	
	private static BraintreeGateway gateway = null;
	
	public static BraintreeGateway getBraintreeGateway(){
		if(gateway == null){
			String envProp = config.getProperty(ENV_PROP);
			Environment environment = null;
			if(envProp != null && envProp.equalsIgnoreCase(PAYPAL_ENVIRONMENT_PRODL)){
				environment = Environment.PRODUCTION;
			}else{
				environment = Environment.SANDBOX;
			}
			gateway = new BraintreeGateway(
					environment,
					getMerchantId(),
					getPublicKey(),
					getPrivateKey()
					);
		}
		return gateway;
	}
	
	/*private static BraintreeGateway gateway = new BraintreeGateway(
			Environment.SANDBOX,
			"r2gxdqrcpthptsym",
			 "mskc3nzddx5qz6qv",
			 "c540c4a827a8732e880a01a9bc3272d1"
			);*/

	/**
	 * @return
	 */
/*	private static BraintreeGateway getBraintreeGateway(){
		Environment environment = null;
		if(gateway == null){
			PayPalData payPalData = new PayPalData();
			if(payPalData.getEnvironment() != null && payPalData.getEnvironment().equalsIgnoreCase(PAYPAL_ENVIRONMENT_PRODL)){
				environment = Environment.PRODUCTION;
			}else{
				environment = Environment.SANDBOX;
			}
			gateway = new BraintreeGateway(
					environment,
					payPalData.getMerchantId(),
					payPalData.getPublicKey(),
					payPalData.getPrivateKey()
					);
		}
		return gateway;
	}*/

	/**
	 * No argument constructor
	 */
	public PayPalData() {
		//this(configFile);
	}

	/**
	 * @param configFile
	 */
/*	public PayPalData(String configFile) {
		configFile = FDStoreProperties.getPayPalEnvironment();
		Properties props = loadProperties(configFile);
		if(props != null){
			this.environment = props.getProperty("environment");
			this.merchantId = props.getProperty("merchantId");
			this.privateKey = props.getProperty("privateKey");
			this.publicKey = props.getProperty("publicKey");
			this.merchantFRESHDIRECT = props.getProperty("Merchant.FRESHDIRECT");
			this.merchantFDW = props.getProperty("Merchant.FDW");
		} else {
			LOGGER.error("Properties is null while loading PayPal configuration file");
		}
	}*/

	/**
	 * @param configFile
	 * @return
	 */
	private Properties loadProperties(String configFile) {
		Properties props = new Properties();
		InputStream stream = null;
		try {
			stream = ClassLoader.getSystemResourceAsStream(configFile);
			if (stream != null) {
				props.load(stream);
			} else {
				LOGGER.error("Stream is null while loading PayPal configuration file");
			}
			return props;
		} catch (IOException ioe) {
			LOGGER.error("Exception while reading PayPal configuration file");
			return null;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// Trying to close. IOException is okay here
				}
			}
		}
	}

	/**
	 * @return the merchantId
	 */
	public static String getMerchantId() {
		//return merchantId;
		return config.getProperty(MERCHANT_ID_PROP);
	}

	/**
	 * @param merchantId the merchantId to set
	 */
	public static void setMerchantId(String merchantId) {
		//this.merchantId = merchantId;
		config.setProperty(MERCHANT_ID_PROP, merchantId);
	}

	/**
	 * @return the privateKey
	 */
	public static String getPrivateKey() {
		//return privateKey;
		return config.getProperty(PRIVATE_KEY_PROP);
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public static void setPrivateKey(String privateKey) {
		//this.privateKey = privateKey;
		config.setProperty(PRIVATE_KEY_PROP, privateKey);
	}

	/**
	 * @return the publicKey
	 */
	public static String getPublicKey() {
		//return publicKey;
		return config.getProperty(PUBLIC_KEY_PROP);
	}

	/**
	 * @param publicKey the publicKey to set
	 */
	public static void setPublicKey(String publicKey) {
		//this.publicKey = publicKey;
		config.setProperty(PUBLIC_KEY_PROP, publicKey);
	}

	/**
	 * @return the environment
	 */
	public static String getEnvironment() {
		//return environment;
		return config.getProperty(ENV_PROP);
	}

	/**
	 * @param environment the environment to set
	 */
	public static void setEnvironment(String environment) {
		//this.environment = environment;
		config.setProperty(ENV_PROP, environment);
	}

	/**
	 * @return the merchantFRESHDIRECT
	 */
	public static String getMerchantFRESHDIRECT() {
		//return merchantFRESHDIRECT;
		return config.getProperty(MERCHANT_FD_PROP);
	}

	/**
	 * @param merchantFRESHDIRECT the merchantFRESHDIRECT to set
	 */
	public void setMerchantFRESHDIRECT(String merchantFRESHDIRECT) {
		//this.merchantFRESHDIRECT = merchantFRESHDIRECT;
		config.setProperty(MERCHANT_FD_PROP, merchantFRESHDIRECT);
	}

	/**
	 * @return the merchantFDW
	 */
	public String getMerchantFDW() {
		//return merchantFDW;
		return config.getProperty(MERCHANT_FDW_PROP);
	}

	/**
	 * @param merchantFDW the merchantFDW to set
	 */
	public void setMerchantFDW(String merchantFDW) {
		//this.merchantFDW = merchantFDW;
		config.setProperty(MERCHANT_FDW_PROP, merchantFDW);
	}

}
