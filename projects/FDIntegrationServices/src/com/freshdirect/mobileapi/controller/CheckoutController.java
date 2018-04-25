package com.freshdirect.mobileapi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpHeaders;
import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDActionNotAllowedException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.EnumResponseAdditional;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.OrderMobileNumberRequest;
import com.freshdirect.mobileapi.controller.data.SubmitOrderExResult;
import com.freshdirect.mobileapi.controller.data.SubmitOrderRequest;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressSelection;
import com.freshdirect.mobileapi.controller.data.request.DeliverySlotReservation;
import com.freshdirect.mobileapi.controller.data.request.Login;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodRequest;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodSelection;
import com.freshdirect.mobileapi.controller.data.response.CVVResponse;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.DeliveryAddresses;
import com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslotPageResponse;
import com.freshdirect.mobileapi.controller.data.response.DynamicAvailabilityError;
import com.freshdirect.mobileapi.controller.data.response.HasCartDetailField;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethods;
import com.freshdirect.mobileapi.controller.data.response.PaymentResponse;
import com.freshdirect.mobileapi.controller.data.response.UnattendedDeliveryAddressResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.Cart;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.DeliveryAddress;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.DeliveryTimeslots;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.Depot;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.Order;
import com.freshdirect.mobileapi.model.PaymentMethod;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;
import com.freshdirect.mobileapi.model.User;
import com.freshdirect.mobileapi.model.tagwrapper.CheckoutControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.SessionParamName;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.BrowseUtil;
import com.freshdirect.mobileapi.util.DeliveryAddressValidatorUtil;
import com.freshdirect.mobileapi.util.ProductPotatoUtil;
import com.freshdirect.storeapi.content.CMSPageRequest;
import com.freshdirect.webapp.ajax.cart.CartOperations;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;
import com.freshdirect.webapp.cos.util.CosFeatureUtil;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UnattendedDeliveryTag;
import com.freshdirect.webapp.taglib.fdstore.UserValidationUtil;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventType;
import com.freshdirect.webapp.unbxdanalytics.event.LocationInfo;
import com.freshdirect.webapp.unbxdanalytics.service.EventLoggerService;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;
import com.freshdirect.webapp.util.RequestUtil;

public class CheckoutController extends BaseController {

    private static final Category LOGGER = LoggerFactory.getInstance(CheckoutController.class);

    private static final String PARAM_SLOT_ID = "slotId";
    private final static String DIR_ERROR_KEY = "ERR_DARKSTORE_RECONCILIATION";
    private final static String DEVICE_ID = "deviceId";
    private final static String ACTION_GET_REMOVE_UNAVAILABLE_ITEMS = "removeunavailableitems";
    private final static String ACTION_GET_ATP_ERROR_DETAIL = "getatperrordetail";
    private final static String ACTION_INIT_CHECKOUT = "initcheckout";
    private final static String ACTION_AUTH_CHECKOUT = "authenticate";
    private final static String ACTION_GET_CONSOLIDATED_CART = "getConsolidatedCart";
    private final static String ACTION_GET_DELIVERY_ADDRESSES = "getdeliveryaddresses";
    private final static String ACTION_ORDER_DETAIL = "orderdetail";
    private final static String ACTION_REVIEW_ORDER_DETAIL = "revieworderdetail";
    private final static String ACTION_SET_DELIVERY_ADDRESS = "setdeliveryaddress";
    private final static String ACTION_SET_DELIVERY_ADDRESS_EX = "setdeliveryaddressex";
    private final static String ACTION_RESERVE_TIMESLOT = "reservetimeslot";
    private final static String ACTION_RESERVE_TIMESLOT_EX = "reservetimeslotex";
    private final static String ACTION_GET_PAYMENT_METHODS = "getpaymentmethods";
    private final static String ACTION_SET_PAYMENT_METHOD = "setpaymentmethod";
    private final static String ACTION_SET_PAYMENT_METHOD_EX = "setPaymentMethodEx";
    private final static String ACTION_ADD_PAYMENT_METHOD = "addpaymentmethod";
    private final static String ACTION_ADD_PAYMENT_METHOD_EX = "addpaymentmethodex";
    private final static String ACTION_ADD_AND_SET_PAYMENT_METHOD = "addandsetpaymentmethod";
    private final static String ACTION_SAVE_PAYMENT_METHOD = "savepaymentmethod";
    private final static String ACTION_EDIT_PAYMENT_METHOD = "editpaymentmethod";
    private final static String ACTION_DELETE_PAYMENT_METHOD = "deletepaymentmethod";
    private final static String ACTION_DELETE_DELIVERY_ADDRESS = "deletedeliveryaddress";
    private final static String ACTION_DELETE_PAYMENT_METHOD_EX = "deletepaymentmethodex";
    private final static String ACTION_DELETE_DELIVERY_ADDRESS_EX = "deletedeliveryaddressex";
    private final static String ACTION_ADD_AND_SET_DELIVERY_ADDRESS = "addandsetdeliveryaddress";
    private final static String ACTION_SUBMIT_ORDER = "submitorder";
    private final static String ACTION_ALCOHOL_AGE_VERIFY = "alcoholageverify";
    private final static String ACTION_GET_SELECTED_DELIVERY_ADDRESS = "getselecteddeliveryaddress";
    private final static String ACTION_GET_PAYMENTMETHOD_VERIFY_STATUS = "getpmverifystatus";
    private static final String ACTION_ACCEPT_DP_TERMSANDCONDITIONS = "acceptDeliveryPassTermsAndConditions";
    private final static String ACTION_REMOVE_SPECIAL_RESTRICTED_ITEMS = "removesplrestricteditems";
    private final static String ACTION_GET_SPECIAL_RESTRICTED_DETAIL = "getsplrestricteditemdetail";
    private final static String ACTION_SUBMIT_ORDER_FDX ="submitOrderEx";
    private final static String ACTION_SET_ORDER_MOBILE_NUMBER_FDX ="setordermobilenumberfdx";
    private static final String ACTION_DELIVERY_ADDRESS_UNATTENDED = "isDeliveryAddressUnattended";
    
