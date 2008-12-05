/**
 * SchedulerBalancingFactor.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerBalancingFactor implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected SchedulerBalancingFactor(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _sbfNumStops = "sbfNumStops";
    public static final java.lang.String _sbfQuantity = "sbfQuantity";
    public static final java.lang.String _sbfCost = "sbfCost";
    public static final java.lang.String _sbfWorkingTime = "sbfWorkingTime";
    public static final SchedulerBalancingFactor sbfNumStops = new SchedulerBalancingFactor(_sbfNumStops);
    public static final SchedulerBalancingFactor sbfQuantity = new SchedulerBalancingFactor(_sbfQuantity);
    public static final SchedulerBalancingFactor sbfCost = new SchedulerBalancingFactor(_sbfCost);
    public static final SchedulerBalancingFactor sbfWorkingTime = new SchedulerBalancingFactor(_sbfWorkingTime);
    public java.lang.String getValue() { return _value_;}
    public static SchedulerBalancingFactor fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        SchedulerBalancingFactor enumeration = (SchedulerBalancingFactor)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static SchedulerBalancingFactor fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(SchedulerBalancingFactor.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerBalancingFactor"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
