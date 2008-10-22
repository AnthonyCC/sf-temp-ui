/**
 * EquipmentType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class EquipmentType  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity equipmentTypeIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities;

    private java.lang.String description;

    private double height;

    private double weight;

    private boolean travelRestrictions;

    private boolean rushHourRestrictions;

    private boolean roadRestrictions;

    public EquipmentType() {
    }

    public EquipmentType(
           com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity equipmentTypeIdentity,
           com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities,
           java.lang.String description,
           double height,
           double weight,
           boolean travelRestrictions,
           boolean rushHourRestrictions,
           boolean roadRestrictions) {
           this.equipmentTypeIdentity = equipmentTypeIdentity;
           this.quantities = quantities;
           this.description = description;
           this.height = height;
           this.weight = weight;
           this.travelRestrictions = travelRestrictions;
           this.rushHourRestrictions = rushHourRestrictions;
           this.roadRestrictions = roadRestrictions;
    }


    /**
     * Gets the equipmentTypeIdentity value for this EquipmentType.
     * 
     * @return equipmentTypeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity getEquipmentTypeIdentity() {
        return equipmentTypeIdentity;
    }


    /**
     * Sets the equipmentTypeIdentity value for this EquipmentType.
     * 
     * @param equipmentTypeIdentity
     */
    public void setEquipmentTypeIdentity(com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity equipmentTypeIdentity) {
        this.equipmentTypeIdentity = equipmentTypeIdentity;
    }


    /**
     * Gets the quantities value for this EquipmentType.
     * 
     * @return quantities
     */
    public com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities getQuantities() {
        return quantities;
    }


    /**
     * Sets the quantities value for this EquipmentType.
     * 
     * @param quantities
     */
    public void setQuantities(com.freshdirect.routing.proxy.stub.transportation.CategoryQuantities quantities) {
        this.quantities = quantities;
    }


    /**
     * Gets the description value for this EquipmentType.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this EquipmentType.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the height value for this EquipmentType.
     * 
     * @return height
     */
    public double getHeight() {
        return height;
    }


    /**
     * Sets the height value for this EquipmentType.
     * 
     * @param height
     */
    public void setHeight(double height) {
        this.height = height;
    }


    /**
     * Gets the weight value for this EquipmentType.
     * 
     * @return weight
     */
    public double getWeight() {
        return weight;
    }


    /**
     * Sets the weight value for this EquipmentType.
     * 
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }


    /**
     * Gets the travelRestrictions value for this EquipmentType.
     * 
     * @return travelRestrictions
     */
    public boolean isTravelRestrictions() {
        return travelRestrictions;
    }


    /**
     * Sets the travelRestrictions value for this EquipmentType.
     * 
     * @param travelRestrictions
     */
    public void setTravelRestrictions(boolean travelRestrictions) {
        this.travelRestrictions = travelRestrictions;
    }


    /**
     * Gets the rushHourRestrictions value for this EquipmentType.
     * 
     * @return rushHourRestrictions
     */
    public boolean isRushHourRestrictions() {
        return rushHourRestrictions;
    }


    /**
     * Sets the rushHourRestrictions value for this EquipmentType.
     * 
     * @param rushHourRestrictions
     */
    public void setRushHourRestrictions(boolean rushHourRestrictions) {
        this.rushHourRestrictions = rushHourRestrictions;
    }


    /**
     * Gets the roadRestrictions value for this EquipmentType.
     * 
     * @return roadRestrictions
     */
    public boolean isRoadRestrictions() {
        return roadRestrictions;
    }


    /**
     * Sets the roadRestrictions value for this EquipmentType.
     * 
     * @param roadRestrictions
     */
    public void setRoadRestrictions(boolean roadRestrictions) {
        this.roadRestrictions = roadRestrictions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EquipmentType)) return false;
        EquipmentType other = (EquipmentType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.equipmentTypeIdentity==null && other.getEquipmentTypeIdentity()==null) || 
             (this.equipmentTypeIdentity!=null &&
              this.equipmentTypeIdentity.equals(other.getEquipmentTypeIdentity()))) &&
            ((this.quantities==null && other.getQuantities()==null) || 
             (this.quantities!=null &&
              this.quantities.equals(other.getQuantities()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.height == other.getHeight() &&
            this.weight == other.getWeight() &&
            this.travelRestrictions == other.isTravelRestrictions() &&
            this.rushHourRestrictions == other.isRushHourRestrictions() &&
            this.roadRestrictions == other.isRoadRestrictions();
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
        if (getEquipmentTypeIdentity() != null) {
            _hashCode += getEquipmentTypeIdentity().hashCode();
        }
        if (getQuantities() != null) {
            _hashCode += getQuantities().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += new Double(getHeight()).hashCode();
        _hashCode += new Double(getWeight()).hashCode();
        _hashCode += (isTravelRestrictions() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRushHourRestrictions() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRoadRestrictions() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EquipmentType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentTypeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentTypeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentTypeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "quantities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "CategoryQuantities"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("height");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "height"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("weight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "weight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("travelRestrictions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "travelRestrictions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rushHourRestrictions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "rushHourRestrictions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roadRestrictions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "roadRestrictions"));
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
