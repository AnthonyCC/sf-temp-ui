package com.freshdirect.temails;

import java.util.Map;

/**
 * Service interface for rules engine.
 */
public interface TEmailStoreI {

	public String getSubsystem();
	
	/**
	 * Get all rules for the subsystem.
	 * 
	 * @return Map of String ruleId -> Rules
	 */
	public Map getTemplates();

	public Template getTemplate(String templateId);

	public void storeTemplate(Template rule);

	public void deleteTemplate(String templateId);

}