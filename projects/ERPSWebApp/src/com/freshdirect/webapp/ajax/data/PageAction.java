package com.freshdirect.webapp.ajax.data;

public enum PageAction {
	ADD_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "addDeliveryAddressMethod"), EDIT_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "editDeliveryAddressMethod"),
	DELETE_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "deleteDeliveryAddressMethod"), SELECT_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "selectDeliveryAddressMethod"),
	GET_DELIVERY_ADDRESS_METHOD(Module.EXPRESS_CHECKOUT, "getDeliveryAddressMethod"), ADD_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "addPaymentMethod"),
	EDIT_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "editPaymentMethod"), DELETE_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "deletePaymentMethod"),
	SELECT_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "selectPaymentMethod"), LOAD_PAYMENT_METHOD(Module.EXPRESS_CHECKOUT, "loadPaymentMethod"),
	SELECT_DELIVERY_TIMESLOT(Module.EXPRESS_CHECKOUT, "selectDeliveryTimeslot"),
	ATP_ADJUST(Module.EXPRESS_CHECKOUT, "atpAdjust"), PLACE_ORDER(Module.EXPRESS_CHECKOUT, "placeOrder"),
	REMOVE_ALCOHOL_FROM_CART(Module.EXPRESS_CHECKOUT, "removeAlcohol"), APPLY_AGE_VERIFICATION_FOR_ALCOHOL_IN_CART(Module.EXPRESS_CHECKOUT, "applyAgeVerification"),
	REMOVE_WINE_AND_SPIRITS_FROM_CART(Module.EXPRESS_CHECKOUT, "removeWineAndSpirit"), REMOVE_EBT_INELIGIBLE_ITEMS_FROM_CART(Module.EXPRESS_CHECKOUT, "removeEbtIneligibleItems");

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
