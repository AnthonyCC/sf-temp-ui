/**
 * MapPointDepArrTimes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapPointDepArrTimes  implements java.io.Serializable {
    private java.util.Calendar departure;

    private java.util.Calendar arrival;

    private com.freshdirect.routing.proxy.stub.roadnet.TimeZoneOptions timeZoneOptions;

    private com.freshdirect.routing.proxy.stub.roadnet.TimeZoneValue localTimeZone;

    public MapPointDepArrTimes() {
    }

    public MapPointDepArrTimes(
           java.util.Calendar departure,
           java.util.Calendar arrival,
           com.freshdirect.routing.proxy.stub.roadnet.TimeZoneOptions timeZoneOptions,
           com.freshdirect.routing.proxy.stub.roadnet.TimeZoneValue localTimeZone) {
           this.departure = departure;
           this.arrival = arrival;
           this.timeZoneOptions = timeZoneOptions;
           this.localTimeZone = localTimeZone;
    }


    /**
     * Gets the departure value for this MapPointDepArrTimes.
     * 
     * @return departure
     */
    public java.util.Calendar getDeparture() {
        return departure;
    }


    /**
     * Sets the departure value for this MapPointDepArrTimes.
     * 
     * @param departure
     */
    public void setDeparture(java.util.Calendar departure) {
        this.departure = departure;
    }


    /**
     * Gets the arrival value for this MapPointDepArrTimes.
     * 
     * @return arrival
     */
    public java.util.Calendar getArrival() {
        return arrival;
    }


    /**
     * Sets the arrival value for this MapPointDepArrTimes.
     * 
     * @param arrival
     */
    public void setArrival(java.util.Calendar arrival) {
        this.arrival = arrival;
    }


    /**
     * Gets the timeZoneOptions value for this MapPointDepArrTimes.
     * 
     * @return timeZoneOptions
     */
    public com.freshdirect.routing.proxy.stub.roadnet.TimeZoneOptions getTimeZoneOptions() {
        return timeZoneOptions;
    }


    /**
     * Sets the timeZoneOptions value for this MapPointDepArrTimes.
     * 
     * @param timeZoneOptions
     */
    public void setTimeZoneOptions(com.freshdirect.routing.proxy.stub.roadnet.TimeZoneOptions timeZoneOptions) {
        this.timeZoneOptions = timeZoneOptions;
    }


    /**
     * Gets the localTimeZone value for this MapPointDepArrTimes.
     * 
     * @return localTimeZone
     */
    public com.freshdirect.routing.proxy.stub.roadnet.TimeZoneValue getLocalTimeZone() {
        return localTimeZone;
    }


    /**
     * Sets the localTimeZone value for this MapPointDepArrTimes.
     * 
     * @param localTimeZone
     */
    public void setLocalTimeZone(com.freshdirect.routing.proxy.stub.roadnet.TimeZoneValue localTimeZone) {
        this.localTimeZone = localTimeZone;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapPointDepArrTimes)) return false;
        MapPointDepArrTimes other = (MapPointDepArrTimes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.departure==null && other.getDeparture()==null) || 
             (this.departure!=null &&
              this.departure.equals(other.getDeparture()))) &&
            ((this.arrival==null && other.getArrival()==null) || 
             (this.arrival!=null &&
              this.arrival.equals(other.getArrival()))) &&
            ((this.timeZoneOptions==null && other.getTimeZoneOptions()==null) || 
             (this.timeZoneOptions!=null &&
              this.timeZoneOptions.equals(other.getTimeZoneOptions()))) &&
            ((this.localTimeZone==null && other.getLocalTimeZone()==null) || 
             (this.localTimeZone!=null &&
              this.localTimeZone.equals(other.getLocalTimeZone())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDeparture() != null) {
            _hashCode += getDeparture().hashCode();
        }
        if (getArrival() != null) {
            _hashCode += getArrival().hashCode();
        }
        if (getTimeZoneOptions() != null) {
            _hashCode += getTimeZoneOptions().hashCode();
        }
        if (getLocalTimeZone() != null) {
            _hashCode += getLocalTimeZone().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapPointDepArrTimes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPointDepArrTimes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("departure");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "departure"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arrival");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "arrival"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZoneOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "timeZoneOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "TimeZoneOptions"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localTimeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "localTimeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "TimeZoneValue"));
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
