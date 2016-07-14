package com.freshdirect.cmsadmin.business;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.ldap.query.LdapQuery;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.repository.jpa.UserPersonaRepository;
import com.freshdirect.cmsadmin.repository.ldap.UserRepository;
import com.freshdirect.cmsadmin.utils.EntityFactory;

/**
 * Unit test cases for test UserService methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class UserServiceTest {

    private static final String FIRST_USER_ID = "userId";
    private static final String SECOND_USER_ID = "userId2";
    private static final String THIRD_USER_ID = "userId3";
    private static final String FIRST_USER_NAME = "userName";
    private static final String SECOND_USER_NAME = "userName2";
    private static final String THIRD_USER_NAME = "userName3";

    @Mock
    private LdapQuery cmsUserQuery;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPersonaRepository userPersonaRepository;

    @Mock
    private Sort sortByNameIgnoreCaseAsc;

    @InjectMocks
    private UserService underTest;

    @Test
    public void testLoadUnassociatedUsersWhereUserNotAssociatedToPersona() {
        List<User> users = Arrays.asList(EntityFactory.createUser(FIRST_USER_ID, FIRST_USER_NAME));
        List<UserPersona> userPersonas = Arrays.asList(EntityFactory.createUserPersona(SECOND_USER_ID, SECOND_USER_NAME, EntityFactory.createPersona()));
        Mockito.when(userRepository.findAll(cmsUserQuery)).thenReturn(users);
        Mockito.when(userPersonaRepository.findAll(sortByNameIgnoreCaseAsc)).thenReturn(userPersonas);
        List<User> unassociatedUsers = underTest.loadUnassociatedUsers();
        Assert.assertFalse(unassociatedUsers.isEmpty());
        Assert.assertEquals(unassociatedUsers.get(0).getFullName(), FIRST_USER_NAME);
    }

    @Test
    public void testLoadUnassociatedUsersWhereUserAssociatedToPersona() {
        List<User> users = Arrays.asList(EntityFactory.createUser(FIRST_USER_ID, FIRST_USER_NAME));
        List<UserPersona> userPersonas = Arrays.asList(EntityFactory.createUserPersona(FIRST_USER_ID, FIRST_USER_NAME, EntityFactory.createPersona()));
        Mockito.when(userRepository.findAll(cmsUserQuery)).thenReturn(users);
        Mockito.when(userPersonaRepository.findAll(sortByNameIgnoreCaseAsc)).thenReturn(userPersonas);
        List<User> unassociatedUsers = underTest.loadUnassociatedUsers();
        Assert.assertTrue(unassociatedUsers.isEmpty());
    }

    @Test
    public void testLoadUnassociatedUsersWhereSingleUserAssociatedToPersona() {
        List<User> users = Arrays.asList(EntityFactory.createUser(FIRST_USER_ID, FIRST_USER_NAME), EntityFactory.createUser(SECOND_USER_ID, SECOND_USER_NAME),
                EntityFactory.createUser(THIRD_USER_ID, THIRD_USER_NAME));
        List<UserPersona> userPersonas = Arrays.asList(EntityFactory.createUserPersona(FIRST_USER_ID, FIRST_USER_NAME, EntityFactory.createPersona()),
                EntityFactory.createUserPersona(SECOND_USER_ID, SECOND_USER_NAME, EntityFactory.createPersona()));
        Mockito.when(userRepository.findAll(cmsUserQuery)).thenReturn(users);
        Mockito.when(userPersonaRepository.findAll(sortByNameIgnoreCaseAsc)).thenReturn(userPersonas);
        List<User> unassociatedUsers = underTest.loadUnassociatedUsers();
        Assert.assertEquals(1, unassociatedUsers.size());
        Assert.assertEquals(unassociatedUsers.get(0).getFullName(), THIRD_USER_NAME);
    }

    @Test
    public void testLoadUnassociatedUsersWherePersonasAreEmpty() {
        List<User> users = Arrays.asList(EntityFactory.createUser(FIRST_USER_ID, FIRST_USER_NAME));
        List<UserPersona> userPersonas = Arrays.asList();
        Mockito.when(userRepository.findAll(cmsUserQuery)).thenReturn(users);
        Mockito.when(userPersonaRepository.findAll(sortByNameIgnoreCaseAsc)).thenReturn(userPersonas);
        List<User> unassociatedUsers = underTest.loadUnassociatedUsers();
        Assert.assertFalse(unassociatedUsers.isEmpty());
        Assert.assertEquals(unassociatedUsers.get(0).getFullName(), FIRST_USER_NAME);
    }

    @Test
    public void testLoadUnassociatedUsersWhereUsersAreEmpty() {
        List<User> users = Arrays.asList();
        List<UserPersona> userPersonas = Arrays.asList(EntityFactory.createUserPersona(FIRST_USER_ID, FIRST_USER_NAME, EntityFactory.createPersona()));
        Mockito.when(userRepository.findAll(cmsUserQuery)).thenReturn(users);
        Mockito.when(userPersonaRepository.findAll(sortByNameIgnoreCaseAsc)).thenReturn(userPersonas);
        List<User> unassociatedUsers = underTest.loadUnassociatedUsers();
        Assert.assertTrue(unassociatedUsers.isEmpty());
    }

    @Test
    public void testLoadSortedUnassociatedUsersWhereUsersWereUnsorted() {
        List<User> users = Arrays.asList(EntityFactory.createUser(FIRST_USER_ID, "xavier"), EntityFactory.createUser(SECOND_USER_ID, "adam"));
        List<UserPersona> userPersonas = Arrays.asList();
        Mockito.when(userRepository.findAll(cmsUserQuery)).thenReturn(users);
        Mockito.when(userPersonaRepository.findAll(sortByNameIgnoreCaseAsc)).thenReturn(userPersonas);
        List<User> unassociatedUsers = underTest.loadUnassociatedUsers();
        Assert.assertEquals(unassociatedUsers.size(), 2);
        Assert.assertEquals(unassociatedUsers.get(0).getFullName(), "adam");
        Assert.assertEquals(unassociatedUsers.get(1).getFullName(), "xavier");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadPermissionsWhenUserIsEmptyThrowsException() {
        UserPersona user = null;
        underTest.loadPermissions(user);
        Assert.fail();
    }

    @Test
    public void testLoadPermissionsWhenUserIsNotEmpty() {
        Permission permission = EntityFactory.createPermission(1L, "First permission");
        UserPersona user = EntityFactory.createUserPersona(FIRST_USER_ID, FIRST_USER_NAME, EntityFactory.createPersona(permission));
        List<Permission> result = underTest.loadPermissions(user);
        Assert.assertEquals(permission, result.get(0));
    }

    @Test
    public void testLoadUser() {
        String accountName = "accountName";
        underTest.loadUser(accountName);
        Mockito.verify(userRepository).findByAccountName(accountName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadUserWhenUserIsEmptyThrowsException() {
        underTest.loadUser(null);
        Assert.fail();
    }

}
