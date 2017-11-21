package com.freshdirect.storeapi.rules;

import java.util.Collection;
import java.util.Map;

import com.freshdirect.rules.Rule;

/**
 * Service interface for rules engine.
 * Will have one instance per subsystem. 
 */
public interface RulesEngineI extends RulesStoreI {

	/**
	 * 
	 * @param context domain-specific context
	 * @return Map of String ruleId -> Object outcome (for rules that have fired).
	 */
	public Map<String, Rule> evaluateRules(Object context);

	/**
	 * Get all rules that have a RuleRef pointing to ruleId.
	 * 
	 * @param ruleId
	 * @return Collection of Rules
	 */
	public Collection<Rule> getDependentRules(String ruleId);

}
