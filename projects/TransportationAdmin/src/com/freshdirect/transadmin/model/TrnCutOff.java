package com.freshdirect.transadmin.model;

import java.math.BigDecimal;
import java.text.ParseException;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.util.TransStringUtil;

public class TrnCutOff implements java.io.Serializable, TrnBaseEntityI {
	
	private String cutOffId;
	private String name;
	private String description;
	private BigDecimal sequenceNo;
	
	private TimeOfDay cutOffTime;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public boolean isObsoleteEntity() {
		return false;
	}

	public String getCutOffId() {
		return cutOffId;
	}

	public void setCutOffId(String cutOffId) {
		this.cutOffId = cutOffId;
	}

	public BigDecimal getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(BigDecimal sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public TimeOfDay getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(TimeOfDay cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	
	public String getCutOffTimeEx() {
		if(getCutOffTime() != null) {
			try {
				return TransStringUtil.getServerTime(getCutOffTime().getNormalDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return "null";
	}
	
}
