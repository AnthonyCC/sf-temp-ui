package com.freshdirect.cmsadmin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.freshdirect.cmsadmin.config.security.inmemory.InMemoryUser;
import com.freshdirect.cmsadmin.config.security.inmemory.InMemoryUserDetailsService;
import com.freshdirect.cmsadmin.config.security.inmemory.InMemoryUserRegistry;

/**
 * Freshdirect security configuration in developer environment.
 */
@Profile(value = { "developer", "euedge-dev-env", "fd-dev-env", "test" })
@EnableWebSecurity
public class InMemoryBasedSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryBasedSecurityConfig.class);

    @Autowired
    private InMemoryUserDetailsService userDetailsService;

    /**
     * Configure local security authenticationManager bean.
     *
     * @param authenticationManagerBuilder
     *            AuthenticationManagerBuilder authenticationManagerBuilder
     * @exception Exception
     *                if an error occurs when adding the inmemory authentication.
     */
    @Autowired
    public void configureLocal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception { // NOPMD
        LOGGER.info("configure in memory local security!");
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        for (InMemoryUser inMemoryUser : InMemoryUserRegistry.inMemoryUsers.values()) {
            authenticationManagerBuilder.inMemoryAuthentication().withUser(inMemoryUser.getUserName()).password(inMemoryUser.getPassword()).authorities("this_will_be_overriden!");
        }
    }
}
