/**
 * EquipmentTypeIdentity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class EquipmentTypeIdentity  extends com.freshdirect.routing.proxy.stub.transportation.RegionIdentity  implements java.io.Serializable {
    private java.lang.String equipmentTypeID;

    public EquipmentTypeIdentity() {
    }

    public EquipmentTypeIdentity(
           java.lang.String regionID,
           java.lang.String equipmentTypeID) {
        super(
            regionID);
        this.equipmentTypeID = equipmentTypeID;
    }


    /**
     * Gets the equipmentTypeID value for this EquipmentTypeIdentity.
     * 
     * @return equipmentTypeID
     */
    public java.lang.String getEquipmentTypeID() {
        return equipmentTypeID;
    }


    /**
     * Sets the equipmentTypeID value for this EquipmentTypeIdentity.
     * 
     * @param equipmentTypeID
     */
    public void setEquipmentTypeID(java.lang.String equipmentTypeID) {
        this.equipmentTypeID = equipmentTypeID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EquipmentTypeIdentity)) return false;
        EquipmentTypeIdentity other = (EquipmentTypeIdentity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
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
        int _hashCode = super.hashCode();
        if (getEquipmentTypeID() != null) {
            _hashCode += getEquipmentTypeID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EquipmentTypeIdentity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentTypeIdentity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentTypeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentTypeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