    private AvalaraContext avalaraContext;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException, NoSessionException {
    	
    	if(UserExists(user)){

	        if (ACTION_INIT_CHECKOUT.equals(action) || ACTION_AUTH_CHECKOUT.equals(action)) {
	            //Validate pre-req. If pass, go directly to get payment method
	            Login requestMessage = null;
	
	            if (ACTION_AUTH_CHECKOUT.equals(action)) {
	                requestMessage = parseRequestObject(request, response, Login.class);
	            }
	            if (validateCheckoutRequirements(model, requestMessage, user, request)) {
	                model = getDeliveryAddresses(model, user);
	                //model = applycredit(model, requestMessage, user, request);
	            }
	        } else if(ACTION_GET_CONSOLIDATED_CART.equals(action)) {       	
	                model = applycredit(model, user, request);           
	        }
	        else if (ACTION_GET_DELIVERY_ADDRESSES.equals(action)) {
	            model = getDeliveryAddresses(model, user);
	        } else if (ACTION_SET_DELIVERY_ADDRESS.equals(action)) {
	            DeliveryAddressSelection reqestMessage = parseRequestObject(request, response, DeliveryAddressSelection.class);
	            model = setDeliveryAddress(model, user, reqestMessage, request);
	        } else if (ACTION_SET_DELIVERY_ADDRESS_EX.equals(action)) {
	            DeliveryAddressSelection reqestMessage = parseRequestObject(request, response, DeliveryAddressSelection.class);
	            model = setDeliveryAddressEx(model, user, reqestMessage, request);
	        } else if (ACTION_ADD_AND_SET_DELIVERY_ADDRESS.equals(action)) {
	        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
	            model = addAndSetDeliveryAddress(model, user, requestMessage, request);
	        }else if (ACTION_RESERVE_TIMESLOT.equals(action)) {
	            String slotId = request.getParameter(PARAM_SLOT_ID);
	            if (slotId == null) {
	                DeliverySlotReservation requestMessage = parseRequestObject(request, response, DeliverySlotReservation.class);
	                model = reserveTimeslot(model, user, requestMessage.getDeliveryTimeslotId(), request);
	            } else {
	                model = reserveTimeslot(model, user, slotId, request);
	            }
	        } else if (ACTION_RESERVE_TIMESLOT_EX.equals(action)) {
	            String slotId = request.getParameter(PARAM_SLOT_ID);
	            if (slotId == null) {
	                DeliverySlotReservation requestMessage = parseRequestObject(request, response, DeliverySlotReservation.class);
	                model = reserveTimeslotEx(model, user, requestMessage.getDeliveryTimeslotId(), request);
	            } else {
	                model = reserveTimeslotEx(model, user, slotId, request);
	            }
	        } else if (ACTION_GET_PAYMENT_METHODS.equals(action)) {
	            Message responseMessage = getPaymentMethods(user);
	            setResponseMessage(model, responseMessage, user);
	        } else if (ACTION_SET_PAYMENT_METHOD.equals(action)) {
	            PaymentMethodSelection requestMessage = parseRequestObject(request, response, PaymentMethodSelection.class);
	            model = setPaymentMethod(model, user, requestMessage, request);
	        } else if (ACTION_SET_PAYMENT_METHOD_EX.equals(action)) {
	            PaymentMethodSelection requestMessage = parseRequestObject(request, response, PaymentMethodSelection.class);
	            model = setPaymentMethodEx(model, user, requestMessage, request);
	        } else if (ACTION_ORDER_DETAIL.equals(action)) {
	            //Check Order Info Currently being ordered.
	            model = reviewOrder(model, user, request, EnumCouponContext.VIEWCART);
	        } else if (ACTION_REVIEW_ORDER_DETAIL.equals(action)) {
	            //Review order. What's being order, where it's going, how it's being paid for, etc.
	            model = reviewOrder(model, user, request, EnumCouponContext.CHECKOUT);
	        } else if (ACTION_SUBMIT_ORDER.equals(action)) {
	        	if(getPostData(request, response)!=null && getPostData(request, response).contains(DEVICE_ID)) {
	        	SubmitOrderRequest requestMessage = parseRequestObject(request, response, SubmitOrderRequest.class);
	            model = submitOrder(model, user, request,requestMessage);
	        	} else {
	        		model = submitOrder(model, user, request);
	        	}
	        } else if (ACTION_GET_ATP_ERROR_DETAIL.equals(action)) {
	            model = getAtpErrorDetail(model, user, request);
	        } else if (ACTION_GET_REMOVE_UNAVAILABLE_ITEMS.equals(action)) {
	            model = removeUnavailableItemsFromCart(model, user, request);
	        } else if (ACTION_ALCOHOL_AGE_VERIFY.equals(action)) {
	            model = verifyAlcoholAge(model, user, request);
	        }else if (ACTION_ADD_PAYMENT_METHOD.equals(action)) {
	        	PaymentMethodRequest requestMessage = parseRequestObject(request, response, PaymentMethodRequest.class);
	            model = addPaymentMethod(model, user, requestMessage, request, response);
	        }else if (ACTION_ADD_PAYMENT_METHOD_EX.equals(action)) {
	        	PaymentMethodRequest requestMessage = parseRequestObject(request, response, PaymentMethodRequest.class);
	            model = addPaymentMethodEx(model, user, requestMessage, request, response);
	        }else if (ACTION_ADD_AND_SET_PAYMENT_METHOD.equals(action)) {
	        	PaymentMethodRequest requestMessage = parseRequestObject(request, response, PaymentMethodRequest.class);
	            model = addAndSetPaymentMethod(model, user, requestMessage, request, response);
	        }else if (ACTION_SAVE_PAYMENT_METHOD.equals(action)) {
	            	PaymentMethodRequest requestMessage = parseRequestObject(request, response, PaymentMethodRequest.class);
	                model = savePaymentMethod(model, user, requestMessage, request, response);
	       }else if (ACTION_EDIT_PAYMENT_METHOD.equals(action)) {
	        	PaymentMethodRequest requestMessage = parseRequestObject(request, response, PaymentMethodRequest.class);
	            model = editPaymentMethod(model, user, requestMessage, request, response);
	        }else if (ACTION_DELETE_PAYMENT_METHOD.equals(action)) {
	        	PaymentMethodRequest requestMessage = parseRequestObject(request, response, PaymentMethodRequest.class);
	            model = deletePaymentMethod(model, user, requestMessage, request);
	        }else if (ACTION_DELETE_PAYMENT_METHOD_EX.equals(action)) {
	        	PaymentMethodRequest requestMessage = parseRequestObject(request, response, PaymentMethodRequest.class);
	            model = deletePaymentMethodEx(model, user, requestMessage, request);
	        }else if (ACTION_DELETE_DELIVERY_ADDRESS.equals(action)) {
	        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
	            model = deleteDeliveryAddress(model, user, requestMessage, request);
	        }else if (ACTION_DELETE_DELIVERY_ADDRESS_EX.equals(action)) {
	        	DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
	            model = deleteDeliveryAddressEx(model, user, requestMessage, request);
	        }else if (ACTION_GET_SELECTED_DELIVERY_ADDRESS.equals(action)) {
	            model = getSelectedDeliveryAddress(model, user);
	        }else if (ACTION_GET_PAYMENTMETHOD_VERIFY_STATUS.equals(action)) {
	            model = getCVVStatus(model, user);
	        }  else if (ACTION_ACCEPT_DP_TERMSANDCONDITIONS.equals(action)) {            
	            model = this.acceptDeliveryPassTerms(model, user, request);
	        }  else if (ACTION_REMOVE_SPECIAL_RESTRICTED_ITEMS.equals(action)) {
	            model = removeSpecialRestrictedItemsFromCart(model, user, request);
	        }  else if (ACTION_GET_SPECIAL_RESTRICTED_DETAIL.equals(action)) {
	            model = getSpecialRestrictedItemDetail(model, user, request);
	        } else if(ACTION_SUBMIT_ORDER_FDX.equals(action)){
	        	model = submitOrderFDX(model,user,request);
	        } else if(ACTION_SET_ORDER_MOBILE_NUMBER_FDX.equals(action)){
	        	OrderMobileNumberRequest requestMessage = parseRequestObject(request, response, OrderMobileNumberRequest.class);
	            model = setOrderMobileNumberEx(model, user, request, requestMessage.getMobile_number());
            } else if (ACTION_DELIVERY_ADDRESS_UNATTENDED.equals(action)) {
                DeliveryAddressRequest requestMessage = parseRequestObject(request, response, DeliveryAddressRequest.class);
                Message responseMessage = isDeliveryAddressUnattended(user, requestMessage, request);
                setResponseMessage(model, responseMessage, user);
	        }
    	}else{
    		Message responseMessage = new Message();
            responseMessage.setStatus(Message.STATUS_FAILED);
            responseMessage =  getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
            setResponseMessage(model, responseMessage, user);
    	}
    	
        return model;
    }
    
    public boolean UserExists(SessionUser user){
    	return user!=null ? true:false;
    }

