package com.freshdirect.cms.ui.editor.publish.entity;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishStatus;

@Entity
@Table(name="PUBLISH")
public class StorePublish {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StorePublishSequence")
    @SequenceGenerator(name = "StorePublishSequence", sequenceName = "publish_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name="user_id")
    private String userId;

    private String description;

    @Column(name="last_modified")
    private Date lastModified;

    @Enumerated(EnumType.STRING)
    private StorePublishStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publishId", cascade = CascadeType.ALL)
    @OrderColumn(name = "sort_order", insertable = true)
    private List<StorePublishMessage> messages;

    public StorePublish() {
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
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


    public StorePublishStatus getStatus() {
        return status;
    }


    public void setStatus(StorePublishStatus status) {
        this.status = status;
    }


    public List<StorePublishMessage> getMessages() {
        return messages;
    }


    public void setMessages(List<StorePublishMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodebuilder = new HashCodeBuilder();
        hashCodebuilder.append(id)
            .append(timestamp)
            .append(userId)
            .append(description)
            .append(status)
            .append(lastModified)
            .append(messages);
        return hashCodebuilder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StorePublish) {
            StorePublish other = (StorePublish) obj;
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(id, other.id)
                .append(timestamp, other.timestamp)
                .append(userId, other.userId)
                .append(description, other.description)
                .append(status, other.status)
                .append(lastModified, other.lastModified)
                .append(messages, other.messages);
            return equalsBuilder.isEquals();
        }
        return false;
    }

    public String lastInfoMessage() {
        String lastMessage = "running.";
        Date lastDate = null;
        for (StorePublishMessage m : this.getMessages()) {
            if (m.getSeverity() == StorePublishMessageSeverity.INFO) {
                if (lastDate == null || lastDate.before(m.getTimestamp())) {
                    if (m.getMessage() != null && m.getMessage().trim().length() > 0) {
                        lastMessage = m.getMessage();
                        lastDate = m.getTimestamp();
                    }
                }
            }
        }
        return lastMessage;
    }

}
