package com.freshdirect.mobileapi.model.tagwrapper;

import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public interface RequestParamName {

    public static final String REQ_PARAM_ACTION = "action";

    public static final String REQ_PARAM_PAYMENT_METHOD_ID = "paymentMethodList";

    public static final String REQ_PARAM_BILLING_REF = "billingRef";

    public static final String REQ_PARAM_BYPASS_ACCOUNT_CHECK = PaymentMethodName.BYPASS_BAD_ACCOUNT_CHECK;

    public static final String REQ_PARAM_SLOT_ID = "deliveryTimeslotId";

    public static final String REQ_PARAM_CHEF_TABLE = "chefstable";

    public static final String REQ_PARAM_GIFT_CARD = "gift_card";

    public static final String REQ_PARAM_DONATION = "donation";

    public static final String REQ_PARAM_WAIVE_DELIVERY_FEE = "waive_delivery_fee";

    public static final String REQ_PARAM_WAIVE_PHONE_FEE = "waive_phone_fee";

    //Add To Cart
    public static final String REQ_PARAM_SKU_CODE = "skuCode";

    //Add To Cart
    public static final String REQ_PARAM_CATEGORY_ID = "catId";

    //Add To Cart
    public static final String REQ_PARAM_PRODUCT_ID = "productId";

    //Add To Cart
    public static final String REQ_PARAM_WINE_CATEGORY_ID = "wineCatId";

    //Add To Cart
    public static final String REQ_PARAM_VARIANT = "variant";

    //Add To Cart
    public static final String REQ_PARAM_QUANTITY = "quantity";

    //Add To Cart
    public static final String REQ_PARAM_SALES_UNIT = "salesUnit";

    //Add To Cart
    public static final String REQ_PARAM_CONSENTED = "consented"; //Looks like this isn't being "set" anywhere.

    //Add To Cart - Appears for products w/ terms and conditions associated
    public static final String REQ_PARAM_AGREE_TO_TERMS = "agreeToTerms";

    //Add To Cart
    public static final String REQ_PARAM_RECIPE_ID = "recipeId";

    //Add To Cart - Indicates users has checked "Email a copy of this recipe on the day of delivery!"
    public static final String REQ_PARAM_REQUEST_NOTIFICATION = "requestNotification"; //TODO: When does this appear?

    //Add To Cart
    public static final String REQ_PARAM_YMAL_BOX = "ymal_box"; //YOU MIGHT ALSO LIKE

    //Add To Cart
    public static final String REQ_PARAM_YMAL_SET_ID = "ymalSetId"; //YOU MIGHT ALSO LIKE

    //Add To Cart
    public static final String REQ_PARAM_YMAL_ORIG_PROD_ID = "originatingProductId"; //YOU MIGHT ALSO LIKE (from which product it was clicked)

    //Add To Cart
    public static final String REQ_PARAM_YMAL_ORIG_ORDER_LINE_ID = "originalOrderLineId"; //YOU MIGHT ALSO LIKE (from which product it was clicked)

    //Add To Cart (set)
    public static final String REQ_PARAM_ATC_SUFFIX = "atc_suffix"; //"Add_To_Card" Suffix 

    //Event Logging
    public static final String REQ_PARAM_IMPRESSESION_ID = "impId"; // seems to be only in effect in smartstore in view cart. 

    //Event Logging
    public static final String REQ_PARAM_TRACKING_CODE = "trk"; // tracking code

    //Event Logging
    public static final String REQ_PARAM_TRACKING_CODE_EXT = "trkd"; // tracking code for deals. appears to be only in effect at smart store 

    //See "populateEvent" in FDEventFactory as alot of stuff!!!

    public static final String REQ_PARAM_CUSTOMER_CREATED_LIST_ID = "ccListId";

    public static final String REQ_PARAM_CART_CLEANUP_REMOVED_STUFF_FLAG = "cartCleanupRemovedSomeStuff";

    //Remove Item From Cart
    public static final String REQ_PARAM_CART_LINE_ID = "cartLine";

    //Apply/Remove Promo Code
    public static final String REQ_PARAM_REDEEM_OVERRIDE_MSG = "redeem_override_msg";

    //Apply/Remove Promo Code
    public static final String REQ_PARAM_RECENT_ORDER_NUMBER = SessionName.RECENT_ORDER_NUMBER;

    //Apply/Remove Promo Code
    public static final String REQ_PARAM_REDEMPTION_CODE = "redemptionCode";

    //Apply/Remove Promo Code
    public static final String REQ_PARAM_AGE_VERIFIED = "age_verified";

    //Cancel Order
    public static final String REQ_PARAM_ORDER_ID = "orderId";

    //Cancel Order
    public static final String REQ_PARAM_CANCEL_REASON = "cancel_reason";

    //Cancel Order
    public static final String REQ_PARAM_CANCEL_NOTES = "cancel_notes";

    //Cancel Order
    public static final String REQ_PARAM_ALLOW_MODIFY_ORDER = "allowModifyOrder";

    //Cancel Order
    public static final String REQ_PARAM_ALLOW_RETURN_ORDER = "allowReturnOrder";

    //Cancel Order
    public static final String REQ_PARAM_IS_REFUSED_ORDER = "isRefusedOrder";

    //Cancel Order
    public static final String REQ_PARAM_ALLOW_CANCEL_ORDER = "allowCancelOrder";

    //Cancel Order
    public static final String REQ_PARAM_ALLOW_COMPLAINT = "allowComplaint";

    //Cancel Order
    public static final String REQ_PARAM_ALLOW_NEW_CHARGES = "allowNewCharges";

    //Cancel Order
    public static final String REQ_PARAM_HAS_PAYMENT_EXCEPTION = "hasPaymentException";

    //Cancel Order
    public static final String REQ_PARAM_ALLOW_RESUBMIT_ORDER = "allowResubmitOrder";

    //What's Good
    public static final String REQ_PARAM_SORTBY = "sortBy";
    public static final String REQ_PARAM_GROCERY_VIRTUAL = "groceryVirtual";
    public static final String REQ_PARAM_SORT_DESCENDING = "sortDescending";
    
    public static final String REQ_PARAM_FD_ACTION = "fdAction";

    public static final String REQ_PARAM_QUICK_CART = "quickCart";

    public static final String REQ_PARAM_SORT_BY = "sortBy";

    public static final String REQ_PARAM_QUICK_SHOP_DEPT_ID = "qsDeptId";

    public static final String REQ_PARAM_ITEM_COUNT = "itemCount";

    public static final String REQ_PARAM_REMOVE = "remove";

    public static final String REQ_PARAM_REMOVE_RECIPE = "removeRecipe";

    public static final String REQ_PARAM_SOURCE = "source";

    //Get Shopping List
    public static final String REQ_PARAM_CUSTOMER_LIST = SessionName.CUSTOMER_CREATED_LISTS;

    public static final String REQ_PARAM_YMAL_RESET = "freshdirect.ymalSource.reset";

    public static final String REQ_PARAM_SMART_STORE_IMPRESSION = SessionName.IMPRESSION;

    //Get Contact Us
    public static final String REQ_PARAM_CONTACT_US_SUBJECT = "subject";

    public static final String REQ_PARAM_CUSTOMER_PK = "customerPK";

    public static final String REQ_PARAM_SEARCH_PARAMS = "searchParams";

    public static final String REQ_PARAM_SEARCH_DEPT_ID = "deptId";

    public static final String REQ_PARAM_SEARCH_CAT_ID= "catId";

    public static final String REQ_PARAM_SEARCH_ORDER = "order";

    public static final String REQ_PARAM_SEARCH_SORT = "sort";

    public static final String REQ_PARAM_SEARCH_BRAND_VALUE = "brandValue";

    public static final String REQ_PARAM_SEARCH_CLASSIFICATION = "classification";
    
    public static final String REQ_PARAM_SEARCH_START = "start";
    
    public static final String REQ_PARAM_SEARCH_VIEW = "view";
    
    public static final String REQ_PARAM_SEARCH_PAGE_SIZE = "pageSize";
    
    public static final String REQ_PARAM_RESERVATION_TYPE = "reservationType";

    public static final String REQ_PARAM_ADDRESS_ID = "addressId";

}
