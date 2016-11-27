/** 
 * RequestAProductTag.java
 * Created Dec 30, 2002
 */
package com.freshdirect.webapp.taglib.fdstore;

/**
 *  @author jangela
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

import com.freshdirect.fdstore.customer.FDCustomerRequest;

public class RequestAProductTag extends AbstractControllerTag {
	
	private final static String SUBJECT = "[WEB] Product Request";

	 protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {
	 	
	 	String actionName = this.getActionName();

	 	if ("sendEmail".equalsIgnoreCase(actionName)){
	 		
			String product1 = request.getParameter("product1");
			String product2 = request.getParameter("product2");
			String product3 = request.getParameter("product3");
			String department = request.getParameter("department");
			
			if ( (product1==null || "".equals(product1.trim())) &&
					(product2==null || "".equals(product2.trim())) &&
					(product2==null || "".equals(product2.trim())) ) {
				result.addError(new ActionError("error", "Please enter one product"));
				return true;
			}

			HttpSession session = pageContext.getSession();
			FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
			FDIdentity identity  = user.getIdentity();
			try {
				// FDCustomerManager.sendProductRequestEmail(identity, SUBJECT, body);
				
				FDCustomerRequest req = new FDCustomerRequest();
				req.setCaseSubjectCode(CrmCaseSubject.CODE_NEW_PRODUCT_REQUEST);
				
				if(identity != null)
					req.setCustomerID(identity.getErpCustomerPK());
				
				FDCustomerInfo info = FDCustomerManager.getCustomerInfo(identity);
				if(info != null)
					req.setCustomerEmail(info.getEmailAddress());
				req.setSubject(SUBJECT);

				req.setInfoLine1(department);
				req.setInfoLine2(product1);
				req.setInfoLine3(product2);
				req.setInfoLine4(product3);
				FDCustomerManager.storeCustomerRequest(req);
			} catch (FDResourceException ex) {
				ex.printStackTrace();
				throw new JspException(ex.getMessage());
			}
	 	}
	 	return true;

	 }



    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

}



