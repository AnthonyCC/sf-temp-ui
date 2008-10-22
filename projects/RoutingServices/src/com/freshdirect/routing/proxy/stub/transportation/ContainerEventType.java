/**
 * ContainerEventType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class ContainerEventType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ContainerEventType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _cetInitialAdd = "cetInitialAdd";
    public static final java.lang.String _cetLoaded = "cetLoaded";
    public static final java.lang.String _cetDeliveredDroppedOff = "cetDeliveredDroppedOff";
    public static final java.lang.String _cetDeliveredReturning = "cetDeliveredReturning";
    public static final java.lang.String _cetPickedUp = "cetPickedUp";
    public static final java.lang.String _cetNotLoaded = "cetNotLoaded";
    public static final java.lang.String _cetNotDelivered = "cetNotDelivered";
    public static final java.lang.String _cetNotPickedUp = "cetNotPickedUp";
    public static final java.lang.String _cetNotFoundAtLoading = "cetNotFoundAtLoading";
    public static final java.lang.String _cetNotFoundAtStop = "cetNotFoundAtStop";
    public static final java.lang.String _cetOnRoute = "cetOnRoute";
    public static final java.lang.String _cetReturn = "cetReturn";
    public static final java.lang.String _cetDeliveredToDifferentStop = "cetDeliveredToDifferentStop";
    public static final java.lang.String _cetPickedupFromDifferentStop = "cetPickedupFromDifferentStop";
    public static final java.lang.String _cetUnloaded = "cetUnloaded";
    public static final java.lang.String _cetNotFoundAtUnloading = "cetNotFoundAtUnloading";
    public static final ContainerEventType cetInitialAdd = new ContainerEventType(_cetInitialAdd);
    public static final ContainerEventType cetLoaded = new ContainerEventType(_cetLoaded);
    public static final ContainerEventType cetDeliveredDroppedOff = new ContainerEventType(_cetDeliveredDroppedOff);
    public static final ContainerEventType cetDeliveredReturning = new ContainerEventType(_cetDeliveredReturning);
    public static final ContainerEventType cetPickedUp = new ContainerEventType(_cetPickedUp);
    public static final ContainerEventType cetNotLoaded = new ContainerEventType(_cetNotLoaded);
    public static final ContainerEventType cetNotDelivered = new ContainerEventType(_cetNotDelivered);
    public static final ContainerEventType cetNotPickedUp = new ContainerEventType(_cetNotPickedUp);
    public static final ContainerEventType cetNotFoundAtLoading = new ContainerEventType(_cetNotFoundAtLoading);
    public static final ContainerEventType cetNotFoundAtStop = new ContainerEventType(_cetNotFoundAtStop);
    public static final ContainerEventType cetOnRoute = new ContainerEventType(_cetOnRoute);
    public static final ContainerEventType cetReturn = new ContainerEventType(_cetReturn);
    public static final ContainerEventType cetDeliveredToDifferentStop = new ContainerEventType(_cetDeliveredToDifferentStop);
    public static final ContainerEventType cetPickedupFromDifferentStop = new ContainerEventType(_cetPickedupFromDifferentStop);
    public static final ContainerEventType cetUnloaded = new ContainerEventType(_cetUnloaded);
    public static final ContainerEventType cetNotFoundAtUnloading = new ContainerEventType(_cetNotFoundAtUnloading);
    public java.lang.String getValue() { return _value_;}
    public static ContainerEventType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ContainerEventType enumeration = (ContainerEventType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ContainerEventType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ContainerEventType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ContainerEventType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
