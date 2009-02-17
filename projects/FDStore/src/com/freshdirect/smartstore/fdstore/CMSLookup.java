package com.freshdirect.smartstore.fdstore;

import com.freshdirect.fdstore.content.ContentNodeModel;

/**
 * Derive scores from CMS.
 * 
 * @author istvan
 *
 */
public interface CMSLookup {

	/**
	 * Get factor value.
	 * @param contentNode
	 * @return factor value
	 */
	public double getVariable(ContentNodeModel contentNode);
}
