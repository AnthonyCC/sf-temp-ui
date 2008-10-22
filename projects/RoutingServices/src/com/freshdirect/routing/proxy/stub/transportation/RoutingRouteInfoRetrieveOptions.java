/**
 * RoutingRouteInfoRetrieveOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingRouteInfoRetrieveOptions  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RoutingDetailLevel level;

    private boolean retrieveActivities;

    private boolean retrieveEquipment;

    private boolean retrieveActive;

    private boolean retrieveBuilt;

    private boolean retrievePublished;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions;

    public RoutingRouteInfoRetrieveOptions() {
    }

    public RoutingRouteInfoRetrieveOptions(
           com.freshdirect.routing.proxy.stub.transportation.RoutingDetailLevel level,
           boolean retrieveActivities,
           boolean retrieveEquipment,
           boolean retrieveActive,
           boolean retrieveBuilt,
           boolean retrievePublished,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
           this.level = level;
           this.retrieveActivities = retrieveActivities;
           this.retrieveEquipment = retrieveEquipment;
           this.retrieveActive = retrieveActive;
           this.retrieveBuilt = retrieveBuilt;
           this.retrievePublished = retrievePublished;
           this.timeZoneOptions = timeZoneOptions;
    }


    /**
     * Gets the level value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @return level
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingDetailLevel getLevel() {
        return level;
    }


    /**
     * Sets the level value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @param level
     */
    public void setLevel(com.freshdirect.routing.proxy.stub.transportation.RoutingDetailLevel level) {
        this.level = level;
    }


    /**
     * Gets the retrieveActivities value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @return retrieveActivities
     */
    public boolean isRetrieveActivities() {
        return retrieveActivities;
    }


    /**
     * Sets the retrieveActivities value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @param retrieveActivities
     */
    public void setRetrieveActivities(boolean retrieveActivities) {
        this.retrieveActivities = retrieveActivities;
    }


    /**
     * Gets the retrieveEquipment value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @return retrieveEquipment
     */
    public boolean isRetrieveEquipment() {
        return retrieveEquipment;
    }


    /**
     * Sets the retrieveEquipment value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @param retrieveEquipment
     */
    public void setRetrieveEquipment(boolean retrieveEquipment) {
        this.retrieveEquipment = retrieveEquipment;
    }


    /**
     * Gets the retrieveActive value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @return retrieveActive
     */
    public boolean isRetrieveActive() {
        return retrieveActive;
    }


    /**
     * Sets the retrieveActive value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @param retrieveActive
     */
    public void setRetrieveActive(boolean retrieveActive) {
        this.retrieveActive = retrieveActive;
    }


    /**
     * Gets the retrieveBuilt value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @return retrieveBuilt
     */
    public boolean isRetrieveBuilt() {
        return retrieveBuilt;
    }


    /**
     * Sets the retrieveBuilt value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @param retrieveBuilt
     */
    public void setRetrieveBuilt(boolean retrieveBuilt) {
        this.retrieveBuilt = retrieveBuilt;
    }


    /**
     * Gets the retrievePublished value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @return retrievePublished
     */
    public boolean isRetrievePublished() {
        return retrievePublished;
    }


    /**
     * Sets the retrievePublished value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @param retrievePublished
     */
    public void setRetrievePublished(boolean retrievePublished) {
        this.retrievePublished = retrievePublished;
    }


    /**
     * Gets the timeZoneOptions value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @return timeZoneOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions getTimeZoneOptions() {
        return timeZoneOptions;
    }


    /**
     * Sets the timeZoneOptions value for this RoutingRouteInfoRetrieveOptions.
     * 
     * @param timeZoneOptions
     */
    public void setTimeZoneOptions(com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
        this.timeZoneOptions = timeZoneOptions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingRouteInfoRetrieveOptions)) return false;
        RoutingRouteInfoRetrieveOptions other = (RoutingRouteInfoRetrieveOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.level==null && other.getLevel()==null) || 
             (this.level!=null &&
              this.level.equals(other.getLevel()))) &&
            this.retrieveActivities == other.isRetrieveActivities() &&
            this.retrieveEquipment == other.isRetrieveEquipment() &&
            this.retrieveActive == other.isRetrieveActive() &&
            this.retrieveBuilt == other.isRetrieveBuilt() &&
            this.retrievePublished == other.isRetrievePublished() &&
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
        int _hashCode = 1;
        if (getLevel() != null) {
            _hashCode += getLevel().hashCode();
        }
        _hashCode += (isRetrieveActivities() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrieveEquipment() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrieveActive() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrieveBuilt() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrievePublished() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getTimeZoneOptions() != null) {
            _hashCode += getTimeZoneOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingRouteInfoRetrieveOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteInfoRetrieveOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("level");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "level"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingDetailLevel"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveActivities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveActivities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveEquipment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveEquipment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveActive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveActive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveBuilt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveBuilt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrievePublished");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrievePublished"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeZoneOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeZoneOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeZoneOptions"));
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
