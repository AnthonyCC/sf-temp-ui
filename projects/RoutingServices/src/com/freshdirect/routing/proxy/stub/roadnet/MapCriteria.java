/**
 * MapCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapCriteria  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.roadnet.MapContext context;

    private com.freshdirect.routing.proxy.stub.roadnet.MapObject[] drawItems;

    public MapCriteria() {
    }

    public MapCriteria(
           com.freshdirect.routing.proxy.stub.roadnet.MapContext context,
           com.freshdirect.routing.proxy.stub.roadnet.MapObject[] drawItems) {
           this.context = context;
           this.drawItems = drawItems;
    }


    /**
     * Gets the context value for this MapCriteria.
     * 
     * @return context
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapContext getContext() {
        return context;
    }


    /**
     * Sets the context value for this MapCriteria.
     * 
     * @param context
     */
    public void setContext(com.freshdirect.routing.proxy.stub.roadnet.MapContext context) {
        this.context = context;
    }


    /**
     * Gets the drawItems value for this MapCriteria.
     * 
     * @return drawItems
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapObject[] getDrawItems() {
        return drawItems;
    }


    /**
     * Sets the drawItems value for this MapCriteria.
     * 
     * @param drawItems
     */
    public void setDrawItems(com.freshdirect.routing.proxy.stub.roadnet.MapObject[] drawItems) {
        this.drawItems = drawItems;
    }

    public com.freshdirect.routing.proxy.stub.roadnet.MapObject getDrawItems(int i) {
        return this.drawItems[i];
    }

    public void setDrawItems(int i, com.freshdirect.routing.proxy.stub.roadnet.MapObject _value) {
        this.drawItems[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapCriteria)) return false;
        MapCriteria other = (MapCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.context==null && other.getContext()==null) || 
             (this.context!=null &&
              this.context.equals(other.getContext()))) &&
            ((this.drawItems==null && other.getDrawItems()==null) || 
             (this.drawItems!=null &&
              java.util.Arrays.equals(this.drawItems, other.getDrawItems())));
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
        if (getContext() != null) {
            _hashCode += getContext().hashCode();
        }
        if (getDrawItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDrawItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDrawItems(), i);
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
        new org.apache.axis.description.TypeDesc(MapCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("context");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "context"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapContext"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("drawItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "drawItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapObject"));
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
