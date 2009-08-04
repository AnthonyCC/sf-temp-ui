package com.freshdirect.crm.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.freshdirect.enums.EnumModel;
import com.freshdirect.framework.core.PrimaryKey;

public class CriteriaBuilder {
	private final StringBuffer sb = new StringBuffer();
	private final List params = new ArrayList();

	public void addEnum(String fieldName, EnumModel value) {
		if (value != null) {
			this.addString(fieldName, value.getCode());
		}
	}

	public void addPK(String fieldName, PrimaryKey value) {
		if (value != null) {
			this.addString(fieldName, value.getId());
		}
	}

	public void addString(String fieldName, String value) {
		addObject(fieldName, value);
	}

	public void addObject(String fieldName, Object value) {
		if (value != null) {
			addAnd();
			sb.append(fieldName).append("=?");
			params.add(value);
		}
	}

	public void addInEnum(String fieldName, EnumModel[] values) {
		String[] codes = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			codes[i] = values[i].getCode();
		}
		this.addInString(fieldName, codes);
	}

	public void addInString(String fieldName, String[] values) {
		if (values.length == 0) {
			return;
		}
		if (values.length == 1) {
			this.addString(fieldName, values[0]);
			return;
		}
		addAnd();
		sb.append(fieldName).append(" IN (");
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append('?');
			this.params.add(values[i]);
		}
		sb.append(')');
	}

	public void addSql(String sql, Object[] values) {
		addAnd();
		sb.append(sql);
		params.addAll(Arrays.asList(values));
	}
	
	public void addSql(String sql, Date date){
		addAnd();
		sb.append(sql);
		params.add(date);
	}

	private void addAnd() {
		if (!params.isEmpty()) {
			sb.append(" AND ");
		}
	}

	public String getCriteria() {
		return this.sb.toString();
	}

	public Object[] getParams() {
		return this.params.toArray();
	}

}