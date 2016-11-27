package com.freshdirect.crm;

import java.io.Serializable;
import java.util.Date;

public class CrmQueueInfo implements Serializable {

	private final String queueCode;
	private final int open;
	private final int unassigned;
	private final Date oldest;

	public CrmQueueInfo(CrmCaseQueue queue, int open, int unassigned, Date oldest) {
		this.queueCode = queue.getCode();
		this.open = open;
		this.unassigned = unassigned;
		this.oldest = oldest;
	}

	public CrmCaseQueue getQueue() {
		return CrmCaseQueue.getEnum(queueCode);
	}

	public int getOpen() {
		return open;
	}

	public int getUnassigned() {
		return unassigned;
	}

	public Date getOldest() {
		return oldest;
	}

	public String toString() {
		return "CrmQueueInfo[" + queueCode + ", " + open + ", " + unassigned + ", " + oldest + "]";
	}

}
