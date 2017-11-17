package com.freshdirect.cms.core.domain;

/**
 * Common attribute properties
 *
 * REQUIRED - attribute value must exists
 * INHERITABLE - attribute is part of bottom-up inheritance chain
 * NONE - there's no flag is set on this attribute
 */
public enum AttributeFlags {
    NONE, REQUIRED, INHERITABLE;

    public static AttributeFlags valueOf(boolean required, boolean inheritable) {

        if (required && inheritable) {
            throw new IllegalArgumentException("Both required and inheritable are not allowed");
        }

        if (required) {
            return REQUIRED;
        } else if (inheritable) {
            return INHERITABLE;
        } else {
            return NONE;
        }
    }

    public boolean isRequired() {
        return this == REQUIRED;
    }

    public boolean isInheritable() {
        return this == INHERITABLE;
    }
}
