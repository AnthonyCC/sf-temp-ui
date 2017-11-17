package com.freshdirect.webapp.ajax;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class JsonHelper {

    private static final Logger LOG = LoggerFactory.getInstance(JsonHelper.class);

    public final static <T> T parseRequestData(HttpServletRequest request, Class<T> typeClass) throws JsonParseException, JsonMappingException, IOException {
        String requestJson = request.getParameter("data");
        if (requestJson == null) {
            requestJson = (String) request.getAttribute("data");
            if (requestJson != null) {
                requestJson = URLDecoder.decode(requestJson, "UTF-8");
            }
        }
        if (requestJson == null) {
            return null;
        }
        LOG.debug("Parsing request data: " + requestJson);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T requestData = objectMapper.readValue(requestJson, typeClass);
        return requestData;
    }

    public static <T> void writeResponseData(HttpServletResponse response, T responseData)
            throws JsonGenerationException, JsonMappingException, IOException {
        configureJsonResponse(response);
        Writer writer = new StringWriter();
        new ObjectMapper().writeValue(writer, responseData);
        ServletOutputStream out = response.getOutputStream();
        String responseJson = writer.toString();
        out.print(responseJson);
        out.flush();
    }

    /**
     * Set common response properties for JSON response
     * 
     * @param response
     */
    private static void configureJsonResponse(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("application/json");
        response.setLocale(Locale.US);
        response.setCharacterEncoding("ISO-8859-1");
    }

}
