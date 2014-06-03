

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
                    * @param retrieveUserDefinedColumnByIdentity382
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn retrieveUserDefinedColumnByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity383)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnByIdentity
                * @param retrieveUserDefinedColumnByIdentity382
            
          */
        public void startretrieveUserDefinedColumnByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity383,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                    * @param buildDispatchDriverDirectionsEx386
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirectionsEx(

                        com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info387)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                * @param buildDispatchDriverDirectionsEx386
            
          */
        public void startbuildDispatchDriverDirectionsEx(

            com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info387,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyResults
                    * @param retrieveStopSurveyResults390
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity391)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyResults
                * @param retrieveStopSurveyResults390
            
          */
        public void startretrieveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity391,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                    * @param schedulerRetrieveFeederRoutes394
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] schedulerRetrieveFeederRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity395,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options396)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                * @param schedulerRetrieveFeederRoutes394
            
          */
        public void startschedulerRetrieveFeederRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity395,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options396,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveActiveAlertRecipientsByLocationIdentity
                    * @param retrieveActiveAlertRecipientsByLocationIdentity399
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] retrieveActiveAlertRecipientsByLocationIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity400)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveActiveAlertRecipientsByLocationIdentity
                * @param retrieveActiveAlertRecipientsByLocationIdentity399
            
          */
        public void startretrieveActiveAlertRecipientsByLocationIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity400,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignDrivers
                    * @param assignDrivers403
                
         */

         
                     public void assignDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity404,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers405)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignDrivers
                * @param assignDrivers403
            
          */
        public void startassignDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity404,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers405,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopByIdentity
                    * @param retrieveStopByIdentity407
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop retrieveStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity408,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options409)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopByIdentity
                * @param retrieveStopByIdentity407
            
          */
        public void startretrieveStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity408,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options409,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeesByCriteria
                    * @param retrieveEmployeesByCriteria412
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria413)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeesByCriteria
                * @param retrieveEmployeesByCriteria412
            
          */
        public void startretrieveEmployeesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria413,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                    * @param retrieveRoutingRouteByIdentity416
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute retrieveRoutingRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity417,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options418)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                * @param retrieveRoutingRouteByIdentity416
            
          */
        public void startretrieveRoutingRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity417,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options418,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                    * @param retrieveRoutingSessionByIdentity421
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession retrieveRoutingSessionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity422,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options423)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                * @param retrieveRoutingSessionByIdentity421
            
          */
        public void startretrieveRoutingSessionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity422,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options423,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                    * @param schedulerRetrieveOrdersByCriteria426
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerRetrieveOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity427,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria428,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options429)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                * @param schedulerRetrieveOrdersByCriteria426
            
          */
        public void startschedulerRetrieveOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity427,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria428,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options429,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                    * @param schedulerDeleteDeliveryWindow432
                
         */

         
                     public void schedulerDeleteDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity433,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window434,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options435)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                * @param schedulerDeleteDeliveryWindow432
            
          */
        public void startschedulerDeleteDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity433,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window434,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options435,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSurveyResults
                    * @param saveStopSurveyResults437
                
         */

         
                     public void saveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity438,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults439)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSurveyResults
                * @param saveStopSurveyResults437
            
          */
        public void startsaveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity438,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults439,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveStandardRoute
                    * @param removeStandardRoute441
                
         */

         
                     public void removeStandardRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity442,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRemoveOptions options443)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveStandardRoute
                * @param removeStandardRoute441
            
          */
        public void startremoveStandardRoute(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity442,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRemoveOptions options443,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerMovableOrders
                    * @param schedulerMovableOrders445
                
         */

         
                     public void schedulerMovableOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity446,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria447,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options448)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerMovableOrders
                * @param schedulerMovableOrders445
            
          */
        public void startschedulerMovableOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity446,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria447,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options448,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                    * @param schedulerRetrieveDeliveryWaveInstancesByCriteria450
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance[] schedulerRetrieveDeliveryWaveInstancesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity451,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria452,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options453)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                * @param schedulerRetrieveDeliveryWaveInstancesByCriteria450
            
          */
        public void startschedulerRetrieveDeliveryWaveInstancesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity451,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria452,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options453,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsRoutes
                    * @param retrieveTelematicsRoutes456
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsRoute[] retrieveTelematicsRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionId457)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsRoutes
                * @param retrieveTelematicsRoutes456
            
          */
        public void startretrieveTelematicsRoutes(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionId457,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CancelStop
                    * @param cancelStop460
                
         */

         
                     public void cancelStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info461)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CancelStop
                * @param cancelStop460
            
          */
        public void startcancelStop(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info461,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryByCriteria
                    * @param retrievePositionHistoryByCriteria463
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria464)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryByCriteria
                * @param retrievePositionHistoryByCriteria463
            
          */
        public void startretrievePositionHistoryByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria464,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoute
                    * @param removeRoute467
                
         */

         
                     public void removeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity468)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoute
                * @param removeRoute467
            
          */
        public void startremoveRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity468,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnlockNotifications
                    * @param unlockNotifications470
                
         */

         
                     public void unlockNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria471)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnlockNotifications
                * @param unlockNotifications470
            
          */
        public void startunlockNotifications(

            com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria471,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerPurge
                    * @param schedulerPurge473
                
         */

         
                     public void schedulerPurge(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity474,boolean reloadXML475)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerPurge
                * @param schedulerPurge473
            
          */
        public void startschedulerPurge(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity474,boolean reloadXML475,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateDeliveryDetails
                    * @param updateDeliveryDetails477
                
         */

         
                     public void updateDeliveryDetails(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info478)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateDeliveryDetails
                * @param updateDeliveryDetails477
            
          */
        public void startupdateDeliveryDetails(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info478,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationComments
                    * @param saveLocationComments480
                
         */

         
                     public void saveLocationComments(

                        com.freshdirect.routing.proxy.stub.transportation.LocationComment[] comments481,com.freshdirect.routing.proxy.stub.transportation.SaveLocationCommentOptions options482)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationComments
                * @param saveLocationComments480
            
          */
        public void startsaveLocationComments(

            com.freshdirect.routing.proxy.stub.transportation.LocationComment[] comments481,com.freshdirect.routing.proxy.stub.transportation.SaveLocationCommentOptions options482,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                    * @param retrieveRoutingRouteDailyStatsByCriteria484
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRoutingRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria485,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options486)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                * @param retrieveRoutingRouteDailyStatsByCriteria484
            
          */
        public void startretrieveRoutingRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria485,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options486,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteNotifications
                    * @param deleteNotifications489
                
         */

         
                     public void deleteNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities490)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteNotifications
                * @param deleteNotifications489
            
          */
        public void startdeleteNotifications(

            com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities490,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrders
                    * @param schedulerOptimizeOrders492
                
         */

         
                     public void schedulerOptimizeOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity493)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrders
                * @param schedulerOptimizeOrders492
            
          */
        public void startschedulerOptimizeOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity493,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                    * @param retrieveDutyPeriodsByCriteria495
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] retrieveDutyPeriodsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria496,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options497)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                * @param retrieveDutyPeriodsByCriteria495
            
          */
        public void startretrieveDutyPeriodsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria496,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options497,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSignature
                    * @param retrieveStopSignature500
                
         */

         
                     public javax.activation.DataHandler retrieveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity501,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType502)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSignature
                * @param retrieveStopSignature500
            
          */
        public void startretrieveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity501,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType502,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationCommentsByCriteria
                    * @param retrieveLocationCommentsByCriteria505
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LocationComment[] retrieveLocationCommentsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCommentCriteria criteria506)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationCommentsByCriteria
                * @param retrieveLocationCommentsByCriteria505
            
          */
        public void startretrieveLocationCommentsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCommentCriteria criteria506,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGlobalConfig
                    * @param retrieveGlobalConfig509
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveGlobalConfig(

                        java.lang.String applicationID510,java.lang.String configGroupID511)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGlobalConfig
                * @param retrieveGlobalConfig509
            
          */
        public void startretrieveGlobalConfig(

            java.lang.String applicationID510,java.lang.String configGroupID511,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionByIdentity
                    * @param retrieveRegionByIdentity514
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region retrieveRegionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity515)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionByIdentity
                * @param retrieveRegionByIdentity514
            
          */
        public void startretrieveRegionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity515,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerLoad
                    * @param schedulerLoad518
                
         */

         
                     public void schedulerLoad(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity519)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerLoad
                * @param schedulerLoad518
            
          */
        public void startschedulerLoad(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity519,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteReport
                    * @param deleteReport521
                
         */

         
                     public void deleteReport(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity522)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteReport
                * @param deleteReport521
            
          */
        public void startdeleteReport(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity522,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyQuestions
                    * @param retrieveStopSurveyQuestions524
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveStopSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity525)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyQuestions
                * @param retrieveStopSurveyQuestions524
            
          */
        public void startretrieveStopSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity525,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedDrivers
                    * @param retrieveAssignedDrivers528
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] retrieveAssignedDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity529)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedDrivers
                * @param retrieveAssignedDrivers528
            
          */
        public void startretrieveAssignedDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity529,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStandardRouteSets
                    * @param saveStandardRouteSets532
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRejection[] saveStandardRouteSets(

                        java.lang.String regionID533,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] standardRouteSets534,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetSaveOptions options535)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStandardRouteSets
                * @param saveStandardRouteSets532
            
          */
        public void startsaveStandardRouteSets(

            java.lang.String regionID533,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] standardRouteSets534,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetSaveOptions options535,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ChangeUserPassword
                    * @param changeUserPassword538
                
         */

         
                     public void changeUserPassword(

                        java.lang.String userID539,java.lang.String oldPassword540,java.lang.String newPassword541)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ChangeUserPassword
                * @param changeUserPassword538
            
          */
        public void startchangeUserPassword(

            java.lang.String userID539,java.lang.String oldPassword540,java.lang.String newPassword541,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                    * @param deleteSapShipmentsBySessionIdentity543
                
         */

         
                     public boolean deleteSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid544)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                * @param deleteSapShipmentsBySessionIdentity543
            
          */
        public void startdeleteSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid544,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                    * @param retrievePlanningUserDefinedFieldInfo547
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedFieldInfo[] retrievePlanningUserDefinedFieldInfo(

                        java.lang.String regionId548)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                * @param retrievePlanningUserDefinedFieldInfo547
            
          */
        public void startretrievePlanningUserDefinedFieldInfo(

            java.lang.String regionId548,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                    * @param retrieveUserDefinedColumnsByCriteria551
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn[] retrieveUserDefinedColumnsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria552)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                * @param retrieveUserDefinedColumnsByCriteria551
            
          */
        public void startretrieveUserDefinedColumnsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria552,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CalculateTimeDist
                    * @param calculateTimeDist555
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TimeDistResult calculateTimeDist(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity556,int fromLatitude557,int fromLongitude558,int toLatitude559,int toLongitude560)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CalculateTimeDist
                * @param calculateTimeDist555
            
          */
        public void startcalculateTimeDist(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity556,int fromLatitude557,int fromLongitude558,int toLatitude559,int toLongitude560,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegionConfig
                    * @param saveRegionConfig563
                
         */

         
                     public void saveRegionConfig(

                        java.lang.String applicationID564,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity565,java.lang.String configGroupID566,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items567)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegionConfig
                * @param saveRegionConfig563
            
          */
        public void startsaveRegionConfig(

            java.lang.String applicationID564,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity565,java.lang.String configGroupID566,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items567,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                    * @param retrieveStopNotesByCriteriaEx569
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopNote[] retrieveStopNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria570,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options571)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                * @param retrieveStopNotesByCriteriaEx569
            
          */
        public void startretrieveStopNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria570,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options571,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteForDevice
                    * @param retrieveRouteForDevice574
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteForDevice(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity575,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options576,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity577)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteForDevice
                * @param retrieveRouteForDevice574
            
          */
        public void startretrieveRouteForDevice(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity575,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options576,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity577,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionETAs
                    * @param updateRoutePositionETAs580
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionETAs(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId581,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos582)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionETAs
                * @param updateRoutePositionETAs580
            
          */
        public void startupdateRoutePositionETAs(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId581,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos582,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SendTextMessageToDriver
                    * @param sendTextMessageToDriver585
                
         */

         
                     public void sendTextMessageToDriver(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity586,java.lang.String message587,java.lang.String fromUserID588)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SendTextMessageToDriver
                * @param sendTextMessageToDriver585
            
          */
        public void startsendTextMessageToDriver(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity586,java.lang.String message587,java.lang.String fromUserID588,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedTable
                    * @param deleteUserDefinedTable590
                
         */

         
                     public void deleteUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId591)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedTable
                * @param deleteUserDefinedTable590
            
          */
        public void startdeleteUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId591,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSkus
                    * @param saveSkus593
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] saveSkus(

                        com.freshdirect.routing.proxy.stub.transportation.Sku[] skus594)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSkus
                * @param saveSkus593
            
          */
        public void startsaveSkus(

            com.freshdirect.routing.proxy.stub.transportation.Sku[] skus594,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                    * @param retrieveSapShipmentsBySessionIdentity597
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SapShipment[] retrieveSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid598)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                * @param retrieveSapShipmentsBySessionIdentity597
            
          */
        public void startretrieveSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid598,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExtendOrderReservation
                    * @param schedulerExtendOrderReservation601
                
         */

         
                     public void schedulerExtendOrderReservation(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity602,java.lang.String orderNumberXML603,int extendMinutes604)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExtendOrderReservation
                * @param schedulerExtendOrderReservation601
            
          */
        public void startschedulerExtendOrderReservation(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity602,java.lang.String orderNumberXML603,int extendMinutes604,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionOptions
                    * @param retrieveRegionOptions606
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RegionOptions retrieveRegionOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity607)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionOptions
                * @param retrieveRegionOptions606
            
          */
        public void startretrieveRegionOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity607,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteSetByIdentity
                    * @param retrieveStandardRouteSetByIdentity610
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet retrieveStandardRouteSetByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity611,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options612)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteSetByIdentity
                * @param retrieveStandardRouteSetByIdentity610
            
          */
        public void startretrieveStandardRouteSetByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity611,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options612,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSignature
                    * @param saveStopSignature615
                
         */

         
                     public void saveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity616,javax.activation.DataHandler signatureData617)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSignature
                * @param saveStopSignature615
            
          */
        public void startsaveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity616,javax.activation.DataHandler signatureData617,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                    * @param retrieveUndeliverableStopCodesByCriteria619
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCode[] retrieveUndeliverableStopCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria620)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                * @param retrieveUndeliverableStopCodesByCriteria619
            
          */
        public void startretrieveUndeliverableStopCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria620,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteRoutingSession
                    * @param deleteRoutingSession623
                
         */

         
                     public void deleteRoutingSession(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity624)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteRoutingSession
                * @param deleteRoutingSession623
            
          */
        public void startdeleteRoutingSession(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity624,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                    * @param schedulerIsExcludingCutoffRoutes626
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult schedulerIsExcludingCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity627)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                * @param schedulerIsExcludingCutoffRoutes626
            
          */
        public void startschedulerIsExcludingCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity627,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                    * @param retrieveRoutingImportOrderByIdentity630
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder retrieveRoutingImportOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity631,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions632)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                * @param retrieveRoutingImportOrderByIdentity630
            
          */
        public void startretrieveRoutingImportOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity631,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions632,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkuByIdentity
                    * @param retrieveSkuByIdentity635
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku retrieveSkuByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity636)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkuByIdentity
                * @param retrieveSkuByIdentity635
            
          */
        public void startretrieveSkuByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity636,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                    * @param retrieveQuantityReasonCodesByCriteria639
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCode[] retrieveQuantityReasonCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria640)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                * @param retrieveQuantityReasonCodesByCriteria639
            
          */
        public void startretrieveQuantityReasonCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria640,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                    * @param retrieveLineItemNotesByCriteriaEx643
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LineItemNote[] retrieveLineItemNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria644,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options645)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                * @param retrieveLineItemNotesByCriteriaEx643
            
          */
        public void startretrieveLineItemNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria644,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options645,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteByIdentity
                    * @param retrieveStandardRouteByIdentity648
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRoute retrieveStandardRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity649,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options650)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteByIdentity
                * @param retrieveStandardRouteByIdentity648
            
          */
        public void startretrieveStandardRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity649,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options650,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                    * @param retrieveOrderNotesByCriteriaEx653
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.OrderNote[] retrieveOrderNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria654,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options655)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                * @param retrieveOrderNotesByCriteriaEx653
            
          */
        public void startretrieveOrderNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria654,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options655,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                    * @param retrieveEquipmentTypeByIdentity658
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType retrieveEquipmentTypeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity659,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options660)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                * @param retrieveEquipmentTypeByIdentity658
            
          */
        public void startretrieveEquipmentTypeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity659,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options660,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUnassigned
                    * @param deleteUnassigned663
                
         */

         
                     public void deleteUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop664)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUnassigned
                * @param deleteUnassigned663
            
          */
        public void startdeleteUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop664,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                    * @param retrieveRoutingSourcedOrdersByCriteria666
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] retrieveRoutingSourcedOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria667,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options668)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                * @param retrieveRoutingSourcedOrdersByCriteria666
            
          */
        public void startretrieveRoutingSourcedOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria667,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options668,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocationComments
                    * @param deleteLocationComments671
                
         */

         
                     public void deleteLocationComments(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCommentIdentity[] comments672)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocationComments
                * @param deleteLocationComments671
            
          */
        public void startdeleteLocationComments(

            com.freshdirect.routing.proxy.stub.transportation.LocationCommentIdentity[] comments672,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserConfig
                    * @param saveUserConfig674
                
         */

         
                     public void saveUserConfig(

                        java.lang.String applicationID675,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity676,java.lang.String configGroupID677,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items678)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserConfig
                * @param saveUserConfig674
            
          */
        public void startsaveUserConfig(

            java.lang.String applicationID675,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity676,java.lang.String configGroupID677,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items678,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeByIdentity
                    * @param retrieveEmployeeByIdentity680
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity681)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeByIdentity
                * @param retrieveEmployeeByIdentity680
            
          */
        public void startretrieveEmployeeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity681,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUpdateOrder
                    * @param schedulerUpdateOrder684
                
         */

         
                     public boolean schedulerUpdateOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity685,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity686,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options687)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUpdateOrder
                * @param schedulerUpdateOrder684
            
          */
        public void startschedulerUpdateOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity685,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity686,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options687,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SurveyResponse
                    * @param surveyResponse690
                
         */

         
                     public void surveyResponse(

                        com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info691)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SurveyResponse
                * @param surveyResponse690
            
          */
        public void startsurveyResponse(

            com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info691,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RedeliverStop
                    * @param redeliverStop693
                
         */

         
                     public void redeliverStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info694)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RedeliverStop
                * @param redeliverStop693
            
          */
        public void startredeliverStop(

            com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info694,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRoutesByCriteria
                    * @param retrieveStandardRoutesByCriteria696
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] retrieveStandardRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteCriteria criteria697,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options698)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRoutesByCriteria
                * @param retrieveStandardRoutesByCriteria696
            
          */
        public void startretrieveStandardRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteCriteria criteria697,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options698,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionsByCriteria
                    * @param retrieveRegionsByCriteria701
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region[] retrieveRegionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria702)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionsByCriteria
                * @param retrieveRegionsByCriteria701
            
          */
        public void startretrieveRegionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria702,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveStandardRouteSet
                    * @param removeStandardRouteSet705
                
         */

         
                     public void removeStandardRouteSet(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity706,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRemoveOptions options707)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveStandardRouteSet
                * @param removeStandardRouteSet705
            
          */
        public void startremoveStandardRouteSet(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity706,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRemoveOptions options707,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyResults
                    * @param retrieveRouteSurveyResults709
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity710,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt711)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyResults
                * @param retrieveRouteSurveyResults709
            
          */
        public void startretrieveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity710,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt711,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveActiveAlertRecipientTypes
                    * @param retrieveActiveAlertRecipientTypes714
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipientType[] retrieveActiveAlertRecipientTypes(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveActiveAlertRecipientTypes
                * @param retrieveActiveAlertRecipientTypes714
            
          */
        public void startretrieveActiveAlertRecipientTypes(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByCriteria
                    * @param retrieveEquipmentByCriteria717
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment[] retrieveEquipmentByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria718,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options719)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByCriteria
                * @param retrieveEquipmentByCriteria717
            
          */
        public void startretrieveEquipmentByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria718,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options719,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartOrigin
                    * @param departOrigin722
                
         */

         
                     public void departOrigin(

                        com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info723)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartOrigin
                * @param departOrigin722
            
          */
        public void startdepartOrigin(

            com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info723,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateRoutingSession
                    * @param createRoutingSession725
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity createRoutingSession(

                        java.lang.String regionId726,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties727)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateRoutingSession
                * @param createRoutingSession725
            
          */
        public void startcreateRoutingSession(

            java.lang.String regionId726,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties727,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                    * @param retrieveDefaultRoutingSessionProperties730
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties retrieveDefaultRoutingSessionProperties(

                        java.lang.String regionId731,java.util.Date sessionDate732)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                * @param retrieveDefaultRoutingSessionProperties730
            
          */
        public void startretrieveDefaultRoutingSessionProperties(

            java.lang.String regionId731,java.util.Date sessionDate732,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStationaryRoutePosition
                    * @param updateStationaryRoutePosition735
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateStationaryRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.StationaryRoutePositionInfo info736)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStationaryRoutePosition
                * @param updateStationaryRoutePosition735
            
          */
        public void startupdateStationaryRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.StationaryRoutePositionInfo info736,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateBatchRoutePosition
                    * @param updateBatchRoutePosition739
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateBatchRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.BatchRoutePositionInfo info740)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateBatchRoutePosition
                * @param updateBatchRoutePosition739
            
          */
        public void startupdateBatchRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.BatchRoutePositionInfo info740,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoutingStop
                    * @param removeRoutingStop743
                
         */

         
                     public void removeRoutingStop(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity744,com.freshdirect.routing.proxy.stub.transportation.RemoveRoutingStopOptions options745)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoutingStop
                * @param removeRoutingStop743
            
          */
        public void startremoveRoutingStop(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity744,com.freshdirect.routing.proxy.stub.transportation.RemoveRoutingStopOptions options745,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                    * @param retrieveDutyPeriodByIdentity747
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod retrieveDutyPeriodByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity748,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options749)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                * @param retrieveDutyPeriodByIdentity747
            
          */
        public void startretrieveDutyPeriodByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity748,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options749,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopsByCriteria
                    * @param retrieveStopsByCriteria752
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria753,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options754)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopsByCriteria
                * @param retrieveStopsByCriteria752
            
          */
        public void startretrieveStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria753,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options754,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                    * @param retrieveDepotSkuAvailabilityByIdentity757
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability retrieveDepotSkuAvailabilityByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity758)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                * @param retrieveDepotSkuAvailabilityByIdentity757
            
          */
        public void startretrieveDepotSkuAvailabilityByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity758,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingRouteNetMatrix
                    * @param buildRoutingRouteNetMatrix761
                
         */

         
                     public void buildRoutingRouteNetMatrix(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity762)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingRouteNetMatrix
                * @param buildRoutingRouteNetMatrix761
            
          */
        public void startbuildRoutingRouteNetMatrix(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity762,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                    * @param retrieveRoutingSessionsByCriteria764
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] retrieveRoutingSessionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria765,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options766)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                * @param retrieveRoutingSessionsByCriteria764
            
          */
        public void startretrieveRoutingSessionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria765,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options766,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReserveOrder
                    * @param schedulerReserveOrder769
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ReserveResult schedulerReserveOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity770,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder771,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow772,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options773)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReserveOrder
                * @param schedulerReserveOrder769
            
          */
        public void startschedulerReserveOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity770,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder771,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow772,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options773,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteSurveyResults
                    * @param saveRouteSurveyResults776
                
         */

         
                     public void saveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity777,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt778,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults779)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteSurveyResults
                * @param saveRouteSurveyResults776
            
          */
        public void startsaveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity777,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt778,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults779,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingRoute
                    * @param saveRoutingRoute781
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SaveRoutingRejection[] saveRoutingRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRoute route782,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingRouteOptions options783)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingRoute
                * @param saveRoutingRoute781
            
          */
        public void startsaveRoutingRoute(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRoute route782,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingRouteOptions options783,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerAnalyzeOrder
                    * @param schedulerAnalyzeOrder786
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] schedulerAnalyzeOrder(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order787,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options788)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerAnalyzeOrder
                * @param schedulerAnalyzeOrder786
            
          */
        public void startschedulerAnalyzeOrder(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order787,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options788,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveActiveAlertRecipients
                    * @param saveActiveAlertRecipients791
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] saveActiveAlertRecipients(

                        com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] activeAlertRecipients792)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveActiveAlertRecipients
                * @param saveActiveAlertRecipients791
            
          */
        public void startsaveActiveAlertRecipients(

            com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] activeAlertRecipients792,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamps
                    * @param convertTimestamps795
                
         */

         
                     public java.util.Calendar[] convertTimestamps(

                        java.util.Calendar[] sourceTimestamps796,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions797,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions798)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamps
                * @param convertTimestamps795
            
          */
        public void startconvertTimestamps(

            java.util.Calendar[] sourceTimestamps796,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions797,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions798,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByCriteria
                    * @param retrieveNotificationsByCriteria801
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria802,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions803,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options804)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByCriteria
                * @param retrieveNotificationsByCriteria801
            
          */
        public void startretrieveNotificationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria802,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions803,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options804,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                    * @param retrieveRoutingUnassignsByCriteria807
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria808,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options809)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                * @param retrieveRoutingUnassignsByCriteria807
            
          */
        public void startretrieveRoutingUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria808,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options809,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__PlaceUnassigned
                    * @param placeUnassigned812
                
         */

         
                     public void placeUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop813,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity814,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition815,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime816,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions817)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__PlaceUnassigned
                * @param placeUnassigned812
            
          */
        public void startplaceUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop813,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity814,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition815,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime816,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions817,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateTelematicsCachePositions
                    * @param updateTelematicsCachePositions819
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions820)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateTelematicsCachePositions
                * @param updateTelematicsCachePositions819
            
          */
        public void startupdateTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions820,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                    * @param retrievePlanningLocationExtensionsByCriteria823
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] retrievePlanningLocationExtensionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria824,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options825)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                * @param retrievePlanningLocationExtensionsByCriteria823
            
          */
        public void startretrievePlanningLocationExtensionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria824,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options825,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BulkArriveDepartStop
                    * @param bulkArriveDepartStop828
                
         */

         
                     public void bulkArriveDepartStop(

                        com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos829)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BulkArriveDepartStop
                * @param bulkArriveDepartStop828
            
          */
        public void startbulkArriveDepartStop(

            com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos829,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DisableWirelessCommunication
                    * @param disableWirelessCommunication831
                
         */

         
                     public void disableWirelessCommunication(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity info832)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DisableWirelessCommunication
                * @param disableWirelessCommunication831
            
          */
        public void startdisableWirelessCommunication(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity info832,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingStop
                    * @param saveRoutingStop834
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SaveRoutingRejection[] saveRoutingStop(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStop stop835,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingStopOptions options836)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingStop
                * @param saveRoutingStop834
            
          */
        public void startsaveRoutingStop(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStop stop835,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingStopOptions options836,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SavePlanningLocationExtensions
                    * @param savePlanningLocationExtensions839
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] savePlanningLocationExtensions(

                        java.lang.String regionId840,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions841)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SavePlanningLocationExtensions
                * @param savePlanningLocationExtensions839
            
          */
        public void startsavePlanningLocationExtensions(

            java.lang.String regionId840,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions841,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CleanupTelematicsCachePositions
                    * @param cleanupTelematicsCachePositions844
                
         */

         
                     public void cleanupTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria845)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CleanupTelematicsCachePositions
                * @param cleanupTelematicsCachePositions844
            
          */
        public void startcleanupTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria845,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStandardRoutes
                    * @param saveStandardRoutes847
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteRejection[] saveStandardRoutes(

                        java.lang.String regionID848,com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] standardRoutes849,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSaveOptions options850)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStandardRoutes
                * @param saveStandardRoutes847
            
          */
        public void startsaveStandardRoutes(

            java.lang.String regionID848,com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] standardRoutes849,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSaveOptions options850,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutesByCriteria
                    * @param retrieveRoutesByCriteria853
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route[] retrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria854,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options855)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutesByCriteria
                * @param retrieveRoutesByCriteria853
            
          */
        public void startretrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria854,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options855,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                    * @param schedulerRetrieveRouteByIdentity858
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute schedulerRetrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity859,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options860)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                * @param schedulerRetrieveRouteByIdentity858
            
          */
        public void startschedulerRetrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity859,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options860,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRouteETAs
                    * @param updateRouteETAs863
                
         */

         
                     public void updateRouteETAs(

                        com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info864)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRouteETAs
                * @param updateRouteETAs863
            
          */
        public void startupdateRouteETAs(

            com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info864,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopEx
                    * @param saveStopEx866
                
         */

         
                     public void saveStopEx(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop867,com.freshdirect.routing.proxy.stub.transportation.SaveStopExOptions options868)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopEx
                * @param saveStopEx866
            
          */
        public void startsaveStopEx(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop867,com.freshdirect.routing.proxy.stub.transportation.SaveStopExOptions options868,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ReturnFault
                    * @param returnFault870
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Fault returnFault(

                        int requestedFaultCode871)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ReturnFault
                * @param returnFault870
            
          */
        public void startreturnFault(

            int requestedFaultCode871,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeRouteStatsByCriteria
                    * @param retrieveEmployeeRouteStatsByCriteria874
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStats[] retrieveEmployeeRouteStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStatsCriteria criteria875)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeRouteStatsByCriteria
                * @param retrieveEmployeeRouteStatsByCriteria874
            
          */
        public void startretrieveEmployeeRouteStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStatsCriteria criteria875,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveyDetails
                    * @param retrieveSurveyDetails878
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyDetails[] retrieveSurveyDetails(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity879)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveyDetails
                * @param retrieveSurveyDetails878
            
          */
        public void startretrieveSurveyDetails(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity879,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AddRICUser
                    * @param addRICUser882
                
         */

         
                     public void addRICUser(

                        com.freshdirect.routing.proxy.stub.transportation.User user883)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AddRICUser
                * @param addRICUser882
            
          */
        public void startaddRICUser(

            com.freshdirect.routing.proxy.stub.transportation.User user883,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveProductsPurchased
                    * @param retrieveProductsPurchased885
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased retrieveProductsPurchased(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity886)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveProductsPurchased
                * @param retrieveProductsPurchased885
            
          */
        public void startretrieveProductsPurchased(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity886,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePermissionsForUser
                    * @param retrievePermissionsForUser889
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserPermissions retrievePermissionsForUser(

                        java.lang.String userID890,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity891)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePermissionsForUser
                * @param retrievePermissionsForUser889
            
          */
        public void startretrievePermissionsForUser(

            java.lang.String userID890,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity891,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocations
                    * @param deleteLocations894
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] deleteLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations895)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocations
                * @param deleteLocations894
            
          */
        public void startdeleteLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations895,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedEquipment
                    * @param retrieveAssignedEquipment898
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] retrieveAssignedEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity899)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedEquipment
                * @param retrieveAssignedEquipment898
            
          */
        public void startretrieveAssignedEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity899,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteReportedDistances
                    * @param saveRouteReportedDistances902
                
         */

         
                     public void saveRouteReportedDistances(

                        com.freshdirect.routing.proxy.stub.transportation.RouteReportedDistance[] reportedDistances903)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteReportedDistances
                * @param saveRouteReportedDistances902
            
          */
        public void startsaveRouteReportedDistances(

            com.freshdirect.routing.proxy.stub.transportation.RouteReportedDistance[] reportedDistances903,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopByIdentity
                    * @param retrieveRoutingStopByIdentity905
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop retrieveRoutingStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity906,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options907)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopByIdentity
                * @param retrieveRoutingStopByIdentity905
            
          */
        public void startretrieveRoutingStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity906,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options907,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation910
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation910
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirections
                    * @param buildDispatchDriverDirections913
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity914)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirections
                * @param buildDispatchDriverDirections913
            
          */
        public void startbuildDispatchDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity914,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                    * @param retrieveTelematicsCachePositionsByCriteria917
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] retrieveTelematicsCachePositionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria918)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                * @param retrieveTelematicsCachePositionsByCriteria917
            
          */
        public void startretrieveTelematicsCachePositionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria918,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStopSignature
                    * @param updateStopSignature921
                
         */

         
                     public void updateStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info922)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStopSignature
                * @param updateStopSignature921
            
          */
        public void startupdateStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info922,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                    * @param schedulerSaveDeliveryWaveInstance924
                
         */

         
                     public java.lang.String[] schedulerSaveDeliveryWaveInstance(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity925,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity926,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes927,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options928)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                * @param schedulerSaveDeliveryWaveInstance924
            
          */
        public void startschedulerSaveDeliveryWaveInstance(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity925,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity926,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes927,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options928,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AllowAdditionOfRICUsers
                    * @param allowAdditionOfRICUsers931
                
         */

         
                     public boolean allowAdditionOfRICUsers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AllowAdditionOfRICUsers
                * @param allowAdditionOfRICUsers931
            
          */
        public void startallowAdditionOfRICUsers(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGPSProviderOptions
                    * @param retrieveGPSProviderOptions934
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.GPSProviderOptions retrieveGPSProviderOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity935)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGPSProviderOptions
                * @param retrieveGPSProviderOptions934
            
          */
        public void startretrieveGPSProviderOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity935,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                    * @param retrieveRoutingLocationsWithOrders938
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrders(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity939)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                * @param retrieveRoutingLocationsWithOrders938
            
          */
        public void startretrieveRoutingLocationsWithOrders(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity939,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteria
                    * @param retrieveRouteNotesByCriteria942
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria943)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteria
                * @param retrieveRouteNotesByCriteria942
            
          */
        public void startretrieveRouteNotesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria943,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveReport
                    * @param saveReport946
                
         */

         
                     public void saveReport(

                        com.freshdirect.routing.proxy.stub.transportation.Report report947)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveReport
                * @param saveReport946
            
          */
        public void startsaveReport(

            com.freshdirect.routing.proxy.stub.transportation.Report report947,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveys
                    * @param retrieveSurveys949
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Survey[] retrieveSurveys(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity950)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveys
                * @param retrieveSurveys949
            
          */
        public void startretrieveSurveys(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity950,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                    * @param retrieveNotificationsByRecipientIdentity953
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByRecipientIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity954,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions955,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options956)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                * @param retrieveNotificationsByRecipientIdentity953
            
          */
        public void startretrieveNotificationsByRecipientIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity954,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions955,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options956,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBalanceRoutes
                    * @param schedulerBalanceRoutes959
                
         */

         
                     public void schedulerBalanceRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity960,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options961)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBalanceRoutes
                * @param schedulerBalanceRoutes959
            
          */
        public void startschedulerBalanceRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity960,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options961,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                    * @param retrieveUserDefinedDataByCriteria963
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] retrieveUserDefinedDataByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity964,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria965,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions966)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                * @param retrieveUserDefinedDataByCriteria963
            
          */
        public void startretrieveUserDefinedDataByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity964,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria965,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions966,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkusByCriteria
                    * @param retrieveSkusByCriteria969
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] retrieveSkusByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria970)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkusByCriteria
                * @param retrieveSkusByCriteria969
            
          */
        public void startretrieveSkusByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria970,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                    * @param retrieveCannedTextMessagesByCriteria973
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] retrieveCannedTextMessagesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria974)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                * @param retrieveCannedTextMessagesByCriteria973
            
          */
        public void startretrieveCannedTextMessagesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria974,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                    * @param retrieveUserDefinedDataByIdentity977
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData retrieveUserDefinedDataByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity978,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity979,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions980)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                * @param retrieveUserDefinedDataByIdentity977
            
          */
        public void startretrieveUserDefinedDataByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity978,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity979,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions980,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                    * @param retrieveEquipmentTypeByCriteria983
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] retrieveEquipmentTypeByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria984,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options985)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                * @param retrieveEquipmentTypeByCriteria983
            
          */
        public void startretrieveEquipmentTypeByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria984,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options985,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                    * @param retrieveRoutingOrderByIdentity988
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder retrieveRoutingOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity989,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options990)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                * @param retrieveRoutingOrderByIdentity988
            
          */
        public void startretrieveRoutingOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity989,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options990,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__TextMessage
                    * @param textMessage993
                
         */

         
                     public void textMessage(

                        com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info994)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__TextMessage
                * @param textMessage993
            
          */
        public void starttextMessage(

            com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info994,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CompleteRoute
                    * @param completeRoute996
                
         */

         
                     public void completeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info997)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CompleteRoute
                * @param completeRoute996
            
          */
        public void startcompleteRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info997,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoutingRoute
                    * @param removeRoutingRoute999
                
         */

         
                     public void removeRoutingRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity0,com.freshdirect.routing.proxy.stub.transportation.RemoveRoutingRouteOptions options1)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoutingRoute
                * @param removeRoutingRoute999
            
          */
        public void startremoveRoutingRoute(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity0,com.freshdirect.routing.proxy.stub.transportation.RemoveRoutingRouteOptions options1,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                    * @param retrieveLocationsByCriteriaEx3
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria4,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options5)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                * @param retrieveLocationsByCriteriaEx3
            
          */
        public void startretrieveLocationsByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria4,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options5,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveStop
                    * @param arriveStop8
                
         */

         
                     public void arriveStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info9)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveStop
                * @param arriveStop8
            
          */
        public void startarriveStop(

            com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info9,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedTable
                    * @param saveUserDefinedTable11
                
         */

         
                     public void saveUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table12)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedTable
                * @param saveUserDefinedTable11
            
          */
        public void startsaveUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table12,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteSetsByCriteria
                    * @param retrieveStandardRouteSetsByCriteria14
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] retrieveStandardRouteSetsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetCriteria criteria15,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options16)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteSetsByCriteria
                * @param retrieveStandardRouteSetsByCriteria14
            
          */
        public void startretrieveStandardRouteSetsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetCriteria criteria15,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options16,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoute
                    * @param saveRoute19
                
         */

         
                     public void saveRoute(

                        com.freshdirect.routing.proxy.stub.transportation.Route route20,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions21,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions22)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoute
                * @param saveRoute19
            
          */
        public void startsaveRoute(

            com.freshdirect.routing.proxy.stub.transportation.Route route20,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions21,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions22,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveDestination
                    * @param arriveDestination24
                
         */

         
                     public void arriveDestination(

                        com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info25)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveDestination
                * @param arriveDestination24
            
          */
        public void startarriveDestination(

            com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info25,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByIdentity
                    * @param retrieveEquipmentByIdentity27
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment retrieveEquipmentByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity28,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options29)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByIdentity
                * @param retrieveEquipmentByIdentity27
            
          */
        public void startretrieveEquipmentByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity28,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options29,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationsEx
                    * @param saveLocationsEx32
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocationsEx(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations33,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options34)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationsEx
                * @param saveLocationsEx32
            
          */
        public void startsaveLocationsEx(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations33,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options34,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteByIdentity
                    * @param retrieveRouteByIdentity37
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity38,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options39)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteByIdentity
                * @param retrieveRouteByIdentity37
            
          */
        public void startretrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity38,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options39,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserConfig
                    * @param retrieveUserConfig42
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveUserConfig(

                        java.lang.String applicationID43,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity44,java.lang.String configGroupID45)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserConfig
                * @param retrieveUserConfig42
            
          */
        public void startretrieveUserConfig(

            java.lang.String applicationID43,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity44,java.lang.String configGroupID45,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionEx
                    * @param updateRoutePositionEx48
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos49)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionEx
                * @param updateRoutePositionEx48
            
          */
        public void startupdateRoutePositionEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos49,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentity
                    * @param retrieveLocationByIdentity52
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity53)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentity
                * @param retrieveLocationByIdentity52
            
          */
        public void startretrieveLocationByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity53,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserByUserID
                    * @param retrieveUserByUserID56
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.User retrieveUserByUserID(

                        java.lang.String userID57)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserByUserID
                * @param retrieveUserByUserID56
            
          */
        public void startretrieveUserByUserID(

            java.lang.String userID57,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignEquipment
                    * @param assignEquipment60
                
         */

         
                     public void assignEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity61,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment62)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignEquipment
                * @param assignEquipment60
            
          */
        public void startassignEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity61,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment62,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveGlobalConfig
                    * @param saveGlobalConfig64
                
         */

         
                     public void saveGlobalConfig(

                        java.lang.String applicationID65,java.lang.String configGroupID66,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items67)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveGlobalConfig
                * @param saveGlobalConfig64
            
          */
        public void startsaveGlobalConfig(

            java.lang.String applicationID65,java.lang.String configGroupID66,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items67,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEquipment
                    * @param saveEquipment69
                
         */

         
                     public void saveEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.Equipment equipment70,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options71)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEquipment
                * @param saveEquipment69
            
          */
        public void startsaveEquipment(

            com.freshdirect.routing.proxy.stub.transportation.Equipment equipment70,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options71,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteExceptionsByCriteria
                    * @param retrieveRouteExceptionsByCriteria73
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteException[] retrieveRouteExceptionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteExceptionCriteria criteria74,com.freshdirect.routing.proxy.stub.transportation.RouteExceptionRetrieveOptions options75)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteExceptionsByCriteria
                * @param retrieveRouteExceptionsByCriteria73
            
          */
        public void startretrieveRouteExceptionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteExceptionCriteria criteria74,com.freshdirect.routing.proxy.stub.transportation.RouteExceptionRetrieveOptions options75,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerConfirmOrder
                    * @param schedulerConfirmOrder78
                
         */

         
                     public void schedulerConfirmOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity79,java.lang.String orderNumberXML80)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerConfirmOrder
                * @param schedulerConfirmOrder78
            
          */
        public void startschedulerConfirmOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity79,java.lang.String orderNumberXML80,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsOptions
                    * @param retrieveTelematicsOptions82
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsOptions retrieveTelematicsOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity83)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsOptions
                * @param retrieveTelematicsOptions82
            
          */
        public void startretrieveTelematicsOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity83,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                    * @param retrieveUserDefinedTableByIdentity86
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable retrieveUserDefinedTableByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity87)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                * @param retrieveUserDefinedTableByIdentity86
            
          */
        public void startretrieveUserDefinedTableByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity87,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteria
                    * @param retrieveLocationsByCriteria90
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria91)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteria
                * @param retrieveLocationsByCriteria90
            
          */
        public void startretrieveLocationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria91,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreatePlanningSession
                    * @param createPlanningSession94
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity createPlanningSession(

                        java.lang.String regionId95,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties96)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreatePlanningSession
                * @param createPlanningSession94
            
          */
        public void startcreatePlanningSession(

            java.lang.String regionId95,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties96,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentityEx
                    * @param retrieveLocationByIdentityEx99
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentityEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity100,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options101)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentityEx
                * @param retrieveLocationByIdentityEx99
            
          */
        public void startretrieveLocationByIdentityEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity100,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options101,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                    * @param retrievePlanningTerritoriesByCriteria104
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningTerritory[] retrievePlanningTerritoriesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria105,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options106)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                * @param retrievePlanningTerritoriesByCriteria104
            
          */
        public void startretrievePlanningTerritoriesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria105,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options106,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamp
                    * @param convertTimestamp109
                
         */

         
                     public java.util.Calendar convertTimestamp(

                        java.util.Calendar sourceTimestamp110,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions111,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions112)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamp
                * @param convertTimestamp109
            
          */
        public void startconvertTimestamp(

            java.util.Calendar sourceTimestamp110,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions111,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions112,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                    * @param schedulerSendRoutesToRoadnetEx115
                
         */

         
                     public void schedulerSendRoutesToRoadnetEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity116,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options117)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                * @param schedulerSendRoutesToRoadnetEx115
            
          */
        public void startschedulerSendRoutesToRoadnetEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity116,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options117,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                    * @param schedulerSendRoutesToRoadnet119
                
         */

         
                     public void schedulerSendRoutesToRoadnet(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity120)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                * @param schedulerSendRoutesToRoadnet119
            
          */
        public void startschedulerSendRoutesToRoadnet(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity120,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportsByCriteria
                    * @param retrieveReportsByCriteria122
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report[] retrieveReportsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria123)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportsByCriteria
                * @param retrieveReportsByCriteria122
            
          */
        public void startretrieveReportsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria123,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                    * @param retrieveRouteNotesByCriteriaEx126
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria127,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options128)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                * @param retrieveRouteNotesByCriteriaEx126
            
          */
        public void startretrieveRouteNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria127,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options128,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedData
                    * @param saveUserDefinedData131
                
         */

         
                     public void saveUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity132,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input133,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions134)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedData
                * @param saveUserDefinedData131
            
          */
        public void startsaveUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity132,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input133,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions134,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUnassignsByCriteria
                    * @param retrieveUnassignsByCriteria136
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria137,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options138)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUnassignsByCriteria
                * @param retrieveUnassignsByCriteria136
            
          */
        public void startretrieveUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria137,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options138,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutes
                    * @param schedulerRebuildRoutes141
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity142)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutes
                * @param schedulerRebuildRoutes141
            
          */
        public void startschedulerRebuildRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity142,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeletePlanningLocationExtensions
                    * @param deletePlanningLocationExtensions145
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] deletePlanningLocationExtensions(

                        java.lang.String regionId146,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions147)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeletePlanningLocationExtensions
                * @param deletePlanningLocationExtensions145
            
          */
        public void startdeletePlanningLocationExtensions(

            java.lang.String regionId146,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions147,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DescribeAuthenticaitonPolicy
                    * @param describeAuthenticaitonPolicy150
                
         */

         
                     public java.lang.String describeAuthenticaitonPolicy(

                        java.lang.String localeId151)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DescribeAuthenticaitonPolicy
                * @param describeAuthenticaitonPolicy150
            
          */
        public void startdescribeAuthenticaitonPolicy(

            java.lang.String localeId151,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop154
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop154
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCancelOrder
                    * @param schedulerCancelOrder157
                
         */

         
                     public void schedulerCancelOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity158,java.lang.String orderNumberXML159)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCancelOrder
                * @param schedulerCancelOrder157
            
          */
        public void startschedulerCancelOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity158,java.lang.String orderNumberXML159,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePosition
                    * @param updateRoutePosition161
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info162)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePosition
                * @param updateRoutePosition161
            
          */
        public void startupdateRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info162,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWindow
                    * @param schedulerSaveDeliveryWindow165
                
         */

         
                     public boolean schedulerSaveDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity166,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options167)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWindow
                * @param schedulerSaveDeliveryWindow165
            
          */
        public void startschedulerSaveDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity166,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options167,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateAdminRouteEx
                    * @param createAdminRouteEx170
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity createAdminRouteEx(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity171,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity172,com.freshdirect.routing.proxy.stub.transportation.CreateAdminRouteOptions options173)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateAdminRouteEx
                * @param createAdminRouteEx170
            
          */
        public void startcreateAdminRouteEx(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity171,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity172,com.freshdirect.routing.proxy.stub.transportation.CreateAdminRouteOptions options173,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateAdminRoute
                    * @param createAdminRoute176
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity createAdminRoute(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity177,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity178)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateAdminRoute
                * @param createAdminRoute176
            
          */
        public void startcreateAdminRoute(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity177,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity178,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                    * @param retrieveRoutingImportOrdersByCriteria181
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] retrieveRoutingImportOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria182,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions183)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                * @param retrieveRoutingImportOrdersByCriteria181
            
          */
        public void startretrieveRoutingImportOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria182,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions183,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SuggestRoute
                    * @param suggestRoute186
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] suggestRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop187,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options188)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SuggestRoute
                * @param suggestRoute186
            
          */
        public void startsuggestRoute(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop187,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options188,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__StartRoute
                    * @param startRoute191
                
         */

         
                     public void startRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info192)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__StartRoute
                * @param startRoute191
            
          */
        public void startstartRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info192,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAccountTypesByCriteria
                    * @param retrieveAccountTypesByCriteria194
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AccountType[] retrieveAccountTypesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.AccountTypeCriteria criteria195)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAccountTypesByCriteria
                * @param retrieveAccountTypesByCriteria194
            
          */
        public void startretrieveAccountTypesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.AccountTypeCriteria criteria195,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                    * @param schedulerExcludeCutoffRoutes198
                
         */

         
                     public void schedulerExcludeCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity199,boolean excludeXML200)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                * @param schedulerExcludeCutoffRoutes198
            
          */
        public void startschedulerExcludeCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity199,boolean excludeXML200,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUnload
                    * @param schedulerUnload202
                
         */

         
                     public void schedulerUnload(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity203)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUnload
                * @param schedulerUnload202
            
          */
        public void startschedulerUnload(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity203,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnassignStop
                    * @param unassignStop205
                
         */

         
                     public void unassignStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop206)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnassignStop
                * @param unassignStop205
            
          */
        public void startunassignStop(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop206,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                    * @param retrieveRouteDailyStatsByCriteria208
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria209,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options210)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                * @param retrieveRouteDailyStatsByCriteria208
            
          */
        public void startretrieveRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria209,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options210,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                    * @param retrievePlanningSessionPropertiesByIdentity213
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession retrievePlanningSessionPropertiesByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity214)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                * @param retrievePlanningSessionPropertiesByIdentity213
            
          */
        public void startretrievePlanningSessionPropertiesByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity214,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingDriverDirections
                    * @param buildRoutingDriverDirections217
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildRoutingDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity218)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingDriverDirections
                * @param buildRoutingDriverDirections217
            
          */
        public void startbuildRoutingDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity218,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocations
                    * @param saveLocations221
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations222)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocations
                * @param saveLocations221
            
          */
        public void startsaveLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations222,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSapShipments
                    * @param saveSapShipments225
                
         */

         
                     public boolean saveSapShipments(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid226,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments227)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSapShipments
                * @param saveSapShipments225
            
          */
        public void startsaveSapShipments(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid226,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments227,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionConfig
                    * @param retrieveRegionConfig230
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveRegionConfig(

                        java.lang.String applicationID231,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity232,java.lang.String configGroupID233)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionConfig
                * @param retrieveRegionConfig230
            
          */
        public void startretrieveRegionConfig(

            java.lang.String applicationID231,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity232,java.lang.String configGroupID233,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                    * @param retrieveRoutingLocationsWithOrdersEx236
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity237,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options238)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                * @param retrieveRoutingLocationsWithOrdersEx236
            
          */
        public void startretrieveRoutingLocationsWithOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity237,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options238,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyQuestions
                    * @param retrieveRouteSurveyQuestions241
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveRouteSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity242,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt243)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyQuestions
                * @param retrieveRouteSurveyQuestions241
            
          */
        public void startretrieveRouteSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity242,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt243,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegion
                    * @param saveRegion246
                
         */

         
                     public void saveRegion(

                        com.freshdirect.routing.proxy.stub.transportation.Region region247)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegion
                * @param saveRegion246
            
          */
        public void startsaveRegion(

            com.freshdirect.routing.proxy.stub.transportation.Region region247,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                    * @param schedulerRetrieveRoutesByCriteria249
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute[] schedulerRetrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria250,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options251)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                * @param schedulerRetrieveRoutesByCriteria249
            
          */
        public void startschedulerRetrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria250,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options251,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipment
                    * @param deleteSapShipment254
                
         */

         
                     public boolean deleteSapShipment(

                        com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid255)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipment
                * @param deleteSapShipment254
            
          */
        public void startdeleteSapShipment(

            com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid255,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUnassigned
                    * @param saveUnassigned258
                
         */

         
                     public void saveUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop259,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options260)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUnassigned
                * @param saveUnassigned258
            
          */
        public void startsaveUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop259,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options260,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrders
                    * @param saveRoutingImportOrders262
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrders(

                        java.lang.String regionId263,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders264,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions265)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrders
                * @param saveRoutingImportOrders262
            
          */
        public void startsaveRoutingImportOrders(

            java.lang.String regionId263,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders264,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions265,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                    * @param retrievePositionHistoryBlocksByCriteria268
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryBlocksByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria269)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                * @param retrievePositionHistoryBlocksByCriteria268
            
          */
        public void startretrievePositionHistoryBlocksByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria269,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SequenceStop
                    * @param sequenceStop272
                
         */

         
                     public void sequenceStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info273)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SequenceStop
                * @param sequenceStop272
            
          */
        public void startsequenceStop(

            com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info273,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportByIdentity
                    * @param retrieveReportByIdentity275
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report retrieveReportByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity276)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportByIdentity
                * @param retrieveReportByIdentity275
            
          */
        public void startretrieveReportByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity276,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedData
                    * @param deleteUserDefinedData279
                
         */

         
                     public void deleteUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity280,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input281)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedData
                * @param deleteUserDefinedData279
            
          */
        public void startdeleteUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity280,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input281,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationServiceStatsByCriteria
                    * @param retrieveLocationServiceStatsByCriteria283
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LocationServiceStats[] retrieveLocationServiceStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationServiceStatsCriteria criteria284)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationServiceStatsByCriteria
                * @param retrieveLocationServiceStatsByCriteria283
            
          */
        public void startretrieveLocationServiceStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationServiceStatsCriteria criteria284,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                    * @param retrieveUserDefinedTablesByCriteria287
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable[] retrieveUserDefinedTablesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria288)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                * @param retrieveUserDefinedTablesByCriteria287
            
          */
        public void startretrieveUserDefinedTablesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria288,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBulkReserveOrders
                    * @param schedulerBulkReserveOrders291
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerBulkReserveOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity292,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders293,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options294)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBulkReserveOrders
                * @param schedulerBulkReserveOrders291
            
          */
        public void startschedulerBulkReserveOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity292,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders293,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options294,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                    * @param retrieveDepotSkusAvailabilitiesByCriteria297
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] retrieveDepotSkusAvailabilitiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria298)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                * @param retrieveDepotSkusAvailabilitiesByCriteria297
            
          */
        public void startretrieveDepotSkusAvailabilitiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria298,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                    * @param retrieveRoutingStopsByCriteria301
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria302,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options303)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                * @param retrieveRoutingStopsByCriteria301
            
          */
        public void startretrieveRoutingStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria302,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options303,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                    * @param retrieveRoutingRoutesByCriteria306
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] retrieveRoutingRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria307,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options308)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                * @param retrieveRoutingRoutesByCriteria306
            
          */
        public void startretrieveRoutingRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria307,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options308,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnsubscribeFromNotifications
                    * @param unsubscribeFromNotifications311
                
         */

         
                     public void unsubscribeFromNotifications(

                        java.lang.String unsubscribeToken312,java.lang.String recipientEmail313)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnsubscribeFromNotifications
                * @param unsubscribeFromNotifications311
            
          */
        public void startunsubscribeFromNotifications(

            java.lang.String unsubscribeToken312,java.lang.String recipientEmail313,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRICRegionsByUser
                    * @param retrieveRICRegionsByUser315
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RICRegionsWithPurchaseInfo retrieveRICRegionsByUser(

                        java.lang.String userId316)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRICRegionsByUser
                * @param retrieveRICRegionsByUser315
            
          */
        public void startretrieveRICRegionsByUser(

            java.lang.String userId316,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MassStopSequence
                    * @param massStopSequence319
                
         */

         
                     public void massStopSequence(

                        com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info320)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MassStopSequence
                * @param massStopSequence319
            
          */
        public void startmassStopSequence(

            com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info320,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                    * @param retrieveDefaultPlanningSessionProperties322
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties retrieveDefaultPlanningSessionProperties(

                        java.lang.String regionId323)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                * @param retrieveDefaultPlanningSessionProperties322
            
          */
        public void startretrieveDefaultPlanningSessionProperties(

            java.lang.String regionId323,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                    * @param schedulerRetrieveOrderByIdentity326
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder schedulerRetrieveOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity327,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options328)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                * @param schedulerRetrieveOrderByIdentity326
            
          */
        public void startschedulerRetrieveOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity327,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options328,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                    * @param schedulerCalculateDeliveryWindowMetrics331
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics[] schedulerCalculateDeliveryWindowMetrics(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity332,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options333)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                * @param schedulerCalculateDeliveryWindowMetrics331
            
          */
        public void startschedulerCalculateDeliveryWindowMetrics(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity332,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options333,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutesEx
                    * @param schedulerRebuildRoutesEx336
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutesEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity337,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options338)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutesEx
                * @param schedulerRebuildRoutesEx336
            
          */
        public void startschedulerRebuildRoutesEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity337,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options338,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                    * @param retrievePlanningSessionPropertiesByCriteria341
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession[] retrievePlanningSessionPropertiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria342)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                * @param retrievePlanningSessionPropertiesByCriteria341
            
          */
        public void startretrievePlanningSessionPropertiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria342,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveDepotSkusAvailabilities
                    * @param saveDepotSkusAvailabilities345
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] saveDepotSkusAvailabilities(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus346)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveDepotSkusAvailabilities
                * @param saveDepotSkusAvailabilities345
            
          */
        public void startsaveDepotSkusAvailabilities(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus346,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationServiceHistory
                    * @param retrieveLocationServiceHistory349
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LocationServiceDetails[] retrieveLocationServiceHistory(

                        com.freshdirect.routing.proxy.stub.transportation.RetrieveLocationServiceHistoryOptions options350)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationServiceHistory
                * @param retrieveLocationServiceHistory349
            
          */
        public void startretrieveLocationServiceHistory(

            com.freshdirect.routing.proxy.stub.transportation.RetrieveLocationServiceHistoryOptions options350,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartStop
                    * @param departStop353
                
         */

         
                     public void departStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info354)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartStop
                * @param departStop353
            
          */
        public void startdepartStop(

            com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info354,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEmployee
                    * @param saveEmployee356
                
         */

         
                     public void saveEmployee(

                        com.freshdirect.routing.proxy.stub.transportation.Employee employee357)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEmployee
                * @param saveEmployee356
            
          */
        public void startsaveEmployee(

            com.freshdirect.routing.proxy.stub.transportation.Employee employee357,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStop
                    * @param saveStop359
                
         */

         
                     public void saveStop(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop360,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions361,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options362)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStop
                * @param saveStop359
            
          */
        public void startsaveStop(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop360,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions361,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options362,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                    * @param retrieveAutoArriveDepartOptions364
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AutoArriveDepartOptions retrieveAutoArriveDepartOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity365)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                * @param retrieveAutoArriveDepartOptions364
            
          */
        public void startretrieveAutoArriveDepartOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity365,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AuthenticateUser
                    * @param authenticateUser368
                
         */

         
                     public int authenticateUser(

                        java.lang.String userID369,java.lang.String password370)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AuthenticateUser
                * @param authenticateUser368
            
          */
        public void startauthenticateUser(

            java.lang.String userID369,java.lang.String password370,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrdersEx
                    * @param saveRoutingImportOrdersEx373
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrdersEx(

                        java.lang.String regionId374,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders375,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions376,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions377)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrdersEx
                * @param saveRoutingImportOrdersEx373
            
          */
        public void startsaveRoutingImportOrdersEx(

            java.lang.String regionId374,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders375,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions376,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions377,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                    * @param retrieveStopCancelCodesByCriteria380
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopCancelCode[] retrieveStopCancelCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria381)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                * @param retrieveStopCancelCodesByCriteria380
            
          */
        public void startretrieveStopCancelCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria381,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    