package com.freshdirect.transadmin.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


@Component
public class JsonDateDeSerializer extends JsonDeserializer<Date>{
 
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
 
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
 
        Date formattedDate = null;
		try {
			formattedDate = dateFormat.parse(jp.getText());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return formattedDate;
    }
 
}