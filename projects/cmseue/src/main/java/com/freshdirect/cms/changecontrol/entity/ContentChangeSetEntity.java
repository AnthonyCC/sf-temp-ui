package com.freshdirect.cms.changecontrol.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The top entity of content change entities It records the fact that something has changed in CMS
 *
 * Attributes - timestamp: when did it happen - userId: who did that - note: some words about the change
 *
 * @author segabor
 *
 */
@Entity(name = "ChangeSet")
@Table(name = "CHANGESET")
public class ContentChangeSetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contentChangeSetSequence")
    @SequenceGenerator(name = "contentChangeSetSequence", sequenceName = "cms_system_seq", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(name = "timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "note", nullable = true)
    private String note;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "changeset_id", nullable = false)
    private List<ContentChangeEntity> changes;

    public ContentChangeSetEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ContentChangeEntity> getChanges() {
        return changes;
    }

    public void setChanges(List<ContentChangeEntity> changes) {
        this.changes = changes;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashcodeBuilder = new HashCodeBuilder();
        hashcodeBuilder.append(getId());
        return hashcodeBuilder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContentChangeSetEntity) {
            ContentChangeSetEntity other = (ContentChangeSetEntity) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getId(), other.getId());
            return builder.isEquals();
        }
        return false;
    }

}
