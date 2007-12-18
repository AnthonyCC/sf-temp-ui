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

	public Map evaluateRules(Object target) {
		Map firedRules = new HashMap();
		Map rules = getRules();
		RuleRuntimeI rt = new RuleRuntime(rules);
		for (Iterator i = rules.values().iterator(); i.hasNext();) {
			Rule r = (Rule) i.next();
			boolean res = rt.evaluateRule(target, r);
			if (res) {
				firedRules.put(r.getId(), r);
			}
		}
		return firedRules;
	}

	public Collection getDependentRules(String ruleId) {
		List l = new ArrayList();
		Map rules = getRules();
		for (Iterator i = rules.values().iterator(); i.hasNext();) {
			Rule r = (Rule) i.next();
			for (Iterator j = r.getConditions().iterator(); j.hasNext();) {
				Object o = j.next();
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
