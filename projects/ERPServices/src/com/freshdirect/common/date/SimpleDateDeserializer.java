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
		JsonNode node = jp.getCodec().readTree(jp);
		final String date = getTextValue(jp, node);
		if (date != null) {
			try {
				return simpleDateFormat.parse(date);
			} catch (ParseException e) {
				throw new JsonParseException(
						"Unparseable date: " + date + ". Supported formats: yyyy-MM-dd, unix-timestamp", e);
			}
		} else {
			// try long value, date could be unix time
			final long timestamp = getLongValue(jp, node);
			if (timestamp > 0) {
				return new Date(timestamp);
			}
		}
		throw new JsonParseException("Unparseable date: " + date + ". Supported formats: yyyy-MM-dd, unix-timestamp");
	}

	private String getTextValue(JsonParser jp, JsonNode node) throws IOException {
		try {
			return node.textValue();
		} catch (Exception e) {
			return jp.getText();
		}
	}

	private long getLongValue(JsonParser jp, JsonNode node) throws IOException {
		try {
			return node.asLong(-1);
		} catch (Exception e) {
			return jp.getLongValue();
		}
	}
}
