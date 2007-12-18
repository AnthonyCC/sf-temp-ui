package com.freshdirect.rules;

import java.io.Serializable;

public interface RuleRuntimeI extends Serializable {

	public boolean evaluateRule(Object target, Rule rule);

	public Rule getRule(String id);

	public Boolean getEvaluationResults(String id);
}