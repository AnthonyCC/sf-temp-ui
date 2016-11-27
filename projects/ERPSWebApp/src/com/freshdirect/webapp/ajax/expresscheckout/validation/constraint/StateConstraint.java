package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class StateConstraint extends RegexConstraint {

	public StateConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Valid state is required.";
	private static final String REGEX = "^(AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY)$";

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

	@Override
	protected String getRegexp() {
		return REGEX;
	}

}
