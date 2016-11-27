package com.freshdirect.dlvadmin;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

public class ObjectSelectionModel implements IPropertySelectionModel {
	
	private static class Entry {
		final Object value;
		final String label;

		Entry(Object value, String label) {
			this.value = value;
			this.label = label;
		}

	}

	private List entries = new ArrayList();

	public void add(Object value, String label) {
		Entry entry = new Entry(value, label);
		entries.add(entry);
	}

	public int getOptionCount() {
		return entries.size();
	}

	private Entry get(int index) {
		return (Entry) entries.get(index);
	}

	public Object getOption(int index) {
		return get(index).value;
	}

	public String getLabel(int index) {
		return get(index).label;
	}

	public String getValue(int index) {
		return String.valueOf(index);
	}

	public Object translateValue(String value) {
		int index = Integer.parseInt(value);
		return get(index).value;
	}
}