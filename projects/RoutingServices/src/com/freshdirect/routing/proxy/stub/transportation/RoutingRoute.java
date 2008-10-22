/**
 * RoutingRoute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingRoute  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity;

    private int routeNumber;

    private java.lang.String routeID;

    private java.lang.String description;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity origin;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity destination;

    private boolean helper;

    private java.util.Calendar startTime;

    private java.util.Calendar completeTime;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities capacity;

    private int color;

    private double distance;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities startingQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities endingQuantity;

    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] routeEquipment;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus status;

    private com.freshdirect.routing.proxy.stub.transportation.DayOfWeek day;

    private boolean teamSplit;

    private java.lang.String modelID;

    private int loadPriority;

    private int maximumTime;

    private int preferredTime;

    private int regularTime;

    private int travelTime;

    private int serviceTime;

    private int totalTime;

    private int paidTime;

    private int nonPaidTime;

    private int breakTime;

    private int paidBreakTime;

    private int waitTime;

    private int paidWaitTime;

    private int layoverTime;

    private int paidLayoverTime;

    private int driverRegularTimeCost;

    private int driverOverTimeCost;

    private int driverStopCost;

    private int driverDistanceCost;

    private int driverPieceCost;

    private int driverFixedCost;

    private int equipmentFixedCost;

    private int equipmentDistanceCost;

    private int reloadTime;

    private java.lang.String creationMethod;

    private int week;

    private int fees;

    private boolean lastStopIsDestination;

    private int depotServiceTime;

    private boolean originIsDestination;

    private boolean calculateReloadTime;

    private int shuttleTime;

    private double shuttleDistance;

    private int preRouteTime;

    private int postRouteTime;

    private double stemDistanceOut;

    private double stemDistanceIn;

    private int stemTimeOut;

    private int stemTimeIn;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity previousReloadRouteIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity nextReloadRouteIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType startingLoadType;

    private java.lang.String preferedSkillSet;

    private com.freshdirect.routing.proxy.stub.transportation.UserIdentity dispatcherUserIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] stops;

    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity actualDriver1Key;

    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity actualDriver2Key;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities actualSizes;

    private java.util.Calendar actualStartTime;

    private java.util.Calendar actualCompleteTime;

    private int actualStops;

    private int actualDistance;

    private int odometerStart;

    private int odometerFinish;

    private int actualHelper;

    private java.lang.String comments;

    private int actualDriverRegularTimeCost;

    private int actualDriverOvertimeCost;

    private int actualDriverStopCost;

    private int actualDriverDistanceCost;

    private int actualDriverPieceCost;

    private int actualDriverFixedCost;

    private int actualEquipmentFixedCost;

    private int actualEquipmentDistanceCost;

    private int actualFees;

    private int actualServiceTime;

    private int actualTravelTime;

    private int actualBreakTime;

    private int actualPaidBreakTime;

    private int actualWaitTime;

    private int actualPaidWaitTime;

    private int actualLayoverTime;

    private int actualPaidLayoverTime;

    private int actualPreRouteTime;

    private int actualPostRouteTime;

    public RoutingRoute() {
    }

    public RoutingRoute(
           com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity,
           int routeNumber,
           java.lang.String routeID,
           java.lang.String description,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity origin,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity destination,
           boolean helper,
           java.util.Calendar startTime,
           java.util.Calendar completeTime,
           com.freshdirect.routing.proxy.stub.transportation.Quantities capacity,
           int color,
           double distance,
           com.freshdirect.routing.proxy.stub.transportation.Quantities startingQuantity,
           com.freshdirect.routing.proxy.stub.transportation.Quantities endingQuantity,
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers,
           com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] routeEquipment,
           com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus status,
           com.freshdirect.routing.proxy.stub.transportation.DayOfWeek day,
           boolean teamSplit,
           java.lang.String modelID,
           int loadPriority,
           int maximumTime,
           int preferredTime,
           int regularTime,
           int travelTime,
           int serviceTime,
           int totalTime,
           int paidTime,
           int nonPaidTime,
           int breakTime,
           int paidBreakTime,
           int waitTime,
           int paidWaitTime,
           int layoverTime,
           int paidLayoverTime,
           int driverRegularTimeCost,
           int driverOverTimeCost,
           int driverStopCost,
           int driverDistanceCost,
           int driverPieceCost,
           int driverFixedCost,
           int equipmentFixedCost,
           int equipmentDistanceCost,
           int reloadTime,
           java.lang.String creationMethod,
           int week,
           int fees,
           boolean lastStopIsDestination,
           int depotServiceTime,
           boolean originIsDestination,
           boolean calculateReloadTime,
           int shuttleTime,
           double shuttleDistance,
           int preRouteTime,
           int postRouteTime,
           double stemDistanceOut,
           double stemDistanceIn,
           int stemTimeOut,
           int stemTimeIn,
           com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity previousReloadRouteIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity nextReloadRouteIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType startingLoadType,
           java.lang.String preferedSkillSet,
           com.freshdirect.routing.proxy.stub.transportation.UserIdentity dispatcherUserIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] stops,
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity actualDriver1Key,
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity actualDriver2Key,
           com.freshdirect.routing.proxy.stub.transportation.Quantities actualSizes,
           java.util.Calendar actualStartTime,
           java.util.Calendar actualCompleteTime,
           int actualStops,
           int actualDistance,
           int odometerStart,
           int odometerFinish,
           int actualHelper,
           java.lang.String comments,
           int actualDriverRegularTimeCost,
           int actualDriverOvertimeCost,
           int actualDriverStopCost,
           int actualDriverDistanceCost,
           int actualDriverPieceCost,
           int actualDriverFixedCost,
           int actualEquipmentFixedCost,
           int actualEquipmentDistanceCost,
           int actualFees,
           int actualServiceTime,
           int actualTravelTime,
           int actualBreakTime,
           int actualPaidBreakTime,
           int actualWaitTime,
           int actualPaidWaitTime,
           int actualLayoverTime,
           int actualPaidLayoverTime,
           int actualPreRouteTime,
           int actualPostRouteTime) {
           this.routeIdentity = routeIdentity;
           this.routeNumber = routeNumber;
           this.routeID = routeID;
           this.description = description;
           this.origin = origin;
           this.destination = destination;
           this.helper = helper;
           this.startTime = startTime;
           this.completeTime = completeTime;
           this.capacity = capacity;
           this.color = color;
           this.distance = distance;
           this.startingQuantity = startingQuantity;
           this.endingQuantity = endingQuantity;
           this.drivers = drivers;
           this.routeEquipment = routeEquipment;
           this.status = status;
           this.day = day;
           this.teamSplit = teamSplit;
           this.modelID = modelID;
           this.loadPriority = loadPriority;
           this.maximumTime = maximumTime;
           this.preferredTime = preferredTime;
           this.regularTime = regularTime;
           this.travelTime = travelTime;
           this.serviceTime = serviceTime;
           this.totalTime = totalTime;
           this.paidTime = paidTime;
           this.nonPaidTime = nonPaidTime;
           this.breakTime = breakTime;
           this.paidBreakTime = paidBreakTime;
           this.waitTime = waitTime;
           this.paidWaitTime = paidWaitTime;
           this.layoverTime = layoverTime;
           this.paidLayoverTime = paidLayoverTime;
           this.driverRegularTimeCost = driverRegularTimeCost;
           this.driverOverTimeCost = driverOverTimeCost;
           this.driverStopCost = driverStopCost;
           this.driverDistanceCost = driverDistanceCost;
           this.driverPieceCost = driverPieceCost;
           this.driverFixedCost = driverFixedCost;
           this.equipmentFixedCost = equipmentFixedCost;
           this.equipmentDistanceCost = equipmentDistanceCost;
           this.reloadTime = reloadTime;
           this.creationMethod = creationMethod;
           this.week = week;
           this.fees = fees;
           this.lastStopIsDestination = lastStopIsDestination;
           this.depotServiceTime = depotServiceTime;
           this.originIsDestination = originIsDestination;
           this.calculateReloadTime = calculateReloadTime;
           this.shuttleTime = shuttleTime;
           this.shuttleDistance = shuttleDistance;
           this.preRouteTime = preRouteTime;
           this.postRouteTime = postRouteTime;
           this.stemDistanceOut = stemDistanceOut;
           this.stemDistanceIn = stemDistanceIn;
           this.stemTimeOut = stemTimeOut;
           this.stemTimeIn = stemTimeIn;
           this.previousReloadRouteIdentity = previousReloadRouteIdentity;
           this.nextReloadRouteIdentity = nextReloadRouteIdentity;
           this.startingLoadType = startingLoadType;
           this.preferedSkillSet = preferedSkillSet;
           this.dispatcherUserIdentity = dispatcherUserIdentity;
           this.stops = stops;
           this.actualDriver1Key = actualDriver1Key;
           this.actualDriver2Key = actualDriver2Key;
           this.actualSizes = actualSizes;
           this.actualStartTime = actualStartTime;
           this.actualCompleteTime = actualCompleteTime;
           this.actualStops = actualStops;
           this.actualDistance = actualDistance;
           this.odometerStart = odometerStart;
           this.odometerFinish = odometerFinish;
           this.actualHelper = actualHelper;
           this.comments = comments;
           this.actualDriverRegularTimeCost = actualDriverRegularTimeCost;
           this.actualDriverOvertimeCost = actualDriverOvertimeCost;
           this.actualDriverStopCost = actualDriverStopCost;
           this.actualDriverDistanceCost = actualDriverDistanceCost;
           this.actualDriverPieceCost = actualDriverPieceCost;
           this.actualDriverFixedCost = actualDriverFixedCost;
           this.actualEquipmentFixedCost = actualEquipmentFixedCost;
           this.actualEquipmentDistanceCost = actualEquipmentDistanceCost;
           this.actualFees = actualFees;
           this.actualServiceTime = actualServiceTime;
           this.actualTravelTime = actualTravelTime;
           this.actualBreakTime = actualBreakTime;
           this.actualPaidBreakTime = actualPaidBreakTime;
           this.actualWaitTime = actualWaitTime;
           this.actualPaidWaitTime = actualPaidWaitTime;
           this.actualLayoverTime = actualLayoverTime;
           this.actualPaidLayoverTime = actualPaidLayoverTime;
           this.actualPreRouteTime = actualPreRouteTime;
           this.actualPostRouteTime = actualPostRouteTime;
    }


    /**
     * Gets the routeIdentity value for this RoutingRoute.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this RoutingRoute.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the routeNumber value for this RoutingRoute.
     * 
     * @return routeNumber
     */
    public int getRouteNumber() {
        return routeNumber;
    }


    /**
     * Sets the routeNumber value for this RoutingRoute.
     * 
     * @param routeNumber
     */
    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }


    /**
     * Gets the routeID value for this RoutingRoute.
     * 
     * @return routeID
     */
    public java.lang.String getRouteID() {
        return routeID;
    }


    /**
     * Sets the routeID value for this RoutingRoute.
     * 
     * @param routeID
     */
    public void setRouteID(java.lang.String routeID) {
        this.routeID = routeID;
    }


    /**
     * Gets the description value for this RoutingRoute.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this RoutingRoute.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the origin value for this RoutingRoute.
     * 
     * @return origin
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getOrigin() {
        return origin;
    }


    /**
     * Sets the origin value for this RoutingRoute.
     * 
     * @param origin
     */
    public void setOrigin(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity origin) {
        this.origin = origin;
    }


    /**
     * Gets the destination value for this RoutingRoute.
     * 
     * @return destination
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getDestination() {
        return destination;
    }


    /**
     * Sets the destination value for this RoutingRoute.
     * 
     * @param destination
     */
    public void setDestination(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity destination) {
        this.destination = destination;
    }


    /**
     * Gets the helper value for this RoutingRoute.
     * 
     * @return helper
     */
    public boolean isHelper() {
        return helper;
    }


    /**
     * Sets the helper value for this RoutingRoute.
     * 
     * @param helper
     */
    public void setHelper(boolean helper) {
        this.helper = helper;
    }


    /**
     * Gets the startTime value for this RoutingRoute.
     * 
     * @return startTime
     */
    public java.util.Calendar getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this RoutingRoute.
     * 
     * @param startTime
     */
    public void setStartTime(java.util.Calendar startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the completeTime value for this RoutingRoute.
     * 
     * @return completeTime
     */
    public java.util.Calendar getCompleteTime() {
        return completeTime;
    }


    /**
     * Sets the completeTime value for this RoutingRoute.
     * 
     * @param completeTime
     */
    public void setCompleteTime(java.util.Calendar completeTime) {
        this.completeTime = completeTime;
    }


    /**
     * Gets the capacity value for this RoutingRoute.
     * 
     * @return capacity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getCapacity() {
        return capacity;
    }


    /**
     * Sets the capacity value for this RoutingRoute.
     * 
     * @param capacity
     */
    public void setCapacity(com.freshdirect.routing.proxy.stub.transportation.Quantities capacity) {
        this.capacity = capacity;
    }


    /**
     * Gets the color value for this RoutingRoute.
     * 
     * @return color
     */
    public int getColor() {
        return color;
    }


    /**
     * Sets the color value for this RoutingRoute.
     * 
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
    }


    /**
     * Gets the distance value for this RoutingRoute.
     * 
     * @return distance
     */
    public double getDistance() {
        return distance;
    }


    /**
     * Sets the distance value for this RoutingRoute.
     * 
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }


    /**
     * Gets the startingQuantity value for this RoutingRoute.
     * 
     * @return startingQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getStartingQuantity() {
        return startingQuantity;
    }


    /**
     * Sets the startingQuantity value for this RoutingRoute.
     * 
     * @param startingQuantity
     */
    public void setStartingQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities startingQuantity) {
        this.startingQuantity = startingQuantity;
    }


    /**
     * Gets the endingQuantity value for this RoutingRoute.
     * 
     * @return endingQuantity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getEndingQuantity() {
        return endingQuantity;
    }


    /**
     * Sets the endingQuantity value for this RoutingRoute.
     * 
     * @param endingQuantity
     */
    public void setEndingQuantity(com.freshdirect.routing.proxy.stub.transportation.Quantities endingQuantity) {
        this.endingQuantity = endingQuantity;
    }


    /**
     * Gets the drivers value for this RoutingRoute.
     * 
     * @return drivers
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] getDrivers() {
        return drivers;
    }


    /**
     * Sets the drivers value for this RoutingRoute.
     * 
     * @param drivers
     */
    public void setDrivers(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers) {
        this.drivers = drivers;
    }

    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getDrivers(int i) {
        return this.drivers[i];
    }

    public void setDrivers(int i, com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity _value) {
        this.drivers[i] = _value;
    }


    /**
     * Gets the routeEquipment value for this RoutingRoute.
     * 
     * @return routeEquipment
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] getRouteEquipment() {
        return routeEquipment;
    }


    /**
     * Sets the routeEquipment value for this RoutingRoute.
     * 
     * @param routeEquipment
     */
    public void setRouteEquipment(com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity[] routeEquipment) {
        this.routeEquipment = routeEquipment;
    }

    public com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity getRouteEquipment(int i) {
        return this.routeEquipment[i];
    }

    public void setRouteEquipment(int i, com.freshdirect.routing.proxy.stub.transportation.RoutingEquipmentIdentity _value) {
        this.routeEquipment[i] = _value;
    }


    /**
     * Gets the status value for this RoutingRoute.
     * 
     * @return status
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this RoutingRoute.
     * 
     * @param status
     */
    public void setStatus(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteStatus status) {
        this.status = status;
    }


    /**
     * Gets the day value for this RoutingRoute.
     * 
     * @return day
     */
    public com.freshdirect.routing.proxy.stub.transportation.DayOfWeek getDay() {
        return day;
    }


    /**
     * Sets the day value for this RoutingRoute.
     * 
     * @param day
     */
    public void setDay(com.freshdirect.routing.proxy.stub.transportation.DayOfWeek day) {
        this.day = day;
    }


    /**
     * Gets the teamSplit value for this RoutingRoute.
     * 
     * @return teamSplit
     */
    public boolean isTeamSplit() {
        return teamSplit;
    }


    /**
     * Sets the teamSplit value for this RoutingRoute.
     * 
     * @param teamSplit
     */
    public void setTeamSplit(boolean teamSplit) {
        this.teamSplit = teamSplit;
    }


    /**
     * Gets the modelID value for this RoutingRoute.
     * 
     * @return modelID
     */
    public java.lang.String getModelID() {
        return modelID;
    }


    /**
     * Sets the modelID value for this RoutingRoute.
     * 
     * @param modelID
     */
    public void setModelID(java.lang.String modelID) {
        this.modelID = modelID;
    }


    /**
     * Gets the loadPriority value for this RoutingRoute.
     * 
     * @return loadPriority
     */
    public int getLoadPriority() {
        return loadPriority;
    }


    /**
     * Sets the loadPriority value for this RoutingRoute.
     * 
     * @param loadPriority
     */
    public void setLoadPriority(int loadPriority) {
        this.loadPriority = loadPriority;
    }


    /**
     * Gets the maximumTime value for this RoutingRoute.
     * 
     * @return maximumTime
     */
    public int getMaximumTime() {
        return maximumTime;
    }


    /**
     * Sets the maximumTime value for this RoutingRoute.
     * 
     * @param maximumTime
     */
    public void setMaximumTime(int maximumTime) {
        this.maximumTime = maximumTime;
    }


    /**
     * Gets the preferredTime value for this RoutingRoute.
     * 
     * @return preferredTime
     */
    public int getPreferredTime() {
        return preferredTime;
    }


    /**
     * Sets the preferredTime value for this RoutingRoute.
     * 
     * @param preferredTime
     */
    public void setPreferredTime(int preferredTime) {
        this.preferredTime = preferredTime;
    }


    /**
     * Gets the regularTime value for this RoutingRoute.
     * 
     * @return regularTime
     */
    public int getRegularTime() {
        return regularTime;
    }


    /**
     * Sets the regularTime value for this RoutingRoute.
     * 
     * @param regularTime
     */
    public void setRegularTime(int regularTime) {
        this.regularTime = regularTime;
    }


    /**
     * Gets the travelTime value for this RoutingRoute.
     * 
     * @return travelTime
     */
    public int getTravelTime() {
        return travelTime;
    }


    /**
     * Sets the travelTime value for this RoutingRoute.
     * 
     * @param travelTime
     */
    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }


    /**
     * Gets the serviceTime value for this RoutingRoute.
     * 
     * @return serviceTime
     */
    public int getServiceTime() {
        return serviceTime;
    }


    /**
     * Sets the serviceTime value for this RoutingRoute.
     * 
     * @param serviceTime
     */
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }


    /**
     * Gets the totalTime value for this RoutingRoute.
     * 
     * @return totalTime
     */
    public int getTotalTime() {
        return totalTime;
    }


    /**
     * Sets the totalTime value for this RoutingRoute.
     * 
     * @param totalTime
     */
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }


    /**
     * Gets the paidTime value for this RoutingRoute.
     * 
     * @return paidTime
     */
    public int getPaidTime() {
        return paidTime;
    }


    /**
     * Sets the paidTime value for this RoutingRoute.
     * 
     * @param paidTime
     */
    public void setPaidTime(int paidTime) {
        this.paidTime = paidTime;
    }


    /**
     * Gets the nonPaidTime value for this RoutingRoute.
     * 
     * @return nonPaidTime
     */
    public int getNonPaidTime() {
        return nonPaidTime;
    }


    /**
     * Sets the nonPaidTime value for this RoutingRoute.
     * 
     * @param nonPaidTime
     */
    public void setNonPaidTime(int nonPaidTime) {
        this.nonPaidTime = nonPaidTime;
    }


    /**
     * Gets the breakTime value for this RoutingRoute.
     * 
     * @return breakTime
     */
    public int getBreakTime() {
        return breakTime;
    }


    /**
     * Sets the breakTime value for this RoutingRoute.
     * 
     * @param breakTime
     */
    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }


    /**
     * Gets the paidBreakTime value for this RoutingRoute.
     * 
     * @return paidBreakTime
     */
    public int getPaidBreakTime() {
        return paidBreakTime;
    }


    /**
     * Sets the paidBreakTime value for this RoutingRoute.
     * 
     * @param paidBreakTime
     */
    public void setPaidBreakTime(int paidBreakTime) {
        this.paidBreakTime = paidBreakTime;
    }


    /**
     * Gets the waitTime value for this RoutingRoute.
     * 
     * @return waitTime
     */
    public int getWaitTime() {
        return waitTime;
    }


    /**
     * Sets the waitTime value for this RoutingRoute.
     * 
     * @param waitTime
     */
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }


    /**
     * Gets the paidWaitTime value for this RoutingRoute.
     * 
     * @return paidWaitTime
     */
    public int getPaidWaitTime() {
        return paidWaitTime;
    }


    /**
     * Sets the paidWaitTime value for this RoutingRoute.
     * 
     * @param paidWaitTime
     */
    public void setPaidWaitTime(int paidWaitTime) {
        this.paidWaitTime = paidWaitTime;
    }


    /**
     * Gets the layoverTime value for this RoutingRoute.
     * 
     * @return layoverTime
     */
    public int getLayoverTime() {
        return layoverTime;
    }


    /**
     * Sets the layoverTime value for this RoutingRoute.
     * 
     * @param layoverTime
     */
    public void setLayoverTime(int layoverTime) {
        this.layoverTime = layoverTime;
    }


    /**
     * Gets the paidLayoverTime value for this RoutingRoute.
     * 
     * @return paidLayoverTime
     */
    public int getPaidLayoverTime() {
        return paidLayoverTime;
    }


    /**
     * Sets the paidLayoverTime value for this RoutingRoute.
     * 
     * @param paidLayoverTime
     */
    public void setPaidLayoverTime(int paidLayoverTime) {
        this.paidLayoverTime = paidLayoverTime;
    }


    /**
     * Gets the driverRegularTimeCost value for this RoutingRoute.
     * 
     * @return driverRegularTimeCost
     */
    public int getDriverRegularTimeCost() {
        return driverRegularTimeCost;
    }


    /**
     * Sets the driverRegularTimeCost value for this RoutingRoute.
     * 
     * @param driverRegularTimeCost
     */
    public void setDriverRegularTimeCost(int driverRegularTimeCost) {
        this.driverRegularTimeCost = driverRegularTimeCost;
    }


    /**
     * Gets the driverOverTimeCost value for this RoutingRoute.
     * 
     * @return driverOverTimeCost
     */
    public int getDriverOverTimeCost() {
        return driverOverTimeCost;
    }


    /**
     * Sets the driverOverTimeCost value for this RoutingRoute.
     * 
     * @param driverOverTimeCost
     */
    public void setDriverOverTimeCost(int driverOverTimeCost) {
        this.driverOverTimeCost = driverOverTimeCost;
    }


    /**
     * Gets the driverStopCost value for this RoutingRoute.
     * 
     * @return driverStopCost
     */
    public int getDriverStopCost() {
        return driverStopCost;
    }


    /**
     * Sets the driverStopCost value for this RoutingRoute.
     * 
     * @param driverStopCost
     */
    public void setDriverStopCost(int driverStopCost) {
        this.driverStopCost = driverStopCost;
    }


    /**
     * Gets the driverDistanceCost value for this RoutingRoute.
     * 
     * @return driverDistanceCost
     */
    public int getDriverDistanceCost() {
        return driverDistanceCost;
    }


    /**
     * Sets the driverDistanceCost value for this RoutingRoute.
     * 
     * @param driverDistanceCost
     */
    public void setDriverDistanceCost(int driverDistanceCost) {
        this.driverDistanceCost = driverDistanceCost;
    }


    /**
     * Gets the driverPieceCost value for this RoutingRoute.
     * 
     * @return driverPieceCost
     */
    public int getDriverPieceCost() {
        return driverPieceCost;
    }


    /**
     * Sets the driverPieceCost value for this RoutingRoute.
     * 
     * @param driverPieceCost
     */
    public void setDriverPieceCost(int driverPieceCost) {
        this.driverPieceCost = driverPieceCost;
    }


    /**
     * Gets the driverFixedCost value for this RoutingRoute.
     * 
     * @return driverFixedCost
     */
    public int getDriverFixedCost() {
        return driverFixedCost;
    }


    /**
     * Sets the driverFixedCost value for this RoutingRoute.
     * 
     * @param driverFixedCost
     */
    public void setDriverFixedCost(int driverFixedCost) {
        this.driverFixedCost = driverFixedCost;
    }


    /**
     * Gets the equipmentFixedCost value for this RoutingRoute.
     * 
     * @return equipmentFixedCost
     */
    public int getEquipmentFixedCost() {
        return equipmentFixedCost;
    }


    /**
     * Sets the equipmentFixedCost value for this RoutingRoute.
     * 
     * @param equipmentFixedCost
     */
    public void setEquipmentFixedCost(int equipmentFixedCost) {
        this.equipmentFixedCost = equipmentFixedCost;
    }


    /**
     * Gets the equipmentDistanceCost value for this RoutingRoute.
     * 
     * @return equipmentDistanceCost
     */
    public int getEquipmentDistanceCost() {
        return equipmentDistanceCost;
    }


    /**
     * Sets the equipmentDistanceCost value for this RoutingRoute.
     * 
     * @param equipmentDistanceCost
     */
    public void setEquipmentDistanceCost(int equipmentDistanceCost) {
        this.equipmentDistanceCost = equipmentDistanceCost;
    }


    /**
     * Gets the reloadTime value for this RoutingRoute.
     * 
     * @return reloadTime
     */
    public int getReloadTime() {
        return reloadTime;
    }


    /**
     * Sets the reloadTime value for this RoutingRoute.
     * 
     * @param reloadTime
     */
    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }


    /**
     * Gets the creationMethod value for this RoutingRoute.
     * 
     * @return creationMethod
     */
    public java.lang.String getCreationMethod() {
        return creationMethod;
    }


    /**
     * Sets the creationMethod value for this RoutingRoute.
     * 
     * @param creationMethod
     */
    public void setCreationMethod(java.lang.String creationMethod) {
        this.creationMethod = creationMethod;
    }


    /**
     * Gets the week value for this RoutingRoute.
     * 
     * @return week
     */
    public int getWeek() {
        return week;
    }


    /**
     * Sets the week value for this RoutingRoute.
     * 
     * @param week
     */
    public void setWeek(int week) {
        this.week = week;
    }


    /**
     * Gets the fees value for this RoutingRoute.
     * 
     * @return fees
     */
    public int getFees() {
        return fees;
    }


    /**
     * Sets the fees value for this RoutingRoute.
     * 
     * @param fees
     */
    public void setFees(int fees) {
        this.fees = fees;
    }


    /**
     * Gets the lastStopIsDestination value for this RoutingRoute.
     * 
     * @return lastStopIsDestination
     */
    public boolean isLastStopIsDestination() {
        return lastStopIsDestination;
    }


    /**
     * Sets the lastStopIsDestination value for this RoutingRoute.
     * 
     * @param lastStopIsDestination
     */
    public void setLastStopIsDestination(boolean lastStopIsDestination) {
        this.lastStopIsDestination = lastStopIsDestination;
    }


    /**
     * Gets the depotServiceTime value for this RoutingRoute.
     * 
     * @return depotServiceTime
     */
    public int getDepotServiceTime() {
        return depotServiceTime;
    }


    /**
     * Sets the depotServiceTime value for this RoutingRoute.
     * 
     * @param depotServiceTime
     */
    public void setDepotServiceTime(int depotServiceTime) {
        this.depotServiceTime = depotServiceTime;
    }


    /**
     * Gets the originIsDestination value for this RoutingRoute.
     * 
     * @return originIsDestination
     */
    public boolean isOriginIsDestination() {
        return originIsDestination;
    }


    /**
     * Sets the originIsDestination value for this RoutingRoute.
     * 
     * @param originIsDestination
     */
    public void setOriginIsDestination(boolean originIsDestination) {
        this.originIsDestination = originIsDestination;
    }


    /**
     * Gets the calculateReloadTime value for this RoutingRoute.
     * 
     * @return calculateReloadTime
     */
    public boolean isCalculateReloadTime() {
        return calculateReloadTime;
    }


    /**
     * Sets the calculateReloadTime value for this RoutingRoute.
     * 
     * @param calculateReloadTime
     */
    public void setCalculateReloadTime(boolean calculateReloadTime) {
        this.calculateReloadTime = calculateReloadTime;
    }


    /**
     * Gets the shuttleTime value for this RoutingRoute.
     * 
     * @return shuttleTime
     */
    public int getShuttleTime() {
        return shuttleTime;
    }


    /**
     * Sets the shuttleTime value for this RoutingRoute.
     * 
     * @param shuttleTime
     */
    public void setShuttleTime(int shuttleTime) {
        this.shuttleTime = shuttleTime;
    }


    /**
     * Gets the shuttleDistance value for this RoutingRoute.
     * 
     * @return shuttleDistance
     */
    public double getShuttleDistance() {
        return shuttleDistance;
    }


    /**
     * Sets the shuttleDistance value for this RoutingRoute.
     * 
     * @param shuttleDistance
     */
    public void setShuttleDistance(double shuttleDistance) {
        this.shuttleDistance = shuttleDistance;
    }


    /**
     * Gets the preRouteTime value for this RoutingRoute.
     * 
     * @return preRouteTime
     */
    public int getPreRouteTime() {
        return preRouteTime;
    }


    /**
     * Sets the preRouteTime value for this RoutingRoute.
     * 
     * @param preRouteTime
     */
    public void setPreRouteTime(int preRouteTime) {
        this.preRouteTime = preRouteTime;
    }


    /**
     * Gets the postRouteTime value for this RoutingRoute.
     * 
     * @return postRouteTime
     */
    public int getPostRouteTime() {
        return postRouteTime;
    }


    /**
     * Sets the postRouteTime value for this RoutingRoute.
     * 
     * @param postRouteTime
     */
    public void setPostRouteTime(int postRouteTime) {
        this.postRouteTime = postRouteTime;
    }


    /**
     * Gets the stemDistanceOut value for this RoutingRoute.
     * 
     * @return stemDistanceOut
     */
    public double getStemDistanceOut() {
        return stemDistanceOut;
    }


    /**
     * Sets the stemDistanceOut value for this RoutingRoute.
     * 
     * @param stemDistanceOut
     */
    public void setStemDistanceOut(double stemDistanceOut) {
        this.stemDistanceOut = stemDistanceOut;
    }


    /**
     * Gets the stemDistanceIn value for this RoutingRoute.
     * 
     * @return stemDistanceIn
     */
    public double getStemDistanceIn() {
        return stemDistanceIn;
    }


    /**
     * Sets the stemDistanceIn value for this RoutingRoute.
     * 
     * @param stemDistanceIn
     */
    public void setStemDistanceIn(double stemDistanceIn) {
        this.stemDistanceIn = stemDistanceIn;
    }


    /**
     * Gets the stemTimeOut value for this RoutingRoute.
     * 
     * @return stemTimeOut
     */
    public int getStemTimeOut() {
        return stemTimeOut;
    }


    /**
     * Sets the stemTimeOut value for this RoutingRoute.
     * 
     * @param stemTimeOut
     */
    public void setStemTimeOut(int stemTimeOut) {
        this.stemTimeOut = stemTimeOut;
    }


    /**
     * Gets the stemTimeIn value for this RoutingRoute.
     * 
     * @return stemTimeIn
     */
    public int getStemTimeIn() {
        return stemTimeIn;
    }


    /**
     * Sets the stemTimeIn value for this RoutingRoute.
     * 
     * @param stemTimeIn
     */
    public void setStemTimeIn(int stemTimeIn) {
        this.stemTimeIn = stemTimeIn;
    }


    /**
     * Gets the previousReloadRouteIdentity value for this RoutingRoute.
     * 
     * @return previousReloadRouteIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity getPreviousReloadRouteIdentity() {
        return previousReloadRouteIdentity;
    }


    /**
     * Sets the previousReloadRouteIdentity value for this RoutingRoute.
     * 
     * @param previousReloadRouteIdentity
     */
    public void setPreviousReloadRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity previousReloadRouteIdentity) {
        this.previousReloadRouteIdentity = previousReloadRouteIdentity;
    }


    /**
     * Gets the nextReloadRouteIdentity value for this RoutingRoute.
     * 
     * @return nextReloadRouteIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity getNextReloadRouteIdentity() {
        return nextReloadRouteIdentity;
    }


    /**
     * Sets the nextReloadRouteIdentity value for this RoutingRoute.
     * 
     * @param nextReloadRouteIdentity
     */
    public void setNextReloadRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity nextReloadRouteIdentity) {
        this.nextReloadRouteIdentity = nextReloadRouteIdentity;
    }


    /**
     * Gets the startingLoadType value for this RoutingRoute.
     * 
     * @return startingLoadType
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType getStartingLoadType() {
        return startingLoadType;
    }


    /**
     * Sets the startingLoadType value for this RoutingRoute.
     * 
     * @param startingLoadType
     */
    public void setStartingLoadType(com.freshdirect.routing.proxy.stub.transportation.RoutingReloadType startingLoadType) {
        this.startingLoadType = startingLoadType;
    }


    /**
     * Gets the preferedSkillSet value for this RoutingRoute.
     * 
     * @return preferedSkillSet
     */
    public java.lang.String getPreferedSkillSet() {
        return preferedSkillSet;
    }


    /**
     * Sets the preferedSkillSet value for this RoutingRoute.
     * 
     * @param preferedSkillSet
     */
    public void setPreferedSkillSet(java.lang.String preferedSkillSet) {
        this.preferedSkillSet = preferedSkillSet;
    }


    /**
     * Gets the dispatcherUserIdentity value for this RoutingRoute.
     * 
     * @return dispatcherUserIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.UserIdentity getDispatcherUserIdentity() {
        return dispatcherUserIdentity;
    }


    /**
     * Sets the dispatcherUserIdentity value for this RoutingRoute.
     * 
     * @param dispatcherUserIdentity
     */
    public void setDispatcherUserIdentity(com.freshdirect.routing.proxy.stub.transportation.UserIdentity dispatcherUserIdentity) {
        this.dispatcherUserIdentity = dispatcherUserIdentity;
    }


    /**
     * Gets the stops value for this RoutingRoute.
     * 
     * @return stops
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] getStops() {
        return stops;
    }


    /**
     * Sets the stops value for this RoutingRoute.
     * 
     * @param stops
     */
    public void setStops(com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] stops) {
        this.stops = stops;
    }

    public com.freshdirect.routing.proxy.stub.transportation.RoutingStop getStops(int i) {
        return this.stops[i];
    }

    public void setStops(int i, com.freshdirect.routing.proxy.stub.transportation.RoutingStop _value) {
        this.stops[i] = _value;
    }


    /**
     * Gets the actualDriver1Key value for this RoutingRoute.
     * 
     * @return actualDriver1Key
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getActualDriver1Key() {
        return actualDriver1Key;
    }


    /**
     * Sets the actualDriver1Key value for this RoutingRoute.
     * 
     * @param actualDriver1Key
     */
    public void setActualDriver1Key(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity actualDriver1Key) {
        this.actualDriver1Key = actualDriver1Key;
    }


    /**
     * Gets the actualDriver2Key value for this RoutingRoute.
     * 
     * @return actualDriver2Key
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getActualDriver2Key() {
        return actualDriver2Key;
    }


    /**
     * Sets the actualDriver2Key value for this RoutingRoute.
     * 
     * @param actualDriver2Key
     */
    public void setActualDriver2Key(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity actualDriver2Key) {
        this.actualDriver2Key = actualDriver2Key;
    }


    /**
     * Gets the actualSizes value for this RoutingRoute.
     * 
     * @return actualSizes
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getActualSizes() {
        return actualSizes;
    }


    /**
     * Sets the actualSizes value for this RoutingRoute.
     * 
     * @param actualSizes
     */
    public void setActualSizes(com.freshdirect.routing.proxy.stub.transportation.Quantities actualSizes) {
        this.actualSizes = actualSizes;
    }


    /**
     * Gets the actualStartTime value for this RoutingRoute.
     * 
     * @return actualStartTime
     */
    public java.util.Calendar getActualStartTime() {
        return actualStartTime;
    }


    /**
     * Sets the actualStartTime value for this RoutingRoute.
     * 
     * @param actualStartTime
     */
    public void setActualStartTime(java.util.Calendar actualStartTime) {
        this.actualStartTime = actualStartTime;
    }


    /**
     * Gets the actualCompleteTime value for this RoutingRoute.
     * 
     * @return actualCompleteTime
     */
    public java.util.Calendar getActualCompleteTime() {
        return actualCompleteTime;
    }


    /**
     * Sets the actualCompleteTime value for this RoutingRoute.
     * 
     * @param actualCompleteTime
     */
    public void setActualCompleteTime(java.util.Calendar actualCompleteTime) {
        this.actualCompleteTime = actualCompleteTime;
    }


    /**
     * Gets the actualStops value for this RoutingRoute.
     * 
     * @return actualStops
     */
    public int getActualStops() {
        return actualStops;
    }


    /**
     * Sets the actualStops value for this RoutingRoute.
     * 
     * @param actualStops
     */
    public void setActualStops(int actualStops) {
        this.actualStops = actualStops;
    }


    /**
     * Gets the actualDistance value for this RoutingRoute.
     * 
     * @return actualDistance
     */
    public int getActualDistance() {
        return actualDistance;
    }


    /**
     * Sets the actualDistance value for this RoutingRoute.
     * 
     * @param actualDistance
     */
    public void setActualDistance(int actualDistance) {
        this.actualDistance = actualDistance;
    }


    /**
     * Gets the odometerStart value for this RoutingRoute.
     * 
     * @return odometerStart
     */
    public int getOdometerStart() {
        return odometerStart;
    }


    /**
     * Sets the odometerStart value for this RoutingRoute.
     * 
     * @param odometerStart
     */
    public void setOdometerStart(int odometerStart) {
        this.odometerStart = odometerStart;
    }


    /**
     * Gets the odometerFinish value for this RoutingRoute.
     * 
     * @return odometerFinish
     */
    public int getOdometerFinish() {
        return odometerFinish;
    }


    /**
     * Sets the odometerFinish value for this RoutingRoute.
     * 
     * @param odometerFinish
     */
    public void setOdometerFinish(int odometerFinish) {
        this.odometerFinish = odometerFinish;
    }


    /**
     * Gets the actualHelper value for this RoutingRoute.
     * 
     * @return actualHelper
     */
    public int getActualHelper() {
        return actualHelper;
    }


    /**
     * Sets the actualHelper value for this RoutingRoute.
     * 
     * @param actualHelper
     */
    public void setActualHelper(int actualHelper) {
        this.actualHelper = actualHelper;
    }


    /**
     * Gets the comments value for this RoutingRoute.
     * 
     * @return comments
     */
    public java.lang.String getComments() {
        return comments;
    }


    /**
     * Sets the comments value for this RoutingRoute.
     * 
     * @param comments
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }


    /**
     * Gets the actualDriverRegularTimeCost value for this RoutingRoute.
     * 
     * @return actualDriverRegularTimeCost
     */
    public int getActualDriverRegularTimeCost() {
        return actualDriverRegularTimeCost;
    }


    /**
     * Sets the actualDriverRegularTimeCost value for this RoutingRoute.
     * 
     * @param actualDriverRegularTimeCost
     */
    public void setActualDriverRegularTimeCost(int actualDriverRegularTimeCost) {
        this.actualDriverRegularTimeCost = actualDriverRegularTimeCost;
    }


    /**
     * Gets the actualDriverOvertimeCost value for this RoutingRoute.
     * 
     * @return actualDriverOvertimeCost
     */
    public int getActualDriverOvertimeCost() {
        return actualDriverOvertimeCost;
    }


    /**
     * Sets the actualDriverOvertimeCost value for this RoutingRoute.
     * 
     * @param actualDriverOvertimeCost
     */
    public void setActualDriverOvertimeCost(int actualDriverOvertimeCost) {
        this.actualDriverOvertimeCost = actualDriverOvertimeCost;
    }


    /**
     * Gets the actualDriverStopCost value for this RoutingRoute.
     * 
     * @return actualDriverStopCost
     */
    public int getActualDriverStopCost() {
        return actualDriverStopCost;
    }


    /**
     * Sets the actualDriverStopCost value for this RoutingRoute.
     * 
     * @param actualDriverStopCost
     */
    public void setActualDriverStopCost(int actualDriverStopCost) {
        this.actualDriverStopCost = actualDriverStopCost;
    }


    /**
     * Gets the actualDriverDistanceCost value for this RoutingRoute.
     * 
     * @return actualDriverDistanceCost
     */
    public int getActualDriverDistanceCost() {
        return actualDriverDistanceCost;
    }


    /**
     * Sets the actualDriverDistanceCost value for this RoutingRoute.
     * 
     * @param actualDriverDistanceCost
     */
    public void setActualDriverDistanceCost(int actualDriverDistanceCost) {
        this.actualDriverDistanceCost = actualDriverDistanceCost;
    }


    /**
     * Gets the actualDriverPieceCost value for this RoutingRoute.
     * 
     * @return actualDriverPieceCost
     */
    public int getActualDriverPieceCost() {
        return actualDriverPieceCost;
    }


    /**
     * Sets the actualDriverPieceCost value for this RoutingRoute.
     * 
     * @param actualDriverPieceCost
     */
    public void setActualDriverPieceCost(int actualDriverPieceCost) {
        this.actualDriverPieceCost = actualDriverPieceCost;
    }


    /**
     * Gets the actualDriverFixedCost value for this RoutingRoute.
     * 
     * @return actualDriverFixedCost
     */
    public int getActualDriverFixedCost() {
        return actualDriverFixedCost;
    }


    /**
     * Sets the actualDriverFixedCost value for this RoutingRoute.
     * 
     * @param actualDriverFixedCost
     */
    public void setActualDriverFixedCost(int actualDriverFixedCost) {
        this.actualDriverFixedCost = actualDriverFixedCost;
    }


    /**
     * Gets the actualEquipmentFixedCost value for this RoutingRoute.
     * 
     * @return actualEquipmentFixedCost
     */
    public int getActualEquipmentFixedCost() {
        return actualEquipmentFixedCost;
    }


    /**
     * Sets the actualEquipmentFixedCost value for this RoutingRoute.
     * 
     * @param actualEquipmentFixedCost
     */
    public void setActualEquipmentFixedCost(int actualEquipmentFixedCost) {
        this.actualEquipmentFixedCost = actualEquipmentFixedCost;
    }


    /**
     * Gets the actualEquipmentDistanceCost value for this RoutingRoute.
     * 
     * @return actualEquipmentDistanceCost
     */
    public int getActualEquipmentDistanceCost() {
        return actualEquipmentDistanceCost;
    }


    /**
     * Sets the actualEquipmentDistanceCost value for this RoutingRoute.
     * 
     * @param actualEquipmentDistanceCost
     */
    public void setActualEquipmentDistanceCost(int actualEquipmentDistanceCost) {
        this.actualEquipmentDistanceCost = actualEquipmentDistanceCost;
    }


    /**
     * Gets the actualFees value for this RoutingRoute.
     * 
     * @return actualFees
     */
    public int getActualFees() {
        return actualFees;
    }


    /**
     * Sets the actualFees value for this RoutingRoute.
     * 
     * @param actualFees
     */
    public void setActualFees(int actualFees) {
        this.actualFees = actualFees;
    }


    /**
     * Gets the actualServiceTime value for this RoutingRoute.
     * 
     * @return actualServiceTime
     */
    public int getActualServiceTime() {
        return actualServiceTime;
    }


    /**
     * Sets the actualServiceTime value for this RoutingRoute.
     * 
     * @param actualServiceTime
     */
    public void setActualServiceTime(int actualServiceTime) {
        this.actualServiceTime = actualServiceTime;
    }


    /**
     * Gets the actualTravelTime value for this RoutingRoute.
     * 
     * @return actualTravelTime
     */
    public int getActualTravelTime() {
        return actualTravelTime;
    }


    /**
     * Sets the actualTravelTime value for this RoutingRoute.
     * 
     * @param actualTravelTime
     */
    public void setActualTravelTime(int actualTravelTime) {
        this.actualTravelTime = actualTravelTime;
    }


    /**
     * Gets the actualBreakTime value for this RoutingRoute.
     * 
     * @return actualBreakTime
     */
    public int getActualBreakTime() {
        return actualBreakTime;
    }


    /**
     * Sets the actualBreakTime value for this RoutingRoute.
     * 
     * @param actualBreakTime
     */
    public void setActualBreakTime(int actualBreakTime) {
        this.actualBreakTime = actualBreakTime;
    }


    /**
     * Gets the actualPaidBreakTime value for this RoutingRoute.
     * 
     * @return actualPaidBreakTime
     */
    public int getActualPaidBreakTime() {
        return actualPaidBreakTime;
    }


    /**
     * Sets the actualPaidBreakTime value for this RoutingRoute.
     * 
     * @param actualPaidBreakTime
     */
    public void setActualPaidBreakTime(int actualPaidBreakTime) {
        this.actualPaidBreakTime = actualPaidBreakTime;
    }


    /**
     * Gets the actualWaitTime value for this RoutingRoute.
     * 
     * @return actualWaitTime
     */
    public int getActualWaitTime() {
        return actualWaitTime;
    }


    /**
     * Sets the actualWaitTime value for this RoutingRoute.
     * 
     * @param actualWaitTime
     */
    public void setActualWaitTime(int actualWaitTime) {
        this.actualWaitTime = actualWaitTime;
    }


    /**
     * Gets the actualPaidWaitTime value for this RoutingRoute.
     * 
     * @return actualPaidWaitTime
     */
    public int getActualPaidWaitTime() {
        return actualPaidWaitTime;
    }


    /**
     * Sets the actualPaidWaitTime value for this RoutingRoute.
     * 
     * @param actualPaidWaitTime
     */
    public void setActualPaidWaitTime(int actualPaidWaitTime) {
        this.actualPaidWaitTime = actualPaidWaitTime;
    }


    /**
     * Gets the actualLayoverTime value for this RoutingRoute.
     * 
     * @return actualLayoverTime
     */
    public int getActualLayoverTime() {
        return actualLayoverTime;
    }


    /**
     * Sets the actualLayoverTime value for this RoutingRoute.
     * 
     * @param actualLayoverTime
     */
    public void setActualLayoverTime(int actualLayoverTime) {
        this.actualLayoverTime = actualLayoverTime;
    }


    /**
     * Gets the actualPaidLayoverTime value for this RoutingRoute.
     * 
     * @return actualPaidLayoverTime
     */
    public int getActualPaidLayoverTime() {
        return actualPaidLayoverTime;
    }


    /**
     * Sets the actualPaidLayoverTime value for this RoutingRoute.
     * 
     * @param actualPaidLayoverTime
     */
    public void setActualPaidLayoverTime(int actualPaidLayoverTime) {
        this.actualPaidLayoverTime = actualPaidLayoverTime;
    }


    /**
     * Gets the actualPreRouteTime value for this RoutingRoute.
     * 
     * @return actualPreRouteTime
     */
    public int getActualPreRouteTime() {
        return actualPreRouteTime;
    }


    /**
     * Sets the actualPreRouteTime value for this RoutingRoute.
     * 
     * @param actualPreRouteTime
     */
    public void setActualPreRouteTime(int actualPreRouteTime) {
        this.actualPreRouteTime = actualPreRouteTime;
    }


    /**
     * Gets the actualPostRouteTime value for this RoutingRoute.
     * 
     * @return actualPostRouteTime
     */
    public int getActualPostRouteTime() {
        return actualPostRouteTime;
    }


    /**
     * Sets the actualPostRouteTime value for this RoutingRoute.
     * 
     * @param actualPostRouteTime
     */
    public void setActualPostRouteTime(int actualPostRouteTime) {
        this.actualPostRouteTime = actualPostRouteTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingRoute)) return false;
        RoutingRoute other = (RoutingRoute) obj;
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
            this.routeNumber == other.getRouteNumber() &&
            ((this.routeID==null && other.getRouteID()==null) || 
             (this.routeID!=null &&
              this.routeID.equals(other.getRouteID()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.origin==null && other.getOrigin()==null) || 
             (this.origin!=null &&
              this.origin.equals(other.getOrigin()))) &&
            ((this.destination==null && other.getDestination()==null) || 
             (this.destination!=null &&
              this.destination.equals(other.getDestination()))) &&
            this.helper == other.isHelper() &&
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
            ((this.completeTime==null && other.getCompleteTime()==null) || 
             (this.completeTime!=null &&
              this.completeTime.equals(other.getCompleteTime()))) &&
            ((this.capacity==null && other.getCapacity()==null) || 
             (this.capacity!=null &&
              this.capacity.equals(other.getCapacity()))) &&
            this.color == other.getColor() &&
            this.distance == other.getDistance() &&
            ((this.startingQuantity==null && other.getStartingQuantity()==null) || 
             (this.startingQuantity!=null &&
              this.startingQuantity.equals(other.getStartingQuantity()))) &&
            ((this.endingQuantity==null && other.getEndingQuantity()==null) || 
             (this.endingQuantity!=null &&
              this.endingQuantity.equals(other.getEndingQuantity()))) &&
            ((this.drivers==null && other.getDrivers()==null) || 
             (this.drivers!=null &&
              java.util.Arrays.equals(this.drivers, other.getDrivers()))) &&
            ((this.routeEquipment==null && other.getRouteEquipment()==null) || 
             (this.routeEquipment!=null &&
              java.util.Arrays.equals(this.routeEquipment, other.getRouteEquipment()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.day==null && other.getDay()==null) || 
             (this.day!=null &&
              this.day.equals(other.getDay()))) &&
            this.teamSplit == other.isTeamSplit() &&
            ((this.modelID==null && other.getModelID()==null) || 
             (this.modelID!=null &&
              this.modelID.equals(other.getModelID()))) &&
            this.loadPriority == other.getLoadPriority() &&
            this.maximumTime == other.getMaximumTime() &&
            this.preferredTime == other.getPreferredTime() &&
            this.regularTime == other.getRegularTime() &&
            this.travelTime == other.getTravelTime() &&
            this.serviceTime == other.getServiceTime() &&
            this.totalTime == other.getTotalTime() &&
            this.paidTime == other.getPaidTime() &&
            this.nonPaidTime == other.getNonPaidTime() &&
            this.breakTime == other.getBreakTime() &&
            this.paidBreakTime == other.getPaidBreakTime() &&
            this.waitTime == other.getWaitTime() &&
            this.paidWaitTime == other.getPaidWaitTime() &&
            this.layoverTime == other.getLayoverTime() &&
            this.paidLayoverTime == other.getPaidLayoverTime() &&
            this.driverRegularTimeCost == other.getDriverRegularTimeCost() &&
            this.driverOverTimeCost == other.getDriverOverTimeCost() &&
            this.driverStopCost == other.getDriverStopCost() &&
            this.driverDistanceCost == other.getDriverDistanceCost() &&
            this.driverPieceCost == other.getDriverPieceCost() &&
            this.driverFixedCost == other.getDriverFixedCost() &&
            this.equipmentFixedCost == other.getEquipmentFixedCost() &&
            this.equipmentDistanceCost == other.getEquipmentDistanceCost() &&
            this.reloadTime == other.getReloadTime() &&
            ((this.creationMethod==null && other.getCreationMethod()==null) || 
             (this.creationMethod!=null &&
              this.creationMethod.equals(other.getCreationMethod()))) &&
            this.week == other.getWeek() &&
            this.fees == other.getFees() &&
            this.lastStopIsDestination == other.isLastStopIsDestination() &&
            this.depotServiceTime == other.getDepotServiceTime() &&
            this.originIsDestination == other.isOriginIsDestination() &&
            this.calculateReloadTime == other.isCalculateReloadTime() &&
            this.shuttleTime == other.getShuttleTime() &&
            this.shuttleDistance == other.getShuttleDistance() &&
            this.preRouteTime == other.getPreRouteTime() &&
            this.postRouteTime == other.getPostRouteTime() &&
            this.stemDistanceOut == other.getStemDistanceOut() &&
            this.stemDistanceIn == other.getStemDistanceIn() &&
            this.stemTimeOut == other.getStemTimeOut() &&
            this.stemTimeIn == other.getStemTimeIn() &&
            ((this.previousReloadRouteIdentity==null && other.getPreviousReloadRouteIdentity()==null) || 
             (this.previousReloadRouteIdentity!=null &&
              this.previousReloadRouteIdentity.equals(other.getPreviousReloadRouteIdentity()))) &&
            ((this.nextReloadRouteIdentity==null && other.getNextReloadRouteIdentity()==null) || 
             (this.nextReloadRouteIdentity!=null &&
              this.nextReloadRouteIdentity.equals(other.getNextReloadRouteIdentity()))) &&
            ((this.startingLoadType==null && other.getStartingLoadType()==null) || 
             (this.startingLoadType!=null &&
              this.startingLoadType.equals(other.getStartingLoadType()))) &&
            ((this.preferedSkillSet==null && other.getPreferedSkillSet()==null) || 
             (this.preferedSkillSet!=null &&
              this.preferedSkillSet.equals(other.getPreferedSkillSet()))) &&
            ((this.dispatcherUserIdentity==null && other.getDispatcherUserIdentity()==null) || 
             (this.dispatcherUserIdentity!=null &&
              this.dispatcherUserIdentity.equals(other.getDispatcherUserIdentity()))) &&
            ((this.stops==null && other.getStops()==null) || 
             (this.stops!=null &&
              java.util.Arrays.equals(this.stops, other.getStops()))) &&
            ((this.actualDriver1Key==null && other.getActualDriver1Key()==null) || 
             (this.actualDriver1Key!=null &&
              this.actualDriver1Key.equals(other.getActualDriver1Key()))) &&
            ((this.actualDriver2Key==null && other.getActualDriver2Key()==null) || 
             (this.actualDriver2Key!=null &&
              this.actualDriver2Key.equals(other.getActualDriver2Key()))) &&
            ((this.actualSizes==null && other.getActualSizes()==null) || 
             (this.actualSizes!=null &&
              this.actualSizes.equals(other.getActualSizes()))) &&
            ((this.actualStartTime==null && other.getActualStartTime()==null) || 
             (this.actualStartTime!=null &&
              this.actualStartTime.equals(other.getActualStartTime()))) &&
            ((this.actualCompleteTime==null && other.getActualCompleteTime()==null) || 
             (this.actualCompleteTime!=null &&
              this.actualCompleteTime.equals(other.getActualCompleteTime()))) &&
            this.actualStops == other.getActualStops() &&
            this.actualDistance == other.getActualDistance() &&
            this.odometerStart == other.getOdometerStart() &&
            this.odometerFinish == other.getOdometerFinish() &&
            this.actualHelper == other.getActualHelper() &&
            ((this.comments==null && other.getComments()==null) || 
             (this.comments!=null &&
              this.comments.equals(other.getComments()))) &&
            this.actualDriverRegularTimeCost == other.getActualDriverRegularTimeCost() &&
            this.actualDriverOvertimeCost == other.getActualDriverOvertimeCost() &&
            this.actualDriverStopCost == other.getActualDriverStopCost() &&
            this.actualDriverDistanceCost == other.getActualDriverDistanceCost() &&
            this.actualDriverPieceCost == other.getActualDriverPieceCost() &&
            this.actualDriverFixedCost == other.getActualDriverFixedCost() &&
            this.actualEquipmentFixedCost == other.getActualEquipmentFixedCost() &&
            this.actualEquipmentDistanceCost == other.getActualEquipmentDistanceCost() &&
            this.actualFees == other.getActualFees() &&
            this.actualServiceTime == other.getActualServiceTime() &&
            this.actualTravelTime == other.getActualTravelTime() &&
            this.actualBreakTime == other.getActualBreakTime() &&
            this.actualPaidBreakTime == other.getActualPaidBreakTime() &&
            this.actualWaitTime == other.getActualWaitTime() &&
            this.actualPaidWaitTime == other.getActualPaidWaitTime() &&
            this.actualLayoverTime == other.getActualLayoverTime() &&
            this.actualPaidLayoverTime == other.getActualPaidLayoverTime() &&
            this.actualPreRouteTime == other.getActualPreRouteTime() &&
            this.actualPostRouteTime == other.getActualPostRouteTime();
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
        _hashCode += getRouteNumber();
        if (getRouteID() != null) {
            _hashCode += getRouteID().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getOrigin() != null) {
            _hashCode += getOrigin().hashCode();
        }
        if (getDestination() != null) {
            _hashCode += getDestination().hashCode();
        }
        _hashCode += (isHelper() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        if (getCompleteTime() != null) {
            _hashCode += getCompleteTime().hashCode();
        }
        if (getCapacity() != null) {
            _hashCode += getCapacity().hashCode();
        }
        _hashCode += getColor();
        _hashCode += new Double(getDistance()).hashCode();
        if (getStartingQuantity() != null) {
            _hashCode += getStartingQuantity().hashCode();
        }
        if (getEndingQuantity() != null) {
            _hashCode += getEndingQuantity().hashCode();
        }
        if (getDrivers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDrivers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDrivers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRouteEquipment() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRouteEquipment());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRouteEquipment(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getDay() != null) {
            _hashCode += getDay().hashCode();
        }
        _hashCode += (isTeamSplit() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getModelID() != null) {
            _hashCode += getModelID().hashCode();
        }
        _hashCode += getLoadPriority();
        _hashCode += getMaximumTime();
        _hashCode += getPreferredTime();
        _hashCode += getRegularTime();
        _hashCode += getTravelTime();
        _hashCode += getServiceTime();
        _hashCode += getTotalTime();
        _hashCode += getPaidTime();
        _hashCode += getNonPaidTime();
        _hashCode += getBreakTime();
        _hashCode += getPaidBreakTime();
        _hashCode += getWaitTime();
        _hashCode += getPaidWaitTime();
        _hashCode += getLayoverTime();
        _hashCode += getPaidLayoverTime();
        _hashCode += getDriverRegularTimeCost();
        _hashCode += getDriverOverTimeCost();
        _hashCode += getDriverStopCost();
        _hashCode += getDriverDistanceCost();
        _hashCode += getDriverPieceCost();
        _hashCode += getDriverFixedCost();
        _hashCode += getEquipmentFixedCost();
        _hashCode += getEquipmentDistanceCost();
        _hashCode += getReloadTime();
        if (getCreationMethod() != null) {
            _hashCode += getCreationMethod().hashCode();
        }
        _hashCode += getWeek();
        _hashCode += getFees();
        _hashCode += (isLastStopIsDestination() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getDepotServiceTime();
        _hashCode += (isOriginIsDestination() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCalculateReloadTime() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getShuttleTime();
        _hashCode += new Double(getShuttleDistance()).hashCode();
        _hashCode += getPreRouteTime();
        _hashCode += getPostRouteTime();
        _hashCode += new Double(getStemDistanceOut()).hashCode();
        _hashCode += new Double(getStemDistanceIn()).hashCode();
        _hashCode += getStemTimeOut();
        _hashCode += getStemTimeIn();
        if (getPreviousReloadRouteIdentity() != null) {
            _hashCode += getPreviousReloadRouteIdentity().hashCode();
        }
        if (getNextReloadRouteIdentity() != null) {
            _hashCode += getNextReloadRouteIdentity().hashCode();
        }
        if (getStartingLoadType() != null) {
            _hashCode += getStartingLoadType().hashCode();
        }
        if (getPreferedSkillSet() != null) {
            _hashCode += getPreferedSkillSet().hashCode();
        }
        if (getDispatcherUserIdentity() != null) {
            _hashCode += getDispatcherUserIdentity().hashCode();
        }
        if (getStops() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStops());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStops(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getActualDriver1Key() != null) {
            _hashCode += getActualDriver1Key().hashCode();
        }
        if (getActualDriver2Key() != null) {
            _hashCode += getActualDriver2Key().hashCode();
        }
        if (getActualSizes() != null) {
            _hashCode += getActualSizes().hashCode();
        }
        if (getActualStartTime() != null) {
            _hashCode += getActualStartTime().hashCode();
        }
        if (getActualCompleteTime() != null) {
            _hashCode += getActualCompleteTime().hashCode();
        }
        _hashCode += getActualStops();
        _hashCode += getActualDistance();
        _hashCode += getOdometerStart();
        _hashCode += getOdometerFinish();
        _hashCode += getActualHelper();
        if (getComments() != null) {
            _hashCode += getComments().hashCode();
        }
        _hashCode += getActualDriverRegularTimeCost();
        _hashCode += getActualDriverOvertimeCost();
        _hashCode += getActualDriverStopCost();
        _hashCode += getActualDriverDistanceCost();
        _hashCode += getActualDriverPieceCost();
        _hashCode += getActualDriverFixedCost();
        _hashCode += getActualEquipmentFixedCost();
        _hashCode += getActualEquipmentDistanceCost();
        _hashCode += getActualFees();
        _hashCode += getActualServiceTime();
        _hashCode += getActualTravelTime();
        _hashCode += getActualBreakTime();
        _hashCode += getActualPaidBreakTime();
        _hashCode += getActualWaitTime();
        _hashCode += getActualPaidWaitTime();
        _hashCode += getActualLayoverTime();
        _hashCode += getActualPaidLayoverTime();
        _hashCode += getActualPreRouteTime();
        _hashCode += getActualPostRouteTime();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingRoute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRoute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("origin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "origin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destination");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "destination"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("helper");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "helper"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "startTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completeTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "completeTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("capacity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "capacity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("color");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "color"));
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
        elemField.setFieldName("startingQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "startingQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endingQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "endingQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drivers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "drivers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeEquipment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeEquipment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingEquipmentIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("day");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "day"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DayOfWeek"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("teamSplit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "teamSplit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modelID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "modelID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loadPriority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "loadPriority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maximumTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maximumTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regularTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regularTime"));
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
        elemField.setFieldName("serviceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "serviceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paidTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "paidTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nonPaidTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "nonPaidTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("breakTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "breakTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paidBreakTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "paidBreakTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waitTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "waitTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paidWaitTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "paidWaitTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("layoverTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "layoverTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paidLayoverTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "paidLayoverTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverRegularTimeCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverRegularTimeCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverOverTimeCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverOverTimeCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverStopCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverStopCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverDistanceCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverDistanceCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverPieceCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverPieceCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverFixedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverFixedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentFixedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentFixedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentDistanceCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentDistanceCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reloadTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "reloadTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "creationMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("week");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "week"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fees");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "fees"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastStopIsDestination");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lastStopIsDestination"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depotServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "depotServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originIsDestination");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "originIsDestination"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("calculateReloadTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "calculateReloadTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shuttleTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "shuttleTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shuttleDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "shuttleDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preRouteTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preRouteTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postRouteTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "postRouteTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stemDistanceOut");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stemDistanceOut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stemDistanceIn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stemDistanceIn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stemTimeOut");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stemTimeOut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stemTimeIn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stemTimeIn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("previousReloadRouteIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "previousReloadRouteIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nextReloadRouteIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "nextReloadRouteIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startingLoadType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "startingLoadType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingReloadType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferedSkillSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferedSkillSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dispatcherUserIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dispatcherUserIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UserIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stops");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stops"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingStop"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDriver1Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDriver1Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDriver2Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDriver2Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualSizes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualSizes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualStartTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualStartTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualCompleteTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualCompleteTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualStops");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualStops"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("odometerStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "odometerStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("odometerFinish");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "odometerFinish"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualHelper");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualHelper"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comments");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "comments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDriverRegularTimeCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDriverRegularTimeCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDriverOvertimeCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDriverOvertimeCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDriverStopCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDriverStopCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDriverDistanceCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDriverDistanceCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDriverPieceCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDriverPieceCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDriverFixedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDriverFixedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualEquipmentFixedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualEquipmentFixedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualEquipmentDistanceCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualEquipmentDistanceCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualFees");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualFees"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("actualBreakTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualBreakTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualPaidBreakTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualPaidBreakTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualWaitTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualWaitTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualPaidWaitTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualPaidWaitTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualLayoverTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualLayoverTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualPaidLayoverTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualPaidLayoverTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualPreRouteTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualPreRouteTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualPostRouteTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualPostRouteTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
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
