package com.freshdirect.webapp.action.fdstore;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicatePaymentMethodException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.enums.CaptchaType;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.fdstore.survey.EnumSurveyType;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyFactory;
import com.freshdirect.fdstore.survey.FDSurveyQuestion;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.fdstore.survey.SurveyKey;
import com.freshdirect.framework.util.MD5Hasher;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.delivery.model.EnumDeliveryStatus;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.action.WebActionSupport;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.AccountUtil;
import com.freshdirect.webapp.util.CaptchaUtil;

public class RegistrationAction extends WebActionSupport {

    private static final Category LOGGER = LoggerFactory.getInstance(RegistrationAction.class);

	private static boolean ALLOW_ALL = false;
	private final int regType;
	private String statusChangePage;
	private boolean signupFromCheckout;
	private String successPage;
	private String fraudPage;
	boolean restrictedAddress = false;
	private String referralId;

	public RegistrationAction(int regType) {
		this.regType = regType;
	}

	public void setStatusChangePage(String statusChangePage) {
		this.statusChangePage = statusChangePage;
	}

	public void setSignupFromCheckout(boolean signupFromCheckout) {
		this.signupFromCheckout = signupFromCheckout;
	}

	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

	public String getSuccessPage() {
		return this.successPage;
	}

	public void setFraudPage(String fraudPage) {
		this.fraudPage = fraudPage;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}

	public String getReferralId() {
		return referralId;
	}

