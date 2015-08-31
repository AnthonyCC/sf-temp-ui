package com.freshdirect.mobileapi.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.EmailPreferencesResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.EmailPreferenceRequest;
import com.freshdirect.mobileapi.controller.data.request.MobilePreferenceRequest;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessageFdxRequest;
import com.freshdirect.mobileapi.controller.data.request.SocialLogin;
import com.freshdirect.mobileapi.controller.data.request.SocialRegisterRequest;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.OrderHistory;
import com.freshdirect.mobileapi.controller.data.response.SocialResponse;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.SiteAccessControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;
import com.freshdirect.webapp.util.LocatorUtil;

public class RegistrationController extends BaseController {

	private static Category LOGGER = LoggerFactory
			.getInstance(RegistrationController.class);

	public static final String ACTION_REGISTER_FROM_IPHONE = "register";
	
	public static final String ACTION_REGISTER_FROM_FDX = "registerfdx";
	
	public static final String ACTION_REGISTER_FROM_SOCIAL = "registersocial";
	
    private final static String ACTION_ADD_DELIVERY_ADDRESS = "adddeliveryaddress";

    private final static String ACTION_EDIT_DELIVERY_ADDRESS = "editdeliveryaddress";
    
    private final static String ACTION_SAVE_DELIVERY_ADDRESS = "savedeliveryaddress";
       
    private final static String ACTION_SET_MOBILE_PREFERENCES = "setmobilepreferences";
    
    private final static String ACTION_SET_EMAIL_PREFERENCES = "setemailpreferences";

    private final static String ACTION_GET_EMAIL_PREFERENCES = "getemailpreferences";

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
			NoSessionException, JsonException {
		if (ACTION_REGISTER_FROM_IPHONE.equals(action)) {
			RegisterMessage requestMessage = parseRequestObject(request,
					response, RegisterMessage.class);
			model = register(model, requestMessage, request, response,user);
		}else if (ACTION_REGISTER_FROM_FDX.equals(action)) {
			RegisterMessageFdxRequest requestMessage = parseRequestObject(request,
					response, RegisterMessageFdxRequest.class);
			model = registerEX(model, requestMessage, request, response,user);			
		}else if (ACTION_REGISTER_FROM_SOCIAL.equals(action)) {
			SocialRegisterRequest requestMessage = parseRequestObject(request,response, SocialRegisterRequest.class);
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
            model = setMobilePreferences(model, user, requestMessage, request);
        } else if (ACTION_SET_EMAIL_PREFERENCES.equals(action)){
        	EmailPreferenceRequest requestMessage = parseRequestObject(request, response, EmailPreferenceRequest.class);
        	model = setEmailPreference(model, user, requestMessage, request);
        } else if (ACTION_GET_EMAIL_PREFERENCES.equals(action)){
        	model = getEmailPreference(model, user, request);
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
			request.getSession().setAttribute(SessionName.APPLICATION,
					EnumTransactionSource.IPHONE_WEBSITE.getCode());
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
			RegisterMessageFdxRequest requestMessage, HttpServletRequest request,
			HttpServletResponse response, SessionUser user) throws FDException,
			NoSessionException, JsonException {	
		Message responseMessage = null;
		FDIdentity userIdentity = null;
			try{
				userIdentity = FDCustomerManager.login(requestMessage.getEmail());
			} catch(Exception ex){
				
			}		
		if(userIdentity == null){
		
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
		
		ResultBundle resultBundle = tagWrapper.register(registerMessage);	
		
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
			//------------------------------		
			/*DeliveryAddressRequest dliveryAddressRequest = new DeliveryAddressRequest();		
			dliveryAddressRequest.setDlvfirstname(requestMessage.getFirstName());
			dliveryAddressRequest.setDlvlastname(requestMessage.getLastName());
			dliveryAddressRequest.setAddress1(requestMessage.getAddress1());
			dliveryAddressRequest.setApartment(requestMessage.getApartment());
			dliveryAddressRequest.setCity(requestMessage.getCity());
			dliveryAddressRequest.setState(requestMessage.getState());
			dliveryAddressRequest.setZipcode(requestMessage.getZipCode());
			dliveryAddressRequest.setDlvServiceType(requestMessage.getServiceType());
			dliveryAddressRequest.setDlvcompanyname(requestMessage.getCompanyName());
			dliveryAddressRequest.setDeliveryInstructions("");
			dliveryAddressRequest.setDlvhomephone(requestMessage.getMobile_number());		
			ResultBundle resultBundleAdd = tagWrapper.addDeliveryAddress(dliveryAddressRequest);		*/
			//------------------------------
		FDSessionUser fduser = (FDSessionUser) user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		
		
		ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);				
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
				
		ResultBundle resultBundle1 = tagWrapper.setMobilePreferences(mobilePreferenceRequest, cm);
		
		ActionResult result = resultBundle.getActionResult();
		ActionResult result1 = resultBundle1.getActionResult();
			//ActionResult result2 = resultBundleAdd.getActionResult();
		
		propogateSetSessionValues(request.getSession(), resultBundle);
		propogateSetSessionValues(request.getSession(), resultBundle1);
		//	propogateSetSessionValues(request.getSession(), resultBundleAdd);			
		//if (result.isSuccess() && result1.isSuccess() && result2.isSuccess()) {
			if (result.isSuccess() && result1.isSuccess()) {
			request.getSession().setAttribute(SessionName.APPLICATION,
					EnumTransactionSource.FDX_IPHONE.getCode());
			user = getUserFromSession(request, response);
			user.setUserContext();
			user.setEligibleForDDPP();
			responseMessage = formatLoginMessage(user);
			resetMobileSessionData(request);
		} else {
			responseMessage = getErrorMessage(result, request);
		}
		responseMessage.addWarningMessages(result.getWarnings());
		} else{
			responseMessage = Message.createFailureMessage("Account already exists with this Email address. "+requestMessage.getEmail());			
		}
		setResponseMessage(model, responseMessage, user);
		return model;
	}
	
