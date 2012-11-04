package com.freshdirect.athena;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.athena.util.AthenaProperties;
import com.freshdirect.athena.util.UrlUtil;

public class DashboardServlet  extends HttpServlet {
	
	/**
	 * Log.
	 */
	private static final Logger LOGGER = Logger.getLogger(DashboardServlet.class);
	
	/**
	 * Init Athena.
	 * 
	 * @throws javax.servlet.ServletException
	 */
	@Override
	public void init() throws ServletException {
		super.init();
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
	    response.getWriter().println(getDashboard(urlInfo.getApi(), request.getQueryString()));
	}
	
		
	private String getDashboard(String dashboardId, String queryString) {
		
		StringBuffer result = new StringBuffer();
		
		result.append("<html>");
				result.append("<head>");
				
				result.append("<script type=\"text/javascript\" src=\"").append("/")
									.append(AthenaProperties.getDefaultXcelsiusRoot()).append("/js/").append("swfobject.js\"></script>");
				result.append("<script type=\"text/javascript\" src=\"").append("/")
									.append(AthenaProperties.getDefaultXcelsiusRoot()).append("/js/").append("swffit.js\"></script>");
				result.append("<script type=\"text/javascript\">");
				result.append("swffit.fit(\"FDXCelsiusDashboard\",800,600);");
				result.append("</script>");
				
				result.append("</head>");

				result.append("<body>");
				
				result.append("<object id=\"FDXCelsiusDashboard\" classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" width=\"100%\" height=\"100%\">");
				result.append("<param name=\"movie\" value=\"").append("/")
									.append(AthenaProperties.getDefaultXcelsiusRoot()).append("/")
											.append(dashboardId).append(".swf\" />");
				if(queryString!=null)
					result.append("<param name=FlashVars value=\"")
					.append(queryString).
					append("\" />");
			  
				result.append("<!--[if !IE]>-->");
				result.append("<object type=\"application/x-shockwave-flash\" data=\"").append("/")
									.append(AthenaProperties.getDefaultXcelsiusRoot()).append("/")
											.append(dashboardId).append(".swf\" width=\"100%\" height=\"100%\">");
				if(queryString!=null)
					result.append("<param name=FlashVars value=\"")
					.append(queryString).
					append("\" />");
				
				result.append("<!--<![endif]-->");
				
				result.append("<!--[if !IE]>-->");
				result.append("</object>");
				result.append("<!--<![endif]-->");
				result.append("</object>");
				
				result.append("</body>");
		result.append("</html>");

		
		
		
	    return result.toString();
	                
	}
}
