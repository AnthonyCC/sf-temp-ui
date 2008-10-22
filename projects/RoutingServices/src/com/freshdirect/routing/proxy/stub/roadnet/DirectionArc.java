/**
 * DirectionArc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class DirectionArc  implements java.io.Serializable {
    private java.lang.String instruction;

    private int time;

    private double distance;

    private com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] vertices;

    public DirectionArc() {
    }

    public DirectionArc(
           java.lang.String instruction,
           int time,
           double distance,
           com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] vertices) {
           this.instruction = instruction;
           this.time = time;
           this.distance = distance;
           this.vertices = vertices;
    }


    /**
     * Gets the instruction value for this DirectionArc.
     * 
     * @return instruction
     */
    public java.lang.String getInstruction() {
        return instruction;
    }


    /**
     * Sets the instruction value for this DirectionArc.
     * 
     * @param instruction
     */
    public void setInstruction(java.lang.String instruction) {
        this.instruction = instruction;
    }


    /**
     * Gets the time value for this DirectionArc.
     * 
     * @return time
     */
    public int getTime() {
        return time;
    }


    /**
     * Sets the time value for this DirectionArc.
     * 
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }


    /**
     * Gets the distance value for this DirectionArc.
     * 
     * @return distance
     */
    public double getDistance() {
        return distance;
    }


    /**
     * Sets the distance value for this DirectionArc.
     * 
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }


    /**
     * Gets the vertices value for this DirectionArc.
     * 
     * @return vertices
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] getVertices() {
        return vertices;
    }


    /**
     * Sets the vertices value for this DirectionArc.
     * 
     * @param vertices
     */
    public void setVertices(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] vertices) {
        this.vertices = vertices;
    }

    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint getVertices(int i) {
        return this.vertices[i];
    }

    public void setVertices(int i, com.freshdirect.routing.proxy.stub.roadnet.MapPoint _value) {
        this.vertices[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DirectionArc)) return false;
        DirectionArc other = (DirectionArc) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.instruction==null && other.getInstruction()==null) || 
             (this.instruction!=null &&
              this.instruction.equals(other.getInstruction()))) &&
            this.time == other.getTime() &&
            this.distance == other.getDistance() &&
            ((this.vertices==null && other.getVertices()==null) || 
             (this.vertices!=null &&
              java.util.Arrays.equals(this.vertices, other.getVertices())));
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
        if (getInstruction() != null) {
            _hashCode += getInstruction().hashCode();
        }
        _hashCode += getTime();
        _hashCode += new Double(getDistance()).hashCode();
        if (getVertices() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVertices());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVertices(), i);
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
        new org.apache.axis.description.TypeDesc(DirectionArc.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "DirectionArc"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instruction");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "instruction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("vertices");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "vertices"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPoint"));
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
