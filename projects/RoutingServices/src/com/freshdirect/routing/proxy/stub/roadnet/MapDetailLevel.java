/**
 * MapDetailLevel.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapDetailLevel implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected MapDetailLevel(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _mdlLeast = "mdlLeast";
    public static final java.lang.String _mdlLess = "mdlLess";
    public static final java.lang.String _mdlAuto = "mdlAuto";
    public static final java.lang.String _mdlMore = "mdlMore";
    public static final java.lang.String _mdlMost = "mdlMost";
    public static final MapDetailLevel mdlLeast = new MapDetailLevel(_mdlLeast);
    public static final MapDetailLevel mdlLess = new MapDetailLevel(_mdlLess);
    public static final MapDetailLevel mdlAuto = new MapDetailLevel(_mdlAuto);
    public static final MapDetailLevel mdlMore = new MapDetailLevel(_mdlMore);
    public static final MapDetailLevel mdlMost = new MapDetailLevel(_mdlMost);
    public java.lang.String getValue() { return _value_;}
    public static MapDetailLevel fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        MapDetailLevel enumeration = (MapDetailLevel)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static MapDetailLevel fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(MapDetailLevel.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapDetailLevel"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
