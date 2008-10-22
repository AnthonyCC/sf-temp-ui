/**
 * GeocodeData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class GeocodeData  implements java.io.Serializable {
    private boolean matched;

    private com.freshdirect.routing.proxy.stub.roadnet.MapPoint coordinate;

    private com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult quality;

    private com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence confidence;

    private java.lang.String qualityDescription;

    private com.freshdirect.routing.proxy.stub.roadnet.MapArc[] candidates;

    public GeocodeData() {
    }

    public GeocodeData(
           boolean matched,
           com.freshdirect.routing.proxy.stub.roadnet.MapPoint coordinate,
           com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult quality,
           com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence confidence,
           java.lang.String qualityDescription,
           com.freshdirect.routing.proxy.stub.roadnet.MapArc[] candidates) {
           this.matched = matched;
           this.coordinate = coordinate;
           this.quality = quality;
           this.confidence = confidence;
           this.qualityDescription = qualityDescription;
           this.candidates = candidates;
    }


    /**
     * Gets the matched value for this GeocodeData.
     * 
     * @return matched
     */
    public boolean isMatched() {
        return matched;
    }


    /**
     * Sets the matched value for this GeocodeData.
     * 
     * @param matched
     */
    public void setMatched(boolean matched) {
        this.matched = matched;
    }


    /**
     * Gets the coordinate value for this GeocodeData.
     * 
     * @return coordinate
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint getCoordinate() {
        return coordinate;
    }


    /**
     * Sets the coordinate value for this GeocodeData.
     * 
     * @param coordinate
     */
    public void setCoordinate(com.freshdirect.routing.proxy.stub.roadnet.MapPoint coordinate) {
        this.coordinate = coordinate;
    }


    /**
     * Gets the quality value for this GeocodeData.
     * 
     * @return quality
     */
    public com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult getQuality() {
        return quality;
    }


    /**
     * Sets the quality value for this GeocodeData.
     * 
     * @param quality
     */
    public void setQuality(com.freshdirect.routing.proxy.stub.roadnet.GeocodeResult quality) {
        this.quality = quality;
    }


    /**
     * Gets the confidence value for this GeocodeData.
     * 
     * @return confidence
     */
    public com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence getConfidence() {
        return confidence;
    }


    /**
     * Sets the confidence value for this GeocodeData.
     * 
     * @param confidence
     */
    public void setConfidence(com.freshdirect.routing.proxy.stub.roadnet.GeocodeConfidence confidence) {
        this.confidence = confidence;
    }


    /**
     * Gets the qualityDescription value for this GeocodeData.
     * 
     * @return qualityDescription
     */
    public java.lang.String getQualityDescription() {
        return qualityDescription;
    }


    /**
     * Sets the qualityDescription value for this GeocodeData.
     * 
     * @param qualityDescription
     */
    public void setQualityDescription(java.lang.String qualityDescription) {
        this.qualityDescription = qualityDescription;
    }


    /**
     * Gets the candidates value for this GeocodeData.
     * 
     * @return candidates
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] getCandidates() {
        return candidates;
    }


    /**
     * Sets the candidates value for this GeocodeData.
     * 
     * @param candidates
     */
    public void setCandidates(com.freshdirect.routing.proxy.stub.roadnet.MapArc[] candidates) {
        this.candidates = candidates;
    }

    public com.freshdirect.routing.proxy.stub.roadnet.MapArc getCandidates(int i) {
        return this.candidates[i];
    }

    public void setCandidates(int i, com.freshdirect.routing.proxy.stub.roadnet.MapArc _value) {
        this.candidates[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GeocodeData)) return false;
        GeocodeData other = (GeocodeData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.matched == other.isMatched() &&
            ((this.coordinate==null && other.getCoordinate()==null) || 
             (this.coordinate!=null &&
              this.coordinate.equals(other.getCoordinate()))) &&
            ((this.quality==null && other.getQuality()==null) || 
             (this.quality!=null &&
              this.quality.equals(other.getQuality()))) &&
            ((this.confidence==null && other.getConfidence()==null) || 
             (this.confidence!=null &&
              this.confidence.equals(other.getConfidence()))) &&
            ((this.qualityDescription==null && other.getQualityDescription()==null) || 
             (this.qualityDescription!=null &&
              this.qualityDescription.equals(other.getQualityDescription()))) &&
            ((this.candidates==null && other.getCandidates()==null) || 
             (this.candidates!=null &&
              java.util.Arrays.equals(this.candidates, other.getCandidates())));
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
        _hashCode += (isMatched() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCoordinate() != null) {
            _hashCode += getCoordinate().hashCode();
        }
        if (getQuality() != null) {
            _hashCode += getQuality().hashCode();
        }
        if (getConfidence() != null) {
            _hashCode += getConfidence().hashCode();
        }
        if (getQualityDescription() != null) {
            _hashCode += getQualityDescription().hashCode();
        }
        if (getCandidates() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCandidates());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCandidates(), i);
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
        new org.apache.axis.description.TypeDesc(GeocodeData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "GeocodeData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matched");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "matched"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordinate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "coordinate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPoint"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quality");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "quality"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "GeocodeResult"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("confidence");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "confidence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "GeocodeConfidence"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qualityDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "qualityDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("candidates");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "candidates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapArc"));
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
