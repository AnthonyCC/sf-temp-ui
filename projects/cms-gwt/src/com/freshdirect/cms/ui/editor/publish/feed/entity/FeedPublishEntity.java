package com.freshdirect.cms.ui.editor.publish.feed.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "publishx")
public class FeedPublishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FeedPublishSequence")
    @SequenceGenerator(name = "FeedPublishSequence", sequenceName = "publishx_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(nullable = false, name = "cro_mod_date", unique = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(nullable = false, name = "user_id")
    private String userId;

    private String description;

    @Column(name = "last_modified")
    private Date lastModified;

    @Enumerated(EnumType.STRING)
    private PublishStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publishId", cascade = CascadeType.ALL)
    @OrderColumn(name = "sort_order", insertable = true)
    private List<FeedPublishMessageEntity> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return creationDate;
    }

    public void setTimestamp(Date timestamp) {
        this.creationDate = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public List<FeedPublishMessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<FeedPublishMessageEntity> messages) {
        this.messages = messages;
    }

    public void addMessage(FeedPublishMessageEntity message) {
        if (messages == null) {
            messages = new ArrayList<FeedPublishMessageEntity>();
        }
        messages.add(message);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Id=").append(id).append("]");
        stringBuilder.append("[TimeStamp=").append(creationDate).append("]");
        stringBuilder.append("[LastModified=").append(lastModified).append("]");
        stringBuilder.append("[UserId=").append(userId).append("]");
        stringBuilder.append("[Description=").append(description).append("]");
        stringBuilder.append("[Messages=").append(messages).append("]");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FeedPublishEntity) {
            FeedPublishEntity other = (FeedPublishEntity) obj;
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(id, other.getId());
            return equalsBuilder.isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(id).append(description).append(creationDate).append(userId).append(status);
        return hashCodeBuilder.hashCode();
    }

    @PrePersist
    private void beforeSave() {
        if (creationDate == null) {
            creationDate = new Date();
        }
        lastModified = new Date();
        if (messages != null) {
            for (int i = 0; i < messages.size(); i++) {
                messages.get(i).setSortOrder(i);
            }
        }
    }

}
