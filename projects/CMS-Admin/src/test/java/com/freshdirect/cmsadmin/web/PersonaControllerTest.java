package com.freshdirect.cmsadmin.web;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.business.PersonaService;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.exception.ValidationException;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.PermissionValidator;
import com.freshdirect.cmsadmin.validation.PersonaValidator;
import com.freshdirect.cmsadmin.web.dto.PersonaPage;
import com.freshdirect.cmsadmin.web.dto.PersonaSelectablePage;

/**
 * Unit test cases for persona permission relationship controller.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PersonaControllerTest {

    @InjectMocks
    private PersonaController underTest;

    @Mock
    private PersonaService personaService;

    @Mock
    private BindingResult result;

    @Mock
    private PersonaValidator personaValidator;

    @Mock
    private PermissionValidator permissionValidator;

    @Mock
    private PageDecorator pageDecorator;

    @Test
    public void testLoadPersonaPage() {
        List<Persona> personas = Arrays.asList(EntityFactory.createPersona());
        Mockito.when(pageDecorator.decoratePersonaSelectablePage(Mockito.any(PersonaSelectablePage.class), Mockito.eq(personas))).thenReturn(new PersonaSelectablePage());
        Mockito.when(personaService.loadAllPersonas()).thenReturn(personas);
        underTest.loadPersonaPage();
        Mockito.verify(pageDecorator).decoratePersonaSelectablePage(Mockito.any(PersonaSelectablePage.class), Mockito.eq(personas));
    }

    @Test
    public void testCreatePersona() {
        Persona persona = EntityFactory.createPersona();
        List<Persona> personas = Arrays.asList(persona);
        Mockito.when(pageDecorator.decoratePersonaSelectablePage(Mockito.any(PersonaSelectablePage.class), Mockito.eq(personas))).thenReturn(new PersonaSelectablePage());
        Mockito.when(personaService.loadAllPersonas()).thenReturn(personas);
        PersonaPage personaPage = underTest.createPersona(persona, result);
        Mockito.verify(personaService).createPersona(persona);
        Mockito.verify(pageDecorator).decoratePersonaSelectablePage(Mockito.any(PersonaSelectablePage.class), Mockito.eq(personas));
        Assert.assertNotNull(personaPage);
    }

    @Test(expected = ValidationException.class)
    public void testCreatePersonaWhenPersonaIsNull() {
        Mockito.doThrow(new ValidationException()).when(personaValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        underTest.createPersona(null, result);
        Assert.fail();
    }

    @Test
    public void testAddPermissionToPersona() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona();
        List<Persona> personas = Arrays.asList(persona);
        Mockito.when(pageDecorator.decoratePersonaSelectablePage(Mockito.any(PersonaSelectablePage.class), Mockito.eq(personas))).thenReturn(new PersonaSelectablePage());
        Mockito.when(personaService.loadAllPersonas()).thenReturn(personas);
        PersonaPage personaPage = underTest.addPermissionToPersona(persona, permission, result);
        Mockito.verify(personaService).addPermissionToPersona(persona, permission);
        Mockito.verify(pageDecorator).decoratePersonaSelectablePage(Mockito.any(PersonaSelectablePage.class), Mockito.eq(personas));
        Assert.assertNotNull(personaPage);
    }

    @Test(expected = ValidationException.class)
    public void testAddPermissionToPersonaWhenPersonaIsNull() {
        Mockito.doThrow(new ValidationException()).when(personaValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        Permission permission = EntityFactory.createPermission();
        underTest.addPermissionToPersona(null, permission, result);
        Assert.fail();
    }

    @Test(expected = ValidationException.class)
    public void testAddPermissionToPersonaWhenPermissionIsNull() {
        Mockito.doThrow(new ValidationException()).when(permissionValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        Persona persona = EntityFactory.createPersona();
        underTest.addPermissionToPersona(persona, null, result);
        Assert.fail();
    }

    @Test
    public void testRemovePermissionFromPersona() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona();
        List<Persona> personas = Arrays.asList(persona);
        Mockito.when(personaService.loadAllPersonas()).thenReturn(personas);
        Mockito.when(pageDecorator.decoratePersonaSelectablePage(Mockito.any(PersonaSelectablePage.class), Mockito.eq(personas))).thenReturn(new PersonaSelectablePage());
        PersonaPage personaPage = underTest.removePermissionFromPersona(persona, permission, result);
        Mockito.verify(personaService).removePermissionFromPersona(persona, permission);
        Mockito.verify(pageDecorator).decoratePersonaSelectablePage(Mockito.any(PersonaSelectablePage.class), Mockito.eq(personas));
        Assert.assertNotNull(personaPage);
    }

    @Test(expected = ValidationException.class)
    public void testRemovePermissionFromPersonaWhenPersonaIsNull() {
        Permission permission = EntityFactory.createPermission();
        Mockito.doThrow(new ValidationException()).when(personaValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        underTest.removePermissionFromPersona(null, permission, result);
        Assert.fail();
    }

    @Test(expected = ValidationException.class)
    public void testRemovePermissionFromPersonaWhenPermissionIsNull() {
        Persona persona = EntityFactory.createPersona();
        Mockito.doThrow(new ValidationException()).when(permissionValidator).validate(Mockito.any(), Mockito.any(BindingResult.class));
        underTest.removePermissionFromPersona(persona, null, result);
        Assert.fail();
    }

}
