/**
 * DeliveryAreaOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DeliveryAreaOrder  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity;

    private java.lang.String orderType;

    private int quantity;

    private int pickupQuantity;

    private boolean confirmed;

    private java.util.Calendar reservedTime;

    private org.apache.axis.types.Time deliveryWindowStart;

    private org.apache.axis.types.Time deliveryWindowEnd;

    private int serviceTime;

    private int latitude;

    private int longitude;

    private java.lang.String locationId;

    private java.lang.String locationType;

    private java.lang.String description;

    private com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[] activities;

    private java.util.Calendar movable;

    private int routeId;

    private int sequenceNumber;

    private int waitSeconds;

    private java.util.Calendar plannedArrivalTime;

    private int distanceTo;

    private int secondsTo;

    private boolean sequenced;

    public DeliveryAreaOrder() {
    }

    public DeliveryAreaOrder(
           com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity,
           java.lang.String orderType,
           int quantity,
           int pickupQuantity,
           boolean confirmed,
           java.util.Calendar reservedTime,
           org.apache.axis.types.Time deliveryWindowStart,
           org.apache.axis.types.Time deliveryWindowEnd,
           int serviceTime,
           int latitude,
           int longitude,
           java.lang.String locationId,
           java.lang.String locationType,
           java.lang.String description,
           com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[] activities,
           java.util.Calendar movable,
           int routeId,
           int sequenceNumber,
           int waitSeconds,
           java.util.Calendar plannedArrivalTime,
           int distanceTo,
           int secondsTo,
           boolean sequenced) {
           this.identity = identity;
           this.orderType = orderType;
           this.quantity = quantity;
           this.pickupQuantity = pickupQuantity;
           this.confirmed = confirmed;
           this.reservedTime = reservedTime;
           this.deliveryWindowStart = deliveryWindowStart;
           this.deliveryWindowEnd = deliveryWindowEnd;
           this.serviceTime = serviceTime;
           this.latitude = latitude;
           this.longitude = longitude;
           this.locationId = locationId;
           this.locationType = locationType;
           this.description = description;
           this.activities = activities;
           this.movable = movable;
           this.routeId = routeId;
           this.sequenceNumber = sequenceNumber;
           this.waitSeconds = waitSeconds;
           this.plannedArrivalTime = plannedArrivalTime;
           this.distanceTo = distanceTo;
           this.secondsTo = secondsTo;
           this.sequenced = sequenced;
    }


    /**
     * Gets the identity value for this DeliveryAreaOrder.
     * 
     * @return identity
     */
    public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity getIdentity() {
        return identity;
    }


    /**
     * Sets the identity value for this DeliveryAreaOrder.
     * 
     * @param identity
     */
    public void setIdentity(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity) {
        this.identity = identity;
    }


    /**
     * Gets the orderType value for this DeliveryAreaOrder.
     * 
     * @return orderType
     */
    public java.lang.String getOrderType() {
        return orderType;
    }


    /**
     * Sets the orderType value for this DeliveryAreaOrder.
     * 
     * @param orderType
     */
    public void setOrderType(java.lang.String orderType) {
        this.orderType = orderType;
    }


    /**
     * Gets the quantity value for this DeliveryAreaOrder.
     * 
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }


    /**
     * Sets the quantity value for this DeliveryAreaOrder.
     * 
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    /**
     * Gets the pickupQuantity value for this DeliveryAreaOrder.
     * 
     * @return pickupQuantity
     */
    public int getPickupQuantity() {
        return pickupQuantity;
    }


    /**
     * Sets the pickupQuantity value for this DeliveryAreaOrder.
     * 
     * @param pickupQuantity
     */
    public void setPickupQuantity(int pickupQuantity) {
        this.pickupQuantity = pickupQuantity;
    }


    /**
     * Gets the confirmed value for this DeliveryAreaOrder.
     * 
     * @return confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }


    /**
     * Sets the confirmed value for this DeliveryAreaOrder.
     * 
     * @param confirmed
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }


    /**
     * Gets the reservedTime value for this DeliveryAreaOrder.
     * 
     * @return reservedTime
     */
    public java.util.Calendar getReservedTime() {
        return reservedTime;
    }


    /**
     * Sets the reservedTime value for this DeliveryAreaOrder.
     * 
     * @param reservedTime
     */
    public void setReservedTime(java.util.Calendar reservedTime) {
        this.reservedTime = reservedTime;
    }


    /**
     * Gets the deliveryWindowStart value for this DeliveryAreaOrder.
     * 
     * @return deliveryWindowStart
     */
    public org.apache.axis.types.Time getDeliveryWindowStart() {
        return deliveryWindowStart;
    }


    /**
     * Sets the deliveryWindowStart value for this DeliveryAreaOrder.
     * 
     * @param deliveryWindowStart
     */
    public void setDeliveryWindowStart(org.apache.axis.types.Time deliveryWindowStart) {
        this.deliveryWindowStart = deliveryWindowStart;
    }


    /**
     * Gets the deliveryWindowEnd value for this DeliveryAreaOrder.
     * 
     * @return deliveryWindowEnd
     */
    public org.apache.axis.types.Time getDeliveryWindowEnd() {
        return deliveryWindowEnd;
    }


    /**
     * Sets the deliveryWindowEnd value for this DeliveryAreaOrder.
     * 
     * @param deliveryWindowEnd
     */
    public void setDeliveryWindowEnd(org.apache.axis.types.Time deliveryWindowEnd) {
        this.deliveryWindowEnd = deliveryWindowEnd;
    }


    /**
     * Gets the serviceTime value for this DeliveryAreaOrder.
     * 
     * @return serviceTime
     */
    public int getServiceTime() {
        return serviceTime;
    }


    /**
     * Sets the serviceTime value for this DeliveryAreaOrder.
     * 
     * @param serviceTime
     */
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }


    /**
     * Gets the latitude value for this DeliveryAreaOrder.
     * 
     * @return latitude
     */
    public int getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude value for this DeliveryAreaOrder.
     * 
     * @param latitude
     */
    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets the longitude value for this DeliveryAreaOrder.
     * 
     * @return longitude
     */
    public int getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude value for this DeliveryAreaOrder.
     * 
     * @param longitude
     */
    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }


    /**
     * Gets the locationId value for this DeliveryAreaOrder.
     * 
     * @return locationId
     */
    public java.lang.String getLocationId() {
        return locationId;
    }


    /**
     * Sets the locationId value for this DeliveryAreaOrder.
     * 
     * @param locationId
     */
    public void setLocationId(java.lang.String locationId) {
        this.locationId = locationId;
    }


    /**
     * Gets the locationType value for this DeliveryAreaOrder.
     * 
     * @return locationType
     */
    public java.lang.String getLocationType() {
        return locationType;
    }


    /**
     * Sets the locationType value for this DeliveryAreaOrder.
     * 
     * @param locationType
     */
    public void setLocationType(java.lang.String locationType) {
        this.locationType = locationType;
    }


    /**
     * Gets the description value for this DeliveryAreaOrder.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this DeliveryAreaOrder.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the activities value for this DeliveryAreaOrder.
     * 
     * @return activities
     */
    public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[] getActivities() {
        return activities;
    }


    /**
     * Sets the activities value for this DeliveryAreaOrder.
     * 
     * @param activities
     */
    public void setActivities(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity[] activities) {
        this.activities = activities;
    }

    public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity getActivities(int i) {
        return this.activities[i];
    }

    public void setActivities(int i, com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderActivity _value) {
        this.activities[i] = _value;
    }


    /**
     * Gets the movable value for this DeliveryAreaOrder.
     * 
     * @return movable
     */
    public java.util.Calendar getMovable() {
        return movable;
    }


    /**
     * Sets the movable value for this DeliveryAreaOrder.
     * 
     * @param movable
     */
    public void setMovable(java.util.Calendar movable) {
        this.movable = movable;
    }


    /**
     * Gets the routeId value for this DeliveryAreaOrder.
     * 
     * @return routeId
     */
    public int getRouteId() {
        return routeId;
    }


    /**
     * Sets the routeId value for this DeliveryAreaOrder.
     * 
     * @param routeId
     */
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }


    /**
     * Gets the sequenceNumber value for this DeliveryAreaOrder.
     * 
     * @return sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }


    /**
     * Sets the sequenceNumber value for this DeliveryAreaOrder.
     * 
     * @param sequenceNumber
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }


    /**
     * Gets the waitSeconds value for this DeliveryAreaOrder.
     * 
     * @return waitSeconds
     */
    public int getWaitSeconds() {
        return waitSeconds;
    }


    /**
     * Sets the waitSeconds value for this DeliveryAreaOrder.
     * 
     * @param waitSeconds
     */
    public void setWaitSeconds(int waitSeconds) {
        this.waitSeconds = waitSeconds;
    }


    /**
     * Gets the plannedArrivalTime value for this DeliveryAreaOrder.
     * 
     * @return plannedArrivalTime
     */
    public java.util.Calendar getPlannedArrivalTime() {
        return plannedArrivalTime;
    }


    /**
     * Sets the plannedArrivalTime value for this DeliveryAreaOrder.
     * 
     * @param plannedArrivalTime
     */
    public void setPlannedArrivalTime(java.util.Calendar plannedArrivalTime) {
        this.plannedArrivalTime = plannedArrivalTime;
    }


    /**
     * Gets the distanceTo value for this DeliveryAreaOrder.
     * 
     * @return distanceTo
     */
    public int getDistanceTo() {
        return distanceTo;
    }


    /**
     * Sets the distanceTo value for this DeliveryAreaOrder.
     * 
     * @param distanceTo
     */
    public void setDistanceTo(int distanceTo) {
        this.distanceTo = distanceTo;
    }


    /**
     * Gets the secondsTo value for this DeliveryAreaOrder.
     * 
     * @return secondsTo
     */
    public int getSecondsTo() {
        return secondsTo;
    }


    /**
     * Sets the secondsTo value for this DeliveryAreaOrder.
     * 
     * @param secondsTo
     */
    public void setSecondsTo(int secondsTo) {
        this.secondsTo = secondsTo;
    }


    /**
     * Gets the sequenced value for this DeliveryAreaOrder.
     * 
     * @return sequenced
     */
    public boolean isSequenced() {
        return sequenced;
    }


    /**
     * Sets the sequenced value for this DeliveryAreaOrder.
     * 
     * @param sequenced
     */
    public void setSequenced(boolean sequenced) {
        this.sequenced = sequenced;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeliveryAreaOrder)) return false;
        DeliveryAreaOrder other = (DeliveryAreaOrder) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identity==null && other.getIdentity()==null) || 
             (this.identity!=null &&
              this.identity.equals(other.getIdentity()))) &&
            ((this.orderType==null && other.getOrderType()==null) || 
             (this.orderType!=null &&
              this.orderType.equals(other.getOrderType()))) &&
            this.quantity == other.getQuantity() &&
            this.pickupQuantity == other.getPickupQuantity() &&
            this.confirmed == other.isConfirmed() &&
            ((this.reservedTime==null && other.getReservedTime()==null) || 
             (this.reservedTime!=null &&
              this.reservedTime.equals(other.getReservedTime()))) &&
            ((this.deliveryWindowStart==null && other.getDeliveryWindowStart()==null) || 
             (this.deliveryWindowStart!=null &&
              this.deliveryWindowStart.equals(other.getDeliveryWindowStart()))) &&
            ((this.deliveryWindowEnd==null && other.getDeliveryWindowEnd()==null) || 
             (this.deliveryWindowEnd!=null &&
              this.deliveryWindowEnd.equals(other.getDeliveryWindowEnd()))) &&
            this.serviceTime == other.getServiceTime() &&
            this.latitude == other.getLatitude() &&
            this.longitude == other.getLongitude() &&
            ((this.locationId==null && other.getLocationId()==null) || 
             (this.locationId!=null &&
              this.locationId.equals(other.getLocationId()))) &&
            ((this.locationType==null && other.getLocationType()==null) || 
             (this.locationType!=null &&
              this.locationType.equals(other.getLocationType()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.activities==null && other.getActivities()==null) || 
             (this.activities!=null &&
              java.util.Arrays.equals(this.activities, other.getActivities()))) &&
            ((this.movable==null && other.getMovable()==null) || 
             (this.movable!=null &&
              this.movable.equals(other.getMovable()))) &&
            this.routeId == other.getRouteId() &&
            this.sequenceNumber == other.getSequenceNumber() &&
            this.waitSeconds == other.getWaitSeconds() &&
            ((this.plannedArrivalTime==null && other.getPlannedArrivalTime()==null) || 
             (this.plannedArrivalTime!=null &&
              this.plannedArrivalTime.equals(other.getPlannedArrivalTime()))) &&
            this.distanceTo == other.getDistanceTo() &&
            this.secondsTo == other.getSecondsTo() &&
            this.sequenced == other.isSequenced();
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
        if (getIdentity() != null) {
            _hashCode += getIdentity().hashCode();
        }
        if (getOrderType() != null) {
            _hashCode += getOrderType().hashCode();
        }
        _hashCode += getQuantity();
        _hashCode += getPickupQuantity();
        _hashCode += (isConfirmed() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getReservedTime() != null) {
            _hashCode += getReservedTime().hashCode();
        }
        if (getDeliveryWindowStart() != null) {
            _hashCode += getDeliveryWindowStart().hashCode();
        }
        if (getDeliveryWindowEnd() != null) {
            _hashCode += getDeliveryWindowEnd().hashCode();
        }
        _hashCode += getServiceTime();
        _hashCode += getLatitude();
        _hashCode += getLongitude();
        if (getLocationId() != null) {
            _hashCode += getLocationId().hashCode();
        }
        if (getLocationType() != null) {
            _hashCode += getLocationType().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getActivities() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActivities());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActivities(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMovable() != null) {
            _hashCode += getMovable().hashCode();
        }
        _hashCode += getRouteId();
        _hashCode += getSequenceNumber();
        _hashCode += getWaitSeconds();
        if (getPlannedArrivalTime() != null) {
            _hashCode += getPlannedArrivalTime().hashCode();
        }
        _hashCode += getDistanceTo();
        _hashCode += getSecondsTo();
        _hashCode += (isSequenced() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DeliveryAreaOrder.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryAreaOrder"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "identity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryAreaOrderIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "quantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pickupQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "pickupQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("confirmed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "confirmed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reservedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "reservedTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryWindowStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryWindowStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryWindowEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryWindowEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "serviceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("locationId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locationId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "locationType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "activities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryAreaOrderActivity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("movable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "movable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sequenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waitSeconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "waitSeconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plannedArrivalTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "plannedArrivalTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distanceTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "distanceTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secondsTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "secondsTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequenced");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sequenced"));
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
