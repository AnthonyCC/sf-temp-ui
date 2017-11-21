package com.freshdirect.storeapi.rules;

import java.util.Map;

import com.freshdirect.rules.Rule;

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

	@Override
	public Map<String,Rule> getRules() {
		return store.getRules();
	}

	@Override
	public Rule getRule(String ruleId) {
		return store.getRule(ruleId);
	}

	@Override
	public void storeRule(Rule rule) {
		this.store.storeRule(rule);
	}

	@Override
	public void deleteRule(String ruleId) {
		this.store.deleteRule(ruleId);
	}
}
