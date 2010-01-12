package com.freshdirect.cms.ui.model.bulkload;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GwtBulkLoadHeader implements Serializable {
	private static final long serialVersionUID = 7309912011880204666L;

	private Map<Integer, GwtBulkLoadHeaderCell> cells;
	transient private GwtBulkLoadPreviewStatus sheetStatus;

	public GwtBulkLoadHeader() {
		this(new GwtBulkLoadPreviewStatus());
	}

	public GwtBulkLoadHeader(GwtBulkLoadPreviewStatus sheetStatus) {
		cells = new HashMap<Integer, GwtBulkLoadHeaderCell>();
		this.sheetStatus = sheetStatus;
	}

	public Map<Integer, GwtBulkLoadHeaderCell> getCells() {
		return cells;
	}

	public GwtBulkLoadPreviewStatus getSheetStatus() {
		return sheetStatus;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		String classSimpleName = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		buf.append(classSimpleName);
		buf.append(cells.toString());
		return buf.toString();
	}
}
