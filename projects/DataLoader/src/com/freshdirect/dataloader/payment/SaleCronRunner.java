/*
 * 
 * SaleCronRunner.java
 * Date: Jul 5, 2002 Time: 5:53:45 PM
 */

package com.freshdirect.dataloader.payment;

/**
 * 
 * @author knadeem
 */
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.payment.ejb.SaleCronHome;
import com.freshdirect.dataloader.payment.ejb.SaleCronSB;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.ejb.DlvManagerHome;
import com.freshdirect.delivery.ejb.DlvManagerSB;
import com.freshdirect.delivery.model.UnassignedDlvReservationModel;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

public class SaleCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(SaleCronRunner.class);

	public static void main(String[] args) {
		long authTimeout;

		if (args.length >= 1) {
			int minutes = 15;
			try {
				// first parameter is for authTimeout
				minutes = Integer.parseInt(args[0]);
			} catch (NumberFormatException ne) {
				LOGGER.warn("Passed in param is not an int", ne);
			}

			authTimeout = minutes * 60 * 1000;
		}else{
			LOGGER.warn("NO timeout was passed for Authorizations defaulting to 15 mins");
			authTimeout = 15 * 60 * 1000;
		}

		Context ctx = null;
		try {
			ctx = getInitialContext();
			SaleCronHome home = (SaleCronHome) ctx.lookup("freshdirect.dataloader.SaleCron");

			SaleCronSB sb = home.create();
			sb.cancelAuthorizationFailed();
			int affected = sb.cutoffSales();

			if (affected > 0 && "true".equalsIgnoreCase(ErpServicesProperties.getSendCutoffEmail())) {
				Calendar cal = DateUtil.toCalendar(new Date());

				if (cal.get(Calendar.HOUR) > 2) {
					cal.add(Calendar.DATE, 1);
				}
				LOGGER.debug("Sending report for " + cal.getTime() + "...");
				CallCenterServices.emailCutoffTimeReport(cal.getTime());
				emailUnassigned();
			}
			//First clear pending reverse auth for cancelled orders.
			sb.reverseAuthorizeSales(authTimeout);
			//Second Pre auth gift card.
			sb.preAuthorizeSales(authTimeout);
			//Third perform CC authorization.
			sb.authorizeSales(authTimeout);
			// remved the following task, create a new cron job for it.
			//sb.captureSales(captureTimeout); 

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg=sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("SaleCronRunner failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);
			if(_msg!=null && _msg.indexOf("timed out while waiting to get an instance from the free pool")==-1)
				email(Calendar.getInstance().getTime(), _msg);		
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
				StringWriter sw = new StringWriter();
				ne.printStackTrace(new PrintWriter(sw));	
				email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
			}
		}
	}

	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	private static void emailUnassigned()
	{
		try
		{
			Context ctx = null;
			ctx = getInitialContext();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			
			DlvManagerSB dlvManager = null;
			DlvManagerHome dlh =(DlvManagerHome) ctx.lookup("freshdirect.delivery.DeliveryManager");
			dlvManager = dlh.create();
			List<UnassignedDlvReservationModel> _unassignedReservations = dlvManager.getUnassignedReservations(cal.getTime(),true);
			email(_unassignedReservations, cal.getTime());
		}
		catch(NamingException e)
		{
			e.printStackTrace();
		} catch (DlvResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="SaleCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Sending Sale Cron report email: ", e);
		}
		
	}
	
	private static void email(List<UnassignedDlvReservationModel> reservations, Date deliveryDate) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="Unassigned Reservations for the delivery date "+dateFormatter.format(deliveryDate);

			StringBuffer buf = new StringBuffer();

			buf.append("<html>").append("<body>");			
			buf.append("<table border=\"1\" valign=\"top\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
			buf.append("<tr>").append("<th>").append("Delivery Date").append("</th>")
							.append("<th>").append("Cutoff").append("</th>")
							.append("<th>").append("Zone").append("</th>")
							.append("<th>").append("Order ID").append("</th>")
							.append("<th>").append("Status Code").append("</th>")
							.append("<th>").append("Reservation Type").append("</th>")
							.append("<th>").append("Reserved Size").append("</th>")
							.append("<th>").append("Reserved Service Time").append("</th>")
							.append("<th>").append("Unassigned Action").append("</th>")
							.append("<th>").append("Update status").append("</th>")
							.append("</tr>");
			String cutoff=null;
			for(Iterator<UnassignedDlvReservationModel> i = reservations.iterator(); i.hasNext();){
				UnassignedDlvReservationModel info =  i.next();
				 
				 if(cutoff==null || cutoff.equals(info.getCutoff()))
				 {
					buf.append("<tr><td>").append(info.getOrderId()).append("</td><td>")
					.append(info.getDeliveryDate()).append("</td><td>")
					.append(info.getCutoff()).append("</td><td>")
					.append(info.getZoneCode()).append("</td><td>")
					.append(info.getOrderId()).append("</td><td>")
					.append(info.getStatusCode()).append("</td><td>")
					.append(info.getReservationType()).append("</td><td>")
					.append(info.getReservedOrderSize()).append("</td><td>")
					.append(info.getReservedServiceTime()).append("</td><td>")
					.append(info.getUnassignedActivityType()).append("</td><td>")
					.append(info.getUpdateStatus()).append("</td></tr>");
					cutoff = info.getCutoff();
				 }
				 else break;

			}

			
			buf.append("</table>").append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),"",
					subject, buf.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending Unassigned Reservation Cron report email: ", e);
		}
		
	}

}
