/**
 * StopPlacementOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class StopPlacementOptions  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.StopPlacementType placementType;

    private com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity;

    public StopPlacementOptions() {
    }

    public StopPlacementOptions(
           com.freshdirect.routing.proxy.stub.transportation.StopPlacementType placementType,
           com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity) {
           this.placementType = placementType;
           this.stopIdentity = stopIdentity;
    }


    /**
     * Gets the placementType value for this StopPlacementOptions.
     * 
     * @return placementType
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopPlacementType getPlacementType() {
        return placementType;
    }


    /**
     * Sets the placementType value for this StopPlacementOptions.
     * 
     * @param placementType
     */
    public void setPlacementType(com.freshdirect.routing.proxy.stub.transportation.StopPlacementType placementType) {
        this.placementType = placementType;
    }


    /**
     * Gets the stopIdentity value for this StopPlacementOptions.
     * 
     * @return stopIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopIdentity getStopIdentity() {
        return stopIdentity;
    }


    /**
     * Sets the stopIdentity value for this StopPlacementOptions.
     * 
     * @param stopIdentity
     */
    public void setStopIdentity(com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity) {
        this.stopIdentity = stopIdentity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StopPlacementOptions)) return false;
        StopPlacementOptions other = (StopPlacementOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.placementType==null && other.getPlacementType()==null) || 
             (this.placementType!=null &&
              this.placementType.equals(other.getPlacementType()))) &&
            ((this.stopIdentity==null && other.getStopIdentity()==null) || 
             (this.stopIdentity!=null &&
              this.stopIdentity.equals(other.getStopIdentity())));
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
        if (getPlacementType() != null) {
            _hashCode += getPlacementType().hashCode();
        }
        if (getStopIdentity() != null) {
            _hashCode += getStopIdentity().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StopPlacementOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopPlacementOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placementType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "placementType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopPlacementType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopIdentity"));
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
