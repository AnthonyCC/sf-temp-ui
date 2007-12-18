package com.freshdirect.cms.fdstore;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;

class LabelProviderUtil {

	private LabelProviderUtil() {
	}

	/**
	 * Safe way to get an attribute as String.
	 * 
	 * @param node content node (never null)
	 * @param name attribute name (never null)
	 * @param defaultValue default value
	 * 
	 * @return attribute value as String, or defaultValue
	 * if attr is undefined or null value.
	 */
	public static String getAttribute(ContentNodeI node, String name, String defaultValue) {
		AttributeI a = node.getAttribute(name);
		if (a == null) {
			return defaultValue;
		}
		Object o = a.getValue();
		return o == null ? defaultValue : o.toString();
	}
}
