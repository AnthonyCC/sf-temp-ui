/**
 * DutyPeriodCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DutyPeriodCriteria  implements java.io.Serializable {
    private java.lang.String regionID;

    private java.lang.String driverRegionID;

    private java.lang.String driverID;

    private java.util.Date routeDate;

    private java.util.Date routeDateStart;

    private java.util.Date routeDateEnd;

    private java.lang.String routeID;

    public DutyPeriodCriteria() {
    }

    public DutyPeriodCriteria(
           java.lang.String regionID,
           java.lang.String driverRegionID,
           java.lang.String driverID,
           java.util.Date routeDate,
           java.util.Date routeDateStart,
           java.util.Date routeDateEnd,
           java.lang.String routeID) {
           this.regionID = regionID;
           this.driverRegionID = driverRegionID;
           this.driverID = driverID;
           this.routeDate = routeDate;
           this.routeDateStart = routeDateStart;
           this.routeDateEnd = routeDateEnd;
           this.routeID = routeID;
    }


    /**
     * Gets the regionID value for this DutyPeriodCriteria.
     * 
     * @return regionID
     */
    public java.lang.String getRegionID() {
        return regionID;
    }


    /**
     * Sets the regionID value for this DutyPeriodCriteria.
     * 
     * @param regionID
     */
    public void setRegionID(java.lang.String regionID) {
        this.regionID = regionID;
    }


    /**
     * Gets the driverRegionID value for this DutyPeriodCriteria.
     * 
     * @return driverRegionID
     */
    public java.lang.String getDriverRegionID() {
        return driverRegionID;
    }


    /**
     * Sets the driverRegionID value for this DutyPeriodCriteria.
     * 
     * @param driverRegionID
     */
    public void setDriverRegionID(java.lang.String driverRegionID) {
        this.driverRegionID = driverRegionID;
    }


    /**
     * Gets the driverID value for this DutyPeriodCriteria.
     * 
     * @return driverID
     */
    public java.lang.String getDriverID() {
        return driverID;
    }


    /**
     * Sets the driverID value for this DutyPeriodCriteria.
     * 
     * @param driverID
     */
    public void setDriverID(java.lang.String driverID) {
        this.driverID = driverID;
    }


    /**
     * Gets the routeDate value for this DutyPeriodCriteria.
     * 
     * @return routeDate
     */
    public java.util.Date getRouteDate() {
        return routeDate;
    }


    /**
     * Sets the routeDate value for this DutyPeriodCriteria.
     * 
     * @param routeDate
     */
    public void setRouteDate(java.util.Date routeDate) {
        this.routeDate = routeDate;
    }


    /**
     * Gets the routeDateStart value for this DutyPeriodCriteria.
     * 
     * @return routeDateStart
     */
    public java.util.Date getRouteDateStart() {
        return routeDateStart;
    }


    /**
     * Sets the routeDateStart value for this DutyPeriodCriteria.
     * 
     * @param routeDateStart
     */
    public void setRouteDateStart(java.util.Date routeDateStart) {
        this.routeDateStart = routeDateStart;
    }


    /**
     * Gets the routeDateEnd value for this DutyPeriodCriteria.
     * 
     * @return routeDateEnd
     */
    public java.util.Date getRouteDateEnd() {
        return routeDateEnd;
    }


    /**
     * Sets the routeDateEnd value for this DutyPeriodCriteria.
     * 
     * @param routeDateEnd
     */
    public void setRouteDateEnd(java.util.Date routeDateEnd) {
        this.routeDateEnd = routeDateEnd;
    }


    /**
     * Gets the routeID value for this DutyPeriodCriteria.
     * 
     * @return routeID
     */
    public java.lang.String getRouteID() {
        return routeID;
    }


    /**
     * Sets the routeID value for this DutyPeriodCriteria.
     * 
     * @param routeID
     */
    public void setRouteID(java.lang.String routeID) {
        this.routeID = routeID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DutyPeriodCriteria)) return false;
        DutyPeriodCriteria other = (DutyPeriodCriteria) obj;
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
            ((this.driverRegionID==null && other.getDriverRegionID()==null) || 
             (this.driverRegionID!=null &&
              this.driverRegionID.equals(other.getDriverRegionID()))) &&
            ((this.driverID==null && other.getDriverID()==null) || 
             (this.driverID!=null &&
              this.driverID.equals(other.getDriverID()))) &&
            ((this.routeDate==null && other.getRouteDate()==null) || 
             (this.routeDate!=null &&
              this.routeDate.equals(other.getRouteDate()))) &&
            ((this.routeDateStart==null && other.getRouteDateStart()==null) || 
             (this.routeDateStart!=null &&
              this.routeDateStart.equals(other.getRouteDateStart()))) &&
            ((this.routeDateEnd==null && other.getRouteDateEnd()==null) || 
             (this.routeDateEnd!=null &&
              this.routeDateEnd.equals(other.getRouteDateEnd()))) &&
            ((this.routeID==null && other.getRouteID()==null) || 
             (this.routeID!=null &&
              this.routeID.equals(other.getRouteID())));
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
        if (getDriverRegionID() != null) {
            _hashCode += getDriverRegionID().hashCode();
        }
        if (getDriverID() != null) {
            _hashCode += getDriverID().hashCode();
        }
        if (getRouteDate() != null) {
            _hashCode += getRouteDate().hashCode();
        }
        if (getRouteDateStart() != null) {
            _hashCode += getRouteDateStart().hashCode();
        }
        if (getRouteDateEnd() != null) {
            _hashCode += getRouteDateEnd().hashCode();
        }
        if (getRouteID() != null) {
            _hashCode += getRouteID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DutyPeriodCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DutyPeriodCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverRegionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverRegionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverID"));
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
        elemField.setFieldName("routeDateStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeDateStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeDateEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeDateEnd"));
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
