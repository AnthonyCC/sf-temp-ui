package com.freshdirect.fdstore.standingorders.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

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

			LOGGER.info("Standing order manual job started" + orders);
			
			lookupSOSHome();
			StandingOrderClientSB clientSB=soHome.create();
			boolean result=clientSB.runManualJob(orders, sendReportEmail!=null?Boolean.valueOf(sendReportEmail):false, sendReminderNotificationEmail!=null?Boolean.valueOf(sendReminderNotificationEmail):false);
			LOGGER.info("Standing order manual job runs successfully " + result);
			
			PrintWriter printWriter=response.getWriter();
			printWriter.append("Standing order job run successfully");
			printWriter.flush();
			
			
		} catch (Exception e) {
			LOGGER.error("Failed to run standing order cron job manually", e);
			invalidateSOSHome();
			sendError(response, "Standing order cron job fails ");
		}

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
}
