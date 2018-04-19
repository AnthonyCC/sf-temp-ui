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
import com.freshdirect.cms.core.service.ContentProvider;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextService;
import com.freshdirect.cms.draft.domain.Draft;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftContentProviderServiceTest {

    private static final String DRAFT_NAME = "testDraft";
    private static final Long DRAFT_ID = 1234L;

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
    private ContentTypeInfoService contentTypeInfoService;

    @Mock
    private ContentProvider contentProvider;

    @Spy
    private ContextService contextService;

    @Mock
    private CacheManager cacheManager;

    private Draft testDraft;
    private DraftContext testDraftContext = new DraftContext(DRAFT_ID, DRAFT_NAME);
    private DraftContext mainDraftContext = new DraftContext();

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

        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "FULL_NAME")).thenReturn(Optional.of(ContentTypes.Product.FULL_NAME));
        Mockito.when(contentTypeInfoService.findAttributeByName(Mockito.any(ContentType.class), Mockito.any(String.class))).thenReturn(Optional.<Attribute>absent());

        // mock content keys
        Mockito.when(contentProviderService.getContentKeysByType(Store))
                .thenReturn(ImmutableSet.<ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey, RootContentKey.STORE_FOODKICK.contentKey));
        Mockito.when(contentProviderService.getContentKeysByType(Department))
                .thenReturn(ImmutableSet.<ContentKey>of(get(Department, "dept1"), get(Department, "dept_fdx")));
        Mockito.when(contentProviderService.getContentKeysByType(Category))
                .thenReturn(ImmutableSet.<ContentKey>of(get(Category, "cat_1"), get(Category, "cat_2"), get(Category, "cat_fdx"), get(Category, "cat_orphan")));
        Mockito.when(contentProviderService.getContentKeysByType(Product))
                .thenReturn(ImmutableSet.<ContentKey>of(get(Product, "prd_1"), get(Product, "prd_2"), get(Product, "prd_orphan")));
        Mockito.when(contentProviderService.getContentKeys())
                .thenReturn(ImmutableSet.<ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey, RootContentKey.STORE_FOODKICK.contentKey,
                        get(Department, "dept1"), get(Department, "dept_fdx"),
                        get(Category, "cat_1"), get(Category, "cat_2"), get(Category, "cat_fdx"), get(Category, "cat_orphan"),
                        get(Product, "prd_1"), get(Product, "prd_2"), get(Product, "prd_orphan")));

        // mock child keys
        Mockito.when(contentProviderService.getAttributeValues(RootContentKey.STORE_FRESHDIRECT.contentKey, asList(new Attribute[] {ContentTypes.Store.departments})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, asList(new ContentKey[] {get(Department, "dept1")})));
        Mockito.when(contentProviderService.getAllAttributesForContentKey(RootContentKey.STORE_FRESHDIRECT.contentKey))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, asList(new ContentKey[] {get(Department, "dept1")})));

        Mockito.when(contentProviderService.getAttributeValues(RootContentKey.STORE_FOODKICK.contentKey, asList(new Attribute[] {ContentTypes.Store.departments})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, asList(new ContentKey[] {get(Department, "dept_fdx")})));
        Mockito.when(contentProviderService.getAllAttributesForContentKey(RootContentKey.STORE_FOODKICK.contentKey))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, asList(new ContentKey[] {get(Department, "dept_fdx")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Department, "dept1"), asList(new Attribute[] {ContentTypes.Department.categories})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, asList(new ContentKey[] {get(Category, "cat_1"), get(Category, "cat_2")})));
        Mockito.when(contentProviderService.getAllAttributesForContentKey(get(Department, "dept1")))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, asList(new ContentKey[] {get(Category, "cat_1"), get(Category, "cat_2")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Department, "dept_fdx"), asList(new Attribute[] {ContentTypes.Department.categories})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, asList(new ContentKey[] {get(Category, "cat_fdx")})));
        Mockito.when(contentProviderService.getAllAttributesForContentKey(get(Department, "dept_fdx")))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, asList(new ContentKey[] {get(Category, "cat_fdx")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Category, "cat_orphan"), asList(new Attribute[] {ContentTypes.Category.subcategories})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.subcategories, asList(new ContentKey[] {get(Category, "cat_1")})));
        Mockito.when(contentProviderService.getAllAttributesForContentKey(get(Category, "cat_orphan")))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.subcategories, asList(new ContentKey[] {get(Category, "cat_1")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Category, "cat_1"), asList(new Attribute[] {ContentTypes.Category.products})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1"), get(Product, "prd_orphan")})));
        Mockito.when(contentProviderService.getAllAttributesForContentKey(get(Category, "cat_1")))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1"), get(Product, "prd_orphan")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Category, "cat_2"), asList(new Attribute[] {ContentTypes.Category.products})))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1"), get(Product, "prd_2")})));
        Mockito.when(contentProviderService.getAllAttributesForContentKey(get(Category, "cat_2")))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1"), get(Product, "prd_2")})));

        Mockito.when(contentProviderService.getAttributeValues(get(Category, "cat_fdx"), asList(new Attribute[] {ContentTypes.Category.products})))
            .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1"), get(Product, "prd_2")})));
        Mockito.when(contentProviderService.getAllAttributesForContentKey(get(Category, "cat_fdx")))
            .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, asList(new ContentKey[] {get(Product, "prd_1"), get(Product, "prd_2")})));

        Mockito.when(contentProviderService.getAllAttributesForContentKey(get(Product, "prd_2")))
                .thenReturn(ImmutableMap.<Attribute, Object>of(ContentTypes.Product.PRIMARY_HOME, asList(new ContentKey[] {get(Category, "cat_2")})));

        final Cache draftParentCache = new ConcurrentMapCache("draftParentCache");
        final Cache draftNodesCache = new ConcurrentMapCache("draftNodes");
        Mockito.when(cacheManager.getCache("draftParentCache")).thenReturn(draftParentCache);
        Mockito.when(cacheManager.getCache("draftNodes")).thenReturn(draftNodesCache);

        final Map<ContentKey, Set<ContentKey>> parentKeysMap = new HashMap<ContentKey, Set<ContentKey>>();
        parentKeysMap.put(get(Product, "prd_1"), ImmutableSet.<ContentKey>of(get(Category, "cat_1"), get(Category, "cat_2")));
        parentKeysMap.put(get(Product, "prd_2"), ImmutableSet.<ContentKey>of(get(Category, "cat_2")));
        parentKeysMap.put(get(Product, "prd_orphan"), ImmutableSet.<ContentKey>of(get(Category, "cat_1")));
        parentKeysMap.put(get(Category, "cat_1"), ImmutableSet.<ContentKey>of(get(Category, "cat_orphan")));
        parentKeysMap.put(get(Category, "cat_2"), ImmutableSet.<ContentKey>of(get(Department, "dept1")));
        parentKeysMap.put(get(Category, "cat_fdx"), ImmutableSet.<ContentKey>of(get(Department, "dept_fdx")));
        parentKeysMap.put(get(Department, "dept1"), ImmutableSet.<ContentKey>of(RootContentKey.STORE_FRESHDIRECT.contentKey));
        parentKeysMap.put(get(Department, "dept_fdx"), ImmutableSet.<ContentKey>of(RootContentKey.STORE_FOODKICK.contentKey));
        Mockito.when(contentProvider.generateParentKeysMap()).thenReturn(parentKeysMap);

        Mockito.when(contentProviderService.getParentKeys(Mockito.any(ContentKey.class))).thenAnswer(new Answer<Set<ContentKey>>() {
            @Override
            public Set<ContentKey> answer(InvocationOnMock invocation) throws Throwable {
                Object[] params = invocation.getArguments();
                Set<ContentKey> result = parentKeysMap.get(params[0]);
                return (Set<ContentKey>) (result == null ? Collections.emptySet() : result);
            }
        });

        // test draft
        testDraft = new Draft();
        testDraft.setId(DRAFT_ID);
        testDraft.setName(DRAFT_NAME);
    }

    @Test
    public void testGetContentKeysOnMain() {
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(mainDraftContext);
        Assert.assertEquals(11, underTest.getContentKeys().size());
        Assert.assertEquals(3, underTest.getContentKeysByType(ContentType.Product).size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Category).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Department).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Store).size());
    }

    @Test
    public void testGetContentKeysOnDraft() {
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(testDraftContext);
        Assert.assertEquals(11, underTest.getContentKeys().size());
        Assert.assertEquals(3, underTest.getContentKeysByType(ContentType.Product).size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Category).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Department).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Store).size());
    }

    @Test
    public void testOrphanContentKeysWithMainDraft() {
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(mainDraftContext);
        Mockito.when(contentProviderService.isOrphan(Mockito.any(ContentKey.class), Mockito.any(ContentKey.class))).thenReturn(Boolean.TRUE);

        assertTrue("It must be orphan", underTest.isOrphan(get(Product, "prd_orphan"), RootContentKey.STORE_FRESHDIRECT.contentKey));
        assertTrue("It must be orphan", underTest.isOrphan(get(Category, "cat_orphan"), RootContentKey.STORE_FRESHDIRECT.contentKey));
        assertTrue("Subcategory of orphan category must be orphan too", underTest.isOrphan(get(Category, "cat_1"), null));
    }

    @Test
    public void testNonOrphanContentKeysWithMainDraft() {
        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(mainDraftContext);
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

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(mainDraftContext);
        Mockito.when(contentProviderService.findContextsOf(productKey)).thenReturn(Arrays.asList(
                Arrays.asList(productKey, parentNotPrimaryHome, department, store),
                Arrays.asList(productKey, parentPrimaryHome, department, store)));
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
            .thenReturn(Optional.<Object>of(Arrays.asList(parentPrimaryHome, parentNotPrimaryHome)));

        Map<ContentKey, ContentKey> productPrimaryHomes = underTest.findPrimaryHomes(productKey);

        Assert.assertNotNull(productPrimaryHomes);
        Assert.assertEquals(1, productPrimaryHomes.size());
        Assert.assertTrue(productPrimaryHomes.keySet().contains(RootContentKey.STORE_FRESHDIRECT.contentKey));
        Assert.assertEquals(parentPrimaryHome, productPrimaryHomes.get(RootContentKey.STORE_FRESHDIRECT.contentKey));

        Assert.assertEquals(11, underTest.getContentKeys().size());
        Assert.assertEquals(3, underTest.getContentKeysByType(ContentType.Product).size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Category).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Department).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Store).size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindPrimaryHomesWithMultipleStoresWithMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "prd_2");
        ContentKey parentNotPrimaryHome = ContentKeyFactory.get(ContentType.Category, "cat_1");
        ContentKey parentPrimaryHome = ContentKeyFactory.get(ContentType.Category, "cat_2");
        ContentKey parentPrimaryHomeInFDX = ContentKeyFactory.get(ContentType.Category, "cat_fdx");
        ContentKey department = ContentKeyFactory.get(ContentType.Department, "dept1");
        ContentKey departmentFDX = ContentKeyFactory.get(ContentType.Department, "dept_fdx");
        ContentKey freshdirectStore = RootContentKey.STORE_FRESHDIRECT.contentKey;
        ContentKey fdxStore = RootContentKey.STORE_FOODKICK.contentKey;

        final List<ContentKey> primaryHomeAttributeValue = Arrays.asList(parentPrimaryHome, parentPrimaryHomeInFDX);

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(mainDraftContext);
        Mockito.when(contentProviderService.getAttributeValue(productKey, ContentTypes.Product.PRIMARY_HOME))
            .thenReturn(Optional.<Object>of(primaryHomeAttributeValue));
        Mockito.when(contentProviderService.findContextsOf(productKey)).thenReturn(Arrays.asList(
                Arrays.asList(productKey, parentNotPrimaryHome, department, freshdirectStore),
                Arrays.asList(productKey, parentPrimaryHome, department, freshdirectStore),
                Arrays.asList(productKey, parentPrimaryHomeInFDX, departmentFDX, fdxStore)));
        Mockito.when(contentProviderService.getParentKeys(productKey)).thenReturn(ImmutableSet.of(parentNotPrimaryHome, parentPrimaryHome, parentPrimaryHomeInFDX));
        Mockito.when(contentProviderService.getParentKeys(parentNotPrimaryHome)).thenReturn(ImmutableSet.of(department));
        Mockito.when(contentProviderService.getParentKeys(parentPrimaryHome)).thenReturn(ImmutableSet.of(department));
        Mockito.when(contentProviderService.getParentKeys(parentPrimaryHomeInFDX)).thenReturn(ImmutableSet.of(departmentFDX));

        Map<ContentKey, ContentKey> productPrimaryHomes = underTest.findPrimaryHomes(productKey);

        Assert.assertNotNull(productPrimaryHomes);
        Assert.assertEquals(2, productPrimaryHomes.size());
        Assert.assertTrue(productPrimaryHomes.containsKey(freshdirectStore));
        Assert.assertTrue(productPrimaryHomes.containsKey(fdxStore));
        Assert.assertEquals(parentPrimaryHome, productPrimaryHomes.get(RootContentKey.STORE_FRESHDIRECT.contentKey));
        Assert.assertEquals(parentPrimaryHomeInFDX, productPrimaryHomes.get(RootContentKey.STORE_FOODKICK.contentKey));

        Assert.assertEquals(11, underTest.getContentKeys().size());
        Assert.assertEquals(3, underTest.getContentKeysByType(ContentType.Product).size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Category).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Department).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Store).size());
    }

    @Test
    public void testGetAllAttributesForContentKeyWithNotMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_prd");
        Map<Attribute, Object> mainNode = ImmutableMap.of(ContentTypes.Product.FULL_NAME, (Object) "full_name_main");

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(testDraftContext);
        DraftChange draftChange = new DraftChange();
        draftChange.setDraft(testDraft);
        draftChange.setAttributeName(ContentTypes.Product.FULL_NAME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue("draft_overriden_full_name");
        List<DraftChange> draftChanges = ImmutableList.of(draftChange);

        Mockito.when(contentProviderService.getAllAttributesForContentKey(productKey)).thenReturn(mainNode);
        Mockito.when(draftService.getDraftChanges(DRAFT_ID)).thenReturn(draftChanges);

        Mockito.when(draftApplicatorService.convertDraftChanges(Mockito.eq(draftChanges)))
            .thenReturn(ImmutableMap.<ContentKey, Map<Attribute, Object>>of(productKey,
                    ImmutableMap.<Attribute, Object>of(ContentTypes.Product.FULL_NAME, "draft_overriden_full_name")));

        Map<Attribute, Object> attributes = underTest.getAllAttributesForContentKey(productKey);

        Assert.assertTrue(attributes.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("draft_overriden_full_name", attributes.get(ContentTypes.Product.FULL_NAME));

        Assert.assertEquals(12, underTest.getContentKeys().size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Product).size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Category).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Department).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Store).size());
    }

    @Test
    public void testGetAttributeValuesWithNotMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_prd");
        Map<Attribute, Object> mainNode = ImmutableMap.of(ContentTypes.Product.FULL_NAME, (Object) "full_name_main");

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(testDraftContext);
        DraftChange draftChange = new DraftChange();
        draftChange.setDraft(testDraft);
        draftChange.setAttributeName(ContentTypes.Product.FULL_NAME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue("draft_overriden_full_name");
        List<DraftChange> draftChanges = ImmutableList.of(draftChange);

        Mockito.when(contentProviderService.getAllAttributesForContentKey(productKey)).thenReturn(mainNode);
        Mockito.when(draftService.getDraftChanges(DRAFT_ID)).thenReturn(ImmutableList.of(draftChange));

        Mockito.when(draftApplicatorService.convertDraftChanges(Mockito.eq(draftChanges)))
            .thenReturn(ImmutableMap.<ContentKey, Map<Attribute, Object>>of(productKey,
                    ImmutableMap.<Attribute, Object>of(ContentTypes.Product.FULL_NAME, "draft_overriden_full_name")));

        Map<Attribute, Object> attributes = underTest.getAttributeValues(productKey, Arrays.asList(ContentTypes.Product.FULL_NAME));

        Assert.assertTrue(attributes.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals("draft_overriden_full_name", attributes.get(ContentTypes.Product.FULL_NAME));

        Assert.assertEquals(12, underTest.getContentKeys().size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Product).size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Category).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Department).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Store).size());
    }

    @Test
    public void testGetAttributeValueWithNotMainDraft() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_prd");
        Map<Attribute, Object> mainNode = ImmutableMap.of(ContentTypes.Product.FULL_NAME, (Object) "full_name_main");

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(testDraftContext);
        DraftChange draftChange = new DraftChange();
        draftChange.setDraft(testDraft);
        draftChange.setAttributeName(ContentTypes.Product.FULL_NAME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue("draft_overriden_full_name");
        List<DraftChange> draftChanges = ImmutableList.of(draftChange);

        Mockito.when(contentProviderService.getAllAttributesForContentKey(productKey)).thenReturn(mainNode);
        Mockito.when(draftService.getDraftChanges(DRAFT_ID)).thenReturn(ImmutableList.of(draftChange));

        Mockito.when(draftApplicatorService.convertDraftChanges(Mockito.eq(draftChanges)))
            .thenReturn(ImmutableMap.<ContentKey, Map<Attribute, Object>>of(productKey,
                    ImmutableMap.<Attribute, Object>of(ContentTypes.Product.FULL_NAME, "draft_overriden_full_name")));

        Optional<Object> attributeValueOptional = underTest.getAttributeValue(productKey, ContentTypes.Product.FULL_NAME);

        Assert.assertTrue(attributeValueOptional.isPresent());
        Assert.assertEquals("draft_overriden_full_name", attributeValueOptional.get());

        Assert.assertEquals(12, underTest.getContentKeys().size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Product).size());
        Assert.assertEquals(4, underTest.getContentKeysByType(ContentType.Category).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Department).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Store).size());
    }

    @Test
    public void testGetParentKeysOnDraftWhenNotParentChanged() {
        ContentKey productOfTest = ContentKeyFactory.get(ContentType.Product, "prd_2");

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(testDraftContext);
        Mockito.when(draftService.getDraftChanges(Mockito.anyLong())).thenReturn(Collections.<DraftChange>emptyList());

        Set<ContentKey> parentsOnMain = contentProviderService.getParentKeys(productOfTest);

        Set<ContentKey> parentsOnDraft = underTest.getParentKeys(get(Product, "prd_2"));

        Assert.assertNotNull(parentsOnDraft);
        Assert.assertFalse(parentsOnDraft.isEmpty());
        Assert.assertEquals(parentsOnMain, parentsOnDraft);
    }

    @Test
    public void testGetParentKeysOnDraftWhenNewParentOnDraft() {
        final ContentKey productOfTest = ContentKeyFactory.get(ContentType.Product, "prd_2");
        final ContentKey parentCatOnDraft = ContentKeyFactory.get(ContentType.Category, "only_exists_on_draft");

        DraftChange dc = new DraftChange();
        dc.setContentKey(parentCatOnDraft.toString());
        dc.setAttributeName(ContentTypes.Category.products.getName());
        dc.setValue(productOfTest.toString());
        List<DraftChange> draftChanges = ImmutableList.of(dc);

        Mockito.when(draftContextHolder.getDraftContext())
                .thenReturn(testDraftContext);
        Mockito.when(draftService.getDraftChanges(Mockito.anyLong()))
                .thenReturn(draftChanges);
        Mockito.when(draftApplicatorService.convertDraftChanges(Mockito.eq(draftChanges)))
                .thenReturn(ImmutableMap.<ContentKey, Map<Attribute, Object>>of(parentCatOnDraft,
                        ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, (Object) ImmutableList.of(productOfTest))));

        Set<ContentKey> parentsOnDraft = underTest.getParentKeys(get(Product, "prd_2"));

        Assert.assertNotNull(parentsOnDraft);
        Assert.assertFalse(parentsOnDraft.isEmpty());
        Assert.assertTrue(parentsOnDraft.size() == 2);
        Assert.assertTrue(parentsOnDraft.contains(get(Category, "cat_2")));
        Assert.assertTrue(parentsOnDraft.contains(parentCatOnDraft));
    }

    @Test
    public void testGetParentKeysOnDraftWhenParentFromMainIsNoParentAnymore() {
        final ContentKey parentCatOnMain = ContentKeyFactory.get(ContentType.Category, "cat_2");

        DraftChange dc = new DraftChange();
        dc.setContentKey(parentCatOnMain.toString());
        dc.setAttributeName(ContentTypes.Category.products.getName());
        dc.setValue(null);
        List<DraftChange> draftChanges = ImmutableList.of(dc);

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(testDraftContext);
        Mockito.when(draftService.getDraftChanges(Mockito.anyLong())).thenReturn(draftChanges);

        Map<ContentKey, Map<Attribute, Object>> draftNodes = ImmutableMap.<ContentKey, Map<Attribute, Object>>of(
                parentCatOnMain, ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, (Object) ImmutableList.of()));

        Mockito.when(draftApplicatorService.convertDraftChanges(Mockito.eq(draftChanges)))
                .thenReturn(draftNodes);

        Set<ContentKey> parentsOnDraft = underTest.getParentKeys(get(Product, "prd_2"));

        Assert.assertNotNull(parentsOnDraft);
        Assert.assertTrue(parentsOnDraft.isEmpty());
        Assert.assertFalse(parentsOnDraft.contains(parentCatOnMain));
    }

    @Test
    public void testGetParentKeysOnDraftWhenParentsWereExchanged() {
        final ContentKey productOfTest = ContentKeyFactory.get(ContentType.Product, "prd_2");
        final ContentKey parentCatOnMain = ContentKeyFactory.get(ContentType.Category, "cat_2");
        final ContentKey parentCatOnDraft = ContentKeyFactory.get(ContentType.Category, "only_exists_on_draft");

        DraftChange dc1 = new DraftChange();
        dc1.setContentKey(parentCatOnDraft.toString());
        dc1.setAttributeName(ContentTypes.Category.products.getName());
        dc1.setValue(productOfTest.toString());

        DraftChange dc2 = new DraftChange();
        dc2.setContentKey(parentCatOnMain.toString());
        dc2.setAttributeName(ContentTypes.Category.products.getName());
        dc2.setValue(null);

        List<DraftChange> draftChanges = ImmutableList.of(dc1, dc2);

        Map<ContentKey, Map<Attribute, Object>> draftNodes = ImmutableMap.<ContentKey, Map<Attribute, Object>>of(
                parentCatOnDraft, ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, (Object) ImmutableList.of(productOfTest)),
                parentCatOnMain, ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, (Object) ImmutableList.of()));

        Mockito.when(draftContextHolder.getDraftContext())
                .thenReturn(testDraftContext);
        Mockito.when(draftService.getDraftChanges(Mockito.anyLong()))
                .thenReturn(draftChanges);
        Mockito.when(draftApplicatorService.convertDraftChanges(Mockito.eq(draftChanges)))
                .thenReturn(draftNodes);

        Set<ContentKey> parentsOnDraft = underTest.getParentKeys(get(Product, "prd_2"));

        Assert.assertNotNull(parentsOnDraft);
        Assert.assertEquals(1, parentsOnDraft.size());
        Assert.assertTrue(parentsOnDraft.contains(parentCatOnDraft));

        Assert.assertEquals(12, underTest.getContentKeys().size());
        Assert.assertEquals(3, underTest.getContentKeysByType(ContentType.Product).size());
        Assert.assertEquals(5, underTest.getContentKeysByType(ContentType.Category).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Department).size());
        Assert.assertEquals(2, underTest.getContentKeysByType(ContentType.Store).size());
    }

    @Test
    public void testFindContextsOf() {
        ContentKey notOrphanProduct = get(Product, "prd_1");

        Mockito.when(draftContextHolder.getDraftContext()).thenReturn(testDraftContext);
        Mockito.when(draftService.getDraftChanges(Mockito.anyLong())).thenReturn(Collections.<DraftChange>emptyList());

        List<List<ContentKey>> contexts = underTest.findContextsOf(notOrphanProduct);

        Assert.assertNotNull(contexts);
        Assert.assertEquals(2, contexts.size());
    }
}
