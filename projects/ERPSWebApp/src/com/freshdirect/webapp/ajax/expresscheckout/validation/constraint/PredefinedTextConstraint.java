package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

import java.util.Set;

public class PredefinedTextConstraint extends AbstractConstraint<String> {

	private static final String ERROR_MESSAGE = "At least one option must be selected.";

	private final Set<String> predefinedText;

	public PredefinedTextConstraint(boolean optional, final Set<String> predefinedText) {
		super(optional);
		this.predefinedText = predefinedText;
	}

	@Override
	public boolean isValid(String value) {
		boolean valid = predefinedText.contains(value);

		if (isOptional() && "".equals(value)) {
			valid = true;
		}
		if (isForceInValid()) {
			valid = false;
		}

		return valid;
	}

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

}
