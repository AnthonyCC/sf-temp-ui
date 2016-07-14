package com.freshdirect.cmsadmin.repository;

import javax.naming.Name;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TestLdapQuery implements LdapQuery {

    @Override
    public Name base() {
        return null;
    }

    @Override
    public SearchScope searchScope() {
        return null;
    }

    @Override
    public Integer timeLimit() {
        return null;
    }

    @Override
    public Integer countLimit() {
        return null;
    }

    @Override
    public String[] attributes() {
        return null;
    }

    @Override
    public Filter filter() {
        return null;
    }

}
