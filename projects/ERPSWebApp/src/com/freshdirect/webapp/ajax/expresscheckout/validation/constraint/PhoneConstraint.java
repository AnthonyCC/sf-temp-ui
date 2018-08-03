package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

import com.freshdirect.common.address.PhoneNumber;

/*
 * You will find that in practice, this validation is being overriden by the phone validator in form.js
 */
public class PhoneConstraint extends AbstractConstraint<String> {

    private static final String ERROR_MESSAGE = "Valid phone number is required." + "<!-- PhoneConstraint -->";

    public PhoneConstraint(boolean optional) {
        super(optional);
    }

    @Override
    public String getErrorMessage() {
        return ERROR_MESSAGE;
    }

    @Override
    public boolean isValid(String value) {
        String phoneNumber = PhoneNumber.normalize(value);
        boolean valid = phoneNumber.length() == 10 && !phoneNumber.startsWith("0");

        if (isOptional() && "".equals(value)) {
            valid = true;
        }

        if (isForceInValid()) {
            valid = false;
        }

        return valid;
    }

}
