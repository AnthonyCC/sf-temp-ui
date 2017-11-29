package com.freshdirect.cms.core.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.Department;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static com.freshdirect.cms.core.domain.RootContentKey.STORE_FRESHDIRECT;
import static com.freshdirect.cms.core.domain.RootContentKey.STORE_FOODKICK;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.util.TestContentBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@RunWith(SpringJUnit4ClassRunner.class)
@Category(UnitTest.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { com.freshdirect.cms.core.service.NodeCollectionContentProviderServiceTest.Config.class })
public class NodeCollectionContentProviderServiceTest {

    private static final Set<ContentKey> NO_PARENTS = Collections.emptySet();

    private static final ContentKey PRODUCT_KEY = get(Product, "product_node");

    private static final ContentKey DEPT_FD_KEY = get(Department, "department_fd");
    private static final ContentKey CAT_TOP_FD_KEY = get(Category, "category_top_fd");
    private static final ContentKey CAT_SUB_FD_KEY = get(Category, "category_sub_fd");

    private static final ContentKey DEPT_FDX_KEY = get(Department, "department_fdx");
    private static final ContentKey CAT_FDX_KEY = get(Category, "category_fdx");

    private static final ContentKey CAT_TOP_ORPHAN_KEY = get(Category, "category_top_orphan");
    private static final ContentKey CAT_SUB_ORPHAN_KEY = get(Category, "category_sub_orphan");

    private static final ContentKey SINGLE_NODE_KEY = get(Department, "singleNode");

    private static final ContentKey NON_EXISTING_NODE = get(Product, "does_not_exist");

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private ContentKeyParentsCollectorService contentKeyParentsCollectorService;

