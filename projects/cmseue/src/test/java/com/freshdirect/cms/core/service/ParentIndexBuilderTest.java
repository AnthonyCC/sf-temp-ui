package com.freshdirect.cms.core.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
@ActiveProfiles("test")
public class ParentIndexBuilderTest {

    @Test
    public void testProductHasParentWithNodeMap() {
        final Map<ContentKey, Map<Attribute, Object>> testChildren = new HashMap<ContentKey, Map<Attribute, Object>>();
        testChildren.put(get(Category, "cat_1"), ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));
        testChildren.put(get(Category, "cat_2"), ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));

        Map<ContentKey, Set<ContentKey>> parentMap = ParentIndexBuilder.createParentKeysMap(testChildren);

        final Set<ContentKey> parentsUnderTest = parentMap.get(get(Product, "prd_1"));
        assertNotNull("Missing parent category", parentsUnderTest);
        assertEquals("Unexpected parents", parentsUnderTest, Sets.newHashSet(get(Category, "cat_1"), get(Category, "cat_2")));
    }

    @Test
    public void testProductWithoutParentWithNodeMap() {
        final Map<ContentKey, Map<Attribute, Object>> testChildren = new HashMap<ContentKey, Map<Attribute, Object>>();
        testChildren.put(get(Category, "cat_1"), ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));
        testChildren.put(get(Category, "cat_2"), ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1")})));

        Map<ContentKey, Set<ContentKey>> parentMap = ParentIndexBuilder.createParentKeysMap(testChildren);

        final Set<ContentKey> parentsUnderTest = parentMap.get(get(Product, "new_prd"));
        assertNull("Product should be orphan", parentsUnderTest);
    }
}
