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
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.UserPersonaValidator;
import com.freshdirect.cmsadmin.validation.ValidationService;

/**
 * Unit test cases for user persona validator methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class UserPersonaValidatorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private UserPersonaValidator underTest;

    @Test
    public void testValidatorAssigneToUserPersonaClass() {
        Assert.assertTrue(underTest.supports(UserPersona.class));
    }

    @Test
    public void testValidatorNotAssigneToOtherClasses() {
        Assert.assertFalse(underTest.supports(Object.class));
    }

    @Test
    public void testValidateHttpMethodGetWhenUserPersonaPropertiesAreOk() {
        UserPersona userPersona = EntityFactory.createUserPersona();
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("GET");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodGetWhenUserPersonaIsNull() {
        UserPersona userPersona = EntityFactory.createUserPersona();
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("GET");
        underTest.validate(null, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodGetWhenUserPersonaIdIsNull() {
        UserPersona userPersona = EntityFactory.createUserPersona(null, "name", null);
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("GET");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPutWhenUserPersonaPropertiesAreOk() {
        UserPersona userPersona = EntityFactory.createUserPersona();
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("PUT");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodDeleteWhenUserPersonaPropertiesAreOk() {
        UserPersona userPersona = EntityFactory.createUserPersona();
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("DELETE");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodDeleteWhenUserPersonaUserIdIsNull() {
        UserPersona userPersona = EntityFactory.createUserPersona(null, "name", null);
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("DELETE");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodDeleteWhenUserPersonaIsNull() {
        UserPersona userPersona = null;
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("DELETE");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenUserPersonaPropertiesAreOk() {
        UserPersona userPersona = EntityFactory.createUserPersona();
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenUserPersonaUserIdIsNull() {
        UserPersona userPersona = EntityFactory.createUserPersona(null, "name", null);
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenUserPersonaPersonaIdIsNull() {
        UserPersona userPersona = EntityFactory.createUserPersona("userId", null, null);
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodOtherWhenUserPersonaPropertiesAreOk() {
        UserPersona userPersona = EntityFactory.createUserPersona();
        BindingResult errors = new BeanPropertyBindingResult(userPersona, "userPersona", true, 256);
        Mockito.when(request.getMethod()).thenReturn("HEAD");
        underTest.validate(userPersona, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }
}
