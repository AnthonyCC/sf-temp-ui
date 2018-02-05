package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class CreditCardConstraint extends RegexConstraint {

	public CreditCardConstraint(boolean optional) {
		super(optional);
	}

	private static final String ERROR_MESSAGE = "Valid credit card number is required."+ 
			"<!-- CreditCardConstraint -->";

	/**
	 * Valid credit card regex from OWASP Validation Regex Repository https://www.owasp.org/index.php/OWASP_Validation_Regex_Repository
	 */
	private static final String REGEX = "^((4\\d{3})|(5[1-5]\\d{2})|(6011)|(7\\d{3}))-?\\d{4}-?\\d{4}-?\\d{4}|3[4,7]\\d{13}|(222[1-9]|2[3-6]\\d{2}|27[0-1]\\d|2720)\\d{12}$";
	
			//"^((4\\d{3})|(5[1-5]\\d{2})|(6011)|(7\\d{3}))-?\\d{4}-?\\d{4}-?\\d{4}|3[4,7]\\d{13}$";
	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

	@Override
	protected String getRegexp() {
		return REGEX;
	}
	
	

}
