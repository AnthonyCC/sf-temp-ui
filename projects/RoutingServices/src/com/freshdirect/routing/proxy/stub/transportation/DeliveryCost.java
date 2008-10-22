/**
 * DeliveryCost.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DeliveryCost  implements java.io.Serializable {
    private int routeId;

    private int stopSequence;

    private org.apache.axis.types.Time waveStartTime;

    private int additionalRunTime;

    private int additionalDistance;

    private int totalTravelTime;

    private int totalWaitTime;

    private int totalDistance;

    private int totalServiceTime;

    private int totalRunTime;

    private int totalQuantity;

    private int prefRunTime;

    private int maxRunTime;

    private int capacity;

    private boolean newRoute;

    private boolean available;

    private int regularHourlyWage;

    private int overtimeHourlyWage;

    private int costPerMile;

    private int fixedRouteSetupCost;

    private int regularWageDurationSeconds;

    private int totalRouteCost;

    private int additionalStopCost;

    private boolean filtered;

    private boolean missedTW;

    private int waveOrdersTaken;

    private int waveQuantityOrdered;

    private int waveVehicles;

    private int waveVehiclesInUse;

    private java.lang.String unavailabilityReason;

    private int totalPUQuantity;

    public DeliveryCost() {
    }

    public DeliveryCost(
           int routeId,
           int stopSequence,
           org.apache.axis.types.Time waveStartTime,
           int additionalRunTime,
           int additionalDistance,
           int totalTravelTime,
           int totalWaitTime,
           int totalDistance,
           int totalServiceTime,
           int totalRunTime,
           int totalQuantity,
           int prefRunTime,
           int maxRunTime,
           int capacity,
           boolean newRoute,
           boolean available,
           int regularHourlyWage,
           int overtimeHourlyWage,
           int costPerMile,
           int fixedRouteSetupCost,
           int regularWageDurationSeconds,
           int totalRouteCost,
           int additionalStopCost,
           boolean filtered,
           boolean missedTW,
           int waveOrdersTaken,
           int waveQuantityOrdered,
           int waveVehicles,
           int waveVehiclesInUse,
           java.lang.String unavailabilityReason,
           int totalPUQuantity) {
           this.routeId = routeId;
           this.stopSequence = stopSequence;
           this.waveStartTime = waveStartTime;
           this.additionalRunTime = additionalRunTime;
           this.additionalDistance = additionalDistance;
           this.totalTravelTime = totalTravelTime;
           this.totalWaitTime = totalWaitTime;
           this.totalDistance = totalDistance;
           this.totalServiceTime = totalServiceTime;
           this.totalRunTime = totalRunTime;
           this.totalQuantity = totalQuantity;
           this.prefRunTime = prefRunTime;
           this.maxRunTime = maxRunTime;
           this.capacity = capacity;
           this.newRoute = newRoute;
           this.available = available;
           this.regularHourlyWage = regularHourlyWage;
           this.overtimeHourlyWage = overtimeHourlyWage;
           this.costPerMile = costPerMile;
           this.fixedRouteSetupCost = fixedRouteSetupCost;
           this.regularWageDurationSeconds = regularWageDurationSeconds;
           this.totalRouteCost = totalRouteCost;
           this.additionalStopCost = additionalStopCost;
           this.filtered = filtered;
           this.missedTW = missedTW;
           this.waveOrdersTaken = waveOrdersTaken;
           this.waveQuantityOrdered = waveQuantityOrdered;
           this.waveVehicles = waveVehicles;
           this.waveVehiclesInUse = waveVehiclesInUse;
           this.unavailabilityReason = unavailabilityReason;
           this.totalPUQuantity = totalPUQuantity;
    }


    /**
     * Gets the routeId value for this DeliveryCost.
     * 
     * @return routeId
     */
    public int getRouteId() {
        return routeId;
    }


    /**
     * Sets the routeId value for this DeliveryCost.
     * 
     * @param routeId
     */
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }


    /**
     * Gets the stopSequence value for this DeliveryCost.
     * 
     * @return stopSequence
     */
    public int getStopSequence() {
        return stopSequence;
    }


    /**
     * Sets the stopSequence value for this DeliveryCost.
     * 
     * @param stopSequence
     */
    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }


    /**
     * Gets the waveStartTime value for this DeliveryCost.
     * 
     * @return waveStartTime
     */
    public org.apache.axis.types.Time getWaveStartTime() {
        return waveStartTime;
    }


    /**
     * Sets the waveStartTime value for this DeliveryCost.
     * 
     * @param waveStartTime
     */
    public void setWaveStartTime(org.apache.axis.types.Time waveStartTime) {
        this.waveStartTime = waveStartTime;
    }


    /**
     * Gets the additionalRunTime value for this DeliveryCost.
     * 
     * @return additionalRunTime
     */
    public int getAdditionalRunTime() {
        return additionalRunTime;
    }


    /**
     * Sets the additionalRunTime value for this DeliveryCost.
     * 
     * @param additionalRunTime
     */
    public void setAdditionalRunTime(int additionalRunTime) {
        this.additionalRunTime = additionalRunTime;
    }


    /**
     * Gets the additionalDistance value for this DeliveryCost.
     * 
     * @return additionalDistance
     */
    public int getAdditionalDistance() {
        return additionalDistance;
    }


    /**
     * Sets the additionalDistance value for this DeliveryCost.
     * 
     * @param additionalDistance
     */
    public void setAdditionalDistance(int additionalDistance) {
        this.additionalDistance = additionalDistance;
    }


    /**
     * Gets the totalTravelTime value for this DeliveryCost.
     * 
     * @return totalTravelTime
     */
    public int getTotalTravelTime() {
        return totalTravelTime;
    }


    /**
     * Sets the totalTravelTime value for this DeliveryCost.
     * 
     * @param totalTravelTime
     */
    public void setTotalTravelTime(int totalTravelTime) {
        this.totalTravelTime = totalTravelTime;
    }


    /**
     * Gets the totalWaitTime value for this DeliveryCost.
     * 
     * @return totalWaitTime
     */
    public int getTotalWaitTime() {
        return totalWaitTime;
    }


    /**
     * Sets the totalWaitTime value for this DeliveryCost.
     * 
     * @param totalWaitTime
     */
    public void setTotalWaitTime(int totalWaitTime) {
        this.totalWaitTime = totalWaitTime;
    }


    /**
     * Gets the totalDistance value for this DeliveryCost.
     * 
     * @return totalDistance
     */
    public int getTotalDistance() {
        return totalDistance;
    }


    /**
     * Sets the totalDistance value for this DeliveryCost.
     * 
     * @param totalDistance
     */
    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }


    /**
     * Gets the totalServiceTime value for this DeliveryCost.
     * 
     * @return totalServiceTime
     */
    public int getTotalServiceTime() {
        return totalServiceTime;
    }


    /**
     * Sets the totalServiceTime value for this DeliveryCost.
     * 
     * @param totalServiceTime
     */
    public void setTotalServiceTime(int totalServiceTime) {
        this.totalServiceTime = totalServiceTime;
    }


    /**
     * Gets the totalRunTime value for this DeliveryCost.
     * 
     * @return totalRunTime
     */
    public int getTotalRunTime() {
        return totalRunTime;
    }


    /**
     * Sets the totalRunTime value for this DeliveryCost.
     * 
     * @param totalRunTime
     */
    public void setTotalRunTime(int totalRunTime) {
        this.totalRunTime = totalRunTime;
    }


    /**
     * Gets the totalQuantity value for this DeliveryCost.
     * 
     * @return totalQuantity
     */
    public int getTotalQuantity() {
        return totalQuantity;
    }


    /**
     * Sets the totalQuantity value for this DeliveryCost.
     * 
     * @param totalQuantity
     */
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }


    /**
     * Gets the prefRunTime value for this DeliveryCost.
     * 
     * @return prefRunTime
     */
    public int getPrefRunTime() {
        return prefRunTime;
    }


    /**
     * Sets the prefRunTime value for this DeliveryCost.
     * 
     * @param prefRunTime
     */
    public void setPrefRunTime(int prefRunTime) {
        this.prefRunTime = prefRunTime;
    }


    /**
     * Gets the maxRunTime value for this DeliveryCost.
     * 
     * @return maxRunTime
     */
    public int getMaxRunTime() {
        return maxRunTime;
    }


    /**
     * Sets the maxRunTime value for this DeliveryCost.
     * 
     * @param maxRunTime
     */
    public void setMaxRunTime(int maxRunTime) {
        this.maxRunTime = maxRunTime;
    }


    /**
     * Gets the capacity value for this DeliveryCost.
     * 
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }


    /**
     * Sets the capacity value for this DeliveryCost.
     * 
     * @param capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    /**
     * Gets the newRoute value for this DeliveryCost.
     * 
     * @return newRoute
     */
    public boolean isNewRoute() {
        return newRoute;
    }


    /**
     * Sets the newRoute value for this DeliveryCost.
     * 
     * @param newRoute
     */
    public void setNewRoute(boolean newRoute) {
        this.newRoute = newRoute;
    }


    /**
     * Gets the available value for this DeliveryCost.
     * 
     * @return available
     */
    public boolean isAvailable() {
        return available;
    }


    /**
     * Sets the available value for this DeliveryCost.
     * 
     * @param available
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }


    /**
     * Gets the regularHourlyWage value for this DeliveryCost.
     * 
     * @return regularHourlyWage
     */
    public int getRegularHourlyWage() {
        return regularHourlyWage;
    }


    /**
     * Sets the regularHourlyWage value for this DeliveryCost.
     * 
     * @param regularHourlyWage
     */
    public void setRegularHourlyWage(int regularHourlyWage) {
        this.regularHourlyWage = regularHourlyWage;
    }


    /**
     * Gets the overtimeHourlyWage value for this DeliveryCost.
     * 
     * @return overtimeHourlyWage
     */
    public int getOvertimeHourlyWage() {
        return overtimeHourlyWage;
    }


    /**
     * Sets the overtimeHourlyWage value for this DeliveryCost.
     * 
     * @param overtimeHourlyWage
     */
    public void setOvertimeHourlyWage(int overtimeHourlyWage) {
        this.overtimeHourlyWage = overtimeHourlyWage;
    }


    /**
     * Gets the costPerMile value for this DeliveryCost.
     * 
     * @return costPerMile
     */
    public int getCostPerMile() {
        return costPerMile;
    }


    /**
     * Sets the costPerMile value for this DeliveryCost.
     * 
     * @param costPerMile
     */
    public void setCostPerMile(int costPerMile) {
        this.costPerMile = costPerMile;
    }


    /**
     * Gets the fixedRouteSetupCost value for this DeliveryCost.
     * 
     * @return fixedRouteSetupCost
     */
    public int getFixedRouteSetupCost() {
        return fixedRouteSetupCost;
    }


    /**
     * Sets the fixedRouteSetupCost value for this DeliveryCost.
     * 
     * @param fixedRouteSetupCost
     */
    public void setFixedRouteSetupCost(int fixedRouteSetupCost) {
        this.fixedRouteSetupCost = fixedRouteSetupCost;
    }


    /**
     * Gets the regularWageDurationSeconds value for this DeliveryCost.
     * 
     * @return regularWageDurationSeconds
     */
    public int getRegularWageDurationSeconds() {
        return regularWageDurationSeconds;
    }


    /**
     * Sets the regularWageDurationSeconds value for this DeliveryCost.
     * 
     * @param regularWageDurationSeconds
     */
    public void setRegularWageDurationSeconds(int regularWageDurationSeconds) {
        this.regularWageDurationSeconds = regularWageDurationSeconds;
    }


    /**
     * Gets the totalRouteCost value for this DeliveryCost.
     * 
     * @return totalRouteCost
     */
    public int getTotalRouteCost() {
        return totalRouteCost;
    }


    /**
     * Sets the totalRouteCost value for this DeliveryCost.
     * 
     * @param totalRouteCost
     */
    public void setTotalRouteCost(int totalRouteCost) {
        this.totalRouteCost = totalRouteCost;
    }


    /**
     * Gets the additionalStopCost value for this DeliveryCost.
     * 
     * @return additionalStopCost
     */
    public int getAdditionalStopCost() {
        return additionalStopCost;
    }


    /**
     * Sets the additionalStopCost value for this DeliveryCost.
     * 
     * @param additionalStopCost
     */
    public void setAdditionalStopCost(int additionalStopCost) {
        this.additionalStopCost = additionalStopCost;
    }


    /**
     * Gets the filtered value for this DeliveryCost.
     * 
     * @return filtered
     */
    public boolean isFiltered() {
        return filtered;
    }


    /**
     * Sets the filtered value for this DeliveryCost.
     * 
     * @param filtered
     */
    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }


    /**
     * Gets the missedTW value for this DeliveryCost.
     * 
     * @return missedTW
     */
    public boolean isMissedTW() {
        return missedTW;
    }


    /**
     * Sets the missedTW value for this DeliveryCost.
     * 
     * @param missedTW
     */
    public void setMissedTW(boolean missedTW) {
        this.missedTW = missedTW;
    }


    /**
     * Gets the waveOrdersTaken value for this DeliveryCost.
     * 
     * @return waveOrdersTaken
     */
    public int getWaveOrdersTaken() {
        return waveOrdersTaken;
    }


    /**
     * Sets the waveOrdersTaken value for this DeliveryCost.
     * 
     * @param waveOrdersTaken
     */
    public void setWaveOrdersTaken(int waveOrdersTaken) {
        this.waveOrdersTaken = waveOrdersTaken;
    }


    /**
     * Gets the waveQuantityOrdered value for this DeliveryCost.
     * 
     * @return waveQuantityOrdered
     */
    public int getWaveQuantityOrdered() {
        return waveQuantityOrdered;
    }


    /**
     * Sets the waveQuantityOrdered value for this DeliveryCost.
     * 
     * @param waveQuantityOrdered
     */
    public void setWaveQuantityOrdered(int waveQuantityOrdered) {
        this.waveQuantityOrdered = waveQuantityOrdered;
    }


    /**
     * Gets the waveVehicles value for this DeliveryCost.
     * 
     * @return waveVehicles
     */
    public int getWaveVehicles() {
        return waveVehicles;
    }


    /**
     * Sets the waveVehicles value for this DeliveryCost.
     * 
     * @param waveVehicles
     */
    public void setWaveVehicles(int waveVehicles) {
        this.waveVehicles = waveVehicles;
    }


    /**
     * Gets the waveVehiclesInUse value for this DeliveryCost.
     * 
     * @return waveVehiclesInUse
     */
    public int getWaveVehiclesInUse() {
        return waveVehiclesInUse;
    }


    /**
     * Sets the waveVehiclesInUse value for this DeliveryCost.
     * 
     * @param waveVehiclesInUse
     */
    public void setWaveVehiclesInUse(int waveVehiclesInUse) {
        this.waveVehiclesInUse = waveVehiclesInUse;
    }


    /**
     * Gets the unavailabilityReason value for this DeliveryCost.
     * 
     * @return unavailabilityReason
     */
    public java.lang.String getUnavailabilityReason() {
        return unavailabilityReason;
    }


    /**
     * Sets the unavailabilityReason value for this DeliveryCost.
     * 
     * @param unavailabilityReason
     */
    public void setUnavailabilityReason(java.lang.String unavailabilityReason) {
        this.unavailabilityReason = unavailabilityReason;
    }


    /**
     * Gets the totalPUQuantity value for this DeliveryCost.
     * 
     * @return totalPUQuantity
     */
    public int getTotalPUQuantity() {
        return totalPUQuantity;
    }


    /**
     * Sets the totalPUQuantity value for this DeliveryCost.
     * 
     * @param totalPUQuantity
     */
    public void setTotalPUQuantity(int totalPUQuantity) {
        this.totalPUQuantity = totalPUQuantity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeliveryCost)) return false;
        DeliveryCost other = (DeliveryCost) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.routeId == other.getRouteId() &&
            this.stopSequence == other.getStopSequence() &&
            ((this.waveStartTime==null && other.getWaveStartTime()==null) || 
             (this.waveStartTime!=null &&
              this.waveStartTime.equals(other.getWaveStartTime()))) &&
            this.additionalRunTime == other.getAdditionalRunTime() &&
            this.additionalDistance == other.getAdditionalDistance() &&
            this.totalTravelTime == other.getTotalTravelTime() &&
            this.totalWaitTime == other.getTotalWaitTime() &&
            this.totalDistance == other.getTotalDistance() &&
            this.totalServiceTime == other.getTotalServiceTime() &&
            this.totalRunTime == other.getTotalRunTime() &&
            this.totalQuantity == other.getTotalQuantity() &&
            this.prefRunTime == other.getPrefRunTime() &&
            this.maxRunTime == other.getMaxRunTime() &&
            this.capacity == other.getCapacity() &&
            this.newRoute == other.isNewRoute() &&
            this.available == other.isAvailable() &&
            this.regularHourlyWage == other.getRegularHourlyWage() &&
            this.overtimeHourlyWage == other.getOvertimeHourlyWage() &&
            this.costPerMile == other.getCostPerMile() &&
            this.fixedRouteSetupCost == other.getFixedRouteSetupCost() &&
            this.regularWageDurationSeconds == other.getRegularWageDurationSeconds() &&
            this.totalRouteCost == other.getTotalRouteCost() &&
            this.additionalStopCost == other.getAdditionalStopCost() &&
            this.filtered == other.isFiltered() &&
            this.missedTW == other.isMissedTW() &&
            this.waveOrdersTaken == other.getWaveOrdersTaken() &&
            this.waveQuantityOrdered == other.getWaveQuantityOrdered() &&
            this.waveVehicles == other.getWaveVehicles() &&
            this.waveVehiclesInUse == other.getWaveVehiclesInUse() &&
            ((this.unavailabilityReason==null && other.getUnavailabilityReason()==null) || 
             (this.unavailabilityReason!=null &&
              this.unavailabilityReason.equals(other.getUnavailabilityReason()))) &&
            this.totalPUQuantity == other.getTotalPUQuantity();
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
        _hashCode += getRouteId();
        _hashCode += getStopSequence();
        if (getWaveStartTime() != null) {
            _hashCode += getWaveStartTime().hashCode();
        }
        _hashCode += getAdditionalRunTime();
        _hashCode += getAdditionalDistance();
        _hashCode += getTotalTravelTime();
        _hashCode += getTotalWaitTime();
        _hashCode += getTotalDistance();
        _hashCode += getTotalServiceTime();
        _hashCode += getTotalRunTime();
        _hashCode += getTotalQuantity();
        _hashCode += getPrefRunTime();
        _hashCode += getMaxRunTime();
        _hashCode += getCapacity();
        _hashCode += (isNewRoute() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAvailable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getRegularHourlyWage();
        _hashCode += getOvertimeHourlyWage();
        _hashCode += getCostPerMile();
        _hashCode += getFixedRouteSetupCost();
        _hashCode += getRegularWageDurationSeconds();
        _hashCode += getTotalRouteCost();
        _hashCode += getAdditionalStopCost();
        _hashCode += (isFiltered() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMissedTW() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getWaveOrdersTaken();
        _hashCode += getWaveQuantityOrdered();
        _hashCode += getWaveVehicles();
        _hashCode += getWaveVehiclesInUse();
        if (getUnavailabilityReason() != null) {
            _hashCode += getUnavailabilityReason().hashCode();
        }
        _hashCode += getTotalPUQuantity();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DeliveryCost.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryCost"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopSequence");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopSequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waveStartTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "waveStartTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalRunTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "additionalRunTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "additionalDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalTravelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalTravelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalWaitTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalWaitTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalServiceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalRunTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalRunTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prefRunTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "prefRunTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxRunTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxRunTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("capacity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "capacity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newRoute");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "newRoute"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("available");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "available"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regularHourlyWage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regularHourlyWage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overtimeHourlyWage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "overtimeHourlyWage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("costPerMile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "costPerMile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fixedRouteSetupCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "fixedRouteSetupCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regularWageDurationSeconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regularWageDurationSeconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalRouteCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalRouteCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalStopCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "additionalStopCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filtered");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "filtered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("missedTW");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "missedTW"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waveOrdersTaken");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "waveOrdersTaken"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waveQuantityOrdered");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "waveQuantityOrdered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waveVehicles");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "waveVehicles"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waveVehiclesInUse");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "waveVehiclesInUse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unavailabilityReason");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "unavailabilityReason"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPUQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalPUQuantity"));
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
