/**
 * RegionOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RegionOptions  implements java.io.Serializable {
    private java.lang.String size1Alias;

    private java.lang.String size2Alias;

    private java.lang.String size3Alias;

    private int size1Precision;

    private int size2Precision;

    private int size3Precision;

    private com.freshdirect.routing.proxy.stub.transportation.UnitDistance unitOfDistance;

    private com.freshdirect.routing.proxy.stub.transportation.DetailLevel detailLevel;

    private com.freshdirect.routing.proxy.stub.transportation.QuantityReference costQuantityReference;

    private int stopCriticalMinutes;

    public RegionOptions() {
    }

    public RegionOptions(
           java.lang.String size1Alias,
           java.lang.String size2Alias,
           java.lang.String size3Alias,
           int size1Precision,
           int size2Precision,
           int size3Precision,
           com.freshdirect.routing.proxy.stub.transportation.UnitDistance unitOfDistance,
           com.freshdirect.routing.proxy.stub.transportation.DetailLevel detailLevel,
           com.freshdirect.routing.proxy.stub.transportation.QuantityReference costQuantityReference,
           int stopCriticalMinutes) {
           this.size1Alias = size1Alias;
           this.size2Alias = size2Alias;
           this.size3Alias = size3Alias;
           this.size1Precision = size1Precision;
           this.size2Precision = size2Precision;
           this.size3Precision = size3Precision;
           this.unitOfDistance = unitOfDistance;
           this.detailLevel = detailLevel;
           this.costQuantityReference = costQuantityReference;
           this.stopCriticalMinutes = stopCriticalMinutes;
    }


    /**
     * Gets the size1Alias value for this RegionOptions.
     * 
     * @return size1Alias
     */
    public java.lang.String getSize1Alias() {
        return size1Alias;
    }


    /**
     * Sets the size1Alias value for this RegionOptions.
     * 
     * @param size1Alias
     */
    public void setSize1Alias(java.lang.String size1Alias) {
        this.size1Alias = size1Alias;
    }


    /**
     * Gets the size2Alias value for this RegionOptions.
     * 
     * @return size2Alias
     */
    public java.lang.String getSize2Alias() {
        return size2Alias;
    }


    /**
     * Sets the size2Alias value for this RegionOptions.
     * 
     * @param size2Alias
     */
    public void setSize2Alias(java.lang.String size2Alias) {
        this.size2Alias = size2Alias;
    }


    /**
     * Gets the size3Alias value for this RegionOptions.
     * 
     * @return size3Alias
     */
    public java.lang.String getSize3Alias() {
        return size3Alias;
    }


    /**
     * Sets the size3Alias value for this RegionOptions.
     * 
     * @param size3Alias
     */
    public void setSize3Alias(java.lang.String size3Alias) {
        this.size3Alias = size3Alias;
    }


    /**
     * Gets the size1Precision value for this RegionOptions.
     * 
     * @return size1Precision
     */
    public int getSize1Precision() {
        return size1Precision;
    }


    /**
     * Sets the size1Precision value for this RegionOptions.
     * 
     * @param size1Precision
     */
    public void setSize1Precision(int size1Precision) {
        this.size1Precision = size1Precision;
    }


    /**
     * Gets the size2Precision value for this RegionOptions.
     * 
     * @return size2Precision
     */
    public int getSize2Precision() {
        return size2Precision;
    }


    /**
     * Sets the size2Precision value for this RegionOptions.
     * 
     * @param size2Precision
     */
    public void setSize2Precision(int size2Precision) {
        this.size2Precision = size2Precision;
    }


    /**
     * Gets the size3Precision value for this RegionOptions.
     * 
     * @return size3Precision
     */
    public int getSize3Precision() {
        return size3Precision;
    }


    /**
     * Sets the size3Precision value for this RegionOptions.
     * 
     * @param size3Precision
     */
    public void setSize3Precision(int size3Precision) {
        this.size3Precision = size3Precision;
    }


    /**
     * Gets the unitOfDistance value for this RegionOptions.
     * 
     * @return unitOfDistance
     */
    public com.freshdirect.routing.proxy.stub.transportation.UnitDistance getUnitOfDistance() {
        return unitOfDistance;
    }


    /**
     * Sets the unitOfDistance value for this RegionOptions.
     * 
     * @param unitOfDistance
     */
    public void setUnitOfDistance(com.freshdirect.routing.proxy.stub.transportation.UnitDistance unitOfDistance) {
        this.unitOfDistance = unitOfDistance;
    }


    /**
     * Gets the detailLevel value for this RegionOptions.
     * 
     * @return detailLevel
     */
    public com.freshdirect.routing.proxy.stub.transportation.DetailLevel getDetailLevel() {
        return detailLevel;
    }


    /**
     * Sets the detailLevel value for this RegionOptions.
     * 
     * @param detailLevel
     */
    public void setDetailLevel(com.freshdirect.routing.proxy.stub.transportation.DetailLevel detailLevel) {
        this.detailLevel = detailLevel;
    }


    /**
     * Gets the costQuantityReference value for this RegionOptions.
     * 
     * @return costQuantityReference
     */
    public com.freshdirect.routing.proxy.stub.transportation.QuantityReference getCostQuantityReference() {
        return costQuantityReference;
    }


    /**
     * Sets the costQuantityReference value for this RegionOptions.
     * 
     * @param costQuantityReference
     */
    public void setCostQuantityReference(com.freshdirect.routing.proxy.stub.transportation.QuantityReference costQuantityReference) {
        this.costQuantityReference = costQuantityReference;
    }


    /**
     * Gets the stopCriticalMinutes value for this RegionOptions.
     * 
     * @return stopCriticalMinutes
     */
    public int getStopCriticalMinutes() {
        return stopCriticalMinutes;
    }


    /**
     * Sets the stopCriticalMinutes value for this RegionOptions.
     * 
     * @param stopCriticalMinutes
     */
    public void setStopCriticalMinutes(int stopCriticalMinutes) {
        this.stopCriticalMinutes = stopCriticalMinutes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegionOptions)) return false;
        RegionOptions other = (RegionOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.size1Alias==null && other.getSize1Alias()==null) || 
             (this.size1Alias!=null &&
              this.size1Alias.equals(other.getSize1Alias()))) &&
            ((this.size2Alias==null && other.getSize2Alias()==null) || 
             (this.size2Alias!=null &&
              this.size2Alias.equals(other.getSize2Alias()))) &&
            ((this.size3Alias==null && other.getSize3Alias()==null) || 
             (this.size3Alias!=null &&
              this.size3Alias.equals(other.getSize3Alias()))) &&
            this.size1Precision == other.getSize1Precision() &&
            this.size2Precision == other.getSize2Precision() &&
            this.size3Precision == other.getSize3Precision() &&
            ((this.unitOfDistance==null && other.getUnitOfDistance()==null) || 
             (this.unitOfDistance!=null &&
              this.unitOfDistance.equals(other.getUnitOfDistance()))) &&
            ((this.detailLevel==null && other.getDetailLevel()==null) || 
             (this.detailLevel!=null &&
              this.detailLevel.equals(other.getDetailLevel()))) &&
            ((this.costQuantityReference==null && other.getCostQuantityReference()==null) || 
             (this.costQuantityReference!=null &&
              this.costQuantityReference.equals(other.getCostQuantityReference()))) &&
            this.stopCriticalMinutes == other.getStopCriticalMinutes();
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
        if (getSize1Alias() != null) {
            _hashCode += getSize1Alias().hashCode();
        }
        if (getSize2Alias() != null) {
            _hashCode += getSize2Alias().hashCode();
        }
        if (getSize3Alias() != null) {
            _hashCode += getSize3Alias().hashCode();
        }
        _hashCode += getSize1Precision();
        _hashCode += getSize2Precision();
        _hashCode += getSize3Precision();
        if (getUnitOfDistance() != null) {
            _hashCode += getUnitOfDistance().hashCode();
        }
        if (getDetailLevel() != null) {
            _hashCode += getDetailLevel().hashCode();
        }
        if (getCostQuantityReference() != null) {
            _hashCode += getCostQuantityReference().hashCode();
        }
        _hashCode += getStopCriticalMinutes();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegionOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RegionOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size1Alias");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "size1Alias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size2Alias");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "size2Alias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size3Alias");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "size3Alias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size1Precision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "size1Precision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size2Precision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "size2Precision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size3Precision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "size3Precision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitOfDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "unitOfDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UnitDistance"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detailLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "detailLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DetailLevel"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("costQuantityReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "costQuantityReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "QuantityReference"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopCriticalMinutes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopCriticalMinutes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
