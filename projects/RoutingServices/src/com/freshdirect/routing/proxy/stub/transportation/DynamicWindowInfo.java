/**
 * DynamicWindowInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DynamicWindowInfo  implements java.io.Serializable {
    private org.apache.axis.types.Time startTime;

    private org.apache.axis.types.Time stopTime;

    private int durationSeconds;

    private int intervalSeconds;

    private org.apache.axis.types.Time breakStartTime;

    private int breakDuration;

    public DynamicWindowInfo() {
    }

    public DynamicWindowInfo(
           org.apache.axis.types.Time startTime,
           org.apache.axis.types.Time stopTime,
           int durationSeconds,
           int intervalSeconds,
           org.apache.axis.types.Time breakStartTime,
           int breakDuration) {
           this.startTime = startTime;
           this.stopTime = stopTime;
           this.durationSeconds = durationSeconds;
           this.intervalSeconds = intervalSeconds;
           this.breakStartTime = breakStartTime;
           this.breakDuration = breakDuration;
    }


    /**
     * Gets the startTime value for this DynamicWindowInfo.
     * 
     * @return startTime
     */
    public org.apache.axis.types.Time getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this DynamicWindowInfo.
     * 
     * @param startTime
     */
    public void setStartTime(org.apache.axis.types.Time startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the stopTime value for this DynamicWindowInfo.
     * 
     * @return stopTime
     */
    public org.apache.axis.types.Time getStopTime() {
        return stopTime;
    }


    /**
     * Sets the stopTime value for this DynamicWindowInfo.
     * 
     * @param stopTime
     */
    public void setStopTime(org.apache.axis.types.Time stopTime) {
        this.stopTime = stopTime;
    }


    /**
     * Gets the durationSeconds value for this DynamicWindowInfo.
     * 
     * @return durationSeconds
     */
    public int getDurationSeconds() {
        return durationSeconds;
    }


    /**
     * Sets the durationSeconds value for this DynamicWindowInfo.
     * 
     * @param durationSeconds
     */
    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }


    /**
     * Gets the intervalSeconds value for this DynamicWindowInfo.
     * 
     * @return intervalSeconds
     */
    public int getIntervalSeconds() {
        return intervalSeconds;
    }


    /**
     * Sets the intervalSeconds value for this DynamicWindowInfo.
     * 
     * @param intervalSeconds
     */
    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }


    /**
     * Gets the breakStartTime value for this DynamicWindowInfo.
     * 
     * @return breakStartTime
     */
    public org.apache.axis.types.Time getBreakStartTime() {
        return breakStartTime;
    }


    /**
     * Sets the breakStartTime value for this DynamicWindowInfo.
     * 
     * @param breakStartTime
     */
    public void setBreakStartTime(org.apache.axis.types.Time breakStartTime) {
        this.breakStartTime = breakStartTime;
    }


    /**
     * Gets the breakDuration value for this DynamicWindowInfo.
     * 
     * @return breakDuration
     */
    public int getBreakDuration() {
        return breakDuration;
    }


    /**
     * Sets the breakDuration value for this DynamicWindowInfo.
     * 
     * @param breakDuration
     */
    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DynamicWindowInfo)) return false;
        DynamicWindowInfo other = (DynamicWindowInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
            ((this.stopTime==null && other.getStopTime()==null) || 
             (this.stopTime!=null &&
              this.stopTime.equals(other.getStopTime()))) &&
            this.durationSeconds == other.getDurationSeconds() &&
            this.intervalSeconds == other.getIntervalSeconds() &&
            ((this.breakStartTime==null && other.getBreakStartTime()==null) || 
             (this.breakStartTime!=null &&
              this.breakStartTime.equals(other.getBreakStartTime()))) &&
            this.breakDuration == other.getBreakDuration();
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
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        if (getStopTime() != null) {
            _hashCode += getStopTime().hashCode();
        }
        _hashCode += getDurationSeconds();
        _hashCode += getIntervalSeconds();
        if (getBreakStartTime() != null) {
            _hashCode += getBreakStartTime().hashCode();
        }
        _hashCode += getBreakDuration();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DynamicWindowInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DynamicWindowInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "startTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("durationSeconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "durationSeconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("intervalSeconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "intervalSeconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("breakStartTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "breakStartTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("breakDuration");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "breakDuration"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
