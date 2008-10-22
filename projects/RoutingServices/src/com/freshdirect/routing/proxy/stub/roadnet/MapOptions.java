/**
 * MapOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public class MapOptions  implements java.io.Serializable {
    private int imageHeight;

    private int imageWidth;

    private boolean generateFullColorImage;

    private com.freshdirect.routing.proxy.stub.roadnet.MapPoint markerPoint;

    private boolean showMarker;

    private boolean showFrame;

    private boolean showTickMarks;

    private boolean showRailroads;

    private boolean showCityLabels;

    private boolean showCountyBorders;

    private boolean showStateBorders;

    private boolean showStreets;

    private boolean showStreetLabels;

    private boolean showLandmarks;

    private boolean fillLandmarks;

    private boolean showLandmarkLabels;

    private boolean showShields;

    private boolean showNodes;

    private boolean showImpasses;

    private boolean showOneWay;

    private boolean showPostalCodes;

    private boolean arcDetailData;

    private boolean compressArcDetailData;

    private int arcDetailDataRangeLimit;

    private boolean showDebugLatLngRange;

    private double fadeFactor;

    private com.freshdirect.routing.proxy.stub.roadnet.MapDetailLevel detailLevel;

    private java.lang.String arcOverrideKey;

    public MapOptions() {
    }

    public MapOptions(
           int imageHeight,
           int imageWidth,
           boolean generateFullColorImage,
           com.freshdirect.routing.proxy.stub.roadnet.MapPoint markerPoint,
           boolean showMarker,
           boolean showFrame,
           boolean showTickMarks,
           boolean showRailroads,
           boolean showCityLabels,
           boolean showCountyBorders,
           boolean showStateBorders,
           boolean showStreets,
           boolean showStreetLabels,
           boolean showLandmarks,
           boolean fillLandmarks,
           boolean showLandmarkLabels,
           boolean showShields,
           boolean showNodes,
           boolean showImpasses,
           boolean showOneWay,
           boolean showPostalCodes,
           boolean arcDetailData,
           boolean compressArcDetailData,
           int arcDetailDataRangeLimit,
           boolean showDebugLatLngRange,
           double fadeFactor,
           com.freshdirect.routing.proxy.stub.roadnet.MapDetailLevel detailLevel,
           java.lang.String arcOverrideKey) {
           this.imageHeight = imageHeight;
           this.imageWidth = imageWidth;
           this.generateFullColorImage = generateFullColorImage;
           this.markerPoint = markerPoint;
           this.showMarker = showMarker;
           this.showFrame = showFrame;
           this.showTickMarks = showTickMarks;
           this.showRailroads = showRailroads;
           this.showCityLabels = showCityLabels;
           this.showCountyBorders = showCountyBorders;
           this.showStateBorders = showStateBorders;
           this.showStreets = showStreets;
           this.showStreetLabels = showStreetLabels;
           this.showLandmarks = showLandmarks;
           this.fillLandmarks = fillLandmarks;
           this.showLandmarkLabels = showLandmarkLabels;
           this.showShields = showShields;
           this.showNodes = showNodes;
           this.showImpasses = showImpasses;
           this.showOneWay = showOneWay;
           this.showPostalCodes = showPostalCodes;
           this.arcDetailData = arcDetailData;
           this.compressArcDetailData = compressArcDetailData;
           this.arcDetailDataRangeLimit = arcDetailDataRangeLimit;
           this.showDebugLatLngRange = showDebugLatLngRange;
           this.fadeFactor = fadeFactor;
           this.detailLevel = detailLevel;
           this.arcOverrideKey = arcOverrideKey;
    }


    /**
     * Gets the imageHeight value for this MapOptions.
     * 
     * @return imageHeight
     */
    public int getImageHeight() {
        return imageHeight;
    }


    /**
     * Sets the imageHeight value for this MapOptions.
     * 
     * @param imageHeight
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }


    /**
     * Gets the imageWidth value for this MapOptions.
     * 
     * @return imageWidth
     */
    public int getImageWidth() {
        return imageWidth;
    }


    /**
     * Sets the imageWidth value for this MapOptions.
     * 
     * @param imageWidth
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }


    /**
     * Gets the generateFullColorImage value for this MapOptions.
     * 
     * @return generateFullColorImage
     */
    public boolean isGenerateFullColorImage() {
        return generateFullColorImage;
    }


    /**
     * Sets the generateFullColorImage value for this MapOptions.
     * 
     * @param generateFullColorImage
     */
    public void setGenerateFullColorImage(boolean generateFullColorImage) {
        this.generateFullColorImage = generateFullColorImage;
    }


    /**
     * Gets the markerPoint value for this MapOptions.
     * 
     * @return markerPoint
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint getMarkerPoint() {
        return markerPoint;
    }


    /**
     * Sets the markerPoint value for this MapOptions.
     * 
     * @param markerPoint
     */
    public void setMarkerPoint(com.freshdirect.routing.proxy.stub.roadnet.MapPoint markerPoint) {
        this.markerPoint = markerPoint;
    }


    /**
     * Gets the showMarker value for this MapOptions.
     * 
     * @return showMarker
     */
    public boolean isShowMarker() {
        return showMarker;
    }


    /**
     * Sets the showMarker value for this MapOptions.
     * 
     * @param showMarker
     */
    public void setShowMarker(boolean showMarker) {
        this.showMarker = showMarker;
    }


    /**
     * Gets the showFrame value for this MapOptions.
     * 
     * @return showFrame
     */
    public boolean isShowFrame() {
        return showFrame;
    }


    /**
     * Sets the showFrame value for this MapOptions.
     * 
     * @param showFrame
     */
    public void setShowFrame(boolean showFrame) {
        this.showFrame = showFrame;
    }


    /**
     * Gets the showTickMarks value for this MapOptions.
     * 
     * @return showTickMarks
     */
    public boolean isShowTickMarks() {
        return showTickMarks;
    }


    /**
     * Sets the showTickMarks value for this MapOptions.
     * 
     * @param showTickMarks
     */
    public void setShowTickMarks(boolean showTickMarks) {
        this.showTickMarks = showTickMarks;
    }


    /**
     * Gets the showRailroads value for this MapOptions.
     * 
     * @return showRailroads
     */
    public boolean isShowRailroads() {
        return showRailroads;
    }


    /**
     * Sets the showRailroads value for this MapOptions.
     * 
     * @param showRailroads
     */
    public void setShowRailroads(boolean showRailroads) {
        this.showRailroads = showRailroads;
    }


    /**
     * Gets the showCityLabels value for this MapOptions.
     * 
     * @return showCityLabels
     */
    public boolean isShowCityLabels() {
        return showCityLabels;
    }


    /**
     * Sets the showCityLabels value for this MapOptions.
     * 
     * @param showCityLabels
     */
    public void setShowCityLabels(boolean showCityLabels) {
        this.showCityLabels = showCityLabels;
    }


    /**
     * Gets the showCountyBorders value for this MapOptions.
     * 
     * @return showCountyBorders
     */
    public boolean isShowCountyBorders() {
        return showCountyBorders;
    }


    /**
     * Sets the showCountyBorders value for this MapOptions.
     * 
     * @param showCountyBorders
     */
    public void setShowCountyBorders(boolean showCountyBorders) {
        this.showCountyBorders = showCountyBorders;
    }


    /**
     * Gets the showStateBorders value for this MapOptions.
     * 
     * @return showStateBorders
     */
    public boolean isShowStateBorders() {
        return showStateBorders;
    }


    /**
     * Sets the showStateBorders value for this MapOptions.
     * 
     * @param showStateBorders
     */
    public void setShowStateBorders(boolean showStateBorders) {
        this.showStateBorders = showStateBorders;
    }


    /**
     * Gets the showStreets value for this MapOptions.
     * 
     * @return showStreets
     */
    public boolean isShowStreets() {
        return showStreets;
    }


    /**
     * Sets the showStreets value for this MapOptions.
     * 
     * @param showStreets
     */
    public void setShowStreets(boolean showStreets) {
        this.showStreets = showStreets;
    }


    /**
     * Gets the showStreetLabels value for this MapOptions.
     * 
     * @return showStreetLabels
     */
    public boolean isShowStreetLabels() {
        return showStreetLabels;
    }


    /**
     * Sets the showStreetLabels value for this MapOptions.
     * 
     * @param showStreetLabels
     */
    public void setShowStreetLabels(boolean showStreetLabels) {
        this.showStreetLabels = showStreetLabels;
    }


    /**
     * Gets the showLandmarks value for this MapOptions.
     * 
     * @return showLandmarks
     */
    public boolean isShowLandmarks() {
        return showLandmarks;
    }


    /**
     * Sets the showLandmarks value for this MapOptions.
     * 
     * @param showLandmarks
     */
    public void setShowLandmarks(boolean showLandmarks) {
        this.showLandmarks = showLandmarks;
    }


    /**
     * Gets the fillLandmarks value for this MapOptions.
     * 
     * @return fillLandmarks
     */
    public boolean isFillLandmarks() {
        return fillLandmarks;
    }


    /**
     * Sets the fillLandmarks value for this MapOptions.
     * 
     * @param fillLandmarks
     */
    public void setFillLandmarks(boolean fillLandmarks) {
        this.fillLandmarks = fillLandmarks;
    }


    /**
     * Gets the showLandmarkLabels value for this MapOptions.
     * 
     * @return showLandmarkLabels
     */
    public boolean isShowLandmarkLabels() {
        return showLandmarkLabels;
    }


    /**
     * Sets the showLandmarkLabels value for this MapOptions.
     * 
     * @param showLandmarkLabels
     */
    public void setShowLandmarkLabels(boolean showLandmarkLabels) {
        this.showLandmarkLabels = showLandmarkLabels;
    }


    /**
     * Gets the showShields value for this MapOptions.
     * 
     * @return showShields
     */
    public boolean isShowShields() {
        return showShields;
    }


    /**
     * Sets the showShields value for this MapOptions.
     * 
     * @param showShields
     */
    public void setShowShields(boolean showShields) {
        this.showShields = showShields;
    }


    /**
     * Gets the showNodes value for this MapOptions.
     * 
     * @return showNodes
     */
    public boolean isShowNodes() {
        return showNodes;
    }


    /**
     * Sets the showNodes value for this MapOptions.
     * 
     * @param showNodes
     */
    public void setShowNodes(boolean showNodes) {
        this.showNodes = showNodes;
    }


    /**
     * Gets the showImpasses value for this MapOptions.
     * 
     * @return showImpasses
     */
    public boolean isShowImpasses() {
        return showImpasses;
    }


    /**
     * Sets the showImpasses value for this MapOptions.
     * 
     * @param showImpasses
     */
    public void setShowImpasses(boolean showImpasses) {
        this.showImpasses = showImpasses;
    }


    /**
     * Gets the showOneWay value for this MapOptions.
     * 
     * @return showOneWay
     */
    public boolean isShowOneWay() {
        return showOneWay;
    }


    /**
     * Sets the showOneWay value for this MapOptions.
     * 
     * @param showOneWay
     */
    public void setShowOneWay(boolean showOneWay) {
        this.showOneWay = showOneWay;
    }


    /**
     * Gets the showPostalCodes value for this MapOptions.
     * 
     * @return showPostalCodes
     */
    public boolean isShowPostalCodes() {
        return showPostalCodes;
    }


    /**
     * Sets the showPostalCodes value for this MapOptions.
     * 
     * @param showPostalCodes
     */
    public void setShowPostalCodes(boolean showPostalCodes) {
        this.showPostalCodes = showPostalCodes;
    }


    /**
     * Gets the arcDetailData value for this MapOptions.
     * 
     * @return arcDetailData
     */
    public boolean isArcDetailData() {
        return arcDetailData;
    }


    /**
     * Sets the arcDetailData value for this MapOptions.
     * 
     * @param arcDetailData
     */
    public void setArcDetailData(boolean arcDetailData) {
        this.arcDetailData = arcDetailData;
    }


    /**
     * Gets the compressArcDetailData value for this MapOptions.
     * 
     * @return compressArcDetailData
     */
    public boolean isCompressArcDetailData() {
        return compressArcDetailData;
    }


    /**
     * Sets the compressArcDetailData value for this MapOptions.
     * 
     * @param compressArcDetailData
     */
    public void setCompressArcDetailData(boolean compressArcDetailData) {
        this.compressArcDetailData = compressArcDetailData;
    }


    /**
     * Gets the arcDetailDataRangeLimit value for this MapOptions.
     * 
     * @return arcDetailDataRangeLimit
     */
    public int getArcDetailDataRangeLimit() {
        return arcDetailDataRangeLimit;
    }


    /**
     * Sets the arcDetailDataRangeLimit value for this MapOptions.
     * 
     * @param arcDetailDataRangeLimit
     */
    public void setArcDetailDataRangeLimit(int arcDetailDataRangeLimit) {
        this.arcDetailDataRangeLimit = arcDetailDataRangeLimit;
    }


    /**
     * Gets the showDebugLatLngRange value for this MapOptions.
     * 
     * @return showDebugLatLngRange
     */
    public boolean isShowDebugLatLngRange() {
        return showDebugLatLngRange;
    }


    /**
     * Sets the showDebugLatLngRange value for this MapOptions.
     * 
     * @param showDebugLatLngRange
     */
    public void setShowDebugLatLngRange(boolean showDebugLatLngRange) {
        this.showDebugLatLngRange = showDebugLatLngRange;
    }


    /**
     * Gets the fadeFactor value for this MapOptions.
     * 
     * @return fadeFactor
     */
    public double getFadeFactor() {
        return fadeFactor;
    }


    /**
     * Sets the fadeFactor value for this MapOptions.
     * 
     * @param fadeFactor
     */
    public void setFadeFactor(double fadeFactor) {
        this.fadeFactor = fadeFactor;
    }


    /**
     * Gets the detailLevel value for this MapOptions.
     * 
     * @return detailLevel
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapDetailLevel getDetailLevel() {
        return detailLevel;
    }


    /**
     * Sets the detailLevel value for this MapOptions.
     * 
     * @param detailLevel
     */
    public void setDetailLevel(com.freshdirect.routing.proxy.stub.roadnet.MapDetailLevel detailLevel) {
        this.detailLevel = detailLevel;
    }


    /**
     * Gets the arcOverrideKey value for this MapOptions.
     * 
     * @return arcOverrideKey
     */
    public java.lang.String getArcOverrideKey() {
        return arcOverrideKey;
    }


    /**
     * Sets the arcOverrideKey value for this MapOptions.
     * 
     * @param arcOverrideKey
     */
    public void setArcOverrideKey(java.lang.String arcOverrideKey) {
        this.arcOverrideKey = arcOverrideKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapOptions)) return false;
        MapOptions other = (MapOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.imageHeight == other.getImageHeight() &&
            this.imageWidth == other.getImageWidth() &&
            this.generateFullColorImage == other.isGenerateFullColorImage() &&
            ((this.markerPoint==null && other.getMarkerPoint()==null) || 
             (this.markerPoint!=null &&
              this.markerPoint.equals(other.getMarkerPoint()))) &&
            this.showMarker == other.isShowMarker() &&
            this.showFrame == other.isShowFrame() &&
            this.showTickMarks == other.isShowTickMarks() &&
            this.showRailroads == other.isShowRailroads() &&
            this.showCityLabels == other.isShowCityLabels() &&
            this.showCountyBorders == other.isShowCountyBorders() &&
            this.showStateBorders == other.isShowStateBorders() &&
            this.showStreets == other.isShowStreets() &&
            this.showStreetLabels == other.isShowStreetLabels() &&
            this.showLandmarks == other.isShowLandmarks() &&
            this.fillLandmarks == other.isFillLandmarks() &&
            this.showLandmarkLabels == other.isShowLandmarkLabels() &&
            this.showShields == other.isShowShields() &&
            this.showNodes == other.isShowNodes() &&
            this.showImpasses == other.isShowImpasses() &&
            this.showOneWay == other.isShowOneWay() &&
            this.showPostalCodes == other.isShowPostalCodes() &&
            this.arcDetailData == other.isArcDetailData() &&
            this.compressArcDetailData == other.isCompressArcDetailData() &&
            this.arcDetailDataRangeLimit == other.getArcDetailDataRangeLimit() &&
            this.showDebugLatLngRange == other.isShowDebugLatLngRange() &&
            this.fadeFactor == other.getFadeFactor() &&
            ((this.detailLevel==null && other.getDetailLevel()==null) || 
             (this.detailLevel!=null &&
              this.detailLevel.equals(other.getDetailLevel()))) &&
            ((this.arcOverrideKey==null && other.getArcOverrideKey()==null) || 
             (this.arcOverrideKey!=null &&
              this.arcOverrideKey.equals(other.getArcOverrideKey())));
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
        _hashCode += getImageHeight();
        _hashCode += getImageWidth();
        _hashCode += (isGenerateFullColorImage() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMarkerPoint() != null) {
            _hashCode += getMarkerPoint().hashCode();
        }
        _hashCode += (isShowMarker() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowFrame() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowTickMarks() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowRailroads() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowCityLabels() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowCountyBorders() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowStateBorders() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowStreets() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowStreetLabels() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowLandmarks() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFillLandmarks() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowLandmarkLabels() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowShields() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowNodes() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowImpasses() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowOneWay() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShowPostalCodes() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isArcDetailData() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCompressArcDetailData() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getArcDetailDataRangeLimit();
        _hashCode += (isShowDebugLatLngRange() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Double(getFadeFactor()).hashCode();
        if (getDetailLevel() != null) {
            _hashCode += getDetailLevel().hashCode();
        }
        if (getArcOverrideKey() != null) {
            _hashCode += getArcOverrideKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "imageHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageWidth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "imageWidth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generateFullColorImage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "generateFullColorImage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("markerPoint");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "markerPoint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapPoint"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showMarker");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showMarker"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showFrame");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showFrame"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showTickMarks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showTickMarks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showRailroads");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showRailroads"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showCityLabels");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showCityLabels"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showCountyBorders");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showCountyBorders"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showStateBorders");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showStateBorders"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showStreets");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showStreets"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showStreetLabels");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showStreetLabels"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showLandmarks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showLandmarks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fillLandmarks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "fillLandmarks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showLandmarkLabels");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showLandmarkLabels"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showShields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showShields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showNodes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showNodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showImpasses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showImpasses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showOneWay");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showOneWay"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showPostalCodes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showPostalCodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arcDetailData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "arcDetailData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compressArcDetailData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "compressArcDetailData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arcDetailDataRangeLimit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "arcDetailDataRangeLimit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showDebugLatLngRange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "showDebugLatLngRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fadeFactor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "fadeFactor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detailLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "detailLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "MapDetailLevel"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arcOverrideKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/RouteNetWebService", "arcOverrideKey"));
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
