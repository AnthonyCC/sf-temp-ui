package com.freshdirect.cms.core.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.Department;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.util.TestContentBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@RunWith(SpringJUnit4ClassRunner.class)
@Category(UnitTest.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { com.freshdirect.cms.core.service.NodeCollectionContentProviderServiceTest.Config.class })
public class NodeCollectionContentProviderServiceTest {

    private final Set<ContentKey> NO_PARENTS = Collections.<ContentKey>emptySet();

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

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private ContentKeyParentsCollectorService contentKeyParentsCollectorService;

    @Test
    public void testParentKeys() {

        Map<ContentKey, Map<Attribute, Object>> testNodes = new HashMap<ContentKey, Map<Attribute, Object>>();

        // build FreshDirect content tree
        // FD > Department:mkt > Category:mkt_high_holidays_2014 > Category:picks_high_holidays_new_yea > Product:fru_pid_2210270
        testNodes.put(RootContentKey.STORE_FRESHDIRECT.contentKey,
                ImmutableMap.<Attribute, Object> of(ContentTypes.Store.departments, asList(new ContentKey[] { get(Department, "mkt") }), ContentTypes.Product.NOT_SEARCHABLE, true));
        testNodes.put(get(Department, "mkt"),
                ImmutableMap.<Attribute, Object> of(ContentTypes.Department.categories, asList(new ContentKey[] { get(Category, "mkt_high_holidays_2014") })));
        testNodes.put(get(Category, "mkt_high_holidays_2014"),
                ImmutableMap.<Attribute, Object> of(ContentTypes.Category.subcategories, asList(new ContentKey[] { get(Category, "picks_high_holidays_new_yea") })));
        testNodes.put(get(Category, "picks_high_holidays_new_yea"),
                ImmutableMap.<Attribute, Object> of(ContentTypes.Category.products, asList(new ContentKey[] { get(Product, "fru_pid_2210270") })));

        // build orphan category
        // Category:apl > Category:apl_apl > Product:fru_pid_2210270
        testNodes.put(get(Category, "apl"),
                ImmutableMap.<Attribute, Object> of(ContentTypes.Category.subcategories, asList(new ContentKey[] { get(Category, "apl_apl") })));
        testNodes.put(get(Category, "apl_apl"),
                ImmutableMap.<Attribute, Object> of(ContentTypes.Category.products, asList(new ContentKey[] { get(Product, "fru_pid_2210270") })));

        testNodes.put(get(Product, "fru_pid_2210270"),
                ImmutableMap.<Attribute, Object> of(ContentTypes.Product.FULL_NAME, "Test Product", ContentTypes.Product.PRIMARY_HOME, asList(new ContentKey[] { get(Category, "fvg_fruit_apples"), get(Category, "apl_apl") })));

        // build FDX tree
        // FDX > Department:mkt_fdx > Category:fvg_fruit_apples > Product:fru_pid_2210270
        testNodes.put(RootContentKey.STORE_FOODKICK.contentKey,
                ImmutableMap.<Attribute, Object> of(ContentTypes.Store.departments, asList(new ContentKey[] { get(Department, "mkt_fdx") }), ContentTypes.Product.NOT_SEARCHABLE, true));
        testNodes.put(get(Department, "mkt_fdx"),
                ImmutableMap.<Attribute, Object> of(ContentTypes.Department.categories, asList(new ContentKey[] { get(Category, "fvg_fruit_apples") })));
        testNodes.put(get(Category, "fvg_fruit_apples"),
                ImmutableMap.<Attribute, Object> of(ContentTypes.Category.products, asList(new ContentKey[] { get(Product, "fru_pid_2210270") })));


        NodeCollectionContentProviderService underTest = new NodeCollectionContentProviderService(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        final Set<ContentKey> parentKeys = underTest.getParentKeys(get(Product, "fru_pid_2210270"));
        assertNotNull(parentKeys);
        assertTrue(parentKeys.size()==3);
        assertTrue(parentKeys.contains(get(Category, "picks_high_holidays_new_yea")));
        assertTrue(parentKeys.contains(get(Category, "apl_apl")));
        assertTrue(parentKeys.contains(get(Category, "fvg_fruit_apples")));

        Map<ContentKey, ContentKey> result = underTest.findPrimaryHomes(get(Product, "fru_pid_2210270"));

        System.out.println("findPrimaryHomes: " + result);

        // TBD
        // Map<ContentKey, Map<Attribute, Object>> filtered = underTest.filterNodesToStore(RootContentKey.STORE_FRESHDIRECT.contentKey);
    }

    @Test
    public void testParentIndexBuilderWithEmptyContent()         {
        TestContainer emptyContainer = new TestContainer(contentTypeInfoService, contentKeyParentsCollectorService, contextService, Collections.<ContentKey, Map<Attribute, Object>>emptyMap());

        final ContentKey testKey = get(Category, "testCat");
        final Map<ContentKey, Set<ContentKey>> parentIndex = emptyContainer.buildParentIndexFor(testKey);
        assertNotNull(parentIndex);
        assertTrue(parentIndex.size() == 1);
        assertTrue(parentIndex.keySet().contains(testKey));
        assertTrue(emptyContainer.findContextsOf(testKey).isEmpty());
    }

    @Test
    public void testParentIndexBuilderWithSingleNodeContent() {
        final ContentKey singleKey = get(Department, "singleNode");
        Map<ContentKey, Map<Attribute, Object>> testNodes = new TestContentBuilder()
            .newDepartment(singleKey.id)
            .build();

        TestContainer singleNodeContainer = new TestContainer(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        final Map<ContentKey, Set<ContentKey>> parentIndex = singleNodeContainer.buildParentIndexFor(singleKey);
        assertParentMapsEqual(ImmutableMap.of(singleKey, NO_PARENTS), parentIndex);

        assertTrue(singleNodeContainer.findContextsOf(singleKey).isEmpty());
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

        TestContainer testContainer = new TestContainer(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        // expected: [ fooChildKey => [fooKey], fooKey => [] ]
        Map<ContentKey, Set<ContentKey>> parentIndex = testContainer.buildParentIndexFor(fooChildKey);
        assertParentMapsEqual(
            ImmutableMap.of(
                fooChildKey, ImmutableSet.of(fooKey),
                fooKey, NO_PARENTS),
            parentIndex);

        // expected: [[ fooChildKey, fooKey ]]
        List<List<ContentKey>> fooContexts = testContainer.findContextsOf(fooChildKey);
        assertTrue(fooContexts.size()==1);
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
        assertTrue(fooContexts.size()==1);
        assertEquals(Arrays.asList(fooGrandChildKey, fooChildKey, fooKey), fooContexts.get(0));

        // expected: [[ fooChildKey, fooKey ]]
        fooContexts = testContainer.findContextsOf(fooChildKey);
        assertTrue(fooContexts.size()==1);
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

        TestContainer testContainer = new TestContainer(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

        // expected: [ fooChildKey => [fooKey], fooKey => [] ]
        Map<ContentKey, Set<ContentKey>> parentIndex = testContainer.buildParentIndexFor(fooChildKey);
        assertParentMapsEqual(
            ImmutableMap.of(
                fooChildKey, ImmutableSet.of(fooKey),
                fooKey, NO_PARENTS),
            parentIndex);

        // expected: [[ fooChildKey, fooKey ]]
        final List<List<ContentKey>> fooContexts = testContainer.findContextsOf(fooChildKey);
        assertTrue(fooContexts.size()==1);
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
        assertTrue(barContexts.size()==1);
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

        final TestContainer testContainer = new TestContainer(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

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
        assertTrue(contexts.size()==1);
        assertEquals(ImmutableList.of(prod1key, homeKey, deptKey), contexts.get(0));

        contexts = testContainer.findContextsOf(prod2key);
        assertTrue(contexts.size()==1);
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

        final TestContainer testContainer = new TestContainer(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

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
        assertTrue(contexts.size()==2);
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

        final TestContainer testContainer = new TestContainer(contentTypeInfoService, contentKeyParentsCollectorService, contextService, testNodes);

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
        assertTrue(contexts.size()==2);
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

    private static class TestContainer extends NodeCollectionContentProviderService {
        public TestContainer(ContentTypeInfoService contentTypeInfoService, ContentKeyParentsCollectorService contentKeyParentsCollectorService,
                ContextService contextService, Map<ContentKey, Map<Attribute, Object>> contentNodes) {
            super(contentTypeInfoService, contentKeyParentsCollectorService, contextService, contentNodes);
        }

        @Override
        public Map<ContentKey, Set<ContentKey>> buildParentIndexFor(final ContentKey contentKey) {
            return super.buildParentIndexFor(contentKey);
        }
    }
}
