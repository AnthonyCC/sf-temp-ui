/**
 * Arc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class Arc  extends com.freshdirect.routing.proxy.stub.roadnet.MapObject  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity arcIdentity;

    private int lineWidth;

    public Arc() {
    }

    public Arc(
           int color,
           com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity arcIdentity,
           int lineWidth) {
        super(
            color);
        this.arcIdentity = arcIdentity;
        this.lineWidth = lineWidth;
    }


    /**
     * Gets the arcIdentity value for this Arc.
     * 
     * @return arcIdentity
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity getArcIdentity() {
        return arcIdentity;
    }


    /**
     * Sets the arcIdentity value for this Arc.
     * 
     * @param arcIdentity
     */
    public void setArcIdentity(com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity arcIdentity) {
        this.arcIdentity = arcIdentity;
    }


    /**
     * Gets the lineWidth value for this Arc.
     * 
     * @return lineWidth
     */
    public int getLineWidth() {
        return lineWidth;
    }


    /**
     * Sets the lineWidth value for this Arc.
     * 
     * @param lineWidth
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Arc)) return false;
        Arc other = (Arc) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.arcIdentity==null && other.getArcIdentity()==null) || 
             (this.arcIdentity!=null &&
              this.arcIdentity.equals(other.getArcIdentity()))) &&
            this.lineWidth == other.getLineWidth();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getArcIdentity() != null) {
            _hashCode += getArcIdentity().hashCode();
        }
        _hashCode += getLineWidth();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Arc.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "Arc"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arcIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "arcIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapArcIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineWidth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "lineWidth"));
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
