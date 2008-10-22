/**
 * OrderActivity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class OrderActivity  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.ActivityIdentity activityIdentity;

    private boolean completed;

    public OrderActivity() {
    }

    public OrderActivity(
           com.freshdirect.routing.proxy.stub.transportation.ActivityIdentity activityIdentity,
           boolean completed) {
           this.activityIdentity = activityIdentity;
           this.completed = completed;
    }


    /**
     * Gets the activityIdentity value for this OrderActivity.
     * 
     * @return activityIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.ActivityIdentity getActivityIdentity() {
        return activityIdentity;
    }


    /**
     * Sets the activityIdentity value for this OrderActivity.
     * 
     * @param activityIdentity
     */
    public void setActivityIdentity(com.freshdirect.routing.proxy.stub.transportation.ActivityIdentity activityIdentity) {
        this.activityIdentity = activityIdentity;
    }


    /**
     * Gets the completed value for this OrderActivity.
     * 
     * @return completed
     */
    public boolean isCompleted() {
        return completed;
    }


    /**
     * Sets the completed value for this OrderActivity.
     * 
     * @param completed
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OrderActivity)) return false;
        OrderActivity other = (OrderActivity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.activityIdentity==null && other.getActivityIdentity()==null) || 
             (this.activityIdentity!=null &&
              this.activityIdentity.equals(other.getActivityIdentity()))) &&
            this.completed == other.isCompleted();
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
        if (getActivityIdentity() != null) {
            _hashCode += getActivityIdentity().hashCode();
        }
        _hashCode += (isCompleted() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OrderActivity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderActivity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "activityIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ActivityIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "completed"));
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
