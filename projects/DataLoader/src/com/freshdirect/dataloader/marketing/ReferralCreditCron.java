package com.freshdirect.dataloader.marketing;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.customer.EnumComplaintLineType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumComplaintType;
import com.freshdirect.customer.EnumSendCreditEmail;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ejb.ActivityLogHome;
import com.freshdirect.customer.ejb.ActivityLogSB;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpCustomerInfoHome;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerHome;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerSB;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.ecomm.converter.ReferralConverter;
import com.freshdirect.fdstore.ecomm.gateway.CallCenterManagerService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerComplaintService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerInfoService;
import com.freshdirect.fdstore.ecomm.gateway.CustomerOrderService;
import com.freshdirect.fdstore.ecomm.gateway.FDReferralManagerService;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.mail.FDReferAFriendCreditEmail;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.referral.ReferralPromotionModel;
import com.freshdirect.fdstore.referral.ejb.FDReferralManagerHome;
import com.freshdirect.fdstore.referral.ejb.FDReferralManagerSB;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.mail.EmailAddress;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.payment.service.FDECommerceService;

public class ReferralCreditCron {
	private static Category LOGGER = LoggerFactory
			.getInstance(ReferralCreditCron.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

	private static Map complaintReasons = new HashMap();

	private static Context ctx = null;

	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ctx = getInitialContext();
		CallCenterManagerHome csManagerHome = (CallCenterManagerHome) ctx
				.lookup(FDStoreProperties.getCallCenterManagerHome());
		if (FDStoreProperties
				.isSF2_0_AndServiceEnabled(FDEcommProperties.CallCenterManagerSB)) {
			complaintReasons = CallCenterManagerService.getInstance()
					.getComplaintReasons(false);
		} else {
			CallCenterManagerSB csb = csManagerHome.create();
			complaintReasons = csb.getComplaintReasons(false);
		}
		FDReferralManagerSB sb = null;
		FDCustomerManagerSB fdsb = null;
		ActivityLogHome aHome = (ActivityLogHome) ctx
				.lookup("freshdirect.customer.ActivityLog");
		ActivityLogSB logSB = aHome.create();
		if(FDStoreProperties.isExtoleRafEnabled()){
			
			
			// MailerGatewayHome mHome = (MailerGatewayHome)
			// ctx.lookup("freshdirect.mail.MailerGateway");
			// MailerGatewaySB mailer = mHome.create();
			// ErpCustomerHome ecHome = (ErpCustomerHome) ctx.lookup(
			// FDStoreProperties.getErpCustomerHome() );
			LOGGER.info("Starting ReferralCreditCron ");
			// ReferralCreditCron cron = new ReferralCreditCron();
	
			// List<ReferralPromotionModel> sales = sb.getSettledSales();
			List<ReferralPromotionModel> sales = null;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				sales = FDReferralManagerService.getInstance().getSettledTransaction();
			} else {
				if (sb == null) {
					FDReferralManagerHome managerHome = (FDReferralManagerHome) ctx
							.lookup(FDStoreProperties.getFDReferralManagerHome());
					sb = managerHome.create();
				}
				sales = sb.getSettledTransaction();
			}
			LOGGER.info(" Sales list of the Advocates :" + sales);
	
			Iterator<ReferralPromotionModel> salesIter = sales.iterator();
			CrmAgentModel agent = new CrmAgentModel("system", "admin", "system",
					"User", true, CrmAgentRole.getEnum("ADM"), false);
			agent.setLdapId("system");
			List<ReferralPromotionModel> models = new ArrayList<ReferralPromotionModel>();
			while (salesIter.hasNext()) {
				try {
					ReferralPromotionModel model = (ReferralPromotionModel) salesIter
							.next();
					String referral_customer_id = model.getRefCustomerId();
					String referral_max_sale_id =   null;
					if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
						referral_max_sale_id = FDReferralManagerService.getInstance()
								.getLatestSTLSale(referral_customer_id);
					} else {
						if (sb == null) {
							FDReferralManagerHome managerHome = (FDReferralManagerHome) ctx
									.lookup(FDStoreProperties.getFDReferralManagerHome());
							sb = managerHome.create();
						}
						referral_max_sale_id = sb.getLatestSTLSale(referral_customer_id);
					}
					LOGGER.info(" Advocate Customer ID : " + referral_customer_id);
				LOGGER.info(" Advocate Settled Sale ID : " + referral_max_sale_id);
				// System.out.println("cust_sale_id:" + model.getSaleId());
				LOGGER.info(" Friend Customer ID : " + model.getCustomerId());
	
					if (referral_max_sale_id != null
							&& referral_max_sale_id.length() != 0) {
						// make sure order exists
						if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerOrder)) {
							boolean isExisted = CustomerOrderService.getInstance().isOrderExisted(referral_max_sale_id);
							 if (!isExisted) {
								 throw new FDResourceException("order " + referral_customer_id+ " does not exist");
							 }
						} else {
							if (fdsb == null) {
								FDCustomerManagerHome fdcmHome = (FDCustomerManagerHome) ctx
										.lookup(FDStoreProperties.getFDCustomerManagerHome());
								fdsb = fdcmHome.create();
							}
							fdsb.getOrder(referral_max_sale_id);
						}
						LOGGER.info("got FDOrder:" + referral_max_sale_id);
	
						// Create complaint
						ErpComplaintModel complaintModel = new ErpComplaintModel();
	
						// Create complain line
						List<ErpComplaintLineModel> lines = new ArrayList<ErpComplaintLineModel>();
						ErpComplaintLineModel line = new ErpComplaintLineModel();
						// Set up the Complaint Line Model with proper info
						LOGGER.info("EnumComplaintLineType.REFERRAL id============ "+EnumComplaintLineType.REFERRAL.getId());
						LOGGER.info("EnumComplaintLineType.REFERRAL statusCode============ "+EnumComplaintLineType.REFERRAL.getStatusCode());
						LOGGER.info("EnumComplaintLineType.REFERRAL name============ "+EnumComplaintLineType.REFERRAL.getName());
						line.setType(EnumComplaintLineType.REFERRAL);
						line.setQuantity(0);
						line.setAmount(model.getReferral_fee());
						List list = (List) complaintReasons.get("RAF");
						if (list == null)
							list = Collections.EMPTY_LIST;
						// List list = ComplaintUtil.getReasonsForDepartment("RAF");
						if (list.size() > 0)
							line.setReason((ErpComplaintReason) list.get(0));
						line.setMethod(EnumComplaintLineMethod.STORE_CREDIT);
	
						lines.add(line);
						LOGGER.info("line.getType()============ "+line.getType());
						complaintModel.addComplaintLines(lines);
						complaintModel.setType(EnumComplaintType
								.getEnum(complaintModel.getComplaintMethod()));
	
						// set complaint details
						complaintModel.setCreatedBy(agent.getUserId());
						complaintModel.setDescription("Referral Credits");//APPDEV-4996
						complaintModel.setCreateDate(new java.util.Date());
						complaintModel.setStatus(EnumComplaintStatus.PENDING);
						// email options - don;t send for now
						complaintModel
								.setEmailOption(EnumSendCreditEmail.DONT_SEND);
	
						LOGGER.info("Almost done with complaint:"
								+ (complaintModel.describe()));
	
						// addcomplaint
						boolean autoApproveAuthorized = true;
						PrimaryKey cPk;
						if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerComplaint)) {
							cPk = new PrimaryKey(CustomerComplaintService.getInstance().addComplaint(complaintModel, referral_max_sale_id,
									referral_customer_id, model.getFDCustomerId(), autoApproveAuthorized,
									Double.parseDouble(model.getReferral_fee() + "")));
						} else {
							if (fdsb == null) {
								FDCustomerManagerHome fdcmHome = (FDCustomerManagerHome) ctx
										.lookup(FDStoreProperties.getFDCustomerManagerHome());
								fdsb = fdcmHome.create();
							}
							cPk = fdsb.addComplaint(complaintModel, referral_max_sale_id,
									referral_customer_id, model.getFDCustomerId(), autoApproveAuthorized,
									Double.parseDouble(model.getReferral_fee() + ""));
						}
	
						// save the credit in customer invites
						// Commenting this since we don't need to keep track of the
						// transaction
						// sb.saveCustomerCredit(referral_customer_id,
						// model.getCustomerId(), model.getReferral_fee(),
						// model.getSaleId(), cPk.getId(),
						// model.getReferral_prgm_id());
	
						// send email to referral
						// Commenting this logic since extole will take care of
						// sending email
	
						/*
						 * String subject = model.getReferralCreditEmailSubject();
						 * String message = model.getReferralCreditEmailText();
						 * ErpCustomerEB eb = ecHome.findByPrimaryKey(new
						 * PrimaryKey( referral_customer_id )); ErpCustomerInfoModel
						 * referralCm = eb.getCustomerInfo(); ErpCustomerEB eb1 =
						 * ecHome.findByPrimaryKey(new PrimaryKey(
						 * model.getCustomerId() )); ErpCustomerInfoModel refereeCm
						 * = eb1.getCustomerInfo();
						 * 
						 * message = message.replace("<first name>",
						 * refereeCm.getFirstName()); message =
						 * message.replace("<last name>", refereeCm.getLastName());
						 * 
						 * FDCustomerInfo fdCustInfo = fdsb.getCustomerInfo(new
						 * FDIdentity(referral_customer_id,
						 * model.getFDCustomerId())); String depotCode =
						 * fdCustInfo.getDepotCode(); String fromEmail =
						 * FDStoreProperties.getCustomerServiceEmail();
						 * 
						 * // Most of the customers dont have the depot code
						 * populated. Removing this logic for now as logistics is
						 * not going to provide depot API if(depotCode != null) {
						 * fromEmail =
						 * FDDeliveryManager.getInstance().getCustomerServiceEmail
						 * (depotCode); }
						 * 
						 * FDReferAFriendCreditEmail xemail =
						 * (FDReferAFriendCreditEmail)
						 * FDEmailFactory.getInstance().createReferAFriendCreditEmail
						 * (referralCm.getFirstName(), message);
						 * xemail.setSubject(subject); xemail.setFromAddress(new
						 * EmailAddress("FreshDirect", fromEmail));
						 * xemail.setRecipient(referralCm.getEmail());
						 * mailer.enqueueEmail(xemail);
						 */
	
						// record the event in activity log
						ErpActivityRecord rec = new ErpActivityRecord();
						rec.setActivityType(EnumAccountActivityType.REFERRAL_CREDIT);
						rec.setSource(EnumTransactionSource.SYSTEM);
						rec.setInitiator("SYSTEM");
						rec.setChangeOrderId(referral_max_sale_id);
						rec.setCustomerId(referral_customer_id);
						rec.setDate(new Date());
						rec.setNote("$" + model.getReferral_fee() + ", "
								+ model.getCustomerId());
						if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ActivityLogSB)){
							FDECommerceService.getInstance().logActivity(rec);
						}
						else{
							logSB.logActivity(rec);
						}
						models.add(model);
					}
					
					// Ignore the exceptions and proceed with the next record.
				} catch (FDResourceException e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
				} catch (NumberFormatException e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
				} catch (RemoteException e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
				} catch (ErpComplaintException e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
				} /*
				 * catch (FinderException e) { LOGGER.error(e); }
				 */
			}
			// Update the Reward Transaction status
			try {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
					FDReferralManagerService.getInstance().updateSetteledRewardTransaction(models);
				} else {
					if (sb == null) {
						FDReferralManagerHome managerHome = (FDReferralManagerHome) ctx
								.lookup(FDStoreProperties.getFDReferralManagerHome());
						sb = managerHome.create();
					}
					sb.updateSetteledRewardTransaction(models);
				}
			} catch (NumberFormatException e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
			} catch (RemoteException e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
			}
		} else{ //Legacy Referral Program Credits

	        MailerGatewayHome mHome = (MailerGatewayHome) ctx.lookup("freshdirect.mail.MailerGateway");
	        ErpCustomerHome ecHome = (ErpCustomerHome) ctx.lookup( FDStoreProperties.getErpCustomerHome() );
	        ErpCustomerInfoHome ecInfoHome = (ErpCustomerInfoHome) ctx.lookup( FDStoreProperties.getErpCustomerInfoHome() );
	        LOGGER.info("Starting up now");
			List<ReferralPromotionModel> sales  = null;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)){
				 sales = FDReferralManagerService.getInstance().getSettledSales();
			}
			else{
				sales = sb.getSettledSales();
			}
			LOGGER.info("Got sales list:" + sales);
			
			Iterator<ReferralPromotionModel> salesIter = sales.iterator();
			CrmAgentModel agent = new CrmAgentModel("system", "admin", "system", "User", true, CrmAgentRole.getEnum("ADM"), false);
			agent.setLdapId("system");
			while(salesIter.hasNext()) {
				try {
					ReferralPromotionModel model = (ReferralPromotionModel) salesIter.next();
					String referral_customer_id = model.getRefCustomerId();
					String referral_max_sale_id = sb.getLatestSTLSale(referral_customer_id);
					System.out.println("referral_customer_id:" + referral_customer_id);
					System.out.println("referral_max_sale_id:" + referral_max_sale_id);
					System.out.println("cust_sale_id:" + model.getSaleId());
					System.out.println("cust_id:" + model.getCustomerId());
					
					if(referral_max_sale_id != null && referral_max_sale_id.length() != 0) {
						// make sure order exists
						if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerOrder)) {
							 boolean isExisted = CustomerOrderService.getInstance().isOrderExisted(referral_max_sale_id);
							 if (!isExisted) {
								 throw new FDResourceException("order " + referral_customer_id+ " does not exist");
							 }
						} else {
							if (fdsb == null) {
								FDCustomerManagerHome fdcmHome = (FDCustomerManagerHome) ctx
										.lookup(FDStoreProperties.getFDCustomerManagerHome());
								fdsb = fdcmHome.create();
							}
							fdsb.getOrder(referral_max_sale_id);
						}
						
						LOGGER.info("got FDOrder:" + referral_max_sale_id);
						
						//Create complaint
						ErpComplaintModel complaintModel = new ErpComplaintModel();
						
						//Create complin line
						List<ErpComplaintLineModel> lines = new ArrayList<ErpComplaintLineModel>();
						ErpComplaintLineModel line = new ErpComplaintLineModel();
					    // Set up the Complaint Line Model with proper info	        
					    line.setType(EnumComplaintLineType.REFERRAL);
					    line.setQuantity(0);
					    line.setAmount(model.getReferral_fee());
					    List list = (List) complaintReasons.get("RAF");
					    if(list == null)
					    	list = Collections.EMPTY_LIST;
					    //List list = ComplaintUtil.getReasonsForDepartment("RAF");
					    if(list.size() > 0)
					        line.setReason( (ErpComplaintReason) list.get(0));
					    line.setMethod( EnumComplaintLineMethod.STORE_CREDIT );
					    
					    lines.add(line);
					    complaintModel.addComplaintLines(lines);
					    complaintModel.setType(EnumComplaintType.getEnum(complaintModel.getComplaintMethod()));
					    
					    //set complaint details
					    complaintModel.setCreatedBy(agent.getUserId());
					    complaintModel.setDescription("Referral Credits");
					    complaintModel.setCreateDate(new java.util.Date());
					    complaintModel.setStatus(EnumComplaintStatus.PENDING);
					    //email options - don;t send for now
					    complaintModel.setEmailOption(EnumSendCreditEmail.DONT_SEND);
					    
					    LOGGER.info("Almost done with complaint:"+ (complaintModel.describe()));
					    
					    //addcomplaint
						boolean autoApproveAuthorized = true;
						PrimaryKey cPk;
						if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerComplaint)) {
							cPk = new PrimaryKey(CustomerComplaintService.getInstance().addComplaint(complaintModel,
									referral_max_sale_id, referral_customer_id, model.getFDCustomerId(),
									autoApproveAuthorized, Double.parseDouble(model.getReferral_fee() + "")));
						} else {
							if (fdsb == null) {
								FDCustomerManagerHome fdcmHome = (FDCustomerManagerHome) ctx
										.lookup(FDStoreProperties.getFDCustomerManagerHome());
								fdsb = fdcmHome.create();
							}
							cPk = fdsb.addComplaint(complaintModel, referral_max_sale_id, referral_customer_id,
									model.getFDCustomerId(), autoApproveAuthorized,
									Double.parseDouble(model.getReferral_fee() + ""));
						}
						// save the credit in customer invites
					    if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)){
					    	FDReferralManagerService.getInstance().saveCustomerCredit(referral_customer_id, model.getCustomerId(), model.getReferral_fee(), model.getSaleId(), cPk.getId(), model.getReferral_prgm_id());
							 
					    }
						else {
							if (sb == null) {
								FDReferralManagerHome managerHome = (FDReferralManagerHome) ctx
										.lookup(FDStoreProperties.getFDReferralManagerHome());
								sb = managerHome.create();
							}
							sb.saveCustomerCredit(referral_customer_id, model.getCustomerId(), model.getReferral_fee(),
									model.getSaleId(), cPk.getId(), model.getReferral_prgm_id());
						}
					    //send email to referral
					    String subject = model.getReferralCreditEmailSubject();
					    String message = model.getReferralCreditEmailText();
					    /*ErpCustomerEB eb = ecHome.findByPrimaryKey(new PrimaryKey( referral_customer_id ));
					    ErpCustomerInfoModel referralCm = eb.getCustomerInfo();*/
					    ErpCustomerInfoModel referralCm = (ErpCustomerInfoModel)ecInfoHome.findByErpCustomerId(referral_customer_id).getModel();
