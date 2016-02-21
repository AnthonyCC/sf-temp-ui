package com.freshdirect.payment;

public class ActionError {

	public final static String GENERIC = "genericError";

	private final String type;
	private final String description;

	public ActionError(String description) {
		this(GENERIC, description);
	}

	public ActionError(String type, String description) {
		this.type = type;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return "ActionError[" + type + ",'" + description + "']";
	}

}
