/**
 * SchedulerReserveOrderOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerReserveOrderOptions  implements java.io.Serializable {
    private int excludeCutoffRoutes;

    private boolean movable;

    private boolean confirm;

    public SchedulerReserveOrderOptions() {
    }

    public SchedulerReserveOrderOptions(
           int excludeCutoffRoutes,
           boolean movable,
           boolean confirm) {
           this.excludeCutoffRoutes = excludeCutoffRoutes;
           this.movable = movable;
           this.confirm = confirm;
    }


    /**
     * Gets the excludeCutoffRoutes value for this SchedulerReserveOrderOptions.
     * 
     * @return excludeCutoffRoutes
     */
    public int getExcludeCutoffRoutes() {
        return excludeCutoffRoutes;
    }


    /**
     * Sets the excludeCutoffRoutes value for this SchedulerReserveOrderOptions.
     * 
     * @param excludeCutoffRoutes
     */
    public void setExcludeCutoffRoutes(int excludeCutoffRoutes) {
        this.excludeCutoffRoutes = excludeCutoffRoutes;
    }


    /**
     * Gets the movable value for this SchedulerReserveOrderOptions.
     * 
     * @return movable
     */
    public boolean isMovable() {
        return movable;
    }


    /**
     * Sets the movable value for this SchedulerReserveOrderOptions.
     * 
     * @param movable
     */
    public void setMovable(boolean movable) {
        this.movable = movable;
    }


    /**
     * Gets the confirm value for this SchedulerReserveOrderOptions.
     * 
     * @return confirm
     */
    public boolean isConfirm() {
        return confirm;
    }


    /**
     * Sets the confirm value for this SchedulerReserveOrderOptions.
     * 
     * @param confirm
     */
    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerReserveOrderOptions)) return false;
        SchedulerReserveOrderOptions other = (SchedulerReserveOrderOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.excludeCutoffRoutes == other.getExcludeCutoffRoutes() &&
            this.movable == other.isMovable() &&
            this.confirm == other.isConfirm();
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
        _hashCode += getExcludeCutoffRoutes();
        _hashCode += (isMovable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isConfirm() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerReserveOrderOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerReserveOrderOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("excludeCutoffRoutes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "excludeCutoffRoutes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("movable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "movable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("confirm");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "confirm"));
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
