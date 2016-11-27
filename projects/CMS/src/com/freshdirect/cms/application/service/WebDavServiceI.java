/**
 * @author ekracoff
 * Created on Mar 25, 2005*/

package com.freshdirect.cms.application.service;

import java.util.List;

import com.freshdirect.cms.ContentNodeI;

/**
 * Service to obtain revision history for content objects.
 * 
 * @TODO refactor to separate package with {@link com.freshdirect.cms.Version}
 */
public interface WebDavServiceI {
	
	/**
	 * Get revision history for a content node.
	 * 
	 * @param content content node to query history for, never null
	 * @return List of {@link com.freshdirect.cms.Version}
	 */
	public abstract List getVersionHistory(ContentNodeI content);

}