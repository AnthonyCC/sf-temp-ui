/**
 * OriginDepartInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class OriginDepartInfo  extends com.freshdirect.routing.proxy.stub.transportation.RouteEventInfo  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private java.util.Calendar actualDeparture;

    private com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions;

    public OriginDepartInfo() {
    }

    public OriginDepartInfo(
           com.freshdirect.routing.proxy.stub.transportation.RouteEventSource source,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           java.util.Calendar actualDeparture,
           com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
        super(
            source);
        this.routeIdentity = routeIdentity;
        this.actualDeparture = actualDeparture;
        this.timeDataQuality = timeDataQuality;
        this.timeZoneOptions = timeZoneOptions;
    }


    /**
     * Gets the routeIdentity value for this OriginDepartInfo.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this OriginDepartInfo.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the actualDeparture value for this OriginDepartInfo.
     * 
     * @return actualDeparture
     */
    public java.util.Calendar getActualDeparture() {
        return actualDeparture;
    }


    /**
     * Sets the actualDeparture value for this OriginDepartInfo.
     * 
     * @param actualDeparture
     */
    public void setActualDeparture(java.util.Calendar actualDeparture) {
        this.actualDeparture = actualDeparture;
    }


    /**
     * Gets the timeDataQuality value for this OriginDepartInfo.
     * 
     * @return timeDataQuality
     */
    public com.freshdirect.routing.proxy.stub.transportation.DataQualityType getTimeDataQuality() {
        return timeDataQuality;
    }


    /**
     * Sets the timeDataQuality value for this OriginDepartInfo.
     * 
     * @param timeDataQuality
     */
    public void setTimeDataQuality(com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality) {
        this.timeDataQuality = timeDataQuality;
    }


    /**
     * Gets the timeZoneOptions value for this OriginDepartInfo.
     * 
     * @return timeZoneOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions getTimeZoneOptions() {
        return timeZoneOptions;
    }


    /**
     * Sets the timeZoneOptions value for this OriginDepartInfo.
     * 
     * @param timeZoneOptions
     */
    public void setTimeZoneOptions(com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
        this.timeZoneOptions = timeZoneOptions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OriginDepartInfo)) return false;
        OriginDepartInfo other = (OriginDepartInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.routeIdentity==null && other.getRouteIdentity()==null) || 
             (this.routeIdentity!=null &&
              this.routeIdentity.equals(other.getRouteIdentity()))) &&
            ((this.actualDeparture==null && other.getActualDeparture()==null) || 
             (this.actualDeparture!=null &&
              this.actualDeparture.equals(other.getActualDeparture()))) &&
            ((this.timeDataQuality==null && other.getTimeDataQuality()==null) || 
             (this.timeDataQuality!=null &&
              this.timeDataQuality.equals(other.getTimeDataQuality()))) &&
            ((this.timeZoneOptions==null && other.getTimeZoneOptions()==null) || 
             (this.timeZoneOptions!=null &&
              this.timeZoneOptions.equals(other.getTimeZoneOptions())));
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
        if (getRouteIdentity() != null) {
            _hashCode += getRouteIdentity().hashCode();
        }
        if (getActualDeparture() != null) {
            _hashCode += getActualDeparture().hashCode();
        }
        if (getTimeDataQuality() != null) {
            _hashCode += getTimeDataQuality().hashCode();
        }
        if (getTimeZoneOptions() != null) {
            _hashCode += getTimeZoneOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OriginDepartInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OriginDepartInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualDeparture");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualDeparture"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeDataQuality");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeDataQuality"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DataQualityType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZoneOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeZoneOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneOptions"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
