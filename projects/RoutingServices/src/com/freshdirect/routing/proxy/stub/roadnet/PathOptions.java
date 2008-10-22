/**
 * PathOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class PathOptions  implements java.io.Serializable {
    private java.lang.String vendorRushHourModelName;

    private com.freshdirect.routing.proxy.stub.roadnet.MapPointDepArrTimes[] departureArrivalTimes;

    private java.lang.String arcOverrideKey;

    public PathOptions() {
    }

    public PathOptions(
           java.lang.String vendorRushHourModelName,
           com.freshdirect.routing.proxy.stub.roadnet.MapPointDepArrTimes[] departureArrivalTimes,
           java.lang.String arcOverrideKey) {
           this.vendorRushHourModelName = vendorRushHourModelName;
           this.departureArrivalTimes = departureArrivalTimes;
           this.arcOverrideKey = arcOverrideKey;
    }


    /**
     * Gets the vendorRushHourModelName value for this PathOptions.
     * 
     * @return vendorRushHourModelName
     */
    public java.lang.String getVendorRushHourModelName() {
        return vendorRushHourModelName;
    }


    /**
     * Sets the vendorRushHourModelName value for this PathOptions.
     * 
     * @param vendorRushHourModelName
     */
    public void setVendorRushHourModelName(java.lang.String vendorRushHourModelName) {
        this.vendorRushHourModelName = vendorRushHourModelName;
    }


    /**
     * Gets the departureArrivalTimes value for this PathOptions.
     * 
     * @return departureArrivalTimes
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPointDepArrTimes[] getDepartureArrivalTimes() {
        return departureArrivalTimes;
    }


    /**
     * Sets the departureArrivalTimes value for this PathOptions.
     * 
     * @param departureArrivalTimes
     */
    public void setDepartureArrivalTimes(com.freshdirect.routing.proxy.stub.roadnet.MapPointDepArrTimes[] departureArrivalTimes) {
        this.departureArrivalTimes = departureArrivalTimes;
    }

    public com.freshdirect.routing.proxy.stub.roadnet.MapPointDepArrTimes getDepartureArrivalTimes(int i) {
        return this.departureArrivalTimes[i];
    }

    public void setDepartureArrivalTimes(int i, com.freshdirect.routing.proxy.stub.roadnet.MapPointDepArrTimes _value) {
        this.departureArrivalTimes[i] = _value;
    }


    /**
     * Gets the arcOverrideKey value for this PathOptions.
     * 
     * @return arcOverrideKey
     */
    public java.lang.String getArcOverrideKey() {
        return arcOverrideKey;
    }


    /**
     * Sets the arcOverrideKey value for this PathOptions.
     * 
     * @param arcOverrideKey
     */
    public void setArcOverrideKey(java.lang.String arcOverrideKey) {
        this.arcOverrideKey = arcOverrideKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PathOptions)) return false;
        PathOptions other = (PathOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.vendorRushHourModelName==null && other.getVendorRushHourModelName()==null) || 
             (this.vendorRushHourModelName!=null &&
              this.vendorRushHourModelName.equals(other.getVendorRushHourModelName()))) &&
            ((this.departureArrivalTimes==null && other.getDepartureArrivalTimes()==null) || 
             (this.departureArrivalTimes!=null &&
              java.util.Arrays.equals(this.departureArrivalTimes, other.getDepartureArrivalTimes()))) &&
            ((this.arcOverrideKey==null && other.getArcOverrideKey()==null) || 
             (this.arcOverrideKey!=null &&
              this.arcOverrideKey.equals(other.getArcOverrideKey())));
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
        if (getVendorRushHourModelName() != null) {
            _hashCode += getVendorRushHourModelName().hashCode();
        }
        if (getDepartureArrivalTimes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDepartureArrivalTimes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDepartureArrivalTimes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getArcOverrideKey() != null) {
            _hashCode += getArcOverrideKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PathOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "PathOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorRushHourModelName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "vendorRushHourModelName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("departureArrivalTimes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "departureArrivalTimes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPointDepArrTimes"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arcOverrideKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "arcOverrideKey"));
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
