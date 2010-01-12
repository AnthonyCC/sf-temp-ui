package com.freshdirect.cms.ui.model.bulkload;

public enum BulkLoadReverseRelationship {
	NO_CHANGE, ADD, REMOVE;
	
	@Override
	public String toString() {
		if (this == ADD)
			return "+";
		else if (this == REMOVE)
			return "-";
		else
			return "";
	}
}
