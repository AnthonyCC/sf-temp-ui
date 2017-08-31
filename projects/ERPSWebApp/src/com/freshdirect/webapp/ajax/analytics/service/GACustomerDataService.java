package com.freshdirect.webapp.ajax.analytics.service;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.ajax.analytics.data.GACustomerData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class GACustomerDataService {

    private static final GACustomerDataService INSTANCE = new GACustomerDataService();

    private GACustomerDataService() {

    }

    public static GACustomerDataService defaultService() {
        return INSTANCE;
    }

    public GACustomerData populateCustomerData(FDSessionUser user, String loginType) throws FDResourceException {

        GACustomerData customer = new GACustomerData();
        if(null != user) {
	        customer.setZipCode(user.getZipCode());
	        customer.setUserId(user.getIdentity() != null ? user.getIdentity().getFDCustomerPK() : null);
	        customer.setUserStatus(getUserLevel(user.getLevel()));
	        customer.setUserType(getUserType(user.getAdjustedValidOrderCount()));
	        customer.setLoginType(loginType);
	        customer.setChefsTable(Boolean.toString(user.isChefsTable()));
	        customer.setDeliveryPass(Boolean.toString(user.isDlvPassActive()));
	        customer.setDeliveryType(user.getUserServiceType() != null ? user.getUserServiceType().name() : null);
	        customer.setCohort(user.getCohortName());
	        customer.setCounty(user.getDefaultCounty());
	        customer.setOrderCount(Integer.toString(user.getAdjustedValidOrderCount()));
	        customer.setDeliveryPassStatus(user.getDlvPassInfo() != null && user.getDlvPassInfo().getStatus() != null ? user.getDlvPassInfo().getStatus().getDisplayName() : null);
	        customer.setCustomerId(user.getIdentity() != null ? user.getIdentity().getErpCustomerPK() : null);
	        String paymentDefaultType = user.getFDCustomer().getDefaultPaymentType().getName();
	        String paymentType = "";
	        if(null !=user.getShoppingCart() && null!=user.getShoppingCart().getPaymentMethod() && null !=user.getShoppingCart().getPaymentMethod().getPK() 
	        		&& !user.getShoppingCart().getPaymentMethod().getPK().getId().equals(user.getFDCustomer().getDefaultPaymentMethodPK())){
	        	paymentType = "custom_selection";
	        }
	        else if(paymentDefaultType.equals(EnumPaymentMethodDefaultType.DEFAULT_CUST)){
	        	paymentType = "customer_default";
	        }else if (paymentDefaultType.equals(EnumPaymentMethodDefaultType.DEFAULT_SYS)){
	        	paymentType = "fd_default";
	        }
	        customer.setDefaultPaymentType(paymentType);
        }

        return customer;
    }


    private String getUserLevel(int userLevel) {
        String level = null;
        switch (userLevel) {
            case 0:
                level = "GUEST";
                break;
            case 1:
                level = "RECOGNIZED";
                break;
            case 2:
                level = "SIGNED_IN";
                break;
            default:
                level = "";
                break;
        }
        return level;
    }

    private String getUserType(int adjustedValidOrderCount) {
        String userType = null;
        if (adjustedValidOrderCount == 0) {
            userType = "new";
        } else {
            userType = "existing";
        }
        return userType;
    }

}
