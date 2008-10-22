/**
 * RoutingSessionCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class RoutingSessionCriteria  implements java.io.Serializable {
    private java.lang.String regionIdentity;

    private java.util.Date dateStart;

    private java.util.Date dateEnd;

    private java.lang.String scenario;

    private java.lang.String description;

    public RoutingSessionCriteria() {
    }

    public RoutingSessionCriteria(
           java.lang.String regionIdentity,
           java.util.Date dateStart,
           java.util.Date dateEnd,
           java.lang.String scenario,
           java.lang.String description) {
           this.regionIdentity = regionIdentity;
           this.dateStart = dateStart;
           this.dateEnd = dateEnd;
           this.scenario = scenario;
           this.description = description;
    }


    /**
     * Gets the regionIdentity value for this RoutingSessionCriteria.
     * 
     * @return regionIdentity
     */
    public java.lang.String getRegionIdentity() {
        return regionIdentity;
    }


    /**
     * Sets the regionIdentity value for this RoutingSessionCriteria.
     * 
     * @param regionIdentity
     */
    public void setRegionIdentity(java.lang.String regionIdentity) {
        this.regionIdentity = regionIdentity;
    }


    /**
     * Gets the dateStart value for this RoutingSessionCriteria.
     * 
     * @return dateStart
     */
    public java.util.Date getDateStart() {
        return dateStart;
    }


    /**
     * Sets the dateStart value for this RoutingSessionCriteria.
     * 
     * @param dateStart
     */
    public void setDateStart(java.util.Date dateStart) {
        this.dateStart = dateStart;
    }


    /**
     * Gets the dateEnd value for this RoutingSessionCriteria.
     * 
     * @return dateEnd
     */
    public java.util.Date getDateEnd() {
        return dateEnd;
    }


    /**
     * Sets the dateEnd value for this RoutingSessionCriteria.
     * 
     * @param dateEnd
     */
    public void setDateEnd(java.util.Date dateEnd) {
        this.dateEnd = dateEnd;
    }


    /**
     * Gets the scenario value for this RoutingSessionCriteria.
     * 
     * @return scenario
     */
    public java.lang.String getScenario() {
        return scenario;
    }


    /**
     * Sets the scenario value for this RoutingSessionCriteria.
     * 
     * @param scenario
     */
    public void setScenario(java.lang.String scenario) {
        this.scenario = scenario;
    }


    /**
     * Gets the description value for this RoutingSessionCriteria.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this RoutingSessionCriteria.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoutingSessionCriteria)) return false;
        RoutingSessionCriteria other = (RoutingSessionCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.regionIdentity==null && other.getRegionIdentity()==null) || 
             (this.regionIdentity!=null &&
              this.regionIdentity.equals(other.getRegionIdentity()))) &&
            ((this.dateStart==null && other.getDateStart()==null) || 
             (this.dateStart!=null &&
              this.dateStart.equals(other.getDateStart()))) &&
            ((this.dateEnd==null && other.getDateEnd()==null) || 
             (this.dateEnd!=null &&
              this.dateEnd.equals(other.getDateEnd()))) &&
            ((this.scenario==null && other.getScenario()==null) || 
             (this.scenario!=null &&
              this.scenario.equals(other.getScenario()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription())));
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
        if (getRegionIdentity() != null) {
            _hashCode += getRegionIdentity().hashCode();
        }
        if (getDateStart() != null) {
            _hashCode += getDateStart().hashCode();
        }
        if (getDateEnd() != null) {
            _hashCode += getDateEnd().hashCode();
        }
        if (getScenario() != null) {
            _hashCode += getScenario().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoutingSessionCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RoutingSessionCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regionIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "dateEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scenario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "scenario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
