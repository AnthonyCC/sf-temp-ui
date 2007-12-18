/**
 * @author ekracoff
 * Created on May 3, 2005*/

package com.freshdirect.ocf.core;

import java.util.Date;
import java.util.Set;


public class RunLog extends Entity{
	private String flightId;
	private Date timestamp;
	private Set customers;
	private String status;
	
	public String getFlightId() {
		return flightId;
	}
	
	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date startTime) {
		this.timestamp = startTime;
	}
	
	/** returns a set of RunCust objects **/
	public Set getCustomers() {
		return customers;
	}
	
	public void setCustomers(Set customers) {
		this.customers = customers;
	}
	
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
}
