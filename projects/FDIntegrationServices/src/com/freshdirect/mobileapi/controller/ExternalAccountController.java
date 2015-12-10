package com.freshdirect.mobileapi.controller;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.UserSocialProfile;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountRegisterRequest;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessage;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessageEx;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLinkRequest;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLogin;
import com.freshdirect.mobileapi.controller.data.request.RegisterMessageFdxRequest;
import com.freshdirect.mobileapi.controller.data.request.SocialLogin;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.SocialLoginResponse;
import com.freshdirect.mobileapi.controller.data.response.ExternalAccountLoginResponse;
import com.freshdirect.mobileapi.controller.data.response.SocialResponse;
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
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

import java.util.List;

public class ExternalAccountController extends BaseController implements SystemMessageList{

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
		} if(ACTION_SOCIAL_CONNECT_ACCOUNT.equals(action)){			
			SocialLogin requestMessage = null;
			requestMessage = parseRequestObject(request, response, SocialLogin.class);
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



	//Social Connect for handling Social login/signup
	private ModelAndView socialConnectAccount(ModelAndView model,
			SessionUser user, SocialLogin requestMessage,
			HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
		
		HashMap<String, String> socialUser = null;
		String userToken = null;
		String userId = null;
		String socialEmail = null;
		String socialAccountProvider = null;
		Message responseMessage = null;
		HttpSession session = request.getSession();
		
		//get access token, provider and context from request pay load
		String accessToken = requestMessage.getAccessToken();
		String providerName = requestMessage.getProvider();
		String context = requestMessage.getContext();
		
		//get oneall social provider
		SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
		//get social user profile by access token
		socialUser = socialProvider.getSocialUserProfileByAccessToken(accessToken, providerName);
		if(socialUser!=null){
			userToken = socialUser.get("userToken");
			socialEmail = (String) socialUser.get("email");
			socialAccountProvider = (String) socialUser.get("provider");
			if(userToken!=null && (!userToken.equalsIgnoreCase(""))) {
				requestMessage.setUserToken(userToken);
			} else {
				responseMessage = new SocialResponse();
				// user token not valid
				((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_INVALID_USERTOKEN);
				((SocialResponse) responseMessage).setResultAction("CANCELED");
				setResponseMessage(model, responseMessage, user);
				return model;
			}
		} else {
			// social profile not found
			responseMessage = new SocialResponse();
			((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_PROFILE_NOT_FOUND);
			((SocialResponse) responseMessage).setResultAction("CANCELED");
			setResponseMessage(model, responseMessage, user);
			return model;
		}		
		
		//get user id from EXTERNAL_ACCOUNT_LINK table using user token
		 userId = ExternalAccountManager.getUserIdForUserToken(userToken);
		
		try {

			
			if(context.equalsIgnoreCase("CREATE") || context.equalsIgnoreCase("SIGNIN")) {

				// when user id from EXTERNAL_ACCOUNT_LINK do login
				if(userId!=null && !userId.equalsIgnoreCase("")) {
					userLogin(userId, session, request, response);
					responseMessage = setCurrentCartToTheUser(user, request, response);
					((LoggedIn) responseMessage).setResultAction("SIGNEDIN");
					if (context.equalsIgnoreCase("CREATE")) {
						((LoggedIn) responseMessage)
						.setResultMessage(MSG_SOCIAL_ACCOUNT_EXIST_SIGNIN);
					} else if (context.equalsIgnoreCase("SIGNIN")) {
						((LoggedIn) responseMessage)
						.setResultMessage(MSG_SOCIAL_AUTO_SIGNIN);
					}
				} else {
					// When user id from EXTERNAL_ACCOUNT_LINK is null
					
					int isFDAccountExist = ExternalAccountManager.isUserEmailAlreadyExist(socialEmail, socialAccountProvider);
					
					if(isFDAccountExist == 0) {
						// FD Account and No Social link
						SessionUser sessionUser = getUserFromSession(request, response);
						if(sessionUser.isLoggedIn()){
							logout(model, user, request, response);
						}
						userLogin(socialEmail, session, request, response);		
						user = getUserFromSession(request, response);
						if(user!=null & user.getFDSessionUser()!=null && user.getFDSessionUser().getIdentity()!=null && user.getUsername()!=null) {
							ExternalAccountManager.linkUserTokenToUserId(
								user.getFDSessionUser().getIdentity().getErpCustomerPK(),
								user.getUsername(),
								socialUser.get("userToken"),
								socialUser.get("identityToken"),
								socialUser.get("provider"),
								socialUser.get("displayName"),
								socialUser.get("preferredUsername"),
								socialEmail, socialUser.get("emailVerified"));
						}
						responseMessage = setCurrentCartToTheUser(user, request, response);
						((LoggedIn) responseMessage)
						.setResultMessage(MessageFormat.format(MSG_SOCIAL_EXISTING_LINK_SIGNIN, socialAccountProvider));
						((LoggedIn) responseMessage).setResultAction("SIGNEDIN");
					} else if (isFDAccountExist == 1){ 
						// FD Account and Non matching Social link
						//if(user!=null & user.getFDSessionUser()!=null && user.getFDSessionUser().getUserId()!=null) {
						SessionUser sessionUser = getUserFromSession(request, response);
						if(sessionUser.isLoggedIn()){
							logout(model, user, request, response);
						}
						userLogin(socialEmail, session, request, response);
						user = getUserFromSession(request, response);
						//}
						if(user!=null & user.getFDSessionUser()!=null && user.getFDSessionUser().getIdentity()!=null && user.getUsername()!=null) {
							ExternalAccountManager.unlinkExternalAccountWithUser(user.getFDSessionUser().getIdentity().getErpCustomerPK(), socialAccountProvider);
							ExternalAccountManager.linkUserTokenToUserId(
								user.getFDSessionUser().getIdentity().getErpCustomerPK(),
								user.getUsername(),
								socialUser.get("userToken"),
								socialUser.get("identityToken"),
								socialUser.get("provider"),
								socialUser.get("displayName"),
								socialUser.get("preferredUsername"),
								socialEmail, socialUser.get("emailVerified"));
						}
						responseMessage = setCurrentCartToTheUser(user, request, response);
						((LoggedIn) responseMessage)
						.setResultMessage(MessageFormat.format(MSG_SOCIAL_EXISTING_LINK_SIGNIN, socialAccountProvider));
						((LoggedIn) responseMessage).setResultAction("SIGNEDIN");
					} else {
						//No FD Account and No matching Social link
						if(context.equalsIgnoreCase("SIGNIN")){
							responseMessage = new SocialResponse();
							((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_NO_ACCOUNT_CONNECTED);
							((SocialResponse) responseMessage).setResultAction("NOMATCH");
						} else if (context.equalsIgnoreCase("CREATE")) {
							//Registering social user
							ResultBundle registrationResultBundle = userRegistration(socialUser, user, request, response);
							/*SessionUser sessionUser = getUserFromSession(request, response);
							if(sessionUser.isLoggedIn()){
								logout(model, user, request, response);
							}*/
							userLogin(socialEmail, session, request, response);	
							user = getUserFromSession(request, response);
							if(user!=null & user.getFDSessionUser()!=null && user.getFDSessionUser().getIdentity()!=null && user.getUsername()!=null) {
								ExternalAccountManager.linkUserTokenToUserId(
									user.getFDSessionUser().getIdentity().getErpCustomerPK(),
									user.getUsername(),
									socialUser.get("userToken"),
									socialUser.get("identityToken"),
									socialUser.get("provider"),
									socialUser.get("displayName"),
									socialUser.get("preferredUsername"),
									socialEmail, socialUser.get("emailVerified"));
							}
							if(registrationResultBundle.getActionResult().isSuccess()) {
								request.getSession().setAttribute(
										SessionName.APPLICATION,
										EnumTransactionSource.FDX_IPHONE.getCode());
								responseMessage = setCurrentCartToTheUser(user,
										request, response);
								((LoggedIn) responseMessage).setResultMessage(MSG_SOCIAL_ACCOUNT_CREATED);
								((LoggedIn) responseMessage).setResultAction("SIGNEDIN");

							}
						}

					}

				}
			} else if (context.equalsIgnoreCase("LINK")) {
				// unlink if there is any link available for the same social network
				if(user!=null & user.getFDSessionUser()!=null && user.getFDSessionUser().getIdentity()!=null && user.getUsername()!=null) {
					ExternalAccountManager.unlinkExternalAccountWithUser(user.getFDSessionUser().getIdentity().getErpCustomerPK(), socialAccountProvider);
					ExternalAccountManager.linkUserTokenToUserId(
						user.getFDSessionUser().getIdentity().getErpCustomerPK(),
						user.getUsername(),
						socialUser.get("userToken"),
						socialUser.get("identityToken"),
						socialUser.get("provider"),
						socialUser.get("displayName"),
						socialUser.get("preferredUsername"),
						socialEmail, socialUser.get("emailVerified"));
				}
				responseMessage = new SocialResponse();
				((SocialResponse) responseMessage).setLoggedInSuccess(true);
				((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_LINKED);
				((SocialResponse) responseMessage).setResultAction("LINKED");
			} else if (context.equalsIgnoreCase("UNLINK")) {
				if(user!=null & user.getFDSessionUser()!=null && user.getFDSessionUser().getIdentity()!=null) {
					ExternalAccountManager.unlinkExternalAccountWithUser(user.getFDSessionUser().getIdentity().getErpCustomerPK(), socialUser.get("provider"));
				}
				responseMessage = new SocialResponse();
				((SocialResponse) responseMessage).setLoggedInSuccess(true);
				((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_UNLINKED);
				((SocialResponse) responseMessage).setResultAction("UNLINKED");

			}
		} catch (Exception e) {
			//TODO: Handle the runtime exception
			e.printStackTrace();
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
	
	public void userLogin(String userId, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws FDAuthenticationException, FDResourceException {
		/*try {*/
			FDIdentity identity = FDCustomerManager.login(userId);
			LOGGER.info("Identity : erpId = " + identity.getErpCustomerPK()
					+ " : fdId = " + identity.getFDCustomerPK());

			FDUser loginUser = FDCustomerManager.recognize(identity);

			LOGGER.info("FDUser : erpId = "
					+ loginUser.getIdentity().getErpCustomerPK() + " : "
					+ loginUser.getIdentity().getFDCustomerPK());

			FDSessionUser currentUser = (FDSessionUser) session
					.getAttribute(SessionName.USER);

			

			LOGGER.info("loginUser is " + loginUser.getFirstName()
					+ " Level = " + loginUser.getLevel());
			LOGGER.info("currentUser is "
					+ (currentUser == null ? "null" : currentUser
							.getFirstName() + currentUser.getLevel()));
			String currentUserId = null;
			if (currentUser == null) {
				// this is the case right after signout
				UserUtil.createSessionUser(request, response, loginUser);

			} else if (!loginUser.getCookie().equals(currentUser.getCookie())) {
				// current user is different from user who just logged in
				int currentLines = currentUser.getShoppingCart()
						.numberOfOrderLines();
				int loginLines = loginUser.getShoppingCart()
						.numberOfOrderLines();

				// address needs to be set using logged in user's information -
				// in case existing cart is used or cart merge
				currentUser.getShoppingCart().setDeliveryAddress(
						loginUser.getShoppingCart().getDeliveryAddress());

				
					// keep current cart
					loginUser.setShoppingCart(currentUser.getShoppingCart());
                    loginUser.getShoppingCart().setUserContextToOrderLines(loginUser.getUserContext());  

			

				// merge coupons
				currentUserId = currentUser.getPrimaryKey();

				// current user has gift card recipients that need to be added
				// to the login user's recipients list
				if (currentUser.getLevel() == FDUserI.GUEST
						&& currentUser.getRecipientList().getRecipients()
								.size() > 0) {
					List<RecipientModel> tempList = currentUser
							.getRecipientList().getRecipients();
					ListIterator<RecipientModel> iterator = tempList
							.listIterator();
					// add currentUser's list to login user
					while (iterator.hasNext()) {
						SavedRecipientModel srm = (SavedRecipientModel) iterator
								.next();
						// reset the FDUserId to the login user
						srm.setFdUserId(loginUser.getUserId());
						loginUser.getRecipientList().removeRecipients(
								EnumGiftCardType.DONATION_GIFTCARD);
						loginUser.getRecipientList().addRecipient(srm);
					}
				}

				loginUser.setGiftCardType(currentUser.getGiftCardType());

				if (currentUser.getDonationTotalQuantity() > 0) {
					loginUser.setDonationTotalQuantity(currentUser
							.getDonationTotalQuantity());
				}
				UserUtil.createSessionUser(request, response, loginUser);
				// The previous recommendations of the current session need to
				// be removed.
				session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
				session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
				session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);

			} else {
				// the logged in user was the same as the current user,
				// that means that they were previously recognized by their
				// cookie before log in
				// just set their login status and move on
				currentUser.isLoggedIn(true);
				session.setAttribute(SessionName.USER, currentUser);
			}
			// loginUser.setEbtAccepted(loginUser.isEbtAccepted()&&(loginUser.getOrderHistory().getUnSettledEBTOrderCount()<=0));
			FDSessionUser user = (FDSessionUser) session
					.getAttribute(SessionName.USER);
			if (user != null) {
				user.setEbtAccepted(user.isEbtAccepted()
						&& (user.getOrderHistory().getUnSettledEBTOrderCount() < 1)
						&& !user.hasEBTAlert());
				FDCustomerCouponUtil
						.initCustomerCoupons(session, currentUserId);
			}

			
			// 
			

			if (user != null) {
				user.setJustLoggedIn(true);
			}

			

		/*} catch (FDResourceException fdre) {
			LOGGER.warn("Resource error during authentication", fdre);
			
			
		} catch (FDAuthenticationException fdae) {
			LOGGER.error(fdae.getMessage());
		}*/
	
	}
	
	private ResultBundle userRegistration(HashMap socialUser, SessionUser user, HttpServletRequest request, HttpServletResponse response) throws FDException {
		ResultBundle resultBundleOne = new ResultBundle();
		 
		// registration and login goes here
		if (socialUser != null) {
			String email = (String) socialUser.get("email");
			String displayName = (String) socialUser.get("displayName");
			String names[] = displayName.split(" ");
			String firstName = (names.length == 0) ? "" : names[0];
			String lastName = (names.length <= 1) ? ""
					: names[names.length - 1];
			String provider = (String) socialUser.get("provider");
			String userToken = (String) socialUser.get("userToken");
			//RegisterMessageFdxRequest requestMessageRegister = new RegisterMessageFdxRequest();
			ExternalAccountRegisterRequest requestMessageRegister = new ExternalAccountRegisterRequest();
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
			/*RegisterMessage registerMessage = new RegisterMessage();
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
					.getWorkPhone());*/
			resultBundleOne = tagWrapper
					.registerSocial(requestMessageRegister);	
			
		}
	
		return resultBundleOne;
	}
	
	private ModelAndView logout(ModelAndView model, SessionUser user,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonException {

		removeUserInSession(user, request, response);

		Message responseMessage = Message
				.createSuccessMessage("User logged out successfully.");
		setResponseMessage(model, responseMessage, user);
		return model;
	}
}
