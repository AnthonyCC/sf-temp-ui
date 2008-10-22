/**
 * MapContext.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapContext  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents;

    private int zoomLevel;

    public MapContext() {
    }

    public MapContext(
           com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents,
           int zoomLevel) {
           this.extents = extents;
           this.zoomLevel = zoomLevel;
    }


    /**
     * Gets the extents value for this MapContext.
     * 
     * @return extents
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapExtents getExtents() {
        return extents;
    }


    /**
     * Sets the extents value for this MapContext.
     * 
     * @param extents
     */
    public void setExtents(com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents) {
        this.extents = extents;
    }


    /**
     * Gets the zoomLevel value for this MapContext.
     * 
     * @return zoomLevel
     */
    public int getZoomLevel() {
        return zoomLevel;
    }


    /**
     * Sets the zoomLevel value for this MapContext.
     * 
     * @param zoomLevel
     */
    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapContext)) return false;
        MapContext other = (MapContext) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.extents==null && other.getExtents()==null) || 
             (this.extents!=null &&
              this.extents.equals(other.getExtents()))) &&
            this.zoomLevel == other.getZoomLevel();
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
        if (getExtents() != null) {
            _hashCode += getExtents().hashCode();
        }
        _hashCode += getZoomLevel();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapContext.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapContext"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "extents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapExtents"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zoomLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "zoomLevel"));
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
