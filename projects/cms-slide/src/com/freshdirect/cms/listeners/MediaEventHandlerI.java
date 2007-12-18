/**
 * @author ekracoff
 * Created on Dec 21, 2004*/

package com.freshdirect.cms.listeners;

import com.freshdirect.cms.validation.ContentValidationException;

public interface MediaEventHandlerI {

	public void create(Media media, String userId) throws ContentValidationException;

	public void move(String sourceUri, String targetUri, String userId) throws ContentValidationException;

	public void copy(String sourceUri, String targetUri, String userId);

	public void delete(String sourceUri, String userId);

	public void update(Media media, String userId);
	
	public boolean isBulkload();

}