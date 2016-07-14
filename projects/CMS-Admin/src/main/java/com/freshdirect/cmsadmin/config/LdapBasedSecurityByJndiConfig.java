package com.freshdirect.cmsadmin.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * Freshdirect LDAP-based security configuration through JNDI.
 */
@Profile(value = { "euedge-dev-env", "fd-dev-env", "production" })
@EnableWebSecurity
@EnableLdapRepositories("com.freshdirect.cmsadmin.repository.ldap")
public class LdapBasedSecurityByJndiConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapBasedSecurityByJndiConfig.class);

    @Autowired
    private CustomLdapAuthoritiesPopulator customLdapAuthoritiesPopulator;

    @Autowired
    @Qualifier("ldapsource")
    private LdapContextSource ldapContextSource;

    /**
     * Configure LDAP authentication through JNDI.
     *
     * @param authenticationManagerBuilder
     *            AuthenticationManagerBuilder authenticationManagerBuilder
     *
     * @exception Exception
     *                if an error occurs when adding the LDAP authentication.
     */
    @Autowired
    public void jndiConfigureLdap(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception { // NOPMD
        Context ctx = new InitialContext();
        String loginUserSearchBase = (String) ctx.lookup("java:comp/env/ldap/loginusersearchbase");
        String loginUserSearchFilter = (String) ctx.lookup("java:comp/env/ldap/loginusersearchfilter");
        LOGGER.info("configure LDAP based security!");
        authenticationManagerBuilder.ldapAuthentication().userSearchBase(loginUserSearchBase).userSearchFilter(loginUserSearchFilter).contextSource(ldapContextSource);
        LdapAuthenticationProviderConfigurer configurer = authenticationManagerBuilder.getConfigurer(LdapAuthenticationProviderConfigurer.class);
        configurer.ldapAuthoritiesPopulator(customLdapAuthoritiesPopulator).rolePrefix("");
    }

    /**
     * Create LdapQuery bean through JNDI.
     *
     * @return ldapQuery
     */
    @Bean(name = "ldapQuery")
    public LdapQuery jndiLdapQuery() throws NamingException {
        Context ctx = new InitialContext();
        String cmsUserSearchBase = (String) ctx.lookup("java:comp/env/ldap/cmsusersearchbase");
        String cmsUserSearchFilter = (String) ctx.lookup("java:comp/env/ldap/cmsusersearchfilter");
        return LdapQueryBuilder.query().base(cmsUserSearchBase).filter(cmsUserSearchFilter);
    }
}
