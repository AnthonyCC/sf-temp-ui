/**
 * Report.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.report;

public class Report  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.report.ReportIdentity reportIdentity;

    private java.lang.String reportFileName;

    private java.lang.String description;

    private com.freshdirect.routing.proxy.stub.report.ReportCriteriaType criteriaType;

    private com.freshdirect.routing.proxy.stub.report.ReportParameter[] reportParameters;

    public Report() {
    }

    public Report(
           com.freshdirect.routing.proxy.stub.report.ReportIdentity reportIdentity,
           java.lang.String reportFileName,
           java.lang.String description,
           com.freshdirect.routing.proxy.stub.report.ReportCriteriaType criteriaType,
           com.freshdirect.routing.proxy.stub.report.ReportParameter[] reportParameters) {
           this.reportIdentity = reportIdentity;
           this.reportFileName = reportFileName;
           this.description = description;
           this.criteriaType = criteriaType;
           this.reportParameters = reportParameters;
    }


    /**
     * Gets the reportIdentity value for this Report.
     * 
     * @return reportIdentity
     */
    public com.freshdirect.routing.proxy.stub.report.ReportIdentity getReportIdentity() {
        return reportIdentity;
    }


    /**
     * Sets the reportIdentity value for this Report.
     * 
     * @param reportIdentity
     */
    public void setReportIdentity(com.freshdirect.routing.proxy.stub.report.ReportIdentity reportIdentity) {
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
    public com.freshdirect.routing.proxy.stub.report.ReportCriteriaType getCriteriaType() {
        return criteriaType;
    }


    /**
     * Sets the criteriaType value for this Report.
     * 
     * @param criteriaType
     */
    public void setCriteriaType(com.freshdirect.routing.proxy.stub.report.ReportCriteriaType criteriaType) {
        this.criteriaType = criteriaType;
    }


    /**
     * Gets the reportParameters value for this Report.
     * 
     * @return reportParameters
     */
    public com.freshdirect.routing.proxy.stub.report.ReportParameter[] getReportParameters() {
        return reportParameters;
    }


    /**
     * Sets the reportParameters value for this Report.
     * 
     * @param reportParameters
     */
    public void setReportParameters(com.freshdirect.routing.proxy.stub.report.ReportParameter[] reportParameters) {
        this.reportParameters = reportParameters;
    }

    public com.freshdirect.routing.proxy.stub.report.ReportParameter getReportParameters(int i) {
        return this.reportParameters[i];
    }

    public void setReportParameters(int i, com.freshdirect.routing.proxy.stub.report.ReportParameter _value) {
        this.reportParameters[i] = _value;
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
            ((this.reportParameters==null && other.getReportParameters()==null) || 
             (this.reportParameters!=null &&
              java.util.Arrays.equals(this.reportParameters, other.getReportParameters())));
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
        if (getReportParameters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReportParameters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReportParameters(), i);
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
        new org.apache.axis.description.TypeDesc(Report.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "Report"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "reportIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportFileName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "reportFileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("criteriaType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "criteriaType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportCriteriaType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "reportParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportParameter"));
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
