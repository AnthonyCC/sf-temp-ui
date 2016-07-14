package com.freshdirect.cmsadmin.web.dto.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.freshdirect.cmsadmin.business.PermissionService;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.web.dto.PermissionSelectionWrapper;
import com.freshdirect.cmsadmin.web.dto.PersonaWithSelectablePermission;

@Component
public class PersonaToSelectablePermissionPersonaConverter implements Converter<List<Persona>, List<PersonaWithSelectablePermission>> {

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<PersonaWithSelectablePermission> convert(List<Persona> source) {
        List<PersonaWithSelectablePermission> result = new ArrayList<PersonaWithSelectablePermission>();
        List<Permission> allPermissions = permissionService.loadAllPermissions();
        for (Persona persona : source) {
            PersonaWithSelectablePermission personaWrapper = new PersonaWithSelectablePermission();
            List<PermissionSelectionWrapper> permissions = new ArrayList<PermissionSelectionWrapper>();
            for (Permission permission : allPermissions) {
                PermissionSelectionWrapper selectablePermission = new PermissionSelectionWrapper();
                selectablePermission.setPermission(permission);
                selectablePermission.setSelected(persona.getPermissions().contains(permission));
                permissions.add(selectablePermission);
            }
            PropertyComparator.sort(permissions, new MutableSortDefinition("permission.name", true, true));
            personaWrapper.setPermissions(permissions);
            personaWrapper.setPersona(persona);
            result.add(personaWrapper);
        }
        PropertyComparator.sort(result, new MutableSortDefinition("persona.name", true, true));
        return result;
    }

}
