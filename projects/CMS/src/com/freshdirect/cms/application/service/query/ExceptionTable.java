/*
 * Created on Mar 15, 2005
 */
package com.freshdirect.cms.application.service.query;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.meta.AttributeDef;

/**
 * {@link com.freshdirect.cms.ITable} implementation that exposes info
 * about an Exception.
 */
class ExceptionTable implements ITable {

	private final static AttributeDefI[] DEF = {new AttributeDef(EnumAttributeType.STRING, "ERROR", "Error occured")};

	private final List rows;

	public ExceptionTable(Exception e) {
		//StringWriter sw = new StringWriter();
		//e.printStackTrace(new PrintWriter(sw));
		//final String msg = sw.getBuffer().toString();
		final String msg = e.getMessage();
		rows = new ArrayList(1);
		rows.add(new ITable.Row() {
			public Object[] getValues() {
				return new Object[] {msg};
			}
		});
	}

	public AttributeDefI[] getColumnDefinitions() {
		return DEF;
	}

	public List getRows() {
		return rows;
	}

}