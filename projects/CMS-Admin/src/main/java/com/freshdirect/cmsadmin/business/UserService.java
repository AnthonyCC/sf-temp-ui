package com.freshdirect.cmsadmin.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.domain.Sort;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.repository.jpa.UserPersonaRepository;
import com.freshdirect.cmsadmin.repository.ldap.UserRepository;

/**
 * Service for user.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LdapQuery cmsUserQuery;

    @Autowired
    private UserPersonaRepository userPersonaRepository;

    @Autowired
    private Sort sortByNameIgnoreCaseAsc;

    /**
     * Load all assigned permission for a specific user.
     *
     * @param user
     *            persona relation object
     * @return assigned permissions
     */
    public List<Permission> loadPermissions(UserPersona user) {
        Assert.notNull(user, "User can't be null.");
        return user.getPersona().getPermissions();
    }

    /**
     * Returns with all unassociated users.
     *
     * @return users
     */
    public List<User> loadUnassociatedUsers() {
        List<User> users = userRepository.findAll(cmsUserQuery);
        List<UserPersona> allUserPersonas = userPersonaRepository.findAll(sortByNameIgnoreCaseAsc);
        List<User> unassociatedUsers = filterUnassociatedUsers(users, allUserPersonas);
        PropertyComparator.sort(unassociatedUsers, new MutableSortDefinition("fullName", true, true));
        return unassociatedUsers;
    }

    /**
     * Returns user with given id.
     *
     * @param accountName
     *            user account name
     * @return user
     */
    public User loadUser(String accountName) {
       Assert.notNull(accountName, "User accountName can't be null.");
       return userRepository.findByAccountName(accountName);
    }

    private List<User> filterUnassociatedUsers(List<User> users, List<UserPersona> userPersonas) {
        List<User> unassociatedUsers = new ArrayList<User>(users);
        for (Iterator<User> iter = unassociatedUsers.listIterator(); iter.hasNext();) {
            String userId = iter.next().getAccountName();
            for (UserPersona userPersona : userPersonas) {
                if (userId.equalsIgnoreCase(userPersona.getUserId())) {
                    iter.remove();
                    continue;
                }
            }
        }
        return unassociatedUsers;
    }

}
