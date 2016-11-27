package com.freshdirect.rules.ui;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tapestry.form.IPropertySelectionModel;

public class LabelPropertySelectionModel implements IPropertySelectionModel {

	private String[] labels;
	private Object[] values;

	public LabelPropertySelectionModel(Map m, boolean allowNull) {
		int n = m.size();
		if (allowNull) {
			n++;
		}

		this.labels = new String[n];
		this.values = new Object[n];
		int idx = 0;
		if (allowNull) {
			idx++;
			labels[0] = "";
		}
		
		for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
			Entry e = (Entry) i.next();
			labels[idx] = (String) e.getKey();
			values[idx] = e.getValue();
			idx++;
		}
	}

	public int getOptionCount() {
		return values.length;
	}

	public Object getOption(int index) {
		return values[index];
	}

	public String getLabel(int index) {
		return labels[index];
	}

	public String getValue(int index) {
		return Integer.toString(index);
	}

	public Object translateValue(String value) {
		int index;
		index = Integer.parseInt(value);
		return values[index];
	}

}
