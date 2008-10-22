/**
 * GeocodeResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class GeocodeResult implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected GeocodeResult(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _grAuto = "grAuto";
    public static final java.lang.String _grManual = "grManual";
    public static final java.lang.String _grGPS = "grGPS";
    public static final java.lang.String _grCity = "grCity";
    public static final java.lang.String _grZip = "grZip";
    public static final java.lang.String _grZipPlus4 = "grZipPlus4";
    public static final java.lang.String _grUnsuccessful = "grUnsuccessful";
    public static final GeocodeResult grAuto = new GeocodeResult(_grAuto);
    public static final GeocodeResult grManual = new GeocodeResult(_grManual);
    public static final GeocodeResult grGPS = new GeocodeResult(_grGPS);
    public static final GeocodeResult grCity = new GeocodeResult(_grCity);
    public static final GeocodeResult grZip = new GeocodeResult(_grZip);
    public static final GeocodeResult grZipPlus4 = new GeocodeResult(_grZipPlus4);
    public static final GeocodeResult grUnsuccessful = new GeocodeResult(_grUnsuccessful);
    public java.lang.String getValue() { return _value_;}
    public static GeocodeResult fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        GeocodeResult enumeration = (GeocodeResult)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static GeocodeResult fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(GeocodeResult.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "GeocodeResult"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
