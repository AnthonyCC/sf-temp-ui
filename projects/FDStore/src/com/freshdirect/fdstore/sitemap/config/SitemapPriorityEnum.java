package com.freshdirect.fdstore.sitemap.config;

public enum SitemapPriorityEnum {

    HIGHEST(1.0d),
    MIDDLE(0.5d),
    LOWEST(0.0d);

    private Double priority;

    private SitemapPriorityEnum(Double priority) {
        this.priority = priority;
    }

    public Double getPriority() {
        return priority;
    }
}
