package com.freshdirect.cms.contentvalidation.correction;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.contentvalidation.validator.PrimaryHomeValidator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.ContextualAttributeFetchScope;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.validation.validator.Validator;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PrimaryHomeCorrectionServiceTest {

    @InjectMocks
    private PrimaryHomeCorrectionService underTest;

    @Mock
    private ContentProviderService contentProviderService;

    @Test
    public void testRepairPrimaryHome() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey parentCategory = ContentKeyFactory.get(ContentType.Category, "parentCategory");
        final ContentKey parentDepartment = ContentKeyFactory.get(ContentType.Department, "parentDept");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(parentCategory));
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(ImmutableList.<ContentKey>of(
                        productKey, parentCategory, parentDepartment, RootContentKey.STORE_FRESHDIRECT.contentKey)));
        Mockito.when(contentProviderService.fetchContextualizedAttributeValue((ContentKey) eq(productKey),
                        (Attribute) eq(ContentTypes.Product.HIDE_URL), (ContentKey) any(), (ContextualAttributeFetchScope) any()))
                .thenReturn(Optional.absent());

        underTest.correct(productKey, null, contentProviderService);

        verify(contentProviderService).updateContent(argThat(new CorrectionMatcher(
                correctionOf(productKey, ImmutableSet.of(parentCategory)))), (ContentUpdateContext) isNull());
    }

    @Test
    public void testRepairPrimaryHomeInBothStores() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey fdParent = ContentKeyFactory.get(ContentType.Category, "fdParentCategory");
        final ContentKey fdxParent = ContentKeyFactory.get(ContentType.Category, "fdxParentCategory");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(fdParent, fdxParent));
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(
                        ImmutableList.<ContentKey>of(productKey, fdParent, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdxParent, RootContentKey.STORE_FOODKICK.contentKey)));
        Mockito.when(contentProviderService.fetchContextualizedAttributeValue((ContentKey) eq(productKey),
                        (Attribute) eq(ContentTypes.Product.HIDE_URL), (ContentKey) any(), (ContextualAttributeFetchScope) any()))
                .thenReturn(Optional.absent());

        underTest.correct(productKey, null, contentProviderService);

        verify(contentProviderService).updateContent(argThat(new CorrectionMatcher(
                correctionOf(productKey, ImmutableSet.of(fdParent, fdxParent)))), (ContentUpdateContext) isNull());
    }

    @Test
    public void testRepairPrimaryHomeInOnlyOneStore() {
        final ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "productForTest");
        final ContentKey fdParent = ContentKeyFactory.get(ContentType.Category, "fdParentCategory");
        final ContentKey fdxParent = ContentKeyFactory.get(ContentType.Category, "fdxParentCategory");
        final ContentKey fdAltParent1 = ContentKeyFactory.get(ContentType.Category, "fdAlternativeParentCategory1");
        final ContentKey fdAltParent2 = ContentKeyFactory.get(ContentType.Category, "fdAlternativeParentCategory2");
        final ContentKey fdAltParent3 = ContentKeyFactory.get(ContentType.Category, "fdAlternativeParentCategory3");
        final ContentKey fdAltParent4 = ContentKeyFactory.get(ContentType.Category, "fdAlternativeParentCategory4");
        final ContentKey fdAltParent5 = ContentKeyFactory.get(ContentType.Category, "fdAlternativeParentCategory5");
        final ContentKey fdAltParent6 = ContentKeyFactory.get(ContentType.Category, "fdAlternativeParentCategory6");

        Mockito.when(contentProviderService.getParentKeys(productKey))
                .thenReturn(ImmutableSet.of(fdAltParent1, fdAltParent2, fdAltParent3, fdParent, fdAltParent4, fdAltParent5, fdAltParent6, fdxParent));
        Mockito.when(contentProviderService.findContextsOf(productKey))
                .thenReturn(ImmutableList.<List<ContentKey>>of(
                        ImmutableList.<ContentKey>of(productKey, fdAltParent1, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdAltParent2, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdAltParent3, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdParent, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdAltParent4, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdAltParent5, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdAltParent6, RootContentKey.STORE_FRESHDIRECT.contentKey),
                        ImmutableList.<ContentKey>of(productKey, fdxParent, RootContentKey.STORE_FOODKICK.contentKey)));
        Mockito.when(contentProviderService.fetchContextualizedAttributeValue((ContentKey) eq(productKey),
                        (Attribute) eq(ContentTypes.Product.HIDE_URL), (ContentKey) any(), (ContextualAttributeFetchScope) any()))
                .thenReturn(Optional.absent());

        Mockito.when(contentProviderService.findPrimaryHomes(productKey))
                .thenReturn(ImmutableMap.<ContentKey, ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey, fdParent));

        underTest.correct(productKey, null, contentProviderService);
        verify(contentProviderService).updateContent(argThat(new CorrectionMatcher(
                correctionOf(productKey, ImmutableSet.of(fdParent, fdxParent)))), (ContentUpdateContext) isNull());
    }

    @Test
    public void testGetSupportedValidatorEqualsPrimaryHomeValidator() {
        Class<? extends Validator> supportedValidator = underTest.getSupportedValidator();
        Assert.assertEquals(PrimaryHomeValidator.class, supportedValidator);
    }


    private static Map<ContentKey, Map<Attribute, Object>> correctionOf(ContentKey nodeKey, Set<ContentKey> primaryHomes) {
        Map<ContentKey, Map<Attribute, Object>> expectedCorrection = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();
        expectedCorrection.put(nodeKey, ImmutableMap.<Attribute, Object>of(ContentTypes.Product.PRIMARY_HOME, ImmutableList.copyOf(primaryHomes)));
        return expectedCorrection;
    }

    private static final class CorrectionMatcher extends ArgumentMatcher<LinkedHashMap<ContentKey, Map<Attribute, Object>>> {

        private final Map<ContentKey, Map<Attribute, Object>> expectedCorrection;

        private CorrectionMatcher(Map<ContentKey, Map<Attribute, Object>> expectedCorrection) {
            this.expectedCorrection = expectedCorrection;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean matches(Object argument) throws ClassCastException {
            Map<ContentKey, Map<Attribute, Object>> actualCorrection = (LinkedHashMap<ContentKey, Map<Attribute, Object>>) argument;
            if (!expectedCorrection.keySet().equals(actualCorrection.keySet())) {
                return false;
            }
            for (ContentKey key : expectedCorrection.keySet()) {
                Map<Attribute, Object> expectedAttrMap = expectedCorrection.get(key);
                Map<Attribute, Object> actualAttrMap = actualCorrection.get(key);
                if (!expectedAttrMap.keySet().equals(actualAttrMap.keySet())) {
                    return false;
                }
                for (Attribute attr : expectedAttrMap.keySet()) {
                    List<ContentKey> expectedValue = (List<ContentKey>) expectedAttrMap.get(attr);
                    List<ContentKey> actualValue = (List<ContentKey>) actualAttrMap.get(attr);
                    if (!new HashSet<ContentKey>(expectedValue).equals(new HashSet<ContentKey>(actualValue))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
