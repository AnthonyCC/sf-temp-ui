package com.freshdirect.cmsadmin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.freshdirect.cmsadmin.config.security.CustomAuthenticationFailureHandler;
import com.freshdirect.cmsadmin.config.security.CustomAuthenticationSuccessHandler;
import com.freshdirect.cmsadmin.config.security.CustomLogoutSuccessHandler;
import com.freshdirect.cmsadmin.web.DefaultViewController;
import com.freshdirect.cmsadmin.web.DraftChangeController;
import com.freshdirect.cmsadmin.web.DraftController;
import com.freshdirect.cmsadmin.web.PersonaUserAssociationController;

/**
 * FreshDirect security common configuration.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // NOPMD
        http.csrf().disable();
        http.authorizeRequests().antMatchers(HttpMethod.GET, DefaultViewController.DEFAULT_VIEW_PATH).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, PersonaUserAssociationController.USER_ASSOCIATED_PERSONA_PATH).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, DraftController.CMS_DRAFT_PATH).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, DraftController.CMS_DRAFT_ACTION_PATH).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, DraftChangeController.CMS_DRAFT_CHANGE_PATH).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, DraftChangeController.CMS_DRAFT_CHANGE_PATH).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, DraftChangeController.CMS_DRAFT_CHANGE_ACTION_PATH).permitAll();
        http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin().failureHandler(customAuthenticationFailureHandler)
                .successHandler(customAuthenticationSuccessHandler).permitAll();
        http.logout().logoutSuccessHandler(customLogoutSuccessHandler);
        http.exceptionHandling().authenticationEntryPoint(loginUrlAuthenticationEntryPoint());
    }

    /**
     * Create authenticationManager bean.
     *
     * @return authenticationManager
     */
    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception { // NOPMD
        return super.authenticationManagerBean();
    }

    /**
     * Create securityContextRepository bean.
     *
     * @return httpSessionSecurityContextRepository
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * Create loginUrlAuthenticationEntryPoint bean.
     *
     * @return loginUrlAuthenticationEntryPoint
     */
    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(DefaultViewController.DEFAULT_VIEW_PATH);
    }
}
