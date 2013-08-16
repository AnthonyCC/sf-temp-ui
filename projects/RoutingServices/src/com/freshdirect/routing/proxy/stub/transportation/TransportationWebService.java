

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
                    * @param retrieveUserDefinedColumnByIdentity357
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn retrieveUserDefinedColumnByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity358)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnByIdentity
                * @param retrieveUserDefinedColumnByIdentity357
            
          */
        public void startretrieveUserDefinedColumnByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity358,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                    * @param buildDispatchDriverDirectionsEx361
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirectionsEx(

                        com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info362)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                * @param buildDispatchDriverDirectionsEx361
            
          */
        public void startbuildDispatchDriverDirectionsEx(

            com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info362,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyResults
                    * @param retrieveStopSurveyResults365
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity366)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyResults
                * @param retrieveStopSurveyResults365
            
          */
        public void startretrieveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity366,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                    * @param schedulerRetrieveFeederRoutes369
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] schedulerRetrieveFeederRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity370,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options371)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                * @param schedulerRetrieveFeederRoutes369
            
          */
        public void startschedulerRetrieveFeederRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity370,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options371,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveActiveAlertRecipientsByLocationIdentity
                    * @param retrieveActiveAlertRecipientsByLocationIdentity374
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] retrieveActiveAlertRecipientsByLocationIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity375)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveActiveAlertRecipientsByLocationIdentity
                * @param retrieveActiveAlertRecipientsByLocationIdentity374
            
          */
        public void startretrieveActiveAlertRecipientsByLocationIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity375,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignDrivers
                    * @param assignDrivers378
                
         */

         
                     public void assignDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity379,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers380)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignDrivers
                * @param assignDrivers378
            
          */
        public void startassignDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity379,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers380,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopByIdentity
                    * @param retrieveStopByIdentity382
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop retrieveStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity383,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options384)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopByIdentity
                * @param retrieveStopByIdentity382
            
          */
        public void startretrieveStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity383,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options384,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeesByCriteria
                    * @param retrieveEmployeesByCriteria387
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria388)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeesByCriteria
                * @param retrieveEmployeesByCriteria387
            
          */
        public void startretrieveEmployeesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria388,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                    * @param retrieveRoutingRouteByIdentity391
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute retrieveRoutingRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity392,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options393)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                * @param retrieveRoutingRouteByIdentity391
            
          */
        public void startretrieveRoutingRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity392,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options393,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                    * @param retrieveRoutingSessionByIdentity396
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession retrieveRoutingSessionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity397,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options398)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                * @param retrieveRoutingSessionByIdentity396
            
          */
        public void startretrieveRoutingSessionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity397,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options398,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                    * @param schedulerRetrieveOrdersByCriteria401
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerRetrieveOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity402,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria403,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options404)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                * @param schedulerRetrieveOrdersByCriteria401
            
          */
        public void startschedulerRetrieveOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity402,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria403,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options404,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                    * @param schedulerDeleteDeliveryWindow407
                
         */

         
                     public void schedulerDeleteDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity408,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window409,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options410)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                * @param schedulerDeleteDeliveryWindow407
            
          */
        public void startschedulerDeleteDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity408,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window409,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options410,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSurveyResults
                    * @param saveStopSurveyResults412
                
         */

         
                     public void saveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity413,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults414)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSurveyResults
                * @param saveStopSurveyResults412
            
          */
        public void startsaveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity413,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults414,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveStandardRoute
                    * @param removeStandardRoute416
                
         */

         
                     public void removeStandardRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity417,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRemoveOptions options418)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveStandardRoute
                * @param removeStandardRoute416
            
          */
        public void startremoveStandardRoute(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity417,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRemoveOptions options418,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerMovableOrders
                    * @param schedulerMovableOrders420
                
         */

         
                     public void schedulerMovableOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity421,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria422,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options423)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerMovableOrders
                * @param schedulerMovableOrders420
            
          */
        public void startschedulerMovableOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity421,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria422,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options423,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                    * @param schedulerRetrieveDeliveryWaveInstancesByCriteria425
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance[] schedulerRetrieveDeliveryWaveInstancesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity426,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria427,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options428)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                * @param schedulerRetrieveDeliveryWaveInstancesByCriteria425
            
          */
        public void startschedulerRetrieveDeliveryWaveInstancesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity426,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria427,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options428,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CancelStop
                    * @param cancelStop431
                
         */

         
                     public void cancelStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info432)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CancelStop
                * @param cancelStop431
            
          */
        public void startcancelStop(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info432,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryByCriteria
                    * @param retrievePositionHistoryByCriteria434
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria435)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryByCriteria
                * @param retrievePositionHistoryByCriteria434
            
          */
        public void startretrievePositionHistoryByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria435,

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
          * Service definition of function ns1__UnlockNotifications
                    * @param unlockNotifications441
                
         */

         
                     public void unlockNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria442)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnlockNotifications
                * @param unlockNotifications441
            
          */
        public void startunlockNotifications(

            com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria442,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerPurge
                    * @param schedulerPurge444
                
         */

         
                     public void schedulerPurge(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity445,boolean reloadXML446)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerPurge
                * @param schedulerPurge444
            
          */
        public void startschedulerPurge(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity445,boolean reloadXML446,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteNotifications
                    * @param deleteNotifications448
                
         */

         
                     public void deleteNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities449)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteNotifications
                * @param deleteNotifications448
            
          */
        public void startdeleteNotifications(

            com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities449,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateDeliveryDetails
                    * @param updateDeliveryDetails451
                
         */

         
                     public void updateDeliveryDetails(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info452)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateDeliveryDetails
                * @param updateDeliveryDetails451
            
          */
        public void startupdateDeliveryDetails(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info452,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrders
                    * @param schedulerOptimizeOrders454
                
         */

         
                     public void schedulerOptimizeOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity455)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrders
                * @param schedulerOptimizeOrders454
            
          */
        public void startschedulerOptimizeOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity455,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                    * @param retrieveDutyPeriodsByCriteria457
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] retrieveDutyPeriodsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria458,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options459)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                * @param retrieveDutyPeriodsByCriteria457
            
          */
        public void startretrieveDutyPeriodsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria458,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options459,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                    * @param retrieveRoutingRouteDailyStatsByCriteria462
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRoutingRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria463,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options464)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                * @param retrieveRoutingRouteDailyStatsByCriteria462
            
          */
        public void startretrieveRoutingRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria463,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options464,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSignature
                    * @param retrieveStopSignature467
                
         */

         
                     public javax.activation.DataHandler retrieveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity468,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType469)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSignature
                * @param retrieveStopSignature467
            
          */
        public void startretrieveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity468,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType469,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGlobalConfig
                    * @param retrieveGlobalConfig472
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveGlobalConfig(

                        java.lang.String applicationID473,java.lang.String configGroupID474)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGlobalConfig
                * @param retrieveGlobalConfig472
            
          */
        public void startretrieveGlobalConfig(

            java.lang.String applicationID473,java.lang.String configGroupID474,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionByIdentity
                    * @param retrieveRegionByIdentity477
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region retrieveRegionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity478)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionByIdentity
                * @param retrieveRegionByIdentity477
            
          */
        public void startretrieveRegionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity478,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerLoad
                    * @param schedulerLoad481
                
         */

         
                     public void schedulerLoad(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity482)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerLoad
                * @param schedulerLoad481
            
          */
        public void startschedulerLoad(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity482,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteReport
                    * @param deleteReport484
                
         */

         
                     public void deleteReport(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity485)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteReport
                * @param deleteReport484
            
          */
        public void startdeleteReport(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity485,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyQuestions
                    * @param retrieveStopSurveyQuestions487
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveStopSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity488)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyQuestions
                * @param retrieveStopSurveyQuestions487
            
          */
        public void startretrieveStopSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity488,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedDrivers
                    * @param retrieveAssignedDrivers491
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] retrieveAssignedDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity492)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedDrivers
                * @param retrieveAssignedDrivers491
            
          */
        public void startretrieveAssignedDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity492,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStandardRouteSets
                    * @param saveStandardRouteSets495
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRejection[] saveStandardRouteSets(

                        java.lang.String regionID496,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] standardRouteSets497,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetSaveOptions options498)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStandardRouteSets
                * @param saveStandardRouteSets495
            
          */
        public void startsaveStandardRouteSets(

            java.lang.String regionID496,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] standardRouteSets497,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetSaveOptions options498,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ChangeUserPassword
                    * @param changeUserPassword501
                
         */

         
                     public void changeUserPassword(

                        java.lang.String userID502,java.lang.String oldPassword503,java.lang.String newPassword504)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ChangeUserPassword
                * @param changeUserPassword501
            
          */
        public void startchangeUserPassword(

            java.lang.String userID502,java.lang.String oldPassword503,java.lang.String newPassword504,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                    * @param deleteSapShipmentsBySessionIdentity506
                
         */

         
                     public boolean deleteSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid507)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                * @param deleteSapShipmentsBySessionIdentity506
            
          */
        public void startdeleteSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid507,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                    * @param retrievePlanningUserDefinedFieldInfo510
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedFieldInfo[] retrievePlanningUserDefinedFieldInfo(

                        java.lang.String regionId511)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                * @param retrievePlanningUserDefinedFieldInfo510
            
          */
        public void startretrievePlanningUserDefinedFieldInfo(

            java.lang.String regionId511,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                    * @param retrieveUserDefinedColumnsByCriteria514
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn[] retrieveUserDefinedColumnsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria515)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                * @param retrieveUserDefinedColumnsByCriteria514
            
          */
        public void startretrieveUserDefinedColumnsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria515,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CalculateTimeDist
                    * @param calculateTimeDist518
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TimeDistResult calculateTimeDist(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity519,int fromLatitude520,int fromLongitude521,int toLatitude522,int toLongitude523)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CalculateTimeDist
                * @param calculateTimeDist518
            
          */
        public void startcalculateTimeDist(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity519,int fromLatitude520,int fromLongitude521,int toLatitude522,int toLongitude523,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegionConfig
                    * @param saveRegionConfig526
                
         */

         
                     public void saveRegionConfig(

                        java.lang.String applicationID527,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity528,java.lang.String configGroupID529,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items530)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegionConfig
                * @param saveRegionConfig526
            
          */
        public void startsaveRegionConfig(

            java.lang.String applicationID527,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity528,java.lang.String configGroupID529,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items530,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                    * @param retrieveStopNotesByCriteriaEx532
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopNote[] retrieveStopNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria533,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options534)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                * @param retrieveStopNotesByCriteriaEx532
            
          */
        public void startretrieveStopNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria533,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options534,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteForDevice
                    * @param retrieveRouteForDevice537
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteForDevice(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity538,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options539,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity540)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteForDevice
                * @param retrieveRouteForDevice537
            
          */
        public void startretrieveRouteForDevice(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity538,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options539,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity540,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionETAs
                    * @param updateRoutePositionETAs543
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionETAs(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId544,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos545)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionETAs
                * @param updateRoutePositionETAs543
            
          */
        public void startupdateRoutePositionETAs(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId544,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos545,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SendTextMessageToDriver
                    * @param sendTextMessageToDriver548
                
         */

         
                     public void sendTextMessageToDriver(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity549,java.lang.String message550,java.lang.String fromUserID551)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SendTextMessageToDriver
                * @param sendTextMessageToDriver548
            
          */
        public void startsendTextMessageToDriver(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity549,java.lang.String message550,java.lang.String fromUserID551,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedTable
                    * @param deleteUserDefinedTable553
                
         */

         
                     public void deleteUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId554)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedTable
                * @param deleteUserDefinedTable553
            
          */
        public void startdeleteUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId554,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSkus
                    * @param saveSkus556
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] saveSkus(

                        com.freshdirect.routing.proxy.stub.transportation.Sku[] skus557)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSkus
                * @param saveSkus556
            
          */
        public void startsaveSkus(

            com.freshdirect.routing.proxy.stub.transportation.Sku[] skus557,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                    * @param retrieveSapShipmentsBySessionIdentity560
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SapShipment[] retrieveSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid561)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                * @param retrieveSapShipmentsBySessionIdentity560
            
          */
        public void startretrieveSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid561,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExtendOrderReservation
                    * @param schedulerExtendOrderReservation564
                
         */

         
                     public void schedulerExtendOrderReservation(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity565,java.lang.String orderNumberXML566,int extendMinutes567)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExtendOrderReservation
                * @param schedulerExtendOrderReservation564
            
          */
        public void startschedulerExtendOrderReservation(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity565,java.lang.String orderNumberXML566,int extendMinutes567,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionOptions
                    * @param retrieveRegionOptions569
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RegionOptions retrieveRegionOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity570)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionOptions
                * @param retrieveRegionOptions569
            
          */
        public void startretrieveRegionOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity570,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteSetByIdentity
                    * @param retrieveStandardRouteSetByIdentity573
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet retrieveStandardRouteSetByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity574,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options575)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteSetByIdentity
                * @param retrieveStandardRouteSetByIdentity573
            
          */
        public void startretrieveStandardRouteSetByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity574,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options575,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSignature
                    * @param saveStopSignature578
                
         */

         
                     public void saveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity579,javax.activation.DataHandler signatureData580)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSignature
                * @param saveStopSignature578
            
          */
        public void startsaveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity579,javax.activation.DataHandler signatureData580,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                    * @param retrieveUndeliverableStopCodesByCriteria582
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCode[] retrieveUndeliverableStopCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria583)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                * @param retrieveUndeliverableStopCodesByCriteria582
            
          */
        public void startretrieveUndeliverableStopCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria583,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteRoutingSession
                    * @param deleteRoutingSession586
                
         */

         
                     public void deleteRoutingSession(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity587)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteRoutingSession
                * @param deleteRoutingSession586
            
          */
        public void startdeleteRoutingSession(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity587,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                    * @param schedulerIsExcludingCutoffRoutes589
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult schedulerIsExcludingCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity590)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                * @param schedulerIsExcludingCutoffRoutes589
            
          */
        public void startschedulerIsExcludingCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity590,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                    * @param retrieveRoutingImportOrderByIdentity593
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder retrieveRoutingImportOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity594,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions595)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                * @param retrieveRoutingImportOrderByIdentity593
            
          */
        public void startretrieveRoutingImportOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity594,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions595,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkuByIdentity
                    * @param retrieveSkuByIdentity598
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku retrieveSkuByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity599)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkuByIdentity
                * @param retrieveSkuByIdentity598
            
          */
        public void startretrieveSkuByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity599,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                    * @param retrieveQuantityReasonCodesByCriteria602
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCode[] retrieveQuantityReasonCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria603)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                * @param retrieveQuantityReasonCodesByCriteria602
            
          */
        public void startretrieveQuantityReasonCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria603,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                    * @param retrieveLineItemNotesByCriteriaEx606
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LineItemNote[] retrieveLineItemNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria607,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options608)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                * @param retrieveLineItemNotesByCriteriaEx606
            
          */
        public void startretrieveLineItemNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria607,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options608,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteByIdentity
                    * @param retrieveStandardRouteByIdentity611
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRoute retrieveStandardRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity612,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options613)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteByIdentity
                * @param retrieveStandardRouteByIdentity611
            
          */
        public void startretrieveStandardRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteIdentity identity612,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options613,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                    * @param retrieveOrderNotesByCriteriaEx616
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.OrderNote[] retrieveOrderNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria617,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options618)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                * @param retrieveOrderNotesByCriteriaEx616
            
          */
        public void startretrieveOrderNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria617,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options618,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                    * @param retrieveEquipmentTypeByIdentity621
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType retrieveEquipmentTypeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity622,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options623)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                * @param retrieveEquipmentTypeByIdentity621
            
          */
        public void startretrieveEquipmentTypeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity622,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options623,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUnassigned
                    * @param deleteUnassigned626
                
         */

         
                     public void deleteUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop627)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUnassigned
                * @param deleteUnassigned626
            
          */
        public void startdeleteUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop627,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                    * @param retrieveRoutingSourcedOrdersByCriteria629
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] retrieveRoutingSourcedOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria630,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options631)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                * @param retrieveRoutingSourcedOrdersByCriteria629
            
          */
        public void startretrieveRoutingSourcedOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria630,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options631,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserConfig
                    * @param saveUserConfig634
                
         */

         
                     public void saveUserConfig(

                        java.lang.String applicationID635,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity636,java.lang.String configGroupID637,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items638)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserConfig
                * @param saveUserConfig634
            
          */
        public void startsaveUserConfig(

            java.lang.String applicationID635,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity636,java.lang.String configGroupID637,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items638,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeByIdentity
                    * @param retrieveEmployeeByIdentity640
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity641)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeByIdentity
                * @param retrieveEmployeeByIdentity640
            
          */
        public void startretrieveEmployeeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity641,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUpdateOrder
                    * @param schedulerUpdateOrder644
                
         */

         
                     public boolean schedulerUpdateOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity645,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity646,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options647)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUpdateOrder
                * @param schedulerUpdateOrder644
            
          */
        public void startschedulerUpdateOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity645,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity646,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options647,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SurveyResponse
                    * @param surveyResponse650
                
         */

         
                     public void surveyResponse(

                        com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info651)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SurveyResponse
                * @param surveyResponse650
            
          */
        public void startsurveyResponse(

            com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info651,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RedeliverStop
                    * @param redeliverStop653
                
         */

         
                     public void redeliverStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info654)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RedeliverStop
                * @param redeliverStop653
            
          */
        public void startredeliverStop(

            com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info654,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRoutesByCriteria
                    * @param retrieveStandardRoutesByCriteria656
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] retrieveStandardRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteCriteria criteria657,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options658)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRoutesByCriteria
                * @param retrieveStandardRoutesByCriteria656
            
          */
        public void startretrieveStandardRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteCriteria criteria657,com.freshdirect.routing.proxy.stub.transportation.StandardRouteRetrieveOptions options658,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionsByCriteria
                    * @param retrieveRegionsByCriteria661
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region[] retrieveRegionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria662)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionsByCriteria
                * @param retrieveRegionsByCriteria661
            
          */
        public void startretrieveRegionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria662,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveStandardRouteSet
                    * @param removeStandardRouteSet665
                
         */

         
                     public void removeStandardRouteSet(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity666,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRemoveOptions options667)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveStandardRouteSet
                * @param removeStandardRouteSet665
            
          */
        public void startremoveStandardRouteSet(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetIdentity identity666,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRemoveOptions options667,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyResults
                    * @param retrieveRouteSurveyResults669
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity670,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt671)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyResults
                * @param retrieveRouteSurveyResults669
            
          */
        public void startretrieveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity670,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt671,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveActiveAlertRecipientTypes
                    * @param retrieveActiveAlertRecipientTypes674
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipientType[] retrieveActiveAlertRecipientTypes(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveActiveAlertRecipientTypes
                * @param retrieveActiveAlertRecipientTypes674
            
          */
        public void startretrieveActiveAlertRecipientTypes(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByCriteria
                    * @param retrieveEquipmentByCriteria677
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment[] retrieveEquipmentByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria678,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options679)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByCriteria
                * @param retrieveEquipmentByCriteria677
            
          */
        public void startretrieveEquipmentByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria678,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options679,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartOrigin
                    * @param departOrigin682
                
         */

         
                     public void departOrigin(

                        com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info683)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartOrigin
                * @param departOrigin682
            
          */
        public void startdepartOrigin(

            com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info683,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateRoutingSession
                    * @param createRoutingSession685
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity createRoutingSession(

                        java.lang.String regionId686,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties687)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateRoutingSession
                * @param createRoutingSession685
            
          */
        public void startcreateRoutingSession(

            java.lang.String regionId686,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties687,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                    * @param retrieveDefaultRoutingSessionProperties690
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties retrieveDefaultRoutingSessionProperties(

                        java.lang.String regionId691,java.util.Date sessionDate692)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                * @param retrieveDefaultRoutingSessionProperties690
            
          */
        public void startretrieveDefaultRoutingSessionProperties(

            java.lang.String regionId691,java.util.Date sessionDate692,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStationaryRoutePosition
                    * @param updateStationaryRoutePosition695
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateStationaryRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.StationaryRoutePositionInfo info696)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStationaryRoutePosition
                * @param updateStationaryRoutePosition695
            
          */
        public void startupdateStationaryRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.StationaryRoutePositionInfo info696,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerAnalyzeOrder
                    * @param schedulerAnalyzeOrder699
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] schedulerAnalyzeOrder(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order700,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options701)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerAnalyzeOrder
                * @param schedulerAnalyzeOrder699
            
          */
        public void startschedulerAnalyzeOrder(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order700,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options701,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                    * @param retrieveDutyPeriodByIdentity704
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod retrieveDutyPeriodByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity705,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options706)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                * @param retrieveDutyPeriodByIdentity704
            
          */
        public void startretrieveDutyPeriodByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity705,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options706,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopsByCriteria
                    * @param retrieveStopsByCriteria709
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria710,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options711)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopsByCriteria
                * @param retrieveStopsByCriteria709
            
          */
        public void startretrieveStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria710,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options711,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                    * @param retrieveDepotSkuAvailabilityByIdentity714
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability retrieveDepotSkuAvailabilityByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity715)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                * @param retrieveDepotSkuAvailabilityByIdentity714
            
          */
        public void startretrieveDepotSkuAvailabilityByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity715,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingRouteNetMatrix
                    * @param buildRoutingRouteNetMatrix718
                
         */

         
                     public void buildRoutingRouteNetMatrix(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity719)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingRouteNetMatrix
                * @param buildRoutingRouteNetMatrix718
            
          */
        public void startbuildRoutingRouteNetMatrix(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity719,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                    * @param retrieveRoutingSessionsByCriteria721
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] retrieveRoutingSessionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria722,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options723)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                * @param retrieveRoutingSessionsByCriteria721
            
          */
        public void startretrieveRoutingSessionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria722,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options723,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveActiveAlertRecipients
                    * @param saveActiveAlertRecipients726
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] saveActiveAlertRecipients(

                        com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] activeAlertRecipients727)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveActiveAlertRecipients
                * @param saveActiveAlertRecipients726
            
          */
        public void startsaveActiveAlertRecipients(

            com.freshdirect.routing.proxy.stub.transportation.ActiveAlertRecipient[] activeAlertRecipients727,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamps
                    * @param convertTimestamps730
                
         */

         
                     public java.util.Calendar[] convertTimestamps(

                        java.util.Calendar[] sourceTimestamps731,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions732,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions733)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamps
                * @param convertTimestamps730
            
          */
        public void startconvertTimestamps(

            java.util.Calendar[] sourceTimestamps731,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions732,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions733,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByCriteria
                    * @param retrieveNotificationsByCriteria736
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria737,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions738,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options739)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByCriteria
                * @param retrieveNotificationsByCriteria736
            
          */
        public void startretrieveNotificationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria737,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions738,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options739,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReserveOrder
                    * @param schedulerReserveOrder742
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ReserveResult schedulerReserveOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity743,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder744,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow745,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options746)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReserveOrder
                * @param schedulerReserveOrder742
            
          */
        public void startschedulerReserveOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity743,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder744,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow745,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options746,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteSurveyResults
                    * @param saveRouteSurveyResults749
                
         */

         
                     public void saveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity750,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt751,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults752)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteSurveyResults
                * @param saveRouteSurveyResults749
            
          */
        public void startsaveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity750,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt751,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults752,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                    * @param retrieveRoutingUnassignsByCriteria754
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria755,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options756)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                * @param retrieveRoutingUnassignsByCriteria754
            
          */
        public void startretrieveRoutingUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria755,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options756,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__PlaceUnassigned
                    * @param placeUnassigned759
                
         */

         
                     public void placeUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop760,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity761,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition762,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime763,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions764)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__PlaceUnassigned
                * @param placeUnassigned759
            
          */
        public void startplaceUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop760,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity761,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition762,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime763,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions764,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateTelematicsCachePositions
                    * @param updateTelematicsCachePositions766
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions767)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateTelematicsCachePositions
                * @param updateTelematicsCachePositions766
            
          */
        public void startupdateTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions767,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                    * @param retrievePlanningLocationExtensionsByCriteria770
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] retrievePlanningLocationExtensionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria771,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options772)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                * @param retrievePlanningLocationExtensionsByCriteria770
            
          */
        public void startretrievePlanningLocationExtensionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria771,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options772,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BulkArriveDepartStop
                    * @param bulkArriveDepartStop775
                
         */

         
                     public void bulkArriveDepartStop(

                        com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos776)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BulkArriveDepartStop
                * @param bulkArriveDepartStop775
            
          */
        public void startbulkArriveDepartStop(

            com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos776,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SavePlanningLocationExtensions
                    * @param savePlanningLocationExtensions778
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] savePlanningLocationExtensions(

                        java.lang.String regionId779,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions780)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SavePlanningLocationExtensions
                * @param savePlanningLocationExtensions778
            
          */
        public void startsavePlanningLocationExtensions(

            java.lang.String regionId779,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions780,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CleanupTelematicsCachePositions
                    * @param cleanupTelematicsCachePositions783
                
         */

         
                     public void cleanupTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria784)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CleanupTelematicsCachePositions
                * @param cleanupTelematicsCachePositions783
            
          */
        public void startcleanupTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria784,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStandardRoutes
                    * @param saveStandardRoutes786
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteRejection[] saveStandardRoutes(

                        java.lang.String regionID787,com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] standardRoutes788,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSaveOptions options789)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStandardRoutes
                * @param saveStandardRoutes786
            
          */
        public void startsaveStandardRoutes(

            java.lang.String regionID787,com.freshdirect.routing.proxy.stub.transportation.StandardRoute[] standardRoutes788,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSaveOptions options789,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutesByCriteria
                    * @param retrieveRoutesByCriteria792
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route[] retrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria793,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options794)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutesByCriteria
                * @param retrieveRoutesByCriteria792
            
          */
        public void startretrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria793,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options794,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                    * @param schedulerRetrieveRouteByIdentity797
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute schedulerRetrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity798,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options799)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                * @param schedulerRetrieveRouteByIdentity797
            
          */
        public void startschedulerRetrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity798,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options799,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRouteETAs
                    * @param updateRouteETAs802
                
         */

         
                     public void updateRouteETAs(

                        com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info803)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRouteETAs
                * @param updateRouteETAs802
            
          */
        public void startupdateRouteETAs(

            com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info803,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopEx
                    * @param saveStopEx805
                
         */

         
                     public void saveStopEx(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop806,com.freshdirect.routing.proxy.stub.transportation.SaveStopExOptions options807)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopEx
                * @param saveStopEx805
            
          */
        public void startsaveStopEx(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop806,com.freshdirect.routing.proxy.stub.transportation.SaveStopExOptions options807,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ReturnFault
                    * @param returnFault809
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Fault returnFault(

                        int requestedFaultCode810)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ReturnFault
                * @param returnFault809
            
          */
        public void startreturnFault(

            int requestedFaultCode810,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeRouteStatsByCriteria
                    * @param retrieveEmployeeRouteStatsByCriteria813
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStats[] retrieveEmployeeRouteStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStatsCriteria criteria814)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeRouteStatsByCriteria
                * @param retrieveEmployeeRouteStatsByCriteria813
            
          */
        public void startretrieveEmployeeRouteStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStatsCriteria criteria814,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveyDetails
                    * @param retrieveSurveyDetails817
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyDetails[] retrieveSurveyDetails(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity818)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveyDetails
                * @param retrieveSurveyDetails817
            
          */
        public void startretrieveSurveyDetails(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity818,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AddRICUser
                    * @param addRICUser821
                
         */

         
                     public void addRICUser(

                        com.freshdirect.routing.proxy.stub.transportation.User user822)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AddRICUser
                * @param addRICUser821
            
          */
        public void startaddRICUser(

            com.freshdirect.routing.proxy.stub.transportation.User user822,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveProductsPurchased
                    * @param retrieveProductsPurchased824
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased retrieveProductsPurchased(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity825)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveProductsPurchased
                * @param retrieveProductsPurchased824
            
          */
        public void startretrieveProductsPurchased(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity825,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePermissionsForUser
                    * @param retrievePermissionsForUser828
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserPermissions retrievePermissionsForUser(

                        java.lang.String userID829,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity830)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePermissionsForUser
                * @param retrievePermissionsForUser828
            
          */
        public void startretrievePermissionsForUser(

            java.lang.String userID829,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity830,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocations
                    * @param deleteLocations833
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] deleteLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations834)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocations
                * @param deleteLocations833
            
          */
        public void startdeleteLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations834,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedEquipment
                    * @param retrieveAssignedEquipment837
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] retrieveAssignedEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity838)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedEquipment
                * @param retrieveAssignedEquipment837
            
          */
        public void startretrieveAssignedEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity838,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteReportedDistances
                    * @param saveRouteReportedDistances841
                
         */

         
                     public void saveRouteReportedDistances(

                        com.freshdirect.routing.proxy.stub.transportation.RouteReportedDistance[] reportedDistances842)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteReportedDistances
                * @param saveRouteReportedDistances841
            
          */
        public void startsaveRouteReportedDistances(

            com.freshdirect.routing.proxy.stub.transportation.RouteReportedDistance[] reportedDistances842,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopByIdentity
                    * @param retrieveRoutingStopByIdentity844
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop retrieveRoutingStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity845,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options846)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopByIdentity
                * @param retrieveRoutingStopByIdentity844
            
          */
        public void startretrieveRoutingStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity845,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options846,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation849
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation849
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirections
                    * @param buildDispatchDriverDirections852
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity853)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirections
                * @param buildDispatchDriverDirections852
            
          */
        public void startbuildDispatchDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity853,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                    * @param retrieveTelematicsCachePositionsByCriteria856
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] retrieveTelematicsCachePositionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria857)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                * @param retrieveTelematicsCachePositionsByCriteria856
            
          */
        public void startretrieveTelematicsCachePositionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria857,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStopSignature
                    * @param updateStopSignature860
                
         */

         
                     public void updateStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info861)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStopSignature
                * @param updateStopSignature860
            
          */
        public void startupdateStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info861,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                    * @param schedulerSaveDeliveryWaveInstance863
                
         */

         
                     public java.lang.String[] schedulerSaveDeliveryWaveInstance(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity864,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity865,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes866,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options867)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                * @param schedulerSaveDeliveryWaveInstance863
            
          */
        public void startschedulerSaveDeliveryWaveInstance(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity864,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity865,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes866,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options867,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AllowAdditionOfRICUsers
                    * @param allowAdditionOfRICUsers870
                
         */

         
                     public boolean allowAdditionOfRICUsers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AllowAdditionOfRICUsers
                * @param allowAdditionOfRICUsers870
            
          */
        public void startallowAdditionOfRICUsers(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGPSProviderOptions
                    * @param retrieveGPSProviderOptions873
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.GPSProviderOptions retrieveGPSProviderOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity874)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGPSProviderOptions
                * @param retrieveGPSProviderOptions873
            
          */
        public void startretrieveGPSProviderOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity874,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                    * @param retrieveRoutingLocationsWithOrders877
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrders(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity878)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                * @param retrieveRoutingLocationsWithOrders877
            
          */
        public void startretrieveRoutingLocationsWithOrders(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity878,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteria
                    * @param retrieveRouteNotesByCriteria881
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria882)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteria
                * @param retrieveRouteNotesByCriteria881
            
          */
        public void startretrieveRouteNotesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria882,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveReport
                    * @param saveReport885
                
         */

         
                     public void saveReport(

                        com.freshdirect.routing.proxy.stub.transportation.Report report886)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveReport
                * @param saveReport885
            
          */
        public void startsaveReport(

            com.freshdirect.routing.proxy.stub.transportation.Report report886,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveys
                    * @param retrieveSurveys888
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Survey[] retrieveSurveys(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity889)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveys
                * @param retrieveSurveys888
            
          */
        public void startretrieveSurveys(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity889,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                    * @param retrieveNotificationsByRecipientIdentity892
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByRecipientIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity893,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions894,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options895)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                * @param retrieveNotificationsByRecipientIdentity892
            
          */
        public void startretrieveNotificationsByRecipientIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity893,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions894,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options895,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBalanceRoutes
                    * @param schedulerBalanceRoutes898
                
         */

         
                     public void schedulerBalanceRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity899,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options900)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBalanceRoutes
                * @param schedulerBalanceRoutes898
            
          */
        public void startschedulerBalanceRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity899,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options900,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                    * @param retrieveUserDefinedDataByCriteria902
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] retrieveUserDefinedDataByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity903,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria904,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions905)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                * @param retrieveUserDefinedDataByCriteria902
            
          */
        public void startretrieveUserDefinedDataByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity903,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria904,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions905,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkusByCriteria
                    * @param retrieveSkusByCriteria908
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] retrieveSkusByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria909)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkusByCriteria
                * @param retrieveSkusByCriteria908
            
          */
        public void startretrieveSkusByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria909,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                    * @param retrieveCannedTextMessagesByCriteria912
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] retrieveCannedTextMessagesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria913)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                * @param retrieveCannedTextMessagesByCriteria912
            
          */
        public void startretrieveCannedTextMessagesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria913,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                    * @param retrieveUserDefinedDataByIdentity916
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData retrieveUserDefinedDataByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity917,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity918,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions919)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                * @param retrieveUserDefinedDataByIdentity916
            
          */
        public void startretrieveUserDefinedDataByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity917,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity918,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions919,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                    * @param retrieveEquipmentTypeByCriteria922
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] retrieveEquipmentTypeByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria923,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options924)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                * @param retrieveEquipmentTypeByCriteria922
            
          */
        public void startretrieveEquipmentTypeByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria923,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options924,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                    * @param retrieveRoutingOrderByIdentity927
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder retrieveRoutingOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity928,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options929)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                * @param retrieveRoutingOrderByIdentity927
            
          */
        public void startretrieveRoutingOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity928,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options929,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__TextMessage
                    * @param textMessage932
                
         */

         
                     public void textMessage(

                        com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info933)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__TextMessage
                * @param textMessage932
            
          */
        public void starttextMessage(

            com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info933,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CompleteRoute
                    * @param completeRoute935
                
         */

         
                     public void completeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info936)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CompleteRoute
                * @param completeRoute935
            
          */
        public void startcompleteRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info936,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                    * @param retrieveLocationsByCriteriaEx938
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria939,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options940)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                * @param retrieveLocationsByCriteriaEx938
            
          */
        public void startretrieveLocationsByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria939,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options940,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveStop
                    * @param arriveStop943
                
         */

         
                     public void arriveStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info944)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveStop
                * @param arriveStop943
            
          */
        public void startarriveStop(

            com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info944,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedTable
                    * @param saveUserDefinedTable946
                
         */

         
                     public void saveUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table947)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedTable
                * @param saveUserDefinedTable946
            
          */
        public void startsaveUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table947,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStandardRouteSetsByCriteria
                    * @param retrieveStandardRouteSetsByCriteria949
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StandardRouteSet[] retrieveStandardRouteSetsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetCriteria criteria950,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options951)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStandardRouteSetsByCriteria
                * @param retrieveStandardRouteSetsByCriteria949
            
          */
        public void startretrieveStandardRouteSetsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetCriteria criteria950,com.freshdirect.routing.proxy.stub.transportation.StandardRouteSetRetrieveOptions options951,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoute
                    * @param saveRoute954
                
         */

         
                     public void saveRoute(

                        com.freshdirect.routing.proxy.stub.transportation.Route route955,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions956,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions957)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoute
                * @param saveRoute954
            
          */
        public void startsaveRoute(

            com.freshdirect.routing.proxy.stub.transportation.Route route955,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions956,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions957,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveDestination
                    * @param arriveDestination959
                
         */

         
                     public void arriveDestination(

                        com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info960)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveDestination
                * @param arriveDestination959
            
          */
        public void startarriveDestination(

            com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info960,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByIdentity
                    * @param retrieveEquipmentByIdentity962
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment retrieveEquipmentByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity963,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options964)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByIdentity
                * @param retrieveEquipmentByIdentity962
            
          */
        public void startretrieveEquipmentByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity963,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options964,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationsEx
                    * @param saveLocationsEx967
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocationsEx(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations968,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options969)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationsEx
                * @param saveLocationsEx967
            
          */
        public void startsaveLocationsEx(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations968,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options969,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteByIdentity
                    * @param retrieveRouteByIdentity972
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity973,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options974)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteByIdentity
                * @param retrieveRouteByIdentity972
            
          */
        public void startretrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity973,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options974,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserConfig
                    * @param retrieveUserConfig977
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveUserConfig(

                        java.lang.String applicationID978,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity979,java.lang.String configGroupID980)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserConfig
                * @param retrieveUserConfig977
            
          */
        public void startretrieveUserConfig(

            java.lang.String applicationID978,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity979,java.lang.String configGroupID980,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionEx
                    * @param updateRoutePositionEx983
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos984)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionEx
                * @param updateRoutePositionEx983
            
          */
        public void startupdateRoutePositionEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos984,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentity
                    * @param retrieveLocationByIdentity987
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity988)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentity
                * @param retrieveLocationByIdentity987
            
          */
        public void startretrieveLocationByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity988,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserByUserID
                    * @param retrieveUserByUserID991
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.User retrieveUserByUserID(

                        java.lang.String userID992)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserByUserID
                * @param retrieveUserByUserID991
            
          */
        public void startretrieveUserByUserID(

            java.lang.String userID992,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignEquipment
                    * @param assignEquipment995
                
         */

         
                     public void assignEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity996,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment997)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignEquipment
                * @param assignEquipment995
            
          */
        public void startassignEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity996,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment997,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveGlobalConfig
                    * @param saveGlobalConfig999
                
         */

         
                     public void saveGlobalConfig(

                        java.lang.String applicationID0,java.lang.String configGroupID1,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items2)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveGlobalConfig
                * @param saveGlobalConfig999
            
          */
        public void startsaveGlobalConfig(

            java.lang.String applicationID0,java.lang.String configGroupID1,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items2,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEquipment
                    * @param saveEquipment4
                
         */

         
                     public void saveEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.Equipment equipment5,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options6)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEquipment
                * @param saveEquipment4
            
          */
        public void startsaveEquipment(

            com.freshdirect.routing.proxy.stub.transportation.Equipment equipment5,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options6,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerConfirmOrder
                    * @param schedulerConfirmOrder8
                
         */

         
                     public void schedulerConfirmOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity9,java.lang.String orderNumberXML10)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerConfirmOrder
                * @param schedulerConfirmOrder8
            
          */
        public void startschedulerConfirmOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity9,java.lang.String orderNumberXML10,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsOptions
                    * @param retrieveTelematicsOptions12
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsOptions retrieveTelematicsOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity13)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsOptions
                * @param retrieveTelematicsOptions12
            
          */
        public void startretrieveTelematicsOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity13,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                    * @param retrieveUserDefinedTableByIdentity16
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable retrieveUserDefinedTableByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity17)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                * @param retrieveUserDefinedTableByIdentity16
            
          */
        public void startretrieveUserDefinedTableByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity17,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteria
                    * @param retrieveLocationsByCriteria20
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria21)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteria
                * @param retrieveLocationsByCriteria20
            
          */
        public void startretrieveLocationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria21,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreatePlanningSession
                    * @param createPlanningSession24
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity createPlanningSession(

                        java.lang.String regionId25,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties26)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreatePlanningSession
                * @param createPlanningSession24
            
          */
        public void startcreatePlanningSession(

            java.lang.String regionId25,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties26,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentityEx
                    * @param retrieveLocationByIdentityEx29
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentityEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity30,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options31)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentityEx
                * @param retrieveLocationByIdentityEx29
            
          */
        public void startretrieveLocationByIdentityEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity30,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options31,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                    * @param retrievePlanningTerritoriesByCriteria34
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningTerritory[] retrievePlanningTerritoriesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria35,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options36)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                * @param retrievePlanningTerritoriesByCriteria34
            
          */
        public void startretrievePlanningTerritoriesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria35,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options36,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamp
                    * @param convertTimestamp39
                
         */

         
                     public java.util.Calendar convertTimestamp(

                        java.util.Calendar sourceTimestamp40,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions41,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions42)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamp
                * @param convertTimestamp39
            
          */
        public void startconvertTimestamp(

            java.util.Calendar sourceTimestamp40,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions41,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions42,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                    * @param schedulerSendRoutesToRoadnetEx45
                
         */

         
                     public void schedulerSendRoutesToRoadnetEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity46,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options47)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                * @param schedulerSendRoutesToRoadnetEx45
            
          */
        public void startschedulerSendRoutesToRoadnetEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity46,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options47,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                    * @param schedulerSendRoutesToRoadnet49
                
         */

         
                     public void schedulerSendRoutesToRoadnet(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity50)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                * @param schedulerSendRoutesToRoadnet49
            
          */
        public void startschedulerSendRoutesToRoadnet(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity50,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportsByCriteria
                    * @param retrieveReportsByCriteria52
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report[] retrieveReportsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria53)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportsByCriteria
                * @param retrieveReportsByCriteria52
            
          */
        public void startretrieveReportsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria53,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                    * @param retrieveRouteNotesByCriteriaEx56
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria57,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options58)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                * @param retrieveRouteNotesByCriteriaEx56
            
          */
        public void startretrieveRouteNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria57,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options58,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedData
                    * @param saveUserDefinedData61
                
         */

         
                     public void saveUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity62,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input63,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions64)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedData
                * @param saveUserDefinedData61
            
          */
        public void startsaveUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity62,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input63,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions64,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUnassignsByCriteria
                    * @param retrieveUnassignsByCriteria66
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria67,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options68)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUnassignsByCriteria
                * @param retrieveUnassignsByCriteria66
            
          */
        public void startretrieveUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria67,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options68,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutes
                    * @param schedulerRebuildRoutes71
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity72)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutes
                * @param schedulerRebuildRoutes71
            
          */
        public void startschedulerRebuildRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity72,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeletePlanningLocationExtensions
                    * @param deletePlanningLocationExtensions75
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] deletePlanningLocationExtensions(

                        java.lang.String regionId76,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions77)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeletePlanningLocationExtensions
                * @param deletePlanningLocationExtensions75
            
          */
        public void startdeletePlanningLocationExtensions(

            java.lang.String regionId76,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions77,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DescribeAuthenticaitonPolicy
                    * @param describeAuthenticaitonPolicy80
                
         */

         
                     public java.lang.String describeAuthenticaitonPolicy(

                        java.lang.String localeId81)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DescribeAuthenticaitonPolicy
                * @param describeAuthenticaitonPolicy80
            
          */
        public void startdescribeAuthenticaitonPolicy(

            java.lang.String localeId81,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop84
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop84
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCancelOrder
                    * @param schedulerCancelOrder87
                
         */

         
                     public void schedulerCancelOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity88,java.lang.String orderNumberXML89)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCancelOrder
                * @param schedulerCancelOrder87
            
          */
        public void startschedulerCancelOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity88,java.lang.String orderNumberXML89,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePosition
                    * @param updateRoutePosition91
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info92)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePosition
                * @param updateRoutePosition91
            
          */
        public void startupdateRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info92,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWindow
                    * @param schedulerSaveDeliveryWindow95
                
         */

         
                     public boolean schedulerSaveDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity96,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options97)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWindow
                * @param schedulerSaveDeliveryWindow95
            
          */
        public void startschedulerSaveDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity96,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options97,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateAdminRoute
                    * @param createAdminRoute100
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity createAdminRoute(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity101,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity102)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateAdminRoute
                * @param createAdminRoute100
            
          */
        public void startcreateAdminRoute(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity101,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity102,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                    * @param retrieveRoutingImportOrdersByCriteria105
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] retrieveRoutingImportOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria106,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions107)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                * @param retrieveRoutingImportOrdersByCriteria105
            
          */
        public void startretrieveRoutingImportOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria106,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions107,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SuggestRoute
                    * @param suggestRoute110
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] suggestRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop111,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options112)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SuggestRoute
                * @param suggestRoute110
            
          */
        public void startsuggestRoute(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop111,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options112,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__StartRoute
                    * @param startRoute115
                
         */

         
                     public void startRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info116)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__StartRoute
                * @param startRoute115
            
          */
        public void startstartRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info116,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAccountTypesByCriteria
                    * @param retrieveAccountTypesByCriteria118
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AccountType[] retrieveAccountTypesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.AccountTypeCriteria criteria119)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAccountTypesByCriteria
                * @param retrieveAccountTypesByCriteria118
            
          */
        public void startretrieveAccountTypesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.AccountTypeCriteria criteria119,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUnload
                    * @param schedulerUnload122
                
         */

         
                     public void schedulerUnload(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity123)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUnload
                * @param schedulerUnload122
            
          */
        public void startschedulerUnload(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity123,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                    * @param schedulerExcludeCutoffRoutes125
                
         */

         
                     public void schedulerExcludeCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity126,boolean excludeXML127)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                * @param schedulerExcludeCutoffRoutes125
            
          */
        public void startschedulerExcludeCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity126,boolean excludeXML127,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnassignStop
                    * @param unassignStop129
                
         */

         
                     public void unassignStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop130)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnassignStop
                * @param unassignStop129
            
          */
        public void startunassignStop(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop130,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                    * @param retrieveRouteDailyStatsByCriteria132
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria133,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options134)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                * @param retrieveRouteDailyStatsByCriteria132
            
          */
        public void startretrieveRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria133,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options134,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                    * @param retrievePlanningSessionPropertiesByIdentity137
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession retrievePlanningSessionPropertiesByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity138)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                * @param retrievePlanningSessionPropertiesByIdentity137
            
          */
        public void startretrievePlanningSessionPropertiesByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity138,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingDriverDirections
                    * @param buildRoutingDriverDirections141
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildRoutingDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity142)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingDriverDirections
                * @param buildRoutingDriverDirections141
            
          */
        public void startbuildRoutingDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity142,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocations
                    * @param saveLocations145
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations146)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocations
                * @param saveLocations145
            
          */
        public void startsaveLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations146,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSapShipments
                    * @param saveSapShipments149
                
         */

         
                     public boolean saveSapShipments(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid150,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments151)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSapShipments
                * @param saveSapShipments149
            
          */
        public void startsaveSapShipments(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid150,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments151,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionConfig
                    * @param retrieveRegionConfig154
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveRegionConfig(

                        java.lang.String applicationID155,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity156,java.lang.String configGroupID157)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionConfig
                * @param retrieveRegionConfig154
            
          */
        public void startretrieveRegionConfig(

            java.lang.String applicationID155,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity156,java.lang.String configGroupID157,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                    * @param retrieveRoutingLocationsWithOrdersEx160
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity161,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options162)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                * @param retrieveRoutingLocationsWithOrdersEx160
            
          */
        public void startretrieveRoutingLocationsWithOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity161,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options162,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyQuestions
                    * @param retrieveRouteSurveyQuestions165
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveRouteSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity166,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt167)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyQuestions
                * @param retrieveRouteSurveyQuestions165
            
          */
        public void startretrieveRouteSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity166,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt167,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegion
                    * @param saveRegion170
                
         */

         
                     public void saveRegion(

                        com.freshdirect.routing.proxy.stub.transportation.Region region171)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegion
                * @param saveRegion170
            
          */
        public void startsaveRegion(

            com.freshdirect.routing.proxy.stub.transportation.Region region171,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                    * @param schedulerRetrieveRoutesByCriteria173
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute[] schedulerRetrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria174,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options175)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                * @param schedulerRetrieveRoutesByCriteria173
            
          */
        public void startschedulerRetrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria174,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options175,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipment
                    * @param deleteSapShipment178
                
         */

         
                     public boolean deleteSapShipment(

                        com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid179)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipment
                * @param deleteSapShipment178
            
          */
        public void startdeleteSapShipment(

            com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid179,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUnassigned
                    * @param saveUnassigned182
                
         */

         
                     public void saveUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop183,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options184)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUnassigned
                * @param saveUnassigned182
            
          */
        public void startsaveUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop183,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options184,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrders
                    * @param saveRoutingImportOrders186
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrders(

                        java.lang.String regionId187,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders188,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions189)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrders
                * @param saveRoutingImportOrders186
            
          */
        public void startsaveRoutingImportOrders(

            java.lang.String regionId187,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders188,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions189,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                    * @param retrievePositionHistoryBlocksByCriteria192
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryBlocksByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria193)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                * @param retrievePositionHistoryBlocksByCriteria192
            
          */
        public void startretrievePositionHistoryBlocksByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria193,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SequenceStop
                    * @param sequenceStop196
                
         */

         
                     public void sequenceStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info197)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SequenceStop
                * @param sequenceStop196
            
          */
        public void startsequenceStop(

            com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info197,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportByIdentity
                    * @param retrieveReportByIdentity199
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report retrieveReportByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity200)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportByIdentity
                * @param retrieveReportByIdentity199
            
          */
        public void startretrieveReportByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity200,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedData
                    * @param deleteUserDefinedData203
                
         */

         
                     public void deleteUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity204,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input205)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedData
                * @param deleteUserDefinedData203
            
          */
        public void startdeleteUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity204,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input205,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationServiceStatsByCriteria
                    * @param retrieveLocationServiceStatsByCriteria207
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LocationServiceStats[] retrieveLocationServiceStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationServiceStatsCriteria criteria208)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationServiceStatsByCriteria
                * @param retrieveLocationServiceStatsByCriteria207
            
          */
        public void startretrieveLocationServiceStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationServiceStatsCriteria criteria208,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                    * @param retrieveUserDefinedTablesByCriteria211
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable[] retrieveUserDefinedTablesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria212)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                * @param retrieveUserDefinedTablesByCriteria211
            
          */
        public void startretrieveUserDefinedTablesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria212,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBulkReserveOrders
                    * @param schedulerBulkReserveOrders215
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerBulkReserveOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity216,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders217,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options218)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBulkReserveOrders
                * @param schedulerBulkReserveOrders215
            
          */
        public void startschedulerBulkReserveOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity216,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders217,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options218,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                    * @param retrieveDepotSkusAvailabilitiesByCriteria221
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] retrieveDepotSkusAvailabilitiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria222)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                * @param retrieveDepotSkusAvailabilitiesByCriteria221
            
          */
        public void startretrieveDepotSkusAvailabilitiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria222,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                    * @param retrieveRoutingStopsByCriteria225
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria226,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options227)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                * @param retrieveRoutingStopsByCriteria225
            
          */
        public void startretrieveRoutingStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria226,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options227,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                    * @param retrieveRoutingRoutesByCriteria230
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] retrieveRoutingRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria231,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options232)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                * @param retrieveRoutingRoutesByCriteria230
            
          */
        public void startretrieveRoutingRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria231,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options232,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnsubscribeFromNotifications
                    * @param unsubscribeFromNotifications235
                
         */

         
                     public void unsubscribeFromNotifications(

                        java.lang.String unsubscribeToken236,java.lang.String recipientEmail237)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnsubscribeFromNotifications
                * @param unsubscribeFromNotifications235
            
          */
        public void startunsubscribeFromNotifications(

            java.lang.String unsubscribeToken236,java.lang.String recipientEmail237,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRICRegionsByUser
                    * @param retrieveRICRegionsByUser239
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RICRegionsWithPurchaseInfo retrieveRICRegionsByUser(

                        java.lang.String userId240)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRICRegionsByUser
                * @param retrieveRICRegionsByUser239
            
          */
        public void startretrieveRICRegionsByUser(

            java.lang.String userId240,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MassStopSequence
                    * @param massStopSequence243
                
         */

         
                     public void massStopSequence(

                        com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info244)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MassStopSequence
                * @param massStopSequence243
            
          */
        public void startmassStopSequence(

            com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info244,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                    * @param retrieveDefaultPlanningSessionProperties246
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties retrieveDefaultPlanningSessionProperties(

                        java.lang.String regionId247)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                * @param retrieveDefaultPlanningSessionProperties246
            
          */
        public void startretrieveDefaultPlanningSessionProperties(

            java.lang.String regionId247,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                    * @param schedulerRetrieveOrderByIdentity250
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder schedulerRetrieveOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity251,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options252)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                * @param schedulerRetrieveOrderByIdentity250
            
          */
        public void startschedulerRetrieveOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity251,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options252,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                    * @param schedulerCalculateDeliveryWindowMetrics255
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics[] schedulerCalculateDeliveryWindowMetrics(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity256,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options257)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                * @param schedulerCalculateDeliveryWindowMetrics255
            
          */
        public void startschedulerCalculateDeliveryWindowMetrics(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity256,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options257,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutesEx
                    * @param schedulerRebuildRoutesEx260
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutesEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity261,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options262)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutesEx
                * @param schedulerRebuildRoutesEx260
            
          */
        public void startschedulerRebuildRoutesEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity261,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options262,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                    * @param retrievePlanningSessionPropertiesByCriteria265
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession[] retrievePlanningSessionPropertiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria266)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                * @param retrievePlanningSessionPropertiesByCriteria265
            
          */
        public void startretrievePlanningSessionPropertiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria266,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveDepotSkusAvailabilities
                    * @param saveDepotSkusAvailabilities269
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] saveDepotSkusAvailabilities(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus270)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveDepotSkusAvailabilities
                * @param saveDepotSkusAvailabilities269
            
          */
        public void startsaveDepotSkusAvailabilities(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus270,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartStop
                    * @param departStop273
                
         */

         
                     public void departStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info274)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartStop
                * @param departStop273
            
          */
        public void startdepartStop(

            com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info274,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEmployee
                    * @param saveEmployee276
                
         */

         
                     public void saveEmployee(

                        com.freshdirect.routing.proxy.stub.transportation.Employee employee277)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEmployee
                * @param saveEmployee276
            
          */
        public void startsaveEmployee(

            com.freshdirect.routing.proxy.stub.transportation.Employee employee277,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStop
                    * @param saveStop279
                
         */

         
                     public void saveStop(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop280,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions281,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options282)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStop
                * @param saveStop279
            
          */
        public void startsaveStop(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop280,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions281,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options282,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                    * @param retrieveAutoArriveDepartOptions284
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AutoArriveDepartOptions retrieveAutoArriveDepartOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity285)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                * @param retrieveAutoArriveDepartOptions284
            
          */
        public void startretrieveAutoArriveDepartOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity285,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AuthenticateUser
                    * @param authenticateUser288
                
         */

         
                     public int authenticateUser(

                        java.lang.String userID289,java.lang.String password290)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AuthenticateUser
                * @param authenticateUser288
            
          */
        public void startauthenticateUser(

            java.lang.String userID289,java.lang.String password290,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrdersEx
                    * @param saveRoutingImportOrdersEx293
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrdersEx(

                        java.lang.String regionId294,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders295,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions296,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions297)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrdersEx
                * @param saveRoutingImportOrdersEx293
            
          */
        public void startsaveRoutingImportOrdersEx(

            java.lang.String regionId294,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders295,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions296,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions297,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                    * @param retrieveStopCancelCodesByCriteria300
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopCancelCode[] retrieveStopCancelCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria301)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                * @param retrieveStopCancelCodesByCriteria300
            
          */
        public void startretrieveStopCancelCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria301,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    