    private static Map<ContentKey, Map<Attribute, Object>> simpleStore() {
        Map<ContentKey, Map<Attribute, Object>> testNodes = new HashMap<ContentKey, Map<Attribute, Object>>();

        // build FreshDirect content tree: FD > Department > Category > Category > Product
        testNodes.put(STORE_FRESHDIRECT.contentKey,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, ImmutableList.of(DEPT_FD_KEY), ContentTypes.Product.NOT_SEARCHABLE, true));
        testNodes.put(DEPT_FD_KEY,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, ImmutableList.of(CAT_TOP_FD_KEY)));
        testNodes.put(CAT_TOP_FD_KEY,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Category.subcategories, ImmutableList.of(CAT_SUB_FD_KEY)));
        testNodes.put(CAT_SUB_FD_KEY,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, ImmutableList.of(PRODUCT_KEY)));

        // build orphan category: Category > Category > Product
        testNodes.put(CAT_TOP_ORPHAN_KEY,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Category.subcategories, ImmutableList.of(CAT_SUB_ORPHAN_KEY)));
        testNodes.put(CAT_SUB_ORPHAN_KEY,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, ImmutableList.of(PRODUCT_KEY)));

        // build FDX tree: FDX > Department > Category > Product
        testNodes.put(STORE_FOODKICK.contentKey,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Store.departments, ImmutableList.of(DEPT_FDX_KEY), ContentTypes.Product.NOT_SEARCHABLE, true));
        testNodes.put(DEPT_FDX_KEY,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Department.categories, ImmutableList.of(CAT_FDX_KEY)));
        testNodes.put(CAT_FDX_KEY,
                ImmutableMap.<Attribute, Object>of(ContentTypes.Category.products, ImmutableList.of(PRODUCT_KEY)));

        // product node, only has valid primary home in FDX
        testNodes.put(PRODUCT_KEY, ImmutableMap.<Attribute, Object>of(
                ContentTypes.Product.FULL_NAME, "Test Product",
                ContentTypes.Product.PRIMARY_HOME, ImmutableList.of(CAT_FDX_KEY, CAT_SUB_ORPHAN_KEY)));

        return testNodes;
    }

    private Map<ContentKey, Map<Attribute, Object>> singleNodeStore() {
        return new TestContentBuilder().newDepartment(SINGLE_NODE_KEY.id).build();
    }

    private static Map<ContentKey, Map<Attribute, Object>> emptyStore() {
        return Collections.<ContentKey, Map<Attribute, Object>>emptyMap();
    }

    private NodeCollectionContentProviderService buildProvider(Map<ContentKey, Map<Attribute, Object>> testNodes) {
        return new NodeCollectionContentProviderService(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);
    }


    @Test
    public void testContainsKey() {
        NodeCollectionContentProviderService underTest = buildProvider(simpleStore());

        assertTrue(underTest.containsContentKey(PRODUCT_KEY));
        assertTrue(underTest.containsContentKey(DEPT_FD_KEY));
        assertTrue(underTest.containsContentKey(CAT_TOP_FD_KEY));
        assertTrue(underTest.containsContentKey(CAT_SUB_FD_KEY));
        assertTrue(underTest.containsContentKey(DEPT_FDX_KEY));
        assertTrue(underTest.containsContentKey(CAT_FDX_KEY));
        assertTrue(underTest.containsContentKey(CAT_TOP_ORPHAN_KEY));
        assertTrue(underTest.containsContentKey(CAT_SUB_ORPHAN_KEY));

        assertTrue(!underTest.containsContentKey(NON_EXISTING_NODE));
    }

    @Test
    public void testParentKeys() {
        NodeCollectionContentProviderService underTest = buildProvider(simpleStore());

        final Set<ContentKey> parentKeys = underTest.getParentKeys(PRODUCT_KEY);
        assertNotNull(parentKeys);
        assertEquals(3, parentKeys.size());
        assertTrue(parentKeys.contains(CAT_SUB_FD_KEY));
        assertTrue(parentKeys.contains(CAT_SUB_ORPHAN_KEY));
        assertTrue(parentKeys.contains(CAT_FDX_KEY));

        final Set<ContentKey> parentsOfNonExisting = underTest.getParentKeys(NON_EXISTING_NODE);
        assertNotNull(parentsOfNonExisting);
        assertTrue(parentsOfNonExisting.isEmpty());
    }

    @Test
    public void testPrimaryHomes() {
        NodeCollectionContentProviderService underTest = buildProvider(simpleStore());

        final Map<ContentKey, ContentKey> primaryHomes = underTest.findPrimaryHomes(PRODUCT_KEY);
        assertNotNull(primaryHomes);
        assertEquals(1, primaryHomes.size());
        assertTrue(primaryHomes.containsKey(STORE_FOODKICK.contentKey));
        assertEquals(CAT_FDX_KEY, primaryHomes.get(STORE_FOODKICK.contentKey));

        // FIXME : throws NPE
//        final Map<ContentKey, ContentKey> primaryHomesOfNonExisting = underTest.findPrimaryHomes(NON_EXISTING_NODE);
//        assertNotNull(primaryHomesOfNonExisting);
//        assertTrue(primaryHomesOfNonExisting.isEmpty());
    }

    @Test
    public void testIsOrphan() {
        NodeCollectionContentProviderService underTest = buildProvider(simpleStore());

        assertTrue(!underTest.isOrphan(PRODUCT_KEY, STORE_FRESHDIRECT.contentKey));
        assertTrue(!underTest.isOrphan(DEPT_FD_KEY, STORE_FRESHDIRECT.contentKey));
        assertTrue(!underTest.isOrphan(CAT_TOP_FD_KEY, STORE_FRESHDIRECT.contentKey));
        assertTrue(!underTest.isOrphan(CAT_SUB_FD_KEY, STORE_FRESHDIRECT.contentKey));
        assertTrue(underTest.isOrphan(DEPT_FDX_KEY, STORE_FRESHDIRECT.contentKey));
        assertTrue(underTest.isOrphan(CAT_FDX_KEY, STORE_FRESHDIRECT.contentKey));
        assertTrue(underTest.isOrphan(CAT_TOP_ORPHAN_KEY, STORE_FRESHDIRECT.contentKey));
        assertTrue(underTest.isOrphan(CAT_SUB_ORPHAN_KEY, STORE_FRESHDIRECT.contentKey));

        assertTrue(!underTest.isOrphan(PRODUCT_KEY, STORE_FOODKICK.contentKey));
        assertTrue(underTest.isOrphan(DEPT_FD_KEY, STORE_FOODKICK.contentKey));
        assertTrue(underTest.isOrphan(CAT_TOP_FD_KEY, STORE_FOODKICK.contentKey));
        assertTrue(underTest.isOrphan(CAT_SUB_FD_KEY, STORE_FOODKICK.contentKey));
        assertTrue(!underTest.isOrphan(DEPT_FDX_KEY, STORE_FOODKICK.contentKey));
        assertTrue(!underTest.isOrphan(CAT_FDX_KEY, STORE_FOODKICK.contentKey));
        assertTrue(underTest.isOrphan(CAT_TOP_ORPHAN_KEY, STORE_FOODKICK.contentKey));
        assertTrue(underTest.isOrphan(CAT_SUB_ORPHAN_KEY, STORE_FOODKICK.contentKey));

        assertTrue(underTest.isOrphan(NON_EXISTING_NODE, STORE_FRESHDIRECT.contentKey));
        assertTrue(underTest.isOrphan(NON_EXISTING_NODE, STORE_FOODKICK.contentKey));
    }

    @Test
    public void testFilterByStore() {
        NodeCollectionContentProviderService underTest = buildProvider(simpleStore());

        final Map<ContentKey, Map<Attribute, Object>> filterFD = underTest.filterNodesToStore(STORE_FRESHDIRECT.contentKey);
        assertNotNull(filterFD);
        assertEquals(5, filterFD.size());
        assertTrue(filterFD.containsKey(STORE_FRESHDIRECT.contentKey));
        assertTrue(filterFD.containsKey(DEPT_FD_KEY));
        assertTrue(filterFD.containsKey(CAT_TOP_FD_KEY));
        assertTrue(filterFD.containsKey(CAT_SUB_FD_KEY));
        assertTrue(filterFD.containsKey(PRODUCT_KEY));

        final Map<ContentKey, Map<Attribute, Object>> filterFDX = underTest.filterNodesToStore(STORE_FOODKICK.contentKey);
        assertNotNull(filterFDX);
        assertEquals(4, filterFDX.size());
        assertTrue(filterFDX.containsKey(STORE_FOODKICK.contentKey));
        assertTrue(filterFDX.containsKey(DEPT_FDX_KEY));
        assertTrue(filterFDX.containsKey(CAT_FDX_KEY));
        assertTrue(filterFDX.containsKey(PRODUCT_KEY));
    }

    @Test
    public void testCollectByStore() {
        NodeCollectionContentProviderService underTest = buildProvider(simpleStore());

        final Map<ContentKey, Set<ContentKey>> parentsByStore = underTest.collectParentsPerStore(PRODUCT_KEY);
        assertNotNull(parentsByStore);
        assertEquals(2, parentsByStore.size());

        assertTrue(parentsByStore.containsKey(STORE_FRESHDIRECT.contentKey));
        final Set<ContentKey> parentsFD = parentsByStore.get(STORE_FRESHDIRECT.contentKey);
        assertEquals(1, parentsFD.size());
        assertTrue(parentsFD.contains(CAT_SUB_FD_KEY));

        assertTrue(parentsByStore.containsKey(STORE_FOODKICK.contentKey));
        final Set<ContentKey> parentsFDX = parentsByStore.get(STORE_FOODKICK.contentKey);
        assertEquals(1, parentsFDX.size());
        assertTrue(parentsFDX.contains(CAT_FDX_KEY));
    }

    @Test
    public void testGetKeysByType() {
        NodeCollectionContentProviderService underTest = buildProvider(simpleStore());

        final Set<ContentKey> departmentNodes = underTest.getContentKeysByType(Department);
        assertNotNull(departmentNodes);
        assertEquals(2, departmentNodes.size());
        assertTrue(departmentNodes.contains(DEPT_FD_KEY));
        assertTrue(departmentNodes.contains(DEPT_FDX_KEY));

        final Set<ContentKey> categoryNodes = underTest.getContentKeysByType(Category);
        assertNotNull(categoryNodes);
        assertEquals(5, categoryNodes.size());
        assertTrue(categoryNodes.contains(CAT_TOP_FD_KEY));
        assertTrue(categoryNodes.contains(CAT_SUB_FD_KEY));
        assertTrue(categoryNodes.contains(CAT_FDX_KEY));
        assertTrue(categoryNodes.contains(CAT_TOP_ORPHAN_KEY));
        assertTrue(categoryNodes.contains(CAT_SUB_ORPHAN_KEY));

        final Set<ContentKey> productNodes = underTest.getContentKeysByType(Product);
        assertNotNull(productNodes);
        assertEquals(1, productNodes.size());
        assertTrue(productNodes.contains(PRODUCT_KEY));

        final Set<ContentKey> skuNodes = underTest.getContentKeysByType(ContentType.Sku);
        assertNotNull(skuNodes);
        assertTrue(skuNodes.isEmpty());
    }

    @Test
    public void testParentIndexBuilderWithEmptyContent() {
        NodeCollectionContentProviderService emptyContainer = buildProvider(emptyStore());

        final ContentKey testKey = get(Category, "nonExistingCategory");
        final Map<ContentKey, Set<ContentKey>> parentIndex = emptyContainer.buildParentIndexFor(testKey);
        assertNotNull(parentIndex);
        assertTrue(parentIndex.size() == 1);
        assertTrue(parentIndex.keySet().contains(testKey));
        assertTrue(parentIndex.get(testKey).isEmpty());
        assertTrue(emptyContainer.findContextsOf(testKey).isEmpty());
    }

    @Test
    public void testParentIndexBuilderWithSingleNodeContent() {
        NodeCollectionContentProviderService singleNodeContainer = buildProvider(singleNodeStore());

        final Map<ContentKey, Set<ContentKey>> parentIndex = singleNodeContainer.buildParentIndexFor(SINGLE_NODE_KEY);
        assertParentMapsEqual(ImmutableMap.of(SINGLE_NODE_KEY, NO_PARENTS), parentIndex);

        assertTrue(singleNodeContainer.findContextsOf(SINGLE_NODE_KEY).isEmpty());
    }



    @Test
    public void testParentIndexBuilderWithNodeHavingParentAndGrandParent() {
        final ContentKey fooKey = get(Department, "fooNode");
        final ContentKey fooChildKey = get(Category, "childFooCat");
        final ContentKey fooGrandChildKey = get(Category, "childChildFooCat");

        Map<ContentKey, Map<Attribute, Object>> testNodes = new TestContentBuilder()
            .newDepartment(fooKey.id)
            .newCategory(fooChildKey.id)
                .asChild()
            .newCategory(fooGrandChildKey.id)
                .asChild()
            .build();

        NodeCollectionContentProviderService testContainer =
                new NodeCollectionContentProviderService(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        // expected: [ fooChildKey => [fooKey], fooKey => [] ]
        Map<ContentKey, Set<ContentKey>> parentIndex = testContainer.buildParentIndexFor(fooChildKey);
        assertParentMapsEqual(
            ImmutableMap.of(
                fooChildKey, ImmutableSet.of(fooKey),
                fooKey, NO_PARENTS),
            parentIndex);

        // expected: [[ fooChildKey, fooKey ]]
        List<List<ContentKey>> fooContexts = testContainer.findContextsOf(fooChildKey);
        assertTrue(fooContexts.size() == 1);
        assertEquals(Arrays.asList(fooChildKey, fooKey), fooContexts.get(0));
        assertTrue(testContainer.findContextsOf(fooKey).isEmpty()); // []

        // expected: [ fooGrandChildKey => [fooChildKey], fooChildKey => [fooKey], fooKey => [] ]
        parentIndex = testContainer.buildParentIndexFor(fooGrandChildKey);
        assertParentMapsEqual(
            ImmutableMap.of(
                fooGrandChildKey, ImmutableSet.of(fooChildKey),
                fooChildKey, ImmutableSet.of(fooKey),
                fooKey, NO_PARENTS),
            parentIndex);

        // expected: [[ fooGrandChildKey, fooChildKey, fooKey ]]
        fooContexts = testContainer.findContextsOf(fooGrandChildKey);
        assertTrue(fooContexts.size() == 1);
        assertEquals(Arrays.asList(fooGrandChildKey, fooChildKey, fooKey), fooContexts.get(0));

        // expected: [[ fooChildKey, fooKey ]]
        fooContexts = testContainer.findContextsOf(fooChildKey);
        assertTrue(fooContexts.size() == 1);
        assertEquals(Arrays.asList(fooChildKey, fooKey), fooContexts.get(0));

        assertTrue(testContainer.findContextsOf(fooKey).isEmpty()); // []
    }

    public void testParentIndexBuilderWithDisjunctNodes() {
        final ContentKey fooKey = get(Department, "fooNode");
        final ContentKey fooChildKey = get(Category, "childFooCat");
        final ContentKey barKey = get(Department, "barNode");
        final ContentKey barChildKey = get(Category, "childBarCat");

        Map<ContentKey, Map<Attribute, Object>> testNodes = new TestContentBuilder()
            .newDepartment(fooKey.id)
            .newCategory(fooChildKey.id)
                .asChild()
            .newDepartment(barKey.id)
            .newCategory(barChildKey.id)
                .asChild()
            .build();

        NodeCollectionContentProviderService testContainer =
                new NodeCollectionContentProviderService(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        // expected: [ fooChildKey => [fooKey], fooKey => [] ]
        Map<ContentKey, Set<ContentKey>> parentIndex = testContainer.buildParentIndexFor(fooChildKey);
        assertParentMapsEqual(
            ImmutableMap.of(
                fooChildKey, ImmutableSet.of(fooKey),
                fooKey, NO_PARENTS),
            parentIndex);

        // expected: [[ fooChildKey, fooKey ]]
        final List<List<ContentKey>> fooContexts = testContainer.findContextsOf(fooChildKey);
        assertTrue(fooContexts.size() == 1);
        assertEquals(Arrays.asList(fooChildKey, fooKey), fooContexts.get(0));

        // expected: []
        assertTrue(testContainer.findContextsOf(fooKey).isEmpty());

        // expected: [ barChildKey => [barKey], barKey => [] ]
        parentIndex = testContainer.buildParentIndexFor(barChildKey);
        assertParentMapsEqual(
            ImmutableMap.of(
                barChildKey, ImmutableSet.of(barKey),
                barKey, NO_PARENTS),
            parentIndex);

        // expected: [[ barChildKey, barKey ]]
        final List<List<ContentKey>> barContexts = testContainer.findContextsOf(barChildKey);
        assertTrue(barContexts.size() == 1);
        assertEquals(Arrays.asList(barChildKey, barKey), barContexts.get(0));
        assertTrue(testContainer.findContextsOf(barKey).isEmpty());
    }

    @Test
    public void testParentIndexBuilderWithAShapedContent() {
        // two products share parent category
        final ContentKey deptKey = get(Department, "dept");
        final ContentKey homeKey = get(Category, "home");
        final ContentKey prod1key = get(Product, "prd1");
        final ContentKey prod2key = get(Product, "prd2");

        final Map<ContentKey, Map<Attribute, Object>> testNodes = new TestContentBuilder()
            .newDepartment(deptKey.id)
            .newCategory(homeKey.id)
                .asChild()
            .newProduct("prd1")
                .connectedTo(homeKey)
            .newProduct("prd2")
                .connectedTo(homeKey)
            .build();

        final NodeCollectionContentProviderService testContainer =
                new NodeCollectionContentProviderService(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        // expected: [ prod1key => [homeKey], homeKey => [deptKey], deptKey => [] ]
        Map<ContentKey, Set<ContentKey>> parentIndex = testContainer.buildParentIndexFor(prod1key);
        assertParentMapsEqual(
            ImmutableMap.of(
                prod1key, ImmutableSet.of(homeKey),
                homeKey, ImmutableSet.of(deptKey),
                deptKey, NO_PARENTS),
            parentIndex);

        // expected: [ prod2key => [homeKey], homeKey => [deptKey], deptKey => [] ]
        parentIndex = testContainer.buildParentIndexFor(prod2key);
        assertParentMapsEqual(
            ImmutableMap.of(
                prod2key, ImmutableSet.of(homeKey),
                homeKey, ImmutableSet.of(deptKey),
                deptKey, NO_PARENTS),
            parentIndex);

        List<List<ContentKey>> contexts = testContainer.findContextsOf(prod1key);
        assertTrue(contexts.size() == 1);
        assertEquals(ImmutableList.of(prod1key, homeKey, deptKey), contexts.get(0));

        contexts = testContainer.findContextsOf(prod2key);
        assertTrue(contexts.size() == 1);
        assertEquals(ImmutableList.of(prod2key, homeKey, deptKey), contexts.get(0));
    }

    @Test
    public void testParentIndexBuilderWithYShapedContent() {
        // two homes, one product
        final ContentKey dept1key = get(Department, "dept1");
        final ContentKey dept2key = get(Department, "dept2");
        final ContentKey home1key = get(Category, "home1");
        final ContentKey home2key = get(Category, "home2");
        final ContentKey prod1key = get(Product, "prd1");

        final Map<ContentKey, Map<Attribute, Object>> testNodes = new TestContentBuilder()
            .newDepartment(dept1key.id)
            .newCategory(home1key.id)
                .asChild()
            .newDepartment(dept2key.id)
            .newCategory(home2key.id)
                .asChild()
            .newProduct("prd1")
                .connectedTo(home1key)
                .connectedTo(home2key)
            .build();

        final NodeCollectionContentProviderService testContainer =
                new NodeCollectionContentProviderService(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        // expected: [ prod1key => [home1key, home2key], home1key => [dept1key], home2key => [dept2key], dept1key => [], dept2key => [] ]
        Map<ContentKey, Set<ContentKey>> parentIndex = testContainer.buildParentIndexFor(prod1key);
        assertParentMapsEqual(
            ImmutableMap.of(
                prod1key, ImmutableSet.of(home1key, home2key),
                home1key, ImmutableSet.of(dept1key),
                home2key, ImmutableSet.of(dept2key),
                dept1key, NO_PARENTS,
                dept2key, NO_PARENTS),
            parentIndex);

        List<List<ContentKey>> contexts = testContainer.findContextsOf(prod1key);
        assertTrue(contexts.size() == 2);
        assertTrue(contexts.contains(ImmutableList.of(prod1key, home1key, dept1key)));
        assertTrue(contexts.contains(ImmutableList.of(prod1key, home2key, dept2key)));
    }

    @Test
    public void testParentIndexBuilderWithDiamondShapedContent() {
        // two homes, one product
        final ContentKey dept1key = get(Department, "dept1");
        final ContentKey home1key = get(Category, "home1");
        final ContentKey home2key = get(Category, "home2");
        final ContentKey prod1key = get(Product, "prd1");

        final Map<ContentKey, Map<Attribute, Object>> testNodes = new TestContentBuilder()
            .newDepartment(dept1key.id)
            .newCategory(home1key.id)
                .connectedTo(dept1key)
            .newCategory(home2key.id)
                .connectedTo(dept1key)
            .newProduct("prd1")
                .connectedTo(home1key)
                .connectedTo(home2key)
            .build();

        final NodeCollectionContentProviderService testContainer =
                new NodeCollectionContentProviderService(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        // expected: [ prod1key => [home1key, home2key], home1key => [dept1key], home2key => [dept1key], dept1key => []]
        Map<ContentKey, Set<ContentKey>> parentIndex = testContainer.buildParentIndexFor(prod1key);
        assertParentMapsEqual(
            ImmutableMap.of(
                prod1key, ImmutableSet.of(home1key, home2key),
                home1key, ImmutableSet.of(dept1key),
                home2key, ImmutableSet.of(dept1key),
                dept1key, NO_PARENTS),
            parentIndex);

        List<List<ContentKey>> contexts = testContainer.findContextsOf(prod1key);
        assertTrue(contexts.size() == 2);
        assertTrue(contexts.contains(ImmutableList.of(prod1key, home1key, dept1key)));
        assertTrue(contexts.contains(ImmutableList.of(prod1key, home2key, dept1key)));
    }

    @Test
    public void testParentIndexBuilderWithCircularReferencedContent() {
        // TBD
    }

    private void assertParentMapsEqual(Map<ContentKey, Set<ContentKey>> expectedMap, Map<ContentKey, Set<ContentKey>> parentIndex) {
        assertNotNull(parentIndex);
        assertTrue(expectedMap.equals(parentIndex));
    }

    @Configuration
    @ComponentScan(basePackageClasses = { ContentKeyParentsCollectorService.class, ContentTypeInfoService.class, ContextService.class })
    @EnableCaching
    static class Config {
        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("parentKeysCache"), new ConcurrentMapCache("allParentKeysCache")));
            return cacheManager;
        }
    }
}
