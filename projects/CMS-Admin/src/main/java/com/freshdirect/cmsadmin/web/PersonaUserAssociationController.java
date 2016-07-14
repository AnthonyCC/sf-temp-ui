package com.freshdirect.cmsadmin.web;

import java.util.List;

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

import com.freshdirect.cmsadmin.business.PersonaAssociationService;
import com.freshdirect.cmsadmin.business.UserService;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.validation.PersonaValidator;
import com.freshdirect.cmsadmin.validation.UserPersonaValidator;
import com.freshdirect.cmsadmin.validation.UserValidator;
import com.freshdirect.cmsadmin.web.dto.GroupedPersonaAssociationPage;
import com.freshdirect.cmsadmin.web.dto.PersonaAssociationPage;

/**
 * User-Persona association controller.
 *
 * @author pkovacs
 *
 */
@PreAuthorize("hasAuthority('PERMISSION_EDITOR')")
@RestController
public class PersonaUserAssociationController {

    public static final String USER_ASSOCIATED_PERSONA_PATH = "/api/user/{id}/persona";
    public static final String USER_PERSONA_ASSOCIATION_PAGE_PATH = "/api/associatepersona";
    public static final String USER_PERSONA_ASSOCATIE_ACTION_PATH = "/api/associatepersona/{id}";

    @Autowired
    private UserPersonaValidator userPersonaValidator;

    @Autowired
    private PersonaValidator personaValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private PersonaAssociationService personaAssociationService;

    @Autowired
    private PageDecorator pageDecorator;

    @Autowired
    private UserValidator userValidator;

    /**
     * Add User-Persona association.
     *
     * @param userPersona
     *            user-persona association
     *
     * @param result
     *            Autowired binding result
     * @return persona association page
     */
    @RequestMapping(value = USER_PERSONA_ASSOCIATION_PAGE_PATH, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaAssociationPage addUserPersonaAssociation(@RequestBody UserPersona userPersona, BindingResult result) {
        userPersonaValidator.validate(userPersona, result);
        User user = userService.loadUser(userPersona.getUserId());
        BindingResult errors = new BeanPropertyBindingResult(user, "user", true, 256);
        userValidator.validate(user, errors);
        userPersona.setName(user.getFullName());
        personaAssociationService.addUserPersonaAssociation(userPersona);
        List<UserPersona> userPersonasAssociatedWithPersona = personaAssociationService.loadAllUserPersonaAssociations();
        return populateGroupedPersonaAssociationPage(userPersonasAssociatedWithPersona);
    }

    /**
     * List all user-persona association.
     *
     * @return persona association page
     */

    @RequestMapping(value = USER_PERSONA_ASSOCIATION_PAGE_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaAssociationPage loadPersonaAssociationPage() {
        List<UserPersona> userPersonasAssociatedWithPersona = personaAssociationService.loadAllUserPersonaAssociations();
        return populateGroupedPersonaAssociationPage(userPersonasAssociatedWithPersona);
    }

    /**
     * Load permissions associated to User via Persona.
     *
     * @param user
     *            the actual user
     * @return associated permissions
     */
    @PreAuthorize("permitAll()")
    @RequestMapping(value = USER_ASSOCIATED_PERSONA_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Persona loadUserAssociatedPermissions(@PathVariable("id") UserPersona user) {
        BindingResult errors = new BeanPropertyBindingResult(user, "user", true, 256);
        userPersonaValidator.validate(user, errors);
        return user.getPersona();
    }

    /**
     * Load User-Persona association.
     *
     * @param persona
     *            Persona persona
     * @return persona association page
     */
    @RequestMapping(value = USER_PERSONA_ASSOCATIE_ACTION_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaAssociationPage loadUsersAssociatedWithPersona(@PathVariable("id") Persona persona) {
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        personaValidator.validate(persona, errors);
        List<UserPersona> userPersonasAssociatedWithPersona = personaAssociationService.loadAllUserPersonaAssociations(persona);
        return populateGroupedPersonaAssociationPage(userPersonasAssociatedWithPersona);
    }

    /**
     * Remove User-Persona association.
     *
     * @param userPersona
     *            user-persona association
     * @return persona association page
     */

    @RequestMapping(value = USER_PERSONA_ASSOCATIE_ACTION_PATH, method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonaAssociationPage removeUserPersonaAssociation(@PathVariable("id") UserPersona userPersona) {
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "user", true, 256);
        userPersonaValidator.validate(userPersona, errors);
        personaAssociationService.removeUserPersonaAssociation(userPersona);
        List<UserPersona> userPersonasAssociatedWithPersona = personaAssociationService.loadAllUserPersonaAssociations();
        return populateGroupedPersonaAssociationPage(userPersonasAssociatedWithPersona);
    }

    private GroupedPersonaAssociationPage populateGroupedPersonaAssociationPage(List<UserPersona> userPersonas) {
        return pageDecorator.decorateGroupedPersonaAssociationPage(new GroupedPersonaAssociationPage(), userPersonas, userService.loadUnassociatedUsers());
    }

}
