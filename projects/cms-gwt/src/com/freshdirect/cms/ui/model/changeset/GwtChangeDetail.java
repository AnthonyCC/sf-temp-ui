package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;

public class GwtChangeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String changeType;
    private String attributeName;
    private String oldValue;
    private String newValue;
    private String mergeValue;

    public GwtChangeDetail() {
    }

    public GwtChangeDetail(String changeType, String attributeName, String oldValue, String newValue, String mergeValue) {
        this.changeType = changeType;
        this.attributeName = attributeName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.mergeValue = mergeValue;
    }

    public GwtChangeDetail(String changeType, String attributeName, String oldValue, String newValue) {
        this(changeType, attributeName, oldValue, newValue, null);
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String name) {
        this.attributeName = name;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getMergeValue() {
        return mergeValue;
    }

    public void setMergeValue(String mergeValue) {
        this.mergeValue = mergeValue;
    }
}
