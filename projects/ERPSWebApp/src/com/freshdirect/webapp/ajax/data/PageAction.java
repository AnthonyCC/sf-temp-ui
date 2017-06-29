package com.freshdirect.webapp.ajax.data;

public enum PageAction {
    ADD_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "addDeliveryAddressMethod"),
    EDIT_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "editDeliveryAddressMethod"),
    DELETE_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "deleteDeliveryAddressMethod"),
    SELECT_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "selectDeliveryAddressMethod"),
    GET_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "getDeliveryAddressMethod"),
    ADD_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "addPaymentMethod"),
    EDIT_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "editPaymentMethod"),
    DELETE_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "deletePaymentMethod"),
    SELECT_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "selectPaymentMethod"),
    LOAD_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "loadPaymentMethod"),
    SELECT_DELIVERY_TIMESLOT(Module.EXPRESS_CHECKOUT, "selectDeliveryTimeslot"),
    ATP_ADJUST(Module.EXPRESS_CHECKOUT, "atpAdjust"),
    PLACE_ORDER(Module.EXPRESS_CHECKOUT, "placeOrder"),
    REMOVE_ALCOHOL_FROM_CART(Module.EXPRESS_CHECKOUT, "removeAlcohol"),
    APPLY_AGE_VERIFICATION_FOR_ALCOHOL_IN_CART(Module.EXPRESS_CHECKOUT, "applyAgeVerification"),
    REMOVE_WINE_AND_SPIRITS_FROM_CART(Module.EXPRESS_CHECKOUT, "removeWineAndSpirit"),
    REMOVE_EBT_INELIGIBLE_ITEMS_FROM_CART(Module.EXPRESS_CHECKOUT, "removeEbtIneligibleItems"),
    APPLY_PROMOTION(Module.EXPRESS_CHECKOUT, "applyPromotion"),
    REMOVE_PROMOTION(Module.EXPRESS_CHECKOUT, "removePromotion"),
    APPLY_GIFT_CARD(Module.EXPRESS_CHECKOUT, "applyGiftCard"),
    REMOVE_GIFT_CARD(Module.EXPRESS_CHECKOUT, "removeGiftCard"),
    MASTERPASS_START_PAIRING(Module.EXPRESS_CHECKOUT, "MP_Pairing_Start"),
    MASTERPASS_SELECT_PAYMENTMETHOD(Module.EXPRESS_CHECKOUT, "selectMPPaymentMethod"),
    MASTERPASS_END_PAIRING(Module.EXPRESS_CHECKOUT, "MP_Pairing_End"),
    MASTERPASS_EXPRESS_CHECKOUT(Module.EXPRESS_CHECKOUT, "MP_Express_Checkout"),
    MASTERPASS_WALLET_ALL_PAYMETHOD_IN_EWALLET(Module.EXPRESS_CHECKOUT, "MP_All_PayMethod_In_Ewallet"),
    MASTERPASS_WALLET_CONNECT_START(Module.EXPRESS_CHECKOUT, "MP_Connect_Start"),
    MASTERPASS_WALLET_CONNECT_END(Module.EXPRESS_CHECKOUT, "MP_Connect_End"),
    MASTERPASS_WALLET_DISCONNECT(Module.EXPRESS_CHECKOUT, "MP_Disconnect_Ewallet"),
    MASTERPASS_WALLET_ALL_PAYMETHOD_IN_EWALLET_MYACCOUNT(Module.EXPRESS_CHECKOUT, "MP_All_PayMethod_In_Ewallet_MyAccount"),
    MASTERPASS_STANDARD_CHECKOUT(Module.EXPRESS_CHECKOUT, "MP_Standard_Checkout"),
    MASTERPASS_STANDARD_CHECKOUT_DATA(Module.EXPRESS_CHECKOUT, "MP_Standard_CheckoutData"),
    MASTERPASS_PICK_MP_PAYMENTMETHOD(Module.EXPRESS_CHECKOUT, "pickMPPaymentMethod"),
    APPLY_CSR_METHOD(Module.EXPRESS_CHECKOUT, "applyCsrMethod"),
    APPLY_ETIP(Module.EXPRESS_CHECKOUT, "applyETip"),
    PAYPAL_START_PAIRING(Module.EXPRESS_CHECKOUT, "PP_Pairing_Start"),
    PAYPAL_END_PAIRING(Module.EXPRESS_CHECKOUT, "PP_Pairing_End"),
    PAYPAL_START_CONNECTING(Module.EXPRESS_CHECKOUT, "PP_Connecting_Start"),
    PAYPAL_END_CONNECTING(Module.EXPRESS_CHECKOUT, "PP_Connecting_End"),
    PAYPAL_WALLET_DISCONNECT(Module.EXPRESS_CHECKOUT, "PP_Disconnect_Wallet"),
    GET_PP_DEVICE_DATA(Module.EXPRESS_CHECKOUT, "get_pp_device_data");

    public final Module module;
    public final String actionName;

    PageAction(Module module, String actionName) {
        this.module = module;
        this.actionName = actionName;
    }

    public static PageAction parse(String actionName) {
        PageAction result = null;
        for (PageAction pageAction : values()) {
            if (pageAction.actionName.equals(actionName)) {
                result = pageAction;
                break;
            }
        }
        return result;
    }

    public enum Module {
        EXPRESS_CHECKOUT;
    }
}
