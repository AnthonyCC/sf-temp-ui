package com.freshdirect.cms.contentvalidation.correction;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.contentvalidation.validator.RecipeChildNodeValidator;
import com.freshdirect.cms.contentvalidation.validator.Validator;
import com.freshdirect.cms.core.service.ContentProviderService;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class RecipeChildNodeCorrectionServiceTest {

    @InjectMocks
    private RecipeChildNodeCorrectionService underTest;

    @Mock
    private ContentProviderService contentProviderService;

    @Test
    public void testGetSupportedValidatorEqualsRecipeChildNodeValidator() {
        Class<? extends Validator> supportedValidator = underTest.getSupportedValidator();
        Assert.assertEquals(RecipeChildNodeValidator.class, supportedValidator);
    }
}
