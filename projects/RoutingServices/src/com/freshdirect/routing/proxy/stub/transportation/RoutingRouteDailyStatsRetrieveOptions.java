/**
 * RoutingRouteDailyStatsRetrieveOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingRouteDailyStatsRetrieveOptions  implements java.io.Serializable {
    private boolean retrieveActive;

    private boolean retrieveBuilt;

    private boolean retrievePublished;

    public RoutingRouteDailyStatsRetrieveOptions() {
    }

    public RoutingRouteDailyStatsRetrieveOptions(
           boolean retrieveActive,
           boolean retrieveBuilt,
           boolean retrievePublished) {
           this.retrieveActive = retrieveActive;
           this.retrieveBuilt = retrieveBuilt;
           this.retrievePublished = retrievePublished;
    }


    /**
     * Gets the retrieveActive value for this RoutingRouteDailyStatsRetrieveOptions.
     * 
     * @return retrieveActive
     */
    public boolean isRetrieveActive() {
        return retrieveActive;
    }


    /**
     * Sets the retrieveActive value for this RoutingRouteDailyStatsRetrieveOptions.
     * 
     * @param retrieveActive
     */
    public void setRetrieveActive(boolean retrieveActive) {
        this.retrieveActive = retrieveActive;
    }


    /**
     * Gets the retrieveBuilt value for this RoutingRouteDailyStatsRetrieveOptions.
     * 
     * @return retrieveBuilt
     */
    public boolean isRetrieveBuilt() {
        return retrieveBuilt;
    }


    /**
     * Sets the retrieveBuilt value for this RoutingRouteDailyStatsRetrieveOptions.
     * 
     * @param retrieveBuilt
     */
    public void setRetrieveBuilt(boolean retrieveBuilt) {
        this.retrieveBuilt = retrieveBuilt;
    }


    /**
     * Gets the retrievePublished value for this RoutingRouteDailyStatsRetrieveOptions.
     * 
     * @return retrievePublished
     */
    public boolean isRetrievePublished() {
        return retrievePublished;
    }


    /**
     * Sets the retrievePublished value for this RoutingRouteDailyStatsRetrieveOptions.
     * 
     * @param retrievePublished
     */
    public void setRetrievePublished(boolean retrievePublished) {
        this.retrievePublished = retrievePublished;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingRouteDailyStatsRetrieveOptions)) return false;
        RoutingRouteDailyStatsRetrieveOptions other = (RoutingRouteDailyStatsRetrieveOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.retrieveActive == other.isRetrieveActive() &&
            this.retrieveBuilt == other.isRetrieveBuilt() &&
            this.retrievePublished == other.isRetrievePublished();
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
        _hashCode += (isRetrieveActive() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrieveBuilt() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrievePublished() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingRouteDailyStatsRetrieveOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteDailyStatsRetrieveOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveActive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveActive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveBuilt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveBuilt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrievePublished");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrievePublished"));
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
