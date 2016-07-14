package com.freshdirect.cmsadmin.config.security;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cmsadmin.business.PersonaAssociationService;
import com.freshdirect.cmsadmin.business.UserService;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.UserPersona;

@Service
public class AuthoritiesLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthoritiesLoader.class);

    @Autowired
    private PersonaAssociationService personaAssociationService;

    @Autowired
    private UserService userService;

    public List<Permission> getGrantedAuthoritiesByUsername(String username) {
        UserPersona userPersona = personaAssociationService.loadUserPersonaAssociation(username);
        List<Permission> grantedAuthorities = Collections.emptyList();
        if (userPersona != null) {
            grantedAuthorities = userService.loadPermissions(userPersona);
        }
        LOGGER.debug("Loaded authorities: " + grantedAuthorities + " for user: " + username);
        return grantedAuthorities;
    }
}
