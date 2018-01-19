package com.freshdirect.webapp.ajax.product.data;

public class ProductPotatoRequestData {

    private String categoryId;
    private String productId;
    private boolean extraField;
    private String groupId;
    private String groupVersion;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isExtraField() {
        return extraField;
    }

    public void setExtraField(boolean extraField) {
        this.extraField = extraField;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupVersion() {
        return groupVersion;
    }

    public void setGroupVersion(String groupVersion) {
        this.groupVersion = groupVersion;
    }

}
