package com.freshdirect.rules;


public class RuleRef implements ConditionI {

	private String id;

	public RuleRef() {
	}

	public RuleRef(String id) {
		this.id = id;
	}

	public boolean evaluate(Object target, RuleRuntimeI ctx) {
		Rule r = ctx.getRule(id);
		if (r == null) {
			throw new RulesRuntimeException("Cannot find Rule for PK: " + id);
		}

		return ctx.evaluateRule(target, r);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean validate() {
		//TODO implement
		return true;
	}

	public boolean equals(Object o) {
		if (o instanceof RuleRef) {
			return ((RuleRef) o).id == this.id;
		}
		return false;
	}

}