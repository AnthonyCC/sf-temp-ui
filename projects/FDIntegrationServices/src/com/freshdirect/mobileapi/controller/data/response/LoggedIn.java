package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class LoggedIn extends Message {

    private String id;

    private String username;

    private boolean isChefTable;

    private Timeslot reservationTimeslot;

    private String firstName;
    
    private String lastName;

    private String customerServicePhoneNumber;
    
    private boolean isBrowseEnabled;
    
    private String newUser;

	private int orderCount;

    private int fdxOrderCount;

    //Added during Mobile Coremetrics Implementation
    private String selectedServiceType;
    private String cohort;
    private int totalOrderCount;
    
    private String fdUserId;

    private List<OrderHistory.Order> orders;
    
    //DOOR3 FD-iPad FDIP-474
    private boolean onMailingList = false;
    private String resultAction;
    private String resultMessage;
    private boolean tcAcknowledge;
    private boolean anonymousAddressSetFromAcc;
    private String plantId;
    private String mobileNumber;
    private List<String> providers;
    private String zipCode;

    private String erpCustomerPK;

    public boolean isTcAcknowledge() {
		return tcAcknowledge;
	}

	public void setTcAcknowledge(boolean tcAcknowledge) {
		this.tcAcknowledge = tcAcknowledge;
	}

	public String getFdUserId() {
		return fdUserId;
	}

	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

    public int getFdxOrderCount() {
        return fdxOrderCount;
    }

    public void setFdxOrderCount(int fdxOrderCount) {
        this.fdxOrderCount = fdxOrderCount;
    }

    public String getCustomerServicePhoneNumber() {
        return customerServicePhoneNumber;
    }

    public void setCustomerServicePhoneNumber(String customerServicePhoneNumber) {
        this.customerServicePhoneNumber = customerServicePhoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private int itemsInCartCount;

    public int getItemsInCartCount() {
        return itemsInCartCount;
    }

    public void setItemsInCartCount(int itemsInCartCount) {
        this.itemsInCartCount = itemsInCartCount;
    }

    public Timeslot getReservationTimeslot() {
        return reservationTimeslot;
    }

    public void setReservationTimeslot(Timeslot reservationTimeslot) {
        this.reservationTimeslot = reservationTimeslot;
    }

    public String getUsername() {
        return username;
    }

    public boolean isChefTable() {
        return isChefTable;
    }

    public void setChefTable(boolean isChefTable) {
        this.isChefTable = isChefTable;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OrderHistory.Order> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderHistory.Order> orders) {
        this.orders = orders;
    }

	public boolean isBrowseEnabled() {
		return isBrowseEnabled;
	}

	public void setBrowseEnabled(boolean isBrowseEnabled) {
		this.isBrowseEnabled = isBrowseEnabled;
	}

	public String getSelectedServiceType() {
		return selectedServiceType;
	}

	public void setSelectedServiceType(String selectedServiceType) {
		this.selectedServiceType = selectedServiceType;
	}

	public String getCohort() {
		return cohort;
	}

	public void setCohort(String cohort) {
		this.cohort = cohort;
	}

	public int getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(int totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	//*************************************************************8
	//DOOR3 FD-iPad FDIP-474
    public boolean isOnMailingList() {
		return onMailingList;
	}

	public void setOnMailingList(boolean onMailingList) {
		this.onMailingList = onMailingList;
	}
	//*************************************************************8

	   
    public String getNewUser() {
		return newUser;
	}

	public void setNewUser(String newUser) {
		this.newUser = newUser;
	}

	public String getResultAction() {
		return resultAction;
	}

	public void setResultAction(String resultAction) {
		this.resultAction = resultAction;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public boolean isAnonymousAddressSetFromAcc() {
		return anonymousAddressSetFromAcc;
	}

	public void setAnonymousAddressSetFromAcc(boolean anonymousAddressSetFromAcc) {
		this.anonymousAddressSetFromAcc = anonymousAddressSetFromAcc;
	}

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<String> getProviders() {
        return providers;
    }

    public void setProviders(List<String> providers) {
        this.providers = providers;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    private boolean isreferralEligible;

	public boolean isIsreferralEligible() {
		return isreferralEligible;
	}

	public void setIsreferralEligible(boolean isreferralEligible) {
		this.isreferralEligible = isreferralEligible;
	}

    public String getErpCustomerPK() {
        return erpCustomerPK;
    }

    public void setErpCustomerPK(String erpCustomerPK) {
        this.erpCustomerPK = erpCustomerPK;
    }
}
