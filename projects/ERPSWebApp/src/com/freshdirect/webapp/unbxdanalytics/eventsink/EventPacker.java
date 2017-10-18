package com.freshdirect.webapp.unbxdanalytics.eventsink;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.google.common.base.Charsets;

public final class EventPacker {

    private static final Logger LOGGER = LoggerFactory.getInstance(EventPacker.class);

    EventPacker() {
    }

    /**
     * Pack up event details and make the query part of the URL of web request
     * 
     * @param event
     * @return
     */
    public static String pack(AnalyticsEventI event) {

        // Assemble 'data' part of payload

        final String jsonPayload = packData(event);

        Map<String, String> queryComponents = new HashMap<String, String>();
        if (jsonPayload != null) {
            try {
                queryComponents.put("data", URLEncoder.encode(jsonPayload, Charsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
            }
        }

        queryComponents.put("UnbxdKey", event.isCosAction() ? FDStoreProperties.getUnbxdCosSiteKey() : FDStoreProperties.getUnbxdSiteKey());
        queryComponents.put("action", event.getType().getAction());
        queryComponents.put("uid", event.getUid());
        queryComponents.put("t", String.valueOf(event.getTimestamp()));

        Collection<String> params = new ArrayList<String>(queryComponents.size());
        for (Map.Entry<String, String> e : queryComponents.entrySet()) {
            params.add(e.getKey() + "=" + e.getValue());
        }

        final String query = StringUtil.join(params, "&");

        return query;
    }



    /**
     * Pack the 'data' part of web payload
     * 
     * @param event UNBXD analytics event
     * @return JSON payload
     */
    protected static String packData(AnalyticsEventI event) {
        String jsonPayload = null;
        // do the JSON
        try {
            Writer writer = new StringWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            
            DefaultSerializerProvider serializerProvider = new DefaultSerializerProvider.Impl();
            serializerProvider.setNullValueSerializer(new NullValueSerializer()); // for sending the null referrer as empty string
            objectMapper.setSerializerProvider(serializerProvider);
            
            objectMapper.writeValue(writer, event);
            jsonPayload = writer.toString();

            LOGGER.debug(jsonPayload);
        } catch (JsonGenerationException e) {
            LOGGER.error(e);
        } catch (JsonMappingException e) {
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return jsonPayload;
    }
    
    private static class NullValueSerializer extends StdSerializer<Object> {
        protected NullValueSerializer() {
            super(Object.class);
        }
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeString("");
        }
    }
}
