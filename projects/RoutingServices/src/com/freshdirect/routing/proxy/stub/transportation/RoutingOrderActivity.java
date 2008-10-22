/**
 * RoutingOrderActivity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingOrderActivity  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivityIdentity orderActivityIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity;

    private java.lang.String orderID;

    public RoutingOrderActivity() {
    }

    public RoutingOrderActivity(
           com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivityIdentity orderActivityIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity,
           java.lang.String orderID) {
           this.orderActivityIdentity = orderActivityIdentity;
           this.orderIdentity = orderIdentity;
           this.orderID = orderID;
    }


    /**
     * Gets the orderActivityIdentity value for this RoutingOrderActivity.
     * 
     * @return orderActivityIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivityIdentity getOrderActivityIdentity() {
        return orderActivityIdentity;
    }


    /**
     * Sets the orderActivityIdentity value for this RoutingOrderActivity.
     * 
     * @param orderActivityIdentity
     */
    public void setOrderActivityIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingOrderActivityIdentity orderActivityIdentity) {
        this.orderActivityIdentity = orderActivityIdentity;
    }


    /**
     * Gets the orderIdentity value for this RoutingOrderActivity.
     * 
     * @return orderIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity getOrderIdentity() {
        return orderIdentity;
    }


    /**
     * Sets the orderIdentity value for this RoutingOrderActivity.
     * 
     * @param orderIdentity
     */
    public void setOrderIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity) {
        this.orderIdentity = orderIdentity;
    }


    /**
     * Gets the orderID value for this RoutingOrderActivity.
     * 
     * @return orderID
     */
    public java.lang.String getOrderID() {
        return orderID;
    }


    /**
     * Sets the orderID value for this RoutingOrderActivity.
     * 
     * @param orderID
     */
    public void setOrderID(java.lang.String orderID) {
        this.orderID = orderID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingOrderActivity)) return false;
        RoutingOrderActivity other = (RoutingOrderActivity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.orderActivityIdentity==null && other.getOrderActivityIdentity()==null) || 
             (this.orderActivityIdentity!=null &&
              this.orderActivityIdentity.equals(other.getOrderActivityIdentity()))) &&
            ((this.orderIdentity==null && other.getOrderIdentity()==null) || 
             (this.orderIdentity!=null &&
              this.orderIdentity.equals(other.getOrderIdentity()))) &&
            ((this.orderID==null && other.getOrderID()==null) || 
             (this.orderID!=null &&
              this.orderID.equals(other.getOrderID())));
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
        if (getOrderActivityIdentity() != null) {
            _hashCode += getOrderActivityIdentity().hashCode();
        }
        if (getOrderIdentity() != null) {
            _hashCode += getOrderIdentity().hashCode();
        }
        if (getOrderID() != null) {
            _hashCode += getOrderID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingOrderActivity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrderActivity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderActivityIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderActivityIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrderActivityIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingOrderIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
