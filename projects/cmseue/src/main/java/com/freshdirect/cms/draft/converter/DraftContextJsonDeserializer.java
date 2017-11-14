package com.freshdirect.cms.draft.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.freshdirect.cms.draft.domain.DraftContext;

public class DraftContextJsonDeserializer extends JsonDeserializer<DraftContext> {

    @Override
    public DraftContext deserialize(JsonParser jsonParser, DeserializationContext deserializeContext) throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);
        return new DraftContext(jsonNode.get("draftId").asLong(), jsonNode.get("draftName").asText());
    }

}
