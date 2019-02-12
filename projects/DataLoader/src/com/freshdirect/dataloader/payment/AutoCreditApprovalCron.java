package com.freshdirect.dataloader.payment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
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
		if (null != args && args.length > 0) {
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
		
		try {
			List<String> regularComplaintIds = regularCreditApprovalEnabled ? collectRegularComplaints() : new ArrayList<String>();
			Map<String, String> regularComplaintErrors = approveRegularComplaints(regularComplaintIds);
			
			List<String> selfIssuedComplaintIds = selfIssuedCreditApprovalEnabled ? collectSelfIssuedComplaints() : new ArrayList<String>();	
			Map<String, String> selfIssuedComplaintErrors = approveSelfIssuedComplaints(selfIssuedComplaintIds);
			
			if (!regularComplaintErrors.isEmpty() || !selfIssuedComplaintErrors.isEmpty()) {
				logAndEmailErrors(regularComplaintErrors, selfIssuedComplaintErrors);
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
		}
	}
	
	private static void exitCronWithUsageError(String errorMessage) {
		System.err.println("AutoCreditApprovalCron error - " + errorMessage);
		System.err.print(collectUsages());
		System.exit(-1);
	}

	private static String collectUsages() {
		final String eol = System.getProperty("line.separator", "\n");
		String usages = new StringBuilder().append("Usages:").append(eol)
				.append("Default: java ").append(CRON_NAME).append(eol)
				.append("Approve regular complaints: java ").append(CRON_NAME).append(" [regular={true | false}]").append(eol)
				.append("Approve self-issued complaints: java ").append(CRON_NAME).append(" [self={true | false}]").append(eol).toString();
		return usages;
	}
	
	private static List<String> collectRegularComplaints() throws FDResourceException, ErpComplaintException, RemoteException {
		List<String> regularComplaintIds;
		regularComplaintIds = CustomerComplaintService.getInstance().autoApproveCredit();
		return regularComplaintIds;
	}
	
	private static List<String> collectSelfIssuedComplaints() throws RemoteException {
		PendingSelfComplaintResponse pendingSelfComplaintResponse = null;
		pendingSelfComplaintResponse = CustomerComplaintService.getInstance().getPendingSelfIssuedComplaints();
		
		List<String> pendingSelfComplaints = null == pendingSelfComplaintResponse ? new ArrayList<String>() : pendingSelfComplaintResponse.getPendingSelfComplaints();
		LOGGER.info("Going to AUTO approve " + pendingSelfComplaints.size() + " self-issued complaints");
		return pendingSelfComplaints;
	}
	
	private static Map<String, String> approveRegularComplaints(List<String> regularComplaintIds) {
		Map<String, String> regularComplaintErrors = new HashMap<String, String>();
		final double regularCreditAutoApproveAmount = ErpServicesProperties.getCreditAutoApproveAmount();
		for (String complaintId : regularComplaintIds) {
			LOGGER.info("Auto approve STARTED for complaint ID : " + complaintId);
			final String errorMessage = approveComplaint(complaintId, regularCreditAutoApproveAmount);
			if (StringUtils.isBlank(errorMessage)) {
				LOGGER.info("Auto approve FINISHED for complaint ID : " + complaintId);
			} else {
				regularComplaintErrors.put(complaintId, errorMessage);
				String errorLogMessage = new StringBuilder("Auto approve FAILED for complaint ID : ").append(complaintId).append("::  ").append(errorMessage).toString();
				LOGGER.warn(errorLogMessage);
			}
		}
		return regularComplaintErrors;
	}

	private static Map<String, String> approveSelfIssuedComplaints(List<String> selfIssuedComplaintIds) {
		Map<String, String> selfIssuedComplaintErrors = new HashMap<String, String>();
		final double selfIssuedComplaintAutoApproveAmount = ErpServicesProperties.getSelfCreditAutoapproveAmountPerComplaint();
		for (String selfIssuedComplaintId : selfIssuedComplaintIds) {
			LOGGER.info("Auto approve STARTED for self-issued complaint ID : " + selfIssuedComplaintId);
			final String errorMessage = approveComplaint(selfIssuedComplaintId, selfIssuedComplaintAutoApproveAmount);
			if (StringUtils.isBlank(errorMessage)) {
				LOGGER.info("Auto approve FINISHED for self-issued complaint ID : " + selfIssuedComplaintId);
			} else {
				selfIssuedComplaintErrors.put(selfIssuedComplaintId, errorMessage);
				String errorLogMessage = new StringBuilder("Auto approve FAILED for self-issued complaint ID : ").append(selfIssuedComplaintId).append("::  ").append(errorMessage).toString();
				LOGGER.warn(errorLogMessage);
			} 
		}
		return selfIssuedComplaintErrors;
	}
	
	private static String approveComplaint(String complaintId, double creditAutoApproveAmount) {
		final String initiator = EnumTransactionSource.SYSTEM.getName().toUpperCase();
		String errorMessage = "";
		try {
			CustomerComplaintService.getInstance().approveComplaint(complaintId, true, initiator, true,
						creditAutoApproveAmount);
		} catch (ErpComplaintException ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			errorMessage = sw.toString();
		} catch (EJBException ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			errorMessage = sw.toString();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			errorMessage = sw.toString();
		}
		return errorMessage;
	}
	
	private static void logAndEmailErrors(Map<String, String> regularComplaintErrors,
			Map<String, String> selfIssuedComplaintErrors) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<table>");
		
		for (Map.Entry<String, String> entry : regularComplaintErrors.entrySet()) {
		    String complaintId = entry.getKey();
		    String errorMessage = entry.getValue();
		    logActivity(complaintId, EnumAccountActivityType.REG_CREDIT_APPR_ERROR);
		    stringBuilder.append(populateErrorDetails(complaintId, errorMessage));
		}
		
		for (Map.Entry<String, String> entry : selfIssuedComplaintErrors.entrySet()) {
		    String complaintId = entry.getKey();
		    String errorMessage = entry.getValue();
		    logActivity(complaintId, EnumAccountActivityType.SELF_CREDIT_APPR_ERROR);
			stringBuilder.append(populateErrorDetails(complaintId, errorMessage));
		}
		
		stringBuilder.append("<\table>");
		email(Calendar.getInstance().getTime(), stringBuilder.toString());
	}
	
	private static void logActivity(String complaintId, EnumAccountActivityType accountActivityType) {
		try {
			String customerId = collectCustomerId(complaintId);
			if (null != customerId) {
				String note = new StringBuilder(accountActivityType.getName()).append(", complaintId: ").append(complaintId).toString();
				ErpActivityRecord erpActivityRecord = new ErpActivityRecord();
				erpActivityRecord.setCustomerId(customerId);
				erpActivityRecord.setActivityType(accountActivityType);
				erpActivityRecord.setDate(new Date());
				erpActivityRecord.setSource(EnumTransactionSource.SYSTEM);
				erpActivityRecord.setNote(note);
				ActivityLog.getInstance().logActivity(erpActivityRecord);
			}
		} catch (Exception e) {
			String message = new StringBuilder("Could not create activity log with complaint ID: ").append(complaintId).toString();
			LOGGER.error(message);
		}
	}
	
	private static String collectCustomerId(String complaintId) throws FinderException, RemoteException {
		String customerId = null;
		try {
			customerId = getErpSaleHome().findByComplaintId(complaintId).getCustomerPk().getId();
		} catch (FinderException e) {
			logCustomerIdException(complaintId, e);
			throw e;
		} catch (RemoteException e) {
			logCustomerIdException(complaintId, e);
			throw e;
		}
		return customerId;
	}
	
	private static ErpSaleHome getErpSaleHome() {
		return FDServiceLocator.getInstance().getErpSaleHome();
	}

	private static void logCustomerIdException(String complaintId, Exception e) {
		String message = new StringBuilder(e.getMessage()).append("Could not retrieve customer ID of complaint ID : ").append(complaintId).toString();
		LOGGER.error(message);
	}

	/**
	 * @param complaintId
	 * @param message
	 * @return 
	 */
	private static String populateErrorDetails(String complaintId, String message) {
		String editedMessage = limitErrorMessageLength(message);
		return new StringBuilder().append("<tr>").append("<td>").append("Complaint Id :" + complaintId).append("</td>")
				.append("<td>Error Message :: " + editedMessage).append("<\td><\tr>").toString();
	}
	
	private static String limitErrorMessageLength(String errorMessage) {
		return errorMessage.substring(0, Math.min(500, errorMessage.length()));
	}
	
	
	private static void email(Date processDate, String exceptionMsg) {

		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject = "AutoCreditApproval Cron :	"
					+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuilder stringBuilder = new StringBuilder();

			stringBuilder.append("<html>").append("<body>");

			if (exceptionMsg != null) {
				stringBuilder.append("Exception is :").append("\n");
				stringBuilder.append(exceptionMsg);
			}
			stringBuilder.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(), ErpServicesProperties.getCronFailureMailCC(), subject,
					stringBuilder.toString(), true, "");

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending AutoCreditApprovalCron report email: ", e);
		}
	}
}
