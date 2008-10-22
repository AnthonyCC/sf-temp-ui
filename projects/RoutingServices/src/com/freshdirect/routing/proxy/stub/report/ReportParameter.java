/**
 * ReportParameter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.report;

public class ReportParameter  implements java.io.Serializable {
    private java.lang.String name;

    private java.lang.String prompt;

    private com.freshdirect.routing.proxy.stub.report.ReportParameterDataType dataType;

    private com.freshdirect.routing.proxy.stub.report.ReportParameterKind discreteOrRangeKind;

    private boolean allowMultipleValues;

    private java.lang.String[] defaultValue;

    private java.lang.String[] value;

    public ReportParameter() {
    }

    public ReportParameter(
           java.lang.String name,
           java.lang.String prompt,
           com.freshdirect.routing.proxy.stub.report.ReportParameterDataType dataType,
           com.freshdirect.routing.proxy.stub.report.ReportParameterKind discreteOrRangeKind,
           boolean allowMultipleValues,
           java.lang.String[] defaultValue,
           java.lang.String[] value) {
           this.name = name;
           this.prompt = prompt;
           this.dataType = dataType;
           this.discreteOrRangeKind = discreteOrRangeKind;
           this.allowMultipleValues = allowMultipleValues;
           this.defaultValue = defaultValue;
           this.value = value;
    }


    /**
     * Gets the name value for this ReportParameter.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ReportParameter.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the prompt value for this ReportParameter.
     * 
     * @return prompt
     */
    public java.lang.String getPrompt() {
        return prompt;
    }


    /**
     * Sets the prompt value for this ReportParameter.
     * 
     * @param prompt
     */
    public void setPrompt(java.lang.String prompt) {
        this.prompt = prompt;
    }


    /**
     * Gets the dataType value for this ReportParameter.
     * 
     * @return dataType
     */
    public com.freshdirect.routing.proxy.stub.report.ReportParameterDataType getDataType() {
        return dataType;
    }


    /**
     * Sets the dataType value for this ReportParameter.
     * 
     * @param dataType
     */
    public void setDataType(com.freshdirect.routing.proxy.stub.report.ReportParameterDataType dataType) {
        this.dataType = dataType;
    }


    /**
     * Gets the discreteOrRangeKind value for this ReportParameter.
     * 
     * @return discreteOrRangeKind
     */
    public com.freshdirect.routing.proxy.stub.report.ReportParameterKind getDiscreteOrRangeKind() {
        return discreteOrRangeKind;
    }


    /**
     * Sets the discreteOrRangeKind value for this ReportParameter.
     * 
     * @param discreteOrRangeKind
     */
    public void setDiscreteOrRangeKind(com.freshdirect.routing.proxy.stub.report.ReportParameterKind discreteOrRangeKind) {
        this.discreteOrRangeKind = discreteOrRangeKind;
    }


    /**
     * Gets the allowMultipleValues value for this ReportParameter.
     * 
     * @return allowMultipleValues
     */
    public boolean isAllowMultipleValues() {
        return allowMultipleValues;
    }


    /**
     * Sets the allowMultipleValues value for this ReportParameter.
     * 
     * @param allowMultipleValues
     */
    public void setAllowMultipleValues(boolean allowMultipleValues) {
        this.allowMultipleValues = allowMultipleValues;
    }


    /**
     * Gets the defaultValue value for this ReportParameter.
     * 
     * @return defaultValue
     */
    public java.lang.String[] getDefaultValue() {
        return defaultValue;
    }


    /**
     * Sets the defaultValue value for this ReportParameter.
     * 
     * @param defaultValue
     */
    public void setDefaultValue(java.lang.String[] defaultValue) {
        this.defaultValue = defaultValue;
    }


    /**
     * Gets the value value for this ReportParameter.
     * 
     * @return value
     */
    public java.lang.String[] getValue() {
        return value;
    }


    /**
     * Sets the value value for this ReportParameter.
     * 
     * @param value
     */
    public void setValue(java.lang.String[] value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReportParameter)) return false;
        ReportParameter other = (ReportParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.prompt==null && other.getPrompt()==null) || 
             (this.prompt!=null &&
              this.prompt.equals(other.getPrompt()))) &&
            ((this.dataType==null && other.getDataType()==null) || 
             (this.dataType!=null &&
              this.dataType.equals(other.getDataType()))) &&
            ((this.discreteOrRangeKind==null && other.getDiscreteOrRangeKind()==null) || 
             (this.discreteOrRangeKind!=null &&
              this.discreteOrRangeKind.equals(other.getDiscreteOrRangeKind()))) &&
            this.allowMultipleValues == other.isAllowMultipleValues() &&
            ((this.defaultValue==null && other.getDefaultValue()==null) || 
             (this.defaultValue!=null &&
              java.util.Arrays.equals(this.defaultValue, other.getDefaultValue()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              java.util.Arrays.equals(this.value, other.getValue())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getPrompt() != null) {
            _hashCode += getPrompt().hashCode();
        }
        if (getDataType() != null) {
            _hashCode += getDataType().hashCode();
        }
        if (getDiscreteOrRangeKind() != null) {
            _hashCode += getDiscreteOrRangeKind().hashCode();
        }
        _hashCode += (isAllowMultipleValues() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDefaultValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDefaultValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDefaultValue(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getValue(), i);
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
        new org.apache.axis.description.TypeDesc(ReportParameter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prompt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "prompt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "dataType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportParameterDataType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("discreteOrRangeKind");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "discreteOrRangeKind"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportParameterKind"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allowMultipleValues");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "allowMultipleValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "defaultValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "values"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "values"));
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
