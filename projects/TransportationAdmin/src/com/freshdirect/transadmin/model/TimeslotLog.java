package com.freshdirect.transadmin.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.transadmin.util.TransStringUtil;

public class TimeslotLog implements java.io.Serializable, TrnBaseEntityI {
	
	private String id;
	private String orderId;
	private String customerId;
	private String eventtype;
	private long responseTime;
	private Date eventDtm;
	private Set timeslotLogDtls = new HashSet(0);

	public TimeslotLog() {
	}

	public TimeslotLog(String id, String orderId, String eventtype,
			Date eventDtm) {
		this.id = id;
		this.orderId = orderId;
		this.eventtype = eventtype;
		this.eventDtm = eventDtm;
	}

	public TimeslotLog(String id, String orderId, String customerId,
			String eventtype, Date eventDtm, long responseTime, Set timeslotLogDtls) {
		this.id = id;
		this.orderId = orderId;
		this.customerId = customerId;
		this.eventtype = eventtype;
		this.eventDtm = eventDtm;
		this.responseTime = responseTime;
		this.timeslotLogDtls = timeslotLogDtls;
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
	
	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

		
	public String getDetailDisplay() 	{
		
		StringBuffer strBuf = new StringBuffer();
		
		if(timeslotLogDtls != null) {
			try {
				Iterator _itr = timeslotLogDtls.iterator();
				Date _tmpDate = null;
				TimeslotLogDtl _dtl = null;
				Map<Date, List<TimeslotLogDtl>> dtlMapping = new HashMap<Date, List<TimeslotLogDtl>>();
				
				while(_itr.hasNext()) {
					_dtl = (TimeslotLogDtl) _itr.next();
					if(!dtlMapping.containsKey(_dtl.getId().getBaseDate())) {
						dtlMapping.put(_dtl.getId().getBaseDate(), new ArrayList<TimeslotLogDtl>());
					}	
					dtlMapping.get(_dtl.getId().getBaseDate()).add(_dtl);
				}
				
				Iterator _keyItr = dtlMapping.keySet().iterator();
				List<TimeslotLogDtl> _tmpLst = null;
				while(_keyItr.hasNext()) {
					_tmpDate = (Date)_keyItr.next();
					strBuf.append("<b>"+TransStringUtil.getServerDate(_tmpDate)+"</b>").append("=");
					_tmpLst = dtlMapping.get(_tmpDate);
					Iterator<TimeslotLogDtl> _dtlItr = _tmpLst.iterator();
					while(_dtlItr.hasNext()) {
						_dtl = _dtlItr.next();						
						strBuf.append(TransStringUtil.formatTimeRange(_dtl.getId().getStartTime(), _dtl.getId().getEndTime()));
						if(_dtlItr.hasNext()) {
							strBuf.append(", ");
						}
					}
					strBuf.append("<br/>");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return strBuf.toString();
	}
	
	@Override
	public boolean isObsoleteEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
