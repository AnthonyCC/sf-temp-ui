package com.freshdirect.mobileapi.model;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDCompositeAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDMuniAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.util.RestrictionUtil;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ActionWarning;
import com.freshdirect.mobileapi.controller.data.AtpErrorData;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.SubmitOrderExResult;
import com.freshdirect.mobileapi.controller.data.request.DeliveryAddressRequest;
import com.freshdirect.mobileapi.controller.data.request.PaymentMethodRequest;
import com.freshdirect.mobileapi.controller.data.response.AtpError;
import com.freshdirect.mobileapi.controller.data.response.AtpError.ItemAvailabilityError;
import com.freshdirect.mobileapi.controller.data.response.Order;
import com.freshdirect.mobileapi.controller.data.response.OrderReceipt;
import com.freshdirect.mobileapi.controller.data.response.SpecialRestrictedItemsError;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.data.Unavailability;
import com.freshdirect.mobileapi.model.tagwrapper.AdjustAvailabilityTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.AgeVerificationControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.CheckoutControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.DlvPassAvailabilityControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.OrderHistoryInfoTagWrapper;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.service.TimeslotService;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class Checkout {

    private static final Category LOGGER = LoggerFactory.getInstance(Checkout.class);

    private final SessionUser sessionUser;

    public Checkout(SessionUser sessionUser) {
        this.sessionUser = sessionUser;
    }

    public ResultBundle checkDeliveryPassAvailability() throws FDException {
        DlvPassAvailabilityControllerTagWrapper tagWrapper = new DlvPassAvailabilityControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.checkDeliveryPassAvailability();
        return result;
    }

    public ResultBundle submitOrder(boolean dlvPassCart) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.submitOrder(dlvPassCart);

        if (result.getActionResult() == null) {
            ActionResult actionResult = new ActionResult();
            actionResult.setError("There was an unexpected error while processing your submitting order.");
            result.setActionResult(actionResult);
        }

        return result;
    }

    public ResultBundle setPaymentMethod(String paymentMethodId, String billingReference, String isAccountLevel, boolean dlvPassCart) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        return tagWrapper.setPaymentMethod(paymentMethodId, billingReference, isAccountLevel, dlvPassCart);
    }

    public ResultBundle setPaymentMethodEx(String paymentMethodId, String billingReference, String isAccountLevel, boolean dlvPassCart) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.setPaymentMethod(paymentMethodId, billingReference, isAccountLevel, dlvPassCart);
        // if we have orderMinimum not met we are going to remove it and pass the result
        boolean isCustomAdded = false;
        ActionResult customActionResult = new ActionResult();
        if (result.getActionResult().isFailure()) {
            Collection<ActionError> errors = result.getActionResult().getErrors();  
            for (ActionError error : errors) {  
                if ("order_minimum".equals(error.getType())) {  
                    continue;   
                } else {    
                    customActionResult.addError(error);
                }   
            }   
            Collection<ActionWarning> warnings = result.getActionResult().getWarnings();    
            for (ActionWarning warning : warnings) {
                customActionResult.addWarning(warning);
            }   
            isCustomAdded = true;   
        }   
        if (isCustomAdded) {
            result.setActionResult(customActionResult);
        }
        return result;
    }

    public ResultBundle addPaymentMethod(PaymentMethodRequest paymentMethod, Object attempt) throws FDException {   
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        tagWrapper.addSessionValue(SessionName.PAYMENT_ATTEMPT, attempt);
        return tagWrapper.addPaymentMethod(paymentMethod);
    }

    public ResultBundle addPaymentMethodEx(PaymentMethodRequest paymentMethod, Object attempt) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        tagWrapper.addSessionValue(SessionName.PAYMENT_ATTEMPT, attempt);
        ResultBundle result = tagWrapper.addPaymentMethodEx(paymentMethod);
        // Creating new ActionResult with deliveryMinimum and age verification Errors removed if any.
        boolean isCustomAdded = false;
        ActionResult customActionResult = new ActionResult();
        if (result.getActionResult().isFailure()) {
            Collection<ActionError> errors = result.getActionResult().getErrors();
            for (ActionError error : errors) {
                if ("order_minimum".equals(error.getType()) || "ERR_AGE_VERIFICATION".equals(error.getType())) {
                    continue;
                } else {
                    customActionResult.addError(error);
                }
            }
            Collection<ActionWarning> warnings = result.getActionResult().getWarnings();
            for (ActionWarning warning : warnings) {
                customActionResult.addWarning(warning);
            }
            isCustomAdded = true;
        }
        if (isCustomAdded) {
            result.setActionResult(customActionResult);
        }
        return result;
    }

    public ResultBundle addAndSetPaymentMethod(PaymentMethodRequest paymentMethod, Object attempt) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        tagWrapper.addSessionValue(SessionName.PAYMENT_ATTEMPT, attempt);
        ResultBundle result = tagWrapper.addAndSetPaymentMethod(paymentMethod);
        return result;
    }

    public ResultBundle editPaymentMethod(PaymentMethodRequest paymentMethod, Object attempt) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        tagWrapper.addSessionValue(SessionName.PAYMENT_ATTEMPT, attempt);
        ResultBundle result = tagWrapper.editPaymentMethod(paymentMethod);
        return result;
    }

    public ResultBundle savePaymentMethod(PaymentMethodRequest paymentMethod) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.savePaymentMethod(paymentMethod);
        return result;
    }

    public ResultBundle deletePaymentMethod(String paymentMethodId, boolean dlvPassCart) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.deletePaymentMethod(paymentMethodId, dlvPassCart);
        return result;
    }

    public ResultBundle deletePaymentMethodEx(String paymentMethodId, boolean dlvPassCart) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.deletePaymentMethodEx(paymentMethodId, dlvPassCart);
        // Creating new ActionResult with deliveryMinimum and age verification Errors removed if any.
        boolean isCustomAdded = false;
        ActionResult customActionResult = new ActionResult();
        if (result.getActionResult().isFailure()) {
            Collection<ActionError> errors = result.getActionResult().getErrors();
            for (ActionError error : errors) {
                if ("order_minimum".equals(error.getType()) || "ERR_AGE_VERIFICATION".equals(error.getType())) {
                    continue;
                } else {
                    customActionResult.addError(error);
                }
            }
            Collection<ActionWarning> warnings = result.getActionResult().getWarnings();
            for (ActionWarning warning : warnings) {
                customActionResult.addWarning(warning);
            }
            isCustomAdded = true;
        }
        if (isCustomAdded) {
            result.setActionResult(customActionResult);
        }
        return result;
    }

    public ResultBundle addAndSetDeliveryAddress(DeliveryAddressRequest deliveryAddress) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.addAndSetDeliveryAddress(deliveryAddress);
        boolean isCustomAdded = false;
        ActionResult customActionResult = new ActionResult();
        if (result.getActionResult().isFailure()) {
            Collection<ActionError> errors = result.getActionResult().getErrors();
            for (ActionError error : errors) {
                if ("order_minimum".equals(error.getType()) || "ERR_AGE_VERIFICATION".equals(error.getType())) {
                    continue;
                } else {
                    customActionResult.addError(error);
                }
            }
            Collection<ActionWarning> warnings = result.getActionResult().getWarnings();
            for (ActionWarning warning : warnings) {
                customActionResult.addWarning(warning);
            }
            isCustomAdded = true;
        }
        if (isCustomAdded) {
            result.setActionResult(customActionResult);
        }
        return result;
    }

    public ResultBundle deleteDeliveryAddress(String deleteShipToAddressId) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.deleteDeliveryAddress(deleteShipToAddressId);
        return result;
    }

    public ResultBundle deleteDeliveryAddressEx(String deleteShipToAddressId) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.deleteDeliveryAddressEx(deleteShipToAddressId);
        // Creating new ActionResult with deliveryMinimum and age verification Errors removed if any.
        boolean isCustomAdded = false;
        ActionResult customActionResult = new ActionResult();
        if (result.getActionResult().isFailure()) {
            Collection<ActionError> errors = result.getActionResult().getErrors();
            for (ActionError error : errors) {
                if ("order_minimum".equals(error.getType()) || "ERR_AGE_VERIFICATION".equals(error.getType())) {
                    continue;
                } else {
                    customActionResult.addError(error);
                }
            }
            Collection<ActionWarning> warnings = result.getActionResult().getWarnings();
            for (ActionWarning warning : warnings) {
                customActionResult.addWarning(warning);
            }
            isCustomAdded = true;
        }
        if (isCustomAdded) {
            result.setActionResult(customActionResult);
        }
        return result;
    }

    public String getPreselectedPaymethodMethodId(Cart cart) {
        String preselectedPaymethodMethodId = null;

        if ((null != cart.getCart().getPaymentMethod())) {
            preselectedPaymethodMethodId = ((ErpPaymentMethodModel) cart.getCart().getPaymentMethod()).getPK().getId();
        }
        if (preselectedPaymethodMethodId == null) {
            try {
                preselectedPaymethodMethodId = FDCustomerManager.getDefaultPaymentMethodPK(sessionUser.getFDSessionUser().getIdentity());
            } catch (FDResourceException e) {
                LOGGER.warn("FDResourceException while trying to find user's default payment method", e);
            }
        }
        return preselectedPaymethodMethodId;
    }

    public String getPreselectedTimeslotId(TimeSlotCalculationResult timeSlotResult, SessionUser user) throws FDResourceException {
        Timeslot foundTimeslot = null;
        String selectedTimeslotId = null;
        String deliveryTimeslotId = null;
        String reservationTimeslotId = null;
        // We can assume that we have shopping cart at this point
        FDCartI cart = sessionUser.getShoppingCart().getCart();
        if (cart.getDeliveryReservation() != null) {
            deliveryTimeslotId = cart.getDeliveryReservation().getTimeslotId();
        }
        if (sessionUser.getReservationTimeslot() != null) {
            reservationTimeslotId = sessionUser.getReservationTimeslot().getTimeslotId();
        }

        foundTimeslot = timeSlotResult.findTimeslotById(deliveryTimeslotId, user);
        if (foundTimeslot == null) {
            foundTimeslot = timeSlotResult.findTimeslotById(reservationTimeslotId, user);
        }
        if (foundTimeslot != null) {
            selectedTimeslotId = foundTimeslot.getTimeslotId();
        }

        return selectedTimeslotId;
    }

    public String getPreselectedDeliveryAddressId() throws FDResourceException {
        String addressId = null;

        Cart cart = sessionUser.getShoppingCart();

        if (cart != null && cart.isModifyCart()) {
            FDCartModel cartModel = cart.getCart();

            if (cartModel == null)
                return sessionUser.getDefaultShipToAddress();

            addressId = cartModel.getDeliveryReservation().getAddressId();
        } else {
            addressId = sessionUser.getDefaultShipToAddress();
            if (addressId == null) {
                addressId = sessionUser.getReservationAddressId();
            }
        }
        return addressId;
    }

    public String getSelectedDeliveryAddressId() throws FDResourceException {
        return sessionUser.getDefaultShipToAddress();
    }

    /**
     * @see com.freshdirect.webapp.taglib.fdstore.CheckoutControllerTag DUP: duplicated the age verification logic on the CheckoutControllerTag class. It was also updated to remove
     *      a reference about a page/HttpServletRequest. We also removed the logic about "app" == "CALLCENTER" check as that should controlled by the controller not model.
     *
     * @throws FDResourceException
     */
    public boolean isAgeVerificationNeeded() throws FDResourceException {
        FDCartModel cart = this.sessionUser.getShoppingCart().getCart();
        return (cart.containsAlcohol() && !cart.isAgeVerified());
    }

    /**
     * TODO: What if this method is called before delivery address/slot is set. Probably need to throw an exception.
     * 
     * @return
     * @throws FDException
     */
    public boolean isDuplicateOrder() throws FDException {
        // Perform duplicate check
        OrderHistoryInfoTagWrapper orderHistoryInfoTagWrapper = new OrderHistoryInfoTagWrapper(this.sessionUser);
        List<FDOrderInfoI> orderInfos = orderHistoryInfoTagWrapper.getOrderHistoryInfo();

        /*
         * DUP: /checkout/step_3_choose.jsp
         * 
         * @see /checkout/step_3_choose.jsp
         *
         * // redirect to Duplicate Order warning page if has another order for the same day String ignoreSaleId = null; if (cart instanceof FDModifyCartModel) { ignoreSaleId =
         * ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId(); }
         * 
         * Date currentDlvStart = DateUtil.truncate(cart.getDeliveryReservation().getStartTime());
         * 
         * for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) { FDOrderInfoI oi = (FDOrderInfoI) hIter.next(); if (!(oi.getErpSalesId().equals(ignoreSaleId)) &&
         * oi.isPending() && currentDlvStart.equals(DateUtil.truncate(oi.getDeliveryStartTime()))) {
         * response.sendRedirect(response.encodeRedirectURL("/checkout/step_2_duplicate.jsp?successPage=/checkout/step_3_choose.jsp")); return; } }
         */
        FDCartModel cart = getFDUser().getShoppingCart();
        String ignoreSaleId = null;
        boolean isDuplicateOrder = false;
        if (cart instanceof FDModifyCartModel) {
            ignoreSaleId = ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
        }
        Date currentDlvStart = DateUtil.truncate(cart.getDeliveryReservation().getStartTime());
        for (Iterator<FDOrderInfoI> hIter = orderInfos.iterator(); hIter.hasNext();) {
            FDOrderInfoI oi = hIter.next();
            if (!(oi.getErpSalesId().equals(ignoreSaleId)) && oi.isPending() && currentDlvStart.equals(DateUtil.truncate(oi.getDeliveryStartTime()))) {
                isDuplicateOrder = true;
            }
        }
        return isDuplicateOrder;
    }

    public ResultBundle setCheckoutDeliveryAddress(String id, DeliveryAddressType type) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.setCheckoutDeliveryAddress(this.sessionUser, id, type);
        if ((result.getActionResult().isSuccess()) && sessionUser.getShoppingCart().isAgeVerified()) {
            AgeVerificationControllerTagWrapper alcoholAddressCheckWrapper = new AgeVerificationControllerTagWrapper(this.sessionUser);
            result = alcoholAddressCheckWrapper.verifyAddress(id, type);
        }
        return result;
    }

    /**
     * This sets the address for FDX by passes the minimum delivery and age restriction.
     * 
     * @param id
     * @param type
     * @return
     * @throws FDException
     */
    public ResultBundle setCheckoutDeliveryAddressEx(String id, DeliveryAddressType type) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.setCheckoutDeliveryAddress(this.sessionUser, id, type);
        // Creating new ActionResult with deliveryMinimum and age verification Errors removed if any.
        boolean isCustomAdded = false;
        ActionResult customActionResult = new ActionResult();
        if (result.getActionResult().isFailure()) {
            Collection<ActionError> errors = result.getActionResult().getErrors();
            for (ActionError error : errors) {
                if ("order_minimum".equals(error.getType()) || "ERR_AGE_VERIFICATION".equals(error.getType())) {
                    continue;
                } else {
                    customActionResult.addError(error);
                }
            }
            Collection<ActionWarning> warnings = result.getActionResult().getWarnings();
            for (ActionWarning warning : warnings) {
                customActionResult.addWarning(warning);
            }
            isCustomAdded = true;
        }
        if (isCustomAdded) {
            result.setActionResult(customActionResult);
        }
        return result;
    }

    public ResultBundle setCheckoutOrderMobileNumberEx(String orderMobileNumber) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = null;
        result = tagWrapper.setCheckoutOrderMobileNumber(this.sessionUser, orderMobileNumber);
        // Creating new ActionResult with deliveryMinimum and age verification Errors removed if any.
        boolean isCustomAdded = false;
        ActionResult customActionResult = new ActionResult();
        if (result.getActionResult().isFailure()) {
            Collection<ActionError> errors = result.getActionResult().getErrors();
            for (ActionError error : errors) {
                if ("order_minimum".equals(error.getType()) || "ERR_AGE_VERIFICATION".equals(error.getType())) {
                    continue;
                } else {
                    customActionResult.addError(error);
                }
            }
            Collection<ActionWarning> warnings = result.getActionResult().getWarnings();
            for (ActionWarning warning : warnings) {
                customActionResult.addWarning(warning);
            }
            isCustomAdded = true;
        }
        if (isCustomAdded) {
            result.setActionResult(customActionResult);
        }
        return result;
    }

    private FDUser getFDUser() {
        return sessionUser.getFDSessionUser().getUser();
    }

    public Order getCurrentOrderDetails(EnumCouponContext ctx, boolean dlvPassCart) throws FDException {
    	if(!dlvPassCart){
    		return sessionUser.getShoppingCart().getCurrentOrderDetails(sessionUser, ctx, dlvPassCart);
    	}else{
    		return sessionUser.getDlvPassCart().getCurrentOrderDetails(sessionUser, ctx, dlvPassCart);
    	}
    }

    public OrderReceipt getOrderReceipt(String orderNumber, boolean dlvPassCart) throws FDException, IllegalAccessException, InvocationTargetException {
        Order order = getCurrentOrderDetails(EnumCouponContext.VIEWORDER, dlvPassCart);
        OrderReceipt orderReceipt = new OrderReceipt();
        BeanUtils.copyProperties(orderReceipt, order);
        orderReceipt.setOrderNumber(orderNumber);
        return orderReceipt;
    }

    /**
     * TODO: Model should not return request data class. Unnecessary coupling of model and view.
     * 
     * @return
     */
    public Message getAtpErrorDetail() {
        /*
         * DUP: FDWebSite/docroot/checkout/step_2_unavail.jsp LAST UPDATED ON: 10/07/2009 LAST UPDATED WITH SVN#: 5951 WHY: The following logic was duplicate because it was
         * specified in a JSP file. WHAT: If there are ATP failure, the duplicated code determines what options are available based on failure condition.
         */
        DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
        Cart cart = this.sessionUser.getShoppingCart();
        Map<String, FDAvailabilityInfo> invsInfoMap = cart.getUnavailabilityMap();

        Map<String, List<Integer>> groupingMap = new HashMap<String, List<Integer>>();
        List<Integer> regularItems = new ArrayList<Integer>();
        List<String> groupingKeyList = new ArrayList<String>();

        List<String> unreadKeys = new ArrayList<String>();
        for (String item : invsInfoMap.keySet()) {
            Integer key;
            try {
                key = Integer.parseInt(item, 10);
                FDCartLineI cartLine = cart.getOrderLineById((key).intValue());
                String rcpSrcId = cartLine.getRecipeSourceId();
                if (rcpSrcId != null) {
                    String deptDecs = cartLine.getDepartmentDesc();
                    List<Integer> rcpItems = groupingMap.get(deptDecs);
                    if (rcpItems == null) {
                        rcpItems = new ArrayList<Integer>();
                        groupingMap.put(deptDecs, rcpItems);
                        groupingKeyList.add(deptDecs);
                    }
                    rcpItems.add(key);
                } else {
                    regularItems.add(key);
                }
            } catch (NumberFormatException e) {
                unreadKeys.add(item);
            }
        }

        if (regularItems.size() > 0) {
            groupingKeyList.add("nonRecipeItems");
            groupingMap.put("nonRecipeItems", regularItems);
        }

        AtpError atpError = new AtpError();

        for (String groupKey : groupingKeyList) {
            AtpError.Group group = new AtpError.Group();
            group.setName(groupKey);
            atpError.addGroup(group);
            for (Integer key : groupingMap.get(groupKey)) {
                AtpError.CartLineItem item = new AtpError.CartLineItem();
                group.addCartLineItem(item);

                FDCartLineI cartLine = cart.getOrderLineById(key.intValue());
                FDAvailabilityInfo info = invsInfoMap.get(key);

                item.setQuantity(cartLine.getQuantity());
                item.setDescription(cartLine.getDescription());
                item.setConfigurationDesc(cartLine.getConfigurationDesc());

                ItemAvailabilityError itemAvailabilityError = new ItemAvailabilityError();
                item.setError(itemAvailabilityError);

                if (info instanceof FDRestrictedAvailabilityInfo) {
                    EnumDlvRestrictionReason rsn = ((FDRestrictedAvailabilityInfo) info).getRestriction().getReason();
                    if (EnumDlvRestrictionReason.KOSHER.equals(rsn)) {
                        itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_KOSHER);
                        itemAvailabilityError.setMessage("Kosher production item - not available Fri, Sat, Sun AM, and holidays");
                        // %><a href="javascript:popup('/shared/departments/kosher/delivery_info.jsp','small')">Kosher production item</a> - not available Fri, Sat, Sun AM, and
                        // holidays<%
                    } else {
                        itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_GENERIC_RESTRICTED_AVAILABILITY);
                        itemAvailabilityError.setMessage(((FDRestrictedAvailabilityInfo) info).getRestriction().getMessage());
                        // %><%= ((FDRestrictedAvailabilityInfo)info).getRestriction().getMessage() %><%
                    }
                } else if (info instanceof FDStockAvailabilityInfo) {
                    itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_LIMITED_INVENTORY);
                    double availQty = ((FDStockAvailabilityInfo) info).getQuantity();
                    item.setInStock(Double.toString(availQty));
                    itemAvailabilityError.setArgs(new String[] { Double.toString(availQty) });
                    itemAvailabilityError.setMessage(availQty == 0 ? "None" : quantityFormatter.format(availQty) + " Available");
                } else if (info instanceof FDCompositeAvailabilityInfo) {
                    ArrayList<String> unavailableOptions = new ArrayList<String>();
                    itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_OPTIONS_UNAVAILABLE);
                    Map<String,FDAvailabilityInfo> componentInfos = ((FDCompositeAvailabilityInfo) info).getComponentInfo();
                    boolean singleOptionIsOut = false;
                    for (Iterator<Map.Entry<String,FDAvailabilityInfo>> i = componentInfos.entrySet().iterator(); i.hasNext();) {
                        Map.Entry<String,FDAvailabilityInfo> e = i.next();
                        String componentKey = e.getKey();
                        if (componentKey != null) {
                            FDProduct fdp = cartLine.lookupFDProduct();
                            String matNo = StringUtils.right(componentKey, 9);
                            System.out.println(matNo);
                            FDVariationOption option = fdp.getVariationOption(matNo);
                            if (option != null) {
                                unavailableOptions.add(option.getDescription());
                                // Check to see if this option is the only option for the variation
                                FDVariation[] vars = fdp.getVariations();
                                for (int vi = 0; vi < vars.length; vi++) {
                                    if (vars[vi].getVariationOption(matNo) != null && vars[vi].getVariationOptions().length > 0) {
                                        singleOptionIsOut = true;
                                    }
                                    ;
                                }

                            }
                        }
                    }
                    itemAvailabilityError.setMessage("The following options are unavailable:" + StringUtils.join(unavailableOptions.iterator(), ","));

                    // The arg index are: 0 : "singleOptionIsOut", is only option flag
                    // The arg index are: 1+ : options that were unavailable
                    String[] args = (String[]) ArrayUtils.add(unavailableOptions.toArray(new String[unavailableOptions.size()]), 0, Boolean.toString(singleOptionIsOut));
                    itemAvailabilityError.setArgs(args);
                } else if (info instanceof FDMuniAvailabilityInfo) {
                    itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_MUNICIPAL_UNAVAILABILITY);
                    itemAvailabilityError.setMessage("FreshDirect does not deliver alcohol outside NY");
                } else {
                    itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_GENERIC_UNAVAILABILITY);
                    itemAvailabilityError.setMessage("Out of stock");
                }
            } // end for
        }

        // Set first date available, in case.
        Date firstAvailableDate = cart.getFirstAvailableDate();
        if (firstAvailableDate != null) {
            atpError.setFirstAvailableDate(firstAvailableDate);
        }

        // Delivey Pass
        List<DlvPassAvailabilityInfo> unavailPasses = cart.getUnavailablePasses();
        if (unavailPasses != null && unavailPasses.size() > 0) {
            AtpError.Group group = new AtpError.Group();
            atpError.addGroup(group);
            for (DlvPassAvailabilityInfo info : unavailPasses) {
                AtpError.CartLineItem item = new AtpError.CartLineItem();

                Integer key = info.getKey();
                FDCartLineI cartLine = cart.getOrderLineById(key.intValue());
                
                item.setDescription(cartLine!=null?cartLine.getDescription():null);
                item.setConfigurationDesc(cartLine!=null?cartLine.getConfigurationDesc():null);
                item.setQuantity(cartLine!=null?cartLine.getQuantity():null);

                ItemAvailabilityError itemAvailabilityError = new ItemAvailabilityError();
                item.setError(itemAvailabilityError);

                itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_DELIVERY_PASS);
                itemAvailabilityError.setMessage(info.getReason());
            }
        }
        // Unavailability Data
        Unavailability unavailability = Unavailability.collectData(this.sessionUser);
        atpError.setUnavailability(unavailability);
        atpError.setUnreadKeys(unreadKeys);
        return atpError;
    }

    public ResultBundle removeUnavailableItemsFromCart(RequestData requestData) throws FDException {
        AdjustAvailabilityTagWrapper wrapper = new AdjustAvailabilityTagWrapper(this.sessionUser);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_REMOVE_CART_EVENT);
        cartEvent.setRequestData(requestData);
        return wrapper.removeUnavailableItemsFromCart(cartEvent);
    }

    // Code from checkout/step_3_unavail.jsp
    public Message getSpecialRestrictedItemDetail() {

        SpecialRestrictedItemsError splRestrictionError = new SpecialRestrictedItemsError();

        if (this.sessionUser != null && this.sessionUser.getShoppingCart() != null) {

            Cart cart = this.sessionUser.getShoppingCart();
            List<FDCartLineI> ebtIneligibleOrderLines = new ArrayList<FDCartLineI>();

            List<FDCartLineI> orderLines = cart.getOrderLines();
            if (orderLines != null && orderLines.size() > 0) {

                for (FDCartLineI cartLine : orderLines) {
                    if (cartLine.getProductRef().lookupProductModel().isExcludedForEBTPayment()) {
                        ebtIneligibleOrderLines.add(cartLine);

                        SpecialRestrictedItemsError.CartLineItem item = new SpecialRestrictedItemsError.CartLineItem();
                        splRestrictionError.addCartLineItem(item);

                        item.setQuantity(cartLine.getQuantity());
                        item.setDescription(cartLine.getDescription());
                        item.setConfigurationDesc(cartLine.getConfigurationDesc());
                    }
                }

                if (splRestrictionError.getItems() != null && ebtIneligibleOrderLines.size() > 0) {
                    splRestrictionError.setErrorCode(MessageCodes.ERR_EBT_RESTRICTED);
                    splRestrictionError.setMessage(MessageCodes.MSG_EBT_PRODUCT_NOT_ALLOWED);
                    cart.setEbtIneligibleOrderLines(ebtIneligibleOrderLines);
                }
            }
        }
        return splRestrictionError;
    }

    /**
     * @param requestData
     *            [For now special means EBT restricted item, may be useful in future for other special restriction]
     * @return
     * @throws FDException
     */
    public ResultBundle removeSpecialRestrictedItemsFromCart(RequestData requestData) throws FDException {
        AdjustAvailabilityTagWrapper wrapper = new AdjustAvailabilityTagWrapper(this.sessionUser);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_REMOVE_CART_EVENT);
        cartEvent.setRequestData(requestData);
        ResultBundle result = wrapper.removeSpecialRestrictedItemsFromCart(cartEvent);
        return result;
    }

    public ResultBundle verifyAlcoholAge() throws FDException {
        AgeVerificationControllerTagWrapper wrapper = new AgeVerificationControllerTagWrapper(this.sessionUser);
        ResultBundle result = wrapper.verifyAlcoholAge();
        return result;
    }

    // -----------------------------------FDX SUBMIT ORDER -------------------------------------------------------------

    /**
     * This is used by checkoutController.submitOrderEx () to check payment and submit order
     * 
     * @param message
     * @param user
     * @param request
     * @return
     * @throws FDException
     */
    public SubmitOrderExResult submitEx(SubmitOrderExResult message, SessionUser user, HttpServletRequest request, boolean dlvPassCart) throws FDException {
        ResultBundle submitResult = this.submitOrder(dlvPassCart);

        if (submitResult.getActionResult().isSuccess()) {
            message.setStatus(Message.STATUS_SUCCESS);
            com.freshdirect.mobileapi.controller.data.response.Order orderReceipt = new com.freshdirect.mobileapi.controller.data.response.Order();
            String orderId = (String) request.getSession().getAttribute(SessionName.RECENT_ORDER_NUMBER);
            com.freshdirect.mobileapi.model.Order order = user.getOrder(orderId);
            if (null != order) {
                orderReceipt = order.getOrderDetail(user, dlvPassCart);
            }
            message.wrap(orderReceipt);
            message.addDebugMessage("Order has been submitted successfully.");
        } else {
            message.setStatus(Message.STATUS_FAILED);
            message.addErrorMessages(submitResult.getActionResult().getErrors(), user);
        }
        message.addWarningMessages(submitResult.getActionResult().getWarnings());

        return message;
    }

    /**
     * This checks if the reservation in the cart is still valid called by checkoutController.submitOrderEx () If everything is set correctly and cutoff, expiry are not stale
     * returns true.
     * 
     * @param user
     * @return
     */
    public boolean checkReservationExpiry(SessionUser user) {
        boolean reservationValid = true;
        Date now = new Date();
        // check if everything with the timeslot reservation is fine.
        if (user.getShoppingCart().getDeliveryReservation().getTimeslot() != null && user.getShoppingCart().getDeliveryReservation().getTimeslot().getDlvStartTime() != null
                && user.getShoppingCart().getDeliveryReservation().getTimeslot().getDlvEndTime() != null) {

            // Check cutoff time already stale
            if (user.getShoppingCart().getDeliveryReservation().getTimeslot().getCutoffDateTime() != null
                    && user.getShoppingCart().getDeliveryReservation().getTimeslot().getCutoffDateTime().before(now)) {
                reservationValid = false;
            }

            if (user.getShoppingCart().isModifyCart()) {
                Date origCutoff = user.getShoppingCart().getDeliveryReservation().getCutoffTime();
                if (new Date().after(origCutoff)) {
                    reservationValid = false;
                }
            }
            if ((user.getShoppingCart().getOriginalReservation() != null
                    && !user.getShoppingCart().getOriginalReservation().getId().equals(user.getShoppingCart().getDeliveryReservation().getId()))
                    || user.getShoppingCart().getOriginalReservation() == null) {
                // check expiration time stale
                if (user.getShoppingCart().getDeliveryReservation().getExpirationDateTime() != null
                        && user.getShoppingCart().getDeliveryReservation().getExpirationDateTime().before(now)) {
                    reservationValid = false;
                }
            }

        } else {
            // the delivery timeslot is not properly set reservation is not valid.
            reservationValid = false;
        }

        return reservationValid;
    }

    /**
     * This is called by checkoutController.submitOrderEx () this check if address is valid for alcohol and if age verified. Delivery Address type is not used by verify Address
     * call we are making in this method. This takes care of Age Verification as well.
     * 
     * @param message
     * @param user
     * @param request
     * @return
     * @throws FDException
     */
    public SubmitOrderExResult checkAddressForAlcoholAndAgeVerification(SubmitOrderExResult message, SessionUser user, HttpServletRequest request) throws FDException {
        boolean isAddressEligibleForAlcohol = false;
        boolean isAgeVerified = false;
        AgeVerificationControllerTagWrapper alcoholAddressCheckWrapper = new AgeVerificationControllerTagWrapper(user);
        ResultBundle result = alcoholAddressCheckWrapper.verifyAddress(user.getShoppingCart().getDeliveryReservation().getTimeslot().getId(), null);
        if (!user.getFDSessionUser().getShoppingCart().isAgeVerified()) {
            result.getActionResult().addError(new ActionError("didnot_agree",
                    "You must certify that you are over 21 in order to proceed with Checkout. If you cannot, please remove the alcohol items from your cart before continuing."));
        }
        if (result.getActionResult().isSuccess()) {
            isAddressEligibleForAlcohol = true;
            isAgeVerified = true;
            message.setAddressValidForAlcohol(isAddressEligibleForAlcohol);
            message.setAgeVerified(isAgeVerified);
        } else {
            if (!user.getShoppingCart().isAgeVerified()) {
                isAgeVerified = false;
            }
            isAddressEligibleForAlcohol = false;

            message.addErrorMessages(result.getActionResult().getErrors(), user);
            if (result.getActionResult().getErrors().size() >= 1) {
                StringBuffer alcoholWarning = new StringBuffer();
                for (ActionError error : result.getActionResult().getErrors()) {
                    alcoholWarning.append(error.getDescription());
                    alcoholWarning.append("\n");
                }
                message.setAlcoholRestrictionMessage(alcoholWarning.toString());
            }
            message.setAddressValidForAlcohol(isAddressEligibleForAlcohol);
            message.setAgeVerified(isAgeVerified);
            message.setStatus(Message.STATUS_FAILED);

        }
        return message;
    }

    /**
     * This is called by checkoutController.submitOrderEx () checks the timeslot validity for alcohol delivery
     * 
     * @param message
     * @param user
     * @param isWebRequest value 'true' is meant for FK Mobile Web clients, 'false' should be the default
     * @return
     * @throws FDResourceException
     */
    public SubmitOrderExResult alcoholTimeSlotCheck(SubmitOrderExResult message, SessionUser user, boolean isWebRequest) throws FDResourceException {
        AddressModel address = AddressUtil.scrubAddress(user.getShoppingCart().getDeliveryAddress(), new ActionResult());
        FDTimeslot timeSlot = FDDeliveryManager.getInstance().getTimeslotsById(user.getShoppingCart().getDeliveryReservation().getTimeslot().getId(),
                user.getShoppingCart().getDeliveryAddress().getBuildingId(), true);
        /*
         * Using DeliveryTimeSlotTag to reuse alcoholRestrictionReasons & isTimeSlotAlcoholRestricted Functionality
         */
        // com.freshdirect.webapp.taglib.fdstore.DeliveryTimeSlotTag dlvTimeSlotTag = new DeliveryTimeSlotTag();
        // get the baseRange - this is used while getting the restrictions for delivery
        DateRange baseRange = new DateRange(timeSlot.getDeliveryDate(), DateUtil.addDays(timeSlot.getDeliveryDate(), 1));

        // get the restrictions
        DlvRestrictionsList restrictions = FDDeliveryManager.getInstance().getDlvRestrictions();

        // filter and get if restriction apply to the given timeslot id
        List<RestrictionI> r = restrictions.getRestrictions(EnumDlvRestrictionCriterion.DELIVERY,
                TimeslotService.defaultService().getAlcoholRestrictionReasons(user.getShoppingCart().getCart()),
                baseRange);
        // Filter Alcohol restrictions by current State and county.
        final String county = FDDeliveryManager.getInstance().getCounty(address);
        List<RestrictionI> alcoholRestrictions = RestrictionUtil.filterAlcoholRestrictionsForStateCounty(address.getState(), county, r);

        boolean isTimeslotRestrictedForAlcohol = TimeslotService.defaultService().isTimeslotAlcoholRestricted(alcoholRestrictions, timeSlot);

        // Reason we flip the boolean value in below statement : we get if timeslot is restricted and are
        // setting timeslot validity.
        message.setTimeSlotValidForAlcohol(!isTimeslotRestrictedForAlcohol);
        if (isTimeslotRestrictedForAlcohol) {
            
            if (isWebRequest) {
                message.addErrorMessage(MessageCodes.ERR_TSLOT_ALC_RESTRICTED, MessageCodes.ERR_TSLOT_ALC_RESTRICTED_MSG);
            } else {
                message.addErrorMessage(MessageCodes.ERR_TSLOT_ALC_RESTRICTED_MSG);
            }
            
            message.setStatus(Message.STATUS_FAILED);

        }
        return message;
    }

    /**
     * Called by checkoutController.submitOrderEx () performs ATP check and populates the message with unavailable items and their possible replacements.
     * 
     * @param message
     * @param request
     * @return
     */
    public SubmitOrderExResult fillAtpErrorDetail(SubmitOrderExResult message, HttpServletRequest request) {
        AtpError atpError = (AtpError) this.getAtpErrorDetail();
        AtpErrorData unavailabilityData = null;
        try {
            unavailabilityData = AtpErrorData.wrap(atpError);
        } catch (ParseException e) {
            message.addErrorMessage("Error While retrieving unvailable items List");
        }
        message.setUnavaialabilityData(unavailabilityData);
        message.setStatus(Message.STATUS_FAILED);
        message.addErrorMessage("ATP ERROR", "One or more items Unavailable.");

        return message;
    }

}
