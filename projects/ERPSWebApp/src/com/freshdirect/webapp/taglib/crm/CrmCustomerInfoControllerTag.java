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
			
			//APPDEV-4177 - Commenting the code to remove forceful validations
			
			/*String password = request.getParameter("password");
			String verifyPassword = request.getParameter("verifyPassword");
			
			if("".equals(password.trim()) || "".equals(verifyPassword.trim())){
				actionResult.addError(true, "password", SystemMessageList.MSG_REQUIRED);
				return true;
			}*/
			
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
					!"Y".equalsIgnoreCase(offers)&& (mobile_number == null || mobile_number.length() == 0)){
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
					actionResult.addError(true, "password", "Please enter a password that is at least six characters long.");
					return true;
				}/* catch (FDResourceException e) {
					actionResult.addError(true, "mobile_number", "Error with SMS Registration please verify the mobile Number");
					return true;
				}*/
			}
		}
		return true;
	}

	private void populateInfo(HttpServletRequest request) {
		this.customerInfo.setUserId(NVL.apply(request.getParameter("userId"), "").trim());
				
		//APPDEV-4177 : Commenting the previous code, trim for the password is done at the validatePassword()
		
		//this.password = NVL.apply(request.getParameter("password"), "").trim();
		//this.verifyPassword = NVL.apply(request.getParameter("verifyPassword"), "").trim();
		 
		this.password = NVL.apply(request.getParameter("password"), "");
		this.verifyPassword = NVL.apply(request.getParameter("verifyPassword"), "");
		
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
		
		//APPDEV-4177- Adding trim in password and verifyPassword
		
		//result.addError("".equals(this.password), "password", SystemMessageList.MSG_REQUIRED);
		//result.addError("".equals(this.verifyPassword), "verifyPassword", SystemMessageList.MSG_REQUIRED);
		
		result.addError("".equals(this.password.trim()), "password", SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(this.verifyPassword.trim()), "verifyPassword", SystemMessageList.MSG_REQUIRED);
		
		// APPDEV-4177- Adding trim in validating match and length for password and verifyPassword
		
		/*if(result.isSuccess()){
			result.addError(!(this.verifyPassword).equals(this.password), "verifyPassword", "passwords must match");
		}
		if(result.isSuccess()){
			result.addError(this.password.length() < 6, "password", "Please enter a password that is at least six characters long.");
		}*/
		
		if(result.isSuccess()){
			result.addError(!(this.verifyPassword.trim()).equals(this.password.trim()), "verifyPassword", "passwords must match");
		}
		if(result.isSuccess()){
			result.addError(this.password.trim().length() < 6, "password", "Please enter a password that is at least six characters long.");
		}
	}
	
	public void updateCustomerInfo() throws ErpDuplicateUserIdException, FDResourceException, ErpInvalidPasswordException {
		FDUserI user = this.getUser();		
		ErpCustomerModel customer = FDCustomerFactory.getErpCustomer(user.getIdentity());
		ErpCustomerInfoModel info = customer.getCustomerInfo();
		boolean beforeUpdateDelNotif = info.isDeliveryNotification();
		boolean beforeUpdateOfferNotif = info.isOffersNotification();
		String existingMobNum= info.getMobileNumber()!=null?info.getMobileNumber().getPhone():"N/A";
		String beforeUpdateOrderNotices=info.getOrderNotices()!=null ? info.getOrderNotices().value() : EnumSMSAlertStatus.NONE.value();
		String beforeUpdateOrderExceptions=info.getOrderExceptions()!=null? info.getOrderExceptions().value() : EnumSMSAlertStatus.NONE.value();
		String beforeUpdateOffers=info.getOffers()!=null ? info.getOffers().value():EnumSMSAlertStatus.NONE.value();
		String beforeUpdatePartnerMessages=info.getPartnerMessages()!=null?info.getPartnerMessages().value():EnumSMSAlertStatus.NONE.value();
		boolean beforeUpdateGoGreen = info.isGoGreen();
		FDActionInfo aInfo = AccountActivityUtil.getActionInfo(pageContext.getSession());
		
		boolean optedInBefore=false;
		if(beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.PENDING.value())
			||	beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())|| beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.PENDING.value())
			|| beforeUpdateOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value()) ||beforeUpdateOffers.equals(EnumSMSAlertStatus.PENDING.value()) ){
			optedInBefore=true;
		}
		
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
		
		if(this.customerInfo.getOrderExceptions().equals(EnumSMSAlertStatus.PENDING.value())&&beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.NONE.value())){
			orderExceptionOptin=true;
		}
		if(this.customerInfo.getOrderNotices().equals(EnumSMSAlertStatus.PENDING.value())&& beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.NONE.value())){
			orderNoticeOptin=true;
		}
		if(this.customerInfo.getOffers().equals(EnumSMSAlertStatus.PENDING.value())&& beforeUpdateOffers.equals(EnumSMSAlertStatus.NONE.value())){
			offersOptin=true;
		}
		
		boolean isAlreadySubscribed=false;
		if(beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||
				beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||
				beforeUpdateOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())){
			isAlreadySubscribed=true;
		}
		
		if(orderNoticeOptin|| orderExceptionOptin|| offersOptin
				|| (this.customerInfo.getMobileNumber().getPhone()!=null&&
						!this.customerInfo.getMobileNumber().getPhone().isEmpty() &&
						this.customerInfo.getMobileNumber().getPhone().length()!=0 &&
						!this.customerInfo.getMobileNumber().getPhone().equals(existingMobNum))){
			SMSAlertManager smsAlertManager=SMSAlertManager.getInstance();
			info.setSmsPreferenceflag("Y");
			boolean isSent=false;
			if(!this.customerInfo.getOrderNotices().equals(EnumSMSAlertStatus.NONE.value())||
					!this.customerInfo.getOrderExceptions().equals(EnumSMSAlertStatus.NONE.value())||
					!this.customerInfo.getOffers().equals(EnumSMSAlertStatus.NONE.value())||
					!this.customerInfo.getPartnerMessages().equals(EnumSMSAlertStatus.NONE.value())){
				
					if(optedInBefore && (this.customerInfo.getMobileNumber().getPhone().equals(existingMobNum))){
						isSent=true;
						if(orderExceptionOptin && isAlreadySubscribed){
							this.customerInfo.setOrderExceptions(EnumSMSAlertStatus.SUBSCRIBED.value());
						} else if(orderExceptionOptin){
							this.customerInfo.setOrderExceptions(EnumSMSAlertStatus.PENDING.value());
						}
						if(orderNoticeOptin && isAlreadySubscribed){
							this.customerInfo.setOrderNotices(EnumSMSAlertStatus.SUBSCRIBED.value());
						} else if(orderNoticeOptin){
							this.customerInfo.setOrderNotices(EnumSMSAlertStatus.PENDING.value());
						}
						if(offersOptin && isAlreadySubscribed){
							this.customerInfo.setOffers(EnumSMSAlertStatus.SUBSCRIBED.value());
						} else if(offersOptin){
							this.customerInfo.setOffers(EnumSMSAlertStatus.PENDING.value());
						}
						
					} else{
						isSent=smsAlertManager.smsOptIn(customer.getId(),this.customerInfo.getMobileNumber().getPhone());
						if(info.getMobileNumber()!=null && !this.customerInfo.getMobileNumber().getPhone().equals(existingMobNum)){
							
							if( beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||
									beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.PENDING.value())){
								this.customerInfo.setOrderNotices(EnumSMSAlertStatus.PENDING.value());
							}
							if(beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||
									beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.PENDING.value())){
								this.customerInfo.setOrderExceptions(EnumSMSAlertStatus.PENDING.value());
							}
							if(beforeUpdateOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||
									beforeUpdateOffers.equals(EnumSMSAlertStatus.PENDING.value())){
								this.customerInfo.setOffers(EnumSMSAlertStatus.PENDING.value());
							}
							
						}
					}
			}
			if(!isSent){
				throw new FDResourceException();
			}
		}
		if (customerInfo.isRecieveFdNews() != info.isReceiveNewsletter()
				|| customerInfo.isTextOnlyEmail() != info.isEmailPlaintext()
				|| customerInfo.isReceiveOptinNewsletter() != info.isReceiveOptinNewsletter()
				|| this.customerInfo.getMobileNumber() != null 
				|| !this.customerInfo.getMobileNumber().getPhone().equals(existingMobNum)
				|| this.customerInfo.isDelNotification() != info.isDeliveryNotification()
				|| this.customerInfo.isOffNotification() != info.isOffersNotification()
				|| ! this.customerInfo.getOrderNotices().equals(beforeUpdateOrderNotices)
				|| ! this.customerInfo.getOrderExceptions().equals(beforeUpdateOrderExceptions)
				|| !this.customerInfo.getOffers().equals(beforeUpdateOffers)
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
			info.setPartnerMessages(EnumSMSAlertStatus.NONE);
			info.setSmsOptinDate(new java.util.Date());
			
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
		/*boolean delNotifChanged = false;
		if(beforeUpdateDelNotif != this.customerInfo.isDelNotification()) {
			delNotifChanged = true;
		}
		boolean offerNotifChanged = false;
		if(beforeUpdateOfferNotif != this.customerInfo.isOffNotification()) {
			offerNotifChanged = true;
		}*/
		boolean goGreenChanged = false;
		if(beforeUpdateGoGreen != this.customerInfo.isGoGreen()) {
			goGreenChanged = true;
		}
		boolean orderNotificationChanged=false;
		if(! beforeUpdateOrderNotices.equals(this.customerInfo.getOrderNotices())){
			orderNotificationChanged=true;
		}
		boolean orderExceptionsChanged=false;
		if(! beforeUpdateOrderExceptions.equals(this.customerInfo.getOrderExceptions())){
			orderExceptionsChanged=true;
		}
		boolean offersChanged=false;
		if(! beforeUpdateOffers.equals(this.customerInfo.getOffers())){
			offersChanged=true;
		}
		if(goGreenChanged || orderNotificationChanged || orderExceptionsChanged || offersChanged ) {
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
			
			/*if(delNotifChanged) {
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
			}*/
			
			if(orderNotificationChanged || orderExceptionsChanged || offersChanged){
				rec.setActivityType( EnumAccountActivityType.SMS_ALERT);
				rec.setNote("Updated SMS Flags- Order Notif:" + this.customerInfo.getOrderNotices() + ", OrderExp Notif:"+ this.customerInfo.getOrderExceptions()+ ", MrkOffers:"+this.customerInfo.getOffers());
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
