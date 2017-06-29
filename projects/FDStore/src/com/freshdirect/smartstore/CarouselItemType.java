package com.freshdirect.smartstore;

public enum CarouselItemType {

    GRID("grid"),
    PRODUCT_SAMPLE_GRID("grid_prodSample");

    private String type;

    private CarouselItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
