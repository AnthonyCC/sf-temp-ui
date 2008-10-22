/**
 * LineItemContainer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class LineItemContainer  implements java.io.Serializable {
    private java.lang.String lineItemID;

    private com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities;

    public LineItemContainer() {
    }

    public LineItemContainer(
           java.lang.String lineItemID,
           com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities) {
           this.lineItemID = lineItemID;
           this.quantities = quantities;
    }


    /**
     * Gets the lineItemID value for this LineItemContainer.
     * 
     * @return lineItemID
     */
    public java.lang.String getLineItemID() {
        return lineItemID;
    }


    /**
     * Sets the lineItemID value for this LineItemContainer.
     * 
     * @param lineItemID
     */
    public void setLineItemID(java.lang.String lineItemID) {
        this.lineItemID = lineItemID;
    }


    /**
     * Gets the quantities value for this LineItemContainer.
     * 
     * @return quantities
     */
    public com.freshdirect.routing.proxy.stub.transportation.ItemQuantities getQuantities() {
        return quantities;
    }


    /**
     * Sets the quantities value for this LineItemContainer.
     * 
     * @param quantities
     */
    public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities) {
        this.quantities = quantities;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LineItemContainer)) return false;
        LineItemContainer other = (LineItemContainer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lineItemID==null && other.getLineItemID()==null) || 
             (this.lineItemID!=null &&
              this.lineItemID.equals(other.getLineItemID()))) &&
            ((this.quantities==null && other.getQuantities()==null) || 
             (this.quantities!=null &&
              this.quantities.equals(other.getQuantities())));
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
        if (getLineItemID() != null) {
            _hashCode += getLineItemID().hashCode();
        }
        if (getQuantities() != null) {
            _hashCode += getQuantities().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LineItemContainer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LineItemContainer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineItemID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lineItemID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "quantities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ItemQuantities"));
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
