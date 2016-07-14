package com.freshdirect.cmsadmin.repository.ldap;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.repository.LdapRepository;

import com.freshdirect.cmsadmin.domain.User;

/**
 * Repository for LDAP users.
 */

public interface UserRepository extends LdapRepository<User> {

    /**
     * Search for User in LDAP by account name.
     *
     * @param accountName
     *            user's account name
     * @return User object
     */
    @Cacheable(cacheNames = "userCache")
    User findByAccountName(String accountName);

    @Cacheable(cacheNames = "userCache")
    @Override
    List<User> findAll(LdapQuery query);
}
