/**
 * NotificationRetrieveOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class NotificationRetrieveOptions  implements java.io.Serializable {
    private boolean autoDelete;

    private int maxRetrieve;

    private com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity;

    public NotificationRetrieveOptions() {
    }

    public NotificationRetrieveOptions(
           boolean autoDelete,
           int maxRetrieve,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity) {
           this.autoDelete = autoDelete;
           this.maxRetrieve = maxRetrieve;
           this.lockIdentity = lockIdentity;
    }


    /**
     * Gets the autoDelete value for this NotificationRetrieveOptions.
     * 
     * @return autoDelete
     */
    public boolean isAutoDelete() {
        return autoDelete;
    }


    /**
     * Sets the autoDelete value for this NotificationRetrieveOptions.
     * 
     * @param autoDelete
     */
    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }


    /**
     * Gets the maxRetrieve value for this NotificationRetrieveOptions.
     * 
     * @return maxRetrieve
     */
    public int getMaxRetrieve() {
        return maxRetrieve;
    }


    /**
     * Sets the maxRetrieve value for this NotificationRetrieveOptions.
     * 
     * @param maxRetrieve
     */
    public void setMaxRetrieve(int maxRetrieve) {
        this.maxRetrieve = maxRetrieve;
    }


    /**
     * Gets the lockIdentity value for this NotificationRetrieveOptions.
     * 
     * @return lockIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity getLockIdentity() {
        return lockIdentity;
    }


    /**
     * Sets the lockIdentity value for this NotificationRetrieveOptions.
     * 
     * @param lockIdentity
     */
    public void setLockIdentity(com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity) {
        this.lockIdentity = lockIdentity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NotificationRetrieveOptions)) return false;
        NotificationRetrieveOptions other = (NotificationRetrieveOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.autoDelete == other.isAutoDelete() &&
            this.maxRetrieve == other.getMaxRetrieve() &&
            ((this.lockIdentity==null && other.getLockIdentity()==null) || 
             (this.lockIdentity!=null &&
              this.lockIdentity.equals(other.getLockIdentity())));
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
        _hashCode += (isAutoDelete() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getMaxRetrieve();
        if (getLockIdentity() != null) {
            _hashCode += getLockIdentity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NotificationRetrieveOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationRetrieveOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autoDelete");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "autoDelete"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxRetrieve");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxRetrieve"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lockIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lockIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationLockIdentity"));
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
