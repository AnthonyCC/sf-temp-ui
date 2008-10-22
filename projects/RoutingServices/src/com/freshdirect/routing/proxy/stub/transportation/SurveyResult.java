/**
 * SurveyResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SurveyResult  implements java.io.Serializable {
    private java.lang.String surveyCode;

    private java.lang.String questionDescriptor;

    private java.lang.String questionText;

    private java.lang.String answer;

    private boolean answered;

    public SurveyResult() {
    }

    public SurveyResult(
           java.lang.String surveyCode,
           java.lang.String questionDescriptor,
           java.lang.String questionText,
           java.lang.String answer,
           boolean answered) {
           this.surveyCode = surveyCode;
           this.questionDescriptor = questionDescriptor;
           this.questionText = questionText;
           this.answer = answer;
           this.answered = answered;
    }


    /**
     * Gets the surveyCode value for this SurveyResult.
     * 
     * @return surveyCode
     */
    public java.lang.String getSurveyCode() {
        return surveyCode;
    }


    /**
     * Sets the surveyCode value for this SurveyResult.
     * 
     * @param surveyCode
     */
    public void setSurveyCode(java.lang.String surveyCode) {
        this.surveyCode = surveyCode;
    }


    /**
     * Gets the questionDescriptor value for this SurveyResult.
     * 
     * @return questionDescriptor
     */
    public java.lang.String getQuestionDescriptor() {
        return questionDescriptor;
    }


    /**
     * Sets the questionDescriptor value for this SurveyResult.
     * 
     * @param questionDescriptor
     */
    public void setQuestionDescriptor(java.lang.String questionDescriptor) {
        this.questionDescriptor = questionDescriptor;
    }


    /**
     * Gets the questionText value for this SurveyResult.
     * 
     * @return questionText
     */
    public java.lang.String getQuestionText() {
        return questionText;
    }


    /**
     * Sets the questionText value for this SurveyResult.
     * 
     * @param questionText
     */
    public void setQuestionText(java.lang.String questionText) {
        this.questionText = questionText;
    }


    /**
     * Gets the answer value for this SurveyResult.
     * 
     * @return answer
     */
    public java.lang.String getAnswer() {
        return answer;
    }


    /**
     * Sets the answer value for this SurveyResult.
     * 
     * @param answer
     */
    public void setAnswer(java.lang.String answer) {
        this.answer = answer;
    }


    /**
     * Gets the answered value for this SurveyResult.
     * 
     * @return answered
     */
    public boolean isAnswered() {
        return answered;
    }


    /**
     * Sets the answered value for this SurveyResult.
     * 
     * @param answered
     */
    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SurveyResult)) return false;
        SurveyResult other = (SurveyResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.surveyCode==null && other.getSurveyCode()==null) || 
             (this.surveyCode!=null &&
              this.surveyCode.equals(other.getSurveyCode()))) &&
            ((this.questionDescriptor==null && other.getQuestionDescriptor()==null) || 
             (this.questionDescriptor!=null &&
              this.questionDescriptor.equals(other.getQuestionDescriptor()))) &&
            ((this.questionText==null && other.getQuestionText()==null) || 
             (this.questionText!=null &&
              this.questionText.equals(other.getQuestionText()))) &&
            ((this.answer==null && other.getAnswer()==null) || 
             (this.answer!=null &&
              this.answer.equals(other.getAnswer()))) &&
            this.answered == other.isAnswered();
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
        if (getSurveyCode() != null) {
            _hashCode += getSurveyCode().hashCode();
        }
        if (getQuestionDescriptor() != null) {
            _hashCode += getQuestionDescriptor().hashCode();
        }
        if (getQuestionText() != null) {
            _hashCode += getQuestionText().hashCode();
        }
        if (getAnswer() != null) {
            _hashCode += getAnswer().hashCode();
        }
        _hashCode += (isAnswered() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SurveyResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SurveyResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("surveyCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "surveyCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("questionDescriptor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "questionDescriptor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("questionText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "questionText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("answer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "answer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("answered");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "answered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
