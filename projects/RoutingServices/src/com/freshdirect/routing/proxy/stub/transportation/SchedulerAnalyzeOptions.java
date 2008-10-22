/**
 * SchedulerAnalyzeOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class SchedulerAnalyzeOptions  implements java.io.Serializable {
    private java.lang.String regionId;

    private java.lang.String area;

    private java.util.Date startDate;

    private int numDays;

    private int excludeCutoffRoutes;

    private boolean useCalendar;

    private com.freshdirect.routing.proxy.stub.transportation.DynamicWindowInfo dynamicWindowInfo;

    public SchedulerAnalyzeOptions() {
    }

    public SchedulerAnalyzeOptions(
           java.lang.String regionId,
           java.lang.String area,
           java.util.Date startDate,
           int numDays,
           int excludeCutoffRoutes,
           boolean useCalendar,
           com.freshdirect.routing.proxy.stub.transportation.DynamicWindowInfo dynamicWindowInfo) {
           this.regionId = regionId;
           this.area = area;
           this.startDate = startDate;
           this.numDays = numDays;
           this.excludeCutoffRoutes = excludeCutoffRoutes;
           this.useCalendar = useCalendar;
           this.dynamicWindowInfo = dynamicWindowInfo;
    }


    /**
     * Gets the regionId value for this SchedulerAnalyzeOptions.
     * 
     * @return regionId
     */
    public java.lang.String getRegionId() {
        return regionId;
    }


    /**
     * Sets the regionId value for this SchedulerAnalyzeOptions.
     * 
     * @param regionId
     */
    public void setRegionId(java.lang.String regionId) {
        this.regionId = regionId;
    }


    /**
     * Gets the area value for this SchedulerAnalyzeOptions.
     * 
     * @return area
     */
    public java.lang.String getArea() {
        return area;
    }


    /**
     * Sets the area value for this SchedulerAnalyzeOptions.
     * 
     * @param area
     */
    public void setArea(java.lang.String area) {
        this.area = area;
    }


    /**
     * Gets the startDate value for this SchedulerAnalyzeOptions.
     * 
     * @return startDate
     */
    public java.util.Date getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this SchedulerAnalyzeOptions.
     * 
     * @param startDate
     */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the numDays value for this SchedulerAnalyzeOptions.
     * 
     * @return numDays
     */
    public int getNumDays() {
        return numDays;
    }


    /**
     * Sets the numDays value for this SchedulerAnalyzeOptions.
     * 
     * @param numDays
     */
    public void setNumDays(int numDays) {
        this.numDays = numDays;
    }


    /**
     * Gets the excludeCutoffRoutes value for this SchedulerAnalyzeOptions.
     * 
     * @return excludeCutoffRoutes
     */
    public int getExcludeCutoffRoutes() {
        return excludeCutoffRoutes;
    }


    /**
     * Sets the excludeCutoffRoutes value for this SchedulerAnalyzeOptions.
     * 
     * @param excludeCutoffRoutes
     */
    public void setExcludeCutoffRoutes(int excludeCutoffRoutes) {
        this.excludeCutoffRoutes = excludeCutoffRoutes;
    }


    /**
     * Gets the useCalendar value for this SchedulerAnalyzeOptions.
     * 
     * @return useCalendar
     */
    public boolean isUseCalendar() {
        return useCalendar;
    }


    /**
     * Sets the useCalendar value for this SchedulerAnalyzeOptions.
     * 
     * @param useCalendar
     */
    public void setUseCalendar(boolean useCalendar) {
        this.useCalendar = useCalendar;
    }


    /**
     * Gets the dynamicWindowInfo value for this SchedulerAnalyzeOptions.
     * 
     * @return dynamicWindowInfo
     */
    public com.freshdirect.routing.proxy.stub.transportation.DynamicWindowInfo getDynamicWindowInfo() {
        return dynamicWindowInfo;
    }


    /**
     * Sets the dynamicWindowInfo value for this SchedulerAnalyzeOptions.
     * 
     * @param dynamicWindowInfo
     */
    public void setDynamicWindowInfo(com.freshdirect.routing.proxy.stub.transportation.DynamicWindowInfo dynamicWindowInfo) {
        this.dynamicWindowInfo = dynamicWindowInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulerAnalyzeOptions)) return false;
        SchedulerAnalyzeOptions other = (SchedulerAnalyzeOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.regionId==null && other.getRegionId()==null) || 
             (this.regionId!=null &&
              this.regionId.equals(other.getRegionId()))) &&
            ((this.area==null && other.getArea()==null) || 
             (this.area!=null &&
              this.area.equals(other.getArea()))) &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            this.numDays == other.getNumDays() &&
            this.excludeCutoffRoutes == other.getExcludeCutoffRoutes() &&
            this.useCalendar == other.isUseCalendar() &&
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
        if (getRegionId() != null) {
            _hashCode += getRegionId().hashCode();
        }
        if (getArea() != null) {
            _hashCode += getArea().hashCode();
        }
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        _hashCode += getNumDays();
        _hashCode += getExcludeCutoffRoutes();
        _hashCode += (isUseCalendar() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDynamicWindowInfo() != null) {
            _hashCode += getDynamicWindowInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulerAnalyzeOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "SchedulerAnalyzeOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "regionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("area");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "area"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "startDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numDays");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "numDays"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("excludeCutoffRoutes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "excludeCutoffRoutes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useCalendar");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "useCalendar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
