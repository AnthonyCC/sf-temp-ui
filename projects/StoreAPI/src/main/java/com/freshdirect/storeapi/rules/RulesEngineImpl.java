package com.freshdirect.storeapi.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.rules.ConditionI;
import com.freshdirect.rules.Rule;
import com.freshdirect.rules.RuleRuntimeI;


public class RulesEngineImpl extends RulesStoreProxy implements RulesEngineI {

	public RulesEngineImpl(RulesStoreI store) {
		super(store);
	}

	@Override
	public Map<String,Rule> evaluateRules(Object target) {
		Map<String,Rule> firedRules = new HashMap<String,Rule>();
		Map<String,Rule> rules = getRules();
		RuleRuntimeI rt = new RuleRuntime(rules);
		for (Rule r : rules.values()) {
			boolean res = rt.evaluateRule(target, r);
			if (res) {
				firedRules.put(r.getId(), r);
			}
		}
		return firedRules;
	}

	@Override
	public Collection<Rule> getDependentRules(String ruleId) {
		List<Rule> l = new ArrayList<Rule>();
		Map<String,Rule> rules = getRules();
		for (Rule r : rules.values()) {
			for (ConditionI o : r.getConditions()) {
				if (o instanceof RuleRef) {
					RuleRef rr = (RuleRef) o;
					if (rr.getId().equals(ruleId)) {
						l.add(r);
					}
				}
			}
		}
		return l;
	}

}
