package com.freshdirect.dataloader.payment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.customer.selfcredit.PendingSelfComplaint;
import com.freshdirect.fdstore.customer.selfcredit.PendingSelfComplaintResponse;
import com.freshdirect.fdstore.ecomm.gateway.CustomerComplaintService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class AutoCreditApprovalCron {

	private final static Category LOGGER = LoggerFactory.getInstance(AutoCreditApprovalCron.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("Automatic Credit Approval Started");
		Context ctx = null;
		try {
			List<String> regularComplaintIds;
			FDCustomerManagerSB sb = null;
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomer)) {
				regularComplaintIds = CustomerComplaintService.getInstance().autoApproveCredit();
			} else {
				ctx = getInitialContext();
				FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
				sb = home.create();
				/* reject the credits older than A month with STF in Sale APPDEV-6785 */
				LOGGER.info("Going to Reject Credits for morethan a month in Pending status");
				List<String> lstToRejCredits = sb.getComplaintsToRejectCredits();
				if (!lstToRejCredits.isEmpty())
					sb.rejectCreditsOlderThanAMonth(lstToRejCredits);
				LOGGER.info("Rejecting process has been  Done now");
				regularComplaintIds = sb.getComplaintsForAutoApproval();
				LOGGER.info("Going to AUTO approve " + regularComplaintIds.size() + " complaints");
			}
			StringBuffer strB = new StringBuffer();
			strB.append("<table>");

			boolean errorFlg = false;
			StringWriter sw = null;
			String initiator = EnumTransactionSource.SYSTEM.getName().toUpperCase();
			for (String complaintId : regularComplaintIds) {
				LOGGER.info("Auto approve STARTED for complaint ID : " + complaintId);
				try {
					if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomer)) {
						CustomerComplaintService.getInstance().approveComplaint(complaintId, true, initiator, true,
								ErpServicesProperties.getCreditAutoApproveAmount());
					} else {
						if (sb == null) {
							FDCustomerManagerHome home = (FDCustomerManagerHome) ctx
									.lookup("freshdirect.fdstore.CustomerManager");
							sb = home.create();
						}
						sb.approveComplaint(complaintId, true, initiator, true,
								ErpServicesProperties.getCreditAutoApproveAmount());
					}
					LOGGER.info("Auto approve FINISHED for comolaint ID : " + complaintId);
				} catch (ErpComplaintException ex) {
					errorFlg = true;
					sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					populateErrorDetails(strB, complaintId,
							sw.toString().substring(0, sw.toString().length() > 500 ? 500 : sw.toString().length()));
					LOGGER.warn("Auto approve FAILED for complaint ID : " + complaintId + "::  " + sw.toString());
				} catch (EJBException ex) {
					errorFlg = true;
					sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					populateErrorDetails(strB, complaintId,
							sw.toString().substring(0, sw.toString().length() > 500 ? 500 : sw.toString().length()));
					LOGGER.warn("Auto approve FAILED for complaint ID : " + complaintId + "::  " + sw.toString());
				} catch (Exception ex) {
					errorFlg = true;
					sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					populateErrorDetails(strB, complaintId,
							sw.toString().substring(0, sw.toString().length() > 500 ? 500 : sw.toString().length()));
					LOGGER.warn("Auto approve FAILED for complaint ID : " + complaintId + "::  " + sw.toString());
				}
			}

			PendingSelfComplaintResponse pendingSelfComplaintResponse = sb.getSelfIssuedComplaintsForAutoApproval();
			List<PendingSelfComplaint> pendingSelfComplaints = pendingSelfComplaintResponse.getPendingSelfComplaints();
			if (!pendingSelfComplaints.isEmpty()) {
				LOGGER.info("Going to AUTO approve " + pendingSelfComplaints.size() + " self-issued complaints");
				for (PendingSelfComplaint pendingSelfComplaint : pendingSelfComplaints) {
					String selfComplaintId = pendingSelfComplaint.getComplaintId();
					LOGGER.info("Auto approve STARTED for self-issued complaint ID : " + selfComplaintId);
					sb.approveComplaint(selfComplaintId, true, initiator, true, ErpServicesProperties.getSelfCreditAutoapproveAmountPerComplaint());
		            LOGGER.info("Auto approve FINISHED for self-issued complaint ID : " + selfComplaintId);
				}
			} else {
				LOGGER.info("No self-issued complaints to AUTO approve");
			}

			if (errorFlg) {
				strB.append("<\table>");
				email(Calendar.getInstance().getTime(), strB.toString());
			}

			LOGGER.info("AutoCreditApprovalCron-finished");
		} catch (

		Exception e) {
			LOGGER.warn("Exception during CreditApproval", e);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			LOGGER.info(new StringBuilder("AutoCreditApprovalCron failed with Exception...").append(sw.toString())
					.toString());
			LOGGER.error(sw.toString());
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				// could not do the cleanup
			}
		}
	}

	/**
	 * @param strB
	 * @param complaintId
	 * @param message
	 */
	private static void populateErrorDetails(StringBuffer strB, String complaintId, String message) {
		strB.append("<tr>").append("<td>").append("Complaint Id :" + complaintId).append("</td>")
				.append("<td>Error Message :: " + message).append("<\td><\tr>");
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
			String subject = "AutoCreditApproval Cron :	"
					+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");

			if (exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(), ErpServicesProperties.getCronFailureMailCC(), subject,
					buff.toString(), true, "");

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending AutoCreditApprovalCron report email: ", e);
		}
	}

}
