package com.freshdirect.listadmin.db;

/**
 * Not a real clause, just a placeholder thing so the front end can
 * know to render certain fields with a dateTimePicker
 * 
 * @author lPekowsky
 *
 */
public class DateTimeClause extends Clause {
	private static final long serialVersionUID = -3325491613483527966L;
	
	private String column;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

}
