package com.freshdirect.dlvadmin.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.BaseValidator;
import org.apache.tapestry.valid.ValidatorException;

import com.freshdirect.framework.util.TimeOfDay;

public class TimeValidator extends BaseValidator {

	private SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");

	public Object toObject(IFormComponent field, String value) throws ValidatorException {
		if (value == null) {
			return null;
		}
		try {
			return new TimeOfDay(timeFormatter.parse(value));
		} catch (ParseException e) {
			throw new ValidatorException("Invalid " + field.getDisplayName() + " - " + e.getMessage());
		}
	}

	public String toString(IFormComponent field, Object value) {
		return value == null ? null : ((TimeOfDay) value).getAsString();
	}

}
