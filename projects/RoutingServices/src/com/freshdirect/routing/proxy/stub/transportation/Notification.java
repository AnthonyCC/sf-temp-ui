/**
 * Notification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Notification  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType;

    private com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity;

    private java.util.Calendar lockDate;

    public Notification() {
    }

    public Notification(
           com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType,
           com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.util.Calendar lockDate) {
           this.notificationType = notificationType;
           this.notificationIdentity = notificationIdentity;
           this.recipientIdentity = recipientIdentity;
           this.lockIdentity = lockIdentity;
           this.lockDate = lockDate;
    }


    /**
     * Gets the notificationType value for this Notification.
     * 
     * @return notificationType
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationType getNotificationType() {
        return notificationType;
    }


    /**
     * Sets the notificationType value for this Notification.
     * 
     * @param notificationType
     */
    public void setNotificationType(com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType) {
        this.notificationType = notificationType;
    }


    /**
     * Gets the notificationIdentity value for this Notification.
     * 
     * @return notificationIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity getNotificationIdentity() {
        return notificationIdentity;
    }


    /**
     * Sets the notificationIdentity value for this Notification.
     * 
     * @param notificationIdentity
     */
    public void setNotificationIdentity(com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity) {
        this.notificationIdentity = notificationIdentity;
    }


    /**
     * Gets the recipientIdentity value for this Notification.
     * 
     * @return recipientIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity getRecipientIdentity() {
        return recipientIdentity;
    }


    /**
     * Sets the recipientIdentity value for this Notification.
     * 
     * @param recipientIdentity
     */
    public void setRecipientIdentity(com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity) {
        this.recipientIdentity = recipientIdentity;
    }


    /**
     * Gets the lockIdentity value for this Notification.
     * 
     * @return lockIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity getLockIdentity() {
        return lockIdentity;
    }


    /**
     * Sets the lockIdentity value for this Notification.
     * 
     * @param lockIdentity
     */
    public void setLockIdentity(com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity) {
        this.lockIdentity = lockIdentity;
    }


    /**
     * Gets the lockDate value for this Notification.
     * 
     * @return lockDate
     */
    public java.util.Calendar getLockDate() {
        return lockDate;
    }


    /**
     * Sets the lockDate value for this Notification.
     * 
     * @param lockDate
     */
    public void setLockDate(java.util.Calendar lockDate) {
        this.lockDate = lockDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Notification)) return false;
        Notification other = (Notification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.notificationType==null && other.getNotificationType()==null) || 
             (this.notificationType!=null &&
              this.notificationType.equals(other.getNotificationType()))) &&
            ((this.notificationIdentity==null && other.getNotificationIdentity()==null) || 
             (this.notificationIdentity!=null &&
              this.notificationIdentity.equals(other.getNotificationIdentity()))) &&
            ((this.recipientIdentity==null && other.getRecipientIdentity()==null) || 
             (this.recipientIdentity!=null &&
              this.recipientIdentity.equals(other.getRecipientIdentity()))) &&
            ((this.lockIdentity==null && other.getLockIdentity()==null) || 
             (this.lockIdentity!=null &&
              this.lockIdentity.equals(other.getLockIdentity()))) &&
            ((this.lockDate==null && other.getLockDate()==null) || 
             (this.lockDate!=null &&
              this.lockDate.equals(other.getLockDate())));
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
        if (getNotificationType() != null) {
            _hashCode += getNotificationType().hashCode();
        }
        if (getNotificationIdentity() != null) {
            _hashCode += getNotificationIdentity().hashCode();
        }
        if (getRecipientIdentity() != null) {
            _hashCode += getRecipientIdentity().hashCode();
        }
        if (getLockIdentity() != null) {
            _hashCode += getLockIdentity().hashCode();
        }
        if (getLockDate() != null) {
            _hashCode += getLockDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Notification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Notification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificationType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "notificationType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificationIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "notificationIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipientIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "recipientIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RecipientIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lockIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lockIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationLockIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lockDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lockDate"));
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
