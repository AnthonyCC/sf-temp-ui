package com.freshdirect.cmsadmin.web.dto;

import java.util.List;

import com.freshdirect.cmsadmin.config.security.dto.UserData;
import com.freshdirect.cmsadmin.domain.Persona;

public class GroupedUserPersona {

    private Persona persona;
    private List<UserData> users;

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<UserData> getUsers() {
        return users;
    }

    public void setUsers(List<UserData> users) {
        this.users = users;
    }
}
