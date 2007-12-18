package com.freshdirect.mktAdmin.model;

import java.io.Serializable;

public class RestrictedPromoCustomerModel implements Serializable {

	private String promotionId=null;
	private String customerId=null;
	private int usageCount=0;
	private String custEmailAddress=null;
	private String firstName=null;
	private String lastName=null;
	
	public RestrictedPromoCustomerModel(){		
	}
	
	public String getCustEmailAddress() {
		return custEmailAddress;
	}
	public void setCustEmailAddress(String custEmailAddress) {
		this.custEmailAddress = custEmailAddress;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	public int getUsageCount() {
		return usageCount;
	}
	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	
	
	public boolean equals(Object o){
	
		if(o==this) return true;
		
		if(o instanceof RestrictedPromoCustomerModel){
			RestrictedPromoCustomerModel model=(RestrictedPromoCustomerModel)o;
			if(this.customerId!=null && this.customerId.equalsIgnoreCase(model.getCustomerId())){
				if(this.promotionId!=null && this.promotionId.equalsIgnoreCase(model.getPromotionId())){
					return true;
				}				
			}
		}		
		return false;
	}
	
	public int hashCode()
	{		
		if(this.customerId!=null && this.promotionId!=null) return this.customerId.hashCode()*this.promotionId.hashCode();
		else return super.hashCode();
	}
}
