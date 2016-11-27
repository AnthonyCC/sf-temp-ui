package com.freshdirect.erp;

import java.io.Serializable;
import java.util.Date;

public class SkuAvailabilityHistory implements Serializable {
	private static final long serialVersionUID = 5641309147912475599L;

	private String skuCode;

	private int version;

	private String unavailabilityStatus;

	private Date eventDate;

	public SkuAvailabilityHistory(String skuCode, int version, String unavailabilityStatus, Date eventDate) {
		super();
		this.skuCode = skuCode;
		this.version = version;
		this.unavailabilityStatus = unavailabilityStatus;
		this.eventDate = eventDate;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public int getVersion() {
		return version;
	}

	public String getUnavailabilityStatus() {
		return unavailabilityStatus;
	}

	public Date getEventDate() {
		return eventDate;
	}
}
