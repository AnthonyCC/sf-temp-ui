package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.OrderInfo;

/**
 * @author Rob
 *
 */
public class LoggedIn extends Message {

    private String id;

    private String username;

    private boolean isChefTable;

    private Timeslot reservationTimeslot;

    private String firstName;
    
    private String customerServicePhoneNumber;
    
    private boolean isBrowseEnabled;
    
    private int orderCount;
    
    //Added during Mobile Coremetrics Implementation
    private String selectedServiceType;
    private String cohort;
    private int totalOrderCount;
    
    private String fdUserId;
    
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

    private List<OrderHistory.Order> orders;

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
		
}
