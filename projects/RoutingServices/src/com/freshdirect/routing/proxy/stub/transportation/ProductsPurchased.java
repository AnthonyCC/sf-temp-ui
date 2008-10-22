/**
 * ProductsPurchased.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class ProductsPurchased  implements java.io.Serializable {
    private boolean roadnet5000;

    private boolean territoryPlanner;

    private boolean fleetLoader;

    private boolean mobileCast;

    private boolean RIC;

    private boolean netMap;

    private boolean netScheduler;

    public ProductsPurchased() {
    }

    public ProductsPurchased(
           boolean roadnet5000,
           boolean territoryPlanner,
           boolean fleetLoader,
           boolean mobileCast,
           boolean RIC,
           boolean netMap,
           boolean netScheduler) {
           this.roadnet5000 = roadnet5000;
           this.territoryPlanner = territoryPlanner;
           this.fleetLoader = fleetLoader;
           this.mobileCast = mobileCast;
           this.RIC = RIC;
           this.netMap = netMap;
           this.netScheduler = netScheduler;
    }


    /**
     * Gets the roadnet5000 value for this ProductsPurchased.
     * 
     * @return roadnet5000
     */
    public boolean isRoadnet5000() {
        return roadnet5000;
    }


    /**
     * Sets the roadnet5000 value for this ProductsPurchased.
     * 
     * @param roadnet5000
     */
    public void setRoadnet5000(boolean roadnet5000) {
        this.roadnet5000 = roadnet5000;
    }


    /**
     * Gets the territoryPlanner value for this ProductsPurchased.
     * 
     * @return territoryPlanner
     */
    public boolean isTerritoryPlanner() {
        return territoryPlanner;
    }


    /**
     * Sets the territoryPlanner value for this ProductsPurchased.
     * 
     * @param territoryPlanner
     */
    public void setTerritoryPlanner(boolean territoryPlanner) {
        this.territoryPlanner = territoryPlanner;
    }


    /**
     * Gets the fleetLoader value for this ProductsPurchased.
     * 
     * @return fleetLoader
     */
    public boolean isFleetLoader() {
        return fleetLoader;
    }


    /**
     * Sets the fleetLoader value for this ProductsPurchased.
     * 
     * @param fleetLoader
     */
    public void setFleetLoader(boolean fleetLoader) {
        this.fleetLoader = fleetLoader;
    }


    /**
     * Gets the mobileCast value for this ProductsPurchased.
     * 
     * @return mobileCast
     */
    public boolean isMobileCast() {
        return mobileCast;
    }


    /**
     * Sets the mobileCast value for this ProductsPurchased.
     * 
     * @param mobileCast
     */
    public void setMobileCast(boolean mobileCast) {
        this.mobileCast = mobileCast;
    }


    /**
     * Gets the RIC value for this ProductsPurchased.
     * 
     * @return RIC
     */
    public boolean isRIC() {
        return RIC;
    }


    /**
     * Sets the RIC value for this ProductsPurchased.
     * 
     * @param RIC
     */
    public void setRIC(boolean RIC) {
        this.RIC = RIC;
    }


    /**
     * Gets the netMap value for this ProductsPurchased.
     * 
     * @return netMap
     */
    public boolean isNetMap() {
        return netMap;
    }


    /**
     * Sets the netMap value for this ProductsPurchased.
     * 
     * @param netMap
     */
    public void setNetMap(boolean netMap) {
        this.netMap = netMap;
    }


    /**
     * Gets the netScheduler value for this ProductsPurchased.
     * 
     * @return netScheduler
     */
    public boolean isNetScheduler() {
        return netScheduler;
    }


    /**
     * Sets the netScheduler value for this ProductsPurchased.
     * 
     * @param netScheduler
     */
    public void setNetScheduler(boolean netScheduler) {
        this.netScheduler = netScheduler;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProductsPurchased)) return false;
        ProductsPurchased other = (ProductsPurchased) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.roadnet5000 == other.isRoadnet5000() &&
            this.territoryPlanner == other.isTerritoryPlanner() &&
            this.fleetLoader == other.isFleetLoader() &&
            this.mobileCast == other.isMobileCast() &&
            this.RIC == other.isRIC() &&
            this.netMap == other.isNetMap() &&
            this.netScheduler == other.isNetScheduler();
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
        _hashCode += (isRoadnet5000() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isTerritoryPlanner() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFleetLoader() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMobileCast() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRIC() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isNetMap() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isNetScheduler() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProductsPurchased.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ProductsPurchased"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roadnet5000");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "Roadnet5000"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("territoryPlanner");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TerritoryPlanner"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fleetLoader");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "FleetLoader"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileCast");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "MobileCast"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RIC");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RIC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("netMap");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NetMap"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("netScheduler");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NetScheduler"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
