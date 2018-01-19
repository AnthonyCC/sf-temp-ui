package com.freshdirect.webapp.ajax.order;

import java.util.List;

import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.Section;

public class OrderInfoData {

    private String orderId;
    private String totalWithoutTax;
    private String estimatedTotal;
    private String subTotal;
    private double itemCount;
    private List<Section> cartSections;
    private boolean containsWineSection;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalWithoutTax() {
        return totalWithoutTax;
    }

    public void setTotalWithoutTax(String totalWithoutTax) {
        this.totalWithoutTax = totalWithoutTax;
    }

    public String getEstimatedTotal() {
        return estimatedTotal;
    }

    public void setEstimatedTotal(String estimatedTotal) {
        this.estimatedTotal = estimatedTotal;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public double getItemCount() {
        return itemCount;
    }

    public void setItemCount(double itemCount) {
        this.itemCount = itemCount;
    }

    public List<Section> getCartSections() {
        return cartSections;
    }

    public void setCartSections(List<Section> cartSections) {
        this.cartSections = cartSections;
    }

    public boolean isContainsWineSection() {
        return containsWineSection;
    }

    public void setContainsWineSection(boolean containsWineSection) {
        this.containsWineSection = containsWineSection;
    }

}
