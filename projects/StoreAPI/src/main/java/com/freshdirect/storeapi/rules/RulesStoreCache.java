package com.freshdirect.storeapi.rules;

import java.util.Map;

import com.freshdirect.rules.Rule;

/**
 * @author knadeem Date Apr 8, 2005
 */
public class RulesStoreCache extends RulesStoreProxy {

    private Map<String, Rule> rules;
	
	private long REFRESH_PERIOD = 0;
	private long lastRefresh = 0;

	public RulesStoreCache(RulesStoreI store) {
		super(store);
	}
	
	public RulesStoreCache(RulesStoreI store, int refresh) {
		super(store);
		this.REFRESH_PERIOD = 1000 * 60 * refresh;
	}

    @Override
    public Map<String, Rule> getRules() {
		this.refreshRules();
		return rules;
	}

	@Override
    public Rule getRule(String ruleId) {
		return getRules().get(ruleId);
	}

	@Override
    public void storeRule(Rule rule) {
		super.storeRule(rule);
		this.rules = null;
	}

	@Override
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
