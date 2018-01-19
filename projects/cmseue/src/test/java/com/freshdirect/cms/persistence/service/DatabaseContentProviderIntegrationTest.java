package com.freshdirect.cms.persistence.service;

import static com.freshdirect.cms.core.domain.ContentKeyFactory.get;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.freshdirect.cms.DatabaseTestConfiguration;
import com.freshdirect.cms.category.IntegrationTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.AttributeFlags;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.util.EntityFactory;
import com.google.common.base.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseTestConfiguration.class })
@Category(IntegrationTest.class)
public class DatabaseContentProviderIntegrationTest {

    @Autowired
    private DatabaseContentProvider databaseContentProvider;

    @Before
    public void setUp() {
        databaseContentProvider.loadAll();
    }
    
    @Test
    public void testGetAttributeValue() {
        Optional<Object> attributeValue = databaseContentProvider.getAttributeValue(EntityFactory.createContentKey(), EntityFactory.createScalarAttribute());

        Assert.assertTrue(attributeValue.isPresent());
        Assert.assertEquals("Test full name", attributeValue.get());
    }

    @Test
    public void testGetAttributeValues() {
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute showTopTenImage = ContentTypes.Product.SHOW_TOP_TEN_IMAGE;
        Attribute keywords = ContentTypes.Product.KEYWORDS;

        Map<Attribute, Object> attributeValues = databaseContentProvider.getAttributeValues(EntityFactory.createContentKey(),
                Arrays.asList(fullName, navName, showTopTenImage, keywords));

        Assert.assertEquals(4, attributeValues.size());
        Assert.assertEquals("Test full name", attributeValues.get(fullName));
        Assert.assertEquals(true, Boolean.parseBoolean(attributeValues.get(showTopTenImage).toString()));
    }

    @Test
    public void testGetAttributeValuesContainingSingleRelationship() {
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute showTopTenImage = ContentTypes.Product.SHOW_TOP_TEN_IMAGE;
        Attribute keywords = ContentTypes.Product.KEYWORDS;
        Attribute preferredSku = ContentTypes.Product.PREFERRED_SKU;

        Map<Attribute, Object> attributeValues = databaseContentProvider.getAttributeValues(EntityFactory.createContentKey(),
                Arrays.asList(fullName, navName, showTopTenImage, keywords, preferredSku));

        Assert.assertEquals(5, attributeValues.size());
        Assert.assertEquals("Test full name", attributeValues.get(fullName));
        Assert.assertEquals(true, attributeValues.get(showTopTenImage));
        Assert.assertEquals(ContentKeyFactory.get(ContentType.Sku, "testdest"), attributeValues.get(preferredSku));
    }

    @Test
    public void testGetAttributeValuesContainingManyRelationship() {
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute showTopTenImage = ContentTypes.Product.SHOW_TOP_TEN_IMAGE;
        Attribute keywords = ContentTypes.Product.KEYWORDS;
        Attribute variationMatrix = ContentTypes.Product.VARIATION_MATRIX;

        Map<Attribute, Object> attributeValues = databaseContentProvider.getAttributeValues(EntityFactory.createContentKey(),
                Arrays.asList(fullName, navName, showTopTenImage, keywords, variationMatrix));

        Assert.assertEquals(5, attributeValues.size());
        Assert.assertEquals("Test full name", attributeValues.get(fullName));
        Assert.assertEquals(true, attributeValues.get(showTopTenImage));
        Assert.assertTrue(List.class.isAssignableFrom(attributeValues.get(variationMatrix).getClass()));

        @SuppressWarnings("unchecked")
        List<ContentKey> relationshipDestinations = (List<ContentKey>) attributeValues.get(variationMatrix);

        Assert.assertEquals(4, relationshipDestinations.size());
        Assert.assertEquals(ContentKeyFactory.get(ContentType.Domain, "product1"), relationshipDestinations.get(0));
        Assert.assertEquals(ContentKeyFactory.get(ContentType.Domain, "product4"), relationshipDestinations.get(1));
        Assert.assertEquals(ContentKeyFactory.get(ContentType.Domain, "product3"), relationshipDestinations.get(2));
        Assert.assertEquals(ContentKeyFactory.get(ContentType.Domain, "product2"), relationshipDestinations.get(3));
    }

