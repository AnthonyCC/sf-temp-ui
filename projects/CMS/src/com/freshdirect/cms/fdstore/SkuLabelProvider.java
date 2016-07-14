package com.freshdirect.cms.fdstore;

import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.labels.ILabelProvider;

/**
 * Label provider for Sku nodes. Renders unavailability status and variation
 * matrix domain values.
 */
public class SkuLabelProvider implements ILabelProvider {

    @Override
	public String getLabel(ContentNodeI node, ContentServiceI contentService, DraftContext draftContext) {
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
		List<ContentKey> l = (List<ContentKey>) node.getAttributeValue("VARIATION_MATRIX");
		if (l != null && !l.isEmpty()) {
		    for (final ContentKey dvKey : l) {
				ContentNodeI domainValue = contentService.getContentNode(dvKey, draftContext);

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
