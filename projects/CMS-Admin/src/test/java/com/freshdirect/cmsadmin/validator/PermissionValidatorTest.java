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
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.PermissionValidator;
import com.freshdirect.cmsadmin.validation.ValidationService;

/**
 * Unit test cases for permission validator methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PermissionValidatorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private PermissionValidator underTest;

    @Test
    public void testValidatorAssigneToPermissionClass() {
        Assert.assertTrue(underTest.supports(Permission.class));
    }

    @Test
    public void testValidatorNotAssigneToOtherClasses() {
        Assert.assertFalse(underTest.supports(Object.class));
    }

    @Test
    public void testValidateHttpMethodGet() {
        Permission permission = EntityFactory.createPermission();
        BindingResult errors = new BeanPropertyBindingResult(permission, "permission", true, 256);
        Mockito.when(request.getMethod()).thenReturn("GET");
        underTest.validate(permission, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPutWhenPermissionPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        BindingResult errors = new BeanPropertyBindingResult(permission, "permission", true, 256);
        Mockito.when(request.getMethod()).thenReturn("PUT");
        underTest.validate(permission, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPutWhenPermissionIdIsNull() {
        Permission permission = EntityFactory.createPermission(null, "name");
        BindingResult errors = new BeanPropertyBindingResult(permission, "permission", true, 256);
        Mockito.when(request.getMethod()).thenReturn("PUT");
        underTest.validate(permission, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodDeleteWhenPermissionPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        BindingResult errors = new BeanPropertyBindingResult(permission, "permission", true, 256);
        Mockito.when(request.getMethod()).thenReturn("DELETE");
        underTest.validate(permission, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodDeleteWhenPermissionIdIsNull() {
        Permission permission = EntityFactory.createPermission(null, "name");
        BindingResult errors = new BeanPropertyBindingResult(permission, "permission", true, 256);
        Mockito.when(request.getMethod()).thenReturn("DELETE");
        underTest.validate(permission, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenPermissionPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        BindingResult errors = new BeanPropertyBindingResult(permission, "permission", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(permission, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }
    
    @Test
    public void testValidateHttpMethodOtherWhenPermissionPropertiesAreOk() {
        Permission permission = EntityFactory.createPermission();
        BindingResult errors = new BeanPropertyBindingResult(permission, "permission", true, 256);
        Mockito.when(request.getMethod()).thenReturn("HEAD");
        underTest.validate(permission, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }
}
