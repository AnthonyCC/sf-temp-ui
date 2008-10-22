/**
 * ReportType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class ReportType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ReportType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _rtUR = "rtUR";
    public static final java.lang.String _rtRN = "rtRN";
    public static final java.lang.String _rtTP = "rtTP";
    public static final java.lang.String _rtFL = "rtFL";
    public static final java.lang.String _rtDP = "rtDP";
    public static final java.lang.String _rtRIC = "rtRIC";
    public static final java.lang.String _rtRICUR = "rtRICUR";
    public static final ReportType rtUR = new ReportType(_rtUR);
    public static final ReportType rtRN = new ReportType(_rtRN);
    public static final ReportType rtTP = new ReportType(_rtTP);
    public static final ReportType rtFL = new ReportType(_rtFL);
    public static final ReportType rtDP = new ReportType(_rtDP);
    public static final ReportType rtRIC = new ReportType(_rtRIC);
    public static final ReportType rtRICUR = new ReportType(_rtRICUR);
    public java.lang.String getValue() { return _value_;}
    public static ReportType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ReportType enumeration = (ReportType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ReportType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ReportType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ReportType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
