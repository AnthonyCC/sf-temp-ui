package com.freshdirect.cmsadmin.config.security.inmemory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.freshdirect.cmsadmin.config.security.AuthoritiesLoader;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryUserDetailsService.class);

    @Autowired
    private InMemoryUserRegistry userRegistry;

    @Autowired
    private AuthoritiesLoader authoritiesLoader;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.debug("Loading inMemory user with username: " + username);
        InMemoryUser user = userRegistry.getInMemoryUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("This user does not exists!");
        }
        return buildUserDetails(user, authoritiesLoader.getGrantedAuthoritiesByUsername(username));
    }

    private UserDetails buildUserDetails(final InMemoryUser user, final List<? extends GrantedAuthority> authorities) {
        return new InMemoryUserDetails(authorities, user);
    }

}
