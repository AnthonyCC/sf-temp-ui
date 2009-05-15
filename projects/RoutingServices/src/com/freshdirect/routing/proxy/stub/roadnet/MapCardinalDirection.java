/**
 * MapCardinalDirection.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapCardinalDirection implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected MapCardinalDirection(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _cdNorth = "cdNorth";
    public static final java.lang.String _cdSouth = "cdSouth";
    public static final java.lang.String _cdEast = "cdEast";
    public static final java.lang.String _cdWest = "cdWest";
    public static final java.lang.String _cdNorthEast = "cdNorthEast";
    public static final java.lang.String _cdNorthWest = "cdNorthWest";
    public static final java.lang.String _cdSouthEast = "cdSouthEast";
    public static final java.lang.String _cdSouthWest = "cdSouthWest";
    public static final java.lang.String _cdNone = "cdNone";
    public static final MapCardinalDirection cdNorth = new MapCardinalDirection(_cdNorth);
    public static final MapCardinalDirection cdSouth = new MapCardinalDirection(_cdSouth);
    public static final MapCardinalDirection cdEast = new MapCardinalDirection(_cdEast);
    public static final MapCardinalDirection cdWest = new MapCardinalDirection(_cdWest);
    public static final MapCardinalDirection cdNorthEast = new MapCardinalDirection(_cdNorthEast);
    public static final MapCardinalDirection cdNorthWest = new MapCardinalDirection(_cdNorthWest);
    public static final MapCardinalDirection cdSouthEast = new MapCardinalDirection(_cdSouthEast);
    public static final MapCardinalDirection cdSouthWest = new MapCardinalDirection(_cdSouthWest);
    public static final MapCardinalDirection cdNone = new MapCardinalDirection(_cdNone);
    public java.lang.String getValue() { return _value_;}
    public static MapCardinalDirection fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        MapCardinalDirection enumeration = (MapCardinalDirection)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static MapCardinalDirection fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapCardinalDirection.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapCardinalDirection"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
