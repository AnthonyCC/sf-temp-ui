package com.freshdirect.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RuleRuntime implements RuleRuntimeI {

	private final Map rules;
	private final Map evaluations;
	private final Set evalStack;

	public RuleRuntime(Map rules) {
		this.rules = rules;
		this.evaluations = new HashMap();
		this.evalStack = new HashSet();
	}

	public boolean evaluateRule(Object target, Rule r) {
		if (evalStack.contains(r.getId())) {
			throw new RulesRuntimeException("Circular Reference to Rule.id: " + r.getId());
		}

		Boolean result = (Boolean) evaluations.get(r.getId());
		if (result == null) {
			evalStack.add(r.getId());
			result = new Boolean(r.evaluate(target, this));
			this.evaluations.put(r.getId(), result);
			evalStack.remove(r.getId());
		}

		return result.booleanValue();
	}

	public Boolean getEvaluationResults(String id) {
		return (Boolean) this.evaluations.get(id);
	}

	public Rule getRule(String id) {
		return (Rule) this.rules.get(id);
	}

}