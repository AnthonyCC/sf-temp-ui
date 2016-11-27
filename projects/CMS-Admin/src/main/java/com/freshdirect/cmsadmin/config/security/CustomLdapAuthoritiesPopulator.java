package com.freshdirect.cmsadmin.config.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

@Component
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLdapAuthoritiesPopulator.class);

    @Autowired
    private AuthoritiesLoader authoritiesLoader;

    @Override
    public List<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        LOGGER.info("#getGrantedAuthotorities");
        return authoritiesLoader.getGrantedAuthoritiesByUsername(username);
    }

}
