

/**
 * TransportationWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.routing.proxy.stub.transportation;

    /*
     *  TransportationWebService java interface
     */

    public interface TransportationWebService {
          

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                    * @param retrieveRoutingRoutesByCriteria244
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] retrieveRoutingRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria245,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options246)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                * @param retrieveRoutingRoutesByCriteria244
            
          */
        public void startretrieveRoutingRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria245,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options246,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrders
                    * @param saveRoutingImportOrders249
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrders(

                        java.lang.String regionId250,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders251,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions252)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrders
                * @param saveRoutingImportOrders249
            
          */
        public void startsaveRoutingImportOrders(

            java.lang.String regionId250,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders251,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions252,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUnassignsByCriteria
                    * @param retrieveUnassignsByCriteria255
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria256,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options257)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUnassignsByCriteria
                * @param retrieveUnassignsByCriteria255
            
          */
        public void startretrieveUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria256,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options257,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateDeliveryDetails
                    * @param updateDeliveryDetails260
                
         */

         
                     public void updateDeliveryDetails(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info261)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateDeliveryDetails
                * @param updateDeliveryDetails260
            
          */
        public void startupdateDeliveryDetails(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info261,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedDrivers
                    * @param retrieveAssignedDrivers263
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] retrieveAssignedDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity264)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedDrivers
                * @param retrieveAssignedDrivers263
            
          */
        public void startretrieveAssignedDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity264,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryByCriteria
                    * @param retrievePositionHistoryByCriteria267
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria268)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryByCriteria
                * @param retrievePositionHistoryByCriteria267
            
          */
        public void startretrievePositionHistoryByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria268,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSignature
                    * @param saveStopSignature271
                
         */

         
                     public void saveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity272,javax.activation.DataHandler signatureData273)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSignature
                * @param saveStopSignature271
            
          */
        public void startsaveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity272,javax.activation.DataHandler signatureData273,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUpdateOrder
                    * @param schedulerUpdateOrder275
                
         */

         
                     public boolean schedulerUpdateOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity276,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity277,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options278)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUpdateOrder
                * @param schedulerUpdateOrder275
            
          */
        public void startschedulerUpdateOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity276,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity277,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options278,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportByIdentity
                    * @param retrieveReportByIdentity281
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report retrieveReportByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity282)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportByIdentity
                * @param retrieveReportByIdentity281
            
          */
        public void startretrieveReportByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity282,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                    * @param schedulerDeleteDeliveryWindow285
                
         */

         
                     public void schedulerDeleteDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity286,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window287,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options288)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                * @param schedulerDeleteDeliveryWindow285
            
          */
        public void startschedulerDeleteDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity286,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window287,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options288,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CancelStop
                    * @param cancelStop290
                
         */

         
                     public void cancelStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info291)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CancelStop
                * @param cancelStop290
            
          */
        public void startcancelStop(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info291,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                    * @param retrieveRoutingImportOrderByIdentity293
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder retrieveRoutingImportOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity294,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions295)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                * @param retrieveRoutingImportOrderByIdentity293
            
          */
        public void startretrieveRoutingImportOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity294,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions295,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerPurge
                    * @param schedulerPurge298
                
         */

         
                     public void schedulerPurge(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity299,boolean reloadXML300)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerPurge
                * @param schedulerPurge298
            
          */
        public void startschedulerPurge(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity299,boolean reloadXML300,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteByIdentity
                    * @param retrieveRouteByIdentity302
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity303,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options304)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteByIdentity
                * @param retrieveRouteByIdentity302
            
          */
        public void startretrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity303,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options304,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentityEx
                    * @param retrieveLocationByIdentityEx307
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentityEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity308,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options309)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentityEx
                * @param retrieveLocationByIdentityEx307
            
          */
        public void startretrieveLocationByIdentityEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity308,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options309,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerAnalyzeOrder
                    * @param schedulerAnalyzeOrder312
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] schedulerAnalyzeOrder(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order313,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options314)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerAnalyzeOrder
                * @param schedulerAnalyzeOrder312
            
          */
        public void startschedulerAnalyzeOrder(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order313,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options314,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteria
                    * @param retrieveRouteNotesByCriteria317
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria318)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteria
                * @param retrieveRouteNotesByCriteria317
            
          */
        public void startretrieveRouteNotesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria318,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocations
                    * @param saveLocations321
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations322)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocations
                * @param saveLocations321
            
          */
        public void startsaveLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations322,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                    * @param schedulerRetrieveFeederRoutes325
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] schedulerRetrieveFeederRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity326,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options327)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                * @param schedulerRetrieveFeederRoutes325
            
          */
        public void startschedulerRetrieveFeederRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity326,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options327,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveDestination
                    * @param arriveDestination330
                
         */

         
                     public void arriveDestination(

                        com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info331)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveDestination
                * @param arriveDestination330
            
          */
        public void startarriveDestination(

            com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info331,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                    * @param retrieveRouteDailyStatsByCriteria333
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria334,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options335)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                * @param retrieveRouteDailyStatsByCriteria333
            
          */
        public void startretrieveRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria334,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options335,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveReport
                    * @param saveReport338
                
         */

         
                     public void saveReport(

                        com.freshdirect.routing.proxy.stub.transportation.Report report339)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveReport
                * @param saveReport338
            
          */
        public void startsaveReport(

            com.freshdirect.routing.proxy.stub.transportation.Report report339,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionsByCriteria
                    * @param retrieveRegionsByCriteria341
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region[] retrieveRegionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria342)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionsByCriteria
                * @param retrieveRegionsByCriteria341
            
          */
        public void startretrieveRegionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria342,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                    * @param retrieveDefaultRoutingSessionProperties345
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties retrieveDefaultRoutingSessionProperties(

                        java.lang.String regionId346,java.util.Date sessionDate347)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                * @param retrieveDefaultRoutingSessionProperties345
            
          */
        public void startretrieveDefaultRoutingSessionProperties(

            java.lang.String regionId346,java.util.Date sessionDate347,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteSurveyResults
                    * @param saveRouteSurveyResults350
                
         */

         
                     public void saveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity351,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt352,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults353)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteSurveyResults
                * @param saveRouteSurveyResults350
            
          */
        public void startsaveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity351,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt352,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults353,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingRouteNetMatrix
                    * @param buildRoutingRouteNetMatrix355
                
         */

         
                     public void buildRoutingRouteNetMatrix(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity356)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingRouteNetMatrix
                * @param buildRoutingRouteNetMatrix355
            
          */
        public void startbuildRoutingRouteNetMatrix(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity356,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ReturnFault
                    * @param returnFault358
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Fault returnFault(

                        int requestedFaultCode359)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ReturnFault
                * @param returnFault358
            
          */
        public void startreturnFault(

            int requestedFaultCode359,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SuggestRoute
                    * @param suggestRoute362
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] suggestRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop363,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options364)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SuggestRoute
                * @param suggestRoute362
            
          */
        public void startsuggestRoute(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop363,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options364,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByCriteria
                    * @param retrieveNotificationsByCriteria367
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria368,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions369,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options370)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByCriteria
                * @param retrieveNotificationsByCriteria367
            
          */
        public void startretrieveNotificationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria368,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions369,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options370,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSignature
                    * @param retrieveStopSignature373
                
         */

         
                     public javax.activation.DataHandler retrieveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity374,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType375)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSignature
                * @param retrieveStopSignature373
            
          */
        public void startretrieveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity374,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType375,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CalculateTimeDist
                    * @param calculateTimeDist378
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TimeDistResult calculateTimeDist(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity379,int fromLatitude380,int fromLongitude381,int toLatitude382,int toLongitude383)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CalculateTimeDist
                * @param calculateTimeDist378
            
          */
        public void startcalculateTimeDist(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity379,int fromLatitude380,int fromLongitude381,int toLatitude382,int toLongitude383,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerMovableOrders
                    * @param schedulerMovableOrders386
                
         */

         
                     public void schedulerMovableOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity387,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria388,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options389)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerMovableOrders
                * @param schedulerMovableOrders386
            
          */
        public void startschedulerMovableOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity387,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria388,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options389,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                    * @param retrieveLocationsByCriteriaEx391
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria392,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options393)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                * @param retrieveLocationsByCriteriaEx391
            
          */
        public void startretrieveLocationsByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria392,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options393,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePermissionsForUser
                    * @param retrievePermissionsForUser396
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserPermissions retrievePermissionsForUser(

                        java.lang.String userID397,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity398)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePermissionsForUser
                * @param retrievePermissionsForUser396
            
          */
        public void startretrievePermissionsForUser(

            java.lang.String userID397,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity398,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyQuestions
                    * @param retrieveRouteSurveyQuestions401
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RetrieveRouteSurveyQuestionsResponse retrieveRouteSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity402,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt403)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyQuestions
                * @param retrieveRouteSurveyQuestions401
            
          */
        public void startretrieveRouteSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity402,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt403,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByCriteria
                    * @param retrieveEquipmentByCriteria407
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment[] retrieveEquipmentByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria408,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options409)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByCriteria
                * @param retrieveEquipmentByCriteria407
            
          */
        public void startretrieveEquipmentByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria408,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options409,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBalanceRoutes
                    * @param schedulerBalanceRoutes412
                
         */

         
                     public void schedulerBalanceRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity413,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options414)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBalanceRoutes
                * @param schedulerBalanceRoutes412
            
          */
        public void startschedulerBalanceRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity413,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options414,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUnassigned
                    * @param saveUnassigned416
                
         */

         
                     public void saveUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop417,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options418)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUnassigned
                * @param saveUnassigned416
            
          */
        public void startsaveUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop417,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options418,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoute
                    * @param removeRoute420
                
         */

         
                     public void removeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity421)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoute
                * @param removeRoute420
            
          */
        public void startremoveRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity421,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                    * @param retrieveNotificationsByRecipientIdentity423
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByRecipientIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity424,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions425,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options426)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                * @param retrieveNotificationsByRecipientIdentity423
            
          */
        public void startretrieveNotificationsByRecipientIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity424,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions425,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options426,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutesByCriteria
                    * @param retrieveRoutesByCriteria429
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route[] retrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria430,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options431)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutesByCriteria
                * @param retrieveRoutesByCriteria429
            
          */
        public void startretrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria430,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options431,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrdersEx
                    * @param schedulerOptimizeOrdersEx434
                
         */

         
                     public void schedulerOptimizeOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity435,com.freshdirect.routing.proxy.stub.transportation.SchedulerOptimizeOrdersExOptions options436)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrdersEx
                * @param schedulerOptimizeOrdersEx434
            
          */
        public void startschedulerOptimizeOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity435,com.freshdirect.routing.proxy.stub.transportation.SchedulerOptimizeOrdersExOptions options436,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegion
                    * @param saveRegion438
                
         */

         
                     public void saveRegion(

                        com.freshdirect.routing.proxy.stub.transportation.Region region439)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegion
                * @param saveRegion438
            
          */
        public void startsaveRegion(

            com.freshdirect.routing.proxy.stub.transportation.Region region439,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionByIdentity
                    * @param retrieveRegionByIdentity441
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region retrieveRegionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity442)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionByIdentity
                * @param retrieveRegionByIdentity441
            
          */
        public void startretrieveRegionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity442,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportsByCriteria
                    * @param retrieveReportsByCriteria445
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report[] retrieveReportsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria446)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportsByCriteria
                * @param retrieveReportsByCriteria445
            
          */
        public void startretrieveReportsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria446,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                    * @param retrieveRoutingLocationsWithOrders449
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrders(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity450)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                * @param retrieveRoutingLocationsWithOrders449
            
          */
        public void startretrieveRoutingLocationsWithOrders(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity450,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserByUserID
                    * @param retrieveUserByUserID453
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.User retrieveUserByUserID(

                        java.lang.String userID454)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserByUserID
                * @param retrieveUserByUserID453
            
          */
        public void startretrieveUserByUserID(

            java.lang.String userID454,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeByIdentity
                    * @param retrieveEmployeeByIdentity457
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity458)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeByIdentity
                * @param retrieveEmployeeByIdentity457
            
          */
        public void startretrieveEmployeeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity458,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUnassigned
                    * @param deleteUnassigned461
                
         */

         
                     public void deleteUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop462)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUnassigned
                * @param deleteUnassigned461
            
          */
        public void startdeleteUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop462,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateRoutingSession
                    * @param createRoutingSession464
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity createRoutingSession(

                        java.lang.String regionId465,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties466)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateRoutingSession
                * @param createRoutingSession464
            
          */
        public void startcreateRoutingSession(

            java.lang.String regionId465,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties466,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AuthenticateUser
                    * @param authenticateUser469
                
         */

         
                     public void authenticateUser(

                        java.lang.String userID470,java.lang.String password471)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AuthenticateUser
                * @param authenticateUser469
            
          */
        public void startauthenticateUser(

            java.lang.String userID470,java.lang.String password471,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnlockNotifications
                    * @param unlockNotifications473
                
         */

         
                     public void unlockNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria474)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnlockNotifications
                * @param unlockNotifications473
            
          */
        public void startunlockNotifications(

            com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria474,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteRoutingSession
                    * @param deleteRoutingSession476
                
         */

         
                     public void deleteRoutingSession(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity477)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteRoutingSession
                * @param deleteRoutingSession476
            
          */
        public void startdeleteRoutingSession(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity477,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserConfig
                    * @param saveUserConfig479
                
         */

         
                     public void saveUserConfig(

                        java.lang.String applicationID480,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity481,java.lang.String configGroupID482,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items483)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserConfig
                * @param saveUserConfig479
            
          */
        public void startsaveUserConfig(

            java.lang.String applicationID480,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity481,java.lang.String configGroupID482,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items483,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                    * @param schedulerRetrieveOrdersByCriteria485
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerRetrieveOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity486,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria487,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options488)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                * @param schedulerRetrieveOrdersByCriteria485
            
          */
        public void startschedulerRetrieveOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity486,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria487,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options488,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation491
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation491
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserConfig
                    * @param retrieveUserConfig494
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveUserConfig(

                        java.lang.String applicationID495,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity496,java.lang.String configGroupID497)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserConfig
                * @param retrieveUserConfig494
            
          */
        public void startretrieveUserConfig(

            java.lang.String applicationID495,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity496,java.lang.String configGroupID497,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeesByCriteria
                    * @param retrieveEmployeesByCriteria500
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria501)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeesByCriteria
                * @param retrieveEmployeesByCriteria500
            
          */
        public void startretrieveEmployeesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria501,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartOrigin
                    * @param departOrigin504
                
         */

         
                     public void departOrigin(

                        com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info505)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartOrigin
                * @param departOrigin504
            
          */
        public void startdepartOrigin(

            com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info505,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStop
                    * @param saveStop507
                
         */

         
                     public void saveStop(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop508,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions509,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options510)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStop
                * @param saveStop507
            
          */
        public void startsaveStop(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop508,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions509,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options510,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRemoveFromServer
                    * @param schedulerRemoveFromServer512
                
         */

         
                     public void schedulerRemoveFromServer(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity513)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRemoveFromServer
                * @param schedulerRemoveFromServer512
            
          */
        public void startschedulerRemoveFromServer(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity513,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnassignStop
                    * @param unassignStop515
                
         */

         
                     public void unassignStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop516)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnassignStop
                * @param unassignStop515
            
          */
        public void startunassignStop(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop516,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                    * @param retrieveEquipmentTypeByIdentity518
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType retrieveEquipmentTypeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity519,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options520)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                * @param retrieveEquipmentTypeByIdentity518
            
          */
        public void startretrieveEquipmentTypeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity519,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options520,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCancelOrder
                    * @param schedulerCancelOrder523
                
         */

         
                     public void schedulerCancelOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity524,java.lang.String orderNumberXML525)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCancelOrder
                * @param schedulerCancelOrder523
            
          */
        public void startschedulerCancelOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity524,java.lang.String orderNumberXML525,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                    * @param schedulerRetrieveDeliveryWaveInstancesByCriteria527
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance[] schedulerRetrieveDeliveryWaveInstancesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity528,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria529,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options530)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                * @param schedulerRetrieveDeliveryWaveInstancesByCriteria527
            
          */
        public void startschedulerRetrieveDeliveryWaveInstancesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity528,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria529,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options530,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteNotifications
                    * @param deleteNotifications533
                
         */

         
                     public void deleteNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities534)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteNotifications
                * @param deleteNotifications533
            
          */
        public void startdeleteNotifications(

            com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities534,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                    * @param retrieveRoutingRouteByIdentity536
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute retrieveRoutingRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity537,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options538)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                * @param retrieveRoutingRouteByIdentity536
            
          */
        public void startretrieveRoutingRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity537,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options538,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyQuestions
                    * @param retrieveStopSurveyQuestions541
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RetrieveStopSurveyQuestionsResponse retrieveStopSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity542)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyQuestions
                * @param retrieveStopSurveyQuestions541
            
          */
        public void startretrieveStopSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity542,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEmployee
                    * @param saveEmployee546
                
         */

         
                     public void saveEmployee(

                        com.freshdirect.routing.proxy.stub.transportation.Employee employee547)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEmployee
                * @param saveEmployee546
            
          */
        public void startsaveEmployee(

            com.freshdirect.routing.proxy.stub.transportation.Employee employee547,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                    * @param retrieveDutyPeriodsByCriteria549
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] retrieveDutyPeriodsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria550,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options551)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                * @param retrieveDutyPeriodsByCriteria549
            
          */
        public void startretrieveDutyPeriodsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria550,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options551,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWindow
                    * @param schedulerSaveDeliveryWindow554
                
         */

         
                     public boolean schedulerSaveDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity555,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options556)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWindow
                * @param schedulerSaveDeliveryWindow554
            
          */
        public void startschedulerSaveDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity555,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options556,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByIdentity
                    * @param retrieveEquipmentByIdentity559
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment retrieveEquipmentByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity560,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options561)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByIdentity
                * @param retrieveEquipmentByIdentity559
            
          */
        public void startretrieveEquipmentByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity560,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options561,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SendTextMessageToDriver
                    * @param sendTextMessageToDriver564
                
         */

         
                     public void sendTextMessageToDriver(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity565,java.lang.String message566,java.lang.String fromUserID567)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SendTextMessageToDriver
                * @param sendTextMessageToDriver564
            
          */
        public void startsendTextMessageToDriver(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity565,java.lang.String message566,java.lang.String fromUserID567,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                    * @param schedulerRetrieveOrderByIdentity569
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder schedulerRetrieveOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity570,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options571)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                * @param schedulerRetrieveOrderByIdentity569
            
          */
        public void startschedulerRetrieveOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity570,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options571,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedEquipment
                    * @param retrieveAssignedEquipment574
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] retrieveAssignedEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity575)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedEquipment
                * @param retrieveAssignedEquipment574
            
          */
        public void startretrieveAssignedEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity575,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyResults
                    * @param retrieveStopSurveyResults578
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity579)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyResults
                * @param retrieveStopSurveyResults578
            
          */
        public void startretrieveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity579,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentity
                    * @param retrieveLocationByIdentity582
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity583)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentity
                * @param retrieveLocationByIdentity582
            
          */
        public void startretrieveLocationByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity583,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrdersEx
                    * @param saveRoutingImportOrdersEx586
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrdersEx(

                        java.lang.String regionId587,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders588,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions589,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions590)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrdersEx
                * @param saveRoutingImportOrdersEx586
            
          */
        public void startsaveRoutingImportOrdersEx(

            java.lang.String regionId587,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders588,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions589,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions590,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExtendOrderReservation
                    * @param schedulerExtendOrderReservation593
                
         */

         
                     public void schedulerExtendOrderReservation(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity594,java.lang.String orderNumberXML595,int extendMinutes596)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExtendOrderReservation
                * @param schedulerExtendOrderReservation593
            
          */
        public void startschedulerExtendOrderReservation(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity594,java.lang.String orderNumberXML595,int extendMinutes596,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionConfig
                    * @param retrieveRegionConfig598
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveRegionConfig(

                        java.lang.String applicationID599,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity600,java.lang.String configGroupID601)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionConfig
                * @param retrieveRegionConfig598
            
          */
        public void startretrieveRegionConfig(

            java.lang.String applicationID599,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity600,java.lang.String configGroupID601,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                    * @param schedulerSendRoutesToRoadnetEx604
                
         */

         
                     public void schedulerSendRoutesToRoadnetEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity605,java.lang.String sessionDescription606)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                * @param schedulerSendRoutesToRoadnetEx604
            
          */
        public void startschedulerSendRoutesToRoadnetEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity605,java.lang.String sessionDescription606,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrders
                    * @param schedulerOptimizeOrders608
                
         */

         
                     public void schedulerOptimizeOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity609)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrders
                * @param schedulerOptimizeOrders608
            
          */
        public void startschedulerOptimizeOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity609,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                    * @param schedulerSendRoutesToRoadnet611
                
         */

         
                     public void schedulerSendRoutesToRoadnet(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity612)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                * @param schedulerSendRoutesToRoadnet611
            
          */
        public void startschedulerSendRoutesToRoadnet(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity612,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignDrivers
                    * @param assignDrivers614
                
         */

         
                     public void assignDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity615,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers616)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignDrivers
                * @param assignDrivers614
            
          */
        public void startassignDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity615,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers616,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                    * @param retrieveRoutingLocationsWithOrdersEx618
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity619,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options620)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                * @param retrieveRoutingLocationsWithOrdersEx618
            
          */
        public void startretrieveRoutingLocationsWithOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity619,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options620,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoute
                    * @param saveRoute623
                
         */

         
                     public void saveRoute(

                        com.freshdirect.routing.proxy.stub.transportation.Route route624,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions625,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions626)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoute
                * @param saveRoute623
            
          */
        public void startsaveRoute(

            com.freshdirect.routing.proxy.stub.transportation.Route route624,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions625,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions626,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEquipment
                    * @param saveEquipment628
                
         */

         
                     public void saveEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.Equipment equipment629,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options630)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEquipment
                * @param saveEquipment628
            
          */
        public void startsaveEquipment(

            com.freshdirect.routing.proxy.stub.transportation.Equipment equipment629,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options630,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopByIdentity
                    * @param retrieveStopByIdentity632
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop retrieveStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity633,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options634)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopByIdentity
                * @param retrieveStopByIdentity632
            
          */
        public void startretrieveStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity633,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options634,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BulkArriveDepartStop
                    * @param bulkArriveDepartStop637
                
         */

         
                     public void bulkArriveDepartStop(

                        com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos638)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BulkArriveDepartStop
                * @param bulkArriveDepartStop637
            
          */
        public void startbulkArriveDepartStop(

            com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos638,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                    * @param retrieveRoutingSessionsByCriteria640
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] retrieveRoutingSessionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria641,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options642)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                * @param retrieveRoutingSessionsByCriteria640
            
          */
        public void startretrieveRoutingSessionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria641,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options642,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                    * @param retrieveRoutingUnassignsByCriteria645
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria646,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options647)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                * @param retrieveRoutingUnassignsByCriteria645
            
          */
        public void startretrieveRoutingUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria646,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options647,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                    * @param retrieveRoutingImportOrdersByCriteria650
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] retrieveRoutingImportOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria651,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions652)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                * @param retrieveRoutingImportOrdersByCriteria650
            
          */
        public void startretrieveRoutingImportOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria651,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions652,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop655
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop655
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ChangeUserPassword
                    * @param changeUserPassword658
                
         */

         
                     public void changeUserPassword(

                        java.lang.String userID659,java.lang.String oldPassword660,java.lang.String newPassword661)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ChangeUserPassword
                * @param changeUserPassword658
            
          */
        public void startchangeUserPassword(

            java.lang.String userID659,java.lang.String oldPassword660,java.lang.String newPassword661,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CompleteRoute
                    * @param completeRoute663
                
         */

         
                     public void completeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info664)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CompleteRoute
                * @param completeRoute663
            
          */
        public void startcompleteRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info664,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                    * @param schedulerExcludeCutoffRoutes666
                
         */

         
                     public void schedulerExcludeCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity667,boolean excludeXML668)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                * @param schedulerExcludeCutoffRoutes666
            
          */
        public void startschedulerExcludeCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity667,boolean excludeXML668,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                    * @param retrieveRoutingOrderByIdentity670
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder retrieveRoutingOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity671,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options672)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                * @param retrieveRoutingOrderByIdentity670
            
          */
        public void startretrieveRoutingOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity671,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options672,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__TextMessage
                    * @param textMessage675
                
         */

         
                     public void textMessage(

                        com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info676)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__TextMessage
                * @param textMessage675
            
          */
        public void starttextMessage(

            com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info676,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                    * @param retrieveRoutingStopsByCriteria678
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria679,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options680)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                * @param retrieveRoutingStopsByCriteria678
            
          */
        public void startretrieveRoutingStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria679,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options680,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                    * @param schedulerCalculateDeliveryWindowMetrics683
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics[] schedulerCalculateDeliveryWindowMetrics(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity684,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options685)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                * @param schedulerCalculateDeliveryWindowMetrics683
            
          */
        public void startschedulerCalculateDeliveryWindowMetrics(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity684,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options685,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocations
                    * @param deleteLocations688
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] deleteLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations689)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocations
                * @param deleteLocations688
            
          */
        public void startdeleteLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations689,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveProductsPurchased
                    * @param retrieveProductsPurchased692
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased retrieveProductsPurchased(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity693)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveProductsPurchased
                * @param retrieveProductsPurchased692
            
          */
        public void startretrieveProductsPurchased(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity693,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                    * @param retrieveRoutingSourcedOrdersByCriteria696
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] retrieveRoutingSourcedOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria697,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options698)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                * @param retrieveRoutingSourcedOrdersByCriteria696
            
          */
        public void startretrieveRoutingSourcedOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria697,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options698,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSurveyResults
                    * @param saveStopSurveyResults701
                
         */

         
                     public void saveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity702,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults703)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSurveyResults
                * @param saveStopSurveyResults701
            
          */
        public void startsaveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity702,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults703,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                    * @param retrieveRoutingRouteDailyStatsByCriteria705
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRoutingRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria706,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options707)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                * @param retrieveRoutingRouteDailyStatsByCriteria705
            
          */
        public void startretrieveRoutingRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria706,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options707,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStopSignature
                    * @param updateStopSignature710
                
         */

         
                     public void updateStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info711)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStopSignature
                * @param updateStopSignature710
            
          */
        public void startupdateStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info711,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveGlobalConfig
                    * @param saveGlobalConfig713
                
         */

         
                     public void saveGlobalConfig(

                        java.lang.String applicationID714,java.lang.String configGroupID715,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items716)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveGlobalConfig
                * @param saveGlobalConfig713
            
          */
        public void startsaveGlobalConfig(

            java.lang.String applicationID714,java.lang.String configGroupID715,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items716,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                    * @param schedulerIsExcludingCutoffRoutes718
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult schedulerIsExcludingCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity719)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                * @param schedulerIsExcludingCutoffRoutes718
            
          */
        public void startschedulerIsExcludingCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity719,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                    * @param retrieveCannedTextMessagesByCriteria722
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] retrieveCannedTextMessagesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria723)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                * @param retrieveCannedTextMessagesByCriteria722
            
          */
        public void startretrieveCannedTextMessagesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria723,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReserveOrder
                    * @param schedulerReserveOrder726
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ReserveResult schedulerReserveOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity727,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder728,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow729,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options730)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReserveOrder
                * @param schedulerReserveOrder726
            
          */
        public void startschedulerReserveOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity727,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder728,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow729,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options730,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteria
                    * @param retrieveLocationsByCriteria733
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria734)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteria
                * @param retrieveLocationsByCriteria733
            
          */
        public void startretrieveLocationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria734,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopsByCriteria
                    * @param retrieveStopsByCriteria737
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria738,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options739)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopsByCriteria
                * @param retrieveStopsByCriteria737
            
          */
        public void startretrieveStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria738,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options739,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionOptions
                    * @param retrieveRegionOptions742
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RegionOptions retrieveRegionOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity743)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionOptions
                * @param retrieveRegionOptions742
            
          */
        public void startretrieveRegionOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity743,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SequenceStop
                    * @param sequenceStop746
                
         */

         
                     public void sequenceStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info747)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SequenceStop
                * @param sequenceStop746
            
          */
        public void startsequenceStop(

            com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info747,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyResults
                    * @param retrieveRouteSurveyResults749
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity750,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt751)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyResults
                * @param retrieveRouteSurveyResults749
            
          */
        public void startretrieveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity750,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt751,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePosition
                    * @param updateRoutePosition754
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info755)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePosition
                * @param updateRoutePosition754
            
          */
        public void startupdateRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info755,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MassStopSequence
                    * @param massStopSequence758
                
         */

         
                     public void massStopSequence(

                        com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info759)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MassStopSequence
                * @param massStopSequence758
            
          */
        public void startmassStopSequence(

            com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info759,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AddRICUser
                    * @param addRICUser761
                
         */

         
                     public void addRICUser(

                        com.freshdirect.routing.proxy.stub.transportation.User user762)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AddRICUser
                * @param addRICUser761
            
          */
        public void startaddRICUser(

            com.freshdirect.routing.proxy.stub.transportation.User user762,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                    * @param retrieveRoutingSessionByIdentity764
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession retrieveRoutingSessionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity765,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options766)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                * @param retrieveRoutingSessionByIdentity764
            
          */
        public void startretrieveRoutingSessionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity765,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options766,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartStop
                    * @param departStop769
                
         */

         
                     public void departStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info770)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartStop
                * @param departStop769
            
          */
        public void startdepartStop(

            com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info770,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerConfirmOrder
                    * @param schedulerConfirmOrder772
                
         */

         
                     public void schedulerConfirmOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity773,java.lang.String orderNumberXML774)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerConfirmOrder
                * @param schedulerConfirmOrder772
            
          */
        public void startschedulerConfirmOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity773,java.lang.String orderNumberXML774,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AllowAdditionOfRICUsers
                    * @param allowAdditionOfRICUsers776
                
         */

         
                     public boolean allowAdditionOfRICUsers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AllowAdditionOfRICUsers
                * @param allowAdditionOfRICUsers776
            
          */
        public void startallowAdditionOfRICUsers(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGlobalConfig
                    * @param retrieveGlobalConfig779
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveGlobalConfig(

                        java.lang.String applicationID780,java.lang.String configGroupID781)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGlobalConfig
                * @param retrieveGlobalConfig779
            
          */
        public void startretrieveGlobalConfig(

            java.lang.String applicationID780,java.lang.String configGroupID781,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                    * @param retrieveEquipmentTypeByCriteria784
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] retrieveEquipmentTypeByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria785,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options786)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                * @param retrieveEquipmentTypeByCriteria784
            
          */
        public void startretrieveEquipmentTypeByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria785,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options786,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                    * @param retrievePositionHistoryBlocksByCriteria789
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryBlocksByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria790)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                * @param retrievePositionHistoryBlocksByCriteria789
            
          */
        public void startretrievePositionHistoryBlocksByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria790,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReloadWaveInstances
                    * @param schedulerReloadWaveInstances793
                
         */

         
                     public boolean schedulerReloadWaveInstances(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity794,com.freshdirect.routing.proxy.stub.transportation.SchedulerReloadWaveInstancesOptions options795)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReloadWaveInstances
                * @param schedulerReloadWaveInstances793
            
          */
        public void startschedulerReloadWaveInstances(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity794,com.freshdirect.routing.proxy.stub.transportation.SchedulerReloadWaveInstancesOptions options795,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationsEx
                    * @param saveLocationsEx798
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocationsEx(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations799,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options800)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationsEx
                * @param saveLocationsEx798
            
          */
        public void startsaveLocationsEx(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations799,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options800,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveStop
                    * @param arriveStop803
                
         */

         
                     public void arriveStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info804)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveStop
                * @param arriveStop803
            
          */
        public void startarriveStop(

            com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info804,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                    * @param schedulerSaveDeliveryWaveInstance806
                
         */

         
                     public java.lang.String[] schedulerSaveDeliveryWaveInstance(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity807,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity808,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes809,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options810)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                * @param schedulerSaveDeliveryWaveInstance806
            
          */
        public void startschedulerSaveDeliveryWaveInstance(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity807,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity808,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes809,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options810,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopByIdentity
                    * @param retrieveRoutingStopByIdentity813
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop retrieveRoutingStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity814,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options815)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopByIdentity
                * @param retrieveRoutingStopByIdentity813
            
          */
        public void startretrieveRoutingStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity814,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options815,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteReport
                    * @param deleteReport818
                
         */

         
                     public void deleteReport(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity819)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteReport
                * @param deleteReport818
            
          */
        public void startdeleteReport(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity819,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegionConfig
                    * @param saveRegionConfig821
                
         */

         
                     public void saveRegionConfig(

                        java.lang.String applicationID822,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity823,java.lang.String configGroupID824,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items825)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegionConfig
                * @param saveRegionConfig821
            
          */
        public void startsaveRegionConfig(

            java.lang.String applicationID822,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity823,java.lang.String configGroupID824,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items825,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBulkReserveOrders
                    * @param schedulerBulkReserveOrders827
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerBulkReserveOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity828,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders829,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options830)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBulkReserveOrders
                * @param schedulerBulkReserveOrders827
            
          */
        public void startschedulerBulkReserveOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity828,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders829,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options830,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamps
                    * @param convertTimestamps833
                
         */

         
                     public java.util.Calendar[] convertTimestamps(

                        java.util.Calendar[] sourceTimestamps834,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions835,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions836)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamps
                * @param convertTimestamps833
            
          */
        public void startconvertTimestamps(

            java.util.Calendar[] sourceTimestamps834,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions835,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions836,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteForDevice
                    * @param retrieveRouteForDevice839
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteForDevice(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity840,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options841,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity842)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteForDevice
                * @param retrieveRouteForDevice839
            
          */
        public void startretrieveRouteForDevice(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity840,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options841,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity842,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignEquipment
                    * @param assignEquipment845
                
         */

         
                     public void assignEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity846,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment847)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignEquipment
                * @param assignEquipment845
            
          */
        public void startassignEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity846,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment847,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamp
                    * @param convertTimestamp849
                
         */

         
                     public java.util.Calendar convertTimestamp(

                        java.util.Calendar sourceTimestamp850,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions851,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions852)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamp
                * @param convertTimestamp849
            
          */
        public void startconvertTimestamp(

            java.util.Calendar sourceTimestamp850,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions851,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions852,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveys
                    * @param retrieveSurveys855
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Survey[] retrieveSurveys(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity856)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveys
                * @param retrieveSurveys855
            
          */
        public void startretrieveSurveys(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity856,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                    * @param retrieveDutyPeriodByIdentity859
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod retrieveDutyPeriodByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity860,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options861)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                * @param retrieveDutyPeriodByIdentity859
            
          */
        public void startretrieveDutyPeriodByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity860,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options861,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__PlaceUnassigned
                    * @param placeUnassigned864
                
         */

         
                     public void placeUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop865,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity866,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition867,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime868,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions869)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__PlaceUnassigned
                * @param placeUnassigned864
            
          */
        public void startplaceUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop865,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity866,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition867,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime868,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions869,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__StartRoute
                    * @param startRoute871
                
         */

         
                     public void startRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info872)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__StartRoute
                * @param startRoute871
            
          */
        public void startstartRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info872,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    