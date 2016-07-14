package com.freshdirect.cmsadmin.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.repository.jpa.PersonaRepository;
import com.freshdirect.cmsadmin.utils.EntityFactory;

/**
 * Unit test cases for test PersonaService methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PersonaService underTest;

    @Test
    public void testLoadAllPersonas() {
        Mockito.when(personaRepository.findAll()).thenReturn(Arrays.asList(new Persona()));
        List<Persona> allPersonas = underTest.loadAllPersonas();
        Assert.assertFalse(allPersonas.isEmpty());
    }

    @Test
    public void testCreatePersona() {
        Persona persona = EntityFactory.createPersona();
        underTest.createPersona(persona);
        Mockito.verify(personaRepository).save(persona);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePersonaWithNullObject() {
        underTest.createPersona(null);
        Assert.fail("Service accepts null persona!!!");
    }

    @Test
    public void testAddPermissionToPersona() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona();
        underTest.addPermissionToPersona(persona, permission);
        Assert.assertFalse(persona.getPermissions().isEmpty());
        Mockito.verify(personaRepository).save(persona);
    }

    @Test
    public void testAddNullPermissionToPersona() {
        Persona persona = EntityFactory.createPersona();
        try {
            underTest.addPermissionToPersona(persona, null);
            Assert.fail("Service accepts null permission!!!");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("permission can't be null", e.getMessage());
            Mockito.verify(personaRepository, Mockito.times(0)).save(persona);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPermissionToNullPersona() {
        Permission permission = EntityFactory.createPermission();
        underTest.addPermissionToPersona(null, permission);
        Assert.fail("Service accepts null persona!!!");
    }

    @Test
    public void testRemovePermissionFromPersona() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(permission);
        underTest.removePermissionFromPersona(persona, permission);
        Assert.assertTrue(persona.getPermissions().isEmpty());
        Mockito.verify(personaRepository).save(persona);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullPermissionFromPersona() {
        Persona persona = EntityFactory.createPersona();
        underTest.removePermissionFromPersona(persona, null);
        Assert.fail("Service accepts null permission!!!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemovePermissionFromNullPersona() {
        Permission permission = EntityFactory.createPermission();
        underTest.removePermissionFromPersona(null, permission);
        Assert.fail("Service accepts null persona!!!");
    }

    @Test
    public void testPopulateAvailablePermissionsForPersona() {
        Permission permission = EntityFactory.createPermission();
        Permission otherPermission = EntityFactory.createPermission(EntityFactory.OTHER_ID, EntityFactory.OTHER_PERMISSION_NAME);
        ArrayList<Permission> allPermissions = new ArrayList<Permission>();
        allPermissions.add(permission);
        allPermissions.add(otherPermission);
        Persona persona = EntityFactory.createPersona(otherPermission);
        Mockito.when(personaRepository.findOne(EntityFactory.ID)).thenReturn(persona);
        Mockito.when(permissionService.loadAllPermissions()).thenReturn(allPermissions);
        List<Permission> availablePermissions = underTest.populateAvailablePermissionsForPersona(EntityFactory.ID);
        Assert.assertEquals(1, availablePermissions.size());
        Assert.assertEquals(permission, availablePermissions.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPopulateAvailablePermissionsForPersonaWithNullId() {
        underTest.populateAvailablePermissionsForPersona(null);
        Assert.fail("Service accepts null id!!!");
    }
}
