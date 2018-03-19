package com.freshdirect.cms.contentvalidation.validator;

import static com.freshdirect.cms.core.domain.ContentTypes.Category.*;

import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.google.common.collect.ImmutableSet;

import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

/**
 * Ensures that the <code>RATING_GROUP_NAMES</code> attribute has valid value: comma separated list of RG_* attribute names (case insensitive).
 */
@Component
public class RatingGroupValidator implements Validator {

    public static final String INVALID_MESSAGE = "Rating Group Names contains invalid value: ";

    private static final Set<String> VALID_RG_NAMES = ImmutableSet.of(
            RG_MORE_USAGE.getName(),
            RG_POP_USAGE.getName(),
            RG_SIZE_PRICE.getName(),
            RG_TASTE_PRICE.getName(),
            RG_TASTE_TYPE_PRICE.getName(),
            RG_TASTE_USE_PRICE.getName(),
            RG_TEXTURE_PRICE.getName(),
            RG_TYPE_PRICE.getName(),
            RG_USAGE.getName(),
            RG_USAGE_PRICE.getName());

    @Override
    public ValidationResults validate(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource) {
        ValidationResults validationResults = new ValidationResults();
        if (ContentType.Category.equals(contentKey.type)) {
            String value = (String) attributesWithValues.get(RATING_GROUP_NAMES);
            if (value != null) {
                StringTokenizer tokenizer = new StringTokenizer(value, ",");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken().trim();
                    if (!VALID_RG_NAMES.contains(token.toUpperCase())) {
                        // TODO : change to ValidationResultLevel.ERROR to enable this validator
                        validationResults.addValidationResult(contentKey, INVALID_MESSAGE + token + "! ", ValidationResultLevel.WARNING, RatingGroupValidator.class);
                    }
                }
            }
        }
        return validationResults;
    }
}
