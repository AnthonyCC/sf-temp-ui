/**
 * StopSequenceInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class StopSequenceInfo  extends com.freshdirect.routing.proxy.stub.transportation.RouteEventInfo  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions;

    public StopSequenceInfo() {
    }

    public StopSequenceInfo(
           com.freshdirect.routing.proxy.stub.transportation.RouteEventSource source,
           com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity,
           com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions) {
        super(
            source);
        this.stopIdentity = stopIdentity;
        this.placementOptions = placementOptions;
    }


    /**
     * Gets the stopIdentity value for this StopSequenceInfo.
     * 
     * @return stopIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopIdentity getStopIdentity() {
        return stopIdentity;
    }


    /**
     * Sets the stopIdentity value for this StopSequenceInfo.
     * 
     * @param stopIdentity
     */
    public void setStopIdentity(com.freshdirect.routing.proxy.stub.transportation.StopIdentity stopIdentity) {
        this.stopIdentity = stopIdentity;
    }


    /**
     * Gets the placementOptions value for this StopSequenceInfo.
     * 
     * @return placementOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions getPlacementOptions() {
        return placementOptions;
    }


    /**
     * Sets the placementOptions value for this StopSequenceInfo.
     * 
     * @param placementOptions
     */
    public void setPlacementOptions(com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions) {
        this.placementOptions = placementOptions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StopSequenceInfo)) return false;
        StopSequenceInfo other = (StopSequenceInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.stopIdentity==null && other.getStopIdentity()==null) || 
             (this.stopIdentity!=null &&
              this.stopIdentity.equals(other.getStopIdentity()))) &&
            ((this.placementOptions==null && other.getPlacementOptions()==null) || 
             (this.placementOptions!=null &&
              this.placementOptions.equals(other.getPlacementOptions())));
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
        if (getStopIdentity() != null) {
            _hashCode += getStopIdentity().hashCode();
        }
        if (getPlacementOptions() != null) {
            _hashCode += getPlacementOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StopSequenceInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopSequenceInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stopIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "stopIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placementOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "placementOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopPlacementOptions"));
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
