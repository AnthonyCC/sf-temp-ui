/**
 * PositionHistoryCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class PositionHistoryCriteria  implements java.io.Serializable {
    private java.lang.String regionID;

    private java.util.Date routeDate;

    private java.lang.String routeID;

    private java.lang.String equipmentID;

    private java.lang.String equipmentTypeID;

    public PositionHistoryCriteria() {
    }

    public PositionHistoryCriteria(
           java.lang.String regionID,
           java.util.Date routeDate,
           java.lang.String routeID,
           java.lang.String equipmentID,
           java.lang.String equipmentTypeID) {
           this.regionID = regionID;
           this.routeDate = routeDate;
           this.routeID = routeID;
           this.equipmentID = equipmentID;
           this.equipmentTypeID = equipmentTypeID;
    }


    /**
     * Gets the regionID value for this PositionHistoryCriteria.
     * 
     * @return regionID
     */
    public java.lang.String getRegionID() {
        return regionID;
    }


    /**
     * Sets the regionID value for this PositionHistoryCriteria.
     * 
     * @param regionID
     */
    public void setRegionID(java.lang.String regionID) {
        this.regionID = regionID;
    }


    /**
     * Gets the routeDate value for this PositionHistoryCriteria.
     * 
     * @return routeDate
     */
    public java.util.Date getRouteDate() {
        return routeDate;
    }


    /**
     * Sets the routeDate value for this PositionHistoryCriteria.
     * 
     * @param routeDate
     */
    public void setRouteDate(java.util.Date routeDate) {
        this.routeDate = routeDate;
    }


    /**
     * Gets the routeID value for this PositionHistoryCriteria.
     * 
     * @return routeID
     */
    public java.lang.String getRouteID() {
        return routeID;
    }


    /**
     * Sets the routeID value for this PositionHistoryCriteria.
     * 
     * @param routeID
     */
    public void setRouteID(java.lang.String routeID) {
        this.routeID = routeID;
    }


    /**
     * Gets the equipmentID value for this PositionHistoryCriteria.
     * 
     * @return equipmentID
     */
    public java.lang.String getEquipmentID() {
        return equipmentID;
    }


    /**
     * Sets the equipmentID value for this PositionHistoryCriteria.
     * 
     * @param equipmentID
     */
    public void setEquipmentID(java.lang.String equipmentID) {
        this.equipmentID = equipmentID;
    }


    /**
     * Gets the equipmentTypeID value for this PositionHistoryCriteria.
     * 
     * @return equipmentTypeID
     */
    public java.lang.String getEquipmentTypeID() {
        return equipmentTypeID;
    }


    /**
     * Sets the equipmentTypeID value for this PositionHistoryCriteria.
     * 
     * @param equipmentTypeID
     */
    public void setEquipmentTypeID(java.lang.String equipmentTypeID) {
        this.equipmentTypeID = equipmentTypeID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PositionHistoryCriteria)) return false;
        PositionHistoryCriteria other = (PositionHistoryCriteria) obj;
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
            ((this.routeDate==null && other.getRouteDate()==null) || 
             (this.routeDate!=null &&
              this.routeDate.equals(other.getRouteDate()))) &&
            ((this.routeID==null && other.getRouteID()==null) || 
             (this.routeID!=null &&
              this.routeID.equals(other.getRouteID()))) &&
            ((this.equipmentID==null && other.getEquipmentID()==null) || 
             (this.equipmentID!=null &&
              this.equipmentID.equals(other.getEquipmentID()))) &&
            ((this.equipmentTypeID==null && other.getEquipmentTypeID()==null) || 
             (this.equipmentTypeID!=null &&
              this.equipmentTypeID.equals(other.getEquipmentTypeID())));
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
        if (getRouteDate() != null) {
            _hashCode += getRouteDate().hashCode();
        }
        if (getRouteID() != null) {
            _hashCode += getRouteID().hashCode();
        }
        if (getEquipmentID() != null) {
            _hashCode += getEquipmentID().hashCode();
        }
        if (getEquipmentTypeID() != null) {
            _hashCode += getEquipmentTypeID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PositionHistoryCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "PositionHistoryCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeID"));
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
        elemField.setFieldName("equipmentTypeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentTypeID"));
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
