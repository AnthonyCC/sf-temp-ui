/**
 * PositionHistoryIdentity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class PositionHistoryIdentity  implements java.io.Serializable {
    private int internalPositionHistoryID;

    private java.util.Calendar positionTime;

    public PositionHistoryIdentity() {
    }

    public PositionHistoryIdentity(
           int internalPositionHistoryID,
           java.util.Calendar positionTime) {
           this.internalPositionHistoryID = internalPositionHistoryID;
           this.positionTime = positionTime;
    }


    /**
     * Gets the internalPositionHistoryID value for this PositionHistoryIdentity.
     * 
     * @return internalPositionHistoryID
     */
    public int getInternalPositionHistoryID() {
        return internalPositionHistoryID;
    }


    /**
     * Sets the internalPositionHistoryID value for this PositionHistoryIdentity.
     * 
     * @param internalPositionHistoryID
     */
    public void setInternalPositionHistoryID(int internalPositionHistoryID) {
        this.internalPositionHistoryID = internalPositionHistoryID;
    }


    /**
     * Gets the positionTime value for this PositionHistoryIdentity.
     * 
     * @return positionTime
     */
    public java.util.Calendar getPositionTime() {
        return positionTime;
    }


    /**
     * Sets the positionTime value for this PositionHistoryIdentity.
     * 
     * @param positionTime
     */
    public void setPositionTime(java.util.Calendar positionTime) {
        this.positionTime = positionTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PositionHistoryIdentity)) return false;
        PositionHistoryIdentity other = (PositionHistoryIdentity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.internalPositionHistoryID == other.getInternalPositionHistoryID() &&
            ((this.positionTime==null && other.getPositionTime()==null) || 
             (this.positionTime!=null &&
              this.positionTime.equals(other.getPositionTime())));
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
        _hashCode += getInternalPositionHistoryID();
        if (getPositionTime() != null) {
            _hashCode += getPositionTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PositionHistoryIdentity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "PositionHistoryIdentity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("internalPositionHistoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "internalPositionHistoryID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
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