    @Test
    public void testGetAttributeValuesWithNotExistingAttribute() {
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute showTopTenImage = ContentTypes.Product.SHOW_TOP_TEN_IMAGE;
        Attribute keywords = ContentTypes.Product.KEYWORDS;
        Attribute notExisting = new Scalar("notExisting", AttributeFlags.NONE, true, Boolean.class);

        Map<Attribute, Object> attributeValues = databaseContentProvider.getAttributeValues(EntityFactory.createContentKey(),
                Arrays.asList(fullName, navName, showTopTenImage, keywords, notExisting));

        Assert.assertEquals(4, attributeValues.size());
        Assert.assertEquals("Test full name", attributeValues.get(fullName));
        Assert.assertEquals(true, attributeValues.get(showTopTenImage));
    }

    @Test
    public void testSaveAttributeScalar() {
        Attribute fullName = ContentTypes.Category.FULL_NAME;
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Category, "test");

        String attributeValue = "Category full name";
        databaseContentProvider.saveAttribute(contentKey, fullName, attributeValue);

        Set<ContentKey> categories = databaseContentProvider.getContentKeysByType(ContentType.Category);
        Optional<Object> fullNameValue = databaseContentProvider.getAttributeValue(contentKey, fullName);

        Assert.assertEquals(2, categories.size());
        Assert.assertTrue(fullNameValue.isPresent());
        Assert.assertEquals(attributeValue, fullNameValue.get());
    }

    @Test
    public void testSaveAttributeRelationshipCardinalityOne() {
        Attribute product = ContentTypes.Product.PREFERRED_SKU;
        ContentKey relationshipSource = ContentKeyFactory.get(ContentType.Product, "productParent");
        ContentKey relationshipValue = ContentKeyFactory.get(ContentType.Sku, "testProduct");

        databaseContentProvider.saveAttribute(relationshipSource, product, relationshipValue);
        Optional<Object> loadedRelationshipValue = databaseContentProvider.getAttributeValue(relationshipSource, product);

        Assert.assertTrue(loadedRelationshipValue.isPresent());
        Assert.assertEquals(relationshipValue, loadedRelationshipValue.get());
    }

    @Test
    public void testSaveAttributeRelationshipCardinalityMany() {
        Attribute products = ContentTypes.Product.MEAL_INCLUDE_PRODUCTS;
        ContentKey relationshipSource = ContentKeyFactory.get(ContentType.Product, "productParent");
        ContentKey relationshipTarget1 = ContentKeyFactory.get(ContentType.Product, "testProduct1");
        ContentKey relationshipTarget2 = ContentKeyFactory.get(ContentType.Product, "testProduct2");
        ContentKey relationshipTarget3 = ContentKeyFactory.get(ContentType.Product, "testProduct3");

        databaseContentProvider.saveAttribute(relationshipSource, products, Arrays.asList(relationshipTarget1, relationshipTarget2, relationshipTarget3));

        Optional<Object> loadedRelationshipValues = databaseContentProvider.getAttributeValue(relationshipSource, products);

        Assert.assertTrue(loadedRelationshipValues.isPresent());
        Assert.assertTrue(List.class.isAssignableFrom(loadedRelationshipValues.get().getClass()));

        @SuppressWarnings("unchecked")
        List<ContentKey> relationshipTargets = (List<ContentKey>) loadedRelationshipValues.get();

        Assert.assertEquals(3, relationshipTargets.size());
        Assert.assertTrue(ContentKey.class.isAssignableFrom(relationshipTargets.get(0).getClass()));
        Assert.assertEquals(relationshipTarget1, relationshipTargets.get(0));
        Assert.assertEquals(relationshipTarget2, relationshipTargets.get(1));
        Assert.assertEquals(relationshipTarget3, relationshipTargets.get(2));
    }

    @Test
    public void testUpdateAttributeScalar() {
        Attribute fullName = ContentTypes.Category.FULL_NAME;
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Category, "test");

        String attributeValue = "Category full name";
        databaseContentProvider.saveAttribute(contentKey, fullName, attributeValue);

        String updatedValue = "Category full name updated";
        databaseContentProvider.saveAttribute(contentKey, fullName, updatedValue);

        databaseContentProvider.getContentKeys();

        Set<ContentKey> categories = databaseContentProvider.getContentKeysByType(ContentType.Category);
        Optional<Object> fullNameValue = databaseContentProvider.getAttributeValue(contentKey, fullName);

        Assert.assertEquals(2, categories.size());
        Assert.assertTrue(fullNameValue.isPresent());
        Assert.assertEquals(updatedValue, fullNameValue.get());
    }

    @Test
    public void testUpdateAttributeRelationshipWithCardinalityOne() {
        Attribute product = ContentTypes.Product.PERFECT_PAIR;
        ContentKey relationshipSource = ContentKeyFactory.get(ContentType.Product, "productParent");
        ContentKey relationshipValue = ContentKeyFactory.get(ContentType.Category, "testProduct");
        ContentKey updatedRelationshipValue = ContentKeyFactory.get(ContentType.Category, "testProductUpdated");

        databaseContentProvider.saveAttribute(relationshipSource, product, relationshipValue);
        databaseContentProvider.saveAttribute(relationshipSource, product, updatedRelationshipValue);
        Optional<Object> loadedRelationshipValue = databaseContentProvider.getAttributeValue(relationshipSource, product);

        Assert.assertTrue(loadedRelationshipValue.isPresent());
        Assert.assertEquals(updatedRelationshipValue, loadedRelationshipValue.get());
    }

    @Test
    public void testUpdateAttributeRelationshipCardinalityMany() {
        Attribute products = ContentTypes.Product.MEAL_INCLUDE_PRODUCTS;
        ContentKey relationshipSource = ContentKeyFactory.get(ContentType.Product, "productParent");
        ContentKey relationshipTarget1 = ContentKeyFactory.get(ContentType.Product, "testProduct1");
        ContentKey relationshipTarget2 = ContentKeyFactory.get(ContentType.Product, "testProduct2");
        ContentKey relationshipTarget3 = ContentKeyFactory.get(ContentType.Product, "testProduct3");

        databaseContentProvider.saveAttribute(relationshipSource, products, Arrays.asList(relationshipTarget1, relationshipTarget2, relationshipTarget3));

        ContentKey updatedRelationshipTarget1 = ContentKeyFactory.get(ContentType.Product, "testProduct3");
        ContentKey updatedRelationshipTarget2 = ContentKeyFactory.get(ContentType.Product, "testProduct1");

        databaseContentProvider.saveAttribute(relationshipSource, products, Arrays.asList(updatedRelationshipTarget1, updatedRelationshipTarget2));

        Optional<Object> loadedRelationshipValues = databaseContentProvider.getAttributeValue(relationshipSource, products);

        Assert.assertTrue(loadedRelationshipValues.isPresent());
        Assert.assertTrue(List.class.isAssignableFrom(loadedRelationshipValues.get().getClass()));

        @SuppressWarnings("unchecked")
        List<ContentKey> relationshipTargets = (List<ContentKey>) loadedRelationshipValues.get();

        Assert.assertEquals(2, relationshipTargets.size());
        Assert.assertTrue(ContentKey.class.isAssignableFrom(relationshipTargets.get(0).getClass()));
        Assert.assertEquals(updatedRelationshipTarget1, relationshipTargets.get(0));
        Assert.assertEquals(updatedRelationshipTarget2, relationshipTargets.get(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSaveAttributes() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "multipleAttributed");
        Attribute fullName = ContentTypes.Product.FULL_NAME;
        Attribute navName = ContentTypes.Product.NAV_NAME;
        Attribute parentRelation = ContentTypes.Product.PERFECT_PAIR;

        ContentKey parentCategory = ContentKeyFactory.get(ContentType.Category, "parentCategory");
        Attribute children = ContentTypes.Product.MEAL_INCLUDE_PRODUCTS;
        ContentKey child1 = ContentKeyFactory.get(ContentType.Product, "child1");
        ContentKey child2 = ContentKeyFactory.get(ContentType.Product, "child2");
        ContentKey child3 = ContentKeyFactory.get(ContentType.Product, "child3");
        ContentKey child4 = ContentKeyFactory.get(ContentType.Product, "child4");

        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(fullName, "Intermediete Product");
        attributesWithValues.put(navName, "IntProd");
        attributesWithValues.put(parentRelation, parentCategory);
        attributesWithValues.put(children, Arrays.asList(child1, child2, child3, child4));

        databaseContentProvider.saveAttributes(contentKey, attributesWithValues);

        Set<ContentKey> allContentKeys = databaseContentProvider.getContentKeys();
        Map<Attribute, Object> loadedAttributesWithValues = databaseContentProvider.getAttributeValues(contentKey, Arrays.asList(fullName, navName, parentRelation, children));

        Assert.assertTrue(allContentKeys.contains(contentKey));
        Assert.assertEquals(attributesWithValues.size(), loadedAttributesWithValues.size());
        Assert.assertEquals(((List<ContentKey>) attributesWithValues.get(children)).size(), ((List<ContentKey>) loadedAttributesWithValues.get(children)).size());

        List<ContentKey> loadedChildren = (List<ContentKey>) loadedAttributesWithValues.get(children);
        Assert.assertEquals(child1, loadedChildren.get(0));
        Assert.assertEquals(child2, loadedChildren.get(1));
        Assert.assertEquals(child3, loadedChildren.get(2));
        Assert.assertEquals(child4, loadedChildren.get(3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllAttributes() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "multipleAttributed1");
        ContentKey parentCategory = ContentKeyFactory.get(ContentType.Category, "parentCategory");
        ContentKey child1 = ContentKeyFactory.get(ContentType.Product, "child1");
        ContentKey child2 = ContentKeyFactory.get(ContentType.Product, "child2");
        ContentKey child3 = ContentKeyFactory.get(ContentType.Product, "child3");
        ContentKey child4 = ContentKeyFactory.get(ContentType.Product, "child4");

        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.Product.FULL_NAME, "Intermediete Product");
        attributesWithValues.put(ContentTypes.Product.NAV_NAME, "IntProd");
        attributesWithValues.put(ContentTypes.Product.PRIMARY_HOME, Arrays.asList(parentCategory));
        attributesWithValues.put(ContentTypes.Product.MEAL_INCLUDE_PRODUCTS, Arrays.asList(child1, child2, child3, child4));

        databaseContentProvider.saveAttributes(contentKey, attributesWithValues);

        Map<Attribute, Object> loadedAttributesWithValues = databaseContentProvider.getAllAttributesForContentKey(contentKey);

        Assert.assertEquals(attributesWithValues.size(), loadedAttributesWithValues.size());
        Assert.assertEquals(((List<ContentKey>) attributesWithValues.get(ContentTypes.Product.MEAL_INCLUDE_PRODUCTS)).size(),
                ((List<ContentKey>) loadedAttributesWithValues.get(ContentTypes.Product.MEAL_INCLUDE_PRODUCTS)).size());

        List<ContentKey> loadedChildren = (List<ContentKey>) loadedAttributesWithValues.get(ContentTypes.Product.MEAL_INCLUDE_PRODUCTS);
        Assert.assertEquals(child1, loadedChildren.get(0));
        Assert.assertEquals(child2, loadedChildren.get(1));
        Assert.assertEquals(child3, loadedChildren.get(2));
        Assert.assertEquals(child4, loadedChildren.get(3));

    }

    @Test
    public void testLoadingContentsHavingOnlyID() {
        // NOTE: commented lines are intentionally left in the test.
        // Actually they do exist in the legacy CMS system
        // but they break the test as Html type contains attributes
        // marked with 'required' flags, so validator will catch them
        //
        // ContentKey mediaKey = get(ContentType.Html, "14841697");
        ContentKey skuKey = get(ContentType.Sku, "GRO0069855");

        // databaseContentProvider.saveAttributes(mediaKey, Collections.<Attribute, Object>emptyMap());
        databaseContentProvider.saveAttributes(skuKey, Collections.<Attribute, Object>emptyMap());


        // Assert.assertTrue(databaseContentProvider.getContentKeys().contains(mediaKey));
        Assert.assertTrue(databaseContentProvider.getContentKeys().contains(skuKey));

        // Map<Attribute, Object> payload = databaseContentProvider.getAllAttributesForContentKey(mediaKey);
        // Assert.assertNotNull(payload);
        // Assert.assertTrue(payload.isEmpty());

        Map<Attribute, Object> payload = databaseContentProvider.getAllAttributesForContentKey(skuKey);
        Assert.assertNotNull(payload);
        Assert.assertTrue(payload.isEmpty());
    }
}
