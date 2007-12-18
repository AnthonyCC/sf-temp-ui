/*
 * Created on Mar 15, 2005
 */
package com.freshdirect.cms.application.service.query;

import com.freshdirect.cms.ContentType;

/**
 * Static class defining {@link com.freshdirect.cms.ContentType}s
 * related to queries.
 */
public class CmsQueryTypes {

	public final static ContentType QUERY_FOLDER = ContentType.get("CmsQueryFolder");
	public final static ContentType QUERY = ContentType.get("CmsQuery");
	public final static ContentType REPORT = ContentType.get("CmsReport");

	private CmsQueryTypes() {
	}

}