package com.freshdirect.athena.util;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.athena.UrlInfo;

public class UrlUtil {
	
	private static final int START_INDEX_PARAM = 2;
	
	public static UrlInfo getUrlInfo(String url) {
		
		String[] splits = url.split("/");
		String api = null;
		String lastParam = null;
		Map<String, String> params = new HashMap<String, String>();
		
		for (int i = START_INDEX_PARAM; i < splits.length; i++) {
			String split = UrlUtils.getInstance().cleanURL(splits[i]);
			
			if(split != null && split.trim().length() > 0) {
				if(api == null) {
					api = split;
				} else if(lastParam == null) {
					lastParam = split;
					params.put(lastParam, null);
				} else {				
					params.put(lastParam, split);
					lastParam = null;
				}
			}
		}
		return new UrlInfo(api, params);
	}

}
