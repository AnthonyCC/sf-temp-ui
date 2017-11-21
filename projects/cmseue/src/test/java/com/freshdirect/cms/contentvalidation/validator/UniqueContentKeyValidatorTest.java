package com.freshdirect.cms.contentvalidation.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class UniqueContentKeyValidatorTest {

    @InjectMocks
    private UniqueContentKeyValidator underTest;

    @Mock
    private ContentProviderService contentProviderService;

    @Test
    public void testValidateWhenEverythingIsOkay() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "uniqueId");
        when(contentProviderService.containsContentKey(productKey)).thenReturn(true);
        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        assertFalse(validationResults.hasError());
        assertFalse(validationResults.hasWarning());
        assertFalse(validationResults.hasInfo());
        assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenExistsSameIdInRestrictedTypes() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "nonUniqueId");
        final ContentKey departmentKey = ContentKeyFactory.get(ContentType.Department, "nonUniqueId");
        when(contentProviderService.containsContentKey(productKey)).thenReturn(true);
        when(contentProviderService.containsContentKey(departmentKey)).thenReturn(true);
        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        assertTrue(validationResults.hasError());
        assertFalse(validationResults.hasWarning());
        assertFalse(validationResults.hasInfo());
        assertEquals(1, validationResults.getValidationResults().size());
        assertEquals(productKey, validationResults.getValidationResults().get(0).getValidatedObject());
        assertEquals(ValidationResultLevel.ERROR, validationResults.getValidationResults().get(0).getFailureLevel());
    }

    @Test
    public void testValidateWhenExistsSameIdInNotRestrictedTypes() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "nonUniqueId");
        final ContentKey webpageKey = ContentKeyFactory.get(ContentType.WebPage, "nonUniqueId");
        when(contentProviderService.containsContentKey(productKey)).thenReturn(true);
        when(contentProviderService.containsContentKey(webpageKey)).thenReturn(true);
        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        assertFalse(validationResults.hasError());
        assertFalse(validationResults.hasWarning());
        assertFalse(validationResults.hasInfo());
        assertEquals(0, validationResults.getValidationResults().size());
    }
}
