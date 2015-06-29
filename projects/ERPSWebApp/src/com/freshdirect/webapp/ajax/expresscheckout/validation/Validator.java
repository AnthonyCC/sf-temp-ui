package com.freshdirect.webapp.ajax.expresscheckout.validation;

import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.Constraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public interface Validator {

	List<ValidationError> validate(Map<String, String> value, Map<String, Constraint<String>> constraints);
}
