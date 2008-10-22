/**
 * LineItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class LineItem  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.LineItemIdentity lineItemIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities;

    private com.freshdirect.routing.proxy.stub.transportation.ReasonCodes reasonCodes;

    private java.lang.String description;

    private java.lang.String userDefinedField1;

    private java.lang.String userDefinedField2;

    private java.lang.String userDefinedField3;

    private boolean removeFlag;

    public LineItem() {
    }

    public LineItem(
           com.freshdirect.routing.proxy.stub.transportation.LineItemIdentity lineItemIdentity,
           com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities,
           com.freshdirect.routing.proxy.stub.transportation.ReasonCodes reasonCodes,
           java.lang.String description,
           java.lang.String userDefinedField1,
           java.lang.String userDefinedField2,
           java.lang.String userDefinedField3,
           boolean removeFlag) {
           this.lineItemIdentity = lineItemIdentity;
           this.quantities = quantities;
           this.reasonCodes = reasonCodes;
           this.description = description;
           this.userDefinedField1 = userDefinedField1;
           this.userDefinedField2 = userDefinedField2;
           this.userDefinedField3 = userDefinedField3;
           this.removeFlag = removeFlag;
    }


    /**
     * Gets the lineItemIdentity value for this LineItem.
     * 
     * @return lineItemIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.LineItemIdentity getLineItemIdentity() {
        return lineItemIdentity;
    }


    /**
     * Sets the lineItemIdentity value for this LineItem.
     * 
     * @param lineItemIdentity
     */
    public void setLineItemIdentity(com.freshdirect.routing.proxy.stub.transportation.LineItemIdentity lineItemIdentity) {
        this.lineItemIdentity = lineItemIdentity;
    }


    /**
     * Gets the quantities value for this LineItem.
     * 
     * @return quantities
     */
    public com.freshdirect.routing.proxy.stub.transportation.ItemQuantities getQuantities() {
        return quantities;
    }


    /**
     * Sets the quantities value for this LineItem.
     * 
     * @param quantities
     */
    public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities) {
        this.quantities = quantities;
    }


    /**
     * Gets the reasonCodes value for this LineItem.
     * 
     * @return reasonCodes
     */
    public com.freshdirect.routing.proxy.stub.transportation.ReasonCodes getReasonCodes() {
        return reasonCodes;
    }


    /**
     * Sets the reasonCodes value for this LineItem.
     * 
     * @param reasonCodes
     */
    public void setReasonCodes(com.freshdirect.routing.proxy.stub.transportation.ReasonCodes reasonCodes) {
        this.reasonCodes = reasonCodes;
    }


    /**
     * Gets the description value for this LineItem.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this LineItem.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the userDefinedField1 value for this LineItem.
     * 
     * @return userDefinedField1
     */
    public java.lang.String getUserDefinedField1() {
        return userDefinedField1;
    }


    /**
     * Sets the userDefinedField1 value for this LineItem.
     * 
     * @param userDefinedField1
     */
    public void setUserDefinedField1(java.lang.String userDefinedField1) {
        this.userDefinedField1 = userDefinedField1;
    }


    /**
     * Gets the userDefinedField2 value for this LineItem.
     * 
     * @return userDefinedField2
     */
    public java.lang.String getUserDefinedField2() {
        return userDefinedField2;
    }


    /**
     * Sets the userDefinedField2 value for this LineItem.
     * 
     * @param userDefinedField2
     */
    public void setUserDefinedField2(java.lang.String userDefinedField2) {
        this.userDefinedField2 = userDefinedField2;
    }


    /**
     * Gets the userDefinedField3 value for this LineItem.
     * 
     * @return userDefinedField3
     */
    public java.lang.String getUserDefinedField3() {
        return userDefinedField3;
    }


    /**
     * Sets the userDefinedField3 value for this LineItem.
     * 
     * @param userDefinedField3
     */
    public void setUserDefinedField3(java.lang.String userDefinedField3) {
        this.userDefinedField3 = userDefinedField3;
    }


    /**
     * Gets the removeFlag value for this LineItem.
     * 
     * @return removeFlag
     */
    public boolean isRemoveFlag() {
        return removeFlag;
    }


    /**
     * Sets the removeFlag value for this LineItem.
     * 
     * @param removeFlag
     */
    public void setRemoveFlag(boolean removeFlag) {
        this.removeFlag = removeFlag;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LineItem)) return false;
        LineItem other = (LineItem) obj;
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
            ((this.quantities==null && other.getQuantities()==null) || 
             (this.quantities!=null &&
              this.quantities.equals(other.getQuantities()))) &&
            ((this.reasonCodes==null && other.getReasonCodes()==null) || 
             (this.reasonCodes!=null &&
              this.reasonCodes.equals(other.getReasonCodes()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.userDefinedField1==null && other.getUserDefinedField1()==null) || 
             (this.userDefinedField1!=null &&
              this.userDefinedField1.equals(other.getUserDefinedField1()))) &&
            ((this.userDefinedField2==null && other.getUserDefinedField2()==null) || 
             (this.userDefinedField2!=null &&
              this.userDefinedField2.equals(other.getUserDefinedField2()))) &&
            ((this.userDefinedField3==null && other.getUserDefinedField3()==null) || 
             (this.userDefinedField3!=null &&
              this.userDefinedField3.equals(other.getUserDefinedField3()))) &&
            this.removeFlag == other.isRemoveFlag();
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
        if (getQuantities() != null) {
            _hashCode += getQuantities().hashCode();
        }
        if (getReasonCodes() != null) {
            _hashCode += getReasonCodes().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getUserDefinedField1() != null) {
            _hashCode += getUserDefinedField1().hashCode();
        }
        if (getUserDefinedField2() != null) {
            _hashCode += getUserDefinedField2().hashCode();
        }
        if (getUserDefinedField3() != null) {
            _hashCode += getUserDefinedField3().hashCode();
        }
        _hashCode += (isRemoveFlag() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LineItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LineItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineItemIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lineItemIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LineItemIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "quantities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ItemQuantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reasonCodes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "reasonCodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ReasonCodes"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefinedField3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "userDefinedField3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("removeFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "removeFlag"));
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
