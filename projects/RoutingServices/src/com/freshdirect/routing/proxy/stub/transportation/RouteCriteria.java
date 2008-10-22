/**
 * RouteCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RouteCriteria  implements java.io.Serializable {
    private java.lang.String regionID;

    private java.util.Date routeDate;

    private java.lang.String routeID;

    private java.lang.Integer internalRouteID;

    private java.lang.String group;

    private java.lang.String gpsUnitID;

    private java.lang.Boolean completed;

    private com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driverIdentity;

    public RouteCriteria() {
    }

    public RouteCriteria(
           java.lang.String regionID,
           java.util.Date routeDate,
           java.lang.String routeID,
           java.lang.Integer internalRouteID,
           java.lang.String group,
           java.lang.String gpsUnitID,
           java.lang.Boolean completed,
           com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driverIdentity) {
           this.regionID = regionID;
           this.routeDate = routeDate;
           this.routeID = routeID;
           this.internalRouteID = internalRouteID;
           this.group = group;
           this.gpsUnitID = gpsUnitID;
           this.completed = completed;
           this.driverIdentity = driverIdentity;
    }


    /**
     * Gets the regionID value for this RouteCriteria.
     * 
     * @return regionID
     */
    public java.lang.String getRegionID() {
        return regionID;
    }


    /**
     * Sets the regionID value for this RouteCriteria.
     * 
     * @param regionID
     */
    public void setRegionID(java.lang.String regionID) {
        this.regionID = regionID;
    }


    /**
     * Gets the routeDate value for this RouteCriteria.
     * 
     * @return routeDate
     */
    public java.util.Date getRouteDate() {
        return routeDate;
    }


    /**
     * Sets the routeDate value for this RouteCriteria.
     * 
     * @param routeDate
     */
    public void setRouteDate(java.util.Date routeDate) {
        this.routeDate = routeDate;
    }


    /**
     * Gets the routeID value for this RouteCriteria.
     * 
     * @return routeID
     */
    public java.lang.String getRouteID() {
        return routeID;
    }


    /**
     * Sets the routeID value for this RouteCriteria.
     * 
     * @param routeID
     */
    public void setRouteID(java.lang.String routeID) {
        this.routeID = routeID;
    }


    /**
     * Gets the internalRouteID value for this RouteCriteria.
     * 
     * @return internalRouteID
     */
    public java.lang.Integer getInternalRouteID() {
        return internalRouteID;
    }


    /**
     * Sets the internalRouteID value for this RouteCriteria.
     * 
     * @param internalRouteID
     */
    public void setInternalRouteID(java.lang.Integer internalRouteID) {
        this.internalRouteID = internalRouteID;
    }


    /**
     * Gets the group value for this RouteCriteria.
     * 
     * @return group
     */
    public java.lang.String getGroup() {
        return group;
    }


    /**
     * Sets the group value for this RouteCriteria.
     * 
     * @param group
     */
    public void setGroup(java.lang.String group) {
        this.group = group;
    }


    /**
     * Gets the gpsUnitID value for this RouteCriteria.
     * 
     * @return gpsUnitID
     */
    public java.lang.String getGpsUnitID() {
        return gpsUnitID;
    }


    /**
     * Sets the gpsUnitID value for this RouteCriteria.
     * 
     * @param gpsUnitID
     */
    public void setGpsUnitID(java.lang.String gpsUnitID) {
        this.gpsUnitID = gpsUnitID;
    }


    /**
     * Gets the completed value for this RouteCriteria.
     * 
     * @return completed
     */
    public java.lang.Boolean getCompleted() {
        return completed;
    }


    /**
     * Sets the completed value for this RouteCriteria.
     * 
     * @param completed
     */
    public void setCompleted(java.lang.Boolean completed) {
        this.completed = completed;
    }


    /**
     * Gets the driverIdentity value for this RouteCriteria.
     * 
     * @return driverIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity getDriverIdentity() {
        return driverIdentity;
    }


    /**
     * Sets the driverIdentity value for this RouteCriteria.
     * 
     * @param driverIdentity
     */
    public void setDriverIdentity(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity driverIdentity) {
        this.driverIdentity = driverIdentity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RouteCriteria)) return false;
        RouteCriteria other = (RouteCriteria) obj;
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
            ((this.internalRouteID==null && other.getInternalRouteID()==null) || 
             (this.internalRouteID!=null &&
              this.internalRouteID.equals(other.getInternalRouteID()))) &&
            ((this.group==null && other.getGroup()==null) || 
             (this.group!=null &&
              this.group.equals(other.getGroup()))) &&
            ((this.gpsUnitID==null && other.getGpsUnitID()==null) || 
             (this.gpsUnitID!=null &&
              this.gpsUnitID.equals(other.getGpsUnitID()))) &&
            ((this.completed==null && other.getCompleted()==null) || 
             (this.completed!=null &&
              this.completed.equals(other.getCompleted()))) &&
            ((this.driverIdentity==null && other.getDriverIdentity()==null) || 
             (this.driverIdentity!=null &&
              this.driverIdentity.equals(other.getDriverIdentity())));
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
        if (getInternalRouteID() != null) {
            _hashCode += getInternalRouteID().hashCode();
        }
        if (getGroup() != null) {
            _hashCode += getGroup().hashCode();
        }
        if (getGpsUnitID() != null) {
            _hashCode += getGpsUnitID().hashCode();
        }
        if (getCompleted() != null) {
            _hashCode += getCompleted().hashCode();
        }
        if (getDriverIdentity() != null) {
            _hashCode += getDriverIdentity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RouteCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteCriteria"));
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
        elemField.setFieldName("internalRouteID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "internalRouteID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "group"));
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
        elemField.setFieldName("completed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "completed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("driverIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "driverIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EmployeeIdentity"));
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
