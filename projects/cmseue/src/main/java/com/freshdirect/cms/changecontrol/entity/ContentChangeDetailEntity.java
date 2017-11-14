package com.freshdirect.cms.changecontrol.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * The bottom entity of content change entities
 * It captures changes belonging to the same content ID (content key)
 *
 * @author segabor
 */
@Embeddable
public class ContentChangeDetailEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "changetype", nullable = false, length = 4)
    private ContentChangeType changeType = ContentChangeType.MOD;

    @Column(name = "attributename", nullable = false, length = 40)
    private String attributeName;

    @Column(name = "oldvalue", nullable = true, length = 4000)
    private String oldValue;

    @Column(name = "newvalue", nullable = true, length = 4000)
    private String newValue;

    public ContentChangeDetailEntity() {
    }

    public ContentChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ContentChangeType changeType) {
        this.changeType = changeType;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
