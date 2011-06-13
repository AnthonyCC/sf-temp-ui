package com.freshdirect.mktAdmin.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.service.MarketAdminAutoUploadHandler;

public class MarketAdminAutoUploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5520924612941043566L;
	private static Category LOGGER = LoggerFactory.getInstance(MarketAdminAutoUploadServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		LOGGER.info("Inside MarketAdminAutoUploadServlet.");
		final String userAgent = request.getHeader("User-Agent");
		if (!"PromoCustAutoUpload/1.0".equalsIgnoreCase(userAgent)) {
			LOGGER.error("Invalid user agent " + userAgent);
			// Invalid user agent
			sendError(response, "Sorry..");
			return;
		}
		MarketAdminAutoUploadHandler.doFetchAndUpload();
		LOGGER.info("Finished MarketAdminAutoUploadServlet.");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
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
}

