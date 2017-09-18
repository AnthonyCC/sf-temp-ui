package com.freshdirect.webapp.ajax.analytics.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.webapp.ajax.analytics.data.GACheckoutData;
import com.freshdirect.webapp.ajax.analytics.data.GoogleAnalyticsData;
import com.freshdirect.webapp.ajax.analytics.domain.GAPageTypeDistinguisher;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SearchParams;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class GoogleAnalyticsDataService {

    private static final GoogleAnalyticsDataService INSTANCE = new GoogleAnalyticsDataService();

    private GoogleAnalyticsDataService() {
    }

    public static GoogleAnalyticsDataService defaultService() {
        return INSTANCE;
    }

    public GoogleAnalyticsData populateBasicGAData(FDSessionUser user, String loginType, Boolean loginSuccess, Boolean signupSuccess, Boolean socialLoginSuccess,
            GAPageTypeDistinguisher distinguisher, Map<String, Object> breadCrumbs)
            throws FDResourceException {
        GoogleAnalyticsData data = new GoogleAnalyticsData();
        data.getGoogleAnalyticsData().put("customer", GACustomerDataService.defaultService().populateCustomerData(user, loginType));
        data.getGoogleAnalyticsData().put("pageType", GAPageTypeDataService.defaultService().populatePageTypeData(distinguisher));
        data.getGoogleAnalyticsData().put("breadcrumb", GABreadCrumbDataService.defaultService().populateBreadCrumbData(breadCrumbs));
        data.getGoogleAnalyticsData().put("login", GALoginDataService.defaultService().populateLoginData(loginSuccess, socialLoginSuccess));
        data.getGoogleAnalyticsData().put("signup", GASignupDataService.defaultService().populateSignupData(signupSuccess));
        return data;
    }

    public GoogleAnalyticsData populateSearchGAData(SearchParams searchParams) {
        GoogleAnalyticsData data = new GoogleAnalyticsData();
        data.getGoogleAnalyticsData().put("search", GASearchResultsDataService.defaultService().populateSearchResultsData(searchParams));
        return data;
    }

    public GoogleAnalyticsData populateAddToCartGAData(FDCartLineI cartLine, String quantity) {
        GoogleAnalyticsData data = new GoogleAnalyticsData();
        data.getGoogleAnalyticsData().put("ATCData", GACartDataService.defaultService().populateCartData(cartLine, quantity));
        return data;
    }

    public GoogleAnalyticsData populateCartLineChangeGAData(FDCartLineI cartLine, String quantity) {
        GoogleAnalyticsData data = new GoogleAnalyticsData();
        data.getGoogleAnalyticsData().put("cartLineChange", GACartDataService.defaultService().populateCartData(cartLine, quantity));
        return data;
    }


    public GoogleAnalyticsData populateCheckoutGAData(FDCartI cart, CartData cartData) {
        GoogleAnalyticsData data = cartData.getGoogleAnalyticsData();
        if (data == null) {
            data = new GoogleAnalyticsData();
        }
        data.getGoogleAnalyticsData().put("checkout", GACheckoutDataService.defaultService().populateCheckoutProductData(cart));
        return data;
    }

    public void populateCheckoutSuccessGAData(CartData cartData, FDOrderI order, HttpSession session) {
        GoogleAnalyticsData data = cartData.getGoogleAnalyticsData();
        GACheckoutDataService.defaultService().populateCheckoutSuccessData((GACheckoutData) data.getGoogleAnalyticsData().get("checkout"), cartData, order, session);
    }

}
