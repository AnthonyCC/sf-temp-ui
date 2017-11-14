package com.freshdirect.cms.contentvalidation.validator;

import java.util.Collections;
import java.util.HashMap;
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
import com.freshdirect.cms.core.domain.ContentTypes.ConfiguredProduct;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.persistence.service.ERPSDataService;
import com.freshdirect.cms.validation.ValidationResults;
import com.google.common.collect.ImmutableSet;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class ConfiguredProductValidatorTest {

    @InjectMocks
    private ConfiguredProductValidator underTest;

    @Mock
    private ERPSDataService erpsDataService;

    @Mock
    private ContentProviderService contentProvider;

    private ContentKey keyUnderTest;
    private ContentKey skuOfUnderTest;
    private Map<Attribute, Object> attributesWithValuesForKeyUnderTest;
    private String materialId;
    private Map<String, Map<String, String>> materialCharacteristics;
    private Map<String, String> characteristics;
    private Map<String, String> salesUnits;
    private ContentKey categoryParent;

    @Before
    public void setUpDataWithoutMocking() {
        keyUnderTest = ContentKeyFactory.get(ContentType.ConfiguredProduct, "underTest");
        skuOfUnderTest = ContentKeyFactory.get(ContentType.Sku, "sku");
        materialId = "materialId";

        attributesWithValuesForKeyUnderTest = new HashMap<Attribute, Object>();
        attributesWithValuesForKeyUnderTest.put(ConfiguredProduct.SKU, skuOfUnderTest);
        attributesWithValuesForKeyUnderTest.put(ConfiguredProduct.SALES_UNIT, "ea");
        attributesWithValuesForKeyUnderTest.put(ConfiguredProduct.OPTIONS, materialId + "=characteristicsKey");

        materialCharacteristics = new HashMap<String, Map<String, String>>();
        characteristics = new HashMap<String, String>();
        characteristics.put("characteristicsKey", "characteristicsValue");
        materialCharacteristics.put(materialId, characteristics);

        salesUnits = new HashMap<String, String>();
        salesUnits.put("ea", "each");
        salesUnits.put("lb", "lamb");

        categoryParent = ContentKeyFactory.get(ContentType.Category, "parent1");

    }

    @Test
    public void testWhenEverythingIsOkay() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent));

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertFalse(validationResults.hasError());
    }

    @Test
    public void testWhenNoSkuFound() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.FALSE);

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());

    }

    @Test
    public void testWhenNoSkuFoundForConfiguredProduct() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent));

        attributesWithValuesForKeyUnderTest.remove(ConfiguredProduct.SKU);

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testWhenNoMaterialFound() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenThrow(IllegalArgumentException.class);

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @Test
    public void testWhenNoCharacteristicsFound() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(null);

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @Test
    public void testWhenNoCharacteristicsValueFound() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent));

        attributesWithValuesForKeyUnderTest.remove(ConfiguredProduct.OPTIONS);

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @Test
    public void testWhenInvalidCharacteristicsValueFound() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent));

        attributesWithValuesForKeyUnderTest.put(ConfiguredProduct.OPTIONS, materialId + "=salesUnit");

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @Test
    public void testWhenNoSalesUnitFoundForSku() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(Collections.<String, String>emptyMap());
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent));

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @Test
    public void testWhenNoSalesUnitConfigured() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent));

        attributesWithValuesForKeyUnderTest.remove(ConfiguredProduct.SALES_UNIT);

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @Test
    public void testWhenInvalidSalesUnitFound() {
        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent));

        attributesWithValuesForKeyUnderTest.put(ConfiguredProduct.SALES_UNIT, "darab");

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @Test
    public void testWhenMultipleParentsFound() {

        final ContentKey notAllowedParent = ContentKeyFactory.get(ContentType.FDFolder, "fdFolder");

        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent, notAllowedParent));

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertTrue(validationResults.hasError());
    }

    @Test
    public void testWhenMultipleCategoryParentsFound() {
        final ContentKey alsoAllowedParent = ContentKeyFactory.get(ContentType.Category, "category2");

        Mockito.when(contentProvider.containsContentKey(skuOfUnderTest)).thenReturn(Boolean.TRUE);
        Mockito.when(erpsDataService.fetchSalesUnitsBySku(skuOfUnderTest)).thenReturn(salesUnits);
        Mockito.when(erpsDataService.fetchMaterialsBySku(skuOfUnderTest)).thenReturn(materialId);
        Mockito.when(erpsDataService.fetchMaterialCharacteristicsBySku(skuOfUnderTest)).thenReturn(materialCharacteristics);
        Mockito.when(contentProvider.getParentKeys(keyUnderTest)).thenReturn(ImmutableSet.<ContentKey>of(categoryParent, alsoAllowedParent));

        ValidationResults validationResults = underTest.validate(keyUnderTest, attributesWithValuesForKeyUnderTest, contentProvider);
        Assert.assertFalse(validationResults.hasError());
    }
}
