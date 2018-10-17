package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.UserSocialProfile;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLinkRequest;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountLogin;
import com.freshdirect.mobileapi.controller.data.request.ExternalAccountRegisterRequest;
import com.freshdirect.mobileapi.controller.data.request.SocialLogin;
import com.freshdirect.mobileapi.controller.data.response.ExternalAccountLoginResponse;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.SocialLoginResponse;
import com.freshdirect.mobileapi.controller.data.response.SocialResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.ExternalAccountControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.MergeCartControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.RegistrationControllerTagWrapper;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SocialGateway;
import com.freshdirect.webapp.taglib.fdstore.SocialProvider;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class ExternalAccountController extends BaseController implements SystemMessageList{

    private static final Category LOGGER = LoggerFactory.getInstance(ExternalAccountController.class);

	//Actions
	private final static String ACTION_RECOGNIZE_ACCOUNT = "login";
	private final static String ACTION_SOCIAL_CONNECT_ACCOUNT = "socialConnect";
	private final static String ACTION_SOCIAL_LOGIN = "socialLogin";
	private final static String ACTION_LINK_ACCOUNT = "linkaccount";
	private final static String ACTION_UNLINK_ACCOUNT = "unlinkaccount";

    @Override
    protected boolean validateUser() {
        return false;
    }

	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws JsonException, FDException  {
		//recognize Account will merge the existing accounts as well as login the user.
	    Message message = null;
		if(ACTION_RECOGNIZE_ACCOUNT.equals(action)){
			ExternalAccountLogin requestMessage = parseRequestObject(request, response, ExternalAccountLogin.class);
			message = recognizeAccountAndLogin(user, requestMessage, request);
		} else if (ACTION_LINK_ACCOUNT.equals(action)){
			ExternalAccountLinkRequest requestMessage = parseRequestObject(request, response, ExternalAccountLinkRequest.class);
			message = connectExistingAccounts(user, requestMessage, request);
		} else if(ACTION_UNLINK_ACCOUNT.equals(action)){
			ExternalAccountLogin requestMessage = parseRequestObject(request, response, ExternalAccountLogin.class);
			message = unlinkExistingAccounts(user, requestMessage, request);
		} if(ACTION_SOCIAL_CONNECT_ACCOUNT.equals(action)){
			SocialLogin requestMessage = parseRequestObject(request, response, SocialLogin.class);
			Map<String, String> socialUserProfile = getSocialUserProfile(requestMessage.getAccessToken(), requestMessage.getProvider());
			message = socialConnectAccount(user, socialUserProfile, requestMessage.getContext(), request, response);
		} if(ACTION_SOCIAL_LOGIN.equals(action)){
		    // use connection token for social login, which comes from oneall service
		    Map<String, String> socialUserProfile = getSocialUserProfile(request.getParameter("connection_token"));
		    message = socialConnectAccount(user, socialUserProfile, request.getParameter("context"), request, response);
		    String redirectUrl = getRedirectUrl(request, message);
			redirectAfterLogin(response, redirectUrl);
		}
		if(message==null){
			message = getErrorMessage("RESP_MSG_NULL", "Response Message Null");
			LOGGER.error("EXTERNALACCOUNTCONTROLLER - Response Message Null for action - " + action + " and user " + (user != null && user.getFDSessionUser() != null
					? (user.getFDSessionUser().getIdentity() != null && user.getFDSessionUser().getFDCustomer() != null
					? user.getFDSessionUser().getFDCustomer().getErpCustomerPK() : user.getFDSessionUser().getPrimaryKey() ) : "NOUSER" ) );
		}
		setResponseMessage(model, message, user);
		return model;
	}

    private String getRedirectUrl(HttpServletRequest request, Message message) {
        String redirectUrl = request.getParameter("redirect_url");
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            if (message instanceof SocialResponse) {
                SocialResponse socialMessage = (SocialResponse) message;
                redirectUrl = addParameter(redirectUrl, "action", socialMessage.getResultAction());
            }
        }
        return redirectUrl;
    }

    private String addParameter(String url, String key, String value){
        if (url.contains("?")){
            url = url +  "&" + key + "=" + value;
        } else {
            url = url +  "?" + key + "=" + value;
        }
        return url;
    }

    private void redirectAfterLogin(HttpServletResponse response, String redirectUrl) {
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            try {
                response.sendRedirect(response.encodeRedirectURL(redirectUrl));
            } catch (IOException e) {
                LOGGER.error("Given redirect url could not be found: " + redirectUrl);
            }
        }
    }

	private Message unlinkExistingAccounts(SessionUser user, ExternalAccountLogin requestMessage, HttpServletRequest request) {
		ExternalAccountControllerTagWrapper wrapper = new ExternalAccountControllerTagWrapper(user);
		ResultBundle resultBundle = wrapper.unlinkExistingAccounts(requestMessage);
		ActionResult result = resultBundle.getActionResult();
		Message responseMessage = new ExternalAccountLoginResponse();
		if (result.isSuccess()) {
			responseMessage.setStatus(Message.STATUS_SUCCESS);
			responseMessage.setSuccessMessage("User Social Account Unlinked Successfully.");
		} else {
			responseMessage.setStatus(Message.STATUS_FAILED);
			responseMessage = getErrorMessage(result, request);
		}
		return responseMessage;
	}

    private Message recognizeAccountAndLogin(SessionUser user,ExternalAccountLogin requestMessage, HttpServletRequest request) throws FDException{
		ExternalAccountControllerTagWrapper wrapper = new ExternalAccountControllerTagWrapper(user);
		setContextHeaders(request, wrapper);
		ResultBundle resultBundle = wrapper.recognizeAccount(requestMessage);
		ActionResult result = resultBundle.getActionResult();
		propogateSetSessionValues(request.getSession(), resultBundle);
		Message responseMessage = new SocialLoginResponse();
		if(result.isSuccess()){
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
		return responseMessage;
	}

    private Map<String, String> getSocialUserProfile(String accessToken, String providerName) {
        Map<String, String> socialUserProfile = null;
        SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
        //get social user profile by access tokenredirectAfterLogin(response, requestMessage);
        if (accessToken != null && !accessToken.isEmpty()) {
            socialUserProfile = socialProvider.getSocialUserProfileByAccessToken(accessToken, providerName);
        }
        return socialUserProfile;
    }

    private Map<String, String> getSocialUserProfile(String connectionToken) {
        Map<String, String> socialUserProfile = null;
        SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
        if (socialProvider != null && connectionToken != null && connectionToken.length() > 0) {
            // Retrieve social user's profile
            socialUserProfile = socialProvider.getSocialUserProfile(connectionToken);
        }
        return socialUserProfile;
    }

	//Social Connect for handling Social login/signup
	private Message socialConnectAccount(SessionUser user, Map<String, String> socialUser, String context,
			HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
		String userToken = null;
		String userId = null;
		String socialEmail = null;
		String socialAccountProvider = null;
		Message responseMessage = null;
		HttpSession session = request.getSession();

        if (request.getSession().getAttribute(SessionName.APPLICATION) == null) {
            request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, null));
        }

		if(socialUser!=null){
			userToken = socialUser.get("userToken");
			socialEmail = socialUser.get("email");
			socialAccountProvider = socialUser.get("provider");
			if(socialEmail == null || socialEmail.equalsIgnoreCase("")) {
				responseMessage = new SocialResponse();
				// no email address found for social login
				((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_PROFILE_EMAIL_NOT_FOUND);
				((SocialResponse) responseMessage).setResultAction("CANCELED");
				return responseMessage;
			}else if(userToken!=null && (!userToken.equalsIgnoreCase(""))) {
//				requestMessage.setUserToken(userToken);
			} else {
				responseMessage = new SocialResponse();
				// user token not valid
				((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_INVALID_USERTOKEN);
				((SocialResponse) responseMessage).setResultAction("CANCELED");
				return responseMessage;
			}
		} else {
			// social profile not found
			responseMessage = new SocialResponse();
			((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_PROFILE_NOT_FOUND);
			((SocialResponse) responseMessage).setResultAction("CANCELED");
			return responseMessage;
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
					((LoggedIn) responseMessage).setFdxDpEnabled(FDStoreProperties.isDlvPassFDXEnabled());
					boolean isPurchaseDlvPassEligible = user.getFDSessionUser().isEligibleForDeliveryPass();
			        if(isPurchaseDlvPassEligible){
			        	isPurchaseDlvPassEligible = (null ==user.getPromotionEligibility() || null ==user.getPromotionEligibility().getWaiveChargeTypePromotionCodes()||user.getPromotionEligibility().getWaiveChargeTypePromotionCodes().isEmpty());        	
			        }
			        ((LoggedIn) responseMessage).setPurchaseDlvPassEligible(isPurchaseDlvPassEligible);
			        ((LoggedIn) responseMessage).setShowDeliveryPassBanner(isPurchaseDlvPassEligible);
					((LoggedIn) responseMessage).setDpActive(user.getFDSessionUser().getDeliveryPassStatus().equals(EnumDlvPassStatus.ACTIVE) ? true : false);
					checkTermsCond(getUserFromSession(request, response),responseMessage);
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
							logout(user, request, response);
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
						((LoggedIn) responseMessage).setFdxDpEnabled(FDStoreProperties.isDlvPassFDXEnabled());
						((LoggedIn) responseMessage).setPurchaseDlvPassEligible(user.getFDSessionUser().isEligibleForDeliveryPass());
						((LoggedIn) responseMessage).setResultAction("SIGNEDIN");
						checkTermsCond(user,responseMessage);
					} else if (isFDAccountExist == 1){
						// FD Account and Non matching Social link
						//if(user!=null & user.getFDSessionUser()!=null && user.getFDSessionUser().getUserId()!=null) {
						SessionUser sessionUser = getUserFromSession(request, response);
						if(sessionUser.isLoggedIn()){
							logout(user, request, response);
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
						((LoggedIn) responseMessage).setFdxDpEnabled(FDStoreProperties.isDlvPassFDXEnabled());
						((LoggedIn) responseMessage).setPurchaseDlvPassEligible(user.getFDSessionUser().isEligibleForDeliveryPass());
						((LoggedIn) responseMessage).setResultAction("SIGNEDIN");
						checkTermsCond(user,responseMessage);
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
								responseMessage = setCurrentCartToTheUser(user,
										request, response);
								((LoggedIn) responseMessage).setResultMessage(MSG_SOCIAL_ACCOUNT_CREATED);
								((LoggedIn) responseMessage).setFdxDpEnabled(FDStoreProperties.isDlvPassFDXEnabled());
								((LoggedIn) responseMessage).setPurchaseDlvPassEligible(user.getFDSessionUser().isEligibleForDeliveryPass());
								((LoggedIn) responseMessage).setResultAction("SIGNEDIN");
								checkTermsCond(user,responseMessage);
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
                SocialProvider socialProvider = SocialGateway.getSocialProvider("ONE_ALL");
                if (socialProvider != null) {
                    socialProvider.deleteSocialIdentity(socialUser.get("identityToken"));
                }
				if(user!=null & user.getFDSessionUser()!=null && user.getFDSessionUser().getIdentity()!=null) {
					ExternalAccountManager.unlinkExternalAccountWithUser(user.getFDSessionUser().getIdentity().getErpCustomerPK(), socialUser.get("provider"));
				}
				responseMessage = new SocialResponse();
				((SocialResponse) responseMessage).setLoggedInSuccess(true);
				((SocialResponse) responseMessage).setResultMessage(MSG_SOCIAL_UNLINKED);
				((SocialResponse) responseMessage).setResultAction("UNLINKED");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return responseMessage;
	}

	private Message connectExistingAccounts(SessionUser user,ExternalAccountLinkRequest requestMessage, HttpServletRequest request) throws FDException{
		ExternalAccountControllerTagWrapper wrapper = new ExternalAccountControllerTagWrapper(user);
		ResultBundle resultBundle = wrapper.connectExistingAccounts(requestMessage);
		ActionResult result = resultBundle.getActionResult();
		Message responseMessage = new ExternalAccountLoginResponse();
		if(result.isSuccess()){
			responseMessage.setStatus(Message.STATUS_SUCCESS);
			((ExternalAccountLoginResponse) responseMessage).setLoggedInSuccess(true);
			responseMessage.setSuccessMessage("User Social Account Linked Successfully.");
		} else {
			responseMessage.setStatus(Message.STATUS_FAILED);
			((ExternalAccountLoginResponse) responseMessage).setLoggedInSuccess(false);
			responseMessage =  getErrorMessage(result, request);
		}
		return responseMessage;
	}

	private Message setCurrentCartToTheUser(SessionUser user, HttpServletRequest request, HttpServletResponse response) throws FDException{
		FDCartModel currentCart = (FDCartModel)request.getSession().getAttribute(SessionName.CURRENT_CART);
		try {
			user = getUserFromSession(request, response);
		} catch (NoSessionException e) {
			//e.printStackTrace();
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


			FDSessionUser currentUser = (FDSessionUser) session
					.getAttribute(SessionName.USER);

			FDUser loginUser = FDCustomerManager.recognize(identity, currentUser != null, CmsManager.getInstance().getEStoreEnum());

			LOGGER.info("FDUser : erpId = "
					+ loginUser.getIdentity().getErpCustomerPK() + " : "
					+ loginUser.getIdentity().getFDCustomerPK());
			

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

				 if ((currentLines > 0) && (loginLines > 0)) {
	                    // keep the current cart in the session and send them to the merge cart page
		                    session.setAttribute(SessionName.CURRENT_CART, currentUser.getShoppingCart());
	                } else if ((currentLines > 0) && (loginLines == 0)) {
	                    // keep current cart
	                    loginUser.setShoppingCart(currentUser.getShoppingCart());
	                    loginUser.getShoppingCart().setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(loginUser.getUserContext()));
	                    loginUser.getShoppingCart().setUserContextToOrderLines(loginUser.getUserContext());
	                }

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
				user.setTcAcknowledge(loginUser.getTcAcknowledge());
			}



		/*} catch (FDResourceException fdre) {
			LOGGER.warn("Resource error during authentication", fdre);


		} catch (FDAuthenticationException fdae) {
			LOGGER.error(fdae.getMessage());
		}*/

	}

	private ResultBundle userRegistration(Map<String, String> socialUser, SessionUser user, HttpServletRequest request, HttpServletResponse response) throws FDException {
		ResultBundle resultBundleOne = new ResultBundle();

		// registration and login goes here
		if (socialUser != null) {
			String email = socialUser.get("email");
			String displayName = socialUser.get("displayName");
			String names[] = displayName.split(" ");
			String firstName = (names.length == 0) ? "" : names[0];
			String lastName = (names.length <= 1) ? ""
					: names[names.length - 1];
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

	private Message logout(SessionUser user, HttpServletRequest request, HttpServletResponse response) {
		removeUserInSession(user, UserCleanupMode.SESSION_AND_COOKIE, request, response);
		return Message.createSuccessMessage("User logged out successfully.");
	}

	private void checkTermsCond(SessionUser user, Message responseMessage){
		((LoggedIn) responseMessage).setTcAcknowledge(user.getTcAcknowledge());
	}
}
