/**
 * RouteNoteCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RouteNoteCriteria  implements java.io.Serializable {
    private java.lang.String fromUserID;

    private java.lang.String regionID;

    private java.util.Date routeDate;

    private java.lang.String routeID;

    private java.lang.Boolean isError;

    private java.lang.Boolean acknowledged;

    public RouteNoteCriteria() {
    }

    public RouteNoteCriteria(
           java.lang.String fromUserID,
           java.lang.String regionID,
           java.util.Date routeDate,
           java.lang.String routeID,
           java.lang.Boolean isError,
           java.lang.Boolean acknowledged) {
           this.fromUserID = fromUserID;
           this.regionID = regionID;
           this.routeDate = routeDate;
           this.routeID = routeID;
           this.isError = isError;
           this.acknowledged = acknowledged;
    }


    /**
     * Gets the fromUserID value for this RouteNoteCriteria.
     * 
     * @return fromUserID
     */
    public java.lang.String getFromUserID() {
        return fromUserID;
    }


    /**
     * Sets the fromUserID value for this RouteNoteCriteria.
     * 
     * @param fromUserID
     */
    public void setFromUserID(java.lang.String fromUserID) {
        this.fromUserID = fromUserID;
    }


    /**
     * Gets the regionID value for this RouteNoteCriteria.
     * 
     * @return regionID
     */
    public java.lang.String getRegionID() {
        return regionID;
    }


    /**
     * Sets the regionID value for this RouteNoteCriteria.
     * 
     * @param regionID
     */
    public void setRegionID(java.lang.String regionID) {
        this.regionID = regionID;
    }


    /**
     * Gets the routeDate value for this RouteNoteCriteria.
     * 
     * @return routeDate
     */
    public java.util.Date getRouteDate() {
        return routeDate;
    }


    /**
     * Sets the routeDate value for this RouteNoteCriteria.
     * 
     * @param routeDate
     */
    public void setRouteDate(java.util.Date routeDate) {
        this.routeDate = routeDate;
    }


    /**
     * Gets the routeID value for this RouteNoteCriteria.
     * 
     * @return routeID
     */
    public java.lang.String getRouteID() {
        return routeID;
    }


    /**
     * Sets the routeID value for this RouteNoteCriteria.
     * 
     * @param routeID
     */
    public void setRouteID(java.lang.String routeID) {
        this.routeID = routeID;
    }


    /**
     * Gets the isError value for this RouteNoteCriteria.
     * 
     * @return isError
     */
    public java.lang.Boolean getIsError() {
        return isError;
    }


    /**
     * Sets the isError value for this RouteNoteCriteria.
     * 
     * @param isError
     */
    public void setIsError(java.lang.Boolean isError) {
        this.isError = isError;
    }


    /**
     * Gets the acknowledged value for this RouteNoteCriteria.
     * 
     * @return acknowledged
     */
    public java.lang.Boolean getAcknowledged() {
        return acknowledged;
    }


    /**
     * Sets the acknowledged value for this RouteNoteCriteria.
     * 
     * @param acknowledged
     */
    public void setAcknowledged(java.lang.Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RouteNoteCriteria)) return false;
        RouteNoteCriteria other = (RouteNoteCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fromUserID==null && other.getFromUserID()==null) || 
             (this.fromUserID!=null &&
              this.fromUserID.equals(other.getFromUserID()))) &&
            ((this.regionID==null && other.getRegionID()==null) || 
             (this.regionID!=null &&
              this.regionID.equals(other.getRegionID()))) &&
            ((this.routeDate==null && other.getRouteDate()==null) || 
             (this.routeDate!=null &&
              this.routeDate.equals(other.getRouteDate()))) &&
            ((this.routeID==null && other.getRouteID()==null) || 
             (this.routeID!=null &&
              this.routeID.equals(other.getRouteID()))) &&
            ((this.isError==null && other.getIsError()==null) || 
             (this.isError!=null &&
              this.isError.equals(other.getIsError()))) &&
            ((this.acknowledged==null && other.getAcknowledged()==null) || 
             (this.acknowledged!=null &&
              this.acknowledged.equals(other.getAcknowledged())));
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
        if (getFromUserID() != null) {
            _hashCode += getFromUserID().hashCode();
        }
        if (getRegionID() != null) {
            _hashCode += getRegionID().hashCode();
        }
        if (getRouteDate() != null) {
            _hashCode += getRouteDate().hashCode();
        }
        if (getRouteID() != null) {
            _hashCode += getRouteID().hashCode();
        }
        if (getIsError() != null) {
            _hashCode += getIsError().hashCode();
        }
        if (getAcknowledged() != null) {
            _hashCode += getAcknowledged().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RouteNoteCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteNoteCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fromUserID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "fromUserID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("isError");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "isError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acknowledged");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "acknowledged"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
