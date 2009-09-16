/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDReservation extends ModelSupport {
	private final Date expirationDateTime;
	private final EnumReservationType type;
	private final FDTimeslot timeslot;
	private final String customerId;
	private final String addressId;
	private final String orderId;
	private final boolean chefsTable;
	private final boolean isUnassigned;
	private final boolean isInUPS;
	 

	public FDReservation(
		PrimaryKey pk,
		FDTimeslot timeslot,
		Date expirationDateTime,
		EnumReservationType type,
		String customerId,
		String addressId,
		boolean chefsTable,
		boolean isUnassigned,
		String orderId,
		boolean isInUPS) {
		this.setPK(pk);
		this.timeslot = timeslot;
		this.expirationDateTime = expirationDateTime;
		this.type = type;
		this.customerId = customerId;
		this.addressId = addressId;
		this.chefsTable = chefsTable;
		this.isUnassigned=isUnassigned;
		this.orderId = orderId;
		this.isInUPS=isInUPS;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getTimeslotId() {
		return this.timeslot.getTimeslotId();
	}
	
	public FDTimeslot getTimeslot() {
		return this.timeslot;
	}

	public String getZoneId() {
		return this.timeslot.getZoneId();
	}

	public Date getExpirationDateTime() {
		return expirationDateTime;
	}

	public Date getStartTime() {
		return this.timeslot.getBegDateTime();
	}

	public Date getEndTime() {
		return this.timeslot.getEndDateTime();
	}

	public Date getCutoffTime() {
		return this.timeslot.getCutoffDateTime();
	}

	public EnumReservationType getReservationType() {
		return this.type;
	}

	public String getCustomerId() {
		return this.customerId;
	}
	
	public String getAddressId() {
		return this.addressId;
	}
	
	public boolean isChefsTable() {
		return chefsTable;
	}
	
	public boolean isUnassigned() {
		return isUnassigned;
	}
	public boolean isInUPS() {
		return isInUPS;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
