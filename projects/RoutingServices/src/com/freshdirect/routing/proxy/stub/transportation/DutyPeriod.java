/**
 * DutyPeriod.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DutyPeriod  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity dutyPeriodIdentity;

    private java.util.Calendar startTime;

    private java.util.Calendar endTime;

    private int travelTime;

    private com.freshdirect.routing.proxy.stub.transportation.DutyPeriodEntryCode entryCode;

    public DutyPeriod() {
    }

    public DutyPeriod(
           com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity dutyPeriodIdentity,
           java.util.Calendar startTime,
           java.util.Calendar endTime,
           int travelTime,
           com.freshdirect.routing.proxy.stub.transportation.DutyPeriodEntryCode entryCode) {
           this.dutyPeriodIdentity = dutyPeriodIdentity;
           this.startTime = startTime;
           this.endTime = endTime;
           this.travelTime = travelTime;
           this.entryCode = entryCode;
    }


    /**
     * Gets the dutyPeriodIdentity value for this DutyPeriod.
     * 
     * @return dutyPeriodIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity getDutyPeriodIdentity() {
        return dutyPeriodIdentity;
    }


    /**
     * Sets the dutyPeriodIdentity value for this DutyPeriod.
     * 
     * @param dutyPeriodIdentity
     */
    public void setDutyPeriodIdentity(com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity dutyPeriodIdentity) {
        this.dutyPeriodIdentity = dutyPeriodIdentity;
    }


    /**
     * Gets the startTime value for this DutyPeriod.
     * 
     * @return startTime
     */
    public java.util.Calendar getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this DutyPeriod.
     * 
     * @param startTime
     */
    public void setStartTime(java.util.Calendar startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the endTime value for this DutyPeriod.
     * 
     * @return endTime
     */
    public java.util.Calendar getEndTime() {
        return endTime;
    }


    /**
     * Sets the endTime value for this DutyPeriod.
     * 
     * @param endTime
     */
    public void setEndTime(java.util.Calendar endTime) {
        this.endTime = endTime;
    }


    /**
     * Gets the travelTime value for this DutyPeriod.
     * 
     * @return travelTime
     */
    public int getTravelTime() {
        return travelTime;
    }


    /**
     * Sets the travelTime value for this DutyPeriod.
     * 
     * @param travelTime
     */
    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }


    /**
     * Gets the entryCode value for this DutyPeriod.
     * 
     * @return entryCode
     */
    public com.freshdirect.routing.proxy.stub.transportation.DutyPeriodEntryCode getEntryCode() {
        return entryCode;
    }


    /**
     * Sets the entryCode value for this DutyPeriod.
     * 
     * @param entryCode
     */
    public void setEntryCode(com.freshdirect.routing.proxy.stub.transportation.DutyPeriodEntryCode entryCode) {
        this.entryCode = entryCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DutyPeriod)) return false;
        DutyPeriod other = (DutyPeriod) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dutyPeriodIdentity==null && other.getDutyPeriodIdentity()==null) || 
             (this.dutyPeriodIdentity!=null &&
              this.dutyPeriodIdentity.equals(other.getDutyPeriodIdentity()))) &&
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
            ((this.endTime==null && other.getEndTime()==null) || 
             (this.endTime!=null &&
              this.endTime.equals(other.getEndTime()))) &&
            this.travelTime == other.getTravelTime() &&
            ((this.entryCode==null && other.getEntryCode()==null) || 
             (this.entryCode!=null &&
              this.entryCode.equals(other.getEntryCode())));
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
        if (getDutyPeriodIdentity() != null) {
            _hashCode += getDutyPeriodIdentity().hashCode();
        }
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        if (getEndTime() != null) {
            _hashCode += getEndTime().hashCode();
        }
        _hashCode += getTravelTime();
        if (getEntryCode() != null) {
            _hashCode += getEntryCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DutyPeriod.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DutyPeriod"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dutyPeriodIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dutyPeriodIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DutyPeriodIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "startTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "endTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("travelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "travelTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entryCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "entryCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DutyPeriodEntryCode"));
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
