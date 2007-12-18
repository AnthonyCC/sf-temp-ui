package com.freshdirect.listadmin.db;

import java.io.Serializable;
import java.util.Map;

import com.freshdirect.listadmin.query.ClauseI;
import com.freshdirect.listadmin.query.QueryContextI;
import com.freshdirect.listadmin.render.sql.SqlQueryRenderer;

public class Clause implements ClauseI, Serializable {
	private static final long serialVersionUID = -2427069395702613762L;

	public final static String CONSTANT_TYPE = "constant";
	public final static String PARAM_TYPE    = "param";
	public final static String QUERY_TYPE    = "query";
	public final static String STATIC_DROPDOWN_TYPE = "staticDropdown";
	public final static String SELECT_DROPDOWN_TYPE = "selectDropdown";
	public final static String QUERY_DROPDOWN_TYPE  = "queryDropdown";
	
	private String clauseId;
	private String templateId;
	private String type;
	private String column;
	private String param;
	private int    operatorId;
	
	public Clause() {}
	public Clause(String column, int operatorId) {
		this.column     = column;
		this.operatorId = operatorId;
	}
	
	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getClauseId() {
		return clauseId;
	}
	public void setClauseId(String clauseId) {
		this.clauseId = clauseId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public static Clause buildClause(String columnName, Map values) {
		Clause c = null;

		// This can probably be greatly streamlined by maintaining a Map of type to 
		// constructors, where each constructor takes the value map and pulls out what
		// it needs.  Or something like that.
		String type     = (String) values.get(columnName + ":type");
		String operator = (String) values.get(columnName + ":operator");
		
		if(CONSTANT_TYPE.equals(type)) {
			c = new ConstantClause(columnName,Integer.parseInt(operator),(String) values.get(columnName + ":constant"));
		} else if(PARAM_TYPE.equals(type)) {
			c = new ParamClause(columnName,Integer.parseInt(operator),(String) values.get(columnName + ":param"));
		} else if(STATIC_DROPDOWN_TYPE.equals(type)) {
			c = new StaticDropdownClause(columnName,
					Integer.parseInt(operator),
					(String) values.get(columnName + ":options"),
					(String) values.get(columnName + ":param"));
		} else if(SELECT_DROPDOWN_TYPE.equals(type)) {
			c = new SelectDropdownClause(columnName,
					Integer.parseInt(operator),
					(String) values.get(columnName + ":dropDownQueryName"),
					(String) values.get(columnName + ":dropDownQueryNameColumn"),
					(String) values.get(columnName + ":dropDownQueryValueColumn"));
		}
		
		return c;
	}

	public boolean getRunnable(QueryContextI queryContext) {
		return true;
	}
	
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
	private Object runtimeValue;
	public void setRuntimeValue(Object runtimeValue) {
		this.runtimeValue = runtimeValue;
	}
	public Object getRuntimeValue() {
		return runtimeValue;
	}
		
	public String getXml() {
		return "";
	}
	
	public String toString() {
		return getColumn() + " " + SqlQueryRenderer.operators[getOperatorId()] + " ";
	}
}
