package com.freshdirect.mobileapi.controller.data.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.mobileapi.controller.data.Message;

public class SearchWebQuery extends Message {

    private String query;
    private String sortBy;
    private Map<String, List<String>> filterByIds = new HashMap<String, List<String>>();
    private boolean orderAscending;
    private int max;
    private int page;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Map<String, List<String>> getFilterByIds() {
        return filterByIds;
    }

    public void setFilterByIds(Map<String, List<String>> filterByIds) {
        this.filterByIds = filterByIds;
    }

    public boolean isOrderAscending() {
        return orderAscending;
    }

    public void setOrderAscending(boolean orderAscending) {
        this.orderAscending = orderAscending;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
