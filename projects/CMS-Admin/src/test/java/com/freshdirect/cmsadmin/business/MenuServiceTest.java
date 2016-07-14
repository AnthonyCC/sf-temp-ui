package com.freshdirect.cmsadmin.business;

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
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.MenuItem;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.PermissionIdentifier;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.web.PersonaController;
import com.freshdirect.cmsadmin.web.PersonaUserAssociationController;

/**
 * Unit test cases for test MenuService methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class MenuServiceTest {

    @Mock
    private PersonaAssociationService personaAssociationService;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private MenuService underTest;

    @Test
    public void testLoadMenuReturnOnlyDefaultPageWithUnauthorizedUser() {
        Permission permission = EntityFactory.createPermission(PermissionIdentifier.PERMISSION_EDITOR.id, PermissionIdentifier.PERMISSION_EDITOR.name());
        Persona persona = EntityFactory.createPersona(permission);
        String userName = "rblush";
        UserPersona userPersona = EntityFactory.createUserPersona(userName, "rose blush", persona);
        Mockito.when(personaAssociationService.loadUserPersonaAssociation(userName)).thenReturn(userPersona);
        Mockito.when(permissionService.loadPermission(PermissionIdentifier.PERMISSION_EDITOR.id)).thenReturn(permission);
        List<MenuItem> menu = underTest.loadMenu("pkatkay");
        Assert.assertEquals(1, menu.size());
        Assert.assertEquals(MenuItem.DEFAULT_PAGE.getName(), menu.get(0).getName());
    }

    @Test
    public void testLoadMenuReturnPersonaUserAssocationPageWithValidLabel() {
        Permission permission = EntityFactory.createPermission(PermissionIdentifier.PERMISSION_EDITOR.id, PermissionIdentifier.PERMISSION_EDITOR.name());
        Persona persona = EntityFactory.createPersona(permission);
        String userName = "rblush";
        UserPersona userPersona = EntityFactory.createUserPersona(userName, "rose blush", persona);
        Mockito.when(personaAssociationService.loadUserPersonaAssociation(Matchers.anyString())).thenReturn(userPersona);
        Mockito.when(permissionService.loadPermission(PermissionIdentifier.PERMISSION_EDITOR.id)).thenReturn(permission);
        List<MenuItem> menu = underTest.loadMenu(userName);
        int defaultPageIndex = menu.indexOf(MenuItem.PERSONA_USER_ASSOCIATION_PAGE);
        Assert.assertEquals(MenuItem.PERSONA_USER_ASSOCIATION_PAGE.getName(), menu.get(defaultPageIndex).getName());
    }

    @Test
    public void testLoadMenuReturnPersonaUserAssocationPageWithValidPath() {
        Permission permission = EntityFactory.createPermission(PermissionIdentifier.PERMISSION_EDITOR.id, PermissionIdentifier.PERMISSION_EDITOR.name());
        Persona persona = EntityFactory.createPersona(permission);
        String userName = "rblush";
        UserPersona userPersona = EntityFactory.createUserPersona(userName, "rose blush", persona);
        Mockito.when(personaAssociationService.loadUserPersonaAssociation(Matchers.anyString())).thenReturn(userPersona);
        Mockito.when(permissionService.loadPermission(PermissionIdentifier.PERMISSION_EDITOR.id)).thenReturn(permission);
        List<MenuItem> menu = underTest.loadMenu(userName);
        int personaPageIndex = menu.indexOf(MenuItem.PERSONA_USER_ASSOCIATION_PAGE);
        Assert.assertEquals(PersonaUserAssociationController.USER_PERSONA_ASSOCIATION_PAGE_PATH, menu.get(personaPageIndex).getPath());
    }

    @Test
    public void testLoadMenuReturnPersonaPageWithValidLabel() {
        Permission permission = EntityFactory.createPermission(PermissionIdentifier.PERMISSION_EDITOR.id, PermissionIdentifier.PERMISSION_EDITOR.name());
        Persona persona = EntityFactory.createPersona(permission);
        String userName = "rblush";
        UserPersona userPersona = EntityFactory.createUserPersona(userName, "rose blush", persona);
        Mockito.when(personaAssociationService.loadUserPersonaAssociation(Matchers.anyString())).thenReturn(userPersona);
        Mockito.when(permissionService.loadPermission(PermissionIdentifier.PERMISSION_EDITOR.id)).thenReturn(permission);
        List<MenuItem> menu = underTest.loadMenu(userName);
        int personaPageIndex = menu.indexOf(MenuItem.PERSONA_PAGE);
        Assert.assertEquals(MenuItem.PERSONA_PAGE.getName(), menu.get(personaPageIndex).getName());
    }

    @Test
    public void testLoadMenuReturnPersonaPageWithValidPath() {
        Permission permission = EntityFactory.createPermission(PermissionIdentifier.PERMISSION_EDITOR.id, PermissionIdentifier.PERMISSION_EDITOR.name());
        Persona persona = EntityFactory.createPersona(permission);
        String userName = "rblush";
        UserPersona userPersona = EntityFactory.createUserPersona(userName, "rose blush", persona);
        Mockito.when(personaAssociationService.loadUserPersonaAssociation(Matchers.anyString())).thenReturn(userPersona);
        Mockito.when(permissionService.loadPermission(PermissionIdentifier.PERMISSION_EDITOR.id)).thenReturn(permission);
        List<MenuItem> menu = underTest.loadMenu(userName);
        int personaPageIndex = menu.indexOf(MenuItem.PERSONA_PAGE);
        Assert.assertEquals(PersonaController.PERSONA_PAGE_PATH, menu.get(personaPageIndex).getPath());
    }
}
