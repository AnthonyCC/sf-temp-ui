package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class RoutingNumberConstraint extends RegexConstraint {

	public RoutingNumberConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Valid routing number is required.";
	private static final String REGEX = "^\\d{9}$";

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

	@Override
	protected String getRegexp() {
		return REGEX;
	}

}
