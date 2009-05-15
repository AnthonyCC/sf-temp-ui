package com.freshdirect.transadmin.model;

import java.util.Date;

public interface PunchInfoI {
	
	public String getEmployeeId();
	
	public Date getDate();
	
	public Date getStartTime();
	
	public Date getEndTime();
	
	public boolean isPunchedIn();
	
	public boolean isPunchedOut();
	
	public boolean isLate();
	
	public Date getInPunchDTM();
	
	public Date getOutPunchDTM();
}
