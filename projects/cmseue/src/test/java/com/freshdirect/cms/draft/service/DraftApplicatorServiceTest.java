package com.freshdirect.cms.draft.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.draft.domain.DraftChange;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftApplicatorServiceTest {

    @Mock
    private DraftChangeToContentNodeApplicator draftChangeToContentNodeApplicator;

    @InjectMocks
    private DraftApplicatorService underTest;

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

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        Map<Attribute, Object> nodeOnMain = new HashMap<Attribute, Object>();
        nodeOnMain.put(ContentTypes.Product.FULL_NAME, "value_on_main");
        nodeOnMain.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredSkuOnMain"));
        nodeOnMain.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")));

        originalNodes.put(contentKey, nodeOnMain);

        Mockito.when(draftChangeToContentNodeApplicator.applyDraftChangeToNode(Mockito.eq(draftChangeScalar), Mockito.anyMap())).thenAnswer(new Answer<Map<Attribute, Object>>() {

            @Override
            public Map<Attribute, Object> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Map<Attribute, Object> parameterMap = (Map<Attribute, Object>) args[1];
                parameterMap.put(ContentTypes.Product.FULL_NAME, "Draft_node");
                return parameterMap;
            }
        });

        Mockito.when(draftChangeToContentNodeApplicator.applyDraftChangeToNode(Mockito.eq(draftChangeSingeRelationship), Mockito.anyMap()))
                .thenAnswer(new Answer<Map<Attribute, Object>>() {

                    @Override
                    public Map<Attribute, Object> answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        Map<Attribute, Object> parameterMap = (Map<Attribute, Object>) args[1];
                        parameterMap.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredsku"));
                        return parameterMap;
                    }
                });

        Mockito.when(draftChangeToContentNodeApplicator.applyDraftChangeToNode(Mockito.eq(draftChangeMultiRelationship), Mockito.anyMap()))
                .thenAnswer(new Answer<Map<Attribute, Object>>() {

                    @Override
                    public Map<Attribute, Object> answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        Map<Attribute, Object> parameterMap = (Map<Attribute, Object>) args[1];
                        parameterMap.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:preferredsku"), ContentKeyFactory.get("Sku:jabba"),
                                ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:greenLantern"), ContentKeyFactory.get("Sku:INeedACoffee")));
                        return parameterMap;
                    }
                });

        Map<ContentKey, Map<Attribute, Object>> draftDecoratedNodes = underTest
                .applyDraftChangesToContentNodes(Arrays.asList(draftChangeScalar, draftChangeSingeRelationship, draftChangeMultiRelationship), originalNodes);

        Assert.assertFalse(draftDecoratedNodes.isEmpty());
        Assert.assertEquals(1, draftDecoratedNodes.size());
        Assert.assertTrue(draftDecoratedNodes.containsKey(contentKey));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.skus));

        Assert.assertEquals(ContentKeyFactory.get("Sku:preferredsku"), draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals("Draft_node", draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(Arrays.asList(ContentKeyFactory.get("Sku:preferredsku"), ContentKeyFactory.get("Sku:jabba"), ContentKeyFactory.get("Sku:batman"),
                ContentKeyFactory.get("Sku:greenLantern"), ContentKeyFactory.get("Sku:INeedACoffee")), draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.skus));
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

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        Map<Attribute, Object> nodeOnMain = new HashMap<Attribute, Object>();
        nodeOnMain.put(ContentTypes.Product.FULL_NAME, "value_on_main");
        nodeOnMain.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredSkuOnMain"));
        nodeOnMain.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")));

        originalNodes.put(contentKey, nodeOnMain);

        Mockito.when(draftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChangeScalar, nodeOnMain)).thenReturn(new HashMap<Attribute, Object>(nodeOnMain) {

            {
                put(ContentTypes.Product.FULL_NAME, "Draft_node");
            }
        });

        Map<ContentKey, Map<Attribute, Object>> draftDecoratedNodes = underTest.applyDraftChangesToContentNodes(Arrays.asList(draftChangeScalar), originalNodes);

        Assert.assertFalse(draftDecoratedNodes.isEmpty());
        Assert.assertTrue(draftDecoratedNodes.containsKey(contentKey));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.skus));

        Assert.assertEquals("Draft_node", draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(ContentKeyFactory.get("Sku:preferredSkuOnMain"), draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals(Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")),
                draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.skus));
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

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        Map<Attribute, Object> nodeOnMain = new HashMap<Attribute, Object>();
        nodeOnMain.put(ContentTypes.Product.FULL_NAME, "value_on_main");
        nodeOnMain.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredSkuOnMain"));
        nodeOnMain.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")));

        originalNodes.put(contentKey, nodeOnMain);

        Mockito.when(draftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChangeSingeRelationship, nodeOnMain)).thenReturn(new HashMap<Attribute, Object>(nodeOnMain) {

            {
                put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredsku"));
            }
        });

        Map<ContentKey, Map<Attribute, Object>> draftDecoratedNodes = underTest.applyDraftChangesToContentNodes(Arrays.asList(draftChangeSingeRelationship), originalNodes);

        Assert.assertFalse(draftDecoratedNodes.isEmpty());
        Assert.assertTrue(draftDecoratedNodes.containsKey(contentKey));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.skus));

        Assert.assertEquals("value_on_main", draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(ContentKeyFactory.get("Sku:preferredsku"), draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals(Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")),
                draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.skus));

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

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        Map<Attribute, Object> nodeOnMain = new HashMap<Attribute, Object>();
        nodeOnMain.put(ContentTypes.Product.FULL_NAME, "value_on_main");
        nodeOnMain.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get("Sku:preferredSkuOnMain"));
        nodeOnMain.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:batman"), ContentKeyFactory.get("Sku:jabba")));

        originalNodes.put(contentKey, nodeOnMain);

        Mockito.when(draftChangeToContentNodeApplicator.applyDraftChangeToNode(draftChangeMultiRelationship, nodeOnMain)).thenReturn(new HashMap<Attribute, Object>(nodeOnMain) {

            {
                put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get("Sku:preferredsku"), ContentKeyFactory.get("Sku:jabba"), ContentKeyFactory.get("Sku:batman"),
                        ContentKeyFactory.get("Sku:greenLantern"), ContentKeyFactory.get("Sku:INeedACoffee")));
            }
        });

        Map<ContentKey, Map<Attribute, Object>> draftDecoratedNodes = underTest.applyDraftChangesToContentNodes(Arrays.asList(draftChangeMultiRelationship), originalNodes);

        Assert.assertFalse(draftDecoratedNodes.isEmpty());
        Assert.assertTrue(draftDecoratedNodes.containsKey(contentKey));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.FULL_NAME));
        Assert.assertTrue(draftDecoratedNodes.get(contentKey).containsKey(ContentTypes.Product.skus));

        Assert.assertEquals("value_on_main", draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.FULL_NAME));
        Assert.assertEquals(ContentKeyFactory.get("Sku:preferredSkuOnMain"), draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.PREFERRED_SKU));
        Assert.assertEquals(Arrays.asList(ContentKeyFactory.get("Sku:preferredsku"), ContentKeyFactory.get("Sku:jabba"), ContentKeyFactory.get("Sku:batman"),
                ContentKeyFactory.get("Sku:greenLantern"), ContentKeyFactory.get("Sku:INeedACoffee")), draftDecoratedNodes.get(contentKey).get(ContentTypes.Product.skus));
    }
}
