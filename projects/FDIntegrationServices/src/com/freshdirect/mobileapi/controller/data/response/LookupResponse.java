package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Lookup;
import com.freshdirect.mobileapi.controller.data.Message;

public class LookupResponse extends Message {
	
    List<Lookup> lookup;

	public List<Lookup> getLookup() {
		return lookup;
	}

	public void setLookup(List<Lookup> lookup) {
		this.lookup = lookup;
	}    

}
