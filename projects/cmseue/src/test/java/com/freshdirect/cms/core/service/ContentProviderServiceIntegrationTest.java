package com.freshdirect.cms.core.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.Department;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static com.freshdirect.cms.core.domain.ContentType.Store;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.freshdirect.cms.category.IntegrationTest;
import com.freshdirect.cms.contentio.service.XmlContentProvider;
import com.freshdirect.cms.contentio.xml.FlexContentHandler;
import com.freshdirect.cms.contentio.xml.XmlContentMetadataService;
import com.freshdirect.cms.contentvalidation.service.ValidatorService;
import com.freshdirect.cms.contentvalidation.service.XmlContentValidatorService;
import com.freshdirect.cms.core.converter.ScalarValueToSerializedValueConverter;
import com.freshdirect.cms.core.converter.SerializedScalarValueToObjectConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.ContextualAttributeFetchScope;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;

@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { com.freshdirect.cms.core.service.ContentProviderServiceIntegrationTest.Config.class })
public class ContentProviderServiceIntegrationTest {

    @Autowired
    private ContentProviderService contentProviderService;

    @Test
    public void testCategoryContext() {
        List<List<ContentKey>> contexts = contentProviderService.findContextsOf(get(Category, "cat_2"));

        Assert.assertNotNull("Unexpected null context list", contexts);
        Assert.assertTrue("Categories like cat_2 may have only single context, found " + contexts.size(), contexts.size() == 1);
        Assert.assertTrue("Missing valid context", contexts.contains(Arrays.asList(new ContentKey[] {
                get(Category, "cat_2"), get(Department, "dept1"), get(Store, "FreshDirect") })));
    }

    @Test
    public void testProductWithTwoContexts() {
        List<List<ContentKey>> contexts = contentProviderService.findContextsOf(get(Product, "prd_1"));

        Assert.assertNotNull("Unexpected null context list", contexts);
        Assert.assertTrue("Product prd_1 must have two contexts", contexts.size() == 2);
        Assert.assertTrue("Missing valid context", contexts.contains(Arrays.asList(new ContentKey[] {
                get(Product, "prd_1"), get(Category, "cat_2"), get(Department, "dept1"), get(Store, "FreshDirect") })));
        Assert.assertTrue("Missing orphan context", contexts.contains(Arrays.asList(new ContentKey[] {
                get(Product, "prd_1"), get(Category, "cat_1"), get(Category, "cat_orphan") })));
    }

    @Test
    public void testProductHasParent() {
        final Set<ContentKey> parentsUnderTest = contentProviderService.getParentKeys(get(Product, "prd_1"));
        assertNotNull("Missing parent category", parentsUnderTest);
        assertEquals("Unexpected parents", Sets.newHashSet(get(Category, "cat_1"), get(Category, "cat_2")), parentsUnderTest);
    }

    @Test
    public void testProductWithoutParent() {
        final Set<ContentKey> parentsUnderTest = contentProviderService.getParentKeys(get(Product, "prd_orphan"));
        assertNotNull("Product should be orphan", parentsUnderTest);
        assertTrue("Product should be orphan", parentsUnderTest.isEmpty());
    }

    @Test
    public void testAttributeWithInheritedValue() {
        final Attribute testAttribute = ContentTypes.Product.NOT_SEARCHABLE;
        ContentKey testProductKey = get(Product, "prd_3");
        ContentKey testParentKey = get(Category, "cat_nosearch");
        List<ContentKey> testContext = Arrays.asList(new ContentKey[] {
                get(Product, "prd_3"), get(Category, "cat_nosearch"), get(Department, "dept2"), get(Store, "FreshDirect") });

        assertTrue("Test attribute must be inheritable", testAttribute.getFlags().isInheritable());

        // check available contexts
        List<List<ContentKey>> contextList = contentProviderService.findContextsOf(testProductKey);
        assertTrue("No contexts found", !contextList.isEmpty());
        assertEquals("Unexpected number of contexts", 2, contextList.size());
        assertTrue("Test context not present in context list", contextList.contains(testContext));

        // check fetching inheritable value
        Optional<Object> value = null;

        // value is not set for the specific content key
        value = contentProviderService.getAttributeValue(testProductKey, testAttribute);
        assertTrue("Product " + testProductKey + " shouldn't have value set for attribute " + testAttribute, !value.isPresent());
        // however, value is set for its parent key
        value = contentProviderService.getAttributeValue(testParentKey, testAttribute);
        assertTrue("Parent category " + testParentKey + " should have value set for attribute " + testAttribute, value.isPresent());

        // attribute is inheritable, so it should be returned
        value = contentProviderService.fetchContextualizedAttributeValue(testProductKey, testAttribute, testParentKey, ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES);
        assertTrue("Inherited value must exist", value.isPresent());
    }

