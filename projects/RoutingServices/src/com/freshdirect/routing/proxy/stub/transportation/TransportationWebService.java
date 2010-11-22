

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
          * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                    * @param retrieveQuantityReasonCodesByCriteria250
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCode[] retrieveQuantityReasonCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria251)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                * @param retrieveQuantityReasonCodesByCriteria250
            
          */
        public void startretrieveQuantityReasonCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria251,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                    * @param retrieveRoutingRoutesByCriteria254
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] retrieveRoutingRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria255,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options256)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                * @param retrieveRoutingRoutesByCriteria254
            
          */
        public void startretrieveRoutingRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria255,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options256,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrders
                    * @param saveRoutingImportOrders259
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrders(

                        java.lang.String regionId260,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders261,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions262)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrders
                * @param saveRoutingImportOrders259
            
          */
        public void startsaveRoutingImportOrders(

            java.lang.String regionId260,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders261,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions262,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUnassignsByCriteria
                    * @param retrieveUnassignsByCriteria265
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria266,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options267)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUnassignsByCriteria
                * @param retrieveUnassignsByCriteria265
            
          */
        public void startretrieveUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria266,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options267,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateDeliveryDetails
                    * @param updateDeliveryDetails270
                
         */

         
                     public void updateDeliveryDetails(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info271)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateDeliveryDetails
                * @param updateDeliveryDetails270
            
          */
        public void startupdateDeliveryDetails(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info271,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedDrivers
                    * @param retrieveAssignedDrivers273
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] retrieveAssignedDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity274)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedDrivers
                * @param retrieveAssignedDrivers273
            
          */
        public void startretrieveAssignedDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity274,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryByCriteria
                    * @param retrievePositionHistoryByCriteria277
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria278)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryByCriteria
                * @param retrievePositionHistoryByCriteria277
            
          */
        public void startretrievePositionHistoryByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria278,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSignature
                    * @param saveStopSignature281
                
         */

         
                     public void saveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity282,javax.activation.DataHandler signatureData283)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSignature
                * @param saveStopSignature281
            
          */
        public void startsaveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity282,javax.activation.DataHandler signatureData283,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUpdateOrder
                    * @param schedulerUpdateOrder285
                
         */

         
                     public boolean schedulerUpdateOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity286,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity287,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options288)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUpdateOrder
                * @param schedulerUpdateOrder285
            
          */
        public void startschedulerUpdateOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity286,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity287,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options288,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportByIdentity
                    * @param retrieveReportByIdentity291
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report retrieveReportByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity292)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportByIdentity
                * @param retrieveReportByIdentity291
            
          */
        public void startretrieveReportByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity292,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                    * @param schedulerDeleteDeliveryWindow295
                
         */

         
                     public void schedulerDeleteDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity296,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window297,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options298)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                * @param schedulerDeleteDeliveryWindow295
            
          */
        public void startschedulerDeleteDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity296,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window297,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options298,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CancelStop
                    * @param cancelStop300
                
         */

         
                     public void cancelStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info301)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CancelStop
                * @param cancelStop300
            
          */
        public void startcancelStop(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info301,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                    * @param retrieveRoutingImportOrderByIdentity303
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder retrieveRoutingImportOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity304,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions305)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                * @param retrieveRoutingImportOrderByIdentity303
            
          */
        public void startretrieveRoutingImportOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity304,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions305,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerPurge
                    * @param schedulerPurge308
                
         */

         
                     public void schedulerPurge(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity309,boolean reloadXML310)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerPurge
                * @param schedulerPurge308
            
          */
        public void startschedulerPurge(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity309,boolean reloadXML310,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteByIdentity
                    * @param retrieveRouteByIdentity312
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity313,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options314)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteByIdentity
                * @param retrieveRouteByIdentity312
            
          */
        public void startretrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity313,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options314,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentityEx
                    * @param retrieveLocationByIdentityEx317
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentityEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity318,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options319)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentityEx
                * @param retrieveLocationByIdentityEx317
            
          */
        public void startretrieveLocationByIdentityEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity318,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options319,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerAnalyzeOrder
                    * @param schedulerAnalyzeOrder322
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] schedulerAnalyzeOrder(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order323,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options324)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerAnalyzeOrder
                * @param schedulerAnalyzeOrder322
            
          */
        public void startschedulerAnalyzeOrder(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order323,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options324,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteria
                    * @param retrieveRouteNotesByCriteria327
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria328)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteria
                * @param retrieveRouteNotesByCriteria327
            
          */
        public void startretrieveRouteNotesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria328,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocations
                    * @param saveLocations331
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations332)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocations
                * @param saveLocations331
            
          */
        public void startsaveLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations332,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                    * @param schedulerRetrieveFeederRoutes335
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] schedulerRetrieveFeederRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity336,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options337)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                * @param schedulerRetrieveFeederRoutes335
            
          */
        public void startschedulerRetrieveFeederRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity336,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options337,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveDestination
                    * @param arriveDestination340
                
         */

         
                     public void arriveDestination(

                        com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info341)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveDestination
                * @param arriveDestination340
            
          */
        public void startarriveDestination(

            com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info341,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                    * @param retrieveRouteDailyStatsByCriteria343
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria344,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options345)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                * @param retrieveRouteDailyStatsByCriteria343
            
          */
        public void startretrieveRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria344,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options345,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                    * @param retrieveStopCancelCodesByCriteria348
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopCancelCode[] retrieveStopCancelCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria349)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                * @param retrieveStopCancelCodesByCriteria348
            
          */
        public void startretrieveStopCancelCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria349,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveReport
                    * @param saveReport352
                
         */

         
                     public void saveReport(

                        com.freshdirect.routing.proxy.stub.transportation.Report report353)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveReport
                * @param saveReport352
            
          */
        public void startsaveReport(

            com.freshdirect.routing.proxy.stub.transportation.Report report353,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionsByCriteria
                    * @param retrieveRegionsByCriteria355
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region[] retrieveRegionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria356)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionsByCriteria
                * @param retrieveRegionsByCriteria355
            
          */
        public void startretrieveRegionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria356,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                    * @param retrieveDefaultRoutingSessionProperties359
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties retrieveDefaultRoutingSessionProperties(

                        java.lang.String regionId360,java.util.Date sessionDate361)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                * @param retrieveDefaultRoutingSessionProperties359
            
          */
        public void startretrieveDefaultRoutingSessionProperties(

            java.lang.String regionId360,java.util.Date sessionDate361,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteSurveyResults
                    * @param saveRouteSurveyResults364
                
         */

         
                     public void saveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity365,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt366,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults367)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteSurveyResults
                * @param saveRouteSurveyResults364
            
          */
        public void startsaveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity365,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt366,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults367,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingRouteNetMatrix
                    * @param buildRoutingRouteNetMatrix369
                
         */

         
                     public void buildRoutingRouteNetMatrix(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity370)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingRouteNetMatrix
                * @param buildRoutingRouteNetMatrix369
            
          */
        public void startbuildRoutingRouteNetMatrix(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity370,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ReturnFault
                    * @param returnFault372
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Fault returnFault(

                        int requestedFaultCode373)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ReturnFault
                * @param returnFault372
            
          */
        public void startreturnFault(

            int requestedFaultCode373,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SuggestRoute
                    * @param suggestRoute376
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] suggestRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop377,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options378)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SuggestRoute
                * @param suggestRoute376
            
          */
        public void startsuggestRoute(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop377,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options378,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByCriteria
                    * @param retrieveNotificationsByCriteria381
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria382,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions383,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options384)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByCriteria
                * @param retrieveNotificationsByCriteria381
            
          */
        public void startretrieveNotificationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria382,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions383,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options384,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CalculateTimeDist
                    * @param calculateTimeDist387
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TimeDistResult calculateTimeDist(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity388,int fromLatitude389,int fromLongitude390,int toLatitude391,int toLongitude392)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CalculateTimeDist
                * @param calculateTimeDist387
            
          */
        public void startcalculateTimeDist(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity388,int fromLatitude389,int fromLongitude390,int toLatitude391,int toLongitude392,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSignature
                    * @param retrieveStopSignature395
                
         */

         
                     public javax.activation.DataHandler retrieveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity396,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType397)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSignature
                * @param retrieveStopSignature395
            
          */
        public void startretrieveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity396,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType397,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerMovableOrders
                    * @param schedulerMovableOrders400
                
         */

         
                     public void schedulerMovableOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity401,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria402,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options403)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerMovableOrders
                * @param schedulerMovableOrders400
            
          */
        public void startschedulerMovableOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity401,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria402,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options403,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRICRegionsByUser
                    * @param retrieveRICRegionsByUser405
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RICRegionsWithPurchaseInfo retrieveRICRegionsByUser(

                        java.lang.String userId406)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRICRegionsByUser
                * @param retrieveRICRegionsByUser405
            
          */
        public void startretrieveRICRegionsByUser(

            java.lang.String userId406,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                    * @param retrieveLocationsByCriteriaEx409
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria410,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options411)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                * @param retrieveLocationsByCriteriaEx409
            
          */
        public void startretrieveLocationsByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria410,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options411,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePermissionsForUser
                    * @param retrievePermissionsForUser414
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserPermissions retrievePermissionsForUser(

                        java.lang.String userID415,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity416)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePermissionsForUser
                * @param retrievePermissionsForUser414
            
          */
        public void startretrievePermissionsForUser(

            java.lang.String userID415,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity416,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyQuestions
                    * @param retrieveRouteSurveyQuestions419
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RetrieveRouteSurveyQuestionsResponse retrieveRouteSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity420,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt421)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyQuestions
                * @param retrieveRouteSurveyQuestions419
            
          */
        public void startretrieveRouteSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity420,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt421,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByCriteria
                    * @param retrieveEquipmentByCriteria425
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment[] retrieveEquipmentByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria426,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options427)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByCriteria
                * @param retrieveEquipmentByCriteria425
            
          */
        public void startretrieveEquipmentByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria426,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options427,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBalanceRoutes
                    * @param schedulerBalanceRoutes430
                
         */

         
                     public void schedulerBalanceRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity431,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options432)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBalanceRoutes
                * @param schedulerBalanceRoutes430
            
          */
        public void startschedulerBalanceRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity431,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options432,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUnassigned
                    * @param saveUnassigned434
                
         */

         
                     public void saveUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop435,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options436)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUnassigned
                * @param saveUnassigned434
            
          */
        public void startsaveUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop435,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options436,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoute
                    * @param removeRoute438
                
         */

         
                     public void removeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity439)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoute
                * @param removeRoute438
            
          */
        public void startremoveRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity439,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                    * @param retrieveNotificationsByRecipientIdentity441
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByRecipientIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity442,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions443,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options444)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                * @param retrieveNotificationsByRecipientIdentity441
            
          */
        public void startretrieveNotificationsByRecipientIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity442,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions443,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options444,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutesByCriteria
                    * @param retrieveRoutesByCriteria447
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route[] retrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria448,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options449)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutesByCriteria
                * @param retrieveRoutesByCriteria447
            
          */
        public void startretrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria448,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options449,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrdersEx
                    * @param schedulerOptimizeOrdersEx452
                
         */

         
                     public void schedulerOptimizeOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity453,com.freshdirect.routing.proxy.stub.transportation.SchedulerOptimizeOrdersExOptions options454)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrdersEx
                * @param schedulerOptimizeOrdersEx452
            
          */
        public void startschedulerOptimizeOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity453,com.freshdirect.routing.proxy.stub.transportation.SchedulerOptimizeOrdersExOptions options454,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegion
                    * @param saveRegion456
                
         */

         
                     public void saveRegion(

                        com.freshdirect.routing.proxy.stub.transportation.Region region457)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegion
                * @param saveRegion456
            
          */
        public void startsaveRegion(

            com.freshdirect.routing.proxy.stub.transportation.Region region457,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionByIdentity
                    * @param retrieveRegionByIdentity459
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region retrieveRegionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity460)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionByIdentity
                * @param retrieveRegionByIdentity459
            
          */
        public void startretrieveRegionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity460,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportsByCriteria
                    * @param retrieveReportsByCriteria463
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report[] retrieveReportsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria464)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportsByCriteria
                * @param retrieveReportsByCriteria463
            
          */
        public void startretrieveReportsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria464,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                    * @param retrieveRoutingLocationsWithOrders467
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrders(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity468)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                * @param retrieveRoutingLocationsWithOrders467
            
          */
        public void startretrieveRoutingLocationsWithOrders(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity468,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserByUserID
                    * @param retrieveUserByUserID471
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.User retrieveUserByUserID(

                        java.lang.String userID472)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserByUserID
                * @param retrieveUserByUserID471
            
          */
        public void startretrieveUserByUserID(

            java.lang.String userID472,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeByIdentity
                    * @param retrieveEmployeeByIdentity475
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity476)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeByIdentity
                * @param retrieveEmployeeByIdentity475
            
          */
        public void startretrieveEmployeeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity476,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUnassigned
                    * @param deleteUnassigned479
                
         */

         
                     public void deleteUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop480)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUnassigned
                * @param deleteUnassigned479
            
          */
        public void startdeleteUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop480,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateRoutingSession
                    * @param createRoutingSession482
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity createRoutingSession(

                        java.lang.String regionId483,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties484)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateRoutingSession
                * @param createRoutingSession482
            
          */
        public void startcreateRoutingSession(

            java.lang.String regionId483,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties484,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                    * @param retrieveUndeliverableStopCodesByCriteria487
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCode[] retrieveUndeliverableStopCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria488)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                * @param retrieveUndeliverableStopCodesByCriteria487
            
          */
        public void startretrieveUndeliverableStopCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria488,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AuthenticateUser
                    * @param authenticateUser491
                
         */

         
                     public void authenticateUser(

                        java.lang.String userID492,java.lang.String password493)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AuthenticateUser
                * @param authenticateUser491
            
          */
        public void startauthenticateUser(

            java.lang.String userID492,java.lang.String password493,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnlockNotifications
                    * @param unlockNotifications495
                
         */

         
                     public void unlockNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria496)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnlockNotifications
                * @param unlockNotifications495
            
          */
        public void startunlockNotifications(

            com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria496,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteRoutingSession
                    * @param deleteRoutingSession498
                
         */

         
                     public void deleteRoutingSession(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity499)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteRoutingSession
                * @param deleteRoutingSession498
            
          */
        public void startdeleteRoutingSession(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity499,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserConfig
                    * @param saveUserConfig501
                
         */

         
                     public void saveUserConfig(

                        java.lang.String applicationID502,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity503,java.lang.String configGroupID504,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items505)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserConfig
                * @param saveUserConfig501
            
          */
        public void startsaveUserConfig(

            java.lang.String applicationID502,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity503,java.lang.String configGroupID504,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items505,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                    * @param schedulerRetrieveOrdersByCriteria507
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerRetrieveOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity508,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria509,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options510)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                * @param schedulerRetrieveOrdersByCriteria507
            
          */
        public void startschedulerRetrieveOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity508,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria509,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options510,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation513
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation513
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserConfig
                    * @param retrieveUserConfig516
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveUserConfig(

                        java.lang.String applicationID517,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity518,java.lang.String configGroupID519)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserConfig
                * @param retrieveUserConfig516
            
          */
        public void startretrieveUserConfig(

            java.lang.String applicationID517,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity518,java.lang.String configGroupID519,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeesByCriteria
                    * @param retrieveEmployeesByCriteria522
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria523)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeesByCriteria
                * @param retrieveEmployeesByCriteria522
            
          */
        public void startretrieveEmployeesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria523,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartOrigin
                    * @param departOrigin526
                
         */

         
                     public void departOrigin(

                        com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info527)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartOrigin
                * @param departOrigin526
            
          */
        public void startdepartOrigin(

            com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info527,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStop
                    * @param saveStop529
                
         */

         
                     public void saveStop(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop530,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions531,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options532)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStop
                * @param saveStop529
            
          */
        public void startsaveStop(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop530,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions531,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options532,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRemoveFromServer
                    * @param schedulerRemoveFromServer534
                
         */

         
                     public void schedulerRemoveFromServer(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity535)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRemoveFromServer
                * @param schedulerRemoveFromServer534
            
          */
        public void startschedulerRemoveFromServer(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity535,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnassignStop
                    * @param unassignStop537
                
         */

         
                     public void unassignStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop538)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnassignStop
                * @param unassignStop537
            
          */
        public void startunassignStop(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop538,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCancelOrder
                    * @param schedulerCancelOrder540
                
         */

         
                     public void schedulerCancelOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity541,java.lang.String orderNumberXML542)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCancelOrder
                * @param schedulerCancelOrder540
            
          */
        public void startschedulerCancelOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity541,java.lang.String orderNumberXML542,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteNotifications
                    * @param deleteNotifications544
                
         */

         
                     public void deleteNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities545)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteNotifications
                * @param deleteNotifications544
            
          */
        public void startdeleteNotifications(

            com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities545,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                    * @param retrieveRoutingRouteByIdentity547
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute retrieveRoutingRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity548,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options549)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                * @param retrieveRoutingRouteByIdentity547
            
          */
        public void startretrieveRoutingRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity548,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options549,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEmployee
                    * @param saveEmployee552
                
         */

         
                     public void saveEmployee(

                        com.freshdirect.routing.proxy.stub.transportation.Employee employee553)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEmployee
                * @param saveEmployee552
            
          */
        public void startsaveEmployee(

            com.freshdirect.routing.proxy.stub.transportation.Employee employee553,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                    * @param retrieveEquipmentTypeByIdentity555
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType retrieveEquipmentTypeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity556,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options557)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                * @param retrieveEquipmentTypeByIdentity555
            
          */
        public void startretrieveEquipmentTypeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity556,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options557,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                    * @param schedulerRetrieveDeliveryWaveInstancesByCriteria560
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance[] schedulerRetrieveDeliveryWaveInstancesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity561,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria562,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options563)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                * @param schedulerRetrieveDeliveryWaveInstancesByCriteria560
            
          */
        public void startschedulerRetrieveDeliveryWaveInstancesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity561,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria562,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options563,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SurveyResponse
                    * @param surveyResponse566
                
         */

         
                     public void surveyResponse(

                        com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info567)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SurveyResponse
                * @param surveyResponse566
            
          */
        public void startsurveyResponse(

            com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info567,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyQuestions
                    * @param retrieveStopSurveyQuestions569
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RetrieveStopSurveyQuestionsResponse retrieveStopSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity570)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyQuestions
                * @param retrieveStopSurveyQuestions569
            
          */
        public void startretrieveStopSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity570,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                    * @param retrieveDutyPeriodsByCriteria574
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] retrieveDutyPeriodsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria575,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options576)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                * @param retrieveDutyPeriodsByCriteria574
            
          */
        public void startretrieveDutyPeriodsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria575,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options576,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWindow
                    * @param schedulerSaveDeliveryWindow579
                
         */

         
                     public boolean schedulerSaveDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity580,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options581)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWindow
                * @param schedulerSaveDeliveryWindow579
            
          */
        public void startschedulerSaveDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity580,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options581,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByIdentity
                    * @param retrieveEquipmentByIdentity584
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment retrieveEquipmentByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity585,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options586)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByIdentity
                * @param retrieveEquipmentByIdentity584
            
          */
        public void startretrieveEquipmentByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity585,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options586,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SendTextMessageToDriver
                    * @param sendTextMessageToDriver589
                
         */

         
                     public void sendTextMessageToDriver(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity590,java.lang.String message591,java.lang.String fromUserID592)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SendTextMessageToDriver
                * @param sendTextMessageToDriver589
            
          */
        public void startsendTextMessageToDriver(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity590,java.lang.String message591,java.lang.String fromUserID592,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                    * @param schedulerRetrieveOrderByIdentity594
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder schedulerRetrieveOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity595,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options596)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                * @param schedulerRetrieveOrderByIdentity594
            
          */
        public void startschedulerRetrieveOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity595,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options596,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedEquipment
                    * @param retrieveAssignedEquipment599
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] retrieveAssignedEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity600)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedEquipment
                * @param retrieveAssignedEquipment599
            
          */
        public void startretrieveAssignedEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity600,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyResults
                    * @param retrieveStopSurveyResults603
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity604)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyResults
                * @param retrieveStopSurveyResults603
            
          */
        public void startretrieveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity604,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentity
                    * @param retrieveLocationByIdentity607
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity608)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentity
                * @param retrieveLocationByIdentity607
            
          */
        public void startretrieveLocationByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity608,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrdersEx
                    * @param saveRoutingImportOrdersEx611
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrdersEx(

                        java.lang.String regionId612,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders613,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions614,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions615)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrdersEx
                * @param saveRoutingImportOrdersEx611
            
          */
        public void startsaveRoutingImportOrdersEx(

            java.lang.String regionId612,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders613,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions614,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions615,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExtendOrderReservation
                    * @param schedulerExtendOrderReservation618
                
         */

         
                     public void schedulerExtendOrderReservation(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity619,java.lang.String orderNumberXML620,int extendMinutes621)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExtendOrderReservation
                * @param schedulerExtendOrderReservation618
            
          */
        public void startschedulerExtendOrderReservation(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity619,java.lang.String orderNumberXML620,int extendMinutes621,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionConfig
                    * @param retrieveRegionConfig623
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveRegionConfig(

                        java.lang.String applicationID624,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity625,java.lang.String configGroupID626)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionConfig
                * @param retrieveRegionConfig623
            
          */
        public void startretrieveRegionConfig(

            java.lang.String applicationID624,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity625,java.lang.String configGroupID626,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                    * @param schedulerSendRoutesToRoadnetEx629
                
         */

         
                     public void schedulerSendRoutesToRoadnetEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity630,java.lang.String sessionDescription631)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                * @param schedulerSendRoutesToRoadnetEx629
            
          */
        public void startschedulerSendRoutesToRoadnetEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity630,java.lang.String sessionDescription631,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrders
                    * @param schedulerOptimizeOrders633
                
         */

         
                     public void schedulerOptimizeOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity634)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrders
                * @param schedulerOptimizeOrders633
            
          */
        public void startschedulerOptimizeOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity634,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                    * @param schedulerSendRoutesToRoadnet636
                
         */

         
                     public void schedulerSendRoutesToRoadnet(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity637)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                * @param schedulerSendRoutesToRoadnet636
            
          */
        public void startschedulerSendRoutesToRoadnet(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity637,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignDrivers
                    * @param assignDrivers639
                
         */

         
                     public void assignDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity640,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers641)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignDrivers
                * @param assignDrivers639
            
          */
        public void startassignDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity640,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers641,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                    * @param retrieveRoutingLocationsWithOrdersEx643
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity644,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options645)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                * @param retrieveRoutingLocationsWithOrdersEx643
            
          */
        public void startretrieveRoutingLocationsWithOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity644,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options645,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoute
                    * @param saveRoute648
                
         */

         
                     public void saveRoute(

                        com.freshdirect.routing.proxy.stub.transportation.Route route649,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions650,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions651)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoute
                * @param saveRoute648
            
          */
        public void startsaveRoute(

            com.freshdirect.routing.proxy.stub.transportation.Route route649,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions650,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions651,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEquipment
                    * @param saveEquipment653
                
         */

         
                     public void saveEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.Equipment equipment654,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options655)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEquipment
                * @param saveEquipment653
            
          */
        public void startsaveEquipment(

            com.freshdirect.routing.proxy.stub.transportation.Equipment equipment654,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options655,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopByIdentity
                    * @param retrieveStopByIdentity657
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop retrieveStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity658,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options659)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopByIdentity
                * @param retrieveStopByIdentity657
            
          */
        public void startretrieveStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity658,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options659,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BulkArriveDepartStop
                    * @param bulkArriveDepartStop662
                
         */

         
                     public void bulkArriveDepartStop(

                        com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos663)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BulkArriveDepartStop
                * @param bulkArriveDepartStop662
            
          */
        public void startbulkArriveDepartStop(

            com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos663,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                    * @param retrieveRoutingSessionsByCriteria665
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] retrieveRoutingSessionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria666,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options667)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                * @param retrieveRoutingSessionsByCriteria665
            
          */
        public void startretrieveRoutingSessionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria666,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options667,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                    * @param retrieveRoutingUnassignsByCriteria670
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria671,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options672)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                * @param retrieveRoutingUnassignsByCriteria670
            
          */
        public void startretrieveRoutingUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria671,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options672,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                    * @param retrieveRoutingImportOrdersByCriteria675
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] retrieveRoutingImportOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria676,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions677)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                * @param retrieveRoutingImportOrdersByCriteria675
            
          */
        public void startretrieveRoutingImportOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria676,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions677,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop680
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop680
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ChangeUserPassword
                    * @param changeUserPassword683
                
         */

         
                     public void changeUserPassword(

                        java.lang.String userID684,java.lang.String oldPassword685,java.lang.String newPassword686)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ChangeUserPassword
                * @param changeUserPassword683
            
          */
        public void startchangeUserPassword(

            java.lang.String userID684,java.lang.String oldPassword685,java.lang.String newPassword686,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CompleteRoute
                    * @param completeRoute688
                
         */

         
                     public void completeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info689)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CompleteRoute
                * @param completeRoute688
            
          */
        public void startcompleteRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info689,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                    * @param schedulerExcludeCutoffRoutes691
                
         */

         
                     public void schedulerExcludeCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity692,boolean excludeXML693)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                * @param schedulerExcludeCutoffRoutes691
            
          */
        public void startschedulerExcludeCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity692,boolean excludeXML693,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                    * @param retrieveRoutingOrderByIdentity695
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder retrieveRoutingOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity696,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options697)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                * @param retrieveRoutingOrderByIdentity695
            
          */
        public void startretrieveRoutingOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity696,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options697,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__TextMessage
                    * @param textMessage700
                
         */

         
                     public void textMessage(

                        com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info701)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__TextMessage
                * @param textMessage700
            
          */
        public void starttextMessage(

            com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info701,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                    * @param retrieveRoutingStopsByCriteria703
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria704,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options705)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                * @param retrieveRoutingStopsByCriteria703
            
          */
        public void startretrieveRoutingStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria704,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options705,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                    * @param schedulerCalculateDeliveryWindowMetrics708
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics[] schedulerCalculateDeliveryWindowMetrics(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity709,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options710)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                * @param schedulerCalculateDeliveryWindowMetrics708
            
          */
        public void startschedulerCalculateDeliveryWindowMetrics(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity709,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options710,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocations
                    * @param deleteLocations713
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] deleteLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations714)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocations
                * @param deleteLocations713
            
          */
        public void startdeleteLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations714,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveProductsPurchased
                    * @param retrieveProductsPurchased717
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased retrieveProductsPurchased(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity718)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveProductsPurchased
                * @param retrieveProductsPurchased717
            
          */
        public void startretrieveProductsPurchased(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity718,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                    * @param retrieveRoutingSourcedOrdersByCriteria721
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] retrieveRoutingSourcedOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria722,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options723)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                * @param retrieveRoutingSourcedOrdersByCriteria721
            
          */
        public void startretrieveRoutingSourcedOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria722,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options723,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSurveyResults
                    * @param saveStopSurveyResults726
                
         */

         
                     public void saveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity727,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults728)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSurveyResults
                * @param saveStopSurveyResults726
            
          */
        public void startsaveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity727,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults728,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                    * @param retrieveRoutingRouteDailyStatsByCriteria730
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRoutingRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria731,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options732)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                * @param retrieveRoutingRouteDailyStatsByCriteria730
            
          */
        public void startretrieveRoutingRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria731,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options732,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStopSignature
                    * @param updateStopSignature735
                
         */

         
                     public void updateStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info736)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStopSignature
                * @param updateStopSignature735
            
          */
        public void startupdateStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info736,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveGlobalConfig
                    * @param saveGlobalConfig738
                
         */

         
                     public void saveGlobalConfig(

                        java.lang.String applicationID739,java.lang.String configGroupID740,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items741)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveGlobalConfig
                * @param saveGlobalConfig738
            
          */
        public void startsaveGlobalConfig(

            java.lang.String applicationID739,java.lang.String configGroupID740,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items741,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                    * @param schedulerIsExcludingCutoffRoutes743
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult schedulerIsExcludingCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity744)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                * @param schedulerIsExcludingCutoffRoutes743
            
          */
        public void startschedulerIsExcludingCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity744,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                    * @param retrieveCannedTextMessagesByCriteria747
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] retrieveCannedTextMessagesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria748)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                * @param retrieveCannedTextMessagesByCriteria747
            
          */
        public void startretrieveCannedTextMessagesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria748,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReserveOrder
                    * @param schedulerReserveOrder751
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ReserveResult schedulerReserveOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity752,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder753,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow754,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options755)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReserveOrder
                * @param schedulerReserveOrder751
            
          */
        public void startschedulerReserveOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity752,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder753,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow754,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options755,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteria
                    * @param retrieveLocationsByCriteria758
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria759)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteria
                * @param retrieveLocationsByCriteria758
            
          */
        public void startretrieveLocationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria759,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopsByCriteria
                    * @param retrieveStopsByCriteria762
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria763,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options764)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopsByCriteria
                * @param retrieveStopsByCriteria762
            
          */
        public void startretrieveStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria763,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options764,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionOptions
                    * @param retrieveRegionOptions767
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RegionOptions retrieveRegionOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity768)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionOptions
                * @param retrieveRegionOptions767
            
          */
        public void startretrieveRegionOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity768,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SequenceStop
                    * @param sequenceStop771
                
         */

         
                     public void sequenceStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info772)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SequenceStop
                * @param sequenceStop771
            
          */
        public void startsequenceStop(

            com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info772,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyResults
                    * @param retrieveRouteSurveyResults774
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity775,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt776)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyResults
                * @param retrieveRouteSurveyResults774
            
          */
        public void startretrieveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity775,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt776,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePosition
                    * @param updateRoutePosition779
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info780)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePosition
                * @param updateRoutePosition779
            
          */
        public void startupdateRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info780,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MassStopSequence
                    * @param massStopSequence783
                
         */

         
                     public void massStopSequence(

                        com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info784)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MassStopSequence
                * @param massStopSequence783
            
          */
        public void startmassStopSequence(

            com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info784,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AddRICUser
                    * @param addRICUser786
                
         */

         
                     public void addRICUser(

                        com.freshdirect.routing.proxy.stub.transportation.User user787)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AddRICUser
                * @param addRICUser786
            
          */
        public void startaddRICUser(

            com.freshdirect.routing.proxy.stub.transportation.User user787,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                    * @param retrieveRoutingSessionByIdentity789
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession retrieveRoutingSessionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity790,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options791)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                * @param retrieveRoutingSessionByIdentity789
            
          */
        public void startretrieveRoutingSessionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity790,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options791,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartStop
                    * @param departStop794
                
         */

         
                     public void departStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info795)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartStop
                * @param departStop794
            
          */
        public void startdepartStop(

            com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info795,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerConfirmOrder
                    * @param schedulerConfirmOrder797
                
         */

         
                     public void schedulerConfirmOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity798,java.lang.String orderNumberXML799)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerConfirmOrder
                * @param schedulerConfirmOrder797
            
          */
        public void startschedulerConfirmOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity798,java.lang.String orderNumberXML799,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AllowAdditionOfRICUsers
                    * @param allowAdditionOfRICUsers801
                
         */

         
                     public boolean allowAdditionOfRICUsers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AllowAdditionOfRICUsers
                * @param allowAdditionOfRICUsers801
            
          */
        public void startallowAdditionOfRICUsers(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGlobalConfig
                    * @param retrieveGlobalConfig804
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveGlobalConfig(

                        java.lang.String applicationID805,java.lang.String configGroupID806)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGlobalConfig
                * @param retrieveGlobalConfig804
            
          */
        public void startretrieveGlobalConfig(

            java.lang.String applicationID805,java.lang.String configGroupID806,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                    * @param retrieveEquipmentTypeByCriteria809
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] retrieveEquipmentTypeByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria810,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options811)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                * @param retrieveEquipmentTypeByCriteria809
            
          */
        public void startretrieveEquipmentTypeByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria810,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options811,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                    * @param retrievePositionHistoryBlocksByCriteria814
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryBlocksByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria815)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                * @param retrievePositionHistoryBlocksByCriteria814
            
          */
        public void startretrievePositionHistoryBlocksByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria815,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReloadWaveInstances
                    * @param schedulerReloadWaveInstances818
                
         */

         
                     public boolean schedulerReloadWaveInstances(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity819,com.freshdirect.routing.proxy.stub.transportation.SchedulerReloadWaveInstancesOptions options820)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReloadWaveInstances
                * @param schedulerReloadWaveInstances818
            
          */
        public void startschedulerReloadWaveInstances(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity819,com.freshdirect.routing.proxy.stub.transportation.SchedulerReloadWaveInstancesOptions options820,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveyDetails
                    * @param retrieveSurveyDetails823
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyDetails[] retrieveSurveyDetails(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity824)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveyDetails
                * @param retrieveSurveyDetails823
            
          */
        public void startretrieveSurveyDetails(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity824,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationsEx
                    * @param saveLocationsEx827
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocationsEx(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations828,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options829)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationsEx
                * @param saveLocationsEx827
            
          */
        public void startsaveLocationsEx(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations828,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options829,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveStop
                    * @param arriveStop832
                
         */

         
                     public void arriveStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info833)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveStop
                * @param arriveStop832
            
          */
        public void startarriveStop(

            com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info833,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                    * @param schedulerSaveDeliveryWaveInstance835
                
         */

         
                     public java.lang.String[] schedulerSaveDeliveryWaveInstance(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity836,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity837,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes838,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options839)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                * @param schedulerSaveDeliveryWaveInstance835
            
          */
        public void startschedulerSaveDeliveryWaveInstance(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity836,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity837,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes838,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options839,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopByIdentity
                    * @param retrieveRoutingStopByIdentity842
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop retrieveRoutingStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity843,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options844)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopByIdentity
                * @param retrieveRoutingStopByIdentity842
            
          */
        public void startretrieveRoutingStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity843,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options844,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteReport
                    * @param deleteReport847
                
         */

         
                     public void deleteReport(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity848)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteReport
                * @param deleteReport847
            
          */
        public void startdeleteReport(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity848,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegionConfig
                    * @param saveRegionConfig850
                
         */

         
                     public void saveRegionConfig(

                        java.lang.String applicationID851,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity852,java.lang.String configGroupID853,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items854)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegionConfig
                * @param saveRegionConfig850
            
          */
        public void startsaveRegionConfig(

            java.lang.String applicationID851,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity852,java.lang.String configGroupID853,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items854,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBulkReserveOrders
                    * @param schedulerBulkReserveOrders856
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerBulkReserveOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity857,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders858,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options859)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBulkReserveOrders
                * @param schedulerBulkReserveOrders856
            
          */
        public void startschedulerBulkReserveOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity857,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders858,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options859,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamps
                    * @param convertTimestamps862
                
         */

         
                     public java.util.Calendar[] convertTimestamps(

                        java.util.Calendar[] sourceTimestamps863,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions864,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions865)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamps
                * @param convertTimestamps862
            
          */
        public void startconvertTimestamps(

            java.util.Calendar[] sourceTimestamps863,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions864,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions865,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteForDevice
                    * @param retrieveRouteForDevice868
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteForDevice(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity869,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options870,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity871)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteForDevice
                * @param retrieveRouteForDevice868
            
          */
        public void startretrieveRouteForDevice(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity869,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options870,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity871,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignEquipment
                    * @param assignEquipment874
                
         */

         
                     public void assignEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity875,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment876)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignEquipment
                * @param assignEquipment874
            
          */
        public void startassignEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity875,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment876,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamp
                    * @param convertTimestamp878
                
         */

         
                     public java.util.Calendar convertTimestamp(

                        java.util.Calendar sourceTimestamp879,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions880,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions881)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamp
                * @param convertTimestamp878
            
          */
        public void startconvertTimestamp(

            java.util.Calendar sourceTimestamp879,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions880,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions881,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveys
                    * @param retrieveSurveys884
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Survey[] retrieveSurveys(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity885)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveys
                * @param retrieveSurveys884
            
          */
        public void startretrieveSurveys(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity885,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                    * @param retrieveDutyPeriodByIdentity888
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod retrieveDutyPeriodByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity889,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options890)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                * @param retrieveDutyPeriodByIdentity888
            
          */
        public void startretrieveDutyPeriodByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity889,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options890,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__PlaceUnassigned
                    * @param placeUnassigned893
                
         */

         
                     public void placeUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop894,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity895,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition896,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime897,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions898)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__PlaceUnassigned
                * @param placeUnassigned893
            
          */
        public void startplaceUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop894,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity895,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition896,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime897,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions898,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__StartRoute
                    * @param startRoute900
                
         */

         
                     public void startRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info901)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__StartRoute
                * @param startRoute900
            
          */
        public void startstartRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info901,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    