package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.FDCouponProperties;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.fdstore.customer.SilverPopupDetails;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.SubmitOrderExResult;
import com.freshdirect.mobileapi.controller.data.request.AckRequest;
import com.freshdirect.mobileapi.controller.data.request.Login;
import com.freshdirect.mobileapi.controller.data.request.PasswordMessageRequest;
import com.freshdirect.mobileapi.controller.data.request.SessionMessage;
import com.freshdirect.mobileapi.controller.data.request.ZipCheck;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.MessageResponse;
import com.freshdirect.mobileapi.controller.data.response.PageMessageResponse;
import com.freshdirect.mobileapi.controller.data.response.SessionResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.tagwrapper.MergeCartControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.storeapi.content.CMSPageRequest;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.LocatorUtil;

public class LoginController extends BaseController  implements SystemMessageList {

    private static final Category LOGGER = LoggerFactory.getInstance(LoginController.class);

    private static final String ACTION_LOGIN = "login";
    private static final String ACTION_LOGOUT = "logout";
    private static final String ACTION_FORGOT_PASSWORD = "forgotpassword";
    private static final String ACTION_CHANGE_PASSWORD = "changepassword";
    private static final String ACTION_PING = "ping";
    private static final String ACTION_INITIATE_SESSION_USER = "initiatesessionuser";
    private static final String ACTION_SOURCE = "source";
	private static final String MSG_INVALID_EMAIL = "Invalid or missing email address. If you need assistance please call us at 1-866-283-7374.";
	private static final String MSG_EMAIL_NOT_EXPIRED = "An email was already sent. Please try again later.";
	private static final String ERR_INVALID_TRANSACTIONCODE = "Invalid Transaction Source";
	private static final String ACTION_SESSION = "checksession";
	private static final String ACTION_ACK = "acknowledge";
	private static final String ACTION_SESSION_ADD_ANONYMOUS_ADDRESS = "addanonymousaddress";
	private static final String FAKE_MERGE_PAGE="fake_merge_page";
	private static final String FAKE_SUCCESS_PAGE="fake_success_page";
    private static final String DIR_ERROR_KEY="ERR_DARKSTORE_RECONCILIATION";

