package com.freshdirect.cms.validation.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;
import com.freshdirect.cms.validation.validator.KeyValidator;
import com.freshdirect.cms.validation.validator.TypeValidator;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class ValidatorServiceTest {

    @InjectMocks
    private ValidatorService underTest;

    @Mock
    private TypeValidator typeValidator;

    @Mock
    private KeyValidator keyValidator;

    @Test
    public void testValidate() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "test");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Product.FULL_NAME, "Test full name");
        attributes.put(ContentTypes.Product.NAV_NAME, "Test nav name");

        Mockito.when(typeValidator.validate(contentKey, attributes, null)).thenReturn(new ValidationResults());
        Mockito.when(keyValidator.validate(contentKey, attributes, null)).thenReturn(new ValidationResults());

        ValidationResults validationResults = underTest.validate(contentKey, attributes);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test(expected = ValidationFailedException.class)
    public void testValidateWithError() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "test");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Product.FULL_NAME, "Test full name");
        attributes.put(ContentTypes.Product.NAV_NAME, "Test nav name");

        ValidationResults bogus = new ValidationResults();
        bogus.addValidationResult(contentKey, "Bogus error", ValidationResultLevel.ERROR, typeValidator.getClass());

        Mockito.when(typeValidator.validate(contentKey, attributes, null)).thenReturn(bogus);
        Mockito.when(keyValidator.validate(contentKey, attributes, null)).thenReturn(new ValidationResults());

        underTest.validate(contentKey, attributes);
    }

    @Test
    public void testValidateWithWarning() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "test");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Product.FULL_NAME, "Test full name");
        attributes.put(ContentTypes.Product.NAV_NAME, "Test nav name");

        ValidationResults bogus = new ValidationResults();
        bogus.addValidationResult(contentKey, "Bogus warning", ValidationResultLevel.WARNING, typeValidator.getClass());

        Mockito.when(typeValidator.validate(contentKey, attributes, null)).thenReturn(bogus);
        Mockito.when(keyValidator.validate(contentKey, attributes, null)).thenReturn(new ValidationResults());

        ValidationResults validationResults = underTest.validate(contentKey, attributes);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertTrue(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }
}
