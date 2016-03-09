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

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDActionNotAllowedException;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.OrderMobileNumberRequest;
import com.freshdirect.mobileapi.controller.data.SubmitOrderExResult;
import com.freshdirect.mobileapi.controller.data.SubmitOrderRequest;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressSelection;
import com.freshdirect.mobileapi.controller.data.request.DeliverySlotReservation;
import com.freshdirect.mobileapi.controller.data.request.Login;
import com.freshdirect.mobileapi.controller.data.request.MobilePreferenceRequest;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodRequest;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodSelection;
import com.freshdirect.mobileapi.controller.data.response.CVVResponse;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.DeliveryAddresses;
import com.freshdirect.mobileapi.controller.data.response.DynamicAvailabilityError;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethods;
import com.freshdirect.mobileapi.controller.data.response.PaymentResponse;
import com.freshdirect.mobileapi.exception.JsonException;
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
import com.freshdirect.webapp.ajax.expresscheckout.location.data.LocationData;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CheckoutController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(CheckoutController.class);

    private static final String PARAM_SLOT_ID = "slotId";

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
    
    private final static String DIR_ERROR_KEY="ERR_DARKSTORE_RECONCILIATION";
    
    private final static String DEVICE_ID="deviceId";
    
    private AvalaraContext avalaraContext;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

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
            model = getPaymentMethods(model, user);
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
        }
        return model;
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
    private ModelAndView reviewOrder(ModelAndView model, SessionUser user, HttpServletRequest request, EnumCouponContext ctx) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        Cart cart = user.getShoppingCart();
        if(EnumCouponContext.CHECKOUT.equals(ctx)){
        	user.setRefreshCouponWalletRequired(true);
        	user.setCouponEvaluationRequired(true);
        }
        if(EnumCouponContext.VIEWCART.equals(ctx) && FDStoreProperties.getAvalaraTaxEnabled()){
        	AvalaraContext avalaraContext =  new AvalaraContext(user.getFDSessionUser().getShoppingCart());
        	cart.getAvalaraTax(avalaraContext);
        }
        Message responseMessage = checkout.getCurrentOrderDetails(ctx);
        if(!user.getFDSessionUser().isCouponsSystemAvailable() && FDCouponProperties.isDisplayMessageCouponsNotAvailable()) {
        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONSYSTEM_UNAVAILABLE
        										, SystemMessageList.MSG_COUPONS_SYSTEM_NOT_AVAILABLE);
        }/*else{
        	user.setRefreshCouponWalletRequired(true);
        	user.setCouponEvaluationRequired(true);
        }*/
        
        if(user.getFDSessionUser().getShoppingCart().getExpCouponDeliveryDate()!=null){
        	responseMessage.addWarningMessage(MessageCodes.WARNING_COUPONS_EXP_DELIVERY_DATE
					, MessageCodes.MSG_COUPONS_EXP_DELIVERY_DATE);
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
        List<ShipToAddress> deliveryAddresses = user.getDeliveryAddresses();
        DeliveryAddresses responseMessage = new DeliveryAddresses((new Checkout(user)).getPreselectedDeliveryAddressId(), ShipToAddress
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
			exp.printStackTrace();
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
    	
        ResultBundle resultBundle = checkout.setCheckoutDeliveryAddress(reqestMessage.getId(), DeliveryAddressType.valueOf(reqestMessage
                .getType()));
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        
        Message responseMessage = null;
        if (result.isSuccess()) {
        	
        	DeliveryAddress deliveryAddress = DeliveryAddress.wrap(user.getShoppingCart().getDeliveryAddress());
            TimeSlotCalculationResult timeSlotResult = deliveryAddress.getDeliveryTimeslot(user, false,isCheckoutAuthenticated(request));

            com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots slotResponse = new com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots(
                    timeSlotResult);
            slotResponse.getCheckoutHeader().setHeader(user.getShoppingCart());
            responseMessage = slotResponse;
            responseMessage.setSuccessMessage("Order delivery Address have been set successfully.");
            
        } else {
        	
            responseMessage = getErrorMessage(result, request);
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
    	
        ResultBundle resultBundle = checkout.setCheckoutDeliveryAddressEx(reqestMessage.getId(), DeliveryAddressType.valueOf(reqestMessage
                .getType()));
        ActionResult result = resultBundle.getActionResult();
        
        propogateSetSessionValues(request.getSession(), resultBundle);
        
        Message responseMessage = new DynamicAvailabilityError();
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
	            }
	            
	            orderReceipt.setOrderNumber(orderId);
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
	            }
	            
	            orderReceipt.setOrderNumber(orderId);
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
        DeliveryTimeslots slot = new com.freshdirect.mobileapi.model.DeliveryTimeslots(user);
        ResultBundle resultBundle = slot.reserveSlot(slotId);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
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
    
    
    /**
     * @param user
     * @param session
     * @return
     * @throws FDException
     */
    private ActionResult performAvailabilityCheck(SessionUser user, HttpSession session) throws FDException {

        /*
         * DUP: FDWebSite/docroot/checkout/step_2_check.jsp
         * LAST UPDATED ON: 10/07/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code determines calls a method on model to perform availability check
         */
        boolean isProductFullyAvailable = user.getShoppingCart().isCartFullyAvailable(user);

        Checkout checkout = new Checkout(user);

        ResultBundle resultBundle = checkout.checkDeliveryPassAvailability();
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(session, resultBundle);

        //TODO: Not only find out availability but also get error on which specific products weren't available
        //And what to do...choose another date? or remove item?

        if (!isProductFullyAvailable) {
        	Double subTotal = null;
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
        	callAvalaraForTax(user);
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
        ResultBundle resultBundle = checkout.setPaymentMethod(reqestMessage.getPaymentMethodId(), reqestMessage.getBillingRef());

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
        ResultBundle resultBundle = checkout.setPaymentMethodEx(reqestMessage.getPaymentMethodId(), reqestMessage.getBillingRef());

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
        ResultBundle resultBundle = checkout.addPaymentMethod(reqestMessage);
        
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
        ResultBundle resultBundle = checkout.addAndSetPaymentMethod(reqestMessage);
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
        ResultBundle resultBundle = checkout.editPaymentMethod(reqestMessage);
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
    	((CVVResponse)responseMessage).setEnabled(FDStoreProperties.isPaymentMethodVerificationEnabled());
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
    private ModelAndView getPaymentMethods(ModelAndView model, SessionUser user) throws FDException, JsonException {
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
        }
        responseMessage.getCheckoutHeader().setHeader(user.getShoppingCart());
        setResponseMessage(model, responseMessage, user);
        return model;
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
    private ModelAndView addAndSetDeliveryAddress(ModelAndView model, SessionUser user, DeliveryAddressRequest reqestMessage,
            HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        Message responseMessage = null;
        if (user.isVoucherHolder()) {
			responseMessage = Message.createSuccessMessage(ACTION_ADD_AND_SET_DELIVERY_ADDRESS);
			throw new FDActionNotAllowedException(
					"This account is not enabled to change delivery address.");
		}
        
        ResultBundle resultBundle = checkout.addAndSetDeliveryAddress(reqestMessage);
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);
        if (result.isSuccess()) {
        	 responseMessage = Message.createSuccessMessage("Delivery Address added successfully.");
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
    private ModelAndView submitOrderFDX(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException{
    	Checkout checkout = new Checkout(user);
    	
    	SubmitOrderExResult message = new SubmitOrderExResult();

    	if(null !=user.getFDSessionUser().getGiftCardList()){
    		user.getFDSessionUser().getShoppingCart().setSelectedGiftCards( user.getFDSessionUser().getGiftCardList().getSelectedGiftcards() );
    	}
		
    	if(user.getFDSessionUser().getShoppingCart().getPaymentMethod() ==null) {
    		checkout.setPaymentMethodEx(checkout.getPreselectedPaymethodMethodId(), ""); 
    	}
    	if(user!=null && user.getShoppingCart()!=null && user.getShoppingCart().getDeliveryReservation()!=null && 
    			user.getFDSessionUser().getShoppingCart().getPaymentMethod() !=null){
    		//Reservation Validity
	    	boolean reservationValid = checkout.checkReservationExpiry(user);
	    	if(reservationValid){
	    		if(user.getShoppingCart().containsAlcohol()){
	    			//Call Address And Alcohol Check
	    			message = checkout.checkAddressForAlcoholAndAgeVerification((SubmitOrderExResult)message, user, request);
	    			if(isSuccess(message.getStatus())){
	    				//Timeslot Alcohol Check
	    				message = checkout.alcoholTimeSlotCheck((SubmitOrderExResult)message, user, request);
	    			}
	    		}
	    		// Atp Check
	    		if(isSuccess(message.getStatus())){
	    		ActionResult availabliltyResult = this.performAvailabilityCheck(user, request.getSession());
	    		 if (!availabliltyResult.isSuccess()){
	    			 message = checkout.fillAtpErrorDetail((SubmitOrderExResult)message, user, request);
	    		 } else {
	    			// message = checkout.submitEx((SubmitOrderExResult)message, user, request);
	    			 
	    				 ResultBundle submitResult = checkout.submitOrder();
		    			 propogateSetSessionValues(request.getSession(), submitResult);
		    				if (submitResult.getActionResult().isSuccess()) {
		    					message.setStatus(Message.STATUS_SUCCESS);
		    					com.freshdirect.mobileapi.controller.data.response.Order orderReceipt = new com.freshdirect.mobileapi.controller.data.response.Order();
		    		             String orderId=(String) request.getSession().getAttribute(SessionName.RECENT_ORDER_NUMBER);
		    		             if(orderId==null && user.getFDSessionUser().getShoppingCart().getPaymentMethod()==null ){
		    		            	 message.addErrorMessage("Payment method not selected");
		    		  				message.setStatus(Message.STATUS_FAILED);
		    		             } else {
			    		             com.freshdirect.mobileapi.model.Order order = user.getOrder(orderId);
				    		         if(null !=order){
				    				            orderReceipt = order.getOrderDetail(user);
				    			            }
				    				 orderReceipt.setOrderNumber(orderId);
				    				 message.wrap(orderReceipt);
			    		             
			    					 message.addDebugMessage("Order has been submitted successfully.");
		    		             }
		    				} else {
		    					message.setStatus(Message.STATUS_FAILED);
		    					message.addErrorMessages(submitResult.getActionResult().getErrors(), user);
		    				}
		    				message.addWarningMessages(submitResult.getActionResult().getWarnings());
	    			 
	    			 
	    		 }
	    		}
	    	} else {
	    		// Reservation expired.
	 			message.addErrorMessage("invalid_reservation", SystemMessageList.MSG_CHECKOUT_EXPIRED_RESERVATION);
	 			message.setStatus(Message.STATUS_FAILED);
	    	}
    	
    	} else {
    		// cart not set up for checkout.
    		if (user==null){
    			message.addErrorMessage("Error processing checkout - User");
 			} else if (user.getShoppingCart()==null){
    			message.addErrorMessage("Error processing checkout - Cart");
 			} else if (user.getShoppingCart().getDeliveryReservation()==null){
    			message.addErrorMessage("Error processing checkout - Reservation");
 			} else if (user.getFDSessionUser().getShoppingCart().getPaymentMethod()==null){
    			message.addErrorMessage("Error processing checkout - Payment Method");
 			}
 			message.setStatus(Message.STATUS_FAILED);
 			
    	}
    	
    	setResponseMessage(model, message, user);
    	return model;
    }
    
    private static boolean isSuccess(String messageStatus) {
    	return !Message.STATUS_FAILED.equals(messageStatus);
    }
}