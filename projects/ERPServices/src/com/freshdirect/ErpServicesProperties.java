/*
 * ErpServiceProperties.java
 *
 * Created on December 12, 2001, 6:34 PM
 */

package com.freshdirect;

/**
 *
 * @author  knadeem
 * @version
 */
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.Properties;
import java.util.Hashtable;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.MathUtil;

public class ErpServicesProperties {
	private static Category LOGGER = LoggerFactory.getInstance( ErpServicesProperties.class );


	private final static String PROP_PROVIDER_URL		= "erpservices.providerURL";
	private final static String PROP_INIT_CTX_FACTORY	= "erpservices.initialContextFactory";
    
    private final static String PROP_PRODUCTTREE_HOME   = "erpservices.productree.home";
    private final static String PROP_ATTRIBUTES_HOME    = "erpservices.attributes.home";
    private final static String PROP_NUTRITION_HOME     = "erpservices.nutrition.home";
    private final static String PROP_INFO_HOME          = "erpservices.info.home";
    private final static String PROP_BATCH_HOME         = "erpservices.batch.home";
	private final static String PROP_PRODUCT_HOME		= "erpservices.product.home";
    private final static String PROP_MATERIAL_HOME      = "erpservices.material.home";
    private final static String PROP_CLASS_HOME         = "erpservices.class.home";
    private final static String PROP_CVPRICE_HOME       = "erpservices.cvprice.home";
    private final static String PROP_COOL_MANAGER_HOME  = "erpservices.cool.home";
    
	private final static String PROP_HORIZON_DAYS		= "erpservices.horizon.days";
    
	private final static String PROP_AUTHORIZE			= "payment.paylinx.authorize";
	private final static String PROP_CYBERSOURCE_IP		= "payment.paylinx.ip";
	private final static String PROP_CYBERSOURCE_PORT	= "payment.paylinx.port";
	private final static String PROP_CYBERSOURCE_NAME	= "payment.paylinx.name";
	private final static String PROP_AVS_CHECK			= "payment.paylinx.avsCheck";
	private final static String PROP_AUTH_HOURS			= "payment.paylinx.authHours";
	private final static String PROP_CHASE_MERCHANT_ID  = "payment.paylinx.chase.merchantId";
	private final static String PROP_AVS_ADDRESS_CHECK  = "payment.paylinx.avs.address_check";
	private final static String PROP_CARD_VERIFICATION_AUTH_AMOUNT="payment.card.verification.amount";
	
	private final static String PROP_FRAUD_CHECK		= "fraud.check";
	private final static String PROP_FRAUD_CHECK_PM		= "fraud.check.paymentMethod";
	private final static String PROP_LOST_PASSWORD_PAGE	= "forgot.password.jsp";
	private final static String PROP_LOST_PASSWORD_PAGE_FDX	= "forgot.password.jsp.fdx";
	private final static String PROP_SEND_EMAIL			= "mailer.send.email";
	private final static String PROP_MAILER_HOST 		= "mail.smtp.host";
	private final static String PROP_MAILER_PROTOCOL		= "mail.transport.protocol";
	private final static String PROP_MAILER_FROM 		= "mail.from";
	private final static String PROP_FD_EMAIL 			= "mailer.fd.email";
	private final static String PROP_XSL_HOME 			= "mailer.xsl.home";
	private final static String PROP_KANA_RECEIVER		= "kana.receiver";
	private final static String PROP_KANA_HOST			= "kana.host";

	public final static String PROP_LOADER_FTP_HOST		= "loader.ftp.host";
	public final static String PROP_LOADER_FTP_USER		= "loader.ftp.user";
	public final static String PROP_LOADER_FTP_PASSWD	= "loader.ftp.passwd";
	public final static String PROP_LOADER_FTP_WORKDIR	= "loader.ftp.workdir";

	public final static String PROP_INVOICES_FTP_HOST 	= "invoices.ftp.host";
	public final static String PROP_INVOICES_FTP_USER	= "invoices.ftp.user";
	public final static String PROP_INVOICES_FTP_PASSWD 	= "invoices.ftp.passwd";
	public final static String PROP_INVOICES_FTP_WORKDIR	= "invoices.ftp.workdir";

	private final static String PROP_PHONE_DISPATCH = "phone.dispatch";
	
	
	private final static String PROP_CREDIT_AUTOAPPROVE_AMOUNT	= "credit.autoApprove.amount";
	private final static String PROP_CASE_SHORTSHIP_PERCENTAGE = "case.shortship.percentage";
	private final static String PROP_FEE_PHONE			= "phone.fee";
	private final static String PROP_FEE_DECLINED_CC		= "declinedCard.fee";
	private final static String PROP_FEE_RESTOCK_RATE	= "restocking.rate";
	private final static String PROP_CRM_BACKEND 		= "crm.backend";
	private final static String PROP_CRM_SYSTEM_USER_NAME 	= "crm.system.user.name";
	private final static String PROP_CRM_SYSTME_USER_PASSWORD = "crm.system.user.password";
	private final static String PROP_CRM_SYSTEM_DRIVER_USER_NAME 	= "crm.system.driver.user.name";
	private final static String PROP_CRM_SYSTME_DRIVER_USER_PASSWORD = "crm.system.driver.user.password";
	private final static String PROP_CRM_CREDIT_BUFFER = "crm.credit.buffer";

	private final static String PROP_CALLCENTER_SUPERVISOR_CODES	= "callcenter.supervisor.codes";
    
    private final static String PROP_JCO_CLIENT_LISTENHOST = "jco.client.listenHost";
    private final static String PROP_JCO_CLIENT_LISTENSERVER = "jco.client.listenServer";
    private final static String PROP_JCO_CLIENT_LISTENENABLED = "jco.client.listenersEnabled";
    
    private final static String PROP_SAP_MAIL_TO = "sap.mail.to";
    private final static String PROP_SAP_MAIL_CC = "sap.mail.cc";
    private final static String PROP_SAP_MAIL_FROM = "sap.mail.from";
    
    private final static String PROP_GC_MAIL_TO = "gc.mail.to";
    private final static String PROP_GC_MAIL_CC = "gc.mail.cc";
    private final static String PROP_GC_MAIL_FROM = "gc.mail.from";

    private final static String PROP_TRAN_MAIL_TO = "tran.mail.to";
    private final static String PROP_TRAN_MAIL_CC = "tran.mail.cc";
    private final static String PROP_TRAN_MAIL_FROM = "tran.mail.from";        
    
    
    private final static String PROP_SAP_SEND_CUTOFF_EMAIL = "sap.mail.cutoff";
    private final static String PROP_CANCEL_HRS_B4_CUTOFF  = "cancel.hours.b4.cutoff";
    private final static String PROP_WAIT_HRS_AFTER_CONFIRM  = "wait.hours.after.confirm";
    private final static String PROP_OCF_MAIL_TO = "ocf.mail.to";
    private final static String PROP_OCF_MAIL_CC = "ocf.mail.cc";
    private final static String PROP_OCF_MAIL_FROM = "ocf.mail.from";
    
