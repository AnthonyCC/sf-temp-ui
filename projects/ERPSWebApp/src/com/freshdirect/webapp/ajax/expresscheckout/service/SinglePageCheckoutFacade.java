package com.freshdirect.webapp.ajax.expresscheckout.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotModel;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.payments.util.PaymentMethodUtil;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAdapter;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.checkout.UnavailabilityPopulator;
import com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.checkout.service.CheckoutService;
import com.freshdirect.webapp.ajax.expresscheckout.content.service.ContentFactoryService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormRestriction;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutSuccessData;
import com.freshdirect.webapp.ajax.expresscheckout.deliverypass.service.SinglePageCheckoutHeaderService;
import com.freshdirect.webapp.ajax.expresscheckout.drawer.service.DrawerService;
import com.freshdirect.webapp.ajax.expresscheckout.gogreen.service.GoGreenService;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.FormLocationData;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.LocationData;
import com.freshdirect.webapp.ajax.expresscheckout.location.service.DeliveryAddressService;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.FormPaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.data.SuccessPageData;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.service.ReceiptService;
import com.freshdirect.webapp.ajax.expresscheckout.sempixels.service.SemPixelService;
import com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.data.TextMessageAlertData;
import com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.service.TextMessageAlertService;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.checkout.PaymentMethodManipulator;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.CheckOrderStatusTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class SinglePageCheckoutFacade {

    private static final String SUB_TOTAL_BOX_JSON_KEY = "subTotalBox";
    public static final String REDIRECT_URL_JSON_KEY = "redirectUrl";
    private static final String ATP_FAILURE_JSON_KEY = "atpFailure";
    public static final String TIMESLOT_JSON_KEY = "timeslot";
    public static final String PAYMENT_JSON_KEY = "payment";
    public static final String ADDRESS_JSON_KEY = "address";
    private static final String RESTRICTION_JSON_KEY = "restriction";
    private static final String WARNING_MESSAGE_LABEL = "warning_message";
    private static final String EXPRESS_CHECKOUT_VIEW_CART_PAGE_URL = "/expressco/view_cart.jsp";
    private static final String CART_DATA_JSON_KEY = "cartData";
    private static final String BILLING_REFERENCE_INFO_JSON_KEY = "billingReferenceInfo";
    private static final String FORM_META_DATA_JSON_KEY = "formMetaData";
    private static final String DELIVERY_CHARGE_ID = "deliverycharge";
    private static final String DELIVERY_CHARGE_NAME = "Delivery Charge";
    private static final String ORDER_FREE_TRIAL_MSG = "ORDER_FREE_TRIAL_MSG";
    // private static final String MASTERPASS_EWALLET_TYPE = "MP";
    // private static final String WALLET_SESSION_CARD_ID="WALLET_CARD_ID";
    // private static final String EWALLET_SESSION_ATTRIBUTE_NAME="EWALLET_CARD_TYPE";
    // private static final String MP_EWALLET_CARD="MP_CARD";

    private static final Category LOGGER = LoggerFactory.getInstance(SinglePageCheckoutFacade.class);

    public static SinglePageCheckoutFacade defaultFacade() {
        return INSTANCE;
    }

    private static final SinglePageCheckoutFacade INSTANCE = new SinglePageCheckoutFacade();

    private final PaymentService paymentService;
    private final ReceiptService receiptService;
    private final TimeslotService timeslotService;
    private final AvailabilityService availabilityService;
    private final ContentFactoryService contentFactoryService;
    private final TextMessageAlertService textMessageAlertService;
    private final DeliveryAddressService deliveryAddressService;

    private SinglePageCheckoutFacade() {
        paymentService = PaymentService.defaultService();
        receiptService = ReceiptService.defaultService();
        timeslotService = TimeslotService.defaultService();
        availabilityService = AvailabilityService.defaultService();
        contentFactoryService = ContentFactoryService.defaultService();
        textMessageAlertService = TextMessageAlertService.defaultService();
        deliveryAddressService = DeliveryAddressService.defaultService();
    }

    public SinglePageCheckoutData load(final FDUserI user, HttpServletRequest request)
            throws FDResourceException, IOException, TemplateException, JspException, RedirectToPage {
    	ErpCustomerModel customerModel = FDCustomerManager.getCustomerPaymentAndCredit(user.getIdentity());
    	boolean dlvPassCart = null !=request.getParameter("dlvPassCart") && "true".equalsIgnoreCase(request.getParameter("dlvPassCart")) ? true: false;
        FDCartI cart = populateCartDataFromParentOrder(user, dlvPassCart);
        SinglePageCheckoutData result = new SinglePageCheckoutData();
        if (StandingOrderHelper.isSO3StandingOrder(user)) {
            result.setStandingOrderResponseData(StandingOrderHelper.populateResponseData(user.getCurrentStandingOrder(), false));
        }
        handleModifyCartPreSelections(user, request);
        result.setHeaderData(SinglePageCheckoutHeaderService.defaultService().populateHeader(user));
        result.setDrawer(DrawerService.defaultService().loadDrawer(user, dlvPassCart));
        FormPaymentData paymentData = loadUserPaymentMethods(user, request, customerModel.getPaymentMethods(), customerModel.getCustomerCredits());
        // remove the xxxx in account number
    	if (paymentData.getPayments() != null) {
    		for(PaymentData payment: paymentData.getPayments()) {
    			if (payment.getAccountNumber() != null) {
    				payment.setAccountNumber(payment.getAccountNumber().replace("XXXX", ""));
    			}
    		}
    	}
        result.setPayment(paymentData);
        result.setFormMetaData(FormMetaDataService.defaultService().populateFormMetaData(user));
        result.setAddress(loadAddress(user, request.getSession(), cart, FDCustomerManager.getShipToAddresses(user.getIdentity())));
        if (FDStoreProperties.getAtpAvailabiltyMockEnabled()) {
            UnavailabilityData atpFailureData = UnavailabilityPopulator.createUnavailabilityData((FDSessionUser) user);
            if (!atpFailureData.getNonReplaceableLines().isEmpty() || !atpFailureData.getReplaceableLines().isEmpty() || atpFailureData.getNotMetMinAmount() != null
                    || !atpFailureData.getPasses().isEmpty()) {
                result.setAtpFailure(atpFailureData);
            }
        }
        if (StandingOrderHelper.isSO3StandingOrder(user)) {
            result.setTimeslot(timeslotService.loadCartTimeslot(user, user.getSoTemplateCart()));
        } else {
        	if(!(user.getShoppingCart().isDlvPassStandAloneCheckoutAllowed() && user.getShoppingCart().containsDlvPassOnly())){
        		result.setTimeslot(timeslotService.loadCartTimeslot(user, user.getShoppingCart()));
        	}
        }
        if (!StandingOrderHelper.isSO3StandingOrder(user)) {
            result.setRestriction(CheckoutService.defaultService().preCheckOrder(user));
            result.setRedirectUrl(populateRedirectUrl(user));
        }
        return result;
    }

    public String populateRedirectUrl(FDUserI user) throws FDResourceException {
    	return RedirectService.defaultService().populateRedirectUrl(EXPRESS_CHECKOUT_VIEW_CART_PAGE_URL, WARNING_MESSAGE_LABEL,
                availabilityService.selectWarningType(user));
    }
    public Map<String, Object> loadByPageAction(FDUserI user, HttpServletRequest request, PageAction pageAction, ValidationResult validationResult) throws FDResourceException,
            IOException, TemplateException, JspException, RedirectToPage, HttpErrorResponse {
        Map<String, Object> result = new HashMap<String, Object>();

        final boolean hasValidationErrors = validationResult != null && !validationResult.getErrors().isEmpty();
        FormRestriction restriction = null;
        // [APPDEV-4425] : prevent restriction check when invalid dlv address is selected
        if (!(PageAction.SELECT_DELIVERY_ADDRESS_METHOD == pageAction && hasValidationErrors)) {
            restriction = CheckoutService.defaultService().preCheckOrder(user);
            result.put(RESTRICTION_JSON_KEY, restriction);
        } else {
            LOGGER.debug("Skipped restriction check for fraud address");
        }

        FDCartI cart = StandingOrderHelper.isSO3StandingOrder(user) ? user.getSoTemplateCart() : populateCartDataFromParentOrder(user, false);

        HttpSession session = request.getSession();
        switch (pageAction) {
            case ADD_DELIVERY_ADDRESS_METHOD:
                //$FALL-THROUGH$
            case EDIT_DELIVERY_ADDRESS_METHOD:
                //$FALL-THROUGH$
            case DELETE_DELIVERY_ADDRESS_METHOD:
                result.put(ADDRESS_JSON_KEY, loadAddress(user, session, cart));
                result.put(TIMESLOT_JSON_KEY, timeslotService.loadCartTimeslot(user, cart));
                if (!StandingOrderHelper.isSO3StandingOrder(user)) {
                    result.put(
                            REDIRECT_URL_JSON_KEY,
                            RedirectService.defaultService().populateRedirectUrl(EXPRESS_CHECKOUT_VIEW_CART_PAGE_URL, WARNING_MESSAGE_LABEL,
                                    availabilityService.selectWarningType(user)));
                }
                result.put(SUB_TOTAL_BOX_JSON_KEY, CartDataService.defaultService().loadCartDataSubTotalBox(request, user));
                break;
            case SELECT_DELIVERY_ADDRESS_METHOD:
                if (validationResult != null && validationResult.getErrors().isEmpty()) {
                	ErpCustomerModel customerModel = FDCustomerManager.getCustomerPaymentAndCredit(user.getIdentity());
                    result.put(ADDRESS_JSON_KEY, loadAddress(user, session, cart));
                    result.put(TIMESLOT_JSON_KEY, timeslotService.loadCartTimeslot(user, cart));
                    Boolean cartPaymentSelectionDisabled = (Boolean) session.getAttribute(SessionName.CART_PAYMENT_SELECTION_DISABLED);
                    if (cartPaymentSelectionDisabled != null && cartPaymentSelectionDisabled) {
                        result.put(PAYMENT_JSON_KEY, loadUserPaymentMethods(user, request, customerModel.getPaymentMethods(), customerModel.getCustomerCredits()));
                    }
                    if (!StandingOrderHelper.isSO3StandingOrder(user)) {
                        result.put(
                                REDIRECT_URL_JSON_KEY,
                                RedirectService.defaultService().populateRedirectUrl(EXPRESS_CHECKOUT_VIEW_CART_PAGE_URL, WARNING_MESSAGE_LABEL,
                                        availabilityService.selectWarningType(user)));
                    }
                    result.put(BILLING_REFERENCE_INFO_JSON_KEY, CartDataService.defaultService().populateBillingReferenceInfo(session, user));
                    result.put(SUB_TOTAL_BOX_JSON_KEY, CartDataService.defaultService().loadCartDataSubTotalBox(request, user));
                    result.put(FORM_META_DATA_JSON_KEY, FormMetaDataService.defaultService().populateFormMetaData(user));
                }
                break;
            case ADD_PAYMENT_METHOD:
                //$FALL-THROUGH$
            case EDIT_PAYMENT_METHOD:
                //$FALL-THROUGH$
            case DELETE_PAYMENT_METHOD:
                //$FALL-THROUGH$
                // case MASTERPASS_SELECT_PAYMENTMETHOD: // Express Checkout
            case MASTERPASS_PICK_MP_PAYMENTMETHOD:
                //$FALL-THROUGH$
            case SELECT_PAYMENT_METHOD:
                result.put(PAYMENT_JSON_KEY, loadUserPaymentMethods(user, request));
                result.put(SUB_TOTAL_BOX_JSON_KEY, CartDataService.defaultService().loadCartDataSubTotalBox(request, user));
                break;
            case SELECT_DELIVERY_TIMESLOT: {
                result.put(TIMESLOT_JSON_KEY, TimeslotService.defaultService().loadCartTimeslot(user, cart));// user.getShoppingCart()
                if (CheckoutService.defaultService().checkAtpCheckEligibleByRestrictions(restriction) && !StandingOrderHelper.isSO3StandingOrder(user)) {
                    result.put(ATP_FAILURE_JSON_KEY, CheckoutService.defaultService().applyAtpCheck(user));
                }
                CartData loadCartData = CartDataService.defaultService().loadCartData(request, user);
                result.put(CART_DATA_JSON_KEY, SoyTemplateEngine.convertToMap(loadCartData));
            }
                break;
            case REMOVE_ALCOHOL_FROM_CART:
                //$FALL-THROUGH$
            case REMOVE_WINE_AND_SPIRITS_FROM_CART:
                //$FALL-THROUGH$
            case REMOVE_EBT_INELIGIBLE_ITEMS_FROM_CART: {
                if (CheckoutService.defaultService().checkAtpCheckEligibleByRestrictions(restriction)) {
                    result.put(ATP_FAILURE_JSON_KEY, CheckoutService.defaultService().applyAtpCheck(user));
                }
                String orderMinimumType = AvailabilityService.defaultService().selectAlcoholicOrderMinimumType(user);
                result.put(REDIRECT_URL_JSON_KEY, RedirectService.defaultService()
                        .populateRedirectUrl(EXPRESS_CHECKOUT_VIEW_CART_PAGE_URL, WARNING_MESSAGE_LABEL, orderMinimumType));
                CartData loadCartData = CartDataService.defaultService().loadCartData(request, user);
                result.put(CART_DATA_JSON_KEY, SoyTemplateEngine.convertToMap(loadCartData));
                break;
            }
            case APPLY_AGE_VERIFICATION_FOR_ALCOHOL_IN_CART: {
                if (CheckoutService.defaultService().checkAtpCheckEligibleByRestrictions(restriction)) {
                    result.put(ATP_FAILURE_JSON_KEY, CheckoutService.defaultService().applyAtpCheck(user));
                }
                String orderMinimumType = AvailabilityService.defaultService().selectAlcoholicOrderMinimumType(user);
                result.put(REDIRECT_URL_JSON_KEY, RedirectService.defaultService()
                        .populateRedirectUrl(EXPRESS_CHECKOUT_VIEW_CART_PAGE_URL, WARNING_MESSAGE_LABEL, orderMinimumType));
                break;
            }
            case ATP_ADJUST: {
                if (CheckoutService.defaultService().checkAtpCheckEligibleByRestrictions(restriction)) {
                    result.put(ATP_FAILURE_JSON_KEY, CheckoutService.defaultService().applyAtpCheck(user));
                }
                result.put(
                        REDIRECT_URL_JSON_KEY,
                        RedirectService.defaultService().populateRedirectUrl(EXPRESS_CHECKOUT_VIEW_CART_PAGE_URL, WARNING_MESSAGE_LABEL,
                                availabilityService.selectWarningType(user)));
                CartData loadCartData = CartDataService.defaultService().loadCartData(request, user);
                result.put(CART_DATA_JSON_KEY, SoyTemplateEngine.convertToMap(loadCartData));
                break;
            }
            case APPLY_PROMOTION:
                //$FALL-THROUGH$
            case REMOVE_PROMOTION: {
                CartData cartData = CartDataService.defaultService().loadCartData(request, user);
                result.put(CART_DATA_JSON_KEY, SoyTemplateEngine.convertToMap(cartData));
                break;
            }
            case APPLY_GIFT_CARD:
                //$FALL-THROUGH$
            case REMOVE_GIFT_CARD: {
                CartData cartData = CartDataService.defaultService().loadCartData(request, user);
                result.put(CART_DATA_JSON_KEY, SoyTemplateEngine.convertToMap(cartData));
                
                result.put(PAYMENT_JSON_KEY, loadUserPaymentMethods(user, request));
                result.put(SUB_TOTAL_BOX_JSON_KEY, CartDataService.defaultService().loadCartDataSubTotalBox(request, user));
                break;
            }

            case APPLY_CSR_METHOD: {
                result.put(SUB_TOTAL_BOX_JSON_KEY, CartDataService.defaultService().loadCartDataSubTotalBox(request, user));
                break;
            }
            case APPLY_ETIP: {

                if (StandingOrderHelper.isSO3StandingOrder(user)) {
                    CartData cartData = CartDataService.defaultService().updateCartData(request, user);
                    cartData.setTipAppliedTick(true);
                    result.put(CART_DATA_JSON_KEY, SoyTemplateEngine.convertToMap(cartData));
                    result.put(SUB_TOTAL_BOX_JSON_KEY, CartDataService.defaultService().loadCartDataSubTotalBox(request, user));
                } else {
                    CartData loadCartData = CartDataService.defaultService().loadCartData(request, user);
                    loadCartData.setTipAppliedTick(true);
                    result.put(CART_DATA_JSON_KEY, SoyTemplateEngine.convertToMap(loadCartData));
                    result.put(SUB_TOTAL_BOX_JSON_KEY, CartDataService.defaultService().loadCartDataSubTotalBox(request, user));
                }

                break;
            }
            default:
                break;
        }
        return result;
    }
    
	public FormLocationData loadAddress(final FDUserI user, final HttpSession session)
			throws FDResourceException, JspException, RedirectToPage {
		
		return loadAddress(user, session, populateCartDataFromParentOrder(user, false), null);
	}

    public FormLocationData loadAddress(final FDUserI user, final HttpSession session, final FDCartI cart) throws FDResourceException, JspException, RedirectToPage {  	
    	return loadAddress(user,session,cart,null);
    }
    
    public FormLocationData loadAddress(final FDUserI user, final HttpSession session, final FDCartI cart, Collection<ErpAddressModel> shippingAddresses) throws FDResourceException, JspException, RedirectToPage {
        List<LocationData> deliveryAddresses = deliveryAddressService.loadAddress(cart, user, session, shippingAddresses);
        FormLocationData formLocation = new FormLocationData();
        formLocation.setAddresses(deliveryAddresses);
        formLocation.setSelected(getSelectedAddressId(deliveryAddresses));

        if (StandingOrderHelper.isSO3StandingOrder(user)) {
        	FDStandingOrder currentSO = user.getCurrentStandingOrder();
    		formLocation.setSelected(null);	
    		//checks valid corporate address
        	List<LocationData> validCoAddress = formLocation.getAddresses();
        	for( LocationData address: validCoAddress){
        		if(address.getId().equals(currentSO.getAddressId())){
        			formLocation.setSelected(currentSO.getAddressId());
        			break;
        		}
        	}if(null == formLocation.getSelected() && null != currentSO.getAddressId()){												//APPBUG-5367
        		LOGGER.debug("addressID: "+currentSO.getAddressId()+" is no more in system, updating now at template level");
        		StandingOrderHelper.evaluteSoAddressId(session, user, currentSO.getAddressId());
        	}
        }
        return formLocation;
    }

    public FormLocationData loadAddressById(final FDUserI user, final String addressId) throws FDResourceException {
        List<LocationData> deliveryAddresses = deliveryAddressService.loadDeliveryAddressById(user, addressId);
        FormLocationData formLocation = new FormLocationData();
        formLocation.setAddresses(deliveryAddresses);
        formLocation.setSelected(getSelectedAddressId(deliveryAddresses));
        return formLocation;
    }

    public LocationData selectedLocationData(FormLocationData formLocation) {
        LocationData address = null;
        String selectedId = formLocation.getSelected();
        if (selectedId == null && !formLocation.getAddresses().isEmpty()) {
            address = formLocation.getAddresses().get(0);
        } else {
            for (LocationData location : formLocation.getAddresses()) {
                if (location.getId().equals(selectedId)) {
                    address = location;
                    break;
                }
            }
        }
        return address;
    }

    public SinglePageCheckoutSuccessData loadSuccess(final String requestURI, final FDUserI user, String orderId, HttpSession session, boolean isSO3Activate) throws FDResourceException, IOException,
            TemplateException {
        SinglePageCheckoutSuccessData result = new SinglePageCheckoutSuccessData();
        
        FDOrderI order = isSO3Activate?new FDStandingOrderAdapter(user.getSoTemplateCart(),user.getCurrentStandingOrder()):loadOrder(orderId, user);
       
        result.setDrawer(DrawerService.defaultService().loadDrawer(user, false));
        result.setAddress(loadCartAddress(order, user));
        result.setPayment(loadCartPayment(order, user));
        result.setTimeslot(timeslotService.loadCartTimeslot(user, order));
        result.setSuccessPageData(loadSuccessPageData(order, requestURI, user));
        result.setTextMessageAlertData(loadTextMessageAlertData(user));
        result.setSemPixelData(SemPixelService.defaultService().populateSemPixelMediaInfo(user, session, order));
       
        if (user.getIdentity() != null) {
            result.setGoGreenShow("I".equalsIgnoreCase(GoGreenService.defaultService().loadGoGreenOption(user)) ? true : false);
        } else {
            result.setGoGreenShow(false);
        }

        return result;
    }

    private FDCartI populateCartDataFromParentOrder(final FDUserI user, boolean dlvPassCart) throws FDResourceException {
        FDCartI cart = null;
        if (StandingOrderHelper.isSO3StandingOrder(user)) {
            cart = user.getSoTemplateCart();
        } else if (user.getMasqueradeContext() != null && user.getMasqueradeContext().isAddOnOrderEnabled()) {
        	LOGGER.info("populateCartDataFromParentOrder "+user.getMasqueradeContext().getParentOrderId());
            cart = loadOrder(user.getMasqueradeContext().getParentOrderId(), user);
            String addressId = FDCustomerManager.getParentOrderAddressId(user.getMasqueradeContext().getParentOrderId());
            LOGGER.info("address id in populateCartDataFromParentOrder "+ addressId);
            ErpAddressModel deliveryAddress = cart.getDeliveryAddress();
            deliveryAddress.setId(NVL.apply(addressId, "addOnAddress"));
            user.getShoppingCart().setDeliveryAddress(deliveryAddress);
            // user.getShoppingCart().setDeliveryReservation(cart.getDeliveryReservation());
        } else {
            //cart = user.getShoppingCart();
        	cart = UserUtil.getCart(user, "", dlvPassCart);
        }
        return cart;
    }

    private String getSelectedAddressId(final List<LocationData> deliveryAddresses) {
        String selectedAddressId = null;
        for (LocationData deliveryAddress : deliveryAddresses) {
            if (deliveryAddress.isSelected()) {
                selectedAddressId = deliveryAddress.getId();
                break;
            }
        }
        return selectedAddressId;
    }

    private String getSelectedPaymentId(final List<PaymentData> payments) {
        String selectedPaymentId = null;
        for (PaymentData payment : payments) {
            if (payment.isSelected()) {
                selectedPaymentId = payment.getId();
                break;
            }
        }
        return selectedPaymentId;
    }

    public List<ValidationError> handleModifyCartPreSelections(FDUserI user, HttpServletRequest request) throws FDResourceException, JspException, RedirectToPage {
        FDCartModel cart = StandingOrderHelper.isSO3StandingOrder(user) ? user.getSoTemplateCart() : user.getShoppingCart();

        List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        HttpSession session = request.getSession();
        if (cart instanceof FDModifyCartModel) {
            Boolean modifyCartPreSelectionCompleted = (Boolean) session.getAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED);
            if (modifyCartPreSelectionCompleted == null) {
                ActionResult actionResult = new ActionResult();
                String addressId = cart.getDeliveryReservation().getAddressId();

                ErpAddressModel deliveryAddress = FDCustomerManager.getAddress(user.getIdentity(), addressId);

				//if the address is a depot if has to call the setDeliveryAddress to get the new zoneinfo.APPDEV-5170
                FDDeliveryDepotModel depot = FDDeliveryManager.getInstance().getDepotByLocationId(addressId);
        
                if (deliveryAddress != null || depot!=null) {
                    DeliveryAddressManipulator.performSetDeliveryAddress(session, user, addressId, null, null, PageAction.SELECT_DELIVERY_ADDRESS_METHOD.actionName, true,
                            actionResult, null, null, null, null, null, null);
                }
                try {
                    if (cart.getDeliveryReservation() != null && deliveryAddress != null) {
                        TimeslotService.defaultService().reserveDeliveryTimeslot(cart.getDeliveryReservation().getTimeslotId(), session);
                    }
                } catch (ReservationException e) {
                    LOGGER.error(MessageFormat.format("Failed to reserve timeslot for timeslot id[{0}]:", cart.getDeliveryReservation().getTimeslotId()), e);
                    throw new FDResourceException(e);
                }
                String billingReference = cart.getPaymentMethod().getBillingRef();
                session.setAttribute(SessionName.PAYMENT_BILLING_REFERENCE, billingReference);
                String paymentId = cart.getPaymentMethod().getPK().getId();
                ErpPaymentMethodI paymentMethod = PaymentMethodUtil.getPaymentMethod(paymentId, user.getPaymentMethods());
                if (paymentMethod != null) {
                    PaymentMethodManipulator.setPaymentMethod(paymentId, null, request, session, actionResult, PageAction.SELECT_PAYMENT_METHOD.actionName, "N", false);
                } else {
                    session.setAttribute(SessionName.CART_PAYMENT_SELECTION_DISABLED, Boolean.TRUE);
                }
                for (ActionError error : actionResult.getErrors()) {
                    validationErrors.add(new ValidationError(error));
                }
                if (validationErrors.isEmpty()) {
                    session.setAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED, true);
                }
            }
        } else {
            session.removeAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED);
        }
        return validationErrors;
    }

    /** based on step_3_choose.jsp */
    private void processGiftCards(FDUserI user, FormPaymentData formPaymentData, List customerCredits) {
        if (user.getGiftCardList() != null) {
            user.getShoppingCart().setSelectedGiftCards(user.getGiftCardList().getSelectedGiftcards());
            user.getDlvPassCart().setSelectedGiftCards(user.getGiftCardList().getSelectedGiftcards());
        }
        try {
            // [APPDEV-2149] SO template only checkout => no order, no dlv
            // timeslot, no giftcard magic
            final boolean isSOTMPL = EnumCheckoutMode.MODIFY_SO_TMPL.equals(user.getCheckoutMode());
            FDCartModel cart = user.getShoppingCart();

            /*
             * Apply Customer credit -- This is done here for knowing the final order amount before displaying the payment selection If Gift card is used on the order Also
             * calculate the 25% perishable buffer amount to decide if another mode of payment is needed.
             */
            if (!isSOTMPL) {
                FDCustomerCreditUtil.applyCustomerCredit(cart, user.getIdentity(), customerCredits);
            }

            double gcSelectedBalance = isSOTMPL ? 0 : user.getGiftcardBalance() - cart.getTotalAppliedGCAmount();
            double gcBufferAmount = 0;
            double ccBufferAmount = 0;
            double perishableBufferAmount = isSOTMPL ? 0 : FDCustomerManager.getPerishableBufferAmount(cart);
            double outStandingBalance = isSOTMPL ? 0 : FDCustomerManager.getOutStandingBalance(cart);

            if (!isSOTMPL && perishableBufferAmount > 0) {
                if (cart.getTotalAppliedGCAmount() > 0) {
                    if (outStandingBalance > 0) {
                        gcBufferAmount = gcSelectedBalance;
                        ccBufferAmount = perishableBufferAmount - gcSelectedBalance;
                    } else {
                        gcBufferAmount = perishableBufferAmount;
                    }
                } else {
                    ccBufferAmount = perishableBufferAmount;
                }
            }

            /* No additional payment type is needed, covered by Giftcards */
            if (!isSOTMPL && cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0 && outStandingBalance <= 0.0) {
                formPaymentData.setCoveredByGiftCard(true);

            } else if (cart.getSelectedGiftCards() != null && cart.getSelectedGiftCards().size() > 0 && gcBufferAmount > 0 && ccBufferAmount > 0) {
                formPaymentData.setBackupPaymentRequiredForGiftCard(true);
            }

        } catch (FDResourceException e) {
            LOGGER.error("processIfCoveredByGiftCard() failed because of FDResourceException", e);
        }

    }

    private FormLocationData loadCartAddress(FDCartI cart, final FDUserI user) throws FDResourceException {
        List<LocationData> cartDeliveryAddresses = deliveryAddressService.loadSuccessLocations(cart, user);

        FormLocationData formLocation = new FormLocationData();
        formLocation.setAddresses(cartDeliveryAddresses);
        formLocation.setSelected(getSelectedAddressId(cartDeliveryAddresses));
        return formLocation;
    }

    private FormPaymentData loadCartPayment(final FDCartI cart, final FDUserI user) throws FDResourceException {
        List<PaymentData> cartPaymentDatas = paymentService.loadCartPayment(cart, user);

        FormPaymentData formPaymentData = new FormPaymentData();
        updateEwalleTMPLogo(cartPaymentDatas);
        formPaymentData.setPayments(cartPaymentDatas);
        formPaymentData.setSelected(getSelectedPaymentId(cartPaymentDatas));

        return formPaymentData;
    }

    /**
     * @param paymentDataList
     */
    private void updateEwalleTMPLogo(List<PaymentData> paymentDataList) {
        int ewalletId = EnumEwalletType.getEnum("MP").getValue();
        for (PaymentData data : paymentDataList) {
            if (data.geteWalletID() != null && data.geteWalletID().equals("" + ewalletId)) {
                data.setMpLogoURL(FDStoreProperties.getMasterpassLogoURL());
            }
        }
    }

    private FDOrderI loadOrder(final String orderNumber, final FDUserI user) throws FDResourceException {
        FDOrderI order;
        if (user != null && user.getIdentity() != null) {
            order = FDCustomerManager.getOrder(user.getIdentity(), orderNumber);
        } else {
            order = FDCustomerManager.getOrder(orderNumber);
        }
        return order;
    }

    private SuccessPageData loadSuccessPageData(final FDOrderI order, final String requestURI, final FDUserI user) throws FDResourceException {
    	boolean freeTrialOptinBasedDPApplied = order.isChargeWaivedByDlvPass(EnumChargeType.DELIVERY) && null ==order.getDeliveryPassId();
    	
        SuccessPageData successPageData = new SuccessPageData();
        successPageData.setHeader(contentFactoryService.getExpressCheckoutReceiptHeader(user));
        successPageData.setRightBlock(contentFactoryService.getExpressCheckoutReceiptEditorial(user));
        successPageData.setOrderId(order.getErpSalesId());
        successPageData.setSoName(order.getStandingOrderName());
        successPageData.setSoOrderDate(order.getSODeliveryDate());
        successPageData.setOrderModifiable(CheckOrderStatusTag.isOrderModifiable(order));
        try {
			successPageData.setHasSettledOrder(user.getOrderHistory().getSettledOrderCount()>0);
		} catch (FDResourceException e) {
			e.printStackTrace();
		}
        successPageData.setDeliveryType(order.getDeliveryType().getCode());
        if(StandingOrderHelper.isSO3StandingOrder(user)){
        	populateSOActivationSuccess(successPageData,user);
        }
        //For Free Trial to show the message

        if (order.isDlvPassApplied() || freeTrialOptinBasedDPApplied) {
        	successPageData.setId(DELIVERY_CHARGE_ID);
        	successPageData.setText(DELIVERY_CHARGE_NAME);
            String deliveryPassAppliedMessage = DeliveryPassUtil.getDlvPassAppliedMessage(user);
            successPageData.setValue(deliveryPassAppliedMessage);
            
            if(freeTrialOptinBasedDPApplied) {
            	successPageData.setDeliveryPassTrialActivated(freeTrialOptinBasedDPApplied);
            }
        }
        successPageData.setReceipt(receiptService.populateReceiptData(order, requestURI, user));
        return successPageData;
    }

    /**
     * 
     * @param successPageData
     * @param user
     */
    private void populateSOActivationSuccess(SuccessPageData successPageData, FDUserI user) {
        FDStandingOrder so=user.getCurrentStandingOrder();
    	successPageData.setSoActivate(true);
    	successPageData.setSoId(so.getId());
    	successPageData.setSoFrequency(so.getFullFrequencyDescription());
    	successPageData.setSoDeliveryDay(so.getDeliveryDay());
    	successPageData.setSoEstimatedTotal(user.getSoTemplateCart().getTotal());
    	successPageData.setSoDeliveryTime(so.getNextDeliveryString() + " " +so.getDeliveryTime());
    	successPageData.setSoName(so.getCustomerListName());
	}

	private TextMessageAlertData loadTextMessageAlertData(final FDUserI user) throws FDResourceException, IOException, TemplateException {
        TextMessageAlertData textMessageAlertData = new TextMessageAlertData();
        textMessageAlertData.setHeader(contentFactoryService.getExpressCheckoutTextMessageAlertHeader(user));
        textMessageAlertData.setShow(textMessageAlertService.showTextMessageAlertPopup(user));
        textMessageAlertData.setMedia(textMessageAlertService.getTermsAndConditionsMedia());
        return textMessageAlertData;
    }

	private FormPaymentData loadUserPaymentMethods(FDUserI user, HttpServletRequest request) throws FDResourceException {
		return loadUserPaymentMethods(user,request,null, null);
	}
    public FormPaymentData loadUserPaymentMethods(FDUserI user, HttpServletRequest request, List<ErpPaymentMethodI> paymentMethods,List customerCredits) throws FDResourceException {

        FormPaymentData formPaymentData = new FormPaymentData();

        if (!StandingOrderHelper.isSO3StandingOrder(user)) {
            processGiftCards(user, formPaymentData, customerCredits);
        }
        // boolean isMPCard = false;

        if (formPaymentData.isCoveredByGiftCard() && !StandingOrderHelper.isSO3StandingOrder(user)) {
            paymentService.setNoPaymentMethod(user, request);
        } else {
        	 List<PaymentData> userPaymentMethods = paymentService.loadUserPaymentMethods(user, request, paymentMethods);
        	 boolean dlvPassCart = null !=request.getParameter("dlvPassCart") && "true".equalsIgnoreCase(request.getParameter("dlvPassCart")) ? true: false;
        	 FDCartModel cart=UserUtil.getCart(user, "", dlvPassCart);
        	//APPDEV-6765
             FDCartModel mCart = cart;
             if (mCart instanceof FDModifyCartModel && !StandingOrderHelper.isSO3StandingOrder(user)) {
                 FDModifyCartModel modifyCart = (FDModifyCartModel) mCart;
                 String orderId = modifyCart.getOriginalOrder().getErpSalesId();
                 try {
//                      FDOrderI order = FDCustomerManager.getOrder(orderId);
                     if(null !=modifyCart.getPaymentMethod() && null !=modifyCart.getPaymentMethod().getPK()){
                    	 String cartPaymentMethodPK= modifyCart.getPaymentMethod().getPK().getId();
                          for(PaymentData PM : userPaymentMethods){
                              if(PM.getId().equalsIgnoreCase(cartPaymentMethodPK) ){
                            	  formPaymentData.setSelected(cartPaymentMethodPK);
                                  PM.setSelected(true);
                              }else{
                                  PM.setSelected(false);
                              }
                           }
                          
                     }else{
                         FDOrderI order = FDCustomerManager.getOrder(orderId);
                         formPaymentData.setSelected(modifyCart.getOriginalOrder().getPaymentMethod().getPK().getId());
                     }
                 } catch (FDResourceException e) {
                     LOGGER.error("Error while retreiving order details order id" + orderId);
                 }
             } else {
				boolean readyToBreak = false;
				for (PaymentData data : userPaymentMethods) {
					if (data.isSelected()) {
						formPaymentData.setSelected(data.getId());
						if (readyToBreak) {
							break;
						} else {
							readyToBreak = true;
						}
					}
					if (data.isDefault()) {
						formPaymentData.setDefault(data.isDefault());
						if (readyToBreak) {
							break;
						} else {
							readyToBreak = true;
						}
					}
				}
			}
        	 formPaymentData.setPayments(userPaymentMethods);
            if (StandingOrderHelper.isSO3StandingOrder(user)) {
                userPaymentMethods = removeEWalletPaymentMethod(userPaymentMethods);
                
                FDStandingOrder currentSO = user.getCurrentStandingOrder();
                formPaymentData.setSelected(null);	
        		//checks valid payments												//COS17-45
                List<PaymentData> userPayments = formPaymentData.getPayments();
            	for( PaymentData payment: userPayments){
            		if(payment.getId().equals(currentSO.getPaymentMethodId())){
            			formPaymentData.setSelected(currentSO.getPaymentMethodId());
            			payment.setSelected(true);
            		}else{
            			payment.setSelected(false);
            		}
            	}if(null == formPaymentData.getSelected() && null != currentSO.getPaymentMethodId()){
            		LOGGER.debug("paymentID: "+currentSO.getPaymentMethodId()+" is no more in system, updating now at template level");
            		StandingOrderHelper.evaluteSOPaymentId(request.getSession(), user, currentSO.getPaymentMethodId());
            	}
            	
                formPaymentData.setPayments(userPaymentMethods);
               // user.getCurrentStandingOrder().setPaymentMethodId(formPaymentData.getSelected());
                formPaymentData.setMpEwalletStatus(false); // Masterpass wallet will be disable for Standing Orders
                formPaymentData.setPpEwalletStatus(false); // PayPal wallet will be disable for Standing Orders

            } else {
                formPaymentData.setMpEwalletStatus(getEwalletStatusWithMasquerade(user, EnumEwalletType.MP.getName()));
                formPaymentData.setPpEwalletStatus(getEwalletStatusWithMasquerade(user, EnumEwalletType.PP.getName()));
            }
            FDCardCount(userPaymentMethods);
            formPaymentData.setMpButtonImgURL(FDStoreProperties.getMasterpassBtnImgURL());

            if (!getEwalletStatus(EnumEwalletType.PP.getName())) {
                userPaymentMethods = removePPWallet(userPaymentMethods);
                formPaymentData.setPayments(userPaymentMethods);
            }
            formPaymentData.setPpEwalletPaired(isPayPalPaired(userPaymentMethods));
            // Express Checkout Code
            /*
             * if(formPaymentData.isMpEwalletStatus()){ ErpCustEWalletModel custEWalletModel = getCustomerEWallet(user); // Check MPEWallet Paired or not for (PaymentData data :
             * userPaymentMethods) { if (data.geteWalletID()!=null && data.geteWalletID().equals("1")) { formPaymentData.setMpEWalletID(data.geteWalletID()); isMPCard = true;
             * break; } } if(custEWalletModel != null && !isMPCard){ if( custEWalletModel.getLongAccessToken() != null && custEWalletModel.getLongAccessToken().length()>0){
             * formPaymentData.setMpCardPaired("Yes"); } } }
             */
        }

        return formPaymentData;
    }

    private boolean isPayPalPaired(List<PaymentData> userPaymentMethod) {
        if (userPaymentMethod != null && !userPaymentMethod.isEmpty()) {
            for (PaymentData paymentData : userPaymentMethod) {
                if (paymentData.geteWalletID() != null && paymentData.geteWalletID().equals("" + EnumEwalletType.PP.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removed PayPal ewallet data if the PayPal wallet status is disabled.
     * 
     * @param userPaymentMethods
     * @param eWalletStatus
     * @return
     */
    private List<PaymentData> removePPWallet(List<PaymentData> userPaymentMethods) {
        List<PaymentData> newPaymentData = new ArrayList<PaymentData>();
        if (userPaymentMethods != null && !userPaymentMethods.isEmpty()) {
            for (PaymentData paymentData : userPaymentMethods) {
                if (paymentData.geteWalletID() != null && paymentData.geteWalletID().equals("" + EnumEwalletType.PP.getValue())) {
                    continue;
                } else {
                    newPaymentData.add(paymentData);
                }
            }
            return newPaymentData;
        } else {
            return userPaymentMethods;
        }
    }

    /**
     * @param userPaymentMethods
     */
    private void FDCardCount(List<PaymentData> userPaymentMethods) {
        int count = 0;
        if (userPaymentMethods != null && !userPaymentMethods.isEmpty()) {
            for (PaymentData paymentData : userPaymentMethods) { // Get FD card Count
                if (paymentData.geteWalletID() == null) {
                    count++;
                }
                if (count > 2) {
                    break;
                }
            }
            if (count < 2) {
                for (PaymentData paymentData : userPaymentMethods) {
                    paymentData.setCanDelete(false);
                }
            }
        }
    }

    /**
     * @param eWalletType
     * @return
     */
    private boolean getEwalletStatusWithMasquerade(FDUserI user, String eWalletType) {
        return FDCustomerManager.getEwalletStatusByType(eWalletType) && user.getMasqueradeContext() == null;
    }

    private boolean getEwalletStatus(String eWalletType) {
        return FDCustomerManager.getEwalletStatusByType(eWalletType);
    }

    /**
     * Remove Ewallet Payment Data from list.
     * 
     * @param userPaymentMethods
     * @param eWalletStatus
     * @return
     */
    private List<PaymentData> removeEWalletPaymentMethod(List<PaymentData> userPaymentMethods) {
        List<PaymentData> newPaymentData = new ArrayList<PaymentData>();
        if (userPaymentMethods != null && !userPaymentMethods.isEmpty()) {
            for (PaymentData paymentData : userPaymentMethods) {
                if (paymentData.geteWalletID() != null) {
                    continue;
                } else {
                    newPaymentData.add(paymentData);
                }
            }
            return newPaymentData;
        } else {
            return userPaymentMethods;
        }
    }
    /*
     * private ErpCustEWalletModel getCustomerEWallet(FDUserI user) throws FDResourceException{ return
     * FDCustomerManager.findLongAccessTokenByCustID(user.getFDCustomer().getErpCustomerPK(),MASTERPASS_EWALLET_TYPE); }
     */
}
