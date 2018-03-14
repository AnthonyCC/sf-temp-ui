package com.freshdirect.fdstore.standingorders.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.MessagingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.webapp.util.StandingOrderUtil;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;

/**
 
 *
 */
public class StandingOrderCronServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2041568217259087086L;

	private static Category LOGGER = LoggerFactory.getInstance(StandingOrderCronServlet.class);
	private static StandingOrderClientHome soHome = null;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String orders=request.getParameter("orders"); 
			String sendReportEmail=request.getParameter("sendReportEmail");
			String sendReminderNotificationEmail=request.getParameter("sendReminderNotificationEmail");
			
			String placeStandingOrder=request.getParameter("placeStandingOrder");
			String standingOrderId = request.getParameter("standingOrderId");
			String altDateStr = request.getParameter("altDate");
			String startTimeStr = request.getParameter("startTime");
			String endTimeStr = request.getParameter("endTime");
			String initiator = request.getParameter("initiator");

			LOGGER.info("Standing order manual job started" + orders);
			if(placeStandingOrder == null){
				lookupSOSHome();
				StandingOrderClientSB clientSB=soHome.create();
				boolean result=clientSB.runManualJob(orders, sendReportEmail!=null?Boolean.valueOf(sendReportEmail):false, sendReminderNotificationEmail!=null?Boolean.valueOf(sendReminderNotificationEmail):false);
				LOGGER.info("Standing order manual job runs successfully " + result);
				
				PrintWriter printWriter=response.getWriter();
				printWriter.append("Standing order job run successfully");
				printWriter.flush();
			}else{
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				
				Date altDate = formatter.parse(altDateStr);
				Date startTime = formatter.parse(startTimeStr);
				Date endTime = formatter.parse(endTimeStr);
				
				
				
				SOResult.Result result = placeStandingOrder(standingOrderId, altDate, startTime, endTime, initiator);
				
				PrintWriter printWriter=response.getWriter();
				if(result!=null){
					printWriter.append(result.getErrorHeader());
				}else{
					printWriter.append("Standing order created successfully");
				}
				printWriter.flush();
			}
			
			
		} catch (Exception e) {
			LOGGER.error("Failed to run standing order cron job manually", e);
			invalidateSOSHome();
			invalidateMailerHome();
			sendError(response, "Standing order cron job fails ");
		}

	}

	private SOResult.Result placeStandingOrder(String standingOrderId, Date altDate, Date startTime, Date endTime, String initiator) {

		lookupMailerHome();
		
		if (standingOrderId == null) {
			LOGGER.error("Empty standingOrderId passed.");
			sendTechnicalMail("Empty standingOrderId passed.");
			return null;
		}
		TimeslotEvent event = new TimeslotEvent(EnumTransactionSource.SYSTEM.getCode(), false, 0.00, false, false, null, EnumCompanyCode.fd.name());		
		
			
		LOGGER.info( "Processing " + standingOrderId + " standing orders." );
		
		SOResult.Result result;
		FDStandingOrder so = null;
		
			try {
				so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(standingOrderId));
				FDActionInfo info = getActionInfo(initiator);
				if(info.getIdentity() == null)info.setIdentity(so.getCustomerIdentity());
				if(null != altDate){
					FDStandingOrderAltDeliveryDate altDateInfo = new FDStandingOrderAltDeliveryDate();
					altDateInfo.setOrigDate(so.getNextDeliveryDate());
					altDateInfo.setAltDate(altDate);
					so.setStartTime(startTime);
					so.setEndTime(endTime);
					so.clearLastError();
					result = StandingOrderUtil.process( so, altDateInfo, event, info, mailerHome, true, true, false );
				}else{
					LOGGER.info( "Alternate date for standing order # " + standingOrderId + " missing." );
					return null;
				}
			} catch (FDResourceException re) {
				invalidateMailerHome();
				LOGGER.error( "Processing standing order failed with FDResourceException!", re );
				result = SOResult.createTechnicalError( so, "Processing standing order failed with FDResourceException!" );
			}
			
			if ( result.isError() ) {
				if ( result.isTechnicalError() && result.isSendErrorEmail() ) {
					// technical error
					sendTechnicalMail( result.getErrorDetail() );
				}
			}
			logActivity( so, result, initiator );

		return result;
	
		
	}

	protected void sendError(HttpServletResponse response, String message) {
		//
		try {
			response.getWriter().append(message);
			response.setStatus(500);
		} catch (IOException e) {
			LOGGER.error("Failed to append message to response", e);
		}
	}

	private static void lookupSOSHome() throws NamingException {
		
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			soHome= (StandingOrderClientHome)ctx.lookup("freshdirect.fdstore.SOClient") ;
		} catch (NamingException ne) {
			throw ne;			
		} catch (Exception ne) {
			LOGGER.error("unable to lookup standing order client ejb",ne);
			throw new NamingException("unable to lookup standing order client ejb ");
		}finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.error("unable to lookup standing order client ejb",ne);
			}
		}
	}
	
	private static void invalidateSOSHome() {
		soHome=null;
	}
	private static MailerGatewayHome mailerHome	= null;
	
	private static void invalidateMailerHome() {
		mailerHome = null;
	}
	
	private static void lookupMailerHome() {
		if (mailerHome != null) {
			return;
		}		
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			mailerHome = (MailerGatewayHome) ctx.lookup( "freshdirect.mail.MailerGateway" );
		} catch ( NamingException ne ) {
			ne.printStackTrace();
		} finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch ( NamingException ne ) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}
	
