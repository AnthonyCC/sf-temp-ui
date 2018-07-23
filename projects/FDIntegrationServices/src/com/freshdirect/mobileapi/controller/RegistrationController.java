package com.freshdirect.mobileapi.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.enums.CaptchaType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDActionNotAllowedException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.EmailPreferencesResult;
import com.freshdirect.mobileapi.controller.data.EnumResponseAdditional;
import com.freshdirect.mobileapi.controller.data.GoGreenPreferencesResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.MobilePreferencesResult;
import com.freshdirect.mobileapi.controller.data.OrderMobileNumberRequest;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.EmailPreferenceRequest;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLinkRequest;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountRegisterRequest;
import com.freshdirect.mobileapi.controller.data.request.MobilePreferenceRequest;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessageEx;
import com.freshdirect.mobileapi.controller.data.response.ExternalAccountLoginResponse;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.MessageResponse;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.DeliveryAddressValidatorUtil;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.sms.EnumSMSAlertStatus;
import com.freshdirect.webapp.ajax.expresscheckout.gogreen.service.GoGreenService;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.CaptchaUtil;

public class RegistrationController extends BaseController implements SystemMessageList {

	private static Category LOGGER = LoggerFactory
			.getInstance(RegistrationController.class);

    private static final String ACTION_REGISTER_FROM_IPHONE = "register";
    private static final String ACTION_REGISTER_FROM_FDX = "registerfdx";
    private static final String ACTION_REGISTER_FROM_SOCIAL = "registersocial";
    private final static String ACTION_ADD_DELIVERY_ADDRESS = "adddeliveryaddress";
    private final static String ACTION_EDIT_DELIVERY_ADDRESS = "editdeliveryaddress";
    private final static String ACTION_SAVE_DELIVERY_ADDRESS = "savedeliveryaddress";
    private final static String ACTION_SET_MOBILE_PREFERENCES = "setmobilepreferences";
    private final static String ACTION_SET_EMAIL_PREFERENCES = "setemailpreferences";
    private final static String ACTION_GET_EMAIL_PREFERENCES = "getemailpreferences";
    private final static String ACTION_GET_MOBILE_PREFERENCES = "getmobilepreferences";
    private final static String ACTION_SET_MOBILE_PREFERENCES_FIRST_ORDER = "setmobilepreferencesfirstorder";
    private final static String ACTION_SET_MOBILE_PREFERENCES_FIRST_ORDERFD = "setmobilepreferencesfirstorderfd";
    private final static String ACTION_SET_MOBILE_GO_GREEN_PREFERENCE = "setmobilegogreenpreferences";
    private final static String ACTION_GET_MOBILE_GO_GREEN_PREFERENCE = "getmobilegogreenpreferences";

