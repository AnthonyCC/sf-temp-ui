package com.freshdirect.cmsadmin.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.business.PermissionService;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.PersonaValidator;

/**
 * Unit test cases for permission persona relationship controller.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PermissionControllerTest {

    @InjectMocks
    private PermissionController underTest;

    @Mock
    private PermissionService permissionService;

    @Mock
    private PersonaValidator personaValidator;

    @Test
    public void testPermissionPageLoadAvailablePermissions() {
        Permission permission = EntityFactory.createPermission();
        Permission otherPermission = EntityFactory.createPermission(EntityFactory.OTHER_ID, EntityFactory.OTHER_PERMISSION_NAME);
        ArrayList<Permission> allPermissions = new ArrayList<Permission>();
        allPermissions.add(permission);
        allPermissions.add(otherPermission);
        Persona persona = EntityFactory.createPersona(otherPermission);
        Mockito.when(permissionService.loadAllPermissions()).thenReturn(allPermissions);
        List<Permission> availablePermissions = underTest.loadUnselectedPermissionsToPersona(persona);
        Assert.assertEquals(1, availablePermissions.size());
        Assert.assertEquals(permission, availablePermissions.get(0));
    }

}
