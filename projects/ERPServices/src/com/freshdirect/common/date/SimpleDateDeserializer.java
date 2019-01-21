package com.freshdirect.common.date;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SimpleDateDeserializer extends JsonDeserializer<Date> {

	private static final Logger LOGGER = LoggerFactory.getInstance(SimpleDateDeserializer.class);

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
		JsonNode node = getJsonNode(jp);
		final Date date = getDateFromTextValue(jp, node);
		if (date != null) {
			return date;
		} else {
			// try long value, date could be unix time
			final long timestamp = getLongValue(jp, node);
			if (timestamp >= 0) {
				return new Date(timestamp);
			}
			throw new JsonParseException("Unparseable date: " + date + ", timestamp=" + timestamp + ". Supported formats: yyyy-MM-dd, unix-timestamp", null);

		}
	}

	private JsonNode getJsonNode(JsonParser jp) {
		try {
			return jp.getCodec().readTree(jp);
		} catch (Exception e) {
			return null;
		}
	}

	private Date getDateFromTextValue(JsonParser jp, JsonNode node) throws IOException {
		String dateTextValue;
		if (node != null) {
			dateTextValue = node.textValue();
		} else {
			dateTextValue = jp.getText();
		}
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return simpleDateFormat.parse(dateTextValue);
		} catch (Exception e) {
			return null;
		}

	}

	private long getLongValue(JsonParser jp, JsonNode node) throws IOException {
		if (node != null) {
			return node.asLong(-1);
		} else {
			return jp.getLongValue();
		}
	}
}
