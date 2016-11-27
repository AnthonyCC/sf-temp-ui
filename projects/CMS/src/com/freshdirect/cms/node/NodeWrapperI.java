/*
 * Created on Aug 10, 2005
 */
package com.freshdirect.cms.node;

import com.freshdirect.cms.ContentNodeI;

/**
 * Generic interface for objects that wrap a single content node.
 * 
 * @see com.freshdirect.cms.node.AttributeMappedNode
 * @see com.freshdirect.cms.application.service.ContentDecoratorService
 */
public interface NodeWrapperI {

	/**
	 * @return the wrapped content node (never null)
	 */
	public ContentNodeI getWrappedNode();

}
