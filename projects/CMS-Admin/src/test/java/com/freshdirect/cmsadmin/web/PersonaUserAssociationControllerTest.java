package com.freshdirect.cmsadmin.web;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.business.PersonaAssociationService;
import com.freshdirect.cmsadmin.business.UserService;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.exception.ValidationException;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.PersonaValidator;
import com.freshdirect.cmsadmin.validation.UserPersonaValidator;
import com.freshdirect.cmsadmin.validation.UserValidator;
import com.freshdirect.cmsadmin.web.dto.GroupedPersonaAssociationPage;

/**
 * Unit test cases for user persona association relationship controller.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PersonaUserAssociationControllerTest {

    @Mock private PersonaAssociationService personaAssociationService;
    @Mock private UserService userService;
    @Mock private UserPersonaValidator userPersonaValidator;
    @Mock private BindingResult result;
    @Mock private PageDecorator pageDecorator;
    @Mock private UserValidator userValidator;
    @Mock private PersonaValidator personaValidator;

    @InjectMocks private PersonaUserAssociationController underTest;

    private List<UserPersona> userPersonas;
    private List<User> unassociatedUsers;

    @Before
    public void setUp() {
        userPersonas = Arrays.asList(EntityFactory.createUserPersona());
        unassociatedUsers = Arrays.asList(EntityFactory.createUser("id", "name"));

        Mockito.when(userService.loadUnassociatedUsers()).thenReturn(unassociatedUsers);
        Mockito.when(personaAssociationService.loadAllUserPersonaAssociations()).thenReturn(userPersonas);
        Mockito.when(pageDecorator.decorateGroupedPersonaAssociationPage(Mockito.any(GroupedPersonaAssociationPage.class), Mockito.eq(userPersonas), Mockito.eq(unassociatedUsers)))
                .thenReturn(new GroupedPersonaAssociationPage());
    }

    @Test
    public void testLoadPersonaAssociationPage() {
        underTest.loadPersonaAssociationPage();
        Mockito.verify(pageDecorator).decorateGroupedPersonaAssociationPage(Mockito.any(GroupedPersonaAssociationPage.class), Mockito.eq(userPersonas),
                Mockito.eq(unassociatedUsers));
    }

    @Test
    public void loadUserAssociatedPermissions() {
        Persona persona = EntityFactory.createPersona();
        UserPersona user = EntityFactory.createUserPersona("id", "name", persona);
        Assert.assertEquals(persona, underTest.loadUserAssociatedPermissions(user));
    }
    
    @Test(expected = ValidationException.class)
    public void loadUserAssociatedPermissionsWhenUserPersonaIsNull() {
        Mockito.doThrow(new ValidationException()).when(userPersonaValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        underTest.loadUserAssociatedPermissions(null);
        Assert.fail();
    }

    @Test
    public void testLoadUsersAssociatedWithGivenPersona() {
        Persona persona = EntityFactory.createPersona();
        Mockito.when(personaAssociationService.loadAllUserPersonaAssociations(persona)).thenReturn(userPersonas);
        underTest.loadUsersAssociatedWithPersona(persona);
        Mockito.verify(pageDecorator).decorateGroupedPersonaAssociationPage(Mockito.any(GroupedPersonaAssociationPage.class), Mockito.eq(userPersonas),
                Mockito.eq(unassociatedUsers));
    }

    @Test(expected = ValidationException.class)
    public void testLoadUsersAssociatedWhenPersonaIsNull() {
        Mockito.doThrow(new ValidationException()).when(personaValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        underTest.loadUsersAssociatedWithPersona(null);
        Assert.fail();
    }

    @Test
    public void testRemoveUserPersonaAssociation() {
        UserPersona deletedUserPersona = EntityFactory.createUserPersona();
        underTest.removeUserPersonaAssociation(deletedUserPersona);
        Mockito.verify(personaAssociationService).removeUserPersonaAssociation(deletedUserPersona);
        Mockito.verify(pageDecorator).decorateGroupedPersonaAssociationPage(Mockito.any(GroupedPersonaAssociationPage.class), Mockito.eq(userPersonas),
                Mockito.eq(unassociatedUsers));
    }

    @Test(expected = ValidationException.class)
    public void testRemoveUserPersonaAssociationWhenUserPersonaIsEmpty() {
        Mockito.doThrow(new ValidationException()).when(userPersonaValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        underTest.removeUserPersonaAssociation(null);
        Assert.fail();
    }

    @Test
    public void testAddUserPersonaAssociation() {
        UserPersona userPersona = EntityFactory.createUserPersona("userId", "userName", EntityFactory.createPersona());
        Mockito.when(userService.loadUser("userId")).thenReturn(EntityFactory.createUser("userId", "userName"));
        underTest.addUserPersonaAssociation(userPersona, result);
        Mockito.verify(personaAssociationService).addUserPersonaAssociation(userPersona);
        Mockito.verify(pageDecorator).decorateGroupedPersonaAssociationPage(Mockito.any(GroupedPersonaAssociationPage.class), Mockito.eq(userPersonas),
                Mockito.eq(unassociatedUsers));
    }

    @Test(expected = ValidationException.class)
    public void testAddUserPersonaAssociationWhenUserIsNotInLdap() {
        UserPersona userPersona = EntityFactory.createUserPersona("userId", "userName", EntityFactory.createPersona());
        Mockito.when(userService.loadUser("userId")).thenReturn(null);
        Mockito.doThrow(new ValidationException()).when(userValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        underTest.addUserPersonaAssociation(userPersona, result);
        Assert.fail();
    }
}
