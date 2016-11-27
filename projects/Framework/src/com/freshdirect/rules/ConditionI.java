package com.freshdirect.rules;

import java.io.Serializable;

public interface ConditionI extends Serializable {

	public boolean evaluate(Object target, RuleRuntimeI ctx);

	public boolean validate();

}