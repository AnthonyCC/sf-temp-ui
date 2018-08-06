package com.freshdirect.webapp.autosuggest.unbxd.dto;
import com.freshdirect.fdstore.customer.UnbxdAutosuggestResults;

import java.util.List;

public class UnbxdAutosuggestResponse {

    private List<UnbxdAutosuggestResults> products;

    public List<UnbxdAutosuggestResults> getProducts() {
        return products;
    }

    public void setProducts(List<UnbxdAutosuggestResults> products) {
        this.products = products;
    }
}
