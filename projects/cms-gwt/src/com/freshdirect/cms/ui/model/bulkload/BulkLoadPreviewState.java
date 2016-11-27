package com.freshdirect.cms.ui.model.bulkload;

public enum BulkLoadPreviewState {
	UNKNOWN, NO_CHANGE, CREATE, UPDATE, DELETE, IGNORE_CELL, IGNORE_COLUMN, WARNING, ERROR_CELL, ERROR_ROW, ERROR_COLUMN, ERROR_SHEET;

	public boolean isOperation() {
		return this == CREATE || this == UPDATE || this == DELETE;
	}

	public boolean isError() {
		return this == ERROR_CELL || this == ERROR_ROW || this == ERROR_COLUMN || this == ERROR_SHEET;
	}

	public boolean isIgnore() {
		return this == IGNORE_COLUMN || this == IGNORE_CELL;
	}

	public boolean isColumnIssue() {
		return this == IGNORE_COLUMN || this == ERROR_COLUMN;
	}

	public boolean isHeaderIssue() {
		return isColumnIssue();
	}
}