    private ModelAndView applycredit(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDResourceException, JsonException {
   	 	
    	Message responseMessage = new Message();
    	Cart cart = user.getShoppingCart();
		if (cart != null) {
			try{
			cart.applycredit(user);
			} catch(Exception e) {
				responseMessage .setSuccessMessage("Exception when applying store credit");
				return model;
			}
		} else {
			responseMessage.addErrorMessage("CART IS EMPTY");
		}
		responseMessage.setSuccessMessage("Store Credit amount has been applied successfully.");
		setResponseMessage(model, responseMessage, user);
        return model;
	}

	public boolean isCheckoutAuthenticated(HttpServletRequest request) {
        return getMobileSessionData(request).isCheckoutAuthenticated();
    }

    public void setCheckoutAuthenticated(HttpServletRequest request) {
        getMobileSessionData(request).setCheckoutAuthenticated(true);
    }

    /**
     * @param model
     * @param user
     * @return
     * @throws JsonException
     * @throws FDResourceException 
     */
    private boolean validateCheckoutRequirements(ModelAndView model, Login loginRequest, SessionUser user, HttpServletRequest request)
            throws JsonException, FDResourceException {
        boolean valid = true;
        Message responseMessage = new Message();
      //With Mobile App having given ability to add/remove payment method this is removed
        /*
        try {
	        if ((user.getDeliveryAddresses() == null) || (user.getDeliveryAddresses().size() == 0)) {
	            responseMessage = getWarningMessage(ERR_NO_DELIVERY_ADDRESS, ERR_NO_DELIVERY_ADDRESS_MSG);
	            valid = false;
	        }
        }catch(FDException fe){
        	throw new FDResourceException(fe);
        }
        */
        //With Mobile App having given ability to add/remove payment method this is removed
        /*if ((user.getPaymentMethods() == null) || (user.getPaymentMethods().size() == 0)) {
            responseMessage = getWarningMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);
            valid = false;
        }*/

        if (valid) {
            if (!isCheckoutAuthenticated(request)) {
                if (loginRequest == null) {
                    valid = false;
                    responseMessage = getErrorMessage(ERR_CHECKOUT_AUTHENTICATION_REQUIRED, "Authentication required for checkout");
                } else {
                    String username = loginRequest.getUsername();
                    String password = loginRequest.getPassword();
                    if (!User.authenticate(username, password)) {
                        valid = false;
                        responseMessage = getErrorMessage(ERR_AUTHENTICATION, "Invalid username and/or password");
                    } else {
                        setCheckoutAuthenticated(request);
                    }
                }
            }
        }
        
        setResponseMessage(model, responseMessage, user);
        return valid;
    }

    private ModelAndView verifyAlcoholAge(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException,
            JsonException {
        Message responseMessage = null;
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.verifyAlcoholAge();
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("User certification of age Verification captured successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);

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
    private ModelAndView removeUnavailableItemsFromCart(ModelAndView model, SessionUser user, HttpServletRequest request)
            throws FDException, JsonException {
        Message responseMessage = null;
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.removeUnavailableItemsFromCart(qetRequestData(request));
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Item(s) removed successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());

        setResponseMessage(model, responseMessage, user);

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
    private ModelAndView reviewOrder(ModelAndView model, SessionUser user, HttpServletRequest request, EnumCouponContext ctx) throws FDException, JsonException, NoSessionException {
        final boolean isWebRequest = isExtraResponseRequested(request);

        Checkout checkout = new Checkout(user);
        Message responseMessage = checkout.getCurrentOrderDetails(ctx);

        if (isWebRequest) {
            if (!user.isLoggedIn()){
                throw new NoSessionException("No session");
            }

            ActionResult result = new ActionResult();
            UserValidationUtil.validateOrderMinimum(request.getSession(), result);
            responseMessage.addErrorMessages(result.getErrors(), user);
        }
        
        Cart cart = user.getShoppingCart();
        if(EnumCouponContext.CHECKOUT.equals(ctx)){
        	user.setRefreshCouponWalletRequired(true);
        	user.setCouponEvaluationRequired(true);
        }

        final FDSessionUser fdSessionUser = user.getFDSessionUser();

        if((EnumCouponContext.VIEWCART.equals(ctx) || EnumCouponContext.CHECKOUT.equals(ctx)) && FDStoreProperties.getAvalaraTaxEnabled()){
        	AvalaraContext avalaraContext =  new AvalaraContext(fdSessionUser.getShoppingCart());
        	cart.getAvalaraTax(avalaraContext);
        }

        // FKMobileWeb: fix cartDetails field, add product potatos
        if (isWebRequest && responseMessage instanceof HasCartDetailField) {
            ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), ((HasCartDetailField) responseMessage).getCartDetail());
        }

        if(!fdSessionUser.isCouponsSystemAvailable() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE
        										, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
        }

        if(fdSessionUser.getShoppingCart().getExpCouponDeliveryDate()!=null){
        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONS_EXP_DELIVERY_DATE
					, MessageCodes.MSG_COUPONS_EXP_DELIVERY_DATE);
        }
        if (cart != null) {
			try{
			cart.applycredit(user);
			} catch(Exception e) {
				responseMessage .setSuccessMessage("Exception when applying store credit");
				return model;
			}
		} else {
			responseMessage.addErrorMessage("CART IS EMPTY");
		}
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    /**
     * @param model
     * @param user
     * @return
     * @throws FDResourceException
     * @throws JsonException
     */
    private ModelAndView getDeliveryAddresses(ModelAndView model, SessionUser user) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        List<ShipToAddress> deliveryAddresses = new ArrayList<ShipToAddress>();
        DeliveryAddresses responseMessage = new DeliveryAddresses();
        if(user!=null&&user.getFDSessionUser()!=null&&user.getFDSessionUser().getIdentity()!=null){
        	deliveryAddresses = user.getDeliveryAddresses();
	        responseMessage = new DeliveryAddresses(checkout.getPreselectedDeliveryAddressId(), checkout.getSelectedDeliveryAddressId(), ShipToAddress
	                .filter(deliveryAddresses, DeliveryAddressType.RESIDENTIAL), ShipToAddress.filter(deliveryAddresses,
	                DeliveryAddressType.CORP), Depot.getPickupDepots());
	        
			if (user.isVoucherHolder()) {
				List<com.freshdirect.mobileapi.controller.data.response.ShipToAddress> latestAddress = new ArrayList<com.freshdirect.mobileapi.controller.data.response.ShipToAddress>();
	
				for (com.freshdirect.mobileapi.controller.data.response.ShipToAddress shipToAddress : responseMessage
						.getResidentialAddresses()) {
					if (shipToAddress.getId().equals(
							responseMessage.getPreSelectedId())) {
						latestAddress.add(shipToAddress);
						break;
					}
				}
				responseMessage.setResidentialAddresses(latestAddress);
				responseMessage.getDepot().clear();
				
			}
        }
        
        responseMessage.setResidentialDeliveryMinimum(user.getMinimumOrderAmount());
        responseMessage.setDepotDeliveryMinimum(user.getMinimumOrderAmount());
        responseMessage.setCorporateDeliveryMinimum(user.getMinCorpOrderAmount());

        responseMessage.getCheckoutHeader().setHeader(user.getShoppingCart());
        responseMessage.setSuccessMessage("Delivery Addresses have been retrieved successfully.");
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView acceptDeliveryPassTerms(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
    	
    	try {
			FDCustomerManager.storeDPTCAgreeDate(AccountActivityUtil.getActionInfo(request.getSession()), user.getFDSessionUser().getIdentity().getErpCustomerPK(), new Date());
		} catch(FDResourceException exp) {
			//exp.printStackTrace();
			setResponseMessage(model, Message.createFailureMessage(MSG_ACCEPT_DP_TERMSANDCONDITIONS_FAILED), user);
			return model;
		}
        
        Message responseMessage = null;
        if (user.getShoppingCart() != null && user.getShoppingCart().getDeliveryAddress() != null ) {
        	
        	DeliveryAddress deliveryAddress = DeliveryAddress.wrap(user.getShoppingCart().getDeliveryAddress());
            TimeSlotCalculationResult timeSlotResult = deliveryAddress.getDeliveryTimeslot(user, false,isCheckoutAuthenticated(request));

            com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots slotResponse = new com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots(
                    timeSlotResult);
            slotResponse.getCheckoutHeader().setHeader(user.getShoppingCart());
            responseMessage = slotResponse;
            responseMessage.setSuccessMessage("Order delivery Address have been set successfully.");
            
        } else {
        	
        	setResponseMessage(model, Message.createFailureMessage(MSG_ACCEPT_DP_TERMSANDCONDITIONS_NOADDRESS), user);
        }
        setResponseMessage(model, responseMessage, user);
        
        return model;
    }
    
    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView setDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressSelection reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	    	
    	Checkout checkout = new Checkout(user);
    	ResultBundle resultBundle = null;
    	Message responseMessage = null;
    	
    	if(reqestMessage.getType() != null) {
	    	 if (isExtraResponseRequested(request)) {
	    	     resultBundle = checkout.setCheckoutDeliveryAddressEx(reqestMessage.getId(), DeliveryAddressType.valueOf(reqestMessage.getType()));
	    	 } else {
	    	     resultBundle = checkout.setCheckoutDeliveryAddress(reqestMessage.getId(), DeliveryAddressType.valueOf(reqestMessage.getType()));
	    	 }
	        ActionResult result = resultBundle.getActionResult();
	        propogateSetSessionValues(request.getSession(), resultBundle);
	        
	        
	        if (result.isSuccess()) {
	            DeliveryAddress deliveryAddress = DeliveryAddress.wrap(user.getShoppingCart().getDeliveryAddress());
	            TimeSlotCalculationResult timeSlotResult = deliveryAddress.getDeliveryTimeslot(user, false, isCheckoutAuthenticated(request));
	            
	            com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots slotResponse = new com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots(
	                    timeSlotResult);
	            slotResponse.getCheckoutHeader().setHeader(user.getShoppingCart());
	
	            if (isExtraResponseRequested(request)) {
	                user.setUserContext();
	                List<FDCartLineI> removedInvalidLines = removeInvalidLines(user.getFDSessionUser(), request.getServerName());
	                List<ProductPotatoData> removedProducts = new ArrayList<ProductPotatoData>(removedInvalidLines.size());
	                for (FDCartLineI cartLine : removedInvalidLines) {
	                    ProductPotatoData productPotato = ProductPotatoUtil.getProductPotato( cartLine.lookupProduct(), user.getFDSessionUser(), false, true);
	                    productPotato.getProductData().setInCartAmount(cartLine.getQuantity());
	                    removedProducts.add(productPotato);
	                }
	                CMSPageRequest pageRequest = new CMSPageRequest();
	                pageRequest.setPlantId(BrowseUtil.getPlantId(user));
	                DeliveryTimeslotPageResponse pageResponse = new DeliveryTimeslotPageResponse();
	                pageResponse.setRemovedProducts(removedProducts);
	                populateHomePages(user, pageRequest, pageResponse, request);
	                pageResponse.setDeliveryTimeslot(slotResponse);
	                responseMessage = pageResponse;
	            } else {
	                ShipToAddress shipToAddressModel = ShipToAddress.wrap(user.getShoppingCart().getDeliveryAddress());
	                com.freshdirect.mobileapi.controller.data.response.ShipToAddress shipToAddressResponse = new com.freshdirect.mobileapi.controller.data.response.ShipToAddress(
	                        shipToAddressModel);
	                slotResponse.setAddress(shipToAddressResponse);
	                responseMessage = slotResponse;
	            }
	            responseMessage.setSuccessMessage("Order delivery Address have been set successfully.");
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
    	} else {
    		responseMessage = new Message();
   	    	responseMessage.setFailureMessage("Address Type is required");
    	}
        setResponseMessage(model, responseMessage, user);
        
        return model;
    }
    
    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView setDeliveryAddressEx(ModelAndView model, SessionUser user, DeliveryAddressSelection reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
    	    	
    	Checkout checkout = new Checkout(user);
    	Message responseMessage = null;
    	
    	if(reqestMessage.getType() != null) {
	        ResultBundle resultBundle = checkout.setCheckoutDeliveryAddressEx(reqestMessage.getId(), DeliveryAddressType.valueOf(reqestMessage.getType()));
	        ActionResult result = resultBundle.getActionResult();
	        
	        propogateSetSessionValues(request.getSession(), resultBundle);
	        
	        responseMessage = new DynamicAvailabilityError();
	        if(result.isSuccess()&& user.getShoppingCart()!=null && user.getFDSessionUser().getShoppingCart().getItemCount()>0) {
	        	
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
	        		
	        	} else {
	        		responseMessage.setSuccessMessage("Address Set Successfully.");
	        	}
	        }
	    
	        
	        if (result.isSuccess()) {    
	        	// FDX-1873 - Show timeslots for anonymous address
	        	if(user != null && user.getAddress() != null) {
	        		user.getAddress().setCustomerAnonymousAddress(false);
	        	}
	        	
	            responseMessage.setSuccessMessage("Address Set Successfully.");            
	        }else {  
	        	
	        	responseMessage = getErrorMessage(result, request);
	        	
	        }
    	} else {
    		responseMessage = new Message();
   	    	responseMessage.setFailureMessage("Address Type is required");
    	}
        setResponseMessage(model, responseMessage, user);
        
        return model;
    }
    
    private ModelAndView setOrderMobileNumberEx(ModelAndView model, SessionUser user, HttpServletRequest request, String orderlMobileNumber) throws FDException, JsonException {
    	Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.setCheckoutOrderMobileNumberEx(orderlMobileNumber);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        Message responseMessage = new DynamicAvailabilityError();
        if (result.isSuccess()) {       	
            responseMessage.setSuccessMessage("Mobile Number Added at on Order level Successfully.");            
        }else {  
        		responseMessage = getErrorMessage(result, request);
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    

    /**
     * @param model
     * @param user
     * @param request
     * @param requestMessage
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView submitOrder(ModelAndView model, SessionUser user, HttpServletRequest request, SubmitOrderRequest requestMessage)  throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        Message responseMessage = null;

        Map<String,String> errors = new HashMap<String, String>();
        boolean checkResult = checkForDeviceId(user,requestMessage,errors); // Check Device ID ; Required only when PayPal wallet is used.
        if(checkResult){ 
	        callAvalaraForTax(user);
	        FDCartModel cartModel = user.getFDSessionUser().getShoppingCart();
	        ResultBundle resultBundle = checkout.submitOrder();
	        ActionResult result = resultBundle.getActionResult();
	        propogateSetSessionValues(request.getSession(), resultBundle);

	        if (result.isSuccess()) {
	
	        	com.freshdirect.mobileapi.controller.data.response.Order orderReceipt = new com.freshdirect.mobileapi.controller.data.response.Order();
	            String orderId=(String) request.getSession().getAttribute(SessionName.RECENT_ORDER_NUMBER);
	           /* try {
	                orderReceipt = checkout.getOrderReceipt((String) request.getSession().getAttribute(SessionName.RECENT_ORDER_NUMBER));
	            } catch (IllegalAccessException e) {
	                throw new FDException(e);
	            } catch (InvocationTargetException e) {
	                throw new FDException(e);
	            }*/
	            
	            Order order = user.getOrder(orderId);
	            if(null !=order){
		            orderReceipt = order.getOrderDetail(user);
		            createAndSendUnbxdAnalyticsEvent(user.getFDSessionUser(), request, cartModel.getOrderLines());
	            }
	            
	            orderReceipt.setOrderNumber(orderId);

	            if (isExtraResponseRequested(request)) {
	                ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), orderReceipt.getCartDetail());
	            }
	            
	            responseMessage = orderReceipt;
	            responseMessage.addDebugMessage("Order has been submitted successfully.");
	            
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        responseMessage.addWarningMessages(result.getWarnings());
        }else{
        	responseMessage = new Message();
        	responseMessage.setErrors(errors);
        }
        
