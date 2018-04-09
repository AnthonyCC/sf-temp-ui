package com.freshdirect.cms.contentio.xml;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;
import static com.freshdirect.cms.core.domain.ContentType.Department;
import static com.freshdirect.cms.core.domain.ContentType.Product;
import static com.freshdirect.cms.core.domain.ContentType.Sku;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.freshdirect.cms.category.IntegrationTest;
import com.freshdirect.cms.contentio.xml.ContentToXmlDocumentConverterTest.TestConfig;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.service.ContentTypeInfoService;

@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {TestConfig.class})
public class ContentToXmlDocumentConverterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentToXmlDocumentConverterTest.class);

    @Autowired
    private ContentToXmlDocumentConverter converter;

    /**
     * Null-valued relationships should be excluded from XML output
     * Input: Product.skus := null
     * Expected XML output:
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <Content>
     *   ...
     *   <Product id="prd1">
     *     <FULL_NAME>Test Product</FULL_NAME>
     *     <!-- <skus> excluded -->
     *   </Product>
     *   ...
     * </Content>
     *
     * @throws IOException
     */
    @Test
    public void testConvertNullRelationshipValueToXMLEntity() throws IOException {

        final ContentKey key = get(Product, "prd1");
        final Relationship relationship = (Relationship) ContentTypes.Product.skus;

        Map<ContentKey, Map<Attribute, Object>> testValues = prepareTestData(key, relationship, null);
        testValues.get(key).put(ContentTypes.Product.FULL_NAME, "Test Product");

        Document doc = converter.convert(testValues);

        //debugXMLDoc(doc);

        assertTrue(key.type + " not found", 1 == doc.selectNodes("//Content/" + key.type + "[@id='" + key.id + "']").size());
        assertTrue("<" + relationship.getName() + "> tag should not present under " + key.type + " tag",
                0 == doc.selectNodes("//Content/" + key.type + "[@id='" + key.id + "']/" + relationship.getName()).size());
    }

    /**
     * Non-empty relationship values should be converted to the basic way
     *
     * Input: Product.skus := {"Sku:sku1"}
     * Expected XML output:
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <Content>
     *   ...
     *   <Product id="prd1">
     *     <skus>
     *       <Sku ref="sku1"/>
     *     </skus>
     *   </Product>
     *   ...
     * </Content>
     *
     * @throws IOException
     */
    @Test
    public void testConvertNonEmptyRelationshipValueToXMLEntity() throws IOException {

        final ContentKey key = get(Product, "prd1");
        final Relationship relationship = (Relationship) ContentTypes.Product.skus;
        final ContentKey skuKey = get(Sku, "sku1");

        Map<ContentKey, Map<Attribute, Object>> testValues = prepareTestData(key, relationship, Arrays.asList(new ContentKey[]{skuKey}));
        Document doc = converter.convert(testValues);

        //debugXMLDoc(doc);

        assertTrue(key.type + " not found", 1 == doc.selectNodes("//Content/" + key.type + "[@id='" + key.id + "']").size());
        assertTrue("Only one child sku is expected under <skus> tag",
                1 == doc.selectNodes("//Content/" + key.type + "[@id='" + key.id + "']/" + relationship.getName() + "/" + ContentType.Sku).size());
        assertTrue(skuKey.type + " with '" + skuKey.id + "' reference not found",
                1 == doc.selectNodes("//Content/" + key.type + "[@id='" + key.id + "']/" + relationship.getName() + "/" + skuKey.type + "[@ref='" + skuKey.id + "']").size());
    }

    /**
     * Empty relationship values are treated special way
     * Input: Department.productTags = {}
     * Expected XML output:
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <Content>
     *   ...
     *   <Department id="dept">
     *     <productTags>
     *       <Null ref="null"/>
     *     </productTags>
     *   </Department>
     *   ...
     * </Content>
     *
     * @throws IOException
     */
    @Test
    public void testConvertEmptyRelationshipValueToXMLEntity() throws IOException {

        final ContentKey key = get(Department, "dept");
        final Relationship relationship = (Relationship) ContentTypes.Department.productTags;

        Map<ContentKey, Map<Attribute, Object>> testValues = prepareTestData(key, relationship, Collections.emptyList());

        Document doc = converter.convert(testValues);

        //debugXMLDoc(doc);

        assertTrue(key.type + " not found", 1 == doc.selectNodes("//Content/" + key.type + "[@id='" + key.id + "']").size());
        assertTrue("No " + ContentType.Tag + " is expected under <" + relationship.getName() + "> tag",
                0 == doc.selectNodes("//Content/" + key.type + "[@id='" + key.id + "']/" + relationship.getName() + "/" + ContentType.Tag).size());
        assertTrue("Special 'Null' tag is expected under <" + relationship.getName() + "> tag",
                1 == doc.selectNodes("//Content/" + key.type + "[@id='" + key.id + "']/" + relationship.getName() + "/Null").size());
    }

    @SuppressWarnings("unused")
    private void debugXMLDoc(Document doc) {
        Writer writer = null;
        try {
            writer = new StringWriter();
            doc.write(writer);
            LOGGER.info(writer.toString());
        } catch (IOException e) {
            LOGGER.error("failure", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private Map<ContentKey, Map<Attribute, Object>> prepareTestData(ContentKey contentKey, Attribute relationship, Object relationshipValue) {
        Map<Attribute, Object> testPayload =  new HashMap<Attribute, Object>();
        testPayload.put(relationship, relationshipValue);

        Map<ContentKey, Map<Attribute, Object>> testValues = new HashMap<ContentKey, Map<Attribute, Object>>();
        testValues.put(contentKey, testPayload);

        return testValues;
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ContentToXmlDocumentConverter converter() {
            return new ContentToXmlDocumentConverter();
        }

        @Bean
        public ContentTypeInfoService typeInfoService() {
            return new ContentTypeInfoService();
        }
    }
}
