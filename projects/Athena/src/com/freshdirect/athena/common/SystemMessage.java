package com.freshdirect.athena.common;

import java.io.Serializable;
import java.util.Date;

public class SystemMessage implements Serializable {
	
	private Date timeStamp;
	
	private String message;

	public SystemMessage(Date timeStamp, String message) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public String getMessage() {
		return message;
	}
	
	

}
