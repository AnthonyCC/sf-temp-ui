package com.freshdirect.dlvadmin;

import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

public class SimpleSelectionModel implements IPropertySelectionModel {

	private Object[] entries;

	public SimpleSelectionModel(List values) {
		this( values.toArray( new Object[values.size()]) );
	}

	public SimpleSelectionModel(Object values[]) {
		this.entries = new Object[values.length+1];
		this.entries[0] = null;
		for(int i = 1; i < entries.length; i++ ){
			this.entries[i] = values[i-1];
		}
	}

	public int getOptionCount() {
		return entries.length;
	}

	public Object getOption(int index) {
		return entries[index];
	}

	public String getLabel(int index) {
		return this.getLabel(entries[index]);
	}

	/**
	 * Template method, override this.
	 */
	protected String getLabel(Object o) {
		return o==null ? "" : o.toString();
	}

	public String getValue(int index) {
		return String.valueOf(index);
	}

	public Object translateValue(String value) {
		try {
			return entries[ Integer.parseInt(value) ];
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
