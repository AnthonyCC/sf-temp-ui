package com.freshdirect.rules;

import java.util.Map;

/**
 * @author knadeem Date Apr 8, 2005
 */
public class RulesStoreProxy implements RulesStoreI {

	private final RulesStoreI store;

	public RulesStoreProxy(RulesStoreI store) {
		this.store = store;
	}

	public String getSubsystem() {
		return this.store.getSubsystem();
	}

	public Map getRules() {
		return store.getRules();
	}

	public Rule getRule(String ruleId) {
		return store.getRule(ruleId);
	}

	public void storeRule(Rule rule) {
		this.store.storeRule(rule);
	}

	public void deleteRule(String ruleId) {
		this.store.deleteRule(ruleId);
	}
}
