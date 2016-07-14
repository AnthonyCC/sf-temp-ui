package com.freshdirect.cms.merge;

import com.freshdirect.cms.application.CmsRequestI;

/**
 * Result of a draft content validation event.
 * MergeResult and ValidateResult have to be different, 
 * because Hivemind differentiate calling methods as parameter pattern, not method name
 */
public class ValidationResult extends MergeResult {

    private static final long serialVersionUID = 4671549497704110645L;

    public ValidationResult(CmsRequestI cmsRequest) {
        super(cmsRequest);
    }

}
