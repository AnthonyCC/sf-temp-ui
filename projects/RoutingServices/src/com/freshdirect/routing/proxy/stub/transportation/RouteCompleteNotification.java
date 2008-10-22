/**
 * RouteCompleteNotification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RouteCompleteNotification  extends com.freshdirect.routing.proxy.stub.transportation.Notification  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.NotificationSource source;

    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private double actualDistance;

    private com.freshdirect.routing.proxy.stub.transportation.DataQualityType distanceDataQuality;

    private java.util.Calendar actualComplete;

    private com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality;

    private java.lang.String postDelayType;

    private int postDelayMinutes;

    public RouteCompleteNotification() {
    }

    public RouteCompleteNotification(
           com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType,
           com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.util.Calendar lockDate,
           com.freshdirect.routing.proxy.stub.transportation.NotificationSource source,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           double actualDistance,
           com.freshdirect.routing.proxy.stub.transportation.DataQualityType distanceDataQuality,
           java.util.Calendar actualComplete,
           com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality,
           java.lang.String postDelayType,
           int postDelayMinutes) {
        super(
            notificationType,
            notificationIdentity,
            recipientIdentity,
            lockIdentity,
            lockDate);
        this.source = source;
        this.routeIdentity = routeIdentity;
        this.actualDistance = actualDistance;
        this.distanceDataQuality = distanceDataQuality;
        this.actualComplete = actualComplete;
        this.timeDataQuality = timeDataQuality;
        this.postDelayType = postDelayType;
        this.postDelayMinutes = postDelayMinutes;
    }


    /**
     * Gets the source value for this RouteCompleteNotification.
     * 
     * @return source
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationSource getSource() {
        return source;
    }


    /**
     * Sets the source value for this RouteCompleteNotification.
     * 
     * @param source
     */
    public void setSource(com.freshdirect.routing.proxy.stub.transportation.NotificationSource source) {
        this.source = source;
    }


    /**
     * Gets the routeIdentity value for this RouteCompleteNotification.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this RouteCompleteNotification.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the actualDistance value for this RouteCompleteNotification.
     * 
     * @return actualDistance
     */
    public double getActualDistance() {
        return actualDistance;
    }


    /**
     * Sets the actualDistance value for this RouteCompleteNotification.
     * 
     * @param actualDistance
     */
    public void setActualDistance(double actualDistance) {
        this.actualDistance = actualDistance;
    }


    /**
     * Gets the distanceDataQuality value for this RouteCompleteNotification.
     * 
     * @return distanceDataQuality
     */
    public com.freshdirect.routing.proxy.stub.transportation.DataQualityType getDistanceDataQuality() {
        return distanceDataQuality;
    }


    /**
     * Sets the distanceDataQuality value for this RouteCompleteNotification.
     * 
     * @param distanceDataQuality
     */
    public void setDistanceDataQuality(com.freshdirect.routing.proxy.stub.transportation.DataQualityType distanceDataQuality) {
        this.distanceDataQuality = distanceDataQuality;
    }


    /**
     * Gets the actualComplete value for this RouteCompleteNotification.
     * 
     * @return actualComplete
     */
    public java.util.Calendar getActualComplete() {
        return actualComplete;
    }


    /**
     * Sets the actualComplete value for this RouteCompleteNotification.
     * 
     * @param actualComplete
     */
    public void setActualComplete(java.util.Calendar actualComplete) {
        this.actualComplete = actualComplete;
    }


    /**
     * Gets the timeDataQuality value for this RouteCompleteNotification.
     * 
     * @return timeDataQuality
     */
    public com.freshdirect.routing.proxy.stub.transportation.DataQualityType getTimeDataQuality() {
        return timeDataQuality;
    }


    /**
     * Sets the timeDataQuality value for this RouteCompleteNotification.
     * 
     * @param timeDataQuality
     */
    public void setTimeDataQuality(com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality) {
        this.timeDataQuality = timeDataQuality;
    }


    /**
     * Gets the postDelayType value for this RouteCompleteNotification.
     * 
     * @return postDelayType
     */
    public java.lang.String getPostDelayType() {
        return postDelayType;
    }


    /**
     * Sets the postDelayType value for this RouteCompleteNotification.
     * 
     * @param postDelayType
     */
    public void setPostDelayType(java.lang.String postDelayType) {
        this.postDelayType = postDelayType;
    }


    /**
     * Gets the postDelayMinutes value for this RouteCompleteNotification.
     * 
     * @return postDelayMinutes
     */
    public int getPostDelayMinutes() {
        return postDelayMinutes;
    }


    /**
     * Sets the postDelayMinutes value for this RouteCompleteNotification.
     * 
     * @param postDelayMinutes
     */
    public void setPostDelayMinutes(int postDelayMinutes) {
        this.postDelayMinutes = postDelayMinutes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RouteCompleteNotification)) return false;
        RouteCompleteNotification other = (RouteCompleteNotification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              this.source.equals(other.getSource()))) &&
            ((this.routeIdentity==null && other.getRouteIdentity()==null) || 
             (this.routeIdentity!=null &&
              this.routeIdentity.equals(other.getRouteIdentity()))) &&
            this.actualDistance == other.getActualDistance() &&
            ((this.distanceDataQuality==null && other.getDistanceDataQuality()==null) || 
             (this.distanceDataQuality!=null &&
              this.distanceDataQuality.equals(other.getDistanceDataQuality()))) &&
            ((this.actualComplete==null && other.getActualComplete()==null) || 
             (this.actualComplete!=null &&
              this.actualComplete.equals(other.getActualComplete()))) &&
            ((this.timeDataQuality==null && other.getTimeDataQuality()==null) || 
             (this.timeDataQuality!=null &&
              this.timeDataQuality.equals(other.getTimeDataQuality()))) &&
            ((this.postDelayType==null && other.getPostDelayType()==null) || 
             (this.postDelayType!=null &&
              this.postDelayType.equals(other.getPostDelayType()))) &&
            this.postDelayMinutes == other.getPostDelayMinutes();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getSource() != null) {
            _hashCode += getSource().hashCode();
        }
        if (getRouteIdentity() != null) {
            _hashCode += getRouteIdentity().hashCode();
        }
        _hashCode += new Double(getActualDistance()).hashCode();
        if (getDistanceDataQuality() != null) {
            _hashCode += getDistanceDataQuality().hashCode();
        }
        if (getActualComplete() != null) {
            _hashCode += getActualComplete().hashCode();
        }
        if (getTimeDataQuality() != null) {
            _hashCode += getTimeDataQuality().hashCode();
        }
        if (getPostDelayType() != null) {
            _hashCode += getPostDelayType().hashCode();
        }
        _hashCode += getPostDelayMinutes();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RouteCompleteNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteCompleteNotification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationSource"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distanceDataQuality");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "distanceDataQuality"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DataQualityType"));
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
        elemField.setFieldName("timeDataQuality");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeDataQuality"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DataQualityType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postDelayType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "postDelayType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postDelayMinutes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "postDelayMinutes"));
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
