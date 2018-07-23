package com.freshdirect.fdlogistics.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.logistics.delivery.model.EnumReservationType;

public class EnumReservationTypeDeserializer extends JsonDeserializer<EnumReservationType> {

	@Override
	public EnumReservationType deserialize(JsonParser jp, DeserializationContext context)
			throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		ObjectNode root = (ObjectNode) mapper.readTree(jp);
		if (root.get("name") == null)
			return null;
		String name = root.get("name").asText();
		return EnumReservationType.getEnum(name);
	}

}
