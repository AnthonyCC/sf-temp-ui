package com.freshdirect.fdstore.coremetrics.tagmodel;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTagModel  {
	
	private Map<Integer,String> attributesMaps = new HashMap<Integer,String>();

	public Map<Integer,String> getAttributesMaps() {
		return attributesMaps;
	}
}