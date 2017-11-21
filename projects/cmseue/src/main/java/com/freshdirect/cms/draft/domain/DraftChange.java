package com.freshdirect.cms.draft.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DraftChange {

    private Long id;
    private String userName;
    private long createdAt;

    private Draft draft;
    private String contentKey;
    private String attributeName;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Draft getDraft() {
        return draft;
    }

    public void setDraft(Draft draft) {
        this.draft = draft;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DraftChange) {
            DraftChange other = (DraftChange) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getId(), other.getId());
            builder.append(getCreatedAt(), other.getCreatedAt());
            builder.append(getUserName(), other.getUserName());
            builder.append(getContentKey(), other.getContentKey());
            builder.append(getAttributeName(), other.getAttributeName());
            builder.append(getValue(), other.getValue());
            builder.append(getDraft(), other.getDraft());
            return builder.isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id).append("::").append(contentKey).append("#").append(attributeName).append(" ~> ").append(value).append(" @").append(createdAt).append(" by ")
                .append(userName)
                .append(" on branch ").append(draft.getName());
        return stringBuilder.toString();
    }
}
