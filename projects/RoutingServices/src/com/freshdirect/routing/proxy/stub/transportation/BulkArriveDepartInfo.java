/**
 * BulkArriveDepartInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class BulkArriveDepartInfo  extends com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo  implements java.io.Serializable {
    private java.util.Calendar actualArrival;

    public BulkArriveDepartInfo() {
    }

    public BulkArriveDepartInfo(
           com.freshdirect.routing.proxy.stub.transportation.RouteEventSource source,
           com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity,
           java.util.Calendar actualDeparture,
           com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality,
           double distanceToStop,
           com.freshdirect.routing.proxy.stub.transportation.DataQualityType distanceDataQuality,
           boolean undeliverable,
           java.lang.String undeliverableCode,
           com.freshdirect.routing.proxy.stub.transportation.StopIdentity redeliveryAfterStopIdentity,
           int delayMinutes,
           java.lang.String delayType,
           java.lang.String consignee,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions,
           java.util.Calendar actualArrival) {
        super(
            source,
            stopIdentity,
            actualDeparture,
            timeDataQuality,
            distanceToStop,
            distanceDataQuality,
            undeliverable,
            undeliverableCode,
            redeliveryAfterStopIdentity,
            delayMinutes,
            delayType,
            consignee,
            timeZoneOptions);
        this.actualArrival = actualArrival;
    }


    /**
     * Gets the actualArrival value for this BulkArriveDepartInfo.
     * 
     * @return actualArrival
     */
    public java.util.Calendar getActualArrival() {
        return actualArrival;
    }


    /**
     * Sets the actualArrival value for this BulkArriveDepartInfo.
     * 
     * @param actualArrival
     */
    public void setActualArrival(java.util.Calendar actualArrival) {
        this.actualArrival = actualArrival;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BulkArriveDepartInfo)) return false;
        BulkArriveDepartInfo other = (BulkArriveDepartInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.actualArrival==null && other.getActualArrival()==null) || 
             (this.actualArrival!=null &&
              this.actualArrival.equals(other.getActualArrival())));
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
        if (getActualArrival() != null) {
            _hashCode += getActualArrival().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BulkArriveDepartInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "BulkArriveDepartInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualArrival");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "actualArrival"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
