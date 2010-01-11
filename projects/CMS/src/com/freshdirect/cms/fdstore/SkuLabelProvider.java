package com.freshdirect.cms.fdstore;

import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.labels.ILabelProvider;

/**
 * Label provider for Sku nodes. Renders unavailability status and variation
 * matrix domain values.
 */
public class SkuLabelProvider implements ILabelProvider {

	public String getLabel(ContentNodeI node) {
		if (!FDContentTypes.SKU.equals(node.getKey().getType())) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		String separator = "";

		// availability
		String x = LabelProviderUtil.getAttribute(node, "UNAVAILABILITY_STATUS", "");
		if (!"".equals(x)) {
			sb.append(x);
			separator = ", ";
		}

		// variation matrix
		List l = (List) node.getAttributeValue("VARIATION_MATRIX");
		if (l != null && !l.isEmpty()) {
			for (Iterator i = l.iterator(); i.hasNext();) {
				ContentKey dvKey = (ContentKey) i.next();
				ContentNodeI domainValue = dvKey.getContentNode();

				String dvLabel = LabelProviderUtil.getAttribute(domainValue,
						"Label", "");
				sb.append(separator);
				sb.append("".equals(dvLabel) ? dvKey.getId() : dvLabel);
				separator = ", ";
			}
		}

		// surrounding parens
		if (!"".equals(separator)) {
			sb.insert(0, " (");
			sb.append(")");
		}

		// prepend main label
		sb.insert(0, node.getKey().getId());

		return sb.toString();
	}

}
