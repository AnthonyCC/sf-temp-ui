package com.freshdirect.cms.core.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
@ActiveProfiles("test")
public class ContentKeyParentsCollectorServiceTest {

    @Mock
    private ContentProvider contentProvider;

    @Mock
    private ContentTypeInfoService contentTypeInfoService;

    @InjectMocks
    private ContentKeyParentsCollectorService parentKeysTableGeneratorService;

    @Before
    public void setupTests() {

        final Map<ContentType, List<Relationship>> relationshipMap = ImmutableMap.<ContentType, List<Relationship>>of(Category,
                asList(new Relationship[] {(Relationship) ContentTypes.Category.products}));

        // mock type info
        Mockito.when(contentTypeInfoService.selectRelationships(Category, false)).thenReturn(relationshipMap.get(Category));

        Mockito.when(contentTypeInfoService.selectAllRelationships()).thenReturn(relationshipMap);

        // mock content keys
        Mockito.when(contentProvider.getContentKeysByType(Category)).thenReturn(ImmutableSet.of(get(Category, "cat_1"), get(Category, "cat_2")));
        Mockito.when(contentProvider.getContentKeysByType(Product)).thenReturn(ImmutableSet.of(get(Product, "prd_1"), get(Product, "new_prd")));

        // mock child keys
        Mockito.when(contentProvider.getAttributeValues(get(Category, "cat_1"), asList(new Attribute[] {ContentTypes.Category.products})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));
        Mockito.when(contentProvider.getAttributeValues(get(Category, "cat_2"), asList(new Attribute[] {ContentTypes.Category.products})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));
        Mockito.when(contentTypeInfoService.selectAllNavigableRelationships()).thenReturn(relationshipMap);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProductHasParent() {
        final Map<ContentKey, Map<Attribute, Object>> testChildren = new HashMap<ContentKey, Map<Attribute, Object>>();
        testChildren.put(get(Category, "cat_1"), ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));
        testChildren.put(get(Category, "cat_2"), ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));
        Mockito
            .when(contentProvider.getAttributesForContentKeys((List<ContentKey>) argThat(
                    Matchers.containsInAnyOrder(get(Category, "cat_1"), get(Category, "cat_2"))), eq(asList(ContentTypes.Category.products))))
            .thenReturn(testChildren);

        Map<ContentKey, Set<ContentKey>> parentMap = parentKeysTableGeneratorService.createParentKeysMap();

        final Set<ContentKey> parentsUnderTest = parentMap.get(get(Product, "prd_1"));
        assertNotNull("Missing parent category", parentsUnderTest);
        assertEquals("Unexpected parents", parentsUnderTest, Sets.newHashSet(get(Category, "cat_1"), get(Category, "cat_2")));
    }

    @Test
    public void testProductWithoutParent() {
        final Map<ContentKey, Map<Attribute, Object>> testChildren = new HashMap<ContentKey, Map<Attribute, Object>>();
        testChildren.put(get(Category, "cat_1"), ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));
        testChildren.put(get(Category, "cat_2"), ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));
        Mockito
            .when(contentProvider.getAttributesForContentKeys(asList(get(Category, "cat_1"), get(Category, "cat_2")), asList(ContentTypes.Category.products)))
            .thenReturn(testChildren);

        Map<ContentKey, Set<ContentKey>> parentMap = parentKeysTableGeneratorService.createParentKeysMap();

        final Set<ContentKey> parentsUnderTest = parentMap.get(get(Product, "new_prd"));
        assertNull("Product should be orphan", parentsUnderTest);
    }
}