/*					    ErpCustomerEB eb1 = ecHome.findByPrimaryKey(new PrimaryKey( model.getCustomerId() ));
					    ErpCustomerInfoModel refereeCm = eb1.getCustomerInfo();*/
					    ErpCustomerInfoModel refereeCm = (ErpCustomerInfoModel)ecInfoHome.findByErpCustomerId(model.getCustomerId()).getModel();

					    message = message.replace("<first name>", refereeCm.getFirstName());
					    message = message.replace("<last name>", refereeCm.getLastName());
						FDCustomerInfo fdCustInfo;
						if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomerInfo)) {
							fdCustInfo = CustomerInfoService.getInstance()
									.getCustomerInfo(new FDIdentity(referral_customer_id, model.getFDCustomerId()));
						} else {
							if (fdsb == null) {
								FDCustomerManagerHome fdcmHome = (FDCustomerManagerHome) ctx
										.lookup(FDStoreProperties.getFDCustomerManagerHome());
								fdsb = fdcmHome.create();
							}
							fdCustInfo = fdsb
									.getCustomerInfo(new FDIdentity(referral_customer_id, model.getFDCustomerId()));
						}
					    String depotCode = fdCustInfo.getDepotCode();
					    String fromEmail = FDStoreProperties.getCustomerServiceEmail();
					    
					    // Most of the customers dont have the depot code populated. Removing this logic for now as logistics is not going to provide depot API
					    if(depotCode != null) {
					    	fromEmail = FDDeliveryManager.getInstance().getCustomerServiceEmail(depotCode);
					    }
					    
					    FDReferAFriendCreditEmail xemail = (FDReferAFriendCreditEmail) FDEmailFactory.getInstance().createReferAFriendCreditEmail(referralCm.getFirstName(), message);
					    xemail.setSubject(subject);
					    xemail.setFromAddress(new EmailAddress("FreshDirect", fromEmail));
					    xemail.setRecipient(referralCm.getEmail());
						if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.MailerGatewaySB)) {
							FDECommerceService.getInstance().enqueueEmail(xemail);
						} else {
							MailerGatewaySB mailer = mHome.create();
							mailer.enqueueEmail(xemail);
						}
					    //record the event in activity log
					    ErpActivityRecord rec = new ErpActivityRecord();
						rec.setActivityType(EnumAccountActivityType.REFERRAL_CREDIT);
						rec.setSource(EnumTransactionSource.SYSTEM);
						rec.setInitiator("SYSTEM");
						rec.setChangeOrderId(model.getSaleId());
						rec.setCustomerId(referral_customer_id);
						rec.setDate(new Date());
						rec.setNote("$" + model.getReferral_fee() + ", " + model.getCustomerId());
						if(FDStoreProperties.isSF2_0_AndServiceEnabled("customer.ejb.ActivityLogSB")){
							FDECommerceService.getInstance().logActivity(rec);
						}
						else{
							logSB.logActivity(rec);
						}
					}
					//Ignore the exceptions and proceed with the next record.
				} catch (FDResourceException e) {
					LOGGER.error(e);
				} catch (NumberFormatException e) {
					LOGGER.error(e);
				} catch (RemoteException e) {
					LOGGER.error(e);
				} catch (ErpComplaintException e) {
					LOGGER.error(e);
				} catch (FinderException e) {
					LOGGER.error(e);
				}
			}
		}

	}

	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					"EEE, MMM d, yyyy");
			String subject = "ReferralCreditCron:	"
					+ (processDate != null ? dateFormatter.format(processDate)
							: " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");

			if (exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),
					ErpServicesProperties.getCronFailureMailCC(), subject,
					buff.toString(), true, "");

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending ReferralCreditCron report email: ", e);
		}

	}

}