private void logActivity ( FDStandingOrder so, SOResult.Result result, String initiator ) {
		
		ErpActivityRecord activityRecord = new ErpActivityRecord();
		activityRecord.setStandingOrderId( so.getId() );
		activityRecord.setCustomerId( so.getCustomerId() );
		activityRecord.setDate( new Date() );
		activityRecord.setInitiator( initiator );
		activityRecord.setSource( EnumTransactionSource.SYSTEM );
		if (result.getErrorHeader() != null || result.getErrorDetail() != null) {
			String note;
			if (result.getErrorHeader() != null) {
				if (result.getErrorDetail() != null)
					note = result.getErrorHeader() + ";" + result.getErrorDetail();
				else
					note = result.getErrorHeader();
			} else {
				if (result.getErrorDetail() != null)
					note = result.getErrorDetail();
				else
					note = null;
			}
			activityRecord.setNote( note );
		}
		activityRecord.setChangeOrderId( result.getSaleId() );
		
		SOResult.Status status = result.getStatus();
		
		if ( status == SOResult.Status.SUCCESS ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_PLACED );
		} else if ( status == SOResult.Status.FAILURE ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_FAILED );
		} else if ( status == SOResult.Status.SKIPPED ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_SKIPPED );					
		} /*else if ( status == SOResult.Status.FORCED_SKIPPED ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_FORCED_SKIPPED );					
		}*/
		if(FDStoreProperties.isSF2_0_AndServiceEnabled("customer.ejb.ActivityLogSB")){
			try {
				FDECommerceService.getInstance().logActivity(activityRecord);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
		new ErpLogActivityCommand( activityRecord ).execute();	
		}
	}
	
	private void sendTechnicalMail ( String msg ) {
		try {
						
			ErpMailSender mailer = new ErpMailSender();
			
			String from = ErpServicesProperties.getStandingOrdersTechnicalErrorFromAddress();
			String recipient = ErpServicesProperties.getStandingOrdersTechnicalErrorRecipientAddress();
			String subject = "TECHNICAL ERROR occurred in the Standing Orders background service!";
			
			StringBuilder message = new StringBuilder(); 
			message.append( "Standing Orders background process failed!\n" );
			
			message.append( "message: " );
			message.append( msg );
			message.append( '\n' );
			
			message.append( " Please check the server log for more details...\n" );
			
			message.append( "timestamp: " );
			message.append( new Date() );
			message.append( '\n' );
			
			try {
				message.append( "ip: " );
				message.append( java.net.InetAddress.getLocalHost().getHostAddress() );
				message.append( '\n' );
				message.append( "host: " );
				message.append( java.net.InetAddress.getLocalHost().getCanonicalHostName() );
				message.append( '\n' );
			} catch ( UnknownHostException e ) {
				message.append( "host is unknown" );				
			}
			
			mailer.sendMail(from, recipient, "", subject, message.toString() );
			
		} catch ( MessagingException e ) {
			LOGGER.error( "Failed to send out technical error report email!" );
			e.printStackTrace();
		}
	}


	private FDActionInfo getActionInfo(String agent){
		
		EnumTransactionSource src = EnumTransactionSource.SYSTEM;
		CrmAgentModel crmAgent = new CrmAgentModel();
		crmAgent.setLdapId(agent);
		FDActionInfo info = new FDActionInfo(src, null, agent, null, crmAgent, null);
		
		return info;
	}
}
