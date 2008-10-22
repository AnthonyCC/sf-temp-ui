/**
 * RoutingRouteCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingRouteCriteria  implements java.io.Serializable {
    private java.lang.String regionIdentity;

    private int internalSessionID;

    private java.lang.String routeID;

    private java.util.Date dateStart;

    private java.util.Date dateEnd;

    public RoutingRouteCriteria() {
    }

    public RoutingRouteCriteria(
           java.lang.String regionIdentity,
           int internalSessionID,
           java.lang.String routeID,
           java.util.Date dateStart,
           java.util.Date dateEnd) {
           this.regionIdentity = regionIdentity;
           this.internalSessionID = internalSessionID;
           this.routeID = routeID;
           this.dateStart = dateStart;
           this.dateEnd = dateEnd;
    }


    /**
     * Gets the regionIdentity value for this RoutingRouteCriteria.
     * 
     * @return regionIdentity
     */
    public java.lang.String getRegionIdentity() {
        return regionIdentity;
    }


    /**
     * Sets the regionIdentity value for this RoutingRouteCriteria.
     * 
     * @param regionIdentity
     */
    public void setRegionIdentity(java.lang.String regionIdentity) {
        this.regionIdentity = regionIdentity;
    }


    /**
     * Gets the internalSessionID value for this RoutingRouteCriteria.
     * 
     * @return internalSessionID
     */
    public int getInternalSessionID() {
        return internalSessionID;
    }


    /**
     * Sets the internalSessionID value for this RoutingRouteCriteria.
     * 
     * @param internalSessionID
     */
    public void setInternalSessionID(int internalSessionID) {
        this.internalSessionID = internalSessionID;
    }


    /**
     * Gets the routeID value for this RoutingRouteCriteria.
     * 
     * @return routeID
     */
    public java.lang.String getRouteID() {
        return routeID;
    }


    /**
     * Sets the routeID value for this RoutingRouteCriteria.
     * 
     * @param routeID
     */
    public void setRouteID(java.lang.String routeID) {
        this.routeID = routeID;
    }


    /**
     * Gets the dateStart value for this RoutingRouteCriteria.
     * 
     * @return dateStart
     */
    public java.util.Date getDateStart() {
        return dateStart;
    }


    /**
     * Sets the dateStart value for this RoutingRouteCriteria.
     * 
     * @param dateStart
     */
    public void setDateStart(java.util.Date dateStart) {
        this.dateStart = dateStart;
    }


    /**
     * Gets the dateEnd value for this RoutingRouteCriteria.
     * 
     * @return dateEnd
     */
    public java.util.Date getDateEnd() {
        return dateEnd;
    }


    /**
     * Sets the dateEnd value for this RoutingRouteCriteria.
     * 
     * @param dateEnd
     */
    public void setDateEnd(java.util.Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingRouteCriteria)) return false;
        RoutingRouteCriteria other = (RoutingRouteCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.regionIdentity==null && other.getRegionIdentity()==null) || 
             (this.regionIdentity!=null &&
              this.regionIdentity.equals(other.getRegionIdentity()))) &&
            this.internalSessionID == other.getInternalSessionID() &&
            ((this.routeID==null && other.getRouteID()==null) || 
             (this.routeID!=null &&
              this.routeID.equals(other.getRouteID()))) &&
            ((this.dateStart==null && other.getDateStart()==null) || 
             (this.dateStart!=null &&
              this.dateStart.equals(other.getDateStart()))) &&
            ((this.dateEnd==null && other.getDateEnd()==null) || 
             (this.dateEnd!=null &&
              this.dateEnd.equals(other.getDateEnd())));
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
        if (getRegionIdentity() != null) {
            _hashCode += getRegionIdentity().hashCode();
        }
        _hashCode += getInternalSessionID();
        if (getRouteID() != null) {
            _hashCode += getRouteID().hashCode();
        }
        if (getDateStart() != null) {
            _hashCode += getDateStart().hashCode();
        }
        if (getDateEnd() != null) {
            _hashCode += getDateEnd().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingRouteCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regionIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("internalSessionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "internalSessionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("dateStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
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
