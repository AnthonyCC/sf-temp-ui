package com.freshdirect.cms.core.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.Department;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static com.freshdirect.cms.core.domain.ContentType.Store;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class ContentProviderServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private ContentTypeInfoService contentTypeInfoService;

    @Mock
    private ContentKeyParentsCollectorService parentKeysTableGeneratorService;

    @Mock
    private ContentProvider contentProvider;

    @Mock
    private ContextService contextService;

    @Spy
    @InjectMocks
    private ContentProviderService testService;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // mock type info
        Mockito.when(contentTypeInfoService.selectRelationships(Store, false)).thenReturn(asList(new Relationship[] { (Relationship) ContentTypes.Store.departments }));
        Mockito.when(contentTypeInfoService.selectRelationships(Department, false)).thenReturn(asList(new Relationship[] { (Relationship) ContentTypes.Department.categories }));
        Mockito.when(contentTypeInfoService.selectRelationships(Category, false))
                .thenReturn(asList(new Relationship[] { (Relationship) ContentTypes.Category.products, (Relationship) ContentTypes.Category.subcategories }));

        // mock content keys
        Mockito.when(contentProvider.getContentKeysByType(Store))
                .thenReturn(ImmutableSet.of(RootContentKey.STORE_FRESHDIRECT.contentKey));
        Mockito.when(contentProvider.getContentKeysByType(Department))
                .thenReturn(ImmutableSet.of(get(Department, "dept1")));
        Mockito.when(contentProvider.getContentKeysByType(Category))
                .thenReturn(ImmutableSet.of(get(Category, "cat_1"), get(Category, "cat_2"), get(Category, "cat_orphan"), get(Category, "cat_orphan_2")));
        Mockito.when(contentProvider.getContentKeysByType(Product))
                .thenReturn(ImmutableSet.of(get(Product, "prd_1"), get(Product, "prd_2"), get(Product, "prd_orphan"), get(Product, "prd_orphan_2")));

        // mock child keys
        Mockito.when(contentProvider.getAttributeValues(RootContentKey.STORE_FRESHDIRECT.contentKey, asList(new Attribute[] { ContentTypes.Store.departments })))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, asList(new ContentKey[] { get(Department, "dept1") })));
        Mockito.when(contentProvider.getAttributeValues(get(Department, "dept1"), asList(new Attribute[] { ContentTypes.Department.categories })))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, asList(new ContentKey[] { get(Category, "cat_2") })));
        Mockito.when(contentProvider.getAttributeValues(get(Category, "cat_orphan"), asList(new Attribute[] { ContentTypes.Category.subcategories })))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.subcategories, asList(new ContentKey[] { get(Category, "cat_1") })));
        Mockito.when(contentProvider.getAttributeValues(get(Category, "cat_orphan_2"), asList(new Attribute[] { ContentTypes.Category.products })))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] { get(Category, "prd_orphan_2") })));

        Mockito.when(contentProvider.getAttributeValues(get(Category, "cat_1"), asList(new Attribute[] { ContentTypes.Category.products })))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] { get(Product, "prd_1"), get(Product, "prd_orphan") })));
        Mockito.when(contentProvider.getAttributeValues(get(Category, "cat_2"), asList(new Attribute[] { ContentTypes.Category.products })))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] { get(Product, "prd_1"), get(Product, "prd_2") })));

        Cache parentKeysCache = new ConcurrentMapCache("parentKeysCache");

        final Map<ContentKey, Set<ContentKey>> parentKeysMap = new HashMap<ContentKey, Set<ContentKey>>();
        parentKeysMap.put(get(Product, "prd_1"), Sets.newHashSet(get(Category, "cat_1"), get(Category, "cat_2")));
        parentKeysMap.put(get(Product, "prd_2"), Sets.newHashSet(get(Category, "cat_2")));
        parentKeysMap.put(get(Product, "prd_orphan"), Sets.newHashSet(get(Category, "cat_1")));
        parentKeysMap.put(get(Category, "cat_1"), Sets.newHashSet(get(Category, "cat_orphan")));
        parentKeysMap.put(get(Category, "cat_2"), Sets.newHashSet(get(Department, "dept1")));
        parentKeysMap.put(get(Department, "dept1"), Sets.newHashSet(RootContentKey.STORE_FRESHDIRECT.contentKey));

        for (Map.Entry<ContentKey, Set<ContentKey>> entry : parentKeysMap.entrySet()) {
            parentKeysCache.put(entry.getKey(), entry.getValue());
        }

        Cache allParentKeysCache = new ConcurrentMapCache("allParentKeysCache");
        allParentKeysCache.put("all", parentKeysCache.getNativeCache());

        Mockito.when(contentProvider.generateParentKeysMap()).thenReturn(parentKeysMap);

        Mockito.when(cacheManager.getCache("parentKeysCache")).thenReturn(parentKeysCache);
        Mockito.when(cacheManager.getCache("allParentKeysCache")).thenReturn(allParentKeysCache);
        Mockito.when(contextService.buildGraphFromParentKeyStructure(Mockito.any(ContentKey.class), Mockito.anyMap())).thenCallRealMethod();
        Mockito.when(contextService.selectTopKeysOf(Mockito.any(ContentKey.class), Mockito.anyList())).thenCallRealMethod();
        Mockito.when(contextService.findContextsOf(Mockito.any(ContentKey.class), Mockito.anyMap())).thenCallRealMethod();
    }

    @Test
    public void testOrphanContentKeys() {
        assertTrue("It must be orphan", testService.isOrphan(get(Product, "prd_orphan"), null));
        assertTrue("It must be orphan", testService.isOrphan(get(Category, "cat_orphan"), null));
        assertTrue("Subcategory of orphan category must be orphan too", testService.isOrphan(get(Category, "cat_1"), null));
        assertTrue("It must be orphan", testService.isOrphan(get(Product, "prd_orphan_2"), null));
        assertTrue("It must be orphan", testService.isOrphan(get(Category, "cat_orphan_2"), null));
    }

    @Test
    public void testNonOrphanContentKeys() {
        assertTrue("Product having ancestry chain up to store level may not be orphan", !testService.isOrphan(get(Product, "prd_2"), RootContentKey.STORE_FRESHDIRECT.contentKey));
        assertTrue("Product having at least one valid context should not be orphan", !testService.isOrphan(get(Product, "prd_1"), RootContentKey.STORE_FRESHDIRECT.contentKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPrimaryHomesWithNullContentKey() {
        testService.findPrimaryHomes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPrimaryHomesWithNotProductContentKey() {
        testService.findPrimaryHomes(ContentKeyFactory.get(ContentType.Category, "whatever"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindPrimaryHomesWhenEverythingIsOkay() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "testProduct");
        ContentKey parentNotPrimaryHome = ContentKeyFactory.get(ContentType.Category, "notPrimaryHome");
        ContentKey parentPrimaryHome = ContentKeyFactory.get(ContentType.Category, "primaryHome");
        ContentKey department = ContentKeyFactory.get(ContentType.Department, "department");
        ContentKey store = RootContentKey.STORE_FRESHDIRECT.contentKey;

        Optional<Object> primaryHomeAttributeValue = Optional.of((Object) Arrays.asList(parentPrimaryHome));

        Mockito.when(contentProvider.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME)).thenReturn(primaryHomeAttributeValue);

        Mockito.doReturn(Arrays.asList(
                Arrays.asList(productKey, parentNotPrimaryHome, department, store),
                Arrays.asList(productKey, parentPrimaryHome, department, store))).when(testService)
            .findContextsOf(productKey);

        Map<ContentKey, ContentKey> productPrimaryHomes = testService.findPrimaryHomes(productKey);

        Assert.assertNotNull(productPrimaryHomes);
        Assert.assertEquals(1, productPrimaryHomes.size());
        Assert.assertEquals(RootContentKey.STORE_FRESHDIRECT.contentKey, productPrimaryHomes.keySet().iterator().next());
        Assert.assertEquals(parentPrimaryHome, productPrimaryHomes.get(RootContentKey.STORE_FRESHDIRECT.contentKey));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindPrimaryHomesWithMultipleStores() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "testProduct");
        ContentKey parentNotPrimaryHome = ContentKeyFactory.get(ContentType.Category, "notPrimaryHome");
        ContentKey parentPrimaryHome = ContentKeyFactory.get(ContentType.Category, "primaryHome");
        ContentKey parentPrimaryHomeInFDX = ContentKeyFactory.get(ContentType.Category, "primaryHomeFDX");
        ContentKey department = ContentKeyFactory.get(ContentType.Department, "department");
        ContentKey departmentFDX = ContentKeyFactory.get(ContentType.Department, "departmentFDX");
        ContentKey freshdirectStore = RootContentKey.STORE_FRESHDIRECT.contentKey;
        ContentKey fdxStore = RootContentKey.STORE_FOODKICK.contentKey;

        Optional<Object> primaryHomeAttributeValue = Optional.of((Object) Arrays.asList(parentPrimaryHome, parentPrimaryHomeInFDX));

        Mockito.when(contentProvider.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME)).thenReturn(primaryHomeAttributeValue);

        Mockito.doReturn(Arrays.asList(
                Arrays.asList(productKey, parentNotPrimaryHome, department, freshdirectStore),
                Arrays.asList(productKey, parentPrimaryHome, department, freshdirectStore),
                Arrays.asList(productKey, parentPrimaryHomeInFDX, departmentFDX, fdxStore)))
            .when(testService).findContextsOf(productKey);

        Map<ContentKey, ContentKey> productPrimaryHomes = testService.findPrimaryHomes(productKey);

        Assert.assertNotNull(productPrimaryHomes);
        Assert.assertEquals(2, productPrimaryHomes.size());
        Assert.assertTrue(productPrimaryHomes.containsKey(freshdirectStore));
        Assert.assertTrue(productPrimaryHomes.containsKey(fdxStore));
        Assert.assertEquals(parentPrimaryHome, productPrimaryHomes.get(RootContentKey.STORE_FRESHDIRECT.contentKey));
        Assert.assertEquals(parentPrimaryHomeInFDX, productPrimaryHomes.get(RootContentKey.STORE_FOODKICK.contentKey));
    }
}
