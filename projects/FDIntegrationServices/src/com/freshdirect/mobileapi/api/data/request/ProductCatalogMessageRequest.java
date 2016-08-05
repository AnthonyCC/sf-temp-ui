package com.freshdirect.mobileapi.api.data.request;

import java.util.ArrayList;
import java.util.List;

public class ProductCatalogMessageRequest {

    private String source;
    private String catalogKey;
    private List<String> productIds = new ArrayList<String>();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCatalogKey() {
        return catalogKey;
    }

    public void setCatalogKey(String catalogKey) {
        this.catalogKey = catalogKey;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

}
