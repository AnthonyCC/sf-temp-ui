package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;
/*
 * You will find that in practice, this validation is being overriden by the zipcode  validator in form.js
 */
public class ZipCodeConstraint extends RegexConstraint {

	public ZipCodeConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Valid zip code is required." +
			"<!-- ZipCodeConstraint -->";;
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