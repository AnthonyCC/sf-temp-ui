package com.freshdirect.listadmin.db;

public class ParamClause extends Clause {
	private static final long serialVersionUID = -4199350779013982526L;
	
	private String column;
	private int operatorId;
	private String param;	
	
	public ParamClause() {}
	
	public ParamClause(String column, int operatorId, String param) {
		this.column     = column;
		this.operatorId = operatorId;
		this.param      = param;
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

	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
		
	public String toString() {
		return super.toString() + "parameter named " + param;
	}
	
	public String getXml() {
		return "<param field='" + getColumn() + "' " +
		       "operatorId='" + getOperatorId()  + "' " +
		       "parameter='" + getParam() + 
		       "'/>";
	}
}
