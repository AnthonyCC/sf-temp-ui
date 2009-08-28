

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
                    * @param distanceToClosestCity50
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DistanceToClosestCityResult distanceToClosestCity(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt51)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DistanceToClosestCity
                * @param distanceToClosestCity50
            
          */
        public void startdistanceToClosestCity(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt51,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FormatPositionText
                    * @param formatPositionText54
                
         */

         
                     public java.lang.String formatPositionText(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt55,boolean useArcDetail56,boolean useKilometers57)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FormatPositionText
                * @param formatPositionText54
            
          */
        public void startformatPositionText(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint pt55,boolean useArcDetail56,boolean useKilometers57,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation60
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation60
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapArcs
                    * @param findMapArcs63
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] findMapArcs(

                        com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria64)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapArcs
                * @param findMapArcs63
            
          */
        public void startfindMapArcs(

            com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria64,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapCenter
                    * @param mapCenter67
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapCenter(

                        int x68,int y69,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria70,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options71)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapCenter
                * @param mapCenter67
            
          */
        public void startmapCenter(

            int x68,int y69,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria70,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options71,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BatchGeocode
                    * @param batchGeocode74
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData[] batchGeocode(

                        com.freshdirect.routing.proxy.stub.roadnet.Address[] adresses75,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options76)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BatchGeocode
                * @param batchGeocode74
            
          */
        public void startbatchGeocode(

            com.freshdirect.routing.proxy.stub.roadnet.Address[] adresses75,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options76,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildMatrix
                    * @param buildMatrix79
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] buildMatrix(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points80)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildMatrix
                * @param buildMatrix79
            
          */
        public void startbuildMatrix(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points80,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Map
                    * @param map83
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData map(

                        com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria84,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options85)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Map
                * @param map83
            
          */
        public void startmap(

            com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria84,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options85,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDriverDirections
                    * @param buildDriverDirections88
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DirectionData buildDriverDirections(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations89,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options90)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDriverDirections
                * @param buildDriverDirections88
            
          */
        public void startbuildDriverDirections(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations89,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options90,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomIn
                    * @param mapZoomIn93
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomIn(

                        int x94,int y95,int zoomDegree96,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria97,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options98)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomIn
                * @param mapZoomIn93
            
          */
        public void startmapZoomIn(

            int x94,int y95,int zoomDegree96,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria97,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options98,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildPathEx
                    * @param buildPathEx101
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.PathData buildPathEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations102,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options103)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildPathEx
                * @param buildPathEx101
            
          */
        public void startbuildPathEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations102,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options103,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomRange
                    * @param mapZoomRange106
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomRange(

                        int ulScreenX107,int ulScreenY108,int lrScreenX109,int lrScreenY110,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria111,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options112)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomRange
                * @param mapZoomRange106
            
          */
        public void startmapZoomRange(

            int ulScreenX107,int ulScreenY108,int lrScreenX109,int lrScreenY110,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria111,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options112,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapArcsEx
                    * @param findMapArcsEx115
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapArc[] findMapArcsEx(

                        com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria116,com.freshdirect.routing.proxy.stub.roadnet.FindMapArcOptions options117)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapArcsEx
                * @param findMapArcsEx115
            
          */
        public void startfindMapArcsEx(

            com.freshdirect.routing.proxy.stub.roadnet.FindMapArcCriteria criteria116,com.freshdirect.routing.proxy.stub.roadnet.FindMapArcOptions options117,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Geocode
                    * @param geocode120
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData geocode(

                        com.freshdirect.routing.proxy.stub.roadnet.Address address121)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Geocode
                * @param geocode120
            
          */
        public void startgeocode(

            com.freshdirect.routing.proxy.stub.roadnet.Address address121,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapGoToPlace
                    * @param mapGoToPlace124
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapGoToPlace(

                        java.lang.String place125,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria126,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options127)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapGoToPlace
                * @param mapGoToPlace124
            
          */
        public void startmapGoToPlace(

            java.lang.String place125,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria126,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options127,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__EnableAllArcsByExtents
                    * @param enableAllArcsByExtents130
                
         */

         
                     public int enableAllArcsByExtents(

                        java.lang.String key131,com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents132)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__EnableAllArcsByExtents
                * @param enableAllArcsByExtents130
            
          */
        public void startenableAllArcsByExtents(

            java.lang.String key131,com.freshdirect.routing.proxy.stub.roadnet.MapExtents extents132,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapZoomOut
                    * @param mapZoomOut135
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapData mapZoomOut(

                        int x136,int y137,int zoomDegree138,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria139,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options140)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapZoomOut
                * @param mapZoomOut135
            
          */
        public void startmapZoomOut(

            int x136,int y137,int zoomDegree138,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria139,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options140,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__GeocodeEx
                    * @param geocodeEx143
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.GeocodeData geocodeEx(

                        com.freshdirect.routing.proxy.stub.roadnet.Address address144,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options145)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__GeocodeEx
                * @param geocodeEx143
            
          */
        public void startgeocodeEx(

            com.freshdirect.routing.proxy.stub.roadnet.Address address144,com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions options145,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop148
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop148
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapRetrieveMapDataVersion
                    * @param mapRetrieveMapDataVersion151
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapDataVersion mapRetrieveMapDataVersion(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapRetrieveMapDataVersion
                * @param mapRetrieveMapDataVersion151
            
          */
        public void startmapRetrieveMapDataVersion(

            

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__FindMapRegionDetails
                    * @param findMapRegionDetails154
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.RegionDetail[] findMapRegionDetails(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] pts155)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__FindMapRegionDetails
                * @param findMapRegionDetails154
            
          */
        public void startfindMapRegionDetails(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] pts155,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveArcOverrides
                    * @param saveArcOverrides158
                
         */

         
                     public int saveArcOverrides(

                        java.lang.String key159,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] overrides160)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveArcOverrides
                * @param saveArcOverrides158
            
          */
        public void startsaveArcOverrides(

            java.lang.String key159,com.freshdirect.routing.proxy.stub.roadnet.MapArcOverride[] overrides160,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildPath
                    * @param buildPath163
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.PathData buildPath(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations164)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildPath
                * @param buildPath163
            
          */
        public void startbuildPath(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations164,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildCompressedPath
                    * @param buildCompressedPath167
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.CompressedPathData buildCompressedPath(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations168,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions169,com.freshdirect.routing.proxy.stub.roadnet.PathCompressionOptions pathCompressionOptions170)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildCompressedPath
                * @param buildCompressedPath167
            
          */
        public void startbuildCompressedPath(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations168,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions169,com.freshdirect.routing.proxy.stub.roadnet.PathCompressionOptions pathCompressionOptions170,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDriverDirectionsEx
                    * @param buildDriverDirectionsEx173
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.DirectionData buildDriverDirectionsEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations174,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options175,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions176)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDriverDirectionsEx
                * @param buildDriverDirectionsEx173
            
          */
        public void startbuildDriverDirectionsEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] destinations174,com.freshdirect.routing.proxy.stub.roadnet.DriverDirectionsOptions options175,com.freshdirect.routing.proxy.stub.roadnet.PathOptions pathOptions176,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildMatrixEx
                    * @param buildMatrixEx179
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.TimeDistanceResult[] buildMatrixEx(

                        com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points180,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options181)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildMatrixEx
                * @param buildMatrixEx179
            
          */
        public void startbuildMatrixEx(

            com.freshdirect.routing.proxy.stub.roadnet.MapPoint[] points180,com.freshdirect.routing.proxy.stub.roadnet.PathOptions options181,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MapConvertToLatLong
                    * @param mapConvertToLatLong184
                
         */

         
                     public com.freshdirect.routing.proxy.stub.roadnet.MapPoint mapConvertToLatLong(

                        int x185,int y186,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria187,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options188)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MapConvertToLatLong
                * @param mapConvertToLatLong184
            
          */
        public void startmapConvertToLatLong(

            int x185,int y186,com.freshdirect.routing.proxy.stub.roadnet.MapCriteria criteria187,com.freshdirect.routing.proxy.stub.roadnet.MapOptions options188,

            final com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    