package com.freshdirect.cms.validation.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

@Component
public class TypeValidator implements Validator {

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> payload, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        ContentType contentType = contentKey.type;
        Set<Attribute> attributesForType = contentTypeInfoService.selectAttributes(contentType);
        if ((attributesForType != null && !attributesForType.isEmpty()) || payload.isEmpty()) {
            validateRequired(contentKey, attributesForType, payload, validationResults);
            for (Attribute attribute : attributesForType) {
                if (payload.containsKey(attribute)) {
                    validateAttribute(contentKey, attribute, payload.get(attribute), validationResults);
                }
            }

            validateExtraneousAttributes(contentKey, payload, validationResults);
        } else {
            validationResults.addValidationResult(contentType, "Can't save data for an empty type", ValidationResultLevel.ERROR, TypeValidator.class);
        }

        return validationResults;
    }

    private void validateExtraneousAttributes(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ValidationResults validationResults) {
        Set<ContentType> extendedTypeSet = contentTypeInfoService.getReachableContentTypes(contentKey.type);
        for (Map.Entry<Attribute, Object> attributeAndValue : attributesWithValues.entrySet()) {
            Attribute attribute = attributeAndValue.getKey();

            boolean attributeDefFound = false;
            Set<ContentType> testSet = new HashSet<ContentType>();
            testSet.add(contentKey.type);

            if (attribute.getFlags().isInheritable()) {
                testSet.addAll(extendedTypeSet);
            }

            for (ContentType aType : testSet) {
                Set<Attribute> attributes = contentTypeInfoService.selectAttributes(aType);
                if (attributes != null && attributes.contains(attribute)) {
                    attributeDefFound = true;
                    break;
                }
            }

            if (!attributeDefFound) {
                if (attribute.getFlags().isInheritable()) {
                    validationResults.addValidationResult(contentKey, "Value of unassigned inherited attribute '" + attribute + "' found.", ValidationResultLevel.ERROR,
                            TypeValidator.class);
                } else {
                    validationResults.addValidationResult(contentKey, "Value of unassigned attribute '" + attribute + "' found.", ValidationResultLevel.ERROR,
                            TypeValidator.class);
                }
            }
        }
    }

    private void validateAttribute(ContentKey contentKey, Attribute attribute, Object attributeValue, ValidationResults validationResults) {
        if (attributeValue != null) {
            if (attribute instanceof Scalar) {
                validateEnumeratedValue(contentKey, attribute, attributeValue, validationResults);
                validateScalarValueType(contentKey, attribute, attributeValue, validationResults);
            } else {
                validateSingleRelationship(contentKey, attribute, attributeValue, validationResults);
                validateMultiRelationship(contentKey, attribute, attributeValue, validationResults);
            }
        }
    }

    private void validateEnumeratedValue(ContentKey contentKey, Attribute attribute, Object attributeValue, ValidationResults validationResults) {
        if (attribute instanceof Scalar) {
            Scalar scalar = (Scalar) attribute;
            if (scalar.isEnumerated()) {
                if (attributeValue != null && !Arrays.asList(scalar.getEnumeratedValues()).contains(attributeValue)) {
                    validationResults.addValidationResult(contentKey,
                            "Invalid enum value '" + attributeValue.toString() + "' found for attribute '" + attribute.getName() + "'",
                            ValidationResultLevel.ERROR, TypeValidator.class);
                }
            }
        }
    }

    private void validateScalarValueType(ContentKey contentKey, Attribute attribute, Object attributeValue, ValidationResults validationResults) {
        if (attribute instanceof Scalar) {
            Scalar scalar = (Scalar) attribute;
            if (!attributeValue.getClass().isAssignableFrom(scalar.getType())) {
                validationResults.addValidationResult(contentKey,
                        "Value type mismatch! " + attributeValue.getClass().getSimpleName() + " of '" + attribute.getName() + "' is not " + scalar.getType().getSimpleName(),
                        ValidationResultLevel.ERROR, TypeValidator.class);
            }
        }
    }

    private void validateSingleRelationship(ContentKey contentKey, Attribute attribute, Object attributeValue, ValidationResults validationResults) {
        if (attribute instanceof Relationship) {
            Relationship relationship = (Relationship) attribute;
            if (relationship.getCardinality() == RelationshipCardinality.ONE) {
                if (attributeValue instanceof ContentKey) {
                    ContentKey destinationKey = (ContentKey) attributeValue;
                    if (!Arrays.asList(relationship.getDestinationTypes()).contains(destinationKey.type)) {
                        validationResults.addValidationResult(contentKey,
                                "Type of " + destinationKey + " not matched by relationship '" + relationship.getName() + "' destination type",
                                ValidationResultLevel.ERROR, TypeValidator.class);
                    }
                } else {
                    validationResults.addValidationResult(contentKey,
                            "Invalid relationship '" + relationship.getName() + "' value", ValidationResultLevel.ERROR, TypeValidator.class);
                }
            }
        }
    }

    private void validateMultiRelationship(ContentKey contentKey, Attribute attribute, Object attributeValue, ValidationResults validationResults) {
        if (attribute instanceof Relationship) {
            Relationship relationship = (Relationship) attribute;
            if (relationship.getCardinality() == RelationshipCardinality.MANY) {
                if (attributeValue instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<ContentKey> relationshipTargets = (List<ContentKey>) attributeValue;
                    for (ContentKey destinationKey : relationshipTargets) {
                        if (!Arrays.asList(relationship.getDestinationTypes()).contains(destinationKey.type)) {
                            validationResults.addValidationResult(contentKey,
                                    "Type of " + destinationKey + " not part of allowed relationship '" + relationship.getName() + "' destinations",
                                    ValidationResultLevel.ERROR, TypeValidator.class);
                        }
                    }
                } else {
                    validationResults.addValidationResult(contentKey,
                            "Invalid relationship '" + relationship.getName() + "' value",
                            ValidationResultLevel.ERROR, TypeValidator.class);
                }
            }
        }
    }

    private void validateRequired(ContentKey contentKey, Set<Attribute> attributesForType, Map<Attribute, Object> attributesWithValues, ValidationResults validationResults) {
        for (Attribute attribute : attributesForType) {
            if (attribute.getFlags().isRequired() && (!attributesWithValues.containsKey(attribute) || attributesWithValues.get(attribute) == null)) {
                validationResults.addValidationResult(contentKey,
                        "No value found for required attribute '" + attribute.getName() + "'",
                        ValidationResultLevel.ERROR, TypeValidator.class);
            }
        }
    }
}
