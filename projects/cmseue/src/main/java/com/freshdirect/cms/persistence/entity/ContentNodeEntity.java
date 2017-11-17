package com.freshdirect.cms.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity(name = "ContentNode")
@Table(name = "CONTENTNODE")
public class ContentNodeEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String contentKey;

    @Column(name = "contenttype_id", nullable = false)
    private String contentType;

    public ContentNodeEntity() {

    }

    public ContentNodeEntity(String contentKey, String contentType) {
        this.contentKey = contentKey;
        this.contentType = contentType;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String type) {
        this.contentType = type;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashcodeBuilder = new HashCodeBuilder();
        hashcodeBuilder.append(getContentKey());
        return hashcodeBuilder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContentNodeEntity) {
            ContentNodeEntity other = (ContentNodeEntity) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getContentKey(), other.getContentKey());
            return builder.isEquals();
        }
        return false;
    }

}
