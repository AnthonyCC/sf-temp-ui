package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class AccountNumberConstraint extends RegexConstraint {

	public AccountNumberConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Account number should be 5 to 17 digits, no alpha chars.";
    private static final String REGEX="^\\d{5,17}$";
	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

	@Override
	protected String getRegexp() {
		return REGEX;
	}

}
