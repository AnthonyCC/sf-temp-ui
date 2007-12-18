package com.freshdirect.cms.labels;

import com.freshdirect.cms.ContentNodeI;

/**
 * Provides a display-label given a {@link ContentNodeI}.
 */
public interface ILabelProvider {

	/**
	 * Get display label for a given {@link ContentNodeI}.
	 * 
	 * @param node the content node (non-null)
	 * @return display-label or null if unsupported
	 */
	public String getLabel(ContentNodeI node);

}
