package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

public class DeliveryInstructionsConstraint extends RegexConstraint {
	
    private static final String ERROR_MESSAGE = "Delivery information can contain letters and numbers only maximum length is 255 characters and is required.";
    
    // Allow every non-empty characters except &,<,>,",/,#,%,= and max lenght of 255 characters
    private static final String REGEX = "^(?!.*[&|<|>|\\\"|/|#|%|=])\\w.{0,254}$";

	public DeliveryInstructionsConstraint(boolean optional) {
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
