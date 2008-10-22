/**
 * SchedulerBulkReserveOrdersOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerBulkReserveOrdersOptions  implements java.io.Serializable {
    private boolean confirm;

    private boolean sequenced;

    private boolean singleRoute;

    private boolean movable;

    public SchedulerBulkReserveOrdersOptions() {
    }

    public SchedulerBulkReserveOrdersOptions(
           boolean confirm,
           boolean sequenced,
           boolean singleRoute,
           boolean movable) {
           this.confirm = confirm;
           this.sequenced = sequenced;
           this.singleRoute = singleRoute;
           this.movable = movable;
    }


    /**
     * Gets the confirm value for this SchedulerBulkReserveOrdersOptions.
     * 
     * @return confirm
     */
    public boolean isConfirm() {
        return confirm;
    }


    /**
     * Sets the confirm value for this SchedulerBulkReserveOrdersOptions.
     * 
     * @param confirm
     */
    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }


    /**
     * Gets the sequenced value for this SchedulerBulkReserveOrdersOptions.
     * 
     * @return sequenced
     */
    public boolean isSequenced() {
        return sequenced;
    }


    /**
     * Sets the sequenced value for this SchedulerBulkReserveOrdersOptions.
     * 
     * @param sequenced
     */
    public void setSequenced(boolean sequenced) {
        this.sequenced = sequenced;
    }


    /**
     * Gets the singleRoute value for this SchedulerBulkReserveOrdersOptions.
     * 
     * @return singleRoute
     */
    public boolean isSingleRoute() {
        return singleRoute;
    }


    /**
     * Sets the singleRoute value for this SchedulerBulkReserveOrdersOptions.
     * 
     * @param singleRoute
     */
    public void setSingleRoute(boolean singleRoute) {
        this.singleRoute = singleRoute;
    }


    /**
     * Gets the movable value for this SchedulerBulkReserveOrdersOptions.
     * 
     * @return movable
     */
    public boolean isMovable() {
        return movable;
    }


    /**
     * Sets the movable value for this SchedulerBulkReserveOrdersOptions.
     * 
     * @param movable
     */
    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerBulkReserveOrdersOptions)) return false;
        SchedulerBulkReserveOrdersOptions other = (SchedulerBulkReserveOrdersOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.confirm == other.isConfirm() &&
            this.sequenced == other.isSequenced() &&
            this.singleRoute == other.isSingleRoute() &&
            this.movable == other.isMovable();
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
        _hashCode += (isConfirm() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isSequenced() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isSingleRoute() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMovable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerBulkReserveOrdersOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerBulkReserveOrdersOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("confirm");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "confirm"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequenced");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sequenced"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("singleRoute");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "singleRoute"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("movable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "movable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
