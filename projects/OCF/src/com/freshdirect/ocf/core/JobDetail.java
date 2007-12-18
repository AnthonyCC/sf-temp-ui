/**
 * @author ekracoff
 * Created on May 19, 2005*/

package com.freshdirect.ocf.core;

import java.util.Date;


public class JobDetail {
	private String jobId;
	private String filePath;
	private Date sentDate;
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getJobId() {
		return jobId;
	}
	
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	public Date getSentDate() {
		return sentDate;
	}
	
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
}
