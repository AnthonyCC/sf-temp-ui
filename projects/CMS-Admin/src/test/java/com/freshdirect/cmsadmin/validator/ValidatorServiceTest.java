package com.freshdirect.cmsadmin.validator;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.exception.ValidationException;
import com.freshdirect.cmsadmin.validation.ValidationService;

/**
 * Unit test cases for validator service methods.
 */
@SuppressWarnings("unchecked")
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class ValidatorServiceTest {

    private static final String GENERAL_ERROR_CODE = "general";

    @InjectMocks
    private ValidationService underTest;

    @Test
    public void testSimpleErrorHappend() {
        BindingResult errors = new BeanPropertyBindingResult(new Object(), "object", true, 256);
        try {
            underTest.createErrorResults(errors);
        } catch (ValidationException e) {
            Assert.assertEquals("Incorrect fields", e.getMessage());
            Assert.assertEquals(1, e.getErrors().size());
            Assert.assertEquals(0, ((List<String>) e.getErrors().get(GENERAL_ERROR_CODE)).size());
        }
    }

    @Test
    public void testFieldErrorHappend() {
        BindingResult errors = new BeanPropertyBindingResult(new Object(), "object", true, 256);
        errors.addError(new FieldError("objectName", "fieldName", "defaultMessage"));
        try {
            underTest.createErrorResults(errors);
        } catch (ValidationException e) {
            Assert.assertEquals("Incorrect fields", e.getMessage());
            Assert.assertEquals(2, e.getErrors().size());
            Assert.assertEquals(0, ((List<String>) e.getErrors().get(GENERAL_ERROR_CODE)).size());
        }
    }

    @Test
    public void testGlobalErrorHappend() {
        BindingResult errors = new BeanPropertyBindingResult(new Object(), "object", true, 256);
        errors.addError(new ObjectError("objectName", "defaultMessage"));
        try {
            underTest.createErrorResults(errors);
        } catch (ValidationException e) {
            Assert.assertEquals("Incorrect fields", e.getMessage());
            Assert.assertEquals(1, e.getErrors().size());
            Assert.assertEquals(1, ((List<String>) e.getErrors().get(GENERAL_ERROR_CODE)).size());
        }
    }

}
