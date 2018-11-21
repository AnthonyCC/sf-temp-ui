package com.freshdirect.cms.persistence.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.freshdirect.cms.cache.CacheEvictors;
import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.AttributeFlags;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.persistence.entity.AttributeEntity;
import com.freshdirect.cms.persistence.entity.ContentNodeEntity;
import com.freshdirect.cms.persistence.entity.RelationshipEntity;
import com.freshdirect.cms.persistence.entity.converter.AttributeEntityToValueConverter;
import com.freshdirect.cms.persistence.entity.converter.ContentKeyToContentNodeEntityConverter;
import com.freshdirect.cms.persistence.entity.converter.ContentNodeEntityToContentKeyConverter;
import com.freshdirect.cms.persistence.entity.converter.RelationshipToRelationshipEntityConverter;
import com.freshdirect.cms.persistence.entity.converter.ScalarToAttributeEntityConverter;
import com.freshdirect.cms.persistence.repository.AttributeEntityRepository;
import com.freshdirect.cms.persistence.repository.BatchSavingRepository;
import com.freshdirect.cms.persistence.repository.ContentNodeEntityRepository;
import com.freshdirect.cms.persistence.repository.NavigationTreeRepository;
import com.freshdirect.cms.persistence.repository.RelationshipEntityRepository;
import com.freshdirect.cms.util.EntityFactory;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DatabaseContentProviderTest {

    @InjectMocks
    private DatabaseContentProvider underTest;

    @Mock
    private ContentTypeInfoService contentTypeInfoService;

    @Mock
    private AttributeEntityRepository attributeEntityRepository;

    @Mock
    private RelationshipEntityRepository relationshipEntityRepository;

    @Mock
    private ContentNodeEntityRepository contentNodeEntityRepository;

    @Spy
    private ContentNodeEntityToContentKeyConverter contentNodeEntityToContentKeyConverter = new ContentNodeEntityToContentKeyConverter();

    @Mock
    private RelationshipToRelationshipEntityConverter relationshipToRelationshipEntityConverter;

    @Mock
    private ScalarToAttributeEntityConverter scalarToAttributeEntityConverter;

    @Mock
    private AttributeEntityToValueConverter attributeEntityToValueConverter;

    @Mock
    private ContentKeyToContentNodeEntityConverter converter;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private CacheEvictors cacheEvictors;

    @Mock
    private NavigationTreeRepository navigationTreeRepository;

    @Mock
    private BatchSavingRepository batchSavingRepository;

    @Before
    public void setupTests() {
        ContentTypeInfoService svc = new ContentTypeInfoService();

        Mockito.when(contentTypeInfoService.selectAttributes(ContentType.Product)).thenReturn(svc.selectAttributes(ContentType.Product));
        Mockito.when(contentTypeInfoService.selectAttributes(ContentType.Category)).thenReturn(svc.selectAttributes(ContentType.Category));
        Mockito.when(cacheManager.getCache(Mockito.anyString())).thenReturn(cache);
        Mockito.when(cache.get(Mockito.any(Object.class))).thenReturn(null);
    }

    @Test
    public void testGetContentKeys() {
        List<ContentNodeEntity> contentNodeEntities = ImmutableList.of(EntityFactory.createContentNode());
        Mockito.when(contentNodeEntityRepository.findAll()).thenReturn(contentNodeEntities);
        underTest.loadAll();

        Set<ContentKey> loadedContentKeys = underTest.getContentKeys();

        Assert.assertNotNull(loadedContentKeys);
        Assert.assertEquals(contentNodeEntities.size(), loadedContentKeys.size());
        Assert.assertEquals(contentNodeEntities.get(0).getContentKey(), loadedContentKeys.iterator().next().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContentKeysByTypeWithNullType() {
        underTest.getContentKeysByType(null);
    }

    @Test
    public void testGetContentKeysByType() {
        Mockito.when(contentNodeEntityRepository.findAll()).thenReturn(
                ImmutableList.of(new ContentNodeEntity("Product:prd1", "Product"), new ContentNodeEntity("Product:prd2", "Product")));
        underTest.loadAll();

        Set<ContentKey> loadedContentKeys = underTest.getContentKeysByType(EntityFactory.CONTENT_TYPE_ENUM);

        Assert.assertNotNull(loadedContentKeys);
        Assert.assertEquals(2, loadedContentKeys.size());
        Assert.assertEquals(ImmutableSet.of(ContentKeyFactory.get(ContentType.Product, "prd1"), ContentKeyFactory.get(ContentType.Product, "prd2")), loadedContentKeys);
    }

    @Test
    public void testGetAttributeValue() {
        AttributeEntity attributeEntity = EntityFactory.createAttribute();
        Attribute scalar = EntityFactory.createScalarAttribute();
        Mockito.when(contentTypeInfoService.selectAttributes(EntityFactory.CONTENT_TYPE_ENUM))
                .thenReturn(Collections.singleton(scalar));
        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(EntityFactory.CONTENT_KEY, Collections.singletonList(EntityFactory.NAME)))
                .thenReturn(Collections.singletonList(attributeEntity));
        Mockito.when(attributeEntityToValueConverter.convert(scalar, attributeEntity))
                .thenReturn(EntityFactory.VALUE);

        Optional<Object> loadedValue = underTest.getAttributeValue(EntityFactory.createContentKey(), scalar);

        Assert.assertTrue(loadedValue.isPresent());
        Assert.assertEquals(attributeEntity.getValue(), loadedValue.get());
    }

    @Test
    public void testGetAttributeValueWithNotExistingAttribute() {
        Mockito.when(attributeEntityRepository.findByContentKeyAndName(EntityFactory.CONTENT_KEY, EntityFactory.NAME)).thenReturn(null);
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
        Map<Attribute, Object> expected = new HashMap<com.freshdirect.cms.core.domain.Attribute, Object>();
        Map<Attribute, Object> loaded = underTest.getAttributeValues(EntityFactory.createContentKey(),
                new ArrayList<com.freshdirect.cms.core.domain.Attribute>());

        Assert.assertEquals(expected, loaded);
        Assert.assertEquals(loaded.values().size(), 0);
        Assert.assertEquals(loaded.keySet().size(), 0);
    }

    @Test
    public void testGetAttributeValues() {
        Attribute attributeInQuestion = ContentTypes.Product.FULL_NAME;
        Map<Attribute, Object> expected = new HashMap<Attribute, Object>();
        expected.put(attributeInQuestion, EntityFactory.VALUE);

        AttributeEntity attributeEntity = EntityFactory.createAttribute();

        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(EntityFactory.CONTENT_KEY), Mockito.anyListOf(String.class)))
                .thenReturn(Arrays.asList(attributeEntity));

        Mockito.when(attributeEntityToValueConverter.convert(attributeInQuestion, attributeEntity)).thenReturn(EntityFactory.VALUE);

        Map<Attribute, Object> loaded = underTest.getAttributeValues(EntityFactory.createContentKey(), Arrays.asList(attributeInQuestion));

        Assert.assertEquals(expected.size(), loaded.size());
        Assert.assertTrue(loaded.containsKey(attributeInQuestion));
        Assert.assertEquals(expected.get(attributeInQuestion), loaded.get(attributeInQuestion));
    }

    @Test
    public void testGetAttributesContainingOneNotExistingAttribute() {
        Attribute attributeInQuestion = ContentTypes.Product.FULL_NAME;
        Attribute notExistingAttribute = EntityFactory.createScalarAttribute("notExisting", AttributeFlags.NONE, String.class);
        Map<Attribute, Object> expected = new HashMap<Attribute, Object>();
        expected.put(attributeInQuestion, EntityFactory.VALUE);

        AttributeEntity attributeEntity = EntityFactory.createAttribute();

        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(EntityFactory.CONTENT_KEY), Mockito.anyListOf(String.class)))
                .thenReturn(Arrays.asList(attributeEntity));
        Mockito.when(attributeEntityToValueConverter.convert(attributeInQuestion, attributeEntity)).thenReturn(EntityFactory.VALUE);

        Map<Attribute, Object> loaded = underTest.getAttributeValues(EntityFactory.createContentKey(), Arrays.asList(attributeInQuestion, notExistingAttribute));

        Assert.assertEquals(expected.size(), loaded.size());
        Assert.assertTrue(loaded.containsKey(attributeInQuestion));
        Assert.assertFalse(loaded.containsKey(notExistingAttribute));
        Assert.assertEquals(expected.get(attributeInQuestion), loaded.get(attributeInQuestion));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAttributeWithNullContentKey() {
        underTest.saveAttribute(null, EntityFactory.createScalarAttribute(), "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAttributeWithNullAttribute() {
        underTest.saveAttribute(EntityFactory.createContentKey(), null, "test");
    }

    @Test
    public void testSaveAttributeWithNullAttributeValue() {
        final ContentKey testKey = EntityFactory.createContentKey();
        final Attribute testAttribute = EntityFactory.createScalarAttribute();

        underTest.saveAttribute(testKey, testAttribute, null);

        Optional<Object> value = underTest.getAttributeValue(testKey, testAttribute);
        Assert.assertNotNull(value);
        Assert.assertTrue(!value.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAttributesWithNullAttributesWithValues() {
        underTest.saveAttributes(EntityFactory.createContentKey(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAttributesWithNullContentKey() {
        underTest.saveAttributes(null, new HashMap<Attribute, Object>());
    }

    @Test
    public void testCacheEvict() {
        Mockito.when(navigationTreeRepository.queryChildrenOfContentKey(Mockito.anySetOf(ContentKey.class))).thenReturn(new HashSet<ContentKey>());
        underTest.cacheEvict(ContentKeyFactory.get(ContentType.Category, "Testing_Category"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChacheEvictWithNullContentKey() {
        underTest.cacheEvict(null);
    }

    @Test
    public void testGetAttributesForContentKeys() {
        ContentKey product1 = ContentKeyFactory.get(ContentType.Product, "test");
        ContentKey product2 = ContentKeyFactory.get(ContentType.Product, "test2");

        AttributeEntity product1FullName = new AttributeEntity();
        product1FullName.setContentKey(product1.toString());
        product1FullName.setName("FULL_NAME");
        AttributeEntity product1NavName = new AttributeEntity();
        product1NavName.setContentKey(product1.toString());
        product1NavName.setName("NAV_NAME");

        AttributeEntity product2FullName = new AttributeEntity();
        product2FullName.setContentKey(product2.toString());
        product2FullName.setName("FULL_NAME");
        AttributeEntity product2NavName = new AttributeEntity();
        product2NavName.setContentKey(product2.toString());
        product2NavName.setName("NAV_NAME");

        product1FullName.setId(1);
        product2FullName.setId(2);
        product1NavName.setId(3);
        product2NavName.setId(4);

        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(product1.toString()), Mockito.anyListOf(String.class)))
                .thenReturn(Arrays.asList(product1FullName, product1NavName));
        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(product2.toString()), Mockito.anyListOf(String.class)))
                .thenReturn(Arrays.asList(product2FullName, product2NavName));
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Product.FULL_NAME, product1FullName)).thenReturn("Product 1 full name");
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Product.NAV_NAME, product1NavName)).thenReturn("Product 1 nav name");
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Product.FULL_NAME, product2FullName)).thenReturn("Product 2 full name");
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Product.NAV_NAME, product2NavName)).thenReturn("Product 2 nav name");

        Map<ContentKey, Map<Attribute, Object>> loaded = underTest.getAttributesForContentKeys(Arrays.asList(product1, product2),
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.NAV_NAME));

        Assert.assertNotNull(loaded);
        Assert.assertEquals(2, loaded.size());
        Assert.assertTrue(loaded.containsKey(product1));
        Assert.assertTrue(loaded.containsKey(product2));

        Assert.assertTrue(loaded.get(product1).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(loaded.get(product1).containsKey(ContentTypes.Product.NAV_NAME));
        Assert.assertEquals("Product 1 full name", loaded.get(product1).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals("Product 1 nav name", loaded.get(product1).get(ContentTypes.Product.NAV_NAME));

        Assert.assertTrue(loaded.get(product2).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(loaded.get(product2).containsKey(ContentTypes.Product.NAV_NAME));
        Assert.assertEquals("Product 2 full name", loaded.get(product2).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals("Product 2 nav name", loaded.get(product2).get(ContentTypes.Product.NAV_NAME));
    }

    @Test
    public void testGetAttributesForContentKeysWithMixedContentTypes() {
        ContentKey product = ContentKeyFactory.get(ContentType.Product, "test");
        ContentKey category = ContentKeyFactory.get(ContentType.Category, "test2");

        AttributeEntity productFullName = new AttributeEntity();
        productFullName.setContentKey(product.toString());
        productFullName.setName("FULL_NAME");
        AttributeEntity productNavName = new AttributeEntity();
        productNavName.setContentKey(product.toString());
        productNavName.setName("NAV_NAME");

        AttributeEntity categoryFullName = new AttributeEntity();
        categoryFullName.setContentKey(category.toString());
        categoryFullName.setName("FULL_NAME");
        AttributeEntity catgoryContainsBeer = new AttributeEntity();
        catgoryContainsBeer.setContentKey(category.toString());
        catgoryContainsBeer.setName("CONTAINS_BEER");

        productFullName.setId(1);
        categoryFullName.setId(2);
        productNavName.setId(3);
        catgoryContainsBeer.setId(4);

        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(product.toString()), Mockito.anyListOf(String.class)))
                .thenReturn(Arrays.asList(productFullName, productNavName));
        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(category.toString()), Mockito.anyListOf(String.class)))
                .thenReturn(Arrays.asList(categoryFullName, catgoryContainsBeer));
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Product.FULL_NAME, productFullName)).thenReturn("Product 1 full name");
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Product.NAV_NAME, productNavName)).thenReturn("Product 1 nav name");
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Category.FULL_NAME, categoryFullName)).thenReturn("Category full name");
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Category.CONTAINS_BEER, catgoryContainsBeer)).thenReturn(false);

        Map<ContentKey, Map<Attribute, Object>> loaded = underTest.getAttributesForContentKeys(Arrays.asList(product, category),
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.NAV_NAME, ContentTypes.Category.CONTAINS_BEER, ContentTypes.Category.FULL_NAME));

        Assert.assertNotNull(loaded);
        Assert.assertEquals(2, loaded.size());
        Assert.assertTrue(loaded.containsKey(product));
        Assert.assertTrue(loaded.containsKey(category));

        Assert.assertTrue(loaded.get(product).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(loaded.get(product).containsKey(ContentTypes.Product.NAV_NAME));
        Assert.assertFalse(loaded.get(product).containsKey(ContentTypes.Category.CONTAINS_BEER));
        Assert.assertEquals(2, loaded.get(product).size());
        Assert.assertEquals("Product 1 full name", loaded.get(product).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals("Product 1 nav name", loaded.get(product).get(ContentTypes.Product.NAV_NAME));

        Assert.assertTrue(loaded.get(category).containsKey(ContentTypes.Category.FULL_NAME));
        Assert.assertTrue(loaded.get(category).containsKey(ContentTypes.Category.CONTAINS_BEER));
        Assert.assertTrue(loaded.get(category).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(2, loaded.get(category).size());
        Assert.assertEquals("Category full name", loaded.get(category).get(ContentTypes.Category.FULL_NAME));
        Assert.assertEquals(false, loaded.get(category).get(ContentTypes.Category.CONTAINS_BEER));
    }

    @Test
    public void testGetAttributesForContentKeysContainingNotExistingContentKey() {
        ContentKey product = ContentKeyFactory.get(ContentType.Product, "test");
        ContentKey category = ContentKeyFactory.get(ContentType.Category, "notExisting");

        AttributeEntity productFullName = new AttributeEntity();
        productFullName.setContentKey(product.toString());
        productFullName.setName("FULL_NAME");
        AttributeEntity productNavName = new AttributeEntity();
        productNavName.setContentKey(product.toString());
        productNavName.setName("NAV_NAME");

        productFullName.setId(1);
        productNavName.setId(2);

        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(product.toString()), Mockito.anyListOf(String.class)))
                .thenReturn(Arrays.asList(productFullName, productNavName));
        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(category.toString()), Mockito.anyListOf(String.class))).thenReturn(null);
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Product.FULL_NAME, productFullName)).thenReturn("Product 1 full name");
        Mockito.when(attributeEntityToValueConverter.convert(ContentTypes.Product.NAV_NAME, productNavName)).thenReturn("Product 1 nav name");

        Map<ContentKey, Map<Attribute, Object>> loaded = underTest.getAttributesForContentKeys(Arrays.asList(product, category),
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.NAV_NAME, ContentTypes.Category.CONTAINS_BEER, ContentTypes.Category.FULL_NAME));

        Assert.assertNotNull(loaded);
        Assert.assertEquals(1, loaded.size());
        Assert.assertTrue(loaded.containsKey(product));
        Assert.assertFalse(loaded.containsKey(category));

        Assert.assertTrue(loaded.get(product).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(loaded.get(product).containsKey(ContentTypes.Product.NAV_NAME));
        Assert.assertFalse(loaded.get(product).containsKey(ContentTypes.Category.CONTAINS_BEER));
        Assert.assertEquals(2, loaded.get(product).size());
        Assert.assertEquals("Product 1 full name", loaded.get(product).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals("Product 1 nav name", loaded.get(product).get(ContentTypes.Product.NAV_NAME));
    }

    @Test
    public void testGetAttributesForContentKeysWithNotExistingContentKey() {
        ContentKey product = ContentKeyFactory.get(ContentType.Product, "notExistingProduct");
        ContentKey category = ContentKeyFactory.get(ContentType.Category, "notExisting");
        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(product.toString()), Mockito.anyListOf(String.class))).thenReturn(null);
        Mockito.when(attributeEntityRepository.findByContentKeyAndNameIn(Mockito.eq(category.toString()), Mockito.anyListOf(String.class))).thenReturn(null);

        Map<ContentKey, Map<Attribute, Object>> loaded = underTest.getAttributesForContentKeys(Arrays.asList(product, category),
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.NAV_NAME, ContentTypes.Category.CONTAINS_BEER, ContentTypes.Category.FULL_NAME));

        Assert.assertNotNull(loaded);
        Assert.assertEquals(0, loaded.size());
        Assert.assertFalse(loaded.containsKey(product));
        Assert.assertFalse(loaded.containsKey(category));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributesForContentKeysWithNullContentKeys() {
        underTest.getAttributesForContentKeys(null,
                Arrays.asList(ContentTypes.Product.FULL_NAME, ContentTypes.Product.NAV_NAME, ContentTypes.Category.CONTAINS_BEER, ContentTypes.Category.FULL_NAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributesForContentKeysWithNullAttributes() {
        ContentKey product = ContentKeyFactory.get(ContentType.Product, "notExistingProduct");
        ContentKey category = ContentKeyFactory.get(ContentType.Category, "notExisting");
        underTest.getAttributesForContentKeys(Arrays.asList(product, category), null);
    }
    
    @Test
    public void testGetAllAttributesForContentKeysWithCorrectOrdering(){
        final ContentKey category1 = ContentKeyFactory.get("Category:parent1");
        
        final RelationshipEntity product1 = EntityFactory.createRelationship(1, 0, "Category:parent1", "Product", "Product:prod1", "children");
        final RelationshipEntity product2 = EntityFactory.createRelationship(2, 1, "Category:parent1", "Product", "Product:prod2", "children");
        final RelationshipEntity product3 = EntityFactory.createRelationship(3, 2, "Category:parent1", "Product", "Product:prod3", "children");
        final RelationshipEntity product4 = EntityFactory.createRelationship(4, 3, "Category:parent1", "Product", "Product:prod4", "children");
        final RelationshipEntity product5 = EntityFactory.createRelationship(5, 4, "Category:parent1", "Product", "Product:prod5", "children");
        
        final Relationship relationshipAttribute = new Relationship("children", AttributeFlags.NONE, false, RelationshipCardinality.MANY, true, ContentType.Product);
        
        Mockito.when(attributeEntityRepository.findByContentKeyIn(Mockito.anyList())).thenReturn(Collections.emptyList());
        Mockito.when(relationshipEntityRepository.findByRelationshipSourceIn(Mockito.anyList())).thenReturn(new ArrayList<RelationshipEntity>() {{
            add(product1);
            add(product2);
            add(product3);
            add(product4);
            add(product5);
        }});
        
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product1)).thenReturn(ContentKeyFactory.get("Product:prod1"));
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product2)).thenReturn(ContentKeyFactory.get("Product:prod2"));
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product3)).thenReturn(ContentKeyFactory.get("Product:prod3"));
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product4)).thenReturn(ContentKeyFactory.get("Product:prod4"));
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product5)).thenReturn(ContentKeyFactory.get("Product:prod5"));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Category, "children")).thenReturn(Optional.of((Attribute) relationshipAttribute));
        
        Map<ContentKey, Map<Attribute, Object>> result = underTest.getAllAttributesForContentKeys(new HashSet<ContentKey>() {{ 
            add(category1);
        }});
        
        Map<Attribute, Object> expectedRelationshipTargets = new HashMap<Attribute, Object>();
        expectedRelationshipTargets.put(relationshipAttribute, new ArrayList<ContentKey>() {{
            add(ContentKeyFactory.get("Product:prod1"));
            add(ContentKeyFactory.get("Product:prod2"));
            add(ContentKeyFactory.get("Product:prod3"));
            add(ContentKeyFactory.get("Product:prod4"));
            add(ContentKeyFactory.get("Product:prod5"));
        }});
        
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey(category1));
        Assert.assertTrue(!result.get(category1).isEmpty());
        Assert.assertEquals(expectedRelationshipTargets, result.get(category1));
    }
    
    /**
     * Although this test is testing the getAllAttributesForContentKeys method, this is just because that's the public method. The target of this test is the 
     * processFetchedRelationships private method by the means of visiting it. 
     * If this test fails, most possibly the cause is: If the relationship entities are not returned in the correct ordinal,
     * the processFetchedRelationships method failes to order them correctly.
     */
    @Test
    public void testGetAllAttributesForContentKeysWithNotCorrectOrdering(){
        final ContentKey category1 = ContentKeyFactory.get("Category:parent1");
        
        final RelationshipEntity product1 = EntityFactory.createRelationship(1, 0, "Category:parent1", "Product", "Product:prod1", "children");
        final RelationshipEntity product2 = EntityFactory.createRelationship(2, 1, "Category:parent1", "Product", "Product:prod2", "children");
        final RelationshipEntity product3 = EntityFactory.createRelationship(3, 2, "Category:parent1", "Product", "Product:prod3", "children");
        final RelationshipEntity product4 = EntityFactory.createRelationship(4, 3, "Category:parent1", "Product", "Product:prod4", "children");
        final RelationshipEntity product5 = EntityFactory.createRelationship(5, 4, "Category:parent1", "Product", "Product:prod5", "children");
        
        final Relationship relationshipAttribute = new Relationship("children", AttributeFlags.NONE, false, RelationshipCardinality.MANY, true, ContentType.Product);
        
        Mockito.when(attributeEntityRepository.findByContentKeyIn(Mockito.anyList())).thenReturn(Collections.emptyList());
        Mockito.when(relationshipEntityRepository.findByRelationshipSourceIn(Mockito.anyList())).thenReturn(new ArrayList<RelationshipEntity>() {{
            add(product5);
            add(product2);
            add(product4);
            add(product3);
            add(product1);
        }});
        
        
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product1)).thenReturn(ContentKeyFactory.get("Product:prod1"));
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product2)).thenReturn(ContentKeyFactory.get("Product:prod2"));
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product3)).thenReturn(ContentKeyFactory.get("Product:prod3"));
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product4)).thenReturn(ContentKeyFactory.get("Product:prod4"));
        Mockito.when(attributeEntityToValueConverter.convert(relationshipAttribute, product5)).thenReturn(ContentKeyFactory.get("Product:prod5"));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Category, "children")).thenReturn(Optional.of((Attribute) relationshipAttribute));
        
        Map<ContentKey, Map<Attribute, Object>> result = underTest.getAllAttributesForContentKeys(new HashSet<ContentKey>() {{ 
            add(category1);
        }});
        
        Map<Attribute, Object> expectedRelationshipTargets = new HashMap<Attribute, Object>();
        expectedRelationshipTargets.put(relationshipAttribute, new ArrayList<ContentKey>() {{
            add(ContentKeyFactory.get("Product:prod1"));
            add(ContentKeyFactory.get("Product:prod2"));
            add(ContentKeyFactory.get("Product:prod3"));
            add(ContentKeyFactory.get("Product:prod4"));
            add(ContentKeyFactory.get("Product:prod5"));
        }});
        
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey(category1));
        Assert.assertTrue(!result.get(category1).isEmpty());
        Assert.assertEquals(expectedRelationshipTargets, result.get(category1)); // the important thing is the order of the products!
    }
}
