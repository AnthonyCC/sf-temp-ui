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

@Entity(name = "Relationship")
@Table(name = "RELATIONSHIP")
public class RelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "relationshipSequence")
    @SequenceGenerator(name = "relationshipSequence", sequenceName = "cms_system_seq", initialValue = 1, allocationSize = 1)
    private int id;

    @Column(name = "parent_contentnode_id", nullable = false)
    private String relationshipSource;

    @Column(name = "def_name", nullable = false)
    private String relationshipName;

    @Column(name = "child_contentnode_id")
    private String relationshipDestination;

    @Column(name = "ordinal", nullable = false)
    private Integer ordinal;

    @Column(name = "def_contenttype", nullable = false)
    private String relationshipDestinationType;

    public RelationshipEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRelationshipSource() {
        return relationshipSource;
    }

    public void setRelationshipSource(String relationshipSource) {
        this.relationshipSource = relationshipSource;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getRelationshipDestination() {
        return relationshipDestination;
    }

    public void setRelationshipDestination(String relationshipDestionation) {
        this.relationshipDestination = relationshipDestionation;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getRelationshipDestinationType() {
        return relationshipDestinationType;
    }

    public void setRelationshipDestinationType(String relationshipDestinationType) {
        this.relationshipDestinationType = relationshipDestinationType;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashcodeBuilder = new HashCodeBuilder();
        hashcodeBuilder.append(getId());
        return hashcodeBuilder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RelationshipEntity) {
            RelationshipEntity other = (RelationshipEntity) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getRelationshipName(), other.getRelationshipName());
            builder.append(getOrdinal(), other.getOrdinal());
            builder.append(getRelationshipSource(), other.getRelationshipSource());
            builder.append(getRelationshipDestination(), other.getRelationshipDestination());
            return builder.isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return getRelationshipSource() + "--[" + getRelationshipName() + "]->" + getRelationshipDestination() + " (ordinal: " + getOrdinal() + ")";
    }
}
