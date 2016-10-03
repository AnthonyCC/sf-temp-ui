package com.freshdirect.mobileapi.controller.data.response;

import java.util.Collection;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.util.SortType;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;

public class WebSearchResult extends Message {

    private String query;
    private String didYouMean;
    private Integer totalResultCount = 0;
    private List<ProductPotatoData> products;
    private List<FilterOption> categories;
    private Collection<FilterOption> departments;
    private Collection<FilterOption> brands;
    private List<SortType> sortOptions;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDidYouMean() {
        return didYouMean;
    }

    public void setDidYouMean(String didYouMean) {
        this.didYouMean = didYouMean;
    }

    public List<ProductPotatoData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductPotatoData> products) {
        this.products = products;
    }

    public List<FilterOption> getCategories() {
        return categories;
    }

    public void setCategories(List<FilterOption> categories) {
        this.categories = categories;
    }

    public Collection<FilterOption> getDepartments() {
        return departments;
    }

    public void setDepartments(Collection<FilterOption> departments) {
        this.departments = departments;
    }

    public Collection<FilterOption> getBrands() {
        return brands;
    }

    public void setBrands(Collection<FilterOption> brands) {
        this.brands = brands;
    }

    public List<SortType> getSortOptions() {
        return sortOptions;
    }

    public void setSortOptions(List<SortType> sortOptions) {
        this.sortOptions = sortOptions;
    }

    public Integer getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(Integer totalResultCount) {
        this.totalResultCount = totalResultCount;
    }
}
