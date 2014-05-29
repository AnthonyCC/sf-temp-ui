package com.freshdirect.fdstore.standingorders.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.fdstore.standingorders.UnavailabilityReason;
import com.freshdirect.fdstore.standingorders.SOResult.Result;
import com.freshdirect.fdstore.standingorders.SOResult.ResultList;
import com.freshdirect.fdstore.standingorders.UnAvailabilityDetails;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;

/**
 * <p>This class is used for processing standing orders from cron job.</p>
 * 
 * <p>It can be used for testing processing standing orders from command-line.
 * When running from command-line, directory of <code>erpservices.properties</code> must be on classpath
 * and it must contain a valid <code>mail.smtp.host</code> property in order to be able to send cron report emails.</p>
 * 
 * <p>Email-sending works only, when <code>sendEmail=true</code> command line argument is set.
 * When <code>order=...</code> is set, the specified SOs will be processed. If no <code>order=...</code> command
 * line argument is set, SOs will be populated by the application.</p>
 */
public class StandingOrdersServiceCmd {

	private static final Logger LOGGER = LoggerFactory.getInstance(StandingOrdersServiceCmd.class);
	
	private static FDStandingOrdersManager	soManager			= FDStandingOrdersManager.getInstance();	
	private static ThreadLocal<StandingOrdersServiceHome> sosHome = new ThreadLocal<StandingOrdersServiceHome>();


