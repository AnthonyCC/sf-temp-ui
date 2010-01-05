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
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.framework.core.*;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
	private Date deliveryDate;
	private String zoneCode;
	private RoutingActivityType unassignedActivityType;
	private String profileName;	
	private boolean inUPS;
	//private String routingOrderId;
	/*private Date unassignedDateTime;
	

	public Date getUnassignedDateTime() {
		return unassignedDateTime;
	}

	public void setUnassignedDateTime(Date _date) {
		this.unassignedDateTime = _date;
	}*/
	
	

	public RoutingActivityType getUnassignedActivityType() {
		return unassignedActivityType;
	}

	public void setUnassignedActivityType(RoutingActivityType unassignedActivityType) {
		this.unassignedActivityType = unassignedActivityType;
	}
	
	public boolean isUnassigned() {
		return unassignedActivityType!=null;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public DlvReservationModel(
		String orderId,
		String customerId,
		int statusCode,
		Date expirationDateTime,
		String timeslotId,
		String zoneId,
		EnumReservationType type, String addressId, Date deliveryDate, String zoneCode/*,Date unassignedDateTime*/,RoutingActivityType unassignedActivityType,boolean inUPS) {
			
		this.orderId = orderId;
		this.customerId = customerId;
		this.statusCode = statusCode;
		this.expirationDateTime = expirationDateTime;
		this.timeslotId = timeslotId;
		this.zoneId = zoneId;
		this.type = type;
		this.addressId = addressId;
		this.deliveryDate=deliveryDate;
		this.zoneCode=zoneCode;
		//this.unassignedDateTime=unassignedDateTime;
		this.unassignedActivityType=unassignedActivityType;
		this.inUPS=inUPS;
		//this.routingOrderId=routingOrderId;
	}

	public DlvReservationModel(
		PrimaryKey pk,
		String orderId,
		String customerId,
		int statusCode,
		Date expirationDateTime,
		String timeslotId,
		String zoneId,
		EnumReservationType type, String addressId, Date deliveryDate, String zoneCode/*,Date unassignedDateTime*/,RoutingActivityType unassignedActivityType,boolean inUPS) {
			
		this(orderId, customerId, statusCode, expirationDateTime, timeslotId, zoneId, type, addressId,deliveryDate,zoneCode/*,unassignedDateTime*/,unassignedActivityType,inUPS);
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

	public boolean isInUPS() {
		return inUPS;
	}

	public void setInUPS(boolean inUPS) {
		this.inUPS = inUPS;
	}
	
	public boolean isChefsTable() {
		return chefsTable;
	}

	public void setChefsTable(boolean chefsTable) {
		this.chefsTable = chefsTable;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

}
