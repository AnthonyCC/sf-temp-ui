package com.freshdirect.cms.contentvalidation.validator;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.freshdirect.cms.validation.validator.Validator;

@Component
public class DateIntervalValidator implements Validator {

    private static final String START_DATE_ATTRIBUTE_NAME = "startDate";
    private static final String END_DATE_ATTRIBUTE_NAME = "endDate";

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();

        if ((hasAttributeWithName(attributesWithValues.keySet(), START_DATE_ATTRIBUTE_NAME)
                && attributesWithValues.get(getAttributeWithName(attributesWithValues.keySet(), START_DATE_ATTRIBUTE_NAME)) != null)
                && (hasAttributeWithName(attributesWithValues.keySet(), END_DATE_ATTRIBUTE_NAME)
                        && attributesWithValues.get(getAttributeWithName(attributesWithValues.keySet(), END_DATE_ATTRIBUTE_NAME)) != null)) {

            Date startDate = (Date) attributesWithValues.get(getAttributeWithName(attributesWithValues.keySet(), START_DATE_ATTRIBUTE_NAME));
            Date endDate = (Date) attributesWithValues.get(getAttributeWithName(attributesWithValues.keySet(), END_DATE_ATTRIBUTE_NAME));

            if (startDate.after(endDate)) {
                validationResults.addValidationResult(contentKey, "Start date is after the end date", ValidationResultLevel.ERROR, DateIntervalValidator.class);
            }
        }

        return validationResults;
    }

    private boolean hasAttributeWithName(Set<Attribute> attributes, String name) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private Attribute getAttributeWithName(Set<Attribute> attributes, String name) {
        Attribute attributeWithName = null;
        for (Attribute attribute : attributes) {
            if (attribute.getName().equalsIgnoreCase(name)) {
                attributeWithName = attribute;
                break;
            }
        }
        return attributeWithName;
    }
}
