package com.freshdirect.temails;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.rules.OgnlCondition;
import com.freshdirect.rules.RulesRuntimeException;

public class BaseParser implements ParserI {

	private final static Logger LOGGER = LoggerFactory.getInstance(OgnlCondition.class);
	
	private String expression;
	private Object formattedObj;
	private String order;
	private String condition;
	private String id;
	private String parentId;
	
	
	public BaseParser(String expression){
		this.expression=expression;
	}
	
	@Override
	public String parse(Object target, TemailRuntimeI ct) {
		try {
			
			if(this.getCondition()!=null && this.getCondition().trim().length()>0){
				Boolean b=(Boolean)Ognl.getValue(this.getCondition(), new OgnlContext(), target);
				if(!b.booleanValue()) return "";
			}
			
			String result = (String) Ognl.getValue(this.getFormattedObject(), new OgnlContext(), target);		
			
			return result;
		} catch (OgnlException e) {
			LOGGER.warn("Failed to evaluate expression '" + expression + "'", e);
			//return "Error";
			throw new TEmailRuntimeException(e);
		}
	}
	
	
	private Object getFormattedObject() {
		if (this.formattedObj != null) {
			return this.formattedObj;
		}
		try {
			this.formattedObj = Ognl.parseExpression(expression);
			return this.formattedObj;
		} catch (OgnlException e) {
			throw new RulesRuntimeException(e);
		}
	}

	@Override
	public String getOrder() {
		// TODO Auto-generated method stub
		return this.order;
	}

	@Override
	public void setOrder(String order) {
		// TODO Auto-generated method stub
        this.order=order; 
	}

	public boolean validate() {
		try {
			Ognl.parseExpression(this.expression);
			return true;
		} catch (OgnlException e) {
			return false;
		}
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public String getParentId() {
		// TODO Auto-generated method stub
		return this.parentId;
	}

	@Override
	public void setChildParser(ParserI p) {
		// TODO Auto-generated method stub
		
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public ParserI getChildParser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
}