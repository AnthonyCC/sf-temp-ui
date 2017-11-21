package com.freshdirect.cms.contentvalidation.domain;

public enum ProductFilterType {
    AND,
    OR,
    ALLERGEN,
    BACK_IN_STOCK,
    BRAND,
    CLAIM,
    CUSTOMER_RATING,
    DOMAIN_VALUE,
    EXPERT_RATING,
    FRESHNESS,
    GLUTEN_FREE,
    KOSHER,
    NEW,
    NUTRITION,
    ON_SALE,
    ORGANIC,
    PRICE,
    SUSTAINABILITY_RATING,
    TAG;

    public static ProductFilterType toEnum(String type) {
        return valueOf(type);
    }
}
