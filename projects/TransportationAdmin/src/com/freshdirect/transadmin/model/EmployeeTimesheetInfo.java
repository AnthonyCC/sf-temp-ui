package com.freshdirect.transadmin.model;


public class EmployeeTimesheetInfo implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -433033882649646849L;

	public EmployeeTimesheetInfo(String badgeId, String clockIn,
			String clockOut, String tipAmount, String onBreak) {
		super();
		this.badgeId = badgeId;
		this.clockIn = clockIn;
		this.clockOut = clockOut;
		this.tipAmount = tipAmount;
		this.onBreak = onBreak;
	}

	private String badgeId;
	
	private String clockIn;
	
	private String clockOut;
	
		
	private String tipAmount;
	
	private String onBreak;

	public String getBadgeId() {
		return badgeId;
	}

	public void setBadgeId(String badgeId) {
		this.badgeId = badgeId;
	}

	public String getClockIn() {
		return clockIn;
	}

	public void setClockIn(String clockIn) {
		this.clockIn = clockIn;
	}

	public String getClockOut() {
		return clockOut;
	}

	public void setClockOut(String clockOut) {
		this.clockOut = clockOut;
	}

	public String getTipAmount() {
		return tipAmount;
	}

	public void setTipAmount(String tipAmount) {
		this.tipAmount = tipAmount;
	}

	public String getOnBreak() {
		return onBreak;
	}

	public void setOnBreak(String onBreak) {
		this.onBreak = onBreak;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((badgeId == null) ? 0 : badgeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeTimesheetInfo other = (EmployeeTimesheetInfo) obj;
		if (badgeId == null) {
			if (other.badgeId != null)
				return false;
		} else if (!badgeId.equals(other.badgeId))
			return false;
		return true;
	}

}
