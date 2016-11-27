package com.freshdirect.mobileapi.controller.data;

import java.text.ParseException;
import java.util.Date;


public class SubmitOrderExResult extends Message{
	private boolean addressValidForAlcohol;
	
	private boolean timeSlotValidForAlcohol;
	
	private String alcoholRestrictionMessage;
	
	private boolean ageVerified;
	
	private AtpErrorData unavaialabilityData;
	
	private String status;

    private Date modificationCutoffTime;
    
    private String deliveryZone;
        
    private String orderNumber;
	
	public SubmitOrderExResult(){
		addressValidForAlcohol=true;
		timeSlotValidForAlcohol=true;
		ageVerified=true;
	}

	public boolean isAddressValidForAlcohol() {
		return addressValidForAlcohol;
	}

	public void setAddressValidForAlcohol(boolean addressValidForAlcohol) {
		this.addressValidForAlcohol = addressValidForAlcohol;
	}

	public boolean isTimeSlotValidForAlcohol() {
		return timeSlotValidForAlcohol;
	}

	public void setTimeSlotValidForAlcohol(boolean timeSlotValidForAlcohol) {
		this.timeSlotValidForAlcohol = timeSlotValidForAlcohol;
	}

	public String getAlcoholRestrictionMessage() {
		return alcoholRestrictionMessage;
	}

	public void setAlcoholRestrictionMessage(String alcoholRestrictionMessage) {
		this.alcoholRestrictionMessage = alcoholRestrictionMessage;
	}

	public boolean isAgeVerified() {
		return ageVerified;
	}

	public void setAgeVerified(boolean ageVerified) {
		this.ageVerified = ageVerified;
	}

	public AtpErrorData getUnavaialabilityData() {
		return unavaialabilityData;
	}

	public void setUnavaialabilityData(AtpErrorData unavaialabilityData) {
		this.unavaialabilityData = unavaialabilityData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModificationCutoffTime() {
		String formatterDate = null;
        if (this.modificationCutoffTime != null) {
            formatterDate = formatter.format(this.modificationCutoffTime);
        }
        return formatterDate;
	}

	public void setModificationCutoffTime(String modificationCutoffTime) {
		 try {
	            this.modificationCutoffTime = formatter.parse(modificationCutoffTime);
	        } catch (ParseException e) {
	            //Do nothing special.
	            this.modificationCutoffTime = null;
	        }
	}

	public String getDeliveryZone() {
		return deliveryZone;
	}

	public void setDeliveryZone(String deliveryZone) {
		this.deliveryZone = deliveryZone;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public  void wrap(com.freshdirect.mobileapi.controller.data.response.Order order){
		this.setStatus(order.getStatus());
		this.setModificationCutoffTime(order.getModificationCutoffTime());
		this.setDeliveryZone(order.getDeliveryZone());
		this.setOrderNumber(order.getOrderNumber());
		
	}

	
	
	
	
	

}
