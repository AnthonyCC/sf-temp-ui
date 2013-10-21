package com.freshdirect.transadmin.model;

import java.text.ParseException;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.util.TransStringUtil;

@SuppressWarnings("serial")
public class DispatchGroup implements java.io.Serializable, TrnBaseEntityI {
	
	private String dispatchGroupId;
	private String name;	
	private TimeOfDay groupTime;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public boolean isObsoleteEntity() {
		return false;
	}
	
	public String getDispatchGroupId() {
		return dispatchGroupId;
	}

	public void setDispatchGroupId(String dispatchGroupId) {
		this.dispatchGroupId = dispatchGroupId;
	}

	public TimeOfDay getGroupTime() {
		return groupTime;
	}

	public void setGroupTime(TimeOfDay groupTime) {
		this.groupTime = groupTime;
	}

	public String getDispatchTimeEx() {
		if(getGroupTime() != null) {
			try {
				return TransStringUtil.getServerTime(getGroupTime().getNormalDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return "null";
	}
	
}
