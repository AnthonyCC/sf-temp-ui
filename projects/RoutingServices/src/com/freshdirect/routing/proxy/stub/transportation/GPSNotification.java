/**
 * GPSNotification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class GPSNotification  extends com.freshdirect.routing.proxy.stub.transportation.Notification  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.NotificationSource source;

    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private java.util.Calendar timeStamp;

    private java.lang.String positionText;

    private com.freshdirect.routing.proxy.stub.transportation.MapPoint position;

    public GPSNotification() {
    }

    public GPSNotification(
           com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType,
           com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.util.Calendar lockDate,
           com.freshdirect.routing.proxy.stub.transportation.NotificationSource source,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           java.util.Calendar timeStamp,
           java.lang.String positionText,
           com.freshdirect.routing.proxy.stub.transportation.MapPoint position) {
        super(
            notificationType,
            notificationIdentity,
            recipientIdentity,
            lockIdentity,
            lockDate);
        this.source = source;
        this.routeIdentity = routeIdentity;
        this.timeStamp = timeStamp;
        this.positionText = positionText;
        this.position = position;
    }


    /**
     * Gets the source value for this GPSNotification.
     * 
     * @return source
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationSource getSource() {
        return source;
    }


    /**
     * Sets the source value for this GPSNotification.
     * 
     * @param source
     */
    public void setSource(com.freshdirect.routing.proxy.stub.transportation.NotificationSource source) {
        this.source = source;
    }


    /**
     * Gets the routeIdentity value for this GPSNotification.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this GPSNotification.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the timeStamp value for this GPSNotification.
     * 
     * @return timeStamp
     */
    public java.util.Calendar getTimeStamp() {
        return timeStamp;
    }


    /**
     * Sets the timeStamp value for this GPSNotification.
     * 
     * @param timeStamp
     */
    public void setTimeStamp(java.util.Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }


    /**
     * Gets the positionText value for this GPSNotification.
     * 
     * @return positionText
     */
    public java.lang.String getPositionText() {
        return positionText;
    }


    /**
     * Sets the positionText value for this GPSNotification.
     * 
     * @param positionText
     */
    public void setPositionText(java.lang.String positionText) {
        this.positionText = positionText;
    }


    /**
     * Gets the position value for this GPSNotification.
     * 
     * @return position
     */
    public com.freshdirect.routing.proxy.stub.transportation.MapPoint getPosition() {
        return position;
    }


    /**
     * Sets the position value for this GPSNotification.
     * 
     * @param position
     */
    public void setPosition(com.freshdirect.routing.proxy.stub.transportation.MapPoint position) {
        this.position = position;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GPSNotification)) return false;
        GPSNotification other = (GPSNotification) obj;
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
            ((this.timeStamp==null && other.getTimeStamp()==null) || 
             (this.timeStamp!=null &&
              this.timeStamp.equals(other.getTimeStamp()))) &&
            ((this.positionText==null && other.getPositionText()==null) || 
             (this.positionText!=null &&
              this.positionText.equals(other.getPositionText()))) &&
            ((this.position==null && other.getPosition()==null) || 
             (this.position!=null &&
              this.position.equals(other.getPosition())));
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
        if (getTimeStamp() != null) {
            _hashCode += getTimeStamp().hashCode();
        }
        if (getPositionText() != null) {
            _hashCode += getPositionText().hashCode();
        }
        if (getPosition() != null) {
            _hashCode += getPosition().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GPSNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "GPSNotification"));
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
        elemField.setFieldName("timeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("position");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "position"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "MapPoint"));
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
