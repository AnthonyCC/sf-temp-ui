package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class NewsLetterControllerTag extends AbstractControllerTag {

	private ErpCustomerInfoModel customerInfo;

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if("updateOptinNewsletter".equals(this.getActionName())){
			this.updateOptinNewsletter(request, actionResult);
		}
		return true;
	}
	
	private void updateOptinNewsletter(HttpServletRequest request, ActionResult actionResult) {
		this.populateForm(request);
		this.validateForm(actionResult);
		if(actionResult.isSuccess()){
			try{
				FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(request.getSession()), this.customerInfo);
			}catch(FDResourceException e){
				actionResult.setError(SystemMessageList.MSG_TECHNICAL_ERROR);
			}
		}
	}
	
	private void validateForm(ActionResult actionResult) {
	}

	private void populateForm(HttpServletRequest request) {
		boolean sendOptinNewsletter = "yes".equalsIgnoreCase(request.getParameter("sendOptinNewsletter"));
		this.customerInfo.setReceiveOptinNewsletter(sendOptinNewsletter);
	}
	
	public void setCustomerInfo(ErpCustomerInfoModel customerInfo){
		this.customerInfo = customerInfo;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
