package com.freshdirect.webapp.taglib;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;

public abstract class AbstractControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static final long	serialVersionUID	= -9139816225596448770L;

	private static Category LOGGER = LoggerFactory.getInstance(AbstractControllerTag.class);

	

	
	private String actionName;
	private String successPage;
	private String result;
	private boolean ajax = false;

	public String getSuccessPage() {
		return successPage;
	}

	public void setSuccessPage(String successPage) {
		if (successPage != null) {
			int schemeDelimiterPos = successPage.indexOf("://");
			int parameterDelimiterPos = successPage.indexOf("?");
			if (schemeDelimiterPos !=-1 && schemeDelimiterPos < parameterDelimiterPos ) {
					LOGGER.debug("successPage before throwing IllegalArgument Exception :"+successPage);
					throw new IllegalArgumentException("Invalid successPage specified");
			}
		}
		
		this.successPage = successPage;
	}
	
	public void setSuccessPage(String successPage, boolean isFullPath) {		
		this.successPage = successPage;
	}	

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setResult(String resultName) {
		this.result = resultName;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public boolean isAjax() {
		return ajax;
	}

	public int doStartTag() throws JspException {
		//
		// perform any actions requested by the user if the request was a POST
		//
		ActionResult actionResult = new ActionResult();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		if ("POST".equalsIgnoreCase(request.getMethod()) || "ordermobilepref".equals(request.getParameter("actionName"))|| "ordersmsalerts".equals(request.getParameter("actionName"))) {
			boolean proceed = this.performAction(request, actionResult);
			if (!proceed) {
				return SKIP_BODY;
			}

			//
			// redirect to success page if an action was successfully performed
			// and a success page was defined
			//
			if (actionResult.isSuccess() && (successPage != null)) {
				if (ajax) {
					LOGGER.debug("Skipping redirect, eval body");
				} else {
					LOGGER.debug("Success, redirecting to: " + successPage);
					
					// enforce https
					if(!FDStoreProperties.isLocalDeployment()){
						if(!successPage.contains("https")) {
							successPage = successPage.replace("http", "https");
						} 
					}				
					
					this.redirectTo(successPage);
					return SKIP_BODY;
				}
			}
		}else if ("GET".equalsIgnoreCase(request.getMethod())) {
			boolean proceed = this.performGetAction(request, actionResult);
			if (!proceed) {
				return SKIP_BODY;
			}			
		}
		//
		// place the result as a scripting variabl1e in the page
		//
		pageContext.setAttribute(this.result, actionResult);
		// for AJAX login
		if (ajax)
			pageContext.getRequest().setAttribute("fd_successPage", getSuccessPage());
		return EVAL_BODY_BUFFERED;

	}

	protected void redirectTo(String destination) throws JspException {
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			response.sendRedirect(response.encodeRedirectURL(destination));
			//JspWriter writer = pageContext.getOut();
			//writer.close();
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		} finally {
        	try {
				pageContext.getOut().close();
			} catch (IOException e) {
	            throw new JspException(e.getMessage());
			}
		}
	}

	protected void forward(String destination) throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		try {
			 RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
			 if (dispatcher != null)
		         dispatcher.forward(request, response);
			 
		} catch (ServletException se) {
			throw new JspException(se.getMessage());
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}
	/**
	 * @return false to SKIP_BODY without redirect
	 */
	protected abstract boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException;
	
	/**
	 * template method to handle get request if need be
	 */
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		return true;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				 new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED)};
		}
	}

}
