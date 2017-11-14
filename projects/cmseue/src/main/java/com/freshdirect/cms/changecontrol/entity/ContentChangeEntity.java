package com.freshdirect.cms.changecontrol.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.google.common.base.Optional;

/**
 * The middle entity of content change entities It captures changes belonging to the same content ID (content key)
 *
 * @author segabor
 */
@Entity(name = "ContentChange")
@Table(name = "CONTENTNODECHANGE")
public final class ContentChangeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contentChangeSequence")
    @SequenceGenerator(name = "contentChangeSequence", sequenceName = "cms_system_seq", initialValue = 1, allocationSize = 1)
    private Integer id;

    /**
     * The ID part of content key
     */
    @Column(name = "contentnode_id", nullable = false, length = 128)
    private String contentId;

    /**
     * The type part of content key
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "contenttype", nullable = false, length = 40)
    private ContentType contentType;

    /**
     * Content Change type
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "changetype", nullable = false, length = 4)
    private ContentChangeType changeType;

    /**
     * List of details
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CHANGEDETAIL", joinColumns = @JoinColumn(name = "CONTENTNODECHANGE_ID"))
    private List<ContentChangeDetailEntity> details;

    public ContentChangeEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ContentChangeType changeType) {
        this.changeType = changeType;
    }

    public List<ContentChangeDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(List<ContentChangeDetailEntity> details) {
        this.details = details;
    }

    public void setContentKey(ContentKey key) {
        setContentType(key.type);
        setContentId(key.id);
    }

    public Optional<ContentKey> getContentKey() {
        if (contentId == null || contentType == null) {
            return Optional.absent();
        }

        return Optional.of(ContentKeyFactory.get(contentType, contentId));
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashcodeBuilder = new HashCodeBuilder();
        hashcodeBuilder.append(getId());
        return hashcodeBuilder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContentChangeEntity) {
            ContentChangeEntity other = (ContentChangeEntity) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getId(), other.getId());
            return builder.isEquals();
        }
        return false;
    }

}
