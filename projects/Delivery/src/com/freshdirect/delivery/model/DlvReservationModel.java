/*
 * DlvReservationModel.java
 *
 * Created on August 27, 2001, 6:30 PM
 */

package com.freshdirect.delivery.model;

/**
 *
 * @author  knadeem
 * @version
 */
import java.util.Date;

import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.framework.core.*;

public class DlvReservationModel extends ModelSupport {

	private String orderId;
	private String customerId;
	private int statusCode;
	private Date expirationDateTime;
	private String timeslotId;
	private String zoneId;
	private EnumReservationType type;
	private String addressId;
	private boolean chefsTable;

	public DlvReservationModel(
		String orderId,
		String customerId,
		int statusCode,
		Date expirationDateTime,
		String timeslotId,
		String zoneId,
		EnumReservationType type, String addressId) {
			
		this.orderId = orderId;
		this.customerId = customerId;
		this.statusCode = statusCode;
		this.expirationDateTime = expirationDateTime;
		this.timeslotId = timeslotId;
		this.zoneId = zoneId;
		this.type = type;
		this.addressId = addressId;
	}

	public DlvReservationModel(
		PrimaryKey pk,
		String orderId,
		String customerId,
		int statusCode,
		Date expirationDateTime,
		String timeslotId,
		String zoneId,
		EnumReservationType type, String addressId) {
			
		this(orderId, customerId, statusCode, expirationDateTime, timeslotId, zoneId, type, addressId);
		this.setPK(pk);

	}

	public String getCustomerId() {
		return customerId;
	}

	public void setTimeslotId(String timeslotId) {
		this.timeslotId = timeslotId;
	}

	public void setExpirationDateTime(Date expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getTimeslotId() {
		return timeslotId;
	}

	public Date getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	
	public EnumReservationType getReservationType(){
		return this.type;
	}
	
	public void setReservationType(EnumReservationType type){
		this.type = type;
	}
	
	public String getAddressId(){
		return this.addressId;
	}
	
	public void setAddressId(String addressId){
		this.addressId = addressId;
	}

	public boolean isChefsTable() {
		return chefsTable;
	}

	public void setChefsTable(boolean chefsTable) {
		this.chefsTable = chefsTable;
	}

}
