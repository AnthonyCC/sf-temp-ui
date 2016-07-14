package com.freshdirect.cmsadmin.web.dto;

import java.util.List;

import com.freshdirect.cmsadmin.domain.Persona;

public class PersonaSelectablePage extends PersonaPage {

    private List<PersonaWithSelectablePermission> selectablePersonas;

    @Override
    public List<Persona> getPersonas() {
        return super.getPersonas();
    }

    public List<PersonaWithSelectablePermission> getSelectablePersonas() {
        return selectablePersonas;
    }

    public void setSelectablePersonas(List<PersonaWithSelectablePermission> selectablePersonas) {
        this.selectablePersonas = selectablePersonas;
    }
}
