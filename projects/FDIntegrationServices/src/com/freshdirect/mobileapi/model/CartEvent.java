package com.freshdirect.mobileapi.model;


public class CartEvent extends WebEvent {

    public final static String FD_ADD_TO_CART_EVENT = "AddToCart";

    public final static String FD_MODIFY_CART_EVENT = "ModifyCart";

    public final static String FD_REMOVE_CART_EVENT = "RemoveFromCart";

    private String productId;

    private String skuCode;

    private String categoryId;

    private String department;

    private String cartlineId;

    private String quantity;

    private String salesUnit;

    private String configuration;

    private String ymalCategory;

    private String originatingProductId;

    private String ymalSetId;

    private String customerCreatedListId;

    private String variantId;

    private String impressionId;

    public CartEvent(String eventType) {
        setEventType(eventType);
    }

    public String getProductId() {
        return productId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getDepartment() {
        return department;
    }

    public String getCartlineId() {
        return cartlineId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSalesUnit() {
        return salesUnit;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getYmalCategory() {
        return ymalCategory;
    }

    public String getOriginatingProductId() {
        return originatingProductId;
    }

    public String getYmalSetId() {
        return ymalSetId;
    }

    public String getCustomerCreatedListId() {
        return customerCreatedListId;
    }

    public String getVariantId() {
        return variantId;
    }

    public String getImpressionId() {
        return impressionId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setCartlineId(String cartlineId) {
        this.cartlineId = cartlineId;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setSalesUnit(String salesUnit) {
        this.salesUnit = salesUnit;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public void setYmalCategory(String ymalCategory) {
        this.ymalCategory = ymalCategory;
    }

    public void setOriginatingProductId(String originatingProductId) {
        this.originatingProductId = originatingProductId;
    }

    public void setYmalSetId(String ymalSetId) {
        this.ymalSetId = ymalSetId;
    }

    public void setCustomerCreatedListId(String customerCreatedListId) {
        this.customerCreatedListId = customerCreatedListId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public void setImpressionId(String impressionId) {
        this.impressionId = impressionId;
    }


}
