package com.freshdirect.mobileapi.model;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.FDRestrictedAvailabilityInfo;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
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
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.AtpError;
import com.freshdirect.mobileapi.controller.data.response.Order;
import com.freshdirect.mobileapi.controller.data.response.OrderReceipt;
import com.freshdirect.mobileapi.controller.data.response.AtpError.ItemAvailabilityError;
import com.freshdirect.mobileapi.exception.ValidationException;
import com.freshdirect.mobileapi.model.DeliveryAddress.DeliveryAddressType;
import com.freshdirect.mobileapi.model.DeliveryTimeslots.TimeSlotCalculationResult;
import com.freshdirect.mobileapi.model.tagwrapper.AdjustAvailabilityTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.AgeVerificationControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.CheckoutControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.DlvPassAvailabilityControllerTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.OrderHistoryInfoTagWrapper;

public class Checkout {

    private static Category LOGGER = LoggerFactory.getInstance(Checkout.class);

    /**
     * @return
     * @throws FDException
     */
    public ResultBundle checkDeliveryPassAvailability() throws FDException {
        DlvPassAvailabilityControllerTagWrapper tagWrapper = new DlvPassAvailabilityControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.checkDeliveryPassAvailability();
        return result;
    }

    /**
     * @return
     * @throws FDException
     */
    public ResultBundle submitOrder() throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.submitOrder();

        if (result.getActionResult() == null) {
            ActionResult actionResult = new ActionResult();
            actionResult.setError("There was an unexpected error while processing your submitting order.");
            result.setActionResult(actionResult);
        }

