package com.freshdirect.mobileapi.controller.data;

import java.text.SimpleDateFormat;

public class Sku {
    public enum ContentType {
        FD_SOURCE("fdSource"), FD_GRADE("fdGrade"), FD_FRENCHING("fdFrenching"), FD_RIPENESS("fdRipeness");

        String value;

        private ContentType(String value) {
            this.value = value;
        }

        public static ContentType getContentType(String value) {
            ContentType result = null;
            ContentType[] types = ContentType.values();
            for (ContentType type : types) {
                if (type.value.equals(value)) {
                    result = type;
                }
            }
            return result;
        }
    }

    private ContentType fdContentType;

    private String domainLabel;

    private String salesUnitDescription;

    private String displayAboutPrice;

    private double price;

    private String priceUnit;

    private double basePrice;

    private String basePriceUnit;

    private String skuRating;

    private String scaledPrices;

    private boolean hasWasPrice;

    private boolean defaultSku;

    private String earliestAvailability;

    private String code;

    public Sku() {
    }

    public String getEarliestAvailability() {
        return earliestAvailability;
    }

    public static Sku wrap(com.freshdirect.mobileapi.model.Sku sku) {
        Sku result = new Sku();
        result.price = sku.getPrice();
        result.priceUnit = sku.getPriceUnit();
        result.basePrice = sku.getBasePrice();
        result.basePriceUnit = sku.getBasePriceUnit();
        result.skuRating = sku.getRating();
        result.hasWasPrice = sku.hasWasPrice();
        result.domainLabel = sku.getDomainLabel();
        result.salesUnitDescription = sku.getSalesUnitDescription();
        result.displayAboutPrice = sku.getDisplayAboutPrice();

        if (sku.getFilteredEarliestAvailability() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(DateFormat.STANDARDIZED_DATE_FORMAT);
            result.earliestAvailability = formatter.format(sku.getFilteredEarliestAvailability());
        }

        result.setFdContentType(ContentType.getContentType(sku.getFdContentType()));
        StringBuffer buff = new StringBuffer();
        for (String scaledPrice : sku.getScaledPrices()) {
            buff.append(scaledPrice);
            buff.append(" ");
        }
        result.scaledPrices = buff.toString();
        result.setCode(sku.getSkuCode());
        return result;
    }

    public String getDomainLabel() {
        return domainLabel;
    }

    public void setDomainLabel(String domainLabel) {
        this.domainLabel = domainLabel;
    }

    public String getSalesUnitDescription() {
        return salesUnitDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getSkuRating() {
        return skuRating;
    }

    public void setSkuRating(String skuRating) {
        this.skuRating = skuRating;
    }

    public String getScaledPrices() {
        return scaledPrices;
    }

    public void setScaledPrices(String scaledPrices) {
        this.scaledPrices = scaledPrices;
    }

    public String getBasePriceUnit() {
        return basePriceUnit;
    }

    public void setBasePriceUnit(String basePriceUnit) {
        this.basePriceUnit = basePriceUnit;
    }

    public ContentType getFdContentType() {
        return fdContentType;
    }

    public void setFdContentType(ContentType fdContentType) {
        this.fdContentType = fdContentType;
    }

    public boolean isHasWasPrice() {
        return hasWasPrice;
    }

    public void setHasWasPrice(boolean hasWasPrice) {
        this.hasWasPrice = hasWasPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isDefaultSku() {
        return defaultSku;
    }

    public void setDefaultSku(boolean defaultSku) {
        this.defaultSku = defaultSku;
    }

    public String getDisplayAboutPrice() {
        return displayAboutPrice;
    }

    public void setDisplayAboutPrice(String displayAboutPrice) {
        this.displayAboutPrice = displayAboutPrice;
    }

}
