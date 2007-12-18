package com.freshdirect.webapp.taglib.fdstore;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class ReminderEmailControllerTag extends AbstractControllerTag {
	
	private ErpCustomerInfoModel customerInfo;

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if("updateReminder".equals(this.getActionName())){
			this.updateReminderEmail(request, actionResult);
		}
		return true;
	}
	
	private void updateReminderEmail(HttpServletRequest request, ActionResult actionResult) {
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
		if(this.customerInfo.isReminderAltEmail()){
			String altEmail = this.customerInfo.getAlternateEmail();
			actionResult.addError("".equals(altEmail), EnumUserInfoName.ALT_EMAIL.getCode(), SystemMessageList.MSG_REQUIRED);
			actionResult.addError((!"".equals(altEmail) && !EmailUtil.isValidEmailAddress(altEmail)), EnumUserInfoName.ALT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_FORMAT);
		}
	}

	private void populateForm(HttpServletRequest request) {
		boolean sendReminder = request.getParameter("sendReminder") != null;
		if(sendReminder){
			int dayOfWeek = Integer.parseInt(NVL.apply(request.getParameter("dayOfWeek"), ""));
			this.customerInfo.setReminderDayOfWeek(dayOfWeek);
			int frequency = Integer.parseInt(NVL.apply(request.getParameter("frequencyOption"), ""));
			this.customerInfo.setReminderFrequency(frequency);
			boolean altEmailOption = request.getParameter("altEmailOption") != null;
			this.customerInfo.setReminderAltEmail(altEmailOption);
			if(altEmailOption){
				this.customerInfo.setAlternateEmail(NVL.apply(request.getParameter(EnumUserInfoName.ALT_EMAIL.getCode()), ""));
			}
			Calendar cal = Calendar.getInstance();
			while(cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek){
				cal.add(Calendar.DATE, -1);
			}
			customerInfo.setLastReminderEmailSend(cal.getTime());
		}else{
			this.customerInfo.setReminderFrequency(0);
			this.customerInfo.setReminderAltEmail(false);
		}
	}
	
	public void setCustomerInfo(ErpCustomerInfoModel customerInfo){
		this.customerInfo = customerInfo;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
