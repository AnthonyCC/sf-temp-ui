package com.freshdirect.cmsadmin.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json utility class.
 */
public final class JsonUtils {

    private JsonUtils() {
    }

    /**
     * Converts java serialize object to json bytes.
     *
     * @throws IOException
     *
     * @param object
     *            Object object
     * @return json bytes
     */
    public static String convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }
}
