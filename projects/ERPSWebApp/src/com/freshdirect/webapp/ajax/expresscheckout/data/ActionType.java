package com.freshdirect.webapp.ajax.expresscheckout.data;

public enum ActionType {

	ADD("add"), REMOVE("remove"), SELECT("select"), EDIT("edit"), GET("get");

	private final String actionName;

	private ActionType(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}
}
