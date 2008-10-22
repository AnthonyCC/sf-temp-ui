/**
 * ReportExportOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.report;

public class ReportExportOptions  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.report.ReportExportType exportType;

    private java.lang.String localeID;

    public ReportExportOptions() {
    }

    public ReportExportOptions(
           com.freshdirect.routing.proxy.stub.report.ReportExportType exportType,
           java.lang.String localeID) {
           this.exportType = exportType;
           this.localeID = localeID;
    }


    /**
     * Gets the exportType value for this ReportExportOptions.
     * 
     * @return exportType
     */
    public com.freshdirect.routing.proxy.stub.report.ReportExportType getExportType() {
        return exportType;
    }


    /**
     * Sets the exportType value for this ReportExportOptions.
     * 
     * @param exportType
     */
    public void setExportType(com.freshdirect.routing.proxy.stub.report.ReportExportType exportType) {
        this.exportType = exportType;
    }


    /**
     * Gets the localeID value for this ReportExportOptions.
     * 
     * @return localeID
     */
    public java.lang.String getLocaleID() {
        return localeID;
    }


    /**
     * Sets the localeID value for this ReportExportOptions.
     * 
     * @param localeID
     */
    public void setLocaleID(java.lang.String localeID) {
        this.localeID = localeID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReportExportOptions)) return false;
        ReportExportOptions other = (ReportExportOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.exportType==null && other.getExportType()==null) || 
             (this.exportType!=null &&
              this.exportType.equals(other.getExportType()))) &&
            ((this.localeID==null && other.getLocaleID()==null) || 
             (this.localeID!=null &&
              this.localeID.equals(other.getLocaleID())));
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
        if (getExportType() != null) {
            _hashCode += getExportType().hashCode();
        }
        if (getLocaleID() != null) {
            _hashCode += getLocaleID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReportExportOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportExportOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exportType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "exportType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportExportType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "localeID"));
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
