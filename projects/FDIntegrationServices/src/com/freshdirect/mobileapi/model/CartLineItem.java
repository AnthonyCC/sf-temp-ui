package com.freshdirect.mobileapi.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CartLineItem {

    private final static Category LOGGER = LoggerFactory.getInstance(CartLineItem.class);

    private FDCartLineModel fdCartline;

    public static CartLineItem wrap(FDCartLineModel fdCartLine) {
        CartLineItem cartLineItem = new CartLineItem();
        cartLineItem.fdCartline = fdCartLine;
        return cartLineItem;
    }

    private CartLineItem() {
    }

    public FDCartLineModel getFdCartline() {
        return fdCartline;
    }

    /**
     * @param skuCode
     * @param quantity
     * @param salesUnit
     * @param variantId
     * @param userSelectVariations
     */
    public CartLineItem(String skuCode, double quantity, String salesUnit, String variantId, Map<String, String> userSelectVariations) {
        // Get FDSku object
        FDProduct product = null;
        try {
            product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
        } catch (FDResourceException fdre) {
            LOGGER.warn("Error accessing resource", fdre);
            //TODO: Revisit exception handling strategy
            throw new RuntimeException("Error accessing resource " + fdre.getMessage());
        } catch (FDSkuNotFoundException fdsnfe) {
            LOGGER.warn("SKU not found", fdsnfe);
            //TODO: Revisit exception handling strategy
            throw new RuntimeException("SKU not found", fdsnfe);
        }
        FDSku fDSku = new FDSku(product);

        ProductModel prodNode = null;
        try {
            prodNode = ContentFactory.getInstance().getProduct(skuCode);
        } catch (FDSkuNotFoundException e) {
            LOGGER.warn("SKU not found", e);
            //TODO: Revisit exception handling strategy
            throw new RuntimeException("SKU not found", e);
        }
        //Supplement user selected variations with defaulted variations to build complete variations map.
        Map<String, String> completeVariations = buildSelectedVariations(product.getVariations(), userSelectVariations);
        FDConfiguration fDConfiguration = new FDConfiguration(quantity, salesUnit, completeVariations);

        fdCartline = new FDCartLineModel(fDSku, prodNode.getProductRef(), fDConfiguration, variantId);
    }

    private Map<String, String> buildSelectedVariations(FDVariation[] variations, Map<String, String> userSelectVariations) {
        /*
         * DUP FROM:
         * Class - com.freshdirect.webapp.taglib.fdstore
         * In method - processSimple
         * 
         * walk through the variations to see what's been set and try to build a variation map
         */

        Map<String, String> selectedVariations = new HashMap<String, String>();
        for (FDVariation variation : variations) {
            FDVariationOption[] options = variation.getVariationOptions();

            String optionName = userSelectVariations.get(variation.getName());

            if (options.length == 1) {
                //
                // there's only a single option, pick that
                //
                selectedVariations.put(variation.getName(), options[0].getName());

            } else if (((optionName == null) || "".equals(optionName)) && variation.isOptional()) {
                //
                // user didn't select anything for an optional variation, pick
                // the SELECTED option for them
                //
                String selected = null;
                for (FDVariationOption option : options) {
                    //TODO: this is legacy logic. Shouldn't it break after finding one? What if there's more than one.
                    if (option.isSelected()) {
                        selected = option.getName();
                    }
                }
                selectedVariations.put(variation.getName(), selected);
            } else if (optionName != null && !"".equals(optionName)) {
                //
                // validate & add the option the user selected
                //
                boolean validOption = false;
                for (FDVariationOption option : options) {
                    if (optionName.equals(option.getName())) {
                        validOption = true;
                        break;
                    }
                }
                if (validOption) {
                    selectedVariations.put(variation.getName(), optionName);
                } else {
                    //TODO: Validation. Revisit w/ framework
                    throw new IllegalArgumentException(variation.getName() + " Please select " + variation.getDescription());
                    //result.addError(new ActionError(variation.getName() + suffix, "Please select " + variation.getDescription()));
                }
            } else {
                //
                // user didn't select anything for a required variation, alert
                // them
                //
                //TODO: Validation. Revisit w/ framework
                throw new IllegalArgumentException(variation.getName() + " Please select " + variation.getDescription());
                //result.addError(new ActionError(variation.getName() + suffix, "Please select " + variation.getDescription()));
            }
        }
        return selectedVariations;
    }
}
