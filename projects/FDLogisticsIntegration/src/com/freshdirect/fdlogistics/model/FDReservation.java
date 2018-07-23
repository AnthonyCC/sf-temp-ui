package com.freshdirect.fdlogistics.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdlogistics.deserializer.EnumRegionServiceTypeDeserializer;
import com.freshdirect.fdlogistics.deserializer.EnumReservationClassDeserializer;
import com.freshdirect.fdlogistics.deserializer.EnumReservationTypeDeserializer;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.logistics.delivery.model.EnumRegionServiceType;
import com.freshdirect.logistics.delivery.model.EnumReservationClass;
import com.freshdirect.logistics.delivery.model.EnumReservationType;



public class FDReservation extends ModelSupport {
	
	private static final long	serialVersionUID	= -8318474657729420003L;
	
	private final Date expirationDateTime;
	@JsonDeserialize(using = EnumReservationTypeDeserializer.class)
	private final EnumReservationType type;
	private FDTimeslot timeslot;
	private final String customerId;
	private final String addressId;
	private final String orderId;
	private final boolean chefsTable;
	private final int statusCode;
	@JsonDeserialize(using = EnumReservationClassDeserializer.class)
	private final EnumReservationClass rsvClass;
	private boolean hasSteeringDiscount;
	@JsonDeserialize(using = EnumRegionServiceTypeDeserializer.class)
	private EnumRegionServiceType regionSvcType;
	private FDDeliveryETAModel deliveryETA;
	private String deliveryFeeTier;
	private ErpAddressModel address;

	public FDReservation(
		PrimaryKey pk,
		FDTimeslot timeslot,
		Date expirationDateTime,
		EnumReservationType type,
		String customerId,
		String addressId,
		boolean chefsTable,
		String orderId,
		int statusCode, EnumReservationClass rsvClass, boolean hasSteeringDiscount,
		EnumRegionServiceType regionSvcType, String deliveryFeeTier) {
		this.setPK(pk);
		this.timeslot = timeslot;
		this.expirationDateTime = expirationDateTime;
		this.type = type;
		this.customerId = customerId;
		this.addressId = addressId;
		this.chefsTable = chefsTable;
		this.orderId = orderId;
		this.statusCode=statusCode;
		this.rsvClass=rsvClass;
		this.hasSteeringDiscount = hasSteeringDiscount;
		this.regionSvcType = regionSvcType;
		this.deliveryFeeTier = deliveryFeeTier;
	}
	
	public FDReservation(
			@JsonProperty("pk") PrimaryKey pk,
			@JsonProperty("timeslot") FDTimeslot timeslot,
			@JsonProperty("expirationDateTime") Date expirationDateTime,
			@JsonProperty("type") EnumReservationType type,
			@JsonProperty("customerId") String customerId,
			@JsonProperty("addressId") String addressId,
			@JsonProperty("address") ErpAddressModel address,
			@JsonProperty("chefsTable") boolean chefsTable,
			@JsonProperty("orderId") String orderId,
			@JsonProperty("statusCode") int statusCode, 
			@JsonProperty("rsvClass") EnumReservationClass rsvClass, 
			@JsonProperty("hasSteeringDiscount") boolean hasSteeringDiscount,
			@JsonProperty("regionSvcType") EnumRegionServiceType regionSvcType, 
			@JsonProperty("deliveryFeeTier") String deliveryFeeTier)  {
			this.setPK(pk);
			this.timeslot = timeslot;
			this.expirationDateTime = expirationDateTime;
			this.type = type;
			this.customerId = customerId;
			this.addressId = addressId;
			this.address = address;
			this.chefsTable = chefsTable;
			this.orderId = orderId;
			this.statusCode=statusCode;
			this.rsvClass=rsvClass;
			this.hasSteeringDiscount = hasSteeringDiscount;
			this.regionSvcType = regionSvcType;
			this.deliveryFeeTier = deliveryFeeTier;
		}
	
	
	public EnumReservationType getType() {
		return type;
	}
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public String getTimeslotId() {
		return this.timeslot.getId();
	}
	
	public FDTimeslot getTimeslot() {
		return this.timeslot;
	}
	
	public void setTimeslot(FDTimeslot timeslot) {
		this.timeslot = timeslot;
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
		return this.timeslot.getStartDateTime();
	}
	
	public Date getDeliveryDate() {
		return this.timeslot.getDeliveryDate();
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
	public EnumRegionServiceType getRegionSvcType() {
		return regionSvcType;
	}
	public void setRegionSvcType(EnumRegionServiceType regionSvcType) {
		this.regionSvcType = regionSvcType;
	}
	public boolean isMinOrderMet() {
		return this.timeslot.isMinOrderMet();
	}
	public double getMinOrderAmt() {
		return this.timeslot.getMinOrderAmt();
	}
	public boolean isMinOrderSlot() {
		return this.timeslot.isMinOrderSlot();
	}
	public FDDeliveryETAModel getDeliveryETA() {
		return deliveryETA;
	}
	public void setDeliveryETA(FDDeliveryETAModel deliveryETA) {
		this.deliveryETA = deliveryETA;
	}


	public String getDeliveryFeeTier() {
		return deliveryFeeTier;
	}


	public void setDeliveryFeeTier(String deliveryFeeTier) {
		this.deliveryFeeTier = deliveryFeeTier;
	}

	public ErpAddressModel getAddress() {
		return address;
	}

	public void setAddress(ErpAddressModel address) {
		this.address = address;
	}

}
