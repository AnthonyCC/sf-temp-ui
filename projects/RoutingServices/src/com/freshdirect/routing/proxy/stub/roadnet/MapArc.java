/**
 * MapArc.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapArc  extends com.freshdirect.routing.proxy.stub.roadnet.Arc  implements java.io.Serializable {
    private java.lang.String defaultName;

    private java.lang.String streetType;

    private java.lang.String preDirection;

    private java.lang.String postDirection;

    private java.lang.String leftLowRange;

    private java.lang.String leftHighRange;

    private java.lang.String rightLowRange;

    private java.lang.String rightHighRange;

    private java.lang.String region1;

    private java.lang.String region2;

    private java.lang.String region3;

    private java.lang.String postalCode;

    private java.lang.String country;

    private java.lang.String formattedRange;

    private java.lang.String formattedName;

    private java.lang.String formattedRegion;

    private com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] vertices;

    private int weight;

    private int height;

    public MapArc() {
    }

    public MapArc(
           int color,
           com.freshdirect.routing.proxy.stub.roadnet.MapArcIdentity arcIdentity,
           int lineWidth,
           java.lang.String defaultName,
           java.lang.String streetType,
           java.lang.String preDirection,
           java.lang.String postDirection,
           java.lang.String leftLowRange,
           java.lang.String leftHighRange,
           java.lang.String rightLowRange,
           java.lang.String rightHighRange,
           java.lang.String region1,
           java.lang.String region2,
           java.lang.String region3,
           java.lang.String postalCode,
           java.lang.String country,
           java.lang.String formattedRange,
           java.lang.String formattedName,
           java.lang.String formattedRegion,
           com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] vertices,
           int weight,
           int height) {
        super(
            color,
            arcIdentity,
            lineWidth);
        this.defaultName = defaultName;
        this.streetType = streetType;
        this.preDirection = preDirection;
        this.postDirection = postDirection;
        this.leftLowRange = leftLowRange;
        this.leftHighRange = leftHighRange;
        this.rightLowRange = rightLowRange;
        this.rightHighRange = rightHighRange;
        this.region1 = region1;
        this.region2 = region2;
        this.region3 = region3;
        this.postalCode = postalCode;
        this.country = country;
        this.formattedRange = formattedRange;
        this.formattedName = formattedName;
        this.formattedRegion = formattedRegion;
        this.vertices = vertices;
        this.weight = weight;
        this.height = height;
    }


    /**
     * Gets the defaultName value for this MapArc.
     * 
     * @return defaultName
     */
    public java.lang.String getDefaultName() {
        return defaultName;
    }


    /**
     * Sets the defaultName value for this MapArc.
     * 
     * @param defaultName
     */
    public void setDefaultName(java.lang.String defaultName) {
        this.defaultName = defaultName;
    }


    /**
     * Gets the streetType value for this MapArc.
     * 
     * @return streetType
     */
    public java.lang.String getStreetType() {
        return streetType;
    }


    /**
     * Sets the streetType value for this MapArc.
     * 
     * @param streetType
     */
    public void setStreetType(java.lang.String streetType) {
        this.streetType = streetType;
    }


    /**
     * Gets the preDirection value for this MapArc.
     * 
     * @return preDirection
     */
    public java.lang.String getPreDirection() {
        return preDirection;
    }


    /**
     * Sets the preDirection value for this MapArc.
     * 
     * @param preDirection
     */
    public void setPreDirection(java.lang.String preDirection) {
        this.preDirection = preDirection;
    }


    /**
     * Gets the postDirection value for this MapArc.
     * 
     * @return postDirection
     */
    public java.lang.String getPostDirection() {
        return postDirection;
    }


    /**
     * Sets the postDirection value for this MapArc.
     * 
     * @param postDirection
     */
    public void setPostDirection(java.lang.String postDirection) {
        this.postDirection = postDirection;
    }


    /**
     * Gets the leftLowRange value for this MapArc.
     * 
     * @return leftLowRange
     */
    public java.lang.String getLeftLowRange() {
        return leftLowRange;
    }


    /**
     * Sets the leftLowRange value for this MapArc.
     * 
     * @param leftLowRange
     */
    public void setLeftLowRange(java.lang.String leftLowRange) {
        this.leftLowRange = leftLowRange;
    }


    /**
     * Gets the leftHighRange value for this MapArc.
     * 
     * @return leftHighRange
     */
    public java.lang.String getLeftHighRange() {
        return leftHighRange;
    }


    /**
     * Sets the leftHighRange value for this MapArc.
     * 
     * @param leftHighRange
     */
    public void setLeftHighRange(java.lang.String leftHighRange) {
        this.leftHighRange = leftHighRange;
    }


    /**
     * Gets the rightLowRange value for this MapArc.
     * 
     * @return rightLowRange
     */
    public java.lang.String getRightLowRange() {
        return rightLowRange;
    }


    /**
     * Sets the rightLowRange value for this MapArc.
     * 
     * @param rightLowRange
     */
    public void setRightLowRange(java.lang.String rightLowRange) {
        this.rightLowRange = rightLowRange;
    }


    /**
     * Gets the rightHighRange value for this MapArc.
     * 
     * @return rightHighRange
     */
    public java.lang.String getRightHighRange() {
        return rightHighRange;
    }


    /**
     * Sets the rightHighRange value for this MapArc.
     * 
     * @param rightHighRange
     */
    public void setRightHighRange(java.lang.String rightHighRange) {
        this.rightHighRange = rightHighRange;
    }


    /**
     * Gets the region1 value for this MapArc.
     * 
     * @return region1
     */
    public java.lang.String getRegion1() {
        return region1;
    }


    /**
     * Sets the region1 value for this MapArc.
     * 
     * @param region1
     */
    public void setRegion1(java.lang.String region1) {
        this.region1 = region1;
    }


    /**
     * Gets the region2 value for this MapArc.
     * 
     * @return region2
     */
    public java.lang.String getRegion2() {
        return region2;
    }


    /**
     * Sets the region2 value for this MapArc.
     * 
     * @param region2
     */
    public void setRegion2(java.lang.String region2) {
        this.region2 = region2;
    }


    /**
     * Gets the region3 value for this MapArc.
     * 
     * @return region3
     */
    public java.lang.String getRegion3() {
        return region3;
    }


    /**
     * Sets the region3 value for this MapArc.
     * 
     * @param region3
     */
    public void setRegion3(java.lang.String region3) {
        this.region3 = region3;
    }


    /**
     * Gets the postalCode value for this MapArc.
     * 
     * @return postalCode
     */
    public java.lang.String getPostalCode() {
        return postalCode;
    }


    /**
     * Sets the postalCode value for this MapArc.
     * 
     * @param postalCode
     */
    public void setPostalCode(java.lang.String postalCode) {
        this.postalCode = postalCode;
    }


    /**
     * Gets the country value for this MapArc.
     * 
     * @return country
     */
    public java.lang.String getCountry() {
        return country;
    }


    /**
     * Sets the country value for this MapArc.
     * 
     * @param country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }


    /**
     * Gets the formattedRange value for this MapArc.
     * 
     * @return formattedRange
     */
    public java.lang.String getFormattedRange() {
        return formattedRange;
    }


    /**
     * Sets the formattedRange value for this MapArc.
     * 
     * @param formattedRange
     */
    public void setFormattedRange(java.lang.String formattedRange) {
        this.formattedRange = formattedRange;
    }


    /**
     * Gets the formattedName value for this MapArc.
     * 
     * @return formattedName
     */
    public java.lang.String getFormattedName() {
        return formattedName;
    }


    /**
     * Sets the formattedName value for this MapArc.
     * 
     * @param formattedName
     */
    public void setFormattedName(java.lang.String formattedName) {
        this.formattedName = formattedName;
    }


    /**
     * Gets the formattedRegion value for this MapArc.
     * 
     * @return formattedRegion
     */
    public java.lang.String getFormattedRegion() {
        return formattedRegion;
    }


    /**
     * Sets the formattedRegion value for this MapArc.
     * 
     * @param formattedRegion
     */
    public void setFormattedRegion(java.lang.String formattedRegion) {
        this.formattedRegion = formattedRegion;
    }


    /**
     * Gets the vertices value for this MapArc.
     * 
     * @return vertices
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] getVertices() {
        return vertices;
    }


    /**
     * Sets the vertices value for this MapArc.
     * 
     * @param vertices
     */
    public void setVertices(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] vertices) {
        this.vertices = vertices;
    }

    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint getVertices(int i) {
        return this.vertices[i];
    }

    public void setVertices(int i, com.freshdirect.routing.proxy.stub.roadnet.MapPoint _value) {
        this.vertices[i] = _value;
    }


    /**
     * Gets the weight value for this MapArc.
     * 
     * @return weight
     */
    public int getWeight() {
        return weight;
    }


    /**
     * Sets the weight value for this MapArc.
     * 
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }


    /**
     * Gets the height value for this MapArc.
     * 
     * @return height
     */
    public int getHeight() {
        return height;
    }


    /**
     * Sets the height value for this MapArc.
     * 
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapArc)) return false;
        MapArc other = (MapArc) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.defaultName==null && other.getDefaultName()==null) || 
             (this.defaultName!=null &&
              this.defaultName.equals(other.getDefaultName()))) &&
            ((this.streetType==null && other.getStreetType()==null) || 
             (this.streetType!=null &&
              this.streetType.equals(other.getStreetType()))) &&
            ((this.preDirection==null && other.getPreDirection()==null) || 
             (this.preDirection!=null &&
              this.preDirection.equals(other.getPreDirection()))) &&
            ((this.postDirection==null && other.getPostDirection()==null) || 
             (this.postDirection!=null &&
              this.postDirection.equals(other.getPostDirection()))) &&
            ((this.leftLowRange==null && other.getLeftLowRange()==null) || 
             (this.leftLowRange!=null &&
              this.leftLowRange.equals(other.getLeftLowRange()))) &&
            ((this.leftHighRange==null && other.getLeftHighRange()==null) || 
             (this.leftHighRange!=null &&
              this.leftHighRange.equals(other.getLeftHighRange()))) &&
            ((this.rightLowRange==null && other.getRightLowRange()==null) || 
             (this.rightLowRange!=null &&
              this.rightLowRange.equals(other.getRightLowRange()))) &&
            ((this.rightHighRange==null && other.getRightHighRange()==null) || 
             (this.rightHighRange!=null &&
              this.rightHighRange.equals(other.getRightHighRange()))) &&
            ((this.region1==null && other.getRegion1()==null) || 
             (this.region1!=null &&
              this.region1.equals(other.getRegion1()))) &&
            ((this.region2==null && other.getRegion2()==null) || 
             (this.region2!=null &&
              this.region2.equals(other.getRegion2()))) &&
            ((this.region3==null && other.getRegion3()==null) || 
             (this.region3!=null &&
              this.region3.equals(other.getRegion3()))) &&
            ((this.postalCode==null && other.getPostalCode()==null) || 
             (this.postalCode!=null &&
              this.postalCode.equals(other.getPostalCode()))) &&
            ((this.country==null && other.getCountry()==null) || 
             (this.country!=null &&
              this.country.equals(other.getCountry()))) &&
            ((this.formattedRange==null && other.getFormattedRange()==null) || 
             (this.formattedRange!=null &&
              this.formattedRange.equals(other.getFormattedRange()))) &&
            ((this.formattedName==null && other.getFormattedName()==null) || 
             (this.formattedName!=null &&
              this.formattedName.equals(other.getFormattedName()))) &&
            ((this.formattedRegion==null && other.getFormattedRegion()==null) || 
             (this.formattedRegion!=null &&
              this.formattedRegion.equals(other.getFormattedRegion()))) &&
            ((this.vertices==null && other.getVertices()==null) || 
             (this.vertices!=null &&
              java.util.Arrays.equals(this.vertices, other.getVertices()))) &&
            this.weight == other.getWeight() &&
            this.height == other.getHeight();
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
        if (getDefaultName() != null) {
            _hashCode += getDefaultName().hashCode();
        }
        if (getStreetType() != null) {
            _hashCode += getStreetType().hashCode();
        }
        if (getPreDirection() != null) {
            _hashCode += getPreDirection().hashCode();
        }
        if (getPostDirection() != null) {
            _hashCode += getPostDirection().hashCode();
        }
        if (getLeftLowRange() != null) {
            _hashCode += getLeftLowRange().hashCode();
        }
        if (getLeftHighRange() != null) {
            _hashCode += getLeftHighRange().hashCode();
        }
        if (getRightLowRange() != null) {
            _hashCode += getRightLowRange().hashCode();
        }
        if (getRightHighRange() != null) {
            _hashCode += getRightHighRange().hashCode();
        }
        if (getRegion1() != null) {
            _hashCode += getRegion1().hashCode();
        }
        if (getRegion2() != null) {
            _hashCode += getRegion2().hashCode();
        }
        if (getRegion3() != null) {
            _hashCode += getRegion3().hashCode();
        }
        if (getPostalCode() != null) {
            _hashCode += getPostalCode().hashCode();
        }
        if (getCountry() != null) {
            _hashCode += getCountry().hashCode();
        }
        if (getFormattedRange() != null) {
            _hashCode += getFormattedRange().hashCode();
        }
        if (getFormattedName() != null) {
            _hashCode += getFormattedName().hashCode();
        }
        if (getFormattedRegion() != null) {
            _hashCode += getFormattedRegion().hashCode();
        }
        if (getVertices() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVertices());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVertices(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getWeight();
        _hashCode += getHeight();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapArc.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapArc"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "defaultName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("streetType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "streetType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preDirection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "preDirection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postDirection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "postDirection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leftLowRange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "leftLowRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leftHighRange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "leftHighRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rightLowRange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "rightLowRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rightHighRange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "rightHighRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "region1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "region2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "region3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postalCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "postalCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "country"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formattedRange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "formattedRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formattedName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "formattedName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formattedRegion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "formattedRegion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vertices");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "vertices"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPoint"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("weight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "weight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("height");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "height"));
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
