package com.freshdirect.webapp.search.unbxd.dto;

import java.util.List;

public class UnbxdSearchResponse {

    private Integer numberOfProducts;
    private List<UnbxdSearchResponseProduct> products;

    public List<UnbxdSearchResponseProduct> getProducts() {
        return products;
    }

    public void setProducts(List<UnbxdSearchResponseProduct> products) {
        this.products = products;
    }

    public Integer getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(Integer numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }
}
