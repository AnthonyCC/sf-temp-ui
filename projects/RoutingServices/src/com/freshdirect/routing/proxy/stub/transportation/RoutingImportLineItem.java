/**
 * RoutingImportLineItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingImportLineItem  implements java.io.Serializable {
    private java.lang.String sku;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities quantities;

    private com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields;

    public RoutingImportLineItem() {
    }

    public RoutingImportLineItem(
           java.lang.String sku,
           com.freshdirect.routing.proxy.stub.transportation.Quantities quantities,
           com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields) {
           this.sku = sku;
           this.quantities = quantities;
           this.userDefinedFields = userDefinedFields;
    }


    /**
     * Gets the sku value for this RoutingImportLineItem.
     * 
     * @return sku
     */
    public java.lang.String getSku() {
        return sku;
    }


    /**
     * Sets the sku value for this RoutingImportLineItem.
     * 
     * @param sku
     */
    public void setSku(java.lang.String sku) {
        this.sku = sku;
    }


    /**
     * Gets the quantities value for this RoutingImportLineItem.
     * 
     * @return quantities
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getQuantities() {
        return quantities;
    }


    /**
     * Sets the quantities value for this RoutingImportLineItem.
     * 
     * @param quantities
     */
    public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.Quantities quantities) {
        this.quantities = quantities;
    }


    /**
     * Gets the userDefinedFields value for this RoutingImportLineItem.
     * 
     * @return userDefinedFields
     */
    public com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields getUserDefinedFields() {
        return userDefinedFields;
    }


    /**
     * Sets the userDefinedFields value for this RoutingImportLineItem.
     * 
     * @param userDefinedFields
     */
    public void setUserDefinedFields(com.freshdirect.routing.proxy.stub.transportation.UserDefinedFields userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingImportLineItem)) return false;
        RoutingImportLineItem other = (RoutingImportLineItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sku==null && other.getSku()==null) || 
             (this.sku!=null &&
              this.sku.equals(other.getSku()))) &&
            ((this.quantities==null && other.getQuantities()==null) || 
             (this.quantities!=null &&
              this.quantities.equals(other.getQuantities()))) &&
            ((this.userDefinedFields==null && other.getUserDefinedFields()==null) || 
             (this.userDefinedFields!=null &&
              this.userDefinedFields.equals(other.getUserDefinedFields())));
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
        if (getSku() != null) {
            _hashCode += getSku().hashCode();
        }
        if (getQuantities() != null) {
            _hashCode += getQuantities().hashCode();
        }
        if (getUserDefinedFields() != null) {
            _hashCode += getUserDefinedFields().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingImportLineItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingImportLineItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sku");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sku"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "quantities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UserDefinedFields"));
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
