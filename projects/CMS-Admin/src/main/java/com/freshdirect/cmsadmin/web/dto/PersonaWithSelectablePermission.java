package com.freshdirect.cmsadmin.web.dto;

import java.util.List;

import com.freshdirect.cmsadmin.domain.Persona;

public class PersonaWithSelectablePermission {

    private Persona persona;
    private List<PermissionSelectionWrapper> permissions;

    public List<PermissionSelectionWrapper> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionSelectionWrapper> permissions) {
        this.permissions = permissions;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
