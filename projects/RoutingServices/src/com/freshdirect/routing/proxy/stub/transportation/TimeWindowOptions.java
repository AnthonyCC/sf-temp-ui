/**
 * TimeWindowOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class TimeWindowOptions  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions;

    private java.util.Calendar adjustStartEarlyMinimum;

    private int adjustStartIncrement;

    private boolean makeAll;

    public TimeWindowOptions() {
    }

    public TimeWindowOptions(
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions,
           java.util.Calendar adjustStartEarlyMinimum,
           int adjustStartIncrement,
           boolean makeAll) {
           this.timeZoneOptions = timeZoneOptions;
           this.adjustStartEarlyMinimum = adjustStartEarlyMinimum;
           this.adjustStartIncrement = adjustStartIncrement;
           this.makeAll = makeAll;
    }


    /**
     * Gets the timeZoneOptions value for this TimeWindowOptions.
     * 
     * @return timeZoneOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions getTimeZoneOptions() {
        return timeZoneOptions;
    }


    /**
     * Sets the timeZoneOptions value for this TimeWindowOptions.
     * 
     * @param timeZoneOptions
     */
    public void setTimeZoneOptions(com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
        this.timeZoneOptions = timeZoneOptions;
    }


    /**
     * Gets the adjustStartEarlyMinimum value for this TimeWindowOptions.
     * 
     * @return adjustStartEarlyMinimum
     */
    public java.util.Calendar getAdjustStartEarlyMinimum() {
        return adjustStartEarlyMinimum;
    }


    /**
     * Sets the adjustStartEarlyMinimum value for this TimeWindowOptions.
     * 
     * @param adjustStartEarlyMinimum
     */
    public void setAdjustStartEarlyMinimum(java.util.Calendar adjustStartEarlyMinimum) {
        this.adjustStartEarlyMinimum = adjustStartEarlyMinimum;
    }


    /**
     * Gets the adjustStartIncrement value for this TimeWindowOptions.
     * 
     * @return adjustStartIncrement
     */
    public int getAdjustStartIncrement() {
        return adjustStartIncrement;
    }


    /**
     * Sets the adjustStartIncrement value for this TimeWindowOptions.
     * 
     * @param adjustStartIncrement
     */
    public void setAdjustStartIncrement(int adjustStartIncrement) {
        this.adjustStartIncrement = adjustStartIncrement;
    }


    /**
     * Gets the makeAll value for this TimeWindowOptions.
     * 
     * @return makeAll
     */
    public boolean isMakeAll() {
        return makeAll;
    }


    /**
     * Sets the makeAll value for this TimeWindowOptions.
     * 
     * @param makeAll
     */
    public void setMakeAll(boolean makeAll) {
        this.makeAll = makeAll;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TimeWindowOptions)) return false;
        TimeWindowOptions other = (TimeWindowOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.timeZoneOptions==null && other.getTimeZoneOptions()==null) || 
             (this.timeZoneOptions!=null &&
              this.timeZoneOptions.equals(other.getTimeZoneOptions()))) &&
            ((this.adjustStartEarlyMinimum==null && other.getAdjustStartEarlyMinimum()==null) || 
             (this.adjustStartEarlyMinimum!=null &&
              this.adjustStartEarlyMinimum.equals(other.getAdjustStartEarlyMinimum()))) &&
            this.adjustStartIncrement == other.getAdjustStartIncrement() &&
            this.makeAll == other.isMakeAll();
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
        if (getTimeZoneOptions() != null) {
            _hashCode += getTimeZoneOptions().hashCode();
        }
        if (getAdjustStartEarlyMinimum() != null) {
            _hashCode += getAdjustStartEarlyMinimum().hashCode();
        }
        _hashCode += getAdjustStartIncrement();
        _hashCode += (isMakeAll() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TimeWindowOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeWindowOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZoneOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeZoneOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneOptions"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adjustStartEarlyMinimum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "adjustStartEarlyMinimum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adjustStartIncrement");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "adjustStartIncrement"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("makeAll");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "makeAll"));
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
