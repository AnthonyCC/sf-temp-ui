package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class TextConstraint extends RegexConstraint {

	private static final String ERROR_MESSAGE = "Valid text is required.";
	// Allow every non-empty characters except &,<,>,",/,#,%,=
	private static final String REGEX = "^(?!.*[&|<|>|\\\"|/|#|%|=])\\w.*$";

	public TextConstraint(boolean optional) {
		super(optional);
	}

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

	@Override
	protected String getRegexp() {
		return REGEX;
	}

}
