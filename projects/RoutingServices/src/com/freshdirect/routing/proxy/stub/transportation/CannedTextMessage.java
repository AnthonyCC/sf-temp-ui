/**
 * CannedTextMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class CannedTextMessage  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageIdentity cannedTextMessageIdentity;

    private java.lang.String text;

    public CannedTextMessage() {
    }

    public CannedTextMessage(
           com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageIdentity cannedTextMessageIdentity,
           java.lang.String text) {
           this.cannedTextMessageIdentity = cannedTextMessageIdentity;
           this.text = text;
    }


    /**
     * Gets the cannedTextMessageIdentity value for this CannedTextMessage.
     * 
     * @return cannedTextMessageIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageIdentity getCannedTextMessageIdentity() {
        return cannedTextMessageIdentity;
    }


    /**
     * Sets the cannedTextMessageIdentity value for this CannedTextMessage.
     * 
     * @param cannedTextMessageIdentity
     */
    public void setCannedTextMessageIdentity(com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageIdentity cannedTextMessageIdentity) {
        this.cannedTextMessageIdentity = cannedTextMessageIdentity;
    }


    /**
     * Gets the text value for this CannedTextMessage.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this CannedTextMessage.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CannedTextMessage)) return false;
        CannedTextMessage other = (CannedTextMessage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cannedTextMessageIdentity==null && other.getCannedTextMessageIdentity()==null) || 
             (this.cannedTextMessageIdentity!=null &&
              this.cannedTextMessageIdentity.equals(other.getCannedTextMessageIdentity()))) &&
            ((this.text==null && other.getText()==null) || 
             (this.text!=null &&
              this.text.equals(other.getText())));
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
        if (getCannedTextMessageIdentity() != null) {
            _hashCode += getCannedTextMessageIdentity().hashCode();
        }
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CannedTextMessage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "CannedTextMessage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cannedTextMessageIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "cannedTextMessageIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "CannedTextMessageIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "text"));
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