        setResponseMessage(model, responseMessage, user);

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
    private ModelAndView submitOrder(ModelAndView model, SessionUser user, HttpServletRequest request)  throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        Message responseMessage = null;

	        callAvalaraForTax(user);
	        FDCartModel cartModel = user.getFDSessionUser().getShoppingCart();
	        ResultBundle resultBundle = checkout.submitOrder();
	        ActionResult result = resultBundle.getActionResult();
	        propogateSetSessionValues(request.getSession(), resultBundle);

	        if (result.isSuccess()) {
	
	        	com.freshdirect.mobileapi.controller.data.response.Order orderReceipt = new com.freshdirect.mobileapi.controller.data.response.Order();
	            String orderId=(String) request.getSession().getAttribute(SessionName.RECENT_ORDER_NUMBER);
	           /* try {
	                orderReceipt = checkout.getOrderReceipt((String) request.getSession().getAttribute(SessionName.RECENT_ORDER_NUMBER));
	            } catch (IllegalAccessException e) {
	                throw new FDException(e);
	            } catch (InvocationTargetException e) {
	                throw new FDException(e);
	            }*/
	            
	            Order order = user.getOrder(orderId);
	            if(null !=order){
		            orderReceipt = order.getOrderDetail(user);
		            createAndSendUnbxdAnalyticsEvent(user.getFDSessionUser(), request, cartModel.getOrderLines());
	            }
	            
	            orderReceipt.setOrderNumber(orderId);
	            
                if (isExtraResponseRequested(request)) {
                    ProductPotatoUtil.populateCartDetailWithPotatoes(user.getFDSessionUser(), orderReceipt.getCartDetail());
                }
	            
	            responseMessage = orderReceipt;
	            responseMessage.addDebugMessage("Order has been submitted successfully.");
	            
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
	        responseMessage.addWarningMessages(result.getWarnings());            
        setResponseMessage(model, responseMessage, user);

        return model;
    }

