package com.freshdirect.cms.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.ChangedContentNode;
import com.freshdirect.cms.util.PrimaryHomeUtil;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

/**
 * Ensures that the <code>PRIMARY_HOME</code> attribute points
 * to a valid parent for nodes of type <code>Product</code>. 
 */
public class PrimaryHomeValidator implements ContentValidatorI {
	
    @Override
    public void validate(ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode) {
        final boolean isPublish = (request == null);

        if (!FDContentTypes.PRODUCT.equals(node.getKey().getType()) || isPublish) {
            // not a product, skip ...
            // auto-fix has been moved to SingleStoreFilterHelper class
            return;
        }

        @SuppressWarnings("unchecked")
        // pick original homes
        List<ContentKey> homes = (List<ContentKey>) node.getAttributeValue("PRIMARY_HOME");
        if (homes == null) {
            homes = Collections.emptyList();
        }

        Set<ContentKey> validHomeKeys = PrimaryHomeUtil.fixPrimaryHomes(node, service, draftContext, null);
        if (validHomeKeys == null) {
            // internal error occurred, abort the validation now.
            return;
        }

        // check if homes set is changed
        if (!validHomeKeys.containsAll(homes) || !homes.containsAll(validHomeKeys)) {
            // make the change
            ContentNodeI clone;
            if (draftContext != DraftContext.MAIN) {
                clone = new ChangedContentNode(node);

            } else {
                clone = node.copy();
            }

            clone.setAttributeValue("PRIMARY_HOME", new ArrayList<ContentKey>(validHomeKeys));

            request.addNode(clone);
        }
    }
}
