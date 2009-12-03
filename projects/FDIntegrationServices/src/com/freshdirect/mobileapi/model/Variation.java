package com.freshdirect.mobileapi.model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;

public class Variation {

    public enum VariationType {
        MULTIPLE_CHOICE, SINGLE_CHOICE, ONLY_ONE_OPTION
    }

    private VariationType type;

    private FDVariation variation;

    private List<VariationOption> options = new ArrayList<VariationOption>();

    private boolean optional;

    private String underLabel;

    private String name;

    private String description;

    public static Variation wrap(FDVariation variation, Product product) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        Variation result = new Variation();
        result.variation = variation;

        FDVariationOption[] options = variation.getVariationOptions();
        Pricing defaultPricing = product.getDefaultProduct().getPricing();

        for (FDVariationOption fdOption : options) {
            VariationOption option = new VariationOption(fdOption);
            result.options.add(option);
            double cvprice = 0.0;

            CharacteristicValuePrice cvp = defaultPricing.findCharacteristicValuePrice(variation.getName(), fdOption.getName());
            if (cvp != null) {
                cvprice = cvp.getPrice();
            }

            if (cvprice > 0.0) {
                option.setCharacteristicValuePrice(currencyFormatter.format(cvp.getPrice()) + "/" + cvp.getPricingUnit());
            } else {
                option.setCharacteristicValuePrice(" - no charge ");
            }

        }

        if (options.length == 1) {
            result.type = VariationType.ONLY_ONE_OPTION;
        } else {

            if (!"checkbox".equals(variation.getDisplayFormat())) {
                result.type = VariationType.SINGLE_CHOICE;

            } else { // "dropdown"
                result.type = VariationType.MULTIPLE_CHOICE;
            }
        }
        result.optional = variation.isOptional();
        result.underLabel = variation.getAttribute(EnumAttributeName.UNDER_LABEL);
        result.name = variation.getName();
        result.description = variation.getAttribute(EnumAttributeName.DESCRIPTION);

        return result;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getUnderLabel() {
        return underLabel;
    }

    public String getName() {
        return name;
    }

    /**
     * For dropdown options (i_product_jspf)
     * @return
     */
    public String getDescription() {
        return description;
    }

    public VariationType getType() {
        return type;
    }

    public List<VariationOption> getOptions() {
        return options;
    }

    public FDVariation getVariation() {
        return variation;
    }

    public void setVariation(FDVariation variation) {
        this.variation = variation;
    }

    public void setType(VariationType type) {
        this.type = type;
    }

    public void setOptions(List<VariationOption> options) {
        this.options = options;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public void setUnderLabel(String underLabel) {
        this.underLabel = underLabel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