    /**
     * @param user
     * @param requestMessage
     * @return
     */
    private boolean checkForDeviceId(SessionUser user,SubmitOrderRequest requestMessage,Map<String,String> errors){
    	boolean result = true;
    	FDSessionUser fdSessionUser =user.getFDSessionUser();
    	if( fdSessionUser.getShoppingCart() !=null && fdSessionUser.getShoppingCart().getPaymentMethod() != null){
    		ErpPaymentMethodI paymentMethod = fdSessionUser.getShoppingCart().getPaymentMethod();
    		if( paymentMethod.geteWalletID() != null && paymentMethod.geteWalletID().equals(""+EnumEwalletType.PP.getValue())){
    			if(requestMessage.getDeviceId() == null || requestMessage.getDeviceId().trim().length() == 0){
    				result = false;
    				errors.put("Error input Device Id is missing","Device ID is required when PayPal wallet is used to place the order.");
    			}
    		}
    	}
    	return result;
    }
    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView reserveTimeslot(ModelAndView model, SessionUser user, String slotId, HttpServletRequest request)
            throws FDException, JsonException {
    	user.setFromLogin("Checkout");
        DeliveryTimeslots slot = new com.freshdirect.mobileapi.model.DeliveryTimeslots(user);
        ResultBundle resultBundle = slot.reserveSlot(slotId);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if(null !=result ){
	        if (result.isSuccess()) {
	            ActionResult availabliltyResult = performAvailabilityCheck(user, request.getSession());
	            if (availabliltyResult.isSuccess()) {
	                responseMessage = Message.createSuccessMessage("Delivery slot reserved successfully.");
	            } else {
	                responseMessage = getErrorMessage(availabliltyResult, request);
	            }
	            responseMessage.addWarningMessages(availabliltyResult.getWarnings());
	        } else {
	            responseMessage = getErrorMessage(result, request);
	        }
        	responseMessage.addWarningMessages(result.getWarnings());
        }
        if(responseMessage==null){
			responseMessage = getErrorMessage("RESP_MSG_NULL", "Response Message Null");
			LOGGER.error("CHECKOUTCONTROLLER - Response Message Null for action reserveTimeslot and user " + (user != null && user.getFDSessionUser() != null 
					? (user.getFDSessionUser().getIdentity() != null && user.getFDSessionUser().getFDCustomer() != null 
					? user.getFDSessionUser().getFDCustomer().getErpCustomerPK() : user.getFDSessionUser().getPrimaryKey() ) : "NOUSER" ) );
		}
        setResponseMessage(model, responseMessage, user);

        return model;
    }
    
