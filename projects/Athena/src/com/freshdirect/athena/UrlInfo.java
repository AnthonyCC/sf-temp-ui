package com.freshdirect.athena;

import java.util.Map;

public class UrlInfo {
	
	private String api;
	
	private Map <String, String> params;

	public UrlInfo(String api, Map<String, String> params) {
		super();
		this.api = api;
		this.params = params;
	}

	public String getApi() {
		return api;
	}

	public Map<String, String> getParams() {
		return params;
	}

	@Override
	public String toString() {
		return "UrlInfo [api=" + api + ", params=" + params + "]";
	}
	
	

}
