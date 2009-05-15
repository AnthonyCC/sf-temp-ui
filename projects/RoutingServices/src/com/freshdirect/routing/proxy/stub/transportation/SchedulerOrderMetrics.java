/**
 * SchedulerOrderMetrics.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerOrderMetrics  implements java.io.Serializable {
    private int items;

    private int deliveryQuantity;

    private int pickupQuantity;

    private int serviceTime;

    private int travelTime;

    public SchedulerOrderMetrics() {
    }

    public SchedulerOrderMetrics(
           int items,
           int deliveryQuantity,
           int pickupQuantity,
           int serviceTime,
           int travelTime) {
           this.items = items;
           this.deliveryQuantity = deliveryQuantity;
           this.pickupQuantity = pickupQuantity;
           this.serviceTime = serviceTime;
           this.travelTime = travelTime;
    }


    /**
     * Gets the items value for this SchedulerOrderMetrics.
     * 
     * @return items
     */
    public int getItems() {
        return items;
    }


    /**
     * Sets the items value for this SchedulerOrderMetrics.
     * 
     * @param items
     */
    public void setItems(int items) {
        this.items = items;
    }


    /**
     * Gets the deliveryQuantity value for this SchedulerOrderMetrics.
     * 
     * @return deliveryQuantity
     */
    public int getDeliveryQuantity() {
        return deliveryQuantity;
    }


    /**
     * Sets the deliveryQuantity value for this SchedulerOrderMetrics.
     * 
     * @param deliveryQuantity
     */
    public void setDeliveryQuantity(int deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }


    /**
     * Gets the pickupQuantity value for this SchedulerOrderMetrics.
     * 
     * @return pickupQuantity
     */
    public int getPickupQuantity() {
        return pickupQuantity;
    }


    /**
     * Sets the pickupQuantity value for this SchedulerOrderMetrics.
     * 
     * @param pickupQuantity
     */
    public void setPickupQuantity(int pickupQuantity) {
        this.pickupQuantity = pickupQuantity;
    }


    /**
     * Gets the serviceTime value for this SchedulerOrderMetrics.
     * 
     * @return serviceTime
     */
    public int getServiceTime() {
        return serviceTime;
    }


    /**
     * Sets the serviceTime value for this SchedulerOrderMetrics.
     * 
     * @param serviceTime
     */
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }


    /**
     * Gets the travelTime value for this SchedulerOrderMetrics.
     * 
     * @return travelTime
     */
    public int getTravelTime() {
        return travelTime;
    }


    /**
     * Sets the travelTime value for this SchedulerOrderMetrics.
     * 
     * @param travelTime
     */
    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerOrderMetrics)) return false;
        SchedulerOrderMetrics other = (SchedulerOrderMetrics) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.items == other.getItems() &&
            this.deliveryQuantity == other.getDeliveryQuantity() &&
            this.pickupQuantity == other.getPickupQuantity() &&
            this.serviceTime == other.getServiceTime() &&
            this.travelTime == other.getTravelTime();
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
        _hashCode += getItems();
        _hashCode += getDeliveryQuantity();
        _hashCode += getPickupQuantity();
        _hashCode += getServiceTime();
        _hashCode += getTravelTime();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerOrderMetrics.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerOrderMetrics"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("items");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "items"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pickupQuantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "pickupQuantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "serviceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("travelTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "travelTime"));
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
