package com.freshdirect.cms.validation.validator;

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
public class KeyValidatorTest {

    @InjectMocks
    private KeyValidator underTest;

    @Test
    public void testValidate() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "test");

        ValidationResults validationResults = underTest.validate(contentKey, null, null);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithEmptyId() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "");

        ValidationResults validationResults = underTest.validate(contentKey, null, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }
}