    @Test
    public void testAttributeWithInheritedAndDirectValue() {
        final Attribute testAttribute = ContentTypes.Product.REDIRECT_URL;
        ContentKey testProductKey = get(Product, "prd_3");
        ContentKey testParentKey = get(Category, "cat_nosearch");
        List<ContentKey> testContext = Arrays.asList(new ContentKey[] {
                get(Product, "prd_3"), get(Category, "cat_nosearch"), get(Department, "dept2"), get(Store, "FreshDirect") });

        assertTrue("Test attribute must be inheritable", testAttribute.getFlags().isInheritable());

        // check available contexts
        List<List<ContentKey>> contextList = contentProviderService.findContextsOf(testProductKey);
        assertTrue("No contexts found", !contextList.isEmpty());
        assertEquals("Unexpected number of contexts", 2, contextList.size());
        assertTrue("Test context not present in context list", contextList.contains(testContext));

        // check fetching inheritable value
        Optional<Object> directValue = null;
        Optional<Object> value = null;
        Optional<Object> inheritedValue = null;

        // value is not set for the specific content key
        directValue = contentProviderService.getAttributeValue(testProductKey, testAttribute);
        assertTrue("Product " + testProductKey + " must have value set for attribute " + testAttribute, directValue.isPresent());
        // however, value is set for its parent key
        value = contentProviderService.getAttributeValue(testParentKey, testAttribute);
        assertTrue("Parent category " + testParentKey + " must have value set for attribute " + testAttribute, value.isPresent());

        assertNotEquals("Product and category level values must be different", directValue.get(), value.get());

        // check returned value against direct and values set on parent content
        inheritedValue = contentProviderService.fetchContextualizedAttributeValue(testProductKey, testAttribute, testParentKey, ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES);
        assertTrue("Value must exist", inheritedValue.isPresent());
        assertEquals("Value must be the same as direct", inheritedValue.get(), directValue.get());
        assertNotEquals("Value may not be the same as parent value", inheritedValue.get(), value.get());
    }

    @Test
    public void testAllAttributesWithInheritedValue() {
        final Attribute testAttribute = ContentTypes.Product.NOT_SEARCHABLE;
        ContentKey testProductKey = get(Product, "prd_3");
        ContentKey testParentKey = get(Category, "cat_nosearch");
        List<ContentKey> testContext = Arrays.asList(new ContentKey[] {
                get(Product, "prd_3"), get(Category, "cat_nosearch"), get(Department, "dept2"), get(Store, "FreshDirect") });

        assertTrue("Test attribute must be inheritable", testAttribute.getFlags().isInheritable());

        // check available contexts
        List<List<ContentKey>> contextList = contentProviderService.findContextsOf(testProductKey);
        assertTrue("No contexts found", !contextList.isEmpty());
        assertEquals("Unexpected number of contexts", 2, contextList.size());
        assertTrue("Test context not present in context list", contextList.contains(testContext));

        // check fetching inheritable value
        Map<Attribute, Object> values = null;

        // value is not set for the specific content key
        values = contentProviderService.getAllAttributesForContentKey(testProductKey);
        assertNull("Product " + testProductKey + " shouldn't have value set for attribute " + testAttribute, values.get(testAttribute));
        // however, value is set for its parent key
        values = contentProviderService.getAllAttributesForContentKey(testParentKey);
        assertNotNull("Parent category " + testParentKey + " should have value set for attribute " + testAttribute, values.get(testAttribute));

        // attribute is inheritable, so it should be returned
        values = contentProviderService.fetchAllContextualizedAttributesForContentKey(testProductKey, testParentKey, ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES);
        assertNotNull("Inherited value must exist", values.get(testAttribute));
    }

