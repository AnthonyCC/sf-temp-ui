package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class ZipCodeConstraint extends RegexConstraint {

	public ZipCodeConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Valid zip code is required.";
	private static final String REGEX = "^\\d\\d\\d\\d\\d$";

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

	@Override
	protected String getRegexp() {
		return REGEX;
	}
}