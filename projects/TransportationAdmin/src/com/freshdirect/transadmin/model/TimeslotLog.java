package com.freshdirect.transadmin.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.freshdirect.transadmin.util.TransStringUtil;

public class TimeslotLog implements java.io.Serializable, TrnBaseEntityI {
	
	private String id;
	private String orderId;
	private String customerId;
	private String eventtype;
	private String responseTime;
	private Date eventDtm;
	private String comments;
	
	private Set timeslotLogDtls = new HashSet(0);

	public TimeslotLog() {
	}

	public TimeslotLog(String id, String orderId, String eventtype,
			Date eventDtm, String comments) {
		this.id = id;
		this.orderId = orderId;
		this.eventtype = eventtype;
		this.eventDtm = eventDtm;
		this.comments = comments;
	}

	public TimeslotLog(String id, String orderId, String customerId,
			String eventtype, Date eventDtm, String responseTime, Set timeslotLogDtls,String comments) {
		this.id = id;
		this.orderId = orderId;
		this.customerId = customerId;
		this.eventtype = eventtype;
		this.eventDtm = eventDtm;
		this.responseTime = responseTime;
		this.timeslotLogDtls = timeslotLogDtls;
		this.comments = comments;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getEventtype() {
		return this.eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	public Date getEventDtm() {
		return this.eventDtm;
	}

	public void setEventDtm(Date eventDtm) {
		this.eventDtm = eventDtm;
	}

	public Set getTimeslotLogDtls() {
		return this.timeslotLogDtls;
	}

	public void setTimeslotLogDtls(Set timeslotLogDtls) {
		this.timeslotLogDtls = timeslotLogDtls;
	}
	
	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getEventTime() {
		if(getEventDtm() != null) {
			return TransStringUtil.formatTime(getEventDtm());
		}
		return null;
	}
			
	@Override
	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
