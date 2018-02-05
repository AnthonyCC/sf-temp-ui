package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

/*
 * You will find that in practice, this validation is being overriden by the phone validator in form.js
 */
public class PhoneConstraint extends RegexConstraint {

	public PhoneConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Valid phone number is required." +
			"<!-- PhoneConstraint -->";
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
