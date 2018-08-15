package com.freshdirect.common.date;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonParseException;

public class SimpleDateDeserializer extends JsonDeserializer<Date> {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
		final String date = getTextValue(jp);
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			throw new JsonParseException("Unparseable date: " + date + ". Supported formats: yyyy-MM-dd", e);
		}
	}
	
	private String getTextValue(JsonParser jp) throws IOException {
		try {
			JsonNode node = jp.getCodec().readTree(jp);
			return node.textValue();
		} catch (Exception e) {
			return jp.getText();
		}
	}

}
