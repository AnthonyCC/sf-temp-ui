package com.freshdirect.analytics.model;

import java.util.Date;

public class BounceData {

	private int cnt;
	private String zone;
	private String sector;
	private Date cutOff;
	private String type;
	private Date snapshotTime;
	private String cutoffTimeFormatted;
	private String snapshotTimeFormatted;
	
	public String getCutoffTimeFormatted() {
		return cutoffTimeFormatted;
	}
	public void setCutoffTimeFormatted(String cutoffTimeFormatted) {
		this.cutoffTimeFormatted = cutoffTimeFormatted;
	}
	
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public Date getCutOff() {
		return cutOff;
	}
	public void setCutOff(Date cutOff) {
		this.cutOff = cutOff;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getSnapshotTime() {
		return snapshotTime;
	}
	public void setSnapshotTime(Date snapshotTime) {
		this.snapshotTime = snapshotTime;
	}
	public String getSnapshotTimeFormatted() {
		return snapshotTimeFormatted;
	}
	public void setSnapshotTimeFormatted(String snapshotTimeFormatted) {
		this.snapshotTimeFormatted = snapshotTimeFormatted;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	
}
