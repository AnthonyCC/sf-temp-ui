/**
 * @author ekracoff
 * Created on Jul 22, 2005*/

package com.freshdirect.fdstore;

import java.io.Serializable;


public class StateCounty implements Serializable{
	private String state;
	private String county;
	
	public StateCounty(String state, String county) {
		this.state = state;
		this.county = county;
	}
	
	public String getCounty() {
		return county;
	}
	
	public String getState() {
		return state;
	}
}
