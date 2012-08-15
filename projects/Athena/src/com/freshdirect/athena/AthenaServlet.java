package com.freshdirect.athena;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.athena.util.UrlUtil;

public class AthenaServlet  extends HttpServlet {

	/**
	 * Log.
	 */
	private static final Logger LOGGER = Logger.getLogger(AthenaServlet.class);
		
	private RequestHandler requestHandler = null;
	/**
	 * Init Athena.
	 * 
	 * @throws javax.servlet.ServletException
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		requestHandler = new RequestHandler();
	}

	/**
	 * Parses the request to get information about what controller is trying to call, then
	 * invoke the action from that controller (if any), and finally gives an answer.<br>
	 * <br>
	 * Basically it only dispatches the request to a controller.
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = request.getRequestURI().substring(request.getContextPath().length());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("url => "+ url);
			LOGGER.debug("HTTP_METHOD => "+ request.getMethod());
			LOGGER.debug("queryString => "+ request.getQueryString());
			LOGGER.debug("Context => "+ request.getContextPath());
		}

		UrlInfo urlInfo = UrlUtil.getUrlInfo(url);
		LOGGER.debug("UrlInfo => "+ urlInfo);	
		requestHandler.handleRequest(request, response, urlInfo);
	}
	
	
}