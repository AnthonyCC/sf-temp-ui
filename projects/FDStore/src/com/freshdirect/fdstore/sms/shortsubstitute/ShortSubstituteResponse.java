package com.freshdirect.fdstore.sms.shortsubstitute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShortSubstituteResponse implements Serializable{
	private static final long serialVersionUID = 1L;

	private List<ShortSubstituteData> shortSubstituteData= new ArrayList<ShortSubstituteData>();

	public List<ShortSubstituteData> getShortSubstituteData() {
		return shortSubstituteData;
	}

	public void setShortSubstituteData(List<ShortSubstituteData> shortSubstituteData) {
		this.shortSubstituteData = shortSubstituteData;
	}


}
