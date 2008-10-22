/**
 * StopType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class StopType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StopType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _stpNone = "stpNone";
    public static final java.lang.String _stpStop = "stpStop";
    public static final java.lang.String _stpPaidBreak = "stpPaidBreak";
    public static final java.lang.String _stpUnpaidBreak = "stpUnpaidBreak";
    public static final java.lang.String _stpPaidWait = "stpPaidWait";
    public static final java.lang.String _stpUnpaidWait = "stpUnpaidWait";
    public static final java.lang.String _stpPaidOvernight = "stpPaidOvernight";
    public static final java.lang.String _stpUnpaidOvernight = "stpUnpaidOvernight";
    public static final java.lang.String _stpDepot = "stpDepot";
    public static final java.lang.String _stpLoadStop = "stpLoadStop";
    public static final java.lang.String _stpLoadMeetPoint = "stpLoadMeetPoint";
    public static final java.lang.String _stpOrigin = "stpOrigin";
    public static final java.lang.String _stpDestination = "stpDestination";
    public static final java.lang.String _stpPreRoute = "stpPreRoute";
    public static final java.lang.String _stpPostRoute = "stpPostRoute";
    public static final StopType stpNone = new StopType(_stpNone);
    public static final StopType stpStop = new StopType(_stpStop);
    public static final StopType stpPaidBreak = new StopType(_stpPaidBreak);
    public static final StopType stpUnpaidBreak = new StopType(_stpUnpaidBreak);
    public static final StopType stpPaidWait = new StopType(_stpPaidWait);
    public static final StopType stpUnpaidWait = new StopType(_stpUnpaidWait);
    public static final StopType stpPaidOvernight = new StopType(_stpPaidOvernight);
    public static final StopType stpUnpaidOvernight = new StopType(_stpUnpaidOvernight);
    public static final StopType stpDepot = new StopType(_stpDepot);
    public static final StopType stpLoadStop = new StopType(_stpLoadStop);
    public static final StopType stpLoadMeetPoint = new StopType(_stpLoadMeetPoint);
    public static final StopType stpOrigin = new StopType(_stpOrigin);
    public static final StopType stpDestination = new StopType(_stpDestination);
    public static final StopType stpPreRoute = new StopType(_stpPreRoute);
    public static final StopType stpPostRoute = new StopType(_stpPostRoute);
    public java.lang.String getValue() { return _value_;}
    public static StopType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        StopType enumeration = (StopType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static StopType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(StopType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
