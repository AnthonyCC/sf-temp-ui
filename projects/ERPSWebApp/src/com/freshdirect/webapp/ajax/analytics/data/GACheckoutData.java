package com.freshdirect.webapp.ajax.analytics.data;

import java.util.List;
import java.util.Map;

import com.freshdirect.fdlogistics.model.FDTimeslot;

public class GACheckoutData {

    private Map<String, GAProductData> products;
    private String orderId;
    private String paymentType;
    private String revenue;
    private String tax;
    private String shippingCost;
    private List<String> couponCode;
    private List<String> redemptionCode;
    private String tipping;
    private String newOrder;
    private String modifyOrder;
    private String discountAmount;
    private String deliveryType;
    private FDTimeslot selectedTimeslotValue;
    private String unavailableTimeslotValue;

    public Map<String, GAProductData> getProducts() {
        return products;
    }

    public void setProducts(Map<String, GAProductData> products) {
        this.products = products;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public List<String> getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(List<String> couponCode) {
        this.couponCode = couponCode;
    }

    public List<String> getRedemptionCode() {
        return redemptionCode;
    }

    public void setRedemptionCode(List<String> redemptionCode) {
        this.redemptionCode = redemptionCode;
    }

    public String getTipping() {
        return tipping;
    }

    public void setTipping(String tipping) {
        this.tipping = tipping;
    }

    public String getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(String newOrder) {
        this.newOrder = newOrder;
    }

    public String getModifyOrder() {
        return modifyOrder;
    }

    public void setModifyOrder(String modifyOrder) {
        this.modifyOrder = modifyOrder;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public FDTimeslot getSelectedTimeslotValue() {
        return selectedTimeslotValue;
    }

    public void setSelectedTimeslotValue(FDTimeslot selectedTimeslotValue) {
        this.selectedTimeslotValue = selectedTimeslotValue;
    }

    public String getUnavailableTimeslotValue() {
        return unavailableTimeslotValue;
    }

    public void setUnavailableTimeslotValue(String unavailableTimeslotValue) {
        this.unavailableTimeslotValue = unavailableTimeslotValue;
    }

}
