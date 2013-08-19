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

import com.freshdirect.common.customer.EnumZoneType;
import com.freshdirect.delivery.EnumRegionServiceType;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.constants.RoutingActivityType;

public class DlvReservationModel extends ModelSupport {

	static final long serialVersionUID = -8162831638079894392L;
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
	private EnumReservationClass rsvClass;
	private Double overrideOrderSize;
	private Double overrideServiceTime;
	
	private Double reservedOrderSize;
	private Double reservedServiceTime;
	private EnumRoutingUpdateStatus updateStatus;
		
	private Long noOfCartons;
	
	private Long noOfFreezers;
	
	private Long noOfCases;
	
	private EnumOrderMetricsSource metricsSource;
		
	private boolean dynamic;
	
	private boolean hasSteeringDiscount;
	
	private String buildingId;
	private String locationId;
	private int reservedOrdersAtBuilding;
	
	private EnumRegionServiceType regionSvcType;
	
	public EnumOrderMetricsSource getMetricsSource() {
		return metricsSource;
	}

	public void setMetricsSource(EnumOrderMetricsSource metricsSource) {
		this.metricsSource = metricsSource;
	}

	public Double getOverrideOrderSize() {
		return overrideOrderSize;
	}

	public Double getOverrideServiceTime() {
		return overrideServiceTime;
	}

	public void setOverrideOrderSize(Double overrideOrderSize) {
		this.overrideOrderSize = overrideOrderSize;
	}

	public void setOverrideServiceTime(Double overrideServiceTime) {
		this.overrideServiceTime = overrideServiceTime;
	}

	public Long getNoOfCartons() {
		return noOfCartons;
	}

	public Long getNoOfFreezers() {
		return noOfFreezers;
	}

	public Long getNoOfCases() {
		return noOfCases;
	}

	public void setNoOfCartons(Long noOfCartons) {
		this.noOfCartons = noOfCartons;
	}

	public void setNoOfFreezers(Long noOfFreezers) {
		this.noOfFreezers = noOfFreezers;
	}

	public void setNoOfCases(Long noOfCases) {
		this.noOfCases = noOfCases;
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
		EnumReservationType type, String addressId, Date deliveryDate, String zoneCode, RoutingActivityType unassignedActivityType,boolean inUPS,
		Double overrideOrderSize, Double overrideServiceTime, Double reservedOrderSize, Double reservedServiceTime, 
		Long noOfCartons, Long noOfCases, Long noOfFreezers,EnumReservationClass rsvClass,  EnumRoutingUpdateStatus status, EnumOrderMetricsSource metricsSource,
		String buildingId, String locationId, int reservedOrdersAtBuilding, EnumRegionServiceType regionSvcType) {
			
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
		this.unassignedActivityType=unassignedActivityType;
		this.inUPS=inUPS;
	
		this.overrideOrderSize = overrideOrderSize;
		this.overrideServiceTime = overrideServiceTime;
		
		this.reservedOrderSize = reservedOrderSize;
		this.reservedServiceTime = reservedServiceTime;
		
		this.noOfCartons =  noOfCartons;
		this.noOfCases =  noOfCases;
		this.noOfFreezers =  noOfFreezers;
		this.rsvClass = rsvClass;
		this.updateStatus = status;
		
		this.buildingId = buildingId;
		this.locationId = locationId;
		this.reservedOrdersAtBuilding = reservedOrdersAtBuilding;
		this.regionSvcType = regionSvcType;
		
	}

	public DlvReservationModel(
		PrimaryKey pk,
		String orderId,
		String customerId,
		int statusCode,
		Date expirationDateTime,
		String timeslotId,
		String zoneId,
		com.freshdirect.delivery.EnumReservationType type2, String addressId, Date deliveryDate, String zoneCode,RoutingActivityType unassignedActivityType,boolean inUPS, 
		Double overrideOrderSize, Double overrideServiceTime, Double reservedOrderSize, Double reservedServiceTime,
		Long noOfCartons, Long noOfCases, Long noOfFreezers, EnumReservationClass rsvClass, EnumRoutingUpdateStatus status, EnumOrderMetricsSource metricsSource,
		String buildingId, String locationId, int reservedOrdersAtBuilding, EnumRegionServiceType regionSvcType) {
			
		this(orderId, customerId, statusCode, expirationDateTime, timeslotId, zoneId, type2, addressId,deliveryDate
						,zoneCode,unassignedActivityType,inUPS
						, overrideOrderSize, overrideServiceTime, reservedOrderSize, reservedServiceTime,
						noOfCartons, noOfCases, noOfFreezers, rsvClass, status, metricsSource,buildingId, locationId, reservedOrdersAtBuilding,regionSvcType);
		this.setPK(pk);

	}

	public DlvReservationModel(PrimaryKey pk, String orderId, int statusCode,
			Date expirationDateTime, EnumReservationType type, Date deliveryDate,
			String zoneCode, RoutingActivityType unassignedActivityType) {
		
		this.orderId = orderId;
		this.statusCode = statusCode;
		this.expirationDateTime = expirationDateTime;
		this.type = type;
		this.deliveryDate=deliveryDate;
		this.zoneCode=zoneCode;
		this.unassignedActivityType=unassignedActivityType;
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
	
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public EnumReservationClass getRsvClass() {
		return rsvClass;
	}

	public void setRsvClass(EnumReservationClass rsvClass) {
		this.rsvClass = rsvClass;
	}
	
	public boolean isPremium(){
		return EnumReservationClass.PREMIUM.equals(this.getRsvClass())||EnumReservationClass.PREMIUMCT.equals(this.getRsvClass());
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
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
	public boolean inBulkZone(){
		return (this.regionSvcType!=null && EnumRegionServiceType.HYBRID.equals(this.regionSvcType));
	}

}
