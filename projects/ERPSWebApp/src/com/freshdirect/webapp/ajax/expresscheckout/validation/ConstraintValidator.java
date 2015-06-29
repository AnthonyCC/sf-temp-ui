package com.freshdirect.webapp.ajax.expresscheckout.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.Constraint;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public class ConstraintValidator implements Validator {

	private static final ConstraintValidator INSTANCE = new ConstraintValidator();

	private ConstraintValidator() {
	}

	public static ConstraintValidator defaultValidator() {
		return INSTANCE;
	}

	@Override
	public List<ValidationError> validate(Map<String, String> datas, Map<String, Constraint<String>> constraints) {
		final List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		for (Map.Entry<String, String> data : datas.entrySet()) {
			final String key = data.getKey();
			final String value = data.getValue();
			if (constraints.containsKey(key)) {
				final Constraint<String> constraint = constraints.get(key);
				boolean valid = constraint.isValid(value);
				if (!valid) {
					validationErrors.add(new ValidationError(key, constraint.getErrorMessage()));
				}
			}
		}
		return validationErrors;
	}

}
