package com.freshdirect.cms.contentvalidation.validator;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.validation.ValidationResults;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class StructureValidatorTest {

    @InjectMocks
    private StructureValidator underTest;

    @Mock
    private ContentProviderService contentProviderService;

    @Test
    public void testValidateWhenEverythingIsOkay() {
        final ContentKey skuKey = ContentKeyFactory.get(ContentType.Sku, "skuForTest");
        final ContentKey parentKey = ContentKeyFactory.get(ContentType.Product, "skuParent");

        Mockito.when(contentProviderService.getParentKeys(skuKey)).thenReturn(new HashSet<ContentKey>() {

            {
                add(parentKey);
            }
        });

        ValidationResults validationResults = underTest.validate(skuKey, null, contentProviderService);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenSkuHasMultipleParents() {
        final ContentKey skuKey = ContentKeyFactory.get(ContentType.Sku, "skuForTest");
        final ContentKey parentKey = ContentKeyFactory.get(ContentType.Product, "skuParent");
        final ContentKey parentKey2 = ContentKeyFactory.get(ContentType.Product, "skuParent2");

        Mockito.when(contentProviderService.getParentKeys(skuKey)).thenReturn(new HashSet<ContentKey>() {

            {
                add(parentKey);
                add(parentKey2);
            }
        });

        ValidationResults validationResults = underTest.validate(skuKey, null, contentProviderService);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenMultipleParentsAreFromIgnorableTypes() {
        final ContentKey webpageSectionKey = ContentKeyFactory.get(ContentType.Section, "sectionForTest");
        final ContentKey webpageKey = ContentKeyFactory.get(ContentType.WebPage, "skuParent");
        final ContentKey webpageKey2 = ContentKeyFactory.get(ContentType.WebPage, "skuParent2");

        Mockito.when(contentProviderService.getParentKeys(webpageSectionKey)).thenReturn(new HashSet<ContentKey>() {

            {
                add(webpageKey);
                add(webpageKey2);
            }
        });

        ValidationResults validationResults = underTest.validate(webpageSectionKey, null, contentProviderService);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }
}
