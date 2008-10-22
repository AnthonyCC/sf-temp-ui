/**
 * RoutingStop.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingStop  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity;

    private int sequenceNumber;

    private int stopIx;

    private java.util.Calendar arrival;

    private int serviceTime;

    private int travelTime;

    private double distance;

    private org.apache.axis.types.Time tw1OpenTime;

    private org.apache.axis.types.Time tw1CloseTime;

    private org.apache.axis.types.Time tw2OpenTime;

    private org.apache.axis.types.Time tw2CloseTime;

    private int deliveryCost;

    private com.freshdirect.routing.proxy.stub.transportation.StopType stopType;

    private org.apache.axis.types.Time openTime;

    private org.apache.axis.types.Time closeTime;

    private int standardFragmentNumber;

    private java.lang.String locationName;

    private java.lang.String storeNumber;

    private java.lang.String contact;

    private com.freshdirect.routing.proxy.stub.transportation.Address address;

    private java.lang.String phoneNumber;

    private java.lang.String deliveryDays;

    private int latitude;

    private int longitude;

    private int buildingDeliverySequence;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone;

    private java.util.Date sessionDate;

    private java.lang.String sessionDescription;

    private java.lang.String routeID;

    private java.lang.String routeDescription;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus routeStatus;

    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driver1Identity;

    private java.lang.String driver1Name;

    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driver2Identity;

    private java.lang.String driver2Name;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities deliveryQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities pickupQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] orders;

    private boolean actualsExist;

    private int actualSequenceNumber;

    private java.util.Calendar actualArrival;

    private int actualServiceTime;

    private int actualTravelTime;

    private double actualDistance;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities actualQuantity;

    public RoutingStop() {
    }

    public RoutingStop(
           com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity,
           int sequenceNumber,
           int stopIx,
           java.util.Calendar arrival,
           int serviceTime,
           int travelTime,
           double distance,
           org.apache.axis.types.Time tw1OpenTime,
           org.apache.axis.types.Time tw1CloseTime,
           org.apache.axis.types.Time tw2OpenTime,
           org.apache.axis.types.Time tw2CloseTime,
           int deliveryCost,
           com.freshdirect.routing.proxy.stub.transportation.StopType stopType,
           org.apache.axis.types.Time openTime,
           org.apache.axis.types.Time closeTime,
           int standardFragmentNumber,
           java.lang.String locationName,
           java.lang.String storeNumber,
           java.lang.String contact,
           com.freshdirect.routing.proxy.stub.transportation.Address address,
           java.lang.String phoneNumber,
           java.lang.String deliveryDays,
           int latitude,
           int longitude,
           int buildingDeliverySequence,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone,
           java.util.Date sessionDate,
           java.lang.String sessionDescription,
           java.lang.String routeID,
           java.lang.String routeDescription,
           com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus routeStatus,
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driver1Identity,
           java.lang.String driver1Name,
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driver2Identity,
           java.lang.String driver2Name,
           com.freshdirect.routing.proxy.stub.transportation.Quantities deliveryQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities pickupQuantity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] orders,
           boolean actualsExist,
           int actualSequenceNumber,
           java.util.Calendar actualArrival,
           int actualServiceTime,
           int actualTravelTime,
           double actualDistance,
           com.freshdirect.routing.proxy.stub.transportation.Quantities actualQuantity) {
           this.routeIdentity = routeIdentity;
           this.stopIdentity = stopIdentity;
           this.locationIdentity = locationIdentity;
           this.sequenceNumber = sequenceNumber;
           this.stopIx = stopIx;
           this.arrival = arrival;
           this.serviceTime = serviceTime;
           this.travelTime = travelTime;
           this.distance = distance;
           this.tw1OpenTime = tw1OpenTime;
           this.tw1CloseTime = tw1CloseTime;
           this.tw2OpenTime = tw2OpenTime;
           this.tw2CloseTime = tw2CloseTime;
           this.deliveryCost = deliveryCost;
           this.stopType = stopType;
           this.openTime = openTime;
           this.closeTime = closeTime;
           this.standardFragmentNumber = standardFragmentNumber;
           this.locationName = locationName;
           this.storeNumber = storeNumber;
           this.contact = contact;
           this.address = address;
           this.phoneNumber = phoneNumber;
           this.deliveryDays = deliveryDays;
           this.latitude = latitude;
           this.longitude = longitude;
           this.buildingDeliverySequence = buildingDeliverySequence;
           this.timeZone = timeZone;
           this.sessionDate = sessionDate;
           this.sessionDescription = sessionDescription;
           this.routeID = routeID;
           this.routeDescription = routeDescription;
           this.routeStatus = routeStatus;
           this.driver1Identity = driver1Identity;
           this.driver1Name = driver1Name;
           this.driver2Identity = driver2Identity;
           this.driver2Name = driver2Name;
           this.deliveryQuantity = deliveryQuantity;
           this.pickupQuantity = pickupQuantity;
           this.orders = orders;
           this.actualsExist = actualsExist;
           this.actualSequenceNumber = actualSequenceNumber;
           this.actualArrival = actualArrival;
           this.actualServiceTime = actualServiceTime;
           this.actualTravelTime = actualTravelTime;
           this.actualDistance = actualDistance;
           this.actualQuantity = actualQuantity;
    }


    /**
     * Gets the routeIdentity value for this RoutingStop.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this RoutingStop.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the stopIdentity value for this RoutingStop.
     * 
     * @return stopIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity getStopIdentity() {
        return stopIdentity;
    }


    /**
     * Sets the stopIdentity value for this RoutingStop.
     * 
     * @param stopIdentity
     */
    public void setStopIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity) {
        this.stopIdentity = stopIdentity;
    }


    /**
     * Gets the locationIdentity value for this RoutingStop.
     * 
     * @return locationIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getLocationIdentity() {
        return locationIdentity;
    }


    /**
     * Sets the locationIdentity value for this RoutingStop.
     * 
     * @param locationIdentity
     */
    public void setLocationIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity) {
        this.locationIdentity = locationIdentity;
    }


    /**
     * Gets the sequenceNumber value for this RoutingStop.
     * 
     * @return sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }


    /**
     * Sets the sequenceNumber value for this RoutingStop.
     * 
     * @param sequenceNumber
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }


    /**
     * Gets the stopIx value for this RoutingStop.
     * 
     * @return stopIx
     */
    public int getStopIx() {
        return stopIx;
    }


    /**
     * Sets the stopIx value for this RoutingStop.
     * 
     * @param stopIx
     */
    public void setStopIx(int stopIx) {
        this.stopIx = stopIx;
    }


    /**
     * Gets the arrival value for this RoutingStop.
     * 
     * @return arrival
     */
    public java.util.Calendar getArrival() {
        return arrival;
    }


    /**
     * Sets the arrival value for this RoutingStop.
     * 
     * @param arrival
     */
    public void setArrival(java.util.Calendar arrival) {
        this.arrival = arrival;
    }


    /**
     * Gets the serviceTime value for this RoutingStop.
     * 
     * @return serviceTime
     */
    public int getServiceTime() {
        return serviceTime;
    }


    /**
     * Sets the serviceTime value for this RoutingStop.
     * 
     * @param serviceTime
     */
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }


    /**
     * Gets the travelTime value for this RoutingStop.
     * 
     * @return travelTime
     */
    public int getTravelTime() {
        return travelTime;
    }


    /**
     * Sets the travelTime value for this RoutingStop.
     * 
     * @param travelTime
     */
    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }


    /**
     * Gets the distance value for this RoutingStop.
     * 
     * @return distance
     */
    public double getDistance() {
        return distance;
    }


    /**
     * Sets the distance value for this RoutingStop.
     * 
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }


    /**
     * Gets the tw1OpenTime value for this RoutingStop.
     * 
     * @return tw1OpenTime
     */
    public org.apache.axis.types.Time getTw1OpenTime() {
        return tw1OpenTime;
    }


    /**
     * Sets the tw1OpenTime value for this RoutingStop.
     * 
     * @param tw1OpenTime
     */
    public void setTw1OpenTime(org.apache.axis.types.Time tw1OpenTime) {
        this.tw1OpenTime = tw1OpenTime;
    }


    /**
     * Gets the tw1CloseTime value for this RoutingStop.
     * 
     * @return tw1CloseTime
     */
    public org.apache.axis.types.Time getTw1CloseTime() {
        return tw1CloseTime;
    }


    /**
     * Sets the tw1CloseTime value for this RoutingStop.
     * 
     * @param tw1CloseTime
     */
    public void setTw1CloseTime(org.apache.axis.types.Time tw1CloseTime) {
        this.tw1CloseTime = tw1CloseTime;
    }


    /**
     * Gets the tw2OpenTime value for this RoutingStop.
     * 
     * @return tw2OpenTime
     */
    public org.apache.axis.types.Time getTw2OpenTime() {
        return tw2OpenTime;
    }


    /**
     * Sets the tw2OpenTime value for this RoutingStop.
     * 
     * @param tw2OpenTime
     */
    public void setTw2OpenTime(org.apache.axis.types.Time tw2OpenTime) {
        this.tw2OpenTime = tw2OpenTime;
    }


    /**
     * Gets the tw2CloseTime value for this RoutingStop.
     * 
     * @return tw2CloseTime
     */
    public org.apache.axis.types.Time getTw2CloseTime() {
        return tw2CloseTime;
    }


    /**
     * Sets the tw2CloseTime value for this RoutingStop.
     * 
     * @param tw2CloseTime
     */
    public void setTw2CloseTime(org.apache.axis.types.Time tw2CloseTime) {
        this.tw2CloseTime = tw2CloseTime;
    }


    /**
     * Gets the deliveryCost value for this RoutingStop.
     * 
     * @return deliveryCost
     */
    public int getDeliveryCost() {
        return deliveryCost;
    }


    /**
     * Sets the deliveryCost value for this RoutingStop.
     * 
     * @param deliveryCost
     */
    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }


    /**
     * Gets the stopType value for this RoutingStop.
     * 
     * @return stopType
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopType getStopType() {
        return stopType;
    }


    /**
     * Sets the stopType value for this RoutingStop.
     * 
     * @param stopType
     */
    public void setStopType(com.freshdirect.routing.proxy.stub.transportation.StopType stopType) {
        this.stopType = stopType;
    }


    /**
     * Gets the openTime value for this RoutingStop.
     * 
     * @return openTime
     */
    public org.apache.axis.types.Time getOpenTime() {
        return openTime;
    }


    /**
     * Sets the openTime value for this RoutingStop.
     * 
     * @param openTime
     */
    public void setOpenTime(org.apache.axis.types.Time openTime) {
        this.openTime = openTime;
    }


    /**
     * Gets the closeTime value for this RoutingStop.
     * 
     * @return closeTime
     */
    public org.apache.axis.types.Time getCloseTime() {
        return closeTime;
    }


    /**
     * Sets the closeTime value for this RoutingStop.
     * 
     * @param closeTime
     */
    public void setCloseTime(org.apache.axis.types.Time closeTime) {
        this.closeTime = closeTime;
    }


    /**
     * Gets the standardFragmentNumber value for this RoutingStop.
     * 
     * @return standardFragmentNumber
     */
    public int getStandardFragmentNumber() {
        return standardFragmentNumber;
    }


    /**
     * Sets the standardFragmentNumber value for this RoutingStop.
     * 
     * @param standardFragmentNumber
     */
    public void setStandardFragmentNumber(int standardFragmentNumber) {
        this.standardFragmentNumber = standardFragmentNumber;
    }


    /**
     * Gets the locationName value for this RoutingStop.
     * 
     * @return locationName
     */
    public java.lang.String getLocationName() {
        return locationName;
    }


    /**
     * Sets the locationName value for this RoutingStop.
     * 
     * @param locationName
     */
    public void setLocationName(java.lang.String locationName) {
        this.locationName = locationName;
    }


    /**
     * Gets the storeNumber value for this RoutingStop.
     * 
     * @return storeNumber
     */
    public java.lang.String getStoreNumber() {
        return storeNumber;
    }


    /**
     * Sets the storeNumber value for this RoutingStop.
     * 
     * @param storeNumber
     */
    public void setStoreNumber(java.lang.String storeNumber) {
        this.storeNumber = storeNumber;
    }


    /**
     * Gets the contact value for this RoutingStop.
     * 
     * @return contact
     */
    public java.lang.String getContact() {
        return contact;
    }


    /**
     * Sets the contact value for this RoutingStop.
     * 
     * @param contact
     */
    public void setContact(java.lang.String contact) {
        this.contact = contact;
    }


    /**
     * Gets the address value for this RoutingStop.
     * 
     * @return address
     */
    public com.freshdirect.routing.proxy.stub.transportation.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this RoutingStop.
     * 
     * @param address
     */
    public void setAddress(com.freshdirect.routing.proxy.stub.transportation.Address address) {
        this.address = address;
    }


    /**
     * Gets the phoneNumber value for this RoutingStop.
     * 
     * @return phoneNumber
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * Sets the phoneNumber value for this RoutingStop.
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Gets the deliveryDays value for this RoutingStop.
     * 
     * @return deliveryDays
     */
    public java.lang.String getDeliveryDays() {
        return deliveryDays;
    }


    /**
     * Sets the deliveryDays value for this RoutingStop.
     * 
     * @param deliveryDays
     */
    public void setDeliveryDays(java.lang.String deliveryDays) {
        this.deliveryDays = deliveryDays;
    }


    /**
     * Gets the latitude value for this RoutingStop.
     * 
     * @return latitude
     */
    public int getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude value for this RoutingStop.
     * 
     * @param latitude
     */
    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets the longitude value for this RoutingStop.
     * 
     * @return longitude
     */
    public int getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude value for this RoutingStop.
     * 
     * @param longitude
     */
    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }


    /**
     * Gets the buildingDeliverySequence value for this RoutingStop.
     * 
     * @return buildingDeliverySequence
     */
    public int getBuildingDeliverySequence() {
        return buildingDeliverySequence;
    }


    /**
     * Sets the buildingDeliverySequence value for this RoutingStop.
     * 
     * @param buildingDeliverySequence
     */
    public void setBuildingDeliverySequence(int buildingDeliverySequence) {
        this.buildingDeliverySequence = buildingDeliverySequence;
    }


    /**
     * Gets the timeZone value for this RoutingStop.
     * 
     * @return timeZone
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue getTimeZone() {
        return timeZone;
    }


    /**
     * Sets the timeZone value for this RoutingStop.
     * 
     * @param timeZone
     */
    public void setTimeZone(com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone) {
        this.timeZone = timeZone;
    }


    /**
     * Gets the sessionDate value for this RoutingStop.
     * 
     * @return sessionDate
     */
    public java.util.Date getSessionDate() {
        return sessionDate;
    }


    /**
     * Sets the sessionDate value for this RoutingStop.
     * 
     * @param sessionDate
     */
    public void setSessionDate(java.util.Date sessionDate) {
        this.sessionDate = sessionDate;
    }


    /**
     * Gets the sessionDescription value for this RoutingStop.
     * 
     * @return sessionDescription
     */
    public java.lang.String getSessionDescription() {
        return sessionDescription;
    }


    /**
     * Sets the sessionDescription value for this RoutingStop.
     * 
     * @param sessionDescription
     */
    public void setSessionDescription(java.lang.String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }


    /**
     * Gets the routeID value for this RoutingStop.
     * 
     * @return routeID
     */
    public java.lang.String getRouteID() {
        return routeID;
    }


    /**
     * Sets the routeID value for this RoutingStop.
     * 
     * @param routeID
     */
    public void setRouteID(java.lang.String routeID) {
        this.routeID = routeID;
    }


    /**
     * Gets the routeDescription value for this RoutingStop.
     * 
     * @return routeDescription
     */
    public java.lang.String getRouteDescription() {
        return routeDescription;
    }


    /**
     * Sets the routeDescription value for this RoutingStop.
     * 
     * @param routeDescription
     */
    public void setRouteDescription(java.lang.String routeDescription) {
        this.routeDescription = routeDescription;
    }


    /**
     * Gets the routeStatus value for this RoutingStop.
     * 
     * @return routeStatus
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus getRouteStatus() {
        return routeStatus;
    }


    /**
     * Sets the routeStatus value for this RoutingStop.
     * 
     * @param routeStatus
     */
    public void setRouteStatus(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus routeStatus) {
        this.routeStatus = routeStatus;
    }


    /**
     * Gets the driver1Identity value for this RoutingStop.
     * 
     * @return driver1Identity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getDriver1Identity() {
        return driver1Identity;
    }


    /**
     * Sets the driver1Identity value for this RoutingStop.
     * 
     * @param driver1Identity
     */
    public void setDriver1Identity(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driver1Identity) {
        this.driver1Identity = driver1Identity;
    }


    /**
     * Gets the driver1Name value for this RoutingStop.
     * 
     * @return driver1Name
     */
    public java.lang.String getDriver1Name() {
        return driver1Name;
    }


    /**
     * Sets the driver1Name value for this RoutingStop.
     * 
     * @param driver1Name
     */
    public void setDriver1Name(java.lang.String driver1Name) {
        this.driver1Name = driver1Name;
    }


    /**
     * Gets the driver2Identity value for this RoutingStop.
     * 
     * @return driver2Identity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getDriver2Identity() {
        return driver2Identity;
    }


    /**
     * Sets the driver2Identity value for this RoutingStop.
     * 
     * @param driver2Identity
     */
    public void setDriver2Identity(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driver2Identity) {
        this.driver2Identity = driver2Identity;
    }


    /**
     * Gets the driver2Name value for this RoutingStop.
     * 
     * @return driver2Name
     */
    public java.lang.String getDriver2Name() {
        return driver2Name;
    }


    /**
     * Sets the driver2Name value for this RoutingStop.
     * 
     * @param driver2Name
     */
    public void setDriver2Name(java.lang.String driver2Name) {
        this.driver2Name = driver2Name;
    }


    /**
     * Gets the deliveryQuantity value for this RoutingStop.
     * 
     * @return deliveryQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getDeliveryQuantity() {
        return deliveryQuantity;
    }


    /**
     * Sets the deliveryQuantity value for this RoutingStop.
     * 
     * @param deliveryQuantity
     */
    public void setDeliveryQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }


    /**
     * Gets the pickupQuantity value for this RoutingStop.
     * 
     * @return pickupQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getPickupQuantity() {
        return pickupQuantity;
    }


    /**
     * Sets the pickupQuantity value for this RoutingStop.
     * 
     * @param pickupQuantity
     */
    public void setPickupQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities pickupQuantity) {
        this.pickupQuantity = pickupQuantity;
    }


    /**
     * Gets the orders value for this RoutingStop.
     * 
     * @return orders
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] getOrders() {
        return orders;
    }


    /**
     * Sets the orders value for this RoutingStop.
     * 
     * @param orders
     */
    public void setOrders(com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] orders) {
        this.orders = orders;
    }

    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder getOrders(int i) {
        return this.orders[i];
    }

    public void setOrders(int i, com.freshdirect.routing.proxy.stub.transportation.RoutingOrder _value) {
        this.orders[i] = _value;
    }


    /**
     * Gets the actualsExist value for this RoutingStop.
     * 
     * @return actualsExist
     */
    public boolean isActualsExist() {
        return actualsExist;
    }


    /**
     * Sets the actualsExist value for this RoutingStop.
     * 
     * @param actualsExist
     */
    public void setActualsExist(boolean actualsExist) {
        this.actualsExist = actualsExist;
    }


    /**
     * Gets the actualSequenceNumber value for this RoutingStop.
     * 
     * @return actualSequenceNumber
     */
    public int getActualSequenceNumber() {
        return actualSequenceNumber;
    }


    /**
     * Sets the actualSequenceNumber value for this RoutingStop.
     * 
     * @param actualSequenceNumber
     */
    public void setActualSequenceNumber(int actualSequenceNumber) {
        this.actualSequenceNumber = actualSequenceNumber;
    }


    /**
     * Gets the actualArrival value for this RoutingStop.
     * 
     * @return actualArrival
     */
    public java.util.Calendar getActualArrival() {
        return actualArrival;
    }


    /**
     * Sets the actualArrival value for this RoutingStop.
     * 
     * @param actualArrival
     */
    public void setActualArrival(java.util.Calendar actualArrival) {
        this.actualArrival = actualArrival;
    }


    /**
     * Gets the actualServiceTime value for this RoutingStop.
     * 
     * @return actualServiceTime
     */
    public int getActualServiceTime() {
        return actualServiceTime;
    }


    /**
     * Sets the actualServiceTime value for this RoutingStop.
     * 
     * @param actualServiceTime
     */
    public void setActualServiceTime(int actualServiceTime) {
        this.actualServiceTime = actualServiceTime;
    }


    /**
     * Gets the actualTravelTime value for this RoutingStop.
     * 
     * @return actualTravelTime
     */
    public int getActualTravelTime() {
        return actualTravelTime;
    }


    /**
     * Sets the actualTravelTime value for this RoutingStop.
     * 
     * @param actualTravelTime
     */
    public void setActualTravelTime(int actualTravelTime) {
        this.actualTravelTime = actualTravelTime;
    }


    /**
     * Gets the actualDistance value for this RoutingStop.
     * 
     * @return actualDistance
     */
    public double getActualDistance() {
        return actualDistance;
    }


    /**
     * Sets the actualDistance value for this RoutingStop.
     * 
     * @param actualDistance
     */
    public void setActualDistance(double actualDistance) {
        this.actualDistance = actualDistance;
    }


    /**
     * Gets the actualQuantity value for this RoutingStop.
     * 
     * @return actualQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getActualQuantity() {
        return actualQuantity;
    }


    /**
     * Sets the actualQuantity value for this RoutingStop.
     * 
     * @param actualQuantity
     */
    public void setActualQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingStop)) return false;
        RoutingStop other = (RoutingStop) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.routeIdentity==null && other.getRouteIdentity()==null) || 
             (this.routeIdentity!=null &&
              this.routeIdentity.equals(other.getRouteIdentity()))) &&
            ((this.stopIdentity==null && other.getStopIdentity()==null) || 
             (this.stopIdentity!=null &&
              this.stopIdentity.equals(other.getStopIdentity()))) &&
            ((this.locationIdentity==null && other.getLocationIdentity()==null) || 
             (this.locationIdentity!=null &&
              this.locationIdentity.equals(other.getLocationIdentity()))) &&
            this.sequenceNumber == other.getSequenceNumber() &&
            this.stopIx == other.getStopIx() &&
            ((this.arrival==null && other.getArrival()==null) || 
             (this.arrival!=null &&
              this.arrival.equals(other.getArrival()))) &&
            this.serviceTime == other.getServiceTime() &&
            this.travelTime == other.getTravelTime() &&
            this.distance == other.getDistance() &&
            ((this.tw1OpenTime==null && other.getTw1OpenTime()==null) || 
             (this.tw1OpenTime!=null &&
              this.tw1OpenTime.equals(other.getTw1OpenTime()))) &&
            ((this.tw1CloseTime==null && other.getTw1CloseTime()==null) || 
             (this.tw1CloseTime!=null &&
              this.tw1CloseTime.equals(other.getTw1CloseTime()))) &&
            ((this.tw2OpenTime==null && other.getTw2OpenTime()==null) || 
             (this.tw2OpenTime!=null &&
              this.tw2OpenTime.equals(other.getTw2OpenTime()))) &&
            ((this.tw2CloseTime==null && other.getTw2CloseTime()==null) || 
             (this.tw2CloseTime!=null &&
              this.tw2CloseTime.equals(other.getTw2CloseTime()))) &&
            this.deliveryCost == other.getDeliveryCost() &&
            ((this.stopType==null && other.getStopType()==null) || 
             (this.stopType!=null &&
              this.stopType.equals(other.getStopType()))) &&
            ((this.openTime==null && other.getOpenTime()==null) || 
             (this.openTime!=null &&
              this.openTime.equals(other.getOpenTime()))) &&
            ((this.closeTime==null && other.getCloseTime()==null) || 
             (this.closeTime!=null &&
              this.closeTime.equals(other.getCloseTime()))) &&
            this.standardFragmentNumber == other.getStandardFragmentNumber() &&
            ((this.locationName==null && other.getLocationName()==null) || 
             (this.locationName!=null &&
              this.locationName.equals(other.getLocationName()))) &&
            ((this.storeNumber==null && other.getStoreNumber()==null) || 
             (this.storeNumber!=null &&
              this.storeNumber.equals(other.getStoreNumber()))) &&
            ((this.contact==null && other.getContact()==null) || 
             (this.contact!=null &&
              this.contact.equals(other.getContact()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.phoneNumber==null && other.getPhoneNumber()==null) || 
             (this.phoneNumber!=null &&
              this.phoneNumber.equals(other.getPhoneNumber()))) &&
            ((this.deliveryDays==null && other.getDeliveryDays()==null) || 
             (this.deliveryDays!=null &&
              this.deliveryDays.equals(other.getDeliveryDays()))) &&
            this.latitude == other.getLatitude() &&
            this.longitude == other.getLongitude() &&
            this.buildingDeliverySequence == other.getBuildingDeliverySequence() &&
            ((this.timeZone==null && other.getTimeZone()==null) || 
             (this.timeZone!=null &&
              this.timeZone.equals(other.getTimeZone()))) &&
            ((this.sessionDate==null && other.getSessionDate()==null) || 
             (this.sessionDate!=null &&
              this.sessionDate.equals(other.getSessionDate()))) &&
            ((this.sessionDescription==null && other.getSessionDescription()==null) || 
             (this.sessionDescription!=null &&
              this.sessionDescription.equals(other.getSessionDescription()))) &&
            ((this.routeID==null && other.getRouteID()==null) || 
             (this.routeID!=null &&
              this.routeID.equals(other.getRouteID()))) &&
            ((this.routeDescription==null && other.getRouteDescription()==null) || 
             (this.routeDescription!=null &&
              this.routeDescription.equals(other.getRouteDescription()))) &&
            ((this.routeStatus==null && other.getRouteStatus()==null) || 
             (this.routeStatus!=null &&
              this.routeStatus.equals(other.getRouteStatus()))) &&
            ((this.driver1Identity==null && other.getDriver1Identity()==null) || 
             (this.driver1Identity!=null &&
              this.driver1Identity.equals(other.getDriver1Identity()))) &&
            ((this.driver1Name==null && other.getDriver1Name()==null) || 
             (this.driver1Name!=null &&
              this.driver1Name.equals(other.getDriver1Name()))) &&
            ((this.driver2Identity==null && other.getDriver2Identity()==null) || 
             (this.driver2Identity!=null &&
              this.driver2Identity.equals(other.getDriver2Identity()))) &&
            ((this.driver2Name==null && other.getDriver2Name()==null) || 
             (this.driver2Name!=null &&
              this.driver2Name.equals(other.getDriver2Name()))) &&
            ((this.deliveryQuantity==null && other.getDeliveryQuantity()==null) || 
             (this.deliveryQuantity!=null &&
              this.deliveryQuantity.equals(other.getDeliveryQuantity()))) &&
            ((this.pickupQuantity==null && other.getPickupQuantity()==null) || 
             (this.pickupQuantity!=null &&
              this.pickupQuantity.equals(other.getPickupQuantity()))) &&
            ((this.orders==null && other.getOrders()==null) || 
             (this.orders!=null &&
              java.util.Arrays.equals(this.orders, other.getOrders()))) &&
            this.actualsExist == other.isActualsExist() &&
            this.actualSequenceNumber == other.getActualSequenceNumber() &&
            ((this.actualArrival==null && other.getActualArrival()==null) || 
             (this.actualArrival!=null &&
              this.actualArrival.equals(other.getActualArrival()))) &&
            this.actualServiceTime == other.getActualServiceTime() &&
            this.actualTravelTime == other.getActualTravelTime() &&
            this.actualDistance == other.getActualDistance() &&
            ((this.actualQuantity==null && other.getActualQuantity()==null) || 
             (this.actualQuantity!=null &&
              this.actualQuantity.equals(other.getActualQuantity())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRouteIdentity() != null) {
            _hashCode += getRouteIdentity().hashCode();
        }
        if (getStopIdentity() != null) {
            _hashCode += getStopIdentity().hashCode();
        }
        if (getLocationIdentity() != null) {
            _hashCode += getLocationIdentity().hashCode();
        }
        _hashCode += getSequenceNumber();
        _hashCode += getStopIx();
        if (getArrival() != null) {
            _hashCode += getArrival().hashCode();
        }
        _hashCode += getServiceTime();
        _hashCode += getTravelTime();
        _hashCode += new Double(getDistance()).hashCode();
        if (getTw1OpenTime() != null) {
            _hashCode += getTw1OpenTime().hashCode();
        }
        if (getTw1CloseTime() != null) {
            _hashCode += getTw1CloseTime().hashCode();
        }
        if (getTw2OpenTime() != null) {
            _hashCode += getTw2OpenTime().hashCode();
        }
        if (getTw2CloseTime() != null) {
            _hashCode += getTw2CloseTime().hashCode();
        }
        _hashCode += getDeliveryCost();
        if (getStopType() != null) {
            _hashCode += getStopType().hashCode();
        }
        if (getOpenTime() != null) {
            _hashCode += getOpenTime().hashCode();
        }
        if (getCloseTime() != null) {
            _hashCode += getCloseTime().hashCode();
        }
        _hashCode += getStandardFragmentNumber();
        if (getLocationName() != null) {
            _hashCode += getLocationName().hashCode();
        }
        if (getStoreNumber() != null) {
            _hashCode += getStoreNumber().hashCode();
        }
        if (getContact() != null) {
            _hashCode += getContact().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getPhoneNumber() != null) {
            _hashCode += getPhoneNumber().hashCode();
        }
        if (getDeliveryDays() != null) {
            _hashCode += getDeliveryDays().hashCode();
        }
        _hashCode += getLatitude();
        _hashCode += getLongitude();
        _hashCode += getBuildingDeliverySequence();
        if (getTimeZone() != null) {
            _hashCode += getTimeZone().hashCode();
        }
        if (getSessionDate() != null) {
            _hashCode += getSessionDate().hashCode();
        }
        if (getSessionDescription() != null) {
            _hashCode += getSessionDescription().hashCode();
        }
        if (getRouteID() != null) {
            _hashCode += getRouteID().hashCode();
        }
        if (getRouteDescription() != null) {
            _hashCode += getRouteDescription().hashCode();
        }
        if (getRouteStatus() != null) {
            _hashCode += getRouteStatus().hashCode();
        }
        if (getDriver1Identity() != null) {
            _hashCode += getDriver1Identity().hashCode();
        }
        if (getDriver1Name() != null) {
            _hashCode += getDriver1Name().hashCode();
        }
        if (getDriver2Identity() != null) {
            _hashCode += getDriver2Identity().hashCode();
        }
        if (getDriver2Name() != null) {
            _hashCode += getDriver2Name().hashCode();
        }
        if (getDeliveryQuantity() != null) {
            _hashCode += getDeliveryQuantity().hashCode();
        }
        if (getPickupQuantity() != null) {
            _hashCode += getPickupQuantity().hashCode();
        }
        if (getOrders() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOrders());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOrders(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isActualsExist() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getActualSequenceNumber();
        if (getActualArrival() != null) {
            _hashCode += getActualArrival().hashCode();
        }
        _hashCode += getActualServiceTime();
        _hashCode += getActualTravelTime();
        _hashCode += new Double(getActualDistance()).hashCode();
        if (getActualQuantity() != null) {
            _hashCode += getActualQuantity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingStop.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingStop"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingStopIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locationIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sequenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopIx");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopIx"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arrival");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "arrival"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "serviceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("travelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "travelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "distance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tw1OpenTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "tw1OpenTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tw1CloseTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "tw1CloseTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tw2OpenTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "tw2OpenTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tw2CloseTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "tw2CloseTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("openTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "openTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("closeTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "closeTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("standardFragmentNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "standardFragmentNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locationName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storeNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "storeNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "contact"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Address"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phoneNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "phoneNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryDays");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryDays"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "latitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "longitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buildingDeliverySequence");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "buildingDeliverySequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneValue"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sessionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sessionDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driver1Identity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driver1Identity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driver1Name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driver1Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driver2Identity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driver2Identity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driver2Name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driver2Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pickupQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "pickupQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orders");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orders"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrder"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualsExist");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualsExist"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualSequenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualSequenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualArrival");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualArrival"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
