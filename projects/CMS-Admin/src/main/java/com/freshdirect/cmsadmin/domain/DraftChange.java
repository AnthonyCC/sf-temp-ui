package com.freshdirect.cmsadmin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class DraftChange {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draftChangeSequence")
    @SequenceGenerator(name = "draftChangeSequence", sequenceName = "DRAFT_CHANGE_SEQUENCE", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(/* name="user", */ length = 32)
    private String userName;
    private long createdAt;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Draft draft;
    @Column(/* name="content_key", */ length = 128)
    private String contentKey;
    @Column(/* name="attribute", */ length = 40)
    private String attributeName;
    @Column(length = 4000)
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
            return builder.isEquals();
        }
        return false;
    }
}
