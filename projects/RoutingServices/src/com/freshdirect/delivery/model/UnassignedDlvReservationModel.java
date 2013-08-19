package com.freshdirect.delivery.model;

import java.util.Date;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.EnumRegionServiceType;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.constants.RoutingActivityType;

public class UnassignedDlvReservationModel extends DlvReservationModel {

	private ContactAddressModel address;
	private String cutoff;
	private Date startTime;
	private Date endTime;
	
	public UnassignedDlvReservationModel(PrimaryKey pk, String orderId,
			String customerId, int statusCode, Date expirationDateTime,
			String timeslotId, String zoneId, EnumReservationType type,
			ContactAddressModel address, Date deliveryDate,String cutoff,Date stime, Date etime, String zoneCode,
			RoutingActivityType unassignedActivityType, boolean inUPS,
			Double overrideOrderSize, Double overrideServiceTime,
			Double reservedOrderSize, Double reservedServiceTime,
			Long noOfCartons, Long noOfCases, Long noOfFreezers, EnumReservationClass rsvClass, 
			EnumRoutingUpdateStatus status, EnumOrderMetricsSource metricsSource,
			String buildingId, String locationId, int reservedOrdersAtBuilding,EnumRegionServiceType regionSvcType) {
		super(pk, orderId, customerId, statusCode, expirationDateTime, timeslotId,
				zoneId, type, address!=null?address.getId():null, deliveryDate, zoneCode,
				unassignedActivityType, inUPS, overrideOrderSize, overrideServiceTime,
				reservedOrderSize, reservedServiceTime, noOfCartons, noOfCases,
				noOfFreezers, rsvClass, status, metricsSource,buildingId, locationId, reservedOrdersAtBuilding,regionSvcType);
		this.address=address;
		this.cutoff = cutoff;
		this.setStartTime(stime);
		this.setEndTime(etime);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7211909957874706385L;
	
	public ContactAddressModel getAddress(){
		return address;
	}
	public String getCutoff(){
		return cutoff;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
