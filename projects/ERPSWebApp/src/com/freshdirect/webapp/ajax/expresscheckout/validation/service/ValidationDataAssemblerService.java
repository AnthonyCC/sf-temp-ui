package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;

public class ValidationDataAssemblerService {

	private static final ValidationDataAssemblerService INSTANCE = new ValidationDataAssemblerService();

	private ValidationDataAssemblerService() {
	}

	public static ValidationDataAssemblerService defaultService() {
		return INSTANCE;
	}

	public Map<String, String> assembleValidatedData(final FormDataRequest request) {
		final Map<String, String> validatedData = new HashMap<String, String>();

		final String edited = request.getEdited();
		final Map<String, String> data = FormDataService.defaultService().getSimpleMap(request);

		if (data.containsKey(edited)) {
			validatedData.put(edited, data.get(edited));
		} else {
			validatedData.putAll(data);
		}

		return validatedData;
	}
}
