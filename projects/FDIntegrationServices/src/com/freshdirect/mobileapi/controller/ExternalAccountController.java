package com.freshdirect.mobileapi.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.UserSocialProfile;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessageEx;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLinkRequest;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLogin;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.SocialLoginResponse;
import com.freshdirect.mobileapi.controller.data.response.ExternalAccountLoginResponse;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.MergeCartControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ExternalAccountControllerTagWrapper;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

import java.util.List;

public class ExternalAccountController extends BaseController{

	private static Category LOGGER = LoggerFactory.getInstance(ExternalAccountController.class);
	
	//Actions
	 private final static String ACTION_RECOGNIZE_ACCOUNT = "login";
	 private final static String ACTION_SOCIAL_CONNECT_ACCOUNT = "socialConnect";
	 private final static String ACTION_LINK_ACCOUNT = "linkaccount";
	 private final static String ACTION_UNLINK_ACCOUNT = "unlinkaccount";
	 
	
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException, FDException  {
		//recognize Account will merge the existing accounts as well as login the user.
		
		if(ACTION_RECOGNIZE_ACCOUNT.equals(action)){
			ExternalAccountLogin requestMessage = null;
			requestMessage = parseRequestObject(request, response, ExternalAccountLogin.class);
			model = recognizeAccountAndLogin(model, user, requestMessage, request);
		} else if (ACTION_LINK_ACCOUNT.equals(action)){
			ExternalAccountLinkRequest requestMessage = null;
			requestMessage = parseRequestObject(request, response, ExternalAccountLinkRequest.class);
			model = connectExistingAccounts(model, user, requestMessage, request);
		} else if(ACTION_UNLINK_ACCOUNT.equals(action)){
			ExternalAccountLogin requestMessage = null;
			requestMessage = parseRequestObject(request, response, ExternalAccountLogin.class);
			model = unlinkExistingAccounts(model, user, requestMessage, request);
		} else if(ACTION_SOCIAL_CONNECT_ACCOUNT.equals(action)){			
			ExternalAccountLogin requestMessage = null;
			requestMessage = parseRequestObject(request, response, ExternalAccountLogin.class);
			model = socialConnectAccount(model, user, requestMessage, request, response);		
		}
		return model;
	}
	
	
	private ModelAndView unlinkExistingAccounts(ModelAndView model,
			SessionUser user, ExternalAccountLogin requestMessage,
			HttpServletRequest request) throws JsonException, FDException {
		ExternalAccountControllerTagWrapper wrapper = new ExternalAccountControllerTagWrapper(
				user);
		ResultBundle resultBundle = wrapper
				.unlinkExistingAccounts(requestMessage);
		ActionResult result = resultBundle.getActionResult();
		Message responseMessage = new ExternalAccountLoginResponse();
		if (result.isSuccess()) {
			responseMessage.setStatus(Message.STATUS_SUCCESS);
			responseMessage
					.setSuccessMessage("User Social Account Unlinked Successfully.");
		} else {
			responseMessage.setStatus(Message.STATUS_FAILED);
			responseMessage = getErrorMessage(result, request);
		}
		setResponseMessage(model, responseMessage, user);
		return model;
	}

private ModelAndView recognizeAccountAndLogin(ModelAndView model, SessionUser user,ExternalAccountLogin requestMessage, HttpServletRequest request ) throws FDException, JsonException{
		ExternalAccountControllerTagWrapper wrapper = new ExternalAccountControllerTagWrapper(user);
		ResultBundle resultBundle = wrapper.recognizeAccount(requestMessage);
		ActionResult result = resultBundle.getActionResult();
		propogateSetSessionValues(request.getSession(), resultBundle);
		Message responseMessage = null;
		if(result.isSuccess()){
			responseMessage = new SocialLoginResponse();
			responseMessage.setStatus(Message.STATUS_SUCCESS);
			((SocialLoginResponse) responseMessage).setUserExists(true);
			((SocialLoginResponse) responseMessage).setLoggedInSuccess(true);
			if(resultBundle.getExtraData(ExternalAccountControllerTagWrapper.EXTERNAL_PROVIDERS) != null)
				((SocialLoginResponse) responseMessage).setProviderTypes((List<String>) resultBundle.getExtraData(ExternalAccountControllerTagWrapper.EXTERNAL_PROVIDERS));
			((SocialLoginResponse) responseMessage).setUserSocialProfile((UserSocialProfile) resultBundle.getExtraData(ExternalAccountControllerTagWrapper.EXTERNAL_USERPROFILE));
			responseMessage.setSuccessMessage("User was recognized and successfully logged in");
		} else {
			 responseMessage =  getErrorMessage(result, request);
			
		}
		setResponseMessage(model, responseMessage, user);
		return model;
	}


