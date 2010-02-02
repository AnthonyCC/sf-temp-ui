package com.freshdirect.delivery;

import java.io.Serializable;
import java.util.Date;

public class TimeslotCapacityContext implements Serializable  {
	
	private boolean isChefsTable;
	
	private Date currentTime;

	public boolean isChefsTable() {
		return isChefsTable;
	}

	public void setChefsTable(boolean isChefsTable) {
		this.isChefsTable = isChefsTable;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}
	
}
