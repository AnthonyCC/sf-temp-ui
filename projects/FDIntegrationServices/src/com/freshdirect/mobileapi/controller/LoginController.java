package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.PasswordNotExpiredException;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.customer.ejb.FDCustomerManagerSB;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.AckRequest;
import com.freshdirect.mobileapi.controller.data.request.Login;
import com.freshdirect.mobileapi.controller.data.request.SessionRequest;
import com.freshdirect.mobileapi.controller.data.request.ZipCheck;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.LoggedIn;
import com.freshdirect.mobileapi.controller.data.response.SessionResponse;
import com.freshdirect.mobileapi.controller.data.response.Timeslot;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.OrderHistory;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;
import com.freshdirect.mobileapi.model.User;
import com.freshdirect.mobileapi.model.tagwrapper.MergeCartControllerTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.LocatorUtil;

public class LoginController extends BaseController  implements SystemMessageList {

	public static final String ACTION_LOGIN = "login";
	
	public static final String ACTION_LOGIN_EX = "loginXE";

	public static final String ACTION_LOGOUT = "logout";

	public static final String ACTION_FORGOTPASSWORD = "forgotpassword";

	public static final String ACTION_PING = "ping";
	
	public static final String ACTION_SOURCE = "source";

	private final static String MSG_INVALID_EMAIL = "Invalid or missing email address. If you need assistance please call us at 1-866-283-7374.";
	private final static String MSG_EMAIL_NOT_EXPIRED = "An email was already sent. Please try again later.";
	
	private final static String ERR_INVALID_TRANSACTIONCODE = "Invalid Transaction Source";
	
	public static final String ACTION_SESSION = "checksession";
	
	public static final String ACTION_ACK = "acknowledge";
	
	public static final String ACTION_SESSION_ADD_ANONYMOUS_ADDRESS = "addanonymousaddress";
	
	private static final String FAKE_MERGE_PAGE="fake_merge_page";
	private static final String FAKE_SUCCESS_PAGE="fake_success_page";
	
    private final static String DIR_ERROR_KEY="ERR_DARKSTORE_RECONCILIATION";
	
