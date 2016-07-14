package com.freshdirect.cmsadmin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.repository.config.EnableLdapRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.freshdirect.cmsadmin.config.security.CustomLdapAuthoritiesPopulator;

/**
 * Freshdirect LDAP-based security configuration.
 */
@Profile(value = { "developer", "schema"})
@EnableWebSecurity
@EnableLdapRepositories("com.freshdirect.cmsadmin.repository.ldap")
public class LdapBasedSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapBasedSecurityConfig.class);

    @Autowired
    private CustomLdapAuthoritiesPopulator customLdapAuthoritiesPopulator;

    @Autowired
    @Qualifier("ldapsource")
    private LdapContextSource ldapContextSource;

    /**
     * Configure LDAP authentication through Spring properties.
     *
     * @param authenticationManagerBuilder
     *            AuthenticationManagerBuilder authenticationManagerBuilder
     * @param loginUserSearchFilter
     *            String userSearchFilter
     * @param loginUserSearchBase
     *            String loginUserSearchBase
     *
     * @exception Exception
     *                if an error occurs when adding the LDAP authentication.
     */
    @Profile(value = { "developer", "schema"})
    @Autowired
    public void configureLdap(AuthenticationManagerBuilder authenticationManagerBuilder, @Value("${ldap.login_user_search_filter}") String loginUserSearchFilter,
            @Value("${ldap.login_user_search_base}") String loginUserSearchBase) throws Exception { // NOPMD
        LOGGER.info("configure LDAP based security!");
        authenticationManagerBuilder.ldapAuthentication().userSearchBase(loginUserSearchBase).userSearchFilter(loginUserSearchFilter).contextSource(ldapContextSource);
        LdapAuthenticationProviderConfigurer configurer = authenticationManagerBuilder.getConfigurer(LdapAuthenticationProviderConfigurer.class);
        configurer.ldapAuthoritiesPopulator(customLdapAuthoritiesPopulator).rolePrefix("");
    }

    /**
     * Create LdapQuery bean through Spring properties.
     *
     * @param cmsUserSearchBase
     *            String cmsUserSearchBase
     * @param cmsUserSearchFilter
     *            String cmsUserSearchFilter
     * @return ldapQuery
     */
    @Profile(value = { "developer", "schema"})
    @Bean(name = "ldapQuery")
    public LdapQuery ldapQuery(@Value("${ldap.cms_user_search_base}") String cmsUserSearchBase, @Value("${ldap.cms_user_search_filter}") String cmsUserSearchFilter) {
        return LdapQueryBuilder.query().base(cmsUserSearchBase).filter(cmsUserSearchFilter);
    }

}
