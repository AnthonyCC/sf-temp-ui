package com.freshdirect.cms.ui.model.bulkload;

import java.io.Serializable;

public class GwtBulkLoadHeaderCell implements Serializable {
	private static final long serialVersionUID = 2621740198984285293L;

	private String columnName;
	transient private String attributeName;
	transient private GwtBulkLoadPreviewStatus columnStatus;

	public GwtBulkLoadHeaderCell() {
	}

	public GwtBulkLoadHeaderCell(String columnName, String attributeName, GwtBulkLoadPreviewStatus status) {
		this.columnName = columnName;
		this.attributeName = attributeName;
		this.columnStatus = status;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public GwtBulkLoadPreviewStatus getColumnStatus() {
		return columnStatus;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		String classSimpleName = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		buf.append(classSimpleName);
		buf.append('{');
		buf.append("columnName=");
		buf.append(getColumnName());
		buf.append(", attributeName=");
		buf.append(getAttributeName());
		buf.append('}');
		return buf.toString();
	}
}
