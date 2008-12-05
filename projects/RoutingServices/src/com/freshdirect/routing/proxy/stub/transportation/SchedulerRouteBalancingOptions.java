/**
 * SchedulerRouteBalancingOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerRouteBalancingOptions  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.SchedulerBalancingFactor balanceBy;

    private double costVsBalanceWeight;

    private com.freshdirect.routing.proxy.stub.transportation.DynamicWindowInfo dynamicWindowInfo;

    public SchedulerRouteBalancingOptions() {
    }

    public SchedulerRouteBalancingOptions(
           com.freshdirect.routing.proxy.stub.transportation.SchedulerBalancingFactor balanceBy,
           double costVsBalanceWeight,
           com.freshdirect.routing.proxy.stub.transportation.DynamicWindowInfo dynamicWindowInfo) {
           this.balanceBy = balanceBy;
           this.costVsBalanceWeight = costVsBalanceWeight;
           this.dynamicWindowInfo = dynamicWindowInfo;
    }


    /**
     * Gets the balanceBy value for this SchedulerRouteBalancingOptions.
     * 
     * @return balanceBy
     */
    public com.freshdirect.routing.proxy.stub.transportation.SchedulerBalancingFactor getBalanceBy() {
        return balanceBy;
    }


    /**
     * Sets the balanceBy value for this SchedulerRouteBalancingOptions.
     * 
     * @param balanceBy
     */
    public void setBalanceBy(com.freshdirect.routing.proxy.stub.transportation.SchedulerBalancingFactor balanceBy) {
        this.balanceBy = balanceBy;
    }


    /**
     * Gets the costVsBalanceWeight value for this SchedulerRouteBalancingOptions.
     * 
     * @return costVsBalanceWeight
     */
    public double getCostVsBalanceWeight() {
        return costVsBalanceWeight;
    }


    /**
     * Sets the costVsBalanceWeight value for this SchedulerRouteBalancingOptions.
     * 
     * @param costVsBalanceWeight
     */
    public void setCostVsBalanceWeight(double costVsBalanceWeight) {
        this.costVsBalanceWeight = costVsBalanceWeight;
    }


    /**
     * Gets the dynamicWindowInfo value for this SchedulerRouteBalancingOptions.
     * 
     * @return dynamicWindowInfo
     */
    public com.freshdirect.routing.proxy.stub.transportation.DynamicWindowInfo getDynamicWindowInfo() {
        return dynamicWindowInfo;
    }


    /**
     * Sets the dynamicWindowInfo value for this SchedulerRouteBalancingOptions.
     * 
     * @param dynamicWindowInfo
     */
    public void setDynamicWindowInfo(com.freshdirect.routing.proxy.stub.transportation.DynamicWindowInfo dynamicWindowInfo) {
        this.dynamicWindowInfo = dynamicWindowInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerRouteBalancingOptions)) return false;
        SchedulerRouteBalancingOptions other = (SchedulerRouteBalancingOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.balanceBy==null && other.getBalanceBy()==null) || 
             (this.balanceBy!=null &&
              this.balanceBy.equals(other.getBalanceBy()))) &&
            this.costVsBalanceWeight == other.getCostVsBalanceWeight() &&
            ((this.dynamicWindowInfo==null && other.getDynamicWindowInfo()==null) || 
             (this.dynamicWindowInfo!=null &&
              this.dynamicWindowInfo.equals(other.getDynamicWindowInfo())));
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
        if (getBalanceBy() != null) {
            _hashCode += getBalanceBy().hashCode();
        }
        _hashCode += new Double(getCostVsBalanceWeight()).hashCode();
        if (getDynamicWindowInfo() != null) {
            _hashCode += getDynamicWindowInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerRouteBalancingOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerRouteBalancingOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balanceBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "balanceBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerBalancingFactor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("costVsBalanceWeight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "costVsBalanceWeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dynamicWindowInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dynamicWindowInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DynamicWindowInfo"));
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