	@Override
    public String execute() throws Exception {
	    //ALLOW_ALL = true;
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		boolean startedAsHomeUser = user.isHomeUser();
		boolean startedAsPickupOnly = user.isPickupOnly();
		boolean startedPromoEligible = user.isEligibleForSignupPromotion();

		ContactInfo cInfo = new ContactInfo(request);
		AccountInfo aInfo = new AccountInfo(request);
		AddressInfo addInfo = new AddressInfo(request);

		EnumServiceType serviceType = addInfo.getAddressType();
		aInfo.validate(actionResult);
		addInfo.validate(actionResult);
		cInfo.validate(actionResult, serviceType);
		// validate captcha if it's enabled
		boolean isCaptchaSuccess = CaptchaUtil.validateCaptcha(request.getParameter("g-recaptcha-response"), request.getRemoteAddr(), CaptchaType.SIGN_UP, session, SessionName.SIGNUP_ATTEMPT, FDStoreProperties.getMaxInvalidSignUpAttempt());
		if (!isCaptchaSuccess) {
			actionResult.addError(new ActionError("captcha", SystemMessageList.MSG_INVALID_CAPTCHA));
		}
		if (!actionResult.isSuccess() /*&& !ALLOW_ALL*/) {
			return ERROR;
		}

		//
		// Scrub the delivery address contained within the request
		// we'll bail out here if the
		//
		
		// VALIDATE DELIVERY ADDRESS
		// NOTE: don't be strict in USPS service checking if user is pickup or depot
		AddressModel dlvAddress = addInfo.getDlvAddress();
		dlvAddress.setServiceType(serviceType);
		DeliveryAddressValidator validator = new DeliveryAddressValidator(dlvAddress, user.isHomeUser() || user.isCorporateUser());
		
		boolean addressValid = validator.validateAddress(actionResult);
		if (!actionResult.isSuccess()  /*&& !ALLOW_ALL*/) {
			CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
			
			return ERROR;
		}

		AddressModel address = validator.getScrubbedAddress();


		if (validator.getServiceResult() != null && validator.getServiceResult().isServiceRestricted()) {
			restrictedAddress = true;
			session.setAttribute(SessionName.BLOCKED_ADDRESS_WARNING, Boolean.TRUE);
		}

		if (addressValid /*|| ALLOW_ALL*/) {

			this.reclassifyUser(user, address, serviceType, validator.getServiceResult());

			String application = (String) session.getAttribute(SessionName.APPLICATION);

			//
			// Absence of an FDIdentity in session means this is a new registration.
			// Presence of an FDIdentity might indicate a registered user but failed predicate function
			// (like adding a credit card in CallCenter), so don't attempt to re-register if one is found.
			//
			if (user.getIdentity() == null) {

				ErpCustomerModel erpCustomer = aInfo.getErpCustomerModel();
				ErpCustomerInfoModel customerInfo = new ErpCustomerInfoModel();
				aInfo.decorateCustomerInfo(customerInfo);
				cInfo.decorateCustomerInfo(customerInfo);
				customerInfo.setRegRefTrackingCode(user.getLastRefTrackingCode());
				// changes done by gopal
				customerInfo.setReferralProgId(user.getLastRefProgId());
				customerInfo.setReferralProgInvtId(user.getLastRefProgInvtId());
				
				erpCustomer.setCustomerInfo(customerInfo);

				ErpAddressModel erpAddress = new ErpAddressModel(address);
				erpAddress.setFirstName(customerInfo.getFirstName());
				erpAddress.setLastName(customerInfo.getLastName());
				erpAddress.setPhone(customerInfo.getHomePhone());
				erpAddress.setAddressInfo(address.getAddressInfo());
				erpAddress.setServiceType(serviceType);
				erpAddress.setCompanyName(addInfo.getCompanyName());

				erpCustomer.addShipToAddress(erpAddress);

				FDCustomerModel fdCustomer = new FDCustomerModel();

				fdCustomer.setPasswordHint(aInfo.getPasswordHint());
				fdCustomer.setDepotCode(user.getDepotCode());
				fdCustomer.setDefaultDepotLocationPK(addInfo.getLocationId());
				fdCustomer.setDepotCode(user.getDepotCode());
	
				FDSurveyResponse survey = aInfo.getMarketingSurvey(new SurveyKey(EnumSurveyType.REGISTRATION_SURVEY, serviceType), request);
	
			try {
					LOGGER.info("Entering final Registration.");
					FDIdentity regIdent = this.doRegistration(fdCustomer, erpCustomer, survey, serviceType);
					//user.getShoppingCart().setZoneInfo(zoneInfo);
					user.setIdentity(regIdent);
					user.invalidateCache();
					user.isLoggedIn(true);
					user.setZipCode(erpAddress.getZipCode());
                    user.setSelectedServiceType(serviceType);
					//Added the following line for zone pricing to keep user service type up-to-date.
                    user.setZPServiceType(serviceType);
					user.updateUserState();
					user.setTcAcknowledge(true);
					//Set the Default Delivery pass status.
					FDUserDlvPassInfo dlvpassInfo = new FDUserDlvPassInfo(EnumDlvPassStatus.NONE, null, null, null, 0.0, null,0,0,0,false,0,null,0,null,null);
					user.getUser().setDlvPassInfo(dlvpassInfo);
					user.getUser().setAssignedCustomerParams(FDCustomerManager.getAssignedCustomerParams(user.getUser()));
					session.setAttribute(SessionName.USER, user);
				} catch (ErpDuplicateUserIdException de) {
					LOGGER.warn("User registration failed due to duplicate id", de);
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_UNIQUE_USERNAME));
				} catch(ErpFraudException fe) {
					LOGGER.warn("User registration failed due to fraud", fe);
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), fe.getFraudReason().getDescription()));
					
				}

			}

			boolean inCallCenter = "callcenter".equalsIgnoreCase(application);
			
			if (inCallCenter && actionResult.isSuccess()) {
				//
				// Do CallCenter-specific stuff
				//
				LOGGER.debug("Creating payment method for customer, check first to see if basic info entered account num");
				if (!"".equals(request.getParameter(PaymentMethodName.ACCOUNT_NUMBER))) {
					LOGGER.debug("CREATING PAYMENT METHOD");
					ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm(request, actionResult, user.getIdentity());
					PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, actionResult, user,true,EnumAccountActivityType.ADD_PAYMENT_METHOD);
					if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
				        String terms = request.getParameter(PaymentMethodName.TERMS);
				        actionResult.addError(
				    	        terms == null || terms.length() <= 0,
				    	        PaymentMethodName.TERMS,SystemMessageList.MSG_REQUIRED
				    	        );
				        if (actionResult.isSuccess()  && !PaymentMethodUtil.hasECheckAccount(user.getIdentity())) {
				        	paymentMethod.setIsTermsAccepted(true);
				        }
					}
					if (actionResult.isSuccess())
						PaymentMethodUtil.addPaymentMethod(request, actionResult, paymentMethod);
					
				} else {
					LOGGER.debug("NO CARD REDIRECT");
					this.setSuccessPage("/main/account_details.jsp?cc=no");
				}
			}

			//
			// we need to see if the user's delivery status changed as a result of registration
			//
			if (this.statusChangePage != null) {
				boolean promoChanged = startedPromoEligible != user.isEligibleForSignupPromotion();
				boolean dlvChanged = (startedAsHomeUser && user.isPickupOnly())
					|| (startedAsPickupOnly && user.isHomeUser())
					|| (user.isDepotUser() && user.isHomeUser())
					|| restrictedAddress;
				//we can never get in here now, there's no SIGNUP promos anymore...
				if (user.isEligibleForSignupPromotion() && (promoChanged || dlvChanged || !signupFromCheckout)) {
					//make sure we use ?/& depending on if there's already query params
					this.setSuccessPage(this.statusChangePage + ( ((this.statusChangePage).indexOf("?") == -1) ? "?" : "&" ) + "promoChange=" + promoChanged + "&dlvChange=" + dlvChanged);
				}
			}
			//set a session attribute so we know registration completed successfully
			if (actionResult.isSuccess()) {
				user.setJustSignedUp(true);
				session.setAttribute("regSuccess", true);
				CaptchaUtil.resetAttempt(request, SessionName.SIGNUP_ATTEMPT);
			} else {
				CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
			}
			
			//Set the
			return SUCCESS;
		} else {
			CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
			// Delivery Address check failed
			return ERROR;
		}
	}

	/*public String executeEx() throws Exception {
	    //ALLOW_ALL = true;
		LOGGER.info("Inside executeEx");
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		ContactInfo cInfo = new ContactInfo(request);
		AccountInfo aInfo = new AccountInfo(request);
		AddressInfo addInfo = new AddressInfo(request);
		
		if("true".equals(request.getParameter("DELIVERYADDRESS"))) {
			if(session.getAttribute("SOCIALCONTACTINFO") != null && session.getAttribute("SOCIALACCOUNTINFO") != null) {		
				cInfo = (ContactInfo) session.getAttribute("SOCIALCONTACTINFO");
				aInfo = (AccountInfo) session.getAttribute("SOCIALACCOUNTINFO");				
			} 
			if(user.getSelectedServiceType().getName().equals(EnumServiceType.CORPORATE.getName())) {				
				addInfo.validate(actionResult);
				cInfo.workPhone = NVL.apply(request.getParameter("busphone"), "").trim();
				cInfo.workPhoneExt = NVL.apply(request.getParameter("busphoneext"), "").trim();
			}
		}
		
		if("true".equals(request.getParameter("LITESIGNUP"))) {
			if(session.getAttribute("LITECONTACTINFO") != null && session.getAttribute("LITEACCOUNTINFO") != null) {		
				cInfo = (ContactInfo) session.getAttribute("LITECONTACTINFO");
				aInfo = (AccountInfo) session.getAttribute("LITEACCOUNTINFO");				
			} 
			if(user.getSelectedServiceType().getName().equals(EnumServiceType.CORPORATE.getName())) {				
				addInfo.validate(actionResult);
				cInfo.workPhone = NVL.apply(request.getParameter("busphone"), "").trim();
				cInfo.workPhoneExt = NVL.apply(request.getParameter("busphoneext"), "").trim();
			}
			this.validateLiteSignup();
			
		}
		else
		{
			if(session.getAttribute("SOCIALONLYACCOUNT_SKIP_VALIDATION") != null){
				session.setAttribute("SOCIALONLYACCOUNT_SKIP_VALIDATION", null);
			} else{
				aInfo.validateEx(actionResult);
				cInfo.validateEx(actionResult);
			}

		}
			
		

		//EnumServiceType serviceType = addInfo.getAddressType();
		
		
		if(session.getAttribute("REFERRALNAME") != null ) {
			
			if("Enter your first name".equals(cInfo.firstName)) {
				actionResult.addError(new ActionError(EnumUserInfoName.DLV_FIRST_NAME.getCode(),SystemMessageList.MSG_REQUIRED));
			}
			
			if("Enter your last name".equals(cInfo.lastName)) {
				actionResult.addError(new ActionError(EnumUserInfoName.DLV_LAST_NAME.getCode(),SystemMessageList.MSG_REQUIRED));
			}
			
			if("Answer".equals(aInfo.passwordHint)) {
				actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD_HINT.getCode(),SystemMessageList.MSG_REQUIRED));
			}
		}

	
	
		if (!actionResult.isSuccess() && !ALLOW_ALL) {
			LOGGER.info("ActionResult not succeed");
			
			 for(ActionError error : actionResult.getErrors()) {
				 LOGGER.error(error.getDescription()); }
			
			return ERROR;
		}
		
		ErpCustomerModel erpCustomer = aInfo.getErpCustomerModel();
		ErpCustomerInfoModel customerInfo = new ErpCustomerInfoModel();
		aInfo.decorateCustomerInfo(customerInfo);
		cInfo.decorateCustomerInfo(customerInfo);
						
		if(session.getAttribute("REFERRALNAME") != null ) {
			
			//Check for new rule. Reject user registration if a same FirstName + LastName + Zipcode 
			//combo already exists in the database.
			if(!FDReferralManager.isUniqueFNLNZipCombo(cInfo.firstName, cInfo.lastName, user.getAddress().getZipCode(), null)) {
				//record the error
				FDReferralManager.storeFailedAttempt(customerInfo.getEmail(),"", user.getAddress().getZipCode(),customerInfo.getFirstName(),customerInfo.getLastName(), (String) session.getAttribute("REFERRALNAME"),"FNLNZipCode Match");
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(),"You already have an account and are ineligible for this referral offer. Please <a href=\'/login/login_main.jsp\'>log in</a> to start shopping or call Customer Service for assistance."));
				session.setAttribute("MSG_FOR_LOGIN_PAGE", "You already have an account and are ineligible for this referral offer. Please log in to start shopping or call Customer Service for assistance.");
				return ERROR;
			}
		}

		//
		// Scrub the delivery address contained within the request
		// we'll bail out here if the
		//
		 
		//String address1 = request.getParameter("address1");
		//boolean isPartialDelivery =  address1 != null && address1.length() > 0;
		EnumServiceType serviceType = user.getSelectedServiceType();
		AddressModel address = user.getAddress();
		//Address will not be null when user signs up for a Partial Delivery address
		if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0) {
			// VALIDATE DELIVERY ADDRESS to see if service restricted
		
			address.setServiceType(user.getSelectedServiceType());
			FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByAddress(address);
			
			this.reclassifyUser(user, address, serviceType, serviceResult);
		} else {
			//Directly from Zip Check page
			String zipCode = user.getZipCode();
			FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, 
					(user.getUserContext()!=null 
					&& user.getUserContext().getStoreContext()!=null)?user.getUserContext().getStoreContext().getEStoreId():EnumEStoreId.FD);
	        AddressModel addr = new AddressModel();
	        addr.setZipCode(zipCode);
        	this.reclassifyUser(user, addr,serviceType , serviceResult);
		}
		
		
			//
			// Absence of an FDIdentity in session means this is a new registration.
			// Presence of an FDIdentity might indicate a registered user but failed predicate function
			// (like adding a credit card in CallCenter), so don't attempt to re-register if one is found.
			//
			if (user.getIdentity() == null) {
				
				customerInfo.setRegRefTrackingCode(user.getLastRefTrackingCode());
				// changes done by gopal
				customerInfo.setReferralProgId(user.getLastRefProgId());
				customerInfo.setReferralProgInvtId(user.getLastRefProgInvtId());
				
				
				customerInfo.setFdTcAgree("X");
				customerInfo.setFdTcAgreeDate(new Date());
				
				erpCustomer.setCustomerInfo(customerInfo);
				ErpAddressModel erpAddress = null;
				// FDX-1873 - Show timeslots for anonymous address
				if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0 && !address.isCustomerAnonymousAddress()) {//Only true when customer came from partial zip check page in IPhone.
					erpAddress = new ErpAddressModel(address);
					erpAddress.setFirstName(customerInfo.getFirstName());
					erpAddress.setLastName(customerInfo.getLastName());
					erpAddress.setPhone(customerInfo.getHomePhone());
					if("true".equals(request.getParameter("LITESIGNUP"))) {
						if(user.getSelectedServiceType().getName().equals(EnumServiceType.CORPORATE.getName())) {
							erpAddress.setPhone(new PhoneNumber(NVL.apply(request.getParameter("busphone"), "").trim()));
						}
					}
					erpAddress.setAddressInfo(address.getAddressInfo());
					erpAddress.setServiceType(serviceType);
					erpCustomer.addShipToAddress(erpAddress);
					if(serviceType.getName().equals(EnumServiceType.CORPORATE.getName())) {
						erpAddress.setCompanyName(addInfo.getCompanyName());
					}
				} else {
					erpAddress=new ErpAddressModel();
					erpAddress.setFirstName(customerInfo.getFirstName());
					erpAddress.setLastName(customerInfo.getLastName());
					erpAddress.setPhone(customerInfo.getHomePhone());
					//Need to set dummy sap billing info for SAP processing.
					erpAddress.setAddress1("23-30 borden ave");
					erpAddress.setCity("Long Island City");
					erpAddress.setState("NY");
					erpAddress.setCountry("US");
					erpAddress.setZipCode("11101");

					//save original zip code before it's overwritten by dummy value
					CmRegistrationTag.setRegistrationOrigZipCode(session, user.getZipCode());
					
					
					 //Alternatively we can pass the actual city,state and zipcode to SAP.
					 
					//Lookup state and city by zipcode.
					/*
					StateCounty scinfo = FDDeliveryManager.getInstance().lookupStateCountyByZip(addInfo.getZipCode());
					erpAddress.setCity(scinfo.getCity());
					erpAddress.setState(scinfo.getState());
					erpAddress.setCountry("US");
					erpAddress.setZipCode(addInfo.getZipCode());
					
					erpAddress.setServiceType(serviceType);
					erpCustomer.setSapBillToAddress(erpAddress);
				}
	
				FDCustomerModel fdCustomer = new FDCustomerModel();
	
				fdCustomer.setPasswordHint(aInfo.getPasswordHint());
				fdCustomer.setDepotCode(user.getDepotCode());
				fdCustomer.setDepotCode(user.getDepotCode());
	
				FDSurveyResponse survey = aInfo.getMarketingSurvey(new SurveyKey(EnumSurveyType.REGISTRATION_SURVEY, serviceType), request);
	
			try {
					LOGGER.info("Entering final Registration.");
					FDIdentity regIdent = this.doRegistration(fdCustomer, erpCustomer, survey, serviceType);
					//user.getShoppingCart().setZoneInfo(zoneInfo);
					user.setIdentity(regIdent);
					user.invalidateCache();
					user.isLoggedIn(true);
					if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0) {
						//update user's zip only if a valid address is supplied. Without this check user's zip will be updated to default zip 11101
						user.setZipCode(erpAddress.getZipCode());
					}
					if(address != null) {
						//This is from partial zip check page from where we will have a valid address.
						user.setSelectedServiceType(AddressUtil.getDeliveryServiceType(erpAddress));
						//Added the following line for zone pricing to keep user service type up-to-date.
						user.setZPServiceType(AddressUtil.getDeliveryServiceType(erpAddress));
					} else {
						//This is from regular zip Check oage.
						user.setSelectedServiceType(serviceType);
						user.setZPServiceType(serviceType);
					}
					user.updateUserState();
					//Set the Default Delivery pass status.
					FDUserDlvPassInfo dlvpassInfo = new FDUserDlvPassInfo(EnumDlvPassStatus.NONE, null, null, null,0,0,0,false,0,null,0,null);
					user.getUser().setDlvPassInfo(dlvpassInfo);
					user.getUser().setAssignedCustomerParams(FDCustomerManager.getAssignedCustomerParams(user.getUser()));
					session.setAttribute(SessionName.USER, user);
					
					// Code for merging social network this the newly registered
					// account.
					@SuppressWarnings("unchecked")
					HashMap<String, String> socialUser = (HashMap<String, String>) session
							.getAttribute(SessionName.SOCIAL_USER);
					
					//String lastPage = (String)session.getAttribute("lastpage");
					
					// For mobile api
			
					String userToken = (String)request.getAttribute("userToken");
					String provider= (String)request.getAttribute("provider");
					
					if(userToken != null && userToken.length() > 0 && provider != null && provider.length() > 0)
					{
						SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
						if(userToken != null)
							socialUser = socialProvider.getSocialUserProfileByUserToken(userToken, provider);
						
						
					}
					
					
					if(socialUser != null && socialUser.get("email") != null  && lastPage.equalsIgnoreCase("signup_lite_social")  )
					{
						LOGGER.info("socialUser email:"+socialUser.get("email"));
						LOGGER.info("user email:"+ user.getUserId());
						//LOGGER.info("last page:"+ lastPage);
						
						try {

							FDSocialManager.mergeSocialAccountWithUser(
									socialUser.get("email"),
									socialUser.get("userToken"),
									socialUser.get("identityToken"),
									socialUser.get("provider"),
									socialUser.get("displayName"),
									socialUser.get("preferredUsername"),
									socialUser.get("email"), socialUser.get("emailVerified"));

						} catch (FDResourceException e1) {
							LOGGER.error("merge:" + e1.getMessage());
						}
						//session.setAttribute("lastpage","socialregistration");
					}
				
				} catch (ErpDuplicateUserIdException de) {
					LOGGER.warn("User registration failed due to duplicate id", de);
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_UNIQUE_USERNAME));
				}
	
			}
		
		return SUCCESS;
	
	}
	*/

	public String executeEx() throws Exception {
	    //ALLOW_ALL = true;
		LOGGER.info("Inside executeEx");
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		ContactInfo cInfo = new ContactInfo(request);
		AccountInfo aInfo = new AccountInfo(request);
		AddressInfo addInfo = new AddressInfo(request);
		
		if("true".equals(request.getParameter("LITESIGNUP"))) {
			if(session.getAttribute("LITECONTACTINFO") != null && session.getAttribute("LITEACCOUNTINFO") != null) {		
				cInfo = (ContactInfo) session.getAttribute("LITECONTACTINFO");
				aInfo = (AccountInfo) session.getAttribute("LITEACCOUNTINFO");				
			} 
			if(user.getSelectedServiceType().getName().equals(EnumServiceType.CORPORATE.getName())) {				
				addInfo.validateEx(actionResult); //just company name and zip code
				cInfo.workPhone = NVL.apply(request.getParameter("busphone"), "").trim();
				cInfo.workPhoneExt = NVL.apply(request.getParameter("busphoneext"), "").trim();
			}
			this.validateLiteSignup();
			
		} else {
			aInfo.validateEx(actionResult);
			cInfo.validateEx(actionResult);
		}
			
		
		if(!(StringUtils.isEmpty(request.getParameter("source")) || request.getParameter("source").equals(EnumExternalLoginSource.SOCIAL.value()))){
			AccountUtil.validatePassword(actionResult, 
					NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "").trim(),
					NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "").trim());
		}
		

		EnumServiceType serviceType = addInfo.getAddressType();
		
		// validate captcha if it's enabled
		boolean isCaptchaSuccess = CaptchaUtil.validateCaptcha(request.getParameter("g-recaptcha-response"), request.getRemoteAddr(), CaptchaType.SIGN_UP, session, SessionName.SIGNUP_ATTEMPT, FDStoreProperties.getMaxInvalidSignUpAttempt());
		if (!isCaptchaSuccess) {
			actionResult.addError(new ActionError("captcha", SystemMessageList.MSG_INVALID_CAPTCHA));
		}
		if(session.getAttribute("CLICKID") != null ) {
			
			if("Enter your first name".equals(cInfo.firstName)) {
				actionResult.addError(new ActionError(EnumUserInfoName.DLV_FIRST_NAME.getCode(),SystemMessageList.MSG_REQUIRED));
			}
			
			if("Enter your last name".equals(cInfo.lastName)) {
				actionResult.addError(new ActionError(EnumUserInfoName.DLV_LAST_NAME.getCode(),SystemMessageList.MSG_REQUIRED));
			}
			
			/*if("Answer".equals(aInfo.passwordHint)) {
				actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD_HINT.getCode(),SystemMessageList.MSG_REQUIRED));
			}*/
		}

	
	
		if (!actionResult.isSuccess() && !ALLOW_ALL) {
			LOGGER.info("ActionResult not succeed");
			CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
			
			 for(ActionError error : actionResult.getErrors()) {
				 LOGGER.error(error.getDescription()); }
			
			return ERROR;
		}
		
		ErpCustomerModel erpCustomer = aInfo.getErpCustomerModel();
		ErpCustomerInfoModel customerInfo = new ErpCustomerInfoModel();
		aInfo.decorateCustomerInfo(customerInfo);
		cInfo.decorateCustomerInfo(customerInfo);
		
		if(session.getAttribute("CLICKID") != null ) {
			
			//Check for new rule. Reject user registration if a same FirstName + LastName + Zipcode 
			//combo already exists in the database.
			if(!FDReferralManager.isUniqueFNLNZipCombo(cInfo.firstName, cInfo.lastName, user.getAddress().getZipCode(), null)) {
				//record the error
				FDReferralManager.storeFailedAttempt(customerInfo.getEmail(),"", user.getAddress().getZipCode(),customerInfo.getFirstName(),customerInfo.getLastName(), (String) session.getAttribute("CLICKID"),"FNLNZipCode Match");
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(),"You already have an account and are ineligible for this referral offer. Please log in to start shopping or call Customer Service for assistance."));
				session.setAttribute("MSG_FOR_LOGIN_PAGE", "You already have an account and are ineligible for this referral offer. Please log in to start shopping or call Customer Service for assistance.");
				CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
				return ERROR;
			}
		}

		//
		// Scrub the delivery address contained within the request
		// we'll bail out here if the
		//
		 
		//String address1 = request.getParameter("address1");
		//boolean isPartialDelivery =  address1 != null && address1.length() > 0;
		serviceType = user.getSelectedServiceType();
		AddressModel address = user.getAddress();
		//Address will not be null when user signs up for a Partial Delivery address
		if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0) {
			// VALIDATE DELIVERY ADDRESS to see if service restricted
		
			address.setServiceType(user.getSelectedServiceType());
			FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByAddress(address);
			
			this.reclassifyUser(user, address, serviceType, serviceResult);
		} else {
			//Directly from Zip Check page
			String zipCode = user.getZipCode();
			FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode);
	        AddressModel addr = new AddressModel();
	        addr.setZipCode(zipCode);
        	this.reclassifyUser(user, addr,serviceType , serviceResult);
		}
		
		
			//
			// Absence of an FDIdentity in session means this is a new registration.
			// Presence of an FDIdentity might indicate a registered user but failed predicate function
			// (like adding a credit card in CallCenter), so don't attempt to re-register if one is found.
			//
			if (user.getIdentity() == null) {
				
				customerInfo.setRegRefTrackingCode(user.getLastRefTrackingCode());
				// changes done by gopal
				customerInfo.setReferralProgId(user.getLastRefProgId());
				customerInfo.setReferralProgInvtId(user.getLastRefProgInvtId());
				customerInfo.setFdTcAgree("X");
				
				erpCustomer.setCustomerInfo(customerInfo);
				ErpAddressModel erpAddress = null;
				if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0) {//Only true when customer came from partial zip check page in IPhone.
					erpAddress = new ErpAddressModel(address);
					erpAddress.setFirstName(customerInfo.getFirstName());
					erpAddress.setLastName(customerInfo.getLastName());
					erpAddress.setPhone(customerInfo.getHomePhone());
					if("true".equals(request.getParameter("LITESIGNUP"))) {
						if(user.getSelectedServiceType().getName().equals(EnumServiceType.CORPORATE.getName())) {
							erpAddress.setPhone(new PhoneNumber(NVL.apply(request.getParameter("busphone"), "").trim()));
						}
					}
					erpAddress.setAddressInfo(address.getAddressInfo());
					erpAddress.setServiceType(serviceType);
					erpCustomer.addShipToAddress(erpAddress);
					if(serviceType.getName().equals(EnumServiceType.CORPORATE.getName())) {
						erpAddress.setCompanyName(addInfo.getCompanyName());
					}
				} else {
					erpAddress=new ErpAddressModel();
					erpAddress.setFirstName(customerInfo.getFirstName());
					erpAddress.setLastName(customerInfo.getLastName());
					erpAddress.setPhone(customerInfo.getHomePhone());
					//Need to set dummy sap billing info for SAP processing.
					erpAddress.setAddress1("23-30 borden ave");
					erpAddress.setCity("Long Island City");
					erpAddress.setState("NY");
					erpAddress.setCountry("US");
					erpAddress.setZipCode("11101");
					
                erpAddress.setServiceType(serviceType);
					erpCustomer.setSapBillToAddress(erpAddress);
				}
	
				FDCustomerModel fdCustomer = new FDCustomerModel();
	
				fdCustomer.setPasswordHint(aInfo.getPasswordHint());
				fdCustomer.setDepotCode(user.getDepotCode());
				fdCustomer.setDepotCode(user.getDepotCode());

				fdCustomer.getCustomerEStoreModel().setRafClickId((String) session.getAttribute("CLICKID"));
				fdCustomer.getCustomerEStoreModel().setRafPromoCode((String) session.getAttribute("COUPONCODE"));
	
				FDSurveyResponse survey = aInfo.getMarketingSurvey(new SurveyKey(EnumSurveyType.REGISTRATION_SURVEY, serviceType), request);
				
				
	
			try {
					LOGGER.info("Entering final Registration.");
					FDIdentity regIdent = this.doRegistration(fdCustomer, erpCustomer, survey, serviceType);
					//user.getShoppingCart().setZoneInfo(zoneInfo);
					user.setIdentity(regIdent);
					user.invalidateCache();
					user.isLoggedIn(true);
					if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0) {
						//update user's zip only if a valid address is supplied. Without this check user's zip will be updated to default zip 11101
						user.setZipCode(erpAddress.getZipCode());
					}
						//This is from regular zip Check oage.
						user.setSelectedServiceType(serviceType);
						user.setZPServiceType(serviceType);
					user.updateUserState();
					//Set the Default Delivery pass status.
					FDUserDlvPassInfo dlvpassInfo = new FDUserDlvPassInfo(EnumDlvPassStatus.NONE, null, null, null, 0.0, null,0,0,0,false,0,null,0,null,null);
					user.getUser().setDlvPassInfo(dlvpassInfo);
					user.getUser().setAssignedCustomerParams(FDCustomerManager.getAssignedCustomerParams(user.getUser()));
					//APPDEV-4381 : leagal terms
					user.setTcAcknowledge(true);
					session.setAttribute(SessionName.USER, user);
					
					// Code for merging social network this the newly registered
					// account.
					@SuppressWarnings("unchecked")
					HashMap<String, String> socialUser = (HashMap<String, String>) session
							.getAttribute(SessionName.SOCIAL_USER);
					
					//String lastPage = (String)session.getAttribute("lastpage");
					
					// For mobile api
			
					String userToken = (String)request.getAttribute("userToken");
					String provider= (String)request.getAttribute("provider");
					
					if(userToken != null && userToken.length() > 0 && provider != null && provider.length() > 0)
					{
						SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
						if(userToken != null)
							socialUser = socialProvider.getSocialUserProfileByUserToken(userToken, provider);
						
						
					}
					
					
					if(socialUser != null && socialUser.get("email") != null /* && lastPage.equalsIgnoreCase("signup_lite_social") */ )
					{
						LOGGER.info("socialUser email:"+socialUser.get("email"));
						LOGGER.info("user email:"+ user.getUserId());
						//LOGGER.info("last page:"+ lastPage);
						
						try {

							ExternalAccountManager.linkUserTokenToUserId(
									regIdent.getErpCustomerPK(),
									socialUser.get("email"),
									socialUser.get("userToken"),
									socialUser.get("identityToken"),
									socialUser.get("provider"),
									socialUser.get("displayName"),
									socialUser.get("preferredUsername"),
									socialUser.get("email"), socialUser.get("emailVerified"));

						} catch (FDResourceException e1) {
							LOGGER.error("merge:" + e1.getMessage());
						}
						//session.setAttribute("lastpage","socialregistration");
					}
				if (actionResult.isSuccess()) {
					user.setJustSignedUp(true);
				//	session.setAttribute("regSuccess", true);
				}
				} catch (ErpDuplicateUserIdException de) {
					LOGGER.warn("User registration failed due to duplicate id", de);
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_UNIQUE_USERNAME));
				}catch (ErpFraudException fe) {
					LOGGER.warn("User registration failed due to ", fe);
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), fe.getFraudReason().getDescription()));
					return ERROR;
				}
	
			}
		if (actionResult.isFailure()) {
			CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
		}
		return SUCCESS;
		
	}
	
	
