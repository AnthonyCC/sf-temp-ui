package com.freshdirect.webapp.autosuggest.unbxd.dto;

import java.util.List;

public class UnbxdAutosuggestResponse {

    private List<UnbxdAutosuggestResultProduct> products;

    public List<UnbxdAutosuggestResultProduct> getProducts() {
        return products;
    }

    public void setProducts(List<UnbxdAutosuggestResultProduct> products) {
        this.products = products;
    }
}
