package com.freshdirect.cmsadmin.web.dto;

import java.util.List;

import com.freshdirect.cmsadmin.domain.UserPersona;

public class GroupedPersonaAssociationPage extends PersonaAssociationPage {

    private List<GroupedUserPersona> groupedUserPersonas;

    public List<GroupedUserPersona> getGroupedUserPersonas() {
        return groupedUserPersonas;
    }

    public void setGroupedUserPersonas(List<GroupedUserPersona> groupedUserPersonas) {
        this.groupedUserPersonas = groupedUserPersonas;
    }

    @Override
    public List<UserPersona> getUserPersonas() {
        return super.getUserPersonas();
    }
}
