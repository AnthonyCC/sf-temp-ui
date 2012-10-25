package com.freshdirect.fdstore.coremetrics.tagmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTagModel implements Serializable {
	
	private Map<Integer,String> attributesMaps = new HashMap<Integer,String>();

	public Map<Integer,String> getAttributesMaps() {
		return attributesMaps;
	}
}