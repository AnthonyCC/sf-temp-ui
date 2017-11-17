package com.freshdirect.cms.contentio.xml;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Product;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.freshdirect.cms.category.IntegrationTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.service.ContentTypeInfoService;


@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {com.freshdirect.cms.contentio.xml.FlexContentHandlerTest.TestConfig.class})
public class FlexContentHandlerTest {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    @Autowired
    private ApplicationContext applicationContext;

    private FlexContentHandler contentHandler;

    private SAXParser parser;

    @Before
    public void prepareTest() throws ParserConfigurationException, SAXException {
        parser = applicationContext.getBean(SAXParser.class);
        contentHandler = applicationContext.getBean(FlexContentHandler.class);
    }

    @Test
    public void testParsingEmptyRelationshipElement() throws SAXException, IOException {
        final String testContent = XML_HEADER
                + "<Content xmlns:dc=\"http://purl.org/dc/elements/1.1\">\n"
                + "  <Product id=\"prd1\"><FULL_NAME>Test Product</FULL_NAME><skus/></Product>\n"
                + "</Content>";

        final ContentKey key = get(Product, "prd1");
        final Relationship relationship = (Relationship) ContentTypes.Product.skus;

        InputSource source = new InputSource(new StringReader(testContent));
        parser.parse(source, contentHandler);

        Assert.assertTrue("No product key found", contentHandler.getContentNodesKeys().contains(key));
        Map<Attribute, Object> productValues = contentHandler.getContentNodeAttributes(key);
        Assert.assertNotNull("Content node could not be found", productValues);
        Assert.assertTrue("Product has to have exactly two attributes", productValues.size() == 2);
        Assert.assertTrue("FULL_NAME attribute could not be found", productValues.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue("skus relationship could not be found", productValues.containsKey(relationship));
        Assert.assertNull("Relationship value must be 'null'", productValues.get(relationship));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testParsingRelationshipElementWithNullChild() throws SAXException, IOException {
        final String testContent = XML_HEADER
                + "<Content xmlns:dc=\"http://purl.org/dc/elements/1.1\">\n"
                + "  <Product id=\"prd1\"><skus><Null ref=\"null\"/></skus></Product>"
                + "</Content>";

        final ContentKey key = get(Product, "prd1");
        final Relationship relationship = (Relationship) ContentTypes.Product.skus;

        InputSource source = new InputSource(new StringReader(testContent));
        parser.parse(source, contentHandler);

        Assert.assertTrue("No product key found", contentHandler.getContentNodesKeys().contains(key));
        Map<Attribute, Object> productValues = contentHandler.getContentNodeAttributes(key);
        Assert.assertNotNull("Content node could not be found", productValues);
        Assert.assertTrue(productValues.size() == 1);
        Assert.assertTrue("skus relationship could not be found", productValues.containsKey(relationship));

        Assert.assertNotNull("Null value found for 'skus' relationship ", productValues.get(relationship));
        Assert.assertTrue("skus relationship is not empty as expected", ((List<ContentKey>) productValues.get(relationship)).isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testParsingNormalRelationshipElement() throws SAXException, IOException {
        final String testContent = XML_HEADER
                + "<Content xmlns:dc=\"http://purl.org/dc/elements/1.1\">\n"
                + "  <Product id=\"prd1\">\n"
                + "  <FULL_NAME>Test Product</FULL_NAME>\n"
                + "  <skus>\n"
                + "    <Sku ref=\"sku1\"/><Sku ref=\"sku2\"/>\n"
                + "  </skus>\n"
                + "  </Product>\n"
                + "</Content>";

        final ContentKey key = get(Product, "prd1");
        final Relationship relationship = (Relationship) ContentTypes.Product.skus;

        InputSource source = new InputSource(new StringReader(testContent));
        parser.parse(source, contentHandler);

        Assert.assertTrue("No product key found", contentHandler.getContentNodesKeys().contains(key));
        Map<Attribute, Object> productValues = contentHandler.getContentNodeAttributes(key);
        Assert.assertNotNull("Content node could not be found", productValues);
        Assert.assertTrue("Product has to have exactly two attributes", productValues.size() == 2);
        Assert.assertTrue("FULL_NAME attribute could not be found", productValues.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue("skus relationship could not be found", productValues.containsKey(relationship));
        Assert.assertNotNull("Relationship value must not be null", productValues.get(relationship));
        Assert.assertTrue("Bad relationship value type", productValues.get(relationship) instanceof List);

        List<ContentKey> skuKeys = (List<ContentKey>) productValues.get(relationship);
        Assert.assertTrue("Value should not be empty", !skuKeys.isEmpty());
        Assert.assertTrue("Relationship does not contain SKU with ID sku1", skuKeys.contains(get(ContentType.Sku, "sku1")));
        Assert.assertTrue("Relationship does not contain SKU with ID sku2", skuKeys.contains(get(ContentType.Sku, "sku2")));
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ContentTypeInfoService typeInfoService() {
            return new ContentTypeInfoService();
        }

        @Bean
        @Scope(BeanDefinition.SCOPE_PROTOTYPE)
        public FlexContentHandler handler() {
            return new FlexContentHandler();
        }

        @Bean
        @Scope(BeanDefinition.SCOPE_PROTOTYPE)
        public SAXParser parser() throws ParserConfigurationException, SAXException {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser saxParser = factory.newSAXParser();
            return saxParser;
        }
    }
}
