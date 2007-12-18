package com.freshdirect.cms.fdstore;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.labels.ILabelProvider;

/**
 * {@link com.freshdirect.cms.labels.ILabelProvider} implementation for content
 * nodes of type <code>EprMaterial</code>. Renders name and description.
 */
public class ErpMaterialLabelProvider implements ILabelProvider {

	public String getLabel(ContentNodeI node) {
		if (!FDContentTypes.ERP_MATERIAL.equals(node.getKey().getType())) {
			return null;
		}
		String name = LabelProviderUtil.getAttribute(node, "name", "");
		if ("".equals(name)) {
			name = node.getKey().getId();
		}

		String desc = LabelProviderUtil.getAttribute(node, "DESCRIPTION", "");
		if ("".equals(desc)) {
			return name;
		} else {
			desc = StringUtils.capitaliseAllWords(desc.toLowerCase());
			return name + " (" + desc + ")";
		}
	}

}
