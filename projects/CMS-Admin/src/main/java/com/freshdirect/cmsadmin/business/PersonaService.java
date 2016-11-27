package com.freshdirect.cmsadmin.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.repository.jpa.PersonaRepository;

/**
 * Service for persona.
 */
@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PermissionService permissionService;

    /**
     * Returns with all personas.
     *
     * @return allPersonas
     */
    public List<Persona> loadAllPersonas() {
        return personaRepository.findAll();
    }

    /**
     * Return with all available (not used) permissions for the specific persona.
     *
     * @param id
     *            Long id
     * @return availablePermissions
     */
    public List<Permission> populateAvailablePermissionsForPersona(Long id) {
        Assert.notNull(id, "id can't be null");
        Persona persona = personaRepository.findOne(id);
        List<Permission> availablePermissions = permissionService.loadAllPermissions();
        availablePermissions.removeAll(persona.getPermissions());
        return availablePermissions;
    }

    /**
     * Update given persona with adding given permission.
     *
     * @param persona
     *            Persona persona
     * @param permission
     *            Permission permission
     */
    public void addPermissionToPersona(Persona persona, Permission permission) {
        Assert.notNull(persona, "persona can't be null");
        Assert.notNull(permission, "permission can't be null");
        persona.getPermissions().add(permission);
        createPersona(persona);
    }

    /**
     * Update given persona with removing given permission.
     *
     * @param persona
     *            Persona persona
     * @param permission
     *            Permission permission
     */
    public void removePermissionFromPersona(Persona persona, Permission permission) {
        Assert.notNull(persona, "persona can't be null");
        Assert.notNull(permission, "permission can't be null");
        persona.getPermissions().remove(permission);
        createPersona(persona);
    }

    /**
     * Update given persona in DB.
     *
     * @param persona
     *            Persona persona
     */
    @Transactional
    public void createPersona(Persona persona) {
        Assert.notNull(persona, "persona can't be null");
        personaRepository.save(persona);
    }

}
