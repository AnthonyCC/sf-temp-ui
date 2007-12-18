package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmCustomerInfoControllerTag extends AbstractControllerTag {
	
	private CrmCustomerInfoI customerInfo;
	private String password;
	private String verifyPassword;
	
	public void setCustomerInfo (CrmCustomerInfoI customerInfo){
		this.customerInfo = customerInfo;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if("updateCustomerInfo".equalsIgnoreCase(this.getActionName())){
			this.populateInfo(request);
			this.validateInfo(actionResult);
			if(!"".equals(this.password) || !"".equals(this.verifyPassword)){
				this.validatePassword(actionResult);
			}
			if(!actionResult.isSuccess()){
				return true;	
			}
			if(!"".equals(this.password)){
				this.customerInfo.setPassword(this.password);
			}
			if(CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)){
				try{
					this.updateCustomerInfo();
				}catch(FDResourceException ex){
					ex.printStackTrace();
					throw new JspException(ex);
				} catch (ErpDuplicateUserIdException e) {
					actionResult.addError(true, "userId", "This email address is already in our database, please enter a different one.");
					return true;
				} catch (ErpInvalidPasswordException e) {
					actionResult.addError(true, "password", "Please enter a password that is at least four characters long.");
					return true;
				}
			}
		}
		return true;
	}

	private void populateInfo(HttpServletRequest request) {
		this.customerInfo.setUserId(NVL.apply(request.getParameter("userId"), "").trim());
		this.password = NVL.apply(request.getParameter("password"), "").trim();
		this.verifyPassword = NVL.apply(request.getParameter("verifyPassword"), "").trim();
		this.customerInfo.setPasswordHint(NVL.apply(request.getParameter("passwordHint"), "").trim());
		this.customerInfo.setRecieveFdNews(request.getParameter("recieveFdNews") != null);
		this.customerInfo.setTextOnlyEmail(request.getParameter("textOnlyEmail") != null);
		this.customerInfo.setReceiveOptinNewsletter(request.getParameter("receiveOptinNewsletter") != null);
	}
	
	private void validateInfo(ActionResult result) {
		result.addError("".equals(this.customerInfo.getUserId()), "userId", SystemMessageList.MSG_REQUIRED);
		result.addError(!EmailUtil.isValidEmailAddress(this.customerInfo.getUserId()), "userId", SystemMessageList.MSG_EMAIL_FORMAT);
		result.addError("".equals(this.customerInfo.getPasswordHint()), "passwordHint", SystemMessageList.MSG_REQUIRED);
	}
	
	private void validatePassword(ActionResult result) {
		result.addError("".equals(this.password), "password", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(this.verifyPassword), "verifyPassword", SystemMessageList.MSG_REQUIRED);
		if(result.isSuccess()){
			result.addError(!this.verifyPassword.equals(this.password), "verifyPassword", "passwords must match");
		}
		if(result.isSuccess()){
			result.addError(this.password.length() < 4, "password", "Please enter a password that is at least four characters long.");
		}
	}
	
	public void updateCustomerInfo() throws ErpDuplicateUserIdException, FDResourceException, ErpInvalidPasswordException {
		FDUserI user = this.getUser();
		
		ErpCustomerModel customer = FDCustomerFactory.getErpCustomer(user.getIdentity());
		ErpCustomerInfoModel info = customer.getCustomerInfo();
		if (customerInfo.isRecieveFdNews() != info.isReceiveNewsletter()
				|| customerInfo.isTextOnlyEmail() != info.isEmailPlaintext()
				|| customerInfo.isReceiveOptinNewsletter() != info.isReceiveOptinNewsletter()) {
			info.setReceiveNewsletter(this.customerInfo.isRecieveFdNews());
			info.setEmailPlaintext(this.customerInfo.isTextOnlyEmail()); 
			info.setReceiveOptinNewsletter(this.customerInfo.isReceiveOptinNewsletter());
			FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), info);
		}
		
		if(!this.customerInfo.getUserId().equalsIgnoreCase(customer.getUserId())){
				//
				// Update UserId first since if new user id is a duplicate it will not perform the second change (of user email address)
				//
				FDCustomerManager.updateUserId(AccountActivityUtil.getActionInfo(pageContext.getSession()), this.customerInfo.getUserId());
				//
				// No errors updating UserId, so update the email address too
				//
				info.setEmail(this.customerInfo.getUserId());
				FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), info);
		}
		
		if(!"".equals(this.password) && !this.customerInfo.getPassword().equals(customer.getPasswordHash())){
			FDCustomerManager.changePassword(
				AccountActivityUtil.getActionInfo(pageContext.getSession()),
				this.customerInfo.getUserId(),
				this.password);
		}
		
		FDCustomerManager.updatePasswordHint(user.getIdentity(), this.customerInfo.getPasswordHint());
	}
	
	private FDUserI getUser() {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		return user;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}

}