	private static Category LOGGER = LoggerFactory.getInstance(LoginController.class);

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
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action,
			SessionUser user) throws FDException, ServiceException,
			NoSessionException, JsonException {
		
		if (ACTION_LOGIN.equals(action)) {
			Login requestMessage = parseRequestObject(request, response,
					Login.class);
			try {
				// Check to see if user session exists
				SessionUser sessionUser = getUserFromSession(request, response);
				if(sessionUser.isLoggedIn()){
					logout(model, user, request, response);
				}
				
			} catch (NoSessionException e) {
				// Do nothing
			}
			model = login(model, requestMessage, request, response, false);
		}if (ACTION_LOGIN_EX.equals(action)) {
			Login requestMessage = parseRequestObject(request, response,
					Login.class);
			try {
				// Check to see if user session exists
				SessionUser sessionUser = getUserFromSession(request, response);
				if(sessionUser.isLoggedIn()){
					logout(model, user, request, response);
				}
				
			} catch (NoSessionException e) {
				// Do nothing
			}
			model = login(model, requestMessage, request, response, true);
		} else if (ACTION_PING.equals(action)) {
			model = ping(model, request, response);
		} else if (ACTION_LOGOUT.equals(action)) {
			model = logout(model, user, request, response);
		}else if (ACTION_SOURCE.equals(action)) {
			 SessionRequest requestMessage = parseRequestObject(request, response, SessionRequest.class); 
			model = transactionSource(model, user, request, response,requestMessage);
			
		} else if (ACTION_FORGOTPASSWORD.equals(action)) {
			try {
				Login requestMessage = parseRequestObject(request, response,
						Login.class);
				LOGGER.debug("Email is going to: "
						+ requestMessage.getUsername());
				FDCustomerManager.sendPasswordEmail(
						requestMessage.getUsername(), false);
				Message responseMessage = Message
						.createSuccessMessage("Password sent successfully.");
				setResponseMessage(model, responseMessage, user);

			} catch (FDResourceException ex) {
				Message responseMessage = getErrorMessage(
						ERR_FORGOTPASSWORD_INVALID_EMAIL, MSG_INVALID_EMAIL);
				setResponseMessage(model, responseMessage, user);
			} catch (PasswordNotExpiredException pe) {
				Message responseMessage = getErrorMessage(
						ERR_FORGOTPASSWORD_EMAIL_NOT_EXPIRED,
						MSG_EMAIL_NOT_EXPIRED);
				setResponseMessage(model, responseMessage, user);
			}

		} else if (ACTION_SESSION.equals(action)) {
			SessionRequest requestMessage = parseRequestObject(request, response, SessionRequest.class);  
			model = checkSession(model, request, response, user, requestMessage);
		}else if (ACTION_ACK.equals(action)) {
			AckRequest requestMessage = parseRequestObject(request, response, AckRequest.class); 
				
			model = udateAck(model, request, response, user, requestMessage);
			user.setTcAcknowledge(true);
			
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
				FDDeliveryZoneInfo zoneInfo =  FDDeliveryManager.getInstance().getZoneInfo(address, date.getTime(), user.getFDSessionUser().getHistoricOrderSize(), null);
				if(user!=null && user.getShoppingCart()!=null && zoneInfo!=null) {
					user.getShoppingCart().setZoneInfo(zoneInfo);
				}
				
				if(!isAddressSet(user,address)) {
					Cart cart = user.getShoppingCart();
			        CartDetail cartDetail = cart.getCartDetail(user, EnumCouponContext.VIEWCART);
			        com.freshdirect.mobileapi.controller.data.response.Cart responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
			        responseMessage.addErrorMessage("Failed to add anonymous address. Cart has invalid items.");
			        responseMessage.setCartDetail(cartDetail);
			        if(!user.getFDSessionUser().isCouponsSystemAvailable()&& FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
			        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
			        }
			        setResponseMessage(model, responseMessage, user);
			        user.getFDSessionUser().resetUserContext();
				        
				} else {
					//FDX-1873 - Show timeslots for anonymous address
					user.getShoppingCart().setDeliveryAddress(null);
		        	List<FDCartLineI> invalidLines=OrderLineUtil.getInvalidLines(user.getShoppingCart().getOrderLines(), user.getFDSessionUser().getUserContext());
		        	
		        	if(invalidLines.size()>0) {
		        		
		        		Cart cart = user.getShoppingCart();
				        CartDetail cartDetail = cart.getCartDetail(user, EnumCouponContext.VIEWCART);
				        com.freshdirect.mobileapi.controller.data.response.Cart _responseMessage = new com.freshdirect.mobileapi.controller.data.response.Cart();
				        _responseMessage.addErrorMessage(DIR_ERROR_KEY,MessageCodes.ERR_DIR_ADDRESS_SET_EX);
				        _responseMessage.setCartDetail(cartDetail);
				        /*if(!user.getFDSessionUser().isCouponsSystemAvailable()) {
				        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
				        }*/
				        setResponseMessage(model, _responseMessage, user);
				        return model;
		        		
		        	} 					
				
					Message responseMessage = Message.createSuccessMessage("Anonymous Address added successfully.");
					setResponseMessage(model, responseMessage, user);
				}
				
				
			} catch (NoSessionException e) {
				 Message responseMessage = getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
	             setResponseMessage(model, responseMessage, user);
			} catch (FDInvalidAddressException e) {
				 Message responseMessage = getErrorMessage("Invalid Address", "Invalid address");
	             setResponseMessage(model, responseMessage, user);
			}
		}
		
		return model;
	}
	
	private ModelAndView udateAck(ModelAndView model,
			HttpServletRequest request, HttpServletResponse response,
			SessionUser user, AckRequest requestMessage)throws NoSessionException,
			FDException {
		Message responseMessage=null;
		if (null == user) {
            throw new NoSessionException("No session");
        }
		try {

       boolean success= FDCustomerManager.updateAck(user.getFDSessionUser().getIdentity(),requestMessage.isAcknowledge(), EnumEStoreId.valueOfContentId("FD".equalsIgnoreCase(requestMessage.getAppSource())?"FreshDirect":requestMessage.getAppSource()).getContentId());
		
       if(!success){
		 responseMessage = getErrorMessage(ERR_AUTHENTICATION,MessageCodes.MSG_ACCEPT_FD_TERMSANDCONDITIONS_FAILED);
       }else {
    	   responseMessage = Message.createSuccessMessage(MessageCodes.MSG_ACCEPT_FD_TERMSANDCONDITIONS);
    	   
       }
		setResponseMessage(model, responseMessage, user);
		} catch (JsonException e) {
			 LOGGER.warn(e);
			
		}
		return model;
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
	private ModelAndView logout(ModelAndView model, SessionUser user,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonException {

		removeUserInSession(user, request, response);

		Message responseMessage = Message
				.createSuccessMessage("User logged out successfully.");
		setResponseMessage(model, responseMessage, user);
		return model;
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
	private ModelAndView transactionSource(ModelAndView model, SessionUser user,
			HttpServletRequest request, HttpServletResponse response, SessionRequest requestMessage)
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
					fdSessionUser = CookieMonster.loadCookie(request);
				} catch (FDResourceException ex) {
					LOGGER.warn(ex);
				}
				if (fdSessionUser != null) {
					FDCustomerCouponUtil.initCustomerCoupons(request
							.getSession());
					((SessionResponse) responseMessage).setSessionExpired(true); 
				} else {
					fdSessionUser = LocatorUtil.useIpLocator(
							request.getSession(), request, response, null);
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
		setResponseMessage(model, responseMessage, user);
		return model; 
}



	private ModelAndView ping(ModelAndView model, HttpServletRequest request,
			HttpServletResponse response) throws NoSessionException,
			FDException, JsonException {
		SessionUser user = getUserFromSession(request, response);
		Message responseMessage = formatLoginMessage(user);
		setResponseMessage(model, responseMessage, user);
		return model;

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
	private ModelAndView login(ModelAndView model, Login requestMessage,
			HttpServletRequest request, HttpServletResponse response, boolean externalLogin)
			throws FDException, NoSessionException, JsonException {
		String username = requestMessage.getUsername();
		String password = requestMessage.getPassword();
		String source = requestMessage.getSource();
		Message responseMessage = null;
		SessionUser user = null;
		
		try {

			// Log in user and store in session
			/*createUserSession(User.login(username, password), source, request,
					response);*/
			//instead of above Call Make a call to UserUtil.loginUser
			
			// APPDEV-4627
	    	if(source != null && source.trim().length() > 0 && EnumTransactionSource.getTransactionSource(source) != null) {
	    		request.getSession().setAttribute(SessionName.APPLICATION, EnumTransactionSource.getTransactionSource(source).getCode());
	    	} else {
	    		request.getSession().setAttribute(SessionName.APPLICATION, EnumTransactionSource.IPHONE_WEBSITE.getCode());
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
			MergeCartControllerTagWrapper tagWrapper = new MergeCartControllerTagWrapper(user);
			ActionResult mergeActionResult = tagWrapper.mergeCart(currentCart);
			if(mergeActionResult.isFailure()){
				throw new FDAuthenticationException();
			}
			user.getFDSessionUser().saveCart();
			responseMessage = formatLoginMessage(user);
			resetMobileSessionData(request);
			if (user != null) {
				FDCustomerCouponUtil.initCustomerCoupons(request.getSession());
				//FDX-1873 - Show timeslots for anonymous address
				if(user.getAddress() != null && user.getAddress().getAddress1() != null && user.getAddress().getAddress1().length() > 0 && user.getAddress().isCustomerAnonymousAddress()) {
					user.getShoppingCart().setDeliveryAddress(null);
				} 
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
				List<String> providers = ExternalAccountManager.getConnectedProvidersByUserId(username);
				if(providers!=null && providers.size()!=0){
					responseMessage = Message.createFailureMessage(MessageFormat.format(MSG_SOCIAL_SOCIALONLY_ACCOUNT_SIGNIN, providers));	
				} else {
				responseMessage = getErrorMessage(ERR_AUTHENTICATION,
						MessageCodes.MSG_AUTHENTICATION_FAILED);
				}
			}
			request.getSession().setAttribute(SessionName.APPLICATION,null);
		}
		setResponseMessage(model, responseMessage, user);
		return model;
	}
	
	private ModelAndView checkSession(ModelAndView model, 
			HttpServletRequest request, HttpServletResponse response, SessionUser user, SessionRequest requestMessage)
			throws FDException, NoSessionException, JsonException {
				
		String source = requestMessage.getSource();
		Message responseMessage = new SessionResponse();
		EnumTransactionSource transactionSource = EnumTransactionSource.getTransactionSource(source);
		
		if(transactionSource == null) {
			responseMessage = getErrorMessage(ERR_INVALID_TRANSACTIONCODE, MessageCodes.MSG_INVALID_TRANSACTIONCODE);
		} else {
			request.getSession().setAttribute(SessionName.APPLICATION, transactionSource.getCode());
			if (user == null) {
				FDSessionUser fdSessionUser = null;
	            try {
	                fdSessionUser = CookieMonster.loadCookie(request);
	            } catch (FDResourceException ex) {
	                LOGGER.warn(ex);
	            }
	            if (fdSessionUser != null) {
	            	FDCustomerCouponUtil.initCustomerCoupons(request.getSession());
	            	((SessionResponse)responseMessage).setSessionExpired(true); // had cookie with no session object so assume session expired	            	
	            } else {
	            	fdSessionUser = LocatorUtil.useIpLocator(request.getSession(), request, response, null);
	            }
	            ((SessionResponse)responseMessage).setSessionIsNew(true); //Cookie recognized or new cookie created, it is still a new session
	            request.getSession().setAttribute(SessionName.USER, fdSessionUser);
	            user = SessionUser.wrap(fdSessionUser);
			} else {
				((SessionResponse)responseMessage).setLoggedIn(user.isLoggedIn());
			}
			responseMessage.setConfiguration(getConfiguration(user));
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

		if (!user.getFDSessionUser().isCouponsSystemAvailable() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
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
		
		((LoggedIn) responseMessage).setTcAcknowledge(user
				.getTcAcknowledge());
		
		// FDX-1873 - Show timeslots for anonymous address
				boolean deliveryAddr = setDeliveryAddress(user);
				((LoggedIn) responseMessage).setAnonymousAddressSetFromAcc(deliveryAddr);
		return responseMessage;
	}
	
	// FDX-1873 - Show timeslots for anonymous address
	//FDX-2036 API - at login, if anon address exists in Address Book of user, select the Address Book address
	public boolean setDeliveryAddress(SessionUser user) throws FDException{
				 List<ShipToAddress> deliveryAddresses = user.getDeliveryAddresses();
				 ShipToAddress anonymousAddr = ShipToAddress.wrap(user.getAddress());
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
