

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
          * Service definition of function ns1__RetrieveUserDefinedColumnByIdentity
                    * @param retrieveUserDefinedColumnByIdentity330
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn retrieveUserDefinedColumnByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity331)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnByIdentity
                * @param retrieveUserDefinedColumnByIdentity330
            
          */
        public void startretrieveUserDefinedColumnByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity331,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                    * @param buildDispatchDriverDirectionsEx334
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirectionsEx(

                        com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info335)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                * @param buildDispatchDriverDirectionsEx334
            
          */
        public void startbuildDispatchDriverDirectionsEx(

            com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info335,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyResults
                    * @param retrieveStopSurveyResults338
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity339)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyResults
                * @param retrieveStopSurveyResults338
            
          */
        public void startretrieveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity339,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                    * @param schedulerRetrieveFeederRoutes342
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] schedulerRetrieveFeederRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity343,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options344)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                * @param schedulerRetrieveFeederRoutes342
            
          */
        public void startschedulerRetrieveFeederRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity343,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options344,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignDrivers
                    * @param assignDrivers347
                
         */

         
                     public void assignDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity348,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers349)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignDrivers
                * @param assignDrivers347
            
          */
        public void startassignDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity348,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers349,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopByIdentity
                    * @param retrieveStopByIdentity351
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop retrieveStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity352,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options353)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopByIdentity
                * @param retrieveStopByIdentity351
            
          */
        public void startretrieveStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity352,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options353,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeesByCriteria
                    * @param retrieveEmployeesByCriteria356
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria357)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeesByCriteria
                * @param retrieveEmployeesByCriteria356
            
          */
        public void startretrieveEmployeesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria357,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                    * @param retrieveRoutingRouteByIdentity360
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute retrieveRoutingRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity361,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options362)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                * @param retrieveRoutingRouteByIdentity360
            
          */
        public void startretrieveRoutingRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity361,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options362,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                    * @param retrieveRoutingSessionByIdentity365
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession retrieveRoutingSessionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity366,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options367)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                * @param retrieveRoutingSessionByIdentity365
            
          */
        public void startretrieveRoutingSessionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity366,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options367,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                    * @param schedulerRetrieveOrdersByCriteria370
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerRetrieveOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity371,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria372,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options373)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                * @param schedulerRetrieveOrdersByCriteria370
            
          */
        public void startschedulerRetrieveOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity371,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria372,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options373,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                    * @param schedulerDeleteDeliveryWindow376
                
         */

         
                     public void schedulerDeleteDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity377,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window378,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options379)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                * @param schedulerDeleteDeliveryWindow376
            
          */
        public void startschedulerDeleteDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity377,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window378,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options379,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSurveyResults
                    * @param saveStopSurveyResults381
                
         */

         
                     public void saveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity382,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults383)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSurveyResults
                * @param saveStopSurveyResults381
            
          */
        public void startsaveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity382,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults383,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerMovableOrders
                    * @param schedulerMovableOrders385
                
         */

         
                     public void schedulerMovableOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity386,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria387,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options388)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerMovableOrders
                * @param schedulerMovableOrders385
            
          */
        public void startschedulerMovableOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity386,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria387,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options388,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                    * @param schedulerRetrieveDeliveryWaveInstancesByCriteria390
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance[] schedulerRetrieveDeliveryWaveInstancesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity391,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria392,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options393)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                * @param schedulerRetrieveDeliveryWaveInstancesByCriteria390
            
          */
        public void startschedulerRetrieveDeliveryWaveInstancesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity391,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria392,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options393,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CancelStop
                    * @param cancelStop396
                
         */

         
                     public void cancelStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info397)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CancelStop
                * @param cancelStop396
            
          */
        public void startcancelStop(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info397,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryByCriteria
                    * @param retrievePositionHistoryByCriteria399
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria400)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryByCriteria
                * @param retrievePositionHistoryByCriteria399
            
          */
        public void startretrievePositionHistoryByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria400,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoute
                    * @param removeRoute403
                
         */

         
                     public void removeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity404)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoute
                * @param removeRoute403
            
          */
        public void startremoveRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity404,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnlockNotifications
                    * @param unlockNotifications406
                
         */

         
                     public void unlockNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria407)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnlockNotifications
                * @param unlockNotifications406
            
          */
        public void startunlockNotifications(

            com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria407,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerPurge
                    * @param schedulerPurge409
                
         */

         
                     public void schedulerPurge(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity410,boolean reloadXML411)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerPurge
                * @param schedulerPurge409
            
          */
        public void startschedulerPurge(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity410,boolean reloadXML411,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteNotifications
                    * @param deleteNotifications413
                
         */

         
                     public void deleteNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities414)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteNotifications
                * @param deleteNotifications413
            
          */
        public void startdeleteNotifications(

            com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities414,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateDeliveryDetails
                    * @param updateDeliveryDetails416
                
         */

         
                     public void updateDeliveryDetails(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info417)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateDeliveryDetails
                * @param updateDeliveryDetails416
            
          */
        public void startupdateDeliveryDetails(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info417,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrders
                    * @param schedulerOptimizeOrders419
                
         */

         
                     public void schedulerOptimizeOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity420)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrders
                * @param schedulerOptimizeOrders419
            
          */
        public void startschedulerOptimizeOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity420,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                    * @param retrieveDutyPeriodsByCriteria422
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] retrieveDutyPeriodsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria423,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options424)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                * @param retrieveDutyPeriodsByCriteria422
            
          */
        public void startretrieveDutyPeriodsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria423,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options424,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                    * @param retrieveRoutingRouteDailyStatsByCriteria427
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRoutingRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria428,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options429)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                * @param retrieveRoutingRouteDailyStatsByCriteria427
            
          */
        public void startretrieveRoutingRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria428,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options429,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSignature
                    * @param retrieveStopSignature432
                
         */

         
                     public javax.activation.DataHandler retrieveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity433,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType434)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSignature
                * @param retrieveStopSignature432
            
          */
        public void startretrieveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity433,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType434,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGlobalConfig
                    * @param retrieveGlobalConfig437
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveGlobalConfig(

                        java.lang.String applicationID438,java.lang.String configGroupID439)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGlobalConfig
                * @param retrieveGlobalConfig437
            
          */
        public void startretrieveGlobalConfig(

            java.lang.String applicationID438,java.lang.String configGroupID439,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionByIdentity
                    * @param retrieveRegionByIdentity442
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region retrieveRegionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity443)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionByIdentity
                * @param retrieveRegionByIdentity442
            
          */
        public void startretrieveRegionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity443,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerLoad
                    * @param schedulerLoad446
                
         */

         
                     public void schedulerLoad(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity447)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerLoad
                * @param schedulerLoad446
            
          */
        public void startschedulerLoad(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity447,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteReport
                    * @param deleteReport449
                
         */

         
                     public void deleteReport(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity450)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteReport
                * @param deleteReport449
            
          */
        public void startdeleteReport(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity450,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyQuestions
                    * @param retrieveStopSurveyQuestions452
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveStopSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity453)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyQuestions
                * @param retrieveStopSurveyQuestions452
            
          */
        public void startretrieveStopSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity453,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedDrivers
                    * @param retrieveAssignedDrivers456
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] retrieveAssignedDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity457)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedDrivers
                * @param retrieveAssignedDrivers456
            
          */
        public void startretrieveAssignedDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity457,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ChangeUserPassword
                    * @param changeUserPassword460
                
         */

         
                     public void changeUserPassword(

                        java.lang.String userID461,java.lang.String oldPassword462,java.lang.String newPassword463)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ChangeUserPassword
                * @param changeUserPassword460
            
          */
        public void startchangeUserPassword(

            java.lang.String userID461,java.lang.String oldPassword462,java.lang.String newPassword463,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                    * @param deleteSapShipmentsBySessionIdentity465
                
         */

         
                     public boolean deleteSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid466)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                * @param deleteSapShipmentsBySessionIdentity465
            
          */
        public void startdeleteSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid466,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                    * @param retrievePlanningUserDefinedFieldInfo469
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedFieldInfo[] retrievePlanningUserDefinedFieldInfo(

                        java.lang.String regionId470)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                * @param retrievePlanningUserDefinedFieldInfo469
            
          */
        public void startretrievePlanningUserDefinedFieldInfo(

            java.lang.String regionId470,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                    * @param retrieveUserDefinedColumnsByCriteria473
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn[] retrieveUserDefinedColumnsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria474)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                * @param retrieveUserDefinedColumnsByCriteria473
            
          */
        public void startretrieveUserDefinedColumnsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria474,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CalculateTimeDist
                    * @param calculateTimeDist477
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TimeDistResult calculateTimeDist(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity478,int fromLatitude479,int fromLongitude480,int toLatitude481,int toLongitude482)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CalculateTimeDist
                * @param calculateTimeDist477
            
          */
        public void startcalculateTimeDist(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity478,int fromLatitude479,int fromLongitude480,int toLatitude481,int toLongitude482,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegionConfig
                    * @param saveRegionConfig485
                
         */

         
                     public void saveRegionConfig(

                        java.lang.String applicationID486,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity487,java.lang.String configGroupID488,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items489)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegionConfig
                * @param saveRegionConfig485
            
          */
        public void startsaveRegionConfig(

            java.lang.String applicationID486,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity487,java.lang.String configGroupID488,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items489,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                    * @param retrieveStopNotesByCriteriaEx491
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopNote[] retrieveStopNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria492,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options493)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                * @param retrieveStopNotesByCriteriaEx491
            
          */
        public void startretrieveStopNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria492,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options493,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteForDevice
                    * @param retrieveRouteForDevice496
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteForDevice(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity497,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options498,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity499)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteForDevice
                * @param retrieveRouteForDevice496
            
          */
        public void startretrieveRouteForDevice(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity497,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options498,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity499,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionETAs
                    * @param updateRoutePositionETAs502
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionETAs(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId503,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos504)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionETAs
                * @param updateRoutePositionETAs502
            
          */
        public void startupdateRoutePositionETAs(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId503,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos504,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SendTextMessageToDriver
                    * @param sendTextMessageToDriver507
                
         */

         
                     public void sendTextMessageToDriver(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity508,java.lang.String message509,java.lang.String fromUserID510)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SendTextMessageToDriver
                * @param sendTextMessageToDriver507
            
          */
        public void startsendTextMessageToDriver(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity508,java.lang.String message509,java.lang.String fromUserID510,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedTable
                    * @param deleteUserDefinedTable512
                
         */

         
                     public void deleteUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId513)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedTable
                * @param deleteUserDefinedTable512
            
          */
        public void startdeleteUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId513,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSkus
                    * @param saveSkus515
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] saveSkus(

                        com.freshdirect.routing.proxy.stub.transportation.Sku[] skus516)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSkus
                * @param saveSkus515
            
          */
        public void startsaveSkus(

            com.freshdirect.routing.proxy.stub.transportation.Sku[] skus516,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                    * @param retrieveSapShipmentsBySessionIdentity519
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SapShipment[] retrieveSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid520)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                * @param retrieveSapShipmentsBySessionIdentity519
            
          */
        public void startretrieveSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid520,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExtendOrderReservation
                    * @param schedulerExtendOrderReservation523
                
         */

         
                     public void schedulerExtendOrderReservation(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity524,java.lang.String orderNumberXML525,int extendMinutes526)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExtendOrderReservation
                * @param schedulerExtendOrderReservation523
            
          */
        public void startschedulerExtendOrderReservation(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity524,java.lang.String orderNumberXML525,int extendMinutes526,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionOptions
                    * @param retrieveRegionOptions528
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RegionOptions retrieveRegionOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity529)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionOptions
                * @param retrieveRegionOptions528
            
          */
        public void startretrieveRegionOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity529,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSignature
                    * @param saveStopSignature532
                
         */

         
                     public void saveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity533,javax.activation.DataHandler signatureData534)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSignature
                * @param saveStopSignature532
            
          */
        public void startsaveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity533,javax.activation.DataHandler signatureData534,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                    * @param retrieveUndeliverableStopCodesByCriteria536
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCode[] retrieveUndeliverableStopCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria537)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                * @param retrieveUndeliverableStopCodesByCriteria536
            
          */
        public void startretrieveUndeliverableStopCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria537,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteRoutingSession
                    * @param deleteRoutingSession540
                
         */

         
                     public void deleteRoutingSession(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity541)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteRoutingSession
                * @param deleteRoutingSession540
            
          */
        public void startdeleteRoutingSession(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity541,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                    * @param schedulerIsExcludingCutoffRoutes543
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult schedulerIsExcludingCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity544)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                * @param schedulerIsExcludingCutoffRoutes543
            
          */
        public void startschedulerIsExcludingCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity544,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                    * @param retrieveRoutingImportOrderByIdentity547
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder retrieveRoutingImportOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity548,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions549)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                * @param retrieveRoutingImportOrderByIdentity547
            
          */
        public void startretrieveRoutingImportOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity548,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions549,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkuByIdentity
                    * @param retrieveSkuByIdentity552
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku retrieveSkuByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity553)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkuByIdentity
                * @param retrieveSkuByIdentity552
            
          */
        public void startretrieveSkuByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity553,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                    * @param retrieveQuantityReasonCodesByCriteria556
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCode[] retrieveQuantityReasonCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria557)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                * @param retrieveQuantityReasonCodesByCriteria556
            
          */
        public void startretrieveQuantityReasonCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria557,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                    * @param retrieveLineItemNotesByCriteriaEx560
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LineItemNote[] retrieveLineItemNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria561,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options562)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                * @param retrieveLineItemNotesByCriteriaEx560
            
          */
        public void startretrieveLineItemNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria561,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options562,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                    * @param retrieveOrderNotesByCriteriaEx565
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.OrderNote[] retrieveOrderNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria566,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options567)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                * @param retrieveOrderNotesByCriteriaEx565
            
          */
        public void startretrieveOrderNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria566,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options567,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                    * @param retrieveEquipmentTypeByIdentity570
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType retrieveEquipmentTypeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity571,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options572)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                * @param retrieveEquipmentTypeByIdentity570
            
          */
        public void startretrieveEquipmentTypeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity571,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options572,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUnassigned
                    * @param deleteUnassigned575
                
         */

         
                     public void deleteUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop576)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUnassigned
                * @param deleteUnassigned575
            
          */
        public void startdeleteUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop576,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                    * @param retrieveRoutingSourcedOrdersByCriteria578
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] retrieveRoutingSourcedOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria579,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options580)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                * @param retrieveRoutingSourcedOrdersByCriteria578
            
          */
        public void startretrieveRoutingSourcedOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria579,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options580,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserConfig
                    * @param saveUserConfig583
                
         */

         
                     public void saveUserConfig(

                        java.lang.String applicationID584,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity585,java.lang.String configGroupID586,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items587)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserConfig
                * @param saveUserConfig583
            
          */
        public void startsaveUserConfig(

            java.lang.String applicationID584,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity585,java.lang.String configGroupID586,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items587,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeByIdentity
                    * @param retrieveEmployeeByIdentity589
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity590)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeByIdentity
                * @param retrieveEmployeeByIdentity589
            
          */
        public void startretrieveEmployeeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity590,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUpdateOrder
                    * @param schedulerUpdateOrder593
                
         */

         
                     public boolean schedulerUpdateOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity594,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity595,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options596)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUpdateOrder
                * @param schedulerUpdateOrder593
            
          */
        public void startschedulerUpdateOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity594,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity595,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options596,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SurveyResponse
                    * @param surveyResponse599
                
         */

         
                     public void surveyResponse(

                        com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info600)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SurveyResponse
                * @param surveyResponse599
            
          */
        public void startsurveyResponse(

            com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info600,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RedeliverStop
                    * @param redeliverStop602
                
         */

         
                     public void redeliverStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info603)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RedeliverStop
                * @param redeliverStop602
            
          */
        public void startredeliverStop(

            com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info603,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByCriteria
                    * @param retrieveEquipmentByCriteria605
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment[] retrieveEquipmentByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria606,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options607)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByCriteria
                * @param retrieveEquipmentByCriteria605
            
          */
        public void startretrieveEquipmentByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria606,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options607,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartOrigin
                    * @param departOrigin610
                
         */

         
                     public void departOrigin(

                        com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info611)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartOrigin
                * @param departOrigin610
            
          */
        public void startdepartOrigin(

            com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info611,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionsByCriteria
                    * @param retrieveRegionsByCriteria613
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region[] retrieveRegionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria614)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionsByCriteria
                * @param retrieveRegionsByCriteria613
            
          */
        public void startretrieveRegionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria614,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateRoutingSession
                    * @param createRoutingSession617
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity createRoutingSession(

                        java.lang.String regionId618,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties619)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateRoutingSession
                * @param createRoutingSession617
            
          */
        public void startcreateRoutingSession(

            java.lang.String regionId618,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties619,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                    * @param retrieveDefaultRoutingSessionProperties622
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties retrieveDefaultRoutingSessionProperties(

                        java.lang.String regionId623,java.util.Date sessionDate624)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                * @param retrieveDefaultRoutingSessionProperties622
            
          */
        public void startretrieveDefaultRoutingSessionProperties(

            java.lang.String regionId623,java.util.Date sessionDate624,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyResults
                    * @param retrieveRouteSurveyResults627
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity628,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt629)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyResults
                * @param retrieveRouteSurveyResults627
            
          */
        public void startretrieveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity628,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt629,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerAnalyzeOrder
                    * @param schedulerAnalyzeOrder632
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] schedulerAnalyzeOrder(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order633,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options634)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerAnalyzeOrder
                * @param schedulerAnalyzeOrder632
            
          */
        public void startschedulerAnalyzeOrder(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order633,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options634,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                    * @param retrieveDutyPeriodByIdentity637
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod retrieveDutyPeriodByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity638,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options639)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                * @param retrieveDutyPeriodByIdentity637
            
          */
        public void startretrieveDutyPeriodByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity638,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options639,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopsByCriteria
                    * @param retrieveStopsByCriteria642
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria643,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options644)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopsByCriteria
                * @param retrieveStopsByCriteria642
            
          */
        public void startretrieveStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria643,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options644,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                    * @param retrieveDepotSkuAvailabilityByIdentity647
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability retrieveDepotSkuAvailabilityByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity648)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                * @param retrieveDepotSkuAvailabilityByIdentity647
            
          */
        public void startretrieveDepotSkuAvailabilityByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity648,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingRouteNetMatrix
                    * @param buildRoutingRouteNetMatrix651
                
         */

         
                     public void buildRoutingRouteNetMatrix(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity652)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingRouteNetMatrix
                * @param buildRoutingRouteNetMatrix651
            
          */
        public void startbuildRoutingRouteNetMatrix(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity652,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                    * @param retrieveRoutingSessionsByCriteria654
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] retrieveRoutingSessionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria655,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options656)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                * @param retrieveRoutingSessionsByCriteria654
            
          */
        public void startretrieveRoutingSessionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria655,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options656,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamps
                    * @param convertTimestamps659
                
         */

         
                     public java.util.Calendar[] convertTimestamps(

                        java.util.Calendar[] sourceTimestamps660,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions661,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions662)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamps
                * @param convertTimestamps659
            
          */
        public void startconvertTimestamps(

            java.util.Calendar[] sourceTimestamps660,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions661,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions662,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByCriteria
                    * @param retrieveNotificationsByCriteria665
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria666,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions667,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options668)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByCriteria
                * @param retrieveNotificationsByCriteria665
            
          */
        public void startretrieveNotificationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria666,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions667,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options668,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReserveOrder
                    * @param schedulerReserveOrder671
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ReserveResult schedulerReserveOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity672,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder673,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow674,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options675)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReserveOrder
                * @param schedulerReserveOrder671
            
          */
        public void startschedulerReserveOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity672,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder673,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow674,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options675,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteSurveyResults
                    * @param saveRouteSurveyResults678
                
         */

         
                     public void saveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity679,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt680,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults681)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteSurveyResults
                * @param saveRouteSurveyResults678
            
          */
        public void startsaveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity679,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt680,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults681,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                    * @param retrieveRoutingUnassignsByCriteria683
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria684,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options685)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                * @param retrieveRoutingUnassignsByCriteria683
            
          */
        public void startretrieveRoutingUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria684,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options685,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__PlaceUnassigned
                    * @param placeUnassigned688
                
         */

         
                     public void placeUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop689,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity690,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition691,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime692,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions693)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__PlaceUnassigned
                * @param placeUnassigned688
            
          */
        public void startplaceUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop689,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity690,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition691,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime692,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions693,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateTelematicsCachePositions
                    * @param updateTelematicsCachePositions695
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions696)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateTelematicsCachePositions
                * @param updateTelematicsCachePositions695
            
          */
        public void startupdateTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions696,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                    * @param retrievePlanningLocationExtensionsByCriteria699
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] retrievePlanningLocationExtensionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria700,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options701)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                * @param retrievePlanningLocationExtensionsByCriteria699
            
          */
        public void startretrievePlanningLocationExtensionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria700,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options701,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BulkArriveDepartStop
                    * @param bulkArriveDepartStop704
                
         */

         
                     public void bulkArriveDepartStop(

                        com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos705)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BulkArriveDepartStop
                * @param bulkArriveDepartStop704
            
          */
        public void startbulkArriveDepartStop(

            com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos705,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SavePlanningLocationExtensions
                    * @param savePlanningLocationExtensions707
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] savePlanningLocationExtensions(

                        java.lang.String regionId708,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions709)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SavePlanningLocationExtensions
                * @param savePlanningLocationExtensions707
            
          */
        public void startsavePlanningLocationExtensions(

            java.lang.String regionId708,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions709,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CleanupTelematicsCachePositions
                    * @param cleanupTelematicsCachePositions712
                
         */

         
                     public void cleanupTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria713)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CleanupTelematicsCachePositions
                * @param cleanupTelematicsCachePositions712
            
          */
        public void startcleanupTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria713,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutesByCriteria
                    * @param retrieveRoutesByCriteria715
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route[] retrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria716,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options717)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutesByCriteria
                * @param retrieveRoutesByCriteria715
            
          */
        public void startretrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria716,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options717,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                    * @param schedulerRetrieveRouteByIdentity720
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute schedulerRetrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity721,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options722)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                * @param schedulerRetrieveRouteByIdentity720
            
          */
        public void startschedulerRetrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity721,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options722,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRouteETAs
                    * @param updateRouteETAs725
                
         */

         
                     public void updateRouteETAs(

                        com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info726)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRouteETAs
                * @param updateRouteETAs725
            
          */
        public void startupdateRouteETAs(

            com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info726,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ReturnFault
                    * @param returnFault728
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Fault returnFault(

                        int requestedFaultCode729)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ReturnFault
                * @param returnFault728
            
          */
        public void startreturnFault(

            int requestedFaultCode729,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveyDetails
                    * @param retrieveSurveyDetails732
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyDetails[] retrieveSurveyDetails(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity733)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveyDetails
                * @param retrieveSurveyDetails732
            
          */
        public void startretrieveSurveyDetails(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity733,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AddRICUser
                    * @param addRICUser736
                
         */

         
                     public void addRICUser(

                        com.freshdirect.routing.proxy.stub.transportation.User user737)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AddRICUser
                * @param addRICUser736
            
          */
        public void startaddRICUser(

            com.freshdirect.routing.proxy.stub.transportation.User user737,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveProductsPurchased
                    * @param retrieveProductsPurchased739
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased retrieveProductsPurchased(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity740)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveProductsPurchased
                * @param retrieveProductsPurchased739
            
          */
        public void startretrieveProductsPurchased(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity740,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePermissionsForUser
                    * @param retrievePermissionsForUser743
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserPermissions retrievePermissionsForUser(

                        java.lang.String userID744,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity745)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePermissionsForUser
                * @param retrievePermissionsForUser743
            
          */
        public void startretrievePermissionsForUser(

            java.lang.String userID744,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity745,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocations
                    * @param deleteLocations748
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] deleteLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations749)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocations
                * @param deleteLocations748
            
          */
        public void startdeleteLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations749,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedEquipment
                    * @param retrieveAssignedEquipment752
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] retrieveAssignedEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity753)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedEquipment
                * @param retrieveAssignedEquipment752
            
          */
        public void startretrieveAssignedEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity753,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopByIdentity
                    * @param retrieveRoutingStopByIdentity756
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop retrieveRoutingStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity757,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options758)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopByIdentity
                * @param retrieveRoutingStopByIdentity756
            
          */
        public void startretrieveRoutingStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity757,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options758,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation761
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation761
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirections
                    * @param buildDispatchDriverDirections764
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity765)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirections
                * @param buildDispatchDriverDirections764
            
          */
        public void startbuildDispatchDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity765,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                    * @param retrieveTelematicsCachePositionsByCriteria768
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] retrieveTelematicsCachePositionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria769)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                * @param retrieveTelematicsCachePositionsByCriteria768
            
          */
        public void startretrieveTelematicsCachePositionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria769,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStopSignature
                    * @param updateStopSignature772
                
         */

         
                     public void updateStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info773)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStopSignature
                * @param updateStopSignature772
            
          */
        public void startupdateStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info773,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                    * @param schedulerSaveDeliveryWaveInstance775
                
         */

         
                     public java.lang.String[] schedulerSaveDeliveryWaveInstance(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity776,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity777,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes778,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options779)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                * @param schedulerSaveDeliveryWaveInstance775
            
          */
        public void startschedulerSaveDeliveryWaveInstance(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity776,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity777,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes778,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options779,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AllowAdditionOfRICUsers
                    * @param allowAdditionOfRICUsers782
                
         */

         
                     public boolean allowAdditionOfRICUsers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AllowAdditionOfRICUsers
                * @param allowAdditionOfRICUsers782
            
          */
        public void startallowAdditionOfRICUsers(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGPSProviderOptions
                    * @param retrieveGPSProviderOptions785
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.GPSProviderOptions retrieveGPSProviderOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity786)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGPSProviderOptions
                * @param retrieveGPSProviderOptions785
            
          */
        public void startretrieveGPSProviderOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity786,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                    * @param retrieveRoutingLocationsWithOrders789
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrders(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity790)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                * @param retrieveRoutingLocationsWithOrders789
            
          */
        public void startretrieveRoutingLocationsWithOrders(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity790,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteria
                    * @param retrieveRouteNotesByCriteria793
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria794)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteria
                * @param retrieveRouteNotesByCriteria793
            
          */
        public void startretrieveRouteNotesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria794,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveReport
                    * @param saveReport797
                
         */

         
                     public void saveReport(

                        com.freshdirect.routing.proxy.stub.transportation.Report report798)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveReport
                * @param saveReport797
            
          */
        public void startsaveReport(

            com.freshdirect.routing.proxy.stub.transportation.Report report798,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveys
                    * @param retrieveSurveys800
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Survey[] retrieveSurveys(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity801)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveys
                * @param retrieveSurveys800
            
          */
        public void startretrieveSurveys(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity801,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                    * @param retrieveNotificationsByRecipientIdentity804
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByRecipientIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity805,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions806,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options807)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                * @param retrieveNotificationsByRecipientIdentity804
            
          */
        public void startretrieveNotificationsByRecipientIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity805,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions806,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options807,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBalanceRoutes
                    * @param schedulerBalanceRoutes810
                
         */

         
                     public void schedulerBalanceRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity811,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options812)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBalanceRoutes
                * @param schedulerBalanceRoutes810
            
          */
        public void startschedulerBalanceRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity811,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options812,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                    * @param retrieveUserDefinedDataByCriteria814
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] retrieveUserDefinedDataByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity815,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria816,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions817)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                * @param retrieveUserDefinedDataByCriteria814
            
          */
        public void startretrieveUserDefinedDataByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity815,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria816,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions817,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkusByCriteria
                    * @param retrieveSkusByCriteria820
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] retrieveSkusByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria821)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkusByCriteria
                * @param retrieveSkusByCriteria820
            
          */
        public void startretrieveSkusByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria821,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                    * @param retrieveCannedTextMessagesByCriteria824
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] retrieveCannedTextMessagesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria825)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                * @param retrieveCannedTextMessagesByCriteria824
            
          */
        public void startretrieveCannedTextMessagesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria825,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                    * @param retrieveUserDefinedDataByIdentity828
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData retrieveUserDefinedDataByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity829,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity830,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions831)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                * @param retrieveUserDefinedDataByIdentity828
            
          */
        public void startretrieveUserDefinedDataByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity829,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity830,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions831,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                    * @param retrieveEquipmentTypeByCriteria834
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] retrieveEquipmentTypeByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria835,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options836)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                * @param retrieveEquipmentTypeByCriteria834
            
          */
        public void startretrieveEquipmentTypeByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria835,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options836,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                    * @param retrieveRoutingOrderByIdentity839
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder retrieveRoutingOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity840,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options841)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                * @param retrieveRoutingOrderByIdentity839
            
          */
        public void startretrieveRoutingOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity840,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options841,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__TextMessage
                    * @param textMessage844
                
         */

         
                     public void textMessage(

                        com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info845)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__TextMessage
                * @param textMessage844
            
          */
        public void starttextMessage(

            com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info845,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CompleteRoute
                    * @param completeRoute847
                
         */

         
                     public void completeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info848)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CompleteRoute
                * @param completeRoute847
            
          */
        public void startcompleteRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info848,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                    * @param retrieveLocationsByCriteriaEx850
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria851,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options852)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                * @param retrieveLocationsByCriteriaEx850
            
          */
        public void startretrieveLocationsByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria851,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options852,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveStop
                    * @param arriveStop855
                
         */

         
                     public void arriveStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info856)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveStop
                * @param arriveStop855
            
          */
        public void startarriveStop(

            com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info856,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedTable
                    * @param saveUserDefinedTable858
                
         */

         
                     public void saveUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table859)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedTable
                * @param saveUserDefinedTable858
            
          */
        public void startsaveUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table859,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoute
                    * @param saveRoute861
                
         */

         
                     public void saveRoute(

                        com.freshdirect.routing.proxy.stub.transportation.Route route862,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions863,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions864)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoute
                * @param saveRoute861
            
          */
        public void startsaveRoute(

            com.freshdirect.routing.proxy.stub.transportation.Route route862,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions863,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions864,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveDestination
                    * @param arriveDestination866
                
         */

         
                     public void arriveDestination(

                        com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info867)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveDestination
                * @param arriveDestination866
            
          */
        public void startarriveDestination(

            com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info867,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByIdentity
                    * @param retrieveEquipmentByIdentity869
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment retrieveEquipmentByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity870,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options871)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByIdentity
                * @param retrieveEquipmentByIdentity869
            
          */
        public void startretrieveEquipmentByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity870,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options871,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationsEx
                    * @param saveLocationsEx874
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocationsEx(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations875,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options876)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationsEx
                * @param saveLocationsEx874
            
          */
        public void startsaveLocationsEx(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations875,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options876,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteByIdentity
                    * @param retrieveRouteByIdentity879
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity880,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options881)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteByIdentity
                * @param retrieveRouteByIdentity879
            
          */
        public void startretrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity880,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options881,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserConfig
                    * @param retrieveUserConfig884
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveUserConfig(

                        java.lang.String applicationID885,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity886,java.lang.String configGroupID887)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserConfig
                * @param retrieveUserConfig884
            
          */
        public void startretrieveUserConfig(

            java.lang.String applicationID885,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity886,java.lang.String configGroupID887,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionEx
                    * @param updateRoutePositionEx890
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos891)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionEx
                * @param updateRoutePositionEx890
            
          */
        public void startupdateRoutePositionEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos891,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentity
                    * @param retrieveLocationByIdentity894
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity895)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentity
                * @param retrieveLocationByIdentity894
            
          */
        public void startretrieveLocationByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity895,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserByUserID
                    * @param retrieveUserByUserID898
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.User retrieveUserByUserID(

                        java.lang.String userID899)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserByUserID
                * @param retrieveUserByUserID898
            
          */
        public void startretrieveUserByUserID(

            java.lang.String userID899,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignEquipment
                    * @param assignEquipment902
                
         */

         
                     public void assignEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity903,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment904)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignEquipment
                * @param assignEquipment902
            
          */
        public void startassignEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity903,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment904,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveGlobalConfig
                    * @param saveGlobalConfig906
                
         */

         
                     public void saveGlobalConfig(

                        java.lang.String applicationID907,java.lang.String configGroupID908,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items909)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveGlobalConfig
                * @param saveGlobalConfig906
            
          */
        public void startsaveGlobalConfig(

            java.lang.String applicationID907,java.lang.String configGroupID908,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items909,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEquipment
                    * @param saveEquipment911
                
         */

         
                     public void saveEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.Equipment equipment912,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options913)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEquipment
                * @param saveEquipment911
            
          */
        public void startsaveEquipment(

            com.freshdirect.routing.proxy.stub.transportation.Equipment equipment912,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options913,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerConfirmOrder
                    * @param schedulerConfirmOrder915
                
         */

         
                     public void schedulerConfirmOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity916,java.lang.String orderNumberXML917)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerConfirmOrder
                * @param schedulerConfirmOrder915
            
          */
        public void startschedulerConfirmOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity916,java.lang.String orderNumberXML917,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsOptions
                    * @param retrieveTelematicsOptions919
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsOptions retrieveTelematicsOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity920)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsOptions
                * @param retrieveTelematicsOptions919
            
          */
        public void startretrieveTelematicsOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity920,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                    * @param retrieveUserDefinedTableByIdentity923
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable retrieveUserDefinedTableByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity924)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                * @param retrieveUserDefinedTableByIdentity923
            
          */
        public void startretrieveUserDefinedTableByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity924,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteria
                    * @param retrieveLocationsByCriteria927
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria928)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteria
                * @param retrieveLocationsByCriteria927
            
          */
        public void startretrieveLocationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria928,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreatePlanningSession
                    * @param createPlanningSession931
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity createPlanningSession(

                        java.lang.String regionId932,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties933)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreatePlanningSession
                * @param createPlanningSession931
            
          */
        public void startcreatePlanningSession(

            java.lang.String regionId932,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties933,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentityEx
                    * @param retrieveLocationByIdentityEx936
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentityEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity937,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options938)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentityEx
                * @param retrieveLocationByIdentityEx936
            
          */
        public void startretrieveLocationByIdentityEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity937,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options938,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                    * @param retrievePlanningTerritoriesByCriteria941
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningTerritory[] retrievePlanningTerritoriesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria942,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options943)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                * @param retrievePlanningTerritoriesByCriteria941
            
          */
        public void startretrievePlanningTerritoriesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria942,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options943,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamp
                    * @param convertTimestamp946
                
         */

         
                     public java.util.Calendar convertTimestamp(

                        java.util.Calendar sourceTimestamp947,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions948,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions949)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamp
                * @param convertTimestamp946
            
          */
        public void startconvertTimestamp(

            java.util.Calendar sourceTimestamp947,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions948,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions949,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                    * @param schedulerSendRoutesToRoadnetEx952
                
         */

         
                     public void schedulerSendRoutesToRoadnetEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity953,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options954)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                * @param schedulerSendRoutesToRoadnetEx952
            
          */
        public void startschedulerSendRoutesToRoadnetEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity953,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options954,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                    * @param schedulerSendRoutesToRoadnet956
                
         */

         
                     public void schedulerSendRoutesToRoadnet(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity957)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                * @param schedulerSendRoutesToRoadnet956
            
          */
        public void startschedulerSendRoutesToRoadnet(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity957,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportsByCriteria
                    * @param retrieveReportsByCriteria959
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report[] retrieveReportsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria960)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportsByCriteria
                * @param retrieveReportsByCriteria959
            
          */
        public void startretrieveReportsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria960,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                    * @param retrieveRouteNotesByCriteriaEx963
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria964,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options965)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                * @param retrieveRouteNotesByCriteriaEx963
            
          */
        public void startretrieveRouteNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria964,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options965,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedData
                    * @param saveUserDefinedData968
                
         */

         
                     public void saveUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity969,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input970,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions971)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedData
                * @param saveUserDefinedData968
            
          */
        public void startsaveUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity969,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input970,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions971,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUnassignsByCriteria
                    * @param retrieveUnassignsByCriteria973
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria974,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options975)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUnassignsByCriteria
                * @param retrieveUnassignsByCriteria973
            
          */
        public void startretrieveUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria974,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options975,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutes
                    * @param schedulerRebuildRoutes978
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity979)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutes
                * @param schedulerRebuildRoutes978
            
          */
        public void startschedulerRebuildRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity979,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeletePlanningLocationExtensions
                    * @param deletePlanningLocationExtensions982
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] deletePlanningLocationExtensions(

                        java.lang.String regionId983,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions984)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeletePlanningLocationExtensions
                * @param deletePlanningLocationExtensions982
            
          */
        public void startdeletePlanningLocationExtensions(

            java.lang.String regionId983,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions984,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DescribeAuthenticaitonPolicy
                    * @param describeAuthenticaitonPolicy987
                
         */

         
                     public java.lang.String describeAuthenticaitonPolicy(

                        java.lang.String localeId988)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DescribeAuthenticaitonPolicy
                * @param describeAuthenticaitonPolicy987
            
          */
        public void startdescribeAuthenticaitonPolicy(

            java.lang.String localeId988,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop991
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop991
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCancelOrder
                    * @param schedulerCancelOrder994
                
         */

         
                     public void schedulerCancelOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity995,java.lang.String orderNumberXML996)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCancelOrder
                * @param schedulerCancelOrder994
            
          */
        public void startschedulerCancelOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity995,java.lang.String orderNumberXML996,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePosition
                    * @param updateRoutePosition998
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info999)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePosition
                * @param updateRoutePosition998
            
          */
        public void startupdateRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info999,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWindow
                    * @param schedulerSaveDeliveryWindow2
                
         */

         
                     public boolean schedulerSaveDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity3,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options4)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWindow
                * @param schedulerSaveDeliveryWindow2
            
          */
        public void startschedulerSaveDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity3,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options4,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                    * @param retrieveRoutingImportOrdersByCriteria7
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] retrieveRoutingImportOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria8,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions9)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                * @param retrieveRoutingImportOrdersByCriteria7
            
          */
        public void startretrieveRoutingImportOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria8,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions9,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SuggestRoute
                    * @param suggestRoute12
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] suggestRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop13,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options14)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SuggestRoute
                * @param suggestRoute12
            
          */
        public void startsuggestRoute(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop13,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options14,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__StartRoute
                    * @param startRoute17
                
         */

         
                     public void startRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info18)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__StartRoute
                * @param startRoute17
            
          */
        public void startstartRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info18,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUnload
                    * @param schedulerUnload20
                
         */

         
                     public void schedulerUnload(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity21)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUnload
                * @param schedulerUnload20
            
          */
        public void startschedulerUnload(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity21,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                    * @param schedulerExcludeCutoffRoutes23
                
         */

         
                     public void schedulerExcludeCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity24,boolean excludeXML25)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                * @param schedulerExcludeCutoffRoutes23
            
          */
        public void startschedulerExcludeCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity24,boolean excludeXML25,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnassignStop
                    * @param unassignStop27
                
         */

         
                     public void unassignStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop28)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnassignStop
                * @param unassignStop27
            
          */
        public void startunassignStop(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop28,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                    * @param retrieveRouteDailyStatsByCriteria30
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria31,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options32)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                * @param retrieveRouteDailyStatsByCriteria30
            
          */
        public void startretrieveRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria31,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options32,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                    * @param retrievePlanningSessionPropertiesByIdentity35
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession retrievePlanningSessionPropertiesByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity36)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                * @param retrievePlanningSessionPropertiesByIdentity35
            
          */
        public void startretrievePlanningSessionPropertiesByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity36,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingDriverDirections
                    * @param buildRoutingDriverDirections39
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildRoutingDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity40)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingDriverDirections
                * @param buildRoutingDriverDirections39
            
          */
        public void startbuildRoutingDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity40,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocations
                    * @param saveLocations43
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations44)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocations
                * @param saveLocations43
            
          */
        public void startsaveLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations44,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSapShipments
                    * @param saveSapShipments47
                
         */

         
                     public boolean saveSapShipments(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid48,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments49)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSapShipments
                * @param saveSapShipments47
            
          */
        public void startsaveSapShipments(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid48,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments49,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionConfig
                    * @param retrieveRegionConfig52
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveRegionConfig(

                        java.lang.String applicationID53,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity54,java.lang.String configGroupID55)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionConfig
                * @param retrieveRegionConfig52
            
          */
        public void startretrieveRegionConfig(

            java.lang.String applicationID53,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity54,java.lang.String configGroupID55,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReloadWaveInstances
                    * @param schedulerReloadWaveInstances58
                
         */

         
                     public boolean schedulerReloadWaveInstances(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity59,com.freshdirect.routing.proxy.stub.transportation.SchedulerReloadWaveInstancesOptions options60)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReloadWaveInstances
                * @param schedulerReloadWaveInstances58
            
          */
        public void startschedulerReloadWaveInstances(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity59,com.freshdirect.routing.proxy.stub.transportation.SchedulerReloadWaveInstancesOptions options60,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                    * @param retrieveRoutingLocationsWithOrdersEx63
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity64,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options65)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                * @param retrieveRoutingLocationsWithOrdersEx63
            
          */
        public void startretrieveRoutingLocationsWithOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity64,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options65,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyQuestions
                    * @param retrieveRouteSurveyQuestions68
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveRouteSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity69,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt70)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyQuestions
                * @param retrieveRouteSurveyQuestions68
            
          */
        public void startretrieveRouteSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity69,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt70,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegion
                    * @param saveRegion73
                
         */

         
                     public void saveRegion(

                        com.freshdirect.routing.proxy.stub.transportation.Region region74)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegion
                * @param saveRegion73
            
          */
        public void startsaveRegion(

            com.freshdirect.routing.proxy.stub.transportation.Region region74,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                    * @param schedulerRetrieveRoutesByCriteria76
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute[] schedulerRetrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria77,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options78)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                * @param schedulerRetrieveRoutesByCriteria76
            
          */
        public void startschedulerRetrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria77,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options78,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipment
                    * @param deleteSapShipment81
                
         */

         
                     public boolean deleteSapShipment(

                        com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid82)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipment
                * @param deleteSapShipment81
            
          */
        public void startdeleteSapShipment(

            com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid82,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUnassigned
                    * @param saveUnassigned85
                
         */

         
                     public void saveUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop86,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options87)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUnassigned
                * @param saveUnassigned85
            
          */
        public void startsaveUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop86,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options87,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrders
                    * @param saveRoutingImportOrders89
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrders(

                        java.lang.String regionId90,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders91,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions92)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrders
                * @param saveRoutingImportOrders89
            
          */
        public void startsaveRoutingImportOrders(

            java.lang.String regionId90,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders91,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions92,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                    * @param retrievePositionHistoryBlocksByCriteria95
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryBlocksByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria96)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                * @param retrievePositionHistoryBlocksByCriteria95
            
          */
        public void startretrievePositionHistoryBlocksByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria96,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SequenceStop
                    * @param sequenceStop99
                
         */

         
                     public void sequenceStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info100)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SequenceStop
                * @param sequenceStop99
            
          */
        public void startsequenceStop(

            com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info100,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportByIdentity
                    * @param retrieveReportByIdentity102
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report retrieveReportByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity103)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportByIdentity
                * @param retrieveReportByIdentity102
            
          */
        public void startretrieveReportByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity103,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedData
                    * @param deleteUserDefinedData106
                
         */

         
                     public void deleteUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity107,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input108)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedData
                * @param deleteUserDefinedData106
            
          */
        public void startdeleteUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity107,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input108,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                    * @param retrieveUserDefinedTablesByCriteria110
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable[] retrieveUserDefinedTablesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria111)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                * @param retrieveUserDefinedTablesByCriteria110
            
          */
        public void startretrieveUserDefinedTablesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria111,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                    * @param retrieveRoutingRoutesByCriteria114
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] retrieveRoutingRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria115,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options116)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                * @param retrieveRoutingRoutesByCriteria114
            
          */
        public void startretrieveRoutingRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria115,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options116,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                    * @param retrieveRoutingStopsByCriteria119
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria120,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options121)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                * @param retrieveRoutingStopsByCriteria119
            
          */
        public void startretrieveRoutingStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria120,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options121,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBulkReserveOrders
                    * @param schedulerBulkReserveOrders124
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerBulkReserveOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity125,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders126,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options127)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBulkReserveOrders
                * @param schedulerBulkReserveOrders124
            
          */
        public void startschedulerBulkReserveOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity125,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders126,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options127,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                    * @param retrieveDepotSkusAvailabilitiesByCriteria130
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] retrieveDepotSkusAvailabilitiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria131)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                * @param retrieveDepotSkusAvailabilitiesByCriteria130
            
          */
        public void startretrieveDepotSkusAvailabilitiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria131,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRICRegionsByUser
                    * @param retrieveRICRegionsByUser134
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RICRegionsWithPurchaseInfo retrieveRICRegionsByUser(

                        java.lang.String userId135)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRICRegionsByUser
                * @param retrieveRICRegionsByUser134
            
          */
        public void startretrieveRICRegionsByUser(

            java.lang.String userId135,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MassStopSequence
                    * @param massStopSequence138
                
         */

         
                     public void massStopSequence(

                        com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info139)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MassStopSequence
                * @param massStopSequence138
            
          */
        public void startmassStopSequence(

            com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info139,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                    * @param retrieveDefaultPlanningSessionProperties141
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties retrieveDefaultPlanningSessionProperties(

                        java.lang.String regionId142)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                * @param retrieveDefaultPlanningSessionProperties141
            
          */
        public void startretrieveDefaultPlanningSessionProperties(

            java.lang.String regionId142,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                    * @param schedulerRetrieveOrderByIdentity145
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder schedulerRetrieveOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity146,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options147)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                * @param schedulerRetrieveOrderByIdentity145
            
          */
        public void startschedulerRetrieveOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity146,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options147,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                    * @param schedulerCalculateDeliveryWindowMetrics150
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics[] schedulerCalculateDeliveryWindowMetrics(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity151,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options152)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                * @param schedulerCalculateDeliveryWindowMetrics150
            
          */
        public void startschedulerCalculateDeliveryWindowMetrics(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity151,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options152,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutesEx
                    * @param schedulerRebuildRoutesEx155
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutesEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity156,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options157)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutesEx
                * @param schedulerRebuildRoutesEx155
            
          */
        public void startschedulerRebuildRoutesEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity156,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options157,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                    * @param retrievePlanningSessionPropertiesByCriteria160
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession[] retrievePlanningSessionPropertiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria161)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                * @param retrievePlanningSessionPropertiesByCriteria160
            
          */
        public void startretrievePlanningSessionPropertiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria161,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveDepotSkusAvailabilities
                    * @param saveDepotSkusAvailabilities164
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] saveDepotSkusAvailabilities(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus165)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveDepotSkusAvailabilities
                * @param saveDepotSkusAvailabilities164
            
          */
        public void startsaveDepotSkusAvailabilities(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus165,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartStop
                    * @param departStop168
                
         */

         
                     public void departStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info169)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartStop
                * @param departStop168
            
          */
        public void startdepartStop(

            com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info169,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEmployee
                    * @param saveEmployee171
                
         */

         
                     public void saveEmployee(

                        com.freshdirect.routing.proxy.stub.transportation.Employee employee172)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEmployee
                * @param saveEmployee171
            
          */
        public void startsaveEmployee(

            com.freshdirect.routing.proxy.stub.transportation.Employee employee172,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStop
                    * @param saveStop174
                
         */

         
                     public void saveStop(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop175,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions176,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options177)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStop
                * @param saveStop174
            
          */
        public void startsaveStop(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop175,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions176,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options177,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                    * @param retrieveAutoArriveDepartOptions179
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AutoArriveDepartOptions retrieveAutoArriveDepartOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity180)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                * @param retrieveAutoArriveDepartOptions179
            
          */
        public void startretrieveAutoArriveDepartOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity180,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AuthenticateUser
                    * @param authenticateUser183
                
         */

         
                     public int authenticateUser(

                        java.lang.String userID184,java.lang.String password185)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AuthenticateUser
                * @param authenticateUser183
            
          */
        public void startauthenticateUser(

            java.lang.String userID184,java.lang.String password185,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrdersEx
                    * @param saveRoutingImportOrdersEx188
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrdersEx(

                        java.lang.String regionId189,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders190,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions191,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions192)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrdersEx
                * @param saveRoutingImportOrdersEx188
            
          */
        public void startsaveRoutingImportOrdersEx(

            java.lang.String regionId189,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders190,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions191,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions192,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                    * @param retrieveStopCancelCodesByCriteria195
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopCancelCode[] retrieveStopCancelCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria196)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                * @param retrieveStopCancelCodesByCriteria195
            
          */
        public void startretrieveStopCancelCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria196,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    