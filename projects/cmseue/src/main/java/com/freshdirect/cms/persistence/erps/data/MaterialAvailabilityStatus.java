package com.freshdirect.cms.persistence.erps.data;

public enum MaterialAvailabilityStatus {

    AVAL("Available") /* never used*/,
    DISC("Discontinued"),
    TBDS("To be Discontinued soon"),
    TEST("Test Status"),
    UNAV("Temporarily Unavailable"),
    SEAS("Out Of Season");

    public final String description;

    MaterialAvailabilityStatus(String description) {
        this.description = description;
    }

    public static MaterialAvailabilityStatus valueOfName(String name) {
        for (MaterialAvailabilityStatus status : values()) {
            if (name.equalsIgnoreCase(status.name())) {
                return status;
            }
        }

        throw new IllegalArgumentException("No status enum found for name " + name);
    }
}
