/**
 * RoutingImportOrderIdentity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingImportOrderIdentity  extends com.freshdirect.routing.proxy.stub.transportation.RegionIdentity  implements java.io.Serializable {
    private int orderID;

    private int sessionID;

    private com.freshdirect.routing.proxy.stub.transportation.SessionType sessionType;

    public RoutingImportOrderIdentity() {
    }

    public RoutingImportOrderIdentity(
           java.lang.String regionID,
           int orderID,
           int sessionID,
           com.freshdirect.routing.proxy.stub.transportation.SessionType sessionType) {
        super(
            regionID);
        this.orderID = orderID;
        this.sessionID = sessionID;
        this.sessionType = sessionType;
    }


    /**
     * Gets the orderID value for this RoutingImportOrderIdentity.
     * 
     * @return orderID
     */
    public int getOrderID() {
        return orderID;
    }


    /**
     * Sets the orderID value for this RoutingImportOrderIdentity.
     * 
     * @param orderID
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }


    /**
     * Gets the sessionID value for this RoutingImportOrderIdentity.
     * 
     * @return sessionID
     */
    public int getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this RoutingImportOrderIdentity.
     * 
     * @param sessionID
     */
    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the sessionType value for this RoutingImportOrderIdentity.
     * 
     * @return sessionType
     */
    public com.freshdirect.routing.proxy.stub.transportation.SessionType getSessionType() {
        return sessionType;
    }


    /**
     * Sets the sessionType value for this RoutingImportOrderIdentity.
     * 
     * @param sessionType
     */
    public void setSessionType(com.freshdirect.routing.proxy.stub.transportation.SessionType sessionType) {
        this.sessionType = sessionType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingImportOrderIdentity)) return false;
        RoutingImportOrderIdentity other = (RoutingImportOrderIdentity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.orderID == other.getOrderID() &&
            this.sessionID == other.getSessionID() &&
            ((this.sessionType==null && other.getSessionType()==null) || 
             (this.sessionType!=null &&
              this.sessionType.equals(other.getSessionType())));
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
        _hashCode += getOrderID();
        _hashCode += getSessionID();
        if (getSessionType() != null) {
            _hashCode += getSessionType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingImportOrderIdentity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingImportOrderIdentity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "orderID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sessionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sessionType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SessionType"));
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
