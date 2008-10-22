/**
 * ChangedOrderFinancialDetailIdentity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class ChangedOrderFinancialDetailIdentity  extends com.freshdirect.routing.proxy.stub.transportation.OrderIdentity  implements java.io.Serializable {
    private int internalFinancialDetailID;

    private com.freshdirect.routing.proxy.stub.transportation.UpdateType changeType;

    public ChangedOrderFinancialDetailIdentity() {
    }

    public ChangedOrderFinancialDetailIdentity(
           java.lang.String regionID,
           java.util.Date routeDate,
           java.lang.String routeID,
           int internalStopID,
           java.lang.String orderNumber,
           int internalFinancialDetailID,
           com.freshdirect.routing.proxy.stub.transportation.UpdateType changeType) {
        super(
            regionID,
            routeDate,
            routeID,
            internalStopID,
            orderNumber);
        this.internalFinancialDetailID = internalFinancialDetailID;
        this.changeType = changeType;
    }


    /**
     * Gets the internalFinancialDetailID value for this ChangedOrderFinancialDetailIdentity.
     * 
     * @return internalFinancialDetailID
     */
    public int getInternalFinancialDetailID() {
        return internalFinancialDetailID;
    }


    /**
     * Sets the internalFinancialDetailID value for this ChangedOrderFinancialDetailIdentity.
     * 
     * @param internalFinancialDetailID
     */
    public void setInternalFinancialDetailID(int internalFinancialDetailID) {
        this.internalFinancialDetailID = internalFinancialDetailID;
    }


    /**
     * Gets the changeType value for this ChangedOrderFinancialDetailIdentity.
     * 
     * @return changeType
     */
    public com.freshdirect.routing.proxy.stub.transportation.UpdateType getChangeType() {
        return changeType;
    }


    /**
     * Sets the changeType value for this ChangedOrderFinancialDetailIdentity.
     * 
     * @param changeType
     */
    public void setChangeType(com.freshdirect.routing.proxy.stub.transportation.UpdateType changeType) {
        this.changeType = changeType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ChangedOrderFinancialDetailIdentity)) return false;
        ChangedOrderFinancialDetailIdentity other = (ChangedOrderFinancialDetailIdentity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.internalFinancialDetailID == other.getInternalFinancialDetailID() &&
            ((this.changeType==null && other.getChangeType()==null) || 
             (this.changeType!=null &&
              this.changeType.equals(other.getChangeType())));
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
        _hashCode += getInternalFinancialDetailID();
        if (getChangeType() != null) {
            _hashCode += getChangeType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ChangedOrderFinancialDetailIdentity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ChangedOrderFinancialDetailIdentity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("internalFinancialDetailID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "internalFinancialDetailID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changeType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "changeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "UpdateType"));
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
