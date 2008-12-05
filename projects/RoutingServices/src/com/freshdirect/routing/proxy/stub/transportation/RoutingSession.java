/**
 * RoutingSession.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingSession  extends com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] routes;

    public RoutingSession() {
    }

    public RoutingSession(
           java.lang.String description,
           java.lang.String scenario,
           java.util.Date sessionDate,
           boolean combineOrders,
           boolean applyRoadRestrictions,
           boolean hoursOfServiceAware,
           com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] routes) {
        super(
            description,
            scenario,
            sessionDate,
            combineOrders,
            applyRoadRestrictions,
            hoursOfServiceAware);
        this.sessionIdentity = sessionIdentity;
        this.routes = routes;
    }


    /**
     * Gets the sessionIdentity value for this RoutingSession.
     * 
     * @return sessionIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity getSessionIdentity() {
        return sessionIdentity;
    }


    /**
     * Sets the sessionIdentity value for this RoutingSession.
     * 
     * @param sessionIdentity
     */
    public void setSessionIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity) {
        this.sessionIdentity = sessionIdentity;
    }


    /**
     * Gets the routes value for this RoutingSession.
     * 
     * @return routes
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] getRoutes() {
        return routes;
    }


    /**
     * Sets the routes value for this RoutingSession.
     * 
     * @param routes
     */
    public void setRoutes(com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] routes) {
        this.routes = routes;
    }

    public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute getRoutes(int i) {
        return this.routes[i];
    }

    public void setRoutes(int i, com.freshdirect.routing.proxy.stub.transportation.RoutingRoute _value) {
        this.routes[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingSession)) return false;
        RoutingSession other = (RoutingSession) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.sessionIdentity==null && other.getSessionIdentity()==null) || 
             (this.sessionIdentity!=null &&
              this.sessionIdentity.equals(other.getSessionIdentity()))) &&
            ((this.routes==null && other.getRoutes()==null) || 
             (this.routes!=null &&
              java.util.Arrays.equals(this.routes, other.getRoutes())));
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
        if (getSessionIdentity() != null) {
            _hashCode += getSessionIdentity().hashCode();
        }
        if (getRoutes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRoutes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRoutes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingSession.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingSession"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "sessionIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingSessionIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingRoute"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
