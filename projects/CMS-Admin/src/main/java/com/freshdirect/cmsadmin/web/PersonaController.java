package com.freshdirect.cmsadmin.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cmsadmin.business.PersonaService;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.validation.PermissionValidator;
import com.freshdirect.cmsadmin.validation.PersonaValidator;
import com.freshdirect.cmsadmin.web.dto.PersonaPage;
import com.freshdirect.cmsadmin.web.dto.PersonaSelectablePage;

/**
 * Persona permission relationship controller.
 *
 * @author pkovacs
 */
@PreAuthorize("hasAuthority('PERMISSION_EDITOR')")
@RestController
public class PersonaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonaController.class);

    public static final String PERSONA_ACTION_PATH = "/api/persona/{id}";
    public static final String PERSONA_PAGE_PATH = "/api/persona";

    @Autowired
    private PermissionValidator permissionValidator;

    @Autowired
    private PersonaValidator personaValidator;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PageDecorator pageDecorator;

    /**
     * Add specific permission to given persona.
     *
     * @param persona
     *            Persona entity
     * @param permission
     *            Permission entity
     * @param result
     *            Autowired binding result
     * @return persona-permission page
     */
    @RequestMapping(value = PERSONA_ACTION_PATH, method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaPage addPermissionToPersona(@PathVariable("id") Persona persona, @RequestBody Permission permission, BindingResult result) {
        personaValidator.validate(persona, result);
        BindingResult permissionErrors = new BeanPropertyBindingResult(permission, "permission", true, 256);
        permissionValidator.validate(permission, permissionErrors);
        personaService.addPermissionToPersona(persona, permission);
        return populatePersonaSelectablePage();
    }

    /**
     * Create persona entity.
     *
     * @param persona
     *            Persona entity
     * @param result
     *            Autowired binding result
     * @return persona-permission page
     */
    @RequestMapping(value = PERSONA_PAGE_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaPage createPersona(@RequestBody Persona persona, BindingResult result) {
        personaValidator.validate(persona, result);
        personaService.createPersona(persona);
        return populatePersonaSelectablePage();
    }

    /**
     * Load persona-permission page.
     *
     * @return persona-permission page
     */
    @RequestMapping(value = PERSONA_PAGE_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaPage loadPersonaPage() {
        return populatePersonaSelectablePage();
    }

    /**
     * Remove given permission from the specified persona.
     *
     * @param persona
     *            Persona entity
     * @param permission
     *            Permission entity
     * @param result
     *            Autowired binding result
     * @return persona-permission page
     */
    @RequestMapping(value = PERSONA_ACTION_PATH, method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaPage removePermissionFromPersona(@PathVariable("id") Persona persona, @RequestBody Permission permission, BindingResult result) {
        permissionValidator.validate(permission, result);
        BindingResult personaErrors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        personaValidator.validate(persona, personaErrors);
        personaService.removePermissionFromPersona(persona, permission);
        return populatePersonaSelectablePage();
    }

    private PersonaSelectablePage populatePersonaSelectablePage() {
        return pageDecorator.decoratePersonaSelectablePage(new PersonaSelectablePage(), personaService.loadAllPersonas());
    }
}
