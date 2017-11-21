package com.freshdirect.cms.ui.editor.publish.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;

@Converter
public class StorePublishMessageSeverityConverter implements AttributeConverter<StorePublishMessageSeverity, Integer>  {

    @Override
    public Integer convertToDatabaseColumn(StorePublishMessageSeverity attribute) {
        return attribute.code;
    }

    @Override
    public StorePublishMessageSeverity convertToEntityAttribute(Integer messageSeverity) {
        StorePublishMessageSeverity convertedValue = StorePublishMessageSeverity.UNKNOWN;
        if (messageSeverity != null) {
            convertedValue = StorePublishMessageSeverity.valueOf(messageSeverity);
        }
        return convertedValue;
    }

}
