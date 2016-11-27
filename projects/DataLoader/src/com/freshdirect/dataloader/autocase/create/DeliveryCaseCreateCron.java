package com.freshdirect.dataloader.autocase.create;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.delivery.ejb.AirclicManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.mail.ErpMailSender;

public class DeliveryCaseCreateCron {

	private final static Category LOGGER = LoggerFactory.getInstance(DeliveryCaseCreateCron.class);
	
	private static CrmAgentModel loginAgent;
	
	private static String crmCaseMedia = "Driver";
	
	public static void main(String[] args) {
		
		Context ctx = null;
		try	
		{
			loginAgent = getLoginAgent();
			Map<String, DeliveryException> result =  AirclicManager.getInstance().getCartonScanInfo();
			if(result != null){
				for(Map.Entry<String, DeliveryException> orderEntry : result.entrySet()){
					DeliveryException model = orderEntry.getValue();
					LOGGER.debug("Creatng case for order # " + model.getOrderId());
					try {
						if(model.getOrderId() != null) {
							FDOrderI order = null;
							try {
								order = FDCustomerManager.getOrder(model.getOrderId());
							} catch (FDResourceException e){
								// do nothing if no order exists
							}
							if(order != null){
								if(model.isEarlyDeliveryReq()){
									createCase(order.getCustomerId(), order.getErpSalesId(), CrmCaseSubject.CODE_EARLY_DELIVERY_REQEUST,
										"[Route/Stop] request for Early delivery - "+ model.getEarlyDlvStatus(), "Drivers attempted early delivery using the customer call feature", null);
								}
								if(model.getRefusedCartons() != null && model.getRefusedCartons().size() > 0){
									createCase(order.getCustomerId(), order.getErpSalesId(), CrmCaseSubject.CODE_REFUSED_CARTON,
										"Last refused at - "+ DateUtil.formatTime(model.getLastRefusedScan()), "Driver Scanned - "+ model.getReturnReason(), model.getRefusedCartons());
								}
								if(model.getLateBoxes() != null && model.getLateBoxes().size() > 0){
									createCase(order.getCustomerId(), order.getErpSalesId(), CrmCaseSubject.CODE_LATE_BOX,
										"Driver scanned at - "+ DateUtil.formatTime(model.getLateBoxScantime()), "Carton has left ProFoods",  model.getLateBoxes());
								}
							}
						}
					} catch(Exception ex){
						StringWriter sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
						email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
					}
				}
			}
			
		} catch (CrmAuthenticationException ae) {
			StringWriter sw = new StringWriter();
			ae.printStackTrace(new PrintWriter(sw));			
			LOGGER.info(new StringBuilder("DeliveryCreateCaseCron failed with Exception...").append(sw.toString()).toString());
			LOGGER.error(sw.toString());
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());	
		} catch (FDResourceException ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));			
			LOGGER.info(new StringBuilder("DeliveryCreateCaseCron failed with Exception...").append(sw.toString()).toString());
			LOGGER.error(sw.toString());
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());	
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));			
			LOGGER.info(new StringBuilder("DeliveryCreateCaseCron failed with Exception...").append(sw.toString()).toString());
			LOGGER.error(sw.toString());
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());		
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				StringWriter sw = new StringWriter();
				ne.printStackTrace(new PrintWriter(sw));	
				email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
			}
		}
	}
	
	private static CrmSystemCaseInfo buildCase(String customerPK, String saleId, CrmCaseSubject subject, String summary, String note, List cartons) {

		PrimaryKey salePK = saleId != null ? new PrimaryKey(saleId) : null;
		return new CrmSystemCaseInfo(new PrimaryKey(customerPK), salePK, subject, summary, note, cartons, loginAgent, crmCaseMedia);
	}

	private static void createCase(String customerID, String saleId, String subject, String summary, String note, List cartons) throws FDResourceException {
		
		CrmSystemCaseInfo caseInfo = buildCase(customerID, saleId, CrmCaseSubject.getEnum(subject), summary, note, cartons);
		FDCustomerManager.createCase(caseInfo);		
	}

	private static CrmAgentModel getLoginAgent() throws CrmAuthenticationException, FDResourceException {
		return CrmManager.getInstance().loginAgent(ErpServicesProperties.getCrmSystemDriverUserName(), ErpServicesProperties.getCrmSystemDriverUserPassword());
	}
	
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="DeliveryCreateCaseCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending Auto Case Create Cron report email: ", e);
		}
		
	}

}
