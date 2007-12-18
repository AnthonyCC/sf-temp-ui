package com.freshdirect.listadmin.query;

import java.util.HashMap;

public class MapQueryContext extends HashMap implements QueryContextI {
	public String get(String key) {
		return (String) super.get(key);
	}
	
	public void put(String key, String value) {
		super.put(key,value);
	}
}
