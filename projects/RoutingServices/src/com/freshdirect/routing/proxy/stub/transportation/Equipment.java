/**
 * Equipment.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Equipment  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity equipmentIdentity;

    private java.lang.String description;

    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity preferredDriverIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity buddyEquipmentIdentity;

    private java.lang.String equipmentStatus;

    private java.util.Date dateLastInspected;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity lastDroppedOffLocationIdentity;

    private java.util.Calendar lastDroppedOffTime;

    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity currentRouteIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity;

    private boolean hasOnboardComputer;

    private java.lang.String gpsUnitID;

    public Equipment() {
    }

    public Equipment(
           com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity equipmentIdentity,
           java.lang.String description,
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity preferredDriverIdentity,
           com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity buddyEquipmentIdentity,
           java.lang.String equipmentStatus,
           java.util.Date dateLastInspected,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity lastDroppedOffLocationIdentity,
           java.util.Calendar lastDroppedOffTime,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity currentRouteIdentity,
           com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity,
           boolean hasOnboardComputer,
           java.lang.String gpsUnitID) {
           this.equipmentIdentity = equipmentIdentity;
           this.description = description;
           this.preferredDriverIdentity = preferredDriverIdentity;
           this.buddyEquipmentIdentity = buddyEquipmentIdentity;
           this.equipmentStatus = equipmentStatus;
           this.dateLastInspected = dateLastInspected;
           this.lastDroppedOffLocationIdentity = lastDroppedOffLocationIdentity;
           this.lastDroppedOffTime = lastDroppedOffTime;
           this.currentRouteIdentity = currentRouteIdentity;
           this.wirelessDeviceIdentity = wirelessDeviceIdentity;
           this.hasOnboardComputer = hasOnboardComputer;
           this.gpsUnitID = gpsUnitID;
    }


    /**
     * Gets the equipmentIdentity value for this Equipment.
     * 
     * @return equipmentIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity getEquipmentIdentity() {
        return equipmentIdentity;
    }


    /**
     * Sets the equipmentIdentity value for this Equipment.
     * 
     * @param equipmentIdentity
     */
    public void setEquipmentIdentity(com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity equipmentIdentity) {
        this.equipmentIdentity = equipmentIdentity;
    }


    /**
     * Gets the description value for this Equipment.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Equipment.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the preferredDriverIdentity value for this Equipment.
     * 
     * @return preferredDriverIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getPreferredDriverIdentity() {
        return preferredDriverIdentity;
    }


    /**
     * Sets the preferredDriverIdentity value for this Equipment.
     * 
     * @param preferredDriverIdentity
     */
    public void setPreferredDriverIdentity(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity preferredDriverIdentity) {
        this.preferredDriverIdentity = preferredDriverIdentity;
    }


    /**
     * Gets the buddyEquipmentIdentity value for this Equipment.
     * 
     * @return buddyEquipmentIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity getBuddyEquipmentIdentity() {
        return buddyEquipmentIdentity;
    }


    /**
     * Sets the buddyEquipmentIdentity value for this Equipment.
     * 
     * @param buddyEquipmentIdentity
     */
    public void setBuddyEquipmentIdentity(com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity buddyEquipmentIdentity) {
        this.buddyEquipmentIdentity = buddyEquipmentIdentity;
    }


    /**
     * Gets the equipmentStatus value for this Equipment.
     * 
     * @return equipmentStatus
     */
    public java.lang.String getEquipmentStatus() {
        return equipmentStatus;
    }


    /**
     * Sets the equipmentStatus value for this Equipment.
     * 
     * @param equipmentStatus
     */
    public void setEquipmentStatus(java.lang.String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }


    /**
     * Gets the dateLastInspected value for this Equipment.
     * 
     * @return dateLastInspected
     */
    public java.util.Date getDateLastInspected() {
        return dateLastInspected;
    }


    /**
     * Sets the dateLastInspected value for this Equipment.
     * 
     * @param dateLastInspected
     */
    public void setDateLastInspected(java.util.Date dateLastInspected) {
        this.dateLastInspected = dateLastInspected;
    }


    /**
     * Gets the lastDroppedOffLocationIdentity value for this Equipment.
     * 
     * @return lastDroppedOffLocationIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getLastDroppedOffLocationIdentity() {
        return lastDroppedOffLocationIdentity;
    }


    /**
     * Sets the lastDroppedOffLocationIdentity value for this Equipment.
     * 
     * @param lastDroppedOffLocationIdentity
     */
    public void setLastDroppedOffLocationIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity lastDroppedOffLocationIdentity) {
        this.lastDroppedOffLocationIdentity = lastDroppedOffLocationIdentity;
    }


    /**
     * Gets the lastDroppedOffTime value for this Equipment.
     * 
     * @return lastDroppedOffTime
     */
    public java.util.Calendar getLastDroppedOffTime() {
        return lastDroppedOffTime;
    }


    /**
     * Sets the lastDroppedOffTime value for this Equipment.
     * 
     * @param lastDroppedOffTime
     */
    public void setLastDroppedOffTime(java.util.Calendar lastDroppedOffTime) {
        this.lastDroppedOffTime = lastDroppedOffTime;
    }


    /**
     * Gets the currentRouteIdentity value for this Equipment.
     * 
     * @return currentRouteIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getCurrentRouteIdentity() {
        return currentRouteIdentity;
    }


    /**
     * Sets the currentRouteIdentity value for this Equipment.
     * 
     * @param currentRouteIdentity
     */
    public void setCurrentRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity currentRouteIdentity) {
        this.currentRouteIdentity = currentRouteIdentity;
    }


    /**
     * Gets the wirelessDeviceIdentity value for this Equipment.
     * 
     * @return wirelessDeviceIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity getWirelessDeviceIdentity() {
        return wirelessDeviceIdentity;
    }


    /**
     * Sets the wirelessDeviceIdentity value for this Equipment.
     * 
     * @param wirelessDeviceIdentity
     */
    public void setWirelessDeviceIdentity(com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity) {
        this.wirelessDeviceIdentity = wirelessDeviceIdentity;
    }


    /**
     * Gets the hasOnboardComputer value for this Equipment.
     * 
     * @return hasOnboardComputer
     */
    public boolean isHasOnboardComputer() {
        return hasOnboardComputer;
    }


    /**
     * Sets the hasOnboardComputer value for this Equipment.
     * 
     * @param hasOnboardComputer
     */
    public void setHasOnboardComputer(boolean hasOnboardComputer) {
        this.hasOnboardComputer = hasOnboardComputer;
    }


    /**
     * Gets the gpsUnitID value for this Equipment.
     * 
     * @return gpsUnitID
     */
    public java.lang.String getGpsUnitID() {
        return gpsUnitID;
    }


    /**
     * Sets the gpsUnitID value for this Equipment.
     * 
     * @param gpsUnitID
     */
    public void setGpsUnitID(java.lang.String gpsUnitID) {
        this.gpsUnitID = gpsUnitID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Equipment)) return false;
        Equipment other = (Equipment) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.equipmentIdentity==null && other.getEquipmentIdentity()==null) || 
             (this.equipmentIdentity!=null &&
              this.equipmentIdentity.equals(other.getEquipmentIdentity()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.preferredDriverIdentity==null && other.getPreferredDriverIdentity()==null) || 
             (this.preferredDriverIdentity!=null &&
              this.preferredDriverIdentity.equals(other.getPreferredDriverIdentity()))) &&
            ((this.buddyEquipmentIdentity==null && other.getBuddyEquipmentIdentity()==null) || 
             (this.buddyEquipmentIdentity!=null &&
              this.buddyEquipmentIdentity.equals(other.getBuddyEquipmentIdentity()))) &&
            ((this.equipmentStatus==null && other.getEquipmentStatus()==null) || 
             (this.equipmentStatus!=null &&
              this.equipmentStatus.equals(other.getEquipmentStatus()))) &&
            ((this.dateLastInspected==null && other.getDateLastInspected()==null) || 
             (this.dateLastInspected!=null &&
              this.dateLastInspected.equals(other.getDateLastInspected()))) &&
            ((this.lastDroppedOffLocationIdentity==null && other.getLastDroppedOffLocationIdentity()==null) || 
             (this.lastDroppedOffLocationIdentity!=null &&
              this.lastDroppedOffLocationIdentity.equals(other.getLastDroppedOffLocationIdentity()))) &&
            ((this.lastDroppedOffTime==null && other.getLastDroppedOffTime()==null) || 
             (this.lastDroppedOffTime!=null &&
              this.lastDroppedOffTime.equals(other.getLastDroppedOffTime()))) &&
            ((this.currentRouteIdentity==null && other.getCurrentRouteIdentity()==null) || 
             (this.currentRouteIdentity!=null &&
              this.currentRouteIdentity.equals(other.getCurrentRouteIdentity()))) &&
            ((this.wirelessDeviceIdentity==null && other.getWirelessDeviceIdentity()==null) || 
             (this.wirelessDeviceIdentity!=null &&
              this.wirelessDeviceIdentity.equals(other.getWirelessDeviceIdentity()))) &&
            this.hasOnboardComputer == other.isHasOnboardComputer() &&
            ((this.gpsUnitID==null && other.getGpsUnitID()==null) || 
             (this.gpsUnitID!=null &&
              this.gpsUnitID.equals(other.getGpsUnitID())));
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
        if (getEquipmentIdentity() != null) {
            _hashCode += getEquipmentIdentity().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getPreferredDriverIdentity() != null) {
            _hashCode += getPreferredDriverIdentity().hashCode();
        }
        if (getBuddyEquipmentIdentity() != null) {
            _hashCode += getBuddyEquipmentIdentity().hashCode();
        }
        if (getEquipmentStatus() != null) {
            _hashCode += getEquipmentStatus().hashCode();
        }
        if (getDateLastInspected() != null) {
            _hashCode += getDateLastInspected().hashCode();
        }
        if (getLastDroppedOffLocationIdentity() != null) {
            _hashCode += getLastDroppedOffLocationIdentity().hashCode();
        }
        if (getLastDroppedOffTime() != null) {
            _hashCode += getLastDroppedOffTime().hashCode();
        }
        if (getCurrentRouteIdentity() != null) {
            _hashCode += getCurrentRouteIdentity().hashCode();
        }
        if (getWirelessDeviceIdentity() != null) {
            _hashCode += getWirelessDeviceIdentity().hashCode();
        }
        _hashCode += (isHasOnboardComputer() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getGpsUnitID() != null) {
            _hashCode += getGpsUnitID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Equipment.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Equipment"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredDriverIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredDriverIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buddyEquipmentIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "buddyEquipmentIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateLastInspected");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateLastInspected"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastDroppedOffLocationIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lastDroppedOffLocationIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastDroppedOffTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lastDroppedOffTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentRouteIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "currentRouteIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wirelessDeviceIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "wirelessDeviceIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "WirelessDeviceIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasOnboardComputer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "hasOnboardComputer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gpsUnitID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "gpsUnitID"));
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
