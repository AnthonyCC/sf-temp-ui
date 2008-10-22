/**
 * Report.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Report  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.ReportIdentity reportIdentity;

    private java.lang.String reportFileName;

    private java.lang.String description;

    private com.freshdirect.routing.proxy.stub.transportation.ReportCriteriaType criteriaType;

    private int optionDlgId;

    private boolean published;

    private int publishedId;

    private java.lang.String publishedUrl;

    public Report() {
    }

    public Report(
           com.freshdirect.routing.proxy.stub.transportation.ReportIdentity reportIdentity,
           java.lang.String reportFileName,
           java.lang.String description,
           com.freshdirect.routing.proxy.stub.transportation.ReportCriteriaType criteriaType,
           int optionDlgId,
           boolean published,
           int publishedId,
           java.lang.String publishedUrl) {
           this.reportIdentity = reportIdentity;
           this.reportFileName = reportFileName;
           this.description = description;
           this.criteriaType = criteriaType;
           this.optionDlgId = optionDlgId;
           this.published = published;
           this.publishedId = publishedId;
           this.publishedUrl = publishedUrl;
    }


    /**
     * Gets the reportIdentity value for this Report.
     * 
     * @return reportIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.ReportIdentity getReportIdentity() {
        return reportIdentity;
    }


    /**
     * Sets the reportIdentity value for this Report.
     * 
     * @param reportIdentity
     */
    public void setReportIdentity(com.freshdirect.routing.proxy.stub.transportation.ReportIdentity reportIdentity) {
        this.reportIdentity = reportIdentity;
    }


    /**
     * Gets the reportFileName value for this Report.
     * 
     * @return reportFileName
     */
    public java.lang.String getReportFileName() {
        return reportFileName;
    }


    /**
     * Sets the reportFileName value for this Report.
     * 
     * @param reportFileName
     */
    public void setReportFileName(java.lang.String reportFileName) {
        this.reportFileName = reportFileName;
    }


    /**
     * Gets the description value for this Report.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Report.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the criteriaType value for this Report.
     * 
     * @return criteriaType
     */
    public com.freshdirect.routing.proxy.stub.transportation.ReportCriteriaType getCriteriaType() {
        return criteriaType;
    }


    /**
     * Sets the criteriaType value for this Report.
     * 
     * @param criteriaType
     */
    public void setCriteriaType(com.freshdirect.routing.proxy.stub.transportation.ReportCriteriaType criteriaType) {
        this.criteriaType = criteriaType;
    }


    /**
     * Gets the optionDlgId value for this Report.
     * 
     * @return optionDlgId
     */
    public int getOptionDlgId() {
        return optionDlgId;
    }


    /**
     * Sets the optionDlgId value for this Report.
     * 
     * @param optionDlgId
     */
    public void setOptionDlgId(int optionDlgId) {
        this.optionDlgId = optionDlgId;
    }


    /**
     * Gets the published value for this Report.
     * 
     * @return published
     */
    public boolean isPublished() {
        return published;
    }


    /**
     * Sets the published value for this Report.
     * 
     * @param published
     */
    public void setPublished(boolean published) {
        this.published = published;
    }


    /**
     * Gets the publishedId value for this Report.
     * 
     * @return publishedId
     */
    public int getPublishedId() {
        return publishedId;
    }


    /**
     * Sets the publishedId value for this Report.
     * 
     * @param publishedId
     */
    public void setPublishedId(int publishedId) {
        this.publishedId = publishedId;
    }


    /**
     * Gets the publishedUrl value for this Report.
     * 
     * @return publishedUrl
     */
    public java.lang.String getPublishedUrl() {
        return publishedUrl;
    }


    /**
     * Sets the publishedUrl value for this Report.
     * 
     * @param publishedUrl
     */
    public void setPublishedUrl(java.lang.String publishedUrl) {
        this.publishedUrl = publishedUrl;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Report)) return false;
        Report other = (Report) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.reportIdentity==null && other.getReportIdentity()==null) || 
             (this.reportIdentity!=null &&
              this.reportIdentity.equals(other.getReportIdentity()))) &&
            ((this.reportFileName==null && other.getReportFileName()==null) || 
             (this.reportFileName!=null &&
              this.reportFileName.equals(other.getReportFileName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.criteriaType==null && other.getCriteriaType()==null) || 
             (this.criteriaType!=null &&
              this.criteriaType.equals(other.getCriteriaType()))) &&
            this.optionDlgId == other.getOptionDlgId() &&
            this.published == other.isPublished() &&
            this.publishedId == other.getPublishedId() &&
            ((this.publishedUrl==null && other.getPublishedUrl()==null) || 
             (this.publishedUrl!=null &&
              this.publishedUrl.equals(other.getPublishedUrl())));
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
        if (getReportIdentity() != null) {
            _hashCode += getReportIdentity().hashCode();
        }
        if (getReportFileName() != null) {
            _hashCode += getReportFileName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getCriteriaType() != null) {
            _hashCode += getCriteriaType().hashCode();
        }
        _hashCode += getOptionDlgId();
        _hashCode += (isPublished() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getPublishedId();
        if (getPublishedUrl() != null) {
            _hashCode += getPublishedUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Report.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Report"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "reportIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ReportIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportFileName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "reportFileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("criteriaType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "criteriaType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ReportCriteriaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("optionDlgId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "optionDlgId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("published");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "published"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publishedId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "publishedId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publishedUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "publishedUrl"));
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
