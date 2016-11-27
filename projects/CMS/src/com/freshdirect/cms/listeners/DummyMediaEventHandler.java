package com.freshdirect.cms.listeners;

import com.freshdirect.cms.validation.ContentValidationException;

public class DummyMediaEventHandler implements MediaEventHandlerI {

	private final static MediaEventHandlerI INSTANCE = new DummyMediaEventHandler();
	
	protected DummyMediaEventHandler() {
	}
	
	public static MediaEventHandlerI getInstance() {
		return INSTANCE;
	}
	
	public void copy(String sourceUri, String targetUri, String userId) {
		throw new UnsupportedOperationException("dummy instance does nothing");
	}

	public void create(Media media, String userId)
			throws ContentValidationException {
		throw new UnsupportedOperationException("dummy instance does nothing");
	}

	public void delete(String sourceUri, String userId) {
		throw new UnsupportedOperationException("dummy instance does nothing");
	}

	public boolean isBulkload() {
		throw new UnsupportedOperationException("dummy instance does nothing");
	}

	public void move(String sourceUri, String targetUri, String userId)
			throws ContentValidationException {
		throw new UnsupportedOperationException("dummy instance does nothing");
	}

	public void update(Media media, String userId) {
		throw new UnsupportedOperationException("dummy instance does nothing");
	}
}
