package com.freshdirect.cms.fdstore;

import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

/**
 * Ensures that the <code>PRIMARY_HOME</code> attribute points
 * to a valid parent for nodes of type <code>Product</code>. 
 */
public class PrimaryHomeValidator implements ContentValidatorI {

	public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request) {
		ContentType t = node.getKey().getType();
		if (FDContentTypes.PRODUCT.equals(t)) {
			ContentKey priHome = (ContentKey) node.getAttribute("PRIMARY_HOME").getValue();

			Set parentKeys = service.getParentKeys(node.getKey());
			if (parentKeys.isEmpty()) {
				// new or orphaned node, leave-as is
				return;
			}

			if (priHome == null || !parentKeys.contains(priHome)) {

				ContentKey ph = (ContentKey) parentKeys.iterator().next();
				
				if (request == null) {
					delegate.record(node.getKey(), "PRIMARY_HOME", "Primary home should be reassigned to " + parentKeys);
					return;
				}
				
				ContentNodeI clone = node.copy();
				clone.getAttribute("PRIMARY_HOME").setValue(ph);

				request.addNode(clone);
			}
		}
	}

}
