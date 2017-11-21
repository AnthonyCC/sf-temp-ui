package com.freshdirect.cms.changecontrol.entity;

/**
 * This enum defines attribute / entity change type
 *
 * @author segabor
 *
 */
public enum ContentChangeType {
    CRE("Create"),
    DEL("Delete"),
    MOD("Modify");

    public final String description;

    ContentChangeType(String description) {
        this.description = description;
    }
}
