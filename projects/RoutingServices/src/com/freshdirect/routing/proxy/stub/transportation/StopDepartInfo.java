/**
 * StopDepartInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class StopDepartInfo  extends com.freshdirect.routing.proxy.stub.transportation.RouteEventInfo  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity;

    private java.util.Calendar actualDeparture;

    private com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality;

    private double distanceToStop;

    private com.freshdirect.routing.proxy.stub.transportation.DataQualityType distanceDataQuality;

    private boolean undeliverable;

    private java.lang.String undeliverableCode;

    private com.freshdirect.routing.proxy.stub.transportation.StopIdentity redeliveryAfterStopIdentity;

    private int delayMinutes;

    private java.lang.String delayType;

    private java.lang.String consignee;

    private com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions;

    public StopDepartInfo() {
    }

    public StopDepartInfo(
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
           com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
        super(
            source);
        this.stopIdentity = stopIdentity;
        this.actualDeparture = actualDeparture;
        this.timeDataQuality = timeDataQuality;
        this.distanceToStop = distanceToStop;
        this.distanceDataQuality = distanceDataQuality;
        this.undeliverable = undeliverable;
        this.undeliverableCode = undeliverableCode;
        this.redeliveryAfterStopIdentity = redeliveryAfterStopIdentity;
        this.delayMinutes = delayMinutes;
        this.delayType = delayType;
        this.consignee = consignee;
        this.timeZoneOptions = timeZoneOptions;
    }


    /**
     * Gets the stopIdentity value for this StopDepartInfo.
     * 
     * @return stopIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopIdentity getStopIdentity() {
        return stopIdentity;
    }


    /**
     * Sets the stopIdentity value for this StopDepartInfo.
     * 
     * @param stopIdentity
     */
    public void setStopIdentity(com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity) {
        this.stopIdentity = stopIdentity;
    }


    /**
     * Gets the actualDeparture value for this StopDepartInfo.
     * 
     * @return actualDeparture
     */
    public java.util.Calendar getActualDeparture() {
        return actualDeparture;
    }


    /**
     * Sets the actualDeparture value for this StopDepartInfo.
     * 
     * @param actualDeparture
     */
    public void setActualDeparture(java.util.Calendar actualDeparture) {
        this.actualDeparture = actualDeparture;
    }


    /**
     * Gets the timeDataQuality value for this StopDepartInfo.
     * 
     * @return timeDataQuality
     */
    public com.freshdirect.routing.proxy.stub.transportation.DataQualityType getTimeDataQuality() {
        return timeDataQuality;
    }


    /**
     * Sets the timeDataQuality value for this StopDepartInfo.
     * 
     * @param timeDataQuality
     */
    public void setTimeDataQuality(com.freshdirect.routing.proxy.stub.transportation.DataQualityType timeDataQuality) {
        this.timeDataQuality = timeDataQuality;
    }


    /**
     * Gets the distanceToStop value for this StopDepartInfo.
     * 
     * @return distanceToStop
     */
    public double getDistanceToStop() {
        return distanceToStop;
    }


    /**
     * Sets the distanceToStop value for this StopDepartInfo.
     * 
     * @param distanceToStop
     */
    public void setDistanceToStop(double distanceToStop) {
        this.distanceToStop = distanceToStop;
    }


    /**
     * Gets the distanceDataQuality value for this StopDepartInfo.
     * 
     * @return distanceDataQuality
     */
    public com.freshdirect.routing.proxy.stub.transportation.DataQualityType getDistanceDataQuality() {
        return distanceDataQuality;
    }


    /**
     * Sets the distanceDataQuality value for this StopDepartInfo.
     * 
     * @param distanceDataQuality
     */
    public void setDistanceDataQuality(com.freshdirect.routing.proxy.stub.transportation.DataQualityType distanceDataQuality) {
        this.distanceDataQuality = distanceDataQuality;
    }


    /**
     * Gets the undeliverable value for this StopDepartInfo.
     * 
     * @return undeliverable
     */
    public boolean isUndeliverable() {
        return undeliverable;
    }


    /**
     * Sets the undeliverable value for this StopDepartInfo.
     * 
     * @param undeliverable
     */
    public void setUndeliverable(boolean undeliverable) {
        this.undeliverable = undeliverable;
    }


    /**
     * Gets the undeliverableCode value for this StopDepartInfo.
     * 
     * @return undeliverableCode
     */
    public java.lang.String getUndeliverableCode() {
        return undeliverableCode;
    }


    /**
     * Sets the undeliverableCode value for this StopDepartInfo.
     * 
     * @param undeliverableCode
     */
    public void setUndeliverableCode(java.lang.String undeliverableCode) {
        this.undeliverableCode = undeliverableCode;
    }


    /**
     * Gets the redeliveryAfterStopIdentity value for this StopDepartInfo.
     * 
     * @return redeliveryAfterStopIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopIdentity getRedeliveryAfterStopIdentity() {
        return redeliveryAfterStopIdentity;
    }


    /**
     * Sets the redeliveryAfterStopIdentity value for this StopDepartInfo.
     * 
     * @param redeliveryAfterStopIdentity
     */
    public void setRedeliveryAfterStopIdentity(com.freshdirect.routing.proxy.stub.transportation.StopIdentity redeliveryAfterStopIdentity) {
        this.redeliveryAfterStopIdentity = redeliveryAfterStopIdentity;
    }


    /**
     * Gets the delayMinutes value for this StopDepartInfo.
     * 
     * @return delayMinutes
     */
    public int getDelayMinutes() {
        return delayMinutes;
    }


    /**
     * Sets the delayMinutes value for this StopDepartInfo.
     * 
     * @param delayMinutes
     */
    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }


    /**
     * Gets the delayType value for this StopDepartInfo.
     * 
     * @return delayType
     */
    public java.lang.String getDelayType() {
        return delayType;
    }


    /**
     * Sets the delayType value for this StopDepartInfo.
     * 
     * @param delayType
     */
    public void setDelayType(java.lang.String delayType) {
        this.delayType = delayType;
    }


    /**
     * Gets the consignee value for this StopDepartInfo.
     * 
     * @return consignee
     */
    public java.lang.String getConsignee() {
        return consignee;
    }


    /**
     * Sets the consignee value for this StopDepartInfo.
     * 
     * @param consignee
     */
    public void setConsignee(java.lang.String consignee) {
        this.consignee = consignee;
    }


    /**
     * Gets the timeZoneOptions value for this StopDepartInfo.
     * 
     * @return timeZoneOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions getTimeZoneOptions() {
        return timeZoneOptions;
    }


    /**
     * Sets the timeZoneOptions value for this StopDepartInfo.
     * 
     * @param timeZoneOptions
     */
    public void setTimeZoneOptions(com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) {
        this.timeZoneOptions = timeZoneOptions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StopDepartInfo)) return false;
        StopDepartInfo other = (StopDepartInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.stopIdentity==null && other.getStopIdentity()==null) || 
             (this.stopIdentity!=null &&
              this.stopIdentity.equals(other.getStopIdentity()))) &&
            ((this.actualDeparture==null && other.getActualDeparture()==null) || 
             (this.actualDeparture!=null &&
              this.actualDeparture.equals(other.getActualDeparture()))) &&
            ((this.timeDataQuality==null && other.getTimeDataQuality()==null) || 
             (this.timeDataQuality!=null &&
              this.timeDataQuality.equals(other.getTimeDataQuality()))) &&
            this.distanceToStop == other.getDistanceToStop() &&
            ((this.distanceDataQuality==null && other.getDistanceDataQuality()==null) || 
             (this.distanceDataQuality!=null &&
              this.distanceDataQuality.equals(other.getDistanceDataQuality()))) &&
            this.undeliverable == other.isUndeliverable() &&
            ((this.undeliverableCode==null && other.getUndeliverableCode()==null) || 
             (this.undeliverableCode!=null &&
              this.undeliverableCode.equals(other.getUndeliverableCode()))) &&
            ((this.redeliveryAfterStopIdentity==null && other.getRedeliveryAfterStopIdentity()==null) || 
             (this.redeliveryAfterStopIdentity!=null &&
              this.redeliveryAfterStopIdentity.equals(other.getRedeliveryAfterStopIdentity()))) &&
            this.delayMinutes == other.getDelayMinutes() &&
            ((this.delayType==null && other.getDelayType()==null) || 
             (this.delayType!=null &&
              this.delayType.equals(other.getDelayType()))) &&
            ((this.consignee==null && other.getConsignee()==null) || 
             (this.consignee!=null &&
              this.consignee.equals(other.getConsignee()))) &&
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
        if (getStopIdentity() != null) {
            _hashCode += getStopIdentity().hashCode();
        }
        if (getActualDeparture() != null) {
            _hashCode += getActualDeparture().hashCode();
        }
        if (getTimeDataQuality() != null) {
            _hashCode += getTimeDataQuality().hashCode();
        }
        _hashCode += new Double(getDistanceToStop()).hashCode();
        if (getDistanceDataQuality() != null) {
            _hashCode += getDistanceDataQuality().hashCode();
        }
        _hashCode += (isUndeliverable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUndeliverableCode() != null) {
            _hashCode += getUndeliverableCode().hashCode();
        }
        if (getRedeliveryAfterStopIdentity() != null) {
            _hashCode += getRedeliveryAfterStopIdentity().hashCode();
        }
        _hashCode += getDelayMinutes();
        if (getDelayType() != null) {
            _hashCode += getDelayType().hashCode();
        }
        if (getConsignee() != null) {
            _hashCode += getConsignee().hashCode();
        }
        if (getTimeZoneOptions() != null) {
            _hashCode += getTimeZoneOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StopDepartInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopDepartInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopIdentity"));
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
        elemField.setFieldName("distanceToStop");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "distanceToStop"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distanceDataQuality");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "distanceDataQuality"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DataQualityType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("undeliverable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "undeliverable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("undeliverableCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "undeliverableCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("redeliveryAfterStopIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "redeliveryAfterStopIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("delayMinutes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "delayMinutes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("delayType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "delayType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("consignee");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "consignee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