	private ModelAndView socialConnectAccount(ModelAndView model,
			SessionUser user, ExternalAccountLogin requestMessage,
			HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
		ExternalAccountControllerTagWrapper wrapper = new ExternalAccountControllerTagWrapper(
				user);
		HashMap<String, String> socialUser = null;
		String userToken = null;
		String accessToken = requestMessage.getAccessToken();
		String providerName = requestMessage.getProvider();
		String source = null;
		SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
		socialUser = socialProvider.getSocialUserProfileByAccessToken(accessToken, providerName);
		if(socialUser!=null){
		userToken = socialUser.get("userToken");
		}
		
		if (socialUser != null) {
			String email = (String) socialUser.get("email");
			if((email==null || email.equalsIgnoreCase("")) && userToken!=null) {
				socialUser = socialProvider.getSocialUserProfileByUserToken(userToken, providerName);	
			}
		}
		
		requestMessage.setUserToken(userToken);
		ResultBundle resultBundle = wrapper.recognizeAccount(requestMessage);
		ActionResult result = resultBundle.getActionResult();
		propogateSetSessionValues(request.getSession(), resultBundle);
		
		Message responseMessage = null;
		//HttpServletResponse response = null;

		String userIdFromDB = ExternalAccountManager.getUserIdForUserToken(userToken);
		
		if (result.isSuccess()) {
			if (userToken != null)
				/*socialUser = socialProvider.getSocialUserProfileByUserToken(
						userToken, providerName);*/
			responseMessage = setCurrentCartToTheUser(user, request, response);
			((LoggedIn) responseMessage).setNewUser("false");
			responseMessage
					.setSuccessMessage("User was recognized and successfully logged in");
		} else if (userIdFromDB == null || userIdFromDB.length() == 0) {
			responseMessage = getErrorMessage(result, request);
			if (responseMessage.getErrors().get("ERR_MERGE_PAGE_REDIRECT") != null) {
				// user social account linking will be done here
				if (userToken != null)
					/*socialUser = socialProvider
							.getSocialUserProfileByUserToken(userToken,
									providerName);*/
				if (socialUser != null) {
					String email = (String) socialUser.get("email");
					String provider = (String) socialUser.get("provider");
					if (userIdFromDB == null || userIdFromDB.length() == 0) {
						ExternalAccountLinkRequest requestMessageSocialLink = new ExternalAccountLinkRequest(email, "", userToken, userToken, provider, source);
						requestMessageSocialLink.setEmail(email);
						requestMessageSocialLink.setExistingToken(userToken);
						requestMessageSocialLink.setNewToken(userToken);
						requestMessageSocialLink.setPassword("");
						requestMessageSocialLink.setProvider(provider);
						ExternalAccountControllerTagWrapper wrapperSocialLink = new ExternalAccountControllerTagWrapper(
								user);
						ResultBundle resultBundleSocialLink = wrapperSocialLink
								.connectExistingAccounts(requestMessageSocialLink);
						ActionResult resultSocialLink = resultBundleSocialLink
								.getActionResult();
						if (resultSocialLink.isSuccess()) {
							responseMessage.setStatus(Message.STATUS_SUCCESS);
							responseMessage = setCurrentCartToTheUser(user,
									request, response);
							((LoggedIn) responseMessage).setNewUser("false");
						} else {
							responseMessage = new ExternalAccountLoginResponse();
							responseMessage.setStatus(Message.STATUS_FAILED);
							((ExternalAccountLoginResponse) responseMessage)
									.setLoggedInSuccess(false);
							responseMessage = getErrorMessage(result, request);
						}
						setResponseMessage(model, responseMessage, user);
						return model;
					}
				}
			} else if (responseMessage.getErrors().get(
					"ERR_SOCIAL_USER_UNRECOGNIZED") != null) {
				// registration and login goes here
				if (userToken != null)
				/*	socialUser = socialProvider
							.getSocialUserProfileByUserToken(userToken,
									providerName);*/
				if (socialUser != null) {
					String email = (String) socialUser.get("email");
					String displayName = (String) socialUser.get("displayName");
					String names[] = displayName.split(" ");
					String firstName = (names.length == 0) ? "" : names[0];
					String lastName = (names.length <= 1) ? ""
							: names[names.length - 1];
					String provider = (String) socialUser.get("provider");
					RegisterMessageEx requestMessageRegister = new RegisterMessageEx();
					requestMessageRegister.setEmail(email);
					requestMessageRegister.setFirstName(firstName);
					requestMessageRegister.setLastName(lastName);
					requestMessageRegister.setMobile_number("");
					requestMessageRegister.setPassword("");
					requestMessageRegister.setWorkPhone("");
					requestMessageRegister.setServiceType("");
					requestMessageRegister.setSecurityQuestion("");
					RegistrationControllerTagWrapper tagWrapper = new RegistrationControllerTagWrapper(
							user.getFDSessionUser());
					RegisterMessage registerMessage = new RegisterMessage();
					registerMessage.setFirstName(requestMessageRegister
							.getFirstName());
					registerMessage.setLastName(requestMessageRegister
							.getLastName());
					registerMessage.setEmail(requestMessageRegister.getEmail());
					registerMessage.setConfirmEmail(requestMessageRegister
							.getEmail());
					registerMessage.setPassword(requestMessageRegister
							.getPassword());
					registerMessage.setConfirmPassword(requestMessageRegister
							.getPassword());
					registerMessage.setSecurityQuestion(requestMessageRegister
							.getSecurityQuestion());
					registerMessage.setServiceType(requestMessageRegister
							.getServiceType());
					registerMessage.setWorkPhone(requestMessageRegister
							.getWorkPhone());
					ResultBundle resultBundleOne = tagWrapper
							.register(registerMessage);
					

					ExternalAccountLinkRequest requestMessageSocialLink = new ExternalAccountLinkRequest(email, "", userToken, userToken, provider, null);
					requestMessageSocialLink.setEmail(email);
					requestMessageSocialLink.setExistingToken(userToken);
					requestMessageSocialLink.setNewToken(userToken);
					requestMessageSocialLink.setPassword("");
					requestMessageSocialLink.setProvider(provider);
					ExternalAccountControllerTagWrapper wrapperSocialLink = new ExternalAccountControllerTagWrapper(
							user);
					ResultBundle resultBundleSocialLink = wrapperSocialLink
							.connectExistingAccounts(requestMessageSocialLink);
					ActionResult resultSocialLink = resultBundleSocialLink
							.getActionResult();
					if (!resultSocialLink.isSuccess()) {
						responseMessage.setStatus(Message.STATUS_FAILED);
						responseMessage = getErrorMessage(result, request);
					}
									
					if (resultBundleOne != null) {
						if (resultBundleOne.getActionResult() != null
								&& resultBundleOne.getActionResult()
										.getErrors() != null
								&& resultBundleOne.getActionResult()
										.getErrors().size() != 0) {
							ActionResult resultOne = resultBundleOne
									.getActionResult();
							responseMessage = getErrorMessage(resultOne,
									request);
							setResponseMessage(model, responseMessage, user);
							return model;
						}
					}
					ActionResult resultOne = resultBundleOne.getActionResult();
					propogateSetSessionValues(request.getSession(),
							resultBundle);
					if (resultOne.isSuccess()) {
						request.getSession().setAttribute(
								SessionName.APPLICATION,
								EnumTransactionSource.FDX_IPHONE.getCode());
						responseMessage = setCurrentCartToTheUser(user,
								request, response);
						((LoggedIn) responseMessage).setNewUser("true");
					} else {
						responseMessage = getErrorMessage(resultOne, request);
					}
					responseMessage.addWarningMessages(resultOne.getWarnings());
				}
			}
		}
		setResponseMessage(model, responseMessage, user);
		return model;
	}
	
