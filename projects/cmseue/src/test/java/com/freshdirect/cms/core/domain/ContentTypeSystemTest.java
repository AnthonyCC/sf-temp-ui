package com.freshdirect.cms.core.domain;

import static com.freshdirect.cms.core.domain.ContentType.CmsQuery;
import static com.freshdirect.cms.core.domain.ContentType.CmsReport;
import static com.freshdirect.cms.core.domain.ContentType.ErpCharacteristic;
import static com.freshdirect.cms.core.domain.ContentType.ErpCharacteristicValue;
import static com.freshdirect.cms.core.domain.ContentType.ErpClass;
import static com.freshdirect.cms.core.domain.ContentType.ErpMaterial;
import static com.freshdirect.cms.core.domain.ContentType.ErpSalesUnit;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.google.common.collect.ImmutableSet;

/**
 * Test CMS Type System through various tests.
 *
 * @author segabor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Category(UnitTest.class)
public class ContentTypeSystemTest {

    // content types known to have no declared attributes in CMS
    private final Set<ContentType> KNOWN_EMPTY_TYPES = ImmutableSet.<ContentType> of(ErpMaterial, ErpClass, ErpCharacteristic, ErpCharacteristicValue, ErpSalesUnit, CmsQuery, CmsReport);

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    /**
     * Review all content types whether they have at least one declared attribute.
     */
    @Test
    public void testTypesHaveAttributes() {

        for (ContentType t : ContentType.values()) {
            final boolean isEmptyType = contentTypeInfoService.selectAttributes(t).isEmpty();
            if (isEmptyType) {
                assertTrue("Content Type " + t.name() + " should have at least one declared attribute", KNOWN_EMPTY_TYPES.contains(t));
            } else {
                assertTrue("Content Type " + t.name() + " is expected to be empty", !KNOWN_EMPTY_TYPES.contains(t));
            }
        }
    }

    @Test
    public void testDistinctAttributeNames() {
        for (ContentType t : ContentType.values()) {
            final Set<Attribute> attributes = contentTypeInfoService.selectAttributes(t);

            Set<String> attributeNames = new HashSet<String>();

            for (Attribute attribute : attributes) {
                final String attributeName = attribute.getName();
                assertTrue("Attribute name '" + attributeName + "' is duplicated in type " + t.name(), !attributeNames.contains(attributeName));

                attributeNames.add(attribute.getName());
            }
        }

    }

    @Test
    public void testAttributeNames() {
        final Pattern namePattern = Pattern.compile("^[a-zA-Z0-9_]*$");

        for (ContentType t : ContentType.values()) {
            final Set<Attribute> attributes = contentTypeInfoService.selectAttributes(t);

            for (Attribute attribute : attributes) {
                final String attributeName = attribute.getName();
                assertTrue("Attribute name '" + attributeName + "' of type " + t.name() + " contains character other than alphabets and underscore!",
                        namePattern.matcher(attributeName).matches());
            }
        }

    }

    @Configuration
    static class ContextConfiguration {

        @Bean
        public ContentTypeInfoService contentTypeInfoService() {
            return new ContentTypeInfoService();
        }
    }
}
