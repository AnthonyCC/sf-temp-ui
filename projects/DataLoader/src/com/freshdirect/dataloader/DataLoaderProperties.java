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

	//PayPal settlement related properties - Start
	private final static String PROP_PP_FTP_IP = "dataloader.pp.ftp.ip";
	private final static String PROP_PP_FTP_USER = "dataloader.pp.ftp.user";
	private final static String PROP_PP_FTP_PASSWORD = "dataloader.pp.ftp.password";
	private final static String PROP_PP_SETTLEMENT_FOLDER = "dataloader.pp.ftp.folder";
	private final static String PROP_PP_SETTLEMENT_FILE_PREFIX = "dataloader.pp.ftp.file.prefix";
	private final static String PROP_PP_SETTLEMENT_FILE_VERSION = "dataloader.pp.ftp.file.version";
	private final static String PROP_PP_SETTLEMENT_FILE_SUFFIX = "dataloader.pp.ftp.file.suffix";
	private final static String PROP_PP_SETTLEMENT_FILE_ALT_SUFFIX = "dataloader.pp.ftp.file.alt.suffix";
	private final static String PROP_PP_SETTLEMENT_FILE_EXTN = "dataloader.pp.ftp.file.extn";
	private final static String PROP_PP_SETTLEMENT_STL_EVENTCODES = "dataloader.pp.stl.eventcodes";
	private final static String PROP_PP_SETTLEMENT_STF_EVENTCODES = "dataloader.pp.stf.eventcodes";
	private final static String PROP_PP_SETTLEMENT_CBK_EVENTCODES = "dataloader.pp.cbk.eventcodes";
	private final static String PROP_PP_SETTLEMENT_CBR_EVENTCODES = "dataloader.pp.cbr.eventcodes";
	private final static String PROP_PP_SETTLEMENT_MISC_FEE_EVENTCODES = "dataloader.pp.cbp.eventcodes"; //charge back processing fee
	private final static String PROP_PP_SETTLEMENT_REF_EVENTCODES = "dataloader.pp.ref.eventcodes";
	private final static String PROP_PP_SETTLEMENT_IGNORABLE_EVENTCODES = "dataloader.pp.ignorable.eventcodes";
	private final static String PROP_PP_SETTLEMENT_FD_ACCOUNTID = "dataloader.pp.fd.accountid";
	private final static String PROP_PP_SETTLEMENT_FDW_ACCOUNTID = "dataloader.pp.fdw.accountid";
	private final static String PROP_PP_SFTP_DELETE_FILES="dataloader.paypal.sftp.deleteFiles";
	private final static String PROP_PP_SETTLEMENT_ENABLED = "dataloader.paypal.enabled";
	//PayPal settlement related properties - End
	
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

		//PayPal settlement related properties - Start
		defaults.put(PROP_PP_FTP_IP, "reports.sandbox.paypal.com");
		defaults.put(PROP_PP_FTP_USER, "sftpID_mrunal.doddanavar-facilit"); //TODO To be changed
		defaults.put(PROP_PP_FTP_PASSWORD, "Fresh@123");
		defaults.put(PROP_PP_SETTLEMENT_FOLDER, "/ppreports/outgoing/");
		defaults.put(PROP_PP_SETTLEMENT_FILE_VERSION, ".009");
		defaults.put(PROP_PP_SETTLEMENT_FILE_PREFIX, "STL-");
		defaults.put(PROP_PP_SETTLEMENT_FILE_SUFFIX, ".009");
		defaults.put(PROP_PP_SETTLEMENT_FILE_EXTN, ".CSV");
		defaults.put(PROP_PP_SFTP_DELETE_FILES, "false");		
		defaults.put(PROP_PP_SETTLEMENT_STL_EVENTCODES, "T0006, T0003");
		defaults.put(PROP_PP_SETTLEMENT_STF_EVENTCODES, "");
		defaults.put(PROP_PP_SETTLEMENT_CBK_EVENTCODES, "T1106, T1201");
		defaults.put(PROP_PP_SETTLEMENT_CBR_EVENTCODES, "T1202, T1205, T1207, T1208");
		defaults.put(PROP_PP_SETTLEMENT_MISC_FEE_EVENTCODES, "T0100, T0106, T0107, T1108");
		defaults.put(PROP_PP_SETTLEMENT_REF_EVENTCODES, "T1107");
		defaults.put(PROP_PP_SETTLEMENT_IGNORABLE_EVENTCODES, "T0400, T0300, T0301");
		defaults.put(PROP_PP_SETTLEMENT_FD_ACCOUNTID, "995LDYH3WGHZ6");
		defaults.put(PROP_PP_SETTLEMENT_FDW_ACCOUNTID, "9GBL2Z78NQM7L");
		defaults.put(PROP_PP_SETTLEMENT_ENABLED, "false");
		//PayPal settlement related properties - End
		
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
	public static boolean isPayPalSFTPFileDeletionEnabled() {
		return Boolean.valueOf(config.getProperty(PROP_PP_SFTP_DELETE_FILES)).booleanValue();
	}
	public static boolean isPayPalSettlementEnabled() {
		return Boolean.valueOf(config.getProperty(PROP_PP_SETTLEMENT_ENABLED)).booleanValue();
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
	
	public static String getPayPalFtpIp() {
		return config.getProperty(PROP_PP_FTP_IP);
	}


	public static String getPayPalFtpUser() {
		return config.getProperty(PROP_PP_FTP_USER);
	}

	public static String getPayPalFtpPassword() {
		return config.getProperty(PROP_PP_FTP_PASSWORD);
	}

	public static String getPayPalStlmntFolder() {
		return config.getProperty(PROP_PP_SETTLEMENT_FOLDER);
	}

	public static String getPayPalStlmntFilePrefix() {
		return config.getProperty(PROP_PP_SETTLEMENT_FILE_PREFIX);
	}

	public static String getPayPalStlmntFileSuffix() {
		return config.getProperty(PROP_PP_SETTLEMENT_FILE_SUFFIX);
	}
	
	public static String getPayPalStlmntFileAltSuffix() {
		return config.getProperty(PROP_PP_SETTLEMENT_FILE_ALT_SUFFIX);
	}
	
	public static String getPayPalStlmntFileExtn() {
		return config.getProperty(PROP_PP_SETTLEMENT_FILE_EXTN);
	}
	
	
	/*public static String getPPSTLEventCodes() {
		return config.getProperty(PROP_PP_SETTLEMENT_STL_EVENTCODES);
	}
	
	public static String getPPSTFEventCodes() {
		return config.getProperty(PROP_PP_SETTLEMENT_STF_EVENTCODES);
	}
	
	public static String getPPCBKEventCodes() {
		return config.getProperty(PROP_PP_SETTLEMENT_CBK_EVENTCODES);
	}
	
	public static String getPPCBREventCodes() {
		return config.getProperty(PROP_PP_SETTLEMENT_CBR_EVENTCODES);
	}
	
	public static String getPPMiscFeeEventCodes() {
		return config.getProperty(PROP_PP_SETTLEMENT_MISC_FEE_EVENTCODES);
	}
	
	public static String getPPREFEventCodes() {
		return config.getProperty(PROP_PP_SETTLEMENT_REF_EVENTCODES);
	}
	
	public static String getPPIgnorableEventCodes() {
		return config.getProperty(PROP_PP_SETTLEMENT_IGNORABLE_EVENTCODES);
	}*/
}
