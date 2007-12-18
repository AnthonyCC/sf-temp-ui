/**
 * @author ekracoff
 * Created on Jun 22, 2005*/

package com.freshdirect.cms.application.service;

import java.util.List;

/**
 * Service to collect metadata about loaded resources containing content.
 * For example, {@link com.freshdirect.cms.application.service.xml.XmlContentService}
 * records DC metadata here during parsing.
 * 
 * @TODO this could use some refactoring, but sufficient for now.
 */
public interface ResourceInfoServiceI {

	/**
	 * Get all recorded metadata events.
	 * 
	 * @return List of String
	 */
	public List getInfoLog();

	/**
	 * Record a metadata information about the resource.
	 * 
	 * @param event event details, never null
	 */
	public void addEvent(String event);

	/**
	 * @return publish ID (or null if unknown)
	 */
	public String getPublishId();

}
