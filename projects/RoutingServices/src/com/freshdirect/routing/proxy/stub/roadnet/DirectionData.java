/**
 * DirectionData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class DirectionData  implements java.io.Serializable {
    private int time;

    private double distance;

    private com.freshdirect.routing.proxy.stub.roadnet.PathDirections[] pathDirections;

    private com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents;

    public DirectionData() {
    }

    public DirectionData(
           int time,
           double distance,
           com.freshdirect.routing.proxy.stub.roadnet.PathDirections[] pathDirections,
           com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents) {
           this.time = time;
           this.distance = distance;
           this.pathDirections = pathDirections;
           this.extents = extents;
    }


    /**
     * Gets the time value for this DirectionData.
     * 
     * @return time
     */
    public int getTime() {
        return time;
    }


    /**
     * Sets the time value for this DirectionData.
     * 
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }


    /**
     * Gets the distance value for this DirectionData.
     * 
     * @return distance
     */
    public double getDistance() {
        return distance;
    }


    /**
     * Sets the distance value for this DirectionData.
     * 
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }


    /**
     * Gets the pathDirections value for this DirectionData.
     * 
     * @return pathDirections
     */
    public com.freshdirect.routing.proxy.stub.roadnet.PathDirections[] getPathDirections() {
        return pathDirections;
    }


    /**
     * Sets the pathDirections value for this DirectionData.
     * 
     * @param pathDirections
     */
    public void setPathDirections(com.freshdirect.routing.proxy.stub.roadnet.PathDirections[] pathDirections) {
        this.pathDirections = pathDirections;
    }

    public com.freshdirect.routing.proxy.stub.roadnet.PathDirections getPathDirections(int i) {
        return this.pathDirections[i];
    }

    public void setPathDirections(int i, com.freshdirect.routing.proxy.stub.roadnet.PathDirections _value) {
        this.pathDirections[i] = _value;
    }


    /**
     * Gets the extents value for this DirectionData.
     * 
     * @return extents
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapExtents getExtents() {
        return extents;
    }


    /**
     * Sets the extents value for this DirectionData.
     * 
     * @param extents
     */
    public void setExtents(com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents) {
        this.extents = extents;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DirectionData)) return false;
        DirectionData other = (DirectionData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.time == other.getTime() &&
            this.distance == other.getDistance() &&
            ((this.pathDirections==null && other.getPathDirections()==null) || 
             (this.pathDirections!=null &&
              java.util.Arrays.equals(this.pathDirections, other.getPathDirections()))) &&
            ((this.extents==null && other.getExtents()==null) || 
             (this.extents!=null &&
              this.extents.equals(other.getExtents())));
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
        _hashCode += getTime();
        _hashCode += new Double(getDistance()).hashCode();
        if (getPathDirections() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPathDirections());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPathDirections(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExtents() != null) {
            _hashCode += getExtents().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DirectionData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "DirectionData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("time");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "distance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pathDirections");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "pathDirections"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "PathDirections"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "extents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapExtents"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