	@Override
    protected boolean validateUser() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws FDException, ServiceException,
			NoSessionException, JsonException, FDActionNotAllowedException {
		if (ACTION_REGISTER_FROM_IPHONE.equals(action)) {
			RegisterMessage requestMessage = parseRequestObject(request,
					response, RegisterMessage.class);
			model = register(model, requestMessage, request, response,user);
		}else if (ACTION_REGISTER_FROM_FDX.equals(action)) {
			RegisterMessageEx requestMessage = parseRequestObject(request,
					response, RegisterMessageEx.class);
			model = registerEX(model, requestMessage, request, response,user);			
		}else if (ACTION_REGISTER_FROM_SOCIAL.equals(action)) {
			ExternalAccountRegisterRequest requestMessage = parseRequestObject(request,response, ExternalAccountRegisterRequest.class);
			model = registerSocial(model, requestMessage, request, response,user);			
		}  else if (ACTION_ADD_DELIVERY_ADDRESS.equals(action)) {
        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
            model = addDeliveryAddress(model, user, requestMessage, request);
        } else if (ACTION_EDIT_DELIVERY_ADDRESS.equals(action)) {
        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
            model = editDeliveryAddress(model, user, requestMessage, request);
        } else if (ACTION_SAVE_DELIVERY_ADDRESS.equals(action)) {
        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
        	if(requestMessage != null && requestMessage.getShipToAddressId() != null && requestMessage.getShipToAddressId().trim().length() > 0) {
        		model = editDeliveryAddress(model, user, requestMessage, request);
        	} else {
        		model = addDeliveryAddress(model, user, requestMessage, request);
        	}
          
        } else if (ACTION_SET_MOBILE_PREFERENCES.equals(action)) {
        	MobilePreferenceRequest requestMessage = parseRequestObject(request, response, MobilePreferenceRequest.class);
            model = setMobilePreferences(model, user, requestMessage, request );
        } else if (ACTION_SET_EMAIL_PREFERENCES.equals(action)){
        	EmailPreferenceRequest requestMessage = parseRequestObject(request, response, EmailPreferenceRequest.class);
        	model = setEmailPreference(model, user, requestMessage, request);
        } else if (ACTION_GET_EMAIL_PREFERENCES.equals(action)){
        	model = getEmailPreference(model, user, request);
        }	
		else if (ACTION_GET_MOBILE_PREFERENCES.equals(action)) {
	            model = getMobilePreferences(model, user, request );
		}
		else if(ACTION_SET_MOBILE_PREFERENCES_FIRST_ORDER.equals(action)){
			OrderMobileNumberRequest requestMessage = parseRequestObject(request, response, OrderMobileNumberRequest.class);
			 model = setMobilePreferencesFirstOrder(model, user, requestMessage, request );
		}
		else if(ACTION_SET_MOBILE_PREFERENCES_FIRST_ORDERFD.equals(action)){
			MobilePreferenceRequest requestMessage = parseRequestObject(request, response, MobilePreferenceRequest.class);
			 model = setMobilePreferencesFirstOrderFD(model, user, requestMessage, request );
		}
		
		else if(ACTION_SET_MOBILE_PREFERENCES_FIRST_ORDER.equals(action)){
			OrderMobileNumberRequest requestMessage = parseRequestObject(request, response, OrderMobileNumberRequest.class);
			 model = setMobilePreferencesFirstOrder(model, user, requestMessage, request );
		}
		else if(ACTION_SET_MOBILE_GO_GREEN_PREFERENCE.equals(action)){
			 model = setMobileGoGreenPreference(model, user, request );
		}
		else if(ACTION_GET_MOBILE_GO_GREEN_PREFERENCE.equals(action)){
			 model = getMobileGoGreenPreference(model, user, request );
		}
		return model;
	}
	/**
	 * @param model
	 * @param user
	 * @param request
	 * @return
	 * @throws FDException
	 * @throws JsonException
	 */
	private ModelAndView register(ModelAndView model,
			RegisterMessage requestMessage, HttpServletRequest request,
			HttpServletResponse response, SessionUser user) throws FDException,
			NoSessionException, JsonException {
		if(user == null)
			throw new NoSessionException("No session");
		RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
		ResultBundle resultBundle = tagWrapper.register(requestMessage);
		ActionResult result = resultBundle.getActionResult();

		propogateSetSessionValues(request.getSession(), resultBundle);

		Message responseMessage = null;
		if (result.isSuccess()) {
            if (request.getSession().getAttribute(SessionName.APPLICATION) == null) {
                request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, null));
            }
			user = getUserFromSession(request, response);
			user.setUserContext();
			user.setEligibleForDDPP();
			// Create a new Visitor object.
			responseMessage = formatLoginMessage(user);
			resetMobileSessionData(request);

		} else {
			responseMessage = getErrorMessage(result, request);
		}
		responseMessage.addWarningMessages(result.getWarnings());
		setResponseMessage(model, responseMessage, user);
		return model;
	}
	
	
	private ModelAndView registerEX(ModelAndView model,
			RegisterMessageEx requestMessage, HttpServletRequest request,
			HttpServletResponse response, SessionUser user) throws FDException,
			NoSessionException, JsonException {	
		Message responseMessage = null;
		FDIdentity userIdentity = null;
		// validate captcha if it's enabled
		boolean isCaptchaSuccess = CaptchaUtil.validateCaptcha(requestMessage.getCaptchaToken(),
				request.getRemoteAddr(), CaptchaType.SIGN_UP, request.getSession(), SessionName.SIGNUP_ATTEMPT,
				FDStoreProperties.getMaxInvalidSignUpAttempt());
		if (!isCaptchaSuccess) {
				responseMessage = new Message();
		        responseMessage.setStatus(Message.STATUS_FAILED);
		       
	            responseMessage.addErrorMessage("captcha", SystemMessageList.MSG_INVALID_CAPTCHA);
		        
	            setResponseMessage(model, responseMessage, user);
				return model;
			
		}
		try{
				userIdentity = FDCustomerManager.login(requestMessage.getEmail());
			} catch(Exception ex){
				
			}		
		if(userIdentity == null){
		
		if(user == null)
			 throw new NoSessionException("No session");
		
	    final boolean isWebRequest = isExtraResponseRequested(request);

	    UserUtil.touchUser(request, user.getFDSessionUser());
		RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());		
		RegisterMessage registerMessage = new RegisterMessage();
		registerMessage.setFirstName(requestMessage.getFirstName());
		registerMessage.setLastName(requestMessage.getLastName());
		registerMessage.setEmail(requestMessage.getEmail());
		registerMessage.setConfirmEmail(requestMessage.getEmail());
		registerMessage.setPassword(requestMessage.getPassword());
		registerMessage.setConfirmPassword(requestMessage.getPassword());
		registerMessage.setSecurityQuestion(requestMessage.getSecurityQuestion());
		registerMessage.setServiceType(requestMessage.getServiceType());
		registerMessage.setWorkPhone(requestMessage.getWorkPhone());
		registerMessage.setRafclickid(requestMessage.getRafclickid());
		registerMessage.setRafpromocode(requestMessage.getRafpromocode());
		registerMessage.setCaptchaToken(requestMessage.getCaptchaToken());
		
		ResultBundle resultBundle = tagWrapper.register(registerMessage);	
		
		if (resultBundle != null) {
			if (resultBundle.getActionResult() != null
					&& resultBundle.getActionResult().getErrors() != null
					&& resultBundle.getActionResult().getErrors().size() != 0) {
				ActionResult result = resultBundle.getActionResult();
				responseMessage = getErrorMessage(result, request);
				setResponseMessage(model, responseMessage, user);
				CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
				responseMessage.setShowCaptcha(CaptchaUtil.isExcessiveAttempt(FDStoreProperties.getMaxInvalidSignUpAttempt(),
						request.getSession(), SessionName.SIGNUP_ATTEMPT));
				return model;
			}
		}	

		
		FDSessionUser fduser = user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		
		MobilePreferenceRequest mobilePreferenceRequest = new MobilePreferenceRequest();
		mobilePreferenceRequest.setMobile_number(requestMessage.getMobile_number());		
    	if(requestMessage.isRecieveSMSAlerts()){	    	
			mobilePreferenceRequest.setOrder_notices("Y");
			mobilePreferenceRequest.setOrder_exceptions("Y");
			mobilePreferenceRequest.setOffers("Y");			
    	}else{;
	    	mobilePreferenceRequest.setOrder_notices("N");
			mobilePreferenceRequest.setOrder_exceptions("N");
			mobilePreferenceRequest.setOffers("N");		
    	}
    	FDCustomerModel FDCustomerModel=FDCustomerFactory.getFDCustomer(identity);
		ResultBundle resultBundle1 = tagWrapper.setMobilePreferences(mobilePreferenceRequest,  FDCustomerModel.getCustomerSmsPreferenceModel(), fduser.getUserContext().getStoreContext().getEStoreId().getContentId());
					
		ActionResult result = resultBundle.getActionResult();
		ActionResult result1 = resultBundle1.getActionResult();
		//	ActionResult result2 = resultBundleAdd.getActionResult();
		
		propogateSetSessionValues(request.getSession(), resultBundle);
		propogateSetSessionValues(request.getSession(), resultBundle1);
		//	propogateSetSessionValues(request.getSession(), resultBundleAdd);			
		//	if (result.isSuccess() && result1.isSuccess() && result2.isSuccess()) {
		
        final boolean isSuccess = result.isSuccess() && result1.isSuccess();

        if (isSuccess) {
        	CaptchaUtil.resetAttempt(request, SessionName.SIGNUP_ATTEMPT);
        }
        if (isWebRequest) {
            MessageResponse webResponse = new MessageResponse();
            if (isSuccess) {
                // mark session with the right source
                if (request.getSession().getAttribute(SessionName.APPLICATION) == null) {
                    request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, null));
                }

                // authenticate user
                user = getUser(request, response);

                // reset mobile app part of session
                resetMobileSessionData(request);

                // setup response
                final LoggedIn login = formatLoginMessage(user);

                populateResponseWithEnabledAdditionsForWebClient(user, webResponse, request, login);

            } else {
                webResponse.addWarningMessages(result.getWarnings());
            }

            responseMessage = webResponse;
		} else {
            if (isSuccess) {
                if (request.getSession().getAttribute(SessionName.APPLICATION) == null) {
                    request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, null));
                }

	            user = getUserFromSession(request, response);
	            user.setUserContext();
	            user.setEligibleForDDPP();

	            responseMessage = formatLoginMessage(user);
	            resetMobileSessionData(request);
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        responseMessage.addWarningMessages(result.getWarnings());
		    
		}

		
		
		} else{
			List<String> providers = ExternalAccountManager.getConnectedProvidersByUserId(requestMessage.getEmail());
			if(providers!=null && providers.size()!=0){
				responseMessage = Message.createFailureMessage(MessageFormat.format(MSG_SOCIAL_SOCIALONLY_ACCOUNT_CREATE, providers));	
			} else {
			responseMessage = Message.createFailureMessage("Account already exists with this Email address. "+requestMessage.getEmail());	
			}
		}
		if (responseMessage.getErrors() == null || responseMessage.getErrors().size() == 0) {
			CaptchaUtil.resetAttempt(request, SessionName.SIGNUP_ATTEMPT);
		} else {
			CaptchaUtil.increaseAttempt(request, SessionName.SIGNUP_ATTEMPT);
			responseMessage.setShowCaptcha(CaptchaUtil.isExcessiveAttempt(FDStoreProperties.getMaxInvalidSignUpAttempt(),
					request.getSession(), SessionName.SIGNUP_ATTEMPT));
		}
		setResponseMessage(model, responseMessage, user);
		return model;
	}
	
	private ModelAndView registerSocial(ModelAndView model, ExternalAccountRegisterRequest requestMessage, HttpServletRequest request, HttpServletResponse response, SessionUser user) throws FDException, JsonException, NoSessionException{
		
		Message responseMessage = null;
		FDIdentity userIdentity = null;
		String userToken = "", providerName="";
		HashMap<String,String> socialUser = null;
		HttpSession session = request.getSession();
		userToken = requestMessage.getUserToken();
		providerName = requestMessage.getProvider();
		String source = requestMessage.getSource();
		
		request.setAttribute("userToken", userToken);
		request.setAttribute("provider", providerName);
		
		if(userToken == null || userToken.length() == 0 || providerName == null || providerName.length()==0)
		{
			LOGGER.debug("user token is missing");
			throw new FDException("Invalid user token or provider");
		
		}
		else
		{
			if(StringUtils.isEmpty(source) || EnumExternalLoginSource.SOCIAL.value().equals(source)){
				SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
				
				if(socialProvider != null && userToken != null)
					socialUser = socialProvider.getSocialUserProfileByUserToken(userToken,providerName);
				if(socialUser == null)
				{
					LOGGER.debug("User's social profile could not be found");
					throw new FDException("User's social profile could not be found");
				}
				else
				{
					if(!requestMessage.getEmail().equalsIgnoreCase(socialUser.get("email")))
						throw new FDException("User Email did not match with Social Email.");
					
					session.setAttribute(SessionName.SOCIAL_USER,socialUser);
				}
			}
		}
			
		try{
			//userIdentity = FDCustomerManager.login(requestMessage.getEmail(),requestMessage.getPassword());
			userIdentity = FDCustomerManager.login(requestMessage.getEmail());
		} catch(Exception ex){
			
		}		
		if(userIdentity == null){
		
		if(user == null || user.getFDSessionUser() == null)
			throw new FDException("FD Session User could not be found");
			
		RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());		
		RegisterMessage registerMessage = new RegisterMessage();
		registerMessage.setFirstName(requestMessage.getFirstName());
		registerMessage.setLastName(requestMessage.getLastName());
		registerMessage.setEmail(requestMessage.getEmail());
		registerMessage.setConfirmEmail(requestMessage.getEmail());
		registerMessage.setPassword(requestMessage.getPassword());
		registerMessage.setConfirmPassword(requestMessage.getPassword());
		registerMessage.setSecurityQuestion(requestMessage.getSecurityQuestion());
		registerMessage.setServiceType(requestMessage.getServiceType());
		registerMessage.setAddress1(requestMessage.getAddress1());
		registerMessage.setApartment(requestMessage.getApartment());
		registerMessage.setCity(requestMessage.getCity());
		registerMessage.setState(requestMessage.getState());
		registerMessage.setZipCode(requestMessage.getZipCode());
		registerMessage.setWorkPhone(requestMessage.getWorkPhone());
		registerMessage.setDlvhomephone(requestMessage.getDlvhomephone());
		
		ExternalAccountLinkRequest linkRequest = new ExternalAccountLinkRequest(requestMessage.getEmail(), requestMessage.getPassword(), 
				requestMessage.getUserToken(), requestMessage.getUserToken(), requestMessage.getProvider(), requestMessage.getSource());
		
		ResultBundle resultBundle = tagWrapper.registerSocial(requestMessage);	
		
		if (resultBundle != null) {
			if (resultBundle.getActionResult() != null
					&& resultBundle.getActionResult().getErrors() != null
					&& resultBundle.getActionResult().getErrors().size() != 0) {
				ActionResult result = resultBundle.getActionResult();
				responseMessage = getErrorMessage(result, request);
				setResponseMessage(model, responseMessage, user);
				return model;
			}
		}	
		
		FDSessionUser fduser = user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		
				
		MobilePreferenceRequest mobilePreferenceRequest = new MobilePreferenceRequest();
		mobilePreferenceRequest.setMobile_number(requestMessage.getMobile_number());		
    	if(requestMessage.isRecieveSMSAlerts()){	    	
			mobilePreferenceRequest.setOrder_notices("Y");
			mobilePreferenceRequest.setOrder_exceptions("Y");
			mobilePreferenceRequest.setOffers("Y");			
    	}else{;
	    	mobilePreferenceRequest.setOrder_notices("N");
			mobilePreferenceRequest.setOrder_exceptions("N");
			mobilePreferenceRequest.setOffers("N");		
    	}
				
    	propogateSetSessionValues(request.getSession(), resultBundle);
    //	ResultBundle resultBundle1 = tagWrapper.setMobilePreferences(mobilePreferenceRequest, cm);
    	FDCustomerModel FDCustomerModel=FDCustomerFactory.getFDCustomer(identity);
    	
    	ResultBundle resultBundle1 = tagWrapper.setMobilePreferences(mobilePreferenceRequest,  FDCustomerModel.getCustomerSmsPreferenceModel(), fduser.getUserContext().getStoreContext().getEStoreId().getContentId());
		
		ActionResult result = resultBundle.getActionResult();
		ActionResult result1 = resultBundle1.getActionResult();
		
		propogateSetSessionValues(request.getSession(), resultBundle);
		propogateSetSessionValues(request.getSession(), resultBundle1);
		//Message responseMessage = null;
		if (result.isSuccess() && result1.isSuccess()) {
			
			ExternalAccountManager.linkUserTokenToUserId(
					identity.getFDCustomerPK(),
					linkRequest.getEmail(),
					linkRequest.getNewToken(),
					linkRequest.getNewToken(),
					linkRequest.getProvider(),
					registerMessage.getFirstName()+" "+registerMessage.getLastName(),
					linkRequest.getEmail(),
					linkRequest.getEmail(),
					"N");
		
	
            if (request.getSession().getAttribute(SessionName.APPLICATION) == null) {
                request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, null));
            }
			user = getUserFromSession(request, response);
			user.setUserContext();
			user.setEligibleForDDPP();
			// Create a new Visitor object.
			//responseMessage = formatLoginMessage(user);
			responseMessage = new ExternalAccountLoginResponse();
			
			responseMessage.setStatus(Message.STATUS_SUCCESS);
			
			((ExternalAccountLoginResponse) responseMessage).setLoggedInSuccess(true);
			((ExternalAccountLoginResponse) responseMessage).setFdxdpenabled(FDStoreProperties.isDlvPassFDXEnabled());
			((ExternalAccountLoginResponse) responseMessage).setPurchaseDlvPassEligible(user.getFDSessionUser().isEligibleForDeliveryPass());
			if(((ExternalAccountLoginResponse) responseMessage).isPurchaseDlvPassEligible()){
				((ExternalAccountLoginResponse) responseMessage).setDpskulist(new ArrayList<String>(Arrays.asList((FDStoreProperties.getFDXDPSku()).split(","))));
	        }
			responseMessage.setSuccessMessage("User registered and successfully logged in");
			
			resetMobileSessionData(request);

		} else {
			responseMessage =  getErrorMessage(result, request);
		}
		responseMessage.addWarningMessages(result.getWarnings());
		} else{
			responseMessage = Message.createFailureMessage("Account already exists with this Email address. "+requestMessage.getEmail());			
		}
		
		setResponseMessage(model, responseMessage, user);
		return model;


	}

	@Override
	protected LoggedIn formatLoginMessage(SessionUser user) throws FDException {
        LoggedIn responseMessage = new LoggedIn();
        responseMessage.setChefTable(user.isChefsTable());
        responseMessage.setCustomerServicePhoneNumber(user
				.getCustomerServiceContact());
		if (user.getReservationTimeslot() != null) {
		    responseMessage.setReservationTimeslot(new Timeslot(
					user.getReservationTimeslot()));
		}
		responseMessage.setFirstName(user.getFirstName());
		responseMessage.setLastName(user.getLastName());
		responseMessage.setUsername(user.getUsername());
		responseMessage.setSuccessMessage("User has been logged in successfully.");
		responseMessage.setItemsInCartCount(user
				.getItemsInCartCount());
        responseMessage.setOrderCount(user.getOrderHistory().getValidOrderCount());
        responseMessage.setFdxOrderCount(user.getOrderHistory().getValidOrderCount(EnumEStoreId.FDX));
		responseMessage.setOrders(java.util.Collections.<OrderHistory.Order>emptyList());
		responseMessage.setFdUserId(user.getPrimaryKey());

		//With Mobile App having given ability to add/remove payment method this is removed
		/* if ((user.getPaymentMethods() == null)	|| (user.getPaymentMethods().size() == 0)) {
			responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);
		}*/
		responseMessage.setBrowseEnabled(MobileApiProperties.isBrowseEnabled());
		
		responseMessage.setSelectedServiceType(user.getSelectedServiceType() != null ? user.getSelectedServiceType().toString() : "");
		responseMessage.setCohort(user.getCohort());
		responseMessage.setTotalOrderCount(user.getTotalOrderCount());
		responseMessage.setTcAcknowledge(user.getTcAcknowledge());
        responseMessage.setZipCode(user.getZipCode());
        responseMessage.setFdxDpEnabled(FDStoreProperties.isDlvPassFDXEnabled());
        responseMessage.setPurchaseDlvPassEligible(user.getFDSessionUser().isEligibleForDeliveryPass());
        if(responseMessage.isPurchaseDlvPassEligible()){
        	responseMessage.setDpskulist(new ArrayList<String>(Arrays.asList((FDStoreProperties.getFDXDPSku()).split(","))));
        }
		
		return responseMessage;
	}
	
    private ModelAndView addDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest requestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        // APPDEV-4315- Intermittent: Cannot Create Address -Start
        Message responseMessage = null;
        if (user != null) {
            if (user.isVoucherHolder()) {
                responseMessage = Message.createSuccessMessage(ACTION_ADD_DELIVERY_ADDRESS);
                throw new FDActionNotAllowedException("This account is not enabled to change delivery address.");
            }

            ActionResult result = null;

            // === FKMW - validate form fields before submitting them to the app layer ===
            
            final boolean isWebRequest = isExtraResponseRequested(request);
            if (isWebRequest) {
                result = DeliveryAddressValidatorUtil.validateDeliveryAddress(requestMessage);

                // halt on any error
                if (!result.isSuccess()) {
                    responseMessage = getErrorMessage(result, request);
                }
            }
            
            // === FKMW end ===
            
            if (responseMessage == null) {
                RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
                ResultBundle resultBundle = tagWrapper.addDeliveryAddress(requestMessage);
                result = resultBundle.getActionResult();
    
                propogateSetSessionValues(request.getSession(), resultBundle);
                if (result.isSuccess()) {
    
                    ErpAddressModel eam = (ErpAddressModel) resultBundle.getExtraData(RegistrationControllerTagWrapper.KEY_RETURNED_SAVED_ADDRESS);
    
                    List<ErpAddressModel> addresses = FDCustomerFactory.getErpCustomer(user.getFDSessionUser().getIdentity()).getShipToAddresses();
                    ShipToAddress newelyAdded = null;
                    for (ErpAddressModel toCheck : addresses) {
                        if (DeliveryAddressManipulator.matchAddress(toCheck, eam)) {
                            newelyAdded = ShipToAddress.wrap(toCheck);
                            break;
                        }
                    }
                    responseMessage = new AddAddressResponse();
                    responseMessage.setSuccessMessage("Delivery Address added successfully.");
                    ((AddAddressResponse) responseMessage).setAddedAddress(newelyAdded);
    
                } else {
                    responseMessage = getErrorMessage(result, request);
                }
            }
            responseMessage.addWarningMessages(result.getWarnings());
            setResponseMessage(model, responseMessage, user);
        } else {
            responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
            setResponseMessage(model, responseMessage, user);
        }
        // APPDEV-4315- Intermittent: Cannot Create Address -End
        return model;
    }

    private ModelAndView editDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest requestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        // APPDEV-4315- Intermittent: Cannot Create Address -Start

        Message responseMessage = null;
        if (user != null) {

            if (user.isVoucherHolder()) {
                responseMessage = Message.createSuccessMessage(ACTION_EDIT_DELIVERY_ADDRESS);
                throw new FDActionNotAllowedException("This account is not enabled to change delivery address.");
            }
            
            
            ActionResult result = null;

            // === FKMW - validate form fields before submitting them to the app layer ===
            
            final boolean isWebRequest = isExtraResponseRequested(request);
            if (isWebRequest) {
                result = DeliveryAddressValidatorUtil.validateDeliveryAddress(requestMessage);

                // halt on any error
                if (!result.isSuccess()) {
                    responseMessage = getErrorMessage(result, request);
                }
            }
            
            // === FKMW end ===
            
            if (responseMessage == null) {
                RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
                ResultBundle resultBundle = tagWrapper.editDeliveryAddress(requestMessage);
    
                result = resultBundle.getActionResult();
    
                propogateSetSessionValues(request.getSession(), resultBundle);
    
                if (result.isSuccess()) {
                    responseMessage = Message.createSuccessMessage("Delivery Address updated successfully.");
                } else {
                    responseMessage = getErrorMessage(result, request);
                }
            }
            responseMessage.addWarningMessages(result.getWarnings());
            setResponseMessage(model, responseMessage, user);
        }

        else {
            responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
            setResponseMessage(model, responseMessage, user);
        }
        // APPDEV-4315- Intermittent: Cannot Create Address -End
        return model;
    }
    
    private ModelAndView setMobilePreferences(ModelAndView model, SessionUser user, MobilePreferenceRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	Message responseMessage = null;   
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());        
        FDSessionUser fduser = user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		if(identity!=null){
			FDCustomerModel fdCustomerModel=FDCustomerFactory.getFDCustomer(identity);
	        ResultBundle resultBundle = tagWrapper.setMobilePreferences(reqestMessage, fdCustomerModel.getCustomerSmsPreferenceModel(), fduser.getUserContext().getStoreContext().getEStoreId().getContentId());
	        ActionResult result = resultBundle.getActionResult();
	        propogateSetSessionValues(request.getSession(), resultBundle);
	        if (result.isSuccess()) {
	            if (isResponseAdditionalEnable(request, EnumResponseAdditional.INCLUDE_USERINFO)) {
	                responseMessage = super.formatLoginMessage(user);
	            } else {
	                responseMessage = new Message();
	            }
	            responseMessage.setSuccessMessage("Contact Number added successfully.");
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        responseMessage.addWarningMessages(result.getWarnings());   
		}else{
			responseMessage = getErrorMessage("NO_IDENTITY_FOUND", "No Identity found for user");
		}
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView setMobilePreferencesFirstOrder(ModelAndView model, SessionUser user, OrderMobileNumberRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	Message responseMessage = null;   
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());        
        FDSessionUser fduser = user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		if(identity!=null){
			FDCustomerModel fdCustomerModel=FDCustomerFactory.getFDCustomer(identity);
	        ResultBundle resultBundle = tagWrapper.setMobilePreferencesFirstOrder(reqestMessage, fdCustomerModel.getCustomerSmsPreferenceModel(), fduser.getUserContext().getStoreContext().getEStoreId().getContentId());
	        ActionResult result = resultBundle.getActionResult();
	        propogateSetSessionValues(request.getSession(), resultBundle);
	        if (result.isSuccess()) {
	            responseMessage = Message.createSuccessMessage("Contact Number added successfully.");
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        responseMessage.addWarningMessages(result.getWarnings());   
		}else{
			responseMessage = getErrorMessage("NO_IDENTITY_FOUND", "No Identity found for user");
		}
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView setMobilePreferencesFirstOrderFD(ModelAndView model, SessionUser user, MobilePreferenceRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	Message responseMessage = null; 
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());        
        FDSessionUser fduser = user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		if(identity!=null){
			FDCustomerModel fdCustomerModel=FDCustomerFactory.getFDCustomer(identity);
	        ResultBundle resultBundle = tagWrapper.setMobilePreferencesFirstOrderFD(reqestMessage, fdCustomerModel.getCustomerSmsPreferenceModel(), fduser.getUserContext().getStoreContext().getEStoreId().getContentId());
	        ActionResult result = resultBundle.getActionResult();
	        propogateSetSessionValues(request.getSession(), resultBundle);
	        if (result.isSuccess()) {
	            responseMessage = Message.createSuccessMessage("Contact Number added successfully.");
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        responseMessage.addWarningMessages(result.getWarnings());   
		}else{
			responseMessage = getErrorMessage("NO_IDENTITY_FOUND", "No Identity found for user");
		}
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
   private ModelAndView setEmailPreference(ModelAndView model, SessionUser user, EmailPreferenceRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {

    	Message responseMessage = null;
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());        
    	
        FDSessionUser fduser = user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		if(identity!=null){
			ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
			
			if("Y".equalsIgnoreCase(reqestMessage.getEmail_subscribed()) ){
				cm.setEmailPreferenceLevel("2");
			} else {
				cm.setEmailPreferenceLevel("0");
			}
										       
			ResultBundle resultBundle = tagWrapper.setEmailPreferences(reqestMessage, cm);
			ActionResult result = resultBundle.getActionResult();
			propogateSetSessionValues(request.getSession(), resultBundle);
			
			if(result.isSuccess()){
				responseMessage = Message.createSuccessMessage("Successfully changed email preference");
			} else {
				responseMessage = getErrorMessage(result, request);
			}
			
			responseMessage.addWarningMessages(result.getWarnings());
		}else{
			responseMessage = getErrorMessage("NO_IDENTITY_FOUND", "No Identity found for user");
		}
    	setResponseMessage(model, responseMessage, user);
    	
    	return model;
    }
    
    private ModelAndView getEmailPreference(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
    	
        FDSessionUser fduser = user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		if(identity!=null){
			ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
			EmailPreferencesResult responseMessage = new EmailPreferencesResult();
			if("2".equals(cm.getEmailPreferenceLevel())){
				responseMessage.setEmail_subscribed("Y");
			} else {
				responseMessage.setEmail_subscribed("N");
			}
			setResponseMessage(model, responseMessage, user);
		}else{
			Message responseMessage = getErrorMessage("NO_IDENTITY_FOUND", "No Identity found for user");
			setResponseMessage(model, responseMessage, user);
		}
    	
    	return model;
    }
    
    private ModelAndView getMobilePreferences(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
       
    	FDSessionUser fduser = user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		if(identity!=null){
			FDCustomerModel fdCustomerModel=FDCustomerFactory.getFDCustomer(identity);
			FDCustomerEStoreModel customerSmsPreferenceModel =fdCustomerModel.getCustomerSmsPreferenceModel();
			MobilePreferencesResult mobileresponseMessage = new MobilePreferencesResult();
			
			if(EnumEStoreId.FDX.getContentId().equals(fduser.getUserContext().getStoreContext().getEStoreId().getContentId()))
				{
					  mobileresponseMessage.setOrder_notices(EnumSMSAlertStatus.NONE.value().equalsIgnoreCase(customerSmsPreferenceModel.getFdxOrderNotices())? false:true);
		    		  mobileresponseMessage.setOrder_exceptions(EnumSMSAlertStatus.NONE.value().equalsIgnoreCase(customerSmsPreferenceModel.getFdxOrderExceptions())? false:true);
					  mobileresponseMessage.setOffers(EnumSMSAlertStatus.NONE.value().equalsIgnoreCase(customerSmsPreferenceModel.getFdxOffers())? false:true);
					  mobileresponseMessage.setMobile_number(customerSmsPreferenceModel.getFdxMobileNumber()!=null?customerSmsPreferenceModel.getFdxMobileNumber().getPhone():"");
				}else{
					  mobileresponseMessage.setOrder_notices(EnumSMSAlertStatus.NONE.value().equalsIgnoreCase(customerSmsPreferenceModel.getOrderNotices())? false:true);
		    		  mobileresponseMessage.setOrder_exceptions(EnumSMSAlertStatus.NONE.value().equalsIgnoreCase(customerSmsPreferenceModel.getOrderExceptions())? false:true);
					  mobileresponseMessage.setOffers(EnumSMSAlertStatus.NONE.value().equalsIgnoreCase(customerSmsPreferenceModel.getOffers())? false:true);
					  mobileresponseMessage.setMobile_number(customerSmsPreferenceModel.getMobileNumber()!=null?customerSmsPreferenceModel.getMobileNumber().getPhone():"");
		    	     }
			
	    	setResponseMessage(model, mobileresponseMessage, user);
		}else{
			Message responseMessage = getErrorMessage("NO_IDENTITY_FOUND", "No Identity found for user");
			setResponseMessage(model, responseMessage, user);
		}
    	return model;
    
    }
    
    private ModelAndView setMobileGoGreenPreference(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
    	Message responseMessage = null; 
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());        
        FDSessionUser fduser = user.getFDSessionUser();
        ResultBundle resultBundle = tagWrapper.setMobileGoGreenPreference(fduser.getUserContext().getStoreContext().getEStoreId().getContentId());
       
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("GoGreen flag has been updated successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());   
    	
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView getMobileGoGreenPreference(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
	        FDSessionUser fduser = user!=null?user.getFDSessionUser():null;
    		String goGreen=null;
    		GoGreenPreferencesResult goGreenPreferencesResult = new GoGreenPreferencesResult();
    		
    		if(fduser!=null&&fduser.getIdentity()!=null&&fduser.getUserContext()!=null&&fduser.getUserContext().getStoreContext()!=null&&fduser.getUserContext().getStoreContext().getEStoreId()!=null&&
    				EnumEStoreId.FD.getContentId().equals(fduser.getUserContext().getStoreContext().getEStoreId().getContentId()))
    			
    			  goGreen=GoGreenService.defaultService().loadGoGreenOption(fduser);
    		
				if ("".equalsIgnoreCase(goGreen) || goGreen == null)
					goGreenPreferencesResult.setGo_green("N");
				else
					goGreenPreferencesResult.setGo_green(goGreen);
		
				setResponseMessage(model, goGreenPreferencesResult, user);
		    			
		        	return model;
        }
}