    @Test
    public void testAllAttributesWithInheritedAndDirectValue() {
        final Attribute testAttribute = ContentTypes.Product.REDIRECT_URL;
        ContentKey testProductKey = get(Product, "prd_3");
        ContentKey testParentKey = get(Category, "cat_nosearch");
        List<ContentKey> testContext = Arrays.asList(new ContentKey[] {
                get(Product, "prd_3"), get(Category, "cat_nosearch"), get(Department, "dept2"), get(Store, "FreshDirect") });

        assertTrue("Test attribute must be inheritable", testAttribute.getFlags().isInheritable());

        // check available contexts
        List<List<ContentKey>> contextList = contentProviderService.findContextsOf(testProductKey);
        assertTrue("No contexts found", !contextList.isEmpty());
        assertEquals("Unexpected number of contexts", 2, contextList.size());
        assertTrue("Test context not present in context list", contextList.contains(testContext));

        // check fetching inheritable value
        Optional<Object> directValue = null;
        Optional<Object> parentValues = null;
        Map<Attribute, Object> values = null;

        // value is not set for the specific content key
        directValue = contentProviderService.getAttributeValue(testProductKey, testAttribute);
        assertTrue("Product " + testProductKey + " must have value set for attribute " + testAttribute, directValue.isPresent());
        // however, value is set for its parent key
        parentValues = contentProviderService.getAttributeValue(testParentKey, testAttribute);
        assertTrue("Parent category " + testParentKey + " must have value set for attribute " + testAttribute, parentValues.isPresent());

        assertNotEquals("Product and category level values must be different", directValue.get(), parentValues.get());

        // check returned value against direct and values set on parent content
        values = contentProviderService.fetchAllContextualizedAttributesForContentKey(testProductKey, testParentKey, ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES);
        assertNotNull("Inherited value must exist", values.get(testAttribute));
        assertEquals("Value must be the same as direct", values.get(testAttribute), directValue.get());
        assertNotEquals("Value may not be the same as parent value", values.get(testAttribute), parentValues.get());
    }

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        public static PropertySourcesPlaceholderConfigurer getPropertyResolver() {
            PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
            configurer.setIgnoreUnresolvablePlaceholders(true);

            Properties props = new Properties();

            props.setProperty("cms.resource.basePath", "classpath:/xml");
            props.setProperty("cms.resource.storexml.name", "WalkerStore.xml");

            configurer.setProperties(props);

            return configurer;
        }

        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(
                    new ConcurrentMapCache("parentKeysCache"),
                    new ConcurrentMapCache("allParentKeysCache"),
                    new ConcurrentMapCache("attributeCache")));
            return cacheManager;
        }

        @Bean
        public ValidatorService validatorService() {
            return new XmlContentValidatorService();
        }

        @Bean
        public ContentProvider contentProvider() {
            return new XmlContentProvider();
        }

        @Bean
        public FlexContentHandler flexContentHandler() {
            return new FlexContentHandler();
        }

        @Bean
        public ContentTypeInfoService contentTypeInfoService() {
            return new ContentTypeInfoService();
        }

        @Bean
        public ScalarValueToSerializedValueConverter scalarValueToSerializedValueConverter() {
            return new ScalarValueToSerializedValueConverter();
        }

        @Bean
        public SerializedScalarValueToObjectConverter serializedScalarValueToSerializedValueConverter() {
            return new SerializedScalarValueToObjectConverter();
        }

        @Bean
        public XmlContentMetadataService metaDataService() {
            return new XmlContentMetadataService();
        }

        @Bean
        public ContentKeyParentsCollectorService contentKeyParentsCollectorService() {
            return new ContentKeyParentsCollectorService();
        }

        @Bean
        public ContentProviderService contentProviderService() {
            return new ContentProviderService();
        }

        @Bean
        public ContextService contextService() {
            return new ContextService();
        }
    }
}
