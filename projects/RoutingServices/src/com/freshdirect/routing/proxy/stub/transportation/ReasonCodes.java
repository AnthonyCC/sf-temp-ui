/**
 * ReasonCodes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class ReasonCodes  implements java.io.Serializable {
    private java.lang.String deliveryCode;

    private java.lang.String overCode;

    private java.lang.String shortCode;

    private java.lang.String damagedCode;

    private java.lang.String pickupCode;

    public ReasonCodes() {
    }

    public ReasonCodes(
           java.lang.String deliveryCode,
           java.lang.String overCode,
           java.lang.String shortCode,
           java.lang.String damagedCode,
           java.lang.String pickupCode) {
           this.deliveryCode = deliveryCode;
           this.overCode = overCode;
           this.shortCode = shortCode;
           this.damagedCode = damagedCode;
           this.pickupCode = pickupCode;
    }


    /**
     * Gets the deliveryCode value for this ReasonCodes.
     * 
     * @return deliveryCode
     */
    public java.lang.String getDeliveryCode() {
        return deliveryCode;
    }


    /**
     * Sets the deliveryCode value for this ReasonCodes.
     * 
     * @param deliveryCode
     */
    public void setDeliveryCode(java.lang.String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }


    /**
     * Gets the overCode value for this ReasonCodes.
     * 
     * @return overCode
     */
    public java.lang.String getOverCode() {
        return overCode;
    }


    /**
     * Sets the overCode value for this ReasonCodes.
     * 
     * @param overCode
     */
    public void setOverCode(java.lang.String overCode) {
        this.overCode = overCode;
    }


    /**
     * Gets the shortCode value for this ReasonCodes.
     * 
     * @return shortCode
     */
    public java.lang.String getShortCode() {
        return shortCode;
    }


    /**
     * Sets the shortCode value for this ReasonCodes.
     * 
     * @param shortCode
     */
    public void setShortCode(java.lang.String shortCode) {
        this.shortCode = shortCode;
    }


    /**
     * Gets the damagedCode value for this ReasonCodes.
     * 
     * @return damagedCode
     */
    public java.lang.String getDamagedCode() {
        return damagedCode;
    }


    /**
     * Sets the damagedCode value for this ReasonCodes.
     * 
     * @param damagedCode
     */
    public void setDamagedCode(java.lang.String damagedCode) {
        this.damagedCode = damagedCode;
    }


    /**
     * Gets the pickupCode value for this ReasonCodes.
     * 
     * @return pickupCode
     */
    public java.lang.String getPickupCode() {
        return pickupCode;
    }


    /**
     * Sets the pickupCode value for this ReasonCodes.
     * 
     * @param pickupCode
     */
    public void setPickupCode(java.lang.String pickupCode) {
        this.pickupCode = pickupCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReasonCodes)) return false;
        ReasonCodes other = (ReasonCodes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.deliveryCode==null && other.getDeliveryCode()==null) || 
             (this.deliveryCode!=null &&
              this.deliveryCode.equals(other.getDeliveryCode()))) &&
            ((this.overCode==null && other.getOverCode()==null) || 
             (this.overCode!=null &&
              this.overCode.equals(other.getOverCode()))) &&
            ((this.shortCode==null && other.getShortCode()==null) || 
             (this.shortCode!=null &&
              this.shortCode.equals(other.getShortCode()))) &&
            ((this.damagedCode==null && other.getDamagedCode()==null) || 
             (this.damagedCode!=null &&
              this.damagedCode.equals(other.getDamagedCode()))) &&
            ((this.pickupCode==null && other.getPickupCode()==null) || 
             (this.pickupCode!=null &&
              this.pickupCode.equals(other.getPickupCode())));
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
        if (getDeliveryCode() != null) {
            _hashCode += getDeliveryCode().hashCode();
        }
        if (getOverCode() != null) {
            _hashCode += getOverCode().hashCode();
        }
        if (getShortCode() != null) {
            _hashCode += getShortCode().hashCode();
        }
        if (getDamagedCode() != null) {
            _hashCode += getDamagedCode().hashCode();
        }
        if (getPickupCode() != null) {
            _hashCode += getPickupCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReasonCodes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ReasonCodes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deliveryCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "overCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shortCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "shortCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("damagedCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "damagedCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pickupCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "pickupCode"));
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
