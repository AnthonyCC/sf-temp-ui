/**
 * Address.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Address  implements java.io.Serializable {
    private java.lang.String line1;

    private java.lang.String line2;

    private java.lang.String crossStreet;

    private java.lang.String region1;

    private java.lang.String region2;

    private java.lang.String region3;

    private java.lang.String postalCode;

    private java.lang.String country;

    public Address() {
    }

    public Address(
           java.lang.String line1,
           java.lang.String line2,
           java.lang.String crossStreet,
           java.lang.String region1,
           java.lang.String region2,
           java.lang.String region3,
           java.lang.String postalCode,
           java.lang.String country) {
           this.line1 = line1;
           this.line2 = line2;
           this.crossStreet = crossStreet;
           this.region1 = region1;
           this.region2 = region2;
           this.region3 = region3;
           this.postalCode = postalCode;
           this.country = country;
    }


    /**
     * Gets the line1 value for this Address.
     * 
     * @return line1
     */
    public java.lang.String getLine1() {
        return line1;
    }


    /**
     * Sets the line1 value for this Address.
     * 
     * @param line1
     */
    public void setLine1(java.lang.String line1) {
        this.line1 = line1;
    }


    /**
     * Gets the line2 value for this Address.
     * 
     * @return line2
     */
    public java.lang.String getLine2() {
        return line2;
    }


    /**
     * Sets the line2 value for this Address.
     * 
     * @param line2
     */
    public void setLine2(java.lang.String line2) {
        this.line2 = line2;
    }


    /**
     * Gets the crossStreet value for this Address.
     * 
     * @return crossStreet
     */
    public java.lang.String getCrossStreet() {
        return crossStreet;
    }


    /**
     * Sets the crossStreet value for this Address.
     * 
     * @param crossStreet
     */
    public void setCrossStreet(java.lang.String crossStreet) {
        this.crossStreet = crossStreet;
    }


    /**
     * Gets the region1 value for this Address.
     * 
     * @return region1
     */
    public java.lang.String getRegion1() {
        return region1;
    }


    /**
     * Sets the region1 value for this Address.
     * 
     * @param region1
     */
    public void setRegion1(java.lang.String region1) {
        this.region1 = region1;
    }


    /**
     * Gets the region2 value for this Address.
     * 
     * @return region2
     */
    public java.lang.String getRegion2() {
        return region2;
    }


    /**
     * Sets the region2 value for this Address.
     * 
     * @param region2
     */
    public void setRegion2(java.lang.String region2) {
        this.region2 = region2;
    }


    /**
     * Gets the region3 value for this Address.
     * 
     * @return region3
     */
    public java.lang.String getRegion3() {
        return region3;
    }


    /**
     * Sets the region3 value for this Address.
     * 
     * @param region3
     */
    public void setRegion3(java.lang.String region3) {
        this.region3 = region3;
    }


    /**
     * Gets the postalCode value for this Address.
     * 
     * @return postalCode
     */
    public java.lang.String getPostalCode() {
        return postalCode;
    }


    /**
     * Sets the postalCode value for this Address.
     * 
     * @param postalCode
     */
    public void setPostalCode(java.lang.String postalCode) {
        this.postalCode = postalCode;
    }


    /**
     * Gets the country value for this Address.
     * 
     * @return country
     */
    public java.lang.String getCountry() {
        return country;
    }


    /**
     * Sets the country value for this Address.
     * 
     * @param country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Address)) return false;
        Address other = (Address) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.line1==null && other.getLine1()==null) || 
             (this.line1!=null &&
              this.line1.equals(other.getLine1()))) &&
            ((this.line2==null && other.getLine2()==null) || 
             (this.line2!=null &&
              this.line2.equals(other.getLine2()))) &&
            ((this.crossStreet==null && other.getCrossStreet()==null) || 
             (this.crossStreet!=null &&
              this.crossStreet.equals(other.getCrossStreet()))) &&
            ((this.region1==null && other.getRegion1()==null) || 
             (this.region1!=null &&
              this.region1.equals(other.getRegion1()))) &&
            ((this.region2==null && other.getRegion2()==null) || 
             (this.region2!=null &&
              this.region2.equals(other.getRegion2()))) &&
            ((this.region3==null && other.getRegion3()==null) || 
             (this.region3!=null &&
              this.region3.equals(other.getRegion3()))) &&
            ((this.postalCode==null && other.getPostalCode()==null) || 
             (this.postalCode!=null &&
              this.postalCode.equals(other.getPostalCode()))) &&
            ((this.country==null && other.getCountry()==null) || 
             (this.country!=null &&
              this.country.equals(other.getCountry())));
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
        if (getLine1() != null) {
            _hashCode += getLine1().hashCode();
        }
        if (getLine2() != null) {
            _hashCode += getLine2().hashCode();
        }
        if (getCrossStreet() != null) {
            _hashCode += getCrossStreet().hashCode();
        }
        if (getRegion1() != null) {
            _hashCode += getRegion1().hashCode();
        }
        if (getRegion2() != null) {
            _hashCode += getRegion2().hashCode();
        }
        if (getRegion3() != null) {
            _hashCode += getRegion3().hashCode();
        }
        if (getPostalCode() != null) {
            _hashCode += getPostalCode().hashCode();
        }
        if (getCountry() != null) {
            _hashCode += getCountry().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Address.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Address"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("line1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "line1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("line2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "line2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("crossStreet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "crossStreet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "region1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "region2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "region3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postalCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "postalCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "country"));
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
