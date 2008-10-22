/**
 * LineItemDeliveryDetailItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class LineItemDeliveryDetailItem  extends com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailItem  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.LineItemIdentity lineItemIdentity;

    private java.lang.String orderStatusCode;

    private java.lang.String referenceNumber;

    public LineItemDeliveryDetailItem() {
    }

    public LineItemDeliveryDetailItem(
           com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities,
           com.freshdirect.routing.proxy.stub.transportation.ReasonCodes reasonCodes,
           java.lang.String userDefinedField1,
           java.lang.String userDefinedField2,
           java.lang.String userDefinedField3,
           com.freshdirect.routing.proxy.stub.transportation.LineItemIdentity lineItemIdentity,
           java.lang.String orderStatusCode,
           java.lang.String referenceNumber) {
        super(
            quantities,
            reasonCodes,
            userDefinedField1,
            userDefinedField2,
            userDefinedField3);
        this.lineItemIdentity = lineItemIdentity;
        this.orderStatusCode = orderStatusCode;
        this.referenceNumber = referenceNumber;
    }


    /**
     * Gets the lineItemIdentity value for this LineItemDeliveryDetailItem.
     * 
     * @return lineItemIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.LineItemIdentity getLineItemIdentity() {
        return lineItemIdentity;
    }


    /**
     * Sets the lineItemIdentity value for this LineItemDeliveryDetailItem.
     * 
     * @param lineItemIdentity
     */
    public void setLineItemIdentity(com.freshdirect.routing.proxy.stub.transportation.LineItemIdentity lineItemIdentity) {
        this.lineItemIdentity = lineItemIdentity;
    }


    /**
     * Gets the orderStatusCode value for this LineItemDeliveryDetailItem.
     * 
     * @return orderStatusCode
     */
    public java.lang.String getOrderStatusCode() {
        return orderStatusCode;
    }


    /**
     * Sets the orderStatusCode value for this LineItemDeliveryDetailItem.
     * 
     * @param orderStatusCode
     */
    public void setOrderStatusCode(java.lang.String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }


    /**
     * Gets the referenceNumber value for this LineItemDeliveryDetailItem.
     * 
     * @return referenceNumber
     */
    public java.lang.String getReferenceNumber() {
        return referenceNumber;
    }


    /**
     * Sets the referenceNumber value for this LineItemDeliveryDetailItem.
     * 
     * @param referenceNumber
     */
    public void setReferenceNumber(java.lang.String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LineItemDeliveryDetailItem)) return false;
        LineItemDeliveryDetailItem other = (LineItemDeliveryDetailItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.lineItemIdentity==null && other.getLineItemIdentity()==null) || 
             (this.lineItemIdentity!=null &&
              this.lineItemIdentity.equals(other.getLineItemIdentity()))) &&
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
        if (getLineItemIdentity() != null) {
            _hashCode += getLineItemIdentity().hashCode();
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
        new org.apache.axis.description.TypeDesc(LineItemDeliveryDetailItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LineItemDeliveryDetailItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineItemIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lineItemIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LineItemIdentity"));
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
