/**
 * DayOfWeek.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DayOfWeek implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected DayOfWeek(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _dowNone = "dowNone";
    public static final java.lang.String _dowSunday = "dowSunday";
    public static final java.lang.String _dowMonday = "dowMonday";
    public static final java.lang.String _dowTuesday = "dowTuesday";
    public static final java.lang.String _dowWednesday = "dowWednesday";
    public static final java.lang.String _dowThursday = "dowThursday";
    public static final java.lang.String _dowFriday = "dowFriday";
    public static final java.lang.String _dowSaturday = "dowSaturday";
    public static final DayOfWeek dowNone = new DayOfWeek(_dowNone);
    public static final DayOfWeek dowSunday = new DayOfWeek(_dowSunday);
    public static final DayOfWeek dowMonday = new DayOfWeek(_dowMonday);
    public static final DayOfWeek dowTuesday = new DayOfWeek(_dowTuesday);
    public static final DayOfWeek dowWednesday = new DayOfWeek(_dowWednesday);
    public static final DayOfWeek dowThursday = new DayOfWeek(_dowThursday);
    public static final DayOfWeek dowFriday = new DayOfWeek(_dowFriday);
    public static final DayOfWeek dowSaturday = new DayOfWeek(_dowSaturday);
    public java.lang.String getValue() { return _value_;}
    public static DayOfWeek fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        DayOfWeek enumeration = (DayOfWeek)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static DayOfWeek fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(DayOfWeek.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DayOfWeek"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
