package com.freshdirect.listadmin.db;


public class JoinClause extends Clause {
	private String parentColumn;
	private String childColumn;
	
	public JoinClause() {}
	
	public String getChildColumn() {
		return childColumn;
	}

	public void setChildColumn(String childColumn) {
		this.childColumn = childColumn;
	}

	public String getParentColumn() {
		return parentColumn;
	}

	public void setParentColumn(String parentColumn) {
		this.parentColumn = parentColumn;
	}
	
	public String getXml() {
		return "<otherField field='" + getParentColumn() + 
		       "' otherField='" + getChildColumn() + "'>";
	}
}
