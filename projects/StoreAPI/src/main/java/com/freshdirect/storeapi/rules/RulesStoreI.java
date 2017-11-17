package com.freshdirect.storeapi.rules;

import java.util.Map;

import com.freshdirect.rules.Rule;

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
	public Map<String,Rule> getRules();

	public Rule getRule(String ruleId);

	public void storeRule(Rule rule);

	public void deleteRule(String ruleId);

}
