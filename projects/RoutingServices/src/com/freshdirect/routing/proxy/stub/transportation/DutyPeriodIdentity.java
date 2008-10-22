/**
 * DutyPeriodIdentity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DutyPeriodIdentity  extends com.freshdirect.routing.proxy.stub.transportation.RouteIdentity  implements java.io.Serializable {
    private java.lang.String driverRegionID;

    private java.lang.String driverID;

    private int journalIx;

    public DutyPeriodIdentity() {
    }

    public DutyPeriodIdentity(
           java.lang.String regionID,
           java.util.Date routeDate,
           java.lang.String routeID,
           java.lang.String driverRegionID,
           java.lang.String driverID,
           int journalIx) {
        super(
            regionID,
            routeDate,
            routeID);
        this.driverRegionID = driverRegionID;
        this.driverID = driverID;
        this.journalIx = journalIx;
    }


    /**
     * Gets the driverRegionID value for this DutyPeriodIdentity.
     * 
     * @return driverRegionID
     */
    public java.lang.String getDriverRegionID() {
        return driverRegionID;
    }


    /**
     * Sets the driverRegionID value for this DutyPeriodIdentity.
     * 
     * @param driverRegionID
     */
    public void setDriverRegionID(java.lang.String driverRegionID) {
        this.driverRegionID = driverRegionID;
    }


    /**
     * Gets the driverID value for this DutyPeriodIdentity.
     * 
     * @return driverID
     */
    public java.lang.String getDriverID() {
        return driverID;
    }


    /**
     * Sets the driverID value for this DutyPeriodIdentity.
     * 
     * @param driverID
     */
    public void setDriverID(java.lang.String driverID) {
        this.driverID = driverID;
    }


    /**
     * Gets the journalIx value for this DutyPeriodIdentity.
     * 
     * @return journalIx
     */
    public int getJournalIx() {
        return journalIx;
    }


    /**
     * Sets the journalIx value for this DutyPeriodIdentity.
     * 
     * @param journalIx
     */
    public void setJournalIx(int journalIx) {
        this.journalIx = journalIx;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DutyPeriodIdentity)) return false;
        DutyPeriodIdentity other = (DutyPeriodIdentity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.driverRegionID==null && other.getDriverRegionID()==null) || 
             (this.driverRegionID!=null &&
              this.driverRegionID.equals(other.getDriverRegionID()))) &&
            ((this.driverID==null && other.getDriverID()==null) || 
             (this.driverID!=null &&
              this.driverID.equals(other.getDriverID()))) &&
            this.journalIx == other.getJournalIx();
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
        if (getDriverRegionID() != null) {
            _hashCode += getDriverRegionID().hashCode();
        }
        if (getDriverID() != null) {
            _hashCode += getDriverID().hashCode();
        }
        _hashCode += getJournalIx();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DutyPeriodIdentity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DutyPeriodIdentity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverRegionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverRegionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("journalIx");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "journalIx"));
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
