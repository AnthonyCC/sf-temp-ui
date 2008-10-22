/**
 * DeliveryWindow.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DeliveryWindow  implements java.io.Serializable {
    private org.apache.axis.types.Time startTime;

    private org.apache.axis.types.Time stopTime;

    private com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity;

    private boolean manuallyClosed;

    private com.freshdirect.routing.proxy.stub.transportation.DeliveryCost deliveryCost;

    public DeliveryWindow() {
    }

    public DeliveryWindow(
           org.apache.axis.types.Time startTime,
           org.apache.axis.types.Time stopTime,
           com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity,
           boolean manuallyClosed,
           com.freshdirect.routing.proxy.stub.transportation.DeliveryCost deliveryCost) {
           this.startTime = startTime;
           this.stopTime = stopTime;
           this.schedulerIdentity = schedulerIdentity;
           this.manuallyClosed = manuallyClosed;
           this.deliveryCost = deliveryCost;
    }


    /**
     * Gets the startTime value for this DeliveryWindow.
     * 
     * @return startTime
     */
    public org.apache.axis.types.Time getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this DeliveryWindow.
     * 
     * @param startTime
     */
    public void setStartTime(org.apache.axis.types.Time startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the stopTime value for this DeliveryWindow.
     * 
     * @return stopTime
     */
    public org.apache.axis.types.Time getStopTime() {
        return stopTime;
    }


    /**
     * Sets the stopTime value for this DeliveryWindow.
     * 
     * @param stopTime
     */
    public void setStopTime(org.apache.axis.types.Time stopTime) {
        this.stopTime = stopTime;
    }


    /**
     * Gets the schedulerIdentity value for this DeliveryWindow.
     * 
     * @return schedulerIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity getSchedulerIdentity() {
        return schedulerIdentity;
    }


    /**
     * Sets the schedulerIdentity value for this DeliveryWindow.
     * 
     * @param schedulerIdentity
     */
    public void setSchedulerIdentity(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity) {
        this.schedulerIdentity = schedulerIdentity;
    }


    /**
     * Gets the manuallyClosed value for this DeliveryWindow.
     * 
     * @return manuallyClosed
     */
    public boolean isManuallyClosed() {
        return manuallyClosed;
    }


    /**
     * Sets the manuallyClosed value for this DeliveryWindow.
     * 
     * @param manuallyClosed
     */
    public void setManuallyClosed(boolean manuallyClosed) {
        this.manuallyClosed = manuallyClosed;
    }


    /**
     * Gets the deliveryCost value for this DeliveryWindow.
     * 
     * @return deliveryCost
     */
    public com.freshdirect.routing.proxy.stub.transportation.DeliveryCost getDeliveryCost() {
        return deliveryCost;
    }


    /**
     * Sets the deliveryCost value for this DeliveryWindow.
     * 
     * @param deliveryCost
     */
    public void setDeliveryCost(com.freshdirect.routing.proxy.stub.transportation.DeliveryCost deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeliveryWindow)) return false;
        DeliveryWindow other = (DeliveryWindow) obj;
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
            ((this.schedulerIdentity==null && other.getSchedulerIdentity()==null) || 
             (this.schedulerIdentity!=null &&
              this.schedulerIdentity.equals(other.getSchedulerIdentity()))) &&
            this.manuallyClosed == other.isManuallyClosed() &&
            ((this.deliveryCost==null && other.getDeliveryCost()==null) || 
             (this.deliveryCost!=null &&
              this.deliveryCost.equals(other.getDeliveryCost())));
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
        if (getSchedulerIdentity() != null) {
            _hashCode += getSchedulerIdentity().hashCode();
        }
        _hashCode += (isManuallyClosed() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDeliveryCost() != null) {
            _hashCode += getDeliveryCost().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DeliveryWindow.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryWindow"));
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
        elemField.setFieldName("schedulerIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "schedulerIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("manuallyClosed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "manuallyClosed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryCost"));
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
