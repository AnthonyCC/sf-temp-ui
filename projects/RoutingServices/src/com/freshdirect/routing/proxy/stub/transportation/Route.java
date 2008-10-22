/**
 * Route.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Route  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private java.lang.String description;

    private int internalRouteID;

    private boolean assigned;

    private boolean outOfSequence;

    private java.lang.String seal;

    private double plannedDistance;

    private double projectedDistance;

    private double actualDistance;

    private java.util.Calendar plannedStart;

    private java.util.Calendar plannedOriginDeparture;

    private java.util.Calendar plannedDestinationArrival;

    private java.util.Calendar plannedComplete;

    private java.util.Calendar projectedOriginDeparture;

    private java.util.Calendar projectedDestinationArrival;

    private java.util.Calendar projectedComplete;

    private java.util.Calendar actualStart;

    private java.util.Calendar actualOriginDeparture;

    private java.util.Calendar actualDestinationArrival;

    private java.util.Calendar actualComplete;

    private double plannedCost;

    private double actualCost;

    private java.lang.String dispatcherID;

    private java.lang.String group;

    private java.lang.String scenario;

    private boolean wirelessLoaded;

    private boolean inProgress;

    private boolean complete;

    private java.lang.String gpsUnitID;

    private int numberOfStops;

    private int numberOfStopsDelivered;

    private int numberOfStopsCancelled;

    private java.lang.String positionText;

    private int positionLatitude;

    private int positionLongitude;

    private int delayMinutes;

    private java.lang.String delayType;

    private int postDelayMinutes;

    private java.lang.String postDelayType;

    private java.lang.String status;

    private java.lang.String routeType;

    private boolean lastStopIsDestination;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity origin;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity destination;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue originTimeZone;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue destinationTimeZone;

    private java.lang.String userDefinedField1;

    private java.lang.String userDefinedField2;

    private java.lang.String userDefinedField3;

    private int color;

    private boolean isDeliveryRoute;

    private java.lang.String driver1Name;

    private java.lang.String driver2Name;

    private com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity preRouteSurveyIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity postRouteSurveyIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.Stop[] stops;

    public Route() {
    }

    public Route(
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           java.lang.String description,
           int internalRouteID,
           boolean assigned,
           boolean outOfSequence,
           java.lang.String seal,
           double plannedDistance,
           double projectedDistance,
           double actualDistance,
           java.util.Calendar plannedStart,
           java.util.Calendar plannedOriginDeparture,
           java.util.Calendar plannedDestinationArrival,
           java.util.Calendar plannedComplete,
           java.util.Calendar projectedOriginDeparture,
           java.util.Calendar projectedDestinationArrival,
           java.util.Calendar projectedComplete,
           java.util.Calendar actualStart,
           java.util.Calendar actualOriginDeparture,
           java.util.Calendar actualDestinationArrival,
           java.util.Calendar actualComplete,
           double plannedCost,
           double actualCost,
           java.lang.String dispatcherID,
           java.lang.String group,
           java.lang.String scenario,
           boolean wirelessLoaded,
           boolean inProgress,
           boolean complete,
           java.lang.String gpsUnitID,
           int numberOfStops,
           int numberOfStopsDelivered,
           int numberOfStopsCancelled,
           java.lang.String positionText,
           int positionLatitude,
           int positionLongitude,
           int delayMinutes,
           java.lang.String delayType,
           int postDelayMinutes,
           java.lang.String postDelayType,
           java.lang.String status,
           java.lang.String routeType,
           boolean lastStopIsDestination,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity origin,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity destination,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue originTimeZone,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue destinationTimeZone,
           java.lang.String userDefinedField1,
           java.lang.String userDefinedField2,
           java.lang.String userDefinedField3,
           int color,
           boolean isDeliveryRoute,
           java.lang.String driver1Name,
           java.lang.String driver2Name,
           com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity preRouteSurveyIdentity,
           com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity postRouteSurveyIdentity,
           com.freshdirect.routing.proxy.stub.transportation.Stop[] stops) {
           this.routeIdentity = routeIdentity;
           this.description = description;
           this.internalRouteID = internalRouteID;
           this.assigned = assigned;
           this.outOfSequence = outOfSequence;
           this.seal = seal;
           this.plannedDistance = plannedDistance;
           this.projectedDistance = projectedDistance;
           this.actualDistance = actualDistance;
           this.plannedStart = plannedStart;
           this.plannedOriginDeparture = plannedOriginDeparture;
           this.plannedDestinationArrival = plannedDestinationArrival;
           this.plannedComplete = plannedComplete;
           this.projectedOriginDeparture = projectedOriginDeparture;
           this.projectedDestinationArrival = projectedDestinationArrival;
           this.projectedComplete = projectedComplete;
           this.actualStart = actualStart;
           this.actualOriginDeparture = actualOriginDeparture;
           this.actualDestinationArrival = actualDestinationArrival;
           this.actualComplete = actualComplete;
           this.plannedCost = plannedCost;
           this.actualCost = actualCost;
           this.dispatcherID = dispatcherID;
           this.group = group;
           this.scenario = scenario;
           this.wirelessLoaded = wirelessLoaded;
           this.inProgress = inProgress;
           this.complete = complete;
           this.gpsUnitID = gpsUnitID;
           this.numberOfStops = numberOfStops;
           this.numberOfStopsDelivered = numberOfStopsDelivered;
           this.numberOfStopsCancelled = numberOfStopsCancelled;
           this.positionText = positionText;
           this.positionLatitude = positionLatitude;
           this.positionLongitude = positionLongitude;
           this.delayMinutes = delayMinutes;
           this.delayType = delayType;
           this.postDelayMinutes = postDelayMinutes;
           this.postDelayType = postDelayType;
           this.status = status;
           this.routeType = routeType;
           this.lastStopIsDestination = lastStopIsDestination;
           this.origin = origin;
           this.destination = destination;
           this.originTimeZone = originTimeZone;
           this.destinationTimeZone = destinationTimeZone;
           this.userDefinedField1 = userDefinedField1;
           this.userDefinedField2 = userDefinedField2;
           this.userDefinedField3 = userDefinedField3;
           this.color = color;
           this.isDeliveryRoute = isDeliveryRoute;
           this.driver1Name = driver1Name;
           this.driver2Name = driver2Name;
           this.preRouteSurveyIdentity = preRouteSurveyIdentity;
           this.postRouteSurveyIdentity = postRouteSurveyIdentity;
           this.stops = stops;
    }


    /**
     * Gets the routeIdentity value for this Route.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this Route.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the description value for this Route.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Route.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the internalRouteID value for this Route.
     * 
     * @return internalRouteID
     */
    public int getInternalRouteID() {
        return internalRouteID;
    }


    /**
     * Sets the internalRouteID value for this Route.
     * 
     * @param internalRouteID
     */
    public void setInternalRouteID(int internalRouteID) {
        this.internalRouteID = internalRouteID;
    }


    /**
     * Gets the assigned value for this Route.
     * 
     * @return assigned
     */
    public boolean isAssigned() {
        return assigned;
    }


    /**
     * Sets the assigned value for this Route.
     * 
     * @param assigned
     */
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }


    /**
     * Gets the outOfSequence value for this Route.
     * 
     * @return outOfSequence
     */
    public boolean isOutOfSequence() {
        return outOfSequence;
    }


    /**
     * Sets the outOfSequence value for this Route.
     * 
     * @param outOfSequence
     */
    public void setOutOfSequence(boolean outOfSequence) {
        this.outOfSequence = outOfSequence;
    }


    /**
     * Gets the seal value for this Route.
     * 
     * @return seal
     */
    public java.lang.String getSeal() {
        return seal;
    }


    /**
     * Sets the seal value for this Route.
     * 
     * @param seal
     */
    public void setSeal(java.lang.String seal) {
        this.seal = seal;
    }


    /**
     * Gets the plannedDistance value for this Route.
     * 
     * @return plannedDistance
     */
    public double getPlannedDistance() {
        return plannedDistance;
    }


    /**
     * Sets the plannedDistance value for this Route.
     * 
     * @param plannedDistance
     */
    public void setPlannedDistance(double plannedDistance) {
        this.plannedDistance = plannedDistance;
    }


    /**
     * Gets the projectedDistance value for this Route.
     * 
     * @return projectedDistance
     */
    public double getProjectedDistance() {
        return projectedDistance;
    }


    /**
     * Sets the projectedDistance value for this Route.
     * 
     * @param projectedDistance
     */
    public void setProjectedDistance(double projectedDistance) {
        this.projectedDistance = projectedDistance;
    }


    /**
     * Gets the actualDistance value for this Route.
     * 
     * @return actualDistance
     */
    public double getActualDistance() {
        return actualDistance;
    }


    /**
     * Sets the actualDistance value for this Route.
     * 
     * @param actualDistance
     */
    public void setActualDistance(double actualDistance) {
        this.actualDistance = actualDistance;
    }


    /**
     * Gets the plannedStart value for this Route.
     * 
     * @return plannedStart
     */
    public java.util.Calendar getPlannedStart() {
        return plannedStart;
    }


    /**
     * Sets the plannedStart value for this Route.
     * 
     * @param plannedStart
     */
    public void setPlannedStart(java.util.Calendar plannedStart) {
        this.plannedStart = plannedStart;
    }


    /**
     * Gets the plannedOriginDeparture value for this Route.
     * 
     * @return plannedOriginDeparture
     */
    public java.util.Calendar getPlannedOriginDeparture() {
        return plannedOriginDeparture;
    }


    /**
     * Sets the plannedOriginDeparture value for this Route.
     * 
     * @param plannedOriginDeparture
     */
    public void setPlannedOriginDeparture(java.util.Calendar plannedOriginDeparture) {
        this.plannedOriginDeparture = plannedOriginDeparture;
    }


    /**
     * Gets the plannedDestinationArrival value for this Route.
     * 
     * @return plannedDestinationArrival
     */
    public java.util.Calendar getPlannedDestinationArrival() {
        return plannedDestinationArrival;
    }


    /**
     * Sets the plannedDestinationArrival value for this Route.
     * 
     * @param plannedDestinationArrival
     */
    public void setPlannedDestinationArrival(java.util.Calendar plannedDestinationArrival) {
        this.plannedDestinationArrival = plannedDestinationArrival;
    }


    /**
     * Gets the plannedComplete value for this Route.
     * 
     * @return plannedComplete
     */
    public java.util.Calendar getPlannedComplete() {
        return plannedComplete;
    }


    /**
     * Sets the plannedComplete value for this Route.
     * 
     * @param plannedComplete
     */
    public void setPlannedComplete(java.util.Calendar plannedComplete) {
        this.plannedComplete = plannedComplete;
    }


    /**
     * Gets the projectedOriginDeparture value for this Route.
     * 
     * @return projectedOriginDeparture
     */
    public java.util.Calendar getProjectedOriginDeparture() {
        return projectedOriginDeparture;
    }


    /**
     * Sets the projectedOriginDeparture value for this Route.
     * 
     * @param projectedOriginDeparture
     */
    public void setProjectedOriginDeparture(java.util.Calendar projectedOriginDeparture) {
        this.projectedOriginDeparture = projectedOriginDeparture;
    }


    /**
     * Gets the projectedDestinationArrival value for this Route.
     * 
     * @return projectedDestinationArrival
     */
    public java.util.Calendar getProjectedDestinationArrival() {
        return projectedDestinationArrival;
    }


    /**
     * Sets the projectedDestinationArrival value for this Route.
     * 
     * @param projectedDestinationArrival
     */
    public void setProjectedDestinationArrival(java.util.Calendar projectedDestinationArrival) {
        this.projectedDestinationArrival = projectedDestinationArrival;
    }


    /**
     * Gets the projectedComplete value for this Route.
     * 
     * @return projectedComplete
     */
    public java.util.Calendar getProjectedComplete() {
        return projectedComplete;
    }


    /**
     * Sets the projectedComplete value for this Route.
     * 
     * @param projectedComplete
     */
    public void setProjectedComplete(java.util.Calendar projectedComplete) {
        this.projectedComplete = projectedComplete;
    }


    /**
     * Gets the actualStart value for this Route.
     * 
     * @return actualStart
     */
    public java.util.Calendar getActualStart() {
        return actualStart;
    }


    /**
     * Sets the actualStart value for this Route.
     * 
     * @param actualStart
     */
    public void setActualStart(java.util.Calendar actualStart) {
        this.actualStart = actualStart;
    }


    /**
     * Gets the actualOriginDeparture value for this Route.
     * 
     * @return actualOriginDeparture
     */
    public java.util.Calendar getActualOriginDeparture() {
        return actualOriginDeparture;
    }


    /**
     * Sets the actualOriginDeparture value for this Route.
     * 
     * @param actualOriginDeparture
     */
    public void setActualOriginDeparture(java.util.Calendar actualOriginDeparture) {
        this.actualOriginDeparture = actualOriginDeparture;
    }


    /**
     * Gets the actualDestinationArrival value for this Route.
     * 
     * @return actualDestinationArrival
     */
    public java.util.Calendar getActualDestinationArrival() {
        return actualDestinationArrival;
    }


    /**
     * Sets the actualDestinationArrival value for this Route.
     * 
     * @param actualDestinationArrival
     */
    public void setActualDestinationArrival(java.util.Calendar actualDestinationArrival) {
        this.actualDestinationArrival = actualDestinationArrival;
    }


    /**
     * Gets the actualComplete value for this Route.
     * 
     * @return actualComplete
     */
    public java.util.Calendar getActualComplete() {
        return actualComplete;
    }


    /**
     * Sets the actualComplete value for this Route.
     * 
     * @param actualComplete
     */
    public void setActualComplete(java.util.Calendar actualComplete) {
        this.actualComplete = actualComplete;
    }


    /**
     * Gets the plannedCost value for this Route.
     * 
     * @return plannedCost
     */
    public double getPlannedCost() {
        return plannedCost;
    }


    /**
     * Sets the plannedCost value for this Route.
     * 
     * @param plannedCost
     */
    public void setPlannedCost(double plannedCost) {
        this.plannedCost = plannedCost;
    }


    /**
     * Gets the actualCost value for this Route.
     * 
     * @return actualCost
     */
    public double getActualCost() {
        return actualCost;
    }


    /**
     * Sets the actualCost value for this Route.
     * 
     * @param actualCost
     */
    public void setActualCost(double actualCost) {
        this.actualCost = actualCost;
    }


    /**
     * Gets the dispatcherID value for this Route.
     * 
     * @return dispatcherID
     */
    public java.lang.String getDispatcherID() {
        return dispatcherID;
    }


    /**
     * Sets the dispatcherID value for this Route.
     * 
     * @param dispatcherID
     */
    public void setDispatcherID(java.lang.String dispatcherID) {
        this.dispatcherID = dispatcherID;
    }


    /**
     * Gets the group value for this Route.
     * 
     * @return group
     */
    public java.lang.String getGroup() {
        return group;
    }


    /**
     * Sets the group value for this Route.
     * 
     * @param group
     */
    public void setGroup(java.lang.String group) {
        this.group = group;
    }


    /**
     * Gets the scenario value for this Route.
     * 
     * @return scenario
     */
    public java.lang.String getScenario() {
        return scenario;
    }


    /**
     * Sets the scenario value for this Route.
     * 
     * @param scenario
     */
    public void setScenario(java.lang.String scenario) {
        this.scenario = scenario;
    }


    /**
     * Gets the wirelessLoaded value for this Route.
     * 
     * @return wirelessLoaded
     */
    public boolean isWirelessLoaded() {
        return wirelessLoaded;
    }


    /**
     * Sets the wirelessLoaded value for this Route.
     * 
     * @param wirelessLoaded
     */
    public void setWirelessLoaded(boolean wirelessLoaded) {
        this.wirelessLoaded = wirelessLoaded;
    }


    /**
     * Gets the inProgress value for this Route.
     * 
     * @return inProgress
     */
    public boolean isInProgress() {
        return inProgress;
    }


    /**
     * Sets the inProgress value for this Route.
     * 
     * @param inProgress
     */
    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }


    /**
     * Gets the complete value for this Route.
     * 
     * @return complete
     */
    public boolean isComplete() {
        return complete;
    }


    /**
     * Sets the complete value for this Route.
     * 
     * @param complete
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }


    /**
     * Gets the gpsUnitID value for this Route.
     * 
     * @return gpsUnitID
     */
    public java.lang.String getGpsUnitID() {
        return gpsUnitID;
    }


    /**
     * Sets the gpsUnitID value for this Route.
     * 
     * @param gpsUnitID
     */
    public void setGpsUnitID(java.lang.String gpsUnitID) {
        this.gpsUnitID = gpsUnitID;
    }


    /**
     * Gets the numberOfStops value for this Route.
     * 
     * @return numberOfStops
     */
    public int getNumberOfStops() {
        return numberOfStops;
    }


    /**
     * Sets the numberOfStops value for this Route.
     * 
     * @param numberOfStops
     */
    public void setNumberOfStops(int numberOfStops) {
        this.numberOfStops = numberOfStops;
    }


    /**
     * Gets the numberOfStopsDelivered value for this Route.
     * 
     * @return numberOfStopsDelivered
     */
    public int getNumberOfStopsDelivered() {
        return numberOfStopsDelivered;
    }


    /**
     * Sets the numberOfStopsDelivered value for this Route.
     * 
     * @param numberOfStopsDelivered
     */
    public void setNumberOfStopsDelivered(int numberOfStopsDelivered) {
        this.numberOfStopsDelivered = numberOfStopsDelivered;
    }


    /**
     * Gets the numberOfStopsCancelled value for this Route.
     * 
     * @return numberOfStopsCancelled
     */
    public int getNumberOfStopsCancelled() {
        return numberOfStopsCancelled;
    }


    /**
     * Sets the numberOfStopsCancelled value for this Route.
     * 
     * @param numberOfStopsCancelled
     */
    public void setNumberOfStopsCancelled(int numberOfStopsCancelled) {
        this.numberOfStopsCancelled = numberOfStopsCancelled;
    }


    /**
     * Gets the positionText value for this Route.
     * 
     * @return positionText
     */
    public java.lang.String getPositionText() {
        return positionText;
    }


    /**
     * Sets the positionText value for this Route.
     * 
     * @param positionText
     */
    public void setPositionText(java.lang.String positionText) {
        this.positionText = positionText;
    }


    /**
     * Gets the positionLatitude value for this Route.
     * 
     * @return positionLatitude
     */
    public int getPositionLatitude() {
        return positionLatitude;
    }


    /**
     * Sets the positionLatitude value for this Route.
     * 
     * @param positionLatitude
     */
    public void setPositionLatitude(int positionLatitude) {
        this.positionLatitude = positionLatitude;
    }


    /**
     * Gets the positionLongitude value for this Route.
     * 
     * @return positionLongitude
     */
    public int getPositionLongitude() {
        return positionLongitude;
    }


    /**
     * Sets the positionLongitude value for this Route.
     * 
     * @param positionLongitude
     */
    public void setPositionLongitude(int positionLongitude) {
        this.positionLongitude = positionLongitude;
    }


    /**
     * Gets the delayMinutes value for this Route.
     * 
     * @return delayMinutes
     */
    public int getDelayMinutes() {
        return delayMinutes;
    }


    /**
     * Sets the delayMinutes value for this Route.
     * 
     * @param delayMinutes
     */
    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }


    /**
     * Gets the delayType value for this Route.
     * 
     * @return delayType
     */
    public java.lang.String getDelayType() {
        return delayType;
    }


    /**
     * Sets the delayType value for this Route.
     * 
     * @param delayType
     */
    public void setDelayType(java.lang.String delayType) {
        this.delayType = delayType;
    }


    /**
     * Gets the postDelayMinutes value for this Route.
     * 
     * @return postDelayMinutes
     */
    public int getPostDelayMinutes() {
        return postDelayMinutes;
    }


    /**
     * Sets the postDelayMinutes value for this Route.
     * 
     * @param postDelayMinutes
     */
    public void setPostDelayMinutes(int postDelayMinutes) {
        this.postDelayMinutes = postDelayMinutes;
    }


    /**
     * Gets the postDelayType value for this Route.
     * 
     * @return postDelayType
     */
    public java.lang.String getPostDelayType() {
        return postDelayType;
    }


    /**
     * Sets the postDelayType value for this Route.
     * 
     * @param postDelayType
     */
    public void setPostDelayType(java.lang.String postDelayType) {
        this.postDelayType = postDelayType;
    }


    /**
     * Gets the status value for this Route.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Route.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the routeType value for this Route.
     * 
     * @return routeType
     */
    public java.lang.String getRouteType() {
        return routeType;
    }


    /**
     * Sets the routeType value for this Route.
     * 
     * @param routeType
     */
    public void setRouteType(java.lang.String routeType) {
        this.routeType = routeType;
    }


    /**
     * Gets the lastStopIsDestination value for this Route.
     * 
     * @return lastStopIsDestination
     */
    public boolean isLastStopIsDestination() {
        return lastStopIsDestination;
    }


    /**
     * Sets the lastStopIsDestination value for this Route.
     * 
     * @param lastStopIsDestination
     */
    public void setLastStopIsDestination(boolean lastStopIsDestination) {
        this.lastStopIsDestination = lastStopIsDestination;
    }


    /**
     * Gets the origin value for this Route.
     * 
     * @return origin
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getOrigin() {
        return origin;
    }


    /**
     * Sets the origin value for this Route.
     * 
     * @param origin
     */
    public void setOrigin(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity origin) {
        this.origin = origin;
    }


    /**
     * Gets the destination value for this Route.
     * 
     * @return destination
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getDestination() {
        return destination;
    }


    /**
     * Sets the destination value for this Route.
     * 
     * @param destination
     */
    public void setDestination(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity destination) {
        this.destination = destination;
    }


    /**
     * Gets the originTimeZone value for this Route.
     * 
     * @return originTimeZone
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue getOriginTimeZone() {
        return originTimeZone;
    }


    /**
     * Sets the originTimeZone value for this Route.
     * 
     * @param originTimeZone
     */
    public void setOriginTimeZone(com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue originTimeZone) {
        this.originTimeZone = originTimeZone;
    }


    /**
     * Gets the destinationTimeZone value for this Route.
     * 
     * @return destinationTimeZone
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue getDestinationTimeZone() {
        return destinationTimeZone;
    }


    /**
     * Sets the destinationTimeZone value for this Route.
     * 
     * @param destinationTimeZone
     */
    public void setDestinationTimeZone(com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue destinationTimeZone) {
        this.destinationTimeZone = destinationTimeZone;
    }


    /**
     * Gets the userDefinedField1 value for this Route.
     * 
     * @return userDefinedField1
     */
    public java.lang.String getUserDefinedField1() {
        return userDefinedField1;
    }


    /**
     * Sets the userDefinedField1 value for this Route.
     * 
     * @param userDefinedField1
     */
    public void setUserDefinedField1(java.lang.String userDefinedField1) {
        this.userDefinedField1 = userDefinedField1;
    }


    /**
     * Gets the userDefinedField2 value for this Route.
     * 
     * @return userDefinedField2
     */
    public java.lang.String getUserDefinedField2() {
        return userDefinedField2;
    }


    /**
     * Sets the userDefinedField2 value for this Route.
     * 
     * @param userDefinedField2
     */
    public void setUserDefinedField2(java.lang.String userDefinedField2) {
        this.userDefinedField2 = userDefinedField2;
    }


    /**
     * Gets the userDefinedField3 value for this Route.
     * 
     * @return userDefinedField3
     */
    public java.lang.String getUserDefinedField3() {
        return userDefinedField3;
    }


    /**
     * Sets the userDefinedField3 value for this Route.
     * 
     * @param userDefinedField3
     */
    public void setUserDefinedField3(java.lang.String userDefinedField3) {
        this.userDefinedField3 = userDefinedField3;
    }


    /**
     * Gets the color value for this Route.
     * 
     * @return color
     */
    public int getColor() {
        return color;
    }


    /**
     * Sets the color value for this Route.
     * 
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
    }


    /**
     * Gets the isDeliveryRoute value for this Route.
     * 
     * @return isDeliveryRoute
     */
    public boolean isIsDeliveryRoute() {
        return isDeliveryRoute;
    }


    /**
     * Sets the isDeliveryRoute value for this Route.
     * 
     * @param isDeliveryRoute
     */
    public void setIsDeliveryRoute(boolean isDeliveryRoute) {
        this.isDeliveryRoute = isDeliveryRoute;
    }


    /**
     * Gets the driver1Name value for this Route.
     * 
     * @return driver1Name
     */
    public java.lang.String getDriver1Name() {
        return driver1Name;
    }


    /**
     * Sets the driver1Name value for this Route.
     * 
     * @param driver1Name
     */
    public void setDriver1Name(java.lang.String driver1Name) {
        this.driver1Name = driver1Name;
    }


    /**
     * Gets the driver2Name value for this Route.
     * 
     * @return driver2Name
     */
    public java.lang.String getDriver2Name() {
        return driver2Name;
    }


    /**
     * Sets the driver2Name value for this Route.
     * 
     * @param driver2Name
     */
    public void setDriver2Name(java.lang.String driver2Name) {
        this.driver2Name = driver2Name;
    }


    /**
     * Gets the preRouteSurveyIdentity value for this Route.
     * 
     * @return preRouteSurveyIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity getPreRouteSurveyIdentity() {
        return preRouteSurveyIdentity;
    }


    /**
     * Sets the preRouteSurveyIdentity value for this Route.
     * 
     * @param preRouteSurveyIdentity
     */
    public void setPreRouteSurveyIdentity(com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity preRouteSurveyIdentity) {
        this.preRouteSurveyIdentity = preRouteSurveyIdentity;
    }


    /**
     * Gets the postRouteSurveyIdentity value for this Route.
     * 
     * @return postRouteSurveyIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity getPostRouteSurveyIdentity() {
        return postRouteSurveyIdentity;
    }


    /**
     * Sets the postRouteSurveyIdentity value for this Route.
     * 
     * @param postRouteSurveyIdentity
     */
    public void setPostRouteSurveyIdentity(com.freshdirect.routing.proxy.stub.transportation.SurveyIdentity postRouteSurveyIdentity) {
        this.postRouteSurveyIdentity = postRouteSurveyIdentity;
    }


    /**
     * Gets the stops value for this Route.
     * 
     * @return stops
     */
    public com.freshdirect.routing.proxy.stub.transportation.Stop[] getStops() {
        return stops;
    }


    /**
     * Sets the stops value for this Route.
     * 
     * @param stops
     */
    public void setStops(com.freshdirect.routing.proxy.stub.transportation.Stop[] stops) {
        this.stops = stops;
    }

    public com.freshdirect.routing.proxy.stub.transportation.Stop getStops(int i) {
        return this.stops[i];
    }

    public void setStops(int i, com.freshdirect.routing.proxy.stub.transportation.Stop _value) {
        this.stops[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Route)) return false;
        Route other = (Route) obj;
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
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.internalRouteID == other.getInternalRouteID() &&
            this.assigned == other.isAssigned() &&
            this.outOfSequence == other.isOutOfSequence() &&
            ((this.seal==null && other.getSeal()==null) || 
             (this.seal!=null &&
              this.seal.equals(other.getSeal()))) &&
            this.plannedDistance == other.getPlannedDistance() &&
            this.projectedDistance == other.getProjectedDistance() &&
            this.actualDistance == other.getActualDistance() &&
            ((this.plannedStart==null && other.getPlannedStart()==null) || 
             (this.plannedStart!=null &&
              this.plannedStart.equals(other.getPlannedStart()))) &&
            ((this.plannedOriginDeparture==null && other.getPlannedOriginDeparture()==null) || 
             (this.plannedOriginDeparture!=null &&
              this.plannedOriginDeparture.equals(other.getPlannedOriginDeparture()))) &&
            ((this.plannedDestinationArrival==null && other.getPlannedDestinationArrival()==null) || 
             (this.plannedDestinationArrival!=null &&
              this.plannedDestinationArrival.equals(other.getPlannedDestinationArrival()))) &&
            ((this.plannedComplete==null && other.getPlannedComplete()==null) || 
             (this.plannedComplete!=null &&
              this.plannedComplete.equals(other.getPlannedComplete()))) &&
            ((this.projectedOriginDeparture==null && other.getProjectedOriginDeparture()==null) || 
             (this.projectedOriginDeparture!=null &&
              this.projectedOriginDeparture.equals(other.getProjectedOriginDeparture()))) &&
            ((this.projectedDestinationArrival==null && other.getProjectedDestinationArrival()==null) || 
             (this.projectedDestinationArrival!=null &&
              this.projectedDestinationArrival.equals(other.getProjectedDestinationArrival()))) &&
            ((this.projectedComplete==null && other.getProjectedComplete()==null) || 
             (this.projectedComplete!=null &&
              this.projectedComplete.equals(other.getProjectedComplete()))) &&
            ((this.actualStart==null && other.getActualStart()==null) || 
             (this.actualStart!=null &&
              this.actualStart.equals(other.getActualStart()))) &&
            ((this.actualOriginDeparture==null && other.getActualOriginDeparture()==null) || 
             (this.actualOriginDeparture!=null &&
              this.actualOriginDeparture.equals(other.getActualOriginDeparture()))) &&
            ((this.actualDestinationArrival==null && other.getActualDestinationArrival()==null) || 
             (this.actualDestinationArrival!=null &&
              this.actualDestinationArrival.equals(other.getActualDestinationArrival()))) &&
            ((this.actualComplete==null && other.getActualComplete()==null) || 
             (this.actualComplete!=null &&
              this.actualComplete.equals(other.getActualComplete()))) &&
            this.plannedCost == other.getPlannedCost() &&
            this.actualCost == other.getActualCost() &&
            ((this.dispatcherID==null && other.getDispatcherID()==null) || 
             (this.dispatcherID!=null &&
              this.dispatcherID.equals(other.getDispatcherID()))) &&
            ((this.group==null && other.getGroup()==null) || 
             (this.group!=null &&
              this.group.equals(other.getGroup()))) &&
            ((this.scenario==null && other.getScenario()==null) || 
             (this.scenario!=null &&
              this.scenario.equals(other.getScenario()))) &&
            this.wirelessLoaded == other.isWirelessLoaded() &&
            this.inProgress == other.isInProgress() &&
            this.complete == other.isComplete() &&
            ((this.gpsUnitID==null && other.getGpsUnitID()==null) || 
             (this.gpsUnitID!=null &&
              this.gpsUnitID.equals(other.getGpsUnitID()))) &&
            this.numberOfStops == other.getNumberOfStops() &&
            this.numberOfStopsDelivered == other.getNumberOfStopsDelivered() &&
            this.numberOfStopsCancelled == other.getNumberOfStopsCancelled() &&
            ((this.positionText==null && other.getPositionText()==null) || 
             (this.positionText!=null &&
              this.positionText.equals(other.getPositionText()))) &&
            this.positionLatitude == other.getPositionLatitude() &&
            this.positionLongitude == other.getPositionLongitude() &&
            this.delayMinutes == other.getDelayMinutes() &&
            ((this.delayType==null && other.getDelayType()==null) || 
             (this.delayType!=null &&
              this.delayType.equals(other.getDelayType()))) &&
            this.postDelayMinutes == other.getPostDelayMinutes() &&
            ((this.postDelayType==null && other.getPostDelayType()==null) || 
             (this.postDelayType!=null &&
              this.postDelayType.equals(other.getPostDelayType()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.routeType==null && other.getRouteType()==null) || 
             (this.routeType!=null &&
              this.routeType.equals(other.getRouteType()))) &&
            this.lastStopIsDestination == other.isLastStopIsDestination() &&
            ((this.origin==null && other.getOrigin()==null) || 
             (this.origin!=null &&
              this.origin.equals(other.getOrigin()))) &&
            ((this.destination==null && other.getDestination()==null) || 
             (this.destination!=null &&
              this.destination.equals(other.getDestination()))) &&
            ((this.originTimeZone==null && other.getOriginTimeZone()==null) || 
             (this.originTimeZone!=null &&
              this.originTimeZone.equals(other.getOriginTimeZone()))) &&
            ((this.destinationTimeZone==null && other.getDestinationTimeZone()==null) || 
             (this.destinationTimeZone!=null &&
              this.destinationTimeZone.equals(other.getDestinationTimeZone()))) &&
            ((this.userDefinedField1==null && other.getUserDefinedField1()==null) || 
             (this.userDefinedField1!=null &&
              this.userDefinedField1.equals(other.getUserDefinedField1()))) &&
            ((this.userDefinedField2==null && other.getUserDefinedField2()==null) || 
             (this.userDefinedField2!=null &&
              this.userDefinedField2.equals(other.getUserDefinedField2()))) &&
            ((this.userDefinedField3==null && other.getUserDefinedField3()==null) || 
             (this.userDefinedField3!=null &&
              this.userDefinedField3.equals(other.getUserDefinedField3()))) &&
            this.color == other.getColor() &&
            this.isDeliveryRoute == other.isIsDeliveryRoute() &&
            ((this.driver1Name==null && other.getDriver1Name()==null) || 
             (this.driver1Name!=null &&
              this.driver1Name.equals(other.getDriver1Name()))) &&
            ((this.driver2Name==null && other.getDriver2Name()==null) || 
             (this.driver2Name!=null &&
              this.driver2Name.equals(other.getDriver2Name()))) &&
            ((this.preRouteSurveyIdentity==null && other.getPreRouteSurveyIdentity()==null) || 
             (this.preRouteSurveyIdentity!=null &&
              this.preRouteSurveyIdentity.equals(other.getPreRouteSurveyIdentity()))) &&
            ((this.postRouteSurveyIdentity==null && other.getPostRouteSurveyIdentity()==null) || 
             (this.postRouteSurveyIdentity!=null &&
              this.postRouteSurveyIdentity.equals(other.getPostRouteSurveyIdentity()))) &&
            ((this.stops==null && other.getStops()==null) || 
             (this.stops!=null &&
              java.util.Arrays.equals(this.stops, other.getStops())));
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
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += getInternalRouteID();
        _hashCode += (isAssigned() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isOutOfSequence() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getSeal() != null) {
            _hashCode += getSeal().hashCode();
        }
        _hashCode += new Double(getPlannedDistance()).hashCode();
        _hashCode += new Double(getProjectedDistance()).hashCode();
        _hashCode += new Double(getActualDistance()).hashCode();
        if (getPlannedStart() != null) {
            _hashCode += getPlannedStart().hashCode();
        }
        if (getPlannedOriginDeparture() != null) {
            _hashCode += getPlannedOriginDeparture().hashCode();
        }
        if (getPlannedDestinationArrival() != null) {
            _hashCode += getPlannedDestinationArrival().hashCode();
        }
        if (getPlannedComplete() != null) {
            _hashCode += getPlannedComplete().hashCode();
        }
        if (getProjectedOriginDeparture() != null) {
            _hashCode += getProjectedOriginDeparture().hashCode();
        }
        if (getProjectedDestinationArrival() != null) {
            _hashCode += getProjectedDestinationArrival().hashCode();
        }
        if (getProjectedComplete() != null) {
            _hashCode += getProjectedComplete().hashCode();
        }
        if (getActualStart() != null) {
            _hashCode += getActualStart().hashCode();
        }
        if (getActualOriginDeparture() != null) {
            _hashCode += getActualOriginDeparture().hashCode();
        }
        if (getActualDestinationArrival() != null) {
            _hashCode += getActualDestinationArrival().hashCode();
        }
        if (getActualComplete() != null) {
            _hashCode += getActualComplete().hashCode();
        }
        _hashCode += new Double(getPlannedCost()).hashCode();
        _hashCode += new Double(getActualCost()).hashCode();
        if (getDispatcherID() != null) {
            _hashCode += getDispatcherID().hashCode();
        }
        if (getGroup() != null) {
            _hashCode += getGroup().hashCode();
        }
        if (getScenario() != null) {
            _hashCode += getScenario().hashCode();
        }
        _hashCode += (isWirelessLoaded() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInProgress() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isComplete() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getGpsUnitID() != null) {
            _hashCode += getGpsUnitID().hashCode();
        }
        _hashCode += getNumberOfStops();
        _hashCode += getNumberOfStopsDelivered();
        _hashCode += getNumberOfStopsCancelled();
        if (getPositionText() != null) {
            _hashCode += getPositionText().hashCode();
        }
        _hashCode += getPositionLatitude();
        _hashCode += getPositionLongitude();
        _hashCode += getDelayMinutes();
        if (getDelayType() != null) {
            _hashCode += getDelayType().hashCode();
        }
        _hashCode += getPostDelayMinutes();
        if (getPostDelayType() != null) {
            _hashCode += getPostDelayType().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getRouteType() != null) {
            _hashCode += getRouteType().hashCode();
        }
        _hashCode += (isLastStopIsDestination() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOrigin() != null) {
            _hashCode += getOrigin().hashCode();
        }
        if (getDestination() != null) {
            _hashCode += getDestination().hashCode();
        }
        if (getOriginTimeZone() != null) {
            _hashCode += getOriginTimeZone().hashCode();
        }
        if (getDestinationTimeZone() != null) {
            _hashCode += getDestinationTimeZone().hashCode();
        }
        if (getUserDefinedField1() != null) {
            _hashCode += getUserDefinedField1().hashCode();
        }
        if (getUserDefinedField2() != null) {
            _hashCode += getUserDefinedField2().hashCode();
        }
        if (getUserDefinedField3() != null) {
            _hashCode += getUserDefinedField3().hashCode();
        }
        _hashCode += getColor();
        _hashCode += (isIsDeliveryRoute() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDriver1Name() != null) {
            _hashCode += getDriver1Name().hashCode();
        }
        if (getDriver2Name() != null) {
            _hashCode += getDriver2Name().hashCode();
        }
        if (getPreRouteSurveyIdentity() != null) {
            _hashCode += getPreRouteSurveyIdentity().hashCode();
        }
        if (getPostRouteSurveyIdentity() != null) {
            _hashCode += getPostRouteSurveyIdentity().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Route.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Route"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("internalRouteID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "internalRouteID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assigned");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "assigned"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outOfSequence");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "outOfSequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("seal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "seal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plannedDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("projectedDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "projectedDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plannedStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plannedOriginDeparture");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedOriginDeparture"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plannedDestinationArrival");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedDestinationArrival"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plannedComplete");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedComplete"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("projectedOriginDeparture");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "projectedOriginDeparture"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("projectedDestinationArrival");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "projectedDestinationArrival"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("projectedComplete");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "projectedComplete"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualOriginDeparture");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualOriginDeparture"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDestinationArrival");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDestinationArrival"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualComplete");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualComplete"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plannedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dispatcherID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dispatcherID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "group"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scenario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "scenario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wirelessLoaded");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "wirelessLoaded"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inProgress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "inProgress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("complete");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "complete"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gpsUnitID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "gpsUnitID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfStops");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "numberOfStops"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfStopsDelivered");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "numberOfStopsDelivered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfStopsCancelled");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "numberOfStopsCancelled"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionLatitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionLatitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionLongitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionLongitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("delayMinutes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "delayMinutes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("delayType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "delayType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postDelayMinutes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "postDelayMinutes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postDelayType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "postDelayType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastStopIsDestination");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lastStopIsDestination"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("originTimeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "originTimeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneValue"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationTimeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "destinationTimeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneValue"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("color");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "color"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isDeliveryRoute");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "isDeliveryRoute"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driver1Name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driver1Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driver2Name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driver2Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preRouteSurveyIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preRouteSurveyIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SurveyIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postRouteSurveyIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "postRouteSurveyIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SurveyIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stops");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stops"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Stop"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
