/**
 * @author ekracoff
 * Created on Jul 22, 2005*/

package com.freshdirect.fdstore;

import java.io.Serializable;


public class StateCounty implements Serializable{
	private String state;
	private String county;
	private String city;
	
	public StateCounty(String state, String county, String city) {
		this.state = state;
		this.county = county;
		this.city = city;
	}
	
	public String getCounty() {
		return county;
	}
	
	public String getState() {
		return state;
	}

	public String getCity() {
		return city;
	}
	
}
