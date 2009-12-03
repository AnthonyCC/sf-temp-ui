package com.freshdirect.mobileapi.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressSelection;
import com.freshdirect.mobileapi.controller.data.request.DeliverySlotReservation;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodSelection;
import com.freshdirect.mobileapi.controller.data.response.DeliveryAddresses;
import com.freshdirect.mobileapi.controller.data.response.OrderReceipt;
import com.freshdirect.mobileapi.controller.data.response.PaymentMethods;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.model.Checkout;
import com.freshdirect.mobileapi.model.DeliveryAddress;
import com.freshdirect.mobileapi.model.DeliveryTimeslots;
import com.freshdirect.mobileapi.model.Depot;
import com.freshdirect.mobileapi.model.PaymentMethod;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.ShipToAddress;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CheckoutController extends BaseController {

    private static Category LOGGER = LoggerFactory.getInstance(CheckoutController.class);

    private static final String PARAM_SLOT_ID = "slotId";

    private final static String ACTION_GET_REMOVE_UNAVAILABLE_ITEMS = "removeunavailableitems";

    private final static String ACTION_GET_ATP_ERROR_DETAIL = "getatperrordetail";

    private final static String ACTION_INIT_CHECKOUT = "initcheckout";

    private final static String ACTION_GET_DELIVERY_ADDRESSES = "getdeliveryaddresses";

    private final static String ACTION_REVIEW_ORDER_DETAIL = "revieworderdetail";

    private final static String ACTION_SET_DELIVERY_ADDRESS = "setdeliveryaddress";

    private final static String ACTION_RESERVE_TIMESLOT = "reservetimeslot";

    private final static String ACTION_GET_PAYMENT_METHODS = "getpaymentmethods";

    private final static String ACTION_SET_PAYMENT_METHOD = "setpaymentmethod";

    private final static String ACTION_SUBMIT_ORDER = "submitorder";

    private final static String ACTION_ALCOHOL_AGE_VERIFY = "alcoholageverify";

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws FDException, ServiceException, JsonException {

        if (ACTION_INIT_CHECKOUT.equals(action)) {
            //Validate pre-req. If pass, go directly to get payment method
            if (validateCheckoutRequirements(model, user)) {
                model = getDeliveryAddresses(model, user);
            }
        } else if (ACTION_GET_DELIVERY_ADDRESSES.equals(action)) {
            model = getDeliveryAddresses(model, user);
        } else if (ACTION_SET_DELIVERY_ADDRESS.equals(action)) {
            DeliveryAddressSelection reqestMessage = parseRequestObject(request, response, DeliveryAddressSelection.class);
            model = setDeliveryAddress(model, user, reqestMessage, request);
        } else if (ACTION_RESERVE_TIMESLOT.equals(action)) {
            String slotId = request.getParameter(PARAM_SLOT_ID);
            if (slotId == null) {
                DeliverySlotReservation requestMessage = parseRequestObject(request, response, DeliverySlotReservation.class);
                model = reserveTimeslot(model, user, requestMessage.getDeliveryTimeslotId(), request);
            } else {
                model = reserveTimeslot(model, user, slotId, request);
            }
        } else if (ACTION_GET_PAYMENT_METHODS.equals(action)) {
            model = getPaymentMethods(model, user);
        } else if (ACTION_SET_PAYMENT_METHOD.equals(action)) {
            PaymentMethodSelection requestMessage = parseRequestObject(request, response, PaymentMethodSelection.class);
            model = setPaymentMethod(model, user, requestMessage, request);
        } else if (ACTION_REVIEW_ORDER_DETAIL.equals(action)) {
            //Review order. What's being order, where it's going, how it's being paid for, etc.
            model = reviewOrder(model, user, request);
        } else if (ACTION_SUBMIT_ORDER.equals(action)) {
            model = submitOrder(model, user, request);
        } else if (ACTION_GET_ATP_ERROR_DETAIL.equals(action)) {
            model = getAtpErrorDetail(model, user, request);
        } else if (ACTION_GET_REMOVE_UNAVAILABLE_ITEMS.equals(action)) {
            model = removeUnavailableItemsFromCart(model, user, request);
        } else if (ACTION_ALCOHOL_AGE_VERIFY.equals(action)) {
            model = verifyAlcoholAge(model, user, request);
        }
        return model;
    }

    /**
     * @param model
     * @param user
     * @return
     * @throws JsonException
     */
    private boolean validateCheckoutRequirements(ModelAndView model, SessionUser user) throws JsonException {
        boolean valid = true;
        Message responseMessage = new Message();
        if ((user.getPaymentMethods() == null) || (user.getPaymentMethods().size() == 0)) {
            responseMessage.addErrorMessage(ERR_NO_PAYMENT_METHOD, ERR_NO_PAYMENT_METHOD_MSG);
            valid = false;
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
    private ModelAndView reviewOrder(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        Message responseMessage = checkout.getCurrentOrderDetails();
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
            TimeSlotCalculationResult timeSlotResult = deliveryAddress.getDeliveryTimeslot(user);

            String preselectedTimeslotId = checkout.getPreselectedTimeslotId(timeSlotResult);

            com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots slotResponse = new com.freshdirect.mobileapi.controller.data.response.DeliveryTimeslots(
                    timeSlotResult);
            slotResponse.setSelectedTimeslotId(preselectedTimeslotId);
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
     * @param request
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView submitOrder(ModelAndView model, SessionUser user, HttpServletRequest request) throws FDException, JsonException {
        Checkout checkout = new Checkout(user);
        ResultBundle resultBundle = checkout.submitOrder();
        ActionResult result = resultBundle.getActionResult();
        propogateSetSessionValues(request.getSession(), resultBundle);

        Message responseMessage = null;
        if (result.isSuccess()) {

            OrderReceipt orderReceipt;
            try {
                orderReceipt = checkout.getOrderReceipt((String) request.getSession().getAttribute(SessionName.RECENT_ORDER_NUMBER));
            } catch (IllegalAccessException e) {
                throw new FDException(e);
            } catch (InvocationTargetException e) {
                throw new FDException(e);
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
            result.addError(new ActionError(ERR_ATP_FAILED, "One of the products were not available."));
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
     * @return
     * @throws FDException
     * @throws JsonException
     */
    private ModelAndView getPaymentMethods(ModelAndView model, SessionUser user) throws FDException, JsonException {
        List paymentMethods = user.getPaymentMethods();
        List<PaymentMethod> electronicChecks = user.getElectronicChecks(paymentMethods);
        List<PaymentMethod> creditCards = user.getCreditCards(paymentMethods);
        boolean isCheckEligible = user.isCheckEligible();
        boolean isEcheckRestricted = user.isEcheckRestricted();

        PaymentMethods responseMessage = new PaymentMethods(isCheckEligible, isEcheckRestricted, creditCards, electronicChecks);
        responseMessage.setSelectedId(new Checkout(user).getPreselectedPaymethodMethodId());
        responseMessage.getCheckoutHeader().setHeader(user.getShoppingCart());
        setResponseMessage(model, responseMessage, user);
        return model;
    }
}
