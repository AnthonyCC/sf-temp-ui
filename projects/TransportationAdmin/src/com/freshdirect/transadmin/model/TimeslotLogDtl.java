package com.freshdirect.transadmin.model;

import java.util.Date;

public class TimeslotLogDtl implements java.io.Serializable {
	
	private TimeslotLogDtlId id;
	
	public TimeslotLogDtl() {
	}

	public TimeslotLogDtl(TimeslotLogDtlId id) {
		this.id = id;
	}

	
	public TimeslotLogDtlId getId() {
		return this.id;
	}

	public void setId(TimeslotLogDtlId id) {
		this.id = id;
	}
	
}
