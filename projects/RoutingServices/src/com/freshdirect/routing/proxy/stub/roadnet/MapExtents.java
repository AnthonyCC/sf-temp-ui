/**
 * MapExtents.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapExtents  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.roadnet.MapPoint upperLeft;

    private com.freshdirect.routing.proxy.stub.roadnet.MapPoint lowerRight;

    public MapExtents() {
    }

    public MapExtents(
           com.freshdirect.routing.proxy.stub.roadnet.MapPoint upperLeft,
           com.freshdirect.routing.proxy.stub.roadnet.MapPoint lowerRight) {
           this.upperLeft = upperLeft;
           this.lowerRight = lowerRight;
    }


    /**
     * Gets the upperLeft value for this MapExtents.
     * 
     * @return upperLeft
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint getUpperLeft() {
        return upperLeft;
    }


    /**
     * Sets the upperLeft value for this MapExtents.
     * 
     * @param upperLeft
     */
    public void setUpperLeft(com.freshdirect.routing.proxy.stub.roadnet.MapPoint upperLeft) {
        this.upperLeft = upperLeft;
    }


    /**
     * Gets the lowerRight value for this MapExtents.
     * 
     * @return lowerRight
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint getLowerRight() {
        return lowerRight;
    }


    /**
     * Sets the lowerRight value for this MapExtents.
     * 
     * @param lowerRight
     */
    public void setLowerRight(com.freshdirect.routing.proxy.stub.roadnet.MapPoint lowerRight) {
        this.lowerRight = lowerRight;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapExtents)) return false;
        MapExtents other = (MapExtents) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.upperLeft==null && other.getUpperLeft()==null) || 
             (this.upperLeft!=null &&
              this.upperLeft.equals(other.getUpperLeft()))) &&
            ((this.lowerRight==null && other.getLowerRight()==null) || 
             (this.lowerRight!=null &&
              this.lowerRight.equals(other.getLowerRight())));
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
        if (getUpperLeft() != null) {
            _hashCode += getUpperLeft().hashCode();
        }
        if (getLowerRight() != null) {
            _hashCode += getLowerRight().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapExtents.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapExtents"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("upperLeft");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "upperLeft"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPoint"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lowerRight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "lowerRight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPoint"));
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
