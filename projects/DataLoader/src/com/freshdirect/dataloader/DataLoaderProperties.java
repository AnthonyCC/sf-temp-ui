package com.freshdirect.dataloader;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

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
	private final static String PROP_PAYMENTECH_BIN_IP = "dataloader.paymentech.bin.ip";
	private final static String PROP_PAYMENTECH_BIN_PORT = "dataloader.paymentech.bin.port";
	
	private final static String PROP_PAYMENTECH_SFTP_HOST = "dataloader.paymentech.sftp.host";
	private final static String PROP_PAYMENTECH_SFTP_USER = "dataloader.paymentech.sftp.user";
	private final static String PROP_PAYMENTECH_SFTP_PASSWORD = "dataloader.paymentech.sftp.password";
	private final static String PROP_PAYMENTECH_SFTP_PRIVATE_KEY = "dataloader.paymentech.sftp.privateKey";
	private final static String PROP_PAYMENTECH_SFTP_ENABLED="dataloader.paymentech.sftp.enabled";
	private final static String PROP_PAYMENTECH_SFTP_DELETE_FILES="dataloader.paymentech.sftp.deleteFiles";
	private static final String PROP_SETTLEMENT_FAILURE_FILE_NAME="dataloader.paymentech.stf.fileName";
	private final static String PROP_PAYMENTECH_STF_SFTP_HOST = "dataloader.paymentech.stf.sftp.host";
	private final static String PROP_PAYMENTECH_STF_SFTP_USER = "dataloader.paymentech.stf.sftp.user";
	private final static String PROP_PAYMENTECH_STF_SFTP_PASSWORD = "dataloader.paymentech.stf.sftp.password";
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
		defaults.put(PROP_WORKING_DIR, "c:/temp/");
		defaults.put(PROP_SAP_FILE_NAME_PREFIX, "sapReconciliation");
		defaults.put(PROP_SAP_FTP_IP, "12.39.155.119");
		defaults.put(PROP_SAP_FTP_USER, "fdweb2sap");
		defaults.put(PROP_SAP_FTP_PASSWORD, "123import");
		defaults.put(PROP_SAP_UPLOAD_FOLDER, "/userdata/web_uploads/");
		defaults.put(PROP_PAYMENTECH_BATCH_IP, "208.237.46.10");
		defaults.put(PROP_PAYMENTECH_BATCH_PORT, "2600");
		defaults.put(PROP_PAYMENTECH_BIN_IP, "206.253.180.137");
		defaults.put(PROP_PAYMENTECH_BIN_PORT, "8522");
		
		defaults.put(PROP_PAYMENTECH_SFTP_HOST, "netconnectbatchvar1.chasepaymentech.net");
		defaults.put(PROP_PAYMENTECH_SFTP_USER, "SVSMVJK7");
		defaults.put(PROP_PAYMENTECH_SFTP_PASSWORD, "D77BSZYG");
		defaults.put(PROP_PAYMENTECH_SFTP_PRIVATE_KEY,"id_rsa_2048_testing_storefront_paymentech");
		defaults.put(PROP_PAYMENTECH_SFTP_ENABLED,"true");
		defaults.put(PROP_PAYMENTECH_SFTP_DELETE_FILES, "false");
		defaults.put(PROP_SETTLEMENT_FAILURE_FILE_NAME,"RejectedDetailRpt");
		defaults.put(PROP_PAYMENTECH_STF_SFTP_HOST, "netconnectbatchvar1.chasepaymentech.net");
		defaults.put(PROP_PAYMENTECH_STF_SFTP_USER, "SVSMVJK7");
		defaults.put(PROP_PAYMENTECH_STF_SFTP_PASSWORD, "D77BSZYG");
		
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
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.PROVIDER_URL, getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory() );
		return new InitialContext(env);
	}
	
	public static String getPaymentechBinIp(){
		return config.getProperty(PROP_PAYMENTECH_BIN_IP);
	}
	
	public static String getPaymentechBinPort(){
		return config.getProperty(PROP_PAYMENTECH_BIN_PORT);
	}

	
	public static String getPaymentSFTPHost() {
		return config.getProperty(PROP_PAYMENTECH_SFTP_HOST);
	}
	
	public static String getPaymentSFTPUser() {
		return config.getProperty(PROP_PAYMENTECH_SFTP_USER);
	}
	
	public static String getPaymentSFTPPassword() {
		return config.getProperty(PROP_PAYMENTECH_SFTP_PASSWORD);
	}
	
	public static String getPaymentSFTPKey() {
		return config.getProperty(PROP_PAYMENTECH_SFTP_PRIVATE_KEY);
	}
	
	public static boolean isPaymentechSFTPEnabled() {
		return Boolean.valueOf(config.getProperty(PROP_PAYMENTECH_SFTP_ENABLED)).booleanValue();
	}
	public static boolean isPaymentechSFTPFileDeletionEnabled() {
		return Boolean.valueOf(config.getProperty(PROP_PAYMENTECH_SFTP_DELETE_FILES)).booleanValue();
	}
	public static String getSettlementFailureFileName(){
    	return config.getProperty(PROP_SETTLEMENT_FAILURE_FILE_NAME);
    }
	
	public static String getPaymentStfSFTPHost() {
		return config.getProperty(PROP_PAYMENTECH_STF_SFTP_HOST);
	}
	
	public static String getPaymentStfSFTPUser() {
		return config.getProperty(PROP_PAYMENTECH_STF_SFTP_USER);
	}
	
	public static String getPaymentStfSFTPPassword() {
		return config.getProperty(PROP_PAYMENTECH_STF_SFTP_PASSWORD);
	}
}
