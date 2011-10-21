package com.freshdirect.delivery.model;

import java.util.Date;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;

public class UnassignedDlvReservationModel extends DlvReservationModel {

	private ContactAddressModel address;

	
	public UnassignedDlvReservationModel(PrimaryKey pk, String orderId,
			String customerId, int statusCode, Date expirationDateTime,
			String timeslotId, String zoneId, EnumReservationType type,
			ContactAddressModel address, Date deliveryDate, String zoneCode,
			RoutingActivityType unassignedActivityType, boolean inUPS,
			Double overrideOrderSize, Double overrideServiceTime,
			Double reservedOrderSize, Double reservedServiceTime,
			Long noOfCartons, Long noOfCases, Long noOfFreezers,
			EnumRoutingUpdateStatus status, EnumOrderMetricsSource metricsSource) {
		super(pk, orderId, customerId, statusCode, expirationDateTime, timeslotId,
				zoneId, type, address!=null?address.getId():null, deliveryDate, zoneCode,
				unassignedActivityType, inUPS, overrideOrderSize, overrideServiceTime,
				reservedOrderSize, reservedServiceTime, noOfCartons, noOfCases,
				noOfFreezers, status, metricsSource);
		this.address=address;
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7211909957874706385L;
	
	public ContactAddressModel getAddress(){
		return address;
	}

}
