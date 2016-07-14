package com.freshdirect.cmsadmin.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Permission entity.
 */
@Entity
public class Permission implements GrantedAuthority {

    private static final long serialVersionUID = -3352322830585911475L;

    @Id
    private Long id;
    private String name;

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

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            Permission other = (Permission) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getId(), other.getId());
            return builder.isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Permission [id=" + id + ", name=" + name + "]";
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return PermissionIdentifier.getPermissionIdentifierById(id);
    }

}
