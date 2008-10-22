/**
 * RoutePositionInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutePositionInfo  extends com.freshdirect.routing.proxy.stub.transportation.RouteEventInfo  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private int latitude;

    private int longitude;

    private java.util.Calendar positionTime;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions;

    private com.freshdirect.routing.proxy.stub.transportation.UnitSpeed unitOfSpeed;

    private double speed;

    public RoutePositionInfo() {
    }

    public RoutePositionInfo(
           com.freshdirect.routing.proxy.stub.transportation.RouteEventSource source,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           int latitude,
           int longitude,
           java.util.Calendar positionTime,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions,
           com.freshdirect.routing.proxy.stub.transportation.UnitSpeed unitOfSpeed,
           double speed) {
        super(
            source);
        this.routeIdentity = routeIdentity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.positionTime = positionTime;
        this.timeZoneOptions = timeZoneOptions;
        this.unitOfSpeed = unitOfSpeed;
        this.speed = speed;
    }


    /**
     * Gets the routeIdentity value for this RoutePositionInfo.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this RoutePositionInfo.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the latitude value for this RoutePositionInfo.
     * 
     * @return latitude
     */
    public int getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude value for this RoutePositionInfo.
     * 
     * @param latitude
     */
    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets the longitude value for this RoutePositionInfo.
     * 
     * @return longitude
     */
    public int getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude value for this RoutePositionInfo.
     * 
     * @param longitude
     */
    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }


    /**
     * Gets the positionTime value for this RoutePositionInfo.
     * 
     * @return positionTime
     */
    public java.util.Calendar getPositionTime() {
        return positionTime;
    }


    /**
     * Sets the positionTime value for this RoutePositionInfo.
     * 
     * @param positionTime
     */
    public void setPositionTime(java.util.Calendar positionTime) {
        this.positionTime = positionTime;
    }


    /**
     * Gets the timeZoneOptions value for this RoutePositionInfo.
     * 
     * @return timeZoneOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions getTimeZoneOptions() {
        return timeZoneOptions;
    }


    /**
     * Sets the timeZoneOptions value for this RoutePositionInfo.
     * 
     * @param timeZoneOptions
     */
    public void setTimeZoneOptions(com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
        this.timeZoneOptions = timeZoneOptions;
    }


    /**
     * Gets the unitOfSpeed value for this RoutePositionInfo.
     * 
     * @return unitOfSpeed
     */
    public com.freshdirect.routing.proxy.stub.transportation.UnitSpeed getUnitOfSpeed() {
        return unitOfSpeed;
    }


    /**
     * Sets the unitOfSpeed value for this RoutePositionInfo.
     * 
     * @param unitOfSpeed
     */
    public void setUnitOfSpeed(com.freshdirect.routing.proxy.stub.transportation.UnitSpeed unitOfSpeed) {
        this.unitOfSpeed = unitOfSpeed;
    }


    /**
     * Gets the speed value for this RoutePositionInfo.
     * 
     * @return speed
     */
    public double getSpeed() {
        return speed;
    }


    /**
     * Sets the speed value for this RoutePositionInfo.
     * 
     * @param speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutePositionInfo)) return false;
        RoutePositionInfo other = (RoutePositionInfo) obj;
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
            this.latitude == other.getLatitude() &&
            this.longitude == other.getLongitude() &&
            ((this.positionTime==null && other.getPositionTime()==null) || 
             (this.positionTime!=null &&
              this.positionTime.equals(other.getPositionTime()))) &&
            ((this.timeZoneOptions==null && other.getTimeZoneOptions()==null) || 
             (this.timeZoneOptions!=null &&
              this.timeZoneOptions.equals(other.getTimeZoneOptions()))) &&
            ((this.unitOfSpeed==null && other.getUnitOfSpeed()==null) || 
             (this.unitOfSpeed!=null &&
              this.unitOfSpeed.equals(other.getUnitOfSpeed()))) &&
            this.speed == other.getSpeed();
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
        _hashCode += getLatitude();
        _hashCode += getLongitude();
        if (getPositionTime() != null) {
            _hashCode += getPositionTime().hashCode();
        }
        if (getTimeZoneOptions() != null) {
            _hashCode += getTimeZoneOptions().hashCode();
        }
        if (getUnitOfSpeed() != null) {
            _hashCode += getUnitOfSpeed().hashCode();
        }
        _hashCode += new Double(getSpeed()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutePositionInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutePositionInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "latitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "longitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZoneOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeZoneOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneOptions"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitOfSpeed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "unitOfSpeed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UnitSpeed"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("speed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "speed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
