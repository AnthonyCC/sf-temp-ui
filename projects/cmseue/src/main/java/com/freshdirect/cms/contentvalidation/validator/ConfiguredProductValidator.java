package com.freshdirect.cms.contentvalidation.validator;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.persistence.service.ERPSDataService;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.google.common.base.Optional;

/**
 * {@link com.freshdirect.cms.validation.ContentValidatorI} that ensures that configuration options are consistent with the selected SKU on <code>ConfiguredProduct</code>
 * instances. Ensures that:
 * <ul>
 * <li>a SKU is specified</li>
 * <li>the corresponding SKU has a material</li>
 * <li>the sales unit is consistent with the material</li>
 * <li>all characteristics of the material are specified and valid</li>
 * <li>no extraneous characteristics are specified</li>
 * <li>contained in either:
 * <ul>
 * <li>0..n Category or</li>
 * <li>0..1 ConfiguredProductGroup or</li>
 * <li>0..1 RecipeSection</li>
 * <li>0..1 StarterList</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @TODO validate quantity (min/max/increment)
 */
@Profile("database")
@Component
public class ConfiguredProductValidator implements Validator {

    private static final String CONFIGURED_PRODUCT_OPTION_SEPARATOR = "=";

    @Autowired
    private ERPSDataService erpsDataProvider;

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        String message = null;
        if (ContentType.ConfiguredProduct.equals(contentKey.type)) {
            Optional<Object> optionalSkuKey = Optional.fromNullable(attributesWithValues.get(ContentTypes.ConfiguredProduct.SKU));
            if (!optionalSkuKey.isPresent()) {
                message = "No SKU specified";
                validationResults.addValidationResult(new ValidationResult(contentKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
            } else {
                ContentKey skuKey = (ContentKey) optionalSkuKey.get();
                if (!contentSource.containsContentKey(skuKey)) {
                    message = MessageFormat.format("{0} not found", skuKey.toString());
                    validationResults.addValidationResult(new ValidationResult(contentKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
                } else {
                    String materialKey = null;
                    try {
                        materialKey = erpsDataProvider.fetchMaterialsBySku(skuKey);
                    } catch (IllegalArgumentException exception) {
                        message = "No material found for this SKU";
                        validationResults.addValidationResult(new ValidationResult(skuKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
                    }
                    if (materialKey != null) {
                        validateSalesUnit(contentKey, validationResults, skuKey, attributesWithValues);
                        validateCharacteristicValues(contentKey, validationResults, skuKey, attributesWithValues);
                        validateParentCounts(contentKey, validationResults, contentSource);
                    }
                }
            }
        }
        return validationResults;
    }

    private void validateCharacteristicValues(ContentKey contentKey, ValidationResults validationResults, ContentKey sku, Map<Attribute, Object> attributesWithValues) {
        String message;
        Map<String, Map<String, String>> materialCharacteristics = erpsDataProvider.fetchMaterialCharacteristicsBySku(sku);
        if (materialCharacteristics == null) {
            validationResults.addValidationResult(new ValidationResult(contentKey, "No characteristics found", ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
            return;
        }

        Map<String, String> configuredProductOptions = null;

        Optional<Object> configuredProductOptionsOptional = Optional.fromNullable(attributesWithValues.get(ContentTypes.ConfiguredProduct.OPTIONS));
        if (configuredProductOptionsOptional.isPresent()) {
            configuredProductOptions = convertStringToMap((String) configuredProductOptionsOptional.get(), CONFIGURED_PRODUCT_OPTION_SEPARATOR);
        } else {
            configuredProductOptions = Collections.emptyMap();
        }

        if (materialCharacteristics != null) {
            for (Map.Entry<String, Map<String, String>> entry : materialCharacteristics.entrySet()) {
                String characteristicName = entry.getKey();
                Map<String, String> erpCharacteristicValues = entry.getValue();
                Set<String> characteristicValues = erpCharacteristicValues.keySet();
                String characteristicValue = configuredProductOptions.get(characteristicName);
                if (characteristicValue == null) {
                    message = MessageFormat.format("No value for characteristic {0}", characteristicName);
                    validationResults.addValidationResult(new ValidationResult(contentKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
                    continue;
                }

                boolean validCharacteristicValue = false;
                for (Object characteristicValueItem : characteristicValues) {
                    if (characteristicValueItem.equals(characteristicValue)) {
                        validCharacteristicValue = true;
                        break;
                    }
                }
                if (!validCharacteristicValue) {
                    SortedSet<Object> sortedCharacteristicValues = new TreeSet<Object>(characteristicValues);
                    message = MessageFormat.format("Invalid characteristic value {0} for characteristic {1}. Valid options are {2}", characteristicValue, characteristicName,
                            sortedCharacteristicValues);
                    validationResults.addValidationResult(new ValidationResult(contentKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
                }
            }
        }
    }

    private void validateParentCounts(ContentKey contentKey, ValidationResults validationResults, ContextualContentProvider contentSource) {
        String message;
        Set<ContentKey> parentKeys = contentSource.getParentKeys(contentKey);
        int categoryCount = 0;
        int configuredProductCount = 0;
        int recipeSectionCount = 0;
        int fdFolderCount = 0;
        int starterListCount = 0;

        for (ContentKey parentKey : parentKeys) {
            switch (parentKey.type) {
                case ConfiguredProductGroup:
                    configuredProductCount++;
                    break;
                case Category:
                    categoryCount++;
                    break;
                case RecipeSection:
                    categoryCount++;
                    break;
                case FDFolder:
                    fdFolderCount++;
                    break;
                case StarterList:
                    starterListCount++;
                    break;
                default:
                    message = MessageFormat.format("Unexpected parent {0}", parentKey.toString());
                    validationResults.addValidationResult(new ValidationResult(contentKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
                    break;
            }
        }
        int sum = (categoryCount > 0 ? 1 : 0) + configuredProductCount + recipeSectionCount + fdFolderCount + starterListCount;
        if (sum > 1) {
            message = MessageFormat.format("Invalid parent counts: {0}", parentKeys);
            validationResults.addValidationResult(new ValidationResult(contentKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
        }
    }

    private void validateSalesUnit(ContentKey contentKey, ValidationResults validationResults, ContentKey sku, Map<Attribute, Object> attributesWithValues) {
        String message;
        Map<String, String> erpSalesUnits = erpsDataProvider.fetchSalesUnitsBySku(sku);
        Set<String> salesUnits = erpSalesUnits.keySet();
        Optional<Object> salesUnit = Optional.fromNullable(attributesWithValues.get(ContentTypes.ConfiguredProduct.SALES_UNIT));
        if (!salesUnit.isPresent()) {
            message = "No sales unit specified";
            validationResults.addValidationResult(new ValidationResult(contentKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
        } else {
            if (!salesUnits.contains(salesUnit.get())) {
                SortedSet<String> sortedSalesUnitKeys = new TreeSet<String>(salesUnits);
                message = MessageFormat.format("Invalid sales unit '{0}' specified, valid units are {1}", salesUnit.get(), sortedSalesUnitKeys);
                validationResults.addValidationResult(new ValidationResult(contentKey, message, ValidationResultLevel.ERROR, ConfiguredProductValidator.class));
            }
        }
    }

    private static Map<String, String> convertStringToMap(String keyValueItems, String separator) {
        Map<String, String> result = new HashMap<String, String>();
        if (keyValueItems != null) {
            StringTokenizer st = new StringTokenizer(keyValueItems, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                int idx = token.indexOf(separator);
                String key = token.substring(0, idx++);
                String value = token.substring(idx, token.length());
                result.put(key, value);
            }
        }
        return result;
    }
}