        return result;
    }

    /**
     * @param paymentMethodId
     * @param billingReference
     * @return
     * @throws FDException
     */
    public ResultBundle setPaymentMethod(String paymentMethodId, String billingReference) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = tagWrapper.setPaymentMethod(paymentMethodId, billingReference);
        return result;
    }

    public String getPreselectedPaymethodMethodId() {
        String preselectedPaymethodMethodId = null;

        Cart shoppingCart = sessionUser.getShoppingCart();
        FDCartI cart = shoppingCart.getCart();
        if ((null != cart.getPaymentMethod()) && !(shoppingCart.isModifyCart())) {
            preselectedPaymethodMethodId = ((ErpPaymentMethodModel) cart.getPaymentMethod()).getPK().getId();
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

    /**
     * @param timeSlotResult
     * @return
     * @throws FDResourceException
     */
    public String getPreselectedTimeslotId(TimeSlotCalculationResult timeSlotResult) throws FDResourceException {
        Timeslot foundTimeslot = null;
        String selectedTimeslotId = null;
        String deliveryTimeslotId = null;
        String reservationTimeslotId = null;
        //We can assume that we have shopping cart at this point
        FDCartI cart = sessionUser.getShoppingCart().getCart();
        if (cart.getDeliveryReservation() != null) {
            deliveryTimeslotId = cart.getDeliveryReservation().getTimeslotId();
        }
        if (sessionUser.getReservationTimeslot() != null) {
            reservationTimeslotId = sessionUser.getReservationTimeslot().getTimeslotId();
        }

        foundTimeslot = timeSlotResult.findTimeslotById(deliveryTimeslotId);
        if (foundTimeslot == null) {
            foundTimeslot = timeSlotResult.findTimeslotById(reservationTimeslotId);
        }
        if (foundTimeslot != null) {
            selectedTimeslotId = foundTimeslot.getTimeslotId();
        }

        return selectedTimeslotId;
    }

    /**
     * @return
     * @throws FDResourceException
     */
    public String getPreselectedDeliveryAddressId() throws FDResourceException {
        String addressId = null;
        if (sessionUser.getShoppingCart().isModifyCart()) {
            addressId = sessionUser.getShoppingCart().getCart().getDeliveryReservation().getAddressId();
        } else {
            addressId = sessionUser.getDefaultShipToAddress();
            if (addressId == null) {
                addressId = sessionUser.getReservationAddressId();
            }
        }
        return addressId;
    }

    /**
     * @see com.freshdirect.webapp.taglib.fdstore.CheckoutControllerTag
     * DUP: duplicated the age verification logic on the CheckoutControllerTag class.
     * It was also updated to remove a reference about a page/HttpServletRequest.
     * We also removed the logic about "app" == "CALLCENTER" check as that should controlled 
     * by the controller not model.
     * 
     * @param app
     * @param request
     * @return
     * @throws FDResourceException
     */
    public boolean isAgeVerificationNeeded() throws FDResourceException {
        FDCartModel cart = this.sessionUser.getShoppingCart().getCart();
        return (cart.containsAlcohol() && !cart.isAgeVerified());
    }

    /**
     * TODO: What if this method is called before delivery address/slot is set.  Probably need to throw an exception.
     * @return
     * @throws FDException
     */
    public boolean isDuplicateOrder() throws FDException {
        //Perform duplicate check
        OrderHistoryInfoTagWrapper orderHistoryInfoTagWrapper = new OrderHistoryInfoTagWrapper(this.sessionUser);
        List<FDOrderInfoI> orderInfos = orderHistoryInfoTagWrapper.getOrderHistoryInfo();

        /*
         * DUP: /checkout/step_3_choose.jsp
         * @see /checkout/step_3_choose.jsp
         * 
            // redirect to Duplicate Order warning page if has another order for the same day
            String ignoreSaleId = null;
            if (cart instanceof FDModifyCartModel) {
                    ignoreSaleId = ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
            }
        
            Date currentDlvStart = DateUtil.truncate(cart.getDeliveryReservation().getStartTime());
            
            for (Iterator hIter = orderHistoryInfo.iterator(); hIter.hasNext(); ) {
                    FDOrderInfoI oi = (FDOrderInfoI) hIter.next();
                    if (!(oi.getErpSalesId().equals(ignoreSaleId))
                            && oi.isPending()
                            && currentDlvStart.equals(DateUtil.truncate(oi.getDeliveryStartTime()))) {
                            response.sendRedirect(response.encodeRedirectURL("/checkout/step_2_duplicate.jsp?successPage=/checkout/step_3_choose.jsp"));
                            return;
                    }
            }
         */
        FDCartModel cart = getFDUser().getShoppingCart();
        String ignoreSaleId = null;
        boolean isDuplicateOrder = false;
        if (cart instanceof FDModifyCartModel) {
            ignoreSaleId = ((FDModifyCartModel) cart).getOriginalOrder().getErpSalesId();
        }
        Date currentDlvStart = DateUtil.truncate(cart.getDeliveryReservation().getStartTime());
        for (Iterator hIter = orderInfos.iterator(); hIter.hasNext();) {
            FDOrderInfoI oi = (FDOrderInfoI) hIter.next();
            if (!(oi.getErpSalesId().equals(ignoreSaleId)) && oi.isPending()
                    && currentDlvStart.equals(DateUtil.truncate(oi.getDeliveryStartTime()))) {
                isDuplicateOrder = true;
            }
        }
        return isDuplicateOrder;
    }

    /**
     * @param id
     * @param type
     * @param user
     * @param request
     * @param result
     * @return
     * @throws ValidationException
     * @throws FDException
     */
    public ResultBundle setCheckoutDeliveryAddress(String id, DeliveryAddressType type) throws FDException {
        CheckoutControllerTagWrapper tagWrapper = new CheckoutControllerTagWrapper(this.sessionUser);
        ResultBundle result = null;
        if ((result == null) || (result.getActionResult().isSuccess())) {
            result = tagWrapper.setCheckoutDeliveryAddress(this.sessionUser, id, type);
        }
        if ((result.getActionResult().isSuccess()) && sessionUser.getShoppingCart().isAgeVerified()) {
            AgeVerificationControllerTagWrapper alcoholAddressCheckWrapper = new AgeVerificationControllerTagWrapper(this.sessionUser);
            result = alcoholAddressCheckWrapper.verifyAddress(id, type);
        }
        return result;
    }

    private SessionUser sessionUser;

    private FDUser getFDUser() {
        return sessionUser.getFDSessionUser().getUser();
    }

    public Checkout(SessionUser sessionUser) {
        this.sessionUser = sessionUser;
    }

    /**
     * @return
     * @throws FDException
     */
    @SuppressWarnings("unchecked")
    public Order getCurrentOrderDetails() throws FDException {
        return sessionUser.getShoppingCart().getOrderDetails(sessionUser);
    }

    /**
     * @param orderNumber
     * @return
     * @throws FDException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public OrderReceipt getOrderReceipt(String orderNumber) throws FDException, IllegalAccessException, InvocationTargetException {
        Order order = getCurrentOrderDetails();
        OrderReceipt orderReceipt = new OrderReceipt();
        BeanUtils.copyProperties(orderReceipt, order);
        orderReceipt.setOrderNumber(orderNumber);
        return orderReceipt;
    }

    /**
     * TODO: Model should not return request data class.  Unnecessary coupling of model and view.
     * @return
     */
    public Message getAtpErrorDetail() {
        /*
         * DUP: FDWebSite/docroot/checkout/step_2_unavail.jsp
         * LAST UPDATED ON: 10/07/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: If there are ATP failure, the duplicated code determines what options are available based on failure
         *     condition.
         */
        DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
        Cart cart = this.sessionUser.getShoppingCart();
        Map invsInfoMap = cart.getUnavailabilityMap();

        Map<String, List<Integer>> groupingMap = new HashMap<String, List<Integer>>();
        List<Integer> regularItems = new ArrayList<Integer>();
        List<String> groupingKeyList = new ArrayList<String>();

        for (Object item : invsInfoMap.keySet()) {
            Integer key = (Integer) item;
            FDCartLineI cartLine = cart.getOrderLineById((key).intValue());
            String rcpSrcId = cartLine.getRecipeSourceId();
            if (rcpSrcId != null) {
                String deptDecs = cartLine.getDepartmentDesc();
                List rcpItems = groupingMap.get(deptDecs);
                if (rcpItems == null) {
                    rcpItems = new ArrayList();
                    groupingMap.put(deptDecs, rcpItems);
                    groupingKeyList.add(deptDecs);
                }
                rcpItems.add(key);
            } else {
                regularItems.add(key);
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
                FDAvailabilityInfo info = (FDAvailabilityInfo) invsInfoMap.get(key);

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
                        //                                        %><a href="javascript:popup('/shared/departments/kosher/delivery_info.jsp','small')">Kosher production item</a> - not available Fri, Sat, Sun AM, and holidays<%
                    } else {
                        itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_GENERIC_RESTRICTED_AVAILABILITY);
                        itemAvailabilityError.setMessage(((FDRestrictedAvailabilityInfo) info).getRestriction().getMessage());
                        //                                        %><%= ((FDRestrictedAvailabilityInfo)info).getRestriction().getMessage() %><%
                    }
                } else if (info instanceof FDStockAvailabilityInfo) {
                    itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_LIMITED_INVENTORY);
                    double availQty = ((FDStockAvailabilityInfo) info).getQuantity();
                    itemAvailabilityError.setArgs(new String[] { Double.toString(availQty) });
                    itemAvailabilityError.setMessage(availQty == 0 ? "None" : quantityFormatter.format(availQty) + " Available");
                } else if (info instanceof FDCompositeAvailabilityInfo) {
                    ArrayList<String> unavailableOptions = new ArrayList<String>();
                    itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_OPTIONS_UNAVAILABLE);
                    Map componentInfos = ((FDCompositeAvailabilityInfo) info).getComponentInfo();
                    boolean singleOptionIsOut = false;
                    for (Iterator i = componentInfos.entrySet().iterator(); i.hasNext();) {
                        Map.Entry e = (Map.Entry) i.next();
                        String componentKey = (String) e.getKey();
                        if (componentKey != null) {
                            FDAvailabilityInfo componentInfo = (FDAvailabilityInfo) e.getValue();
                            FDProduct fdp = cartLine.lookupFDProduct();
                            String matNo = StringUtils.right(componentKey, 9);
                            System.out.println(matNo);
                            FDVariationOption option = fdp.getVariationOption(matNo);
                            if (option != null) {
                                unavailableOptions.add(option.getDescription());
                                //Check to see if this option is the only option for the variation
                                FDVariation[] vars = fdp.getVariations();
                                for (int vi = 0; vi < vars.length; vi++) {
                                    FDVariation aVar = vars[vi];
                                    if (vars[vi].getVariationOption(matNo) != null && vars[vi].getVariationOptions().length > 0) {
                                        singleOptionIsOut = true;
                                    }
                                    ;
                                }

                            }
                        }
                    }
                    itemAvailabilityError.setMessage("The following options are unavailable:"
                            + StringUtils.join(unavailableOptions.iterator(), ","));

                    //The arg index are: 0 : "singleOptionIsOut", is only option flag
                    //The arg index are: 1+ : options that were unavailable                    
                    String[] args = (String[]) ArrayUtils.add(unavailableOptions.toArray(new String[unavailableOptions.size()]), 0, Boolean
                            .toString(singleOptionIsOut));
                    itemAvailabilityError.setArgs(args);
                } else if (info instanceof FDMuniAvailabilityInfo) {
                    MunicipalityInfo muni = ((FDMuniAvailabilityInfo) info).getMunicipalityInfo();
                    itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_MUNICIPAL_UNAVAILABILITY);
                    itemAvailabilityError.setMessage("FreshDirect does not deliver alcohol outside NY");
                } else {
                    itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_GENERIC_UNAVAILABILITY);
                    itemAvailabilityError.setMessage("Out of stock");
                }
            }
        }

        //Set first date available, in case.
        Date firstAvailableDate = cart.getFirstAvailableDate();
        if (firstAvailableDate != null) {
            atpError.setFirstAvailableDate(firstAvailableDate);
        }

        //Delivey Pass
        List<DlvPassAvailabilityInfo> unavailPasses = cart.getUnavailablePasses();
        if (unavailPasses != null && unavailPasses.size() > 0) {
            AtpError.Group group = new AtpError.Group();
            atpError.addGroup(group);
            for (DlvPassAvailabilityInfo info : unavailPasses) {
                AtpError.CartLineItem item = new AtpError.CartLineItem();

                Integer key = info.getKey();
                FDCartLineI cartLine = cart.getOrderLineById(key.intValue());

                item.setDescription(cartLine.getDescription());
                item.setConfigurationDesc(cartLine.getConfigurationDesc());
                item.setQuantity(cartLine.getQuantity());

                ItemAvailabilityError itemAvailabilityError = new ItemAvailabilityError();
                item.setError(itemAvailabilityError);

                itemAvailabilityError.setErrorCode(MessageCodes.ERR_ATP_TYPE_DELIVERY_PASS);
                itemAvailabilityError.setMessage(info.getReason());
            }
        }
        return atpError;
    }

    /**
     * @param requestData
     * @return
     * @throws FDException
     */
    public ResultBundle removeUnavailableItemsFromCart(RequestData requestData) throws FDException {
        AdjustAvailabilityTagWrapper wrapper = new AdjustAvailabilityTagWrapper(this.sessionUser);
        CartEvent cartEvent = new CartEvent(CartEvent.FD_REMOVE_CART_EVENT);
        cartEvent.setRequestData(requestData);
        ResultBundle result = wrapper.removeUnavailableItemsFromCart(cartEvent);
        return result;
    }

    public ResultBundle verifyAlcoholAge() throws FDException {
        AgeVerificationControllerTagWrapper wrapper = new AgeVerificationControllerTagWrapper(this.sessionUser);
        ResultBundle result = wrapper.verifyAlcoholAge();
        return result;
    }

}
