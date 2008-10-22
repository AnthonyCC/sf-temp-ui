/**
 * PlacementCost.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class PlacementCost  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity;

    private com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition;

    private double grade;

    private double addedDistance;

    private int addedTime;

    private java.util.Calendar adjustedRouteStartTime;

    private com.freshdirect.routing.proxy.stub.transportation.TimeWindowCost originalTimeWindowCost;

    private com.freshdirect.routing.proxy.stub.transportation.TimeWindowCost newTimeWindowCost;

    public PlacementCost() {
    }

    public PlacementCost(
           com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity,
           com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition,
           double grade,
           double addedDistance,
           int addedTime,
           java.util.Calendar adjustedRouteStartTime,
           com.freshdirect.routing.proxy.stub.transportation.TimeWindowCost originalTimeWindowCost,
           com.freshdirect.routing.proxy.stub.transportation.TimeWindowCost newTimeWindowCost) {
           this.routeIdentity = routeIdentity;
           this.placementPosition = placementPosition;
           this.grade = grade;
           this.addedDistance = addedDistance;
           this.addedTime = addedTime;
           this.adjustedRouteStartTime = adjustedRouteStartTime;
           this.originalTimeWindowCost = originalTimeWindowCost;
           this.newTimeWindowCost = newTimeWindowCost;
    }


    /**
     * Gets the routeIdentity value for this PlacementCost.
     * 
     * @return routeIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity getRouteIdentity() {
        return routeIdentity;
    }


    /**
     * Sets the routeIdentity value for this PlacementCost.
     * 
     * @param routeIdentity
     */
    public void setRouteIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity) {
        this.routeIdentity = routeIdentity;
    }


    /**
     * Gets the placementPosition value for this PlacementCost.
     * 
     * @return placementPosition
     */
    public com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions getPlacementPosition() {
        return placementPosition;
    }


    /**
     * Sets the placementPosition value for this PlacementCost.
     * 
     * @param placementPosition
     */
    public void setPlacementPosition(com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition) {
        this.placementPosition = placementPosition;
    }


    /**
     * Gets the grade value for this PlacementCost.
     * 
     * @return grade
     */
    public double getGrade() {
        return grade;
    }


    /**
     * Sets the grade value for this PlacementCost.
     * 
     * @param grade
     */
    public void setGrade(double grade) {
        this.grade = grade;
    }


    /**
     * Gets the addedDistance value for this PlacementCost.
     * 
     * @return addedDistance
     */
    public double getAddedDistance() {
        return addedDistance;
    }


    /**
     * Sets the addedDistance value for this PlacementCost.
     * 
     * @param addedDistance
     */
    public void setAddedDistance(double addedDistance) {
        this.addedDistance = addedDistance;
    }


    /**
     * Gets the addedTime value for this PlacementCost.
     * 
     * @return addedTime
     */
    public int getAddedTime() {
        return addedTime;
    }


    /**
     * Sets the addedTime value for this PlacementCost.
     * 
     * @param addedTime
     */
    public void setAddedTime(int addedTime) {
        this.addedTime = addedTime;
    }


    /**
     * Gets the adjustedRouteStartTime value for this PlacementCost.
     * 
     * @return adjustedRouteStartTime
     */
    public java.util.Calendar getAdjustedRouteStartTime() {
        return adjustedRouteStartTime;
    }


    /**
     * Sets the adjustedRouteStartTime value for this PlacementCost.
     * 
     * @param adjustedRouteStartTime
     */
    public void setAdjustedRouteStartTime(java.util.Calendar adjustedRouteStartTime) {
        this.adjustedRouteStartTime = adjustedRouteStartTime;
    }


    /**
     * Gets the originalTimeWindowCost value for this PlacementCost.
     * 
     * @return originalTimeWindowCost
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeWindowCost getOriginalTimeWindowCost() {
        return originalTimeWindowCost;
    }


    /**
     * Sets the originalTimeWindowCost value for this PlacementCost.
     * 
     * @param originalTimeWindowCost
     */
    public void setOriginalTimeWindowCost(com.freshdirect.routing.proxy.stub.transportation.TimeWindowCost originalTimeWindowCost) {
        this.originalTimeWindowCost = originalTimeWindowCost;
    }


    /**
     * Gets the newTimeWindowCost value for this PlacementCost.
     * 
     * @return newTimeWindowCost
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeWindowCost getNewTimeWindowCost() {
        return newTimeWindowCost;
    }


    /**
     * Sets the newTimeWindowCost value for this PlacementCost.
     * 
     * @param newTimeWindowCost
     */
    public void setNewTimeWindowCost(com.freshdirect.routing.proxy.stub.transportation.TimeWindowCost newTimeWindowCost) {
        this.newTimeWindowCost = newTimeWindowCost;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PlacementCost)) return false;
        PlacementCost other = (PlacementCost) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.routeIdentity==null && other.getRouteIdentity()==null) || 
             (this.routeIdentity!=null &&
              this.routeIdentity.equals(other.getRouteIdentity()))) &&
            ((this.placementPosition==null && other.getPlacementPosition()==null) || 
             (this.placementPosition!=null &&
              this.placementPosition.equals(other.getPlacementPosition()))) &&
            this.grade == other.getGrade() &&
            this.addedDistance == other.getAddedDistance() &&
            this.addedTime == other.getAddedTime() &&
            ((this.adjustedRouteStartTime==null && other.getAdjustedRouteStartTime()==null) || 
             (this.adjustedRouteStartTime!=null &&
              this.adjustedRouteStartTime.equals(other.getAdjustedRouteStartTime()))) &&
            ((this.originalTimeWindowCost==null && other.getOriginalTimeWindowCost()==null) || 
             (this.originalTimeWindowCost!=null &&
              this.originalTimeWindowCost.equals(other.getOriginalTimeWindowCost()))) &&
            ((this.newTimeWindowCost==null && other.getNewTimeWindowCost()==null) || 
             (this.newTimeWindowCost!=null &&
              this.newTimeWindowCost.equals(other.getNewTimeWindowCost())));
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
        if (getRouteIdentity() != null) {
            _hashCode += getRouteIdentity().hashCode();
        }
        if (getPlacementPosition() != null) {
            _hashCode += getPlacementPosition().hashCode();
        }
        _hashCode += new Double(getGrade()).hashCode();
        _hashCode += new Double(getAddedDistance()).hashCode();
        _hashCode += getAddedTime();
        if (getAdjustedRouteStartTime() != null) {
            _hashCode += getAdjustedRouteStartTime().hashCode();
        }
        if (getOriginalTimeWindowCost() != null) {
            _hashCode += getOriginalTimeWindowCost().hashCode();
        }
        if (getNewTimeWindowCost() != null) {
            _hashCode += getNewTimeWindowCost().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PlacementCost.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "PlacementCost"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("routeIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "routeIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "RouteIdentity"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placementPosition");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "placementPosition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "StopPlacementOptions"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grade");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "grade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addedDistance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "addedDistance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "addedTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adjustedRouteStartTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "adjustedRouteStartTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originalTimeWindowCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "originalTimeWindowCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeWindowCost"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newTimeWindowCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "newTimeWindowCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TimeWindowCost"));
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
