/**
 * MapArcOverride.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapArcOverride  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity arcIdentity;

    private boolean disabled;

    public MapArcOverride() {
    }

    public MapArcOverride(
           com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity arcIdentity,
           boolean disabled) {
           this.arcIdentity = arcIdentity;
           this.disabled = disabled;
    }


    /**
     * Gets the arcIdentity value for this MapArcOverride.
     * 
     * @return arcIdentity
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity getArcIdentity() {
        return arcIdentity;
    }


    /**
     * Sets the arcIdentity value for this MapArcOverride.
     * 
     * @param arcIdentity
     */
    public void setArcIdentity(com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity arcIdentity) {
        this.arcIdentity = arcIdentity;
    }


    /**
     * Gets the disabled value for this MapArcOverride.
     * 
     * @return disabled
     */
    public boolean isDisabled() {
        return disabled;
    }


    /**
     * Sets the disabled value for this MapArcOverride.
     * 
     * @param disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapArcOverride)) return false;
        MapArcOverride other = (MapArcOverride) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.arcIdentity==null && other.getArcIdentity()==null) || 
             (this.arcIdentity!=null &&
              this.arcIdentity.equals(other.getArcIdentity()))) &&
            this.disabled == other.isDisabled();
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
        if (getArcIdentity() != null) {
            _hashCode += getArcIdentity().hashCode();
        }
        _hashCode += (isDisabled() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapArcOverride.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapArcOverride"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arcIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "arcIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapArcIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disabled");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "disabled"));
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
