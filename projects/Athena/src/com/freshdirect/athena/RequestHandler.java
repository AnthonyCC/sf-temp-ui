package com.freshdirect.athena;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.freshdirect.athena.cache.CacheManager;
import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.config.ConfigManager;
import com.freshdirect.athena.config.Datasource;

public class RequestHandler {
	
	private static final Logger LOGGER = Logger.getLogger(RequestHandler.class);
	
	private Serializer serializer = new Persister();
	
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, UrlInfo urlInfo)  throws ServletException, IOException  {
				
		if(urlInfo.getApi() != null && CacheManager.getInstance().isSupportedApi(urlInfo.getApi())) {
			response.setContentType("text/xml");	
			try {
				LOGGER.debug("RequestHandler.handleApi() =>"+urlInfo.getApi());
				serializer.write(CacheManager.getInstance().getData(urlInfo.getApi(), urlInfo.getParams())
										,  response.getWriter());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ServletException();
			}
		} else {	
			
			response.setContentType("text/html");		    
		    response.getWriter().println(getIndexText());
		}
	}
	
	final String HTML_START = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
    "Transitional//EN\">\n" +
    "<html>\n" +
    "<head>" +
    "<title>Athena - FreshDirect Enterprise Integration  Platform</title>" +
    "<style type=\"text/css\">" +
    " body {" +
    " color: blue; " +
    " background-color: #CFCFCF } " +
    " table { margin: 1em; border-collapse: collapse; } " +
    " td, th { padding: .3em; border: 1px #ccc solid; } " +
    " thead { background: #fc9; } " +
    " tbody { background: #9cf; } " +
    " #highlight tr.hilight { background: #c9f; } " +
    " </style>" +
    "</head>\n" +
    "<body>\n" +
    "<h1><center>Welcome to Athena - FreshDirect Enterprise Integration  Platform</center></h1>\n" ;
	
	final String HTML_END = " </body></html>";
	
	private String getIndexText() {
		
		StringBuffer result = new StringBuffer();
		result.append(HTML_START).append("\n");
		result.append(HTML_END).append("\n");
		
		result.append("<table> <thead> <tr><th>Name</th><th>DataSource</th></tr> </thead>");
		result.append("<tbody>");
		for(Map.Entry<String, Datasource> dbPoolEntry : ConfigManager.getInstance().getDataSourceMapping().entrySet()) {
			result.append("<tr><td>").append(dbPoolEntry.getKey()).append("</td><td>")
						.append(dbPoolEntry.getValue().toString()).append("</td></tr>");
			
		}
		result.append("</tbody>");
		result.append("</table>");
		
		result.append("<table> <thead> <tr><th>Name</th><th>API</th></tr> </thead>");
		result.append("<tbody>");
		for(Map.Entry<String, Api> apiEntry : ConfigManager.getInstance().getServiceMapping().entrySet()) {
			result.append("<tr><td>").append(apiEntry.getKey()).append("</td><td>")
						.append(apiEntry.getValue().toString()).append("</td></tr>");
			
		}
		result.append("</tbody>");
		result.append("</table>");
		
	    return result.toString();
	                
	}
}
