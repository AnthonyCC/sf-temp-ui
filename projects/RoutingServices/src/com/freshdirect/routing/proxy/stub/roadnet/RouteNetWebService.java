

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
          * Service definition of function ns1__POIShowEnabled
                    * @param pOIShowEnabled52
                
         */

         
                     public boolean pOIShowEnabled(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__POIShowEnabled
                * @param pOIShowEnabled52
            
          */
        public void startpOIShowEnabled(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapArcs
                    * @param findMapArcs55
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] findMapArcs(

                        com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria56)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapArcs
                * @param findMapArcs55
            
          */
        public void startfindMapArcs(

            com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria56,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildPathEx
                    * @param buildPathEx59
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.PathData buildPathEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations60,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options61)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildPathEx
                * @param buildPathEx59
            
          */
        public void startbuildPathEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations60,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options61,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildMatrixEx
                    * @param buildMatrixEx64
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] buildMatrixEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points65,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options66)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildMatrixEx
                * @param buildMatrixEx64
            
          */
        public void startbuildMatrixEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points65,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options66,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapConvertToLatLong
                    * @param mapConvertToLatLong69
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapPoint mapConvertToLatLong(

                        int x70,int y71,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria72,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options73)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapConvertToLatLong
                * @param mapConvertToLatLong69
            
          */
        public void startmapConvertToLatLong(

            int x70,int y71,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria72,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options73,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop76
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop76
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapGoToPlace
                    * @param mapGoToPlace79
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapGoToPlace(

                        java.lang.String place80,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria81,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options82)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapGoToPlace
                * @param mapGoToPlace79
            
          */
        public void startmapGoToPlace(

            java.lang.String place80,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria81,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options82,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveArcOverrides
                    * @param saveArcOverrides85
                
         */

         
                     public int saveArcOverrides(

                        java.lang.String str86,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] vec87)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveArcOverrides
                * @param saveArcOverrides85
            
          */
        public void startsaveArcOverrides(

            java.lang.String str86,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] vec87,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapCenter
                    * @param mapCenter90
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapCenter(

                        int x91,int y92,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria93,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options94)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapCenter
                * @param mapCenter90
            
          */
        public void startmapCenter(

            int x91,int y92,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria93,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options94,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomIn
                    * @param mapZoomIn97
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomIn(

                        int x98,int y99,int zoomDegree100,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria101,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options102)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomIn
                * @param mapZoomIn97
            
          */
        public void startmapZoomIn(

            int x98,int y99,int zoomDegree100,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria101,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options102,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DistanceToClosestCity
                    * @param distanceToClosestCity105
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DistanceToClosestCityResult distanceToClosestCity(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt106)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DistanceToClosestCity
                * @param distanceToClosestCity105
            
          */
        public void startdistanceToClosestCity(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt106,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDriverDirectionsEx
                    * @param buildDriverDirectionsEx109
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DirectionData buildDriverDirectionsEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations110,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options111,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions112)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDriverDirectionsEx
                * @param buildDriverDirectionsEx109
            
          */
        public void startbuildDriverDirectionsEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations110,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options111,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions112,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildCompressedPath
                    * @param buildCompressedPath115
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.CompressedPathData buildCompressedPath(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations116,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions117,com.freshdirect.routing.proxy.stub.roadnet.PathCompressionOptions pathCompressionOptions118)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildCompressedPath
                * @param buildCompressedPath115
            
          */
        public void startbuildCompressedPath(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations116,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions117,com.freshdirect.routing.proxy.stub.roadnet.PathCompressionOptions pathCompressionOptions118,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Geocode
                    * @param geocode121
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData geocode(

                        com.freshdirect.routing.proxy.stub.roadnet.Address address122)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Geocode
                * @param geocode121
            
          */
        public void startgeocode(

            com.freshdirect.routing.proxy.stub.roadnet.Address address122,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildPath
                    * @param buildPath125
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.PathData buildPath(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations126)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildPath
                * @param buildPath125
            
          */
        public void startbuildPath(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations126,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapRegionDetails
                    * @param findMapRegionDetails129
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.RegionDetail[] findMapRegionDetails(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] pts130)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapRegionDetails
                * @param findMapRegionDetails129
            
          */
        public void startfindMapRegionDetails(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] pts130,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapRetrieveMapDataVersion
                    * @param mapRetrieveMapDataVersion133
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapDataVersion mapRetrieveMapDataVersion(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapRetrieveMapDataVersion
                * @param mapRetrieveMapDataVersion133
            
          */
        public void startmapRetrieveMapDataVersion(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveArcOverridesEx
                    * @param saveArcOverridesEx136
                
         */

         
                     public int saveArcOverridesEx(

                        java.lang.String key137,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] overrides138,com.freshdirect.routing.proxy.stub.roadnet.ArcOverrideSaveOptions options139)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveArcOverridesEx
                * @param saveArcOverridesEx136
            
          */
        public void startsaveArcOverridesEx(

            java.lang.String key137,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] overrides138,com.freshdirect.routing.proxy.stub.roadnet.ArcOverrideSaveOptions options139,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomOut
                    * @param mapZoomOut142
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomOut(

                        int x143,int y144,int zoomDegree145,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria146,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options147)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomOut
                * @param mapZoomOut142
            
          */
        public void startmapZoomOut(

            int x143,int y144,int zoomDegree145,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria146,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options147,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__GeocodeEx
                    * @param geocodeEx150
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData geocodeEx(

                        com.freshdirect.routing.proxy.stub.roadnet.Address address151,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options152)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__GeocodeEx
                * @param geocodeEx150
            
          */
        public void startgeocodeEx(

            com.freshdirect.routing.proxy.stub.roadnet.Address address151,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options152,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapArcsEx
                    * @param findMapArcsEx155
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] findMapArcsEx(

                        com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria156,com.freshdirect.routing.proxy.stub.roadnet.FindMapArcOptions options157)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapArcsEx
                * @param findMapArcsEx155
            
          */
        public void startfindMapArcsEx(

            com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria156,com.freshdirect.routing.proxy.stub.roadnet.FindMapArcOptions options157,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FormatPositionText
                    * @param formatPositionText160
                
         */

         
                     public java.lang.String formatPositionText(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt161,boolean useArcDetail162,boolean useKilometers163)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FormatPositionText
                * @param formatPositionText160
            
          */
        public void startformatPositionText(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt161,boolean useArcDetail162,boolean useKilometers163,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDriverDirections
                    * @param buildDriverDirections166
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DirectionData buildDriverDirections(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations167,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options168)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDriverDirections
                * @param buildDriverDirections166
            
          */
        public void startbuildDriverDirections(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations167,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options168,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation171
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation171
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BatchGeocode
                    * @param batchGeocode174
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData[] batchGeocode(

                        com.freshdirect.routing.proxy.stub.roadnet.Address[] adresses175,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options176)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BatchGeocode
                * @param batchGeocode174
            
          */
        public void startbatchGeocode(

            com.freshdirect.routing.proxy.stub.roadnet.Address[] adresses175,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options176,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__EnableAllArcsByExtents
                    * @param enableAllArcsByExtents179
                
         */

         
                     public int enableAllArcsByExtents(

                        java.lang.String key180,com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents181)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__EnableAllArcsByExtents
                * @param enableAllArcsByExtents179
            
          */
        public void startenableAllArcsByExtents(

            java.lang.String key180,com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents181,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Map
                    * @param map184
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData map(

                        com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria185,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options186)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Map
                * @param map184
            
          */
        public void startmap(

            com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria185,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options186,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomRange
                    * @param mapZoomRange189
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomRange(

                        int ulScreenX190,int ulScreenY191,int lrScreenX192,int lrScreenY193,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria194,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options195)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomRange
                * @param mapZoomRange189
            
          */
        public void startmapZoomRange(

            int ulScreenX190,int ulScreenY191,int lrScreenX192,int lrScreenY193,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria194,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options195,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildMatrix
                    * @param buildMatrix198
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] buildMatrix(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points199)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildMatrix
                * @param buildMatrix198
            
          */
        public void startbuildMatrix(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points199,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    