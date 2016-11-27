package com.freshdirect.fdstore.cms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.cms.CMSPublishManager;


/**
 * Promo Publish Replica Service Point.
 * 
 * @author segabor
 *
 */
public class FeedPublishServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4693560915139423591L;
	private static final Logger LOGGER = Logger.getLogger(FeedPublishServlet.class);
	

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String feedData = request.getParameter("feedData");
		String feedId = request.getParameter("feedId");
		String storeId = request.getParameter("storeId");
		if(feedData != null){
			try{
				CMSPublishManager.createFeed(feedId, storeId, feedData );
			} catch(Exception e){
				LOGGER.error(e);
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String feedData = request.getParameter("feedData");
		String feedId = request.getParameter("feedId");
		String storeId = request.getParameter("storeId");
		if(feedData != null){
			try{
				CMSPublishManager.createFeed(feedId, storeId, feedData);
			} catch(Exception e){
				LOGGER.error(e);
			}
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
}