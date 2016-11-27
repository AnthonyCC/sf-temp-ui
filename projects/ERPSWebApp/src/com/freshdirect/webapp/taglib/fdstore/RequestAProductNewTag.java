/** 
 * RequestAProductNewTag.java
 * Created Dec 30, 2002
 */
package com.freshdirect.webapp.taglib.fdstore;

/**
 *  @author batchley
 */
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.request.FDProductRequest;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class RequestAProductNewTag extends AbstractControllerTag {

	private static Category LOGGER = LoggerFactory.getInstance(RequestAProductNewTag.class);

	 protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {
	 	
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDIdentity identity  = user.getIdentity();
		String customerId=identity.getErpCustomerPK();
	 	String actionName = this.getActionName();
	 	
	 	if ("requestProducts".equalsIgnoreCase(actionName)){

	 		List productRequests=getProductRequests(request,customerId);
			boolean hasProductReq=false;
			if( productRequests.size()>0) {
				hasProductReq=true;
			}
			if (!hasProductReq) {
				result.addError(new ActionError("error", "Please enter at least one request."));
				return true;
			}
			try {
				if(result.isSuccess()) {
					LOGGER.debug("performAction " + productRequests);
					FDCustomerManager.storeProductRequest(productRequests);
				}
			} catch (FDResourceException ex) {
				LOGGER.error(ex.toString());
				throw new JspException(ex.getMessage());
			}
	 	}
	 	return true;

	 }

	private List getProductRequests(HttpServletRequest request, String customerId) {
		LOGGER.debug("getProductRequests " + request + " and " + customerId);
		
 		String[][] productReq=new String[3][4];
 		List productRequests=new ArrayList(3);
 		
 		for(int row=0;row<3;row++) {
	 		productReq[row][0]=request.getParameter("cat_prod"+(row+1));
	 		//productReq[row][1]=request.getParameter("subCategory"+(row+1));
	 		productReq[row][1]=request.getParameter("brandParams_prod"+(row+1));
	 		productReq[row][2]=request.getParameter("descrip_prod"+(row+1));
	 		productReq[row][3]=request.getParameter("dept_prod"+(row+1));
	 		
	 		//check for defaults and clear them to make save fail
	 		if ("Brand".equals(productReq[row][1])) { productReq[row][1] = ""; }
	 		if ("Description".equals(productReq[row][2])) { productReq[row][2] = ""; }
	 		
	 		if(isValued(productReq[row][1])||isValued(productReq[row][2])) {
	 			productRequests.add(getProductRequest(productReq[row][0], productReq[row][1], productReq[row][2], productReq[row][3], customerId)) ;
	 		}
 		}
		return productRequests;
	}
	
    private FDProductRequest getProductRequest(String cat, String subCat, String prodName, String dept, String customerId) {
    	
    	FDProductRequest prodReq=new FDProductRequest();
    	prodReq.setCategory(cat);
    	prodReq.setSubCategory(subCat);
    	prodReq.setProductName(prodName);
    	prodReq.setDept(dept);
    	prodReq.setCustomerId(customerId);
    	
    	return prodReq;
    	
    }
    private boolean isValued(String input) {
    	if(input==null || "".equals(input.trim())) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

}



