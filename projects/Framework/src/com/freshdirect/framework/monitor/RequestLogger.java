package com.freshdirect.framework.monitor;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class RequestLogger {
	private static final Logger LOGGER = LoggerFactory.getInstance(RequestLogger.class);

	public static void logRequest(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		StringBuilder buf = new StringBuilder();
		buf.append('"');
		buf.append(httpRequest.getRequestURI());
		String query = httpRequest.getQueryString();
		if (query != null) {
			buf.append('?');
			buf.append(query);
		}
		buf.append("\" \"");
		HttpSession session = httpRequest.getSession(false);
		if (session != null) {
			buf.append(session.getId());
		}
		buf.append('"');
		LOGGER.info(buf.toString());
	}
}
