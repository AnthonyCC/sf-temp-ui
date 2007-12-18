package com.freshdirect.cms.listeners;

import com.freshdirect.cms.ContentKey;

public class MediaAssociation {

	private final ContentKey contentKey;

	private final String attributeName;

	public MediaAssociation(ContentKey contentKey, String attributeName) {
		this.contentKey = contentKey;
		this.attributeName = attributeName;
	}

	public ContentKey getContentKey() {
		return contentKey;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String toString() {
		return contentKey.getEncoded() + "." + attributeName;
	}

}
