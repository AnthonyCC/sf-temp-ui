/*
 * Created on Feb 8, 2005
 */
package com.freshdirect.cms.application.service;

import com.freshdirect.cms.ContentNodeI;

/**
 * Interface for decorating {@link com.freshdirect.cms.ContentNodeI}
 * instances with additional attibutes, etc.
 * 
 * @see com.freshdirect.cms.application.service.ContentDecoratorService
 */
public interface ContentDecoratorI {

	/**
	 * Method is responsible for wrapping (or cloning) node.
	 * Returned node must have same key as param node.
	 * 
	 * @param node ContentNodeI to decorate (never null)
	 * @return decorated node, or null if no decoration was done 
	 */
	public ContentNodeI decorateNode(ContentNodeI node);

}