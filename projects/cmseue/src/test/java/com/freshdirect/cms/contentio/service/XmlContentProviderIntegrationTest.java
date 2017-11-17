package com.freshdirect.cms.contentio.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.freshdirect.cms.category.IntegrationTest;
import com.freshdirect.cms.contentio.xml.FlexContentHandler;
import com.freshdirect.cms.contentio.xml.XmlContentMetadataService;
import com.freshdirect.cms.core.converter.SerializedScalarValueToObjectConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.AttributeFlags;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentKeyParentsCollectorService;
import com.freshdirect.cms.core.service.ContentProvider;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.google.common.base.Optional;

@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { XmlContentProviderIntegrationTest.Config.class })
public class XmlContentProviderIntegrationTest {

    @Autowired
    private XmlContentProvider contentProvider;

    @Before
    public void initTest() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        contentProvider.loadAll();
    }

    @Test
    public void testGetAttributeValue() {
        Optional<Object> attributeValue = contentProvider.getAttributeValue(ContentKeyFactory.get(ContentType.Product, "spe_fd_rub_moroccan"), ContentTypes.Product.FULL_NAME);

        Assert.assertTrue(attributeValue.isPresent());
        Assert.assertEquals("FreshDirect Rockin' Moroccan Rub, Jar", attributeValue.get());
    }

    @Test
    public void testGetAttributeValues() {
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute perishable = ContentTypes.Product.PERISHABLE;
        Attribute skus = ContentTypes.Product.skus;

        Map<Attribute, Object> attributeValues = contentProvider.getAttributeValues(ContentKeyFactory.get(ContentType.Product, "spe_fd_rub_moroccan"),
                Arrays.asList(fullName, navName, perishable, skus));

        Assert.assertEquals(4, attributeValues.size());
        Assert.assertEquals("FreshDirect Rockin' Moroccan Rub, Jar", attributeValues.get(fullName));
        Assert.assertEquals(false, attributeValues.get(perishable));
    }

    @Test
    public void testGetAttributeValuesContainingSingleRelationship() {
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute perishable = ContentTypes.Product.PERISHABLE;
        Attribute productImage = ContentTypes.Product.PROD_IMAGE;

        Map<Attribute, Object> attributeValues = contentProvider.getAttributeValues(ContentKeyFactory.get(ContentType.Product, "spe_fd_rub_moroccan"),
                Arrays.asList(fullName, navName, perishable, productImage));

        Assert.assertEquals(4, attributeValues.size());
        Assert.assertEquals("FreshDirect Rockin' Moroccan Rub, Jar", attributeValues.get(fullName));
        Assert.assertEquals(ContentKeyFactory.get(ContentType.Image, "4928790"), attributeValues.get(productImage));
    }

    @Test
    public void testGetAttributeValuesContainingManyRelationship() {
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute perishable = ContentTypes.Product.PERISHABLE;
        Attribute skus = ContentTypes.Product.skus;

        Map<Attribute, Object> attributeValues = contentProvider.getAttributeValues(ContentKeyFactory.get(ContentType.Product, "spe_fd_rub_moroccan"),
                Arrays.asList(fullName, navName, perishable, skus));

        Assert.assertEquals(4, attributeValues.size());
        Assert.assertEquals("FreshDirect Rockin' Moroccan Rub, Jar", attributeValues.get(fullName));
        Assert.assertTrue(List.class.isAssignableFrom(attributeValues.get(skus).getClass()));

        @SuppressWarnings("unchecked")
        List<ContentKey> relationshipDestinations = (List<ContentKey>) attributeValues.get(skus);

        Assert.assertEquals(1, relationshipDestinations.size());
        Assert.assertEquals(ContentKeyFactory.get(ContentType.Sku, "SPE0070672"), relationshipDestinations.get(0));
    }

    @Test
    public void testGetAttributeValuesWithNotExistingAttribute() {
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute perishable = ContentTypes.Product.PERISHABLE;
        Attribute skus = ContentTypes.Product.skus;
        Attribute notExisting = new Scalar("notExisting", AttributeFlags.NONE, true, Boolean.class);

        Map<Attribute, Object> attributeValues = contentProvider.getAttributeValues(ContentKeyFactory.get(ContentType.Product, "spe_fd_rub_moroccan"),
                Arrays.asList(fullName, navName, perishable, skus, notExisting));

        Assert.assertEquals(4, attributeValues.size());
        Assert.assertEquals("FreshDirect Rockin' Moroccan Rub, Jar", attributeValues.get(fullName));
        Assert.assertEquals(false, attributeValues.get(perishable));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllAttributes() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "spe_fd_rub_moroccan");

        Map<Attribute, Object> loadedAttributesWithValues = contentProvider.getAllAttributesForContentKey(contentKey);

        Assert.assertEquals(19, loadedAttributesWithValues.size());
        Assert.assertEquals(1, ((List<ContentKey>) loadedAttributesWithValues.get(ContentTypes.Product.brands)).size());
    }

    @Test
    public void testLoadingContentsHavingOnlyID() {
        ContentKey mediaKey = get(ContentType.Html, "14841697");
        ContentKey skuKey = get(ContentType.Sku, "GRO0069855");

        Assert.assertTrue(contentProvider.getContentKeys().contains(mediaKey));
        Assert.assertTrue(contentProvider.getContentKeys().contains(skuKey));

        Map<Attribute, Object> payload = contentProvider.getAllAttributesForContentKey(mediaKey);
        Assert.assertNotNull(payload);
        Assert.assertTrue(payload.isEmpty());

        payload = contentProvider.getAllAttributesForContentKey(skuKey);
        Assert.assertNotNull(payload);
        Assert.assertTrue(payload.isEmpty());
    }

    @Configuration
    static class Config {

        @Bean
        public ContentProvider contentProvider() {
            return new XmlContentProvider();
        }

        @Bean
        public XmlContentMetadataService xmlContentMetadataService() {
            return new XmlContentMetadataService();
        }

        @Bean
        public ContentKeyParentsCollectorService contentKeyParentsCollectorService() {
            return new ContentKeyParentsCollectorService();
        }

        @Bean
        public DefaultHandler flexContentHandler() {
            return new FlexContentHandler();
        }

        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("parentKeysCache"), new ConcurrentMapCache("allParentKeysCache")));
            return cacheManager;
        }

        @Bean
        public SerializedScalarValueToObjectConverter coverter() {
            return new SerializedScalarValueToObjectConverter();
        }

        @Bean
        public ContentTypeInfoService contentTypeInfoService() {
            return new ContentTypeInfoService();
        }

        @Bean
        public PropertySourcesPlaceholderConfigurer testPropertyPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
            configurer.setIgnoreUnresolvablePlaceholders(true);

            Properties props = new Properties();

            props.setProperty("cms.resource.basePath", "classpath:/xml");
            props.setProperty("cms.resource.storexml.name", "Store_extracted.xml");

            configurer.setProperties(props);

            return configurer;
        }
    }
}
