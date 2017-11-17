package com.freshdirect.cms.contentvalidation.service;

import java.util.Map;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResults;

public interface ValidatorService {

    ValidationResults validate(ContentKey key, Map<Attribute, Object> attributes, ContextualContentProvider contentSource);
}
