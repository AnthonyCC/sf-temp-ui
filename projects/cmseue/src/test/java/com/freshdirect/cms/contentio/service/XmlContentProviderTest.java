package com.freshdirect.cms.contentio.service;

import java.util.ArrayList;
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
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
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
import com.freshdirect.cms.util.EntityFactory;
import com.google.common.base.Optional;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class XmlContentProviderTest {

    private static final String MOCKED_CATEGORY_ALT_TEXT = "Category alt text";

    private static final String MOCKED_CATEGORY_FULL_NAME = "Category full name";

    private static final String MOCKED_CATEGORY_ID = "test_cat";

    private static final String MOCKED_PRODUCT_AKA = "test aka";

    private static final String MOCKED_PRODUCT_FULL_NAME = "mocked product full name";

    private static final String MOCKED_PRODUCT_ID = "test_prod";

    @InjectMocks
    private XmlContentProvider underTest;

    @Mock
    private FlexContentHandler flexContentHandler;

    @Mock
    private SerializedScalarValueToObjectConverter serializedScalarValueToObjectConverter;

    @Mock
    private XmlContentMetadataService xmlContentMetadataService;

    @Mock
    private ContentKeyParentsCollectorService contentKeyParentsCollectorService;

    @Before
    public void beforeTests() {
        Map<ContentKey, Map<Attribute, Object>> nodes = new HashMap<ContentKey, Map<Attribute, Object>>();

        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Product.FULL_NAME, MOCKED_PRODUCT_FULL_NAME);
        attributes.put(ContentTypes.Product.AKA, MOCKED_PRODUCT_AKA);
        attributes.put(ContentTypes.Product.PRIMARY_HOME, ContentKeyFactory.get(ContentType.Category, "ph_cat"));
        attributes.put(ContentTypes.Product.brands, Arrays.asList(ContentKeyFactory.get(ContentType.Brand, "brand1"), ContentKeyFactory.get(ContentType.Brand, "brand2")));

        Map<Attribute, Object> categoryAttributes = new HashMap<Attribute, Object>();
        categoryAttributes.put(ContentTypes.Category.FULL_NAME, MOCKED_CATEGORY_FULL_NAME);
        categoryAttributes.put(ContentTypes.Category.ALT_TEXT, MOCKED_CATEGORY_ALT_TEXT);

        nodes.put(ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID), attributes);
        nodes.put(ContentKeyFactory.get(ContentType.Category, MOCKED_CATEGORY_ID), categoryAttributes);

        Mockito.when(flexContentHandler.getContentNodes()).thenReturn(nodes);
        Mockito.when(serializedScalarValueToObjectConverter.convert(ContentTypes.Product.FULL_NAME, MOCKED_PRODUCT_FULL_NAME)).thenReturn(MOCKED_PRODUCT_FULL_NAME);
        Mockito.when(serializedScalarValueToObjectConverter.convert(ContentTypes.Product.AKA, MOCKED_PRODUCT_AKA)).thenReturn(MOCKED_PRODUCT_AKA);
        Mockito.when(serializedScalarValueToObjectConverter.convert(ContentTypes.Category.FULL_NAME, MOCKED_CATEGORY_FULL_NAME)).thenReturn(MOCKED_CATEGORY_FULL_NAME);
        Mockito.when(serializedScalarValueToObjectConverter.convert(ContentTypes.Category.ALT_TEXT, MOCKED_CATEGORY_ALT_TEXT)).thenReturn(MOCKED_CATEGORY_ALT_TEXT);

        underTest.buildAll(flexContentHandler);
    }

    @Test
    public void testGetContentKeys() {
        Set<ContentKey> loadedContentKeys = underTest.getContentKeys();

        Assert.assertNotNull(loadedContentKeys);
        Assert.assertEquals(2, loadedContentKeys.size());
        Assert.assertTrue(loadedContentKeys.contains(ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContenKeysByTypeWithNullType() {
        underTest.getContentKeysByType(null);
    }

    @Test
    public void testGetContentKeysByType() {

        Set<ContentKey> loadedContentKeys = underTest.getContentKeysByType(ContentType.Product);

        Assert.assertNotNull(loadedContentKeys);
        Assert.assertEquals(1, loadedContentKeys.size());
        Assert.assertEquals(ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID), loadedContentKeys.iterator().next());
    }

    @Test
    public void testGetAttributeValue() {

        Optional<Object> loadedValue = underTest.getAttributeValue(ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID), ContentTypes.Product.FULL_NAME);

        Assert.assertTrue(loadedValue.isPresent());
        Assert.assertEquals(MOCKED_PRODUCT_FULL_NAME, loadedValue.get());
    }

    @Test
    public void testGetAttributeValueWithNotExistingAttribute() {

        Optional<Object> loadedValue = underTest.getAttributeValue(EntityFactory.createContentKey(), EntityFactory.createScalarAttribute());

        Assert.assertFalse(loadedValue.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeValueWithNullContentKey() {
        underTest.getAttributeValue(null, EntityFactory.createScalarAttribute());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeValueWithNullName() {
        underTest.getAttributeValue(EntityFactory.createContentKey(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeValuesWithNullContentKey() {
        underTest.getAttributeValues(null, Arrays.asList(EntityFactory.createScalarAttribute()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeValuesWithNullAttributes() {
        underTest.getAttributeValues(EntityFactory.createContentKey(), null);
    }

    @Test
    public void testGetAttributeValuesWithEmptyAttributesList() {
        Map<com.freshdirect.cms.core.domain.Attribute, Object> expected = new HashMap<com.freshdirect.cms.core.domain.Attribute, Object>();
        Map<com.freshdirect.cms.core.domain.Attribute, Object> loaded = underTest.getAttributeValues(EntityFactory.createContentKey(),
                new ArrayList<com.freshdirect.cms.core.domain.Attribute>());

        Assert.assertEquals(expected, loaded);
        Assert.assertEquals(loaded.values().size(), 0);
        Assert.assertEquals(loaded.keySet().size(), 0);
    }

    @Test
    public void testGetAttributeValues() {

        Map<Attribute, Object> loaded = underTest.getAttributeValues(ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID), Arrays.asList(ContentTypes.Product.FULL_NAME));

        Assert.assertEquals(1, loaded.size());
        Assert.assertTrue(loaded.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(MOCKED_PRODUCT_FULL_NAME, loaded.get(ContentTypes.Product.FULL_NAME));
    }

    @Test
    public void testGetAttributesContainingOneNotExistingAttribute() {

        Scalar notExistingAttribute = new Scalar("notExisting", AttributeFlags.NONE, true, String.class);
        Map<Attribute, Object> loaded = underTest.getAttributeValues(ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID),
                Arrays.asList(ContentTypes.Product.FULL_NAME, notExistingAttribute));

        Assert.assertEquals(1, loaded.size());
        Assert.assertTrue(loaded.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertFalse(loaded.containsKey(notExistingAttribute));
        Assert.assertEquals(MOCKED_PRODUCT_FULL_NAME, loaded.get(ContentTypes.Product.FULL_NAME));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveAttributeWithNullContentKey() {
        underTest.saveAttribute(null, EntityFactory.createScalarAttribute(), "test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveAttributeWithNullAttribute() {
        underTest.saveAttribute(EntityFactory.createContentKey(), null, "test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveAttributeWithNullAttributeValue() {
        underTest.saveAttribute(EntityFactory.createContentKey(), EntityFactory.createScalarAttribute(), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveAttributesWithNullAttributesWithValues() {
        underTest.saveAttributes(EntityFactory.createContentKey(), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveAttributesWithNullContentKey() {
        underTest.saveAttributes(null, new HashMap<Attribute, Object>());
    }

    @Test
    public void testGetAttributesForContentKeys() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID);
        Map<ContentKey, Map<Attribute, Object>> loaded = underTest.getAttributesForContentKeys(Arrays.asList(contentKey),
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.AKA));

        Assert.assertNotNull(loaded);
        Assert.assertEquals(1, loaded.size());
        Assert.assertTrue(loaded.containsKey(contentKey));
        Assert.assertEquals(2, loaded.get(contentKey).size());
        Assert.assertTrue(loaded.get(contentKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(loaded.get(contentKey).containsKey(ContentTypes.Product.AKA));
        Assert.assertEquals(MOCKED_PRODUCT_FULL_NAME, loaded.get(contentKey).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(MOCKED_PRODUCT_AKA, loaded.get(contentKey).get(ContentTypes.Product.AKA));
    }

    @Test
    public void testGetAttributesForContentKeysWithMixedContentTypes() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID);
        ContentKey categoryKey = ContentKeyFactory.get(ContentType.Category, MOCKED_CATEGORY_ID);
        Map<ContentKey, Map<Attribute, Object>> loaded = underTest.getAttributesForContentKeys(Arrays.asList(contentKey, categoryKey),
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.AKA, ContentTypes.Category.ALT_TEXT));

        Assert.assertNotNull(loaded);
        Assert.assertEquals(2, loaded.size());
        Assert.assertTrue(loaded.containsKey(contentKey));
        Assert.assertTrue(loaded.containsKey(categoryKey));
        Assert.assertEquals(2, loaded.get(contentKey).size());
        Assert.assertEquals(2, loaded.get(categoryKey).size());
        Assert.assertTrue(loaded.get(contentKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(loaded.get(contentKey).containsKey(ContentTypes.Product.AKA));
        Assert.assertTrue(loaded.get(categoryKey).containsKey(ContentTypes.Category.ALT_TEXT));
        Assert.assertEquals(MOCKED_PRODUCT_FULL_NAME, loaded.get(contentKey).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(MOCKED_PRODUCT_AKA, loaded.get(contentKey).get(ContentTypes.Product.AKA));
        Assert.assertEquals(MOCKED_CATEGORY_ALT_TEXT, loaded.get(categoryKey).get(ContentTypes.Category.ALT_TEXT));
    }

    @Test
    public void testGetAttributesForContentKeysWithNotExistingContentKey() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID);
        ContentKey categoryKey = ContentKeyFactory.get(ContentType.Category, "notExisting");
        Map<ContentKey, Map<Attribute, Object>> loaded = underTest.getAttributesForContentKeys(Arrays.asList(contentKey, categoryKey),
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.AKA, ContentTypes.Category.ALT_TEXT));

        Assert.assertNotNull(loaded);
        Assert.assertEquals(1, loaded.size());
        Assert.assertTrue(loaded.containsKey(contentKey));
        Assert.assertFalse(loaded.containsKey(categoryKey));
        Assert.assertEquals(2, loaded.get(contentKey).size());
        Assert.assertTrue(loaded.get(contentKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(loaded.get(contentKey).containsKey(ContentTypes.Product.AKA));

        Assert.assertEquals(MOCKED_PRODUCT_FULL_NAME, loaded.get(contentKey).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(MOCKED_PRODUCT_AKA, loaded.get(contentKey).get(ContentTypes.Product.AKA));
    }

    @Test
    public void testGetAttributesForContentKeysWithNotExistingAttribute() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID);
        ContentKey categoryKey = ContentKeyFactory.get(ContentType.Category, MOCKED_CATEGORY_ID);
        Map<ContentKey, Map<Attribute, Object>> loaded = underTest.getAttributesForContentKeys(Arrays.asList(contentKey, categoryKey),
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.ALTERNATE_IMAGE, ContentTypes.Category.ALT_TEXT));

        Assert.assertNotNull(loaded);
        Assert.assertEquals(2, loaded.size());
        Assert.assertTrue(loaded.containsKey(contentKey));
        Assert.assertTrue(loaded.containsKey(categoryKey));
        Assert.assertEquals(1, loaded.get(contentKey).size());
        Assert.assertEquals(2, loaded.get(categoryKey).size());
        Assert.assertTrue(loaded.get(contentKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertFalse(loaded.get(contentKey).containsKey(ContentTypes.Product.ALTERNATE_IMAGE));
        Assert.assertTrue(loaded.get(categoryKey).containsKey(ContentTypes.Category.ALT_TEXT));
        Assert.assertEquals(MOCKED_PRODUCT_FULL_NAME, loaded.get(contentKey).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(MOCKED_CATEGORY_ALT_TEXT, loaded.get(categoryKey).get(ContentTypes.Category.ALT_TEXT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributesForContentKeysWithNullContentKeys() {
        underTest.getAttributesForContentKeys(null, Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.AKA, ContentTypes.Category.ALT_TEXT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributesForContentKeysWithNullAttributes() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, MOCKED_PRODUCT_ID);
        ContentKey categoryKey = ContentKeyFactory.get(ContentType.Category, MOCKED_CATEGORY_ID);
        underTest.getAttributesForContentKeys(Arrays.asList(contentKey, categoryKey), null);
    }
}
