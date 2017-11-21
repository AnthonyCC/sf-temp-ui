package com.freshdirect.cms.contentvalidation.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.validation.ValidationResults;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class WhitespaceValidatorTest {

    @InjectMocks
    private WhitespaceValidator underTest;

    @Test
    public void testValidateWhenEverythingIsOkay() {
        String goodId = "xyz";
        ContentKey keyToValidate = ContentKeyFactory.get(ContentType.Product, goodId);
        ValidationResults validationResults = underTest.validate(keyToValidate, null, null);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testWhenBeginsWithWhitespace() {
        String notGoodId = " xyz";
        ContentKey keyToValidate = ContentKeyFactory.get(ContentType.Product, notGoodId);
        ValidationResults validationResults = underTest.validate(keyToValidate, null, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testWhenEndsWithWhitespace() {
        String notGoodId = "xyz ";
        ContentKey keyToValidate = ContentKeyFactory.get(ContentType.Product, notGoodId);
        ValidationResults validationResults = underTest.validate(keyToValidate, null, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testWhenContainsWhitespace() {
        String notGoodId = "xy z";
        ContentKey keyToValidate = ContentKeyFactory.get(ContentType.Product, notGoodId);
        ValidationResults validationResults = underTest.validate(keyToValidate, null, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testWhenContainsTab() {
        String notGoodId = "xy\tz";
        ContentKey keyToValidate = ContentKeyFactory.get(ContentType.Product, notGoodId);
        ValidationResults validationResults = underTest.validate(keyToValidate, null, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testWhenContainsNewLine() {
        String notGoodId = "xy\nz";
        ContentKey keyToValidate = ContentKeyFactory.get(ContentType.Product, notGoodId);
        ValidationResults validationResults = underTest.validate(keyToValidate, null, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }
}
