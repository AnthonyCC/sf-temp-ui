/**
 * RouteNetPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public interface RouteNetPortType extends java.rmi.Remote {

    /**
     * Service definition of function ns1__Nop
     */
    public java.lang.Integer nop() throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__VersionInformation
     */
    public java.lang.String versionInformation() throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__Map
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapData map(com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria, com.freshdirect.routing.proxy.stub.roadnet.MapOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__MapZoomIn
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomIn(int x, int y, int zoomDegree, com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria, com.freshdirect.routing.proxy.stub.roadnet.MapOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__MapZoomOut
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomOut(int x, int y, int zoomDegree, com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria, com.freshdirect.routing.proxy.stub.roadnet.MapOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__MapCenter
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapData mapCenter(int x, int y, com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria, com.freshdirect.routing.proxy.stub.roadnet.MapOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__MapZoomRange
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomRange(int ulScreenX, int ulScreenY, int lrScreenX, int lrScreenY, com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria, com.freshdirect.routing.proxy.stub.roadnet.MapOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__MapGoToPlace
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapData mapGoToPlace(java.lang.String place, com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria, com.freshdirect.routing.proxy.stub.roadnet.MapOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__FindMapArcs
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] findMapArcs(com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__FindMapArcsEx
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] findMapArcsEx(com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria, com.freshdirect.routing.proxy.stub.roadnet.FindMapArcOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__FindMapRegionDetails
     */
    public com.freshdirect.routing.proxy.stub.roadnet.RegionDetail[] findMapRegionDetails(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] pts) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__MapConvertToLatLong
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapPoint mapConvertToLatLong(int x, int y, com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria, com.freshdirect.routing.proxy.stub.roadnet.MapOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__MapRetrieveMapDataVersion
     */
    public com.freshdirect.routing.proxy.stub.roadnet.MapDataVersion mapRetrieveMapDataVersion() throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__FormatPositionText
     */
    public java.lang.String formatPositionText(com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt, boolean useArcDetail, boolean useKilometers) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveArcOverrides
     */
    public int saveArcOverrides(java.lang.String key, com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] overrides) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__EnableAllArcsByExtents
     */
    public int enableAllArcsByExtents(java.lang.String key, com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__Geocode
     */
    public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData geocode(com.freshdirect.routing.proxy.stub.roadnet.Address address) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__GeocodeEx
     */
    public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData geocodeEx(com.freshdirect.routing.proxy.stub.roadnet.Address address, com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__BatchGeocode
     */
    public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData[] batchGeocode(com.freshdirect.routing.proxy.stub.roadnet.Address[] adresses, com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__BuildMatrix
     */
    public com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] buildMatrix(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__BuildMatrixEx
     */
    public com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] buildMatrixEx(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points, com.freshdirect.routing.proxy.stub.roadnet.PathOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__BuildPath
     */
    public com.freshdirect.routing.proxy.stub.roadnet.PathData buildPath(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__BuildPathEx
     */
    public com.freshdirect.routing.proxy.stub.roadnet.PathData buildPathEx(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations, com.freshdirect.routing.proxy.stub.roadnet.PathOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__BuildDriverDirections
     */
    public com.freshdirect.routing.proxy.stub.roadnet.DirectionData buildDriverDirections(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations, com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__BuildDriverDirectionsEx
     */
    public com.freshdirect.routing.proxy.stub.roadnet.DirectionData buildDriverDirectionsEx(com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations, com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options, com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions) throws java.rmi.RemoteException;
}
