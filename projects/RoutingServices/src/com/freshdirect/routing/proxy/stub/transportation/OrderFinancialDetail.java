/**
 * OrderFinancialDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class OrderFinancialDetail  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetailIdentity orderFinancialDetailIdentity;

    private java.lang.String financialDetailCode;

    private double amount;

    private java.lang.String detailID;

    private java.lang.String detailDescription;

    private java.util.Date detailDate;

    public OrderFinancialDetail() {
    }

    public OrderFinancialDetail(
           com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetailIdentity orderFinancialDetailIdentity,
           java.lang.String financialDetailCode,
           double amount,
           java.lang.String detailID,
           java.lang.String detailDescription,
           java.util.Date detailDate) {
           this.orderFinancialDetailIdentity = orderFinancialDetailIdentity;
           this.financialDetailCode = financialDetailCode;
           this.amount = amount;
           this.detailID = detailID;
           this.detailDescription = detailDescription;
           this.detailDate = detailDate;
    }


    /**
     * Gets the orderFinancialDetailIdentity value for this OrderFinancialDetail.
     * 
     * @return orderFinancialDetailIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetailIdentity getOrderFinancialDetailIdentity() {
        return orderFinancialDetailIdentity;
    }


    /**
     * Sets the orderFinancialDetailIdentity value for this OrderFinancialDetail.
     * 
     * @param orderFinancialDetailIdentity
     */
    public void setOrderFinancialDetailIdentity(com.freshdirect.routing.proxy.stub.transportation.OrderFinancialDetailIdentity orderFinancialDetailIdentity) {
        this.orderFinancialDetailIdentity = orderFinancialDetailIdentity;
    }


    /**
     * Gets the financialDetailCode value for this OrderFinancialDetail.
     * 
     * @return financialDetailCode
     */
    public java.lang.String getFinancialDetailCode() {
        return financialDetailCode;
    }


    /**
     * Sets the financialDetailCode value for this OrderFinancialDetail.
     * 
     * @param financialDetailCode
     */
    public void setFinancialDetailCode(java.lang.String financialDetailCode) {
        this.financialDetailCode = financialDetailCode;
    }


    /**
     * Gets the amount value for this OrderFinancialDetail.
     * 
     * @return amount
     */
    public double getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this OrderFinancialDetail.
     * 
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }


    /**
     * Gets the detailID value for this OrderFinancialDetail.
     * 
     * @return detailID
     */
    public java.lang.String getDetailID() {
        return detailID;
    }


    /**
     * Sets the detailID value for this OrderFinancialDetail.
     * 
     * @param detailID
     */
    public void setDetailID(java.lang.String detailID) {
        this.detailID = detailID;
    }


    /**
     * Gets the detailDescription value for this OrderFinancialDetail.
     * 
     * @return detailDescription
     */
    public java.lang.String getDetailDescription() {
        return detailDescription;
    }


    /**
     * Sets the detailDescription value for this OrderFinancialDetail.
     * 
     * @param detailDescription
     */
    public void setDetailDescription(java.lang.String detailDescription) {
        this.detailDescription = detailDescription;
    }


    /**
     * Gets the detailDate value for this OrderFinancialDetail.
     * 
     * @return detailDate
     */
    public java.util.Date getDetailDate() {
        return detailDate;
    }


    /**
     * Sets the detailDate value for this OrderFinancialDetail.
     * 
     * @param detailDate
     */
    public void setDetailDate(java.util.Date detailDate) {
        this.detailDate = detailDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OrderFinancialDetail)) return false;
        OrderFinancialDetail other = (OrderFinancialDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.orderFinancialDetailIdentity==null && other.getOrderFinancialDetailIdentity()==null) || 
             (this.orderFinancialDetailIdentity!=null &&
              this.orderFinancialDetailIdentity.equals(other.getOrderFinancialDetailIdentity()))) &&
            ((this.financialDetailCode==null && other.getFinancialDetailCode()==null) || 
             (this.financialDetailCode!=null &&
              this.financialDetailCode.equals(other.getFinancialDetailCode()))) &&
            this.amount == other.getAmount() &&
            ((this.detailID==null && other.getDetailID()==null) || 
             (this.detailID!=null &&
              this.detailID.equals(other.getDetailID()))) &&
            ((this.detailDescription==null && other.getDetailDescription()==null) || 
             (this.detailDescription!=null &&
              this.detailDescription.equals(other.getDetailDescription()))) &&
            ((this.detailDate==null && other.getDetailDate()==null) || 
             (this.detailDate!=null &&
              this.detailDate.equals(other.getDetailDate())));
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
        if (getOrderFinancialDetailIdentity() != null) {
            _hashCode += getOrderFinancialDetailIdentity().hashCode();
        }
        if (getFinancialDetailCode() != null) {
            _hashCode += getFinancialDetailCode().hashCode();
        }
        _hashCode += new Double(getAmount()).hashCode();
        if (getDetailID() != null) {
            _hashCode += getDetailID().hashCode();
        }
        if (getDetailDescription() != null) {
            _hashCode += getDetailDescription().hashCode();
        }
        if (getDetailDate() != null) {
            _hashCode += getDetailDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OrderFinancialDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderFinancialDetail"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderFinancialDetailIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderFinancialDetailIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderFinancialDetailIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("financialDetailCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "financialDetailCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "amount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detailID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "detailID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detailDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "detailDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detailDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "detailDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
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
