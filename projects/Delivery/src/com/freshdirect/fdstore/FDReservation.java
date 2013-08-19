package com.freshdirect.fdstore;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.freshdirect.delivery.EnumRegionServiceType;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.model.EnumReservationClass;
import com.freshdirect.routing.constants.*;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;



public class FDReservation extends ModelSupport {
	
	private static final long	serialVersionUID	= -8318474657729420003L;
	
	private final Date expirationDateTime;
	private final EnumReservationType type;
	private final FDTimeslot timeslot;
	private final String customerId;
	private final String addressId;
	private final String orderId;
	private final boolean chefsTable;
	private final boolean isUnassigned;
	private final boolean isInUPS;
	private final RoutingActivityType unassignedActivityType;
	private final int statusCode;
	private final EnumReservationClass rsvClass;
	private boolean hasSteeringDiscount;
	private String buildingId;
	private String locationId;
	private int reservedOrdersAtBuilding;
	private EnumRegionServiceType regionSvcType;
	
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
		boolean isInUPS,
		RoutingActivityType unassignedActivityType,
		int statusCode, EnumReservationClass rsvClass,
		String buildingId, String locationId, int reservedOrdersAtBuilding, EnumRegionServiceType regionSvcType) {
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
		this.unassignedActivityType=unassignedActivityType;
		this.statusCode=statusCode;
		this.rsvClass=rsvClass;
		this.buildingId = buildingId;
		this.locationId = locationId;
		this.reservedOrdersAtBuilding = reservedOrdersAtBuilding;
		this.regionSvcType = regionSvcType;
	}
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
		boolean isInUPS,
		RoutingActivityType unassignedActivityType,
		int statusCode, EnumReservationClass rsvClass, boolean hasSteeringDiscount,
		String buildingId, String locationId, int reservedOrdersAtBuilding, EnumRegionServiceType regionSvcType) {
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
		this.unassignedActivityType=unassignedActivityType;
		this.statusCode=statusCode;
		this.rsvClass=rsvClass;
		this.hasSteeringDiscount = hasSteeringDiscount;
		this.buildingId = buildingId;
		this.locationId = locationId;
		this.reservedOrdersAtBuilding = reservedOrdersAtBuilding;
		this.regionSvcType = regionSvcType;
		
	}
	public EnumReservationType getType() {
		return type;
	}
	public int getStatusCode() {
		return statusCode;
	}
	
	public RoutingActivityType getUnassignedActivityType() {
		return unassignedActivityType;
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
	
	public String getZoneCode() {
		return this.timeslot.getZoneCode();
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
	
	public EnumReservationClass getRsvClass() {
		return rsvClass;
	}
	
	public boolean isPremium(){
		return EnumReservationClass.PREMIUM.equals(this.getRsvClass())||EnumReservationClass.PREMIUMCT.equals(this.getRsvClass());
	}
	public boolean hasSteeringDiscount() {
		return hasSteeringDiscount;
	}
	public void setHasSteeringDiscount(boolean hasSteeringDiscount) {
		this.hasSteeringDiscount = hasSteeringDiscount;
	}
	
	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public int getReservedOrdersAtBuilding() {
		return reservedOrdersAtBuilding;
	}

	public void setReservedOrdersAtBuilding(int reservedOrdersAtBuilding) {
		this.reservedOrdersAtBuilding = reservedOrdersAtBuilding;
	}
	public EnumRegionServiceType getRegionSvcType() {
		return regionSvcType;
	}
	public void setRegionSvcType(EnumRegionServiceType regionSvcType) {
		this.regionSvcType = regionSvcType;
	}
	public boolean isInBulkZone(){
		return (this.regionSvcType!=null && EnumRegionServiceType.isHybrid(this.regionSvcType));
	}
}
