package com.freshdirect.webapp.ajax.carousel;

public enum CarouselType {

    CART("cart"),
    YMAL("ymal"),
    DEALS("deals"),
    SEARCH("search"),
    CHECKOUT("checkout"),
    QUICKSHOP("quickshop"),
    PRESIDENT_PICKS("pres-picks");

    private String type;

    private CarouselType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static CarouselType fromString(String type) {
        for (CarouselType carouselType : CarouselType.values()) {
            if (carouselType.type.equalsIgnoreCase(type)) {
                return carouselType;
            }
        }
        return QUICKSHOP;
    }
}
