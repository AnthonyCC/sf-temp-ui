package com.freshdirect.fdstore.silverpopup.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.customer.SilverPopupDetails;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerHome;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.silverpopup.util.FDIBMPushNotification;
import com.freshdirect.framework.util.log.LoggerFactory;

/*
 *	@Author Vamsi Krishna
 */
public class SilverPopupCronServlet extends HttpServlet {

	private static final long serialVersionUID = 2041568217259087086L;

	private static Category LOGGER = LoggerFactory.getInstance(SilverPopupCronServlet.class);
	private static FDCustomerManagerHome fcHome = null;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			lookupSPSHome();
			FDCustomerManagerSB clientSB = fcHome.create();
			List<SilverPopupDetails> result = clientSB.getSilverPopupDetails();
			LOGGER.info("FDCustomer manager silver popup retrieved successfully " + result);
			if (null != result && !result.isEmpty()) {
				LOGGER.debug("Before calling IBM silverpopup ");
				FDIBMPushNotification service = new FDIBMPushNotification();
				for (SilverPopupDetails detail : result) {
					if(service.execute(detail)){
					clientSB.updateSPSuccessDetails(detail);
					}
				}
								
			} else {
				LOGGER.debug("There are no details are available to call notificaiton");
			}
			if(response != null && null != response.getWriter()){
			PrintWriter printWriter = response.getWriter();
			printWriter.append("FDCustomer manager job run successfully " );
			printWriter.flush();
				}
		} catch (Exception e) {
			LOGGER.error("Failed to run SilverPopupCronServlet cron job manually", e);
			invalidateFCHome();
			sendError(response, "Silver  cron job fails ");
		}

	}

	protected void sendError(HttpServletResponse response, String message) {
		try {
			response.getWriter().append(message);
			response.setStatus(500);
		} catch (IOException e) {
			LOGGER.error("Failed to append message to response", e);
			}
	}

	private static void lookupSPSHome() throws NamingException {
		
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			 fcHome = (FDCustomerManagerHome) ctx.lookup("freshdirect.fdstore.CustomerManager");
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
	
	private static void invalidateFCHome() {
		fcHome=null;
	}
}
