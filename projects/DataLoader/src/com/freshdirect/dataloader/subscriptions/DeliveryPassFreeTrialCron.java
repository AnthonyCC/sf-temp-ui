package com.freshdirect.dataloader.subscriptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.configuration.RootConfiguration;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDPaymentInadequateException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.fdstore.deliverypass.DeliveryPassFreeTrialUtil;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.payments.util.PaymentMethodUtil;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.storeapi.configuration.StoreAPIConfig;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;

/**
 * @author Nikhil Subramanyam
 *
*/

public class DeliveryPassFreeTrialCron {

	private final static Category LOGGER = LoggerFactory.getInstance(DeliveryPassFreeTrialCron.class);

	private final static String CLASS_NAME=DeliveryPassFreeTrialCron.class.getSimpleName();

	public static void main(String[] args) {

		if(!FDStoreProperties.isDlvPassFreeTrialOptinFeatureEnabled()){
            LOGGER.info("DeliveryPassFreeTrialCron : " + "Free Trial Opt-in feature is not enabled.");
            return;
        }
		List<String> custIds = null;
		Context ctx = null;
		try {
			initializeSpringContext();
			ctx = getInitialContext();

			custIds = getAllCustIdsOfFreeTrialSubsOrder();
			if (custIds != null && custIds.size()>0) {
				String arSKU = FDStoreProperties.getTwoMonthTrailDPSku();//"MKT0072335";
				LOGGER.info(
						"DeliveryPassFreeTrialCron : " + custIds.size() + " customers eligible for FreeTrial.");
				for (String erpCustomerID : custIds ) {
					try {
						String orderId = DeliveryPassFreeTrialUtil.placeDpSubscriptionOrder(erpCustomerID, arSKU);
						System.out.println("order placed" +orderId);
					} catch (FDResourceException e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						email(erpCustomerID, sw.toString());
					} catch (Exception e) {
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						email(erpCustomerID, sw.toString());
					}
				}
			}

		} catch (NamingException e) {
			LOGGER.error("Error running DeliveryPassFreeTrialCron :", e);
			email("ALL", e.toString());
		} finally {
			//emailPendingPassReport();
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				LOGGER.error(ne);
			}
		}
	}
	
	private static synchronized Context getInitialContext() throws NamingException {

		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}

	private static void initializeSpringContext() {
	        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
	        rootContext.register(RootConfiguration.class);
	        rootContext.register(StoreAPIConfig.class);
	        rootContext.refresh();
	}

	public static List<String> getAllCustIdsOfFreeTrialSubsOrder() {
		List<String> custIds = null;
		try {
			custIds = FDCustomerManager.getAllCustIdsOfFreeTrialSubsOrder();
		}  catch (FDResourceException e) {
			LOGGER.warn(e);
		}
		return custIds;
	}

	public static void email(String customerID, String exceptionMsg) {

		try {
			Date now = DateUtil.truncate(new Date());
			String subject="Unable to create free-trial deliverypass order for customer id :	"+customerID;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			StringBuffer buff = new StringBuffer();
			String br = "\n";
			buff.append(subject).append(" on ").append(dateFormatter.format(now)).append(br);
			buff.append(br);
			buff.append("Exception is :").append(br);
			buff.append(exceptionMsg);

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
							subject, buff.toString());

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending free-trial deliveryPass exception email: ", e);
		}
	}
}
