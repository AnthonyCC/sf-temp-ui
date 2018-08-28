package com.freshdirect.cms.ui.model.changeset;

import java.io.Serializable;

public class GwtChangeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String changeType;
    private String attributeName;
    private String oldValue;
    private String newValue;

    public GwtChangeDetail() {
    }

    public GwtChangeDetail(String changeType, String attributeName, String oldValue, String newValue) {
        this.changeType = changeType;
        this.attributeName = attributeName;
        this.oldValue = oldValue;
        this.newValue = newValue;
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

}
