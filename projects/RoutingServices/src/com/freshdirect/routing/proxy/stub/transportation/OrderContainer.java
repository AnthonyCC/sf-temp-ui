/**
 * OrderContainer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class OrderContainer  implements java.io.Serializable {
    private com.freshdirect.routing.proxy.stub.transportation.ContainerIdentity containerIdentity;

    private java.lang.String containerID;

    private java.lang.String containerType;

    private boolean droppedOff;

    private boolean planned;

    private java.lang.String barcode;

    private com.freshdirect.routing.proxy.stub.transportation.ContainerServiceType serviceType;

    private com.freshdirect.routing.proxy.stub.transportation.LineItemContainer[] lineItems;

    private com.freshdirect.routing.proxy.stub.transportation.ContainerEvent[] containerEvents;

    public OrderContainer() {
    }

    public OrderContainer(
           com.freshdirect.routing.proxy.stub.transportation.ContainerIdentity containerIdentity,
           java.lang.String containerID,
           java.lang.String containerType,
           boolean droppedOff,
           boolean planned,
           java.lang.String barcode,
           com.freshdirect.routing.proxy.stub.transportation.ContainerServiceType serviceType,
           com.freshdirect.routing.proxy.stub.transportation.LineItemContainer[] lineItems,
           com.freshdirect.routing.proxy.stub.transportation.ContainerEvent[] containerEvents) {
           this.containerIdentity = containerIdentity;
           this.containerID = containerID;
           this.containerType = containerType;
           this.droppedOff = droppedOff;
           this.planned = planned;
           this.barcode = barcode;
           this.serviceType = serviceType;
           this.lineItems = lineItems;
           this.containerEvents = containerEvents;
    }


    /**
     * Gets the containerIdentity value for this OrderContainer.
     * 
     * @return containerIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.ContainerIdentity getContainerIdentity() {
        return containerIdentity;
    }


    /**
     * Sets the containerIdentity value for this OrderContainer.
     * 
     * @param containerIdentity
     */
    public void setContainerIdentity(com.freshdirect.routing.proxy.stub.transportation.ContainerIdentity containerIdentity) {
        this.containerIdentity = containerIdentity;
    }


    /**
     * Gets the containerID value for this OrderContainer.
     * 
     * @return containerID
     */
    public java.lang.String getContainerID() {
        return containerID;
    }


    /**
     * Sets the containerID value for this OrderContainer.
     * 
     * @param containerID
     */
    public void setContainerID(java.lang.String containerID) {
        this.containerID = containerID;
    }


    /**
     * Gets the containerType value for this OrderContainer.
     * 
     * @return containerType
     */
    public java.lang.String getContainerType() {
        return containerType;
    }


    /**
     * Sets the containerType value for this OrderContainer.
     * 
     * @param containerType
     */
    public void setContainerType(java.lang.String containerType) {
        this.containerType = containerType;
    }


    /**
     * Gets the droppedOff value for this OrderContainer.
     * 
     * @return droppedOff
     */
    public boolean isDroppedOff() {
        return droppedOff;
    }


    /**
     * Sets the droppedOff value for this OrderContainer.
     * 
     * @param droppedOff
     */
    public void setDroppedOff(boolean droppedOff) {
        this.droppedOff = droppedOff;
    }


    /**
     * Gets the planned value for this OrderContainer.
     * 
     * @return planned
     */
    public boolean isPlanned() {
        return planned;
    }


    /**
     * Sets the planned value for this OrderContainer.
     * 
     * @param planned
     */
    public void setPlanned(boolean planned) {
        this.planned = planned;
    }


    /**
     * Gets the barcode value for this OrderContainer.
     * 
     * @return barcode
     */
    public java.lang.String getBarcode() {
        return barcode;
    }


    /**
     * Sets the barcode value for this OrderContainer.
     * 
     * @param barcode
     */
    public void setBarcode(java.lang.String barcode) {
        this.barcode = barcode;
    }


    /**
     * Gets the serviceType value for this OrderContainer.
     * 
     * @return serviceType
     */
    public com.freshdirect.routing.proxy.stub.transportation.ContainerServiceType getServiceType() {
        return serviceType;
    }


    /**
     * Sets the serviceType value for this OrderContainer.
     * 
     * @param serviceType
     */
    public void setServiceType(com.freshdirect.routing.proxy.stub.transportation.ContainerServiceType serviceType) {
        this.serviceType = serviceType;
    }


    /**
     * Gets the lineItems value for this OrderContainer.
     * 
     * @return lineItems
     */
    public com.freshdirect.routing.proxy.stub.transportation.LineItemContainer[] getLineItems() {
        return lineItems;
    }


    /**
     * Sets the lineItems value for this OrderContainer.
     * 
     * @param lineItems
     */
    public void setLineItems(com.freshdirect.routing.proxy.stub.transportation.LineItemContainer[] lineItems) {
        this.lineItems = lineItems;
    }

    public com.freshdirect.routing.proxy.stub.transportation.LineItemContainer getLineItems(int i) {
        return this.lineItems[i];
    }

    public void setLineItems(int i, com.freshdirect.routing.proxy.stub.transportation.LineItemContainer _value) {
        this.lineItems[i] = _value;
    }


    /**
     * Gets the containerEvents value for this OrderContainer.
     * 
     * @return containerEvents
     */
    public com.freshdirect.routing.proxy.stub.transportation.ContainerEvent[] getContainerEvents() {
        return containerEvents;
    }


    /**
     * Sets the containerEvents value for this OrderContainer.
     * 
     * @param containerEvents
     */
    public void setContainerEvents(com.freshdirect.routing.proxy.stub.transportation.ContainerEvent[] containerEvents) {
        this.containerEvents = containerEvents;
    }

    public com.freshdirect.routing.proxy.stub.transportation.ContainerEvent getContainerEvents(int i) {
        return this.containerEvents[i];
    }

    public void setContainerEvents(int i, com.freshdirect.routing.proxy.stub.transportation.ContainerEvent _value) {
        this.containerEvents[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OrderContainer)) return false;
        OrderContainer other = (OrderContainer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.containerIdentity==null && other.getContainerIdentity()==null) || 
             (this.containerIdentity!=null &&
              this.containerIdentity.equals(other.getContainerIdentity()))) &&
            ((this.containerID==null && other.getContainerID()==null) || 
             (this.containerID!=null &&
              this.containerID.equals(other.getContainerID()))) &&
            ((this.containerType==null && other.getContainerType()==null) || 
             (this.containerType!=null &&
              this.containerType.equals(other.getContainerType()))) &&
            this.droppedOff == other.isDroppedOff() &&
            this.planned == other.isPlanned() &&
            ((this.barcode==null && other.getBarcode()==null) || 
             (this.barcode!=null &&
              this.barcode.equals(other.getBarcode()))) &&
            ((this.serviceType==null && other.getServiceType()==null) || 
             (this.serviceType!=null &&
              this.serviceType.equals(other.getServiceType()))) &&
            ((this.lineItems==null && other.getLineItems()==null) || 
             (this.lineItems!=null &&
              java.util.Arrays.equals(this.lineItems, other.getLineItems()))) &&
            ((this.containerEvents==null && other.getContainerEvents()==null) || 
             (this.containerEvents!=null &&
              java.util.Arrays.equals(this.containerEvents, other.getContainerEvents())));
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
        if (getContainerIdentity() != null) {
            _hashCode += getContainerIdentity().hashCode();
        }
        if (getContainerID() != null) {
            _hashCode += getContainerID().hashCode();
        }
        if (getContainerType() != null) {
            _hashCode += getContainerType().hashCode();
        }
        _hashCode += (isDroppedOff() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPlanned() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getBarcode() != null) {
            _hashCode += getBarcode().hashCode();
        }
        if (getServiceType() != null) {
            _hashCode += getServiceType().hashCode();
        }
        if (getLineItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLineItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLineItems(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getContainerEvents() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContainerEvents());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContainerEvents(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OrderContainer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "OrderContainer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("containerIdentity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "containerIdentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ContainerIdentity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("containerID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "containerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("containerType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "containerType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("droppedOff");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "droppedOff"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planned");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "planned"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("barcode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "barcode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "serviceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ContainerServiceType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "lineItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "LineItemContainer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("containerEvents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "containerEvents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "ContainerEvent"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
