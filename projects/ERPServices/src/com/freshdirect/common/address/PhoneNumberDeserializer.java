package com.freshdirect.common.address;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PhoneNumberDeserializer extends JsonDeserializer<PhoneNumber> {

	@Override
	public PhoneNumber deserialize(JsonParser jp, DeserializationContext context)
			throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		ObjectNode root = (ObjectNode) mapper.readTree(jp);
		JsonNode phoneNode = root.get("phone");
		JsonNode extensionNode = root.get("extension");
		JsonNode typeNode = root.get("type");
		return new PhoneNumber(phoneNode != null && !phoneNode.isNull() ? phoneNode.asText() : "",
				extensionNode != null && !extensionNode.isNull() ? extensionNode.asText() : "",
				typeNode != null && !typeNode.isNull()? typeNode.asText() : "");

	}

}
