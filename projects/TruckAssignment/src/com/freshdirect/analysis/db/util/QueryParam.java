package com.freshdirect.analysis.db.util;

public class QueryParam {
	private int type;
	private Object value;

	public QueryParam(int type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "QueryParam [type=" + type + ", value=" + value + "]";
	}
}
