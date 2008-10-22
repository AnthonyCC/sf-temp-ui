/**
 * EquipmentCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class EquipmentCriteria  implements java.io.Serializable {
    private java.lang.String regionID;

    private java.lang.String equipmentType;

    private java.lang.String equipmentID;

    private java.lang.String gpsUnitID;

    private java.lang.String equipmentStatus;

    public EquipmentCriteria() {
    }

    public EquipmentCriteria(
           java.lang.String regionID,
           java.lang.String equipmentType,
           java.lang.String equipmentID,
           java.lang.String gpsUnitID,
           java.lang.String equipmentStatus) {
           this.regionID = regionID;
           this.equipmentType = equipmentType;
           this.equipmentID = equipmentID;
           this.gpsUnitID = gpsUnitID;
           this.equipmentStatus = equipmentStatus;
    }


    /**
     * Gets the regionID value for this EquipmentCriteria.
     * 
     * @return regionID
     */
    public java.lang.String getRegionID() {
        return regionID;
    }


    /**
     * Sets the regionID value for this EquipmentCriteria.
     * 
     * @param regionID
     */
    public void setRegionID(java.lang.String regionID) {
        this.regionID = regionID;
    }


    /**
     * Gets the equipmentType value for this EquipmentCriteria.
     * 
     * @return equipmentType
     */
    public java.lang.String getEquipmentType() {
        return equipmentType;
    }


    /**
     * Sets the equipmentType value for this EquipmentCriteria.
     * 
     * @param equipmentType
     */
    public void setEquipmentType(java.lang.String equipmentType) {
        this.equipmentType = equipmentType;
    }


    /**
     * Gets the equipmentID value for this EquipmentCriteria.
     * 
     * @return equipmentID
     */
    public java.lang.String getEquipmentID() {
        return equipmentID;
    }


    /**
     * Sets the equipmentID value for this EquipmentCriteria.
     * 
     * @param equipmentID
     */
    public void setEquipmentID(java.lang.String equipmentID) {
        this.equipmentID = equipmentID;
    }


    /**
     * Gets the gpsUnitID value for this EquipmentCriteria.
     * 
     * @return gpsUnitID
     */
    public java.lang.String getGpsUnitID() {
        return gpsUnitID;
    }


    /**
     * Sets the gpsUnitID value for this EquipmentCriteria.
     * 
     * @param gpsUnitID
     */
    public void setGpsUnitID(java.lang.String gpsUnitID) {
        this.gpsUnitID = gpsUnitID;
    }


    /**
     * Gets the equipmentStatus value for this EquipmentCriteria.
     * 
     * @return equipmentStatus
     */
    public java.lang.String getEquipmentStatus() {
        return equipmentStatus;
    }


    /**
     * Sets the equipmentStatus value for this EquipmentCriteria.
     * 
     * @param equipmentStatus
     */
    public void setEquipmentStatus(java.lang.String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EquipmentCriteria)) return false;
        EquipmentCriteria other = (EquipmentCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.regionID==null && other.getRegionID()==null) || 
             (this.regionID!=null &&
              this.regionID.equals(other.getRegionID()))) &&
            ((this.equipmentType==null && other.getEquipmentType()==null) || 
             (this.equipmentType!=null &&
              this.equipmentType.equals(other.getEquipmentType()))) &&
            ((this.equipmentID==null && other.getEquipmentID()==null) || 
             (this.equipmentID!=null &&
              this.equipmentID.equals(other.getEquipmentID()))) &&
            ((this.gpsUnitID==null && other.getGpsUnitID()==null) || 
             (this.gpsUnitID!=null &&
              this.gpsUnitID.equals(other.getGpsUnitID()))) &&
            ((this.equipmentStatus==null && other.getEquipmentStatus()==null) || 
             (this.equipmentStatus!=null &&
              this.equipmentStatus.equals(other.getEquipmentStatus())));
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
        if (getRegionID() != null) {
            _hashCode += getRegionID().hashCode();
        }
        if (getEquipmentType() != null) {
            _hashCode += getEquipmentType().hashCode();
        }
        if (getEquipmentID() != null) {
            _hashCode += getEquipmentID().hashCode();
        }
        if (getGpsUnitID() != null) {
            _hashCode += getGpsUnitID().hashCode();
        }
        if (getEquipmentStatus() != null) {
            _hashCode += getEquipmentStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EquipmentCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gpsUnitID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "gpsUnitID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentStatus"));
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
