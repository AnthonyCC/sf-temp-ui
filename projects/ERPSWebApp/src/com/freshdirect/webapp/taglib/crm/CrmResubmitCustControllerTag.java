package com.freshdirect.webapp.taglib.crm;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmResubmitCustControllerTag extends AbstractControllerTag {
	
	private String customerId;
	private String id;
	
	public void setId(String id){
		this.id = id;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if("resubmitCustomer".equalsIgnoreCase(this.getActionName())){
			String custId=null;
			try {	
				if(this.customerId != null){
					this.resubmitCustomer(this.customerId.trim());
				}else{
					String[] ids = request.getParameterValues("customerId");					
					for(int i = 0; i < ids.length; i++) {
						custId = ids[i];
						if(custId != null){
							this.resubmitCustomer(custId.trim());
						}
					}
				}
			} catch (FDResourceException e) {				
				actionResult.addError(true, "resubmitCustomerError", " Customer(s) already resubmitted ");				
				performGetAction(request,actionResult);
				return true;
			}
		}
		return true;
	}
		
	private void resubmitCustomer(String customerId) throws FDResourceException {
		CallCenterServices.resubmitCustomer(customerId);
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if(this.customerId == null){
			try{
				
				List customerList = CallCenterServices.getNSMCustomers();
				pageContext.setAttribute(this.id, customerList != null ? customerList : Collections.EMPTY_LIST);
			} catch (FDResourceException ex) {
				throw new JspException(ex);
			}
		}
		return true;
	}

	// as a quickfix for the problem 
	
	
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
		            new VariableInfo(data.getAttributeString("id"),
		                            "java.util.List",
		                            true,
		                            VariableInfo.NESTED),
						            new VariableInfo(data.getAttributeString("result"),
			                            "com.freshdirect.framework.webapp.ActionResult",
			                            true,
			                            VariableInfo.NESTED)             
		        };

		    }
	}
}
