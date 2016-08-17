package com.freshdirect.mobileapi.api.validation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMethod;

import com.freshdirect.mobileapi.controller.data.request.PasswordMessageRequest;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

@Component
public class PasswordValidator implements Validator {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ValidationDtoConverter validationDtoConverter;

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordMessageRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordMessageRequest passwordRequest = (PasswordMessageRequest) target;

        switch (RequestMethod.valueOf(request.getMethod())) {
            case GET:
                break;
            case PUT:
                break;
            case DELETE:
                break;
            case POST:
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "BAD_EMAIL", "Email must not be null or whitespace");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "token", "BAD_TOKEN", "Token must not be null or whitespace");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "BAD_PASSWORD", "Password must not be null or whitespace");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "BAD_CONFIRM_PASSWORD", "Confirm password must not be null or whitespace");

                if (!errors.hasErrors() && passwordRequest.getPassword().length() < 6) {
                    errors.rejectValue("password", "PASSWORD_SHORT", SystemMessageList.MSG_PASSWORD_LENGTH);
                }

                if (!errors.hasErrors() && !passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {
                    errors.rejectValue("password", "PASSWORDS_NOT_EQUAL", "Password does not equal to confirm password");
                }

                break;
            default:
                break;
        }

        if (errors.hasErrors()) {
            validationDtoConverter.createErrorResults(errors);
        }

    }

}
