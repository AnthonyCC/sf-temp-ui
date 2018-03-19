package com.freshdirect.cms.contentvalidation.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.contentvalidation.correction.CorrectionManager;
import com.freshdirect.cms.contentvalidation.validator.Validator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.exception.ValidationFailedException;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class ValidatingAndCorrectingServiceTest {

    @InjectMocks
    private DatabaseContentValidatorService underTest;

    @Mock
    private ContentValidatorService contentValidatorService;

    @Mock
    private CorrectionManager correctionManager;

    @Test
    public void testValidateWithValidObject() {
        ContentKey validProduct = ContentKeyFactory.get(ContentType.Product, "validProduct");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();

        Mockito.when(contentValidatorService.validate(validProduct, attributesWithValues, null)).thenReturn(new ValidationResults());

        ValidationResults validationResults = underTest.validate(validProduct, attributesWithValues, null);

        Assert.assertFalse(validationResults.hasError());
    }

    @Test
    public void testValidateWithRepairableObject() {
        ContentKey repairableProduct = ContentKeyFactory.get(ContentType.Product, "repairableProduct");
        final Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();

        ValidationResults firstValidationResults = new ValidationResults();
        firstValidationResults.addValidationResult(repairableProduct, "Bogus message!", ValidationResultLevel.ERROR, Validator.class);
        Mockito.when(contentValidatorService.validate(repairableProduct, attributesWithValues, null)).thenReturn(firstValidationResults).thenReturn(new ValidationResults());

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                attributesWithValues.put(ContentTypes.Product.FULL_NAME, "whatever");
                return null;
            }
        }).when(correctionManager).runCorrectionServicesOn(repairableProduct, attributesWithValues, firstValidationResults, null);

        ValidationResults validationResults = underTest.validate(repairableProduct, attributesWithValues, null);

        Assert.assertFalse(validationResults.hasError());
    }

    @Test(expected = ValidationFailedException.class)
    public void testValidateWithIrrepairableObject() {
        ContentKey repairableProduct = ContentKeyFactory.get(ContentType.Product, "repairableProduct");
        final Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();

        ValidationResults firstValidationResults = new ValidationResults();
        firstValidationResults.addValidationResult(repairableProduct, "Bogus message!", ValidationResultLevel.ERROR, Validator.class);
        Mockito.when(contentValidatorService.validate(repairableProduct, attributesWithValues, null)).thenThrow(new ValidationFailedException(firstValidationResults));

        Mockito.doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                attributesWithValues.put(ContentTypes.Product.FULL_NAME, "whatever");
                return null;
            }
        }).when(correctionManager).runCorrectionServicesOn(repairableProduct, attributesWithValues, firstValidationResults, null);

        ValidationResults validationResults = underTest.validate(repairableProduct, attributesWithValues, null);

        Assert.assertTrue(validationResults.hasError());
    }
}
