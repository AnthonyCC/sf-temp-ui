
/**
 * RouteNetWebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.routing.proxy.stub.roadnet;

    /**
     *  RouteNetWebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class RouteNetWebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public RouteNetWebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public RouteNetWebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for distanceToClosestCity method
            * override this method for handling normal response from distanceToClosestCity operation
            */
           public void receiveResultdistanceToClosestCity(
                    com.freshdirect.routing.proxy.stub.roadnet.DistanceToClosestCityResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from distanceToClosestCity operation
           */
            public void receiveErrordistanceToClosestCity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for formatPositionText method
            * override this method for handling normal response from formatPositionText operation
            */
           public void receiveResultformatPositionText(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from formatPositionText operation
           */
            public void receiveErrorformatPositionText(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for versionInformation method
            * override this method for handling normal response from versionInformation operation
            */
           public void receiveResultversionInformation(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from versionInformation operation
           */
            public void receiveErrorversionInformation(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for findMapArcs method
            * override this method for handling normal response from findMapArcs operation
            */
           public void receiveResultfindMapArcs(
                    com.freshdirect.routing.proxy.stub.roadnet.MapArc[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findMapArcs operation
           */
            public void receiveErrorfindMapArcs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mapCenter method
            * override this method for handling normal response from mapCenter operation
            */
           public void receiveResultmapCenter(
                    com.freshdirect.routing.proxy.stub.roadnet.MapData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mapCenter operation
           */
            public void receiveErrormapCenter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for batchGeocode method
            * override this method for handling normal response from batchGeocode operation
            */
           public void receiveResultbatchGeocode(
                    com.freshdirect.routing.proxy.stub.roadnet.GeocodeData[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from batchGeocode operation
           */
            public void receiveErrorbatchGeocode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buildMatrix method
            * override this method for handling normal response from buildMatrix operation
            */
           public void receiveResultbuildMatrix(
                    com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buildMatrix operation
           */
            public void receiveErrorbuildMatrix(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for map method
            * override this method for handling normal response from map operation
            */
           public void receiveResultmap(
                    com.freshdirect.routing.proxy.stub.roadnet.MapData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from map operation
           */
            public void receiveErrormap(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveArcOverridesEx method
            * override this method for handling normal response from saveArcOverridesEx operation
            */
           public void receiveResultsaveArcOverridesEx(
                    int result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveArcOverridesEx operation
           */
            public void receiveErrorsaveArcOverridesEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buildDriverDirections method
            * override this method for handling normal response from buildDriverDirections operation
            */
           public void receiveResultbuildDriverDirections(
                    com.freshdirect.routing.proxy.stub.roadnet.DirectionData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buildDriverDirections operation
           */
            public void receiveErrorbuildDriverDirections(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mapZoomIn method
            * override this method for handling normal response from mapZoomIn operation
            */
           public void receiveResultmapZoomIn(
                    com.freshdirect.routing.proxy.stub.roadnet.MapData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mapZoomIn operation
           */
            public void receiveErrormapZoomIn(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buildPathEx method
            * override this method for handling normal response from buildPathEx operation
            */
           public void receiveResultbuildPathEx(
                    com.freshdirect.routing.proxy.stub.roadnet.PathData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buildPathEx operation
           */
            public void receiveErrorbuildPathEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mapZoomRange method
            * override this method for handling normal response from mapZoomRange operation
            */
           public void receiveResultmapZoomRange(
                    com.freshdirect.routing.proxy.stub.roadnet.MapData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mapZoomRange operation
           */
            public void receiveErrormapZoomRange(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for findMapArcsEx method
            * override this method for handling normal response from findMapArcsEx operation
            */
           public void receiveResultfindMapArcsEx(
                    com.freshdirect.routing.proxy.stub.roadnet.MapArc[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findMapArcsEx operation
           */
            public void receiveErrorfindMapArcsEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for geocode method
            * override this method for handling normal response from geocode operation
            */
           public void receiveResultgeocode(
                    com.freshdirect.routing.proxy.stub.roadnet.GeocodeData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from geocode operation
           */
            public void receiveErrorgeocode(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mapGoToPlace method
            * override this method for handling normal response from mapGoToPlace operation
            */
           public void receiveResultmapGoToPlace(
                    com.freshdirect.routing.proxy.stub.roadnet.MapData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mapGoToPlace operation
           */
            public void receiveErrormapGoToPlace(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for enableAllArcsByExtents method
            * override this method for handling normal response from enableAllArcsByExtents operation
            */
           public void receiveResultenableAllArcsByExtents(
                    int result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from enableAllArcsByExtents operation
           */
            public void receiveErrorenableAllArcsByExtents(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pOIShowEnabled method
            * override this method for handling normal response from pOIShowEnabled operation
            */
           public void receiveResultpOIShowEnabled(
                    boolean result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pOIShowEnabled operation
           */
            public void receiveErrorpOIShowEnabled(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mapZoomOut method
            * override this method for handling normal response from mapZoomOut operation
            */
           public void receiveResultmapZoomOut(
                    com.freshdirect.routing.proxy.stub.roadnet.MapData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mapZoomOut operation
           */
            public void receiveErrormapZoomOut(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for geocodeEx method
            * override this method for handling normal response from geocodeEx operation
            */
           public void receiveResultgeocodeEx(
                    com.freshdirect.routing.proxy.stub.roadnet.GeocodeData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from geocodeEx operation
           */
            public void receiveErrorgeocodeEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for nop method
            * override this method for handling normal response from nop operation
            */
           public void receiveResultnop(
                    int result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from nop operation
           */
            public void receiveErrornop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mapRetrieveMapDataVersion method
            * override this method for handling normal response from mapRetrieveMapDataVersion operation
            */
           public void receiveResultmapRetrieveMapDataVersion(
                    com.freshdirect.routing.proxy.stub.roadnet.MapDataVersion result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mapRetrieveMapDataVersion operation
           */
            public void receiveErrormapRetrieveMapDataVersion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for findMapRegionDetails method
            * override this method for handling normal response from findMapRegionDetails operation
            */
           public void receiveResultfindMapRegionDetails(
                    com.freshdirect.routing.proxy.stub.roadnet.RegionDetail[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findMapRegionDetails operation
           */
            public void receiveErrorfindMapRegionDetails(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveArcOverrides method
            * override this method for handling normal response from saveArcOverrides operation
            */
           public void receiveResultsaveArcOverrides(
                    int result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveArcOverrides operation
           */
            public void receiveErrorsaveArcOverrides(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buildPath method
            * override this method for handling normal response from buildPath operation
            */
           public void receiveResultbuildPath(
                    com.freshdirect.routing.proxy.stub.roadnet.PathData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buildPath operation
           */
            public void receiveErrorbuildPath(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buildCompressedPath method
            * override this method for handling normal response from buildCompressedPath operation
            */
           public void receiveResultbuildCompressedPath(
                    com.freshdirect.routing.proxy.stub.roadnet.CompressedPathData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buildCompressedPath operation
           */
            public void receiveErrorbuildCompressedPath(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buildDriverDirectionsEx method
            * override this method for handling normal response from buildDriverDirectionsEx operation
            */
           public void receiveResultbuildDriverDirectionsEx(
                    com.freshdirect.routing.proxy.stub.roadnet.DirectionData result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buildDriverDirectionsEx operation
           */
            public void receiveErrorbuildDriverDirectionsEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buildMatrixEx method
            * override this method for handling normal response from buildMatrixEx operation
            */
           public void receiveResultbuildMatrixEx(
                    com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buildMatrixEx operation
           */
            public void receiveErrorbuildMatrixEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for mapConvertToLatLong method
            * override this method for handling normal response from mapConvertToLatLong operation
            */
           public void receiveResultmapConvertToLatLong(
                    com.freshdirect.routing.proxy.stub.roadnet.MapPoint result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from mapConvertToLatLong operation
           */
            public void receiveErrormapConvertToLatLong(java.lang.Exception e) {
            }
                


    }
    