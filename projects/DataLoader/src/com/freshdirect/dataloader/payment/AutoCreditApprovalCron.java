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
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class AutoCreditApprovalCron {

	private final static Category LOGGER = LoggerFactory.getInstance(AutoCreditApprovalCron.class);

	public static void main(String[] args) throws Exception {
		LOGGER.info("Automatic Credit Approval Started");
		Context ctx = null;
		try {
			
			ctx = getInitialContext();
			FDCustomerManagerHome home = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
			FDCustomerManagerSB sb = home.create();
			List ids = sb.getComplaintsForAutoApproval();
			LOGGER.info("Going to AUTO approve " + ids.size() + " complaints");

			StringBuffer strB=new StringBuffer();
			strB.append("<table>");
			boolean errorFlg=false;
			StringWriter sw=null;
			for (Iterator i = ids.iterator(); i.hasNext();) {
				String complaintId = (String) i.next();
				LOGGER.info("Auto approve STARTED for complaint ID : " + complaintId);
				try {
					sb.approveComplaint(complaintId, true, "SYSTEM", true,ErpServicesProperties.getCreditAutoApproveAmount());
					LOGGER.info("Auto approve FINISHED for comolaint ID : " + complaintId);
				} catch (ErpComplaintException ex) {
					errorFlg=true;
					sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					populateErrorDetails(strB, complaintId, " Auto approve FAILED ");
					LOGGER.warn("Auto approve FAILED for complaint ID : " + complaintId +"::  "+ sw.toString());
				}catch (EJBException ex) {
					errorFlg=true;
					sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					populateErrorDetails(strB, complaintId, " Auto approve FAILED ");
					LOGGER.warn("Auto approve FAILED for complaint ID : " + complaintId +"::  "+ sw.toString());
				}catch (Exception ex) {
					errorFlg=true;
					sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
					populateErrorDetails(strB, complaintId, " Auto approve FAILED " );
					LOGGER.warn("Auto approve FAILED for complaint ID : " + complaintId +"::  "+ sw.toString());
				}
			}
			
			if(errorFlg){
				strB.append("<\table>");
				email(Calendar.getInstance().getTime(), strB.toString());

			}
			LOGGER.info("AutoCreditApprovalCron-finished");
		} catch (Exception e) {
			LOGGER.warn("Exception during CreditApproval", e);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			LOGGER.info(new StringBuilder("AutoCreditApprovalCron failed with Exception...").append(sw.toString()).toString());
			LOGGER.error(sw.toString());
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
			}
		}
		
	}

	/**
	 * @param strB
	 * @param complaintId
	 * @param message
	 */
	private static void populateErrorDetails(StringBuffer strB, String complaintId,
			String message) {
		strB.append("<tr>").append("<td>").append("Complaint Id :"+ complaintId).
		append("</td>").append("<td>Error Message :: "+message).append("<\td><\tr>");
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
			String subject="AutoCreditApproval Cron :	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending AutoCreditApprovalCron report email: ", e);
		}
	}


}
