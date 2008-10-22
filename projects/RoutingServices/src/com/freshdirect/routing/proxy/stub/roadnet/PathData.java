/**
 * PathData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class PathData  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] shapePoints;

    private int[] destinationIndex;

    public PathData() {
    }

    public PathData(
           com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] shapePoints,
           int[] destinationIndex) {
           this.shapePoints = shapePoints;
           this.destinationIndex = destinationIndex;
    }


    /**
     * Gets the shapePoints value for this PathData.
     * 
     * @return shapePoints
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] getShapePoints() {
        return shapePoints;
    }


    /**
     * Sets the shapePoints value for this PathData.
     * 
     * @param shapePoints
     */
    public void setShapePoints(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] shapePoints) {
        this.shapePoints = shapePoints;
    }

    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint getShapePoints(int i) {
        return this.shapePoints[i];
    }

    public void setShapePoints(int i, com.freshdirect.routing.proxy.stub.roadnet.MapPoint _value) {
        this.shapePoints[i] = _value;
    }


    /**
     * Gets the destinationIndex value for this PathData.
     * 
     * @return destinationIndex
     */
    public int[] getDestinationIndex() {
        return destinationIndex;
    }


    /**
     * Sets the destinationIndex value for this PathData.
     * 
     * @param destinationIndex
     */
    public void setDestinationIndex(int[] destinationIndex) {
        this.destinationIndex = destinationIndex;
    }

    public int getDestinationIndex(int i) {
        return this.destinationIndex[i];
    }

    public void setDestinationIndex(int i, int _value) {
        this.destinationIndex[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PathData)) return false;
        PathData other = (PathData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.shapePoints==null && other.getShapePoints()==null) || 
             (this.shapePoints!=null &&
              java.util.Arrays.equals(this.shapePoints, other.getShapePoints()))) &&
            ((this.destinationIndex==null && other.getDestinationIndex()==null) || 
             (this.destinationIndex!=null &&
              java.util.Arrays.equals(this.destinationIndex, other.getDestinationIndex())));
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
        if (getShapePoints() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getShapePoints());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getShapePoints(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDestinationIndex() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestinationIndex());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestinationIndex(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PathData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "PathData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shapePoints");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "shapePoints"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPoint"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "destinationIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
