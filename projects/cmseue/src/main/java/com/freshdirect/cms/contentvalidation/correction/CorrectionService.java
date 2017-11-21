package com.freshdirect.cms.contentvalidation.correction;

import java.util.Map;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.validator.Validator;

public interface CorrectionService {

    /**
     * Every CorrectionService implementation supports only one {@link com.freshdirect.cms.validation.validator.Validator}. The CorrectionService is potentially able to fix the
     * errors which were identified by those validator
     *
     * @return the supported Validator's class
     */
    Class<? extends Validator> getSupportedValidator();

    /**
     * This method is used to automatically repair data. Examples: automatically assigning primary home for products. This runs after a validation cycle. If a validation error is
     * found then the correctionServices are run, to try to fix those errors
     *
     * @param contentKey
     *            the key to identify the node
     * @param attributesWithValues
     *            all of the attributes and values for the node
     */
    void correct(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, ContextualContentProvider contentSource);
}
