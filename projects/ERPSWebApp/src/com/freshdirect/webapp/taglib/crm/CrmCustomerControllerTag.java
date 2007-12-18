package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;

public class CrmCustomerControllerTag extends AbstractControllerTag {

	private ErpCustomerInfoModel customerInfo;

	public void setCustomerInfo(ErpCustomerInfoModel customerInfo) {
		this.customerInfo = customerInfo;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {

		try {
			if (this.getActionName().equalsIgnoreCase("updateCustomerInfo")) {
				this.updateCustomerInfo(request, actionResult);
			}
		} catch (FDResourceException e) {
			throw new JspException(e);
		}

		return true;
	}

	private void updateCustomerInfo(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		this.populateCustomerInfo(request);
		this.validateCustomerInfo(actionResult);
		if (actionResult.isSuccess()) {
			if(CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)){
				FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), this.customerInfo);
			}
		}
	}

	private void populateCustomerInfo(HttpServletRequest request) {
		this.customerInfo.setTitle(NVL.apply(request.getParameter("title"), "").trim());
		this.customerInfo.setFirstName(NVL.apply(request.getParameter("firstName"), "").trim());
		this.customerInfo.setMiddleName(NVL.apply(request.getParameter("middleName"), "").trim());
		this.customerInfo.setLastName(NVL.apply(request.getParameter("lastName"), "").trim());
		String phone = NVL.apply(request.getParameter("homePhone"), "").trim();
		String ext = NVL.apply(request.getParameter("homeExt"), "").trim();
		this.customerInfo.setHomePhone(!"".equals(phone) ? new PhoneNumber(phone, ext) : null);

		phone = NVL.apply(request.getParameter("workPhone"), "").trim();
		ext = NVL.apply(request.getParameter("workExt"), "").trim();
		this.customerInfo.setBusinessPhone(!"".equals(phone) ? new PhoneNumber(phone, ext) : null);

		phone = NVL.apply(request.getParameter("cellPhone"), "").trim();
		ext = NVL.apply(request.getParameter("cellExt"), "").trim();
		this.customerInfo.setCellPhone(!"".equals(phone) ? new PhoneNumber(phone, ext) : null);

		this.customerInfo.setAlternateEmail(NVL.apply(request.getParameter("altEmail"), "").trim());
		this.customerInfo.setWorkDepartment(NVL.apply(request.getParameter("workDepartment"), "").trim());
		this.customerInfo.setEmployeeId(NVL.apply(request.getParameter("employeeId"), "").trim());
	}

	private void validateCustomerInfo(ActionResult actionResult) {
		actionResult.addError("".equals(this.customerInfo.getFirstName()), "firstName", "required");
		actionResult.addError("".equals(this.customerInfo.getLastName()), "lastName", "required");
		actionResult.addError(this.customerInfo.getHomePhone() == null, "homePhone", "required");
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
