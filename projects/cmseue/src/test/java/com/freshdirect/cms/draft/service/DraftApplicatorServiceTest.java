package com.freshdirect.cms.draft.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftApplicatorServiceTest {

    @Mock
    private ContentTypeInfoService contentTypeInfoService;

    @InjectMocks
    private DraftApplicatorService underTest;

    @Before
    public void setUp() {
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "FULL_NAME")).thenReturn(Optional.of(ContentTypes.Product.FULL_NAME));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "PREFERRED_SKU")).thenReturn(Optional.of(ContentTypes.Product.PREFERRED_SKU));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "skus")).thenReturn(Optional.of(ContentTypes.Product.skus));
    }
    
    @Test
    public void testApplyDraftChangesToContentNodesWithMultipleChangeForOneNode() {
        String contentKeyAsString = "Product:draft_modified";
        ContentKey contentKey = ContentKeyFactory.get(contentKeyAsString);

        DraftChange draftChangeScalar = new DraftChange();
        draftChangeScalar.setId(1L);
        draftChangeScalar.setAttributeName("FULL_NAME");
        draftChangeScalar.setContentKey(contentKeyAsString);
        draftChangeScalar.setValue("Draft_node");
        draftChangeScalar.setUserName("testUser");
        draftChangeScalar.setCreatedAt(System.currentTimeMillis());

        DraftChange draftChangeSingeRelationship = new DraftChange();
        draftChangeSingeRelationship.setId(2L);
        draftChangeSingeRelationship.setAttributeName("PREFERRED_SKU");
        draftChangeSingeRelationship.setContentKey(contentKeyAsString);
        draftChangeSingeRelationship.setValue("Sku:preferredsku");
        draftChangeSingeRelationship.setUserName("testUser");
        draftChangeSingeRelationship.setCreatedAt(System.currentTimeMillis());

        DraftChange draftChangeMultiRelationship = new DraftChange();
        draftChangeMultiRelationship.setId(3L);
        draftChangeMultiRelationship.setAttributeName("skus");
        draftChangeMultiRelationship.setContentKey(contentKeyAsString);
        draftChangeMultiRelationship.setValue("Sku:preferredsku|Sku:jabba|Sku:batman|Sku:greenLantern|Sku:INeedACoffee");
        draftChangeMultiRelationship.setUserName("testUser");
        draftChangeMultiRelationship.setCreatedAt(System.currentTimeMillis());

        List<DraftChange> draftChanges = ImmutableList.of(draftChangeScalar, draftChangeSingeRelationship, draftChangeMultiRelationship);
        
        Map<Attribute, Object> contentNode = new HashMap<Attribute, Object>();
        contentNode.put(ContentTypes.Product.FULL_NAME, "value_on_main");
        contentNode.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredSkuOnMain"));
        contentNode.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")));

        contentNode.putAll(underTest.convertDraftChanges(draftChanges).get(contentKey));
        
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.skus));

        Assert.assertEquals(ContentKeyFactory.get("Sku:preferredsku"), contentNode.get(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals("Draft_node", contentNode.get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(Arrays.asList(ContentKeyFactory.get("Sku:preferredsku"), ContentKeyFactory.get("Sku:jabba"), ContentKeyFactory.get("Sku:batman"),
                ContentKeyFactory.get("Sku:greenLantern"), ContentKeyFactory.get("Sku:INeedACoffee")), contentNode.get(ContentTypes.Product.skus));
    }

    @Test
    public void testApplyDraftChangesToContentNodesWithScalarChange() {
        String contentKeyAsString = "Product:draft_modified";
        ContentKey contentKey = ContentKeyFactory.get(contentKeyAsString);

        DraftChange draftChangeScalar = new DraftChange();
        draftChangeScalar.setAttributeName("FULL_NAME");
        draftChangeScalar.setContentKey(contentKeyAsString);
        draftChangeScalar.setValue("Draft_node");
        draftChangeScalar.setUserName("testUser");
        draftChangeScalar.setCreatedAt(System.currentTimeMillis());

        List<DraftChange> draftChanges = ImmutableList.of(draftChangeScalar);

        Map<Attribute, Object> contentNode = new HashMap<Attribute, Object>();
        contentNode.put(ContentTypes.Product.FULL_NAME, "value_on_main");
        contentNode.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredSkuOnMain"));
        contentNode.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")));

        contentNode.putAll(underTest.convertDraftChanges(draftChanges).get(contentKey));

        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.skus));

        Assert.assertEquals("Draft_node", contentNode.get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(ContentKeyFactory.get("Sku:preferredSkuOnMain"), contentNode.get(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals(Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")),
                contentNode.get(ContentTypes.Product.skus));
    }

    @Test
    public void testApplyDraftChangesToContentNodesWithSingleRelationship() {
        String contentKeyAsString = "Product:draft_modified";
        ContentKey contentKey = ContentKeyFactory.get(contentKeyAsString);

        DraftChange draftChangeSingeRelationship = new DraftChange();
        draftChangeSingeRelationship.setAttributeName("PREFERRED_SKU");
        draftChangeSingeRelationship.setContentKey(contentKeyAsString);
        draftChangeSingeRelationship.setValue("Sku:preferredsku");
        draftChangeSingeRelationship.setUserName("testUser");
        draftChangeSingeRelationship.setCreatedAt(System.currentTimeMillis());

        List<DraftChange> draftChanges = ImmutableList.of(draftChangeSingeRelationship);

        Map<Attribute, Object> contentNode = new HashMap<Attribute, Object>();
        contentNode.put(ContentTypes.Product.FULL_NAME, "value_on_main");
        contentNode.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredSkuOnMain"));
        contentNode.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")));

        contentNode.putAll(underTest.convertDraftChanges(draftChanges).get(contentKey));

        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.skus));

        Assert.assertEquals("value_on_main", contentNode.get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(ContentKeyFactory.get("Sku:preferredsku"), contentNode.get(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals(Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")),
                contentNode.get(ContentTypes.Product.skus));
    }

    @Test
    public void testApplyDraftChangesToContentNodesWithMultiRelationship() {
        String contentKeyAsString = "Product:draft_modified";
        ContentKey contentKey = ContentKeyFactory.get(contentKeyAsString);

        DraftChange draftChangeMultiRelationship = new DraftChange();
        draftChangeMultiRelationship.setAttributeName("skus");
        draftChangeMultiRelationship.setContentKey(contentKeyAsString);
        draftChangeMultiRelationship.setValue("Sku:preferredsku|Sku:jabba|Sku:batman|Sku:greenLantern|Sku:INeedACoffee");
        draftChangeMultiRelationship.setUserName("testUser");
        draftChangeMultiRelationship.setCreatedAt(System.currentTimeMillis());

        List<DraftChange> draftChanges = ImmutableList.of(draftChangeMultiRelationship);

        Map<Attribute, Object> contentNode = new HashMap<Attribute, Object>();
        contentNode.put(ContentTypes.Product.FULL_NAME, "value_on_main");
        contentNode.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredSkuOnMain"));
        contentNode.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")));

        contentNode.putAll(underTest.convertDraftChanges(draftChanges).get(contentKey));

        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(contentNode.containsKey(ContentTypes.Product.skus));

        Assert.assertEquals("value_on_main", contentNode.get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(ContentKeyFactory.get("Sku:preferredSkuOnMain"), contentNode.get(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals(Arrays.asList(ContentKeyFactory.get("Sku:preferredsku"), ContentKeyFactory.get("Sku:jabba"), ContentKeyFactory.get("Sku:batman"),
                ContentKeyFactory.get("Sku:greenLantern"), ContentKeyFactory.get("Sku:INeedACoffee")), contentNode.get(ContentTypes.Product.skus));
    }
}
