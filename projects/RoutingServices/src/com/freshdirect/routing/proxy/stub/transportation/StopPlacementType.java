/**
 * StopPlacementType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class StopPlacementType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StopPlacementType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _sptNone = "sptNone";
    public static final java.lang.String _sptAutoSequence = "sptAutoSequence";
    public static final java.lang.String _sptBestInsertion = "sptBestInsertion";
    public static final java.lang.String _sptAfterStop = "sptAfterStop";
    public static final java.lang.String _sptFirst = "sptFirst";
    public static final java.lang.String _sptLast = "sptLast";
    public static final StopPlacementType sptNone = new StopPlacementType(_sptNone);
    public static final StopPlacementType sptAutoSequence = new StopPlacementType(_sptAutoSequence);
    public static final StopPlacementType sptBestInsertion = new StopPlacementType(_sptBestInsertion);
    public static final StopPlacementType sptAfterStop = new StopPlacementType(_sptAfterStop);
    public static final StopPlacementType sptFirst = new StopPlacementType(_sptFirst);
    public static final StopPlacementType sptLast = new StopPlacementType(_sptLast);
    public java.lang.String getValue() { return _value_;}
    public static StopPlacementType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        StopPlacementType enumeration = (StopPlacementType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static StopPlacementType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(StopPlacementType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopPlacementType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
