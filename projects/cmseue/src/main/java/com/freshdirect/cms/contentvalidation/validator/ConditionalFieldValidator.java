package com.freshdirect.cms.contentvalidation.validator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.contentvalidation.domain.ConditionalValidatorFieldRules;
import com.freshdirect.cms.contentvalidation.domain.ConditionalValidatorFieldRuleset;
import com.freshdirect.cms.contentvalidation.domain.ProductFilterType;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.validator.Validator;

@Component
public class ConditionalFieldValidator implements Validator {

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();

        ContentType contentType = contentKey.type;

        if (ContentType.ProductFilter.equals(contentType)) {

            String filterType = (String) attributesWithValues.get(ContentTypes.ProductFilter.type);
            if (filterType == null) {
                validationResults.addValidationResult(contentKey, "Type is empty", ValidationResultLevel.ERROR, ConditionalFieldValidator.class);
            } else {
                switch (ProductFilterType.toEnum(filterType)) {
                    case AND:
                        //$FALL-THROUGH$
                    case OR:
                        validateFields(ConditionalValidatorFieldRuleset.PRODUCT_FILTER_COMBINATION, validationResults, contentKey, attributesWithValues);
                        break;

                    case BACK_IN_STOCK:
                        //$FALL-THROUGH$
                    case NEW:
                        //$FALL-THROUGH$
                    case KOSHER:
                        //$FALL-THROUGH$
                    case ON_SALE:
                        validateFields(ConditionalValidatorFieldRuleset.PRODUCT_FILTER_BINARY, validationResults, contentKey, attributesWithValues);
                        break;

                    case ALLERGEN:
                        //$FALL-THROUGH$
                    case CLAIM:
                        //$FALL-THROUGH$
                    case ORGANIC:
                        validateFields(ConditionalValidatorFieldRuleset.PRODUCT_FILTER_ERPSY_FLAG, validationResults, contentKey, attributesWithValues);
                        break;

                    case BRAND:
                        validateFields(ConditionalValidatorFieldRuleset.PRODUCT_FILTER_BRAND, validationResults, contentKey, attributesWithValues);
                        break;

                    case DOMAIN_VALUE:
                        validateFields(ConditionalValidatorFieldRuleset.PRODUCT_FILTER_DOMAIN_VALUE, validationResults, contentKey, attributesWithValues);
                        break;

                    case TAG:
                        validateFields(ConditionalValidatorFieldRuleset.PRODUCT_FILTER_TAG, validationResults, contentKey, attributesWithValues);
                        break;

                    case CUSTOMER_RATING:
                        //$FALL-THROUGH$
                    case EXPERT_RATING:
                        //$FALL-THROUGH$
                    case FRESHNESS:
                        //$FALL-THROUGH$
                    case SUSTAINABILITY_RATING:
                        //$FALL-THROUGH$
                    case PRICE:
                        validateFields(ConditionalValidatorFieldRuleset.PRODUCT_FILTER_RANGE, validationResults, contentKey, attributesWithValues);
                        break;

                    case NUTRITION:
                        validateFields(ConditionalValidatorFieldRuleset.PRODUCT_FILTER_RANGE_NUTRITION, validationResults, contentKey, attributesWithValues);
                        break;

                    default:
                        validationResults.addValidationResult(contentKey, "Type '" + filterType + "; is invalid", ValidationResultLevel.ERROR, ConditionalFieldValidator.class);
                        break;

                }
            }
        }
        return validationResults;
    }

    private void validateFields(ConditionalValidatorFieldRuleset conditionalValidatorFieldRules, ValidationResults validationResults, ContentKey contentKey,
            Map<Attribute, Object> attributesWithValues) {
        StringBuilder errorTextStringBuilder = new StringBuilder();

        checkRequiredFields(conditionalValidatorFieldRules.getRules(), contentKey, attributesWithValues, validationResults);
        checkUsedFields(conditionalValidatorFieldRules.getRules(), contentKey, attributesWithValues, validationResults);

        errorTextStringBuilder.append(checkRequiredGroups(conditionalValidatorFieldRules.getRules(), contentKey, attributesWithValues));

        if (errorTextStringBuilder.length() != 0) {
            validationResults.addValidationResult(contentKey, errorTextStringBuilder.toString(), ValidationResultLevel.ERROR, ConditionalFieldValidator.class);
        }

    }

    private void checkRequiredFields(ConditionalValidatorFieldRules conditionalValidatorFieldRules, ContentKey contentKey, Map<Attribute, Object> attributesWithValues,
            ValidationResults validationResults) {
        for (Attribute requiredField : conditionalValidatorFieldRules.getRequiredFields()) {
            if (!attributesWithValues.containsKey(requiredField) || !isSet(attributesWithValues.get(requiredField))) {
                validationResults.addValidationResult(contentKey, "Please set '" + requiredField + "'", ValidationResultLevel.ERROR, ConditionalFieldValidator.class);
            }
        }
    }

    private void checkUsedFields(ConditionalValidatorFieldRules conditionalValidatorFieldRules, ContentKey contentKey, Map<Attribute, Object> attributesWithValues,
            ValidationResults validationResults) {
        for (Attribute usedField : attributesWithValues.keySet()) {
            if (!conditionalValidatorFieldRules.getUsedFields().contains(usedField) && isSet(attributesWithValues.get(usedField))) {
                validationResults.addValidationResult(contentKey, "Please clear '" + usedField + "'", ValidationResultLevel.ERROR, ConditionalFieldValidator.class);
            }
        }
    }

    private String checkRequiredGroups(ConditionalValidatorFieldRules conditionalValidatorFieldRules, ContentKey contentKey, Map<Attribute, Object> attributesWithValues) {

        StringBuilder errorTextBuilder = new StringBuilder();
        for (List<Attribute> requiredGroup : conditionalValidatorFieldRules.getRequiredFieldGroups()) {
            boolean allEmpty = true;
            StringBuilder fieldList = new StringBuilder();

            String delimiter = "', '";
            for (Attribute attribute : requiredGroup) {
                fieldList.append(attribute + delimiter);
                if (isSet(attributesWithValues.get(attribute))) {
                    allEmpty = false;
                    break;
                }
            }

            if (allEmpty) {
                String fieldListStr = fieldList.toString();
                errorTextBuilder.append("Please set at least one of the following: '" + fieldListStr.substring(0, fieldListStr.length() - delimiter.length()) + "'");
            }
        }
        return errorTextBuilder.toString();
    }

    private boolean isSet(Object attributeValue) {
        return attributeValue != null && !"".equals(attributeValue) && !(attributeValue instanceof Collection<?> && ((Collection<?>) attributeValue).isEmpty());
    }

}
