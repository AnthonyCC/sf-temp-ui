/**
 * RoutingImportOrderRejectCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingImportOrderRejectCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected RoutingImportOrderRejectCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _riorcUnknownError = "riorcUnknownError";
    public static final java.lang.String _riorcDatabaseError = "riorcDatabaseError";
    public static final java.lang.String _riorcInvalidSessionKey = "riorcInvalidSessionKey";
    public static final java.lang.String _riorcSessionLocked = "riorcSessionLocked";
    public static final java.lang.String _riorcInvalidLocation = "riorcInvalidLocation";
    public static final java.lang.String _riorcDuplicateOrder = "riorcDuplicateOrder";
    public static final java.lang.String _riorcFailAddToBuiltRoute = "riorcFailAddToBuiltRoute";
    public static final java.lang.String _riorcFailModifyBuiltRoute = "riorcFailModifyBuiltRoute";
    public static final java.lang.String _riorcFailManualOrder = "riorcFailManualOrder";
    public static final java.lang.String _riorcFailSplitOrder = "riorcFailSplitOrder";
    public static final java.lang.String _riorcFailOrderNumberRequired = "riorcFailOrderNumberRequired";
    public static final RoutingImportOrderRejectCode riorcUnknownError = new RoutingImportOrderRejectCode(_riorcUnknownError);
    public static final RoutingImportOrderRejectCode riorcDatabaseError = new RoutingImportOrderRejectCode(_riorcDatabaseError);
    public static final RoutingImportOrderRejectCode riorcInvalidSessionKey = new RoutingImportOrderRejectCode(_riorcInvalidSessionKey);
    public static final RoutingImportOrderRejectCode riorcSessionLocked = new RoutingImportOrderRejectCode(_riorcSessionLocked);
    public static final RoutingImportOrderRejectCode riorcInvalidLocation = new RoutingImportOrderRejectCode(_riorcInvalidLocation);
    public static final RoutingImportOrderRejectCode riorcDuplicateOrder = new RoutingImportOrderRejectCode(_riorcDuplicateOrder);
    public static final RoutingImportOrderRejectCode riorcFailAddToBuiltRoute = new RoutingImportOrderRejectCode(_riorcFailAddToBuiltRoute);
    public static final RoutingImportOrderRejectCode riorcFailModifyBuiltRoute = new RoutingImportOrderRejectCode(_riorcFailModifyBuiltRoute);
    public static final RoutingImportOrderRejectCode riorcFailManualOrder = new RoutingImportOrderRejectCode(_riorcFailManualOrder);
    public static final RoutingImportOrderRejectCode riorcFailSplitOrder = new RoutingImportOrderRejectCode(_riorcFailSplitOrder);
    public static final RoutingImportOrderRejectCode riorcFailOrderNumberRequired = new RoutingImportOrderRejectCode(_riorcFailOrderNumberRequired);
    public java.lang.String getValue() { return _value_;}
    public static RoutingImportOrderRejectCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        RoutingImportOrderRejectCode enumeration = (RoutingImportOrderRejectCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static RoutingImportOrderRejectCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(RoutingImportOrderRejectCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingImportOrderRejectCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
