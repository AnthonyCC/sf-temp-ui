/**
 * LateGpsExceptionNotification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class LateGpsExceptionNotification  extends com.freshdirect.routing.proxy.stub.transportation.Notification  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private java.util.Calendar timeStamp;

    private java.util.Calendar recievedTime;

    public LateGpsExceptionNotification() {
    }

    public LateGpsExceptionNotification(
           com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType,
           com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.util.Calendar lockDate,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           java.util.Calendar timeStamp,
           java.util.Calendar recievedTime) {
        super(
            notificationType,
            notificationIdentity,
            recipientIdentity,
            lockIdentity,
            lockDate);
        this.routeIdentity = routeIdentity;
        this.timeStamp = timeStamp;
        this.recievedTime = recievedTime;
    }


    /**
     * Gets the routeIdentity value for this LateGpsExceptionNotification.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this LateGpsExceptionNotification.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the timeStamp value for this LateGpsExceptionNotification.
     * 
     * @return timeStamp
     */
    public java.util.Calendar getTimeStamp() {
        return timeStamp;
    }


    /**
     * Sets the timeStamp value for this LateGpsExceptionNotification.
     * 
     * @param timeStamp
     */
    public void setTimeStamp(java.util.Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }


    /**
     * Gets the recievedTime value for this LateGpsExceptionNotification.
     * 
     * @return recievedTime
     */
    public java.util.Calendar getRecievedTime() {
        return recievedTime;
    }


    /**
     * Sets the recievedTime value for this LateGpsExceptionNotification.
     * 
     * @param recievedTime
     */
    public void setRecievedTime(java.util.Calendar recievedTime) {
        this.recievedTime = recievedTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LateGpsExceptionNotification)) return false;
        LateGpsExceptionNotification other = (LateGpsExceptionNotification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.routeIdentity==null && other.getRouteIdentity()==null) || 
             (this.routeIdentity!=null &&
              this.routeIdentity.equals(other.getRouteIdentity()))) &&
            ((this.timeStamp==null && other.getTimeStamp()==null) || 
             (this.timeStamp!=null &&
              this.timeStamp.equals(other.getTimeStamp()))) &&
            ((this.recievedTime==null && other.getRecievedTime()==null) || 
             (this.recievedTime!=null &&
              this.recievedTime.equals(other.getRecievedTime())));
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
        if (getRouteIdentity() != null) {
            _hashCode += getRouteIdentity().hashCode();
        }
        if (getTimeStamp() != null) {
            _hashCode += getTimeStamp().hashCode();
        }
        if (getRecievedTime() != null) {
            _hashCode += getRecievedTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LateGpsExceptionNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LateGpsExceptionNotification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recievedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "recievedTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
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