    private final static String PROP_OCF_SEND_EMAIL = "ocf.mail.send";
    private final static String PROP_CAPTURE_CRON_QUEUE = "capture.cron.queue";    
    
    
    private final static String PROP_FEE_BOUNCED_CHECK = "payment.bouncedCheck.fee";
    private final static String PROP_PERISABLE_AUTH_BUFFER = "payment.perishable.auth.buffer";
    private final static String PROP_EVENT_QUEUE_SIZE="event.queue.size";
    
    private final static String PROP_IMPRESSIONS_COUNT_LIMIT = "impressions.count.limit";
    private final static String PROP_IMPRESSIONS_ENTRY_LIMIT = "impressions.entry.limit";
    private final static String PROP_IMPRESSIONS_FLUSH_SECONDS = "impressions.flush.seconds";
    
    private final static String PROP_CLICKTHROUGHS_COUNT_LIMIT = "clickthroughs.count.limit";
    private final static String PROP_CLICKTHROUGHS_ENTRY_LIMIT = "clickthroughs.entry.limit";
    private final static String PROP_CLICKTHROUGHS_FLUSH_SECONDS = "clickthroughs.flush.seconds";
    
    private final static String PROP_DEFAULT_ZONE_ID="zone.default.zoneId";
    
    private final static String PROP_MASTER_DEFAULT_ZONE_ID = "zone.master_default.zoneId";
    
	private final static Properties config;

	private final static String PROP_PROMOTION_RT_SIZE_LIMIT = "promotion.rt.size.limit";
	
	private final static String PROP_CART_ORDERLINE_LIMT = "cart.orderline.limit";

	private final static String PROP_SUBSCRIPTION_MAIL_TO="subscription.mail.to";

	private final static String PROP_SUBSCRIPTION_MAIL_CC="subscription.mail.cc";

	private final static String PROP_SUBSCRIPTION_MAIL_FROM = "subscription.mail.from";

	private final static String PROP_SUBSCRIPTION_SEND_EMAIL = "subscription.mail.send";
			
	private final static String PROP_FUNCTION_ROUTEINFO = "sap.function.routeinfo";
	
	private final static String PROP_FUNCTION_TRUCKINFO = "sap.function.truckinfo";
	
	private final static String PROP_FUNCTION_ZONEINFO = "sap.function.zoneinfo";
	
	private final static String PROP_FUNCTION_COOLINFO = "sap.function.coolinfo";
			
	private final static String PROP_FD_GIVEX_TOKEN  = "payment.givex.fd.tokenId";
	private final static String PROP_FD_GIVEX_USER  = "payment.givex.fd.user";
	private final static String PROP_FD_GIVEX_USER_PASSWD  = "payment.givex.fd.user.passwd";
	private final static String PROP_GIVEX_SERVER_URL  = "payment.givex.url";
	private final static String PROP_GIVEX_SERVER_SEC_URL  = "payment.givex.backup.url";
	private final static String PROP_GIVEX_TRAN_TIMEOUT  = "payment.givex.transaction.timeout";
	private final static String PROP_GIVEX_NUM_ENCRYPTION_KEY = "givex.num.encryption.key";
	private final static String PROP_GIFT_CARD_STRICT_ORDER_MAX = "giftcard.strict.order.max";
	
	private final static String PROP_GIFT_CARD_ORDER_COUNT = "giftcard.order.count";
	private final static String PROP_GIFT_CARD_ORDER_MAX = "giftcard.order.max";
	private final static String PROP_REGISTER_CRON_QUEUE = "register.cron.queue";
	private final static String PROP_PRE_AUTHORIZE	= "payment.giftcard.preauthorize";
	
	private final static String PROP_AVS_ERROR_ORDER_COUNT="payment.avs_error.order.count";
	private final static String PROP_SIGNUP_PROMO_DUPLICATE_ADDR_DELV_DAYS = "signup.promo.duplicate.addr.delivery.days";
	
	private final static String SO_TECH_RECIPIENT = "standingorders.technical.error.recipient.address";
	private final static String SO_ATPREPORT_RECIPIENT = "standingorders.atp.failurereport.recipient.address";
	private final static String SO_TECH_FROM = "standingorders.technical.error.from.address";
	
	private final static String MASQUERADE_STOREFRONT_BASEURL = "masquerade.storefront.baseurl";
	private final static String MASQUERADE_FDX_STOREFRONT_BASEURL = "masquerade.fdx.storefront.baseurl";
	private final static String MASQUERADE_SECURITYTICKET_EXPIRATION = "masquerade.securityticket.expiration";
	
	private final static String PROP_CRON_FAILURE_MAIL_TO = "cron.mail.to";
	private final static String PROP_CRON_FAILURE_MAIL_CC = "cron.mail.cc";
	private final static String PROP_CRON_FAILURE_MAIL_FROM = "cron.mail.from";
	    
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_FTP_WORKDIR	= "mktadmin.auto.uploader.ftp.workdir";
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_FTP_BACKUPDIR	= "mktadmin.auto.uploader.ftp.backupdir";
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_FTP_FAILEDDIR	= "mktadmin.auto.uploader.ftp.failedDir";
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_WORKDIR	= "mktadmin.auto.uploader.local.workdir";
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_BACKUPDIR	= "mktadmin.auto.uploader.local.backupdir";
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_FAILEDDIR	= "mktadmin.auto.uploader.local.failedDir";
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_FTP_HOST		= "mktadmin.auto.uploader.ftp.host";
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_FTP_USER		= "mktadmin.auto.uploader.ftp.user";
	public final static String PROP_MKTADMIN_AUTO_UPLOADER_FTP_PASSWD	= "mktadmin.auto.uploader.ftp.passwd";
	public final static String PROP_MKTADMIN_UPSOUTAGE_FROM_ADDRESS		= "mktAdmin.upsoutage.mail.from";
	public final static String PROP_MKTADMIN_UPSOUTAGE_SUBJECT		    = "mktAdmin.upsoutage.mail.subject";
	public final static String PROP_MKTADMIN_UPSOUTAGE_MESSAGE	        = "mktAdmin.upsoutage.mail.message";
	
	private final static String PROP_AIRCLIC_BLACKHOLE		= "airclic.blackhole";
	private final static String PROP_HANDOFF_ADDRESS_LINE2		= "send.handoff.addressline2";
	private final static String PROP_HANDOFF_TRAILER_INFO_ENABLED = "send.handoff.trailerinfo.enabled";

