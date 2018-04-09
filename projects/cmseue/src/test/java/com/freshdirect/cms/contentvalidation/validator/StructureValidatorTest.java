package com.freshdirect.cms.contentvalidation.validator;

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
import com.google.common.collect.ImmutableSet;

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

        Mockito.when(contentProviderService.getParentKeys(skuKey)).thenReturn(ImmutableSet.of(parentKey));

        ValidationResults validationResults = underTest.validate(skuKey, null, contentProviderService);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenSkuHasMultipleParents() {
        final ContentKey skuKey = ContentKeyFactory.get(ContentType.Sku, "skuForTest");
        final ContentKey parentKey = ContentKeyFactory.get(ContentType.Product, "skuParent");
        final ContentKey parentKey2 = ContentKeyFactory.get(ContentType.Product, "skuParent2");

        Mockito.when(contentProviderService.getParentKeys(skuKey)).thenReturn(ImmutableSet.of(parentKey, parentKey2));

        ValidationResults validationResults = underTest.validate(skuKey, null, contentProviderService);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenNotInValidableTypes() {
        final ContentKey childKey = ContentKeyFactory.get(ContentType.Section, "child");
        final ContentKey parentKey = ContentKeyFactory.get(ContentType.WebPage, "parent");
        final ContentKey parentKey2 = ContentKeyFactory.get(ContentType.Department, "parent2");

        Mockito.when(contentProviderService.getParentKeys(childKey)).thenReturn(ImmutableSet.of(parentKey, parentKey2));

        ValidationResults validationResults = underTest.validate(childKey, null, contentProviderService);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenMultipleParentsAreFromIgnorableTypes() {
        final ContentKey childKey = ContentKeyFactory.get(ContentType.Department, "child");
        final ContentKey parentKey = ContentKeyFactory.get(ContentType.DarkStore, "parent");
        final ContentKey parentKey2 = ContentKeyFactory.get(ContentType.DarkStore, "parent2");

        Mockito.when(contentProviderService.getParentKeys(childKey)).thenReturn(ImmutableSet.of(parentKey, parentKey2));

        ValidationResults validationResults = underTest.validate(childKey, null, contentProviderService);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenTwoParentsButOneIsIgnorable() {
        final ContentKey childKey = ContentKeyFactory.get(ContentType.Department, "child");
        final ContentKey parentKey = ContentKeyFactory.get(ContentType.DarkStore, "parent");
        final ContentKey parentKey2 = ContentKeyFactory.get(ContentType.Store, "parent2");

        Mockito.when(contentProviderService.getParentKeys(childKey)).thenReturn(ImmutableSet.of(parentKey, parentKey2));

        ValidationResults validationResults = underTest.validate(childKey, null, contentProviderService);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }
}
