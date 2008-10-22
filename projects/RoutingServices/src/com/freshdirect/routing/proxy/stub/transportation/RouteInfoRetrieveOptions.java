/**
 * RouteInfoRetrieveOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RouteInfoRetrieveOptions  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.DetailLevel level;

    private boolean retrieveContainers;

    private boolean retrieveFinancialInformation;

    private boolean retrieveDriverAlerts;

    private boolean retrieveActivities;

    private boolean retrieveRolledUpQuantities;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions;

    public RouteInfoRetrieveOptions() {
    }

    public RouteInfoRetrieveOptions(
           com.freshdirect.routing.proxy.stub.transportation.DetailLevel level,
           boolean retrieveContainers,
           boolean retrieveFinancialInformation,
           boolean retrieveDriverAlerts,
           boolean retrieveActivities,
           boolean retrieveRolledUpQuantities,
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
           this.level = level;
           this.retrieveContainers = retrieveContainers;
           this.retrieveFinancialInformation = retrieveFinancialInformation;
           this.retrieveDriverAlerts = retrieveDriverAlerts;
           this.retrieveActivities = retrieveActivities;
           this.retrieveRolledUpQuantities = retrieveRolledUpQuantities;
           this.timeZoneOptions = timeZoneOptions;
    }


    /**
     * Gets the level value for this RouteInfoRetrieveOptions.
     * 
     * @return level
     */
    public com.freshdirect.routing.proxy.stub.transportation.DetailLevel getLevel() {
        return level;
    }


    /**
     * Sets the level value for this RouteInfoRetrieveOptions.
     * 
     * @param level
     */
    public void setLevel(com.freshdirect.routing.proxy.stub.transportation.DetailLevel level) {
        this.level = level;
    }


    /**
     * Gets the retrieveContainers value for this RouteInfoRetrieveOptions.
     * 
     * @return retrieveContainers
     */
    public boolean isRetrieveContainers() {
        return retrieveContainers;
    }


    /**
     * Sets the retrieveContainers value for this RouteInfoRetrieveOptions.
     * 
     * @param retrieveContainers
     */
    public void setRetrieveContainers(boolean retrieveContainers) {
        this.retrieveContainers = retrieveContainers;
    }


    /**
     * Gets the retrieveFinancialInformation value for this RouteInfoRetrieveOptions.
     * 
     * @return retrieveFinancialInformation
     */
    public boolean isRetrieveFinancialInformation() {
        return retrieveFinancialInformation;
    }


    /**
     * Sets the retrieveFinancialInformation value for this RouteInfoRetrieveOptions.
     * 
     * @param retrieveFinancialInformation
     */
    public void setRetrieveFinancialInformation(boolean retrieveFinancialInformation) {
        this.retrieveFinancialInformation = retrieveFinancialInformation;
    }


    /**
     * Gets the retrieveDriverAlerts value for this RouteInfoRetrieveOptions.
     * 
     * @return retrieveDriverAlerts
     */
    public boolean isRetrieveDriverAlerts() {
        return retrieveDriverAlerts;
    }


    /**
     * Sets the retrieveDriverAlerts value for this RouteInfoRetrieveOptions.
     * 
     * @param retrieveDriverAlerts
     */
    public void setRetrieveDriverAlerts(boolean retrieveDriverAlerts) {
        this.retrieveDriverAlerts = retrieveDriverAlerts;
    }


    /**
     * Gets the retrieveActivities value for this RouteInfoRetrieveOptions.
     * 
     * @return retrieveActivities
     */
    public boolean isRetrieveActivities() {
        return retrieveActivities;
    }


    /**
     * Sets the retrieveActivities value for this RouteInfoRetrieveOptions.
     * 
     * @param retrieveActivities
     */
    public void setRetrieveActivities(boolean retrieveActivities) {
        this.retrieveActivities = retrieveActivities;
    }


    /**
     * Gets the retrieveRolledUpQuantities value for this RouteInfoRetrieveOptions.
     * 
     * @return retrieveRolledUpQuantities
     */
    public boolean isRetrieveRolledUpQuantities() {
        return retrieveRolledUpQuantities;
    }


    /**
     * Sets the retrieveRolledUpQuantities value for this RouteInfoRetrieveOptions.
     * 
     * @param retrieveRolledUpQuantities
     */
    public void setRetrieveRolledUpQuantities(boolean retrieveRolledUpQuantities) {
        this.retrieveRolledUpQuantities = retrieveRolledUpQuantities;
    }


    /**
     * Gets the timeZoneOptions value for this RouteInfoRetrieveOptions.
     * 
     * @return timeZoneOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions getTimeZoneOptions() {
        return timeZoneOptions;
    }


    /**
     * Sets the timeZoneOptions value for this RouteInfoRetrieveOptions.
     * 
     * @param timeZoneOptions
     */
    public void setTimeZoneOptions(com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
        this.timeZoneOptions = timeZoneOptions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RouteInfoRetrieveOptions)) return false;
        RouteInfoRetrieveOptions other = (RouteInfoRetrieveOptions) obj;
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
            this.retrieveContainers == other.isRetrieveContainers() &&
            this.retrieveFinancialInformation == other.isRetrieveFinancialInformation() &&
            this.retrieveDriverAlerts == other.isRetrieveDriverAlerts() &&
            this.retrieveActivities == other.isRetrieveActivities() &&
            this.retrieveRolledUpQuantities == other.isRetrieveRolledUpQuantities() &&
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
        _hashCode += (isRetrieveContainers() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrieveFinancialInformation() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrieveDriverAlerts() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrieveActivities() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetrieveRolledUpQuantities() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getTimeZoneOptions() != null) {
            _hashCode += getTimeZoneOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RouteInfoRetrieveOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteInfoRetrieveOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("level");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "level"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DetailLevel"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveContainers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveContainers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveFinancialInformation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveFinancialInformation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveDriverAlerts");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveDriverAlerts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveActivities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveActivities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveRolledUpQuantities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "retrieveRolledUpQuantities"));
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
