package com.freshdirect.cms.draft.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.Department;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static com.freshdirect.cms.core.domain.ContentType.Store;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
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
import com.freshdirect.cms.core.service.ContentKeyParentsCollectorService;
import com.freshdirect.cms.core.service.ContentProvider;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextService;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftContentProviderServiceTest {

    @Spy
    @InjectMocks
    private DraftContentProviderService underTest;

    @Mock
    private ContentProviderService contentProviderService;

    @Mock
    private DraftService draftService;

    @Mock
    private DraftApplicatorService draftApplicatorService;

    @Mock
    private DraftChangeExtractorService draftChangeExtractorService;

    @Mock
    private DraftContextHolder draftContextHolder;

    @Mock
    private ContentKeyParentsCollectorService contentKeyParentsCollectorService;

    @Mock
    private ContentTypeInfoService contentTypeInfoService;

    @Mock
    private ContentProvider contentProvider;

    @Mock
    private ContextService contextService;

    @Mock
    private CacheManager cacheManager;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {

        // mock type info
        Mockito.when(contentTypeInfoService.selectRelationships(Store, false))
                .thenReturn(asList(new Relationship[] {(Relationship) ContentTypes.Store.departments}));
        Mockito.when(contentTypeInfoService.selectRelationships(Department, false))
                .thenReturn(asList(new Relationship[] {(Relationship) ContentTypes.Department.categories}));
        Mockito.when(contentTypeInfoService.selectRelationships(Category, false))
                .thenReturn(asList(new Relationship[] {(Relationship) ContentTypes.Category.products, (Relationship) ContentTypes.Category.subcategories}));

        // mock content keys
        Mockito.when(contentProviderService.getContentKeysByType(Store))
                .thenReturn(ImmutableSet.<ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey, RootContentKey.STORE_FOODKICK.contentKey));
        Mockito.when(contentProviderService.getContentKeysByType(Department))
                .thenReturn(ImmutableSet.<ContentKey>of(get(Department, "dept1"), get(Department, "dept_fdx")));
        Mockito.when(contentProviderService.getContentKeysByType(Category))
                .thenReturn(ImmutableSet.<ContentKey>of(get(Category, "cat_1"), get(Category, "cat_2"), get(Category, "cat_orphan")));
        Mockito.when(contentProviderService.getContentKeysByType(Product))
                .thenReturn(ImmutableSet.<ContentKey>of(get(Product, "prd_1"), get(Product, "prd_1"), get(Product, "prd_orphan")));

        // mock child keys
        Mockito.when(contentProviderService.getAttributeValues(RootContentKey.STORE_FRESHDIRECT.contentKey, asList(new Attribute[] {ContentTypes.Store.departments})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, asList(new ContentKey[] {get(Department, "dept1")})));
        Mockito.when(contentProviderService.getAttributeValues(RootContentKey.STORE_FOODKICK.contentKey, asList(new Attribute[] {ContentTypes.Store.departments})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, asList(new ContentKey[] {get(Department, "dept_fdx")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Department, "dept1"), asList(new Attribute[] {ContentTypes.Department.categories})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, asList(new ContentKey[] {get(Category, "cat_2")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Department, "dept_fdx"), asList(new Attribute[] {ContentTypes.Department.categories})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, asList(new ContentKey[] {get(Category, "cat_2")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Category, "cat_orphan"), asList(new Attribute[] {ContentTypes.Category.subcategories})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.subcategories, asList(new ContentKey[] {get(Category, "cat_1")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Category, "cat_1"), asList(new Attribute[] {ContentTypes.Category.products})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1"), get(Product, "prd_orphan")})));
        Mockito.when(contentProviderService.getAttributeValues(get(Category, "cat_2"), asList(new Attribute[] {ContentTypes.Category.products})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1"), get(Product, "prd_2")})));

        Mockito.when(contentProviderService.getAllAttributesForContentKey(get(Product, "prd_2")))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Product.PRIMARY_HOME, asList(new ContentKey[] {get(Category, "cat_2")})));

        final Cache parentKeysCache = new ConcurrentMapCache("parentKeysCache");
        final Cache draftParentCache = new ConcurrentMapCache("draftParentCache");
        final Cache draftNodesCache = new ConcurrentMapCache("draftNodes");

        final Map<ContentKey, Set<ContentKey>> parentKeysMap = new HashMap<ContentKey, Set<ContentKey>>();
        parentKeysMap.put(get(Product, "prd_1"), ImmutableSet.<ContentKey>of(get(Category, "cat_1"), get(Category, "cat_2")));
        parentKeysMap.put(get(Product, "prd_2"), ImmutableSet.<ContentKey>of(get(Category, "cat_2")));
        parentKeysMap.put(get(Product, "prd_orphan"), ImmutableSet.<ContentKey>of(get(Category, "cat_1")));
        parentKeysMap.put(get(Category, "cat_1"), ImmutableSet.<ContentKey>of(get(Category, "cat_orphan")));
        parentKeysMap.put(get(Category, "cat_2"), ImmutableSet.<ContentKey>of(get(Department, "dept1")));
        parentKeysMap.put(get(Department, "dept1"), ImmutableSet.<ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey));

        for (Map.Entry<ContentKey, Set<ContentKey>> entry : parentKeysMap.entrySet()) {
            parentKeysCache.put(entry.getKey(), entry.getValue());
        }

        Cache allParentKeysCache = new ConcurrentMapCache("allParentKeysCache");
        allParentKeysCache.put("all", parentKeysCache.getNativeCache());

        Mockito.when(contentProvider.generateParentKeysMap()).thenReturn(parentKeysMap);

        Mockito.when(cacheManager.getCache("parentKeysCache")).thenReturn(parentKeysCache);
        Mockito.when(cacheManager.getCache("allParentKeysCache")).thenReturn(allParentKeysCache);
        Mockito.when(cacheManager.getCache("draftParentCache")).thenReturn(draftParentCache);
        Mockito.when(cacheManager.getCache("draftNodes")).thenReturn(draftNodesCache);

        Mockito.when(contextService.buildGraphFromParentKeyStructure(Mockito.any(ContentKey.class), Mockito.anyMap())).thenCallRealMethod();
        Mockito.when(contextService.selectTopKeysOf(Mockito.any(ContentKey.class), Mockito.anyList())).thenCallRealMethod();
        Mockito.when(contextService.findContextsOf(Mockito.any(ContentKey.class), Mockito.anyMap())).thenCallRealMethod();

        Mockito.when(contentKeyParentsCollectorService.createParentKeysMap(Mockito.anyMap()))
                .thenReturn((Map<ContentKey, Set<ContentKey>>) allParentKeysCache.get("all").get());

        Mockito.when(contentProviderService.getParentKeys(Mockito.any(ContentKey.class))).thenAnswer(new Answer<Set<ContentKey>>() {
            @Override
            public Set<ContentKey> answer(InvocationOnMock invocation) throws Throwable {
                Object[] params = invocation.getArguments();
                ValueWrapper cachedEntry = parentKeysCache.get(params[0]);
                return (Set<ContentKey>) (cachedEntry == null ? Collections.emptySet() : cachedEntry.get());
            }
        });

    }

    @Test
    public void testOrphanContentKeysWithMainDraft() {
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(new DraftContext());
        Mockito.when(contentProviderService.isOrphan(Mockito.any(ContentKey.class), Mockito.any(ContentKey.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return Boolean.TRUE;
            }
        });

        assertTrue("It must be orphan", underTest.isOrphan(get(Product, "prd_orphan"), RootContentKey.STORE_FRESHDIRECT.contentKey));
        assertTrue("It must be orphan", underTest.isOrphan(get(Category, "cat_orphan"), RootContentKey.STORE_FRESHDIRECT.contentKey));
        assertTrue("Subcategory of orphan category must be orphan too", underTest.isOrphan(get(Category, "cat_1"), null));
    }

    @Test
    public void testNonOrphanContentKeysWithMainDraft() {
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(new DraftContext());
        assertTrue("Product having ancestry chain up to store level may not be orphan", !underTest.isOrphan(get(Product, "prd_2"), RootContentKey.STORE_FRESHDIRECT.contentKey));
        assertTrue("Product having at least one valid context should not be orphan", !underTest.isOrphan(get(Product, "prd_1"), RootContentKey.STORE_FRESHDIRECT.contentKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPrimaryHomesWithNullContentKeyWithMainDraft() {
        underTest.findPrimaryHomes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPrimaryHomesWithNotProductContentKeyWithMainDraft() {
        underTest.findPrimaryHomes(ContentKeyFactory.get(ContentType.Category, "whatever"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindPrimaryHomesWhenEverythingIsOkayWithMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "prd_2");
        ContentKey parentNotPrimaryHome = ContentKeyFactory.get(ContentType.Category, "cat_1");
        ContentKey parentPrimaryHome = ContentKeyFactory.get(ContentType.Category, "cat_2");
        ContentKey department = ContentKeyFactory.get(ContentType.Department, "dept1");
        ContentKey store = RootContentKey.STORE_FRESHDIRECT.contentKey;

        Mockito
            .doReturn(
                Arrays.asList(
                    Arrays.asList(productKey, parentNotPrimaryHome, department, store),
                    Arrays.asList(productKey, parentPrimaryHome, department, store)
                )
            )
            .when(underTest).findContextsOf(productKey);
        Mockito
            .when(draftContextHolder.getDraftContext()).thenReturn(new DraftContext());
        Mockito
            .when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
            .thenReturn(Optional.<Object>of(Arrays.asList(parentPrimaryHome, parentNotPrimaryHome)));


        Map<ContentKey, ContentKey> productPrimaryHomes = underTest.findPrimaryHomes(productKey);

        Assert.assertNotNull(productPrimaryHomes);
        Assert.assertEquals(1, productPrimaryHomes.size());
        Assert.assertTrue(productPrimaryHomes.keySet().contains(RootContentKey.STORE_FRESHDIRECT.contentKey));
        Assert.assertEquals(parentPrimaryHome, productPrimaryHomes.get(RootContentKey.STORE_FRESHDIRECT.contentKey));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindPrimaryHomesWithMultipleStoresWithMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "prd_2");
        ContentKey parentNotPrimaryHome = ContentKeyFactory.get(ContentType.Category, "cat_1");
        ContentKey parentPrimaryHome = ContentKeyFactory.get(ContentType.Category, "cat_2");
        ContentKey parentPrimaryHomeInFDX = ContentKeyFactory.get(ContentType.Category, "cat_2");
        ContentKey department = ContentKeyFactory.get(ContentType.Department, "dept1");
        ContentKey departmentFDX = ContentKeyFactory.get(ContentType.Department, "dept_fdx");
        ContentKey freshdirectStore = RootContentKey.STORE_FRESHDIRECT.contentKey;
        ContentKey fdxStore = RootContentKey.STORE_FOODKICK.contentKey;

        final List<ContentKey> primaryHomeAttributeValue = Arrays.asList(parentPrimaryHome, parentPrimaryHomeInFDX);

        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
            .thenReturn(Optional.<Object>of(primaryHomeAttributeValue));

        Mockito.doReturn(
                Arrays.asList(Arrays.asList(productKey, parentNotPrimaryHome, department, freshdirectStore),
                Arrays.asList(productKey, parentPrimaryHome, department, freshdirectStore),
                Arrays.asList(productKey, parentPrimaryHomeInFDX, departmentFDX, fdxStore)))
            .when(underTest).findContextsOf(productKey);

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(new DraftContext());

        Map<ContentKey, ContentKey> productPrimaryHomes = underTest.findPrimaryHomes(productKey);

        Assert.assertNotNull(productPrimaryHomes);
        Assert.assertEquals(2, productPrimaryHomes.size());
        Assert.assertTrue(productPrimaryHomes.containsKey(freshdirectStore));
        Assert.assertTrue(productPrimaryHomes.containsKey(fdxStore));
        Assert.assertEquals(parentPrimaryHome, productPrimaryHomes.get(RootContentKey.STORE_FRESHDIRECT.contentKey));
        Assert.assertEquals(parentPrimaryHomeInFDX, productPrimaryHomes.get(RootContentKey.STORE_FOODKICK.contentKey));
    }

    @Test
    public void testGetAllAttributesForContentKeyWithNotMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_prd");

        Map<Attribute, Object> mainNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.FULL_NAME, "full_name_main");

        Map<ContentKey, Map<Attribute, Object>> mainNodeWithContentKey = new HashMap<ContentKey, Map<Attribute, Object>>();
        mainNodeWithContentKey.put(productKey, mainNode);

        Map<Attribute, Object> draftNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.FULL_NAME, "draft_overriden_full_name");

        Map<ContentKey, Map<Attribute, Object>> draftNodeWithContentKey = new HashMap<ContentKey, Map<Attribute, Object>>();
        mainNodeWithContentKey.put(productKey, draftNode);

        DraftContext draftContext = new DraftContext();
        draftContext.setDraftId(1);
        draftContext.setDraftName("testDraftContext");
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(draftContext);

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName(ContentTypes.Product.FULL_NAME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue("draft_overriden_full_name");

        Mockito.when(contentProviderService.getAllAttributesForContentKey(productKey)).thenReturn(mainNode);

        Mockito.when(draftService.getDraftChanges(draftContext.getDraftId())).thenReturn(Arrays.asList(draftChange));
        Mockito.when(draftApplicatorService.applyDraftChangesToContentNodes(Arrays.asList(draftChange), mainNodeWithContentKey)).thenReturn(draftNodeWithContentKey);

        Map<Attribute, Object> attributes = underTest.getAllAttributesForContentKey(productKey);

        Assert.assertTrue(attributes.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("draft_overriden_full_name", attributes.get(ContentTypes.Product.FULL_NAME));
    }

    @Test
    public void testGetAttributeValuesWithNotMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_prd");

        Map<Attribute, Object> mainNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.FULL_NAME, "full_name_main");

        Map<ContentKey, Map<Attribute, Object>> mainNodeWithContentKey = new HashMap<ContentKey, Map<Attribute, Object>>();
        mainNodeWithContentKey.put(productKey, mainNode);

        Map<Attribute, Object> draftNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.FULL_NAME, "draft_overriden_full_name");

        Map<ContentKey, Map<Attribute, Object>> draftNodeWithContentKey = new HashMap<ContentKey, Map<Attribute, Object>>();
        mainNodeWithContentKey.put(productKey, draftNode);

        DraftContext draftContext = new DraftContext();
        draftContext.setDraftId(1);
        draftContext.setDraftName("testDraftContext");
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(draftContext);

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName(ContentTypes.Product.FULL_NAME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue("draft_overriden_full_name");

        Mockito.when(contentProviderService.getAllAttributesForContentKey(productKey)).thenReturn(mainNode);

        Mockito.when(draftService.getDraftChanges(draftContext.getDraftId())).thenReturn(Arrays.asList(draftChange));
        Mockito.when(draftApplicatorService.applyDraftChangesToContentNodes(Arrays.asList(draftChange), mainNodeWithContentKey)).thenReturn(draftNodeWithContentKey);

        Map<Attribute, Object> attributes = underTest.getAttributeValues(productKey, Arrays.asList(ContentTypes.Product.FULL_NAME));

        Assert.assertTrue(attributes.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("draft_overriden_full_name", attributes.get(ContentTypes.Product.FULL_NAME));
    }

    @Test
    public void testGetAttributeValueWithNotMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_prd");

        Map<Attribute, Object> mainNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.FULL_NAME, "full_name_main");

        Map<ContentKey, Map<Attribute, Object>> mainNodeWithContentKey = new HashMap<ContentKey, Map<Attribute, Object>>();
        mainNodeWithContentKey.put(productKey, mainNode);

        Map<Attribute, Object> draftNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.FULL_NAME, "draft_overriden_full_name");

        Map<ContentKey, Map<Attribute, Object>> draftNodeWithContentKey = new HashMap<ContentKey, Map<Attribute, Object>>();
        mainNodeWithContentKey.put(productKey, draftNode);

        DraftContext draftContext = new DraftContext();
        draftContext.setDraftId(1);
        draftContext.setDraftName("testDraftContext");
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(draftContext);

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName(ContentTypes.Product.FULL_NAME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue("draft_overriden_full_name");

        Mockito.when(contentProviderService.getAllAttributesForContentKey(productKey)).thenReturn(mainNode);

        Mockito.when(draftService.getDraftChanges(draftContext.getDraftId())).thenReturn(Arrays.asList(draftChange));
        Mockito.when(draftApplicatorService.applyDraftChangesToContentNodes(Arrays.asList(draftChange), mainNodeWithContentKey)).thenReturn(draftNodeWithContentKey);

        Optional<Object> attributeValueOptional = underTest.getAttributeValue(productKey, ContentTypes.Product.FULL_NAME);

        Assert.assertTrue(attributeValueOptional.isPresent());
        Assert.assertEquals("draft_overriden_full_name", attributeValueOptional.get());
    }

    @Test
    public void testGetParentKeysOnDraftWhenNotParentChanged() {
        DraftContext draftContext = new DraftContext();
        draftContext.setDraftId(1L);
        draftContext.setDraftName("test");

        ContentKey productOfTest = ContentKeyFactory.get(ContentType.Product, "prd_2");

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(draftContext);
        Mockito.when(draftService.getAllChangedContentKeys(Mockito.anyLong())).thenReturn(new HashSet<ContentKey>());

        Set<ContentKey> parentsOnMain = contentProviderService.getParentKeys(productOfTest);

        Set<ContentKey> parentsOnDraft = underTest.getParentKeys(get(Product, "prd_2"));

        Assert.assertNotNull(parentsOnDraft);
        Assert.assertFalse(parentsOnDraft.isEmpty());
        Assert.assertEquals(parentsOnMain, parentsOnDraft);
    }

    @Test
    public void testGetParentKeysOnDraftWhenNewParentOnDraft() {
        DraftContext draftContext = new DraftContext();
        draftContext.setDraftId(1L);
        draftContext.setDraftName("test");

        final ContentKey productOfTest = ContentKeyFactory.get(ContentType.Product, "prd_2");
        final ContentKey parentCatOnDraft = ContentKeyFactory.get(ContentType.Category, "only_exists_on_draft");

        Mockito.when(draftContextHolder.getDraftContext())
                .thenReturn(draftContext);
        Mockito.when(draftService.getAllChangedContentKeys(Mockito.anyLong()))
                .thenReturn(ImmutableSet.<ContentKey>of(parentCatOnDraft));
        Mockito.doReturn(ImmutableSet.<ContentKey>of(productOfTest))
                .when(underTest).getChildKeys(parentCatOnDraft, true);

        Set<ContentKey> parentsOnDraft = underTest.getParentKeys(get(Product, "prd_2"));

        Assert.assertNotNull(parentsOnDraft);
        Assert.assertFalse(parentsOnDraft.isEmpty());
        Assert.assertTrue(parentsOnDraft.size() > 1);
        Assert.assertTrue(parentsOnDraft.contains(get(Category, "cat_2")));
        Assert.assertTrue(parentsOnDraft.contains(parentCatOnDraft));
    }

    @Test
    public void testGetParentKeysOnDraftWhenParentFromMainIsNoParentAnymore() {
        DraftContext draftContext = new DraftContext();
        draftContext.setDraftId(1L);
        draftContext.setDraftName("test");

        final ContentKey parentCatOnMain = ContentKeyFactory.get(ContentType.Category, "cat_2");

        Mockito.when(draftContextHolder.getDraftContext())
                .thenReturn(draftContext);
        Mockito.when(draftService.getAllChangedContentKeys(Mockito.anyLong()))
                .thenReturn(ImmutableSet.<ContentKey>of(parentCatOnMain));
        Mockito.doReturn(new HashSet<ContentKey>())
                .when(underTest).getChildKeys(parentCatOnMain, true);

        Set<ContentKey> parentsOnDraft = underTest.getParentKeys(get(Product, "prd_2"));

        Assert.assertNotNull(parentsOnDraft);
        Assert.assertTrue(parentsOnDraft.isEmpty());
        Assert.assertFalse(parentsOnDraft.contains(parentCatOnMain));
    }

    @Test
    public void testGetParentKeysOnDraftWhenParentsWereExchanged() {
        DraftContext draftContext = new DraftContext();
        draftContext.setDraftId(1L);
        draftContext.setDraftName("test");

        final ContentKey productOfTest = ContentKeyFactory.get(ContentType.Product, "prd_2");
        final ContentKey parentCatOnMain = ContentKeyFactory.get(ContentType.Category, "cat_2");
        final ContentKey parentCatOnDraft = ContentKeyFactory.get(ContentType.Category, "only_exists_on_draft");

        Mockito.when(draftContextHolder.getDraftContext())
                .thenReturn(draftContext);
        Mockito.when(draftService.getAllChangedContentKeys(Mockito.anyLong()))
                .thenReturn(ImmutableSet.<ContentKey>of(parentCatOnDraft, parentCatOnMain));
        Mockito.doReturn(ImmutableSet.<ContentKey>of(productOfTest))
                .when(underTest).getChildKeys(parentCatOnDraft, true);
        Mockito.doReturn(new HashSet<ContentKey>())
                .when(underTest).getChildKeys(parentCatOnMain, true);

        Set<ContentKey> parentsOnDraft = underTest.getParentKeys(get(Product, "prd_2"));

        Assert.assertNotNull(parentsOnDraft);
        Assert.assertEquals(1, parentsOnDraft.size());
        Assert.assertTrue(parentsOnDraft.contains(parentCatOnDraft));
    }

    @Test
    public void testFindContextsOf() {
        DraftContext draftContext = new DraftContext();
        draftContext.setDraftId(1L);
        draftContext.setDraftName("test");

        ContentKey notOrphanProduct = get(Product, "prd_1");

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(draftContext);
        Mockito.when(draftService.getAllChangedContentKeys(Mockito.anyLong())).thenReturn(new HashSet<ContentKey>());

        List<List<ContentKey>> contexts = underTest.findContextsOf(notOrphanProduct);

        Assert.assertNotNull(contexts);
        Assert.assertEquals(2, contexts.size());
    }
}
