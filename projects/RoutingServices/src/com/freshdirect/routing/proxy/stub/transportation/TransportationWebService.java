

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
                    * @param retrieveUserDefinedColumnByIdentity394
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn retrieveUserDefinedColumnByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity395)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnByIdentity
                * @param retrieveUserDefinedColumnByIdentity394
            
          */
        public void startretrieveUserDefinedColumnByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity395,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                    * @param buildDispatchDriverDirectionsEx398
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirectionsEx(

                        com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info399)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                * @param buildDispatchDriverDirectionsEx398
            
          */
        public void startbuildDispatchDriverDirectionsEx(

            com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info399,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyResults
                    * @param retrieveStopSurveyResults402
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity403)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyResults
                * @param retrieveStopSurveyResults402
            
          */
        public void startretrieveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity403,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                    * @param schedulerRetrieveFeederRoutes406
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] schedulerRetrieveFeederRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity407,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options408)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                * @param schedulerRetrieveFeederRoutes406
            
          */
        public void startschedulerRetrieveFeederRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity407,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options408,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveActiveAlertRecipientsByLocationIdentity
                    * @param retrieveActiveAlertRecipientsByLocationIdentity411
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] retrieveActiveAlertRecipientsByLocationIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity412)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveActiveAlertRecipientsByLocationIdentity
                * @param retrieveActiveAlertRecipientsByLocationIdentity411
            
          */
        public void startretrieveActiveAlertRecipientsByLocationIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity412,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignDrivers
                    * @param assignDrivers415
                
         */

         
                     public void assignDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity416,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers417)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignDrivers
                * @param assignDrivers415
            
          */
        public void startassignDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity416,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers417,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopByIdentity
                    * @param retrieveStopByIdentity419
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop retrieveStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity420,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options421)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopByIdentity
                * @param retrieveStopByIdentity419
            
          */
        public void startretrieveStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity420,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options421,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeesByCriteria
                    * @param retrieveEmployeesByCriteria424
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria425)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeesByCriteria
                * @param retrieveEmployeesByCriteria424
            
          */
        public void startretrieveEmployeesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria425,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                    * @param retrieveRoutingRouteByIdentity428
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute retrieveRoutingRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity429,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options430)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                * @param retrieveRoutingRouteByIdentity428
            
          */
        public void startretrieveRoutingRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity429,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options430,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                    * @param retrieveRoutingSessionByIdentity433
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession retrieveRoutingSessionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity434,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options435)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                * @param retrieveRoutingSessionByIdentity433
            
          */
        public void startretrieveRoutingSessionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity434,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options435,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                    * @param schedulerRetrieveOrdersByCriteria438
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerRetrieveOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity439,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria440,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options441)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                * @param schedulerRetrieveOrdersByCriteria438
            
          */
        public void startschedulerRetrieveOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity439,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria440,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options441,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                    * @param schedulerDeleteDeliveryWindow444
                
         */

         
                     public void schedulerDeleteDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity445,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window446,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options447)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                * @param schedulerDeleteDeliveryWindow444
            
          */
        public void startschedulerDeleteDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity445,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window446,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options447,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSurveyResults
                    * @param saveStopSurveyResults449
                
         */

         
                     public void saveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity450,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults451)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSurveyResults
                * @param saveStopSurveyResults449
            
          */
        public void startsaveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity450,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults451,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveStandardRoute
                    * @param removeStandardRoute453
                
         */

         
                     public void removeStandardRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity454,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRemoveOptions options455)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveStandardRoute
                * @param removeStandardRoute453
            
          */
        public void startremoveStandardRoute(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity454,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRemoveOptions options455,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerMovableOrders
                    * @param schedulerMovableOrders457
                
         */

         
                     public void schedulerMovableOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity458,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria459,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options460)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerMovableOrders
                * @param schedulerMovableOrders457
            
          */
        public void startschedulerMovableOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity458,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria459,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options460,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                    * @param schedulerRetrieveDeliveryWaveInstancesByCriteria462
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance[] schedulerRetrieveDeliveryWaveInstancesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity463,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria464,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options465)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                * @param schedulerRetrieveDeliveryWaveInstancesByCriteria462
            
          */
        public void startschedulerRetrieveDeliveryWaveInstancesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity463,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria464,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options465,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsRoutes
                    * @param retrieveTelematicsRoutes468
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsRoute[] retrieveTelematicsRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionId469)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsRoutes
                * @param retrieveTelematicsRoutes468
            
          */
        public void startretrieveTelematicsRoutes(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionId469,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CancelStop
                    * @param cancelStop472
                
         */

         
                     public void cancelStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info473)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CancelStop
                * @param cancelStop472
            
          */
        public void startcancelStop(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info473,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryByCriteria
                    * @param retrievePositionHistoryByCriteria475
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria476)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryByCriteria
                * @param retrievePositionHistoryByCriteria475
            
          */
        public void startretrievePositionHistoryByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria476,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoute
                    * @param removeRoute479
                
         */

         
                     public void removeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity480)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoute
                * @param removeRoute479
            
          */
        public void startremoveRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity480,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnlockNotifications
                    * @param unlockNotifications482
                
         */

         
                     public void unlockNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria483)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnlockNotifications
                * @param unlockNotifications482
            
          */
        public void startunlockNotifications(

            com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria483,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerPurge
                    * @param schedulerPurge485
                
         */

         
                     public void schedulerPurge(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity486,boolean reloadXML487)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerPurge
                * @param schedulerPurge485
            
          */
        public void startschedulerPurge(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity486,boolean reloadXML487,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AcknowledgeRouteExceptions
                    * @param acknowledgeRouteExceptions489
                
         */

         
                     public void acknowledgeRouteExceptions(

                        int[] routeExceptions490)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AcknowledgeRouteExceptions
                * @param acknowledgeRouteExceptions489
            
          */
        public void startacknowledgeRouteExceptions(

            int[] routeExceptions490,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateDeliveryDetails
                    * @param updateDeliveryDetails492
                
         */

         
                     public void updateDeliveryDetails(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info493)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateDeliveryDetails
                * @param updateDeliveryDetails492
            
          */
        public void startupdateDeliveryDetails(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info493,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationComments
                    * @param saveLocationComments495
                
         */

         
                     public void saveLocationComments(

                        com.freshdirect.routing.proxy.stub.transportation.LocationComment[] comments496,com.freshdirect.routing.proxy.stub.transportation.SaveLocationCommentOptions options497)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationComments
                * @param saveLocationComments495
            
          */
        public void startsaveLocationComments(

            com.freshdirect.routing.proxy.stub.transportation.LocationComment[] comments496,com.freshdirect.routing.proxy.stub.transportation.SaveLocationCommentOptions options497,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                    * @param retrieveRoutingRouteDailyStatsByCriteria499
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRoutingRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria500,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options501)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                * @param retrieveRoutingRouteDailyStatsByCriteria499
            
          */
        public void startretrieveRoutingRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria500,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options501,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteNotifications
                    * @param deleteNotifications504
                
         */

         
                     public void deleteNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities505)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteNotifications
                * @param deleteNotifications504
            
          */
        public void startdeleteNotifications(

            com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities505,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrders
                    * @param schedulerOptimizeOrders507
                
         */

         
                     public void schedulerOptimizeOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity508)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrders
                * @param schedulerOptimizeOrders507
            
          */
        public void startschedulerOptimizeOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity508,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                    * @param retrieveDutyPeriodsByCriteria510
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] retrieveDutyPeriodsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria511,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options512)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                * @param retrieveDutyPeriodsByCriteria510
            
          */
        public void startretrieveDutyPeriodsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria511,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options512,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSignature
                    * @param retrieveStopSignature515
                
         */

         
                     public javax.activation.DataHandler retrieveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity516,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType517)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSignature
                * @param retrieveStopSignature515
            
          */
        public void startretrieveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity516,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType517,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationCommentsByCriteria
                    * @param retrieveLocationCommentsByCriteria520
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LocationComment[] retrieveLocationCommentsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCommentCriteria criteria521)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationCommentsByCriteria
                * @param retrieveLocationCommentsByCriteria520
            
          */
        public void startretrieveLocationCommentsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCommentCriteria criteria521,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGlobalConfig
                    * @param retrieveGlobalConfig524
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveGlobalConfig(

                        java.lang.String applicationID525,java.lang.String configGroupID526)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGlobalConfig
                * @param retrieveGlobalConfig524
            
          */
        public void startretrieveGlobalConfig(

            java.lang.String applicationID525,java.lang.String configGroupID526,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionByIdentity
                    * @param retrieveRegionByIdentity529
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region retrieveRegionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity530)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionByIdentity
                * @param retrieveRegionByIdentity529
            
          */
        public void startretrieveRegionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity530,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerLoad
                    * @param schedulerLoad533
                
         */

         
                     public void schedulerLoad(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity534)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerLoad
                * @param schedulerLoad533
            
          */
        public void startschedulerLoad(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity534,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteReport
                    * @param deleteReport536
                
         */

         
                     public void deleteReport(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity537)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteReport
                * @param deleteReport536
            
          */
        public void startdeleteReport(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity537,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyQuestions
                    * @param retrieveStopSurveyQuestions539
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveStopSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity540)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyQuestions
                * @param retrieveStopSurveyQuestions539
            
          */
        public void startretrieveStopSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity540,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedDrivers
                    * @param retrieveAssignedDrivers543
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] retrieveAssignedDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity544)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedDrivers
                * @param retrieveAssignedDrivers543
            
          */
        public void startretrieveAssignedDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity544,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStandardRouteSets
                    * @param saveStandardRouteSets547
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRejection[] saveStandardRouteSets(

                        java.lang.String regionID548,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] standardRouteSets549,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetSaveOptions options550)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStandardRouteSets
                * @param saveStandardRouteSets547
            
          */
        public void startsaveStandardRouteSets(

            java.lang.String regionID548,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] standardRouteSets549,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetSaveOptions options550,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ChangeUserPassword
                    * @param changeUserPassword553
                
         */

         
                     public void changeUserPassword(

                        java.lang.String userID554,java.lang.String oldPassword555,java.lang.String newPassword556)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ChangeUserPassword
                * @param changeUserPassword553
            
          */
        public void startchangeUserPassword(

            java.lang.String userID554,java.lang.String oldPassword555,java.lang.String newPassword556,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                    * @param deleteSapShipmentsBySessionIdentity558
                
         */

         
                     public boolean deleteSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid559)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                * @param deleteSapShipmentsBySessionIdentity558
            
          */
        public void startdeleteSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid559,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                    * @param retrievePlanningUserDefinedFieldInfo562
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedFieldInfo[] retrievePlanningUserDefinedFieldInfo(

                        java.lang.String regionId563)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                * @param retrievePlanningUserDefinedFieldInfo562
            
          */
        public void startretrievePlanningUserDefinedFieldInfo(

            java.lang.String regionId563,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                    * @param retrieveUserDefinedColumnsByCriteria566
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn[] retrieveUserDefinedColumnsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria567)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                * @param retrieveUserDefinedColumnsByCriteria566
            
          */
        public void startretrieveUserDefinedColumnsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria567,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CalculateTimeDist
                    * @param calculateTimeDist570
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TimeDistResult calculateTimeDist(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity571,int fromLatitude572,int fromLongitude573,int toLatitude574,int toLongitude575)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CalculateTimeDist
                * @param calculateTimeDist570
            
          */
        public void startcalculateTimeDist(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity571,int fromLatitude572,int fromLongitude573,int toLatitude574,int toLongitude575,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegionConfig
                    * @param saveRegionConfig578
                
         */

         
                     public void saveRegionConfig(

                        java.lang.String applicationID579,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity580,java.lang.String configGroupID581,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items582)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegionConfig
                * @param saveRegionConfig578
            
          */
        public void startsaveRegionConfig(

            java.lang.String applicationID579,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity580,java.lang.String configGroupID581,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items582,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                    * @param retrieveStopNotesByCriteriaEx584
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopNote[] retrieveStopNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria585,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options586)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                * @param retrieveStopNotesByCriteriaEx584
            
          */
        public void startretrieveStopNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria585,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options586,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteForDevice
                    * @param retrieveRouteForDevice589
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteForDevice(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity590,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options591,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity592)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteForDevice
                * @param retrieveRouteForDevice589
            
          */
        public void startretrieveRouteForDevice(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity590,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options591,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity592,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionETAs
                    * @param updateRoutePositionETAs595
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionETAs(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId596,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos597)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionETAs
                * @param updateRoutePositionETAs595
            
          */
        public void startupdateRoutePositionETAs(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId596,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos597,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SendTextMessageToDriver
                    * @param sendTextMessageToDriver600
                
         */

         
                     public void sendTextMessageToDriver(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity601,java.lang.String message602,java.lang.String fromUserID603)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SendTextMessageToDriver
                * @param sendTextMessageToDriver600
            
          */
        public void startsendTextMessageToDriver(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity601,java.lang.String message602,java.lang.String fromUserID603,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedTable
                    * @param deleteUserDefinedTable605
                
         */

         
                     public void deleteUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId606)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedTable
                * @param deleteUserDefinedTable605
            
          */
        public void startdeleteUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId606,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSkus
                    * @param saveSkus608
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] saveSkus(

                        com.freshdirect.routing.proxy.stub.transportation.Sku[] skus609)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSkus
                * @param saveSkus608
            
          */
        public void startsaveSkus(

            com.freshdirect.routing.proxy.stub.transportation.Sku[] skus609,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                    * @param retrieveSapShipmentsBySessionIdentity612
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SapShipment[] retrieveSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid613)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                * @param retrieveSapShipmentsBySessionIdentity612
            
          */
        public void startretrieveSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid613,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExtendOrderReservation
                    * @param schedulerExtendOrderReservation616
                
         */

         
                     public void schedulerExtendOrderReservation(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity617,java.lang.String orderNumberXML618,int extendMinutes619)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExtendOrderReservation
                * @param schedulerExtendOrderReservation616
            
          */
        public void startschedulerExtendOrderReservation(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity617,java.lang.String orderNumberXML618,int extendMinutes619,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionOptions
                    * @param retrieveRegionOptions621
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RegionOptions retrieveRegionOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity622)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionOptions
                * @param retrieveRegionOptions621
            
          */
        public void startretrieveRegionOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity622,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteSetByIdentity
                    * @param retrieveStandardRouteSetByIdentity625
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet retrieveStandardRouteSetByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity626,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options627)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteSetByIdentity
                * @param retrieveStandardRouteSetByIdentity625
            
          */
        public void startretrieveStandardRouteSetByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity626,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options627,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSignature
                    * @param saveStopSignature630
                
         */

         
                     public void saveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity631,javax.activation.DataHandler signatureData632)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSignature
                * @param saveStopSignature630
            
          */
        public void startsaveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity631,javax.activation.DataHandler signatureData632,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveDepartStop
                    * @param arriveDepartStop634
                
         */

         
                     public void arriveDepartStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopArriveDepartInfo info635)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveDepartStop
                * @param arriveDepartStop634
            
          */
        public void startarriveDepartStop(

            com.freshdirect.routing.proxy.stub.transportation.StopArriveDepartInfo info635,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeesByCriteriaEx
                    * @param retrieveEmployeesByCriteriaEx637
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria638,com.freshdirect.routing.proxy.stub.transportation.EmployeeRetrieveOptions options639)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeesByCriteriaEx
                * @param retrieveEmployeesByCriteriaEx637
            
          */
        public void startretrieveEmployeesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria638,com.freshdirect.routing.proxy.stub.transportation.EmployeeRetrieveOptions options639,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                    * @param retrieveQuantityReasonCodesByCriteria642
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCode[] retrieveQuantityReasonCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria643)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                * @param retrieveQuantityReasonCodesByCriteria642
            
          */
        public void startretrieveQuantityReasonCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria643,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                    * @param retrieveLineItemNotesByCriteriaEx646
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LineItemNote[] retrieveLineItemNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria647,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options648)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                * @param retrieveLineItemNotesByCriteriaEx646
            
          */
        public void startretrieveLineItemNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria647,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options648,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                    * @param retrieveOrderNotesByCriteriaEx651
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.OrderNote[] retrieveOrderNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria652,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options653)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                * @param retrieveOrderNotesByCriteriaEx651
            
          */
        public void startretrieveOrderNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria652,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options653,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                    * @param retrieveEquipmentTypeByIdentity656
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType retrieveEquipmentTypeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity657,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options658)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                * @param retrieveEquipmentTypeByIdentity656
            
          */
        public void startretrieveEquipmentTypeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity657,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options658,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                    * @param retrieveRoutingSourcedOrdersByCriteria661
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] retrieveRoutingSourcedOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria662,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options663)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                * @param retrieveRoutingSourcedOrdersByCriteria661
            
          */
        public void startretrieveRoutingSourcedOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria662,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options663,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                    * @param retrieveUndeliverableStopCodesByCriteria666
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCode[] retrieveUndeliverableStopCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria667)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                * @param retrieveUndeliverableStopCodesByCriteria666
            
          */
        public void startretrieveUndeliverableStopCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria667,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteRoutingSession
                    * @param deleteRoutingSession670
                
         */

         
                     public void deleteRoutingSession(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity671)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteRoutingSession
                * @param deleteRoutingSession670
            
          */
        public void startdeleteRoutingSession(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity671,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                    * @param schedulerIsExcludingCutoffRoutes673
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult schedulerIsExcludingCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity674)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                * @param schedulerIsExcludingCutoffRoutes673
            
          */
        public void startschedulerIsExcludingCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity674,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                    * @param retrieveRoutingImportOrderByIdentity677
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder retrieveRoutingImportOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity678,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions679)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                * @param retrieveRoutingImportOrderByIdentity677
            
          */
        public void startretrieveRoutingImportOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity678,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions679,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkuByIdentity
                    * @param retrieveSkuByIdentity682
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku retrieveSkuByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity683)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkuByIdentity
                * @param retrieveSkuByIdentity682
            
          */
        public void startretrieveSkuByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity683,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteByIdentity
                    * @param retrieveStandardRouteByIdentity686
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRoute retrieveStandardRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity687,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options688)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteByIdentity
                * @param retrieveStandardRouteByIdentity686
            
          */
        public void startretrieveStandardRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity687,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options688,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUnassigned
                    * @param deleteUnassigned691
                
         */

         
                     public void deleteUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop692)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUnassigned
                * @param deleteUnassigned691
            
          */
        public void startdeleteUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop692,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocationComments
                    * @param deleteLocationComments694
                
         */

         
                     public void deleteLocationComments(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCommentIdentity[] comments695)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocationComments
                * @param deleteLocationComments694
            
          */
        public void startdeleteLocationComments(

            com.freshdirect.routing.proxy.stub.transportation.LocationCommentIdentity[] comments695,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserConfig
                    * @param saveUserConfig697
                
         */

         
                     public void saveUserConfig(

                        java.lang.String applicationID698,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity699,java.lang.String configGroupID700,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items701)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserConfig
                * @param saveUserConfig697
            
          */
        public void startsaveUserConfig(

            java.lang.String applicationID698,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity699,java.lang.String configGroupID700,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items701,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeByIdentity
                    * @param retrieveEmployeeByIdentity703
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity704)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeByIdentity
                * @param retrieveEmployeeByIdentity703
            
          */
        public void startretrieveEmployeeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity704,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUpdateOrder
                    * @param schedulerUpdateOrder707
                
         */

         
                     public boolean schedulerUpdateOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity708,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity709,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options710)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUpdateOrder
                * @param schedulerUpdateOrder707
            
          */
        public void startschedulerUpdateOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity708,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity709,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options710,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SurveyResponse
                    * @param surveyResponse713
                
         */

         
                     public void surveyResponse(

                        com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info714)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SurveyResponse
                * @param surveyResponse713
            
          */
        public void startsurveyResponse(

            com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info714,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RedeliverStop
                    * @param redeliverStop716
                
         */

         
                     public void redeliverStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info717)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RedeliverStop
                * @param redeliverStop716
            
          */
        public void startredeliverStop(

            com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info717,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRoutesByCriteria
                    * @param retrieveStandardRoutesByCriteria719
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] retrieveStandardRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteCriteria criteria720,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options721)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRoutesByCriteria
                * @param retrieveStandardRoutesByCriteria719
            
          */
        public void startretrieveStandardRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteCriteria criteria720,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options721,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionsByCriteria
                    * @param retrieveRegionsByCriteria724
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region[] retrieveRegionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria725)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionsByCriteria
                * @param retrieveRegionsByCriteria724
            
          */
        public void startretrieveRegionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria725,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveStandardRouteSet
                    * @param removeStandardRouteSet728
                
         */

         
                     public void removeStandardRouteSet(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity729,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRemoveOptions options730)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveStandardRouteSet
                * @param removeStandardRouteSet728
            
          */
        public void startremoveStandardRouteSet(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity729,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRemoveOptions options730,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyResults
                    * @param retrieveRouteSurveyResults732
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity733,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt734)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyResults
                * @param retrieveRouteSurveyResults732
            
          */
        public void startretrieveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity733,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt734,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveActiveAlertRecipientTypes
                    * @param retrieveActiveAlertRecipientTypes737
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipientType[] retrieveActiveAlertRecipientTypes(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveActiveAlertRecipientTypes
                * @param retrieveActiveAlertRecipientTypes737
            
          */
        public void startretrieveActiveAlertRecipientTypes(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByCriteria
                    * @param retrieveEquipmentByCriteria740
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment[] retrieveEquipmentByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria741,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options742)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByCriteria
                * @param retrieveEquipmentByCriteria740
            
          */
        public void startretrieveEquipmentByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria741,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options742,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartOrigin
                    * @param departOrigin745
                
         */

         
                     public void departOrigin(

                        com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info746)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartOrigin
                * @param departOrigin745
            
          */
        public void startdepartOrigin(

            com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info746,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateRoutingSession
                    * @param createRoutingSession748
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity createRoutingSession(

                        java.lang.String regionId749,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties750)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateRoutingSession
                * @param createRoutingSession748
            
          */
        public void startcreateRoutingSession(

            java.lang.String regionId749,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties750,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                    * @param retrieveDefaultRoutingSessionProperties753
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties retrieveDefaultRoutingSessionProperties(

                        java.lang.String regionId754,java.util.Date sessionDate755)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                * @param retrieveDefaultRoutingSessionProperties753
            
          */
        public void startretrieveDefaultRoutingSessionProperties(

            java.lang.String regionId754,java.util.Date sessionDate755,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStationaryRoutePosition
                    * @param updateStationaryRoutePosition758
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateStationaryRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.StationaryRoutePositionInfo info759)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStationaryRoutePosition
                * @param updateStationaryRoutePosition758
            
          */
        public void startupdateStationaryRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.StationaryRoutePositionInfo info759,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateBatchRoutePosition
                    * @param updateBatchRoutePosition762
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateBatchRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.BatchRoutePositionInfo info763)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateBatchRoutePosition
                * @param updateBatchRoutePosition762
            
          */
        public void startupdateBatchRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.BatchRoutePositionInfo info763,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoutingStop
                    * @param removeRoutingStop766
                
         */

         
                     public void removeRoutingStop(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity767,com.freshdirect.routing.proxy.stub.transportation.RemoveRoutingStopOptions options768)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoutingStop
                * @param removeRoutingStop766
            
          */
        public void startremoveRoutingStop(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity stopIdentity767,com.freshdirect.routing.proxy.stub.transportation.RemoveRoutingStopOptions options768,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                    * @param retrieveDutyPeriodByIdentity770
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod retrieveDutyPeriodByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity771,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options772)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                * @param retrieveDutyPeriodByIdentity770
            
          */
        public void startretrieveDutyPeriodByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity771,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options772,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopsByCriteria
                    * @param retrieveStopsByCriteria775
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria776,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options777)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopsByCriteria
                * @param retrieveStopsByCriteria775
            
          */
        public void startretrieveStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria776,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options777,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                    * @param retrieveDepotSkuAvailabilityByIdentity780
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability retrieveDepotSkuAvailabilityByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity781)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                * @param retrieveDepotSkuAvailabilityByIdentity780
            
          */
        public void startretrieveDepotSkuAvailabilityByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity781,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingRouteNetMatrix
                    * @param buildRoutingRouteNetMatrix784
                
         */

         
                     public void buildRoutingRouteNetMatrix(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity785)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingRouteNetMatrix
                * @param buildRoutingRouteNetMatrix784
            
          */
        public void startbuildRoutingRouteNetMatrix(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity785,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                    * @param retrieveRoutingSessionsByCriteria787
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] retrieveRoutingSessionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria788,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options789)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                * @param retrieveRoutingSessionsByCriteria787
            
          */
        public void startretrieveRoutingSessionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria788,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options789,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReserveOrder
                    * @param schedulerReserveOrder792
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ReserveResult schedulerReserveOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity793,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder794,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow795,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options796)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReserveOrder
                * @param schedulerReserveOrder792
            
          */
        public void startschedulerReserveOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity793,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder794,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow795,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options796,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteSurveyResults
                    * @param saveRouteSurveyResults799
                
         */

         
                     public void saveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity800,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt801,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults802)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteSurveyResults
                * @param saveRouteSurveyResults799
            
          */
        public void startsaveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity800,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt801,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults802,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingRoute
                    * @param saveRoutingRoute804
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SaveRoutingRejection[] saveRoutingRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRoute route805,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingRouteOptions options806)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingRoute
                * @param saveRoutingRoute804
            
          */
        public void startsaveRoutingRoute(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRoute route805,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingRouteOptions options806,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerAnalyzeOrder
                    * @param schedulerAnalyzeOrder809
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] schedulerAnalyzeOrder(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order810,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options811)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerAnalyzeOrder
                * @param schedulerAnalyzeOrder809
            
          */
        public void startschedulerAnalyzeOrder(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order810,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options811,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveActiveAlertRecipients
                    * @param saveActiveAlertRecipients814
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] saveActiveAlertRecipients(

                        com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] activeAlertRecipients815)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveActiveAlertRecipients
                * @param saveActiveAlertRecipients814
            
          */
        public void startsaveActiveAlertRecipients(

            com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] activeAlertRecipients815,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamps
                    * @param convertTimestamps818
                
         */

         
                     public java.util.Calendar[] convertTimestamps(

                        java.util.Calendar[] sourceTimestamps819,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions820,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions821)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamps
                * @param convertTimestamps818
            
          */
        public void startconvertTimestamps(

            java.util.Calendar[] sourceTimestamps819,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions820,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions821,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByCriteria
                    * @param retrieveNotificationsByCriteria824
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria825,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions826,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options827)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByCriteria
                * @param retrieveNotificationsByCriteria824
            
          */
        public void startretrieveNotificationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria825,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions826,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options827,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                    * @param retrieveRoutingUnassignsByCriteria830
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria831,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options832)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                * @param retrieveRoutingUnassignsByCriteria830
            
          */
        public void startretrieveRoutingUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria831,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options832,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__PlaceUnassigned
                    * @param placeUnassigned835
                
         */

         
                     public void placeUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop836,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity837,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition838,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime839,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions840)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__PlaceUnassigned
                * @param placeUnassigned835
            
          */
        public void startplaceUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop836,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity837,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition838,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime839,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions840,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateTelematicsCachePositions
                    * @param updateTelematicsCachePositions842
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions843)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateTelematicsCachePositions
                * @param updateTelematicsCachePositions842
            
          */
        public void startupdateTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions843,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                    * @param retrievePlanningLocationExtensionsByCriteria846
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] retrievePlanningLocationExtensionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria847,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options848)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                * @param retrievePlanningLocationExtensionsByCriteria846
            
          */
        public void startretrievePlanningLocationExtensionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria847,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options848,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BulkArriveDepartStop
                    * @param bulkArriveDepartStop851
                
         */

         
                     public void bulkArriveDepartStop(

                        com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos852)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BulkArriveDepartStop
                * @param bulkArriveDepartStop851
            
          */
        public void startbulkArriveDepartStop(

            com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos852,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DisableWirelessCommunication
                    * @param disableWirelessCommunication854
                
         */

         
                     public void disableWirelessCommunication(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity info855)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DisableWirelessCommunication
                * @param disableWirelessCommunication854
            
          */
        public void startdisableWirelessCommunication(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity info855,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SavePlanningLocationExtensions
                    * @param savePlanningLocationExtensions857
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] savePlanningLocationExtensions(

                        java.lang.String regionId858,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions859)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SavePlanningLocationExtensions
                * @param savePlanningLocationExtensions857
            
          */
        public void startsavePlanningLocationExtensions(

            java.lang.String regionId858,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions859,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SavePrintTemplate
                    * @param savePrintTemplate862
                
         */

         
                     public void savePrintTemplate(

                        com.freshdirect.routing.proxy.stub.transportation.PrintTemplate printTemplate863)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SavePrintTemplate
                * @param savePrintTemplate862
            
          */
        public void startsavePrintTemplate(

            com.freshdirect.routing.proxy.stub.transportation.PrintTemplate printTemplate863,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingStop
                    * @param saveRoutingStop865
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SaveRoutingRejection[] saveRoutingStop(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStop stop866,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingStopOptions options867)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingStop
                * @param saveRoutingStop865
            
          */
        public void startsaveRoutingStop(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStop stop866,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingStopOptions options867,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CleanupTelematicsCachePositions
                    * @param cleanupTelematicsCachePositions870
                
         */

         
                     public void cleanupTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria871)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CleanupTelematicsCachePositions
                * @param cleanupTelematicsCachePositions870
            
          */
        public void startcleanupTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria871,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStandardRoutes
                    * @param saveStandardRoutes873
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteRejection[] saveStandardRoutes(

                        java.lang.String regionID874,com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] standardRoutes875,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSaveOptions options876)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStandardRoutes
                * @param saveStandardRoutes873
            
          */
        public void startsaveStandardRoutes(

            java.lang.String regionID874,com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] standardRoutes875,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSaveOptions options876,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutesByCriteria
                    * @param retrieveRoutesByCriteria879
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route[] retrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria880,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options881)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutesByCriteria
                * @param retrieveRoutesByCriteria879
            
          */
        public void startretrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria880,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options881,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                    * @param schedulerRetrieveRouteByIdentity884
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute schedulerRetrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity885,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options886)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                * @param schedulerRetrieveRouteByIdentity884
            
          */
        public void startschedulerRetrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity885,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options886,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRouteETAs
                    * @param updateRouteETAs889
                
         */

         
                     public void updateRouteETAs(

                        com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info890)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRouteETAs
                * @param updateRouteETAs889
            
          */
        public void startupdateRouteETAs(

            com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info890,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopEx
                    * @param saveStopEx892
                
         */

         
                     public void saveStopEx(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop893,com.freshdirect.routing.proxy.stub.transportation.SaveStopExOptions options894)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopEx
                * @param saveStopEx892
            
          */
        public void startsaveStopEx(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop893,com.freshdirect.routing.proxy.stub.transportation.SaveStopExOptions options894,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ReturnFault
                    * @param returnFault896
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Fault returnFault(

                        int requestedFaultCode897)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ReturnFault
                * @param returnFault896
            
          */
        public void startreturnFault(

            int requestedFaultCode897,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeRouteStatsByCriteria
                    * @param retrieveEmployeeRouteStatsByCriteria900
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStats[] retrieveEmployeeRouteStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStatsCriteria criteria901)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeRouteStatsByCriteria
                * @param retrieveEmployeeRouteStatsByCriteria900
            
          */
        public void startretrieveEmployeeRouteStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStatsCriteria criteria901,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveyDetails
                    * @param retrieveSurveyDetails904
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyDetails[] retrieveSurveyDetails(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity905)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveyDetails
                * @param retrieveSurveyDetails904
            
          */
        public void startretrieveSurveyDetails(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity905,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AddRICUser
                    * @param addRICUser908
                
         */

         
                     public void addRICUser(

                        com.freshdirect.routing.proxy.stub.transportation.User user909)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AddRICUser
                * @param addRICUser908
            
          */
        public void startaddRICUser(

            com.freshdirect.routing.proxy.stub.transportation.User user909,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveProductsPurchased
                    * @param retrieveProductsPurchased911
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased retrieveProductsPurchased(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity912)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveProductsPurchased
                * @param retrieveProductsPurchased911
            
          */
        public void startretrieveProductsPurchased(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity912,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePermissionsForUser
                    * @param retrievePermissionsForUser915
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserPermissions retrievePermissionsForUser(

                        java.lang.String userID916,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity917)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePermissionsForUser
                * @param retrievePermissionsForUser915
            
          */
        public void startretrievePermissionsForUser(

            java.lang.String userID916,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity917,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocations
                    * @param deleteLocations920
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] deleteLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations921)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocations
                * @param deleteLocations920
            
          */
        public void startdeleteLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations921,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedEquipment
                    * @param retrieveAssignedEquipment924
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] retrieveAssignedEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity925)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedEquipment
                * @param retrieveAssignedEquipment924
            
          */
        public void startretrieveAssignedEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity925,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteReportedDistances
                    * @param saveRouteReportedDistances928
                
         */

         
                     public void saveRouteReportedDistances(

                        com.freshdirect.routing.proxy.stub.transportation.RouteReportedDistance[] reportedDistances929)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteReportedDistances
                * @param saveRouteReportedDistances928
            
          */
        public void startsaveRouteReportedDistances(

            com.freshdirect.routing.proxy.stub.transportation.RouteReportedDistance[] reportedDistances929,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopByIdentity
                    * @param retrieveRoutingStopByIdentity931
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop retrieveRoutingStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity932,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options933)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopByIdentity
                * @param retrieveRoutingStopByIdentity931
            
          */
        public void startretrieveRoutingStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity932,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options933,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation936
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation936
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirections
                    * @param buildDispatchDriverDirections939
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity940)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirections
                * @param buildDispatchDriverDirections939
            
          */
        public void startbuildDispatchDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity940,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                    * @param retrieveTelematicsCachePositionsByCriteria943
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] retrieveTelematicsCachePositionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria944)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                * @param retrieveTelematicsCachePositionsByCriteria943
            
          */
        public void startretrieveTelematicsCachePositionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria944,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStopSignature
                    * @param updateStopSignature947
                
         */

         
                     public void updateStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info948)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStopSignature
                * @param updateStopSignature947
            
          */
        public void startupdateStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info948,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                    * @param schedulerSaveDeliveryWaveInstance950
                
         */

         
                     public java.lang.String[] schedulerSaveDeliveryWaveInstance(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity951,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity952,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes953,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options954)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                * @param schedulerSaveDeliveryWaveInstance950
            
          */
        public void startschedulerSaveDeliveryWaveInstance(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity951,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity952,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes953,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options954,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AllowAdditionOfRICUsers
                    * @param allowAdditionOfRICUsers957
                
         */

         
                     public boolean allowAdditionOfRICUsers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AllowAdditionOfRICUsers
                * @param allowAdditionOfRICUsers957
            
          */
        public void startallowAdditionOfRICUsers(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGPSProviderOptions
                    * @param retrieveGPSProviderOptions960
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.GPSProviderOptions retrieveGPSProviderOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity961)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGPSProviderOptions
                * @param retrieveGPSProviderOptions960
            
          */
        public void startretrieveGPSProviderOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity961,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                    * @param retrieveRoutingLocationsWithOrders964
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrders(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity965)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                * @param retrieveRoutingLocationsWithOrders964
            
          */
        public void startretrieveRoutingLocationsWithOrders(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity965,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteria
                    * @param retrieveRouteNotesByCriteria968
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria969)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteria
                * @param retrieveRouteNotesByCriteria968
            
          */
        public void startretrieveRouteNotesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria969,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveReport
                    * @param saveReport972
                
         */

         
                     public void saveReport(

                        com.freshdirect.routing.proxy.stub.transportation.Report report973)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveReport
                * @param saveReport972
            
          */
        public void startsaveReport(

            com.freshdirect.routing.proxy.stub.transportation.Report report973,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveys
                    * @param retrieveSurveys975
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Survey[] retrieveSurveys(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity976)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveys
                * @param retrieveSurveys975
            
          */
        public void startretrieveSurveys(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity976,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                    * @param retrieveNotificationsByRecipientIdentity979
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByRecipientIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity980,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions981,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options982)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                * @param retrieveNotificationsByRecipientIdentity979
            
          */
        public void startretrieveNotificationsByRecipientIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity980,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions981,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options982,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBalanceRoutes
                    * @param schedulerBalanceRoutes985
                
         */

         
                     public void schedulerBalanceRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity986,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options987)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBalanceRoutes
                * @param schedulerBalanceRoutes985
            
          */
        public void startschedulerBalanceRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity986,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options987,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                    * @param retrieveUserDefinedDataByCriteria989
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] retrieveUserDefinedDataByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity990,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria991,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions992)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                * @param retrieveUserDefinedDataByCriteria989
            
          */
        public void startretrieveUserDefinedDataByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity990,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria991,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions992,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkusByCriteria
                    * @param retrieveSkusByCriteria995
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] retrieveSkusByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria996)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkusByCriteria
                * @param retrieveSkusByCriteria995
            
          */
        public void startretrieveSkusByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria996,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                    * @param retrieveCannedTextMessagesByCriteria999
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] retrieveCannedTextMessagesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria0)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                * @param retrieveCannedTextMessagesByCriteria999
            
          */
        public void startretrieveCannedTextMessagesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria0,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                    * @param retrieveUserDefinedDataByIdentity3
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData retrieveUserDefinedDataByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity4,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity5,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions6)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                * @param retrieveUserDefinedDataByIdentity3
            
          */
        public void startretrieveUserDefinedDataByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity4,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity5,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions6,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                    * @param retrieveEquipmentTypeByCriteria9
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] retrieveEquipmentTypeByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria10,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options11)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                * @param retrieveEquipmentTypeByCriteria9
            
          */
        public void startretrieveEquipmentTypeByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria10,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options11,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                    * @param retrieveRoutingOrderByIdentity14
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder retrieveRoutingOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity15,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options16)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                * @param retrieveRoutingOrderByIdentity14
            
          */
        public void startretrieveRoutingOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity15,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options16,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__TextMessage
                    * @param textMessage19
                
         */

         
                     public void textMessage(

                        com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info20)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__TextMessage
                * @param textMessage19
            
          */
        public void starttextMessage(

            com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info20,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CompleteRoute
                    * @param completeRoute22
                
         */

         
                     public void completeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info23)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CompleteRoute
                * @param completeRoute22
            
          */
        public void startcompleteRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info23,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoutingRoute
                    * @param removeRoutingRoute25
                
         */

         
                     public void removeRoutingRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity26,com.freshdirect.routing.proxy.stub.transportation.RemoveRoutingRouteOptions options27)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoutingRoute
                * @param removeRoutingRoute25
            
          */
        public void startremoveRoutingRoute(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity26,com.freshdirect.routing.proxy.stub.transportation.RemoveRoutingRouteOptions options27,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                    * @param retrieveLocationsByCriteriaEx29
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria30,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options31)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                * @param retrieveLocationsByCriteriaEx29
            
          */
        public void startretrieveLocationsByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria30,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options31,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveStop
                    * @param arriveStop34
                
         */

         
                     public void arriveStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info35)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveStop
                * @param arriveStop34
            
          */
        public void startarriveStop(

            com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info35,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedTable
                    * @param saveUserDefinedTable37
                
         */

         
                     public void saveUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table38)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedTable
                * @param saveUserDefinedTable37
            
          */
        public void startsaveUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table38,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteSetsByCriteria
                    * @param retrieveStandardRouteSetsByCriteria40
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] retrieveStandardRouteSetsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetCriteria criteria41,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options42)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteSetsByCriteria
                * @param retrieveStandardRouteSetsByCriteria40
            
          */
        public void startretrieveStandardRouteSetsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetCriteria criteria41,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options42,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveAllEmployeeLocationAssignments
                    * @param removeAllEmployeeLocationAssignments45
                
         */

         
                     public void removeAllEmployeeLocationAssignments(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity46)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveAllEmployeeLocationAssignments
                * @param removeAllEmployeeLocationAssignments45
            
          */
        public void startremoveAllEmployeeLocationAssignments(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity46,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoute
                    * @param saveRoute48
                
         */

         
                     public void saveRoute(

                        com.freshdirect.routing.proxy.stub.transportation.Route route49,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions50,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions51)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoute
                * @param saveRoute48
            
          */
        public void startsaveRoute(

            com.freshdirect.routing.proxy.stub.transportation.Route route49,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions50,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions51,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveDestination
                    * @param arriveDestination53
                
         */

         
                     public void arriveDestination(

                        com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info54)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveDestination
                * @param arriveDestination53
            
          */
        public void startarriveDestination(

            com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info54,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByIdentity
                    * @param retrieveEquipmentByIdentity56
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment retrieveEquipmentByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity57,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options58)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByIdentity
                * @param retrieveEquipmentByIdentity56
            
          */
        public void startretrieveEquipmentByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity57,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options58,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationsEx
                    * @param saveLocationsEx61
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocationsEx(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations62,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options63)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationsEx
                * @param saveLocationsEx61
            
          */
        public void startsaveLocationsEx(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations62,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options63,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteByIdentity
                    * @param retrieveRouteByIdentity66
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity67,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options68)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteByIdentity
                * @param retrieveRouteByIdentity66
            
          */
        public void startretrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity67,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options68,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserConfig
                    * @param retrieveUserConfig71
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveUserConfig(

                        java.lang.String applicationID72,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity73,java.lang.String configGroupID74)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserConfig
                * @param retrieveUserConfig71
            
          */
        public void startretrieveUserConfig(

            java.lang.String applicationID72,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity73,java.lang.String configGroupID74,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionEx
                    * @param updateRoutePositionEx77
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos78)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionEx
                * @param updateRoutePositionEx77
            
          */
        public void startupdateRoutePositionEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos78,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentity
                    * @param retrieveLocationByIdentity81
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity82)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentity
                * @param retrieveLocationByIdentity81
            
          */
        public void startretrieveLocationByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity82,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveGlobalConfig
                    * @param saveGlobalConfig85
                
         */

         
                     public void saveGlobalConfig(

                        java.lang.String applicationID86,java.lang.String configGroupID87,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items88)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveGlobalConfig
                * @param saveGlobalConfig85
            
          */
        public void startsaveGlobalConfig(

            java.lang.String applicationID86,java.lang.String configGroupID87,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items88,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserByUserID
                    * @param retrieveUserByUserID90
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.User retrieveUserByUserID(

                        java.lang.String userID91)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserByUserID
                * @param retrieveUserByUserID90
            
          */
        public void startretrieveUserByUserID(

            java.lang.String userID91,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignEquipment
                    * @param assignEquipment94
                
         */

         
                     public void assignEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity95,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment96)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignEquipment
                * @param assignEquipment94
            
          */
        public void startassignEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity95,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment96,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEquipment
                    * @param saveEquipment98
                
         */

         
                     public void saveEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.Equipment equipment99,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options100)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEquipment
                * @param saveEquipment98
            
          */
        public void startsaveEquipment(

            com.freshdirect.routing.proxy.stub.transportation.Equipment equipment99,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options100,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteExceptionsByCriteria
                    * @param retrieveRouteExceptionsByCriteria102
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteException[] retrieveRouteExceptionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteExceptionCriteria criteria103,com.freshdirect.routing.proxy.stub.transportation.RouteExceptionRetrieveOptions options104)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteExceptionsByCriteria
                * @param retrieveRouteExceptionsByCriteria102
            
          */
        public void startretrieveRouteExceptionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteExceptionCriteria criteria103,com.freshdirect.routing.proxy.stub.transportation.RouteExceptionRetrieveOptions options104,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeByIdentityEx
                    * @param retrieveEmployeeByIdentityEx107
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentityEx(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity108,com.freshdirect.routing.proxy.stub.transportation.EmployeeRetrieveOptions options109)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeByIdentityEx
                * @param retrieveEmployeeByIdentityEx107
            
          */
        public void startretrieveEmployeeByIdentityEx(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity108,com.freshdirect.routing.proxy.stub.transportation.EmployeeRetrieveOptions options109,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerConfirmOrder
                    * @param schedulerConfirmOrder112
                
         */

         
                     public void schedulerConfirmOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity113,java.lang.String orderNumberXML114)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerConfirmOrder
                * @param schedulerConfirmOrder112
            
          */
        public void startschedulerConfirmOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity113,java.lang.String orderNumberXML114,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsOptions
                    * @param retrieveTelematicsOptions116
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsOptions retrieveTelematicsOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity117)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsOptions
                * @param retrieveTelematicsOptions116
            
          */
        public void startretrieveTelematicsOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity117,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                    * @param retrieveUserDefinedTableByIdentity120
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable retrieveUserDefinedTableByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity121)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                * @param retrieveUserDefinedTableByIdentity120
            
          */
        public void startretrieveUserDefinedTableByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity121,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteria
                    * @param retrieveLocationsByCriteria124
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria125)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteria
                * @param retrieveLocationsByCriteria124
            
          */
        public void startretrieveLocationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria125,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreatePlanningSession
                    * @param createPlanningSession128
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity createPlanningSession(

                        java.lang.String regionId129,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties130)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreatePlanningSession
                * @param createPlanningSession128
            
          */
        public void startcreatePlanningSession(

            java.lang.String regionId129,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties130,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentityEx
                    * @param retrieveLocationByIdentityEx133
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentityEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity134,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options135)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentityEx
                * @param retrieveLocationByIdentityEx133
            
          */
        public void startretrieveLocationByIdentityEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity134,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options135,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                    * @param retrievePlanningTerritoriesByCriteria138
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningTerritory[] retrievePlanningTerritoriesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria139,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options140)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                * @param retrievePlanningTerritoriesByCriteria138
            
          */
        public void startretrievePlanningTerritoriesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria139,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options140,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamp
                    * @param convertTimestamp143
                
         */

         
                     public java.util.Calendar convertTimestamp(

                        java.util.Calendar sourceTimestamp144,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions145,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions146)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamp
                * @param convertTimestamp143
            
          */
        public void startconvertTimestamp(

            java.util.Calendar sourceTimestamp144,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions145,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions146,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                    * @param schedulerSendRoutesToRoadnetEx149
                
         */

         
                     public void schedulerSendRoutesToRoadnetEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity150,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options151)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                * @param schedulerSendRoutesToRoadnetEx149
            
          */
        public void startschedulerSendRoutesToRoadnetEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity150,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options151,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveEmployeeLocationAssignments
                    * @param removeEmployeeLocationAssignments153
                
         */

         
                     public void removeEmployeeLocationAssignments(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity154,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity[] locationIds155)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveEmployeeLocationAssignments
                * @param removeEmployeeLocationAssignments153
            
          */
        public void startremoveEmployeeLocationAssignments(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity154,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity[] locationIds155,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                    * @param schedulerSendRoutesToRoadnet157
                
         */

         
                     public void schedulerSendRoutesToRoadnet(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity158)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                * @param schedulerSendRoutesToRoadnet157
            
          */
        public void startschedulerSendRoutesToRoadnet(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity158,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportsByCriteria
                    * @param retrieveReportsByCriteria160
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report[] retrieveReportsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria161)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportsByCriteria
                * @param retrieveReportsByCriteria160
            
          */
        public void startretrieveReportsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria161,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                    * @param retrieveRouteNotesByCriteriaEx164
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria165,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options166)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                * @param retrieveRouteNotesByCriteriaEx164
            
          */
        public void startretrieveRouteNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria165,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options166,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedData
                    * @param saveUserDefinedData169
                
         */

         
                     public void saveUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity170,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input171,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions172)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedData
                * @param saveUserDefinedData169
            
          */
        public void startsaveUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity170,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input171,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions172,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUnassignsByCriteria
                    * @param retrieveUnassignsByCriteria174
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria175,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options176)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUnassignsByCriteria
                * @param retrieveUnassignsByCriteria174
            
          */
        public void startretrieveUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria175,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options176,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutes
                    * @param schedulerRebuildRoutes179
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity180)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutes
                * @param schedulerRebuildRoutes179
            
          */
        public void startschedulerRebuildRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity180,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeletePlanningLocationExtensions
                    * @param deletePlanningLocationExtensions183
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] deletePlanningLocationExtensions(

                        java.lang.String regionId184,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions185)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeletePlanningLocationExtensions
                * @param deletePlanningLocationExtensions183
            
          */
        public void startdeletePlanningLocationExtensions(

            java.lang.String regionId184,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions185,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DescribeAuthenticaitonPolicy
                    * @param describeAuthenticaitonPolicy188
                
         */

         
                     public java.lang.String describeAuthenticaitonPolicy(

                        java.lang.String localeId189)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DescribeAuthenticaitonPolicy
                * @param describeAuthenticaitonPolicy188
            
          */
        public void startdescribeAuthenticaitonPolicy(

            java.lang.String localeId189,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop192
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop192
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCancelOrder
                    * @param schedulerCancelOrder195
                
         */

         
                     public void schedulerCancelOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity196,java.lang.String orderNumberXML197)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCancelOrder
                * @param schedulerCancelOrder195
            
          */
        public void startschedulerCancelOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity196,java.lang.String orderNumberXML197,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePosition
                    * @param updateRoutePosition199
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info200)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePosition
                * @param updateRoutePosition199
            
          */
        public void startupdateRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info200,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWindow
                    * @param schedulerSaveDeliveryWindow203
                
         */

         
                     public boolean schedulerSaveDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity204,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options205)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWindow
                * @param schedulerSaveDeliveryWindow203
            
          */
        public void startschedulerSaveDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity204,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options205,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateAdminRouteEx
                    * @param createAdminRouteEx208
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity createAdminRouteEx(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity209,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity210,com.freshdirect.routing.proxy.stub.transportation.CreateAdminRouteOptions options211)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateAdminRouteEx
                * @param createAdminRouteEx208
            
          */
        public void startcreateAdminRouteEx(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity209,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity210,com.freshdirect.routing.proxy.stub.transportation.CreateAdminRouteOptions options211,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateAdminRoute
                    * @param createAdminRoute214
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity createAdminRoute(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity215,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity216)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateAdminRoute
                * @param createAdminRoute214
            
          */
        public void startcreateAdminRoute(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity215,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity216,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                    * @param retrieveRoutingImportOrdersByCriteria219
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] retrieveRoutingImportOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria220,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions221)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                * @param retrieveRoutingImportOrdersByCriteria219
            
          */
        public void startretrieveRoutingImportOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria220,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions221,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SuggestRoute
                    * @param suggestRoute224
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] suggestRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop225,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options226)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SuggestRoute
                * @param suggestRoute224
            
          */
        public void startsuggestRoute(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop225,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options226,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__StartRoute
                    * @param startRoute229
                
         */

         
                     public void startRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info230)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__StartRoute
                * @param startRoute229
            
          */
        public void startstartRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info230,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAccountTypesByCriteria
                    * @param retrieveAccountTypesByCriteria232
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AccountType[] retrieveAccountTypesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.AccountTypeCriteria criteria233)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAccountTypesByCriteria
                * @param retrieveAccountTypesByCriteria232
            
          */
        public void startretrieveAccountTypesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.AccountTypeCriteria criteria233,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                    * @param schedulerExcludeCutoffRoutes236
                
         */

         
                     public void schedulerExcludeCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity237,boolean excludeXML238)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                * @param schedulerExcludeCutoffRoutes236
            
          */
        public void startschedulerExcludeCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity237,boolean excludeXML238,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUnload
                    * @param schedulerUnload240
                
         */

         
                     public void schedulerUnload(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity241)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUnload
                * @param schedulerUnload240
            
          */
        public void startschedulerUnload(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity241,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnassignStop
                    * @param unassignStop243
                
         */

         
                     public void unassignStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop244)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnassignStop
                * @param unassignStop243
            
          */
        public void startunassignStop(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop244,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                    * @param retrieveRouteDailyStatsByCriteria246
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria247,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options248)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                * @param retrieveRouteDailyStatsByCriteria246
            
          */
        public void startretrieveRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria247,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options248,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                    * @param retrievePlanningSessionPropertiesByIdentity251
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession retrievePlanningSessionPropertiesByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity252)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                * @param retrievePlanningSessionPropertiesByIdentity251
            
          */
        public void startretrievePlanningSessionPropertiesByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity252,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePrintTemplatesByCriteria
                    * @param retrievePrintTemplatesByCriteria255
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PrintTemplate[] retrievePrintTemplatesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PrintTemplateCriteria criteria256,com.freshdirect.routing.proxy.stub.transportation.PrintTemplateRetrieveOptions options257)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePrintTemplatesByCriteria
                * @param retrievePrintTemplatesByCriteria255
            
          */
        public void startretrievePrintTemplatesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PrintTemplateCriteria criteria256,com.freshdirect.routing.proxy.stub.transportation.PrintTemplateRetrieveOptions options257,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingDriverDirections
                    * @param buildRoutingDriverDirections260
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildRoutingDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity261)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingDriverDirections
                * @param buildRoutingDriverDirections260
            
          */
        public void startbuildRoutingDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity261,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocations
                    * @param saveLocations264
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations265)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocations
                * @param saveLocations264
            
          */
        public void startsaveLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations265,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSapShipments
                    * @param saveSapShipments268
                
         */

         
                     public boolean saveSapShipments(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid269,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments270)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSapShipments
                * @param saveSapShipments268
            
          */
        public void startsaveSapShipments(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid269,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments270,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionConfig
                    * @param retrieveRegionConfig273
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveRegionConfig(

                        java.lang.String applicationID274,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity275,java.lang.String configGroupID276)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionConfig
                * @param retrieveRegionConfig273
            
          */
        public void startretrieveRegionConfig(

            java.lang.String applicationID274,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity275,java.lang.String configGroupID276,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                    * @param retrieveRoutingLocationsWithOrdersEx279
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity280,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options281)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                * @param retrieveRoutingLocationsWithOrdersEx279
            
          */
        public void startretrieveRoutingLocationsWithOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity280,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options281,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyQuestions
                    * @param retrieveRouteSurveyQuestions284
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveRouteSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity285,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt286)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyQuestions
                * @param retrieveRouteSurveyQuestions284
            
          */
        public void startretrieveRouteSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity285,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt286,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegion
                    * @param saveRegion289
                
         */

         
                     public void saveRegion(

                        com.freshdirect.routing.proxy.stub.transportation.Region region290)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegion
                * @param saveRegion289
            
          */
        public void startsaveRegion(

            com.freshdirect.routing.proxy.stub.transportation.Region region290,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                    * @param schedulerRetrieveRoutesByCriteria292
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute[] schedulerRetrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria293,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options294)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                * @param schedulerRetrieveRoutesByCriteria292
            
          */
        public void startschedulerRetrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria293,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options294,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipment
                    * @param deleteSapShipment297
                
         */

         
                     public boolean deleteSapShipment(

                        com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid298)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipment
                * @param deleteSapShipment297
            
          */
        public void startdeleteSapShipment(

            com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid298,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUnassigned
                    * @param saveUnassigned301
                
         */

         
                     public void saveUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop302,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options303)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUnassigned
                * @param saveUnassigned301
            
          */
        public void startsaveUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop302,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options303,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrders
                    * @param saveRoutingImportOrders305
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrders(

                        java.lang.String regionId306,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders307,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions308)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrders
                * @param saveRoutingImportOrders305
            
          */
        public void startsaveRoutingImportOrders(

            java.lang.String regionId306,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders307,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions308,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                    * @param retrievePositionHistoryBlocksByCriteria311
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryBlocksByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria312)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                * @param retrievePositionHistoryBlocksByCriteria311
            
          */
        public void startretrievePositionHistoryBlocksByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria312,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SequenceStop
                    * @param sequenceStop315
                
         */

         
                     public void sequenceStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info316)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SequenceStop
                * @param sequenceStop315
            
          */
        public void startsequenceStop(

            com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info316,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportByIdentity
                    * @param retrieveReportByIdentity318
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report retrieveReportByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity319)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportByIdentity
                * @param retrieveReportByIdentity318
            
          */
        public void startretrieveReportByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity319,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedData
                    * @param deleteUserDefinedData322
                
         */

         
                     public void deleteUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity323,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input324)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedData
                * @param deleteUserDefinedData322
            
          */
        public void startdeleteUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity323,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input324,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationServiceStatsByCriteria
                    * @param retrieveLocationServiceStatsByCriteria326
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LocationServiceStats[] retrieveLocationServiceStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationServiceStatsCriteria criteria327)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationServiceStatsByCriteria
                * @param retrieveLocationServiceStatsByCriteria326
            
          */
        public void startretrieveLocationServiceStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationServiceStatsCriteria criteria327,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                    * @param retrieveUserDefinedTablesByCriteria330
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable[] retrieveUserDefinedTablesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria331)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                * @param retrieveUserDefinedTablesByCriteria330
            
          */
        public void startretrieveUserDefinedTablesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria331,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBulkReserveOrders
                    * @param schedulerBulkReserveOrders334
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerBulkReserveOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity335,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders336,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options337)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBulkReserveOrders
                * @param schedulerBulkReserveOrders334
            
          */
        public void startschedulerBulkReserveOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity335,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders336,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options337,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                    * @param retrieveDepotSkusAvailabilitiesByCriteria340
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] retrieveDepotSkusAvailabilitiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria341)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                * @param retrieveDepotSkusAvailabilitiesByCriteria340
            
          */
        public void startretrieveDepotSkusAvailabilitiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria341,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                    * @param retrieveRoutingStopsByCriteria344
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria345,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options346)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                * @param retrieveRoutingStopsByCriteria344
            
          */
        public void startretrieveRoutingStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria345,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options346,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                    * @param retrieveRoutingRoutesByCriteria349
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] retrieveRoutingRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria350,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options351)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                * @param retrieveRoutingRoutesByCriteria349
            
          */
        public void startretrieveRoutingRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria350,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options351,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnsubscribeFromNotifications
                    * @param unsubscribeFromNotifications354
                
         */

         
                     public void unsubscribeFromNotifications(

                        java.lang.String unsubscribeToken355,java.lang.String recipientEmail356)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnsubscribeFromNotifications
                * @param unsubscribeFromNotifications354
            
          */
        public void startunsubscribeFromNotifications(

            java.lang.String unsubscribeToken355,java.lang.String recipientEmail356,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRICRegionsByUser
                    * @param retrieveRICRegionsByUser358
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RICRegionsWithPurchaseInfo retrieveRICRegionsByUser(

                        java.lang.String userId359)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRICRegionsByUser
                * @param retrieveRICRegionsByUser358
            
          */
        public void startretrieveRICRegionsByUser(

            java.lang.String userId359,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MassStopSequence
                    * @param massStopSequence362
                
         */

         
                     public void massStopSequence(

                        com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info363)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MassStopSequence
                * @param massStopSequence362
            
          */
        public void startmassStopSequence(

            com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info363,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                    * @param retrieveDefaultPlanningSessionProperties365
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties retrieveDefaultPlanningSessionProperties(

                        java.lang.String regionId366)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                * @param retrieveDefaultPlanningSessionProperties365
            
          */
        public void startretrieveDefaultPlanningSessionProperties(

            java.lang.String regionId366,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                    * @param schedulerRetrieveOrderByIdentity369
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder schedulerRetrieveOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity370,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options371)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                * @param schedulerRetrieveOrderByIdentity369
            
          */
        public void startschedulerRetrieveOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity370,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options371,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                    * @param schedulerCalculateDeliveryWindowMetrics374
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics[] schedulerCalculateDeliveryWindowMetrics(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity375,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options376)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                * @param schedulerCalculateDeliveryWindowMetrics374
            
          */
        public void startschedulerCalculateDeliveryWindowMetrics(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity375,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options376,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutesEx
                    * @param schedulerRebuildRoutesEx379
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutesEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity380,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options381)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutesEx
                * @param schedulerRebuildRoutesEx379
            
          */
        public void startschedulerRebuildRoutesEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity380,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options381,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                    * @param retrievePlanningSessionPropertiesByCriteria384
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession[] retrievePlanningSessionPropertiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria385)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                * @param retrievePlanningSessionPropertiesByCriteria384
            
          */
        public void startretrievePlanningSessionPropertiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria385,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveDepotSkusAvailabilities
                    * @param saveDepotSkusAvailabilities388
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] saveDepotSkusAvailabilities(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus389)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveDepotSkusAvailabilities
                * @param saveDepotSkusAvailabilities388
            
          */
        public void startsaveDepotSkusAvailabilities(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus389,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationServiceHistory
                    * @param retrieveLocationServiceHistory392
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LocationServiceDetails[] retrieveLocationServiceHistory(

                        com.freshdirect.routing.proxy.stub.transportation.RetrieveLocationServiceHistoryOptions options393)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationServiceHistory
                * @param retrieveLocationServiceHistory392
            
          */
        public void startretrieveLocationServiceHistory(

            com.freshdirect.routing.proxy.stub.transportation.RetrieveLocationServiceHistoryOptions options393,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartStop
                    * @param departStop396
                
         */

         
                     public void departStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info397)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartStop
                * @param departStop396
            
          */
        public void startdepartStop(

            com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info397,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEmployee
                    * @param saveEmployee399
                
         */

         
                     public void saveEmployee(

                        com.freshdirect.routing.proxy.stub.transportation.Employee employee400)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEmployee
                * @param saveEmployee399
            
          */
        public void startsaveEmployee(

            com.freshdirect.routing.proxy.stub.transportation.Employee employee400,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStop
                    * @param saveStop402
                
         */

         
                     public void saveStop(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop403,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions404,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options405)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStop
                * @param saveStop402
            
          */
        public void startsaveStop(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop403,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions404,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options405,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                    * @param retrieveAutoArriveDepartOptions407
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AutoArriveDepartOptions retrieveAutoArriveDepartOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity408)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                * @param retrieveAutoArriveDepartOptions407
            
          */
        public void startretrieveAutoArriveDepartOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity408,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AuthenticateUser
                    * @param authenticateUser411
                
         */

         
                     public int authenticateUser(

                        java.lang.String userID412,java.lang.String password413)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AuthenticateUser
                * @param authenticateUser411
            
          */
        public void startauthenticateUser(

            java.lang.String userID412,java.lang.String password413,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrdersEx
                    * @param saveRoutingImportOrdersEx416
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrdersEx(

                        java.lang.String regionId417,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders418,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions419,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions420)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrdersEx
                * @param saveRoutingImportOrdersEx416
            
          */
        public void startsaveRoutingImportOrdersEx(

            java.lang.String regionId417,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders418,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions419,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions420,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                    * @param retrieveStopCancelCodesByCriteria423
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopCancelCode[] retrieveStopCancelCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria424)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                * @param retrieveStopCancelCodesByCriteria423
            
          */
        public void startretrieveStopCancelCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria424,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    