	private ModelAndView connectExistingAccounts(ModelAndView model, SessionUser user,ExternalAccountLinkRequest requestMessage, HttpServletRequest request ) throws FDException, JsonException{
		ExternalAccountControllerTagWrapper wrapper = new ExternalAccountControllerTagWrapper(user);
		//ResultBundle resultBundle = wrapper.connectExistingAccounts(requestMessage);
		ResultBundle resultBundle = wrapper.connectExistingAccounts(requestMessage);
		
		ActionResult result = resultBundle.getActionResult();
		//Message responseMessage = null;
		
		Message responseMessage = new ExternalAccountLoginResponse();
		
		if(result.isSuccess()){
			responseMessage.setStatus(Message.STATUS_SUCCESS);
			
			((ExternalAccountLoginResponse) responseMessage).setLoggedInSuccess(true);
			
			responseMessage.setSuccessMessage("User Social Account Linked Successfully.");
		} else {
			 //responseMessage = getErrorMessage(result, request);
			responseMessage.setStatus(Message.STATUS_FAILED);
			
			((ExternalAccountLoginResponse) responseMessage).setLoggedInSuccess(false);
			
			//responseMessage.setSuccessMessage("Unable to Link User's Social Account.");
			
			responseMessage =  getErrorMessage(result, request);
			
		}
		
		setResponseMessage(model, responseMessage, user);
		
		return model;
	}
	
