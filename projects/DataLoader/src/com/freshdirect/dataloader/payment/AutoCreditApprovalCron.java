package com.freshdirect.dataloader.payment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
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
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.customer.selfcredit.PendingSelfComplaintResponse;
import com.freshdirect.fdstore.ecomm.gateway.CustomerComplaintService;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class AutoCreditApprovalCron {

	private final static Category LOGGER = LoggerFactory.getInstance(AutoCreditApprovalCron.class);
	private final static String REGULAR_PARAMETER= "regular=";
	private final static String SELF_PARAMETER="self=";
	private final static String CRON_NAME = AutoCreditApprovalCron.class.getName();

	public static void main(String[] args) throws Exception {
		boolean regularCreditApprovalEnabled = true;
		boolean selfIssuedCreditApprovalEnabled = true;
		if (null != args) {
			if (args.length > 1) {
				exitCronWithUsageError("Invalid parameters: can not accept more than 1 parameter.");
			}
			String arg = args[0];
			if (arg.startsWith(REGULAR_PARAMETER)) {
				regularCreditApprovalEnabled = Boolean.valueOf(arg.substring(REGULAR_PARAMETER.length())).booleanValue();
				if (!regularCreditApprovalEnabled) {
					exitCronWithUsageError("Invalid parameters: parameter " + arg + " is invalid.");
				}
				selfIssuedCreditApprovalEnabled = false;
			}else if (arg.startsWith(SELF_PARAMETER)) {
				selfIssuedCreditApprovalEnabled = Boolean.valueOf(arg.substring(SELF_PARAMETER.length())).booleanValue();
				if (!selfIssuedCreditApprovalEnabled) {
					exitCronWithUsageError("Invalid parameters: parameter " + arg + " is invalid.");
				}
				regularCreditApprovalEnabled = false;
			} else {
				exitCronWithUsageError("Invalid parameters: " + arg + " parameter name does not exist.");
			}
		}
		
		LOGGER.info("Automatic Credit Approval Started");
		LOGGER.info("Regular Credit Approval Enabled: " + String.valueOf(regularCreditApprovalEnabled));
		LOGGER.info("Self-issued Credit Approval Enabled: " + String.valueOf(selfIssuedCreditApprovalEnabled));
		
		final boolean isSF2_0_AndServiceEnabled = FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCustomer);
		Context ctx = getInitialContext();
		FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
		FDCustomerManagerSB sb = home.create();
		
		try {
			boolean errorFlg = false;
			StringBuffer strB = new StringBuffer();
			
			List<String> regularComplaintIds = regularCreditApprovalEnabled ? collectRegularComplaints(isSF2_0_AndServiceEnabled, sb) : new ArrayList<String>();
			strB.append("<table>");
			double regularCreditAutoApproveAmount = ErpServicesProperties.getCreditAutoApproveAmount();
			for (String complaintId : regularComplaintIds) {
				errorFlg = approveComplaint(complaintId, regularCreditAutoApproveAmount, isSF2_0_AndServiceEnabled, sb, errorFlg, strB, false);
			}
			
			List<String> selfIssuedComplaintIds = selfIssuedCreditApprovalEnabled ? collectSelfIssuedComplaints(isSF2_0_AndServiceEnabled, sb) : new ArrayList<String>();	
			double selfIssuedComplaintAutoApproveAmount = ErpServicesProperties.getSelfCreditAutoapproveAmountPerComplaint();
			for (String selfIssuedComplaintId : selfIssuedComplaintIds) {
				errorFlg = approveComplaint(selfIssuedComplaintId, selfIssuedComplaintAutoApproveAmount, isSF2_0_AndServiceEnabled, sb, errorFlg, strB, true);
			}
			
			if (errorFlg) {
				strB.append("<\table>");
				email(Calendar.getInstance().getTime(), strB.toString());
			}

			LOGGER.info("AutoCreditApprovalCron-finished");
		} catch (Exception e) {
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

	private static void exitCronWithUsageError(String errorMessage) {
		System.err.println(collectErrorMessage(errorMessage));
		System.err.print(collectUsages());
		System.exit(-1);
	}

	private static String collectErrorMessage(String errorMessage) {
		String error = new StringBuffer().append("AutoCreditApprovalCron error - ").append(errorMessage).toString();
		return error;
	}

	private static String collectUsages() {
		final String eol = System.getProperty("line.separator", "\n");
		String usages = new StringBuffer().append("Usages:").append(eol)
				.append("Default: java ").append(CRON_NAME).append(eol)
				.append("Approve regular complaints: java ").append(CRON_NAME).append(" [regular={true | false}]").append(eol)
				.append("Approve self-issued complaints: java ").append(CRON_NAME).append(" [self={true | false}]").append(eol).toString();
		return usages;
	}
	
	private static List<String> collectRegularComplaints(boolean isSF2_0_AndServiceEnabled, FDCustomerManagerSB sb) throws FDResourceException, ErpComplaintException, RemoteException {
		List<String> regularComplaintIds;
		if (isSF2_0_AndServiceEnabled) {
			regularComplaintIds = CustomerComplaintService.getInstance().autoApproveCredit();
		} else {
			/* reject the credits older than A month with STF in Sale APPDEV-6785 */
			LOGGER.info("Going to Reject Credits for morethan a month in Pending status");
			List<String> lstToRejCredits = sb.getComplaintsToRejectCredits();
			if (!lstToRejCredits.isEmpty())
				sb.rejectCreditsOlderThanAMonth(lstToRejCredits);
			LOGGER.info("Rejecting process has been  Done now");
			regularComplaintIds = sb.getComplaintsForAutoApproval();
			LOGGER.info("Going to AUTO approve " + regularComplaintIds.size() + " complaints");
		}
		return regularComplaintIds;
	}
	
	private static List<String> collectSelfIssuedComplaints(boolean isSF2_0_AndServiceEnabled, FDCustomerManagerSB sb) throws RemoteException, FDResourceException {
		PendingSelfComplaintResponse pendingSelfComplaintResponse = null;
		if (isSF2_0_AndServiceEnabled) {
			 pendingSelfComplaintResponse = CustomerComplaintService.getInstance().getPendingSelfIssuedComplaints();
		} else {
			pendingSelfComplaintResponse = sb.getSelfIssuedComplaintsForAutoApproval();
		}
		List<String> pendingSelfComplaints = null == pendingSelfComplaintResponse ? new ArrayList<String>() : pendingSelfComplaintResponse.getPendingSelfComplaints();
		LOGGER.info("Going to AUTO approve " + pendingSelfComplaints.size() + " self-issued complaints");
		return pendingSelfComplaints;
	}

	private static boolean approveComplaint(String complaintId, double creditAutoApproveAmount,
			boolean isSF2_0_AndServiceEnabled, FDCustomerManagerSB sb, boolean errorFlg, StringBuffer strB, boolean isSelfCredit) {
		String initiator = EnumTransactionSource.SYSTEM.getName().toUpperCase();
		String approvalStartMessage = collectApprovalStartMessage(isSelfCredit, complaintId);
		LOGGER.info(approvalStartMessage);
		try {
			if (isSF2_0_AndServiceEnabled) {
				CustomerComplaintService.getInstance().approveComplaint(complaintId, true, initiator, true,
						creditAutoApproveAmount);
			} else {
				sb.approveComplaint(complaintId, true, initiator, true,
						ErpServicesProperties.getCreditAutoApproveAmount());
			}
			String approvalFinishedMessage = collectApprovalFinishedMessage(isSelfCredit, complaintId);
			LOGGER.info(approvalFinishedMessage);
		} catch (ErpComplaintException ex) {
			errorFlg = true;
			logApprovalError(ex, sb, strB, complaintId, isSelfCredit);
		} catch (EJBException ex) {
			errorFlg = true;
			logApprovalError(ex, sb, strB, complaintId, isSelfCredit);
		} catch (Exception ex) {
			errorFlg = true;
			logApprovalError(ex, sb, strB, complaintId, isSelfCredit);
		}
		return errorFlg;
	}

	private static String collectApprovalStartMessage(boolean isSelfCredit, String complaintId) {
		String approvalStartMessage = isSelfCredit ? "Auto approve STARTED for self-issued complaint ID : " : "Auto approve STARTED for complaint ID : "; 
		return new StringBuffer(approvalStartMessage).append(complaintId).toString();
	}
	
	private static String collectApprovalFinishedMessage(boolean isSelfCredit, String complaintId) {
		String approvalFinishedMessage = isSelfCredit ? "Auto approve FINISHED for self-issued complaint ID : " : "Auto approve FINISHED for complaint ID : "; 
		return new StringBuffer(approvalFinishedMessage).append(complaintId).toString();
	}

	private static void logApprovalError(Exception ex, FDCustomerManagerSB sb, StringBuffer strB, String complaintId, boolean isSelfCredit) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		populateErrorDetails(strB, complaintId, limitErrorMessageLength(sw));
		String errorMessage = collectApproveFailedMessage(isSelfCredit, complaintId, sw);
		LOGGER.warn(errorMessage);
		try {
			sb.logComplaintApprovalErrorActivity(isSelfCredit, complaintId);
		} catch (Exception e) {
			String message = new StringBuffer("Could not create activity log with complaint ID: ").append(complaintId).toString();
			LOGGER.error(message);
		}
	}

	private static String limitErrorMessageLength(StringWriter sw) {
		return sw.toString().substring(0, Math.min(500, sw.toString().length()));
	}

	private static String collectApproveFailedMessage(boolean isSelfCredit, String complaintId, StringWriter sw) {
		String message = isSelfCredit ? "Auto approve FAILED for self-issued complaint ID : " : "Auto approve FAILED for complaint ID : ";
		return new StringBuffer(message).append(complaintId).append("::  ").append(sw.toString()).toString();
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
