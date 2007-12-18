/*
 * Created on Mar 7, 2005
 */
package com.freshdirect.cms.ui.tapestry.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.cms.EnumDefI;

/**
 * @author vszathmary
 */
public class EnumDefSelectionModel implements IPropertySelectionModel {

	private final List values;
	private final List labels;

	public EnumDefSelectionModel(EnumDefI enumDef, boolean allowNull) {
		Map enumValues = enumDef.getValues();
		values = new ArrayList();
		labels = new ArrayList();
		if (allowNull) {
			values.add(null);
			labels.add("");
		}
		values.addAll(enumValues.keySet());
		labels.addAll(enumValues.values());
	}

	public int getOptionCount() {
		return labels.size();
	}

	public Object getOption(int index) {
		return values.get(index);
	}

	public String getLabel(int index) {
		return (String) labels.get(index);
	}

	public String getValue(int index) {
		return Integer.toString(index);
	}

	public Object translateValue(String value) {
		int index = Integer.parseInt(value);
		return values.get(index);
	}

}