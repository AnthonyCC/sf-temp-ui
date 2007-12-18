package com.freshdirect.dlvadmin;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;


/** @deprecated */
public class StringSelectionModel implements IPropertySelectionModel {
	
	private static class Entry {
		final String primaryKey;
		final String label;

		Entry(String primaryKey, String label) {
			this.primaryKey = primaryKey;
			this.label = label;
		}

	}

	private List entries = new ArrayList();

	public void add(String key, String label) {
		Entry entry;

		entry = new Entry(key, label);
		entries.add(entry);
	}

	public int getOptionCount() {
		return entries.size();
	}

	private Entry get(int index) {
		return (Entry) entries.get(index);
	}

	public Object getOption(int index) {
		return get(index).primaryKey;
	}

	public String getLabel(int index) {
		return get(index).label;
	}

	public String getValue(int index) {
		String primaryKey;

		primaryKey = get(index).primaryKey;

		if (primaryKey == null)
			return "";

		return primaryKey.toString();
	}

	public Object translateValue(String value) {
		if (value.equals(""))
			return null;

		return value;
	}
}