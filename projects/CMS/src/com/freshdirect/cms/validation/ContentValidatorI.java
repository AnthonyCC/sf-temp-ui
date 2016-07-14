package com.freshdirect.cms.validation;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;

/**
 * Content-node validator interface.
 */
public interface ContentValidatorI {

	/**
	 * Validate a {@link ContentNodeI}instance. Relationships should be
	 * validated against the specified {@link ContentServiceI}. Validation
	 * issues are collected in the {@link ContentValidationDelegate}.
	 * If a <code>request</code> parameter is provided, the validator may
	 * make suggested changes there, instead of reporting a validation error.
	 * 
	 * @param delegate delegate to collect errors in
	 * @param service backing content service to validate against
	 * @param node the node to validate
	 * @param request request collecting suggested changes (optional)
	 * @param oldNode the original node, if it's available

	 */
	public void validate(ContentValidationDelegate delegate,
			ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode);

}