/**
 * DeliveryWaveAttributes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class DeliveryWaveAttributes  implements java.io.Serializable {
    private org.apache.axis.types.Time startTime;

    private com.freshdirect.routing.proxy.stub.transportation.LocationIdentity depot;

    private com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity equipmentType;

    private int numberOfVehicles;

    private int preferredRuntime;

    private int maximumRuntime;

    private java.lang.String rushHourModel;

    private boolean advancedRushHour;

    private int hourlyWageDuration;

    private int hourlyWage;

    private int overtimeWage;

    public DeliveryWaveAttributes() {
    }

    public DeliveryWaveAttributes(
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
           int overtimeWage) {
           this.startTime = startTime;
           this.depot = depot;
           this.equipmentType = equipmentType;
           this.numberOfVehicles = numberOfVehicles;
           this.preferredRuntime = preferredRuntime;
           this.maximumRuntime = maximumRuntime;
           this.rushHourModel = rushHourModel;
           this.advancedRushHour = advancedRushHour;
           this.hourlyWageDuration = hourlyWageDuration;
           this.hourlyWage = hourlyWage;
           this.overtimeWage = overtimeWage;
    }


    /**
     * Gets the startTime value for this DeliveryWaveAttributes.
     * 
     * @return startTime
     */
    public org.apache.axis.types.Time getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this DeliveryWaveAttributes.
     * 
     * @param startTime
     */
    public void setStartTime(org.apache.axis.types.Time startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the depot value for this DeliveryWaveAttributes.
     * 
     * @return depot
     */
    public com.freshdirect.routing.proxy.stub.transportation.LocationIdentity getDepot() {
        return depot;
    }


    /**
     * Sets the depot value for this DeliveryWaveAttributes.
     * 
     * @param depot
     */
    public void setDepot(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity depot) {
        this.depot = depot;
    }


    /**
     * Gets the equipmentType value for this DeliveryWaveAttributes.
     * 
     * @return equipmentType
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity getEquipmentType() {
        return equipmentType;
    }


    /**
     * Sets the equipmentType value for this DeliveryWaveAttributes.
     * 
     * @param equipmentType
     */
    public void setEquipmentType(com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity equipmentType) {
        this.equipmentType = equipmentType;
    }


    /**
     * Gets the numberOfVehicles value for this DeliveryWaveAttributes.
     * 
     * @return numberOfVehicles
     */
    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }


    /**
     * Sets the numberOfVehicles value for this DeliveryWaveAttributes.
     * 
     * @param numberOfVehicles
     */
    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }


    /**
     * Gets the preferredRuntime value for this DeliveryWaveAttributes.
     * 
     * @return preferredRuntime
     */
    public int getPreferredRuntime() {
        return preferredRuntime;
    }


    /**
     * Sets the preferredRuntime value for this DeliveryWaveAttributes.
     * 
     * @param preferredRuntime
     */
    public void setPreferredRuntime(int preferredRuntime) {
        this.preferredRuntime = preferredRuntime;
    }


    /**
     * Gets the maximumRuntime value for this DeliveryWaveAttributes.
     * 
     * @return maximumRuntime
     */
    public int getMaximumRuntime() {
        return maximumRuntime;
    }


    /**
     * Sets the maximumRuntime value for this DeliveryWaveAttributes.
     * 
     * @param maximumRuntime
     */
    public void setMaximumRuntime(int maximumRuntime) {
        this.maximumRuntime = maximumRuntime;
    }


    /**
     * Gets the rushHourModel value for this DeliveryWaveAttributes.
     * 
     * @return rushHourModel
     */
    public java.lang.String getRushHourModel() {
        return rushHourModel;
    }


    /**
     * Sets the rushHourModel value for this DeliveryWaveAttributes.
     * 
     * @param rushHourModel
     */
    public void setRushHourModel(java.lang.String rushHourModel) {
        this.rushHourModel = rushHourModel;
    }


    /**
     * Gets the advancedRushHour value for this DeliveryWaveAttributes.
     * 
     * @return advancedRushHour
     */
    public boolean isAdvancedRushHour() {
        return advancedRushHour;
    }


    /**
     * Sets the advancedRushHour value for this DeliveryWaveAttributes.
     * 
     * @param advancedRushHour
     */
    public void setAdvancedRushHour(boolean advancedRushHour) {
        this.advancedRushHour = advancedRushHour;
    }


    /**
     * Gets the hourlyWageDuration value for this DeliveryWaveAttributes.
     * 
     * @return hourlyWageDuration
     */
    public int getHourlyWageDuration() {
        return hourlyWageDuration;
    }


    /**
     * Sets the hourlyWageDuration value for this DeliveryWaveAttributes.
     * 
     * @param hourlyWageDuration
     */
    public void setHourlyWageDuration(int hourlyWageDuration) {
        this.hourlyWageDuration = hourlyWageDuration;
    }


    /**
     * Gets the hourlyWage value for this DeliveryWaveAttributes.
     * 
     * @return hourlyWage
     */
    public int getHourlyWage() {
        return hourlyWage;
    }


    /**
     * Sets the hourlyWage value for this DeliveryWaveAttributes.
     * 
     * @param hourlyWage
     */
    public void setHourlyWage(int hourlyWage) {
        this.hourlyWage = hourlyWage;
    }


    /**
     * Gets the overtimeWage value for this DeliveryWaveAttributes.
     * 
     * @return overtimeWage
     */
    public int getOvertimeWage() {
        return overtimeWage;
    }


    /**
     * Sets the overtimeWage value for this DeliveryWaveAttributes.
     * 
     * @param overtimeWage
     */
    public void setOvertimeWage(int overtimeWage) {
        this.overtimeWage = overtimeWage;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeliveryWaveAttributes)) return false;
        DeliveryWaveAttributes other = (DeliveryWaveAttributes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
            ((this.depot==null && other.getDepot()==null) || 
             (this.depot!=null &&
              this.depot.equals(other.getDepot()))) &&
            ((this.equipmentType==null && other.getEquipmentType()==null) || 
             (this.equipmentType!=null &&
              this.equipmentType.equals(other.getEquipmentType()))) &&
            this.numberOfVehicles == other.getNumberOfVehicles() &&
            this.preferredRuntime == other.getPreferredRuntime() &&
            this.maximumRuntime == other.getMaximumRuntime() &&
            ((this.rushHourModel==null && other.getRushHourModel()==null) || 
             (this.rushHourModel!=null &&
              this.rushHourModel.equals(other.getRushHourModel()))) &&
            this.advancedRushHour == other.isAdvancedRushHour() &&
            this.hourlyWageDuration == other.getHourlyWageDuration() &&
            this.hourlyWage == other.getHourlyWage() &&
            this.overtimeWage == other.getOvertimeWage();
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
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        if (getDepot() != null) {
            _hashCode += getDepot().hashCode();
        }
        if (getEquipmentType() != null) {
            _hashCode += getEquipmentType().hashCode();
        }
        _hashCode += getNumberOfVehicles();
        _hashCode += getPreferredRuntime();
        _hashCode += getMaximumRuntime();
        if (getRushHourModel() != null) {
            _hashCode += getRushHourModel().hashCode();
        }
        _hashCode += (isAdvancedRushHour() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getHourlyWageDuration();
        _hashCode += getHourlyWage();
        _hashCode += getOvertimeWage();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DeliveryWaveAttributes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "DeliveryWaveAttributes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "startTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depot");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "depot"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LocationIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentTypeIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfVehicles");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "numberOfVehicles"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preferredRuntime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "preferredRuntime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maximumRuntime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "maximumRuntime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rushHourModel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "rushHourModel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("advancedRushHour");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "advancedRushHour"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hourlyWageDuration");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "hourlyWageDuration"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hourlyWage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "hourlyWage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overtimeWage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "overtimeWage"));
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
