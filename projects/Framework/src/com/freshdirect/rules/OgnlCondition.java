package com.freshdirect.rules;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

public class OgnlCondition implements ConditionI {

	private final static Category LOGGER = LoggerFactory.getInstance(OgnlCondition.class);

	private String expression;
	private transient Object condition;

	public OgnlCondition() {
	}

	public OgnlCondition(String expression) {
		this.expression = expression;
	}

	public boolean evaluate(Object target, RuleRuntimeI ctx) {
		try {
			Boolean result = (Boolean) Ognl.getValue(this.getCondition(), new OgnlContext(), target);
			return result.booleanValue();
		} catch (OgnlException e) {
			LOGGER.warn("Failed to evaluate expression '" + expression + "'", e);
			return false;
		}
	}

	private Object getCondition() {
		if (this.condition != null) {
			return this.condition;
		}
		try {
			this.condition = Ognl.parseExpression(expression);
			return this.condition;
		} catch (OgnlException e) {
			throw new RulesRuntimeException(e);
		}
	}

	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean validate() {
		try {
			Ognl.parseExpression(this.expression);
			return true;
		} catch (OgnlException e) {
			return false;
		}
	}
}