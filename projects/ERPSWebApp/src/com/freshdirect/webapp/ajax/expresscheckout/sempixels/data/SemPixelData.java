package com.freshdirect.webapp.ajax.expresscheckout.sempixels.data;

import java.util.List;

public class SemPixelData {

    private boolean isNewOrder;
    private boolean isModifyOrder;
    private String subtotal;
    private String subtotalND;
    private String orderId;
    private String userCounty;
    private int totalCartItems;
    private String discountAmount;
    private String discountAmountND;
    private int validOrders;
    private String productId;
    private List<String> redeemedPromotions;

    public String getDiscountAmount() {
        return discountAmount;
    }

    public String getDiscountAmountND() {
        return discountAmountND;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getSubtotalND() {
        return subtotalND;
    }

    public int getTotalCartItems() {
        return totalCartItems;
    }

    public String getUserCounty() {
        return userCounty;
    }

    public int getValidOrders() {
        return validOrders;
    }

    public boolean isModifyOrder() {
        return isModifyOrder;
    }

    public boolean isNewOrder() {
        return isNewOrder;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setDiscountAmountND(String discountAmountND) {
        this.discountAmountND = discountAmountND;
    }

    public void setModifyOrder(boolean isModifyOrder) {
        this.isModifyOrder = isModifyOrder;
    }

    public void setNewOrder(boolean isNewOrder) {
        this.isNewOrder = isNewOrder;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public void setSubtotalND(String subtotalND) {
        this.subtotalND = subtotalND;
    }

    public void setTotalCartItems(int totalCartItems) {
        this.totalCartItems = totalCartItems;
    }

    public void setUserCounty(String userCounty) {
        this.userCounty = userCounty;
    }

    public void setValidOrders(int validOrders) {
        this.validOrders = validOrders;
    }

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public List<String> getRedeemedPromotions() {
		return redeemedPromotions;
	}

	public void setRedeemedPromotions(List<String> redeemedPromotions) {
		this.redeemedPromotions = redeemedPromotions;
	}
}
