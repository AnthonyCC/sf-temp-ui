/**
 * RoutePathDeviationExceptionNotification.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutePathDeviationExceptionNotification  extends com.freshdirect.routing.proxy.stub.transportation.Notification  implements java.io.Serializable {
    private java.util.Calendar timeStamp;

    private double deviatedDistance;

    private org.apache.axis.types.Time timeInDeviation;

    private com.freshdirect.routing.proxy.stub.transportation.StopIdentity destinationStop;

    public RoutePathDeviationExceptionNotification() {
    }

    public RoutePathDeviationExceptionNotification(
           com.freshdirect.routing.proxy.stub.transportation.NotificationType notificationType,
           com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity notificationIdentity,
           com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity recipientIdentity,
           com.freshdirect.routing.proxy.stub.transportation.NotificationLockIdentity lockIdentity,
           java.util.Calendar lockDate,
           java.util.Calendar timeStamp,
           double deviatedDistance,
           org.apache.axis.types.Time timeInDeviation,
           com.freshdirect.routing.proxy.stub.transportation.StopIdentity destinationStop) {
        super(
            notificationType,
            notificationIdentity,
            recipientIdentity,
            lockIdentity,
            lockDate);
        this.timeStamp = timeStamp;
        this.deviatedDistance = deviatedDistance;
        this.timeInDeviation = timeInDeviation;
        this.destinationStop = destinationStop;
    }


    /**
     * Gets the timeStamp value for this RoutePathDeviationExceptionNotification.
     * 
     * @return timeStamp
     */
    public java.util.Calendar getTimeStamp() {
        return timeStamp;
    }


    /**
     * Sets the timeStamp value for this RoutePathDeviationExceptionNotification.
     * 
     * @param timeStamp
     */
    public void setTimeStamp(java.util.Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }


    /**
     * Gets the deviatedDistance value for this RoutePathDeviationExceptionNotification.
     * 
     * @return deviatedDistance
     */
    public double getDeviatedDistance() {
        return deviatedDistance;
    }


    /**
     * Sets the deviatedDistance value for this RoutePathDeviationExceptionNotification.
     * 
     * @param deviatedDistance
     */
    public void setDeviatedDistance(double deviatedDistance) {
        this.deviatedDistance = deviatedDistance;
    }


    /**
     * Gets the timeInDeviation value for this RoutePathDeviationExceptionNotification.
     * 
     * @return timeInDeviation
     */
    public org.apache.axis.types.Time getTimeInDeviation() {
        return timeInDeviation;
    }


    /**
     * Sets the timeInDeviation value for this RoutePathDeviationExceptionNotification.
     * 
     * @param timeInDeviation
     */
    public void setTimeInDeviation(org.apache.axis.types.Time timeInDeviation) {
        this.timeInDeviation = timeInDeviation;
    }


    /**
     * Gets the destinationStop value for this RoutePathDeviationExceptionNotification.
     * 
     * @return destinationStop
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopIdentity getDestinationStop() {
        return destinationStop;
    }


    /**
     * Sets the destinationStop value for this RoutePathDeviationExceptionNotification.
     * 
     * @param destinationStop
     */
    public void setDestinationStop(com.freshdirect.routing.proxy.stub.transportation.StopIdentity destinationStop) {
        this.destinationStop = destinationStop;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutePathDeviationExceptionNotification)) return false;
        RoutePathDeviationExceptionNotification other = (RoutePathDeviationExceptionNotification) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.timeStamp==null && other.getTimeStamp()==null) || 
             (this.timeStamp!=null &&
              this.timeStamp.equals(other.getTimeStamp()))) &&
            this.deviatedDistance == other.getDeviatedDistance() &&
            ((this.timeInDeviation==null && other.getTimeInDeviation()==null) || 
             (this.timeInDeviation!=null &&
              this.timeInDeviation.equals(other.getTimeInDeviation()))) &&
            ((this.destinationStop==null && other.getDestinationStop()==null) || 
             (this.destinationStop!=null &&
              this.destinationStop.equals(other.getDestinationStop())));
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
        if (getTimeStamp() != null) {
            _hashCode += getTimeStamp().hashCode();
        }
        _hashCode += new Double(getDeviatedDistance()).hashCode();
        if (getTimeInDeviation() != null) {
            _hashCode += getTimeInDeviation().hashCode();
        }
        if (getDestinationStop() != null) {
            _hashCode += getDestinationStop().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutePathDeviationExceptionNotification.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutePathDeviationExceptionNotification"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deviatedDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "deviatedDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeInDeviation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "timeInDeviation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "time"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationStop");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "destinationStop"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopIdentity"));
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
