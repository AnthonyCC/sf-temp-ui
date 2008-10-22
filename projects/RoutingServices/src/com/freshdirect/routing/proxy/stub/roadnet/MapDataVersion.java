/**
 * MapDataVersion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapDataVersion  implements java.io.Serializable {
    private int networkVersion;

    private java.lang.String languageCode;

    private java.lang.String codePage;

    private java.lang.String vendorName;

    private java.lang.String vendorVersion;

    private java.lang.String countryCodes;

    private java.util.Date creationDate;

    private java.lang.String vendorCopyrightShort;

    private java.lang.String vendorCopyrightLong;

    public MapDataVersion() {
    }

    public MapDataVersion(
           int networkVersion,
           java.lang.String languageCode,
           java.lang.String codePage,
           java.lang.String vendorName,
           java.lang.String vendorVersion,
           java.lang.String countryCodes,
           java.util.Date creationDate,
           java.lang.String vendorCopyrightShort,
           java.lang.String vendorCopyrightLong) {
           this.networkVersion = networkVersion;
           this.languageCode = languageCode;
           this.codePage = codePage;
           this.vendorName = vendorName;
           this.vendorVersion = vendorVersion;
           this.countryCodes = countryCodes;
           this.creationDate = creationDate;
           this.vendorCopyrightShort = vendorCopyrightShort;
           this.vendorCopyrightLong = vendorCopyrightLong;
    }


    /**
     * Gets the networkVersion value for this MapDataVersion.
     * 
     * @return networkVersion
     */
    public int getNetworkVersion() {
        return networkVersion;
    }


    /**
     * Sets the networkVersion value for this MapDataVersion.
     * 
     * @param networkVersion
     */
    public void setNetworkVersion(int networkVersion) {
        this.networkVersion = networkVersion;
    }


    /**
     * Gets the languageCode value for this MapDataVersion.
     * 
     * @return languageCode
     */
    public java.lang.String getLanguageCode() {
        return languageCode;
    }


    /**
     * Sets the languageCode value for this MapDataVersion.
     * 
     * @param languageCode
     */
    public void setLanguageCode(java.lang.String languageCode) {
        this.languageCode = languageCode;
    }


    /**
     * Gets the codePage value for this MapDataVersion.
     * 
     * @return codePage
     */
    public java.lang.String getCodePage() {
        return codePage;
    }


    /**
     * Sets the codePage value for this MapDataVersion.
     * 
     * @param codePage
     */
    public void setCodePage(java.lang.String codePage) {
        this.codePage = codePage;
    }


    /**
     * Gets the vendorName value for this MapDataVersion.
     * 
     * @return vendorName
     */
    public java.lang.String getVendorName() {
        return vendorName;
    }


    /**
     * Sets the vendorName value for this MapDataVersion.
     * 
     * @param vendorName
     */
    public void setVendorName(java.lang.String vendorName) {
        this.vendorName = vendorName;
    }


    /**
     * Gets the vendorVersion value for this MapDataVersion.
     * 
     * @return vendorVersion
     */
    public java.lang.String getVendorVersion() {
        return vendorVersion;
    }


    /**
     * Sets the vendorVersion value for this MapDataVersion.
     * 
     * @param vendorVersion
     */
    public void setVendorVersion(java.lang.String vendorVersion) {
        this.vendorVersion = vendorVersion;
    }


    /**
     * Gets the countryCodes value for this MapDataVersion.
     * 
     * @return countryCodes
     */
    public java.lang.String getCountryCodes() {
        return countryCodes;
    }


    /**
     * Sets the countryCodes value for this MapDataVersion.
     * 
     * @param countryCodes
     */
    public void setCountryCodes(java.lang.String countryCodes) {
        this.countryCodes = countryCodes;
    }


    /**
     * Gets the creationDate value for this MapDataVersion.
     * 
     * @return creationDate
     */
    public java.util.Date getCreationDate() {
        return creationDate;
    }


    /**
     * Sets the creationDate value for this MapDataVersion.
     * 
     * @param creationDate
     */
    public void setCreationDate(java.util.Date creationDate) {
        this.creationDate = creationDate;
    }


    /**
     * Gets the vendorCopyrightShort value for this MapDataVersion.
     * 
     * @return vendorCopyrightShort
     */
    public java.lang.String getVendorCopyrightShort() {
        return vendorCopyrightShort;
    }


    /**
     * Sets the vendorCopyrightShort value for this MapDataVersion.
     * 
     * @param vendorCopyrightShort
     */
    public void setVendorCopyrightShort(java.lang.String vendorCopyrightShort) {
        this.vendorCopyrightShort = vendorCopyrightShort;
    }


    /**
     * Gets the vendorCopyrightLong value for this MapDataVersion.
     * 
     * @return vendorCopyrightLong
     */
    public java.lang.String getVendorCopyrightLong() {
        return vendorCopyrightLong;
    }


    /**
     * Sets the vendorCopyrightLong value for this MapDataVersion.
     * 
     * @param vendorCopyrightLong
     */
    public void setVendorCopyrightLong(java.lang.String vendorCopyrightLong) {
        this.vendorCopyrightLong = vendorCopyrightLong;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapDataVersion)) return false;
        MapDataVersion other = (MapDataVersion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.networkVersion == other.getNetworkVersion() &&
            ((this.languageCode==null && other.getLanguageCode()==null) || 
             (this.languageCode!=null &&
              this.languageCode.equals(other.getLanguageCode()))) &&
            ((this.codePage==null && other.getCodePage()==null) || 
             (this.codePage!=null &&
              this.codePage.equals(other.getCodePage()))) &&
            ((this.vendorName==null && other.getVendorName()==null) || 
             (this.vendorName!=null &&
              this.vendorName.equals(other.getVendorName()))) &&
            ((this.vendorVersion==null && other.getVendorVersion()==null) || 
             (this.vendorVersion!=null &&
              this.vendorVersion.equals(other.getVendorVersion()))) &&
            ((this.countryCodes==null && other.getCountryCodes()==null) || 
             (this.countryCodes!=null &&
              this.countryCodes.equals(other.getCountryCodes()))) &&
            ((this.creationDate==null && other.getCreationDate()==null) || 
             (this.creationDate!=null &&
              this.creationDate.equals(other.getCreationDate()))) &&
            ((this.vendorCopyrightShort==null && other.getVendorCopyrightShort()==null) || 
             (this.vendorCopyrightShort!=null &&
              this.vendorCopyrightShort.equals(other.getVendorCopyrightShort()))) &&
            ((this.vendorCopyrightLong==null && other.getVendorCopyrightLong()==null) || 
             (this.vendorCopyrightLong!=null &&
              this.vendorCopyrightLong.equals(other.getVendorCopyrightLong())));
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
        _hashCode += getNetworkVersion();
        if (getLanguageCode() != null) {
            _hashCode += getLanguageCode().hashCode();
        }
        if (getCodePage() != null) {
            _hashCode += getCodePage().hashCode();
        }
        if (getVendorName() != null) {
            _hashCode += getVendorName().hashCode();
        }
        if (getVendorVersion() != null) {
            _hashCode += getVendorVersion().hashCode();
        }
        if (getCountryCodes() != null) {
            _hashCode += getCountryCodes().hashCode();
        }
        if (getCreationDate() != null) {
            _hashCode += getCreationDate().hashCode();
        }
        if (getVendorCopyrightShort() != null) {
            _hashCode += getVendorCopyrightShort().hashCode();
        }
        if (getVendorCopyrightLong() != null) {
            _hashCode += getVendorCopyrightLong().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapDataVersion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapDataVersion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("networkVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "networkVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("languageCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "languageCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codePage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "codePage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "vendorName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "vendorVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("countryCodes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "countryCodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "creationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorCopyrightShort");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "vendorCopyrightShort"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorCopyrightLong");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "vendorCopyrightLong"));
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
