package com.freshdirect.webapp.taglib.crm;

import java.rmi.RemoteException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.delivery.sms.SMSAlertManager;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.CallCenterManagerSessionBean;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.fdstore.customer.ejb.FDServiceLocator;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.sms.EnumSMSAlertStatus;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmCustomerInfoControllerTag extends AbstractControllerTag {
	
	private final static Category LOGGER = LoggerFactory.getInstance(CrmCustomerInfoControllerTag.class);
	
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
			//Fdx SMS alerts.
			String fdxMobileNumber = request.getParameter("fdx_mobile_number");
			String fdxOrderNotices=request.getParameter("fdx_order_notices");
			String fdxOrderExceptions=request.getParameter("fdx_order_exceptions");
			String fdxOffers=request.getParameter("fdx_offers");
						
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
				PhoneNumber fdxPhone = new PhoneNumber(fdxMobileNumber);
				if(!phone.isValid()) {
					actionResult.addError(true, "mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
					return true;
				}
			} else if(mobile_number != null && mobile_number.length() != 0) {
				if("Y".equalsIgnoreCase(order_notices)||"Y".equalsIgnoreCase(order_exceptions) ||
						"Y".equalsIgnoreCase(offers)|| "Y".equalsIgnoreCase(partner_messages)){
					PhoneNumber phone = new PhoneNumber(mobile_number);
					if(!phone.isValid()){
						actionResult.addError(true, "fdx_mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
						return true;
					}
				}
				//commenting this logic as per ponnu based on the FDX mobile save requirements without checking notification preferences
				/*else{
					actionResult.addError(true, "mobile_number", SystemMessageList.MSG_OPTIN_REQ);
					return true;
				}*/
			} else if(!"Y".equalsIgnoreCase(order_notices)&&!"Y".equalsIgnoreCase(order_exceptions) &&
					!"Y".equalsIgnoreCase(offers)&& (mobile_number == null || mobile_number.length() == 0)){
				optOut=true;
			}
			else{
				actionResult.addError(true, "mobile_number", SystemMessageList.MSG_REQUIRED);
				return true;
			}
			
			//FDX SMS Alert for mobile validation
			if("Y".equalsIgnoreCase(fdxOrderNotices)||"Y".equalsIgnoreCase(fdxOrderExceptions) ||
					"Y".equalsIgnoreCase(fdxOffers)|| "Y".equalsIgnoreCase(partner_messages)){
				if(fdxMobileNumber == null || fdxMobileNumber.length() == 0) {
					actionResult.addError(true, "fdx_mobile_number", SystemMessageList.MSG_REQUIRED);
					return true;
				}
				PhoneNumber phone = new PhoneNumber(fdxMobileNumber);
				if(!phone.isValid()) {
					actionResult.addError(true, "fdx_mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
					return true;
				}
			} else if(fdxMobileNumber != null && fdxMobileNumber.length() != 0) {
				if("Y".equalsIgnoreCase(fdxOrderNotices)||"Y".equalsIgnoreCase(fdxOrderExceptions) ||
						"Y".equalsIgnoreCase(fdxOffers)|| "Y".equalsIgnoreCase(partner_messages)){
					PhoneNumber phone = new PhoneNumber(fdxMobileNumber);
					if(!phone.isValid()){
						actionResult.addError(true, "mobile_number", SystemMessageList.MSG_PHONE_FORMAT);
						return true;
					}
				}
			} else if(!"Y".equalsIgnoreCase(fdxOrderNotices)&&!"Y".equalsIgnoreCase(fdxOrderExceptions) &&
					!"Y".equalsIgnoreCase(fdxOffers)&& (fdxMobileNumber == null || fdxMobileNumber.length() == 0)){
				optOut=true;
			}
			else{
				actionResult.addError(true, "fdx_mobile_number", SystemMessageList.MSG_REQUIRED);
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
		this.customerInfo.setReceiveFdxNews(request.getParameter("recieveFdxNews") != null);
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
		//FDX SMS Alerts
		this.customerInfo.setFdxDeliveryNotification("Y".equals(request.getParameter("text_delivery")));
		this.customerInfo.setFdxOffersNotification("Y".equals(request.getParameter("text_offers")));
		this.customerInfo.setFdxMobileNumber(new PhoneNumber(NVL.apply(request.getParameter("fdx_mobile_number"), "")));
		this.customerInfo.setFdxOrderNotices("Y".equals(request.getParameter("fdx_order_notices"))?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value());
		this.customerInfo.setFdxOrderExceptions("Y".equals(request.getParameter("fdx_order_exceptions"))?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value());
		this.customerInfo.setFdxOffers("Y".equals(request.getParameter("fdx_offers"))?EnumSMSAlertStatus.SUBSCRIBED.value():EnumSMSAlertStatus.NONE.value());
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
		FDCustomerModel fdCustomerModel=FDCustomerFactory.getFDCustomer(user.getIdentity());
		FDCustomerEStoreModel customerSmsPreferenceModel = fdCustomerModel.getCustomerSmsPreferenceModel();
		FDCustomerEStoreModel fdCustomerEStoreModel = fdCustomerModel.getCustomerEStoreModel();
		boolean beforeUpdateDelNotif = customerSmsPreferenceModel.getDeliveryNotification();
		boolean beforeUpdateOfferNotif = customerSmsPreferenceModel.getDeliveryNotification();
		String existingMobNum= customerSmsPreferenceModel.getMobileNumber()!=null?customerSmsPreferenceModel.getMobileNumber().getPhone():"N/A";
		String fdxExistingMobNum= customerSmsPreferenceModel.getFdxMobileNumber()!=null?customerSmsPreferenceModel.getFdxMobileNumber().getPhone():"N/A";
		
		String beforeUpdateOrderNotices=customerSmsPreferenceModel.getOrderNotices()!=null ? customerSmsPreferenceModel.getOrderNotices(): EnumSMSAlertStatus.NONE.value();
		String beforeUpdateOrderExceptions=customerSmsPreferenceModel.getOrderExceptions()!=null? customerSmsPreferenceModel.getOrderExceptions() : EnumSMSAlertStatus.NONE.value();
		String beforeUpdateOffers=customerSmsPreferenceModel.getOffers()!=null ? customerSmsPreferenceModel.getOffers():EnumSMSAlertStatus.NONE.value();
		String beforeUpdatePartnerMessages=customerSmsPreferenceModel.getPartnerMessages()!=null?customerSmsPreferenceModel.getPartnerMessages():EnumSMSAlertStatus.NONE.value();
			//FDX SMS Alert	
		String beforeUpdateFdxOrderNotices=customerSmsPreferenceModel.getFdxOrderNotices()!=null ? customerSmsPreferenceModel.getFdxOrderNotices(): EnumSMSAlertStatus.NONE.value();
		String beforeUpdateFdxOrderExceptions=customerSmsPreferenceModel.getFdxOrderExceptions()!=null? customerSmsPreferenceModel.getFdxOrderExceptions(): EnumSMSAlertStatus.NONE.value();
		String beforeUpdateFdxOffers=customerSmsPreferenceModel.getFdxOffers()!=null ? customerSmsPreferenceModel.getFdxOffers():EnumSMSAlertStatus.NONE.value();
		String beforeUpdateFdxPartnerMessages=customerSmsPreferenceModel.getFdxPartnerMessages()!=null?customerSmsPreferenceModel.getPartnerMessages():EnumSMSAlertStatus.NONE.value();
		
		boolean beforeUpdateGoGreen = info.isGoGreen();
		FDActionInfo aInfo = AccountActivityUtil.getActionInfo(pageContext.getSession());
		
		boolean optedInBefore=false;
		boolean optedInFdxBeforeNonMarketing=false;
		boolean optedInFdxBeforeMarketing=false;
		
		if(beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.PENDING.value())
			||	beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())|| beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.PENDING.value())
			|| beforeUpdateOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value()) ||beforeUpdateOffers.equals(EnumSMSAlertStatus.PENDING.value()) ){
			optedInBefore=true;
		}
		
		if(beforeUpdateFdxOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||beforeUpdateFdxOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value()))
			optedInFdxBeforeNonMarketing=true;
		
			optedInFdxBeforeMarketing=beforeUpdateFdxOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value());
		
		if(beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& EnumSMSAlertStatus.PENDING.value().equals(this.customerInfo.getOrderNotices())){
			customerInfo.setOrderNotices(beforeUpdateOrderNotices);
		}
 		if(beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& EnumSMSAlertStatus.PENDING.value().equals(this.customerInfo.getOrderExceptions())){
			customerInfo.setOrderExceptions(beforeUpdateOrderExceptions);
		}
		if(beforeUpdateOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& EnumSMSAlertStatus.PENDING.value().equals(this.customerInfo.getOffers())){
			customerInfo.setOffers(beforeUpdateOffers);
		}
		if(beforeUpdatePartnerMessages.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& EnumSMSAlertStatus.PENDING.value().equals(this.customerInfo.getPartnerMessages())){
			customerInfo.setPartnerMessages(beforeUpdatePartnerMessages);
		}
		
		//FDX SMS Alert	
		
		if(beforeUpdateFdxOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& EnumSMSAlertStatus.SUBSCRIBED.value().equals(customerInfo.getFdxOrderNotices())){
			customerInfo.setFdxOrderNotices(beforeUpdateFdxOrderNotices);
		}
		if(beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& EnumSMSAlertStatus.SUBSCRIBED.value().equals(customerInfo.getFdxOrderExceptions())){
			customerInfo.setFdxOrderExceptions(beforeUpdateFdxOrderExceptions);
		}
		if(beforeUpdateFdxOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& EnumSMSAlertStatus.SUBSCRIBED.value().equals(customerInfo.getFdxOffers())){
			customerInfo.setFdxOffers(beforeUpdateFdxOffers);
		}
		if(beforeUpdatePartnerMessages.equals(EnumSMSAlertStatus.SUBSCRIBED.value())&& EnumSMSAlertStatus.SUBSCRIBED.value().equals(customerInfo.getFdxPartnerMessages())){
			customerInfo.setPartnerMessages(beforeUpdateFdxPartnerMessages);
		}
		
		
		boolean orderNoticeOptin=false;
		boolean orderExceptionOptin=false;
		boolean offersOptin=false;
		boolean fdxOrderNoticeOptin=false;
		boolean fdxOrderExceptionOptin=false;
		boolean fdxOffersOptin=false;
		
		
		if(EnumSMSAlertStatus.PENDING.value().equals(this.customerInfo.getOrderExceptions())&&beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.NONE.value())){
			orderExceptionOptin=true;
		}
		if(EnumSMSAlertStatus.PENDING.value().equals(this.customerInfo.getOrderNotices())&& beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.NONE.value())){
			orderNoticeOptin=true;
		}
		if(EnumSMSAlertStatus.PENDING.value().equals(this.customerInfo.getOffers())&& beforeUpdateOffers.equals(EnumSMSAlertStatus.NONE.value())){
			offersOptin=true;
		}
		
			
		//FDX SMS Alert	
		
		
		if(EnumSMSAlertStatus.SUBSCRIBED.value().equals(this.customerInfo.getFdxOrderExceptions())&& beforeUpdateFdxOrderExceptions.equals(EnumSMSAlertStatus.NONE.value())){
			fdxOrderExceptionOptin=true;
		}
		if(EnumSMSAlertStatus.SUBSCRIBED.value().equals(this.customerInfo.getFdxOrderNotices()) && beforeUpdateFdxOrderNotices.equals(EnumSMSAlertStatus.NONE.value())){
			fdxOrderNoticeOptin=true;
		}
		if(EnumSMSAlertStatus.SUBSCRIBED.value().equals(this.customerInfo.getFdxOffers()) && beforeUpdateFdxOffers.equals(EnumSMSAlertStatus.NONE.value())){
			fdxOffersOptin=true;
		}
		
		
		boolean isAlreadySubscribed=false;
		if(beforeUpdateOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())|| beforeUpdateOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||
				beforeUpdateOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())){
			isAlreadySubscribed=true;
		}
		
		//FDX SMS Alert	
		boolean isAlreadyFdxSubscribed=false;
		if(beforeUpdateFdxOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||beforeUpdateFdxOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())||
				beforeUpdateFdxOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())){
			isAlreadyFdxSubscribed=true;
		}
		
		
		
		if(orderNoticeOptin|| orderExceptionOptin|| offersOptin
				|| (this.customerInfo.getMobileNumber().getPhone()!=null&&
						!this.customerInfo.getMobileNumber().getPhone().isEmpty() &&
						this.customerInfo.getMobileNumber().getPhone().length()!=0 &&
						!this.customerInfo.getMobileNumber().getPhone().equals(existingMobNum))){
			
			SMSAlertManager smsAlertManager=SMSAlertManager.getInstance();
			customerSmsPreferenceModel.setSmsPreferenceflag("Y");
			boolean isSent=true;
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
						if(this.customerInfo.getMobileNumber().getPhone()!=null&& !"".equalsIgnoreCase(this.customerInfo.getMobileNumber().getPhone()))
							isSent=smsAlertManager.smsOptIn(customer.getId(),this.customerInfo.getMobileNumber().getPhone(), EnumEStoreId.FD.getContentId());
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
				LOGGER.info("not able to reach the sms due to SMS Gateway has some exception");
			}
		}
		if (customerInfo.isRecieveFdNews() != fdCustomerEStoreModel.getEmailOptIn()//info.isReceiveNewsletter()
				|| customerInfo.isTextOnlyEmail() != info.isEmailPlaintext()
				|| customerInfo.isReceiveOptinNewsletter() != info.isReceiveOptinNewsletter()
				|| this.customerInfo.getMobileNumber() != null 
				|| !this.customerInfo.getMobileNumber().getPhone().equals(existingMobNum)
				|| this.customerInfo.isDelNotification() != customerSmsPreferenceModel.getDeliveryNotification()
				|| this.customerInfo.isOffNotification() != customerSmsPreferenceModel.getOffersNotification()
				|| ! this.customerInfo.getOrderNotices().equals(beforeUpdateOrderNotices)
				|| ! this.customerInfo.getOrderExceptions().equals(beforeUpdateOrderExceptions)
				|| !this.customerInfo.getOffers().equals(beforeUpdateOffers)
				|| this.customerInfo.isGoGreen() != info.isGoGreen()) {
			info.setReceiveNewsletter(this.customerInfo.isRecieveFdNews());
			info.setEmailPlaintext(this.customerInfo.isTextOnlyEmail()); 
			info.setReceiveOptinNewsletter(this.customerInfo.isReceiveOptinNewsletter());
			customerSmsPreferenceModel.setMobileNumber(this.customerInfo.getMobileNumber());
			customerSmsPreferenceModel.setDeliveryNotification(this.customerInfo.isDelNotification());
			customerSmsPreferenceModel.setOffersNotification(this.customerInfo.isOffNotification());
			customerSmsPreferenceModel.setOrderNotices(EnumSMSAlertStatus.getEnum(this.customerInfo.getOrderNotices()).value());
			customerSmsPreferenceModel.setOrderExceptions(EnumSMSAlertStatus.getEnum(this.customerInfo.getOrderExceptions()).value());
			customerSmsPreferenceModel.setOffers(EnumSMSAlertStatus.getEnum(this.customerInfo.getOffers()).value());
			customerSmsPreferenceModel.setPartnerMessages(EnumSMSAlertStatus.NONE.value());
			customerSmsPreferenceModel.setSmsOptinDate(new java.util.Date());
			customerSmsPreferenceModel.setCrmStore(EnumEStoreId.FD.getContentId());
			info.setGoGreen(this.customerInfo.isGoGreen());
			FDCustomerManager.updateCustomerInfo(aInfo, info);
			FDCustomerManager.setFdxSmsPreferences(customerSmsPreferenceModel, user.getIdentity().getErpCustomerPK());
			FDCustomerManager.storeEmailPreferenceFlag(user.getIdentity().getFDCustomerPK(), this.customerInfo.isRecieveFdNews()?"X":"", EnumEStoreId.FD);
		}
		
		//FDX SMS Alert	
		
		if(fdxOrderNoticeOptin|| fdxOrderExceptionOptin|| fdxOffersOptin || (this.customerInfo.getFdxMobileNumber().getPhone()!=null&&
						!this.customerInfo.getFdxMobileNumber().getPhone().isEmpty() &&	this.customerInfo.getFdxMobileNumber().getPhone().length()!=0 &&
						!this.customerInfo.getFdxMobileNumber().getPhone().equals(fdxExistingMobNum)))
		{
			SMSAlertManager smsAlertManager=SMSAlertManager.getInstance();
			customerSmsPreferenceModel.setFdxSmsPreferenceflag("Y");
			boolean isSent=true;
			if(this.customerInfo.getFdxOrderNotices().equals(EnumSMSAlertStatus.NONE.value())||	!this.customerInfo.getFdxOrderExceptions().equals(EnumSMSAlertStatus.NONE.value())||
					!this.customerInfo.getFdxOffers().equals(EnumSMSAlertStatus.NONE.value()))
				{
					isSent=true;
					if(optedInFdxBeforeNonMarketing && (this.customerInfo.getFdxMobileNumber().getPhone().equals(fdxExistingMobNum)))
					{
						
						if(fdxOrderExceptionOptin && isAlreadyFdxSubscribed){
							this.customerInfo.setFdxOrderExceptions(EnumSMSAlertStatus.SUBSCRIBED.value());
						} else if(orderExceptionOptin){
							this.customerInfo.setFdxOrderExceptions(EnumSMSAlertStatus.SUBSCRIBED.value());
						}
						if(fdxOrderNoticeOptin && isAlreadyFdxSubscribed){
							this.customerInfo.setFdxOrderNotices(EnumSMSAlertStatus.SUBSCRIBED.value());
						} else if(fdxOrderNoticeOptin){
							this.customerInfo.setFdxOrderNotices(EnumSMSAlertStatus.SUBSCRIBED.value());
						}
						
					} else{
						if(this.customerInfo.getFdxMobileNumber().getPhone()!=null&& !"".equalsIgnoreCase(this.customerInfo.getFdxMobileNumber().getPhone()))
						isSent=smsAlertManager.smsOptInNonMarketing(customer.getId(), this.customerInfo.getFdxMobileNumber().getPhone(), EnumEStoreId.FDX.getContentId());
						
						if(customerSmsPreferenceModel.getFdxMobileNumber()!=null && !this.customerInfo.getFdxMobileNumber().getPhone().equals(fdxExistingMobNum)){
							
							if( beforeUpdateFdxOrderNotices.equals(EnumSMSAlertStatus.SUBSCRIBED.value())){
								this.customerInfo.setFdxOrderNotices(EnumSMSAlertStatus.SUBSCRIBED.value());
							}
							if(beforeUpdateFdxOrderExceptions.equals(EnumSMSAlertStatus.SUBSCRIBED.value())){
								this.customerInfo.setFdxOrderExceptions(EnumSMSAlertStatus.SUBSCRIBED.value());
							}
						}
					}
					if(optedInFdxBeforeMarketing && (this.customerInfo.getFdxMobileNumber().getPhone().equals(fdxExistingMobNum)))
					{
						
						if(fdxOffersOptin && isAlreadyFdxSubscribed){
							this.customerInfo.setFdxOffers(EnumSMSAlertStatus.SUBSCRIBED.value());
						} else if(fdxOffersOptin){
							this.customerInfo.setFdxOffers(EnumSMSAlertStatus.SUBSCRIBED.value());
						}
					} else{
						if(this.customerInfo.getFdxMobileNumber().getPhone()!=null&& !"".equalsIgnoreCase(this.customerInfo.getFdxMobileNumber().getPhone()))
						isSent=smsAlertManager.smsOptInMarketing(customer.getId(), this.customerInfo.getFdxMobileNumber().getPhone(), EnumEStoreId.FDX.getContentId());
						if(customerSmsPreferenceModel.getFdxMobileNumber()!=null && !this.customerInfo.getFdxMobileNumber().getPhone().equals(fdxExistingMobNum)){
							if(beforeUpdateFdxOffers.equals(EnumSMSAlertStatus.SUBSCRIBED.value())){
								this.customerInfo.setFdxOffers(EnumSMSAlertStatus.SUBSCRIBED.value());
							}
						}
					}
			}
			if(!isSent){
				LOGGER.info("not able to reach the sms due to SMS Gateway has some exception");			}
		}
		if (customerInfo.isReceiveFdxNews() != fdCustomerEStoreModel.getFdxEmailOptIn()//info.isReceiveNewsletter()
				|| customerInfo.isTextOnlyEmail() != info.isEmailPlaintext()
				|| customerInfo.isReceiveOptinNewsletter() != info.isReceiveOptinNewsletter()
				|| this.customerInfo.getFdxMobileNumber() != null 
				|| !this.customerInfo.getFdxMobileNumber().getPhone().equals(existingMobNum)
				|| this.customerInfo.isFdxDeliveryNotification() != customerSmsPreferenceModel.getFdxdeliveryNotification()
				|| this.customerInfo.isFdxOffersNotification() != customerSmsPreferenceModel.getFdxOffersNotification()
				|| !this.customerInfo.getFdxOrderNotices().equals(beforeUpdateOrderNotices)
				|| !this.customerInfo.getFdxOrderExceptions().equals(beforeUpdateOrderExceptions)
				|| !this.customerInfo.getFdxOffers().equals(beforeUpdateOffers)
				|| this.customerInfo.isGoGreen() != info.isGoGreen()) {
			info.setReceiveNewsletter(this.customerInfo.isRecieveFdNews());
			info.setEmailPlaintext(this.customerInfo.isTextOnlyEmail()); 
			info.setReceiveOptinNewsletter(this.customerInfo.isReceiveOptinNewsletter());
			customerSmsPreferenceModel.setFdxMobileNumber(this.customerInfo.getFdxMobileNumber());
			customerSmsPreferenceModel.setFdxdeliveryNotification(this.customerInfo.isDelNotification());
			customerSmsPreferenceModel.setFdxOffersNotification((this.customerInfo.isOffNotification()));
			customerSmsPreferenceModel.setFdxOrderNotices(EnumSMSAlertStatus.getEnum(this.customerInfo.getFdxOrderNotices()).value());
			customerSmsPreferenceModel.setFdxOrderExceptions(EnumSMSAlertStatus.getEnum(this.customerInfo.getFdxOrderExceptions()).value());
			customerSmsPreferenceModel.setFdxOffers(EnumSMSAlertStatus.getEnum(this.customerInfo.getFdxOffers()).value());
			customerSmsPreferenceModel.setFdxPartnerMessages((EnumSMSAlertStatus.NONE).value());
			customerSmsPreferenceModel.setSmsOptinDate(new java.util.Date());
			customerSmsPreferenceModel.setCrmStore(EnumEStoreId.FDX.getContentId());
			if(customerSmsPreferenceModel.getFdxMobileNumber()!=null ){
			FDDeliveryManager.getInstance().addSubscriptions(customer.getId(), PhoneNumber.normalize(customerSmsPreferenceModel.getFdxMobileNumber().getPhone()), null, null, customerSmsPreferenceModel.getFdxOrderNotices(),
					customerSmsPreferenceModel.getFdxOrderExceptions(), customerSmsPreferenceModel.getFdxOffers(), "N",	new Date(), EnumEStoreId.FDX.toString());
			}
			FDCustomerManager.setFdxSmsPreferences(customerSmsPreferenceModel, user.getIdentity().getErpCustomerPK());
			FDCustomerManager.storeEmailPreferenceFlag(user.getIdentity().getFDCustomerPK(), this.customerInfo.isReceiveFdxNews()?"X":"", EnumEStoreId.FDX);
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
		// FDX SMS For Activity Log
		boolean fddOrderNotificationChanged=false;
		if(! beforeUpdateOrderNotices.equals(this.customerInfo.getFdxOrderNotices())){
			fddOrderNotificationChanged=true;
		}
		boolean fdxOrderExceptionsChanged=false;
		if(! beforeUpdateOrderExceptions.equals(this.customerInfo.getFdxOrderExceptions())){
			fdxOrderExceptionsChanged=true;
		}
		boolean FdxOffersChanged=false;
		if(! beforeUpdateOffers.equals(this.customerInfo.getFdxOffers())){
			FdxOffersChanged=true;
		}
		
		if(goGreenChanged || orderNotificationChanged || orderExceptionsChanged || offersChanged || fddOrderNotificationChanged || fdxOrderExceptionsChanged || FdxOffersChanged ) {
			ErpActivityRecord rec = new ErpActivityRecord();
			rec.setSource(aInfo.getSource());
			rec.setInitiator(aInfo.getInitiator());
			rec.setCustomerId(user.getIdentity().getErpCustomerPK());
			
			if(goGreenChanged) {
				rec.setActivityType( EnumAccountActivityType.GO_GREEN );
				rec.setNote("Flag updated to " + (this.customerInfo.isGoGreen()?"Y":"N"));
				logActivity(rec);

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
				rec.setNote("Updated FD SMS Flags- Order Notif:" + customerSmsPreferenceModel.getOrderNotices() + ", OrderExp Notif:"+ customerSmsPreferenceModel.getOrderExceptions()+ ", MrkOffers:"+ customerSmsPreferenceModel.getOffers());
				logActivity(rec);
			}
			// FDX SMS For Activity Log
			if(fddOrderNotificationChanged || fdxOrderExceptionsChanged || FdxOffersChanged){
				rec.setActivityType( EnumAccountActivityType.SMS_ALERT);
				rec.setNote("Updated FDX SMS Flags- Delivery Updates:" + customerSmsPreferenceModel.getFdxOrderNotices() + ", Order Status:"+ customerSmsPreferenceModel.getFdxOrderExceptions()+ ", Offers:"+ customerSmsPreferenceModel.getFdxOffers());
				logActivity(rec);
			}
			
		}
	}
	
	private void logActivity(ErpActivityRecord rec) {
		if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ActivityLogSB)) {
			try {
				FDECommerceService.getInstance().logActivity(rec);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			new ErpLogActivityCommand(rec).execute();
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