/*	public String executeEx() throws Exception {
	    //ALLOW_ALL = true;
		LOGGER.info("Inside executeEx");
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		ContactInfo cInfo = new ContactInfo(request);
		AccountInfo aInfo = new AccountInfo(request);
		AddressInfo addInfo = new AddressInfo(request);
		
		if("true".equals(request.getParameter("LITESIGNUP"))) {
			if(session.getAttribute("LITECONTACTINFO") != null && session.getAttribute("LITEACCOUNTINFO") != null) {		
				cInfo = (ContactInfo) session.getAttribute("LITECONTACTINFO");
				aInfo = (AccountInfo) session.getAttribute("LITEACCOUNTINFO");				
			} 
			if(user.getSelectedServiceType().getName().equals(EnumServiceType.CORPORATE.getName())) {				
				addInfo.validate(actionResult);
				cInfo.workPhone = NVL.apply(request.getParameter("busphone"), "").trim();
				cInfo.workPhoneExt = NVL.apply(request.getParameter("busphoneext"), "").trim();
			}
			this.validateLiteSignup();
			
		}
		else
		{
			aInfo.validateEx(actionResult);
			cInfo.validateEx(actionResult);
		}
			
		

		//EnumServiceType serviceType = addInfo.getAddressType();
		
		
		if(session.getAttribute("CLICKID") != null ) {
			
			if("Enter your first name".equals(cInfo.firstName)) {
				actionResult.addError(new ActionError(EnumUserInfoName.DLV_FIRST_NAME.getCode(),SystemMessageList.MSG_REQUIRED));
			}
			
			if("Enter your last name".equals(cInfo.lastName)) {
				actionResult.addError(new ActionError(EnumUserInfoName.DLV_LAST_NAME.getCode(),SystemMessageList.MSG_REQUIRED));
			}
			
			if("Answer".equals(aInfo.passwordHint)) {
				actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD_HINT.getCode(),SystemMessageList.MSG_REQUIRED));
			}
		}

	
	
		if (!actionResult.isSuccess() && !ALLOW_ALL) {
			LOGGER.info("ActionResult not succeed");
			
			 for(ActionError error : actionResult.getErrors()) {
				 LOGGER.error(error.getDescription()); }
			
			return ERROR;
		}
		
		ErpCustomerModel erpCustomer = aInfo.getErpCustomerModel();
		ErpCustomerInfoModel customerInfo = new ErpCustomerInfoModel();
		aInfo.decorateCustomerInfo(customerInfo);
		cInfo.decorateCustomerInfo(customerInfo);
		
		if(session.getAttribute("CLICKID") != null ) {
			
			//Check for new rule. Reject user registration if a same FirstName + LastName + Zipcode 
			//combo already exists in the database.
			if(!FDReferralManager.isUniqueFNLNZipCombo(cInfo.firstName, cInfo.lastName, user.getAddress().getZipCode(), null)) {
				//record the error
				FDReferralManager.storeFailedAttempt(customerInfo.getEmail(),"", user.getAddress().getZipCode(),customerInfo.getFirstName(),customerInfo.getLastName(), (String) session.getAttribute("CLICKID"),"FNLNZipCode Match");
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(),"You already have an account and are ineligible for this referral offer. Please <a href=\'/login/login_main.jsp\'>log in</a> to start shopping or call Customer Service for assistance."));
				session.setAttribute("MSG_FOR_LOGIN_PAGE", "You already have an account and are ineligible for this referral offer. Please log in to start shopping or call Customer Service for assistance.");
				return ERROR;
			}
		}

		//
		// Scrub the delivery address contained within the request
		// we'll bail out here if the
		//
		 
		//String address1 = request.getParameter("address1");
		//boolean isPartialDelivery =  address1 != null && address1.length() > 0;
		EnumServiceType serviceType = user.getSelectedServiceType();
		AddressModel address = user.getAddress();
		//Address will not be null when user signs up for a Partial Delivery address
		if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0) {
			// VALIDATE DELIVERY ADDRESS to see if service restricted
		
			address.setServiceType(user.getSelectedServiceType());
			FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByAddress(address);
			
			this.reclassifyUser(user, address, serviceType, serviceResult);
		} else {
			//Directly from Zip Check page
			String zipCode = user.getZipCode();
			FDDeliveryServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode);
	        AddressModel addr = new AddressModel();
	        addr.setZipCode(zipCode);
        	this.reclassifyUser(user, addr,serviceType , serviceResult);
		}
		
		
			//
			// Absence of an FDIdentity in session means this is a new registration.
			// Presence of an FDIdentity might indicate a registered user but failed predicate function
			// (like adding a credit card in CallCenter), so don't attempt to re-register if one is found.
			//
			if (user.getIdentity() == null) {
				
				customerInfo.setRegRefTrackingCode(user.getLastRefTrackingCode());
				// changes done by gopal
				customerInfo.setReferralProgId(user.getLastRefProgId());
				customerInfo.setReferralProgInvtId(user.getLastRefProgInvtId());
				
				erpCustomer.setCustomerInfo(customerInfo);
				ErpAddressModel erpAddress = null;
				if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0) {//Only true when customer came from partial zip check page in IPhone.
					erpAddress = new ErpAddressModel(address);
					erpAddress.setFirstName(customerInfo.getFirstName());
					erpAddress.setLastName(customerInfo.getLastName());
					erpAddress.setPhone(customerInfo.getHomePhone());
					if("true".equals(request.getParameter("LITESIGNUP"))) {
						if(user.getSelectedServiceType().getName().equals(EnumServiceType.CORPORATE.getName())) {
							erpAddress.setPhone(new PhoneNumber(NVL.apply(request.getParameter("busphone"), "").trim()));
						}
					}
					erpAddress.setAddressInfo(address.getAddressInfo());
					erpAddress.setServiceType(serviceType);
					erpCustomer.addShipToAddress(erpAddress);
					if(serviceType.getName().equals(EnumServiceType.CORPORATE.getName())) {
						erpAddress.setCompanyName(addInfo.getCompanyName());
					}
				} else {
					erpAddress=new ErpAddressModel();
					erpAddress.setFirstName(customerInfo.getFirstName());
					erpAddress.setLastName(customerInfo.getLastName());
					erpAddress.setPhone(customerInfo.getHomePhone());
					//Need to set dummy sap billing info for SAP processing.
					erpAddress.setAddress1("23-30 borden ave");
					erpAddress.setCity("Long Island City");
					erpAddress.setState("NY");
					erpAddress.setCountry("US");
					erpAddress.setZipCode("11101");

					//save original zip code before it's overwritten by dummy value
					CmRegistrationTag.setRegistrationOrigZipCode(session, user.getZipCode());
					
					
					 //Alternatively we can pass the actual city,state and zipcode to SAP.
					 
					//Lookup state and city by zipcode.
					
					StateCounty scinfo = FDDeliveryManager.getInstance().lookupStateCountyByZip(addInfo.getZipCode());
					erpAddress.setCity(scinfo.getCity());
					erpAddress.setState(scinfo.getState());
					erpAddress.setCountry("US");
					erpAddress.setZipCode(addInfo.getZipCode());
					
					erpAddress.setServiceType(serviceType);
					erpCustomer.setSapBillToAddress(erpAddress);
				}
	
				FDCustomerModel fdCustomer = new FDCustomerModel();
	
				fdCustomer.setPasswordHint(aInfo.getPasswordHint());
				fdCustomer.setDepotCode(user.getDepotCode());
				fdCustomer.setDepotCode(user.getDepotCode());
				// setting dummy valuess for testing
			//	fdCustomer.setRafClickId("dummyClickID");
			//	fdCustomer.setRafPromoCode("dummyPromoCode");
				fdCustomer.setRafClickId((String) session.getAttribute("CLICKID"));
				fdCustomer.setRafPromoCode((String) session.getAttribute("COUPONCODE"));
	
				FDSurveyResponse survey = aInfo.getMarketingSurvey(new SurveyKey(EnumSurveyType.REGISTRATION_SURVEY, serviceType), request);
	
			try {
					LOGGER.info("Entering final Registration.");
					FDIdentity regIdent = this.doRegistration(fdCustomer, erpCustomer, survey, serviceType);
					//user.getShoppingCart().setZoneInfo(zoneInfo);
					user.setIdentity(regIdent);
					user.invalidateCache();
					user.isLoggedIn(true);
					if(address != null && address.getAddress1() != null && address.getAddress1().length() > 0) {
						//update user's zip only if a valid address is supplied. Without this check user's zip will be updated to default zip 11101
						user.setZipCode(erpAddress.getZipCode());
					}
					if(address != null) {
						//This is from partial zip check page from where we will have a valid address.
						user.setSelectedServiceType(AddressUtil.getDeliveryServiceType(erpAddress));
						//Added the following line for zone pricing to keep user service type up-to-date.
						user.setZPServiceType(AddressUtil.getDeliveryServiceType(erpAddress));
					} else {
						//This is from regular zip Check oage.
						user.setSelectedServiceType(serviceType);
						user.setZPServiceType(serviceType);
					}
					user.updateUserState();
					//Set the Default Delivery pass status.
					FDUserDlvPassInfo dlvpassInfo = new FDUserDlvPassInfo(EnumDlvPassStatus.NONE, null, null, null,0,0,0,false,0,null,0,null);
					user.getUser().setDlvPassInfo(dlvpassInfo);
					user.getUser().setAssignedCustomerParams(FDCustomerManager.getAssignedCustomerParams(user.getUser()));
					session.setAttribute(SessionName.USER, user);
					
					// Code for merging social network this the newly registered
					// account.
					@SuppressWarnings("unchecked")
					HashMap<String, String> socialUser = (HashMap<String, String>) session
							.getAttribute(SessionName.SOCIAL_USER);
					
					//String lastPage = (String)session.getAttribute("lastpage");
					
					// For mobile api
			
					String userToken = (String)request.getAttribute("userToken");
					String provider= (String)request.getAttribute("provider");
					
					if(userToken != null && userToken.length() > 0 && provider != null && provider.length() > 0)
					{
						SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
						if(userToken != null)
							socialUser = socialProvider.getSocialUserProfileByUserToken(userToken, provider);
						
						
					}
					
					
					if(socialUser != null && socialUser.get("email") != null  && lastPage.equalsIgnoreCase("signup_lite_social")  )
					{
						LOGGER.info("socialUser email:"+socialUser.get("email"));
						LOGGER.info("user email:"+ user.getUserId());
						//LOGGER.info("last page:"+ lastPage);
						
						try {

							ExternalAccountManager.linkUserTokenToUserId(
									regIdent.getErpCustomerPK(),
									socialUser.get("email"),
									socialUser.get("userToken"),
									socialUser.get("identityToken"),
									socialUser.get("provider"),
									socialUser.get("displayName"),
									socialUser.get("preferredUsername"),
									socialUser.get("email"), socialUser.get("emailVerified"));

						} catch (FDResourceException e1) {
							LOGGER.error("merge:" + e1.getMessage());
						}
						//session.setAttribute("lastpage","socialregistration");
					}
				
				} catch (ErpDuplicateUserIdException de) {
					LOGGER.warn("User registration failed due to duplicate id", de);
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_UNIQUE_USERNAME));
				}
	
			}
		
		return SUCCESS;
		
	}*/

	
	
	private void reclassifyUser(
		FDSessionUser user,
		AddressModel address,
		EnumServiceType serviceType,
		FDDeliveryServiceSelectionResult serviceResult) throws FDResourceException {
		
		HttpSession session = this.getWebActionContext().getSession();

		/*if (serviceResult == null && ALLOW_ALL) {
		    serviceResult = new DlvServiceSelectionResult();
		    serviceResult.addServiceStatus(serviceType, EnumDeliveryStatus.DELIVER);
		}*/
		
		//user.setZipCode(address.getZipCode());
		user.setAddress(address);

		EnumDeliveryStatus status = serviceResult.getServiceStatus(serviceType);
		
		if (EnumDeliveryStatus.DELIVER.equals(status) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status) || EnumDeliveryStatus.COS_ENABLED.equals(status)) {
			user.setSelectedServiceType(serviceType);
			//Added the following line for zone pricing to keep user service type up-to-date.
			user.setZPServiceType(serviceType);
		} else {
			user.setSelectedServiceType(EnumServiceType.PICKUP);
			//Added the following line for zone pricing to keep user service type up-to-date.
			user.setZPServiceType(EnumServiceType.PICKUP);
		}
		user.setAvailableServices(serviceResult.getAvailableServices());
		
		session.setAttribute(SessionName.USER, user);
		FDCustomerManager.storeUser(user.getUser());
	}

	protected FDIdentity doRegistration(
		FDCustomerModel fdCustomer,
		ErpCustomerModel erpCustomer,
		FDSurveyResponse survey,
		EnumServiceType serviceType) throws ErpDuplicateUserIdException,ErpDuplicatePaymentMethodException,
		ErpPaymentMethodException,FDResourceException,ErpFraudException {
		
		LOGGER.debug("RegistrationAction: In doRegistration");

		HttpSession session = this.getWebActionContext().getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		RegistrationResult regResult = FDCustomerManager.register(AccountActivityUtil.getActionInfo(session), erpCustomer,
			fdCustomer, user.getCookie(), user.isPickupOnly(), user.isEligibleForSignupPromotion(), survey, serviceType);
		FDIdentity identity = regResult.getIdentity();

		if (regResult.hasPossibleFraud()) {
			LOGGER.info("Possible Fraud condition. Allowing registration but will redirect to page with FRAUD WARNING.");
			this.setSuccessPage(this.fraudPage);
			LOGGER.debug("successPage re-set to " + this.fraudPage);
		}
		LOGGER.debug("RegistrationAction: doRegistration() " + identity);
		return identity;

	}
	
	public String validateLiteSignup() {
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();
		HttpSession session = this.getWebActionContext().getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		ContactInfo cInfo = new ContactInfo(request);
		AccountInfo aInfo = new AccountInfo(request);
		if("true".equals(request.getParameter("LITESIGNUP"))) {
			if(session.getAttribute("LITECONTACTINFO") != null && session.getAttribute("LITEACCOUNTINFO") != null) {		
				cInfo = (ContactInfo) session.getAttribute("LITECONTACTINFO");
				aInfo = (AccountInfo) session.getAttribute("LITEACCOUNTINFO");				
			} 
			if(user.getSelectedServiceType().getName().equals(EnumServiceType.CORPORATE.getName())) {
				cInfo.workPhone = NVL.apply(request.getParameter("busphone"), "").trim();
				cInfo.workPhoneExt = NVL.apply(request.getParameter("busphoneext"), "").trim();
			}
			
		}

		if(!"true".equals(request.getParameter("EXPRESSSIGNUP_SKIP_VALIDATION"))) {
			aInfo.validateExSLite(actionResult);
		}
		
//		if(!"true".equals(request.getParameter("LITESIGNUP"))) {
		if(!"true".equals(request.getParameter("EXPRESSSIGNUP_SKIP_VALIDATION"))) {
			cInfo.validateEx(actionResult);
		}
//		}
		
		if(!"true".equals(request.getParameter("EXPRESSSIGNUP_SKIP_VALIDATION"))) {
			AccountUtil.validatePasswordEx(actionResult, aInfo.password, aInfo.repeatPassword);	
		}
			
		try {
			if(FDCustomerManager.dupeEmailAddress(aInfo.emailAddress) != null) {
				
				if(FDStoreProperties.isSocialLoginEnabled()){ 
					if(FDStoreProperties.isSocialLoginEnabled() ){ 
						//actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(),SystemMessageList.MSG_UNIQUE_USERNAME_FOR_LSIGNUP_SOCIAL));	
						List<String> providers = ExternalAccountManager.getConnectedProvidersByUserId(aInfo.emailAddress);
						if(providers!=null && providers.size()!=0){
							String providersStr ="";
							for(String provider:providers){
							 providersStr = provider+","+providersStr;
							}
							actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(),(MessageFormat.format(SystemMessageList.MSG_SOCIAL_SOCIALONLY_ACCOUNT_CREATE, providersStr.substring(0,providersStr.length()-1)))));	
						} else {
							actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(),SystemMessageList.MSG_UNIQUE_USERNAME_FOR_LSIGNUP_SOCIAL));
						}
					}
				}else{
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(),SystemMessageList.MSG_UNIQUE_USERNAME_FOR_LSIGNUP));
				}
				
			}
		} catch (FDResourceException e) {			
		}
		
		if (!actionResult.isSuccess()) {
			return ERROR;
		}
		
		//store contactInfo and AccountInfo in session
		session.setAttribute("LITECONTACTINFO", cInfo);
		session.setAttribute("LITEACCOUNTINFO", aInfo);
		return SUCCESS;
	}
	
	public String validateSocialSignupEmail() {
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();
		
		AccountInfo aInfo = new AccountInfo(request);
		
		try {
			if(FDCustomerManager.dupeEmailAddress(aInfo.emailAddress) != null) {
				if(FDStoreProperties.isSocialLoginEnabled()){ 
					//actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(),SystemMessageList.MSG_UNIQUE_USERNAME_FOR_LSIGNUP_SOCIAL));	
					List<String> providers = ExternalAccountManager.getConnectedProvidersByUserId(aInfo.emailAddress);
					if(providers!=null && providers.size()!=0){
						String providersStr ="";
						for(String provider:providers){
						 providersStr = provider+","+providersStr;
						}
						actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(),(MessageFormat.format(SystemMessageList.MSG_SOCIAL_SOCIALONLY_ACCOUNT_CREATE,  providersStr.substring(0,providersStr.length()-1)))));	
					} else {
						actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(),SystemMessageList.MSG_UNIQUE_USERNAME_FOR_LSIGNUP_SOCIAL));	
					}
				}else{
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(),SystemMessageList.MSG_UNIQUE_USERNAME_FOR_LSIGNUP));	
				}
							
			}
		} catch (FDResourceException e) {			
		}
		
		if (!actionResult.isSuccess()) {
			return ERROR;
		}
		
		//store Email/Password in session
		HttpSession session = this.getWebActionContext().getSession();
		session.setAttribute("SOCIALACCOUNTINFO", aInfo);
		
		return SUCCESS;
	}
	
	public String validateSocialSignupFirstLast() {
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();

		ContactInfo cInfo = new ContactInfo(request);
		HttpSession session = this.getWebActionContext().getSession();
		
		if(request.getParameter("serviceType").equals("HOME")){
			cInfo.validate(actionResult,EnumServiceType.HOME);
		}
		else
		{
			cInfo.validate(actionResult,EnumServiceType.CORPORATE);
		}
		
		if (!actionResult.isSuccess()) {
			return ERROR;
		}
		
		//store contactInfo and AccountInfo in session
		session.setAttribute("SOCIALCONTACTINFO", cInfo);
		
		return SUCCESS;
	}
	class ContactInfo {

		private String title;
		private String firstName;
		private String lastName;

		private String department;
		private String employeeId;

		private String homePhone;
		private String homePhoneExt;
		private String workPhone;
		private String workPhoneExt;
		private String cellPhone;
		private String cellPhoneExt;

		public ContactInfo(HttpServletRequest request) {
			this.initialize(request);
		}

		private void initialize(HttpServletRequest request) {

			this.title = NVL.apply(request.getParameter("title"), "");
			
			this.firstName = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "").trim();
			if((this.firstName !=null ) && this.firstName.trim() == "" && request.getAttribute("firstName") != null){
				this.firstName = (String)request.getAttribute("firstName");
			}
			this.lastName = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "").trim();
			if((this.lastName !=null ) && this.lastName.trim() == "" && request.getAttribute("lastName") != null){
				this.lastName = (String)request.getAttribute("lastName");
			}
			
			this.homePhone = NVL.apply(request.getParameter(EnumUserInfoName.DLV_HOME_PHONE.getCode()), "").trim();
			this.homePhoneExt = NVL.apply(request.getParameter("homephoneext"), "").trim();

			this.workPhone = NVL.apply(request.getParameter("busphone"), "").trim();
			this.workPhoneExt = NVL.apply(request.getParameter("busphoneext"), "").trim();

			this.cellPhone = NVL.apply(request.getParameter("cellphone"), "").trim();
			this.cellPhoneExt = NVL.apply(request.getParameter("cellphoneext"), "").trim();
			this.department = NVL.apply(request.getParameter("workDepartment"), "").trim();
			this.employeeId = NVL.apply(request.getParameter("employeeId"), "").trim();
		}

		public void validate(ActionResult actionResult, EnumServiceType serviceType) {
			actionResult.addError("".equals(this.lastName), EnumUserInfoName.DLV_LAST_NAME.getCode(),
				SystemMessageList.MSG_REQUIRED);
			
			actionResult.addError("".equals(this.firstName), EnumUserInfoName.DLV_FIRST_NAME.getCode(),
				SystemMessageList.MSG_REQUIRED);

			if ((regType == AccountUtil.DEPOT_USER || regType == AccountUtil.HOME_USER) || EnumServiceType.HOME.equals(serviceType)) {
				actionResult.addError("".equals(this.homePhone) || homePhone.length() < 10, EnumUserInfoName.DLV_HOME_PHONE
					.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			
			if (regType == AccountUtil.DEPOT_USER || EnumServiceType.CORPORATE.equals(serviceType)) {
				actionResult.addError("".equals(this.workPhone) || this.workPhone.length() < 10, "busphone",
					SystemMessageList.MSG_REQUIRED);
			}
			
			actionResult.addError(!validatePhoneNumber(this.homePhone), EnumUserInfoName.DLV_HOME_PHONE.getCode(), SystemMessageList.MSG_NUM_REQ );
			actionResult.addError(!validatePhoneNumber(this.workPhone), EnumUserInfoName.DLV_WORK_PHONE.getCode(), SystemMessageList.MSG_NUM_REQ );
			actionResult.addError(!validatePhoneNumber(this.cellPhone),  EnumUserInfoName.DLV_ALT_PHONE.getCode(), SystemMessageList.MSG_NUM_REQ );

			//TODO Logistics ReIntegration Task Check Depot Logic still valid.
			/*if (regType == AccountUtil.DEPOT_USER) {
				actionResult.addError("".equals(this.department), "workDepartment", SystemMessageList.MSG_REQUIRED);
				try {
					//getUser().getDepotCode();
					DlvDepotModel depot = FDDepotManager.getInstance().getDepot("");
					if (depot != null && depot.getRequireEmployeeId()) {
						actionResult.addError(employeeId == null || employeeId.length() < 1, "employeeId",
							SystemMessageList.MSG_REQUIRED);
					}
				} catch (FDResourceException fdre) {
					LOGGER.error("Difficulty looking up depot details", fdre);
					actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
				}
			}*/
		}
		
		public void validateEx(ActionResult actionResult) {
			actionResult.addError("".equals(this.lastName), EnumUserInfoName.DLV_LAST_NAME.getCode(),
				SystemMessageList.MSG_REQUIRED);
			
			actionResult.addError("".equals(this.firstName), EnumUserInfoName.DLV_FIRST_NAME.getCode(),
				SystemMessageList.MSG_REQUIRED);

		
		}
		
		public boolean validatePhoneNumber(String phoneNumber){
			phoneNumber = phoneNumber.trim();
			if(phoneNumber != null && !"".equals(phoneNumber) && phoneNumber.length() > 0 && phoneNumber.length() < 10){
				return false;
			}
			return true;
		}

		public void decorateCustomerInfo(ErpCustomerInfoModel customerInfo) {

			customerInfo.setTitle(this.title);
			customerInfo.setBusinessPhone(new PhoneNumber(this.workPhone, this.workPhoneExt));
			customerInfo.setCellPhone(new PhoneNumber(this.cellPhone, this.cellPhoneExt));
			if ( (customerInfo.getEmail() != null) && ( (this.firstName == null) || ("".equals(this.firstName.trim())) ) ){
				String fname = customerInfo.getEmail().substring(0, customerInfo.getEmail().indexOf("@"));
				customerInfo.setFirstName(fname);
			}else{
				if(this.firstName != null && this.firstName.indexOf("@") > 0) {
					customerInfo.setFirstName(this.firstName.substring(0, this.firstName.indexOf("@")));
				} else {
					customerInfo.setFirstName(this.firstName);
				}
			}
			if((customerInfo.getEmail() != null ) &&  ((this.lastName == null) || ("".equals(this.firstName.trim())))){
				String lname = customerInfo.getEmail().substring(0, customerInfo.getEmail().indexOf("@"));
				customerInfo.setLastName(lname);
			}else{
				customerInfo.setLastName(this.lastName);
			}						
			customerInfo.setHomePhone(new PhoneNumber(this.homePhone, this.homePhoneExt));
			customerInfo.setWorkDepartment(this.department);
			customerInfo.setEmployeeId(this.employeeId);
		}
	}

	static class AccountInfo {
		private String emailAddress;
		private String repeatEmailAddress;
		private String altEmailAddress;

		private String password;
		private String repeatPassword;
		private String passwordHint;

		private String deliveryInstructions;

		private boolean receiveNews;
		private String emailPreferenceLevel;
		private boolean plainTextEmail;
		private boolean termsAccepted;
		private boolean socialLoginOnly;

		private String companyNameSignup;

		public AccountInfo(HttpServletRequest request) {
			this.initialize(request);
		}


        private void initialize(HttpServletRequest request) {
        	
        	if(request.getSession().getAttribute("SOCIALONLYEMAIL") != null){
        		this.emailAddress = (String)request.getSession().getAttribute("SOCIALONLYEMAIL");
        		request.getSession().setAttribute("SOCIALONLYEMAIL",null);
        	}
        	else{
        		this.emailAddress = NVL.apply(request.getParameter(EnumUserInfoName.EMAIL.getCode()), "").trim();
        	}
			this.repeatEmailAddress = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode()), "").trim();
			this.altEmailAddress = NVL.apply(request.getParameter(EnumUserInfoName.ALT_EMAIL.getCode()), "").trim();
			if(request.getParameter("LITESIGNUP_SOCIAL") != null && "true".equals(request.getParameter("LITESIGNUP_SOCIAL"))
					&& (StringUtils.isEmpty(request.getParameter("source")) || EnumExternalLoginSource.SOCIAL.value().equalsIgnoreCase(request.getParameter("source"))))
			{	
				this.password="^0X!3X!X!1^";  //Dummy password for social login. will not be exposed to anyone. 
				this.socialLoginOnly = true;
			}			
			else if(request.getSession().getAttribute("SOCIALONLYACCOUNT") != null)
			{
				this.password="^0X!3X!X!1^";  //Dummy password for social login. will not be exposed to anyone. 
				this.socialLoginOnly = true;
				request.getSession().setAttribute("SOCIALONLYACCOUNT",null);
			}
			else 
				this.password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "").trim();
			this.repeatPassword = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_PASSWORD.getCode()), "").trim();
			/*this.passwordHint = NVL.apply(request.getParameter("password_hint"), "").trim();*/
			
            this.receiveNews = true;
            this.emailPreferenceLevel = "2";
            this.plainTextEmail = false;
			
			this.termsAccepted = request.getParameter("terms") != null;

			this.deliveryInstructions = NVL.apply(request.getParameter(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode()),
				"none").trim();
			

			this.companyNameSignup = NVL.apply(request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "").trim();
		}

		public void validate(ActionResult actionResult) {
			actionResult.addError("".equals(emailAddress), EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_REQUIRED);

			actionResult.addError(!actionResult.hasError(EnumUserInfoName.EMAIL.getCode())
				&& !EmailUtil.isValidEmailAddress(emailAddress), EnumUserInfoName.EMAIL.getCode(),
				SystemMessageList.MSG_EMAIL_FORMAT);

			
			if ("".equals(repeatEmailAddress)) {
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_REQUIRED));
			} else if (!emailAddress.equalsIgnoreCase(repeatEmailAddress)) {
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_REPEAT));
			}
			
			/*
			actionResult.addError(!"".equals(altEmailAddress) && !EmailUtil.isValidEmailAddress(altEmailAddress),
				EnumUserInfoName.ALT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_FORMAT);
			 */
			AccountUtil.validatePassword(actionResult, password, repeatPassword);
			
			
			//actionResult.addError("".equals(passwordHint), EnumUserInfoName.PASSWORD_HINT.getCode(), SystemMessageList.MSG_REQUIRED);

			    actionResult.addError(!termsAccepted, "terms", SystemMessageList.MSG_AGREEMENT_CHECK);
		}

		public void validateEx(ActionResult actionResult) {
			actionResult.addError("".equals(emailAddress), EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_REQUIRED);

			actionResult.addError(!actionResult.hasError(EnumUserInfoName.EMAIL.getCode())
				&& !EmailUtil.isValidEmailAddress(emailAddress), EnumUserInfoName.EMAIL.getCode(),
				SystemMessageList.MSG_EMAIL_FORMAT);
			
			/*
			if (!emailAddress.equalsIgnoreCase(repeatEmailAddress)) {
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_REPEAT));
			} else if ("".equals(repeatEmailAddress)) {
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_REQUIRED));
			} 

			actionResult.addError(!"".equals(altEmailAddress) && !EmailUtil.isValidEmailAddress(altEmailAddress),
				EnumUserInfoName.ALT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_FORMAT);
			
			AccountUtil.validatePasswordEx(actionResult, password, repeatPassword);
			*/
			/*actionResult
				.addError("".equals(passwordHint), EnumUserInfoName.PASSWORD_HINT.getCode(), SystemMessageList.MSG_REQUIRED);
			*/
			/*
			actionResult.addError(!termsAccepted, "terms", SystemMessageList.MSG_AGREEMENT_CHECK);
			*/
		}
		
		public void validateExSLite(ActionResult actionResult) {
			actionResult.addError("".equals(emailAddress), EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_REQUIRED);
			
			actionResult.addError("".equals(repeatEmailAddress), EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_REQUIRED);
			actionResult.addError(!emailAddress.equals(repeatEmailAddress), EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_EMAIL_REPEAT);

			actionResult.addError(!actionResult.hasError(EnumUserInfoName.EMAIL.getCode())
				&& !EmailUtil.isValidEmailAddress(emailAddress), EnumUserInfoName.EMAIL.getCode(),
				SystemMessageList.MSG_EMAIL_FORMAT);
			
			// APPDEV-4409 : Removing the PAssword Hint Validation.
			//actionResult
			//	.addError("".equals(passwordHint), EnumUserInfoName.PASSWORD_HINT.getCode(), SystemMessageList.MSG_REQUIRED);
			
		}

		
		
		public void decorateCustomerInfo(ErpCustomerInfoModel customerInfo) {
			customerInfo.setEmail(this.emailAddress);
			customerInfo.setUnsubscribeDate(this.receiveNews ? null : new java.util.Date());
			customerInfo.setAlternateEmail(altEmailAddress.trim());
			customerInfo.setReceiveNewsletter(this.receiveNews); // FD Email Preference
			customerInfo.setEmailPreferenceLevel(this.emailPreferenceLevel); // FDX Email Preference
			customerInfo.setEmailPlaintext(this.plainTextEmail);
			customerInfo.setCompanyNameSignup(this.companyNameSignup);
		}

		public ErpCustomerModel getErpCustomerModel() {
			ErpCustomerModel erpCustomer = new ErpCustomerModel();
			erpCustomer.setUserId(this.emailAddress);
			erpCustomer.setPasswordHash(MD5Hasher.hash(this.password));
			erpCustomer.setActive(true);
			erpCustomer.setSocialLoginOnly(this.socialLoginOnly);
			return erpCustomer;
		}

		public FDSurveyResponse getMarketingSurvey(SurveyKey surveyKey, HttpServletRequest request) {

			FDSurveyResponse surveyResponse = new FDSurveyResponse(null, surveyKey);
			FDSurvey survey = null;
			try {
				survey = FDSurveyFactory.getInstance().getSurvey(surveyKey);
			} catch (Exception e) {
				LOGGER.error("getMarketingSurvey SurveyKey=" + surveyKey, e);
			}
			if (survey != null) {
				for (FDSurveyQuestion q : survey.getQuestions()) {
				    String[] value = request.getParameterValues(q.getName());
				    if (value != null && value.length > 0) {
				        surveyResponse.addAnswer(q.getName(), value);
				    }
				}
			}
			
            return surveyResponse.getAnswers().isEmpty() ? null : surveyResponse;
		}

		public String getPasswordHint() {
			return this.passwordHint;
		}
	}

	private class AddressInfo {

		private String addressOrLocation;
		private String locationId;

		private EnumServiceType serviceType;
		private String companyName;
		private String street1;
		private String street2;
		private String apt;
		private String city;
		private String state;
		private String zipcode;
		
		public AddressInfo(HttpServletRequest request) {
			this.initialize(request);
		}

		private void initialize(HttpServletRequest request) {
			this.serviceType = NVL.apply(EnumServiceType.getEnum(request
				.getParameter(EnumUserInfoName.DLV_SERVICE_TYPE.getCode())), EnumServiceType.HOME);
			this.companyName = NVL.apply(request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "");
			this.street1 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim();
			this.street2 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_2.getCode()), "").trim();
			this.apt = NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim();
			this.city = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim();
			this.state = NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim();
			this.zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "").trim();
			this.addressOrLocation = NVL.apply(request.getParameter("selectAddressList"), "").trim();

			if (!"".equals(this.addressOrLocation) && addressOrLocation.indexOf("DEPOT_") > -1) {
				locationId = this.addressOrLocation.substring(6, addressOrLocation.length());
			}
		}

		public void validate(ActionResult result) {

			if (EnumServiceType.CORPORATE.equals(this.serviceType)) {
				result
					.addError("".equals(companyName), EnumUserInfoName.DLV_COMPANY_NAME.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			result.addError("".equals(street1), EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED);
			result.addError("".equals(city), EnumUserInfoName.DLV_CITY.getCode(), SystemMessageList.MSG_REQUIRED);

			if (state.length() < 2) {
				result.addError(new ActionError(EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED));
			} else {
				result.addError(!AddressUtil.validateState(state), EnumUserInfoName.DLV_STATE.getCode(),
					SystemMessageList.MSG_UNRECOGNIZE_STATE);
			}

			result.addError(zipcode.length() < 5, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED);

			if (regType == AccountUtil.DEPOT_USER) {
				result.addError(locationId == null || locationId.length() < 1, "locationId", SystemMessageList.MSG_REQUIRED);
			}

		}

		public void validateEx(ActionResult result) {

			if (EnumServiceType.CORPORATE.equals(this.serviceType)) {
				result
					.addError("".equals(companyName), EnumUserInfoName.DLV_COMPANY_NAME.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			//result.addError("".equals(street1), EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED);
			//result.addError("".equals(city), EnumUserInfoName.DLV_CITY.getCode(), SystemMessageList.MSG_REQUIRED);

//			if (state.length() < 2) {
//				result.addError(new ActionError(EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED));
//			} else {
//				result.addError(!AddressUtil.validateState(state), EnumUserInfoName.DLV_STATE.getCode(),
//					SystemMessageList.MSG_UNRECOGNIZE_STATE);
//			}

			result.addError(zipcode.length() < 5, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED);

//			if (regType == AccountUtil.DEPOT_USER) {
//				result.addError(locationId == null || locationId.length() < 1, "locationId", SystemMessageList.MSG_REQUIRED);
//			}

		}

		public AddressModel getDlvAddress() {
			AddressModel address = new AddressModel();
			address.setAddress1(street1);
			address.setAddress2(street2);
			address.setApartment(apt);
			address.setCity(city);
			address.setState(state);
			address.setZipCode(zipcode);
			return address;
		}

		public String getLocationId() {
			return this.locationId;
		}

		public EnumServiceType getAddressType() {
			return this.serviceType;
		}

		public String getCompanyName() {
			return this.companyName;
		}
		
		public String getZipCode() {
			return this.zipcode;
		}
		
	}	
	
	

}