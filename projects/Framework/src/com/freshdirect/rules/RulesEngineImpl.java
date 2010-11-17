package com.freshdirect.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RulesEngineImpl extends RulesStoreProxy implements RulesEngineI {

	public RulesEngineImpl(RulesStoreI store) {
		super(store);
	}

	public Map<String,Rule> evaluateRules(Object target) {
		Map<String,Rule> firedRules = new HashMap<String,Rule>();
		Map<String,Rule> rules = (Map<String,Rule>) getRules();
		RuleRuntimeI rt = new RuleRuntime(rules);
		for (Rule r : rules.values()) {
			boolean res = rt.evaluateRule(target, r);
			if (res) {
				firedRules.put(r.getId(), r);
			}
		}
		return firedRules;
	}

	public Collection<Rule> getDependentRules(String ruleId) {
		List<Rule> l = new ArrayList<Rule>();
		Map<String,Rule> rules = (Map<String,Rule>) getRules();
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
