package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;


public class PhoneExtensionConstraint extends RegexConstraint {

    private static final String ERROR_MESSAGE = "Use letters and numbers only and max. 5 characters";
    // Allow every non-empty characters except &,<,>,",/,#,%,= and max length of 5 characters
    private static final String REGEX = "^(?!.*[&|<|>|\\\"|/|#|%|=])\\w.{0,4}$";

    public PhoneExtensionConstraint(boolean optional) {
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
