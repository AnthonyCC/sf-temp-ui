/**
 * StopCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class StopCriteria  implements java.io.Serializable {
    private java.lang.String regionID;

    private java.util.Date routeDate;

    private java.util.Date routeDateStart;

    private java.util.Date routeDateEnd;

    private java.lang.String routeID;

    private java.lang.String locationType;

    private java.lang.String locationID;

    private java.lang.String locationName;

    private com.freshdirect.routing.proxy.stub.transportation.StopType stopType;

    private java.lang.Boolean undeliverable;

    private java.lang.String orderNumber;

    private java.lang.String referenceNumber;

    private java.lang.String phoneNumber;

    private java.lang.String postalCode;

    private java.lang.String driver1Name;

    private java.lang.String driver2Name;

    private java.util.Date plannedArrivalDateStart;

    private java.util.Date plannedArrivalDateEnd;

    private boolean activeOrdersOnly;

    public StopCriteria() {
    }

    public StopCriteria(
           java.lang.String regionID,
           java.util.Date routeDate,
           java.util.Date routeDateStart,
           java.util.Date routeDateEnd,
           java.lang.String routeID,
           java.lang.String locationType,
           java.lang.String locationID,
           java.lang.String locationName,
           com.freshdirect.routing.proxy.stub.transportation.StopType stopType,
           java.lang.Boolean undeliverable,
           java.lang.String orderNumber,
           java.lang.String referenceNumber,
           java.lang.String phoneNumber,
           java.lang.String postalCode,
           java.lang.String driver1Name,
           java.lang.String driver2Name,
           java.util.Date plannedArrivalDateStart,
           java.util.Date plannedArrivalDateEnd,
           boolean activeOrdersOnly) {
           this.regionID = regionID;
           this.routeDate = routeDate;
           this.routeDateStart = routeDateStart;
           this.routeDateEnd = routeDateEnd;
           this.routeID = routeID;
           this.locationType = locationType;
           this.locationID = locationID;
           this.locationName = locationName;
           this.stopType = stopType;
           this.undeliverable = undeliverable;
           this.orderNumber = orderNumber;
           this.referenceNumber = referenceNumber;
           this.phoneNumber = phoneNumber;
           this.postalCode = postalCode;
           this.driver1Name = driver1Name;
           this.driver2Name = driver2Name;
           this.plannedArrivalDateStart = plannedArrivalDateStart;
           this.plannedArrivalDateEnd = plannedArrivalDateEnd;
           this.activeOrdersOnly = activeOrdersOnly;
    }


    /**
     * Gets the regionID value for this StopCriteria.
     * 
     * @return regionID
     */
    public java.lang.String getRegionID() {
        return regionID;
    }


    /**
     * Sets the regionID value for this StopCriteria.
     * 
     * @param regionID
     */
    public void setRegionID(java.lang.String regionID) {
        this.regionID = regionID;
    }


    /**
     * Gets the routeDate value for this StopCriteria.
     * 
     * @return routeDate
     */
    public java.util.Date getRouteDate() {
        return routeDate;
    }


    /**
     * Sets the routeDate value for this StopCriteria.
     * 
     * @param routeDate
     */
    public void setRouteDate(java.util.Date routeDate) {
        this.routeDate = routeDate;
    }


    /**
     * Gets the routeDateStart value for this StopCriteria.
     * 
     * @return routeDateStart
     */
    public java.util.Date getRouteDateStart() {
        return routeDateStart;
    }


    /**
     * Sets the routeDateStart value for this StopCriteria.
     * 
     * @param routeDateStart
     */
    public void setRouteDateStart(java.util.Date routeDateStart) {
        this.routeDateStart = routeDateStart;
    }


    /**
     * Gets the routeDateEnd value for this StopCriteria.
     * 
     * @return routeDateEnd
     */
    public java.util.Date getRouteDateEnd() {
        return routeDateEnd;
    }


    /**
     * Sets the routeDateEnd value for this StopCriteria.
     * 
     * @param routeDateEnd
     */
    public void setRouteDateEnd(java.util.Date routeDateEnd) {
        this.routeDateEnd = routeDateEnd;
    }


    /**
     * Gets the routeID value for this StopCriteria.
     * 
     * @return routeID
     */
    public java.lang.String getRouteID() {
        return routeID;
    }


    /**
     * Sets the routeID value for this StopCriteria.
     * 
     * @param routeID
     */
    public void setRouteID(java.lang.String routeID) {
        this.routeID = routeID;
    }


    /**
     * Gets the locationType value for this StopCriteria.
     * 
     * @return locationType
     */
    public java.lang.String getLocationType() {
        return locationType;
    }


    /**
     * Sets the locationType value for this StopCriteria.
     * 
     * @param locationType
     */
    public void setLocationType(java.lang.String locationType) {
        this.locationType = locationType;
    }


    /**
     * Gets the locationID value for this StopCriteria.
     * 
     * @return locationID
     */
    public java.lang.String getLocationID() {
        return locationID;
    }


    /**
     * Sets the locationID value for this StopCriteria.
     * 
     * @param locationID
     */
    public void setLocationID(java.lang.String locationID) {
        this.locationID = locationID;
    }


    /**
     * Gets the locationName value for this StopCriteria.
     * 
     * @return locationName
     */
    public java.lang.String getLocationName() {
        return locationName;
    }


    /**
     * Sets the locationName value for this StopCriteria.
     * 
     * @param locationName
     */
    public void setLocationName(java.lang.String locationName) {
        this.locationName = locationName;
    }


    /**
     * Gets the stopType value for this StopCriteria.
     * 
     * @return stopType
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopType getStopType() {
        return stopType;
    }


    /**
     * Sets the stopType value for this StopCriteria.
     * 
     * @param stopType
     */
    public void setStopType(com.freshdirect.routing.proxy.stub.transportation.StopType stopType) {
        this.stopType = stopType;
    }


    /**
     * Gets the undeliverable value for this StopCriteria.
     * 
     * @return undeliverable
     */
    public java.lang.Boolean getUndeliverable() {
        return undeliverable;
    }


    /**
     * Sets the undeliverable value for this StopCriteria.
     * 
     * @param undeliverable
     */
    public void setUndeliverable(java.lang.Boolean undeliverable) {
        this.undeliverable = undeliverable;
    }


    /**
     * Gets the orderNumber value for this StopCriteria.
     * 
     * @return orderNumber
     */
    public java.lang.String getOrderNumber() {
        return orderNumber;
    }


    /**
     * Sets the orderNumber value for this StopCriteria.
     * 
     * @param orderNumber
     */
    public void setOrderNumber(java.lang.String orderNumber) {
        this.orderNumber = orderNumber;
    }


    /**
     * Gets the referenceNumber value for this StopCriteria.
     * 
     * @return referenceNumber
     */
    public java.lang.String getReferenceNumber() {
        return referenceNumber;
    }


    /**
     * Sets the referenceNumber value for this StopCriteria.
     * 
     * @param referenceNumber
     */
    public void setReferenceNumber(java.lang.String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


    /**
     * Gets the phoneNumber value for this StopCriteria.
     * 
     * @return phoneNumber
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * Sets the phoneNumber value for this StopCriteria.
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Gets the postalCode value for this StopCriteria.
     * 
     * @return postalCode
     */
    public java.lang.String getPostalCode() {
        return postalCode;
    }


    /**
     * Sets the postalCode value for this StopCriteria.
     * 
     * @param postalCode
     */
    public void setPostalCode(java.lang.String postalCode) {
        this.postalCode = postalCode;
    }


    /**
     * Gets the driver1Name value for this StopCriteria.
     * 
     * @return driver1Name
     */
    public java.lang.String getDriver1Name() {
        return driver1Name;
    }


    /**
     * Sets the driver1Name value for this StopCriteria.
     * 
     * @param driver1Name
     */
    public void setDriver1Name(java.lang.String driver1Name) {
        this.driver1Name = driver1Name;
    }


    /**
     * Gets the driver2Name value for this StopCriteria.
     * 
     * @return driver2Name
     */
    public java.lang.String getDriver2Name() {
        return driver2Name;
    }


    /**
     * Sets the driver2Name value for this StopCriteria.
     * 
     * @param driver2Name
     */
    public void setDriver2Name(java.lang.String driver2Name) {
        this.driver2Name = driver2Name;
    }


    /**
     * Gets the plannedArrivalDateStart value for this StopCriteria.
     * 
     * @return plannedArrivalDateStart
     */
    public java.util.Date getPlannedArrivalDateStart() {
        return plannedArrivalDateStart;
    }


    /**
     * Sets the plannedArrivalDateStart value for this StopCriteria.
     * 
     * @param plannedArrivalDateStart
     */
    public void setPlannedArrivalDateStart(java.util.Date plannedArrivalDateStart) {
        this.plannedArrivalDateStart = plannedArrivalDateStart;
    }


    /**
     * Gets the plannedArrivalDateEnd value for this StopCriteria.
     * 
     * @return plannedArrivalDateEnd
     */
    public java.util.Date getPlannedArrivalDateEnd() {
        return plannedArrivalDateEnd;
    }


    /**
     * Sets the plannedArrivalDateEnd value for this StopCriteria.
     * 
     * @param plannedArrivalDateEnd
     */
    public void setPlannedArrivalDateEnd(java.util.Date plannedArrivalDateEnd) {
        this.plannedArrivalDateEnd = plannedArrivalDateEnd;
    }


    /**
     * Gets the activeOrdersOnly value for this StopCriteria.
     * 
     * @return activeOrdersOnly
     */
    public boolean isActiveOrdersOnly() {
        return activeOrdersOnly;
    }


    /**
     * Sets the activeOrdersOnly value for this StopCriteria.
     * 
     * @param activeOrdersOnly
     */
    public void setActiveOrdersOnly(boolean activeOrdersOnly) {
        this.activeOrdersOnly = activeOrdersOnly;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StopCriteria)) return false;
        StopCriteria other = (StopCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.regionID==null && other.getRegionID()==null) || 
             (this.regionID!=null &&
              this.regionID.equals(other.getRegionID()))) &&
            ((this.routeDate==null && other.getRouteDate()==null) || 
             (this.routeDate!=null &&
              this.routeDate.equals(other.getRouteDate()))) &&
            ((this.routeDateStart==null && other.getRouteDateStart()==null) || 
             (this.routeDateStart!=null &&
              this.routeDateStart.equals(other.getRouteDateStart()))) &&
            ((this.routeDateEnd==null && other.getRouteDateEnd()==null) || 
             (this.routeDateEnd!=null &&
              this.routeDateEnd.equals(other.getRouteDateEnd()))) &&
            ((this.routeID==null && other.getRouteID()==null) || 
             (this.routeID!=null &&
              this.routeID.equals(other.getRouteID()))) &&
            ((this.locationType==null && other.getLocationType()==null) || 
             (this.locationType!=null &&
              this.locationType.equals(other.getLocationType()))) &&
            ((this.locationID==null && other.getLocationID()==null) || 
             (this.locationID!=null &&
              this.locationID.equals(other.getLocationID()))) &&
            ((this.locationName==null && other.getLocationName()==null) || 
             (this.locationName!=null &&
              this.locationName.equals(other.getLocationName()))) &&
            ((this.stopType==null && other.getStopType()==null) || 
             (this.stopType!=null &&
              this.stopType.equals(other.getStopType()))) &&
            ((this.undeliverable==null && other.getUndeliverable()==null) || 
             (this.undeliverable!=null &&
              this.undeliverable.equals(other.getUndeliverable()))) &&
            ((this.orderNumber==null && other.getOrderNumber()==null) || 
             (this.orderNumber!=null &&
              this.orderNumber.equals(other.getOrderNumber()))) &&
            ((this.referenceNumber==null && other.getReferenceNumber()==null) || 
             (this.referenceNumber!=null &&
              this.referenceNumber.equals(other.getReferenceNumber()))) &&
            ((this.phoneNumber==null && other.getPhoneNumber()==null) || 
             (this.phoneNumber!=null &&
              this.phoneNumber.equals(other.getPhoneNumber()))) &&
            ((this.postalCode==null && other.getPostalCode()==null) || 
             (this.postalCode!=null &&
              this.postalCode.equals(other.getPostalCode()))) &&
            ((this.driver1Name==null && other.getDriver1Name()==null) || 
             (this.driver1Name!=null &&
              this.driver1Name.equals(other.getDriver1Name()))) &&
            ((this.driver2Name==null && other.getDriver2Name()==null) || 
             (this.driver2Name!=null &&
              this.driver2Name.equals(other.getDriver2Name()))) &&
            ((this.plannedArrivalDateStart==null && other.getPlannedArrivalDateStart()==null) || 
             (this.plannedArrivalDateStart!=null &&
              this.plannedArrivalDateStart.equals(other.getPlannedArrivalDateStart()))) &&
            ((this.plannedArrivalDateEnd==null && other.getPlannedArrivalDateEnd()==null) || 
             (this.plannedArrivalDateEnd!=null &&
              this.plannedArrivalDateEnd.equals(other.getPlannedArrivalDateEnd()))) &&
            this.activeOrdersOnly == other.isActiveOrdersOnly();
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
        if (getRegionID() != null) {
            _hashCode += getRegionID().hashCode();
        }
        if (getRouteDate() != null) {
            _hashCode += getRouteDate().hashCode();
        }
        if (getRouteDateStart() != null) {
            _hashCode += getRouteDateStart().hashCode();
        }
        if (getRouteDateEnd() != null) {
            _hashCode += getRouteDateEnd().hashCode();
        }
        if (getRouteID() != null) {
            _hashCode += getRouteID().hashCode();
        }
        if (getLocationType() != null) {
            _hashCode += getLocationType().hashCode();
        }
        if (getLocationID() != null) {
            _hashCode += getLocationID().hashCode();
        }
        if (getLocationName() != null) {
            _hashCode += getLocationName().hashCode();
        }
        if (getStopType() != null) {
            _hashCode += getStopType().hashCode();
        }
        if (getUndeliverable() != null) {
            _hashCode += getUndeliverable().hashCode();
        }
        if (getOrderNumber() != null) {
            _hashCode += getOrderNumber().hashCode();
        }
        if (getReferenceNumber() != null) {
            _hashCode += getReferenceNumber().hashCode();
        }
        if (getPhoneNumber() != null) {
            _hashCode += getPhoneNumber().hashCode();
        }
        if (getPostalCode() != null) {
            _hashCode += getPostalCode().hashCode();
        }
        if (getDriver1Name() != null) {
            _hashCode += getDriver1Name().hashCode();
        }
        if (getDriver2Name() != null) {
            _hashCode += getDriver2Name().hashCode();
        }
        if (getPlannedArrivalDateStart() != null) {
            _hashCode += getPlannedArrivalDateStart().hashCode();
        }
        if (getPlannedArrivalDateEnd() != null) {
            _hashCode += getPlannedArrivalDateEnd().hashCode();
        }
        _hashCode += (isActiveOrdersOnly() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StopCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeDateStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeDateStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeDateEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeDateEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
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
        elemField.setFieldName("locationType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locationType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("stopType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("undeliverable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "undeliverable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "referenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phoneNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "phoneNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postalCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "postalCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("plannedArrivalDateStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedArrivalDateStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plannedArrivalDateEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedArrivalDateEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeOrdersOnly");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "activeOrdersOnly"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
