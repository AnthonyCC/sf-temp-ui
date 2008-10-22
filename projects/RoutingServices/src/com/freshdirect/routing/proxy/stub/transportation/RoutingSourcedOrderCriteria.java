/**
 * RoutingSourcedOrderCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingSourcedOrderCriteria  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity;

    public RoutingSourcedOrderCriteria() {
    }

    public RoutingSourcedOrderCriteria(
           com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity) {
           this.sessionIdentity = sessionIdentity;
           this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the sessionIdentity value for this RoutingSourcedOrderCriteria.
     * 
     * @return sessionIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity getSessionIdentity() {
        return sessionIdentity;
    }


    /**
     * Sets the sessionIdentity value for this RoutingSourcedOrderCriteria.
     * 
     * @param sessionIdentity
     */
    public void setSessionIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity) {
        this.sessionIdentity = sessionIdentity;
    }


    /**
     * Gets the routeIdentity value for this RoutingSourcedOrderCriteria.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this RoutingSourcedOrderCriteria.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingSourcedOrderCriteria)) return false;
        RoutingSourcedOrderCriteria other = (RoutingSourcedOrderCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sessionIdentity==null && other.getSessionIdentity()==null) || 
             (this.sessionIdentity!=null &&
              this.sessionIdentity.equals(other.getSessionIdentity()))) &&
            ((this.routeIdentity==null && other.getRouteIdentity()==null) || 
             (this.routeIdentity!=null &&
              this.routeIdentity.equals(other.getRouteIdentity())));
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
        if (getSessionIdentity() != null) {
            _hashCode += getSessionIdentity().hashCode();
        }
        if (getRouteIdentity() != null) {
            _hashCode += getRouteIdentity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingSourcedOrderCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingSourcedOrderCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sessionIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingSessionIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRouteIdentity"));
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
