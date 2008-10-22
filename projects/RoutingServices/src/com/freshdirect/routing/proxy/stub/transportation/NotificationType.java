/**
 * NotificationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class NotificationType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected NotificationType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _ntRouteStart = "ntRouteStart";
    public static final java.lang.String _ntOriginDepart = "ntOriginDepart";
    public static final java.lang.String _ntDestinationArrive = "ntDestinationArrive";
    public static final java.lang.String _ntRouteComplete = "ntRouteComplete";
    public static final java.lang.String _ntRouteChange = "ntRouteChange";
    public static final java.lang.String _ntStopSequence = "ntStopSequence";
    public static final java.lang.String _ntStopArrive = "ntStopArrive";
    public static final java.lang.String _ntStopDepart = "ntStopDepart";
    public static final java.lang.String _ntStopCancel = "ntStopCancel";
    public static final java.lang.String _ntGPS = "ntGPS";
    public static final java.lang.String _ntDeliveryDetail = "ntDeliveryDetail";
    public static final java.lang.String _ntRoutePathDeviationException = "ntRoutePathDeviationException";
    public static final java.lang.String _ntRPDExitPointException = "ntRPDExitPointException";
    public static final java.lang.String _ntUnplannedStopException = "ntUnplannedStopException";
    public static final java.lang.String _ntEarlyArrivalException = "ntEarlyArrivalException";
    public static final java.lang.String _ntLateDepartureException = "ntLateDepartureException";
    public static final java.lang.String _ntServiceTimeException = "ntServiceTimeException";
    public static final java.lang.String _ntGpsGapException = "ntGpsGapException";
    public static final java.lang.String _ntLateGpsException = "ntLateGpsException";
    public static final java.lang.String _ntNoGpsException = "ntNoGpsException";
    public static final java.lang.String _ntTimeOffPlannedException = "ntTimeOffPlannedException";
    public static final java.lang.String _ntOutOfSequenceException = "ntOutOfSequenceException";
    public static final java.lang.String _ntOSDMonitoringException = "ntOSDMonitoringException";
    public static final java.lang.String _ntDriverCancelException = "ntDriverCancelException";
    public static final java.lang.String _ntUndeliverableStopException = "ntUndeliverableStopException";
    public static final java.lang.String _ntOutOfContactException = "ntOutOfContactException";
    public static final java.lang.String _ntUserException = "ntUserException";
    public static final java.lang.String _ntSpeedViolationException = "ntSpeedViolationException";
    public static final java.lang.String _ntEventReceiptException = "ntEventReceiptException";
    public static final java.lang.String _ntStartCompleteOutOfBoundsException = "ntStartCompleteOutOfBoundsException";
    public static final java.lang.String _ntDutyPeriodViolationException = "ntDutyPeriodViolationException";
    public static final java.lang.String _ntOrdersSent = "ntOrdersSent";
    public static final NotificationType ntRouteStart = new NotificationType(_ntRouteStart);
    public static final NotificationType ntOriginDepart = new NotificationType(_ntOriginDepart);
    public static final NotificationType ntDestinationArrive = new NotificationType(_ntDestinationArrive);
    public static final NotificationType ntRouteComplete = new NotificationType(_ntRouteComplete);
    public static final NotificationType ntRouteChange = new NotificationType(_ntRouteChange);
    public static final NotificationType ntStopSequence = new NotificationType(_ntStopSequence);
    public static final NotificationType ntStopArrive = new NotificationType(_ntStopArrive);
    public static final NotificationType ntStopDepart = new NotificationType(_ntStopDepart);
    public static final NotificationType ntStopCancel = new NotificationType(_ntStopCancel);
    public static final NotificationType ntGPS = new NotificationType(_ntGPS);
    public static final NotificationType ntDeliveryDetail = new NotificationType(_ntDeliveryDetail);
    public static final NotificationType ntRoutePathDeviationException = new NotificationType(_ntRoutePathDeviationException);
    public static final NotificationType ntRPDExitPointException = new NotificationType(_ntRPDExitPointException);
    public static final NotificationType ntUnplannedStopException = new NotificationType(_ntUnplannedStopException);
    public static final NotificationType ntEarlyArrivalException = new NotificationType(_ntEarlyArrivalException);
    public static final NotificationType ntLateDepartureException = new NotificationType(_ntLateDepartureException);
    public static final NotificationType ntServiceTimeException = new NotificationType(_ntServiceTimeException);
    public static final NotificationType ntGpsGapException = new NotificationType(_ntGpsGapException);
    public static final NotificationType ntLateGpsException = new NotificationType(_ntLateGpsException);
    public static final NotificationType ntNoGpsException = new NotificationType(_ntNoGpsException);
    public static final NotificationType ntTimeOffPlannedException = new NotificationType(_ntTimeOffPlannedException);
    public static final NotificationType ntOutOfSequenceException = new NotificationType(_ntOutOfSequenceException);
    public static final NotificationType ntOSDMonitoringException = new NotificationType(_ntOSDMonitoringException);
    public static final NotificationType ntDriverCancelException = new NotificationType(_ntDriverCancelException);
    public static final NotificationType ntUndeliverableStopException = new NotificationType(_ntUndeliverableStopException);
    public static final NotificationType ntOutOfContactException = new NotificationType(_ntOutOfContactException);
    public static final NotificationType ntUserException = new NotificationType(_ntUserException);
    public static final NotificationType ntSpeedViolationException = new NotificationType(_ntSpeedViolationException);
    public static final NotificationType ntEventReceiptException = new NotificationType(_ntEventReceiptException);
    public static final NotificationType ntStartCompleteOutOfBoundsException = new NotificationType(_ntStartCompleteOutOfBoundsException);
    public static final NotificationType ntDutyPeriodViolationException = new NotificationType(_ntDutyPeriodViolationException);
    public static final NotificationType ntOrdersSent = new NotificationType(_ntOrdersSent);
    public java.lang.String getValue() { return _value_;}
    public static NotificationType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        NotificationType enumeration = (NotificationType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static NotificationType fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NotificationType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "NotificationType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
