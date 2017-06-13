package com.freshdirect.webapp.ajax.viewcart.service;

public enum CarouselItemType {

    GRID("grid"),
    SAMPLE_PRODUCT_GRID("grid_prodSample");

    private String type;

    private CarouselItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
