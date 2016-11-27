package com.freshdirect.cms.util;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class PublishId {
	private static final Logger LOGGER = LoggerFactory.getInstance(PublishId.class);

	private static PublishId INSTANCE = null;

	public synchronized static PublishId getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PublishId();
		return INSTANCE;
	}

	private String publishId;

	protected PublishId() {

	}

	public void setPublishId(String publishId) {
		this.publishId = publishId;
		LOGGER.info("Publish ID was set to '" + publishId + "'");
	}

	public String getPublishId() {
		return publishId;
	}
}
