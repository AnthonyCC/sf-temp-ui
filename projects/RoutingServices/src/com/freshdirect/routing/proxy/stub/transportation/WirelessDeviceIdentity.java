/**
 * WirelessDeviceIdentity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class WirelessDeviceIdentity  extends com.freshdirect.routing.proxy.stub.transportation.RegionIdentity  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.WirelessProvider wirelessProvider;

    private java.lang.String wirelessID;

    public WirelessDeviceIdentity() {
    }

    public WirelessDeviceIdentity(
           java.lang.String regionID,
           com.freshdirect.routing.proxy.stub.transportation.WirelessProvider wirelessProvider,
           java.lang.String wirelessID) {
        super(
            regionID);
        this.wirelessProvider = wirelessProvider;
        this.wirelessID = wirelessID;
    }


    /**
     * Gets the wirelessProvider value for this WirelessDeviceIdentity.
     * 
     * @return wirelessProvider
     */
    public com.freshdirect.routing.proxy.stub.transportation.WirelessProvider getWirelessProvider() {
        return wirelessProvider;
    }


    /**
     * Sets the wirelessProvider value for this WirelessDeviceIdentity.
     * 
     * @param wirelessProvider
     */
    public void setWirelessProvider(com.freshdirect.routing.proxy.stub.transportation.WirelessProvider wirelessProvider) {
        this.wirelessProvider = wirelessProvider;
    }


    /**
     * Gets the wirelessID value for this WirelessDeviceIdentity.
     * 
     * @return wirelessID
     */
    public java.lang.String getWirelessID() {
        return wirelessID;
    }


    /**
     * Sets the wirelessID value for this WirelessDeviceIdentity.
     * 
     * @param wirelessID
     */
    public void setWirelessID(java.lang.String wirelessID) {
        this.wirelessID = wirelessID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WirelessDeviceIdentity)) return false;
        WirelessDeviceIdentity other = (WirelessDeviceIdentity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.wirelessProvider==null && other.getWirelessProvider()==null) || 
             (this.wirelessProvider!=null &&
              this.wirelessProvider.equals(other.getWirelessProvider()))) &&
            ((this.wirelessID==null && other.getWirelessID()==null) || 
             (this.wirelessID!=null &&
              this.wirelessID.equals(other.getWirelessID())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getWirelessProvider() != null) {
            _hashCode += getWirelessProvider().hashCode();
        }
        if (getWirelessID() != null) {
            _hashCode += getWirelessID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WirelessDeviceIdentity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "WirelessDeviceIdentity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wirelessProvider");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "wirelessProvider"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "WirelessProvider"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wirelessID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "wirelessID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
