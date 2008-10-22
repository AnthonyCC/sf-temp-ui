/**
 * TimeWindowCost.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class TimeWindowCost  implements java.io.Serializable {
    private int numMissed;

    private int totalMissedTime;

    private int maxMissedTime;

    public TimeWindowCost() {
    }

    public TimeWindowCost(
           int numMissed,
           int totalMissedTime,
           int maxMissedTime) {
           this.numMissed = numMissed;
           this.totalMissedTime = totalMissedTime;
           this.maxMissedTime = maxMissedTime;
    }


    /**
     * Gets the numMissed value for this TimeWindowCost.
     * 
     * @return numMissed
     */
    public int getNumMissed() {
        return numMissed;
    }


    /**
     * Sets the numMissed value for this TimeWindowCost.
     * 
     * @param numMissed
     */
    public void setNumMissed(int numMissed) {
        this.numMissed = numMissed;
    }


    /**
     * Gets the totalMissedTime value for this TimeWindowCost.
     * 
     * @return totalMissedTime
     */
    public int getTotalMissedTime() {
        return totalMissedTime;
    }


    /**
     * Sets the totalMissedTime value for this TimeWindowCost.
     * 
     * @param totalMissedTime
     */
    public void setTotalMissedTime(int totalMissedTime) {
        this.totalMissedTime = totalMissedTime;
    }


    /**
     * Gets the maxMissedTime value for this TimeWindowCost.
     * 
     * @return maxMissedTime
     */
    public int getMaxMissedTime() {
        return maxMissedTime;
    }


    /**
     * Sets the maxMissedTime value for this TimeWindowCost.
     * 
     * @param maxMissedTime
     */
    public void setMaxMissedTime(int maxMissedTime) {
        this.maxMissedTime = maxMissedTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TimeWindowCost)) return false;
        TimeWindowCost other = (TimeWindowCost) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.numMissed == other.getNumMissed() &&
            this.totalMissedTime == other.getTotalMissedTime() &&
            this.maxMissedTime == other.getMaxMissedTime();
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
        _hashCode += getNumMissed();
        _hashCode += getTotalMissedTime();
        _hashCode += getMaxMissedTime();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TimeWindowCost.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeWindowCost"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numMissed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "numMissed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalMissedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalMissedTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxMissedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maxMissedTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
