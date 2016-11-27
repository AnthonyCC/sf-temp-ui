package com.freshdirect.cms.application;

import com.freshdirect.cms.ContentNodeI;

/**
 * Service for retrieving media content nodes by media path (uri)
 * 
 * @author csongor
 * 
 */
public interface MediaServiceI extends ContentServiceI {
	/**
	 * @param uri
	 *            the path of the media
	 * @return ContentNodeI (or null if not found, or type is not supported)
	 */
	public ContentNodeI getContentNode(String uri);
}
