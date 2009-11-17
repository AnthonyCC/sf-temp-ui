/*
 * Created on May 12, 2005
 */
package com.freshdirect.cms.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;

/**
 * Delegate object to collect issues during validation.
 */
public class ContentValidationDelegate implements Serializable {

	/** List of {@link ContentValidationMessage} */
	private final List<ContentValidationMessage> messages = new ArrayList<ContentValidationMessage>();

	public void record(String message) {
		this.messages.add(new ContentValidationMessage(message));
	}

	public void record(ContentKey contentKey, String message) {
		this.messages.add(new ContentValidationMessage(contentKey, message));
	}

	public void record(ContentKey contentKey, String attribute, String message) {
		this.messages.add(new ContentValidationMessage(contentKey, attribute, message));
	}

	/**
	 * @return List of {@link ContentValidationMessage}
	 */
	public List<ContentValidationMessage> getValidationMessages() {
		return messages;
	}

	public boolean isEmpty() {
		return messages.isEmpty();
	}

	public String toString() {
		return messages.toString();
	}

}
