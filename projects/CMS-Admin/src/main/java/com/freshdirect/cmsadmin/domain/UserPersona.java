package com.freshdirect.cmsadmin.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * User entity to associated with persona.
 */
@Entity
public class UserPersona {

    @Id
    private String userId;
    private String name;
    @ManyToOne
    @PrimaryKeyJoinColumn
    private Persona persona;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getUserId());
        builder.append(getPersona());
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserPersona) {
            UserPersona other = (UserPersona) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getUserId(), other.getUserId());
            builder.append(getPersona(), other.getPersona());
            return builder.isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserPersona [userId=" + userId + ", persona=" + persona + "]";
    }
}
