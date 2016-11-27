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
import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.UserValidator;
import com.freshdirect.cmsadmin.validation.ValidationService;

/**
 * Unit test cases for user validator methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class UserValidatorTest {

    private static final String ID = "id";
    private static final String NAME = "name";

    @Mock
    private HttpServletRequest request;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private UserValidator underTest;

    @Test
    public void testValidatorAssigneToUserClass() {
        Assert.assertTrue(underTest.supports(User.class));
    }

    @Test
    public void testValidatorNotAssigneToOtherClasses() {
        Assert.assertFalse(underTest.supports(Object.class));
    }

    @Test
    public void testValidateWhenUserPropertiesAreOk() {
        User user = EntityFactory.createUser(ID, NAME);
        BindingResult errors = new BeanPropertyBindingResult(user, "user", true, 256);
        underTest.validate(user, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateWhenUserIdIsNull() {
        User user = EntityFactory.createUser(null, NAME);
        BindingResult errors = new BeanPropertyBindingResult(user, "user", true, 256);
        underTest.validate(user, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateWhenUserNameIsNull() {
        User user = EntityFactory.createUser(ID, NAME);
        BindingResult errors = new BeanPropertyBindingResult(user, "user", true, 256);
        underTest.validate(null, errors);
        Mockito.verify(validationService).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }
}
