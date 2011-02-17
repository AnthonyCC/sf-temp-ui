package com.freshdirect.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RuleRuntime implements RuleRuntimeI {
	private static final long serialVersionUID = 7334316148936198977L;

	private final Map<String,Rule> rules;
	private final Map<String,Boolean> evaluations;
	private final Set<String> evalStack;

	public RuleRuntime(Map<String,Rule> rules) {
		this.rules = rules;
		this.evaluations = new HashMap<String,Boolean>();
		this.evalStack = new HashSet<String>();
	}

	public boolean evaluateRule(Object target, Rule r) {
		if (evalStack.contains(r.getId())) {
			throw new RulesRuntimeException("Circular Reference to Rule.id: " + r.getId());
		}

		Boolean result = evaluations.get(r.getId());
		if (result == null) {
			evalStack.add(r.getId());
			result = Boolean.valueOf(r.evaluate(target, this));
			this.evaluations.put(r.getId(), result);
			evalStack.remove(r.getId());
		}

		return result.booleanValue();
	}

	public Boolean getEvaluationResults(String id) {
		return this.evaluations.get(id);
	}

	public Rule getRule(String id) {
		return this.rules.get(id);
	}

}