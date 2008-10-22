/**
 * SchedulerUpdateOrderOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerUpdateOrderOptions  implements java.io.Serializable {
    private java.lang.Integer newQuantity;

    private java.lang.Integer newPUQuantity;

    private java.lang.Integer newServiceTime;

    public SchedulerUpdateOrderOptions() {
    }

    public SchedulerUpdateOrderOptions(
           java.lang.Integer newQuantity,
           java.lang.Integer newPUQuantity,
           java.lang.Integer newServiceTime) {
           this.newQuantity = newQuantity;
           this.newPUQuantity = newPUQuantity;
           this.newServiceTime = newServiceTime;
    }


    /**
     * Gets the newQuantity value for this SchedulerUpdateOrderOptions.
     * 
     * @return newQuantity
     */
    public java.lang.Integer getNewQuantity() {
        return newQuantity;
    }


    /**
     * Sets the newQuantity value for this SchedulerUpdateOrderOptions.
     * 
     * @param newQuantity
     */
    public void setNewQuantity(java.lang.Integer newQuantity) {
        this.newQuantity = newQuantity;
    }


    /**
     * Gets the newPUQuantity value for this SchedulerUpdateOrderOptions.
     * 
     * @return newPUQuantity
     */
    public java.lang.Integer getNewPUQuantity() {
        return newPUQuantity;
    }


    /**
     * Sets the newPUQuantity value for this SchedulerUpdateOrderOptions.
     * 
     * @param newPUQuantity
     */
    public void setNewPUQuantity(java.lang.Integer newPUQuantity) {
        this.newPUQuantity = newPUQuantity;
    }


    /**
     * Gets the newServiceTime value for this SchedulerUpdateOrderOptions.
     * 
     * @return newServiceTime
     */
    public java.lang.Integer getNewServiceTime() {
        return newServiceTime;
    }


    /**
     * Sets the newServiceTime value for this SchedulerUpdateOrderOptions.
     * 
     * @param newServiceTime
     */
    public void setNewServiceTime(java.lang.Integer newServiceTime) {
        this.newServiceTime = newServiceTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerUpdateOrderOptions)) return false;
        SchedulerUpdateOrderOptions other = (SchedulerUpdateOrderOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.newQuantity==null && other.getNewQuantity()==null) || 
             (this.newQuantity!=null &&
              this.newQuantity.equals(other.getNewQuantity()))) &&
            ((this.newPUQuantity==null && other.getNewPUQuantity()==null) || 
             (this.newPUQuantity!=null &&
              this.newPUQuantity.equals(other.getNewPUQuantity()))) &&
            ((this.newServiceTime==null && other.getNewServiceTime()==null) || 
             (this.newServiceTime!=null &&
              this.newServiceTime.equals(other.getNewServiceTime())));
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
        if (getNewQuantity() != null) {
            _hashCode += getNewQuantity().hashCode();
        }
        if (getNewPUQuantity() != null) {
            _hashCode += getNewPUQuantity().hashCode();
        }
        if (getNewServiceTime() != null) {
            _hashCode += getNewServiceTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerUpdateOrderOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerUpdateOrderOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "newQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newPUQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "newPUQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newServiceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "newServiceTime"));
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
