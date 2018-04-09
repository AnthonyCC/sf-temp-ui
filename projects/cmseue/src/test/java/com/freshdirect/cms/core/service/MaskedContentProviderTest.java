package com.freshdirect.cms.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.util.TestContentBuilder;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class MaskedContentProviderTest {

    @Mock
    ContextualContentProvider baseProvider;

    @Mock
    ContextualContentProvider maskProvider;

    private MaskedContentProvider testService;

    @Before
    public void setUp() {
        testService = new MaskedContentProvider(baseProvider, maskProvider);
    }

    @Test
    public void testNonOverlappingNodes() {
        final ContentKey baseProductKey = keyOf(ContentType.Product, "prd1");
        final ContentKey maskProductKey = keyOf(ContentType.Product, "mprd1");

        final String baseProductId = "prd1";
        final String maskProductId = "mprd1";

        Map<ContentKey, Map<Attribute, Object>> testBaseContent = new TestContentBuilder()
            .newProduct(baseProductId, "Base Product")
            .build();

        Map<ContentKey, Map<Attribute, Object>> testMaskContent = new TestContentBuilder()
            .newProduct(maskProductId, "Mask Product")
            .build();

        Mockito
            .when(baseProvider.getContentKeys())
            .thenReturn(testBaseContent.keySet());
        Mockito
            .when(baseProvider.containsContentKey(baseProductKey))
            .thenReturn(true);
        Mockito
            .when(baseProvider.getAttributeValue(baseProductKey, ContentTypes.Product.FULL_NAME))
            .thenReturn(Optional.<Object>of(testBaseContent.get(baseProductKey).get(ContentTypes.Product.FULL_NAME)));
        Mockito
            .when(baseProvider.getAllAttributesForContentKey(baseProductKey))
            .thenReturn(testBaseContent.get(baseProductKey));
        Mockito
            .when(baseProvider.getAttributeValues(baseProductKey, ImmutableList.of(ContentTypes.Product.FULL_NAME)))
            .thenReturn(testBaseContent.get(baseProductKey));

        Mockito
            .when(maskProvider.getContentKeys())
            .thenReturn(testMaskContent.keySet());
        Mockito
            .when(maskProvider.containsContentKey(maskProductKey))
            .thenReturn(true);
        Mockito
            .when(maskProvider.getAttributeValue(maskProductKey, ContentTypes.Product.FULL_NAME))
            .thenReturn(Optional.<Object>of(testMaskContent.get(maskProductKey).get(ContentTypes.Product.FULL_NAME)));
        Mockito
            .when(maskProvider.getAllAttributesForContentKey(maskProductKey))
            .thenReturn(testMaskContent.get(maskProductKey));
        Mockito
            .when(maskProvider.getAttributeValues(maskProductKey, ImmutableList.of(ContentTypes.Product.FULL_NAME)))
            .thenReturn(testMaskContent.get(maskProductKey));

        // test parent keys
        assertEquals(2, testService.getContentKeys().size());
        assertTrue(testService.getContentKeys().contains(baseProductKey));
        assertTrue(testService.getContentKeys().contains(maskProductKey));

        // test value getters
        assertTrue(testService.getAttributeValue(baseProductKey, ContentTypes.Product.FULL_NAME).isPresent());
        assertEquals(testService.getAttributeValue(baseProductKey, ContentTypes.Product.FULL_NAME).get(), "Base Product");
        assertTrue(testService.getAttributeValue(maskProductKey, ContentTypes.Product.FULL_NAME).isPresent());
        assertEquals(testService.getAttributeValue(maskProductKey, ContentTypes.Product.FULL_NAME).get(), "Mask Product");

        assertTrue(testService.getAllAttributesForContentKey(baseProductKey).equals(testBaseContent.get(baseProductKey)));
        assertTrue(testService.getAllAttributesForContentKey(maskProductKey).equals(testMaskContent.get(maskProductKey)));
        assertTrue(testService.getAttributeValues(baseProductKey, ImmutableList.of(ContentTypes.Product.FULL_NAME)).equals(testBaseContent.get(baseProductKey)));
        assertTrue(testService.getAttributeValues(maskProductKey, ImmutableList.of(ContentTypes.Product.FULL_NAME)).equals(testMaskContent.get(maskProductKey)));

        // test contexts
        assertTrue(testService.findContextsOf(baseProductKey).isEmpty());
        assertTrue(testService.findContextsOf(maskProductKey).isEmpty());
    }

    private static ContentKey keyOf(ContentType type, String id) {
        return ContentKeyFactory.get(type, id);
    }
}
