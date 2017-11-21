package com.freshdirect.cms.core.converter;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.domain.Time;

@Service
public class ScalarValueToSerializedValueConverter {

    private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.date();
    private static final DateTimeFormatter TIME_FORMAT = ISODateTimeFormat.hourMinute();

    /**
     * Converts the typed attribute value to string
     *
     * @param attribute
     *            the scalar attribute
     * @param value
     *            the typed value of the scalar
     * @return a string, representing the attribute value
     */
    public String convert(Scalar attribute, Object value) {
        String returnValue = null;
        if (attribute.getType().isAssignableFrom(Date.class)) {
            returnValue = DATE_FORMAT.print(new DateTime(value));
        } else if (attribute.getType().isAssignableFrom(Time.class)) {
            returnValue = TIME_FORMAT.print(new DateTime(value));
        } else if (value != null) {
            returnValue = String.valueOf(value);
        }
        return returnValue;
    }
}