	private final static String PROP_DP_REPORT_MAIL_TO = "dpReport.mail.to";
	private final static String PROP_DP_REPORT_MAIL_CC = "dpReport.mail.cc";
	private final static String PROP_DP_REPORT_MAIL_FROM = "dpReport.mail.from";
	
	private final static String PROP_MATERIALBATCH_LOADERSTATUS_EXPIRY   = "erpservices.materialbatch.status.expiry"; // in minutes
	
	public final static String PROP_EWALLET_NOTIFY_EMAIL_FROM	= "ewallet.notify.email.from";
	public final static String PROP_EWALLET_NOTIFY_EMAIL_TO	= "ewallet.notify.email.to";
	public final static String PROP_EWALLET_NOTIFY_EMAIL_CC	= "ewallet.notify.email.cc";
	public final static String PROP_EWALLET_NOTIFY_EMAIL_ENABLED	= "ewallet.notify.email.send";
	public final static String PROP_EWALLET_POSTBACK_CHUNK_SIZE = "ewallet.notify.postback.chunk";
	public final static String PROP_EWALLET_POSTBACK_MAXDAYS = "ewallet.notify.postback.maxdays";
	private final static String PROP_PP_SETTLEMENT_STL_EVENTCODES = "dataloader.pp.stl.eventcodes";
	private final static String PROP_PP_SETTLEMENT_STF_EVENTCODES = "dataloader.pp.stf.eventcodes";
	private final static String PROP_PP_SETTLEMENT_CBK_EVENTCODES = "dataloader.pp.cbk.eventcodes";
	private final static String PROP_PP_SETTLEMENT_CBR_EVENTCODES = "dataloader.pp.cbr.eventcodes";
	private final static String PROP_PP_SETTLEMENT_MISC_FEE_EVENTCODES = "dataloader.pp.cbp.eventcodes"; //charge back processing fee
	private final static String PROP_PP_SETTLEMENT_REF_EVENTCODES = "dataloader.pp.ref.eventcodes";
	private final static String PROP_PP_SETTLEMENT_FD_ACCOUNTID = "dataloader.pp.fd.accountid";
	private final static String PROP_PP_SETTLEMENT_FDW_ACCOUNTID = "dataloader.pp.fdw.accountid";
	
	private final static String PROP_HOOK_lOGIC_ENABLE = "fdstore.hooklogic.enabled";
	
	public final static String PROP_HOOK_LOGIC_URL="fdstore.erp.hooklogic.url";
	public final static String PROP_HOOK_LOGIC_CONFIRMATION_URL="fdstore.erp.hooklogic.confirmation.url";
	
	
	public final static String PROP_HOOK_LOGIC_API_KEY="fdstore.Erp.hookloigc.apikey";
	private final static String PROP_HL_READ_TIMEOUT_PERIOD = "fdstore.HL.read.timeout.period";
	private final static String PROP_HL_CONNECTION_TIMEOUT_PERIOD = "fdstore.HL.connection.timeout.period";
	public final static String PROP_ORDER_PRODUCT_FEED_FILENAME="fdstore.Erp.order.feed.filename";
	public final static String PROP_HLCODE="fdstore.erp.hl.code";
	public final static String PROP_HL_CLIENT_ID="fdstore.erp.hl.clientid";
	
	public final static String PROP_HOOK_LOGIC_CULTURE="fdstore.erp.hl.culture";
	
	public final static String PROP_HOOK_LOGIC_IC="fdstore.erp.hl.ic";
	
	public final static String PROP_HOOK_LOGIC_PLATFORM="fdstore.erp.hl.platform";
	
	public final static String PROP_HOOK_LOGIC_MEDIASOURCE="fdstore.erp.hl.mediasource";
	
	public final static String PROP_HOOK_LOGIC_HLPT="fdstore.erp.hl.hlpt";
	
	public final static String PROP_HOOK_LOGIC_CONFIRMAITON_HLPT="fdstore.erp.hl.confirmaiton.hlpt";
	
	
	public final static String PROP_HOOK_LOGIC_STRATEGY="fdstore.erp.hl.strategy";
	
