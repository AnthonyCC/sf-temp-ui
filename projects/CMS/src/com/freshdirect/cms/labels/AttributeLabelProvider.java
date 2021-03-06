package com.freshdirect.cms.labels;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;

/**
 * Uses a specific attribute's value as the label. If the attribute
 * doesn't exist or is blank, null is returned.
 */
public class AttributeLabelProvider implements ILabelProvider {

	private final String attribute;

	/**
	 * @param attribute name of the attribute to use as label
	 */
	public AttributeLabelProvider(String attribute) {
		this.attribute = attribute;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.cms.labels.ILabelProvider#getLabel(com.freshdirect.cms.ContentNodeI)
	 */
	@Override
	public String getLabel(ContentNodeI node, ContentServiceI contentService, DraftContext draftContext) {
		AttributeI a = node.getAttribute(attribute);
		if (a == null) {
			return null;
		}
		Object v = a.getValue();
		if (v == null) {
			return null;
		}
		return "".equals(v) ? null : String.valueOf(v);
	}

}
