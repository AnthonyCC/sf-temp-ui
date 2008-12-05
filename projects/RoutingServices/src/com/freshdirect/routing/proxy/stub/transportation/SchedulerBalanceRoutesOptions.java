/**
 * SchedulerBalanceRoutesOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerBalanceRoutesOptions  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.SchedulerRouteBalancingOptions balancingOptions;

    public SchedulerBalanceRoutesOptions() {
    }

    public SchedulerBalanceRoutesOptions(
           com.freshdirect.routing.proxy.stub.transportation.SchedulerRouteBalancingOptions balancingOptions) {
           this.balancingOptions = balancingOptions;
    }


    /**
     * Gets the balancingOptions value for this SchedulerBalanceRoutesOptions.
     * 
     * @return balancingOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.SchedulerRouteBalancingOptions getBalancingOptions() {
        return balancingOptions;
    }


    /**
     * Sets the balancingOptions value for this SchedulerBalanceRoutesOptions.
     * 
     * @param balancingOptions
     */
    public void setBalancingOptions(com.freshdirect.routing.proxy.stub.transportation.SchedulerRouteBalancingOptions balancingOptions) {
        this.balancingOptions = balancingOptions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerBalanceRoutesOptions)) return false;
        SchedulerBalanceRoutesOptions other = (SchedulerBalanceRoutesOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.balancingOptions==null && other.getBalancingOptions()==null) || 
             (this.balancingOptions!=null &&
              this.balancingOptions.equals(other.getBalancingOptions())));
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
        if (getBalancingOptions() != null) {
            _hashCode += getBalancingOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerBalanceRoutesOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerBalanceRoutesOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balancingOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "balancingOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerRouteBalancingOptions"));
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
