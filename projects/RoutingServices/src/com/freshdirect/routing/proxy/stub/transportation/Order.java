/**
 * Order.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Order  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.OrderIdentity orderIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities;

    private com.freshdirect.routing.proxy.stub.transportation.ReasonCodes reasonCodes;

    private java.lang.String userDefinedField1;

    private java.lang.String userDefinedField2;

    private java.lang.String userDefinedField3;

    private java.lang.String specialInstructions;

    private java.lang.String referenceNumber;

    private java.lang.String orderStatusCode;

    private boolean removeFlag;

    private com.freshdirect.routing.proxy.stub.transportation.LineItem[] lineItems;

    private com.freshdirect.routing.proxy.stub.transportation.OrderContainer[] containers;

    private com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetail[] financialDetails;

    private com.freshdirect.routing.proxy.stub.transportation.OrderActivity[] activities;

    public Order() {
    }

    public Order(
           com.freshdirect.routing.proxy.stub.transportation.OrderIdentity orderIdentity,
           com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities,
           com.freshdirect.routing.proxy.stub.transportation.ReasonCodes reasonCodes,
           java.lang.String userDefinedField1,
           java.lang.String userDefinedField2,
           java.lang.String userDefinedField3,
           java.lang.String specialInstructions,
           java.lang.String referenceNumber,
           java.lang.String orderStatusCode,
           boolean removeFlag,
           com.freshdirect.routing.proxy.stub.transportation.LineItem[] lineItems,
           com.freshdirect.routing.proxy.stub.transportation.OrderContainer[] containers,
           com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetail[] financialDetails,
           com.freshdirect.routing.proxy.stub.transportation.OrderActivity[] activities) {
           this.orderIdentity = orderIdentity;
           this.quantities = quantities;
           this.reasonCodes = reasonCodes;
           this.userDefinedField1 = userDefinedField1;
           this.userDefinedField2 = userDefinedField2;
           this.userDefinedField3 = userDefinedField3;
           this.specialInstructions = specialInstructions;
           this.referenceNumber = referenceNumber;
           this.orderStatusCode = orderStatusCode;
           this.removeFlag = removeFlag;
           this.lineItems = lineItems;
           this.containers = containers;
           this.financialDetails = financialDetails;
           this.activities = activities;
    }


    /**
     * Gets the orderIdentity value for this Order.
     * 
     * @return orderIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.OrderIdentity getOrderIdentity() {
        return orderIdentity;
    }


    /**
     * Sets the orderIdentity value for this Order.
     * 
     * @param orderIdentity
     */
    public void setOrderIdentity(com.freshdirect.routing.proxy.stub.transportation.OrderIdentity orderIdentity) {
        this.orderIdentity = orderIdentity;
    }


    /**
     * Gets the quantities value for this Order.
     * 
     * @return quantities
     */
    public com.freshdirect.routing.proxy.stub.transportation.ItemQuantities getQuantities() {
        return quantities;
    }


    /**
     * Sets the quantities value for this Order.
     * 
     * @param quantities
     */
    public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.ItemQuantities quantities) {
        this.quantities = quantities;
    }


    /**
     * Gets the reasonCodes value for this Order.
     * 
     * @return reasonCodes
     */
    public com.freshdirect.routing.proxy.stub.transportation.ReasonCodes getReasonCodes() {
        return reasonCodes;
    }


    /**
     * Sets the reasonCodes value for this Order.
     * 
     * @param reasonCodes
     */
    public void setReasonCodes(com.freshdirect.routing.proxy.stub.transportation.ReasonCodes reasonCodes) {
        this.reasonCodes = reasonCodes;
    }


    /**
     * Gets the userDefinedField1 value for this Order.
     * 
     * @return userDefinedField1
     */
    public java.lang.String getUserDefinedField1() {
        return userDefinedField1;
    }


    /**
     * Sets the userDefinedField1 value for this Order.
     * 
     * @param userDefinedField1
     */
    public void setUserDefinedField1(java.lang.String userDefinedField1) {
        this.userDefinedField1 = userDefinedField1;
    }


    /**
     * Gets the userDefinedField2 value for this Order.
     * 
     * @return userDefinedField2
     */
    public java.lang.String getUserDefinedField2() {
        return userDefinedField2;
    }


    /**
     * Sets the userDefinedField2 value for this Order.
     * 
     * @param userDefinedField2
     */
    public void setUserDefinedField2(java.lang.String userDefinedField2) {
        this.userDefinedField2 = userDefinedField2;
    }


    /**
     * Gets the userDefinedField3 value for this Order.
     * 
     * @return userDefinedField3
     */
    public java.lang.String getUserDefinedField3() {
        return userDefinedField3;
    }


    /**
     * Sets the userDefinedField3 value for this Order.
     * 
     * @param userDefinedField3
     */
    public void setUserDefinedField3(java.lang.String userDefinedField3) {
        this.userDefinedField3 = userDefinedField3;
    }


    /**
     * Gets the specialInstructions value for this Order.
     * 
     * @return specialInstructions
     */
    public java.lang.String getSpecialInstructions() {
        return specialInstructions;
    }


    /**
     * Sets the specialInstructions value for this Order.
     * 
     * @param specialInstructions
     */
    public void setSpecialInstructions(java.lang.String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }


    /**
     * Gets the referenceNumber value for this Order.
     * 
     * @return referenceNumber
     */
    public java.lang.String getReferenceNumber() {
        return referenceNumber;
    }


    /**
     * Sets the referenceNumber value for this Order.
     * 
     * @param referenceNumber
     */
    public void setReferenceNumber(java.lang.String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


    /**
     * Gets the orderStatusCode value for this Order.
     * 
     * @return orderStatusCode
     */
    public java.lang.String getOrderStatusCode() {
        return orderStatusCode;
    }


    /**
     * Sets the orderStatusCode value for this Order.
     * 
     * @param orderStatusCode
     */
    public void setOrderStatusCode(java.lang.String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }


    /**
     * Gets the removeFlag value for this Order.
     * 
     * @return removeFlag
     */
    public boolean isRemoveFlag() {
        return removeFlag;
    }


    /**
     * Sets the removeFlag value for this Order.
     * 
     * @param removeFlag
     */
    public void setRemoveFlag(boolean removeFlag) {
        this.removeFlag = removeFlag;
    }


    /**
     * Gets the lineItems value for this Order.
     * 
     * @return lineItems
     */
    public com.freshdirect.routing.proxy.stub.transportation.LineItem[] getLineItems() {
        return lineItems;
    }


    /**
     * Sets the lineItems value for this Order.
     * 
     * @param lineItems
     */
    public void setLineItems(com.freshdirect.routing.proxy.stub.transportation.LineItem[] lineItems) {
        this.lineItems = lineItems;
    }

    public com.freshdirect.routing.proxy.stub.transportation.LineItem getLineItems(int i) {
        return this.lineItems[i];
    }

    public void setLineItems(int i, com.freshdirect.routing.proxy.stub.transportation.LineItem _value) {
        this.lineItems[i] = _value;
    }


    /**
     * Gets the containers value for this Order.
     * 
     * @return containers
     */
    public com.freshdirect.routing.proxy.stub.transportation.OrderContainer[] getContainers() {
        return containers;
    }


    /**
     * Sets the containers value for this Order.
     * 
     * @param containers
     */
    public void setContainers(com.freshdirect.routing.proxy.stub.transportation.OrderContainer[] containers) {
        this.containers = containers;
    }

    public com.freshdirect.routing.proxy.stub.transportation.OrderContainer getContainers(int i) {
        return this.containers[i];
    }

    public void setContainers(int i, com.freshdirect.routing.proxy.stub.transportation.OrderContainer _value) {
        this.containers[i] = _value;
    }


    /**
     * Gets the financialDetails value for this Order.
     * 
     * @return financialDetails
     */
    public com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetail[] getFinancialDetails() {
        return financialDetails;
    }


    /**
     * Sets the financialDetails value for this Order.
     * 
     * @param financialDetails
     */
    public void setFinancialDetails(com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetail[] financialDetails) {
        this.financialDetails = financialDetails;
    }

    public com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetail getFinancialDetails(int i) {
        return this.financialDetails[i];
    }

    public void setFinancialDetails(int i, com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetail _value) {
        this.financialDetails[i] = _value;
    }


    /**
     * Gets the activities value for this Order.
     * 
     * @return activities
     */
    public com.freshdirect.routing.proxy.stub.transportation.OrderActivity[] getActivities() {
        return activities;
    }


    /**
     * Sets the activities value for this Order.
     * 
     * @param activities
     */
    public void setActivities(com.freshdirect.routing.proxy.stub.transportation.OrderActivity[] activities) {
        this.activities = activities;
    }

    public com.freshdirect.routing.proxy.stub.transportation.OrderActivity getActivities(int i) {
        return this.activities[i];
    }

    public void setActivities(int i, com.freshdirect.routing.proxy.stub.transportation.OrderActivity _value) {
        this.activities[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Order)) return false;
        Order other = (Order) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.orderIdentity==null && other.getOrderIdentity()==null) || 
             (this.orderIdentity!=null &&
              this.orderIdentity.equals(other.getOrderIdentity()))) &&
            ((this.quantities==null && other.getQuantities()==null) || 
             (this.quantities!=null &&
              this.quantities.equals(other.getQuantities()))) &&
            ((this.reasonCodes==null && other.getReasonCodes()==null) || 
             (this.reasonCodes!=null &&
              this.reasonCodes.equals(other.getReasonCodes()))) &&
            ((this.userDefinedField1==null && other.getUserDefinedField1()==null) || 
             (this.userDefinedField1!=null &&
              this.userDefinedField1.equals(other.getUserDefinedField1()))) &&
            ((this.userDefinedField2==null && other.getUserDefinedField2()==null) || 
             (this.userDefinedField2!=null &&
              this.userDefinedField2.equals(other.getUserDefinedField2()))) &&
            ((this.userDefinedField3==null && other.getUserDefinedField3()==null) || 
             (this.userDefinedField3!=null &&
              this.userDefinedField3.equals(other.getUserDefinedField3()))) &&
            ((this.specialInstructions==null && other.getSpecialInstructions()==null) || 
             (this.specialInstructions!=null &&
              this.specialInstructions.equals(other.getSpecialInstructions()))) &&
            ((this.referenceNumber==null && other.getReferenceNumber()==null) || 
             (this.referenceNumber!=null &&
              this.referenceNumber.equals(other.getReferenceNumber()))) &&
            ((this.orderStatusCode==null && other.getOrderStatusCode()==null) || 
             (this.orderStatusCode!=null &&
              this.orderStatusCode.equals(other.getOrderStatusCode()))) &&
            this.removeFlag == other.isRemoveFlag() &&
            ((this.lineItems==null && other.getLineItems()==null) || 
             (this.lineItems!=null &&
              java.util.Arrays.equals(this.lineItems, other.getLineItems()))) &&
            ((this.containers==null && other.getContainers()==null) || 
             (this.containers!=null &&
              java.util.Arrays.equals(this.containers, other.getContainers()))) &&
            ((this.financialDetails==null && other.getFinancialDetails()==null) || 
             (this.financialDetails!=null &&
              java.util.Arrays.equals(this.financialDetails, other.getFinancialDetails()))) &&
            ((this.activities==null && other.getActivities()==null) || 
             (this.activities!=null &&
              java.util.Arrays.equals(this.activities, other.getActivities())));
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
        if (getOrderIdentity() != null) {
            _hashCode += getOrderIdentity().hashCode();
        }
        if (getQuantities() != null) {
            _hashCode += getQuantities().hashCode();
        }
        if (getReasonCodes() != null) {
            _hashCode += getReasonCodes().hashCode();
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
        if (getSpecialInstructions() != null) {
            _hashCode += getSpecialInstructions().hashCode();
        }
        if (getReferenceNumber() != null) {
            _hashCode += getReferenceNumber().hashCode();
        }
        if (getOrderStatusCode() != null) {
            _hashCode += getOrderStatusCode().hashCode();
        }
        _hashCode += (isRemoveFlag() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLineItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLineItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLineItems(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getContainers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContainers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContainers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getFinancialDetails() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFinancialDetails());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFinancialDetails(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getActivities() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActivities());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActivities(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Order.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Order"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderIdentity"));
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
        elemField.setFieldName("specialInstructions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "specialInstructions"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderStatusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderStatusCode"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lineItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LineItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("containers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "containers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderContainer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("financialDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "financialDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderFinancialDetail"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "activities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderActivity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
