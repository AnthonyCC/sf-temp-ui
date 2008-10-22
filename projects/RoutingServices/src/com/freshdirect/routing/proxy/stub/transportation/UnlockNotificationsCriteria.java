/**
 * UnlockNotificationsCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class UnlockNotificationsCriteria  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity;

    private java.lang.Integer maxAge;

    public UnlockNotificationsCriteria() {
    }

    public UnlockNotificationsCriteria(
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.lang.Integer maxAge) {
           this.recipientIdentity = recipientIdentity;
           this.lockIdentity = lockIdentity;
           this.maxAge = maxAge;
    }


    /**
     * Gets the recipientIdentity value for this UnlockNotificationsCriteria.
     * 
     * @return recipientIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity getRecipientIdentity() {
        return recipientIdentity;
    }


    /**
     * Sets the recipientIdentity value for this UnlockNotificationsCriteria.
     * 
     * @param recipientIdentity
     */
    public void setRecipientIdentity(com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity) {
        this.recipientIdentity = recipientIdentity;
    }


    /**
     * Gets the lockIdentity value for this UnlockNotificationsCriteria.
     * 
     * @return lockIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity getLockIdentity() {
        return lockIdentity;
    }


    /**
     * Sets the lockIdentity value for this UnlockNotificationsCriteria.
     * 
     * @param lockIdentity
     */
    public void setLockIdentity(com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity) {
        this.lockIdentity = lockIdentity;
    }


    /**
     * Gets the maxAge value for this UnlockNotificationsCriteria.
     * 
     * @return maxAge
     */
    public java.lang.Integer getMaxAge() {
        return maxAge;
    }


    /**
     * Sets the maxAge value for this UnlockNotificationsCriteria.
     * 
     * @param maxAge
     */
    public void setMaxAge(java.lang.Integer maxAge) {
        this.maxAge = maxAge;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnlockNotificationsCriteria)) return false;
        UnlockNotificationsCriteria other = (UnlockNotificationsCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.recipientIdentity==null && other.getRecipientIdentity()==null) || 
             (this.recipientIdentity!=null &&
              this.recipientIdentity.equals(other.getRecipientIdentity()))) &&
            ((this.lockIdentity==null && other.getLockIdentity()==null) || 
             (this.lockIdentity!=null &&
              this.lockIdentity.equals(other.getLockIdentity()))) &&
            ((this.maxAge==null && other.getMaxAge()==null) || 
             (this.maxAge!=null &&
              this.maxAge.equals(other.getMaxAge())));
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
        if (getRecipientIdentity() != null) {
            _hashCode += getRecipientIdentity().hashCode();
        }
        if (getLockIdentity() != null) {
            _hashCode += getLockIdentity().hashCode();
        }
        if (getMaxAge() != null) {
            _hashCode += getMaxAge().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UnlockNotificationsCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UnlockNotificationsCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipientIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "recipientIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RecipientIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lockIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lockIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationLockIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxAge");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxAge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
