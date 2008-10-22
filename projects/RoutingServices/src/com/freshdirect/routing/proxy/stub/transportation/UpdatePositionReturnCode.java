/**
 * UpdatePositionReturnCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class UpdatePositionReturnCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected UpdatePositionReturnCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _uprcUnknown = "uprcUnknown";
    public static final java.lang.String _uprcValid = "uprcValid";
    public static final java.lang.String _uprcZeroLatLong = "uprcZeroLatLong";
    public static final java.lang.String _uprcBadSpeed = "uprcBadSpeed";
    public static final java.lang.String _uprcUnreasonable = "uprcUnreasonable";
    public static final UpdatePositionReturnCode uprcUnknown = new UpdatePositionReturnCode(_uprcUnknown);
    public static final UpdatePositionReturnCode uprcValid = new UpdatePositionReturnCode(_uprcValid);
    public static final UpdatePositionReturnCode uprcZeroLatLong = new UpdatePositionReturnCode(_uprcZeroLatLong);
    public static final UpdatePositionReturnCode uprcBadSpeed = new UpdatePositionReturnCode(_uprcBadSpeed);
    public static final UpdatePositionReturnCode uprcUnreasonable = new UpdatePositionReturnCode(_uprcUnreasonable);
    public java.lang.String getValue() { return _value_;}
    public static UpdatePositionReturnCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        UpdatePositionReturnCode enumeration = (UpdatePositionReturnCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static UpdatePositionReturnCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(UpdatePositionReturnCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UpdatePositionReturnCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
