/**
 * SchedulerOrdersCanceledNotification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerOrdersCanceledNotification  extends com.freshdirect.routing.proxy.stub.transportation.Notification  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity;

    private java.lang.String[] orderNumbers;

    public SchedulerOrdersCanceledNotification() {
    }

    public SchedulerOrdersCanceledNotification(
           com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType,
           com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.util.Calendar lockDate,
           com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity,
           java.lang.String[] orderNumbers) {
        super(
            notificationType,
            notificationIdentity,
            recipientIdentity,
            lockIdentity,
            lockDate);
        this.schedulerIdentity = schedulerIdentity;
        this.orderNumbers = orderNumbers;
    }


    /**
     * Gets the schedulerIdentity value for this SchedulerOrdersCanceledNotification.
     * 
     * @return schedulerIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity getSchedulerIdentity() {
        return schedulerIdentity;
    }


    /**
     * Sets the schedulerIdentity value for this SchedulerOrdersCanceledNotification.
     * 
     * @param schedulerIdentity
     */
    public void setSchedulerIdentity(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity) {
        this.schedulerIdentity = schedulerIdentity;
    }


    /**
     * Gets the orderNumbers value for this SchedulerOrdersCanceledNotification.
     * 
     * @return orderNumbers
     */
    public java.lang.String[] getOrderNumbers() {
        return orderNumbers;
    }


    /**
     * Sets the orderNumbers value for this SchedulerOrdersCanceledNotification.
     * 
     * @param orderNumbers
     */
    public void setOrderNumbers(java.lang.String[] orderNumbers) {
        this.orderNumbers = orderNumbers;
    }

    public java.lang.String getOrderNumbers(int i) {
        return this.orderNumbers[i];
    }

    public void setOrderNumbers(int i, java.lang.String _value) {
        this.orderNumbers[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerOrdersCanceledNotification)) return false;
        SchedulerOrdersCanceledNotification other = (SchedulerOrdersCanceledNotification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.schedulerIdentity==null && other.getSchedulerIdentity()==null) || 
             (this.schedulerIdentity!=null &&
              this.schedulerIdentity.equals(other.getSchedulerIdentity()))) &&
            ((this.orderNumbers==null && other.getOrderNumbers()==null) || 
             (this.orderNumbers!=null &&
              java.util.Arrays.equals(this.orderNumbers, other.getOrderNumbers())));
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
        if (getSchedulerIdentity() != null) {
            _hashCode += getSchedulerIdentity().hashCode();
        }
        if (getOrderNumbers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOrderNumbers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOrderNumbers(), i);
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
        new org.apache.axis.description.TypeDesc(SchedulerOrdersCanceledNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerOrdersCanceledNotification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schedulerIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "schedulerIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderNumbers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderNumbers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
