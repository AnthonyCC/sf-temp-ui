package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.response.FilterGroup;
import com.freshdirect.mobileapi.model.AdProducts;
import com.freshdirect.webapp.ajax.browse.data.PagerData;
import com.freshdirect.webapp.ajax.browse.data.SortOptionData;

public class SearchWebResult extends Message {

    private String didYouMean;
    private String query;
    private int totalResultCount;
    private List<String> productIds;
    private List<String> favProductIds;
    private List<SortOptionData> sortOptions;
    private List<FilterGroup> filterGroups = new ArrayList<FilterGroup>();
    private List<AdProducts> adProducts;
    private String pageBeacon;
    private PagerData pager;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

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

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public List<String> getProductIds() {
        return this.productIds;
    }

    public void setFavProductIds(List<String> favpProductIds) {
        this.favProductIds = favpProductIds;
    }

    public List<String> getFavProductIds() {
        return this.favProductIds;
    }

    public int getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(int totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public String getDidYouMean() {
        return didYouMean;
    }

    public void setDidYouMean(String didYouMean) {
        this.didYouMean = didYouMean;
    }

    public void setSortOptions(List<SortOptionData> sortOptions) {
        this.sortOptions = sortOptions;
    }

    public List<SortOptionData> getSortOptions() {
        return this.sortOptions;
    }

    public List<FilterGroup> getFilterGroups() {
        return filterGroups;
    }

    public void setFilterGroups(List<FilterGroup> filterGroups) {
        this.filterGroups = filterGroups;
    }

    public PagerData getPager() {
        return pager;
    }

    public void setPager(PagerData pager) {
        this.pager = pager;
    }
}