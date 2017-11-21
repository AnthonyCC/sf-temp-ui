package com.freshdirect.cms.contentvalidation.validator;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.validation.ValidationResults;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class ConditionalFieldValidatorTest {

    @InjectMocks
    private ConditionalFieldValidator underTest;

    @Test
    public void testValidateWhenEverythingIsOkay() {
        ContentKey productFilterKey = ContentKeyFactory.get(ContentType.ProductFilter, "productFilterForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.ProductFilter.name, "test");
        attributesWithValues.put(ContentTypes.ProductFilter.type, "PRICE");
        attributesWithValues.put(ContentTypes.ProductFilter.fromValue, 2.00);

        ValidationResults validationResults = underTest.validate(productFilterKey, attributesWithValues, null);

        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithANDTypeAndMissingFilters() {
        ContentKey productFilterKey = ContentKeyFactory.get(ContentType.ProductFilter, "productFilterForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.ProductFilter.name, "test");
        attributesWithValues.put(ContentTypes.ProductFilter.type, "AND");

        ValidationResults validationResults = underTest.validate(productFilterKey, attributesWithValues, null);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithORTypeAndMissingFilters() {
        ContentKey productFilterKey = ContentKeyFactory.get(ContentType.ProductFilter, "productFilterForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.ProductFilter.name, "test");
        attributesWithValues.put(ContentTypes.ProductFilter.type, "OR");
        attributesWithValues.put(ContentTypes.ProductFilter.filters, null);

        ValidationResults validationResults = underTest.validate(productFilterKey, attributesWithValues, null);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithORGANICTypeAndMissingErpsyFlag() {
        ContentKey productFilterKey = ContentKeyFactory.get(ContentType.ProductFilter, "productFilterForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.ProductFilter.name, "test");
        attributesWithValues.put(ContentTypes.ProductFilter.type, "ORGANIC");

        ValidationResults validationResults = underTest.validate(productFilterKey, attributesWithValues, null);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithRequiredGroup() {
        ContentKey productFilterKey = ContentKeyFactory.get(ContentType.ProductFilter, "productFilterForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.ProductFilter.name, "test");
        attributesWithValues.put(ContentTypes.ProductFilter.type, "PRICE");

        ValidationResults validationResults = underTest.validate(productFilterKey, attributesWithValues, null);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithMoreUsedFieldsThanAllowed() {
        ContentKey productFilterKey = ContentKeyFactory.get(ContentType.ProductFilter, "productFilterForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.ProductFilter.name, "test");
        attributesWithValues.put(ContentTypes.ProductFilter.type, "AND");
        attributesWithValues.put(ContentTypes.ProductFilter.fromValue, 2.00);

        ValidationResults validationResults = underTest.validate(productFilterKey, attributesWithValues, null);
        Assert.assertEquals(2, validationResults.getValidationResults().size()); // set 'filters', clear 'fromValue'
    }
}
