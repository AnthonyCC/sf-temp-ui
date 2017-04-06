package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.mobileapi.controller.data.response.FilterOption;
import com.freshdirect.mobileapi.model.AdProducts;
import com.freshdirect.mobileapi.util.SortType;

public class SearchResult extends Message {
    private String didYouMean;
    
    private String query = "";

    private Integer totalResultCount = 0;

    private List<ProductSearchResult> products = new ArrayList<ProductSearchResult>();
    private List<ProductSearchResult> favproducts = new ArrayList<ProductSearchResult>();

    private List<String> productIds;    
    private List<String> favProductIds;

    private List<FilterOption> brands = new ArrayList<FilterOption>();

    private List<FilterOption> departments = new ArrayList<FilterOption>();

    private List<FilterOption> categories = new ArrayList<FilterOption>();

    private List<SortType> sortOptions = new ArrayList<SortType>();
    
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
    
    public List<ProductSearchResult> getFavProducts() {
        return favproducts;
    }

    public void setFavProducts(List<ProductSearchResult> favproducts) {
        this.favproducts = favproducts;
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
    
    public void setFavProductsFromModel(List<com.freshdirect.mobileapi.model.Product> products) {
        this.favproducts.clear();
        for (com.freshdirect.mobileapi.model.Product product : products) {
            this.favproducts.add(ProductSearchResult.wrap(product));
        }
    }
    
    private List<AdProducts> adProducts = new ArrayList<AdProducts>();
	private String pageBeacon;
    
    public List<AdProducts> getAdProducts() {
		return adProducts;
	}

	public void setAdProducts(List<AdProducts> adProducts) {
		this.adProducts = adProducts;
	}
	public String getPageBeacon() {
		return pageBeacon;
	}

	public void setPageBeacon(String pageBeacon) {
		this.pageBeacon = pageBeacon;
	}
    public void setProductIds(List<String> productIds){
    	this.productIds = productIds;
    }
    
    public List<String> getProductIds(){
    	return this.productIds;
    }
    
    public void setFavProductIds(List<String> favpProductIds){
    	this.favProductIds = favpProductIds;
    }
    
    public List<String> getFavProductIds(){
    	return this.favProductIds;
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

    public void setSortOptions(List<SortType> sortOptions){
    	this.sortOptions = sortOptions;
    }
    
    public List<SortType> getSortOptions()
    {
    	return this.sortOptions;
    }
    
    public void setDefaultSortOptions()
    {
    	this.sortOptions.clear();
    	this.sortOptions.add(SortType.RELEVANCY);
    	this.sortOptions.add(SortType.NAME);
    	this.sortOptions.add(SortType.PRICE);
    	this.sortOptions.add(SortType.POPULARITY);
    	this.sortOptions.add(SortType.SALE);
    }
}