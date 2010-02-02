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
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;

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
	private Double orderSize;
	private Double serviceTime;
	private Double reservedOrderSize;
	private Double reservedServiceTime;
	private EnumRoutingUpdateStatus updateStatus;
		

	public Double getOrderSize() {
		return orderSize;
	}

	public void setOrderSize(Double orderSize) {
		this.orderSize = orderSize;
	}

	public Double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Double serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Double getReservedOrderSize() {
		return reservedOrderSize;
	}

	public void setReservedOrderSize(Double reservedOrderSize) {
		this.reservedOrderSize = reservedOrderSize;
	}

	public Double getReservedServiceTime() {
		return reservedServiceTime;
	}

	public void setReservedServiceTime(Double reservedServiceTime) {
		this.reservedServiceTime = reservedServiceTime;
	}

	public EnumRoutingUpdateStatus getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(EnumRoutingUpdateStatus updateStatus) {
		this.updateStatus = updateStatus;
	}

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
		EnumReservationType type, String addressId, Date deliveryDate, String zoneCode/*,Date unassignedDateTime*/,RoutingActivityType unassignedActivityType,boolean inUPS,
		Double orderSize, Double serviceTime, Double reservedOrderSize, Double reservedServiceTime, EnumRoutingUpdateStatus status) {
			
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
		this.orderSize = orderSize;
		this.serviceTime = serviceTime;
		this.reservedOrderSize = reservedOrderSize;
		this.reservedServiceTime = reservedServiceTime;
		this.updateStatus = status;
	}

	public DlvReservationModel(
		PrimaryKey pk,
		String orderId,
		String customerId,
		int statusCode,
		Date expirationDateTime,
		String timeslotId,
		String zoneId,
		EnumReservationType type, String addressId, Date deliveryDate, String zoneCode/*,Date unassignedDateTime*/,RoutingActivityType unassignedActivityType,boolean inUPS, 
		Double orderSize, Double serviceTime, Double reservedOrderSize, Double reservedServiceTime, EnumRoutingUpdateStatus status) {
			
		this(orderId, customerId, statusCode, expirationDateTime, timeslotId, zoneId, type, addressId,deliveryDate
						,zoneCode/*,unassignedDateTime*/,unassignedActivityType,inUPS
						, orderSize, serviceTime, reservedOrderSize, reservedServiceTime , status);
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
