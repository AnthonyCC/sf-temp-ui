package com.freshdirect.cms.core.converter;

import java.util.Arrays;
import java.util.Date;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.domain.Time;

@Service
public class SerializedScalarValueToObjectConverter {

    public static final DateTimeFormatter YEAR_MONTH_DAY_FORMATTER = ISODateTimeFormat.date();
    public static final DateTimeFormatter HOUR_MINUTE_FORMATTER = ISODateTimeFormat.hourMinute();

    /**
     * Converts a serialized (string) attribute value to a typed value;
     *
     * @param attribute
     *            the attribute for which the value needs to be converted
     * @param serializedValue
     *            the serialized value of the attribute
     * @return an object, containing the typed value of the attribute (for example a Boolean, or Integer)
     */
    public Object convert(Attribute attribute, String serializedValue) {
        Object returnValue = null;
        if (attribute instanceof Scalar) {
            Scalar scalarAttribute = (Scalar) attribute;
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

        }
        return returnValue;
    }
}
