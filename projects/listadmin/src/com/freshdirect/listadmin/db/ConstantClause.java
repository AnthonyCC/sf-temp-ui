package com.freshdirect.listadmin.db;

public class ConstantClause extends Clause {
	private static final long serialVersionUID = -3680119997428853492L;
	
	private String constant;

	public ConstantClause() {}
	
	public ConstantClause(String column, int operatorId, String constant) {
		super(column, operatorId);
		this.constant   = constant;
	}
	
	public String getConstant() {
		return constant;
	}
	public void setConstant(String constant) {
		this.constant = constant;
	}
	
	public String toString() {
		return super.toString() + "constant value: " + constant;
	}
	
	public String getXml() {
		return "<constant field='" + getColumn() + "' " +
		       "operatorId='" + getOperatorId()  + "' " +
		       "constant='" + getConstant() + 
		       "'/>";
	}
}
