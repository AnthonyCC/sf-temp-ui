/**
 * FindMapArcAddressCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class FindMapArcAddressCriteria  extends com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria  implements java.io.Serializable {
    private java.lang.String houseNumber;

    private java.lang.String streetName;

    private java.lang.String streetType;

    private java.lang.String preDirection;

    private java.lang.String postDirection;

    private java.lang.String region1;

    private java.lang.String region2;

    private java.lang.String region3;

    private java.lang.String postalCode;

    private java.lang.String country;

    public FindMapArcAddressCriteria() {
    }

    public FindMapArcAddressCriteria(
           int arcsToFind,
           com.freshdirect.routing.proxy.stub.roadnet.MapContext context,
           java.lang.String houseNumber,
           java.lang.String streetName,
           java.lang.String streetType,
           java.lang.String preDirection,
           java.lang.String postDirection,
           java.lang.String region1,
           java.lang.String region2,
           java.lang.String region3,
           java.lang.String postalCode,
           java.lang.String country) {
        super(
            arcsToFind,
            context);
        this.houseNumber = houseNumber;
        this.streetName = streetName;
        this.streetType = streetType;
        this.preDirection = preDirection;
        this.postDirection = postDirection;
        this.region1 = region1;
        this.region2 = region2;
        this.region3 = region3;
        this.postalCode = postalCode;
        this.country = country;
    }


    /**
     * Gets the houseNumber value for this FindMapArcAddressCriteria.
     * 
     * @return houseNumber
     */
    public java.lang.String getHouseNumber() {
        return houseNumber;
    }


    /**
     * Sets the houseNumber value for this FindMapArcAddressCriteria.
     * 
     * @param houseNumber
     */
    public void setHouseNumber(java.lang.String houseNumber) {
        this.houseNumber = houseNumber;
    }


    /**
     * Gets the streetName value for this FindMapArcAddressCriteria.
     * 
     * @return streetName
     */
    public java.lang.String getStreetName() {
        return streetName;
    }


    /**
     * Sets the streetName value for this FindMapArcAddressCriteria.
     * 
     * @param streetName
     */
    public void setStreetName(java.lang.String streetName) {
        this.streetName = streetName;
    }


    /**
     * Gets the streetType value for this FindMapArcAddressCriteria.
     * 
     * @return streetType
     */
    public java.lang.String getStreetType() {
        return streetType;
    }


    /**
     * Sets the streetType value for this FindMapArcAddressCriteria.
     * 
     * @param streetType
     */
    public void setStreetType(java.lang.String streetType) {
        this.streetType = streetType;
    }


    /**
     * Gets the preDirection value for this FindMapArcAddressCriteria.
     * 
     * @return preDirection
     */
    public java.lang.String getPreDirection() {
        return preDirection;
    }


    /**
     * Sets the preDirection value for this FindMapArcAddressCriteria.
     * 
     * @param preDirection
     */
    public void setPreDirection(java.lang.String preDirection) {
        this.preDirection = preDirection;
    }


    /**
     * Gets the postDirection value for this FindMapArcAddressCriteria.
     * 
     * @return postDirection
     */
    public java.lang.String getPostDirection() {
        return postDirection;
    }


    /**
     * Sets the postDirection value for this FindMapArcAddressCriteria.
     * 
     * @param postDirection
     */
    public void setPostDirection(java.lang.String postDirection) {
        this.postDirection = postDirection;
    }


    /**
     * Gets the region1 value for this FindMapArcAddressCriteria.
     * 
     * @return region1
     */
    public java.lang.String getRegion1() {
        return region1;
    }


    /**
     * Sets the region1 value for this FindMapArcAddressCriteria.
     * 
     * @param region1
     */
    public void setRegion1(java.lang.String region1) {
        this.region1 = region1;
    }


    /**
     * Gets the region2 value for this FindMapArcAddressCriteria.
     * 
     * @return region2
     */
    public java.lang.String getRegion2() {
        return region2;
    }


    /**
     * Sets the region2 value for this FindMapArcAddressCriteria.
     * 
     * @param region2
     */
    public void setRegion2(java.lang.String region2) {
        this.region2 = region2;
    }


    /**
     * Gets the region3 value for this FindMapArcAddressCriteria.
     * 
     * @return region3
     */
    public java.lang.String getRegion3() {
        return region3;
    }


    /**
     * Sets the region3 value for this FindMapArcAddressCriteria.
     * 
     * @param region3
     */
    public void setRegion3(java.lang.String region3) {
        this.region3 = region3;
    }


    /**
     * Gets the postalCode value for this FindMapArcAddressCriteria.
     * 
     * @return postalCode
     */
    public java.lang.String getPostalCode() {
        return postalCode;
    }


    /**
     * Sets the postalCode value for this FindMapArcAddressCriteria.
     * 
     * @param postalCode
     */
    public void setPostalCode(java.lang.String postalCode) {
        this.postalCode = postalCode;
    }


    /**
     * Gets the country value for this FindMapArcAddressCriteria.
     * 
     * @return country
     */
    public java.lang.String getCountry() {
        return country;
    }


    /**
     * Sets the country value for this FindMapArcAddressCriteria.
     * 
     * @param country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FindMapArcAddressCriteria)) return false;
        FindMapArcAddressCriteria other = (FindMapArcAddressCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.houseNumber==null && other.getHouseNumber()==null) || 
             (this.houseNumber!=null &&
              this.houseNumber.equals(other.getHouseNumber()))) &&
            ((this.streetName==null && other.getStreetName()==null) || 
             (this.streetName!=null &&
              this.streetName.equals(other.getStreetName()))) &&
            ((this.streetType==null && other.getStreetType()==null) || 
             (this.streetType!=null &&
              this.streetType.equals(other.getStreetType()))) &&
            ((this.preDirection==null && other.getPreDirection()==null) || 
             (this.preDirection!=null &&
              this.preDirection.equals(other.getPreDirection()))) &&
            ((this.postDirection==null && other.getPostDirection()==null) || 
             (this.postDirection!=null &&
              this.postDirection.equals(other.getPostDirection()))) &&
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
        int _hashCode = super.hashCode();
        if (getHouseNumber() != null) {
            _hashCode += getHouseNumber().hashCode();
        }
        if (getStreetName() != null) {
            _hashCode += getStreetName().hashCode();
        }
        if (getStreetType() != null) {
            _hashCode += getStreetType().hashCode();
        }
        if (getPreDirection() != null) {
            _hashCode += getPreDirection().hashCode();
        }
        if (getPostDirection() != null) {
            _hashCode += getPostDirection().hashCode();
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
        new org.apache.axis.description.TypeDesc(FindMapArcAddressCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "FindMapArcAddressCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("houseNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "houseNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("streetName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "streetName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("streetType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "streetType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preDirection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "preDirection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postDirection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "postDirection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "region1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "region2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "region3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postalCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "postalCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "country"));
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
