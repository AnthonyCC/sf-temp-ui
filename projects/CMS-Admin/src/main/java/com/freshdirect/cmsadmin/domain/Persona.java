package com.freshdirect.cmsadmin.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Persona entity.
 */
@Entity
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personaIdSequence")
    @SequenceGenerator(name = "personaIdSequence", sequenceName = "PERSONA_ID_SEQ", allocationSize = 1)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private List<Permission> permissions;

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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        builder.append(getName());
        builder.append(getPermissions());
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Persona) {
            Persona other = (Persona) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getId(), other.getId());
            builder.append(getName(), other.getName());
            builder.append(getPermissions(), other.getPermissions());
            return builder.isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Persona [id=" + id + ", name=" + name + ", permissions=" + permissions + "]";
    }
}
