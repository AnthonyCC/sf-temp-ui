package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.sms.SMSAlertManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.sms.EnumSMSAlertStatus;
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
			boolean optOut=false;
			String mobile_number = request.getParameter("mobile_number");
			String order_notices=request.getParameter("order_notices");
			String order_exceptions=request.getParameter("order_exceptions");
			String offers=request.getParameter("offers");
			String partner_messages=request.getParameter("partner_messages");
			
			if("Y".equalsIgnoreCase(order_notices)||"Y".equalsIgnoreCase(order_exceptions) ||
					"Y".equalsIgnoreCase(offers)|| "Y".equalsIgnoreCase(partner_messages)){
				if(mobile_number == null || mobile_number.length() == 0) {
					actionResult.addError(true, "mobile_number", SystemMessageList.MSG_REQUIRED);
					return true;
				}
				PhoneNumber phone = new PhoneNumber(mobile_number);
				if(!phone.isValid()) {
					actionResult.addError(true, "mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
					return true;
				}
			} else if(mobile_number != null && mobile_number.length() != 0) {
				if("Y".equalsIgnoreCase(order_notices)||"Y".equalsIgnoreCase(order_exceptions) ||
						"Y".equalsIgnoreCase(offers)|| "Y".equalsIgnoreCase(partner_messages)){
					PhoneNumber phone = new PhoneNumber(mobile_number);
					if(!phone.isValid()){
						actionResult.addError(true, "mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
						return true;
					}
				}else{
					actionResult.addError(true, "mobile_number", SystemMessageList.MSG_OPTIN_REQ);
					return true;
				}
			} else if(!"Y".equalsIgnoreCase(order_notices)&&!"Y".equalsIgnoreCase(order_exceptions) &&
					!"Y".equalsIgnoreCase(offers)&& !"Y".equalsIgnoreCase(partner_messages) && (mobile_number == null || mobile_number.length() == 0)){
				optOut=true;
			}
			else{
				actionResult.addError(true, "mobile_number", SystemMessageList.MSG_REQUIRED);
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
				} catch (DlvResourceException e) {
					actionResult.addError(true, "mobile_number", "Error While SMS Registration please verify the mobile Number");
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
		this.customerInfo.setMobileNumber(new PhoneNumber(NVL.apply(request.getParameter("mobile_number"), "")));
		this.customerInfo.setDelNotification("Y".equals(request.getParameter("text_delivery")));
		this.customerInfo.setOffNotification("Y".equals(request.getParameter("text_offers")));
		this.customerInfo.setOrderNotices("Y".equals(request.getParameter("order_notices"))?EnumSMSAlertStatus.PENDING.value():EnumSMSAlertStatus.NONE.value());
		this.customerInfo.setOrderExceptions("Y".equals(request.getParameter("order_exceptions"))?EnumSMSAlertStatus.PENDING.value():EnumSMSAlertStatus.NONE.value());
		this.customerInfo.setOffers("Y".equals(request.getParameter("offers"))?EnumSMSAlertStatus.PENDING.value():EnumSMSAlertStatus.NONE.value());
		this.customerInfo.setPartnerMessages("Y".equals(request.getParameter("partner_messages"))?EnumSMSAlertStatus.PENDING.value():EnumSMSAlertStatus.NONE.value());
		this.customerInfo.setGoGreen("Y".equals(request.getParameter("go_green")));
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
	
	public void updateCustomerInfo() throws ErpDuplicateUserIdException, FDResourceException, ErpInvalidPasswordException, DlvResourceException {
		FDUserI user = this.getUser();		
		ErpCustomerModel customer = FDCustomerFactory.getErpCustomer(user.getIdentity());
		ErpCustomerInfoModel info = customer.getCustomerInfo();
		boolean beforeUpdateDelNotif = info.isDeliveryNotification();
		boolean beforeUpdateOfferNotif = info.isOffersNotification();
		String beforeUpdateOrderNotices=info.getOrderNotices().value();
		String beforeUpdateOrderExceptions=info.getOrderExceptions().value();
		String beforeUpdateOffers=info.getOffers().value();
		String beforeUpdatePartnerMessages=info.getPartnerMessages().value();
		boolean beforeUpdateGoGreen = info.isGoGreen();
		FDActionInfo aInfo = AccountActivityUtil.getActionInfo(pageContext.getSession());
		
		if(beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& this.customerInfo.getOrderNotices().equals(EnumSMSAlertStatus.PENDING.value())){
			customerInfo.setOrderNotices(beforeUpdateOrderNotices);
		}
		if(beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& this.customerInfo.getOrderExceptions().equals(EnumSMSAlertStatus.PENDING.value())){
			customerInfo.setOrderExceptions(beforeUpdateOrderExceptions);
		}
		if(beforeUpdateOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& this.customerInfo.getOffers().equals(EnumSMSAlertStatus.PENDING.value())){
			customerInfo.setOffers(beforeUpdateOffers);
		}
		if(beforeUpdatePartnerMessages.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& this.customerInfo.getPartnerMessages().equals(EnumSMSAlertStatus.PENDING.value())){
			customerInfo.setPartnerMessages(beforeUpdatePartnerMessages);
		}
		boolean orderNoticeOptin=false;
		boolean orderExceptionOptin=false;
		boolean offersOptin=false;
		boolean partnerMessagesOptin=false;
		if(this.customerInfo.getOrderExceptions().equals(EnumSMSAlertStatus.NONE.value())&& info.getOrderExceptions().value().equals(EnumSMSAlertStatus.PENDING.value())){
			orderExceptionOptin=true;
		}
		if(this.customerInfo.getOrderNotices().equals(EnumSMSAlertStatus.NONE.value())&& info.getOrderNotices().value().equals(EnumSMSAlertStatus.PENDING.value())){
			orderNoticeOptin=true;
		}
		if(this.customerInfo.getOffers().equals(EnumSMSAlertStatus.NONE.value())&& info.getOffers().value().equals(EnumSMSAlertStatus.PENDING.value())){
			offersOptin=true;
		}
		if(this.customerInfo.getPartnerMessages().equals(EnumSMSAlertStatus.NONE.value())&& info.getPartnerMessages().value().equals(EnumSMSAlertStatus.PENDING.value())){
			partnerMessagesOptin=true;
		}
		if(orderNoticeOptin|| orderExceptionOptin|| offersOptin|| partnerMessagesOptin
				|| (info.getMobileNumber() != null && this.customerInfo.getMobileNumber().getPhone() != info.getMobileNumber().getPhone())){
			SMSAlertManager smsAlertManager=SMSAlertManager.getInstance();
			boolean isSent=false;
			if(!info.getOrderNotices().value().equals(EnumSMSAlertStatus.NONE.value())||
					!info.getOrderExceptions().value().equals(EnumSMSAlertStatus.NONE.value())||
					!info.getOffers().value().equals(EnumSMSAlertStatus.NONE.value())||
					!info.getPartnerMessages().value().equals(EnumSMSAlertStatus.NONE.value())){
				isSent=smsAlertManager.smsOptIn(customer.getId(),info.getMobileNumber().getPhone());
			}
			if(!isSent){
				throw new DlvResourceException();
			}
		}
		if (customerInfo.isRecieveFdNews() != info.isReceiveNewsletter()
				|| customerInfo.isTextOnlyEmail() != info.isEmailPlaintext()
				|| customerInfo.isReceiveOptinNewsletter() != info.isReceiveOptinNewsletter()
				|| this.customerInfo.getMobileNumber() != null 
				|| (info.getMobileNumber() != null && this.customerInfo.getMobileNumber().getPhone() != info.getMobileNumber().getPhone()) 
				|| this.customerInfo.isDelNotification() != info.isDeliveryNotification()
				|| this.customerInfo.isOffNotification() != info.isOffersNotification()
				|| (! this.customerInfo.getOrderNotices().equals(info.getOrderNotices().value()))
				|| (! this.customerInfo.getOrderExceptions().equals(info.getOrderExceptions().value()))
				|| (! this.customerInfo.getOffers().equals(info.getOffers().value()))
				|| (! this.customerInfo.getPartnerMessages().equals(info.getPartnerMessages().value()))
				|| this.customerInfo.isGoGreen() != info.isGoGreen()) {
			info.setReceiveNewsletter(this.customerInfo.isRecieveFdNews());
			info.setEmailPlaintext(this.customerInfo.isTextOnlyEmail()); 
			info.setReceiveOptinNewsletter(this.customerInfo.isReceiveOptinNewsletter());
			info.setMobileNumber(this.customerInfo.getMobileNumber());
			info.setDeliveryNotification(this.customerInfo.isDelNotification());
			info.setOffersNotification(this.customerInfo.isOffNotification());
			info.setOrderNotices(EnumSMSAlertStatus.getEnum(this.customerInfo.getOrderNotices()));
			info.setOrderExceptions(EnumSMSAlertStatus.getEnum(this.customerInfo.getOrderExceptions()));
			info.setOffers(EnumSMSAlertStatus.getEnum(this.customerInfo.getOffers()));
			info.setPartnerMessages(EnumSMSAlertStatus.getEnum(this.customerInfo.getPartnerMessages()));
			info.setGoGreen(this.customerInfo.isGoGreen());
			FDCustomerManager.updateCustomerInfo(aInfo, info);
		}
		
		if(!this.customerInfo.getUserId().equalsIgnoreCase(customer.getUserId())){
				//
				// Update UserId first since if new user id is a duplicate it will not perform the second change (of user email address)
				//
				FDCustomerManager.updateUserId(aInfo, this.customerInfo.getUserId());
				//
				// No errors updating UserId, so update the email address too
				//
				info.setEmail(this.customerInfo.getUserId());
				FDCustomerManager.updateCustomerInfo(aInfo, info);
		}
		
		if(!"".equals(this.password) && !this.customerInfo.getPassword().equals(customer.getPasswordHash())){
			FDCustomerManager.changePassword(
				aInfo,
				this.customerInfo.getUserId(),
				this.password);
		}
		
		FDCustomerManager.updatePasswordHint(user.getIdentity(), this.customerInfo.getPasswordHint());
		
		//Record activity info for new gogreen and notification flags
		boolean delNotifChanged = false;
		if(beforeUpdateDelNotif != this.customerInfo.isDelNotification()) {
			delNotifChanged = true;
		}
		boolean offerNotifChanged = false;
		if(beforeUpdateOfferNotif != this.customerInfo.isOffNotification()) {
			offerNotifChanged = true;
		}
		boolean goGreenChanged = false;
		if(beforeUpdateGoGreen != this.customerInfo.isGoGreen()) {
			goGreenChanged = true;
		}
		if(delNotifChanged || offerNotifChanged || goGreenChanged ) {
			ErpActivityRecord rec = new ErpActivityRecord();
			rec.setSource(aInfo.getSource());
			rec.setInitiator(aInfo.getInitiator());
			rec.setCustomerId(user.getIdentity().getErpCustomerPK());
			
			if(goGreenChanged) {
				rec.setActivityType( EnumAccountActivityType.GO_GREEN );
				rec.setNote("Flag updated to " + (this.customerInfo.isGoGreen()?"Y":"N"));
				ErpLogActivityCommand command = new ErpLogActivityCommand(rec);
				command.execute();
			}
			
			if(delNotifChanged) {
				rec.setActivityType( EnumAccountActivityType.DELIVERY_NOTIFICATION );
				rec.setNote("Flag updated to " + (this.customerInfo.isDelNotification()?"Y":"N"));
				ErpLogActivityCommand command = new ErpLogActivityCommand(rec);
				command.execute();
			}
			
			if(offerNotifChanged) {
				rec.setActivityType( EnumAccountActivityType.OFFER_NOTIFICATION );
				rec.setNote("Flag updated to " + (this.customerInfo.isOffNotification()?"Y":"N"));
				ErpLogActivityCommand command = new ErpLogActivityCommand(rec);
				command.execute();
			}
		}
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
