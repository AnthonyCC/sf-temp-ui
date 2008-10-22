/**
 * OrderDeliveryDetailItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class OrderDeliveryDetailItem  extends com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailItem  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.OrderIdentity orderIdentity;

    private java.lang.String orderStatusCode;

    private java.lang.String referenceNumber;

    public OrderDeliveryDetailItem() {
    }

    public OrderDeliveryDetailItem(
           com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities,
           com.freshdirect.routing.proxy.stub.transportation.ReasonCodes reasonCodes,
           java.lang.String userDefinedField1,
           java.lang.String userDefinedField2,
           java.lang.String userDefinedField3,
           com.freshdirect.routing.proxy.stub.transportation.OrderIdentity orderIdentity,
           java.lang.String orderStatusCode,
           java.lang.String referenceNumber) {
        super(
            quantities,
            reasonCodes,
            userDefinedField1,
            userDefinedField2,
            userDefinedField3);
        this.orderIdentity = orderIdentity;
        this.orderStatusCode = orderStatusCode;
        this.referenceNumber = referenceNumber;
    }


    /**
     * Gets the orderIdentity value for this OrderDeliveryDetailItem.
     * 
     * @return orderIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.OrderIdentity getOrderIdentity() {
        return orderIdentity;
    }


    /**
     * Sets the orderIdentity value for this OrderDeliveryDetailItem.
     * 
     * @param orderIdentity
     */
    public void setOrderIdentity(com.freshdirect.routing.proxy.stub.transportation.OrderIdentity orderIdentity) {
        this.orderIdentity = orderIdentity;
    }


    /**
     * Gets the orderStatusCode value for this OrderDeliveryDetailItem.
     * 
     * @return orderStatusCode
     */
    public java.lang.String getOrderStatusCode() {
        return orderStatusCode;
    }


    /**
     * Sets the orderStatusCode value for this OrderDeliveryDetailItem.
     * 
     * @param orderStatusCode
     */
    public void setOrderStatusCode(java.lang.String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }


    /**
     * Gets the referenceNumber value for this OrderDeliveryDetailItem.
     * 
     * @return referenceNumber
     */
    public java.lang.String getReferenceNumber() {
        return referenceNumber;
    }


    /**
     * Sets the referenceNumber value for this OrderDeliveryDetailItem.
     * 
     * @param referenceNumber
     */
    public void setReferenceNumber(java.lang.String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OrderDeliveryDetailItem)) return false;
        OrderDeliveryDetailItem other = (OrderDeliveryDetailItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.orderIdentity==null && other.getOrderIdentity()==null) || 
             (this.orderIdentity!=null &&
              this.orderIdentity.equals(other.getOrderIdentity()))) &&
            ((this.orderStatusCode==null && other.getOrderStatusCode()==null) || 
             (this.orderStatusCode!=null &&
              this.orderStatusCode.equals(other.getOrderStatusCode()))) &&
            ((this.referenceNumber==null && other.getReferenceNumber()==null) || 
             (this.referenceNumber!=null &&
              this.referenceNumber.equals(other.getReferenceNumber())));
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
        if (getOrderIdentity() != null) {
            _hashCode += getOrderIdentity().hashCode();
        }
        if (getOrderStatusCode() != null) {
            _hashCode += getOrderStatusCode().hashCode();
        }
        if (getReferenceNumber() != null) {
            _hashCode += getReferenceNumber().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OrderDeliveryDetailItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderDeliveryDetailItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderStatusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderStatusCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "referenceNumber"));
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
