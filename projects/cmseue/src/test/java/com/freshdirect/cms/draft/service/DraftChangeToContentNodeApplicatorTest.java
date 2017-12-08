package com.freshdirect.cms.draft.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.converter.SerializedScalarValueToObjectConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.google.common.base.Optional;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftChangeToContentNodeApplicatorTest {

    @Mock
    private ContentTypeInfoService contentTypeInfoService;

    @Mock
    private SerializedScalarValueToObjectConverter serializedValueConverter;

    @InjectMocks
    private DraftChangeToContentNodeApplicator underTest;

    @Test
    public void testApplyDraftChangeToNodeWithScalarAttribute() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_product");

        Map<Attribute, Object> mainNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.FULL_NAME, "main_full_name");

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName(ContentTypes.Product.FULL_NAME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue("draft_overriden_full_name");

        Mockito.when(contentTypeInfoService.findAttributeByName(productKey.type, ContentTypes.Product.FULL_NAME.getName()))
                .thenReturn(Optional.fromNullable(ContentTypes.Product.FULL_NAME));
        Mockito.when(serializedValueConverter.convert(ContentTypes.Product.FULL_NAME, "draft_overriden_full_name")).thenReturn("draft_overriden_full_name");

        Map<Attribute, Object> draftAppliedAttributes = underTest.applyDraftChangeToNode(draftChange, mainNode);

        Assert.assertTrue(draftAppliedAttributes.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals("draft_overriden_full_name", draftAppliedAttributes.get(ContentTypes.Product.FULL_NAME));
    }

    @Test
    public void testApplyDraftChangeToNodeWithRelationshipAttribute() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_product");

        ContentKey skuForProductOnMain = ContentKeyFactory.get(ContentType.Sku, "sku_on_main");
        ContentKey skuForProductOnDraft = ContentKeyFactory.get(ContentType.Sku, "sku_on_draft");

        Map<Attribute, Object> mainNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.PREFERRED_SKU, skuForProductOnMain);

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName(ContentTypes.Product.PREFERRED_SKU.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue(skuForProductOnDraft.toString());

        Mockito.when(contentTypeInfoService.findAttributeByName(productKey.type, ContentTypes.Product.PREFERRED_SKU.getName()))
                .thenReturn(Optional.fromNullable(ContentTypes.Product.PREFERRED_SKU));

        Map<Attribute, Object> draftAppliedAttributes = underTest.applyDraftChangeToNode(draftChange, mainNode);

        Assert.assertTrue(draftAppliedAttributes.containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals(skuForProductOnDraft, draftAppliedAttributes.get(ContentTypes.Product.PREFERRED_SKU));
    }

    @Test
    public void testGetContentKeysFromSingleRelationshipValue() {
        ContentKey relationshipTarget = ContentKeyFactory.get(ContentType.Sku, "sku_target");

        Relationship singleRelationship = (Relationship) ContentTypes.Product.PREFERRED_SKU;

        List<ContentKey> results = underTest.getContentKeysFromRelationshipValue(singleRelationship, relationshipTarget.toString());

        Assert.assertFalse(results.isEmpty());
        Assert.assertEquals(1, results.size());
        Assert.assertTrue(results.contains(relationshipTarget));
    }

    @Test
    public void testGetContentKeysFromMultiRelationshipValue() {
        ContentKey relationshipTarget = ContentKeyFactory.get(ContentType.Sku, "sku_target");
        ContentKey relationshipTarget2 = ContentKeyFactory.get(ContentType.Sku, "sku_target2");

        Relationship multiRelationship = (Relationship) ContentTypes.Product.skus;

        List<ContentKey> results = underTest.getContentKeysFromRelationshipValue(multiRelationship, relationshipTarget.toString() + "|" + relationshipTarget2.toString());

        Assert.assertFalse(results.isEmpty());
        Assert.assertEquals(2, results.size());
        Assert.assertTrue(results.contains(relationshipTarget));
        Assert.assertTrue(results.contains(relationshipTarget2));
    }

    @Test
    public void testCreateContentNodeFromDraftChange() {

        ContentKey resultNodeKey = ContentKeyFactory.get(ContentType.Product, "not_existing_on_main");

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName("FULL_NAME");
        draftChange.setContentKey("Product:not_existing_on_main");
        draftChange.setValue("Draft_node");
        draftChange.setUserName("testUser");
        draftChange.setCreatedAt(System.currentTimeMillis());

        Mockito.when(contentTypeInfoService.findAttributeByName(resultNodeKey.type, ContentTypes.Product.FULL_NAME.getName()))
                .thenReturn(Optional.fromNullable(ContentTypes.Product.FULL_NAME));
        Mockito.when(serializedValueConverter.convert(ContentTypes.Product.FULL_NAME, "Draft_node")).thenReturn("Draft_node");

        Map<ContentKey, Map<Attribute, Object>> resultNode = underTest.createContentNodeFromDraftChange(draftChange);

        Assert.assertFalse(resultNode.isEmpty());
        Assert.assertTrue(resultNode.containsKey(resultNodeKey));
        Assert.assertTrue(resultNode.get(resultNodeKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals("Draft_node", resultNode.get(resultNodeKey).get(ContentTypes.Product.FULL_NAME));
    }

    @Test
    public void testApplyDraftChangeToNodeWhenDraftChangeValueIsNullAndAttributeIsNotRelationship() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_product");

        Map<Attribute, Object> mainNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.FULL_NAME, "main_full_name");

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName(ContentTypes.Product.FULL_NAME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue(null);

        Mockito.when(contentTypeInfoService.findAttributeByName(productKey.type, ContentTypes.Product.FULL_NAME.getName()))
                .thenReturn(Optional.fromNullable(ContentTypes.Product.FULL_NAME));

        Map<Attribute, Object> draftAppliedAttributes = underTest.applyDraftChangeToNode(draftChange, mainNode);

        Assert.assertTrue(draftAppliedAttributes.containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(null, draftAppliedAttributes.get(ContentTypes.Product.FULL_NAME));
    }

    @Test
    public void testApplyDraftChangeToNodeWhenDraftChangeValueIsNullAndAttributeIsRelationshipOne() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_product");

        Map<Attribute, Object> mainNode = new HashMap<Attribute, Object>();
        mainNode.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:testPreferred"));

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName(ContentTypes.Product.PREFERRED_SKU.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue(null);

        Mockito.when(contentTypeInfoService.findAttributeByName(productKey.type, ContentTypes.Product.PREFERRED_SKU.getName()))
                .thenReturn(Optional.fromNullable(ContentTypes.Product.PREFERRED_SKU));

        Map<Attribute, Object> draftAppliedAttributes = underTest.applyDraftChangeToNode(draftChange, mainNode);

        Assert.assertTrue(draftAppliedAttributes.containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals(null, draftAppliedAttributes.get(ContentTypes.Product.PREFERRED_SKU));
    }

    @Test
    public void testApplyDraftChangeToNodeWhenDraftChangeValueIsNullAndAttributeIsRelationshipMany() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "test_product");

        Map<Attribute, Object> mainNode = new HashMap<Attribute, Object>();

        DraftChange draftChange = new DraftChange();
        draftChange.setAttributeName(ContentTypes.Product.PRIMARY_HOME.getName());
        draftChange.setContentKey(productKey.toString());
        draftChange.setCreatedAt(System.currentTimeMillis());
        draftChange.setUserName("testUser");
        draftChange.setValue(null);

        Mockito.when(contentTypeInfoService.findAttributeByName(productKey.type, ContentTypes.Product.PRIMARY_HOME.getName()))
                .thenReturn(Optional.fromNullable(ContentTypes.Product.PRIMARY_HOME));

        Map<Attribute, Object> draftAppliedAttributes = underTest.applyDraftChangeToNode(draftChange, mainNode);

        Assert.assertTrue(draftAppliedAttributes.containsKey(ContentTypes.Product.PRIMARY_HOME));
        Assert.assertTrue(draftAppliedAttributes.get(ContentTypes.Product.PRIMARY_HOME) instanceof List<?>);
        Assert.assertEquals(0, ((List<ContentKey>) draftAppliedAttributes.get(ContentTypes.Product.PRIMARY_HOME)).size());
    }
}
