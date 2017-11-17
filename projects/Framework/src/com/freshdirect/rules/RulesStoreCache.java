package com.freshdirect.rules;

import java.util.Map;

/**
 * @author knadeem Date Apr 8, 2005
 */
public class RulesStoreCache extends RulesStoreProxy {

	private Map rules;
	
	private long REFRESH_PERIOD = 0;
	private long lastRefresh = 0;

	public RulesStoreCache(RulesStoreI store) {
		super(store);
	}
	
	public RulesStoreCache(RulesStoreI store, int refresh) {
		super(store);
		this.REFRESH_PERIOD = 1000 * 60 * refresh;
	}

	public Map getRules() {
		this.refreshRules();
		return rules;
	}

	public Rule getRule(String ruleId) {
		return (Rule) getRules().get(ruleId);
	}

	public void storeRule(Rule rule) {
		super.storeRule(rule);
		this.rules = null;
	}

	public void deleteRule(String ruleId) {
		super.deleteRule(ruleId);
		this.rules.remove(ruleId);
	}
	
	private void refreshRules () {
		if (rules == null || System.currentTimeMillis() - lastRefresh > REFRESH_PERIOD) {
			this.rules = super.getRules();
			
			lastRefresh = System.currentTimeMillis();
		}
	}

}
