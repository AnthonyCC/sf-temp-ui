/**
 * QuestionDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class QuestionDefinition  implements java.io.Serializable {
    private int sequenceNumber;

    private java.lang.String regionId;

    private java.lang.String questionDescriptor;

    private java.lang.String questionText;

    private com.freshdirect.routing.proxy.stub.transportation.SurveyAnswerType answerType;

    private java.lang.String answerMask;

    private boolean required;

    public QuestionDefinition() {
    }

    public QuestionDefinition(
           int sequenceNumber,
           java.lang.String regionId,
           java.lang.String questionDescriptor,
           java.lang.String questionText,
           com.freshdirect.routing.proxy.stub.transportation.SurveyAnswerType answerType,
           java.lang.String answerMask,
           boolean required) {
           this.sequenceNumber = sequenceNumber;
           this.regionId = regionId;
           this.questionDescriptor = questionDescriptor;
           this.questionText = questionText;
           this.answerType = answerType;
           this.answerMask = answerMask;
           this.required = required;
    }


    /**
     * Gets the sequenceNumber value for this QuestionDefinition.
     * 
     * @return sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }


    /**
     * Sets the sequenceNumber value for this QuestionDefinition.
     * 
     * @param sequenceNumber
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }


    /**
     * Gets the regionId value for this QuestionDefinition.
     * 
     * @return regionId
     */
    public java.lang.String getRegionId() {
        return regionId;
    }


    /**
     * Sets the regionId value for this QuestionDefinition.
     * 
     * @param regionId
     */
    public void setRegionId(java.lang.String regionId) {
        this.regionId = regionId;
    }


    /**
     * Gets the questionDescriptor value for this QuestionDefinition.
     * 
     * @return questionDescriptor
     */
    public java.lang.String getQuestionDescriptor() {
        return questionDescriptor;
    }


    /**
     * Sets the questionDescriptor value for this QuestionDefinition.
     * 
     * @param questionDescriptor
     */
    public void setQuestionDescriptor(java.lang.String questionDescriptor) {
        this.questionDescriptor = questionDescriptor;
    }


    /**
     * Gets the questionText value for this QuestionDefinition.
     * 
     * @return questionText
     */
    public java.lang.String getQuestionText() {
        return questionText;
    }


    /**
     * Sets the questionText value for this QuestionDefinition.
     * 
     * @param questionText
     */
    public void setQuestionText(java.lang.String questionText) {
        this.questionText = questionText;
    }


    /**
     * Gets the answerType value for this QuestionDefinition.
     * 
     * @return answerType
     */
    public com.freshdirect.routing.proxy.stub.transportation.SurveyAnswerType getAnswerType() {
        return answerType;
    }


    /**
     * Sets the answerType value for this QuestionDefinition.
     * 
     * @param answerType
     */
    public void setAnswerType(com.freshdirect.routing.proxy.stub.transportation.SurveyAnswerType answerType) {
        this.answerType = answerType;
    }


    /**
     * Gets the answerMask value for this QuestionDefinition.
     * 
     * @return answerMask
     */
    public java.lang.String getAnswerMask() {
        return answerMask;
    }


    /**
     * Sets the answerMask value for this QuestionDefinition.
     * 
     * @param answerMask
     */
    public void setAnswerMask(java.lang.String answerMask) {
        this.answerMask = answerMask;
    }


    /**
     * Gets the required value for this QuestionDefinition.
     * 
     * @return required
     */
    public boolean isRequired() {
        return required;
    }


    /**
     * Sets the required value for this QuestionDefinition.
     * 
     * @param required
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QuestionDefinition)) return false;
        QuestionDefinition other = (QuestionDefinition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.sequenceNumber == other.getSequenceNumber() &&
            ((this.regionId==null && other.getRegionId()==null) || 
             (this.regionId!=null &&
              this.regionId.equals(other.getRegionId()))) &&
            ((this.questionDescriptor==null && other.getQuestionDescriptor()==null) || 
             (this.questionDescriptor!=null &&
              this.questionDescriptor.equals(other.getQuestionDescriptor()))) &&
            ((this.questionText==null && other.getQuestionText()==null) || 
             (this.questionText!=null &&
              this.questionText.equals(other.getQuestionText()))) &&
            ((this.answerType==null && other.getAnswerType()==null) || 
             (this.answerType!=null &&
              this.answerType.equals(other.getAnswerType()))) &&
            ((this.answerMask==null && other.getAnswerMask()==null) || 
             (this.answerMask!=null &&
              this.answerMask.equals(other.getAnswerMask()))) &&
            this.required == other.isRequired();
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
        _hashCode += getSequenceNumber();
        if (getRegionId() != null) {
            _hashCode += getRegionId().hashCode();
        }
        if (getQuestionDescriptor() != null) {
            _hashCode += getQuestionDescriptor().hashCode();
        }
        if (getQuestionText() != null) {
            _hashCode += getQuestionText().hashCode();
        }
        if (getAnswerType() != null) {
            _hashCode += getAnswerType().hashCode();
        }
        if (getAnswerMask() != null) {
            _hashCode += getAnswerMask().hashCode();
        }
        _hashCode += (isRequired() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QuestionDefinition.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "QuestionDefinition"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sequenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("questionDescriptor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "questionDescriptor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("questionText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "questionText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("answerType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "answerType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SurveyAnswerType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("answerMask");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "answerMask"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("required");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "required"));
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
