/**
 * RoutingLineItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingLineItem  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RoutingLineItemIdentity lineItemIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity;

    private java.lang.String sku;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities quantities;

    private com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields;

    private java.lang.String skuDescription;

    public RoutingLineItem() {
    }

    public RoutingLineItem(
           com.freshdirect.routing.proxy.stub.transportation.RoutingLineItemIdentity lineItemIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity,
           java.lang.String sku,
           com.freshdirect.routing.proxy.stub.transportation.Quantities quantities,
           com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields,
           java.lang.String skuDescription) {
           this.lineItemIdentity = lineItemIdentity;
           this.orderIdentity = orderIdentity;
           this.sku = sku;
           this.quantities = quantities;
           this.userDefinedFields = userDefinedFields;
           this.skuDescription = skuDescription;
    }


    /**
     * Gets the lineItemIdentity value for this RoutingLineItem.
     * 
     * @return lineItemIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingLineItemIdentity getLineItemIdentity() {
        return lineItemIdentity;
    }


    /**
     * Sets the lineItemIdentity value for this RoutingLineItem.
     * 
     * @param lineItemIdentity
     */
    public void setLineItemIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingLineItemIdentity lineItemIdentity) {
        this.lineItemIdentity = lineItemIdentity;
    }


    /**
     * Gets the orderIdentity value for this RoutingLineItem.
     * 
     * @return orderIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity getOrderIdentity() {
        return orderIdentity;
    }


    /**
     * Sets the orderIdentity value for this RoutingLineItem.
     * 
     * @param orderIdentity
     */
    public void setOrderIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity orderIdentity) {
        this.orderIdentity = orderIdentity;
    }


    /**
     * Gets the sku value for this RoutingLineItem.
     * 
     * @return sku
     */
    public java.lang.String getSku() {
        return sku;
    }


    /**
     * Sets the sku value for this RoutingLineItem.
     * 
     * @param sku
     */
    public void setSku(java.lang.String sku) {
        this.sku = sku;
    }


    /**
     * Gets the quantities value for this RoutingLineItem.
     * 
     * @return quantities
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getQuantities() {
        return quantities;
    }


    /**
     * Sets the quantities value for this RoutingLineItem.
     * 
     * @param quantities
     */
    public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.Quantities quantities) {
        this.quantities = quantities;
    }


    /**
     * Gets the userDefinedFields value for this RoutingLineItem.
     * 
     * @return userDefinedFields
     */
    public com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }


    /**
     * Sets the userDefinedFields value for this RoutingLineItem.
     * 
     * @param userDefinedFields
     */
    public void setUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }


    /**
     * Gets the skuDescription value for this RoutingLineItem.
     * 
     * @return skuDescription
     */
    public java.lang.String getSkuDescription() {
        return skuDescription;
    }


    /**
     * Sets the skuDescription value for this RoutingLineItem.
     * 
     * @param skuDescription
     */
    public void setSkuDescription(java.lang.String skuDescription) {
        this.skuDescription = skuDescription;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingLineItem)) return false;
        RoutingLineItem other = (RoutingLineItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lineItemIdentity==null && other.getLineItemIdentity()==null) || 
             (this.lineItemIdentity!=null &&
              this.lineItemIdentity.equals(other.getLineItemIdentity()))) &&
            ((this.orderIdentity==null && other.getOrderIdentity()==null) || 
             (this.orderIdentity!=null &&
              this.orderIdentity.equals(other.getOrderIdentity()))) &&
            ((this.sku==null && other.getSku()==null) || 
             (this.sku!=null &&
              this.sku.equals(other.getSku()))) &&
            ((this.quantities==null && other.getQuantities()==null) || 
             (this.quantities!=null &&
              this.quantities.equals(other.getQuantities()))) &&
            ((this.userDefinedFields==null && other.getUserDefinedFields()==null) || 
             (this.userDefinedFields!=null &&
              this.userDefinedFields.equals(other.getUserDefinedFields()))) &&
            ((this.skuDescription==null && other.getSkuDescription()==null) || 
             (this.skuDescription!=null &&
              this.skuDescription.equals(other.getSkuDescription())));
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
        if (getLineItemIdentity() != null) {
            _hashCode += getLineItemIdentity().hashCode();
        }
        if (getOrderIdentity() != null) {
            _hashCode += getOrderIdentity().hashCode();
        }
        if (getSku() != null) {
            _hashCode += getSku().hashCode();
        }
        if (getQuantities() != null) {
            _hashCode += getQuantities().hashCode();
        }
        if (getUserDefinedFields() != null) {
            _hashCode += getUserDefinedFields().hashCode();
        }
        if (getSkuDescription() != null) {
            _hashCode += getSkuDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingLineItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingLineItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineItemIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lineItemIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingLineItemIdentity"));
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
        elemField.setFieldName("sku");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sku"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "quantities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UserDefinedFields"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skuDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "skuDescription"));
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
