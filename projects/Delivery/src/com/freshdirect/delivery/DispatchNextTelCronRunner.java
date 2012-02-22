package com.freshdirect.delivery;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.delivery.ejb.AirclicManagerHome;
import com.freshdirect.delivery.ejb.AirclicManagerSB;
import com.freshdirect.delivery.model.AirclicNextelVO;
import com.freshdirect.delivery.model.DispatchNextTelVO;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.util.RoutingServicesProperties;

/**
 * @author kkanuganti
 *
 */
public class DispatchNextTelCronRunner {
	
	private final static Category LOGGER = LoggerFactory.getInstance(DispatchNextTelCronRunner.class);
	
	private static ThreadLocal<AirclicManagerHome> airclicHome = new ThreadLocal<AirclicManagerHome>();

	private static void lookupAirclickrHome() throws FDResourceException {
		if ( airclicHome.get() != null ) {
			return;
		}
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			airclicHome.set( (AirclicManagerHome) ctx.lookup("freshdirect.delivery.AirclicManager") );
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
	
	private static void invalidateAirclicHome() {
		airclicHome.set( null );
	}
	
	public static void main(String[] args) {
		
		LOGGER.info("DispatchNextTelCronRunner-started");		
		try {
			Date processDate = null;
			if (args.length >= 1) {
				for (String arg : args) {
					try { 
						if (arg.startsWith("processDate=")) {
							processDate = DateUtil.truncate(DateUtil.toCalendar(DateUtil.parse(arg.substring("processDate=".length())))).getTime();
						}						 
					} catch (Exception e) {
						System.err.println("Usage: java com.freshdirect.dataloader.airclic.DispatchNextTelCronRunner [processDate={date value}]");
						System.exit(-1);
					}
				}
			}
			if(processDate == null){
				processDate = DateUtil.truncate(Calendar.getInstance()).getTime();
			}			
			if(!ErpServicesProperties.isAirclicBlackhole())	{				
				 //processNextelDataSyncOld(processDate);
				 processNextelDataSync(processDate);
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));			
			LOGGER.info(new StringBuilder("DispatchNextTelCronRunner failed with Exception...").append(sw.toString()).toString());
			LOGGER.error(sw.toString());
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());		
		}
		LOGGER.info("DispatchNextTelCronRunner-finished");
	}
	
	/**
	 * @param processDate
	 * @throws Exception
	 */
	private static void processNextelDataSyncOld(Date processDate) throws Exception {
		try {
			lookupAirclickrHome();
			AirclicManagerSB sb = airclicHome.get().create();
			
			LOGGER.info( "Starting to sync dispatch resource nexttel data" );
			
			Map<String, AirclicNextelVO> nextTelMapping = sb.getNXOutScan(processDate);
			Map<String, String> nextTelAssetMapping = sb.getNextTelAssets();
			Map<String, DispatchNextTelVO> dispatchNexTelMapping = sb.getDispatchResourceNextTel(processDate);
			
			List<String> noCNLst = new ArrayList<String>();
			for(Map.Entry<String, AirclicNextelVO> nextTelEntry : nextTelMapping.entrySet()){
				AirclicNextelVO _vo = nextTelEntry.getValue();
				_vo.setCnNo(nextTelAssetMapping.get(_vo.getNextTelNo()) != null ? nextTelAssetMapping.get(_vo.getNextTelNo()) : null);
				if(nextTelAssetMapping.get(_vo.getNextTelNo()) == null){
					noCNLst.add(_vo.getNextTelNo());
				}
			}
			
			List<DispatchNextTelVO> updateResourceNexTelLst = new ArrayList<DispatchNextTelVO>();
			List<DispatchNextTelVO> noNextelDataLst = new ArrayList<DispatchNextTelVO>();

			for (Map.Entry<String, DispatchNextTelVO> resourceEntry : dispatchNexTelMapping.entrySet()) {
				if (nextTelMapping.containsKey(resourceEntry.getKey())) {
					DispatchNextTelVO _resourceNexTel = resourceEntry.getValue();
					AirclicNextelVO _airclicNexTelInfo = nextTelMapping.get(resourceEntry.getKey());
					if (_airclicNexTelInfo.getCnNo() != null){
						_airclicNexTelInfo.setCnNo(_airclicNexTelInfo.getCnNo().replaceAll("[a-zA-Z+]", ""));
						if(!_airclicNexTelInfo.getCnNo().equalsIgnoreCase(_resourceNexTel.getNextTelNo())) {
							_resourceNexTel.setNextTelNo(_airclicNexTelInfo.getCnNo());
							updateResourceNexTelLst.add(_resourceNexTel);
						}
					}
				} else if (resourceEntry.getValue().getNextTelNo() == null){
					noNextelDataLst.add(resourceEntry.getValue());
				}
			}
			LOGGER.info( "Updating dispatch resource nexttel data >> "+ updateResourceNexTelLst.size() + " count.");
			if(updateResourceNexTelLst.size() > 0) {				
				sb.updateEmployeeNexTelData(updateResourceNexTelLst);
			}
			sendReportMail(processDate, updateResourceNexTelLst, noCNLst, noNextelDataLst);
			LOGGER.info( "Finished syncing dispatch resource nexttel data." );			
			
		} catch ( CreateException e ) {
			invalidateAirclicHome();
			throw new Exception(e);			
		} catch ( RemoteException e ) {
			invalidateAirclicHome();
			throw new Exception(e);
		} catch (DlvResourceException e) {				
			e.printStackTrace();
		} catch ( FDResourceException e ) {
			invalidateAirclicHome();
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	/**
	 * @param processDate
	 * @throws Exception
	 */
	private static void processNextelDataSync(Date processDate) throws Exception {
		try {
			lookupAirclickrHome();
			AirclicManagerSB sb = airclicHome.get().create();
			
			LOGGER.info( "Starting to sync dispatch resource nexttel data" );
			
			Map<String, AirclicNextelVO> nextTelMapping = sb.getNXOutScan(processDate);
			
			Map<String, DispatchNextTelVO> dispatchNexTelMapping = sb.getDispatchResourceNextTel(processDate);			
			
			List<DispatchNextTelVO> updateResourceNexTelLst = new ArrayList<DispatchNextTelVO>();
			List<DispatchNextTelVO> noNextelDataLst = new ArrayList<DispatchNextTelVO>();

			for (Map.Entry<String, DispatchNextTelVO> resourceEntry : dispatchNexTelMapping.entrySet()) {
				if (nextTelMapping.containsKey(resourceEntry.getKey())) {
					DispatchNextTelVO _resourceNexTel = resourceEntry.getValue();
					AirclicNextelVO _airclicNexTelInfo = nextTelMapping.get(resourceEntry.getKey());
					if (_airclicNexTelInfo.getNextTelNo() != null){						
						if(!_airclicNexTelInfo.getNextTelNo().equalsIgnoreCase(_resourceNexTel.getNextTelNo())) {
							_resourceNexTel.setNextTelNo(_airclicNexTelInfo.getNextTelNo());
							updateResourceNexTelLst.add(_resourceNexTel);
						}
					}
				} else if (resourceEntry.getValue().getNextTelNo() == null){
					noNextelDataLst.add(resourceEntry.getValue());
				}
			}
			LOGGER.info( "Updating dispatch resource nexttel data >> "+ updateResourceNexTelLst.size() + " count.");
			if(updateResourceNexTelLst.size() > 0) {				
				sb.updateEmployeeNexTelData(updateResourceNexTelLst);
			}
			sendReportMail(processDate, updateResourceNexTelLst, null, noNextelDataLst);
			LOGGER.info( "Finished syncing dispatch resource nexttel data." );			
			
		} catch ( CreateException e ) {
			invalidateAirclicHome();
			throw new Exception(e);			
		} catch ( RemoteException e ) {
			invalidateAirclicHome();
			throw new Exception(e);
		} catch (DlvResourceException e) {				
			e.printStackTrace();
		} catch ( FDResourceException e ) {
			invalidateAirclicHome();
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	
	private static void sendReportMail(Date processDate,List<DispatchNextTelVO> updateResourceNexTelLst, List<String> noCNLst, List<DispatchNextTelVO> noNextelDataLst ) {
	
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="Dispatch Resource Handheld Synchronizer Cron : "+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");
			buff.append("<h2>").append("DispatchHandheldCronRunner synchronized "+(updateResourceNexTelLst != null ? updateResourceNexTelLst.size() : "0")
					+" resource handhelds for date "+(processDate != null ? dateFormatter.format(processDate) : " date error")).append("</h2>");
			
			/*if(noNextelDataLst != null && noNextelDataLst.size() > 0){
				buff.append("<table border=\"1\" valign=\"top\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
				buff.append("<tr>").append("<th>").append("Employee").append("</th>").append("</tr>");
				Iterator<DispatchNextTelVO> itr = noNextelDataLst.iterator();
				while(itr.hasNext()){
					DispatchNextTelVO _nextelVO = itr.next();
					buff.append("<tr>").append("<td>").append(_nextelVO.getEmployeeId()).append("</td>").append("</tr>");				
				}
				buff.append("</table>");
			}*/
			if(updateResourceNexTelLst != null && updateResourceNexTelLst.size() > 0){
				buff.append("<table border=\"1\" valign=\"top\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
				buff.append("<tr>").append("<th>").append("Employee").append("</th>").append("<th>").append("PhoneNumber").append("</th>").append("</tr>");
				Iterator<DispatchNextTelVO> itr = updateResourceNexTelLst.iterator();
				while(itr.hasNext()){
					DispatchNextTelVO _nextelVO = itr.next();
					buff.append("<tr>").append("<td>").append(_nextelVO.getEmployeeId()).append("</td>").append("<td>").append(_nextelVO.getNextTelNo()).append("</td>").append("</tr>");				
				}
				buff.append("</table>");
			}
			
			if(noCNLst != null && noCNLst.size() > 0){
				buff.append("&nbsp;&nbsp;&nbsp;<table border=\"1\" valign=\"top\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">");
				buff.append("<tr>").append("<th>").append("Handheld(s) with no matching CN(s)").append("</br>").append(" in Transp Asset list").append("</th>").append("</tr>");			
				Iterator<String> itr = noCNLst.iterator();
				while(itr.hasNext()){
					String _nextel = itr.next();
					buff.append("<tr>").append("<td>").append(_nextel).append("</td>").append("</tr>");				
				}
				buff.append("</table>");
			}
			
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(RoutingServicesProperties.getRoutingSubscriptionMailFrom(),
					RoutingServicesProperties.getRoutingSubscriptionMailTo(),
					RoutingServicesProperties.getRoutingSubscriptionMailCC(), subject, buff.toString(), true, "");
			
		} catch (MessagingException e) {
			LOGGER.warn("Error Sending Standing Order cron report email: ", e);
		}
	}
	
	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="DispatchNextTelDataSyncCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

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
			LOGGER.warn("Error Updating Dispatch Nexttel DataSync Cron report email: ", e);
		}
		
	}

}
