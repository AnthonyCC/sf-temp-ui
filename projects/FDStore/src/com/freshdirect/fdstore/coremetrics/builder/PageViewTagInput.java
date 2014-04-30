package com.freshdirect.fdstore.coremetrics.builder;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class PageViewTagInput implements Serializable {
	private static final long serialVersionUID = -1642096101082416197L;

	/**
	 * This source type typically denotes normal request source
	 * In this case CM input is built up from various request parameters
	 */
	public static final int SRC_REQUEST = 0;

	/**
	 * Use this type when CM will be populated from JSON data
	 * passed via AJAX request
	 */
	public static final int SRC_JSON = 1;

	/**
	 * input source
	 */
	public int source;
	
	public String uri;
	public String id; // Page ID (Content Node)
	public String page;


	/**
	 * Populate context from request
	 * 
	 * @param request
	 * @return
	 */
	public static PageViewTagInput populateFromRequest(HttpServletRequest request) {
		PageViewTagInput obj = new PageViewTagInput();
		
		obj.source = SRC_REQUEST;
		
		// process values
		obj.uri = request.getRequestURI();
		obj.id = request.getParameter("id");
		obj.page = request.getParameter("page");

		return obj;
	}
	
	public static PageViewTagInput populateFromJSONInput(final String uri, final Map<String,Object> valueMap) {
		PageViewTagInput obj = new PageViewTagInput();
		
		obj.source = SRC_JSON;

		obj.uri = uri;

		if (valueMap.containsKey("id")) {
			obj.id = (String) valueMap.get("id");
		}
		if (valueMap.containsKey("page")) {
			obj.id = (String) valueMap.get("page");
		}

		return obj;
	}
}
