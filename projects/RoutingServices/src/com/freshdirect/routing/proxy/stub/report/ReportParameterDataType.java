/**
 * ReportParameterDataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.report;

public class ReportParameterDataType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ReportParameterDataType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _rptNone = "rptNone";
    public static final java.lang.String _rptString = "rptString";
    public static final java.lang.String _rptNumber = "rptNumber";
    public static final java.lang.String _rptDate = "rptDate";
    public static final java.lang.String _rptDateTime = "rptDateTime";
    public static final java.lang.String _rptBoolean = "rptBoolean";
    public static final ReportParameterDataType rptNone = new ReportParameterDataType(_rptNone);
    public static final ReportParameterDataType rptString = new ReportParameterDataType(_rptString);
    public static final ReportParameterDataType rptNumber = new ReportParameterDataType(_rptNumber);
    public static final ReportParameterDataType rptDate = new ReportParameterDataType(_rptDate);
    public static final ReportParameterDataType rptDateTime = new ReportParameterDataType(_rptDateTime);
    public static final ReportParameterDataType rptBoolean = new ReportParameterDataType(_rptBoolean);
    public java.lang.String getValue() { return _value_;}
    public static ReportParameterDataType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ReportParameterDataType enumeration = (ReportParameterDataType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ReportParameterDataType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ReportParameterDataType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportParameterDataType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
