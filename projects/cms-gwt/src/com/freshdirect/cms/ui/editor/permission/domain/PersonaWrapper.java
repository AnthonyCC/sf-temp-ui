package com.freshdirect.cms.ui.editor.permission.domain;

public class PersonaWrapper {

    private Persona persona;
    private boolean updateUser;

    public PersonaWrapper(Persona persona, boolean updateUser) {
        this.persona = persona;
        this.updateUser = updateUser;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public boolean isUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(boolean updateUser) {
        this.updateUser = updateUser;
    }

}
