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
    public List<ValidationError> validateByDatas(Map<String, String> datas, Map<String, Constraint<String>> constraints) {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        for (Map.Entry<String, String> dataEntry : datas.entrySet()) {
            final String key = dataEntry.getKey();
            final String data = dataEntry.getValue();
            if (constraints.containsKey(key)) {
                final Constraint<String> constraint = constraints.get(key);
                if (!constraint.isValid(data) || (null != data && key.equalsIgnoreCase("cardHolderName") && Character.isDigit(data.trim().charAt(0)) )) {
                    validationErrors.add(new ValidationError(key, constraint.getErrorMessage()));
                }
            }
        }
        return validationErrors;
    }

    @Override
    public List<ValidationError> validateByConstraints(Map<String, String> datas, Map<String, Constraint<String>> constraints) {
        final List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        for (Map.Entry<String, Constraint<String>> constraintEntry : constraints.entrySet()) {
            final String key = constraintEntry.getKey();
            final Constraint<String> constraint = constraintEntry.getValue();
            if (datas.containsKey(key)) {
                final String data = datas.get(key);
                if (!constraint.isValid(data)) {
                    validationErrors.add(new ValidationError(key, constraint.getErrorMessage()));
                }
            }
        }
        return validationErrors;
    }

}
