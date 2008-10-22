/**
 * RoutingOrderType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingOrderType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected RoutingOrderType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _rotOrder = "rotOrder";
    public static final java.lang.String _rotPickup = "rotPickup";
    public static final java.lang.String _rotFullReload = "rotFullReload";
    public static final java.lang.String _rotAsNeededReload = "rotAsNeededReload";
    public static final java.lang.String _rotEmpty = "rotEmpty";
    public static final RoutingOrderType rotOrder = new RoutingOrderType(_rotOrder);
    public static final RoutingOrderType rotPickup = new RoutingOrderType(_rotPickup);
    public static final RoutingOrderType rotFullReload = new RoutingOrderType(_rotFullReload);
    public static final RoutingOrderType rotAsNeededReload = new RoutingOrderType(_rotAsNeededReload);
    public static final RoutingOrderType rotEmpty = new RoutingOrderType(_rotEmpty);
    public java.lang.String getValue() { return _value_;}
    public static RoutingOrderType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        RoutingOrderType enumeration = (RoutingOrderType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static RoutingOrderType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(RoutingOrderType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrderType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
