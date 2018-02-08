package com.freshdirect.mobileapi.controller.data.response;

import java.util.Set;

import com.freshdirect.mobileapi.controller.data.PhoneNumber;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.StoreModel;

public class ShipToAddress extends DeliveryAddress {

    private String firstName;

    private String lastName;

    private PhoneNumber contactPhoneNumber;

    private PhoneNumber altContactPhoneNumber;

    private String altFirstName;

    private String altLastName;

    private String altApartment;

    private PhoneNumber altPhone;

    private String altType;
    
//Appdev 4351 : Delivery Instructions not Recorded
    private String deliveryInstruction;
	
    private String instructions;
    
    private Set availableServiceTypes;
    
    private String unattendedDeliveryFlag;
    
    private String unattendedDeliveryInstructions;

	public Set getAvailableServiceTypes() {
		return availableServiceTypes;
	}

	public ShipToAddress(com.freshdirect.mobileapi.model.ShipToAddress address) {
		StoreModel store = ContentFactory.getInstance().getStore();
		this.id = address.getId();
        if (null != address.getType()) {
            this.type = address.getType().toString();
        }
        this.id = address.getId();
        this.firstName = address.getFirstName();
        this.lastName = address.getLastName();
        this.companyName = address.getCompanyName();
        this.street1 = address.getStreet1();
        this.apartment = address.getApartment();
        this.street2 = address.getStreet2();
        this.city = address.getCity();
        this.state = address.getState();
        this.postalCode = address.getPostalCode();
        
        //Appdev 4351 : Delivery Instructions not Recorded
        this.deliveryInstruction = address.getDeliveryInstruction();
        
        if (null != address.getContactPhone()) {
            this.contactPhoneNumber = new PhoneNumber(address.getContactPhone().getPhone(), address.getContactPhone().getExtension());
        }
        if (null != address.getAltContactPhone()) {
            this.altContactPhoneNumber = new PhoneNumber(address.getAltContactPhone().getPhone(), address.getAltContactPhone()
                    .getExtension());
        }
        this.altFirstName = address.getAltDeliveryFirstName();
        this.altLastName = address.getAltDeliveryLastName();
        this.altApartment = address.getAltDeliveryApt();
        if (null != address.getAltDeliveryPhone()) {
            this.altPhone = new PhoneNumber(address.getAltDeliveryPhone().getPhone(), address.getAltDeliveryPhone().getExtension());
        }
        this.altType = (address.getAltType() == null ? null : address.getAltType().getCode());
        this.instructions = address.getInstructions();
        //APPDEV-5440 - FD Mobile - latency issue with server calls
        if(store!=null && store.getContentName()!=null && "FDX".equals(store.getContentName())) {
            this.availableServiceTypes = address.getAvailableServiceTypes();
         }
        
        this.unattendedDeliveryFlag = address.getUnattendedDeliveryFlag().getName();
        this.unattendedDeliveryInstructions = address.getUnattendedDeliveryInstructions();
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
    
    public void setAvailableServiceTypes(Set<String> availableServiceTypes) {
		this.availableServiceTypes = availableServiceTypes;
	}

    public PhoneNumber getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(PhoneNumber contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public PhoneNumber getAltContactPhoneNumber() {
        return altContactPhoneNumber;
    }

    public void setAltContactPhoneNumber(PhoneNumber altContactPhoneNumber) {
        this.altContactPhoneNumber = altContactPhoneNumber;
    }

    public String getAltFirstName() {
        return altFirstName;
    }

    public void setAltFirstName(String altFirstName) {
        this.altFirstName = altFirstName;
    }

    public String getAltLastName() {
        return altLastName;
    }

    public void setAltLastName(String altLastName) {
        this.altLastName = altLastName;
    }

    public String getAltApartment() {
        return altApartment;
    }

    public void setAltApartment(String altApartment) {
        this.altApartment = altApartment;
    }

    public PhoneNumber getAltPhone() {
        return altPhone;
    }

    public void setAltPhone(PhoneNumber altPhone) {
        this.altPhone = altPhone;
    }

    public String getAltType() {
        return altType;
    }

    public void setAltType(String altType) {
        this.altType = altType;
    }

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	  //Appdev 4351 : Delivery Instructions not Recorded
	public String getDeliveryInstruction() {
		return deliveryInstruction;
	}

	public void setDeliveryInstruction(String deliveryInstruction) {
		this.deliveryInstruction = deliveryInstruction;
	}

    public String getUnattendedDeliveryFlag() {
        return unattendedDeliveryFlag;
    }

    public void setUnattendedDeliveryFlag(String unattendedDeliveryFlag) {
        this.unattendedDeliveryFlag = unattendedDeliveryFlag;
    }
    
    public String getUnattendedDeliveryInstructions() {
        return unattendedDeliveryInstructions;
    }
    
    public void setUnattendedDeliveryInstructions(String unattendedDeliveryInstructions) {
        this.unattendedDeliveryInstructions = unattendedDeliveryInstructions;
    }
}
