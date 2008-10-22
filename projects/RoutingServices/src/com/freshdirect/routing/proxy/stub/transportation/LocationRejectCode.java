/**
 * LocationRejectCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class LocationRejectCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected LocationRejectCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _lrcDatabaseError = "lrcDatabaseError";
    public static final java.lang.String _lrcInvalidLocationID = "lrcInvalidLocationID";
    public static final java.lang.String _lrcInvalidLocationType = "lrcInvalidLocationType";
    public static final java.lang.String _lrcInvalidServiceTimeType = "lrcInvalidServiceTimeType";
    public static final java.lang.String _lrcInvalidTimeWindowType = "lrcInvalidTimeWindowType";
    public static final LocationRejectCode lrcDatabaseError = new LocationRejectCode(_lrcDatabaseError);
    public static final LocationRejectCode lrcInvalidLocationID = new LocationRejectCode(_lrcInvalidLocationID);
    public static final LocationRejectCode lrcInvalidLocationType = new LocationRejectCode(_lrcInvalidLocationType);
    public static final LocationRejectCode lrcInvalidServiceTimeType = new LocationRejectCode(_lrcInvalidServiceTimeType);
    public static final LocationRejectCode lrcInvalidTimeWindowType = new LocationRejectCode(_lrcInvalidTimeWindowType);
    public java.lang.String getValue() { return _value_;}
    public static LocationRejectCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        LocationRejectCode enumeration = (LocationRejectCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static LocationRejectCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(LocationRejectCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationRejectCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