	@Override
    protected boolean validateUser() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.freshdirect.mobileapi.controller.BaseController#processRequest(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.web.servlet.ModelAndView, java.lang.String,
	 * com.freshdirect.mobileapi.model.SessionUser)
	 */
	@Override
    protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws FDException, ServiceException,
			NoSessionException, JsonException {

	    Message responseMessage = null;
		if (ACTION_LOGIN.equals(action)) {
			Login requestMessage = parseRequestObject(request, response,
					Login.class);
			try {
				// Check to see if user session exists
				SessionUser sessionUser = getUserFromSession(request, response);
				if(sessionUser.isLoggedIn()){
					logout(user, request, response);
				}

			} catch (NoSessionException e) {
				// Do nothing
			}
			responseMessage = login(requestMessage, request, response, false);
		} else if (ACTION_PING.equals(action)) {
		    responseMessage = ping(request, response);
        } else if (ACTION_INITIATE_SESSION_USER.equals(action)) {
            responseMessage = initiateSessionUser(request, response);
		} else if (ACTION_LOGOUT.equals(action)) {
		    responseMessage = logout(user, request, response);
		}else if (ACTION_SOURCE.equals(action)) {
		    SessionMessage requestMessage = parseRequestObject(request, response, SessionMessage.class);
			responseMessage = transactionSource(user, request, response,requestMessage);
		} else if (ACTION_FORGOT_PASSWORD.equals(action)) {
		    Login requestMessage = parseRequestObject(request, response, Login.class);
			responseMessage = forgotPassword(user, request, response, requestMessage);
		} else if (ACTION_CHANGE_PASSWORD.equals(action)) {
		    PasswordMessageRequest requestMessage = parseRequestObject(request, response, PasswordMessageRequest.class);
		    responseMessage = changePassword(request, response, requestMessage);
		} else if (ACTION_SESSION.equals(action)) {
		    SessionMessage requestMessage = parseRequestObject(request, response, SessionMessage.class);
			responseMessage = checkSession(request, response, user, requestMessage);
		}else if (ACTION_ACK.equals(action)) {
			AckRequest requestMessage = parseRequestObject(request, response, AckRequest.class);
			responseMessage = updateAck(request, response, user, requestMessage);
		}  else if (ACTION_SESSION_ADD_ANONYMOUS_ADDRESS.equals(action)) {
			ZipCheck requestMessage = parseRequestObject(request, response, ZipCheck.class);
			try {
				// Check to see if user session exists
				getUserFromSession(request, response);
				AddressModel address = new AddressModel();
				address.setAddress1(requestMessage.getAddress1());
				address.setApartment(requestMessage.getApartment());
				address.setCity(requestMessage.getCity());
				address.setState(requestMessage.getState());
				address.setZipCode(requestMessage.getZipCode());
				//FDX-1873 - Show timeslots for anonymous address
				address.setCustomerAnonymousAddress(true);
				user.setAddress(address);
				Calendar date = new GregorianCalendar();
				date.add( Calendar.DATE, 7 );
				FDDeliveryZoneInfo zoneInfo =  FDDeliveryManager.getInstance().getZoneInfo(address, date.getTime(), user.getFDSessionUser().getHistoricOrderSize(), null, (user.getFDSessionUser().getIdentity()!=null)?user.getFDSessionUser().getIdentity().getErpCustomerPK():null);
				if(user!=null && user.getShoppingCart()!=null && zoneInfo!=null) {
					user.getShoppingCart().setZoneInfo(zoneInfo);
				}

				if(!isAddressSet(user,address)) {
					Cart cart = user.getShoppingCart();
			        CartDetail cartDetail = cart.getCartDetail(user, EnumCouponContext.VIEWCART);
			        responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
			        responseMessage.addErrorMessage("Failed to add anonymous address. Cart has invalid items.");
			        ((com.freshdirect.mobileapi.controller.data.response.Cart)responseMessage).setCartDetail(cartDetail);
			        if(!user.getFDSessionUser().isCouponsSystemAvailable()&& FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
			        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
			        }
			        user.getFDSessionUser().resetUserContext();
				} else {
					//FDX-1873 - Show timeslots for anonymous address
					user.getShoppingCart().setDeliveryAddress(null);
		        	List<FDCartLineI> invalidLines=OrderLineUtil.getInvalidLines(user.getShoppingCart().getOrderLines(), user.getFDSessionUser().getUserContext());

		        	if(invalidLines.size()>0) {

		        		Cart cart = user.getShoppingCart();
				        CartDetail cartDetail = cart.getCartDetail(user, EnumCouponContext.VIEWCART);
				        responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
				        responseMessage.addErrorMessage(DIR_ERROR_KEY,MessageCodes.ERR_DIR_ADDRESS_SET_EX);
				        ((com.freshdirect.mobileapi.controller.data.response.Cart)responseMessage).setCartDetail(cartDetail);
				        /*if(!user.getFDSessionUser().isCouponsSystemAvailable()) {
				        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
				        }*/
				        setResponseMessage(model, responseMessage, user);
				        return model;
		        	}

					responseMessage = Message.createSuccessMessage("Anonymous Address added successfully.");
				}


			} catch (NoSessionException e) {
				 responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
			} catch (FDInvalidAddressException e) {
				 responseMessage = getErrorMessage("Invalid Address", "Invalid address");
			}
		}
		if(responseMessage==null){
			responseMessage = getErrorMessage("RESP_MSG_NULL", "Response Message Null");
			LOGGER.error("LOGINCONTROLLER - Response Message Null for action - " + action + " and user " + (user != null && user.getFDSessionUser() != null 
					? (user.getFDSessionUser().getIdentity() != null && user.getFDSessionUser().getFDCustomer() != null 
					? user.getFDSessionUser().getFDCustomer().getErpCustomerPK() : user.getFDSessionUser().getPrimaryKey() ) : "NOUSER" ) );
		}
		setResponseMessage(model, responseMessage, user);
		return model;
	}

    private Message forgotPassword(SessionUser user, HttpServletRequest request, HttpServletResponse response, Login requestMessage) throws JsonException {
        Message responseMessage = Message.createSuccessMessage("Password sent successfully.");

        try {
        	LOGGER.debug("Email is going to: " + requestMessage.getUsername());
        	FDCustomerManager.sendPasswordEmail(requestMessage.getUsername(), false);
        } catch (FDResourceException ex) {
        	responseMessage = getErrorMessage(
        			ERR_FORGOTPASSWORD_INVALID_EMAIL, MSG_INVALID_EMAIL);
        } catch (PasswordNotExpiredException pe) {
        	responseMessage = getErrorMessage(
        			ERR_FORGOTPASSWORD_EMAIL_NOT_EXPIRED,
        			MSG_EMAIL_NOT_EXPIRED);
        }
        return responseMessage;
    }

    private Message changePassword(HttpServletRequest request, HttpServletResponse response, PasswordMessageRequest passwordRequest) throws FDException, NoSessionException, JsonException {
        Message passwordResponse = null;
        String email = passwordRequest.getUsername();
        String password = passwordRequest.getPassword();
        try{
        if (FDCustomerManager.isPasswordRequestExpired(email, password)) {
            passwordResponse = Message.createFailureMessage(MSG_CHANGE_PASSWORD_TOKEN_EXPIRED);
        } else {
            try {
                FDCustomerManager.changePassword(AccountActivityUtil.getActionInfo(request.getSession()), email, password);
                LOGGER.debug("Password has been changed for email: " + email);
                passwordResponse = login(passwordRequest, request, response, false);
                passwordResponse.setSuccessMessage("User changes password successfully.");
            } catch (ErpInvalidPasswordException e) {
                LOGGER.warn("Could not change password for email: " + email);
                passwordResponse = Message.createFailureMessage(SystemMessageList.MSG_PASSWORD_LENGTH);
            }
        }
        } catch (FDResourceException e){
            LOGGER.warn("Try to change password with invalid username: " + email);
            passwordResponse = Message.createFailureMessage(MSG_USER_NOT_FOUND);
        }
        return passwordResponse;
    }

    private Message updateAck(HttpServletRequest request, HttpServletResponse response, SessionUser user, AckRequest requestMessage) throws NoSessionException, FDException {
        Message responseMessage = null;
        if (null == user) {
            throw new NoSessionException("No session");
        }

        String contentId = EnumEStoreId.valueOfContentId("FD".equalsIgnoreCase(requestMessage.getAppSource()) ? "FreshDirect" : requestMessage.getAppSource()).getContentId();
        boolean success = FDCustomerManager.updateAck(user.getFDSessionUser().getIdentity(), requestMessage.isAcknowledge(), contentId);

        if (!success) {
            responseMessage = getErrorMessage(ERR_AUTHENTICATION, MessageCodes.MSG_ACCEPT_FD_TERMSANDCONDITIONS_FAILED);
        } else {
            user.setTcAcknowledge(true);
            if (isExtraResponseRequested(request)) {
                MessageResponse messageResponse = new MessageResponse();
                populateResponseWithEnabledAdditionsForWebClient(user, messageResponse, request, null);
                responseMessage = messageResponse;
            } else {
                responseMessage = Message.createSuccessMessage(MessageCodes.MSG_ACCEPT_FD_TERMSANDCONDITIONS);
            }
        }
        return responseMessage;
    }

	private boolean isAddressSet(SessionUser user, AddressModel address) {
		return user.getAddress().isSameLocation(address);
	}



	/**
	 * @param request
	 * @return
	 * @throws JsonException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	private Message logout(SessionUser user, HttpServletRequest request, HttpServletResponse response)
			throws JsonException {
		Message responseMessage = null;
		try{
			removeUserInSession(user, request, response);
			responseMessage = Message
				.createSuccessMessage("User logged out successfully.");
		}catch(IllegalStateException e){
			responseMessage = getErrorMessage("SESSION_INVALID_EXCEPTION","USER session is invalid");
		}
		return responseMessage;
	}
	/**
	 * @param request
	 * @param requestMessage
	 * @return
	 * @throws JsonException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	private Message transactionSource(SessionUser user, HttpServletRequest request, HttpServletResponse response, SessionMessage requestMessage)
			throws JsonException {

		String source = requestMessage.getSource();
		Message responseMessage = new SessionResponse();
		EnumTransactionSource transactionSource = EnumTransactionSource.getTransactionSource(source);

		if (transactionSource == null) {
			responseMessage = getErrorMessage(ERR_INVALID_TRANSACTIONCODE, MessageCodes.MSG_INVALID_TRANSACTIONCODE);
		} else {
			request.getSession().setAttribute(SessionName.APPLICATION,transactionSource.getCode());
			if (user == null) {
				FDSessionUser fdSessionUser = null;
				try {
                    fdSessionUser = UserUtil.getSessionUserByCookie(request);
				} catch (FDResourceException ex) {
					LOGGER.warn(ex);
				}
				if (fdSessionUser != null) {
					FDCustomerCouponUtil.initCustomerCoupons(request
							.getSession());
					((SessionResponse) responseMessage).setSessionExpired(true);
				} else {
					fdSessionUser = LocatorUtil.useIpLocator(
                            request.getSession(), request, response);
				}
				((SessionResponse) responseMessage).setSessionIsNew(true);
				request.getSession().setAttribute(SessionName.USER,fdSessionUser);
				user = SessionUser.wrap(fdSessionUser);
			} else {
				((SessionResponse) responseMessage).setLoggedIn(user
						.isLoggedIn());
			}
			responseMessage.setConfiguration(getConfiguration(user));
		}
		return responseMessage;
}

    private Message ping(HttpServletRequest request, HttpServletResponse response) throws NoSessionException, FDException {
        Message responseMessage = null;
        SessionUser user = getUserFromSession(request, response);
        if (isExtraResponseRequested(request)) {
            MessageResponse messageResponse = new MessageResponse();
            populateResponseWithEnabledAdditionsForWebClient(user, messageResponse, request, null);
            responseMessage = messageResponse;
        } else {
        	try{
        		responseMessage = formatLoginMessage(user);
        	}catch(IllegalStateException e){
        		// supress
        	}
        }
        return responseMessage;
    }

    private Message initiateSessionUser(HttpServletRequest request, HttpServletResponse response) {
        Message responseMessage = new Message();
        try {
            getUserFromSession(request, response);
            responseMessage.setStatus(Message.STATUS_SUCCESS);
        } catch (NoSessionException e) {
            responseMessage.setStatus(Message.STATUS_FAILED);
        }
        return responseMessage;
    }


	/**
	 * @param requestMessage
	 * @param request
	 * @param response
	 * @return
	 * @throws FDException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws PricingException
	 * @throws NoSessionException
	 * @throws JsonException
	 */
	private Message login(Login requestMessage,HttpServletRequest request, HttpServletResponse response, boolean externalLogin)
			throws FDException, NoSessionException, JsonException {
		String username = requestMessage.getUsername();
		String password = requestMessage.getPassword();
		String source = requestMessage.getSource();
		String channel = requestMessage.getChannel();
		String destination = requestMessage.getDestination();
		String qualifier = requestMessage.getQualifier();
		String rafclickid = requestMessage.getRafclickid();
		String rafpromocode = requestMessage.getRafpromocode();
		Message responseMessage = null;
		SessionUser user = null;

		try {

			// Log in user and store in session
			/*createUserSession(User.login(username, password), source, request,
					response);*/
			//instead of above Call Make a call to UserUtil.loginUser

			// APPDEV-4627
		    if (request.getSession().getAttribute(SessionName.APPLICATION) == null){
		        request.getSession().setAttribute(SessionName.APPLICATION, getTransactionSourceCode(request, source));
		    }
	    	// end APPDEV-4627

			ActionResult actionResult = new ActionResult();
			UserUtil.loginUser(request.getSession(), request, response, actionResult, username, password, FAKE_MERGE_PAGE, FAKE_SUCCESS_PAGE, externalLogin);


			if(actionResult.isFailure()){
				ActionError actionError=actionResult.getError("authentication");
				if(null!=actionError && SystemMessageList.MSG_VOUCHER_REDEMPTION_FDX_NOT_ALLOWED.equalsIgnoreCase(
						actionError.getDescription())){
					throw new FDAuthenticationException("voucherredemption");

				} else {
					throw new FDAuthenticationException();
				}

			}
			LOGGER.debug("Current cart object : "+request.getSession().getAttribute(SessionName.CURRENT_CART));

			FDCartModel currentCart = (FDCartModel)request.getSession().getAttribute(SessionName.CURRENT_CART);

			//propogateSetSessionValues(request.getSession(), new ResultBundle().setActionResult(actionResult));
			user = getUserFromSession(request, response);
			user.setUserContext();
			user.setEligibleForDDPP();

			//Call the MergeCartControllerTagWrapper
			if(FDStoreProperties.isObsoleteMergeCartPageEnabled()){
				MergeCartControllerTagWrapper tagWrapper = new MergeCartControllerTagWrapper(user);
				ActionResult mergeActionResult = tagWrapper.mergeCart(currentCart);
				if(mergeActionResult.isFailure()){
					throw new FDAuthenticationException();
				}
			}
			user.getFDSessionUser().saveCart();
			//Silver popup changes start
			if(user.getFDSessionUser().getIdentity()!=null){
				SilverPopupDetails details = new SilverPopupDetails();
				details.setCustomerId(user.getFDSessionUser().getIdentity().getErpCustomerPK());
				details.setDestination(destination);
				details.setQualifier(qualifier);
				details.setChannel(channel);
				if (null != details.getDestination() && !details.getDestination().isEmpty() && null!=details.getQualifier()  && !details.getQualifier().isEmpty()) {
					user.getFDSessionUser().insertOrUpdateSilverPopup(details);
				}
			}
			//Silver popup changes End
            if (isExtraResponseRequested(request)) {
                CMSPageRequest pageRequest = new CMSPageRequest();
                pageRequest.setRequestedDate(new Date());
                pageRequest.setPlantId(BrowseUtil.getPlantId(user));
                PageMessageResponse pageResponse = new PageMessageResponse();
                populateHomePages(user, pageRequest, pageResponse, request);
                responseMessage = pageResponse;
            } else {
                responseMessage = formatLoginMessage(user);
            }
			resetMobileSessionData(request);
			if (user != null) {
				FDCustomerCouponUtil.initCustomerCoupons(request.getSession());
				//FDX-1873 - Show timeslots for anonymous address
				if(user.getAddress() != null && user.getAddress().getAddress1() != null && user.getAddress().getAddress1().length() > 0 && user.getAddress().isCustomerAnonymousAddress()) {
					user.getShoppingCart().setDeliveryAddress(null);
				}
			}
			user.setFromLogin("Login");
			EnumEStoreId eStore = (user.getUserContext() != null && user.getUserContext().getStoreContext() != null) ? user.getUserContext().getStoreContext().getEStoreId()
                    : EnumEStoreId.FD;
            if (EnumEStoreId.FDX.equals(eStore)) {
				if(user!=null && user.getFDSessionUser()!=null && user.getFDSessionUser().getShoppingCart()!=null && user.getFDSessionUser().getShoppingCart().getItemCount()>0){
					if(user.getFDSessionUser().getShoppingCart().getDeliveryReservation()==null){
						FDTimeslot fdt = new FDTimeslot();
						Date date = new Date();
						fdt.setDlvStartTime(new TimeOfDay(date));
						fdt.setDlvEndTime(new TimeOfDay(date));
						fdt.setDeliveryDate(date);
						fdt.setCutoffDateTime(date);
						fdt.setMinOrderAmt(0);
						PrimaryKey pk = new PrimaryKey();
						user.getFDSessionUser().getShoppingCart().setDeliveryReservation(new FDReservation(pk, fdt, null, null, null, null, false, null, 0, null, false, null, null));
					}
					CheckoutController cc = new CheckoutController();
					ActionResult availabliltyResult = cc.performAvailabilityCheck(user, request.getSession());
		            if (!availabliltyResult.isSuccess()) {
		            	Checkout chk = new Checkout(user);
		            	SubmitOrderExResult message = new SubmitOrderExResult();
		            	message = chk.fillAtpErrorDetail(message, request);
		            	responseMessage.setUnavaialabilityData(message.getUnavaialabilityData());
		            }
				}
			}
            if(rafclickid!=null && rafpromocode!=null && !user.getFDSessionUser().getOrderHistory().hasSettledOrders(eStore)){
            	FDCustomerManager.updateRAFClickIDPromoCode(user.getFDSessionUser().getIdentity(), rafclickid, rafpromocode, eStore);
            }
		} catch (FDAuthenticationException ex) {
			if ("Account disabled".equals(ex.getMessage())) {
				responseMessage = getErrorMessage(ERR_AUTHENTICATION,
						MessageFormat.format(SystemMessageList.MSG_DEACTIVATED,
								new Object[] { UserUtil
										.getCustomerServiceContact(request) }));
			} else if("voucherredemption".equals(ex.getMessage())){
	     		responseMessage = getErrorMessage(VOUCHER_AUTHENTICATION,
	            		MessageFormat.format(SystemMessageList.MSG_VOUCHER_REDEMPTION_FDX_NOT_ALLOWED,
	            		new Object[] { UserUtil.getCustomerServiceContact(request)}));
			} else {
				responseMessage = getErrorMessage(ERR_AUTHENTICATION,
						MessageCodes.MSG_AUTHENTICATION_FAILED);
			}
			request.getSession().setAttribute(SessionName.APPLICATION,null);
		}catch(IllegalStateException e){
			responseMessage = getErrorMessage("SESSION_INVALID_EXCEPTION","USER session is invalid");
			request.getSession().setAttribute(SessionName.APPLICATION,null);
		}
		return responseMessage;
	}

    private Message checkSession(HttpServletRequest request, HttpServletResponse response, SessionUser user, SessionMessage requestMessage)
            throws FDException, NoSessionException, JsonException {
        String source = requestMessage.getSource();
        Message responseMessage = null;
        EnumTransactionSource transactionSource = EnumTransactionSource.getTransactionSource(source);

        if (transactionSource == null) {
            responseMessage = getErrorMessage(ERR_INVALID_TRANSACTIONCODE, MessageCodes.MSG_INVALID_TRANSACTIONCODE);
        } else {
            request.getSession().setAttribute(SessionName.APPLICATION, transactionSource.getCode());
            responseMessage = new SessionResponse();
            if (user == null) {
                FDSessionUser fdSessionUser = null;
                try {
                    fdSessionUser = UserUtil.getSessionUserByCookie(request);
                } catch (FDResourceException ex) {
                    LOGGER.warn(ex);
                }
                if (fdSessionUser != null) {
                    FDCustomerCouponUtil.initCustomerCoupons(request.getSession());
                    ((SessionResponse) responseMessage).setSessionExpired(true); // had cookie with no session object so assume session expired
                } else {
                    fdSessionUser = LocatorUtil.useIpLocator(request.getSession(), request, response);
                }
                ((SessionResponse) responseMessage).setSessionIsNew(true); // Cookie recognized or new cookie created, it is still a new session
                request.getSession().setAttribute(SessionName.USER, fdSessionUser);
                user = SessionUser.wrap(fdSessionUser);
            } else {
                ((SessionResponse) responseMessage).setLoggedIn(user.isLoggedIn());
            }
            responseMessage.setConfiguration(getConfiguration(user));
        }
        return responseMessage;
    }

    @Override
    protected LoggedIn formatLoginMessage(SessionUser user) throws FDException {
        LoggedIn responseMessage = super.formatLoginMessage(user);
//        responseMessage.setTcAcknowledge(user.getTcAcknowledge());
        // FDX-1873 - Show timeslots for anonymous address
        boolean deliveryAddr = setDeliveryAddress(user);
        responseMessage.setAnonymousAddressSetFromAcc(deliveryAddr);
        responseMessage.setIsreferralEligible(user.getFDSessionUser().isReferralProgramAvailable());
        return responseMessage;
    }

	// FDX-1873 - Show timeslots for anonymous address
	//FDX-2036 API - at login, if anon address exists in Address Book of user, select the Address Book address
	public boolean setDeliveryAddress(SessionUser user) throws FDException{
		if(user.getAddress()!=null && user.getAddress().isCustomerAnonymousAddress() && user.getAddress().getAddress1()!=null && user.getAddress().getAddress1().trim().length()>0) {
		List<ErpAddressModel> addresses = FDCustomerFactory.getErpCustomer(user.getFDSessionUser().getIdentity()).getShipToAddresses();
		for(ErpAddressModel acctAddr: addresses) {
			boolean isAddressMatching = matchAddress(acctAddr, user.getAddress());
			if(isAddressMatching) {
				Checkout checkout = new Checkout(user);
				// ResultBundle resultBundle = checkout.setCheckoutDeliveryAddressEx(acctAddr.getId(), DeliveryAddressType.valueOf(user.getAddress().getServiceType()));
				ResultBundle resultBundle = checkout.setCheckoutDeliveryAddressEx(acctAddr.getId(), DeliveryAddressType.RESIDENTIAL);
				ActionResult result = resultBundle.getActionResult();
				if(result.isSuccess()){
					user.getAddress().setCustomerAnonymousAddress(false);
					return true;
				} else {
					return false;
				}
			}
		}
		}
		return false;
	}

	// FDX-1873 - Show timeslots for anonymous address
	public static boolean matchAddress(ErpAddressModel addr1, AddressModel addr2) {
		if (addr1 == null || addr2 == null)
			return false;
		if (addr1.getAddress1() != null && addr1.getAddress1().equalsIgnoreCase(addr2.getAddress1())
				&& ((addr1.getAddress2() == null && addr2.getAddress2() == null) || (addr1.getAddress2() != null && addr1.getAddress2().equalsIgnoreCase(addr2.getAddress2())) || (addr1.getAddress2().trim().length()==0 && addr2.getAddress2().trim().length()==0 ))
				&& addr1.getCity() != null && addr1.getCity().equalsIgnoreCase(addr2.getCity())) {
			return true;
		}
		return false;
	}
}
