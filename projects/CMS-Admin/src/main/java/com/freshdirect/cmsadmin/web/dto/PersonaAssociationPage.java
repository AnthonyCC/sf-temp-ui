package com.freshdirect.cmsadmin.web.dto;

import java.util.List;

import com.freshdirect.cmsadmin.config.security.dto.UserData;
import com.freshdirect.cmsadmin.domain.UserPersona;

/**
 * Persona association page with menu and user persona lists.
 */
public class PersonaAssociationPage extends BasicPage {

    private List<UserPersona> userPersonas;
    private List<UserData> unassociatedUsers;

    public List<UserPersona> getUserPersonas() {
        return userPersonas;
    }

    public void setUserPersonas(List<UserPersona> userPersonas) {
        this.userPersonas = userPersonas;
    }

    public List<UserData> getUnassociatedUsers() {
        return unassociatedUsers;
    }

    public void setUnassociatedUsers(List<UserData> unassociatedUsers) {
        this.unassociatedUsers = unassociatedUsers;
    }

}
