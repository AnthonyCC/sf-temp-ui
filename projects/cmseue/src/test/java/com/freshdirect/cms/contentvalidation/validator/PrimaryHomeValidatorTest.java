package com.freshdirect.cms.contentvalidation.validator;

import java.util.List;

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
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.validation.ValidationResults;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PrimaryHomeValidatorTest {

    @InjectMocks
    private PrimaryHomeValidator underTest;

    @Mock
    private ContentProviderService contentProviderService;

    @Test
    public void testValidateWhenEverythingIsOkay() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey productParent = ContentKeyFactory.get(ContentType.Category, "productParent");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(productParent));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.<Object>of(ImmutableList.<ContentKey>of(productParent)));
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(true);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(ImmutableList.<ContentKey>of(productKey, productParent, RootContentKey.STORE_FRESHDIRECT.contentKey)));
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey, productParent));

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenEverythingIsOkayInBothStores() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey fdParent = ContentKeyFactory.get(ContentType.Category, "fdParent");
        final ContentKey fdxParent = ContentKeyFactory.get(ContentType.Category, "fdxParent");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(fdParent, fdxParent));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.<Object>of(ImmutableList.<ContentKey>of(fdParent, fdxParent)));
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(
                        ImmutableList.<ContentKey>of(productKey, fdParent, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdxParent, RootContentKey.STORE_FOODKICK.contentKey)));
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey, fdParent, RootContentKey.STORE_FOODKICK.contentKey, fdxParent));

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenMissingPrimaryHome() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey productParent = ContentKeyFactory.get(ContentType.Category, "productParent");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(productParent));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.absent());
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(true);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(ImmutableList.<ContentKey>of(productKey, productParent, RootContentKey.STORE_FRESHDIRECT.contentKey)));
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of());

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenMissingPrimaryHomeFromBothStores() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey fdParent = ContentKeyFactory.get(ContentType.Category, "fdParent");
        final ContentKey fdxParent = ContentKeyFactory.get(ContentType.Category, "fdxParent");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(fdParent, fdxParent));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.absent());
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(
                        ImmutableList.<ContentKey>of(productKey, fdParent, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdxParent, RootContentKey.STORE_FOODKICK.contentKey)));
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of());

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        Assert.assertEquals(2, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenMissingPrimaryHomeFromOnlyOneStore() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey fdParent = ContentKeyFactory.get(ContentType.Category, "fdParent");
        final ContentKey fdxParent = ContentKeyFactory.get(ContentType.Category, "fdxParent");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(fdParent, fdxParent));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.<Object>of(ImmutableList.<ContentKey>of(fdParent)));
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(
                        ImmutableList.<ContentKey>of(productKey, fdParent, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdxParent, RootContentKey.STORE_FOODKICK.contentKey)));
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey, fdParent));

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenMissingParent() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey productParent = ContentKeyFactory.get(ContentType.Category, "productParent");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.<ContentKey>of());
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.<Object>of(ImmutableList.<ContentKey>of(productParent)));
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(true);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(true);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of());
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of());

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        // same message twice, should be only one
        Assert.assertEquals(2, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenInvalidPrimaryHome() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey productParent = ContentKeyFactory.get(ContentType.Category, "productParent");
        final ContentKey invalidPrimaryHome = ContentKeyFactory.get(ContentType.Category, "invalidPrimaryHome");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(productParent));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.<Object>of(ImmutableList.<ContentKey>of(invalidPrimaryHome)));
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(true);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(ImmutableList.<ContentKey>of(productKey, productParent, RootContentKey.STORE_FRESHDIRECT.contentKey)));
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of());

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenInvalidPrimaryHomeInBothStores() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey fdParent = ContentKeyFactory.get(ContentType.Category, "fdParent");
        final ContentKey fdxParent = ContentKeyFactory.get(ContentType.Category, "fdxParent");
        final ContentKey invalidPrimaryHome = ContentKeyFactory.get(ContentType.Category, "invalidPrimaryHome");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(fdParent, fdxParent));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.<Object>of(ImmutableList.<ContentKey>of(invalidPrimaryHome)));
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(
                        ImmutableList.<ContentKey>of(productKey, fdParent, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdxParent, RootContentKey.STORE_FOODKICK.contentKey)));
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of());

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        Assert.assertEquals(2, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenInvalidPrimaryHomeInOnlyOneStore() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey fdParent = ContentKeyFactory.get(ContentType.Category, "fdParent");
        final ContentKey fdxParent = ContentKeyFactory.get(ContentType.Category, "fdxParent");
        final ContentKey invalidPrimaryHome = ContentKeyFactory.get(ContentType.Category, "invalidPrimaryHome");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(fdParent, fdxParent));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
                .thenReturn(Optional.<Object>of(ImmutableList.<ContentKey>of(fdParent, invalidPrimaryHome)));
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.isOrphan(productKey, RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(false);
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(
                        ImmutableList.<ContentKey>of(productKey, fdParent, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdxParent, RootContentKey.STORE_FOODKICK.contentKey)));
        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey, fdParent));

        ValidationResults validationResults = underTest.validate(productKey, null, contentProviderService);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }
}
