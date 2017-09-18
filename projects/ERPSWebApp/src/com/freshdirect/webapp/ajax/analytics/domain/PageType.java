package com.freshdirect.webapp.ajax.analytics.domain;

import java.util.Arrays;
import java.util.List;

public enum PageType {
    HOMEPAGE("/index.jsp", "/welcome.jsp"),
    LOGIN("/social/login.jsp", "/login/login.jsp"),
    REGISTER("/social/signup_lite.jsp"),
    CATEGORY_LIST("/browse.jsp?pageType=category_list"),
    PRODUCT_LIST("/browse.jsp?pageType=product_list"),
    SEARCH("/srch.jsp"),
    TOP_ITEMS("/quickshop/qs_top_items.jsp"),
    PAST_ORDERS("/quickshop/qs_past_orders.jsp"),
    SHOPPING_LISTS("/quickshop/qs_shop_from_list.jsp"),
    STANDING_ORDER(""),
    PRODUCT_DETAIL("/pdp.jsp"),
    VIEW_TIMESLOTS("/your_account/delivery_info_avail_slots.jsp"),
    RESERVE_TIMESLOT("/your_account/reserve_timeslot.jsp"),
    CART_CONFIRM("/cart_confirm_pdp.jsp"),
    VIEW_CART("/expressco/view_cart.jsp"),
    CHECKOUT("/expressco/checkout.jsp"),
    RECEIPT("/expressco/success.jsp"),
    ACCOUNT("/your_account/signin_information.jsp"),
    HELP("/help/index.jsp"),
    ORDER_DETAILS("/your_account/order_details.jsp"),
    CANCEL_ORDER("/your_account/cancel_order.jsp"),
    ORDER_HISTORY("/your_account/order_history.jsp"),
    GIFT_CARDS("/gift_card/purchase/landing.jsp"),
    CREDITS(""),
    NEW_PRODUCTS("/srch.jsp?pageType=newproducts"),
    E_COUPONS("/srch.jsp?pageType=ecoupon"),
    FRESH_DEALS("/srch.jsp?pageType=pres_picks"),
    STAFF_PICKS("/srch.jsp?pageType=staff_picks"),
    MEAL_KITS("/handpick/category.jsp?id=meals_kits_meals", "/browse.jsp?id=meals_kits_meals"),
    RECIPE_DEPARTMENT("/recipe_dept.jsp"),
    RECIPE_CATEGORY("/recipe_cat.jsp"),
    RECIPE_SUBCATEGORY("/recipe_subcat.jsp"),
    RECIPE_PAGE("/recipe.jsp"),
    ABOUT_US("/browse_special.jsp?id=about_overview"),
    SALE("/browse.jsp?id=wgd_deals"),
    TOP_RATED("/browse.jsp?id=top_rated"),
    LOCAL("/browse.jsp?id=local"),
    JULY_4("/browse.jsp?id=wgd_summer_central"),
    NOT_RECOGNIZED("");

    private List<String> urls;

    private PageType(String... url) {
        this.urls = Arrays.asList(url);
    }

    public static PageType getPageType(String url) {
        PageType pageType = null;
        for (PageType type : PageType.values()) {
            if (type.getUrls().contains(url)) {
                pageType = type;
                break;
            }
        }
        if (pageType == null) {
            pageType = NOT_RECOGNIZED;
        }
        return pageType;
    }

    public static String namePageType(String url) {
        return getPageType(url).name();
    }

    public List<String> getUrls() {
        return urls;
    }

}