	static {
		Properties defaults = new Properties();

		defaults.put(PROP_PROVIDER_URL, 	"t3://127.0.0.1:7001");
		defaults.put(PROP_INIT_CTX_FACTORY,	"weblogic.jndi.WLInitialContextFactory");

        defaults.put(PROP_PRODUCTTREE_HOME,	"freshdirect.erp.ProductTree");
        defaults.put(PROP_ATTRIBUTES_HOME,	"freshdirect.content.AttributeFacade");
        defaults.put(PROP_NUTRITION_HOME,	"freshdirect.content.Nutrition");
        defaults.put(PROP_BATCH_HOME,       "freshdirect.erp.BatchManager");
        defaults.put(PROP_INFO_HOME,	    "freshdirect.erp.Info");
		defaults.put(PROP_PRODUCT_HOME,	    "freshdirect.erp.Product");
        defaults.put(PROP_MATERIAL_HOME,	"freshdirect.erp.Material");
        defaults.put(PROP_CLASS_HOME,	    "freshdirect.erp.Class");
        defaults.put(PROP_CVPRICE_HOME,	    "freshdirect.erp.CharacteristicValuePrice");
        defaults.put(PROP_COOL_MANAGER_HOME,"freshdirect.erp.COOLManager");
        
		defaults.put(PROP_HORIZON_DAYS, "7");
       
		defaults.put(PROP_AUTHORIZE, "true");
		defaults.put(PROP_CYBERSOURCE_IP, "10.53.5.11");
		defaults.put(PROP_CYBERSOURCE_PORT, "1530");
		defaults.put(PROP_CYBERSOURCE_NAME, "demo");
		defaults.put(PROP_AVS_CHECK, "false");
		defaults.put(PROP_AUTH_HOURS, "48");
		defaults.put(PROP_CHASE_MERCHANT_ID, "Chase");

		defaults.put(PROP_FRAUD_CHECK, "true");
		defaults.put(PROP_FRAUD_CHECK_PM, "true");

		defaults.put(PROP_LOST_PASSWORD_PAGE, "http://www.freshdirect.com/login/retrieve_password.jsp");
		defaults.put(PROP_LOST_PASSWORD_PAGE_FDX, "https://www.freshdirect.com/login/retrieve_password.jsp"); //this will be a different page

		defaults.put(PROP_SEND_EMAIL, "true");
		defaults.put(PROP_MAILER_HOST, "storesmtp.nyc2.freshdirect.com");
		defaults.put(PROP_MAILER_PROTOCOL, "smtp");
		defaults.put(PROP_MAILER_FROM, "orders@freshdirect.com");
		defaults.put(PROP_FD_EMAIL, "customerservice@freshdirect.com");
		defaults.put(PROP_XSL_HOME, "com/freshdirect/resource/email/xslt/");

		defaults.put(PROP_KANA_RECEIVER, "xmlgateway.asp");
		defaults.put(PROP_KANA_HOST, "kanatst.lic.corp.freshdirect.com");

		defaults.put(PROP_CREDIT_AUTOAPPROVE_AMOUNT, "14.0");
		defaults.put(PROP_CASE_SHORTSHIP_PERCENTAGE, "0.2");
		defaults.put(PROP_FEE_PHONE, "9.99");
		defaults.put(PROP_FEE_DECLINED_CC, "2.99");
		defaults.put(PROP_FEE_RESTOCK_RATE, ".25");
		defaults.put(PROP_CRM_CREDIT_BUFFER, ".25");

		defaults.put(PROP_PHONE_DISPATCH, "7189281555");

		defaults.put(PROP_CALLCENTER_SUPERVISOR_CODES, "b4g0n6,8psg4d3n,p8ntbl4k,b2bw1ld,dc0in4no");
		defaults.put(PROP_CRM_BACKEND, "kana");
		defaults.put(PROP_CRM_SYSTEM_USER_NAME, "system");
		defaults.put(PROP_CRM_SYSTME_USER_PASSWORD, "system");
		defaults.put(PROP_CRM_SYSTEM_DRIVER_USER_NAME, "systemdriver");
		defaults.put(PROP_CRM_SYSTME_DRIVER_USER_PASSWORD, "systemdriver");
		
		defaults.put(PROP_JCO_CLIENT_LISTENENABLED, "false");
		
		defaults.put(PROP_SAP_MAIL_TO, "erp@freshdirect.com");
		defaults.put(PROP_SAP_MAIL_CC, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_SAP_MAIL_FROM, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_SAP_SEND_CUTOFF_EMAIL, "true");
		defaults.put(PROP_CANCEL_HRS_B4_CUTOFF, "1");
		defaults.put(PROP_WAIT_HRS_AFTER_CONFIRM, "4");
		
		defaults.put(PROP_OCF_MAIL_TO, "appsupport@freshdirect.com");
		defaults.put(PROP_OCF_MAIL_CC, "");
		defaults.put(PROP_OCF_MAIL_FROM, "applicationdevelopment@freshdirect.com");
		
		defaults.put(PROP_CRON_FAILURE_MAIL_TO, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_CRON_FAILURE_MAIL_CC, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_CRON_FAILURE_MAIL_FROM, "applicationdevelopment@freshdirect.com");		
		
		defaults.put(PROP_GC_MAIL_TO, "appsupport@freshdirect.com");
		defaults.put(PROP_GC_MAIL_CC, "");
		defaults.put(PROP_GC_MAIL_FROM, "applicationdevelopment@freshdirect.com");
		
		defaults.put(PROP_TRAN_MAIL_TO, "appsupport@freshdirect.com");
		defaults.put(PROP_TRAN_MAIL_CC, "");
		defaults.put(PROP_TRAN_MAIL_FROM, "applicationdevelopment@freshdirect.com");
		
		defaults.put(PROP_OCF_SEND_EMAIL, "true");
		defaults.put(PROP_CAPTURE_CRON_QUEUE, "false");
		
		defaults.put(PROP_FEE_BOUNCED_CHECK, "25.00");
		defaults.put(PROP_PERISABLE_AUTH_BUFFER, ".25"); //25%
		defaults.put(PROP_EVENT_QUEUE_SIZE, "1500"); 
		
		defaults.put(PROP_IMPRESSIONS_COUNT_LIMIT, "" + Integer.MAX_VALUE);
		defaults.put(PROP_IMPRESSIONS_ENTRY_LIMIT, "7000");
		defaults.put(PROP_IMPRESSIONS_FLUSH_SECONDS, "300");
		
		defaults.put(PROP_CLICKTHROUGHS_COUNT_LIMIT, "" + Integer.MAX_VALUE);
		defaults.put(PROP_CLICKTHROUGHS_ENTRY_LIMIT, "7000");
		defaults.put(PROP_CLICKTHROUGHS_FLUSH_SECONDS, "300");
		
		defaults.put(PROP_PROMOTION_RT_SIZE_LIMIT, "2000");
		
		defaults.put(PROP_CART_ORDERLINE_LIMT, "500");
		
		defaults.put(PROP_SUBSCRIPTION_MAIL_TO, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_SUBSCRIPTION_MAIL_CC, "");
		defaults.put(PROP_SUBSCRIPTION_MAIL_FROM, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_SUBSCRIPTION_SEND_EMAIL, "true");
				
		defaults.put(PROP_FUNCTION_ROUTEINFO, "ZBAPI_ROUTE_INFO");
		defaults.put(PROP_FUNCTION_TRUCKINFO, "ZBAPI_TRUCK_INFO");
		
		
		defaults.put(PROP_FUNCTION_COOLINFO, "ZSDI_COUNTRY_ORIGIN");
		
		defaults.put(PROP_FD_GIVEX_TOKEN, "535253d3a36d10810d1471de42f3c3a5");
		defaults.put(PROP_FD_GIVEX_USER, "10947");
		defaults.put(PROP_FD_GIVEX_USER_PASSWD, "7368");
		defaults.put(PROP_GIVEX_SERVER_URL, "https://dev-gapi.givex.com:50081/1.0/trans/");
		defaults.put(PROP_GIVEX_SERVER_SEC_URL, "https://149.99.39.146:50081/1.0/trans/");
		//defaults.put(PROP_GIVEX_SERVER_URL, "https://www.freshdirect.com/login/login.jsp");
		//defaults.put(PROP_GIVEX_SERVER_SEC_URL, "https://www.freshdirect.com/");		
		defaults.put(PROP_GIVEX_TRAN_TIMEOUT, "15");
		defaults.put(PROP_GIVEX_NUM_ENCRYPTION_KEY, "5f4dcc3b5aa765d61d8327deb882cf99");
		defaults.put(PROP_GIFT_CARD_STRICT_ORDER_MAX, "5000.00");
		defaults.put(PROP_GIFT_CARD_ORDER_MAX, "750.00");
		defaults.put(PROP_GIFT_CARD_ORDER_COUNT, "3");
		defaults.put(PROP_REGISTER_CRON_QUEUE, "false");
		defaults.put(PROP_PRE_AUTHORIZE, "true");

		defaults.put(PROP_DEFAULT_ZONE_ID, "0000100000,0000100001,0000100002");
		defaults.put(PROP_MASTER_DEFAULT_ZONE_ID, "0000100000");
		
		
		defaults.put(PROP_AVS_ERROR_ORDER_COUNT, "5");
		defaults.put(PROP_SIGNUP_PROMO_DUPLICATE_ADDR_DELV_DAYS, "180");
		defaults.put(PROP_AVS_ADDRESS_CHECK, "false");
		
		defaults.put( SO_TECH_RECIPIENT, "SOIssues@freshdirect.com" );
		defaults.put( SO_ATPREPORT_RECIPIENT, "SO-ATPWouldbefailures@freshdirect.com" );
		
		defaults.put( SO_TECH_FROM, "no-reply@freshdirect.com" );
		
		defaults.put( MASQUERADE_STOREFRONT_BASEURL, "https://www.freshdirect.com/" );
		defaults.put( MASQUERADE_FDX_STOREFRONT_BASEURL, "https://foodkick.freshdirect.com/" );
		defaults.put( MASQUERADE_SECURITYTICKET_EXPIRATION, "5" );
		defaults.put(PROP_CARD_VERIFICATION_AUTH_AMOUNT,"1");
		
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_FTP_WORKDIR, "");
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_FTP_BACKUPDIR, "");
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_FTP_FAILEDDIR, "");
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_WORKDIR, "");
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_BACKUPDIR, "");
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_LOCAL_FAILEDDIR, "");
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_FTP_HOST, "");
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_FTP_USER, "");
		defaults.put(PROP_MKTADMIN_AUTO_UPLOADER_FTP_PASSWD, "");
		defaults.put(PROP_MKTADMIN_UPSOUTAGE_FROM_ADDRESS, "");
		defaults.put(PROP_MKTADMIN_UPSOUTAGE_SUBJECT, "");
		defaults.put(PROP_MKTADMIN_UPSOUTAGE_MESSAGE, "");
		defaults.put(PROP_AIRCLIC_BLACKHOLE, "false");
		defaults.put(PROP_HANDOFF_ADDRESS_LINE2, "true");
		defaults.put(PROP_HANDOFF_TRAILER_INFO_ENABLED, "true");
		
		defaults.put(PROP_DP_REPORT_MAIL_TO, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_DP_REPORT_MAIL_CC, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_DP_REPORT_MAIL_FROM, "applicationdevelopment@freshdirect.com");
				
		defaults.put(PROP_MATERIALBATCH_LOADERSTATUS_EXPIRY, "30");
		
		defaults.put(PROP_EWALLET_NOTIFY_EMAIL_TO, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_EWALLET_NOTIFY_EMAIL_CC, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_EWALLET_NOTIFY_EMAIL_FROM, "applicationdevelopment@freshdirect.com");
		defaults.put(PROP_EWALLET_NOTIFY_EMAIL_ENABLED, "true");
		defaults.put(PROP_EWALLET_POSTBACK_CHUNK_SIZE, "0");
		defaults.put(PROP_EWALLET_POSTBACK_MAXDAYS, "7");
		defaults.put(PROP_GIVEX_SERVER_SEC_URL, "https://149.99.39.146:50081/1.0/trans/");
		defaults.put(PROP_HOOK_LOGIC_API_KEY, "ba0f338d-f678-4fc3-a81b-d24d5ac4ffd1");//Test API Key
		defaults.put(PROP_HOOK_LOGIC_URL, "http://uat1.hlserve.com/delivery/api/search?");
		defaults.put(PROP_HOOK_LOGIC_CONFIRMATION_URL, "http://uat1.hlserve.com/delivery/api/confirmation?");
		defaults.put(PROP_ORDER_PRODUCT_FEED_FILENAME, "freshdirect_daily_orders_");
		defaults.put(PROP_HLCODE, "HOOKLOGICSFTP");
		defaults.put(PROP_HL_CLIENT_ID, "258");
		defaults.put(PROP_HOOK_LOGIC_CULTURE, "en-US");
		defaults.put(PROP_HOOK_LOGIC_IC, "core");
		defaults.put(PROP_HOOK_LOGIC_PLATFORM, "web");
		defaults.put(PROP_HOOK_LOGIC_MEDIASOURCE, "");
		defaults.put(PROP_HOOK_LOGIC_HLPT, "S");
		defaults.put(PROP_HOOK_LOGIC_CONFIRMAITON_HLPT, "C");
		defaults.put(PROP_HOOK_LOGIC_STRATEGY, "inmarket");
		
		
		
		
		defaults.put(PROP_PP_SETTLEMENT_STL_EVENTCODES, "T0006, T0003");
		defaults.put(PROP_PP_SETTLEMENT_STF_EVENTCODES, "");
		defaults.put(PROP_PP_SETTLEMENT_CBK_EVENTCODES, "T1106, T1201");
		defaults.put(PROP_PP_SETTLEMENT_CBR_EVENTCODES, "T1202, T1205, T1207, T1208");
		defaults.put(PROP_PP_SETTLEMENT_MISC_FEE_EVENTCODES, "T0100, T0106, T0107, T1108");
		defaults.put(PROP_PP_SETTLEMENT_REF_EVENTCODES, "T1107");
		defaults.put(PROP_PP_SETTLEMENT_FD_ACCOUNTID, "995LDYH3WGHZ6");
		defaults.put(PROP_PP_SETTLEMENT_FDW_ACCOUNTID, "9GBL2Z78NQM7L");
		
		defaults.put(PROP_HOOK_lOGIC_ENABLE, "false");
		defaults.put(PROP_HL_READ_TIMEOUT_PERIOD, "10");//secs
		defaults.put(PROP_HL_CONNECTION_TIMEOUT_PERIOD, "10");//secs
		
		config = ConfigHelper.getPropertiesFromClassLoader("erpservices.properties", defaults);
		LOGGER.info("Loaded configuration: "+config);
	}

	/** Creates new ErpServiceProperties */
    public ErpServicesProperties() {
    }

	/**
	 *  A method to set a specific property.
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  Use with care - it's here for testing purposes only.
	 *  
	 *  @param key the name of the property to set.
	 *  @param value the new value of the property.
	 */
	public static void set(String key, String value) {
		config.setProperty(key, value);
	}

	public static String getProviderURL() {
		return config.getProperty(PROP_PROVIDER_URL);
	}

	public static String getInitialContextFactory() {
		return config.getProperty(PROP_INIT_CTX_FACTORY);
	}

    public static String getProductTreeHome() {
		return config.getProperty(PROP_PRODUCTTREE_HOME);
	}
    
    public static String getBatchHome() {
		return config.getProperty(PROP_BATCH_HOME);
	}
    
    public static String getAttributesHome() {
		return config.getProperty(PROP_ATTRIBUTES_HOME);
	}
    
    public static String getNutritionHome() {
		return config.getProperty(PROP_NUTRITION_HOME);
	}
    
    public static String getInfoHome() {
		return config.getProperty(PROP_INFO_HOME);
	}
    
	public static String getProductHome() {
		return config.getProperty(PROP_PRODUCT_HOME);
	}
    
    public static String getMaterialHome() {
		return config.getProperty(PROP_MATERIAL_HOME);
	}
    
    public static String getCharacteristicValuePriceHome() {
		return config.getProperty(PROP_CVPRICE_HOME);
	}
    
    public static String getClassHome() {
		return config.getProperty(PROP_CLASS_HOME);
	}

	public static int getHorizonDays() {
		return Integer.parseInt(config.getProperty(PROP_HORIZON_DAYS));	
	}

	public static String getAuthorize() {
		return config.getProperty(PROP_AUTHORIZE);
	}

	public static String getCybersourceIp() {
		return config.getProperty(PROP_CYBERSOURCE_IP);
	}

	public static String getCybersourcePort() {
		return config.getProperty(PROP_CYBERSOURCE_PORT);
	}

	public static String getCybersourceName() {
		return config.getProperty(PROP_CYBERSOURCE_NAME);
	}
	
	public static String getChaseMerchantId() {
		return config.getProperty(PROP_CHASE_MERCHANT_ID);
	}

	public static String getAvsCheck(){
		return config.getProperty(PROP_AVS_CHECK);
	}

	public static boolean isAvsAddressMatchReqd(){		
		return Boolean.valueOf(config.getProperty(PROP_AVS_ADDRESS_CHECK)).booleanValue();
	}	

	public static String getAuthHours(){
		return config.getProperty(PROP_AUTH_HOURS);
	}

	public static String getCheckForFraud() {
		return config.getProperty(PROP_FRAUD_CHECK);
	}

	public static String getCheckForPaymentMethodFraud() {
		return config.getProperty(PROP_FRAUD_CHECK_PM);
	}

	public static String getForgotPasswordPage() {
		return config.getProperty(PROP_LOST_PASSWORD_PAGE);
	}
	
	public static String getForgotPasswordPageFDX() {
		return config.getProperty(PROP_LOST_PASSWORD_PAGE_FDX);
	}

	public static String getSendEmail() {
		return config.getProperty(PROP_SEND_EMAIL);
	}

	public static String getFDEmailAddress() {
		return config.getProperty(PROP_FD_EMAIL);
	}

	public static String getMailerXslHome() {
		return config.getProperty(PROP_XSL_HOME);
	}

	public static String getMailerHost() {
		return config.getProperty(PROP_MAILER_HOST);
	}

	public static String getMailerProtocol() {
		return config.getProperty(PROP_MAILER_PROTOCOL);
	}

	public static String getMailerFromAddress() {
		return config.getProperty(PROP_MAILER_FROM);
	}

	public static String getKanaReceiver() {
		return config.getProperty(PROP_KANA_RECEIVER);
	}

	public static String getKanaHost() {
		return config.getProperty(PROP_KANA_HOST);
	}

	public static double getCreditAutoApproveAmount() {
		return Double.parseDouble(config.getProperty(PROP_CREDIT_AUTOAPPROVE_AMOUNT));	
	}

	public static double getCaseShortshipPercentage() {
		return Double.parseDouble(config.getProperty(PROP_CASE_SHORTSHIP_PERCENTAGE));	
	}

	public static double getPhoneHandlingFee() {
		return Double.parseDouble(config.getProperty(PROP_FEE_PHONE));
	}

	public static String getDeclinedCreditCardFee() {
		return config.getProperty(PROP_FEE_DECLINED_CC);
	}

	public static String getRestockingRate() {
		return config.getProperty(PROP_FEE_RESTOCK_RATE);
	}
	
	public static double getCreditBuffer() {
		return MathUtil.roundDecimal(Double.parseDouble(config.getProperty(PROP_CRM_CREDIT_BUFFER)));
	}

	public static String getCallCenterSupervisorCodes() {
		return config.getProperty(PROP_CALLCENTER_SUPERVISOR_CODES);
	}

	public static String getProperty(String name) {
		return config.getProperty(name);
	}

	public static PhoneNumber getPhoneDispatch() {
		return new PhoneNumber(config.getProperty(PROP_PHONE_DISPATCH));
	}
	
	public static String getCrmBackend(){
		return config.getProperty(PROP_CRM_BACKEND);
	}
	
	public static String getCrmSystemUserName(){
		return config.getProperty(PROP_CRM_SYSTEM_USER_NAME);
	}
	
	public static String getCrmSystemUserPassword(){
		return config.getProperty(PROP_CRM_SYSTME_USER_PASSWORD);
	}
	
	public static String getJcoClientListenHost() {
		return config.getProperty(PROP_JCO_CLIENT_LISTENHOST);
	}
	
	public static String getJcoClientListenServer() {
		return config.getProperty(PROP_JCO_CLIENT_LISTENSERVER);
	}
	
	public static boolean getJcoClientListenersEnabled() {
		return Boolean.valueOf(config.getProperty(PROP_JCO_CLIENT_LISTENENABLED)).booleanValue();
	}
	
	public static String getSapMailTo() {
		return config.getProperty(PROP_SAP_MAIL_TO);
	}
	
	public static String getSapMailCC() {
		return config.getProperty(PROP_SAP_MAIL_CC);
	}
	
	public static String getSapMailFrom() {
		return config.getProperty(PROP_CRON_FAILURE_MAIL_FROM);
	}
	
	public static String getCronFailureMailTo() {
		return config.getProperty(PROP_CRON_FAILURE_MAIL_TO);
	}
	
	public static String getCronFailureMailCC() {
		return config.getProperty(PROP_CRON_FAILURE_MAIL_CC);
	}
	
	public static String getCronFailureMailFrom() {
		return config.getProperty(PROP_SAP_MAIL_FROM);
	}
	
	public static String getOcfMailTo() {
		return config.getProperty(PROP_OCF_MAIL_TO);
	}
	
	public static String getOCfMailCC() {
		return config.getProperty(PROP_OCF_MAIL_CC);
	}
	
	public static String getOcfMailFrom() {
		return config.getProperty(PROP_OCF_MAIL_FROM);
	}

	public static String getGCMailTo() {
		return config.getProperty(PROP_GC_MAIL_TO);
	}
	
	public static String getGCMailCC() {
		return config.getProperty(PROP_GC_MAIL_CC);
	}
	
	public static String getGCMailFrom() {
		return config.getProperty(PROP_GC_MAIL_FROM);
	}
	

	public static String getTranEmailFrom() {
		return config.getProperty(PROP_TRAN_MAIL_FROM);
	}
	
	
	public static String getTranMailTo() {
		return config.getProperty(PROP_TRAN_MAIL_TO);
	}
	
	
	public static String getTranMailCC() {
		return config.getProperty(PROP_TRAN_MAIL_CC);
	}
	
	
	public static boolean isSendOcfEmail(){
		return Boolean.valueOf(config.getProperty(PROP_OCF_SEND_EMAIL)).booleanValue(); 
	}

	public static boolean isUseQueue(){
		return Boolean.valueOf(config.getProperty(PROP_CAPTURE_CRON_QUEUE)).booleanValue(); 
	}

	public static boolean isUseRegisterQueue(){
		return Boolean.valueOf(config.getProperty(PROP_REGISTER_CRON_QUEUE)).booleanValue(); 
	}
	
	public static String getSendCutoffEmail(){
		return config.getProperty(PROP_SAP_SEND_CUTOFF_EMAIL);
	}
	
	public static int getCancelOrdersB4Cutoff(){
		return Integer.parseInt(config.getProperty(PROP_CANCEL_HRS_B4_CUTOFF));
	}

	public static int getWaitingTimeAfterConfirm(){
		return Integer.parseInt(config.getProperty(PROP_WAIT_HRS_AFTER_CONFIRM));
	}

	public static String getBouncedCheckFee() {
		return config.getProperty(PROP_FEE_BOUNCED_CHECK);
	}
	
	public static double getPerishableAuthBuffer() {
		return Double.parseDouble(config.getProperty(PROP_PERISABLE_AUTH_BUFFER));
	}
	
	public static int getEventQueueSize() {
		return Integer.parseInt(config.getProperty(PROP_EVENT_QUEUE_SIZE));
	}
	
	public static int getImpressionsCountLimit() {
		return Integer.parseInt(config.getProperty(PROP_IMPRESSIONS_COUNT_LIMIT));
	}
	
	public static int getImpressionsEntryLimit() {
		return Integer.parseInt(config.getProperty(PROP_IMPRESSIONS_ENTRY_LIMIT));
	}
	
	public static int getImpressionsFlushSeconds() {
		return Integer.parseInt(config.getProperty(PROP_IMPRESSIONS_FLUSH_SECONDS));
	}
	
	public static int getClickThroughsCountLimit() {
		return Integer.parseInt(config.getProperty(PROP_CLICKTHROUGHS_COUNT_LIMIT));
	}
	
	public static int getClickThroughsEntryLimit() {
		return Integer.parseInt(config.getProperty(PROP_CLICKTHROUGHS_ENTRY_LIMIT));
	}
	
	public static int getClickThroughsFlushSeconds() {
		return Integer.parseInt(config.getProperty(PROP_CLICKTHROUGHS_FLUSH_SECONDS));
	}
	
	
	public static int getPromotionRTSizeLimit() {
		return Integer.parseInt(config.getProperty(PROP_PROMOTION_RT_SIZE_LIMIT));
	}

	public static Context getInitialContext() throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		env.put(Context.PROVIDER_URL, getProviderURL() );
		env.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory() );
		return new InitialContext(env);
	}
	
	public static int getCartOrderLineLimit() {
		return Integer.parseInt(config.getProperty(PROP_CART_ORDERLINE_LIMT));
	}
	public static String getSubscriptionMailTo() {
		return config.getProperty(PROP_SUBSCRIPTION_MAIL_TO);
	}
	
	public static String getSubscriptionMailCC() {
		return config.getProperty(PROP_SUBSCRIPTION_MAIL_CC);
	}
	
	public static String getSubscriptionMailFrom() {
		return config.getProperty(PROP_SUBSCRIPTION_MAIL_FROM);
	}
	
	public static boolean isSendSubscriptionEmail(){
		return Boolean.valueOf(config.getProperty(PROP_SUBSCRIPTION_SEND_EMAIL)).booleanValue(); 
	}
			
	public static String getRouteInfoFunctionName() {
		return config.getProperty(PROP_FUNCTION_ROUTEINFO);
	}
	
	public static String getTruckInfoFunctionName() {
		return config.getProperty(PROP_FUNCTION_TRUCKINFO);
	}
	
	
	public static String getZoneInfoFunctionName() {
		return config.getProperty(PROP_FUNCTION_ZONEINFO);
	}

	
	public static String getCOOLManagerHome() {
		return config.getProperty(PROP_COOL_MANAGER_HOME);
	}
	
	public static String getCOOLInfoFunctionName() {
		return config.getProperty(PROP_FUNCTION_COOLINFO);
	}
	
	
	public static String getFDGivexToken() {
		return config.getProperty(PROP_FD_GIVEX_TOKEN);
	}
	
	public static String getFDGivexUser() {
		return config.getProperty(PROP_FD_GIVEX_USER);
	}
	
	public static String getFDGivexUserPassword() {
		return config.getProperty(PROP_FD_GIVEX_USER_PASSWD);
	}

	
	public static String getGivexServerURL() {
		return config.getProperty(PROP_GIVEX_SERVER_URL);
	}

	public static String getGivexServerSecondaryURL() {
		return config.getProperty(PROP_GIVEX_SERVER_SEC_URL);
	}

	public static int getGivexTransactionTimeOut() {
		return Integer.parseInt(config.getProperty(PROP_GIVEX_TRAN_TIMEOUT));
	}
	
	public static String getGivexNumEncryptionKey(){
		return config.getProperty(PROP_GIVEX_NUM_ENCRYPTION_KEY);
	}
	
	public static double getGiftCardStrictOrderLimit() {
		return Double.parseDouble(config.getProperty(PROP_GIFT_CARD_STRICT_ORDER_MAX));
	}	
	
	public static double getGiftCardOrderLimit() {
		return Double.parseDouble(config.getProperty(PROP_GIFT_CARD_ORDER_MAX));
	}
	
	public static int getGiftCardOrderCountLimit() {
		return Integer.parseInt(config.getProperty(PROP_GIFT_CARD_ORDER_COUNT));
	}
	
	public static String getPreAuthorize() {
		return config.getProperty(PROP_PRE_AUTHORIZE);
	}
		
	public static int getAvsErrorOrderCountLimit() {
		return Integer.parseInt(config.getProperty(PROP_AVS_ERROR_ORDER_COUNT));
	}
	
	public static String getDefaultSAPZoneId() {
		return config.getProperty(PROP_DEFAULT_ZONE_ID);
	}
	
	
	public static String getMasterDefaultZoneId(){
		return config.getProperty(PROP_MASTER_DEFAULT_ZONE_ID);
	}
	
	public static int getSignupPromoDeliveryDaysLimit(){
		return Integer.parseInt(config.getProperty(PROP_SIGNUP_PROMO_DUPLICATE_ADDR_DELV_DAYS));		
		
	}
	
	public static String getStandingOrdersTechnicalErrorRecipientAddress() {
		return config.getProperty(SO_TECH_RECIPIENT);
	}
	
	public static String getStandingOrdersATPFailureReportRecipientAddress() {
		return config.getProperty(SO_ATPREPORT_RECIPIENT);
	}
	
	public static String getStandingOrdersTechnicalErrorFromAddress() {
		return config.getProperty(SO_TECH_FROM);
	}
	
	public static String getMasqueradeStoreFrontBaseUrl() {
		return config.getProperty(MASQUERADE_STOREFRONT_BASEURL);
	}
	
	public static String getMasqueradeFDXStoreFrontBaseUrl() {
		return config.getProperty(MASQUERADE_FDX_STOREFRONT_BASEURL);
	}
	
	public static int getMasqueradeSecurityTicketExpiration() {
		return Integer.parseInt( config.getProperty(MASQUERADE_SECURITYTICKET_EXPIRATION) );
	}
	
	public static double getCardVerificationAuthAmount() {
		return Double.parseDouble( config.getProperty(PROP_CARD_VERIFICATION_AUTH_AMOUNT) );
	}
	
	public static String getMktAdmUpsOutageFromAddress() {
		return config.getProperty(PROP_MKTADMIN_UPSOUTAGE_FROM_ADDRESS);
	}
	
	public static String getMktAdmUpsOutageSubject() {
		return config.getProperty(PROP_MKTADMIN_UPSOUTAGE_SUBJECT);
	}
	
	public static String getMktAdmUpsOutageMessage() {
		return config.getProperty(PROP_MKTADMIN_UPSOUTAGE_MESSAGE);
	}
	public static boolean isAirclicBlackhole() {
        return Boolean.valueOf(config.getProperty(PROP_AIRCLIC_BLACKHOLE)).booleanValue();
    }
	public static boolean isSendAddressLine2() {
        return Boolean.valueOf(config.getProperty(PROP_HANDOFF_ADDRESS_LINE2)).booleanValue();
    }
	public static boolean isSendTrailerInfo() {
        return Boolean.valueOf(config.getProperty(PROP_HANDOFF_TRAILER_INFO_ENABLED)).booleanValue();
    }
	public static String getCrmSystemDriverUserName(){
		return config.getProperty(PROP_CRM_SYSTEM_DRIVER_USER_NAME);
	}
	
	public static String getCrmSystemDriverUserPassword(){
		return config.getProperty(PROP_CRM_SYSTME_DRIVER_USER_PASSWORD);
	}
	public static String getDPReportMailFrom() {
		return config.getProperty(PROP_DP_REPORT_MAIL_FROM);
	}
	
	public static String getDPReportMailTo() {
		return config.getProperty(PROP_DP_REPORT_MAIL_TO);
	}
	
	public static String getDPReportMailCC() {
		return config.getProperty(PROP_DP_REPORT_MAIL_CC);
	}
	
	public static int getMaterialBatchLoaderStatusExpiry(){
		return Integer.parseInt(config.getProperty(PROP_MATERIALBATCH_LOADERSTATUS_EXPIRY));
	}
	
	public static String geteWalletNotifyFrom() {
		return config.getProperty(PROP_EWALLET_NOTIFY_EMAIL_FROM);
	}
	
	public static String geteWalletNotifyTo() {
		return config.getProperty(PROP_EWALLET_NOTIFY_EMAIL_TO);
	}
	
	
	public static String geteWalletNotifyCC() {
		return config.getProperty(PROP_EWALLET_NOTIFY_EMAIL_CC);
	}
	
	public static String geteWalletNotifyEnabled() {
		return config.getProperty(PROP_EWALLET_NOTIFY_EMAIL_ENABLED);
	}
	
	public static int geteWalletPostbackChunkSize() {
		int result = 0;
		try {
			result = Integer.parseInt(config.getProperty(PROP_EWALLET_POSTBACK_CHUNK_SIZE));
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}
	
	public static int geteWalletPostbackMaxDays() {
		int result = 0;
		try {
			result = Integer.parseInt(config.getProperty(PROP_EWALLET_POSTBACK_MAXDAYS));
		} catch (Exception e) {
			result = 7;
		}
		return result;
	}
	
	public static String getPPFDAccountIds() {
		return config.getProperty(PROP_PP_SETTLEMENT_FD_ACCOUNTID);
	}
	
	public static String getPPFDWAccountIds() {
		return config.getProperty(PROP_PP_SETTLEMENT_FDW_ACCOUNTID);
	}
	
	public static String getPPSTLEventCodes() {
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
	
	public static String getPPREFEventCodes() {
		return config.getProperty(PROP_PP_SETTLEMENT_REF_EVENTCODES);
	}


	public static String getHLBrandProductAdvertiseURL() {
		return config.getProperty(PROP_HOOK_LOGIC_URL);
	}
	
	public static String getHLBrandProductAdvertiseConfirmationURL() {
		return config.getProperty(PROP_HOOK_LOGIC_CONFIRMATION_URL);
	}
	
	public static String getHLBrandProductAdvertiseAPIKey() {
		return config.getProperty(PROP_HOOK_LOGIC_API_KEY);
	}
	
	
	public static   String getBrandProductAdProviderPlatform(){
		return config.getProperty(PROP_HOOK_LOGIC_PLATFORM);
	}
	
	public static   String getBrandProductAdProviderCulture() {
		return config.getProperty(PROP_HOOK_LOGIC_CULTURE);
	}
	public static   String getBrandProductAdProviderIc(){
		return config.getProperty(PROP_HOOK_LOGIC_IC);
	}
	
	public static   String getBrandProductAdProviderMediaSource() {
		return config.getProperty(PROP_HOOK_LOGIC_MEDIASOURCE);
	}
	
	public static   String getBrandProductAdProviderHlpt(){
		return config.getProperty(PROP_HOOK_LOGIC_HLPT);
	}
	
	public static   String getBrandProductAdProviderConformationHlpt(){
		return config.getProperty(PROP_HOOK_LOGIC_CONFIRMAITON_HLPT);
	}
	
	
	
	public static   String getBrandProductAdProviderStrategy() {
		return config.getProperty(PROP_HOOK_LOGIC_STRATEGY);	
	}
	
	
	public static boolean isHookLogicEnabled() {
    	return (Boolean.valueOf(config.getProperty(PROP_HOOK_lOGIC_ENABLE))).booleanValue();
    }
	
	public static Integer getHLReadTimeoutPeriod() {
		try {
			return Integer.parseInt(config.getProperty(PROP_HL_READ_TIMEOUT_PERIOD));
		} catch (NumberFormatException e) {
			return 10;
		}
	}
	
	public static Integer getHLConnectionTimeoutPeriod() {
		try {
			return Integer.parseInt(config.getProperty(PROP_HL_CONNECTION_TIMEOUT_PERIOD));
		} catch (NumberFormatException e) {
			return 10;
		}
	}
	
	public static String getOrderProductFeedFileName() {
		return config.getProperty(PROP_ORDER_PRODUCT_FEED_FILENAME);
	}
	
	public static String getHLCode() {
		return config.getProperty(PROP_HLCODE);
	}
	public static String getHLClientId() {
		return config.getProperty(PROP_HL_CLIENT_ID);
	}
	
}
