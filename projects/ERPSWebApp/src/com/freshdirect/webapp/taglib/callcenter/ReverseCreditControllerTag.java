/*
 * Created on Apr 2, 2003
 */
package com.freshdirect.webapp.taglib.callcenter;

/**
 * @author knadeem
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class ReverseCreditControllerTag extends AbstractControllerTag {
	
	private String saleId;
	
	public void setSaleId(String saleId){
		this.saleId = saleId;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String action = request.getParameter("action");
		if("reverse_credit".equals(action)){
			String complaintId = request.getParameter("complaint_id");
			try{
				CallCenterServices.reverseCustomerCredit(this.saleId, complaintId);
				CrmSession.invalidateCachedOrder(request.getSession());
			}catch(FDResourceException fe){
				actionResult.addError(new ActionError("technical_difficulties", "We are experiencing technical difficulties please try later"));
				return true;
			}catch(ErpTransactionException te){
				actionResult.addError(new ActionError("transaction_error", te.getMessage()));
				return true;
			}catch(ErpComplaintException ce){
				actionResult.addError(new ActionError("complaint_error", ce.getMessage()));
				return true;
			}
		}
		return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
						            new VariableInfo(data.getAttributeString("result"),
			                            "com.freshdirect.framework.webapp.ActionResult",
			                            true,
			                            VariableInfo.NESTED)             
		        };

		    }
	}

}
