package com.freshdirect.dataloader.marketing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ejb.ActivityLogHome;
import com.freshdirect.customer.ejb.ActivityLogSB;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerHome;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerSB;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
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
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;

public class ReferralCreditCron {
	private static Category	LOGGER	= LoggerFactory.getInstance( ReferralCreditCron.class );
	
	private final static ServiceLocator LOCATOR = new ServiceLocator();
	
	private static Map complaintReasons	= new HashMap();
	
	private static Context ctx = null;
	
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	public List<ReferralPromotionModel> getSettledSales() {
		try {
			return FDReferralManager.getSettledSales();
		} catch (FDResourceException e) {
			LOGGER.error("Error while getting settled sales", e);
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ctx = getInitialContext();
		CallCenterManagerHome csManagerHome = (CallCenterManagerHome) ctx.lookup(FDStoreProperties.getCallCenterManagerHome());
		CallCenterManagerSB csb = csManagerHome.create();		
		complaintReasons = csb.getComplaintReasons(false);
		FDReferralManagerHome managerHome = (FDReferralManagerHome) ctx.lookup(FDStoreProperties.getFDReferralManagerHome());
		FDReferralManagerSB sb = managerHome.create();
		FDCustomerManagerHome fdcmHome = (FDCustomerManagerHome) ctx.lookup(FDStoreProperties.getFDCustomerManagerHome());
        FDCustomerManagerSB fdsb = fdcmHome.create();
        ActivityLogHome aHome = (ActivityLogHome) ctx.lookup("freshdirect.customer.ActivityLog");
        ActivityLogSB logSB = aHome.create();
        MailerGatewayHome mHome = (MailerGatewayHome) ctx.lookup("freshdirect.mail.MailerGateway");
        MailerGatewaySB mailer = mHome.create();
        ErpCustomerHome ecHome = (ErpCustomerHome) ctx.lookup( FDStoreProperties.getErpCustomerHome() );
		System.out.println("Starting up now");
		ReferralCreditCron cron = new ReferralCreditCron();
		List<ReferralPromotionModel> sales = sb.getSettledSales();
		
		System.out.println("Got sales list:" + sales);
		
		Iterator<ReferralPromotionModel> salesIter = sales.iterator();
		CrmAgentModel agent = new CrmAgentModel("system", "admin", "system", "User", true, CrmAgentRole.getEnum("ADM"), false);
		agent.setLdapId("system");
		while(salesIter.hasNext()) {
			ReferralPromotionModel model = (ReferralPromotionModel) salesIter.next();
			String referral_customer_id = model.getRefCustomerId();
			String referral_max_sale_id = sb.getLatestSTLSale(referral_customer_id);
			System.out.println("referral_customer_id:" + referral_customer_id);
			System.out.println("referral_max_sale_id:" + referral_max_sale_id);
			System.out.println("cust_sale_id:" + model.getSaleId());
			System.out.println("cust_id:" + model.getCustomerId());
			
			if(referral_max_sale_id != null && referral_max_sale_id.length() != 0) {
				//Create FDORderI object
				FDOrderI order = fdsb.getOrder(referral_max_sale_id);
				
				System.out.println("got FDOrder:" + order.toString());
				
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
		        complaintModel.setDescription("Referral credit testing1");
		        complaintModel.setCreateDate(new java.util.Date());
		        complaintModel.setStatus(EnumComplaintStatus.PENDING);
		        //email options - don;t send for now
		        complaintModel.setEmailOption(EnumSendCreditEmail.DONT_SEND);
		        
		        System.out.println("Almost done with complaint:"+ (complaintModel.describe()));
		        
		        //addcomplaint
		        boolean autoApproveAuthorized = true;
		        PrimaryKey cPk = fdsb.addComplaint(complaintModel, referral_max_sale_id,referral_customer_id,model.getFDCustomerId(),autoApproveAuthorized,Double.parseDouble(model.getReferral_fee()+""));
		        
		        //save the credit in customer invites
		        sb.saveCustomerCredit(referral_customer_id, model.getCustomerId(), model.getReferral_fee(), model.getSaleId(), cPk.getId(), model.getReferral_prgm_id());
		        
		        //send email to referral
		        String subject = model.getReferralCreditEmailSubject();
		        String message = model.getReferralCreditEmailText();
		        ErpCustomerEB eb = ecHome.findByPrimaryKey(new PrimaryKey( referral_customer_id ));
		        ErpCustomerInfoModel referralCm = eb.getCustomerInfo();
		        ErpCustomerEB eb1 = ecHome.findByPrimaryKey(new PrimaryKey( model.getCustomerId() ));
		        ErpCustomerInfoModel refereeCm = eb1.getCustomerInfo();
	
		        message = message.replace("<first name>", refereeCm.getFirstName());
		        message = message.replace("<last name>", refereeCm.getLastName());
		        
		        FDCustomerInfo fdCustInfo = fdsb.getCustomerInfo(new FDIdentity(referral_customer_id, model.getFDCustomerId()));
		        String depotCode = fdCustInfo.getDepotCode();
		        String fromEmail = FDStoreProperties.getCustomerServiceEmail();
		        if(depotCode != null) {
		        	fromEmail = FDDepotManager.getInstance().getCustomerServiceEmail(depotCode);
		        }
		        
		        FDReferAFriendCreditEmail xemail = (FDReferAFriendCreditEmail) FDEmailFactory.getInstance().createReferAFriendCreditEmail(referralCm.getFirstName(), message);
		        xemail.setSubject(subject);
		        xemail.setFromAddress(new EmailAddress("FreshDirect", fromEmail));
		        xemail.setRecipient(referralCm.getEmail());
		        mailer.enqueueEmail(xemail);
		        
		        //record the event in activity log
		        ErpActivityRecord rec = new ErpActivityRecord();
				rec.setActivityType(EnumAccountActivityType.REFERRAL_CREDIT);
				rec.setSource(EnumTransactionSource.SYSTEM);
				rec.setInitiator("SYSTEM");
				rec.setChangeOrderId(model.getSaleId());
				rec.setCustomerId(referral_customer_id);
				rec.setDate(new Date());
				rec.setNote("$" + model.getReferral_fee() + ", " + model.getCustomerId());
				logSB.logActivity(rec);
			}
		}
		
	}

}
