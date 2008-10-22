/**
 * StopCancelNotification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class StopCancelNotification  extends com.freshdirect.routing.proxy.stub.transportation.Notification  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.NotificationSource source;

    private com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity;

    private java.lang.String cancelCode;

    public StopCancelNotification() {
    }

    public StopCancelNotification(
           com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType,
           com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.util.Calendar lockDate,
           com.freshdirect.routing.proxy.stub.transportation.NotificationSource source,
           com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity,
           java.lang.String cancelCode) {
        super(
            notificationType,
            notificationIdentity,
            recipientIdentity,
            lockIdentity,
            lockDate);
        this.source = source;
        this.stopIdentity = stopIdentity;
        this.cancelCode = cancelCode;
    }


    /**
     * Gets the source value for this StopCancelNotification.
     * 
     * @return source
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationSource getSource() {
        return source;
    }


    /**
     * Sets the source value for this StopCancelNotification.
     * 
     * @param source
     */
    public void setSource(com.freshdirect.routing.proxy.stub.transportation.NotificationSource source) {
        this.source = source;
    }


    /**
     * Gets the stopIdentity value for this StopCancelNotification.
     * 
     * @return stopIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopIdentity getStopIdentity() {
        return stopIdentity;
    }


    /**
     * Sets the stopIdentity value for this StopCancelNotification.
     * 
     * @param stopIdentity
     */
    public void setStopIdentity(com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity) {
        this.stopIdentity = stopIdentity;
    }


    /**
     * Gets the cancelCode value for this StopCancelNotification.
     * 
     * @return cancelCode
     */
    public java.lang.String getCancelCode() {
        return cancelCode;
    }


    /**
     * Sets the cancelCode value for this StopCancelNotification.
     * 
     * @param cancelCode
     */
    public void setCancelCode(java.lang.String cancelCode) {
        this.cancelCode = cancelCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StopCancelNotification)) return false;
        StopCancelNotification other = (StopCancelNotification) obj;
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
            ((this.stopIdentity==null && other.getStopIdentity()==null) || 
             (this.stopIdentity!=null &&
              this.stopIdentity.equals(other.getStopIdentity()))) &&
            ((this.cancelCode==null && other.getCancelCode()==null) || 
             (this.cancelCode!=null &&
              this.cancelCode.equals(other.getCancelCode())));
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
        if (getStopIdentity() != null) {
            _hashCode += getStopIdentity().hashCode();
        }
        if (getCancelCode() != null) {
            _hashCode += getCancelCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StopCancelNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopCancelNotification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationSource"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cancelCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "cancelCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
