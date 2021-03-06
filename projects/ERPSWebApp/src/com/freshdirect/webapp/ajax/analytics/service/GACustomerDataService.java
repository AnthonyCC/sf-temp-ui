package com.freshdirect.webapp.ajax.analytics.service;

import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
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
	        final FDIdentity identity = user.getIdentity();
	        final int orderCount = settledOrderCount(user);

	        customer.setZipCode(user.getZipCode());
	        customer.setUserId(identity != null ? identity.getFDCustomerPK() : null);
	        customer.setUserStatus(getUserLevel(user.getLevel()));
	        customer.setUserType(getUserType(orderCount));
	        customer.setLoginType(loginType);
	        customer.setChefsTable(Boolean.toString(user.isChefsTable()));
	        customer.setDeliveryPass(Boolean.toString(user.isDlvPassActive()));
	        customer.setDeliveryType(user.getSelectedServiceType() != null ? user.getSelectedServiceType().name() : null);
	        customer.setCohort(user.getCohortName());
	        customer.setCounty(user.getDefaultCounty());
	        customer.setOrderCount(Integer.toString(orderCount));
	        customer.setDeliveryPassStatus(user.getDlvPassInfo() != null && user.getDlvPassInfo().getStatus() != null ? user.getDlvPassInfo().getStatus().getDisplayName() : null);
	        customer.setCustomerId(identity != null ? identity.getErpCustomerPK() : null);
	        customer.setModifyMode((user.getShoppingCart() instanceof FDModifyCartModel));
	        customer.setHasActiveSO3s((user.getActiveSO3s().size()>0)?"T":"F");

	        /* to differentiate weblogic(1) vs tomcat(2) */
	        String storeVersion = System.getProperty("catalina.base");
	        if (storeVersion!= null && storeVersion.trim().length() > 0) {
	        	customer.setStorefrontVersion("2");
	        } else {
	        	customer.setStorefrontVersion("1");
	        }

	        String paymentType = "";
	        if(null != identity){
		        try {
					String paymentDefaultType = null !=user.getFDCustomer().getDefaultPaymentType() ? user.getFDCustomer().getDefaultPaymentType().getName():"";
					if(null !=user.getShoppingCart() && null!=user.getShoppingCart().getPaymentMethod() && null !=user.getShoppingCart().getPaymentMethod().getPK()
							&& !user.getShoppingCart().getPaymentMethod().getPK().getId().equals(user.getFDCustomer().getDefaultPaymentMethodPK())){
						paymentType = "custom_selection";
					}
					else if(paymentDefaultType.equals(EnumPaymentMethodDefaultType.DEFAULT_CUST.getName())){
						paymentType = "customer_default";
					}else if (paymentDefaultType.equals(EnumPaymentMethodDefaultType.DEFAULT_SYS.getName())){
						paymentType = "fd_default";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

    private int settledOrderCount(FDSessionUser customer) {
        int orderCount = 0;
        try {
            orderCount = customer.getOrderHistory().getSettledOrderCount(EnumEStoreId.FD);
        } catch (FDResourceException exc) {
        }

        return orderCount;
    }
}
