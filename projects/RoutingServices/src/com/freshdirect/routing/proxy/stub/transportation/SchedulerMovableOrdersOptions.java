/**
 * SchedulerMovableOrdersOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerMovableOrdersOptions  implements java.io.Serializable {
    private boolean movable;

    private boolean runOptimizerFirst;

    public SchedulerMovableOrdersOptions() {
    }

    public SchedulerMovableOrdersOptions(
           boolean movable,
           boolean runOptimizerFirst) {
           this.movable = movable;
           this.runOptimizerFirst = runOptimizerFirst;
    }


    /**
     * Gets the movable value for this SchedulerMovableOrdersOptions.
     * 
     * @return movable
     */
    public boolean isMovable() {
        return movable;
    }


    /**
     * Sets the movable value for this SchedulerMovableOrdersOptions.
     * 
     * @param movable
     */
    public void setMovable(boolean movable) {
        this.movable = movable;
    }


    /**
     * Gets the runOptimizerFirst value for this SchedulerMovableOrdersOptions.
     * 
     * @return runOptimizerFirst
     */
    public boolean isRunOptimizerFirst() {
        return runOptimizerFirst;
    }


    /**
     * Sets the runOptimizerFirst value for this SchedulerMovableOrdersOptions.
     * 
     * @param runOptimizerFirst
     */
    public void setRunOptimizerFirst(boolean runOptimizerFirst) {
        this.runOptimizerFirst = runOptimizerFirst;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerMovableOrdersOptions)) return false;
        SchedulerMovableOrdersOptions other = (SchedulerMovableOrdersOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.movable == other.isMovable() &&
            this.runOptimizerFirst == other.isRunOptimizerFirst();
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
        _hashCode += (isMovable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRunOptimizerFirst() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerMovableOrdersOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerMovableOrdersOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("movable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "movable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runOptimizerFirst");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "runOptimizerFirst"));
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
