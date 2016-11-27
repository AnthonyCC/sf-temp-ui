package com.freshdirect.cmsadmin.validator;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.PersonaValidator;
import com.freshdirect.cmsadmin.validation.ValidationService;

/**
 * Unit test cases for persona validator methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PersonaValidatorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private PersonaValidator underTest;

    @Test
    public void testValidatorAssigneToPersonaClass() {
        Assert.assertTrue(underTest.supports(Persona.class));
    }

    @Test
    public void testValidatorNotAssigneToOtherClasses() {
        Assert.assertFalse(underTest.supports(Object.class));
    }

    @Test
    public void testValidateHttpMethodGetWhenPersonaPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("GET");
        underTest.validate(persona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodGetWhenPersonaIsNull() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("GET");
        underTest.validate(null, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodGetWhenPersonaIdIsNull() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(null, "name", permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("GET");
        underTest.validate(persona, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPutWhenPersonaPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("PUT");
        underTest.validate(persona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodDeleteWhenPersonaPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("DELETE");
        underTest.validate(persona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodDeleteWhenPersonaIsNull() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("DELETE");
        underTest.validate(null, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodDeleteWhenPersonaIdIsNull() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(null, "name", permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("DELETE");
        underTest.validate(persona, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenPersonaPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(persona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenPersonaIdIsNull() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(null, "name", permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(persona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodOtherWhenPersonaPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        Persona persona = EntityFactory.createPersona(permission);
        BindingResult errors = new BeanPropertyBindingResult(persona, "persona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("HEAD");
        underTest.validate(persona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }
}
