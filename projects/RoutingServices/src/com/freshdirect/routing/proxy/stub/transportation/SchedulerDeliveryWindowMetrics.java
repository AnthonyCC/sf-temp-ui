/**
 * SchedulerDeliveryWindowMetrics.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerDeliveryWindowMetrics  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowBase window;

    private int vehiclesInUse;

    private int allocatedVehicles;

    private com.freshdirect.routing.proxy.stub.transportation.SchedulerOrderMetrics confirmed;

    private com.freshdirect.routing.proxy.stub.transportation.SchedulerOrderMetrics reserved;

    public SchedulerDeliveryWindowMetrics() {
    }

    public SchedulerDeliveryWindowMetrics(
           com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowBase window,
           int vehiclesInUse,
           int allocatedVehicles,
           com.freshdirect.routing.proxy.stub.transportation.SchedulerOrderMetrics confirmed,
           com.freshdirect.routing.proxy.stub.transportation.SchedulerOrderMetrics reserved) {
           this.window = window;
           this.vehiclesInUse = vehiclesInUse;
           this.allocatedVehicles = allocatedVehicles;
           this.confirmed = confirmed;
           this.reserved = reserved;
    }


    /**
     * Gets the window value for this SchedulerDeliveryWindowMetrics.
     * 
     * @return window
     */
    public com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowBase getWindow() {
        return window;
    }


    /**
     * Sets the window value for this SchedulerDeliveryWindowMetrics.
     * 
     * @param window
     */
    public void setWindow(com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowBase window) {
        this.window = window;
    }


    /**
     * Gets the vehiclesInUse value for this SchedulerDeliveryWindowMetrics.
     * 
     * @return vehiclesInUse
     */
    public int getVehiclesInUse() {
        return vehiclesInUse;
    }


    /**
     * Sets the vehiclesInUse value for this SchedulerDeliveryWindowMetrics.
     * 
     * @param vehiclesInUse
     */
    public void setVehiclesInUse(int vehiclesInUse) {
        this.vehiclesInUse = vehiclesInUse;
    }


    /**
     * Gets the allocatedVehicles value for this SchedulerDeliveryWindowMetrics.
     * 
     * @return allocatedVehicles
     */
    public int getAllocatedVehicles() {
        return allocatedVehicles;
    }


    /**
     * Sets the allocatedVehicles value for this SchedulerDeliveryWindowMetrics.
     * 
     * @param allocatedVehicles
     */
    public void setAllocatedVehicles(int allocatedVehicles) {
        this.allocatedVehicles = allocatedVehicles;
    }


    /**
     * Gets the confirmed value for this SchedulerDeliveryWindowMetrics.
     * 
     * @return confirmed
     */
    public com.freshdirect.routing.proxy.stub.transportation.SchedulerOrderMetrics getConfirmed() {
        return confirmed;
    }


    /**
     * Sets the confirmed value for this SchedulerDeliveryWindowMetrics.
     * 
     * @param confirmed
     */
    public void setConfirmed(com.freshdirect.routing.proxy.stub.transportation.SchedulerOrderMetrics confirmed) {
        this.confirmed = confirmed;
    }


    /**
     * Gets the reserved value for this SchedulerDeliveryWindowMetrics.
     * 
     * @return reserved
     */
    public com.freshdirect.routing.proxy.stub.transportation.SchedulerOrderMetrics getReserved() {
        return reserved;
    }


    /**
     * Sets the reserved value for this SchedulerDeliveryWindowMetrics.
     * 
     * @param reserved
     */
    public void setReserved(com.freshdirect.routing.proxy.stub.transportation.SchedulerOrderMetrics reserved) {
        this.reserved = reserved;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerDeliveryWindowMetrics)) return false;
        SchedulerDeliveryWindowMetrics other = (SchedulerDeliveryWindowMetrics) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.window==null && other.getWindow()==null) || 
             (this.window!=null &&
              this.window.equals(other.getWindow()))) &&
            this.vehiclesInUse == other.getVehiclesInUse() &&
            this.allocatedVehicles == other.getAllocatedVehicles() &&
            ((this.confirmed==null && other.getConfirmed()==null) || 
             (this.confirmed!=null &&
              this.confirmed.equals(other.getConfirmed()))) &&
            ((this.reserved==null && other.getReserved()==null) || 
             (this.reserved!=null &&
              this.reserved.equals(other.getReserved())));
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
        if (getWindow() != null) {
            _hashCode += getWindow().hashCode();
        }
        _hashCode += getVehiclesInUse();
        _hashCode += getAllocatedVehicles();
        if (getConfirmed() != null) {
            _hashCode += getConfirmed().hashCode();
        }
        if (getReserved() != null) {
            _hashCode += getReserved().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerDeliveryWindowMetrics.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerDeliveryWindowMetrics"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("window");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "window"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerDeliveryWindowBase"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vehiclesInUse");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "vehiclesInUse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allocatedVehicles");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "allocatedVehicles"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("confirmed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "confirmed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerOrderMetrics"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reserved");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "reserved"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerOrderMetrics"));
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
