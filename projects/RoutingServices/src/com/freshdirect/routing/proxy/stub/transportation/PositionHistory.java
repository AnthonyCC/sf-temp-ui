/**
 * PositionHistory.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class PositionHistory  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.PositionHistoryIdentity positionHistoryIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity equipmentIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.MapPoint position;

    private java.lang.String positionText;

    private com.freshdirect.routing.proxy.stub.transportation.PositionReasonCode positionReasonCode;

    private java.util.Calendar receivedTime;

    private int speed;

    public PositionHistory() {
    }

    public PositionHistory(
           com.freshdirect.routing.proxy.stub.transportation.PositionHistoryIdentity positionHistoryIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity equipmentIdentity,
           com.freshdirect.routing.proxy.stub.transportation.MapPoint position,
           java.lang.String positionText,
           com.freshdirect.routing.proxy.stub.transportation.PositionReasonCode positionReasonCode,
           java.util.Calendar receivedTime,
           int speed) {
           this.positionHistoryIdentity = positionHistoryIdentity;
           this.routeIdentity = routeIdentity;
           this.equipmentIdentity = equipmentIdentity;
           this.position = position;
           this.positionText = positionText;
           this.positionReasonCode = positionReasonCode;
           this.receivedTime = receivedTime;
           this.speed = speed;
    }


    /**
     * Gets the positionHistoryIdentity value for this PositionHistory.
     * 
     * @return positionHistoryIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.PositionHistoryIdentity getPositionHistoryIdentity() {
        return positionHistoryIdentity;
    }


    /**
     * Sets the positionHistoryIdentity value for this PositionHistory.
     * 
     * @param positionHistoryIdentity
     */
    public void setPositionHistoryIdentity(com.freshdirect.routing.proxy.stub.transportation.PositionHistoryIdentity positionHistoryIdentity) {
        this.positionHistoryIdentity = positionHistoryIdentity;
    }


    /**
     * Gets the routeIdentity value for this PositionHistory.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this PositionHistory.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the equipmentIdentity value for this PositionHistory.
     * 
     * @return equipmentIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity getEquipmentIdentity() {
        return equipmentIdentity;
    }


    /**
     * Sets the equipmentIdentity value for this PositionHistory.
     * 
     * @param equipmentIdentity
     */
    public void setEquipmentIdentity(com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity equipmentIdentity) {
        this.equipmentIdentity = equipmentIdentity;
    }


    /**
     * Gets the position value for this PositionHistory.
     * 
     * @return position
     */
    public com.freshdirect.routing.proxy.stub.transportation.MapPoint getPosition() {
        return position;
    }


    /**
     * Sets the position value for this PositionHistory.
     * 
     * @param position
     */
    public void setPosition(com.freshdirect.routing.proxy.stub.transportation.MapPoint position) {
        this.position = position;
    }


    /**
     * Gets the positionText value for this PositionHistory.
     * 
     * @return positionText
     */
    public java.lang.String getPositionText() {
        return positionText;
    }


    /**
     * Sets the positionText value for this PositionHistory.
     * 
     * @param positionText
     */
    public void setPositionText(java.lang.String positionText) {
        this.positionText = positionText;
    }


    /**
     * Gets the positionReasonCode value for this PositionHistory.
     * 
     * @return positionReasonCode
     */
    public com.freshdirect.routing.proxy.stub.transportation.PositionReasonCode getPositionReasonCode() {
        return positionReasonCode;
    }


    /**
     * Sets the positionReasonCode value for this PositionHistory.
     * 
     * @param positionReasonCode
     */
    public void setPositionReasonCode(com.freshdirect.routing.proxy.stub.transportation.PositionReasonCode positionReasonCode) {
        this.positionReasonCode = positionReasonCode;
    }


    /**
     * Gets the receivedTime value for this PositionHistory.
     * 
     * @return receivedTime
     */
    public java.util.Calendar getReceivedTime() {
        return receivedTime;
    }


    /**
     * Sets the receivedTime value for this PositionHistory.
     * 
     * @param receivedTime
     */
    public void setReceivedTime(java.util.Calendar receivedTime) {
        this.receivedTime = receivedTime;
    }


    /**
     * Gets the speed value for this PositionHistory.
     * 
     * @return speed
     */
    public int getSpeed() {
        return speed;
    }


    /**
     * Sets the speed value for this PositionHistory.
     * 
     * @param speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PositionHistory)) return false;
        PositionHistory other = (PositionHistory) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.positionHistoryIdentity==null && other.getPositionHistoryIdentity()==null) || 
             (this.positionHistoryIdentity!=null &&
              this.positionHistoryIdentity.equals(other.getPositionHistoryIdentity()))) &&
            ((this.routeIdentity==null && other.getRouteIdentity()==null) || 
             (this.routeIdentity!=null &&
              this.routeIdentity.equals(other.getRouteIdentity()))) &&
            ((this.equipmentIdentity==null && other.getEquipmentIdentity()==null) || 
             (this.equipmentIdentity!=null &&
              this.equipmentIdentity.equals(other.getEquipmentIdentity()))) &&
            ((this.position==null && other.getPosition()==null) || 
             (this.position!=null &&
              this.position.equals(other.getPosition()))) &&
            ((this.positionText==null && other.getPositionText()==null) || 
             (this.positionText!=null &&
              this.positionText.equals(other.getPositionText()))) &&
            ((this.positionReasonCode==null && other.getPositionReasonCode()==null) || 
             (this.positionReasonCode!=null &&
              this.positionReasonCode.equals(other.getPositionReasonCode()))) &&
            ((this.receivedTime==null && other.getReceivedTime()==null) || 
             (this.receivedTime!=null &&
              this.receivedTime.equals(other.getReceivedTime()))) &&
            this.speed == other.getSpeed();
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
        if (getPositionHistoryIdentity() != null) {
            _hashCode += getPositionHistoryIdentity().hashCode();
        }
        if (getRouteIdentity() != null) {
            _hashCode += getRouteIdentity().hashCode();
        }
        if (getEquipmentIdentity() != null) {
            _hashCode += getEquipmentIdentity().hashCode();
        }
        if (getPosition() != null) {
            _hashCode += getPosition().hashCode();
        }
        if (getPositionText() != null) {
            _hashCode += getPositionText().hashCode();
        }
        if (getPositionReasonCode() != null) {
            _hashCode += getPositionReasonCode().hashCode();
        }
        if (getReceivedTime() != null) {
            _hashCode += getReceivedTime().hashCode();
        }
        _hashCode += getSpeed();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PositionHistory.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "PositionHistory"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionHistoryIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionHistoryIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "PositionHistoryIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("equipmentIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "equipmentIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "EquipmentIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("position");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "position"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "MapPoint"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionReasonCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "positionReasonCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "PositionReasonCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("receivedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "receivedTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("speed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "speed"));
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
