package com.freshdirect.fdstore.coremetrics.builder;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.framework.util.NVL;

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
	public String page; //help page 'page' param or search like page 'pageType' param


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
		obj.page = NVL.apply(request.getParameter("page"), request.getParameter("pageType"));

		return obj;
	}
	
	public static PageViewTagInput populateFromJSONInput(final String uri, final Map<String,Object> valueMap) {
		PageViewTagInput obj = new PageViewTagInput();
		
		obj.source = SRC_JSON;

		obj.uri = uri;

		if (valueMap.containsKey("id")) {
			obj.id = (String) valueMap.get("id");
		}
		if (valueMap.containsKey("page") || valueMap.containsKey("pageType")) {
			obj.page = NVL.apply((String) valueMap.get("page"), (String) valueMap.get("pageType"));
		}

		return obj;
	}
}
