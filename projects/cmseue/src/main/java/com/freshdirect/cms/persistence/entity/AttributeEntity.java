package com.freshdirect.cms.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity(name = "Attribute")
@Table(name = "ATTRIBUTE")
public class AttributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attributeSequence")
    @SequenceGenerator(name = "attributeSequence", sequenceName = "cms_system_seq", initialValue = 1, allocationSize = 1)
    private int id;

    @Column(name = "contentnode_id", nullable = false)
    private String contentKey;

    @Column(name = "value")
    private String value;

    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Column(name = "def_name", nullable = false)
    private String name;

    @Column(name = "def_contenttype", nullable = false)
    private String contentType;

    public AttributeEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "ContentKey: " + contentKey + ", Name: " + name + ", Value: " + value;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashcodeBuilder = new HashCodeBuilder();
        hashcodeBuilder.append(getId());
        return hashcodeBuilder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AttributeEntity) {
            AttributeEntity other = (AttributeEntity) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getId(), other.getId());
            return builder.isEquals();
        }
        return false;
    }
}
