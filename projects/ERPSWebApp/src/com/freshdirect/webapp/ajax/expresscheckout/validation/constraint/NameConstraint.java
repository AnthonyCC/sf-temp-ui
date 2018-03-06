package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class NameConstraint extends RegexConstraint {

	public NameConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Please enter name exactly as it appears on card.";

	// Allow every non-empty characters except &,<,>,",#,%,=
	private static final String REGEX = "^(?!.*[&|<|>|\\\"|/|#|%|=])\\w.*$";

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

	@Override
	protected String getRegexp() {
		return REGEX;
	}
}
