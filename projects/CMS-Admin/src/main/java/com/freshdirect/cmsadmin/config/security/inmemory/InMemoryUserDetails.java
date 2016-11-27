package com.freshdirect.cmsadmin.config.security.inmemory;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class InMemoryUserDetails implements UserDetails {

    private static final long serialVersionUID = -4668461317189865280L;
    private List<? extends GrantedAuthority> authorities;

    private InMemoryUser user;

    public InMemoryUserDetails(List<? extends GrantedAuthority> authorities, InMemoryUser user) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE;
    }

}
