package com.freshdirect.fdstore.customer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;

import com.freshdirect.Fixture;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.content.SkuModel;

public class OrderLineUtilTest extends Fixture {

    private static final String SKU_CODE = "skuCode";
    private static final String SALES_UNIT_NAME = "salesUnit";
    private static final String VARIATION_NAME = "variationName";
    private static final String VARIATION_OPTION_NAME = "variationOptionName";
    private static final String PRODUCT_MODEL_FULL_NAME = "productModelFullName";
    private static final String DEPARTMENT_MODEL_FULL_NAME = "departmentModelFullName";

    private FDSku fdSku;
    private SkuModel skuModel;
    private FDSalesUnit salesUnit;
    private FDVariation variation;
    private FDProduct fdProductModel;
    private ProductModel productModel;
    private FDConfigurableI configuration;
    private DepartmentModel departmentModel;
    private ProductReference productReference;
    private FDVariationOption variationOption;
    private FDProductSelectionI productSelection;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fdSku = context.mock(FDSku.class, "sku");
        skuModel = context.mock(SkuModel.class, "skuModel");
        variation = context.mock(FDVariation.class, "variation");
        salesUnit = context.mock(FDSalesUnit.class, "salesUnit");
        productModel = context.mock(ProductModel.class, "productModel");
        fdProductModel = context.mock(FDProduct.class, "fdProductModel");
        configuration = context.mock(FDConfigurableI.class, "configurable");
        departmentModel = context.mock(DepartmentModel.class, "departmentModel");
        variationOption = context.mock(FDVariationOption.class, "variationOption");
        productReference = context.mock(ProductReference.class, "productReference");
        productSelection = context.mock(FDProductSelectionI.class, "productSelectionLine");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        context.assertIsSatisfied();
    }

    public void testDescribeCanNotLookupProduct() {
        expectingMissingDescribeProperties();

        try {
            OrderLineUtil.describe(productSelection);
            fail();
        } catch (FDInvalidConfigurationException e) {
            assertEquals("Can't find product in store for selection " + productReference.toString(), e.getMessage());
        }
    }

    public void testDescribeCanNotLookupFdProduct() {
        expectingDescribeProperties();
        expectingMisssingModels();

        try {
            OrderLineUtil.describe(productSelection);
            fail();
        } catch (FDInvalidConfigurationException e) {
            assertEquals("Sku not found", e.getMessage());
        }
    }

    public void testDescribeConfigurationDescriptonIsEmpty() throws FDInvalidConfigurationException {
        expectingDescribeProperties();
        expectingModels();
        expectingMissingSalesUnitDescription();
        expectingMissingSkuVariantDescription();
        expectingMissingVariationOptions(EnumProductLayout.PERISHABLE);

        assertSetConfigurationDescription("");

        OrderLineUtil.describe(productSelection);
    }

    public void testDescribeConfigurationDescriptonContainsVariationOptionsIsEmpty() throws FDInvalidConfigurationException {
        Map<String, String> configurationOptions = Collections.emptyMap();

        expectingDescribeProperties();
        expectingModels();
        expectingMissingSalesUnitDescription();
        expectingMissingSkuVariantDescription();
        expectingConfigurationOptions(configurationOptions, EnumProductLayout.PERISHABLE);

        assertSetConfigurationDescription("");

        OrderLineUtil.describe(productSelection);
    }

    public void testDescribeConfigurationDescriptonContainsVariationOptionsButOptionNameNotMatch() throws FDInvalidConfigurationException {
        Map<String, String> configurationOptions = new HashMap<String, String>();
        configurationOptions.put(VARIATION_NAME, "");

        expectingDescribeProperties();
        expectingModels();
        expectingMissingSalesUnitDescription();
        expectingMissingSkuVariantDescription();
        expectingConfigurationOptions(configurationOptions, EnumProductLayout.PERISHABLE);

        context.checking(new Expectations() {

            {
                oneOf(variation).getVariationOptions();
                will(returnValue(new FDVariationOption[] { variationOption }));

                oneOf(variationOption).getName();
                will(returnValue(VARIATION_OPTION_NAME));
            }
        });

        assertSetConfigurationDescription("");

        OrderLineUtil.describe(productSelection);
    }

    public void testDescribeConfigurationDescriptonContainsVariationOptionsAndProductLayoutNotHolidayMealBundleAndNormalDescription() throws FDInvalidConfigurationException {
        final String variantOptionDescription = "variantOptionDescription";
        Map<String, String> configurationOptions = new HashMap<String, String>();
        configurationOptions.put(VARIATION_NAME, VARIATION_OPTION_NAME);

        expectingDescribeProperties();
        expectingModels();
        expectingMissingSalesUnitDescription();
        expectingMissingSkuVariantDescription();
        expectingConfigurationOptions(configurationOptions, EnumProductLayout.PERISHABLE);
        expectingVariantOptionsWithSkuCode(variantOptionDescription);

        assertSetConfigurationDescription(variantOptionDescription);

        OrderLineUtil.describe(productSelection);
    }

    private void expectingMissingDescribeProperties() {
        context.checking(new Expectations() {

            {
                oneOf(productSelection).lookupProduct();
                will(returnValue(null));

                oneOf(productSelection).getProductRef();
                will(returnValue(productReference));
            }
        });
    }

    private void expectingDescribeProperties() {
        context.checking(new Expectations() {

            {
                oneOf(productSelection).lookupProduct();
                will(returnValue(productModel));

                oneOf(productModel).getFullName();
                will(returnValue(PRODUCT_MODEL_FULL_NAME));

                oneOf(productSelection).setDescription(PRODUCT_MODEL_FULL_NAME);

                oneOf(productSelection).getRecipeSourceId();
                will(returnValue(null));

                oneOf(productSelection).getExternalGroup();
                will(returnValue(null));

                oneOf(productModel).getDepartment();
                will(returnValue(departmentModel));

                oneOf(departmentModel).getFullName();
                will(returnValue(DEPARTMENT_MODEL_FULL_NAME));

                oneOf(productSelection).setDepartmentDesc(DEPARTMENT_MODEL_FULL_NAME);
            }
        });
    }

    private void expectingMisssingModels() {
        context.checking(new Expectations() {

            {
                oneOf(productSelection).lookupFDProduct();
                will(returnValue(null));

                oneOf(productSelection).lookupProduct();
                will(returnValue(productModel));

                oneOf(productSelection).getSkuCode();
                will(returnValue(SKU_CODE));

                oneOf(productModel).getSku(SKU_CODE);
                will(returnValue(skuModel));

                oneOf(productSelection).getSku();
                will(returnValue(fdSku));
            }
        });
    }

    private void expectingModels() {
        context.checking(new Expectations() {

            {
                oneOf(productSelection).lookupFDProduct();
                will(returnValue(fdProductModel));

                oneOf(productSelection).lookupProduct();
                will(returnValue(productModel));

                oneOf(productSelection).getSkuCode();
                will(returnValue(SKU_CODE));

                oneOf(productModel).getSku(SKU_CODE);
                will(returnValue(skuModel));
            }
        });
    }

    private void expectingMissingSalesUnitDescription() {
        context.checking(new Expectations() {

            {
                oneOf(productSelection).getConfiguration();
                will(returnValue(configuration));

                oneOf(configuration).getSalesUnit();
                will(returnValue(SALES_UNIT_NAME));

                oneOf(fdProductModel).getSalesUnit(SALES_UNIT_NAME);
                will(returnValue(salesUnit));

                oneOf(salesUnit).getDescription();
                will(returnValue(null));
            }
        });
    }

    private void expectingMissingSkuVariantDescription() {
        context.checking(new Expectations() {

            {
                oneOf(skuModel).getVariationMatrix();
                will(returnValue(Collections.emptyList()));

                oneOf(skuModel).getVariationOptions();
                will(returnValue(Collections.emptyList()));
            }
        });
    }

    private void expectingMissingVariationOptions(final EnumProductLayout productLayout) {
        context.checking(new Expectations() {

            {
                oneOf(productModel).getSpecialLayout();
                will(returnValue(productLayout));

                oneOf(fdProductModel).getVariations();
                will(returnValue(new FDVariation[] {}));
            }
        });
    }

    private void expectingConfigurationOptions(final Map<String, String> configurationOptions, final EnumProductLayout productLayout) {
        context.checking(new Expectations() {

            {
                oneOf(productModel).getSpecialLayout();
                will(returnValue(productLayout));

                oneOf(fdProductModel).getVariations();
                will(returnValue(new FDVariation[] { variation }));

                oneOf(productSelection).getConfiguration();
                will(returnValue(configuration));

                oneOf(variation).getName();
                will(returnValue(VARIATION_NAME));

                oneOf(configuration).getOptions();
                will(returnValue(configurationOptions));
            }
        });
    }

    private void expectingVariantOptionsWithSkuCode(final String variantOptionDescription) {
        context.checking(new Expectations() {

            {
                oneOf(variation).getVariationOptions();
                will(returnValue(new FDVariationOption[] { variationOption }));

                oneOf(variationOption).getName();
                will(returnValue(VARIATION_OPTION_NAME));

                oneOf(variationOption).getDescription();
                will(returnValue(variantOptionDescription));
            }
        });
    }

    private void assertSetConfigurationDescription(final String configurationDescription) {
        context.checking(new Expectations() {

            {
                oneOf(productSelection).setConfigurationDesc(configurationDescription);
            }
        });

    }

}
