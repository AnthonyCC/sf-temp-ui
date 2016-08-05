package com.freshdirect.mobileapi.api;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateConverter implements Converter<String, Date>{

    public Date convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return new Date(Long.parseLong(source) * 1000);
    }
}
