package com.freshdirect.webapp.ajax.expresscheckout.availability.service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.atp.FDStockAvailabilityInfo;
import com.freshdirect.fdstore.atp.NullAvailability;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.JspMethods;

public class AvailabilityService {

    private static final String DELIVERY_PASS_ONLY = "dlv_pass_only";
    private static final String DELIVERY_PASS_CANCELLED = "dlv_pass_cancelled";
    private static final String GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY = "generalUnderOrderMinimumMessageKey";

    private static final int DEFAULT_ATP_RESTRICTION_TIMEOUT = 30000;

    private static final AvailabilityService INSTANCE = new AvailabilityService();

    private AvailabilityService() {
    }

    public static AvailabilityService defaultService() {
        return INSTANCE;
    }

    public boolean checkCartAtpAvailability(final FDUserI user) throws FDResourceException {
        FDCartModel cart = FDCustomerManager.checkAvailability(user.getIdentity(), user.getShoppingCart(), DEFAULT_ATP_RESTRICTION_TIMEOUT);
        user.updateUserState();
        return cart.isFullyAvailable();
    }

    public String checkCartAvailabilityAdjustResult(FDUserI user) throws FDResourceException {
        String errorMessage = "";
        FDCartModel cart = user.getShoppingCart();
        boolean isEbt = false;
        if (cart.getPaymentMethod() != null) {
            isEbt = EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType());
        }
        if (cart.containsDlvPassOnly()) {
            errorMessage = "Your cart contains only delivery pass item(s).";
        } else if (!user.isOrderMinimumMet() && user.getMasqueradeContext() == null) {
            if (!isEbt) {
                errorMessage = "Because some items were unavailable for delivery at the time you selected, they've been removed from your cart."
                        + " Unfortunately this means that your order total has been adjusted and falls below our $" + (int) user.getMinimumOrderAmount()
                        + " minimum. Please <a href='/index.jsp'>shop for replacement items</a> and then return to Checkout." + " We apologize for the inconvenience.";
            } else {
                errorMessage = "Because some items were ineligible to be payed by EBT, they've been removed from your cart."
                        + " Unfortunately this means that your order total has been adjusted and falls below our $" + (int) user.getMinimumOrderAmount()
                        + " minimum. Please <a href='/index.jsp'>shop for replacement items</a> and then return to Checkout." + " We apologize for the inconvenience.";
            }
        } else if (cart.getSubTotal() < cart.getDeliveryReservation().getMinOrderAmt()) {
            errorMessage = "Unfortunately your order total falls below our delivery $" + (int) cart.getDeliveryReservation().getMinOrderAmt()
                    + " minimum. Please <a href='/index.jsp'>shop for replacement items</a> and then return to Checkout." + " We apologize for the inconvenience.";
        }
        return errorMessage;
    }

    public Map<String, String> populateWarningMessages(FDUserI user) throws FDResourceException {
        Map<String, String> warningMessages = new HashMap<String, String>();
        warningMessages.put(GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY, SystemMessageList.MSG_GENERAL_UNDER_ORDER_MINIMUM_MESSAGE);
        warningMessages.put(DELIVERY_PASS_ONLY, SystemMessageList.MSG_CONTAINS_DLV_PASS_ONLY);
        warningMessages.put(DELIVERY_PASS_CANCELLED, SystemMessageList.MSG_UNLIMITED_PASS_CANCELLED);
        return warningMessages;
    }

    public String translateWarningMessage(String warningMessageKey, FDUserI user) throws FDResourceException {
        String warningMessage = null;
        if (warningMessageKey != null) {
            Map<String, String> populateWarningMessages = AvailabilityService.defaultService().populateWarningMessages(user);
            warningMessage = populateWarningMessages.get(warningMessageKey);
            if (AvailabilityService.GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY.equals(warningMessageKey)) {
                warningMessage = MessageFormat.format(warningMessage, JspMethods.formatPrice(user.getMinimumOrderAmount()));
            }
        }
        return warningMessage;
    }

    public String selectAlcoholicOrderMinimumType(FDUserI user) throws FDResourceException {
        String warningType = null;
        if (!user.isOrderMinimumMet()) {
            warningType = GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY;
        }
        return warningType;
    }

    public String selectWarningType(FDUserI user) throws FDResourceException {
        String warningType = null;
        FDCartModel cart = user.getShoppingCart();
        ErpAddressModel deliveryAddress = cart.getDeliveryAddress();

        if (cart.containsDlvPassOnly()) {
            warningType = DELIVERY_PASS_ONLY;
        } else if (user.getDlvPassInfo() != null && user.getDlvPassInfo().isUnlimited() && user.isDlvPassCancelled() && cart.isDlvPassAlreadyApplied()) {
            warningType = DELIVERY_PASS_CANCELLED;
        } else if (!user.isOrderMinimumMet() && deliveryAddress != null && user.getMasqueradeContext() == null) {
            warningType = GENERAL_UNDER_ORDER_MINIMUM_MESSAGE_KEY;
        }
        return warningType;
    }

    public void adjustCartAvailability(final HttpServletRequest request, List<String> removeCartLineIds, final FDUserI user) throws FDResourceException,
            FDInvalidConfigurationException {
        FDCartModel cart = user.getShoppingCart();
        if (cart.getPaymentMethod() != null && EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType())) {
            for (FDCartLineI cartLine : cart.getEbtIneligibleOrderLines()) {
                cart.removeOrderLineById(cartLine.getRandomId());
                // Create FD remove cart event.
                FDEventUtil.logRemoveCartEvent(cartLine, request);
            }
        } else {
            // unavMap will be empty if ATC happened on step_2_unavail.jsp, but amounts are already set to deliverable amounts
            // see ShoppingCartUtil.getSubTotal(clonedCart) in UnavailabilityPopulator
            Map<String, FDAvailabilityInfo> unavMap = cart.getUnavailabilityMap();
            for (Iterator<Entry<String, FDAvailabilityInfo>> i = unavMap.entrySet().iterator(); i.hasNext();) {
                Entry<String, FDAvailabilityInfo> entry = i.next();
                String key = entry.getKey();
                FDAvailabilityInfo info = entry.getValue();
                int cartLineId = Integer.parseInt(key);
                FDCartLineI cartline = cart.getOrderLineById(cartLineId);
                int cartIndex = cart.getOrderLineIndex(cartLineId);
                if (info instanceof FDStockAvailabilityInfo) {
                    FDStockAvailabilityInfo sInfo = (FDStockAvailabilityInfo) info;

                    if (sInfo.getQuantity() == 0) {
                        // remove
                        cart.removeOrderLine(cartIndex);
                        // Create FD remove cart event.
                        FDEventUtil.logRemoveCartEvent(cartline, request);

                    } else {
                        cartline.setQuantity(sInfo.getQuantity());
                        // Create FD Modify cart event.
                        FDEventUtil.logEditCartEvent(cartline, request);
                    }

                } else {
                    // remove
                    cart.removeOrderLine(cartIndex);
                    FDEventUtil.logRemoveCartEvent(cartline, request);
                }
            }
            // Remove unavailable delivery passes.
            List<DlvPassAvailabilityInfo> unavailPasses = cart.getUnavailablePasses();
            if (unavailPasses != null && unavailPasses.size() > 0) {
                for (Iterator<DlvPassAvailabilityInfo> i = unavailPasses.iterator(); i.hasNext();) {
                    DlvPassAvailabilityInfo info = i.next();
                    Integer key = info.getKey();
                    FDCartLineI cartline = cart.getOrderLineById(key.intValue());
                    int cartIndex = cart.getOrderLineIndex(key.intValue());
                    cart.removeOrderLine(cartIndex);
                    FDEventUtil.logRemoveCartEvent(cartline, request);
                }
                unavailPasses.clear();
            }

            // remove those which were explicitly selected for removal and the ones that are not available
            // (if unavMap is empty because of ATC this will remove unavailable items)
            if (removeCartLineIds != null) {
                for (String removeCartLineId : removeCartLineIds) {
                    int cartLineIdInt = Integer.parseInt(removeCartLineId);
                    int cartIndex = cart.getOrderLineIndex(cartLineIdInt);
                    if (cartIndex > -1) {
                        FDCartLineI cartline = cart.getOrderLineById(cartLineIdInt);
                        cart.removeOrderLine(cartIndex);
                        FDEventUtil.logRemoveCartEvent(cartline, request);
                    }
                }
            }
        }
        refreshCartAndUser(user, cart);
    }

    private void refreshCartAndUser(final FDUserI user, FDCartModel cart) throws FDResourceException, FDInvalidConfigurationException {
        cart.refreshAll(true);
        // This method retains all product keys that are in the cart in the dcpd promo product info.
        user.getDCPDPromoProductCache().retainAll(cart.getProductKeysForLineItems());
        // Revalidate the cart for deliverypass.
        cart.handleDeliveryPass();
        user.updateUserState();
        // Coupons have to re-evaluated.
        user.setCouponEvaluationRequired(true);

        cart.setAvailability(NullAvailability.AVAILABLE);
    }

}
