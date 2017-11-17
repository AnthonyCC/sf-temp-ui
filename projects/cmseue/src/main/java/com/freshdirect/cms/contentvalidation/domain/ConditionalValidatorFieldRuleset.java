package com.freshdirect.cms.contentvalidation.domain;

import java.util.Arrays;

import com.freshdirect.cms.core.domain.ContentTypes;

public enum ConditionalValidatorFieldRuleset {
    PRODUCT_FILTER_BINARY(
            new ConditionalValidatorFieldRules().addRequired(ContentTypes.ProductFilter.name).addRequired(ContentTypes.ProductFilter.type)
                    .addOptional(ContentTypes.ProductFilter.invert)),
    PRODUCT_FILTER_COMBINATION(new ConditionalValidatorFieldRules(PRODUCT_FILTER_BINARY.getRules()).addRequired(ContentTypes.ProductFilter.filters)),
    PRODUCT_FILTER_ERPSY_FLAG(new ConditionalValidatorFieldRules(PRODUCT_FILTER_BINARY.getRules()).addRequired(ContentTypes.ProductFilter.erpsyFlagCode)),
    PRODUCT_FILTER_BRAND(new ConditionalValidatorFieldRules(PRODUCT_FILTER_BINARY.getRules()).addRequired(ContentTypes.ProductFilter.brand)),
    PRODUCT_FILTER_DOMAIN_VALUE(new ConditionalValidatorFieldRules(PRODUCT_FILTER_BINARY.getRules()).addRequired(ContentTypes.ProductFilter.domainValue)),
    PRODUCT_FILTER_TAG(new ConditionalValidatorFieldRules(PRODUCT_FILTER_BINARY.getRules()).addRequired(ContentTypes.ProductFilter.tag)),
    PRODUCT_FILTER_RANGE(
            new ConditionalValidatorFieldRules(PRODUCT_FILTER_BINARY.getRules())
                    .addRequiredGroup(Arrays.asList(ContentTypes.ProductFilter.fromValue, ContentTypes.ProductFilter.toValue))),
    PRODUCT_FILTER_RANGE_NUTRITION(new ConditionalValidatorFieldRules(PRODUCT_FILTER_RANGE.getRules()).addRequired(ContentTypes.ProductFilter.nutritionCode));

    private ConditionalValidatorFieldRules conditionalValidatorFieldRules;

    ConditionalValidatorFieldRuleset(ConditionalValidatorFieldRules conditionalValidatorFieldRules) {
        this.conditionalValidatorFieldRules = conditionalValidatorFieldRules;
    }

    public ConditionalValidatorFieldRules getRules() {
        return this.conditionalValidatorFieldRules;
    }
}
