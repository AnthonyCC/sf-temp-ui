/**
 * TimeZoneOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class TimeZoneOptions  implements java.io.Serializable {
    private boolean embeddedInTimestamp;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptionsType optionType;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone;

    public TimeZoneOptions() {
    }

    public TimeZoneOptions(
           boolean embeddedInTimestamp,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptionsType optionType,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone) {
           this.embeddedInTimestamp = embeddedInTimestamp;
           this.optionType = optionType;
           this.timeZone = timeZone;
    }


    /**
     * Gets the embeddedInTimestamp value for this TimeZoneOptions.
     * 
     * @return embeddedInTimestamp
     */
    public boolean isEmbeddedInTimestamp() {
        return embeddedInTimestamp;
    }


    /**
     * Sets the embeddedInTimestamp value for this TimeZoneOptions.
     * 
     * @param embeddedInTimestamp
     */
    public void setEmbeddedInTimestamp(boolean embeddedInTimestamp) {
        this.embeddedInTimestamp = embeddedInTimestamp;
    }


    /**
     * Gets the optionType value for this TimeZoneOptions.
     * 
     * @return optionType
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptionsType getOptionType() {
        return optionType;
    }


    /**
     * Sets the optionType value for this TimeZoneOptions.
     * 
     * @param optionType
     */
    public void setOptionType(com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptionsType optionType) {
        this.optionType = optionType;
    }


    /**
     * Gets the timeZone value for this TimeZoneOptions.
     * 
     * @return timeZone
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue getTimeZone() {
        return timeZone;
    }


    /**
     * Sets the timeZone value for this TimeZoneOptions.
     * 
     * @param timeZone
     */
    public void setTimeZone(com.freshdirect.routing.proxy.stub.transportation.TimeZoneValue timeZone) {
        this.timeZone = timeZone;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TimeZoneOptions)) return false;
        TimeZoneOptions other = (TimeZoneOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.embeddedInTimestamp == other.isEmbeddedInTimestamp() &&
            ((this.optionType==null && other.getOptionType()==null) || 
             (this.optionType!=null &&
              this.optionType.equals(other.getOptionType()))) &&
            ((this.timeZone==null && other.getTimeZone()==null) || 
             (this.timeZone!=null &&
              this.timeZone.equals(other.getTimeZone())));
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
        _hashCode += (isEmbeddedInTimestamp() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOptionType() != null) {
            _hashCode += getOptionType().hashCode();
        }
        if (getTimeZone() != null) {
            _hashCode += getTimeZone().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TimeZoneOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("embeddedInTimestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "embeddedInTimestamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("optionType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "optionType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneOptionsType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneValue"));
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