	private static void lookupSOSHome() throws FDResourceException {
		if ( sosHome.get() != null ) {
			return;
		}
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			sosHome.set( (StandingOrdersServiceHome)ctx.lookup( StandingOrdersServiceHome.JNDI_HOME ) );
		} catch (NamingException ne) {
			throw new FDResourceException(ne);			
		} finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("cannot close Context while trying to cleanup", ne);				
			}
		}
	}
	
	private static void invalidateSOSHome() {
		sosHome.set( null );
	}

	
	
	public static void main( String[] args ) {	
		StandingOrdersJobConfig jobConfig = new StandingOrdersJobConfig();
		
		List<String> soIdList = null;
		try{
			if (args.length >= 1) {
				for (String arg : args) {
					try { 
						if (arg.startsWith("orders=")) {
							String orders=arg.substring("orders=".length());
							if(null !=orders && !orders.trim().equalsIgnoreCase("")){
								String[] order = orders.split(",");								
								Set<String> soSet = new HashSet<String>(Arrays.asList(order));
								soIdList = new ArrayList<String>(soSet);
								
							}
						}  else if (arg.startsWith("sendReportEmail=")) {								
							jobConfig.setSendReportEmail(Boolean.valueOf(arg.substring("sendReportEmail=".length())).booleanValue()); 
						}  else if (arg.startsWith("isSendReminderNotificationEmail=")) {								
							jobConfig.setSendReminderNotificationEmail(Boolean.valueOf(arg.substring("isSendReminderNotificationEmail=".length())).booleanValue()); 
						}  else if(arg.startsWith("createIfSoiExistsForWeek=")) {
							jobConfig.setCreateIfSoiExistsForWeek(Boolean.valueOf(arg.substring("createIfSoiExistsForWeek=".length())).booleanValue()); 							
						}
					} catch (Exception e) {
						System.err.println("Usage: java com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceCmd [orders=(,) separated}] [sendEmail={true | false}]");
						System.exit(-1);
					}
				}
			}
			LOGGER.info( "jobConfig: "+ jobConfig );
						
			SOResult.ResultList result = placeStandingOrders(soIdList, jobConfig);
				
			if (jobConfig.isSendReportEmail()) {
				sendReportMail(result);
			}
			
			if (FDStoreProperties.isIgnoreATPFailureForSO()) {
				sendATPReportMail(result);
			}
			
		} catch (Exception e) {
			LOGGER.error("StandingOrdersServiceCmd failed with Exception...",e);
			sendExceptionMail(Calendar.getInstance().getTime(), e);
		}
	}

	private static SOResult.ResultList placeStandingOrders(Collection<String> soList, StandingOrdersJobConfig jobConfig) {
		try {
			lookupSOSHome();
			StandingOrdersServiceSB sb = sosHome.get().create();
			
			LOGGER.info( "Starting to place orders..." );
			
			SOResult.ResultList result = sb.placeStandingOrders(soList, jobConfig);
			
			LOGGER.info( "Finished placing orders." );
			LOGGER.info( "  success : " + result.getSuccessCount() );
			LOGGER.info( "  failed  : " + result.getFailedCount() );
			LOGGER.info( "  skipped : " + result.getSkippedCount() );
			LOGGER.info( "  total   : " + result.getTotalCount() );
			
			return result;
			
		} catch ( CreateException e ) {
			invalidateSOSHome();
			LOGGER.error("CreateException",e);
		} catch ( RemoteException e ) {
			invalidateSOSHome();
			LOGGER.error("RemoteException",e);
		} catch ( FDResourceException e ) {
			invalidateSOSHome();
			LOGGER.error("FDResourceException",e);
		}
		return null;
	}

	private static void sendATPReportMail(ResultList result) {
		try {
			LOGGER.info( "Cron ATP report enabled" );
			final List<Result> resultList = result.getResultsList();
			Map<String, Double> unavailableDetails = new TreeMap<String, Double>();
			if(resultList != null) {
				for(Result res : resultList) {
					Map<FDCartLineI, UnAvailabilityDetails> details = res.getUnavailabilityDetails();
					if(details != null) {
						for (Map.Entry<FDCartLineI, UnAvailabilityDetails> entry : details.entrySet()) {
							if(entry.getValue().getReason().equals(UnavailabilityReason.ATP)) {
								if(!unavailableDetails.containsKey(entry.getKey().getMaterialNumber())) {
									unavailableDetails.put(entry.getKey().getMaterialNumber(), new Double(0.0));
								}
								unavailableDetails.put(entry.getKey().getMaterialNumber()
											, unavailableDetails.get(entry.getKey().getMaterialNumber()) + entry.getValue().getUnavailQty());
							}
						}
					}
					
				}
				final String atpFailureReportMailTo = ErpServicesProperties.getStandingOrdersATPFailureReportRecipientAddress();
				
				SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
				Date date = Calendar.getInstance().getTime();
				String subject=FDStoreProperties.getStandingOrderReportEmailSubject()+ (date != null ? " "+dateFormatter.format(date) : " date error");
				
				StringBuffer buffer = new StringBuffer();
				buffer.append("<html>").append("<body>");

				
				buffer.append( "<h2>Consolidated ATP failure report:</h2>" );
				
				buffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"float:none;\">");
				buffer.append("<tr>")
				.append("<th nowrap=\"nowrap\">").append("Material ID").append("</th>")				
				.append("<th nowrap=\"nowrap\">").append("Unavailable Qty").append("</th>")	
				.append("</tr>");
				for (Map.Entry<String, Double> entry : unavailableDetails.entrySet()) {
					buffer.append("<tr>")
					.append("<td nowrap=\"nowrap\">").append(entry.getKey()).append("</td>")					
					.append("<td nowrap=\"nowrap\">").append(entry.getValue()).append("</td>")
					.append("</tr>");	
				}
				buffer.append("</table>");
			
			
				buffer.append("</table>");
				buffer.append("<br/><br/><br/>");
				
				buffer.append("</body>").append("</html>");
	
				
				ErpMailSender mailer = new ErpMailSender();
				mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
						atpFailureReportMailTo,ErpServicesProperties.getCronFailureMailCC(),
						"Standing Order ATP Failure Report", buffer.toString(), true, "");
				LOGGER.info( "SO ATP failure  report sent to "+ atpFailureReportMailTo );
			}
			
		} catch (MessagingException e) {
			LOGGER.warn("Error Sending Standing Order cron report email: ", e);
		}
		
	}
	
	
	private static void sendReportMail(ResultList result) {
		try {
			LOGGER.info( "Cron report enabled" );
			final List<Result> resultList = result.getResultsList();
			final String techMailTo = ErpServicesProperties.getStandingOrdersTechnicalErrorRecipientAddress();
			
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			Date date = Calendar.getInstance().getTime();
			String subject=FDStoreProperties.getStandingOrderReportEmailSubject()+ (date != null ? " "+dateFormatter.format(date) : " date error");
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("<html>").append("<body>");

			buffer.append( "<h1>Cron report:</h1>" );
			buffer.append( "<h2>Summary of this run of the background service:</h2>" );

			buffer.append( "  success : " ).append( result.getSuccessCount() ).append("<br/>");
			buffer.append( "  failed  : " ).append( result.getFailedCount() ).append("<br/>");
			buffer.append( "  skipped : " ).append( result.getSkippedCount() ).append("<br/>");
			buffer.append( "  total   : " ).append( result.getTotalCount() ).append("<br/>");
			if (result.isErrorOccured()) {
				buffer.append("<br/>");
				buffer.append( "  <span style=\"color:orange;\">WARNING: Standing Order Coremetrics tracking is temporarily suspended due to 3 consecutive errors.</span> " ).append("<br/>");
			}

			if ( resultList != null ) {
				
				buffer.append( "<h2>Details of this run of the background service:</h2>" );
				buffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"float:none;\">");
				buffer.append("<tr>")
				.append("<th nowrap=\"nowrap\">").append("Standing Order #").append("</th>")				
				.append("<th nowrap=\"nowrap\">").append("SO name").append("</th>")				
				.append("<th nowrap=\"nowrap\">").append("Status").append("</th>")				
				.append("<th nowrap=\"nowrap\">").append("Error").append("</th>")				
				.append("<th nowrap=\"nowrap\">").append("Mail sent to").append("</th>")				
				.append("<th nowrap=\"nowrap\" >").append("OrderId").append("</th>")				
				.append("<th nowrap=\"nowrap\">").append("Customer Id").append("</th>")
				.append("<th nowrap=\"nowrap\">").append("Customer email").append("</th>")
				.append("<th nowrap=\"nowrap\">").append("Details").append("</th>")
				
				.append("</tr>");
				
				for ( Result r : resultList ) {
					
					String customerEmail = r.getCustomerInfo()!=null ? r.getCustomerInfo().getEmailAddress():"-N/A-";
					String customerId = r.getCustId()!=null ? r.getCustId():"-N/A-";
					String statusString = r.getStatus().toString();
					
					if ( r.isTechnicalError() ) {
						statusString = "<span style=\"color:red;\">" + statusString + "</span>";
					} else if ( r.isError() ) {
						statusString = "<span style=\"color:orange;\">" + statusString + "</span>";
					} else if ( r.isSkipped() ) {
						statusString = "<span style=\"color:gray;\">" + statusString + "</span>";
					} else {
						statusString = "<span style=\"color:blue;\">" + statusString + "</span>";
					}
					
					buffer.append("<tr>")
					.append("<td nowrap=\"nowrap\">").append(r.getSoId()).append("</td>")					
					.append("<td nowrap=\"nowrap\">").append(r.getSoName()).append("</td>")					
					.append("<td nowrap=\"nowrap\">").append(statusString).append("</td>")					
					.append("<td nowrap=\"nowrap\">").append(r.isError()?r.getErrorCode().toString():"-none-").append("</td>")					
					.append("<td nowrap=\"nowrap\">").append(r.isErrorEmailSentToCustomer()?customerEmail:(r.isErrorEmailSentToAdmins()?techMailTo:"-not sent-")).append("</td>")
					.append("<td nowrap=\"nowrap\">").append(r.getSaleId()!=null?r.getSaleId():"-order not created-").append("</td>")
					.append("<td nowrap=\"nowrap\">").append(customerId).append("</td>")
					.append("<td nowrap=\"nowrap\">").append(customerEmail).append("</td>")
					.append("<td nowrap=\"nowrap\">").append(getDetails(r)).append("</td>")
					
					.append("</tr>");					
				}
				buffer.append("</table>");
				buffer.append("<br/><br/><br/>");			
			}
		
			
			buffer.append( "<h1>Database report:</h1>" );
			
			FDStandingOrderInfoList soFailedList = (FDStandingOrderInfoList)soManager.getFailedStandingOrdersCustInfo();
			FDStandingOrderInfoList soMechanicalFailedList = (FDStandingOrderInfoList)soManager.getMechanicalFailedStandingOrdersCustInfo();
			if(null != soFailedList){
				List<FDStandingOrderInfo> soFailedInfoList = soFailedList.getStandingOrdersInfo();
				buffer.append("<h2>Failed Standing Orders: "+soFailedInfoList.size()+"</h2><br/><br/>");
				
				if(!soFailedInfoList.isEmpty()){
					buffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"float:none;\">");
					buffer.append("<tr>")
					.append("<th nowrap=\"nowrap\">").append("Standing Order #").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Standing Order Name").append("</th>")
					.append("<th nowrap=\"nowrap\" >").append("Customer").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Customer Id").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Company").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Next Order Delivery Date").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Delivery Time").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Order Frequency (In Weeks)").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Error").append("</th>")			
					.append("<th nowrap=\"nowrap\">").append("Failed On").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Address").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Business Phone").append("</th>")
					.append("<th nowrap=\"nowrap\">").append("Cell Phone").append("</th>")				
					.append("<th nowrap=\"nowrap\">").append("Payment Method").append("</th>").append("</tr>");
					
					for (Iterator<FDStandingOrderInfo> iterator = soFailedInfoList.iterator(); iterator
							.hasNext();) {
						FDStandingOrderInfo soInfo = (FDStandingOrderInfo) iterator.next();
						
						buffer.append("<tr>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getSoID()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getSoName()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getUserId()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getCustomerId()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getCompanyName()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getNextDate()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(FDTimeslot.getDisplayString(true,soInfo.getStartTime(),soInfo.getEndTime())).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getFrequency()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getErrorHeader()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getFailedOn()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getAddress()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getBusinessPhone()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getCellPhone()).append("</td>")				
						.append("<td nowrap=\"nowrap\">").append(soInfo.getPaymentMethod()).append("</td>")					
						.append("</tr>");					
					}
					buffer.append("</table>");
					buffer.append("<br/><br/><br/>");
				}
			}
			
			if(null != soMechanicalFailedList){
				
				List<FDStandingOrderInfo> soMecFailedInfoList = soMechanicalFailedList.getStandingOrdersInfo();
				buffer.append("<h2>Mechanical Failures: "+soMecFailedInfoList.size()+"</h2><br/><br/>");
				if(!soMecFailedInfoList.isEmpty()){
					buffer.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"float:none;\">");
					buffer.append("<tr>")
					.append("<th>").append("Standing Order #").append("</th>")
					.append("<th>").append("Standing Order Name").append("</th>")
					.append("<th>").append("Customer").append("</th>")
					.append("<th>").append("Customer Id").append("</th>")
					.append("<th>").append("Company").append("</th>")
					.append("<th>").append("Order Date").append("</th>")				
					.append("<th>").append("Address").append("</th>")
					.append("<th>").append("Business Phone").append("</th>")
					.append("<th>").append("Cell Phone").append("</th>")
					.append("</tr>");
					
					for (Iterator<FDStandingOrderInfo> iterator = soMecFailedInfoList.iterator(); iterator.hasNext();) {
						FDStandingOrderInfo soInfo = (FDStandingOrderInfo) iterator.next();
						
						buffer.append("<tr>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getSoID()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getSoName()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getUserId()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getCustomerId()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getCompanyName()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getNextDate()).append("</td>")					
						.append("<td nowrap=\"nowrap\">").append(soInfo.getAddress()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getBusinessPhone()).append("</td>")
						.append("<td nowrap=\"nowrap\">").append(soInfo.getCellPhone()).append("</td>")								
						.append("</tr>");					
					}
					buffer.append("</table>");
					buffer.append("<br/><br/><br/>");
				}
			}
			
			buffer.append("</body>").append("</html>");

			
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(FDStoreProperties.getCustomerServiceEmail(),
					FDStoreProperties.getStandingOrderReportToEmail(),
					FDStoreProperties.getStandingOrderReportToEmail(),
					subject, buffer.toString(), true, "");
			LOGGER.info( "Cron report sent to "+ FDStoreProperties.getStandingOrderReportToEmail() );
		} catch (FDResourceException e) {
			LOGGER.warn("Error getting failed standing orders: ", e);
		} catch (MessagingException e) {
			LOGGER.warn("Error Sending Standing Order cron report email: ", e);
		}
		
	}
	
	private static String getDetails( Result result ) {
		StringBuilder sb = new StringBuilder();
		if ( result.getInternalMessage() != null ) {
			sb.append( result.getInternalMessage() );
			sb.append( "<br/>" );
		} else if ( result.getErrorHeader() != null ) {
			sb.append( result.getErrorHeader() );
			sb.append( "<br/>" );
		}
		
		if ( result.hasInvalidItems() ) {
			sb.append( "<br/>Has invalid items<br/>" );
		}
		
		List<String> unavailableItems = result.getUnavailableItems();
		if ( unavailableItems != null && !unavailableItems.isEmpty() ) {
			sb.append( "<br />Unavailable items:<br />" );
			for ( String unavailableItem : unavailableItems ) {
				sb.append( unavailableItem + "<br />" );
			}
		}
		return sb.toString();
	}

	private static void sendExceptionMail( Date processDate, Throwable exception ) {
		try {
			
			StringWriter sw = new StringWriter();
			exception.printStackTrace(new PrintWriter(sw));
			String exceptionMsg = sw.getBuffer().toString();

			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="StandingOrderCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
}
