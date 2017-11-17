package com.freshdirect.cms.draft.converter;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.converter.SerializedScalarValueToObjectConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.Scalar;

@Service
public class AttributeValueToStringConverter {

    public String convert(Attribute attribute, Object value) {
        String attributeValue;
        if (attribute instanceof Scalar) {
            Scalar scalar = (Scalar) attribute;
            if (scalar.getType().isAssignableFrom(Date.class)) {
                attributeValue = SerializedScalarValueToObjectConverter.YEAR_MONTH_DAY_FORMATTER.print(((Date) value).getTime());
            } else {
                attributeValue = String.valueOf(value);
            }
        } else {
            attributeValue = String.valueOf(value);
        }
        return attributeValue;
    }
}
