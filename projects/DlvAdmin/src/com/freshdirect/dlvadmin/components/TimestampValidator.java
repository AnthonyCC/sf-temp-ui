package com.freshdirect.dlvadmin.components;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.BaseValidator;
import org.apache.tapestry.valid.ValidatorException;

public class TimestampValidator extends BaseValidator {

	private SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");

	public Object toObject(IFormComponent field, String value) throws ValidatorException {
		if (value == null) {
			return null;
		}
		try {
			return new Timestamp(timeFormatter.parse(value).getTime());
		} catch (ParseException e) {
			throw new ValidatorException("Invalid " + field.getDisplayName() + " - " + e.getMessage());
		}
	}

	public String toString(IFormComponent field, Object value) {
		return value == null ? null : timeFormatter.format(value);
	}

}
