package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.catalog.model.Product;

public class ProductCatalogMessageResponse extends MessageResponse {

    private static final long serialVersionUID = -4220385142597682317L;

    private List<Product> products;
    private String plantId;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

}
