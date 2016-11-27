/**
 * 
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;


public class RobinHoodControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance(RobinHoodControllerTag.class);
	private String rhSubmitOrderPage = "/robin_hood/rh_submit_order.jsp";

	FDUser user = null;
	
	// Var's declared in the TLD for this tag
    String actionName = null;
    String successPage = null;
    String resultName = null;
    
    
    public String getSuccessPage() {
        return this.successPage;
    }

    public void setSuccessPage(String sp) {
        this.successPage = sp;
    }

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    
    public String getResultName() {
        return this.resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	        ActionResult result = new ActionResult();

	        HttpSession session = pageContext.getSession();
	        FDSessionUser fs_user = (FDSessionUser)session.getAttribute(USER);
	        FDCartI cart = fs_user.getDonationCart();
	        user = fs_user.getUser();
	        
	        if (("POST".equalsIgnoreCase(request.getMethod()))) {
	        	if("checkout".equalsIgnoreCase(actionName)) {
	        		String quantity = request.getParameter("quantity");
	        		//TODO: Validate the quantity to be >=1.
	        		user.setDonationTotalQuantity(Integer.parseInt(quantity));
                	setSuccessPage(rhSubmitOrderPage);
                }
	        	
	        	 if (result.getErrors().isEmpty() && (successPage != null)) {
	                 LOGGER.debug("Success, redirecting to: "+successPage);
	                 HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
	                 try {
	                     response.sendRedirect(response.encodeRedirectURL(successPage));
	                     JspWriter writer = pageContext.getOut();
	                     writer.close();
	                 } catch (IOException ioe) {
	                     //
	                     // if there was a problem redirecting, continue and evaluate/skip tag body as usual
	                     //
	                     LOGGER.warn("IOException during redirect", ioe);
	                 }
	             }
	        } else  if (("GET".equalsIgnoreCase(request.getMethod()))) {
	        	//TODO: Determine which landing page it should go to.
	        }
	        
	        pageContext.setAttribute(resultName, result);
	        return EVAL_BODY_BUFFERED;
	}
    
    
}
