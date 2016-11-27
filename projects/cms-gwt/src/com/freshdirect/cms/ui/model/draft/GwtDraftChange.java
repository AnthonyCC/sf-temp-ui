package com.freshdirect.cms.ui.model.draft;

import java.io.Serializable;
import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class GwtDraftChange implements Serializable {

    private static final long serialVersionUID = 2834595486615092645L;

    private Long draftId;
    private Date createdAt;
    private String userName;
    private String contentKey;
    private String attributeName;
    private String changedValue;
    private String validationError;

    public GwtDraftChange() {
    }

    public Long getDraftId() {
        return draftId;
    }

    public void setDraftId(Long draftId) {
        this.draftId = draftId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getChangedValue() {
        return changedValue;
    }

    public void setChangedValue(String changedValue) {
        this.changedValue = changedValue;
    }

    public String getValidationError() {
        return validationError;
    }

    public void setValidationError(String validationError) {
        this.validationError = validationError;
    }

    public BaseModelData toModelData() {
        BaseModelData data = new BaseModelData();

        data.set("id", draftId);
        data.set("createdAt", createdAt);
        data.set("userName", userName);
        data.set("contentKey", contentKey);
        data.set("attributeName", attributeName);
        data.set("changedValue", changedValue);
        data.set("validationError", validationError);

        return data;
    }
}
