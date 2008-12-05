/**
 * SaveLocationsExOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SaveLocationsExOptions  implements java.io.Serializable {
    private boolean disableGeocoding;

    private boolean disableLocationPreloading;

    public SaveLocationsExOptions() {
    }

    public SaveLocationsExOptions(
           boolean disableGeocoding,
           boolean disableLocationPreloading) {
           this.disableGeocoding = disableGeocoding;
           this.disableLocationPreloading = disableLocationPreloading;
    }


    /**
     * Gets the disableGeocoding value for this SaveLocationsExOptions.
     * 
     * @return disableGeocoding
     */
    public boolean isDisableGeocoding() {
        return disableGeocoding;
    }


    /**
     * Sets the disableGeocoding value for this SaveLocationsExOptions.
     * 
     * @param disableGeocoding
     */
    public void setDisableGeocoding(boolean disableGeocoding) {
        this.disableGeocoding = disableGeocoding;
    }


    /**
     * Gets the disableLocationPreloading value for this SaveLocationsExOptions.
     * 
     * @return disableLocationPreloading
     */
    public boolean isDisableLocationPreloading() {
        return disableLocationPreloading;
    }


    /**
     * Sets the disableLocationPreloading value for this SaveLocationsExOptions.
     * 
     * @param disableLocationPreloading
     */
    public void setDisableLocationPreloading(boolean disableLocationPreloading) {
        this.disableLocationPreloading = disableLocationPreloading;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SaveLocationsExOptions)) return false;
        SaveLocationsExOptions other = (SaveLocationsExOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.disableGeocoding == other.isDisableGeocoding() &&
            this.disableLocationPreloading == other.isDisableLocationPreloading();
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
        _hashCode += (isDisableGeocoding() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDisableLocationPreloading() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SaveLocationsExOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SaveLocationsExOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disableGeocoding");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "disableGeocoding"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disableLocationPreloading");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "disableLocationPreloading"));
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