    private ModelAndView reserveTimeslotEx(ModelAndView model, SessionUser user, String slotId, HttpServletRequest request)
            throws FDException, JsonException {
    	DeliveryTimeslots slot = new com.freshdirect.mobileapi.model.DeliveryTimeslots(user);
        ResultBundle resultBundle = slot.reserveSlotEx(slotId);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()){
        	
        	// For updating the cart with the updated delivery fee value according to the reserved time slot
        	if(user!=null && user.getFDSessionUser()!=null) {
        	user.getFDSessionUser().updateUserState();
        	}
        	responseMessage = Message.createSuccessMessage("Delivery slot reserved successfully.");
        } else {
        	 responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
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
    private ModelAndView getAtpErrorDetail(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException,
            JsonException {
        if (user == null) {
            user = fakeUser(request.getSession());
        }
        Checkout checkout = new Checkout(user);
        Message responseMessage = checkout.getAtpErrorDetail();
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    public ActionResult performAvailabilityCheck(SessionUser user, HttpSession session) throws FDException {
    	return performAvailabilityCheck(user,session,false);
    }
    
    /**
     * @param user
     * @param session
     * @return
     * @throws FDException
     */
    public ActionResult performAvailabilityCheck(SessionUser user, HttpSession session, boolean isDlvPassOnlyCart) throws FDException {

        /*
         * DUP: FDWebSite/docroot/checkout/step_2_check.jsp
         * LAST UPDATED ON: 10/07/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code determines calls a method on model to perform availability check
         */
        boolean isProductFullyAvailable = !isDlvPassOnlyCart ? user.getShoppingCart().isCartFullyAvailable(user): true;

        Checkout checkout = new Checkout(user);

        ResultBundle resultBundle = checkout.checkDeliveryPassAvailability();
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(session, resultBundle);

        //TODO: Not only find out availability but also get error on which specific products weren't available
        //And what to do...choose another date? or remove item?
        Double subTotal = user.getShoppingCart().getSubTotal();
        
        if (!isProductFullyAvailable) {
        	Cart cart = user.getShoppingCart();
        	FDReservation reservation = cart.getDeliveryReservation();
        	Cart clonedCart = Cart.cloneCart(cart);
        	clonedCart.setUnavailablePasses(cart.getUnavailablePasses());
        	clonedCart.setAvailability(cart.getAvailability());
        	subTotal = clonedCart.getSubTotalATPCheck();
        	TimeslotLogic.applyOrderMinimum(user.getFDSessionUser(), reservation.getTimeslot(), subTotal);		

        	if(subTotal!=null && subTotal < reservation.getMinOrderAmt()){
        		result.addError(new ActionError(ERR_ATP_MIN_ORDER_FAILED, "Your order total has fallen below the "+ TimeslotLogic.formatMinAmount(reservation.getMinOrderAmt()) + " required for the selected delivery time. Unavailable items will remain in your cart."));
        	}else{
        		result.addError(new ActionError(ERR_ATP_FAILED, "One of the products were not available."));
        	}
        }
        else{
        	if(isDlvPassOnlyCart ||(subTotal != null && subTotal > (user!=null&&user.getShoppingCart()!=null&&user.getShoppingCart().getDeliveryReservation()!=null?
        											user.getShoppingCart().getDeliveryReservation().getMinOrderAmt():0))) {
				callAvalaraForTax(user);
        	}
        }

        return result;
    }

	private void callAvalaraForTax(SessionUser user) {
		if(FDStoreProperties.getAvalaraTaxEnabled()){		
		Cart cart = user.getShoppingCart();
		avalaraContext = new AvalaraContext(user.getFDSessionUser().getShoppingCart());
		avalaraContext.setCommit(false);
		cart.getAvalaraTax(avalaraContext); 
		if(avalaraContext.isAvalaraTaxed())
		user.setIsAvalaraTaxed(true);
		}
	}

    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView setPaymentMethod(ModelAndView model, SessionUser user, PaymentMethodSelection reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {

        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.setPaymentMethod(reqestMessage.getPaymentMethodId(), reqestMessage.getBillingRef(), reqestMessage.getIsAccountLevel());

        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Payment method set successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    /**
     * @param model
     * @param user
     * @param reqestMessage
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView setPaymentMethodEx(ModelAndView model, SessionUser user, PaymentMethodSelection reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.setPaymentMethodEx(reqestMessage.getPaymentMethodId(), reqestMessage.getBillingRef(), reqestMessage.getIsAccountLevel());

        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            if (isResponseAdditionalEnable(request, EnumResponseAdditional.INCLUDE_PAYMENT)) {
                responseMessage = getPaymentMethods(user);
            } else {
                responseMessage = Message.createSuccessMessage("Payment method set successfully.");
            }
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private void verifyPaymentMethodFailure(HttpServletRequest request, HttpServletResponse response
    											, SessionUser user, Message responseMessage) {
    	if(request.getSession().getAttribute(SessionParamName.SESSION_PARAM_PYMT_VERIFYFLD) !=null) {
    		removeUserInSession(user, request, response);
    		responseMessage.addWarningMessage(WARN_SESSION_REMOVED, "true");
        }
    }
    
    private ModelAndView addPaymentMethodEx(ModelAndView model, SessionUser user, PaymentMethodRequest reqestMessage,
            HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.addPaymentMethodEx(reqestMessage);
        
        ActionResult result = resultBundle.getActionResult();
        
        propogateSetSessionValues(request.getSession(), resultBundle);
                
        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Payment method added successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        verifyPaymentMethodFailure(request, response, user, responseMessage);
        setResponseMessage(model, responseMessage, user);        
        return model;
    }
    
    private ModelAndView addPaymentMethod(ModelAndView model, SessionUser user, PaymentMethodRequest reqestMessage,
            HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.addPaymentMethod(reqestMessage, request.getSession().getAttribute(SessionName.PAYMENT_ATTEMPT));
        
        ActionResult result = resultBundle.getActionResult();
        
        propogateSetSessionValues(request.getSession(), resultBundle);
                
        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Payment method added successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        verifyPaymentMethodFailure(request, response, user, responseMessage);
        setResponseMessage(model, responseMessage, user);        
        return model;
    }
    
    
    private ModelAndView savePaymentMethod(ModelAndView model, SessionUser user, PaymentMethodRequest reqestMessage,
            HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);        
        ResultBundle resultBundle = checkout.savePaymentMethod(reqestMessage);
        
        ActionResult result = resultBundle.getActionResult();
        Message responseMessage = null;        
        String str = (String)resultBundle.getExtraData(CheckoutControllerTagWrapper.REQUESTED_PAYMENT_ID);               
        propogateSetSessionValues(request.getSession(), resultBundle);          
        if (result.isSuccess()) {            
            responseMessage = new PaymentResponse();
            ((PaymentResponse) responseMessage).setPaymentId(str.toString()==null ? "":str.toString());             
            ((PaymentResponse) responseMessage).setSuccessMessage("Payment method saved successfully."); 
            
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        verifyPaymentMethodFailure(request, response, user, responseMessage);
        
        setResponseMessage(model, responseMessage, user);
        
        return model;
    }

    private ModelAndView addAndSetPaymentMethod(ModelAndView model, SessionUser user, PaymentMethodRequest reqestMessage,
            HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.addAndSetPaymentMethod(reqestMessage, request.getSession().getAttribute(SessionName.PAYMENT_ATTEMPT));
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);
                
        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Payment method added successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        verifyPaymentMethodFailure(request, response, user, responseMessage);
        
        setResponseMessage(model, responseMessage, user);
        
        return model;
    }

    private ModelAndView editPaymentMethod(ModelAndView model, SessionUser user, PaymentMethodRequest reqestMessage,
            HttpServletRequest request, HttpServletResponse response) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.editPaymentMethod(reqestMessage, request.getSession().getAttribute(SessionName.PAYMENT_ATTEMPT));
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);
        
        
        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Payment method updated successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings()); 
        verifyPaymentMethodFailure(request, response, user, responseMessage);
                
        setResponseMessage(model, responseMessage, user);
        
        return model;
    }
    
    private ModelAndView deletePaymentMethod(ModelAndView model, SessionUser user, PaymentMethodRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.deletePaymentMethod(reqestMessage.getPaymentMethodId());
        if(user!=null && user.getFDSessionUser()!=null && user.getFDSessionUser().getShoppingCart()!=null && 
        		user.getFDSessionUser().getShoppingCart().getPaymentMethod()!= null &&
        		user.getFDSessionUser().getShoppingCart().getPaymentMethod().getPK()!= null &&
        		reqestMessage.getPaymentMethodId().equals(user.getFDSessionUser().getShoppingCart().getPaymentMethod().getPK().getId())){
        	user.getFDSessionUser().getShoppingCart().setPaymentMethod(null);
        }
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Payment method deleted successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView deletePaymentMethodEx(ModelAndView model, SessionUser user, PaymentMethodRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.deletePaymentMethodEx(reqestMessage.getPaymentMethodId());
        if(user!=null && user.getFDSessionUser()!=null && user.getFDSessionUser().getShoppingCart()!=null && 
        		user.getFDSessionUser().getShoppingCart().getPaymentMethod()!= null &&
        		user.getFDSessionUser().getShoppingCart().getPaymentMethod().getPK()!= null &&
        		reqestMessage.getPaymentMethodId().equals(user.getFDSessionUser().getShoppingCart().getPaymentMethod().getPK().getId())){
        	user.getFDSessionUser().getShoppingCart().setPaymentMethod(null);
        }
        ActionResult result = resultBundle.getActionResult();

        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Payment method deleted successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
                
    private ModelAndView deleteDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        Message responseMessage = null;
        if (user.isVoucherHolder()) {
			responseMessage = Message.createSuccessMessage(ACTION_DELETE_DELIVERY_ADDRESS);
			throw new FDActionNotAllowedException(
					"This account is not enabled to change delivery address.");
		}
        
        ResultBundle resultBundle = checkout.deleteDeliveryAddress(reqestMessage.getShipToAddressId());
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Delivery Address deleted successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView deleteDeliveryAddressEx(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        Message responseMessage = null;
        if (user.isVoucherHolder()) {
			responseMessage = Message.createSuccessMessage(ACTION_DELETE_DELIVERY_ADDRESS);
			throw new FDActionNotAllowedException(
					"This account is not enabled to change delivery address.");
		}
        
        ResultBundle resultBundle = checkout.deleteDeliveryAddressEx(reqestMessage.getShipToAddressId());
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Delivery Address deleted successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView getCVVStatus(ModelAndView model, SessionUser user) throws FDException, JsonException {

    	Message responseMessage = new CVVResponse();
    	((CVVResponse)responseMessage).setEnabled(FDStoreProperties.isPaymentMethodVerificationForMobileApiEnabled());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    private ModelAndView getSelectedDeliveryAddress(ModelAndView model, SessionUser user) throws FDException, JsonException {

    	Message responseMessage = null;
        //Delivery Address
        //If missing, just don't display it.
    	Cart cart = user.getShoppingCart();
    	ErpAddressModel dlvAddress =  user.getShoppingCart().getDeliveryAddress();
        if (null != dlvAddress) {
            boolean pickupOrder = cart instanceof FDOrderI ? EnumDeliveryType.PICKUP.equals(((FDOrderI) cart).getDeliveryType())
                    : dlvAddress instanceof ErpDepotAddressModel && ((ErpDepotAddressModel) dlvAddress).isPickup();
            boolean isHomeOrder = cart instanceof FDOrderI ? EnumDeliveryType.HOME.equals(((FDOrderI) cart).getDeliveryType())
                    : dlvAddress instanceof ErpAddressModel;
            boolean isCorporateOrder = cart instanceof FDOrderI ? EnumDeliveryType.CORPORATE.equals(((FDOrderI) cart).getDeliveryType())
                    : dlvAddress instanceof ErpAddressModel;

            if (pickupOrder) {
                
                responseMessage = getErrorMessage("invalidbilladdress", "Cannot add pickup address as a billing address.");
            } else {
            	responseMessage = new DeliveryAddresses();
            	List<com.freshdirect.mobileapi.controller.data.response.ShipToAddress> selectedAddress = new ArrayList<com.freshdirect.mobileapi.controller.data.response.ShipToAddress>();
            	com.freshdirect.mobileapi.controller.data.response.ShipToAddress shipToAddress = new com.freshdirect.mobileapi.controller.data.response.ShipToAddress(ShipToAddress
                        .wrap(dlvAddress));
            	selectedAddress.add(shipToAddress);
            	
                if (isHomeOrder ) {
                	((DeliveryAddresses)responseMessage).setResidentialAddresses(selectedAddress);
                } else if (isCorporateOrder) {
                	((DeliveryAddresses)responseMessage).setCorporateAddresses(selectedAddress);
                } else {
                    throw new IllegalArgumentException("Unrecongized delivery type. dlvAddress.getId=" + dlvAddress.getId());
                }
            }
        } else {
        	responseMessage = getErrorMessage("shiptoaddrnotselected", "Delivery address not selected.");
        }
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    /**
     * @param model
     * @param user
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private Message getPaymentMethods(SessionUser user) throws FDException, JsonException {
		// Discard Masterpass Wallet Cards from list -- For Standard Checkout Feature
		List<ErpPaymentMethodI> paymentMethods = discardEwalletCards(user.getPaymentMethods(),"MP");
		List<PaymentMethod> ewallet = user.getEwallet(paymentMethods);
		paymentMethods = discardEwalletCards(paymentMethods,"PP");
        List<PaymentMethod> electronicChecks = user.getElectronicChecks(paymentMethods);
        List<PaymentMethod> creditCards = user.getCreditCards(paymentMethods);
        List<PaymentMethod> ebtCards = user.getEBTCards(paymentMethods);
        
        boolean isCheckEligible = user.isCheckEligible();
        boolean isEcheckRestricted = user.isEcheckRestricted();
        boolean isEbtAccepted = user.isEbtAccepted();

        PaymentMethods responseMessage = new PaymentMethods(isCheckEligible, isEcheckRestricted, isEbtAccepted, creditCards,electronicChecks,ebtCards,ewallet);

        if ((responseMessage.getCreditCards() != null && responseMessage.getCreditCards().size() == 0) 
        		&& (responseMessage.getElectronicChecks() != null && responseMessage.getElectronicChecks().size() == 0) 
        		&& (responseMessage.getEwallet() != null && responseMessage.getEwallet().size() == 0)) {
            responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);         
        }
        // Commented below as condition is invalid because credit card check also should be there. Added the Ewallet check in the above condition itself.
        /*else if((responseMessage.getEwallet() != null && responseMessage.getEwallet().size() == 0) 
        		&& (responseMessage.getElectronicChecks() != null && responseMessage.getElectronicChecks().size() == 0)) 
        {
        	
        	responseMessage.addWarningMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);
        	
        }*/
        else {
        	responseMessage.setSelectedId(new Checkout(user).getPreselectedPaymethodMethodId());
        	responseMessage.setDefaultId(user.getFDSessionUser().getFDCustomer().getDefaultPaymentMethodPK());
        	responseMessage.setDefaultType(user.getFDSessionUser().getFDCustomer().getDefaultPaymentType());
        }
        responseMessage.getCheckoutHeader().setHeader(user.getShoppingCart());
        responseMessage.setSuccessMessage("Payment methods fetched successfully.");
        return responseMessage;
    }
    /**
     * @param paymentMethods
     * @param walletType
     */
    private List<ErpPaymentMethodI> discardEwalletCards(List<ErpPaymentMethodI> paymentMethods, String walletType){
    	List<ErpPaymentMethodI> updatedPaymentMethodList  = new ArrayList<ErpPaymentMethodI>();
    	if(paymentMethods != null && !paymentMethods.isEmpty()){
    		for(ErpPaymentMethodI paymentMethod : paymentMethods){
    			if( paymentMethod.geteWalletID() != null ){
    				int ewalletId = Integer.parseInt(paymentMethod.geteWalletID());
    	    		if( ewalletId == EnumEwalletType.getEnum(walletType).getValue()){
    	    			continue;
    	    		}else{
    	    			updatedPaymentMethodList.add(paymentMethod);
    	    		}
    			}else{
    				updatedPaymentMethodList.add(paymentMethod);
    			}
    		}
    	}else{
    		Collections.<ErpPaymentMethodI>emptyList();
    	}
    	return updatedPaymentMethodList;
    }

    private ModelAndView addAndSetDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest requestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        Message responseMessage = null;
        if (user.isVoucherHolder()) {
			responseMessage = Message.createSuccessMessage(ACTION_ADD_AND_SET_DELIVERY_ADDRESS);
			throw new FDActionNotAllowedException(
					"This account is not enabled to change delivery address.");
		}
        
        ActionResult result = null;
        
        // === FKMW - validate form fields before submitting them to the app layer ===
        
        final boolean isWebRequest = isExtraResponseRequested(request);
        if (isWebRequest) {
            result = DeliveryAddressValidatorUtil.validateDeliveryAddress(requestMessage);

            // halt on any error
            if (result.isFailure()) {
                responseMessage = getErrorMessage(result, request);
            }
        }
        
        // === FKMW end ===
        
        if (responseMessage == null) {
            ResultBundle resultBundle = checkout.addAndSetDeliveryAddress(requestMessage);
            result = resultBundle.getActionResult();
            if (!isWebRequest) {
                propogateSetSessionValues(request.getSession(), resultBundle);
            }

            if (result.isSuccess()) {
                if (isExtraResponseRequested(request)) {
                    user.setUserContext();
                    DeliveryAddress deliveryAddress = DeliveryAddress.wrap(user.getShoppingCart().getDeliveryAddress());
                    TimeSlotCalculationResult timeSlotResult = deliveryAddress.getDeliveryTimeslot(user, false, isCheckoutAuthenticated(request));
                    com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots slotResponse = new com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots(
                            timeSlotResult);
                    slotResponse.getCheckoutHeader().setHeader(user.getShoppingCart());
                    CMSPageRequest pageRequest = new CMSPageRequest();
                    pageRequest.setPlantId(BrowseUtil.getPlantId(user));
                    DeliveryTimeslotPageResponse pageResponse = new DeliveryTimeslotPageResponse();
                    populateHomePages(user, pageRequest, pageResponse, request);
                    pageResponse.setDeliveryTimeslot(slotResponse);
                    responseMessage = pageResponse;
                } else {
                    responseMessage = Message.createSuccessMessage("Delivery Address added successfully.");
                }
            } else {
                responseMessage = getErrorMessage(result, request);
            }
        }
        responseMessage.addWarningMessages(result.getWarnings());

        if(responseMessage.getErrors().containsKey(MessageCodes.ERR_NO_DELIVERY_ADDRESS) && isWebRequest){
        	responseMessage.getErrors().put(MessageCodes.ERR_NO_DELIVERY_ADDRESS, MessageCodes.MSG_DONT_DELIVER_TO_ADDRESS_MOB_FDX);
        }
        
        setResponseMessage(model, responseMessage, user);
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
    private ModelAndView removeSpecialRestrictedItemsFromCart(ModelAndView model, SessionUser user, HttpServletRequest request)
            throws FDException, JsonException {
    	
        Message responseMessage = null;
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.removeSpecialRestrictedItemsFromCart(qetRequestData(request));
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        if (result.isSuccess()) {
            responseMessage = Message.createSuccessMessage("Item(s) removed successfully.");
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        responseMessage.addWarningMessages(result.getWarnings());

        setResponseMessage(model, responseMessage, user);

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
    private ModelAndView getSpecialRestrictedItemDetail(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException,
            JsonException {
        Checkout checkout = new Checkout(user);
        Message responseMessage = checkout.getSpecialRestrictedItemDetail();
        setResponseMessage(model, responseMessage, user);
        return model;
    }
    
    
    
    /**
     * 
     * @param model
     * @param user
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView submitOrderFDX(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
        final boolean isWebRequest = isExtraResponseRequested(request);
        final Checkout checkout = new Checkout(user);
        SubmitOrderExResult message = new SubmitOrderExResult();
        final FDUserI fdUser = user.getFDSessionUser();

        if (isWebRequest) {
            removeInvalidLines(fdUser, request.getServerName());
        }

        if (null != fdUser.getGiftCardList()) {
            fdUser.getShoppingCart().setSelectedGiftCards(fdUser.getGiftCardList().getSelectedGiftcards());
        }

        if (fdUser.getShoppingCart().getPaymentMethod() == null) {
            checkout.setPaymentMethodEx(checkout.getPreselectedPaymethodMethodId(), "","N");
        }

        ResultBundle dlvValidationResult = new ResultBundle();
        dlvValidationResult.setActionResult(new ActionResult());
        
//        if(user!=null && user.getUserContext() != null && user.getUserContext().getStoreContext() != null && 
//           user.getUserContext().getStoreContext().getEStoreId().equals(EnumEStoreId.FD) && user.isChefsTable()){
//        	TimeslotService.defaultService().applyPreReservedDeliveryTimeslot(request.getSession());
//        }
        
        /*
         * code refactored as part of DP17-122 to allow DeliveryPass only orders
         * 
         * */
        boolean isDlvPassCartOnlyNotAllowed= false;//Cart contains DeliveryPass and allowed , or no deliverypass in the cart
        boolean isDlvPassCartOnly = user.getFDSessionUser().getShoppingCart().containsDlvPassOnly();
        		
        		if(!FDStoreProperties.isDlvPassStandAloneCheckoutEnabled() && isDlvPassCartOnly){
        			isDlvPassCartOnlyNotAllowed = true;
        			message.addErrorMessage("error_dlv_pass_only", SystemMessageList.MSG_CONTAINS_DLV_PASS_ONLY);
        			//dlvValidationResult.getActionResult().addError(new ActionError("error_dlv_pass_only", SystemMessageList.MSG_CONTAINS_DLV_PASS_ONLY));        			
                    message.setStatus(Message.STATUS_FAILED);
                    setResponseMessage(model, message, user);
                    return model;
        		}

        /*if (user != null && user.getShoppingCart() != null && user.getShoppingCart().getDeliveryReservation() != null
                && fdUser.getShoppingCart().getPaymentMethod() != null
                && (UserValidationUtil.validateContainsDlvPassOnly(request, dlvValidationResult.getActionResult()) != true))*/
        	
        if (user != null && user.getShoppingCart() != null && !isDlvPassCartOnlyNotAllowed
                && fdUser.getShoppingCart().getPaymentMethod() != null) {
            // Reservation Validity
            
            if (!isDlvPassCartOnly) {
            	if(user.getShoppingCart().getDeliveryReservation() != null){
            		boolean reservationValid = checkout.checkReservationExpiry(user);
            		if(reservationValid){
		                if (user.getShoppingCart().containsAlcohol()) {
		                    // Call Address And Alcohol Check
		                    message = checkout.checkAddressForAlcoholAndAgeVerification(message, user, request);
		                    if (isSuccess(message.getStatus())) {
		                        // Timeslot Alcohol Check
		                        message = checkout.alcoholTimeSlotCheck(message, user, isWebRequest);
		                    }
		                }
		                // Fraud Address Check
		                if (isSuccess(message.getStatus()) && user.getShoppingCart().getDeliveryAddress() != null){
		                    EnumRestrictedAddressReason reason = FDDeliveryManager.getInstance().checkAddressForRestrictions( user.getShoppingCart().getDeliveryAddress() );
		            		if ( !EnumRestrictedAddressReason.NONE.equals( reason ) ) {
		            			message.addErrorMessage("Unable to place order contact customer service");
		                        message.setStatus(Message.STATUS_FAILED);
		                    }
		                }
            		} else{
            			message.addErrorMessage("invalid_reservation", SystemMessageList.MSG_CHECKOUT_EXPIRED_RESERVATION);
                        message.setStatus(Message.STATUS_FAILED);
            		}
            	} else {
            		EnumEStoreId eStore = (user.getUserContext() != null && user.getUserContext().getStoreContext() != null) ? user.getUserContext().getStoreContext().getEStoreId()
                            : EnumEStoreId.FD;
                    if (EnumEStoreId.FDX.equals(eStore)) {
                        message.addErrorMessage("Please select your delivery time");
                        message.setStatus(Message.STATUS_FAILED);
                    } else {
                        message.addErrorMessage("There is an issue with the delivery time, please select another one");
                        message.setStatus(Message.STATUS_FAILED);
                    }
            	}
           }
//              else {   
                // Atp Check
                if (isSuccess(message.getStatus())) {
                	user.setFromLogin("Checkout");
                    ActionResult availabliltyResult = this.performAvailabilityCheck(user, request.getSession(),isDlvPassCartOnly);
                    if (!availabliltyResult.isSuccess()) {
                    	if(availabliltyResult.getFirstError().getDescription().equals(AvailabilityService.REASON_MAX_PASSES) ||
                    	   availabliltyResult.getFirstError().getDescription().equals(AvailabilityService.REASON_PASS_EXISTS))
                    	{
                    		message.addErrorMessage("Your account already has Delivery Pass enabled, please remove to continue");
                            message.setStatus(Message.STATUS_FAILED);
                    	}
                    	else if(availabliltyResult.getFirstError().getDescription().equals(AvailabilityService.REASON_TOO_MANY_PASSES))
                    	{
                    		message.addErrorMessage("Too many Delivery Passes added, please choose one and continue");
                            message.setStatus(Message.STATUS_FAILED);
                    	}
                    	else if(availabliltyResult.getFirstError().getDescription().equals(AvailabilityService.REASON_NOT_ELIGIBLE))
                    	{
                    		message.addErrorMessage("You are not currently eligible for Delivery Passes. Please contact Customer Service");
                            message.setStatus(Message.STATUS_FAILED);
                    	}
                    	else if(availabliltyResult.getFirstError().getDescription().equals(AvailabilityService.REASON_PROMOTIONAL_PASS))
                    	{
                    		message.addErrorMessage("You are not currently eligible for Delivery Passes. Please contact Customer Service");
                            message.setStatus(Message.STATUS_FAILED);
                    	}
                    	else if(availabliltyResult.getFirstError().getDescription().equals(AvailabilityService.REASON_MULTIPLE_AUTORENEW_PASSES))
                    	{
                    		message.addErrorMessage("You already have a Delivery Pass scheduled to automatically renew. Please remove and continue");
                            message.setStatus(Message.STATUS_FAILED);
                    	}
                    	else
                    	{
                    		message = checkout.fillAtpErrorDetail(message, request);
                    	}
                    } 
                    
                    else {
                        // message = checkout.submitEx((SubmitOrderExResult)message, user, request);
                        FDCartModel cartModel = fdUser.getShoppingCart();
                        ResultBundle submitResult = checkout.submitOrder();
                        propogateSetSessionValues(request.getSession(), submitResult);
                        if (submitResult.getActionResult().isSuccess()) {
                            message.setStatus(Message.STATUS_SUCCESS);
                            com.freshdirect.mobileapi.controller.data.response.Order orderReceipt = new com.freshdirect.mobileapi.controller.data.response.Order();
                            String orderId = (String) request.getSession().getAttribute(SessionName.RECENT_ORDER_NUMBER);
                            if (orderId == null && cartModel.getPaymentMethod() == null) {
                                message.addErrorMessage("Payment method not selected");
                                message.setStatus(Message.STATUS_FAILED);
                            } else {
                                com.freshdirect.mobileapi.model.Order order = user.getOrder(orderId);
                                if (null != order) {
                                    orderReceipt = order.getOrderDetail(user);
                                    if (isWebRequest) {
                                        ProductPotatoUtil.populateCartDetailWithPotatoes(fdUser, orderReceipt.getCartDetail());
                                    }
                                }
                                orderReceipt.setOrderNumber(orderId);
                                message.wrap(orderReceipt);

                                message.addDebugMessage("Order has been submitted successfully.");
                                createAndSendUnbxdAnalyticsEvent(fdUser, request, cartModel.getOrderLines());
                            }
                        } else {
                            message.setStatus(Message.STATUS_FAILED);
                            message.addErrorMessages(submitResult.getActionResult().getErrors(), user);
                        }
                        message.addWarningMessages(submitResult.getActionResult().getWarnings());

                    }
                }
            
            
        

        } else {
            // cart not set up for checkout.
            if (user == null) {
                message.addErrorMessage("Error processing checkout - User");
            } else if (user.getShoppingCart() == null) {
                message.addErrorMessage("Error processing checkout - Cart");
            } else if (user.getShoppingCart().getDeliveryReservation() == null) {
                EnumEStoreId eStore = (user.getUserContext() != null && user.getUserContext().getStoreContext() != null) ? user.getUserContext().getStoreContext().getEStoreId()
                        : EnumEStoreId.FD;
                if (EnumEStoreId.FDX.equals(eStore)) {
                    message.addErrorMessage("Please select your delivery time");
                } else {
                    message.addErrorMessage("There is an issue with the delivery time, please select another one");
                }
            } else if (fdUser.getShoppingCart().getPaymentMethod() == null) {
                message.addErrorMessage("Error processing checkout - Payment Method");
            } else if (dlvValidationResult.getActionResult().getError("error_dlv_pass_only") != null
                    && dlvValidationResult.getActionResult().getError("error_dlv_pass_only").getDescription() != null
                    && (!dlvValidationResult.getActionResult().getError("error_dlv_pass_only").getDescription().equals(""))) {
                message.addErrorMessage(dlvValidationResult.getActionResult().getError("error_dlv_pass_only").getDescription());
            }
            message.setStatus(Message.STATUS_FAILED);

        }

        setResponseMessage(model, message, user);
        return model;
    }

    private List<FDCartLineI> removeInvalidLines(final FDUserI fdUser, String serverName) {
        FDCartModel cart = fdUser.getShoppingCart();
        List<FDCartLineI> invalidLines = OrderLineUtil.getInvalidLines(cart.getOrderLines(), fdUser.getUserContext());
        for (FDCartLineI invalidLine : invalidLines) {
            CartOperations.removeCartLine(fdUser, cart, invalidLine, serverName);
        }
        return invalidLines;
    }
    
    private static boolean isSuccess(String messageStatus) {
    	return !Message.STATUS_FAILED.equals(messageStatus);
    }
    
    private void createAndSendUnbxdAnalyticsEvent(FDUserI user, HttpServletRequest request, List<FDCartLineI> cartLines){
        if(FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, request.getCookies(), user)){
        		final boolean cosAction = CosFeatureUtil.isUnbxdCosAction(user, request.getCookies());
            final Visitor visitor = Visitor.withUser(user);
            final LocationInfo location = LocationInfo.withUrlAndReferer(RequestUtil.getFullRequestUrl(request), request.getHeader(HttpHeaders.REFERER));
            LOGGER.debug("UNBXD Service active, cartline size: " + cartLines.size());
            for(FDCartLineI cartLine : cartLines){
                AnalyticsEventI orderEvent = AnalyticsEventFactory.createEvent(AnalyticsEventType.ORDER, visitor, location, null, null, cartLine, cosAction);
                EventLoggerService.getInstance().log(orderEvent);
            }
        }
    }

    private Message isDeliveryAddressUnattended(SessionUser user, DeliveryAddressRequest address, HttpServletRequest request) {
        Message responseMessage = null;
        ActionResult result = DeliveryAddressValidatorUtil.validateUnattendeDeliveryAddress(address);
        if (result.isSuccess()) {
            UnattendedDeliveryAddressResponse unattendedResponse = new UnattendedDeliveryAddressResponse();
            unattendedResponse.setUnattendedDelivery(UnattendedDeliveryTag.checkUnattendedDelivery(createErpAddressModel(address)));
            unattendedResponse.setStatus(Message.STATUS_SUCCESS);
            responseMessage = unattendedResponse;
        } else {
            responseMessage = getErrorMessage(result, request);
        }
        return responseMessage;
    }

    private ErpAddressModel createErpAddressModel(DeliveryAddressRequest address) {
        ErpAddressModel addressModel = new ErpAddressModel();
        addressModel.setAddress1(address.getAddress1());
        addressModel.setAddress2(address.getAddress2());
        addressModel.setCity(address.getCity());
        addressModel.setZipCode(address.getZipcode());
        addressModel.setState(address.getState());
        addressModel.setApartment(address.getApartment());
        return addressModel;
    }
}
