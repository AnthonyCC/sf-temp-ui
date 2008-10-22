/**
 * WirelessProvider.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class WirelessProvider implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected WirelessProvider(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _wpNone = "wpNone";
    public static final java.lang.String _wpQualcomm = "wpQualcomm";
    public static final java.lang.String _wpRNMobile = "wpRNMobile";
    public static final java.lang.String _wpWAP = "wpWAP";
    public static final java.lang.String _wpJavaMobile = "wpJavaMobile";
    public static final java.lang.String _wpBroadbeam = "wpBroadbeam";
    public static final java.lang.String _wpITrak = "wpITrak";
    public static final java.lang.String _wpCustom = "wpCustom";
    public static final java.lang.String _wpHHCommunicator = "wpHHCommunicator";
    public static final WirelessProvider wpNone = new WirelessProvider(_wpNone);
    public static final WirelessProvider wpQualcomm = new WirelessProvider(_wpQualcomm);
    public static final WirelessProvider wpRNMobile = new WirelessProvider(_wpRNMobile);
    public static final WirelessProvider wpWAP = new WirelessProvider(_wpWAP);
    public static final WirelessProvider wpJavaMobile = new WirelessProvider(_wpJavaMobile);
    public static final WirelessProvider wpBroadbeam = new WirelessProvider(_wpBroadbeam);
    public static final WirelessProvider wpITrak = new WirelessProvider(_wpITrak);
    public static final WirelessProvider wpCustom = new WirelessProvider(_wpCustom);
    public static final WirelessProvider wpHHCommunicator = new WirelessProvider(_wpHHCommunicator);
    public java.lang.String getValue() { return _value_;}
    public static WirelessProvider fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        WirelessProvider enumeration = (WirelessProvider)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static WirelessProvider fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(WirelessProvider.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "WirelessProvider"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
