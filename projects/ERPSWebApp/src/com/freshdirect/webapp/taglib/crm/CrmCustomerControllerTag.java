package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpDuplicateDisplayNameException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CrmCustomerControllerTag extends AbstractControllerTag {

	private ErpCustomerInfoModel customerInfo;

	public void setCustomerInfo(ErpCustomerInfoModel customerInfo) {
		this.customerInfo = customerInfo;
	}

	protected FDIdentity getIdentity() {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		return (user == null) ? null : user.getIdentity();
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
		ext = phone.length() == 0 ? "" : ext;
		this.customerInfo.setHomePhone(!"".equals(phone) ? new PhoneNumber(phone, ext) : null);

		phone = NVL.apply(request.getParameter("workPhone"), "").trim();
		ext = NVL.apply(request.getParameter("workExt"), "").trim();
		ext = phone.length() == 0 ? "" : ext;
		this.customerInfo.setBusinessPhone(!"".equals(phone) ? new PhoneNumber(phone, ext) : null);

		phone = NVL.apply(request.getParameter("cellPhone"), "").trim();
		ext = NVL.apply(request.getParameter("cellExt"), "").trim();
		ext = phone.length() == 0 ? "" : ext;
		this.customerInfo.setCellPhone(!"".equals(phone) ? new PhoneNumber(phone, ext) : null);

		this.customerInfo.setAlternateEmail(NVL.apply(request.getParameter("altEmail"), "").trim());
		this.customerInfo.setWorkDepartment(NVL.apply(request.getParameter("workDepartment"), "").trim());
		this.customerInfo.setEmployeeId(NVL.apply(request.getParameter("employeeId"), "").trim());
		this.customerInfo.setDisplayName(NVL.apply(request.getParameter("displayName"), "").trim());
		this.customerInfo.setIndustry(request.getParameter("industry").trim());
		this.customerInfo.setNumOfEmployees(Integer.parseInt(request.getParameter("numOfEmployees").trim()));
		this.customerInfo.setSecondEmailAddress(request.getParameter("secondEmailAddress").trim());
		
	}

	private void validateCustomerInfo(ActionResult actionResult) {
		actionResult.addError("".equals(this.customerInfo.getFirstName()), "firstName", "required");
		actionResult.addError("".equals(this.customerInfo.getLastName()), "lastName", "required");
		actionResult.addError(this.customerInfo.getHomePhone() != null
				&& PhoneNumber.normalize(this.customerInfo.getHomePhone().getPhone()).length() != 10, "homePhone", "requires 10 digits: 3 digit area-code + 7 digit local number");
		actionResult.addError(this.customerInfo.getBusinessPhone() != null
				&& PhoneNumber.normalize(this.customerInfo.getBusinessPhone().getPhone()).length() != 10, "busPhone", "requires 10 digits: 3 digit area-code + 7 digit local number");
		actionResult.addError(this.customerInfo.getCellPhone() != null
				&& PhoneNumber.normalize(this.customerInfo.getCellPhone().getPhone()).length() != 10, "cellPhone", "requires 10 digits: 3 digit area-code + 7 digit local number");
		
		if (this.customerInfo.getDisplayName()!=null && !"".equals(this.customerInfo.getDisplayName())) 
		{
			try
			{
				FDCustomerManager.isDisplayNameUsed(this.customerInfo.getDisplayName(), getIdentity().getErpCustomerPK());
			}
			catch(ErpDuplicateDisplayNameException fde)
			{
					actionResult.addError(true, "displayName", 
							fde.getMessage());
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
