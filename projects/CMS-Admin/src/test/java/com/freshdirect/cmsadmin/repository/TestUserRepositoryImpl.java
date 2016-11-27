package com.freshdirect.cmsadmin.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.Name;

import org.springframework.context.annotation.Profile;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Repository;

import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.repository.ldap.UserRepository;

@Profile("test")
@Repository
public class TestUserRepositoryImpl implements UserRepository {

    private List<User> users;

    public TestUserRepositoryImpl() {
        users = new ArrayList<User>();
        users.add(createUser("admin"));
        users.add(createUser("user"));
    }

    @Override
    public User findOne(LdapQuery ldapQuery) {
        return users.get(0);
    }

    @Override
    public <S extends User> S save(S entity) {
        users.add(entity);
        return entity;
    }

    @Override
    public <S extends User> Iterable<S> save(Iterable<S> entities) {
        for (S entity : entities) {
            users.add(entity);
        }
        return entities;
    }

    @Override
    public User findOne(Name id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean exists(Name id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return new ArrayList<User>(users);
    }

    @Override
    public Iterable<User> findAll(Iterable<Name> ids) {
        return null;
    }

    @Override
    public long count() {
        return users.size();
    }

    @Override
    public void delete(Name id) {
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            if (user.getId().equals(id)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void delete(User entity) {
        users.remove(entity);
    }

    @Override
    public void delete(Iterable<? extends User> entities) {
        users.remove(entities);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public User findByAccountName(String accountName) {
        for (User user : users) {
            if (user.getAccountName().equals(accountName)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll(LdapQuery query) {
        return new ArrayList<User>(users);
    }

    private User createUser(String name) {
        User user = new User();
        user.setId("dn=" + name);
        user.setAccountName(name);
        user.setFullName(name);
        return user;
    }

}
