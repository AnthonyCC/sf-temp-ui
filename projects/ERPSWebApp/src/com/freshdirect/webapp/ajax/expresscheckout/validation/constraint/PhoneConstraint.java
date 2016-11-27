package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class PhoneConstraint extends RegexConstraint {

	public PhoneConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Valid phone number is required.";
	private static final String REGEX = "^[\\d \\-()x]+$";

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

	@Override
	protected String getRegexp() {
		return REGEX;
	}

}
