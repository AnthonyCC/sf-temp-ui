package com.freshdirect.cms.draft.service;

import java.util.Arrays;
import java.util.Collections;
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
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.google.common.base.Optional;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftChangeExtractorServiceTest {

    @Mock
    private ContentTypeInfoService contentTypeInfoService;

    @InjectMocks
    private DraftChangeExtractorService underTest;

    @Test
    public void testExtractChangesWithScalarAttributeChanged() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "testProd");
        Map<Attribute, Object> originalAttributes = new HashMap<Attribute, Object>();
        originalAttributes.put(ContentTypes.Product.FULL_NAME, "originalName");

        Map<Attribute, Object> changedAttributes = new HashMap<Attribute, Object>();
        changedAttributes.put(ContentTypes.Product.FULL_NAME, "changedName");

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        originalNodes.put(productKey, originalAttributes);

        Map<ContentKey, Map<Attribute, Object>> changedNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        changedNodes.put(productKey, changedAttributes);

        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "FULL_NAME")).thenReturn(Optional.fromNullable(ContentTypes.Product.FULL_NAME));

        List<DraftChange> draftChanges = underTest.extractChangesFromRequest(changedNodes, originalNodes, new DraftContext(1L, "test-draft"), "testUser");

        Assert.assertFalse(draftChanges.isEmpty());
        Assert.assertEquals(1, draftChanges.size());
        Assert.assertEquals("changedName", draftChanges.get(0).getValue());
    }

    @Test
    public void testExtractChangesWithSingleRelationshipChanged() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "testProd");
        Map<Attribute, Object> originalAttributes = new HashMap<Attribute, Object>();
        originalAttributes.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get(ContentType.Sku, "original"));

        Map<Attribute, Object> changedAttributes = new HashMap<Attribute, Object>();
        changedAttributes.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get(ContentType.Sku, "changed"));

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        originalNodes.put(productKey, originalAttributes);

        Map<ContentKey, Map<Attribute, Object>> changedNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        changedNodes.put(productKey, changedAttributes);

        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "PREFERRED_SKU")).thenReturn(Optional.fromNullable(ContentTypes.Product.PREFERRED_SKU));

        List<DraftChange> draftChanges = underTest.extractChangesFromRequest(changedNodes, originalNodes, new DraftContext(1L, "test-draft"), "testUser");

        Assert.assertFalse(draftChanges.isEmpty());
        Assert.assertEquals(1, draftChanges.size());
        Assert.assertEquals("Sku:changed", draftChanges.get(0).getValue());
    }

    @Test
    public void testExtractChangesWithMultiRelationshipChanged() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "testProd");
        Map<Attribute, Object> originalAttributes = new HashMap<Attribute, Object>();
        originalAttributes.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get(ContentType.Sku, "original"), ContentKeyFactory.get(ContentType.Sku, "original2")));

        Map<Attribute, Object> changedAttributes = new HashMap<Attribute, Object>();
        changedAttributes.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get(ContentType.Sku, "changed1"), ContentKeyFactory.get(ContentType.Sku, "changed2"),
                ContentKeyFactory.get(ContentType.Sku, "changed3")));

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        originalNodes.put(productKey, originalAttributes);

        Map<ContentKey, Map<Attribute, Object>> changedNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        changedNodes.put(productKey, changedAttributes);

        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "skus")).thenReturn(Optional.fromNullable(ContentTypes.Product.skus));

        List<DraftChange> draftChanges = underTest.extractChangesFromRequest(changedNodes, originalNodes, new DraftContext(1L, "test-draft"), "testUser");

        Assert.assertFalse(draftChanges.isEmpty());
        Assert.assertEquals(1, draftChanges.size());
        Assert.assertEquals("Sku:changed1|Sku:changed2|Sku:changed3", draftChanges.get(0).getValue());
    }

    @Test
    public void testExtractChangesWithMultipleAttributesChanged() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "testProd");
        Map<Attribute, Object> originalAttributes = new HashMap<Attribute, Object>();
        originalAttributes.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get(ContentType.Sku, "original"), ContentKeyFactory.get(ContentType.Sku, "original2")));
        originalAttributes.put(ContentTypes.Product.FULL_NAME, "originalName");
        originalAttributes.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get(ContentType.Sku, "original"));

        Map<Attribute, Object> changedAttributes = new HashMap<Attribute, Object>();
        changedAttributes.put(ContentTypes.Product.skus, Arrays.asList(ContentKeyFactory.get(ContentType.Sku, "changed1"), ContentKeyFactory.get(ContentType.Sku, "changed2"),
                ContentKeyFactory.get(ContentType.Sku, "changed3")));
        changedAttributes.put(ContentTypes.Product.FULL_NAME, "changedName");
        changedAttributes.put(ContentTypes.Product.PREFERRED_SKU, ContentKeyFactory.get(ContentType.Sku, "changed"));

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        originalNodes.put(productKey, originalAttributes);

        Map<ContentKey, Map<Attribute, Object>> changedNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        changedNodes.put(productKey, changedAttributes);

        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "skus")).thenReturn(Optional.fromNullable(ContentTypes.Product.skus));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "PREFERRED_SKU")).thenReturn(Optional.fromNullable(ContentTypes.Product.PREFERRED_SKU));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "FULL_NAME")).thenReturn(Optional.fromNullable(ContentTypes.Product.FULL_NAME));

        List<DraftChange> draftChanges = underTest.extractChangesFromRequest(changedNodes, originalNodes, new DraftContext(1L, "test-draft"), "testUser");

        Assert.assertFalse(draftChanges.isEmpty());
        Assert.assertEquals(3, draftChanges.size());

        for (DraftChange draftChange : draftChanges) {
            Assert.assertEquals(productKey.toString(), draftChange.getContentKey());
            if (draftChange.getAttributeName().equals("FULL_NAME")) {
                Assert.assertEquals("changedName", draftChange.getValue());
            } else if (draftChange.getAttributeName().equals("PREFERRED_SKU")) {
                Assert.assertEquals("Sku:changed", draftChange.getValue());
            } else if (draftChange.getAttributeName().equals("skus")) {
                Assert.assertEquals("Sku:changed1|Sku:changed2|Sku:changed3", draftChange.getValue());
            } else {
                Assert.fail("Invalid draft change!");
            }
        }
    }

    @Test
    public void testExtractChangesWithNullValues() {
        ContentKey productKey = ContentKeyFactory.get(ContentType.Product, "testProduct");
        Map<Attribute, Object> originalAttributes = new HashMap<Attribute, Object>();
        originalAttributes.put(ContentTypes.Product.SEASON_TEXT, "originalSeasonText");
        originalAttributes.put(ContentTypes.Product.SELL_BY_SALESUNIT, "QUANTITY");
        originalAttributes.put(ContentTypes.Product.EXCLUDED_EBT_PAYMENT, Boolean.TRUE);
        originalAttributes.put(ContentTypes.Product.PROD_DESCR, ContentKeyFactory.get(ContentType.Html, "test-media"));
        originalAttributes.put(ContentTypes.Product.skus,
                Arrays.asList(ContentKeyFactory.get(ContentType.Sku, "original-sku"), ContentKeyFactory.get(ContentType.Sku, "original-sku2")));
        originalAttributes.put(ContentTypes.Product.brands,
                Arrays.asList(ContentKeyFactory.get(ContentType.Brand, "original-brand"), ContentKeyFactory.get(ContentType.Brand, "original-brand2")));

        Map<Attribute, Object> changedAttributes = new HashMap<Attribute, Object>();
        changedAttributes.put(ContentTypes.Product.SEASON_TEXT, null);
        changedAttributes.put(ContentTypes.Product.SELL_BY_SALESUNIT, null);
        changedAttributes.put(ContentTypes.Product.EXCLUDED_EBT_PAYMENT, null);
        changedAttributes.put(ContentTypes.Product.PROD_DESCR, null);
        changedAttributes.put(ContentTypes.Product.skus, null);
        changedAttributes.put(ContentTypes.Product.brands, Collections.emptyList());

        Map<ContentKey, Map<Attribute, Object>> originalNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        originalNodes.put(productKey, originalAttributes);

        Map<ContentKey, Map<Attribute, Object>> changedNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        changedNodes.put(productKey, changedAttributes);

        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "SEASON_TEXT"))
            .thenReturn(Optional.fromNullable(ContentTypes.Product.SEASON_TEXT));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "SELL_BY_SALESUNIT"))
            .thenReturn(Optional.fromNullable(ContentTypes.Product.SELL_BY_SALESUNIT));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "EXCLUDED_EBT_PAYMENT"))
            .thenReturn(Optional.fromNullable(ContentTypes.Product.EXCLUDED_EBT_PAYMENT));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "PROD_DESCR"))
            .thenReturn(Optional.fromNullable(ContentTypes.Product.PROD_DESCR));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "skus"))
            .thenReturn(Optional.fromNullable(ContentTypes.Product.skus));
        Mockito.when(contentTypeInfoService.findAttributeByName(ContentType.Product, "brands"))
            .thenReturn(Optional.fromNullable(ContentTypes.Product.brands));

        List<DraftChange> draftChanges = underTest.extractChangesFromRequest(changedNodes, originalNodes, new DraftContext(1L, "test-draft"), "testUser");

        Assert.assertEquals(6, draftChanges.size());
        for (DraftChange draftChange : draftChanges) {
            Assert.assertEquals(productKey.toString(), draftChange.getContentKey());
            Assert.assertEquals(null, draftChange.getValue());
        }
    }
}