	private Message formatLoginMessage(SessionUser user) throws FDException {
		Message responseMessage = null;

		OrderHistory history = user.getOrderHistory();
		String cutoffMessage = user.getCutoffInfo();

		OrderInfo closestPendingOrder = history
				.getClosestPendingOrderInfo(new Date());
		List<OrderInfo> orderHistoryInfo = new ArrayList<OrderInfo>();
		if (closestPendingOrder != null) {
			orderHistoryInfo.add(closestPendingOrder);
		}

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
				.setOrders(com.freshdirect.mobileapi.controller.data.response.OrderHistory.Order
						.createOrderList(orderHistoryInfo, user));
		((LoggedIn) responseMessage)
				.setSuccessMessage("User has been logged in successfully.");
		((LoggedIn) responseMessage).setItemsInCartCount(user
				.getItemsInCartCount());
		((LoggedIn) responseMessage).setOrderCount(user.getOrderHistory()
				.getValidOrderCount());
		((LoggedIn) responseMessage).setFdUserId(user.getPrimaryKey());

		// DOOR3 FD-iPad FDIP-474
		//Note: The field is inappropriately named. It is not referring to whether or not the user is on the FD mailing
		//list, but rather intended to represent whether or not the user is to be notified
		//in the event of service being introduced to their area.
		((LoggedIn) responseMessage).setOnMailingList( user.isFutureZoneNotificationEmailSentForCurrentAddress() );

		if (cutoffMessage != null) {
			responseMessage.addNoticeMessage(
					MessageCodes.NOTICE_DELIVERY_CUTOFF, cutoffMessage);
		}

		if (!user.getFDSessionUser().isCouponsSystemAvailable()) {
			responseMessage.addWarningMessage(
					MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE,
					SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
		}

		// With Mobile App having given ability to add/remove payment method
		// this is removed
		/*
		 * if ((user.getPaymentMethods() == null) ||
		 * (user.getPaymentMethods().size() == 0)) {
		 * responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD,
		 * ERR_NO_PAYMENT_METHOD_MSG); }
		 */
		((LoggedIn) responseMessage).setBrowseEnabled(MobileApiProperties
				.isBrowseEnabled());

		// Added during Mobile Coremetrics Implementation
		((LoggedIn) responseMessage).setSelectedServiceType(user
				.getSelectedServiceType() != null ? user
				.getSelectedServiceType().toString() : "");
		((LoggedIn) responseMessage).setCohort(user.getCohort());
		((LoggedIn) responseMessage).setTotalOrderCount(user
				.getTotalOrderCount());

		return responseMessage;
	}
	
	private Message setCurrentCartToTheUser(SessionUser user, HttpServletRequest request, HttpServletResponse response) throws FDException{
		FDCartModel currentCart = (FDCartModel)request.getSession().getAttribute(SessionName.CURRENT_CART);
		try {
			user = getUserFromSession(request, response);
		} catch (NoSessionException e) {
			e.printStackTrace();
		}
		MergeCartControllerTagWrapper tagWrapper = new MergeCartControllerTagWrapper(user);
		ActionResult mergeActionResult = tagWrapper.mergeCart(currentCart);
		if(mergeActionResult.isFailure()){
			throw new FDAuthenticationException();
		}
		user.getFDSessionUser().saveCart();
		UserUtil.createSessionUser(request, response, user.getFDSessionUser());
		Message responseMessage = formatLoginMessage(user);
		resetMobileSessionData(request);
		if (user != null) {
			FDCustomerCouponUtil.initCustomerCoupons(request.getSession());
		}
		return responseMessage;
	}
}
