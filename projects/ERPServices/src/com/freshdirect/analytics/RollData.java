package com.freshdirect.analytics;

import java.util.Date;

public class RollData {

	private int cnt;
	private String zone;
	private Date cutOff;
	private Date time;
	private Date snapshotTime;
	private String cutoffTimeFormatted;
	private String snapshotTimeFormatted;

	public String getCutoffTimeFormatted() {
		return cutoffTimeFormatted;
	}
	public void setCutoffTimeFormatted(String cutoffTimeFormatted) {
		this.cutoffTimeFormatted = cutoffTimeFormatted;
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
