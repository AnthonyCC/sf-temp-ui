package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ExternalAccountControllerTag extends
		com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static final long serialVersionUID = -7466852553179595103L;
	private static Category LOGGER = LoggerFactory.getInstance(ExternalAccountControllerTag.class);
	private Object result;
	
	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		
		HttpSession session = this.pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		String source = (request.getParameter("source") == null) ? "" : (String)request.getParameter("source");
	
		String redirectPage = AccountServiceFactory.getService(source).login(session, request, response);
		
		if(!FDStoreProperties.isLocalDeployment()){
			if(null != redirectPage && !redirectPage.contains("https")) {
				redirectPage = redirectPage.replace("http", "https");
			} 
		}


		doRedirect(redirectPage);
		
		return SKIP_BODY;
		
	}

	
	private int doRedirect(String url) throws JspException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			response.sendRedirect(response.encodeRedirectURL(url));
			//JspWriter writer = pageContext.getOut();
			//writer.close();
			return SKIP_BODY;
			
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
