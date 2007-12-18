package com.freshdirect.rules;

import java.util.Map;

/**
 * Service interface for rules engine.
 */
public interface RulesStoreI {

	public String getSubsystem();
	
	/**
	 * Get all rules for the subsystem.
	 * 
	 * @return Map of String ruleId -> Rules
	 */
	public Map getRules();

	public Rule getRule(String ruleId);

	public void storeRule(Rule rule);

	public void deleteRule(String ruleId);

}
