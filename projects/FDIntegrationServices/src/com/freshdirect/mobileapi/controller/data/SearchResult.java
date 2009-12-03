package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.response.FilterOption;

public class SearchResult extends Message {
    private String didYouMean;
    
    private String query = "";

    private Integer totalResultCount = 0;

    private List<ProductSearchResult> products = new ArrayList<ProductSearchResult>();

    private List<FilterOption> brands = new ArrayList<FilterOption>();

    private List<FilterOption> departments = new ArrayList<FilterOption>();

    private List<FilterOption> categories = new ArrayList<FilterOption>();

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getResultCount() {
        return products.size();
    }

    public List<ProductSearchResult> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSearchResult> products) {
        this.products = products;
    }

    public List<FilterOption> getBrands() {
        return brands;
    }

    public void setBrands(List<FilterOption> brands) {
        this.brands = brands;
    }

    public List<FilterOption> getDepartments() {
        return departments;
    }

    public void setDepartments(List<FilterOption> departments) {
        this.departments = departments;
    }

    public List<FilterOption> getCategories() {
        return categories;
    }

    public void setCategories(List<FilterOption> categories) {
        this.categories = categories;
    }

    public void setProductsFromModel(List<com.freshdirect.mobileapi.model.Product> products) {
        this.products.clear();
        for (com.freshdirect.mobileapi.model.Product product : products) {
            this.products.add(ProductSearchResult.wrap(product));
        }
    }

    public Integer getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(Integer totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public String getDidYouMean() {
        return didYouMean;
    }

    public void setDidYouMean(String didYouMean) {
        this.didYouMean = didYouMean;
    }

}
