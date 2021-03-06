package com.freshdirect.webapp.ajax.cart;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.content.attributes.AttributeCollection;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;

@RunWith(MockitoJUnitRunner.class)
public class CartOperationsTest {

    @Mock
    private ProductModel productModel;

    @Test
    public void testValidateConfigurationWithNullInput() {
        // check validateConfiguration with no-value parameters
        String testOutput = CartOperations.validateConfiguration(null, null);
        Assert.assertNull("Validation result must be null if ATC input is null", testOutput);
    }

    @Test
    public void testValidateConfigurationWithNullFDProduct() {
        try {
            CartOperations.validateConfiguration(null, new AddToCartItem());
            Assert.fail("validateConfiguration must crash if no FDProduct parameter is supplied");
        } catch (NullPointerException expectedException) {
            // method is expected to crash when FDProduct input is not specified
        }
    }

    @Test
    public void testValidateConfigurationWithNullVariation() {
        FDVariation emptyVariations[] = null;
        FDProduct testFDProduct = prepareTestProduct(emptyVariations);

        String testOutput = CartOperations.validateConfiguration(testFDProduct, null);
        Assert.assertNull("Resulted value must be null if ATC input is null", testOutput);
    }

    @Test
    public void testValidateConfigurationWithEmptyInputs() {
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] {});
        AddToCartItem atcItem = new AddToCartItem();

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);
        Assert.assertNotNull(atcItem.getConfiguration());
        Assert.assertTrue(atcItem.getConfiguration().isEmpty());
    }

    @Test
    public void testValidateConfigurationWithSingleVariationAndNoUserConfig() {
        FDVariation singleOption = FDVariationFactory.createVariationWithSingleOption("variation1", "option1");
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { singleOption });

        AddToCartItem atcItem = new AddToCartItem();

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);

        // test configuration size
        Assert.assertNotNull(atcItem.getConfiguration());
        Assert.assertEquals(1, atcItem.getConfiguration().size());

        // test if configuration has 'variation1' entry
        Assert.assertTrue(atcItem.getConfiguration().containsKey("variation1"));
    }

    @Test
    public void testValidateConfigurationWithSingleVariationAndBadUserConfig() {
        FDVariation singleOption = FDVariationFactory.createVariationWithSingleOption("variation1", "option1");
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { singleOption });

        AddToCartItem atcItem = new AddToCartItem();

        atcItem.getConfiguration().put("variation1", "otherOption");

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);

        // test configuration
        Assert.assertNotNull(atcItem.getConfiguration());
        Assert.assertEquals(1, atcItem.getConfiguration().size());

        // test if configuration has 'variation1' entry with the valid configuration option
        Assert.assertTrue(atcItem.getConfiguration().containsKey("variation1"));
        Assert.assertEquals("option1", atcItem.getConfiguration().get("variation1"));
    }

    @Test
    public void testValidateConfigurationWithSingleVariationAndAndExtraUserConfig() {
        FDVariation singleOption = FDVariationFactory.createVariationWithSingleOption("variation1", "option1");
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { singleOption });

        AddToCartItem atcItem = new AddToCartItem();
        atcItem.getConfiguration().put("extraVariation", "whateverOption");

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);

        // test configuration size
        Assert.assertNotNull(atcItem.getConfiguration());
        // FIXME legacy issue! Extra configuration option remains
        // in the configuration map
        // Expected: extra configuration entries should be removed!
        Assert.assertEquals(1, atcItem.getConfiguration().size());

        // test if configuration has 'variation1' entry
        Assert.assertTrue(atcItem.getConfiguration().containsKey("variation1"));
    }

    @Test
    public void testValidateConfigurationWithOptionalVariationHavingOptionNotSelected() {
        // REGRESSION, optional variation without selected entry sets 'null' configuration option
        FDVariation variationWithoutSelectedOption = FDVariationFactory.createOptionalVariation("variation1", Arrays.asList("option1", "option2"), null);
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { variationWithoutSelectedOption });

        AddToCartItem atcItem = new AddToCartItem();

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);

        // Expected: validateConfiguration method won't make any change
        // When optional variation has no selected option
        Assert.assertNotNull(atcItem.getConfiguration());
        Assert.assertEquals(1, atcItem.getConfiguration().size());
        Assert.assertEquals(null, atcItem.getConfiguration().get("variation1"));
    }

    @Test
    public void testValidateConfigurationWithEmptyOptionalVariation() {
        FDVariation variationWithoutOption = FDVariationFactory.createOptionalVariation("variation1", Collections.<String> emptyList(), null);
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { variationWithoutOption });

        AddToCartItem atcItem = new AddToCartItem();

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);

        // Expected: validateConfiguration method won't make any change
        // When optional variation contains no options at all
        Assert.assertNotNull(atcItem.getConfiguration());
        Assert.assertEquals(1, atcItem.getConfiguration().size());
        Assert.assertEquals(null, atcItem.getConfiguration().get("variation1"));

    }

    @Test
    public void testValidateConfigurationWithOptionalVariationHavingFirstOptionSelected() {
        FDVariation variationHavingFirstOptionSelected = FDVariationFactory.createOptionalVariation("variation1", Arrays.asList("option1", "option2"), "option1");
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { variationHavingFirstOptionSelected });

        AddToCartItem atcItem = new AddToCartItem();

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);

        // Expected: validateConfiguration selects 'option1'
        Assert.assertNotNull(atcItem.getConfiguration());
        Assert.assertEquals(1, atcItem.getConfiguration().size());
        Assert.assertEquals("option1", atcItem.getConfiguration().get("variation1"));
    }

    @Test
    public void testValidateConfigurationWithOptionalVariationHavingSecondOptionSelected() {
        FDVariation variationHavingSecondOptionSelected = FDVariationFactory.createOptionalVariation("variation1", Arrays.asList("option1", "option2"), "option2");
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { variationHavingSecondOptionSelected });

        AddToCartItem atcItem = new AddToCartItem();

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);

        // Expected: validateConfiguration selects 'option2'
        Assert.assertNotNull(atcItem.getConfiguration());
        Assert.assertEquals(1, atcItem.getConfiguration().size());
        Assert.assertEquals("option2", atcItem.getConfiguration().get("variation1"));
    }

    @Test
    public void testValidateConfigurationWithOptionalVariationAndExtraUserConfig() {
        FDVariation variationHavingFirstOptionSelected = FDVariationFactory.createOptionalVariation("variation1", Arrays.asList("option1", "option2"), "option1");
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { variationHavingFirstOptionSelected });

        AddToCartItem atcItem = new AddToCartItem();
        atcItem.getConfiguration().put("extraVariation", "whateverOption");

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);

        // Expected: validateConfiguration selects 'option1'
        Assert.assertNotNull(atcItem.getConfiguration());
        // FIXME legacy issue! Extra configuration option remains
        // in the configuration map
        // Expected: extra configuration entries should be removed!
        Assert.assertEquals(1, atcItem.getConfiguration().size());
        Assert.assertEquals("option1", atcItem.getConfiguration().get("variation1"));
    }

    @Test
    public void testValidateConfigurationWithEmptyRequiredVariation() {
        FDVariation emptyVariation = FDVariationFactory.createRequiredVariation("emptyVariation", Collections.<String> emptyList());
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { emptyVariation });

        AddToCartItem atcItem = new AddToCartItem();

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertEquals("Please select emptyVariation", result);

        atcItem.getConfiguration().put("emptyVariation", "whateverOption");
        result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertEquals("Please select emptyVariation", result);
    }

    @Test
    public void testValidateConfigurationWithRequiredVariationAndNoUserConfig() {
        FDVariation requiredVariation = FDVariationFactory.createRequiredVariation("requiredVariation", Arrays.asList("option1", "option2"));
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { requiredVariation });

        AddToCartItem atcItem = new AddToCartItem();
        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);

        // FIXME: legacy issue! configureValidator does not care
        // about not configured required variation
        // Expected: it should complain about missing configuration
        Assert.assertEquals("Please select requiredVariation", result);
    }

    @Test
    public void testValidateConfigurationWithRequiredVariationAndBadUserConfig() {
        FDVariation requiredVariation = FDVariationFactory.createRequiredVariation("requiredVariation", Arrays.asList("option1", "option2"));
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { requiredVariation });

        AddToCartItem atcItem = new AddToCartItem();
        atcItem.getConfiguration().put("extraVariation", "whateverOption");

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);

        // FIXME: legacy issue! configureValidator falsely accepts
        // bad or expired options
        // Expected: it should complain about bad configuration
        Assert.assertEquals("Please select requiredVariation", result);
    }

    @Test
    public void testValidateConfigurationWithRequiredVariationAndRightUserConfig() {
        FDVariation requiredVariation = FDVariationFactory.createRequiredVariation("requiredVariation", Arrays.asList("option1"));
        FDProduct testFDProduct = prepareTestProduct(new FDVariation[] { requiredVariation });

        AddToCartItem atcItem = new AddToCartItem();
        atcItem.getConfiguration().put("requiredVariation", "option1");

        String result = CartOperations.validateConfiguration(testFDProduct, atcItem);
        Assert.assertNull("Unexpected error message", result);
    }

    @Test
    public void groupProductWhenProductDoesNotSoldSoldBySalesUnitAndPricedByEA() {
        Mockito.when(productModel.isSoldBySalesUnits()).thenReturn(false);
        boolean result = CartOperations.isProductGroupable(productModel, prepareSalesUnit("EA"));
        Assert.assertTrue(result);
    }

    @Test
    public void groupProductWhenProductDoesNotSoldSoldBySalesUnitAndPricedByCS() {
        Mockito.when(productModel.isSoldBySalesUnits()).thenReturn(false);
        boolean result = CartOperations.isProductGroupable(productModel, prepareSalesUnit("CS"));
        Assert.assertTrue(result);
    }

    @Test
    public void groupProductWhenProductDoesNotSoldSoldBySalesUnitAndPricedByPAK() {
        Mockito.when(productModel.isSoldBySalesUnits()).thenReturn(false);
        boolean result = CartOperations.isProductGroupable(productModel, prepareSalesUnit("PAK"));
        Assert.assertTrue(result);
    }

    @Test
    public void doNotGroupProductWhenProductSoldBySalesUnits() {
        Mockito.when(productModel.isSoldBySalesUnits()).thenReturn(true);
        boolean result = CartOperations.isProductGroupable(productModel, prepareSalesUnit("EA"));
        Assert.assertFalse(result);
    }

    @Test
    public void doNotGroupProductWhenProductSoldByLB() {
        Mockito.when(productModel.isSoldBySalesUnits()).thenReturn(false);
        boolean result = CartOperations.isProductGroupable(productModel, prepareSalesUnit("LB"));
        Assert.assertFalse(result);
    }

    @Test
    public void doNotGroupProductWhenSalesUnitIsNull() {
        boolean result = CartOperations.isProductGroupable(productModel, null);
        Assert.assertFalse(result);
    }

    private FDSalesUnit prepareSalesUnit(String name) {
        return new FDSalesUnit(null, name, "description", 0, 0, "baseUnit");
    }

    private FDProduct prepareTestProduct(FDVariation variations[]) {
        return new FDProduct("TST00001", 1, null, null, variations, null, null, null, null, null, null);
    }

    private static class FDVariationFactory {

        public static FDVariation createVariationWithSingleOption(String varName, String optionName) {

            AttributeCollection optionAttributes = new AttributeCollection();
            optionAttributes.setAttribute(optionName, optionName);

            FDVariationOption option = new FDVariationOption(optionAttributes, optionName, "test desc");

            AttributeCollection variationAttributes = new AttributeCollection();
            return new FDVariation(variationAttributes, varName, new FDVariationOption[] { option });
        }

        public static FDVariation createOptionalVariation(String varName, List<String> optionNames, String selectedOptionName) {

            FDVariationOption options[] = new FDVariationOption[optionNames.size()];
            int k = 0;
            for (String optionName : optionNames) {
                AttributeCollection optionAttributes = new AttributeCollection();
                if (selectedOptionName != null && selectedOptionName.equals(optionName)) {
                    optionAttributes.setAttribute(EnumAttributeName.SELECTED.toString(), true);
                }
                FDVariationOption option = new FDVariationOption(optionAttributes, optionName, "test desc");
                options[k] = option;
                k++;
            }

            AttributeCollection variationAttributes = new AttributeCollection();
            variationAttributes.setAttribute(EnumAttributeName.OPTIONAL.toString(), true);

            return new FDVariation(variationAttributes, varName, options);
        }

        public static FDVariation createRequiredVariation(String varName, List<String> optionNames) {

            FDVariationOption options[] = new FDVariationOption[optionNames.size()];
            int k = 0;
            for (String optionName : optionNames) {
                AttributeCollection optionAttributes = new AttributeCollection();
                /*
                 * if (selectedOptionName != null && selectedOptionName.equals(optionName)) { optionAttributes.setAttribute(EnumAttributeName.SELECTED.toString(), true); }
                 */
                FDVariationOption option = new FDVariationOption(optionAttributes, optionName, "test desc");
                options[k] = option;
                k++;
            }

            AttributeCollection variationAttributes = new AttributeCollection();
            // variationAttributes.setAttribute(EnumAttributeName.OPTIONAL.toString(), true);

            return new FDVariation(variationAttributes, varName, options);
        }
    }

}
