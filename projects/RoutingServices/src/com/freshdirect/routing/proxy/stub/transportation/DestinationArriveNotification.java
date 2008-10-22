/**
 * DestinationArriveNotification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DestinationArriveNotification  extends com.freshdirect.routing.proxy.stub.transportation.Notification  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.NotificationSource source;

    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private java.util.Calendar actualArrival;

    private com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality;

    public DestinationArriveNotification() {
    }

    public DestinationArriveNotification(
           com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType,
           com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.util.Calendar lockDate,
           com.freshdirect.routing.proxy.stub.transportation.NotificationSource source,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           java.util.Calendar actualArrival,
           com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality) {
        super(
            notificationType,
            notificationIdentity,
            recipientIdentity,
            lockIdentity,
            lockDate);
        this.source = source;
        this.routeIdentity = routeIdentity;
        this.actualArrival = actualArrival;
        this.timeDataQuality = timeDataQuality;
    }


    /**
     * Gets the source value for this DestinationArriveNotification.
     * 
     * @return source
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationSource getSource() {
        return source;
    }


    /**
     * Sets the source value for this DestinationArriveNotification.
     * 
     * @param source
     */
    public void setSource(com.freshdirect.routing.proxy.stub.transportation.NotificationSource source) {
        this.source = source;
    }


    /**
     * Gets the routeIdentity value for this DestinationArriveNotification.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this DestinationArriveNotification.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the actualArrival value for this DestinationArriveNotification.
     * 
     * @return actualArrival
     */
    public java.util.Calendar getActualArrival() {
        return actualArrival;
    }


    /**
     * Sets the actualArrival value for this DestinationArriveNotification.
     * 
     * @param actualArrival
     */
    public void setActualArrival(java.util.Calendar actualArrival) {
        this.actualArrival = actualArrival;
    }


    /**
     * Gets the timeDataQuality value for this DestinationArriveNotification.
     * 
     * @return timeDataQuality
     */
    public com.freshdirect.routing.proxy.stub.transportation.DataQualityType getTimeDataQuality() {
        return timeDataQuality;
    }


    /**
     * Sets the timeDataQuality value for this DestinationArriveNotification.
     * 
     * @param timeDataQuality
     */
    public void setTimeDataQuality(com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality) {
        this.timeDataQuality = timeDataQuality;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DestinationArriveNotification)) return false;
        DestinationArriveNotification other = (DestinationArriveNotification) obj;
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
            ((this.actualArrival==null && other.getActualArrival()==null) || 
             (this.actualArrival!=null &&
              this.actualArrival.equals(other.getActualArrival()))) &&
            ((this.timeDataQuality==null && other.getTimeDataQuality()==null) || 
             (this.timeDataQuality!=null &&
              this.timeDataQuality.equals(other.getTimeDataQuality())));
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
        if (getActualArrival() != null) {
            _hashCode += getActualArrival().hashCode();
        }
        if (getTimeDataQuality() != null) {
            _hashCode += getTimeDataQuality().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DestinationArriveNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DestinationArriveNotification"));
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
        elemField.setFieldName("actualArrival");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualArrival"));
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
