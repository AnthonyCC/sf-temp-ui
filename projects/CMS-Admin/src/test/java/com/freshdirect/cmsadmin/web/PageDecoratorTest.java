package com.freshdirect.cmsadmin.web;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.business.MenuService;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.config.security.SecurityContextWrapper;
import com.freshdirect.cmsadmin.domain.MenuItem;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.web.dto.BasicPage;
import com.freshdirect.cmsadmin.web.dto.PersonaAssociationPage;
import com.freshdirect.cmsadmin.web.dto.PersonaPage;
import com.freshdirect.cmsadmin.web.dto.converter.UserToUserDataConverter;

/**
 * Unit test cases for page decorator methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PageDecoratorTest {

    private static final String USER_NAME = "username";

    @InjectMocks
    private PageDecorator underTest;

    @Mock
    private MenuService menuService;

    @Mock
    private UserToUserDataConverter userDataConverter;

    @Mock
    private SecurityContextWrapper securityContextWrapper;

    @Test
    public void testBasicPageContainsUserIdAndMenusWithValidUser() {
        List<MenuItem> menus = Arrays.asList(MenuItem.values());
        Mockito.when(securityContextWrapper.isUserAuthenticated()).thenReturn(true);
        Mockito.when(securityContextWrapper.getAuthenticatedUserName()).thenReturn(USER_NAME);
        Mockito.when(menuService.loadMenu(Matchers.anyString())).thenReturn(menus);
        BasicPage page = underTest.decorateBasicPage(new BasicPage());
        Assert.assertEquals(USER_NAME, page.getUser().getId());
        Assert.assertEquals(USER_NAME, page.getUser().getName());
        Assert.assertEquals(menus, page.getMenuItems());
    }

    @Test
    public void testBasicPageContainsDefaultMenusWithNoUserWithInvalidCredential() {
        Mockito.when(securityContextWrapper.isUserAuthenticated()).thenReturn(false);
        BasicPage page = underTest.decorateBasicPage(new BasicPage());
        Assert.assertNull(page.getUser());
        Assert.assertEquals(1, page.getMenuItems().size());
        Assert.assertEquals(MenuItem.DEFAULT_PAGE, page.getMenuItems().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBasicPageThrowExceptionWhenPageIsNull() {
        underTest.decorateBasicPage(null);
    }

    @Test
    public void testPersonaPageContainsUserIdAndMenusWithValidUser() {
        List<MenuItem> menus = Arrays.asList(MenuItem.values());
        List<Persona> personas = Arrays.asList(EntityFactory.createPersona());
        Mockito.when(securityContextWrapper.isUserAuthenticated()).thenReturn(true);
        Mockito.when(securityContextWrapper.getAuthenticatedUserName()).thenReturn(USER_NAME);
        Mockito.when(menuService.loadMenu(Matchers.anyString())).thenReturn(menus);
        PersonaPage page = underTest.decoratePersonaPage(new PersonaPage(), personas);
        Assert.assertEquals(USER_NAME, page.getUser().getId());
        Assert.assertEquals(USER_NAME, page.getUser().getName());
        Assert.assertEquals(menus, page.getMenuItems());
        Assert.assertEquals(personas, page.getPersonas());
    }

    @Test
    public void testPersonaPageContainsDefaultMenusWithNoUserWithInvalidCredential() {
        List<Persona> personas = Arrays.asList(EntityFactory.createPersona());
        Mockito.when(securityContextWrapper.isUserAuthenticated()).thenReturn(false);
        PersonaPage page = underTest.decoratePersonaPage(new PersonaPage(), personas);
        Assert.assertNull(page.getUser());
        Assert.assertEquals(1, page.getMenuItems().size());
        Assert.assertEquals(MenuItem.DEFAULT_PAGE, page.getMenuItems().get(0));
        Assert.assertNull(page.getPersonas());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonaPageThrowExceptionWhenPageIsNull() {
        underTest.decoratePersonaPage(null, null);
    }

    @Test
    public void testPersonaAssociationContainsUserIdAndMenusWithValidUser() {
        List<MenuItem> menus = Arrays.asList(MenuItem.values());
        List<UserPersona> userPersonas = Arrays.asList(EntityFactory.createUserPersona());
        List<User> unassociatedUsers = Arrays.asList(new User());
        Mockito.when(securityContextWrapper.isUserAuthenticated()).thenReturn(true);
        Mockito.when(securityContextWrapper.getAuthenticatedUserName()).thenReturn(USER_NAME);
        Mockito.when(menuService.loadMenu(Matchers.anyString())).thenReturn(menus);
        PersonaAssociationPage page = underTest.decoratePersonaAssociationPage(new PersonaAssociationPage(), userPersonas, unassociatedUsers);
        Assert.assertEquals(USER_NAME, page.getUser().getId());
        Assert.assertEquals(USER_NAME, page.getUser().getName());
        Assert.assertEquals(menus, page.getMenuItems());
        Assert.assertEquals(userPersonas, page.getUserPersonas());
        Mockito.verify(userDataConverter).convert(unassociatedUsers);
    }

    @Test
    public void testPersonaAssociationContainsDefaultMenusWithNoUserWithInvalidCredential() {
        List<UserPersona> userPersonas = Arrays.asList(EntityFactory.createUserPersona());
        List<User> unassociatedUsers = Arrays.asList(new User());
        Mockito.when(securityContextWrapper.isUserAuthenticated()).thenReturn(false);
        PersonaAssociationPage page = underTest.decoratePersonaAssociationPage(new PersonaAssociationPage(), userPersonas, unassociatedUsers);
        Assert.assertNull(page.getUser());
        Assert.assertEquals(1, page.getMenuItems().size());
        Assert.assertEquals(MenuItem.DEFAULT_PAGE, page.getMenuItems().get(0));
        Assert.assertNull(page.getUnassociatedUsers());
        Assert.assertNull(page.getUserPersonas());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPersonaAssociationPageThrowExceptionWhenPageIsNull() {
        underTest.decoratePersonaAssociationPage(null, null, null);
    }
}
