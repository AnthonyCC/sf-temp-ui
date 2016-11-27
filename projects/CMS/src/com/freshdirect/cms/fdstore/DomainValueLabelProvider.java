package com.freshdirect.cms.fdstore;

import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.labels.ILabelProvider;

/**
 * {@link com.freshdirect.cms.labels.ILabelProvider} implementation for
 * content nodes of type <code>DomainValue</code>.
 */
public class DomainValueLabelProvider implements ILabelProvider {

    @Override
	public String getLabel(ContentNodeI node, ContentServiceI contentService, DraftContext draftContext) {
		if (!FDContentTypes.DOMAINVALUE.equals(node.getKey().getType())) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		Set parentKeys = contentService.getParentKeys(node.getKey(), draftContext);
		if (!parentKeys.isEmpty()) {
			ContentKey parentKey = (ContentKey) parentKeys.iterator().next();
			ContentNodeI parentNode = contentService.getContentNode(parentKey, draftContext);
			sb.append(getLabelAttr(parentNode)).append(": ");
		}
		
		sb.append(getLabelAttr(node));
		return sb.toString();
	}

	private String getLabelAttr(ContentNodeI node) {
		AttributeI labelAttr = node.getAttribute("Label");
		if (labelAttr == null) {
			throw new CmsRuntimeException(
					"No Label attribute defined for node " + node);
		}
		String s = (String) labelAttr.getValue();
		return s != null ? s : node.getKey().getId();
	}
}
