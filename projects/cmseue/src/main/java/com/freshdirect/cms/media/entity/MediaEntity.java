package com.freshdirect.cms.media.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "media")
public class MediaEntity {

    @Id
    private String id;

    @Column(name = "uri", nullable = false)
    private String uri;

    private Integer width;

    private Integer height;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "last_modified")
    private Date lastModified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @PrePersist
    private void preSave() {
        lastModified = new Date();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Id: ").append(id).append("| Uri: ").append(uri).append(" (").append(width).append(" x ").append(height).append(" )").append("| mime: ")
                .append(mimeType).append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodebuilder = new HashCodeBuilder();
        hashCodebuilder.append(id).append(uri).append(type).append(mimeType).append(lastModified);
        return hashCodebuilder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MediaEntity) {
            MediaEntity other = (MediaEntity) obj;
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(id, other.getId()).append(uri, other.getUri()).append(type, other.getType()).append(lastModified, other.getLastModified()).append(mimeType,
                    other.getMimeType());
            return equalsBuilder.isEquals();
        }
        return false;
    }
}
