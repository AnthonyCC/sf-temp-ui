package com.freshdirect.cms.core.converter;

import java.util.Arrays;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.domain.Time;

public final class ScalarValueConverter {

    private static final DateTimeFormatter YEAR_MONTH_DAY_FORMATTER = ISODateTimeFormat.yearMonthDay();
    private static final DateTimeFormatter HOUR_MINUTE_FORMATTER = ISODateTimeFormat.hourMinute();

    private ScalarValueConverter() {
    }

    /**
     * Converts the typed attribute value to string
     *
     * @param attribute
     *            the scalar attribute
     * @param value
     *            the typed value of the scalar
     * @return a string, representing the attribute value
     */
    public static String serializeToString(Scalar attribute, Object value) {
        String returnValue = null;
        if (value != null && attribute.getType().isAssignableFrom(Date.class)) {
            returnValue = ScalarValueConverter.YEAR_MONTH_DAY_FORMATTER.print(new DateTime(value));
        } else if (value != null && attribute.getType().isAssignableFrom(Time.class)) {
            returnValue = ScalarValueConverter.HOUR_MINUTE_FORMATTER.print(new DateTime(value));
        } else if (value != null) {
            returnValue = String.valueOf(value);
        }
        return returnValue;
    }

    /**
     * Converts a serialized (String) attribute value to an Object,
     * which can be of the following actual types:
     *
     *  * null
     *  * String
     *  * Integer
     *  * Boolean
     *  * Date
     *  * Double
     *
     * @param attribute
     *            the attribute for which the value needs to be converted
     * @param serializedValue
     *            the serialized value of the attribute
     * @return an object, containing the typed value of the attribute as an Object
     */
    public static Object deserializeToObject(Scalar scalarAttribute, String serializedValue) {
        if (serializedValue == null) {
            return null;
        }

        Object returnValue = null;
        Class<?> scalarType = scalarAttribute.getType();

        if (scalarAttribute.isEnumerated()) {
            // enums support only two types
            if (scalarType.isAssignableFrom(String.class)) {
                returnValue = serializedValue;
            } else if (scalarType.isAssignableFrom(Integer.class)) {
                returnValue = Integer.parseInt(serializedValue);
            }

            if (returnValue != null) {
                // try to match decoded value to enums.
                if (!Arrays.asList(scalarAttribute.getEnumeratedValues()).contains(returnValue)) {
                    returnValue = null;
                }
            }
        } else {
            if (scalarType.isAssignableFrom(String.class)) {
                returnValue = serializedValue;
            } else if (scalarType.isAssignableFrom(Boolean.class)) {
                returnValue = Boolean.parseBoolean(serializedValue);
            } else if (scalarType.isAssignableFrom(Integer.class)) {
                returnValue = Integer.parseInt(serializedValue);
            } else if (scalarType.isAssignableFrom(Date.class)) {
                returnValue = YEAR_MONTH_DAY_FORMATTER.parseDateTime(serializedValue).toDate();
            } else if (scalarType.isAssignableFrom(Time.class)) {
                returnValue = HOUR_MINUTE_FORMATTER.parseDateTime(serializedValue).toDate();
            } else if (scalarType.isAssignableFrom(Double.class)) {
                returnValue = Double.parseDouble(serializedValue);
            }
        }
        return returnValue;
    }
}
