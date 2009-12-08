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

    /**
     * @author Rob
     *
     */

}
