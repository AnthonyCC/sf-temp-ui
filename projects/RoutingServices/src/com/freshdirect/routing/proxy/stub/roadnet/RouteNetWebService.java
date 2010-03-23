

/**
 * RouteNetWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.routing.proxy.stub.roadnet;

    /*
     *  RouteNetWebService java interface
     */

    public interface RouteNetWebService {
          

        /**
          * Auto generated method signature
          * Service definition of function ns1__DistanceToClosestCity
                    * @param distanceToClosestCity52
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DistanceToClosestCityResult distanceToClosestCity(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt53)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DistanceToClosestCity
                * @param distanceToClosestCity52
            
          */
        public void startdistanceToClosestCity(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt53,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FormatPositionText
                    * @param formatPositionText56
                
         */

         
                     public java.lang.String formatPositionText(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt57,boolean useArcDetail58,boolean useKilometers59)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FormatPositionText
                * @param formatPositionText56
            
          */
        public void startformatPositionText(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt57,boolean useArcDetail58,boolean useKilometers59,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation62
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation62
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapArcs
                    * @param findMapArcs65
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] findMapArcs(

                        com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria66)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapArcs
                * @param findMapArcs65
            
          */
        public void startfindMapArcs(

            com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria66,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapCenter
                    * @param mapCenter69
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapCenter(

                        int x70,int y71,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria72,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options73)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapCenter
                * @param mapCenter69
            
          */
        public void startmapCenter(

            int x70,int y71,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria72,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options73,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BatchGeocode
                    * @param batchGeocode76
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData[] batchGeocode(

                        com.freshdirect.routing.proxy.stub.roadnet.Address[] adresses77,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options78)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BatchGeocode
                * @param batchGeocode76
            
          */
        public void startbatchGeocode(

            com.freshdirect.routing.proxy.stub.roadnet.Address[] adresses77,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options78,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildMatrix
                    * @param buildMatrix81
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] buildMatrix(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points82)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildMatrix
                * @param buildMatrix81
            
          */
        public void startbuildMatrix(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points82,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Map
                    * @param map85
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData map(

                        com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria86,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options87)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Map
                * @param map85
            
          */
        public void startmap(

            com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria86,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options87,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveArcOverridesEx
                    * @param saveArcOverridesEx90
                
         */

         
                     public int saveArcOverridesEx(

                        java.lang.String key91,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] overrides92,com.freshdirect.routing.proxy.stub.roadnet.ArcOverrideSaveOptions options93)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveArcOverridesEx
                * @param saveArcOverridesEx90
            
          */
        public void startsaveArcOverridesEx(

            java.lang.String key91,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] overrides92,com.freshdirect.routing.proxy.stub.roadnet.ArcOverrideSaveOptions options93,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDriverDirections
                    * @param buildDriverDirections96
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DirectionData buildDriverDirections(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations97,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options98)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDriverDirections
                * @param buildDriverDirections96
            
          */
        public void startbuildDriverDirections(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations97,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options98,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomIn
                    * @param mapZoomIn101
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomIn(

                        int x102,int y103,int zoomDegree104,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria105,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options106)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomIn
                * @param mapZoomIn101
            
          */
        public void startmapZoomIn(

            int x102,int y103,int zoomDegree104,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria105,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options106,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildPathEx
                    * @param buildPathEx109
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.PathData buildPathEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations110,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options111)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildPathEx
                * @param buildPathEx109
            
          */
        public void startbuildPathEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations110,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options111,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomRange
                    * @param mapZoomRange114
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomRange(

                        int ulScreenX115,int ulScreenY116,int lrScreenX117,int lrScreenY118,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria119,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options120)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomRange
                * @param mapZoomRange114
            
          */
        public void startmapZoomRange(

            int ulScreenX115,int ulScreenY116,int lrScreenX117,int lrScreenY118,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria119,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options120,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapArcsEx
                    * @param findMapArcsEx123
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] findMapArcsEx(

                        com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria124,com.freshdirect.routing.proxy.stub.roadnet.FindMapArcOptions options125)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapArcsEx
                * @param findMapArcsEx123
            
          */
        public void startfindMapArcsEx(

            com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria124,com.freshdirect.routing.proxy.stub.roadnet.FindMapArcOptions options125,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Geocode
                    * @param geocode128
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData geocode(

                        com.freshdirect.routing.proxy.stub.roadnet.Address address129)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Geocode
                * @param geocode128
            
          */
        public void startgeocode(

            com.freshdirect.routing.proxy.stub.roadnet.Address address129,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapGoToPlace
                    * @param mapGoToPlace132
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapGoToPlace(

                        java.lang.String place133,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria134,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options135)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapGoToPlace
                * @param mapGoToPlace132
            
          */
        public void startmapGoToPlace(

            java.lang.String place133,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria134,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options135,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__EnableAllArcsByExtents
                    * @param enableAllArcsByExtents138
                
         */

         
                     public int enableAllArcsByExtents(

                        java.lang.String key139,com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents140)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__EnableAllArcsByExtents
                * @param enableAllArcsByExtents138
            
          */
        public void startenableAllArcsByExtents(

            java.lang.String key139,com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents140,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__POIShowEnabled
                    * @param pOIShowEnabled143
                
         */

         
                     public boolean pOIShowEnabled(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__POIShowEnabled
                * @param pOIShowEnabled143
            
          */
        public void startpOIShowEnabled(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomOut
                    * @param mapZoomOut146
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomOut(

                        int x147,int y148,int zoomDegree149,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria150,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options151)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomOut
                * @param mapZoomOut146
            
          */
        public void startmapZoomOut(

            int x147,int y148,int zoomDegree149,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria150,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options151,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__GeocodeEx
                    * @param geocodeEx154
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData geocodeEx(

                        com.freshdirect.routing.proxy.stub.roadnet.Address address155,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options156)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__GeocodeEx
                * @param geocodeEx154
            
          */
        public void startgeocodeEx(

            com.freshdirect.routing.proxy.stub.roadnet.Address address155,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options156,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop159
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop159
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapRetrieveMapDataVersion
                    * @param mapRetrieveMapDataVersion162
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapDataVersion mapRetrieveMapDataVersion(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapRetrieveMapDataVersion
                * @param mapRetrieveMapDataVersion162
            
          */
        public void startmapRetrieveMapDataVersion(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapRegionDetails
                    * @param findMapRegionDetails165
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.RegionDetail[] findMapRegionDetails(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] pts166)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapRegionDetails
                * @param findMapRegionDetails165
            
          */
        public void startfindMapRegionDetails(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] pts166,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveArcOverrides
                    * @param saveArcOverrides169
                
         */

         
                     public int saveArcOverrides(

                        java.lang.String str170,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] vec171)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveArcOverrides
                * @param saveArcOverrides169
            
          */
        public void startsaveArcOverrides(

            java.lang.String str170,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] vec171,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildPath
                    * @param buildPath174
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.PathData buildPath(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations175)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildPath
                * @param buildPath174
            
          */
        public void startbuildPath(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations175,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildCompressedPath
                    * @param buildCompressedPath178
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.CompressedPathData buildCompressedPath(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations179,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions180,com.freshdirect.routing.proxy.stub.roadnet.PathCompressionOptions pathCompressionOptions181)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildCompressedPath
                * @param buildCompressedPath178
            
          */
        public void startbuildCompressedPath(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations179,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions180,com.freshdirect.routing.proxy.stub.roadnet.PathCompressionOptions pathCompressionOptions181,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDriverDirectionsEx
                    * @param buildDriverDirectionsEx184
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DirectionData buildDriverDirectionsEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations185,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options186,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions187)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDriverDirectionsEx
                * @param buildDriverDirectionsEx184
            
          */
        public void startbuildDriverDirectionsEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations185,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options186,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions187,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildMatrixEx
                    * @param buildMatrixEx190
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] buildMatrixEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points191,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options192)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildMatrixEx
                * @param buildMatrixEx190
            
          */
        public void startbuildMatrixEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points191,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options192,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapConvertToLatLong
                    * @param mapConvertToLatLong195
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapPoint mapConvertToLatLong(

                        int x196,int y197,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria198,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options199)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapConvertToLatLong
                * @param mapConvertToLatLong195
            
          */
        public void startmapConvertToLatLong(

            int x196,int y197,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria198,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options199,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    