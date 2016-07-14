package com.freshdirect.cmsadmin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Profile(value = { "developer", "euedge-dev-env", "fd-dev-env", "production", "schema" })
@Configuration
public class LdapTemplateConfig {

    @Autowired
    private LdapContextSource ldapContextSource;
    /**
     * Create LdapTemplate bean.
     *
     * @return ldapTemplate
     */
    @Bean
    public LdapTemplate ldapTemplate() {
        LdapTemplate ldapTemplate = new LdapTemplate();
        ldapTemplate.setContextSource(ldapContextSource);
        return ldapTemplate;
    }
}
