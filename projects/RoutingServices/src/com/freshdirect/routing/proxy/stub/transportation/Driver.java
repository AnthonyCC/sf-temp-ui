/**
 * Driver.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class Driver  extends com.freshdirect.routing.proxy.stub.transportation.Employee  implements java.io.Serializable {
    private java.lang.String licenseNumber;

    private java.util.Date dateLicenseExpires;

    private int dotPeriod;

    private int dotMaxTime;

    private java.util.Date dateLastPhysical;

    private java.util.Date dateLastDrugTest;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferredOrigin;

    private com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity preferredEquipmentIdentity;

    private java.lang.String preferredRouteID;

    private boolean preloadDriverDirections;

    private com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity;

    public Driver() {
    }

    public Driver(
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity,
           java.lang.String employeeType,
           boolean active,
           java.lang.String employeeStatus,
           java.lang.String lastName,
           java.lang.String firstName,
           java.lang.String middleName,
           java.lang.String homePhoneNumber,
           java.lang.String workPhoneNumber,
           java.util.Date dateOfBirth,
           java.util.Date dateStarted,
           java.util.Date dateTerminated,
           java.lang.String ADPFileNumber,
           java.lang.String password,
           com.freshdirect.routing.proxy.stub.transportation.Address address,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity currentRouteIdentity,
           java.lang.String licenseNumber,
           java.util.Date dateLicenseExpires,
           int dotPeriod,
           int dotMaxTime,
           java.util.Date dateLastPhysical,
           java.util.Date dateLastDrugTest,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferredOrigin,
           com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity preferredEquipmentIdentity,
           java.lang.String preferredRouteID,
           boolean preloadDriverDirections,
           com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity) {
        super(
            employeeIdentity,
            employeeType,
            active,
            employeeStatus,
            lastName,
            firstName,
            middleName,
            homePhoneNumber,
            workPhoneNumber,
            dateOfBirth,
            dateStarted,
            dateTerminated,
            ADPFileNumber,
            password,
            address,
            currentRouteIdentity);
        this.licenseNumber = licenseNumber;
        this.dateLicenseExpires = dateLicenseExpires;
        this.dotPeriod = dotPeriod;
        this.dotMaxTime = dotMaxTime;
        this.dateLastPhysical = dateLastPhysical;
        this.dateLastDrugTest = dateLastDrugTest;
        this.preferredOrigin = preferredOrigin;
        this.preferredEquipmentIdentity = preferredEquipmentIdentity;
        this.preferredRouteID = preferredRouteID;
        this.preloadDriverDirections = preloadDriverDirections;
        this.wirelessDeviceIdentity = wirelessDeviceIdentity;
    }


    /**
     * Gets the licenseNumber value for this Driver.
     * 
     * @return licenseNumber
     */
    public java.lang.String getLicenseNumber() {
        return licenseNumber;
    }


    /**
     * Sets the licenseNumber value for this Driver.
     * 
     * @param licenseNumber
     */
    public void setLicenseNumber(java.lang.String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }


    /**
     * Gets the dateLicenseExpires value for this Driver.
     * 
     * @return dateLicenseExpires
     */
    public java.util.Date getDateLicenseExpires() {
        return dateLicenseExpires;
    }


    /**
     * Sets the dateLicenseExpires value for this Driver.
     * 
     * @param dateLicenseExpires
     */
    public void setDateLicenseExpires(java.util.Date dateLicenseExpires) {
        this.dateLicenseExpires = dateLicenseExpires;
    }


    /**
     * Gets the dotPeriod value for this Driver.
     * 
     * @return dotPeriod
     */
    public int getDotPeriod() {
        return dotPeriod;
    }


    /**
     * Sets the dotPeriod value for this Driver.
     * 
     * @param dotPeriod
     */
    public void setDotPeriod(int dotPeriod) {
        this.dotPeriod = dotPeriod;
    }


    /**
     * Gets the dotMaxTime value for this Driver.
     * 
     * @return dotMaxTime
     */
    public int getDotMaxTime() {
        return dotMaxTime;
    }


    /**
     * Sets the dotMaxTime value for this Driver.
     * 
     * @param dotMaxTime
     */
    public void setDotMaxTime(int dotMaxTime) {
        this.dotMaxTime = dotMaxTime;
    }


    /**
     * Gets the dateLastPhysical value for this Driver.
     * 
     * @return dateLastPhysical
     */
    public java.util.Date getDateLastPhysical() {
        return dateLastPhysical;
    }


    /**
     * Sets the dateLastPhysical value for this Driver.
     * 
     * @param dateLastPhysical
     */
    public void setDateLastPhysical(java.util.Date dateLastPhysical) {
        this.dateLastPhysical = dateLastPhysical;
    }


    /**
     * Gets the dateLastDrugTest value for this Driver.
     * 
     * @return dateLastDrugTest
     */
    public java.util.Date getDateLastDrugTest() {
        return dateLastDrugTest;
    }


    /**
     * Sets the dateLastDrugTest value for this Driver.
     * 
     * @param dateLastDrugTest
     */
    public void setDateLastDrugTest(java.util.Date dateLastDrugTest) {
        this.dateLastDrugTest = dateLastDrugTest;
    }


    /**
     * Gets the preferredOrigin value for this Driver.
     * 
     * @return preferredOrigin
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getPreferredOrigin() {
        return preferredOrigin;
    }


    /**
     * Sets the preferredOrigin value for this Driver.
     * 
     * @param preferredOrigin
     */
    public void setPreferredOrigin(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity preferredOrigin) {
        this.preferredOrigin = preferredOrigin;
    }


    /**
     * Gets the preferredEquipmentIdentity value for this Driver.
     * 
     * @return preferredEquipmentIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity getPreferredEquipmentIdentity() {
        return preferredEquipmentIdentity;
    }


    /**
     * Sets the preferredEquipmentIdentity value for this Driver.
     * 
     * @param preferredEquipmentIdentity
     */
    public void setPreferredEquipmentIdentity(com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity preferredEquipmentIdentity) {
        this.preferredEquipmentIdentity = preferredEquipmentIdentity;
    }


    /**
     * Gets the preferredRouteID value for this Driver.
     * 
     * @return preferredRouteID
     */
    public java.lang.String getPreferredRouteID() {
        return preferredRouteID;
    }


    /**
     * Sets the preferredRouteID value for this Driver.
     * 
     * @param preferredRouteID
     */
    public void setPreferredRouteID(java.lang.String preferredRouteID) {
        this.preferredRouteID = preferredRouteID;
    }


    /**
     * Gets the preloadDriverDirections value for this Driver.
     * 
     * @return preloadDriverDirections
     */
    public boolean isPreloadDriverDirections() {
        return preloadDriverDirections;
    }


    /**
     * Sets the preloadDriverDirections value for this Driver.
     * 
     * @param preloadDriverDirections
     */
    public void setPreloadDriverDirections(boolean preloadDriverDirections) {
        this.preloadDriverDirections = preloadDriverDirections;
    }


    /**
     * Gets the wirelessDeviceIdentity value for this Driver.
     * 
     * @return wirelessDeviceIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity getWirelessDeviceIdentity() {
        return wirelessDeviceIdentity;
    }


    /**
     * Sets the wirelessDeviceIdentity value for this Driver.
     * 
     * @param wirelessDeviceIdentity
     */
    public void setWirelessDeviceIdentity(com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity) {
        this.wirelessDeviceIdentity = wirelessDeviceIdentity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Driver)) return false;
        Driver other = (Driver) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.licenseNumber==null && other.getLicenseNumber()==null) || 
             (this.licenseNumber!=null &&
              this.licenseNumber.equals(other.getLicenseNumber()))) &&
            ((this.dateLicenseExpires==null && other.getDateLicenseExpires()==null) || 
             (this.dateLicenseExpires!=null &&
              this.dateLicenseExpires.equals(other.getDateLicenseExpires()))) &&
            this.dotPeriod == other.getDotPeriod() &&
            this.dotMaxTime == other.getDotMaxTime() &&
            ((this.dateLastPhysical==null && other.getDateLastPhysical()==null) || 
             (this.dateLastPhysical!=null &&
              this.dateLastPhysical.equals(other.getDateLastPhysical()))) &&
            ((this.dateLastDrugTest==null && other.getDateLastDrugTest()==null) || 
             (this.dateLastDrugTest!=null &&
              this.dateLastDrugTest.equals(other.getDateLastDrugTest()))) &&
            ((this.preferredOrigin==null && other.getPreferredOrigin()==null) || 
             (this.preferredOrigin!=null &&
              this.preferredOrigin.equals(other.getPreferredOrigin()))) &&
            ((this.preferredEquipmentIdentity==null && other.getPreferredEquipmentIdentity()==null) || 
             (this.preferredEquipmentIdentity!=null &&
              this.preferredEquipmentIdentity.equals(other.getPreferredEquipmentIdentity()))) &&
            ((this.preferredRouteID==null && other.getPreferredRouteID()==null) || 
             (this.preferredRouteID!=null &&
              this.preferredRouteID.equals(other.getPreferredRouteID()))) &&
            this.preloadDriverDirections == other.isPreloadDriverDirections() &&
            ((this.wirelessDeviceIdentity==null && other.getWirelessDeviceIdentity()==null) || 
             (this.wirelessDeviceIdentity!=null &&
              this.wirelessDeviceIdentity.equals(other.getWirelessDeviceIdentity())));
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
        if (getLicenseNumber() != null) {
            _hashCode += getLicenseNumber().hashCode();
        }
        if (getDateLicenseExpires() != null) {
            _hashCode += getDateLicenseExpires().hashCode();
        }
        _hashCode += getDotPeriod();
        _hashCode += getDotMaxTime();
        if (getDateLastPhysical() != null) {
            _hashCode += getDateLastPhysical().hashCode();
        }
        if (getDateLastDrugTest() != null) {
            _hashCode += getDateLastDrugTest().hashCode();
        }
        if (getPreferredOrigin() != null) {
            _hashCode += getPreferredOrigin().hashCode();
        }
        if (getPreferredEquipmentIdentity() != null) {
            _hashCode += getPreferredEquipmentIdentity().hashCode();
        }
        if (getPreferredRouteID() != null) {
            _hashCode += getPreferredRouteID().hashCode();
        }
        _hashCode += (isPreloadDriverDirections() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getWirelessDeviceIdentity() != null) {
            _hashCode += getWirelessDeviceIdentity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Driver.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Driver"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("licenseNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "licenseNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateLicenseExpires");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateLicenseExpires"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dotPeriod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dotPeriod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dotMaxTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dotMaxTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateLastPhysical");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateLastPhysical"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateLastDrugTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateLastDrugTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredOrigin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredOrigin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredEquipmentIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredEquipmentIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredRouteID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredRouteID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preloadDriverDirections");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preloadDriverDirections"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wirelessDeviceIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "wirelessDeviceIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "WirelessDeviceIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
