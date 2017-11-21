package com.freshdirect.cms.ui.editor.permission.domain;

import com.freshdirect.cms.ui.editor.permission.converter.PermissionConverter;

public class PersonaConverter {

    private static final PersonaConverter INSTANCE = new PersonaConverter();

    private PersonaConverter() {
    }

    public static PersonaConverter defaultConverter() {
        return INSTANCE;
    }

    public Persona convert(PersonaConversionData personaConversionData) {
        Persona result = new Persona();
        result.setId(personaConversionData.getId());
        result.setName(personaConversionData.getName());
        result.setPermissions(PermissionConverter.defaultConverter().convert(personaConversionData.getPermissions()));
        return result;
    }
}
