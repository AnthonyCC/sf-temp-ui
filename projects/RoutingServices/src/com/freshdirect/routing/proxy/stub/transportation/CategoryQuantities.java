/**
 * CategoryQuantities.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class CategoryQuantities  extends com.freshdirect.routing.proxy.stub.transportation.Quantities  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory1;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory2;

    private com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory3;

    public CategoryQuantities() {
    }

    public CategoryQuantities(
           double size1,
           double size2,
           double size3,
           com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory1,
           com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory2,
           com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory3) {
        super(
            size1,
            size2,
            size3);
        this.subtotalCategory1 = subtotalCategory1;
        this.subtotalCategory2 = subtotalCategory2;
        this.subtotalCategory3 = subtotalCategory3;
    }


    /**
     * Gets the subtotalCategory1 value for this CategoryQuantities.
     * 
     * @return subtotalCategory1
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getSubtotalCategory1() {
        return subtotalCategory1;
    }


    /**
     * Sets the subtotalCategory1 value for this CategoryQuantities.
     * 
     * @param subtotalCategory1
     */
    public void setSubtotalCategory1(com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory1) {
        this.subtotalCategory1 = subtotalCategory1;
    }


    /**
     * Gets the subtotalCategory2 value for this CategoryQuantities.
     * 
     * @return subtotalCategory2
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getSubtotalCategory2() {
        return subtotalCategory2;
    }


    /**
     * Sets the subtotalCategory2 value for this CategoryQuantities.
     * 
     * @param subtotalCategory2
     */
    public void setSubtotalCategory2(com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory2) {
        this.subtotalCategory2 = subtotalCategory2;
    }


    /**
     * Gets the subtotalCategory3 value for this CategoryQuantities.
     * 
     * @return subtotalCategory3
     */
    public com.freshdirect.routing.proxy.stub.transportation.Quantities getSubtotalCategory3() {
        return subtotalCategory3;
    }


    /**
     * Sets the subtotalCategory3 value for this CategoryQuantities.
     * 
     * @param subtotalCategory3
     */
    public void setSubtotalCategory3(com.freshdirect.routing.proxy.stub.transportation.Quantities subtotalCategory3) {
        this.subtotalCategory3 = subtotalCategory3;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CategoryQuantities)) return false;
        CategoryQuantities other = (CategoryQuantities) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.subtotalCategory1==null && other.getSubtotalCategory1()==null) || 
             (this.subtotalCategory1!=null &&
              this.subtotalCategory1.equals(other.getSubtotalCategory1()))) &&
            ((this.subtotalCategory2==null && other.getSubtotalCategory2()==null) || 
             (this.subtotalCategory2!=null &&
              this.subtotalCategory2.equals(other.getSubtotalCategory2()))) &&
            ((this.subtotalCategory3==null && other.getSubtotalCategory3()==null) || 
             (this.subtotalCategory3!=null &&
              this.subtotalCategory3.equals(other.getSubtotalCategory3())));
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
        if (getSubtotalCategory1() != null) {
            _hashCode += getSubtotalCategory1().hashCode();
        }
        if (getSubtotalCategory2() != null) {
            _hashCode += getSubtotalCategory2().hashCode();
        }
        if (getSubtotalCategory3() != null) {
            _hashCode += getSubtotalCategory3().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CategoryQuantities.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "CategoryQuantities"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subtotalCategory1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "subtotalCategory1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subtotalCategory2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "subtotalCategory2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subtotalCategory3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "subtotalCategory3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Quantities"));
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
