/**
 * ReportCriteriaType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class ReportCriteriaType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ReportCriteriaType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "ctPageNone";
    public static final java.lang.String _value2 = "ctPS-SessionsTerrWeeksDays";
    public static final java.lang.String _value3 = "ctPS-SessionsTerr";
    public static final java.lang.String _value4 = "ctPS-SessionAnalysis";
    public static final java.lang.String _value5 = "ctPS-Sessions";
    public static final java.lang.String _value6 = "ctPS-SessionsTerrWeeks";
    public static final java.lang.String _value7 = "ctDR-Routes";
    public static final java.lang.String _value8 = "ctDR-Sessions";
    public static final java.lang.String _value9 = "ctStdRoute";
    public static final java.lang.String _value10 = "ctDateRange";
    public static final java.lang.String _value11 = "ctRegion";
    public static final java.lang.String _value12 = "ctMC-DateRange";
    public static final ReportCriteriaType value1 = new ReportCriteriaType(_value1);
    public static final ReportCriteriaType value2 = new ReportCriteriaType(_value2);
    public static final ReportCriteriaType value3 = new ReportCriteriaType(_value3);
    public static final ReportCriteriaType value4 = new ReportCriteriaType(_value4);
    public static final ReportCriteriaType value5 = new ReportCriteriaType(_value5);
    public static final ReportCriteriaType value6 = new ReportCriteriaType(_value6);
    public static final ReportCriteriaType value7 = new ReportCriteriaType(_value7);
    public static final ReportCriteriaType value8 = new ReportCriteriaType(_value8);
    public static final ReportCriteriaType value9 = new ReportCriteriaType(_value9);
    public static final ReportCriteriaType value10 = new ReportCriteriaType(_value10);
    public static final ReportCriteriaType value11 = new ReportCriteriaType(_value11);
    public static final ReportCriteriaType value12 = new ReportCriteriaType(_value12);
    public java.lang.String getValue() { return _value_;}
    public static ReportCriteriaType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ReportCriteriaType enumeration = (ReportCriteriaType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ReportCriteriaType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ReportCriteriaType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ReportCriteriaType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
