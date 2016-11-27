package com.freshdirect.cmsadmin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cmsadmin.business.PermissionService;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.validation.PersonaValidator;

/**
 * Permission persona relationship controller.
 *
 * @author pkovacs
 */
@PreAuthorize("hasAuthority('PERMISSION_EDITOR')")
@RestController
public class PermissionController {

    public static final String PERMISSION_PERSONA_ACTION_PATH = "/api/permission/persona/{id}";

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PersonaValidator personaValidator;

    /**
     * Get all unselected permissions for the given persona.
     *
     * @param persona
     *            Persona entity
     * @return unselected permissions for the given persona
     */
    @RequestMapping(value = PERMISSION_PERSONA_ACTION_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Permission> loadUnselectedPermissionsToPersona(@PathVariable("id") Persona persona) {
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        personaValidator.validate(persona, errors);
        List<Permission> availablePermissions = permissionService.loadAllPermissions();
        availablePermissions.removeAll(persona.getPermissions());
        return availablePermissions;
    }
}