	private ModelAndView registerSocial(ModelAndView model, SocialRegisterRequest requestMessage, HttpServletRequest request, HttpServletResponse response, SessionUser user) throws FDException, JsonException, NoSessionException{
		
		Message responseMessage = null;
		FDIdentity userIdentity = null;
		String userToken = "", providerName="";
		HashMap<String,String> socialUser = null;
		HttpSession session = request.getSession();
		userToken = requestMessage.getUserToken();
		providerName = requestMessage.getProvider();
		
		request.setAttribute("userToken", userToken);
		request.setAttribute("provider", providerName);
		
		if(userToken == null || userToken.length() == 0 || providerName == null || providerName.length()==0)
		{
			LOGGER.debug("user token is missing");
			throw new FDException("Invalid user token or provider");
		
		}
		else
		{
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
		
		ResultBundle resultBundle = tagWrapper.registerSocial(requestMessage);	
		
		FDSessionUser fduser = (FDSessionUser) user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		
		ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);				
				
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
    	
    	ResultBundle resultBundle1 = tagWrapper.setMobilePreferences(mobilePreferenceRequest, cm);
		
		ActionResult result = resultBundle.getActionResult();
		ActionResult result1 = resultBundle1.getActionResult();
		
		propogateSetSessionValues(request.getSession(), resultBundle);
		propogateSetSessionValues(request.getSession(), resultBundle1);
		//Message responseMessage = null;
		if (result.isSuccess() && result1.isSuccess()) {
			request.getSession().setAttribute(SessionName.APPLICATION,
					EnumTransactionSource.FDX_IPHONE.getCode());
			user = getUserFromSession(request, response);
			user.setUserContext();
			user.setEligibleForDDPP();
			// Create a new Visitor object.
			//responseMessage = formatLoginMessage(user);
			responseMessage = new SocialResponse();
			
			responseMessage.setStatus(Message.STATUS_SUCCESS);
			
			((SocialResponse) responseMessage).setLoggedInSuccess(true);
			
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


	private Message formatLoginMessage(SessionUser user) throws FDException {
		Message responseMessage = null;

		responseMessage = new LoggedIn();
		((LoggedIn) responseMessage).setChefTable(user.isChefsTable());
		((LoggedIn) responseMessage).setCustomerServicePhoneNumber(user
				.getCustomerServiceContact());
		if (user.getReservationTimeslot() != null) {
			((LoggedIn) responseMessage).setReservationTimeslot(new Timeslot(
					user.getReservationTimeslot()));
		}
		((LoggedIn) responseMessage).setFirstName(user.getFirstName());
        ((LoggedIn) responseMessage).setLastName(user.getLastName());
		((LoggedIn) responseMessage).setUsername(user.getUsername());
		((LoggedIn) responseMessage)
				.setSuccessMessage("User has been logged in successfully.");
		((LoggedIn) responseMessage).setItemsInCartCount(user
				.getItemsInCartCount());
		((LoggedIn) responseMessage).setOrderCount(user.getOrderHistory().getValidOrderCount());
        ((LoggedIn) responseMessage).setOrders(java.util.Collections.<OrderHistory.Order>emptyList());
		((LoggedIn) responseMessage).setFdUserId(user.getPrimaryKey());

		//With Mobile App having given ability to add/remove payment method this is removed
		/* if ((user.getPaymentMethods() == null)	|| (user.getPaymentMethods().size() == 0)) {
			responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);
		}*/
		((LoggedIn) responseMessage).setBrowseEnabled(MobileApiProperties.isBrowseEnabled());
		
		//Added during Mobile Coremetrics Implementation
		((LoggedIn) responseMessage).setSelectedServiceType(user.getSelectedServiceType() != null ? user.getSelectedServiceType().toString() : "");
		((LoggedIn) responseMessage).setCohort(user.getCohort());
		((LoggedIn) responseMessage).setTotalOrderCount(user.getTotalOrderCount());
		
		return responseMessage;
	}
	
    private ModelAndView addDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	//APPDEV-4315- Intermittent: Cannot Create Address -Start
    	if(user!=null){
	    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
	        ResultBundle resultBundle = tagWrapper.addDeliveryAddress(reqestMessage);
	        ActionResult result = resultBundle.getActionResult();
	
	        propogateSetSessionValues(request.getSession(), resultBundle);
	
	        Message responseMessage = null;
	        if (result.isSuccess()) {
	        	ErpAddressModel eam = (ErpAddressModel)resultBundle.getExtraData(RegistrationControllerTagWrapper.KEY_RETURNED_SAVED_ADDRESS);
		        
		        List<ErpAddressModel> addresses = FDCustomerFactory.getErpCustomer(user.getFDSessionUser().getIdentity()).getShipToAddresses();
		        ShipToAddress newelyAdded = null;
		        for(ErpAddressModel toCheck : addresses){
		        	if(DeliveryAddressManipulator.matchAddress(toCheck, eam)){
		        		newelyAdded = ShipToAddress.wrap(toCheck);
		        		break;
		        	}
		        }
		        responseMessage = new AddAddressResponse();
		        ((AddAddressResponse)responseMessage).setAddedAddress(newelyAdded);
		        
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        responseMessage.addWarningMessages(result.getWarnings());
	        setResponseMessage(model, responseMessage, user);
    	}
    	else {
    		Message responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
    		setResponseMessage(model, responseMessage, user);
    	}
        //APPDEV-4315- Intermittent: Cannot Create Address -End
        return model;
    }

    private ModelAndView editDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	//APPDEV-4315- Intermittent: Cannot Create Address -Start
    	if(user!=null){
	    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());
	        ResultBundle resultBundle = tagWrapper.editDeliveryAddress(reqestMessage);
	        ActionResult result = resultBundle.getActionResult();
	
	        propogateSetSessionValues(request.getSession(), resultBundle);
	
	        Message responseMessage = null;
	        if (result.isSuccess()) {
	            responseMessage = Message.createSuccessMessage("Delivery Address updated successfully.");
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        responseMessage.addWarningMessages(result.getWarnings());
	        setResponseMessage(model, responseMessage, user);
    	}
    	else{
    		Message responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
    		setResponseMessage(model, responseMessage, user);  		
    	}
        //APPDEV-4315- Intermittent: Cannot Create Address -End
        return model;
    }
    
    private ModelAndView setMobilePreferences(ModelAndView model, SessionUser user, MobilePreferenceRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	Message responseMessage = null;   
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());        
        FDSessionUser fduser = (FDSessionUser) user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		
		ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
									       
        ResultBundle resultBundle = tagWrapper.setMobilePreferences(reqestMessage, cm);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Contact Number added successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());   
    	
        setResponseMessage(model, responseMessage, user);
        return model;
    }
	
    private ModelAndView setEmailPreference(ModelAndView model, SessionUser user, EmailPreferenceRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {

    	Message responseMessage = null;
    	RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(user.getFDSessionUser());        
    	
        FDSessionUser fduser = (FDSessionUser) user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		
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
    	setResponseMessage(model, responseMessage, user);
    	
    	return model;
    }
    
    private ModelAndView getEmailPreference(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
    	
        FDSessionUser fduser = (FDSessionUser) user.getFDSessionUser();        
		FDIdentity identity  = fduser.getIdentity();
		
		ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
		EmailPreferencesResult responseMessage = new EmailPreferencesResult();
		if("2".equals(cm.getEmailPreferenceLevel())){
			responseMessage.setEmail_subscribed("Y");
		} else {
			responseMessage.setEmail_subscribed("N");
		}
		
    	setResponseMessage(model, responseMessage, user);
    	
    	return model;
    }
}
