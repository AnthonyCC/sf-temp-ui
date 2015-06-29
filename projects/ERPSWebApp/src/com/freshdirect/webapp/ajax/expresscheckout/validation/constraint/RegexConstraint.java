package com.freshdirect.webapp.ajax.expresscheckout.validation.constraint;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexConstraint extends AbstractConstraint<String> {

	public RegexConstraint(boolean optional) {
		super(optional);
	}

	protected abstract String getRegexp();

	@Override
	public boolean isValid(String value) {
		final Pattern pattern = compile(getRegexp(), CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(value);
		boolean valid = matcher.matches();

		if (isOptional() && "".equals(value)) {
			valid = true;
		}
		if (isForceInValid()) {
			valid = false;
		}

		return valid;
	}

}
