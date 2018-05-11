package com.freshdirect.fdstore.sms.shortsubstitute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortSubstituteResponse implements Serializable{
	private static final long serialVersionUID = 1L;

	
	
	private Map<String,ShortSubstituteData> shortSubstituteMap= new HashMap<String, ShortSubstituteData>();
	
	public Map<String, ShortSubstituteData> getShortSubstituteMap() {
		return shortSubstituteMap;
	}

	public void setShortSubstituteMap(
			Map<String, ShortSubstituteData> shortSubstituteMap) {
		this.shortSubstituteMap = shortSubstituteMap;
	}

	private List<ShortSubstituteData> shortSubstituteData= new ArrayList<ShortSubstituteData>();

	public List<ShortSubstituteData> getShortSubstituteData() {
		return shortSubstituteData;
	}

	public void setShortSubstituteData(List<ShortSubstituteData> shortSubstituteData) {
		this.shortSubstituteData = shortSubstituteData;
	}


}
