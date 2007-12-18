package com.freshdirect.cms.validation;

import java.io.Serializable;

import com.freshdirect.cms.ContentKey;

/**
 * Represents a validation issue with optional location information.
 * The properties {@link #contentKey} and {@link #attribute} may specify
 * what triggered the issue.
 */
public class ContentValidationMessage implements Serializable {

	private final ContentKey contentKey;
	private final String attribute;
	private final String message;

	public ContentValidationMessage(String message) {
		this(null, message);
	}

	public ContentValidationMessage(ContentKey contentKey, String message) {
		this(contentKey, null, message);
	}

	public ContentValidationMessage(ContentKey contentKey, String attribute, String message) {
		this.contentKey = contentKey;
		this.attribute = attribute;
		this.message = message;
	}

	public ContentKey getContentKey() {
		return contentKey;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getMessage() {
		return message;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (contentKey != null) {
			sb.append(contentKey.getEncoded());
			if (attribute != null) {
				sb.append(".").append(attribute);
			}
			sb.append(": ");
		}
		sb.append(message);
		return sb.toString();
	}

}
