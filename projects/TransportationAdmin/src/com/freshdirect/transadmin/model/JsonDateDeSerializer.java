package com.freshdirect.transadmin.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;


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