package com.freshdirect.webapp.ajax.analytics.data;


public class GACustomerData {

    private String zipCode;
    private String userId;
    private String userStatus;
    private String userType;
    private String loginType;
    private String chefsTable;
    private String deliveryPass;
    private String deliveryType;
    private String cohort;
    // attributes needed by the old tags
    private String county;
    private String marketingSegment;
    private String orderCount;
    private String deliveryPassStatus;
    private String customerId;
    private String defaultPaymentType;
    private boolean isModifyMode = false;

	public GACustomerData(String zipCode, String userId, String userStatus, String userType, String loginType, String chefsTable, String deliveryPass, String deliveryType,
            String cohort) {
        super();
        this.zipCode = zipCode;
        this.userId = userId;
        this.userStatus = userStatus;
        this.userType = userType;
        this.loginType = loginType;
        this.chefsTable = chefsTable;
        this.deliveryPass = deliveryPass;
        this.deliveryType = deliveryType;
        this.cohort = cohort;
    }

    public GACustomerData() {
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getChefsTable() {
        return chefsTable;
    }

    public void setChefsTable(String chefsTable) {
        this.chefsTable = chefsTable;
    }

    public String getDeliveryPass() {
        return deliveryPass;
    }

    public void setDeliveryPass(String deliveryPass) {
        this.deliveryPass = deliveryPass;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getCohort() {
        return cohort;
    }

    public void setCohort(String cohort) {
        this.cohort = cohort;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getMarketingSegment() {
        return marketingSegment;
    }

    public void setMarketingSegment(String marketingSegment) {
        this.marketingSegment = marketingSegment;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getDeliveryPassStatus() {
        return deliveryPassStatus;
    }

    public void setDeliveryPassStatus(String deliveryPassStatus) {
        this.deliveryPassStatus = deliveryPassStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

	public String getDefaultPaymentType() {
		return defaultPaymentType;
	}

	public void setDefaultPaymentType(String defaultPaymentType) {
		this.defaultPaymentType = defaultPaymentType;
	}
	
    public boolean isModifyMode() {
		return isModifyMode;
	}

	public void setModifyMode(boolean isModifyMode) {
		this.isModifyMode = isModifyMode;
	}
}
