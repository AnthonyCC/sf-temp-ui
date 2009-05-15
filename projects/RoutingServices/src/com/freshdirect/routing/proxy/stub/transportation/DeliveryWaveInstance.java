/**
 * DeliveryWaveInstance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DeliveryWaveInstance  extends com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity;

    private int totalCost;

    private int numberOfVehiclesAvailable;

    public DeliveryWaveInstance() {
    }

    public DeliveryWaveInstance(
           org.apache.axis.types.Time startTime,
           com.freshdirect.routing.proxy.stub.transportation.LocationIdentity depot,
           com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity equipmentType,
           int numberOfVehicles,
           int preferredRuntime,
           int maximumRuntime,
           java.lang.String rushHourModel,
           boolean advancedRushHour,
           int hourlyWageDuration,
           int hourlyWage,
           int overtimeWage,
           com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity,
           int totalCost,
           int numberOfVehiclesAvailable) {
        super(
            startTime,
            depot,
            equipmentType,
            numberOfVehicles,
            preferredRuntime,
            maximumRuntime,
            rushHourModel,
            advancedRushHour,
            hourlyWageDuration,
            hourlyWage,
            overtimeWage);
        this.waveIdentity = waveIdentity;
        this.totalCost = totalCost;
        this.numberOfVehiclesAvailable = numberOfVehiclesAvailable;
    }


    /**
     * Gets the waveIdentity value for this DeliveryWaveInstance.
     * 
     * @return waveIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity getWaveIdentity() {
        return waveIdentity;
    }


    /**
     * Sets the waveIdentity value for this DeliveryWaveInstance.
     * 
     * @param waveIdentity
     */
    public void setWaveIdentity(com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity) {
        this.waveIdentity = waveIdentity;
    }


    /**
     * Gets the totalCost value for this DeliveryWaveInstance.
     * 
     * @return totalCost
     */
    public int getTotalCost() {
        return totalCost;
    }


    /**
     * Sets the totalCost value for this DeliveryWaveInstance.
     * 
     * @param totalCost
     */
    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }


    /**
     * Gets the numberOfVehiclesAvailable value for this DeliveryWaveInstance.
     * 
     * @return numberOfVehiclesAvailable
     */
    public int getNumberOfVehiclesAvailable() {
        return numberOfVehiclesAvailable;
    }


    /**
     * Sets the numberOfVehiclesAvailable value for this DeliveryWaveInstance.
     * 
     * @param numberOfVehiclesAvailable
     */
    public void setNumberOfVehiclesAvailable(int numberOfVehiclesAvailable) {
        this.numberOfVehiclesAvailable = numberOfVehiclesAvailable;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeliveryWaveInstance)) return false;
        DeliveryWaveInstance other = (DeliveryWaveInstance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.waveIdentity==null && other.getWaveIdentity()==null) || 
             (this.waveIdentity!=null &&
              this.waveIdentity.equals(other.getWaveIdentity()))) &&
            this.totalCost == other.getTotalCost() &&
            this.numberOfVehiclesAvailable == other.getNumberOfVehiclesAvailable();
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
        if (getWaveIdentity() != null) {
            _hashCode += getWaveIdentity().hashCode();
        }
        _hashCode += getTotalCost();
        _hashCode += getNumberOfVehiclesAvailable();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DeliveryWaveInstance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryWaveInstance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waveIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "waveIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryWaveInstanceIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "totalCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfVehiclesAvailable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "numberOfVehiclesAvailable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
