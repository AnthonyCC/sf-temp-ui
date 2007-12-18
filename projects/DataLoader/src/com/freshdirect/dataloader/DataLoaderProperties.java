package com.freshdirect.dataloader;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.Properties;
import java.util.Hashtable;

import com.freshdirect.framework.util.ConfigHelper;

public class DataLoaderProperties {
	
	private static Category LOGGER = LoggerFactory.getInstance( DataLoaderProperties.class );
	
	private final static String PROP_PROVIDER_URL = "dataloader.providerURL";
	private final static String PROP_INIT_CTX_FACTORY = "dataloader.initialContextFactory";
	private final static String PROP_FTP_IP = "dataloader.ftp.ip";
	private final static String PROP_FTP_USER = "dataloader.ftp.user";
	private final static String PROP_FTP_PASSWORD = "dataloader.ftp.password";
	private final static String PROP_SUMMARY_FILE_NAME = "dataloader.summaryFile.name";
	private final static String PROP_TRANSACTION_FILE_NAME = "dataloader.transactionFile.name";
	private final static String PROP_WORKING_DIR = "dataloader.working.dir";
	private final static String PROP_SAP_FILE_NAME_PREFIX = "dataloader.sapFile.name.prefix";
	private final static String PROP_SAP_FTP_IP = "dataloader.sap.ftp.ip";
	private final static String PROP_SAP_FTP_USER = "dataloader.sap.ftp.user";
	private final static String PROP_SAP_FTP_PASSWORD = "dataloader.sap.ftp.password";
	private final static String PROP_SAP_UPLOAD_FOLDER = "dataloader.sap.upload.folder";
	private final static String PROP_PAYMENTECH_BATCH_IP = "dataloader.paymentech.batch.ip";
	private final static String PROP_PAYMENTECH_BATCH_PORT = "dataloader.paymentech.batch.port";
	
	private final static Properties config;
	
	static {
		Properties defaults = new Properties();
		defaults.put(PROP_PROVIDER_URL, 	"t3://127.0.0.1:8080");
		defaults.put(PROP_INIT_CTX_FACTORY,	"weblogic.jndi.WLInitialContextFactory");
		defaults.put(PROP_FTP_IP, "ems1.nyc1.freshdirect.com");
		defaults.put(PROP_FTP_USER, "bmadmin");
		defaults.put(PROP_FTP_PASSWORD, "sun1ray");
		defaults.put(PROP_SUMMARY_FILE_NAME, "M044.txt");
		defaults.put(PROP_TRANSACTION_FILE_NAME, "E012.txt");
		defaults.put(PROP_WORKING_DIR, "c:/tmp");
		defaults.put(PROP_SAP_FILE_NAME_PREFIX, "sapReconciliation");
		defaults.put(PROP_SAP_FTP_IP, "12.39.155.119");
		defaults.put(PROP_SAP_FTP_USER, "fdweb2sap");
		defaults.put(PROP_SAP_FTP_PASSWORD, "123import");
		defaults.put(PROP_SAP_UPLOAD_FOLDER, "/userdata/web_uploads/");
		defaults.put(PROP_PAYMENTECH_BATCH_IP, "208.237.46.10");
		defaults.put(PROP_PAYMENTECH_BATCH_PORT, "2600");
		
		config = ConfigHelper.getPropertiesFromClassLoader("erpservices.properties", defaults);
		LOGGER.info("Loaded configuration for DataLoader: "+config);
	}
	/** Creates new DlvProperties */
    public DataLoaderProperties() {
    }
	
	public static String getProviderURL() {
		return config.getProperty(PROP_PROVIDER_URL);
	}
	
	public static String getFtpIp(){
		return config.getProperty(PROP_FTP_IP);
	}
	
	public static String getFtpUser() {
		return config.getProperty(PROP_FTP_USER);
	}
	
	public static String getFtpPassword(){
		return config.getProperty(PROP_FTP_PASSWORD);
	}
	
	public static String getSapFtpIp(){
		return config.getProperty(PROP_SAP_FTP_IP);
	}
	
	public static String getSapFtpUser(){
		return config.getProperty(PROP_SAP_FTP_USER);
	}
	
	public static String getSapFtpPassword(){
		return config.getProperty(PROP_SAP_FTP_PASSWORD);
	}
	
	public static String getSapUploadFolder(){
		return config.getProperty(PROP_SAP_UPLOAD_FOLDER);
	}
	
	public static String getSummaryFileName(){
		return config.getProperty(PROP_SUMMARY_FILE_NAME);
	}
	
	public static String getTransactionFileName(){
		return config.getProperty(PROP_TRANSACTION_FILE_NAME);
	}
	
	public static String getInitialContextFactory() {
		return config.getProperty(PROP_INIT_CTX_FACTORY);
	}
	
	public static String getWorkingDir(){
		return config.getProperty(PROP_WORKING_DIR);
	}
	
	public static String getSapFileNamePrefix(){
		return config.getProperty(PROP_SAP_FILE_NAME_PREFIX);
	}
	
	public static String getPaymentechBatchIp(){
		return config.getProperty(PROP_PAYMENTECH_BATCH_IP);
	}
	
	public static String getPaymentechBatchPort(){
		return config.getProperty(PROP_PAYMENTECH_BATCH_PORT);
	}
	
	public static Context getInitialContext() throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.PROVIDER_URL, getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory() );
		return new InitialContext(env);
	}

}
