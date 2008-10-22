/**
 * GeocodeOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class GeocodeOptions  implements java.io.Serializable {
    private boolean returnCandidates;

    private boolean returnMatchingArc;

    private boolean setQualityDescription;

    private boolean metricUnits;

    public GeocodeOptions() {
    }

    public GeocodeOptions(
           boolean returnCandidates,
           boolean returnMatchingArc,
           boolean setQualityDescription,
           boolean metricUnits) {
           this.returnCandidates = returnCandidates;
           this.returnMatchingArc = returnMatchingArc;
           this.setQualityDescription = setQualityDescription;
           this.metricUnits = metricUnits;
    }


    /**
     * Gets the returnCandidates value for this GeocodeOptions.
     * 
     * @return returnCandidates
     */
    public boolean isReturnCandidates() {
        return returnCandidates;
    }


    /**
     * Sets the returnCandidates value for this GeocodeOptions.
     * 
     * @param returnCandidates
     */
    public void setReturnCandidates(boolean returnCandidates) {
        this.returnCandidates = returnCandidates;
    }


    /**
     * Gets the returnMatchingArc value for this GeocodeOptions.
     * 
     * @return returnMatchingArc
     */
    public boolean isReturnMatchingArc() {
        return returnMatchingArc;
    }


    /**
     * Sets the returnMatchingArc value for this GeocodeOptions.
     * 
     * @param returnMatchingArc
     */
    public void setReturnMatchingArc(boolean returnMatchingArc) {
        this.returnMatchingArc = returnMatchingArc;
    }


    /**
     * Gets the setQualityDescription value for this GeocodeOptions.
     * 
     * @return setQualityDescription
     */
    public boolean isSetQualityDescription() {
        return setQualityDescription;
    }


    /**
     * Sets the setQualityDescription value for this GeocodeOptions.
     * 
     * @param setQualityDescription
     */
    public void setSetQualityDescription(boolean setQualityDescription) {
        this.setQualityDescription = setQualityDescription;
    }


    /**
     * Gets the metricUnits value for this GeocodeOptions.
     * 
     * @return metricUnits
     */
    public boolean isMetricUnits() {
        return metricUnits;
    }


    /**
     * Sets the metricUnits value for this GeocodeOptions.
     * 
     * @param metricUnits
     */
    public void setMetricUnits(boolean metricUnits) {
        this.metricUnits = metricUnits;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GeocodeOptions)) return false;
        GeocodeOptions other = (GeocodeOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.returnCandidates == other.isReturnCandidates() &&
            this.returnMatchingArc == other.isReturnMatchingArc() &&
            this.setQualityDescription == other.isSetQualityDescription() &&
            this.metricUnits == other.isMetricUnits();
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
        _hashCode += (isReturnCandidates() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isReturnMatchingArc() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isSetQualityDescription() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMetricUnits() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GeocodeOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "GeocodeOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnCandidates");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "returnCandidates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnMatchingArc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "returnMatchingArc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("setQualityDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "setQualityDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metricUnits");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "metricUnits"));
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
