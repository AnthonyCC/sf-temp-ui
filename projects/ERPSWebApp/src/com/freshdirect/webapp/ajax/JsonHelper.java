package com.freshdirect.webapp.ajax;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class JsonHelper {

	private static final Logger LOG = LoggerFactory.getInstance(JsonHelper.class);

	public final static <T> T parseRequestData(HttpServletRequest request, Class<T> typeClass) throws JsonParseException, JsonMappingException, IOException {

		String reqJson = request.getParameter("data");
		if (reqJson == null) {
			reqJson = (String) request.getAttribute("data");
			if (reqJson != null) {
				reqJson = URLDecoder.decode(reqJson, "UTF-8");
			}
		}
		if (reqJson == null) {
			return null;
		}

		LOG.debug("Parsing request data: " + reqJson);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		T reqData = objectMapper.readValue(reqJson, typeClass);

		return reqData;
	}

}
