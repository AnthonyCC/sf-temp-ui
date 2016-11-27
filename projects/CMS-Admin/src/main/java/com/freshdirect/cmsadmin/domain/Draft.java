package com.freshdirect.cmsadmin.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Draft {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "draftSequence")
    @SequenceGenerator(name = "draftSequence", sequenceName = "DRAFT_SEQUENCE", initialValue = 1, allocationSize = 1)
    private Long id;
    private String name;

    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "last_modified_date")
    private Date lastModifiedAt;

    @Enumerated(EnumType.STRING)
    private DraftStatus draftStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public DraftStatus getDraftStatus() {
        return draftStatus;
    }

    public void setDraftStatus(DraftStatus draftStatus) {
        this.draftStatus = draftStatus;
    }


    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Draft) {
            Draft other = (Draft) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getId(), other.getId());
            return builder.isEquals();
        }
        return false;
    }
    
    @PrePersist
    public void onInsert() {
        createdAt = new Date();
        lastModifiedAt = createdAt;
        draftStatus = DraftStatus.CREATED;
    }

    @PreUpdate
    public void onUpdate() {
        lastModifiedAt = new Date();
    }
}
