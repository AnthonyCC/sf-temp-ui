/**
 * FindMapArcPointCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class FindMapArcPointCriteria  extends com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.roadnet.MapPoint searchPt;

    private double searchRadius;

    public FindMapArcPointCriteria() {
    }

    public FindMapArcPointCriteria(
           int arcsToFind,
           com.freshdirect.routing.proxy.stub.roadnet.MapContext context,
           com.freshdirect.routing.proxy.stub.roadnet.MapPoint searchPt,
           double searchRadius) {
        super(
            arcsToFind,
            context);
        this.searchPt = searchPt;
        this.searchRadius = searchRadius;
    }


    /**
     * Gets the searchPt value for this FindMapArcPointCriteria.
     * 
     * @return searchPt
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint getSearchPt() {
        return searchPt;
    }


    /**
     * Sets the searchPt value for this FindMapArcPointCriteria.
     * 
     * @param searchPt
     */
    public void setSearchPt(com.freshdirect.routing.proxy.stub.roadnet.MapPoint searchPt) {
        this.searchPt = searchPt;
    }


    /**
     * Gets the searchRadius value for this FindMapArcPointCriteria.
     * 
     * @return searchRadius
     */
    public double getSearchRadius() {
        return searchRadius;
    }


    /**
     * Sets the searchRadius value for this FindMapArcPointCriteria.
     * 
     * @param searchRadius
     */
    public void setSearchRadius(double searchRadius) {
        this.searchRadius = searchRadius;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FindMapArcPointCriteria)) return false;
        FindMapArcPointCriteria other = (FindMapArcPointCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.searchPt==null && other.getSearchPt()==null) || 
             (this.searchPt!=null &&
              this.searchPt.equals(other.getSearchPt()))) &&
            this.searchRadius == other.getSearchRadius();
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
        if (getSearchPt() != null) {
            _hashCode += getSearchPt().hashCode();
        }
        _hashCode += new Double(getSearchRadius()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FindMapArcPointCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "FindMapArcPointCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchPt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "searchPt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPoint"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchRadius");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "searchRadius"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
