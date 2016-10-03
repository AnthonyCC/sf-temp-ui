package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.Category;
import com.freshdirect.mobileapi.model.Department;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;

public class WebBrowseResult extends Message {

    private Integer totalResultCount = 0;
    private Integer resultCount = 0;
    private List<ProductPotatoData> products = new ArrayList<ProductPotatoData>();
    private List<Department> departments = new ArrayList<Department>();
    private List<Category> categories = new ArrayList<Category>();
    private Map<String, SortedSet<String>> filters = new LinkedHashMap<String, SortedSet<String>>();
    private boolean isBottomLevel = false;
    private Idea featuredCard = null;
    private String fullName;

    public Integer getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(Integer totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public List<ProductPotatoData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductPotatoData> products) {
        this.products = products;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Map<String, SortedSet<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, SortedSet<String>> filters) {
        this.filters = filters;
    }

    public boolean isBottomLevel() {
        return isBottomLevel;
    }

    public void setBottomLevel(boolean isBottomLevel) {
        this.isBottomLevel = isBottomLevel;
    }

    public Idea getFeaturedCard() {
        return featuredCard;
    }

    public void setFeaturedCard(Idea featuredCard) {
        this.featuredCard = featuredCard;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
