package com.freshdirect.framework.monitor;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class RequestLogger {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(RequestLogger.class);
	
	private static final String EMPTY = "";
	private static final String SEPARATOR = "|";
	private static final String FDCOOKIE = "FDUser";
	private static final String NSCOOKIE = "NSC_";
	private static final String IPHEADER = "X-Forwarded-For";
	private static final String AKAMAI_IPHEADER = "True-Client-IP";
	private static final String USER_AGENT = "User-Agent";
	
	public static void logRequest(ServletRequest request) {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		StringBuffer buf = new StringBuffer();
		buf.append(SEPARATOR);//Start
		buf.append(httpRequest.getRequestURI());
	
		String query = httpRequest.getQueryString();
		if (query != null) {
			buf.append('?');
			buf.append(query);
		}
		
		buf.append(SEPARATOR);
		HttpSession session = httpRequest.getSession(false);
		if (session != null) {
			buf.append(session.getId());
		}
		
		buf.append(SEPARATOR);
		buf.append(getServerName());
				
		buf.append(SEPARATOR);
		buf.append(getClientIp(httpRequest));
		
		buf.append(SEPARATOR);
		buf.append(getHttpHeader(httpRequest, AKAMAI_IPHEADER));
		
		buf.append(SEPARATOR);
		buf.append(getHttpHeader(httpRequest, USER_AGENT));
				
		buf.append(SEPARATOR);
		buf.append(getCookies(httpRequest));
		
		buf.append(SEPARATOR); //End
		LOGGER.info(buf.toString());
	}
	
	public static String getCookies(HttpServletRequest request) {
		
		String _fdUser = null;
		String _nsUser = null;
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {			
			
			for (int i = 0; i < cookies.length; i++) {
				
				if(cookies[i] != null && cookies[i].getName() != null) {
					
					if (FDCOOKIE.equals(cookies[i].getName())) {
						_fdUser = cookies[i].getValue();
					} else if (cookies[i].getName().startsWith(NSCOOKIE)) {
						_nsUser = cookies[i].getValue();
					}
				}
			}
		}
		return (_fdUser != null ? _fdUser : EMPTY) + SEPARATOR + (_nsUser != null ? _nsUser : EMPTY);
	}
	
	public static String getHttpHeader(HttpServletRequest request, String headerName) {
		return request.getHeader(headerName) != null ? request.getHeader(headerName) : EMPTY;
	}
	
	public static String getClientIp(HttpServletRequest request) {
		
		String ip = null;
		String xffHeader = request.getHeader(IPHEADER);
		
		if (xffHeader != null){
			ip = xffHeader.substring(xffHeader.lastIndexOf(",")+1).trim();
		}
		
		if (ip==null || ip.length()==0){
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static String getServerName() {
		
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return EMPTY;
	}
}
