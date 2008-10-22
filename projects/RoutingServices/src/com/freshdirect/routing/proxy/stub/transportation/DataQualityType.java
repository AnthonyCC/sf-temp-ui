/**
 * DataQualityType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DataQualityType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected DataQualityType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _dqtUnknown = "dqtUnknown";
    public static final java.lang.String _dqtAssumedFromProjection = "dqtAssumedFromProjection";
    public static final java.lang.String _dqtDriverEntered = "dqtDriverEntered";
    public static final java.lang.String _dqtDispatcherEntered = "dqtDispatcherEntered";
    public static final java.lang.String _dqtAutoCaptured = "dqtAutoCaptured";
    public static final java.lang.String _dqtGeoComputed = "dqtGeoComputed";
    public static final java.lang.String _dqtGeoComputedCluster = "dqtGeoComputedCluster";
    public static final DataQualityType dqtUnknown = new DataQualityType(_dqtUnknown);
    public static final DataQualityType dqtAssumedFromProjection = new DataQualityType(_dqtAssumedFromProjection);
    public static final DataQualityType dqtDriverEntered = new DataQualityType(_dqtDriverEntered);
    public static final DataQualityType dqtDispatcherEntered = new DataQualityType(_dqtDispatcherEntered);
    public static final DataQualityType dqtAutoCaptured = new DataQualityType(_dqtAutoCaptured);
    public static final DataQualityType dqtGeoComputed = new DataQualityType(_dqtGeoComputed);
    public static final DataQualityType dqtGeoComputedCluster = new DataQualityType(_dqtGeoComputedCluster);
    public java.lang.String getValue() { return _value_;}
    public static DataQualityType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        DataQualityType enumeration = (DataQualityType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static DataQualityType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(DataQualityType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DataQualityType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
