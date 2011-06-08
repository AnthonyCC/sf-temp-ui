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
import java.util.Set;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.util.RoutingServicesProperties;


public class StandingOrdersServiceCmd {

	private static final Logger LOGGER = LoggerFactory.getInstance(StandingOrdersServiceCmd.class);
	private static FDStandingOrdersManager	soManager			= FDStandingOrdersManager.getInstance();	
	
	
	public static void main( String[] args ) {
		
		
		
		boolean isSendEmail = false;
		List<String> soList = null;
		try{
			if (args.length >= 1) {
				for (String arg : args) {
					try { 
						if (arg.startsWith("orders=")) {
							String orders=arg.substring("orders=".length());
							if(null !=orders && !orders.trim().equalsIgnoreCase("")){
								String[] order = orders.split(",");								
								Set<String> soSet = new HashSet<String>(Arrays.asList(order));
								soList = new ArrayList<String>(soSet);
								
							}
						}  else if (arg.startsWith("sendEmail=")) {								
							isSendEmail = Boolean.valueOf(arg.substring("sendEmail=".length())).booleanValue(); 
						}
					} catch (Exception e) {
						System.err.println("Usage: java com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceCmd [orders=(,) separated}] [sendEmail={true | false}]");
						System.exit(-1);
					}
				}
			}
			//placeStandingOrders();
//			soList=new ArrayList<String>(10) {{add("123");add("2201264415");}};
//			placeStandingOrders(soList);
			if(null != soList && !soList.isEmpty()){
				placeStandingOrders(soList);
			}else{
				placeStandingOrders();
			}
			if(isSendEmail){
				sendReportMail();
			}
		}catch(Exception e){
			LOGGER.info(new StringBuilder("StandingOrdersServiceCmd failed with Exception...").append(e.toString()).toString());
			LOGGER.error(e);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
		}
	}
		

	private static void sendReportMail() {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			Date date = Calendar.getInstance().getTime();
			String subject=FDStoreProperties.getStandingOrderReportEmailSubject()+ (date != null ? " "+dateFormatter.format(date) : " date error");
			StringBuffer buffer = new StringBuffer();
			buffer.append("<html>").append("<body>");
			FDStandingOrderInfoList soFailedList = (FDStandingOrderInfoList)soManager.getFailedStandingOrdersCustInfo();
			FDStandingOrderInfoList soMechanicalFailedList = (FDStandingOrderInfoList)soManager.getMechanicalFailedStandingOrdersCustInfo();
			if(null != soFailedList){
				List<FDStandingOrderInfo> soFailedInfoList = soFailedList.getStandingOrdersInfo();
				buffer.append("<br/><br/><br/>");
				buffer.append("<b>Failed Standing Orders: "+soFailedInfoList.size()+"</b><br/><br/>");
				buffer.append("<table border=\"1\" valign=\"top\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
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
				
				for (Iterator iterator = soFailedInfoList.iterator(); iterator
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
			}
			if(null != soMechanicalFailedList){
				List<FDStandingOrderInfo> soMecFailedInfoList = soMechanicalFailedList.getStandingOrdersInfo();
				buffer.append("<br/><br/><br/>");
				buffer.append("<b>Mechanical Failures: "+soMecFailedInfoList.size()+"</b><br/><br/>");
				buffer.append("<table border=\"1\" valign=\"top\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
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
				
				for (Iterator iterator = soMecFailedInfoList.iterator(); iterator
						.hasNext();) {
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
			}
			
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(FDStoreProperties.getCustomerServiceEmail(),
					FDStoreProperties.getStandingOrderReportToEmail(),
					FDStoreProperties.getStandingOrderReportToEmail(),
					subject, buffer.toString(), true, "");
		} catch (FDResourceException e) {
			LOGGER.warn("Error getting failed standing orders: ", e);
		} catch (MessagingException e) {
			LOGGER.warn("Error Sending Standing Order cron report email: ", e);
		}
		
	}


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

	private static void placeStandingOrders() {
		try {
			lookupSOSHome();
			StandingOrdersServiceSB sb = sosHome.get().create();
			
			LOGGER.info( "Starting to place orders..." );
			
			StandingOrdersServiceResult.Counter result = sb.placeStandingOrders();
			
			LOGGER.info( "Finished placing orders." );
			LOGGER.info( "  success : " + result.getSuccessCount() );
			LOGGER.info( "  failed  : " + result.getFailedCount() );
			LOGGER.info( "  skipped : " + result.getSkippedCount() );
			LOGGER.info( "  total   : " + result.getTotalCount() );
			
		} catch ( CreateException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		} catch ( RemoteException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		} catch ( FDResourceException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		}
	}
	private static void placeStandingOrders(Collection<String> soList) {
		try {
			lookupSOSHome();
			StandingOrdersServiceSB sb = sosHome.get().create();
			
			LOGGER.info( "Starting to place orders..." );
			
			StandingOrdersServiceResult.Counter result = sb.placeStandingOrders(soList);
			
			LOGGER.info( "Finished placing orders." );
			LOGGER.info( "  success : " + result.getSuccessCount() );
			LOGGER.info( "  failed  : " + result.getFailedCount() );
			LOGGER.info( "  skipped : " + result.getSkippedCount() );
			LOGGER.info( "  total   : " + result.getTotalCount() );
			
		} catch ( CreateException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		} catch ( RemoteException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		} catch ( FDResourceException e ) {
			invalidateSOSHome();
			e.printStackTrace();
		}
	}

	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
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
