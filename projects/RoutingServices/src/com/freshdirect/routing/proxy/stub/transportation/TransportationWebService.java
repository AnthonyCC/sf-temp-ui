

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
                    * @param retrieveUserDefinedColumnByIdentity335
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn retrieveUserDefinedColumnByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity336)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnByIdentity
                * @param retrieveUserDefinedColumnByIdentity335
            
          */
        public void startretrieveUserDefinedColumnByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnIdentity columnIdentity336,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                    * @param buildDispatchDriverDirectionsEx339
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirectionsEx(

                        com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info340)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirectionsEx
                * @param buildDispatchDriverDirectionsEx339
            
          */
        public void startbuildDispatchDriverDirectionsEx(

            com.freshdirect.routing.proxy.stub.transportation.BuildDriverDirectionsExInfo info340,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyResults
                    * @param retrieveStopSurveyResults343
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity344)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyResults
                * @param retrieveStopSurveyResults343
            
          */
        public void startretrieveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity344,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                    * @param schedulerRetrieveFeederRoutes347
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] schedulerRetrieveFeederRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity348,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options349)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveFeederRoutes
                * @param schedulerRetrieveFeederRoutes347
            
          */
        public void startschedulerRetrieveFeederRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity348,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options349,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignDrivers
                    * @param assignDrivers352
                
         */

         
                     public void assignDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity353,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers354)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignDrivers
                * @param assignDrivers352
            
          */
        public void startassignDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity353,com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers354,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopByIdentity
                    * @param retrieveStopByIdentity356
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop retrieveStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity357,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options358)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopByIdentity
                * @param retrieveStopByIdentity356
            
          */
        public void startretrieveStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity357,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options358,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeesByCriteria
                    * @param retrieveEmployeesByCriteria361
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria362)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeesByCriteria
                * @param retrieveEmployeesByCriteria361
            
          */
        public void startretrieveEmployeesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria362,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                    * @param retrieveRoutingRouteByIdentity365
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute retrieveRoutingRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity366,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options367)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteByIdentity
                * @param retrieveRoutingRouteByIdentity365
            
          */
        public void startretrieveRoutingRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity366,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options367,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                    * @param retrieveRoutingSessionByIdentity370
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession retrieveRoutingSessionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity371,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options372)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionByIdentity
                * @param retrieveRoutingSessionByIdentity370
            
          */
        public void startretrieveRoutingSessionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity371,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options372,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                    * @param schedulerRetrieveOrdersByCriteria375
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerRetrieveOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity376,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria377,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options378)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
                * @param schedulerRetrieveOrdersByCriteria375
            
          */
        public void startschedulerRetrieveOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity376,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria377,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options378,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                    * @param schedulerDeleteDeliveryWindow381
                
         */

         
                     public void schedulerDeleteDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity382,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window383,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options384)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerDeleteDeliveryWindow
                * @param schedulerDeleteDeliveryWindow381
            
          */
        public void startschedulerDeleteDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity382,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow window383,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeleteDeliveryWindowOptions options384,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSurveyResults
                    * @param saveStopSurveyResults386
                
         */

         
                     public void saveStopSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity387,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults388)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSurveyResults
                * @param saveStopSurveyResults386
            
          */
        public void startsaveStopSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity387,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults388,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerMovableOrders
                    * @param schedulerMovableOrders390
                
         */

         
                     public void schedulerMovableOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity391,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria392,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options393)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerMovableOrders
                * @param schedulerMovableOrders390
            
          */
        public void startschedulerMovableOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity391,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersCriteria criteria392,com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options393,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                    * @param schedulerRetrieveDeliveryWaveInstancesByCriteria395
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance[] schedulerRetrieveDeliveryWaveInstancesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity396,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria397,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options398)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveDeliveryWaveInstancesByCriteria
                * @param schedulerRetrieveDeliveryWaveInstancesByCriteria395
            
          */
        public void startschedulerRetrieveDeliveryWaveInstancesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity396,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWaveInstanceCriteria criteria397,com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveDeliveryWaveInstanceOptions options398,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CancelStop
                    * @param cancelStop401
                
         */

         
                     public void cancelStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info402)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CancelStop
                * @param cancelStop401
            
          */
        public void startcancelStop(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info402,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryByCriteria
                    * @param retrievePositionHistoryByCriteria404
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria405)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryByCriteria
                * @param retrievePositionHistoryByCriteria404
            
          */
        public void startretrievePositionHistoryByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria405,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RemoveRoute
                    * @param removeRoute408
                
         */

         
                     public void removeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity409)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RemoveRoute
                * @param removeRoute408
            
          */
        public void startremoveRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity409,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnlockNotifications
                    * @param unlockNotifications411
                
         */

         
                     public void unlockNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria412)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnlockNotifications
                * @param unlockNotifications411
            
          */
        public void startunlockNotifications(

            com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria412,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerPurge
                    * @param schedulerPurge414
                
         */

         
                     public void schedulerPurge(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity415,boolean reloadXML416)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerPurge
                * @param schedulerPurge414
            
          */
        public void startschedulerPurge(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity415,boolean reloadXML416,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteNotifications
                    * @param deleteNotifications418
                
         */

         
                     public void deleteNotifications(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities419)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteNotifications
                * @param deleteNotifications418
            
          */
        public void startdeleteNotifications(

            com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities419,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateDeliveryDetails
                    * @param updateDeliveryDetails421
                
         */

         
                     public void updateDeliveryDetails(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info422)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateDeliveryDetails
                * @param updateDeliveryDetails421
            
          */
        public void startupdateDeliveryDetails(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info422,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerOptimizeOrders
                    * @param schedulerOptimizeOrders424
                
         */

         
                     public void schedulerOptimizeOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity425)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerOptimizeOrders
                * @param schedulerOptimizeOrders424
            
          */
        public void startschedulerOptimizeOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity425,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                    * @param retrieveDutyPeriodsByCriteria427
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] retrieveDutyPeriodsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria428,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options429)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
                * @param retrieveDutyPeriodsByCriteria427
            
          */
        public void startretrieveDutyPeriodsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria428,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options429,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                    * @param retrieveRoutingRouteDailyStatsByCriteria432
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRoutingRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria433,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options434)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
                * @param retrieveRoutingRouteDailyStatsByCriteria432
            
          */
        public void startretrieveRoutingRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria433,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options434,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSignature
                    * @param retrieveStopSignature437
                
         */

         
                     public javax.activation.DataHandler retrieveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity438,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType439)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSignature
                * @param retrieveStopSignature437
            
          */
        public void startretrieveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity438,com.freshdirect.routing.proxy.stub.transportation.ImageType imageType439,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGlobalConfig
                    * @param retrieveGlobalConfig442
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveGlobalConfig(

                        java.lang.String applicationID443,java.lang.String configGroupID444)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGlobalConfig
                * @param retrieveGlobalConfig442
            
          */
        public void startretrieveGlobalConfig(

            java.lang.String applicationID443,java.lang.String configGroupID444,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionByIdentity
                    * @param retrieveRegionByIdentity447
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region retrieveRegionByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity448)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionByIdentity
                * @param retrieveRegionByIdentity447
            
          */
        public void startretrieveRegionByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity448,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerLoad
                    * @param schedulerLoad451
                
         */

         
                     public void schedulerLoad(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity452)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerLoad
                * @param schedulerLoad451
            
          */
        public void startschedulerLoad(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity452,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteReport
                    * @param deleteReport454
                
         */

         
                     public void deleteReport(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity455)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteReport
                * @param deleteReport454
            
          */
        public void startdeleteReport(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity455,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopSurveyQuestions
                    * @param retrieveStopSurveyQuestions457
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveStopSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity458)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopSurveyQuestions
                * @param retrieveStopSurveyQuestions457
            
          */
        public void startretrieveStopSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity458,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedDrivers
                    * @param retrieveAssignedDrivers461
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] retrieveAssignedDrivers(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity462)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedDrivers
                * @param retrieveAssignedDrivers461
            
          */
        public void startretrieveAssignedDrivers(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity462,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ChangeUserPassword
                    * @param changeUserPassword465
                
         */

         
                     public void changeUserPassword(

                        java.lang.String userID466,java.lang.String oldPassword467,java.lang.String newPassword468)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ChangeUserPassword
                * @param changeUserPassword465
            
          */
        public void startchangeUserPassword(

            java.lang.String userID466,java.lang.String oldPassword467,java.lang.String newPassword468,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                    * @param deleteSapShipmentsBySessionIdentity470
                
         */

         
                     public boolean deleteSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid471)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipmentsBySessionIdentity
                * @param deleteSapShipmentsBySessionIdentity470
            
          */
        public void startdeleteSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid471,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                    * @param retrievePlanningUserDefinedFieldInfo474
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningUserDefinedFieldInfo[] retrievePlanningUserDefinedFieldInfo(

                        java.lang.String regionId475)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningUserDefinedFieldInfo
                * @param retrievePlanningUserDefinedFieldInfo474
            
          */
        public void startretrievePlanningUserDefinedFieldInfo(

            java.lang.String regionId475,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                    * @param retrieveUserDefinedColumnsByCriteria478
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumn[] retrieveUserDefinedColumnsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria479)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedColumnsByCriteria
                * @param retrieveUserDefinedColumnsByCriteria478
            
          */
        public void startretrieveUserDefinedColumnsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedColumnCriteria columnCriteria479,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CalculateTimeDist
                    * @param calculateTimeDist482
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TimeDistResult calculateTimeDist(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity483,int fromLatitude484,int fromLongitude485,int toLatitude486,int toLongitude487)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CalculateTimeDist
                * @param calculateTimeDist482
            
          */
        public void startcalculateTimeDist(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity483,int fromLatitude484,int fromLongitude485,int toLatitude486,int toLongitude487,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegionConfig
                    * @param saveRegionConfig490
                
         */

         
                     public void saveRegionConfig(

                        java.lang.String applicationID491,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity492,java.lang.String configGroupID493,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items494)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegionConfig
                * @param saveRegionConfig490
            
          */
        public void startsaveRegionConfig(

            java.lang.String applicationID491,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity492,java.lang.String configGroupID493,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items494,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                    * @param retrieveStopNotesByCriteriaEx496
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopNote[] retrieveStopNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria497,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options498)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopNotesByCriteriaEx
                * @param retrieveStopNotesByCriteriaEx496
            
          */
        public void startretrieveStopNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.StopNoteCriteria criteria497,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options498,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteForDevice
                    * @param retrieveRouteForDevice501
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteForDevice(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity502,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options503,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity504)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteForDevice
                * @param retrieveRouteForDevice501
            
          */
        public void startretrieveRouteForDevice(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity502,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options503,com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity504,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionETAs
                    * @param updateRoutePositionETAs507
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionETAs(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId508,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos509)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionETAs
                * @param updateRoutePositionETAs507
            
          */
        public void startupdateRoutePositionETAs(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeId508,com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos509,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SendTextMessageToDriver
                    * @param sendTextMessageToDriver512
                
         */

         
                     public void sendTextMessageToDriver(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity513,java.lang.String message514,java.lang.String fromUserID515)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SendTextMessageToDriver
                * @param sendTextMessageToDriver512
            
          */
        public void startsendTextMessageToDriver(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity513,java.lang.String message514,java.lang.String fromUserID515,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedTable
                    * @param deleteUserDefinedTable517
                
         */

         
                     public void deleteUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId518)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedTable
                * @param deleteUserDefinedTable517
            
          */
        public void startdeleteUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableId518,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSkus
                    * @param saveSkus520
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] saveSkus(

                        com.freshdirect.routing.proxy.stub.transportation.Sku[] skus521)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSkus
                * @param saveSkus520
            
          */
        public void startsaveSkus(

            com.freshdirect.routing.proxy.stub.transportation.Sku[] skus521,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                    * @param retrieveSapShipmentsBySessionIdentity524
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SapShipment[] retrieveSapShipmentsBySessionIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid525)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSapShipmentsBySessionIdentity
                * @param retrieveSapShipmentsBySessionIdentity524
            
          */
        public void startretrieveSapShipmentsBySessionIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid525,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExtendOrderReservation
                    * @param schedulerExtendOrderReservation528
                
         */

         
                     public void schedulerExtendOrderReservation(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity529,java.lang.String orderNumberXML530,int extendMinutes531)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExtendOrderReservation
                * @param schedulerExtendOrderReservation528
            
          */
        public void startschedulerExtendOrderReservation(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity529,java.lang.String orderNumberXML530,int extendMinutes531,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionOptions
                    * @param retrieveRegionOptions533
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RegionOptions retrieveRegionOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity534)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionOptions
                * @param retrieveRegionOptions533
            
          */
        public void startretrieveRegionOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity534,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStopSignature
                    * @param saveStopSignature537
                
         */

         
                     public void saveStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity538,javax.activation.DataHandler signatureData539)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStopSignature
                * @param saveStopSignature537
            
          */
        public void startsaveStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity538,javax.activation.DataHandler signatureData539,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                    * @param retrieveUndeliverableStopCodesByCriteria541
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCode[] retrieveUndeliverableStopCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria542)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUndeliverableStopCodesByCriteria
                * @param retrieveUndeliverableStopCodesByCriteria541
            
          */
        public void startretrieveUndeliverableStopCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCodeCriteria criteria542,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteRoutingSession
                    * @param deleteRoutingSession545
                
         */

         
                     public void deleteRoutingSession(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity546)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteRoutingSession
                * @param deleteRoutingSession545
            
          */
        public void startdeleteRoutingSession(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity546,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                    * @param schedulerIsExcludingCutoffRoutes548
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult schedulerIsExcludingCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity549)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
                * @param schedulerIsExcludingCutoffRoutes548
            
          */
        public void startschedulerIsExcludingCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity549,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                    * @param retrieveRoutingImportOrderByIdentity552
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder retrieveRoutingImportOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity553,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions554)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
                * @param retrieveRoutingImportOrderByIdentity552
            
          */
        public void startretrieveRoutingImportOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity553,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions554,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkuByIdentity
                    * @param retrieveSkuByIdentity557
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku retrieveSkuByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity558)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkuByIdentity
                * @param retrieveSkuByIdentity557
            
          */
        public void startretrieveSkuByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.SkuIdentity identity558,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                    * @param retrieveQuantityReasonCodesByCriteria561
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCode[] retrieveQuantityReasonCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria562)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveQuantityReasonCodesByCriteria
                * @param retrieveQuantityReasonCodesByCriteria561
            
          */
        public void startretrieveQuantityReasonCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCodeCriteria criteria562,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                    * @param retrieveLineItemNotesByCriteriaEx565
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LineItemNote[] retrieveLineItemNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria566,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options567)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLineItemNotesByCriteriaEx
                * @param retrieveLineItemNotesByCriteriaEx565
            
          */
        public void startretrieveLineItemNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LineItemNoteCriteria criteria566,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options567,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                    * @param retrieveOrderNotesByCriteriaEx570
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.OrderNote[] retrieveOrderNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria571,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options572)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveOrderNotesByCriteriaEx
                * @param retrieveOrderNotesByCriteriaEx570
            
          */
        public void startretrieveOrderNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.OrderNoteCriteria criteria571,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options572,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                    * @param retrieveEquipmentTypeByIdentity575
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType retrieveEquipmentTypeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity576,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options577)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
                * @param retrieveEquipmentTypeByIdentity575
            
          */
        public void startretrieveEquipmentTypeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity576,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options577,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUnassigned
                    * @param deleteUnassigned580
                
         */

         
                     public void deleteUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop581)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUnassigned
                * @param deleteUnassigned580
            
          */
        public void startdeleteUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop581,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                    * @param retrieveRoutingSourcedOrdersByCriteria583
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] retrieveRoutingSourcedOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria584,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options585)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
                * @param retrieveRoutingSourcedOrdersByCriteria583
            
          */
        public void startretrieveRoutingSourcedOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria584,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options585,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserConfig
                    * @param saveUserConfig588
                
         */

         
                     public void saveUserConfig(

                        java.lang.String applicationID589,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity590,java.lang.String configGroupID591,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items592)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserConfig
                * @param saveUserConfig588
            
          */
        public void startsaveUserConfig(

            java.lang.String applicationID589,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity590,java.lang.String configGroupID591,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items592,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeByIdentity
                    * @param retrieveEmployeeByIdentity594
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity595)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeByIdentity
                * @param retrieveEmployeeByIdentity594
            
          */
        public void startretrieveEmployeeByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity595,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUpdateOrder
                    * @param schedulerUpdateOrder598
                
         */

         
                     public boolean schedulerUpdateOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity599,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity600,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options601)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUpdateOrder
                * @param schedulerUpdateOrder598
            
          */
        public void startschedulerUpdateOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity599,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity600,com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options601,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SurveyResponse
                    * @param surveyResponse604
                
         */

         
                     public void surveyResponse(

                        com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info605)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SurveyResponse
                * @param surveyResponse604
            
          */
        public void startsurveyResponse(

            com.freshdirect.routing.proxy.stub.transportation.SurveyResponseInfo info605,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RedeliverStop
                    * @param redeliverStop607
                
         */

         
                     public void redeliverStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info608)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RedeliverStop
                * @param redeliverStop607
            
          */
        public void startredeliverStop(

            com.freshdirect.routing.proxy.stub.transportation.StopRedeliverInfo info608,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByCriteria
                    * @param retrieveEquipmentByCriteria610
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment[] retrieveEquipmentByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria611,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options612)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByCriteria
                * @param retrieveEquipmentByCriteria610
            
          */
        public void startretrieveEquipmentByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria611,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options612,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartOrigin
                    * @param departOrigin615
                
         */

         
                     public void departOrigin(

                        com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info616)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartOrigin
                * @param departOrigin615
            
          */
        public void startdepartOrigin(

            com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info616,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionsByCriteria
                    * @param retrieveRegionsByCriteria618
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Region[] retrieveRegionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria619)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionsByCriteria
                * @param retrieveRegionsByCriteria618
            
          */
        public void startretrieveRegionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria619,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateRoutingSession
                    * @param createRoutingSession622
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity createRoutingSession(

                        java.lang.String regionId623,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties624)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateRoutingSession
                * @param createRoutingSession622
            
          */
        public void startcreateRoutingSession(

            java.lang.String regionId623,com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties624,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                    * @param retrieveDefaultRoutingSessionProperties627
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties retrieveDefaultRoutingSessionProperties(

                        java.lang.String regionId628,java.util.Date sessionDate629)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
                * @param retrieveDefaultRoutingSessionProperties627
            
          */
        public void startretrieveDefaultRoutingSessionProperties(

            java.lang.String regionId628,java.util.Date sessionDate629,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyResults
                    * @param retrieveRouteSurveyResults632
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity633,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt634)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyResults
                * @param retrieveRouteSurveyResults632
            
          */
        public void startretrieveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity633,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt634,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerAnalyzeOrder
                    * @param schedulerAnalyzeOrder637
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] schedulerAnalyzeOrder(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order638,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options639)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerAnalyzeOrder
                * @param schedulerAnalyzeOrder637
            
          */
        public void startschedulerAnalyzeOrder(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order638,com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options639,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                    * @param retrieveDutyPeriodByIdentity642
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod retrieveDutyPeriodByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity643,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options644)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDutyPeriodByIdentity
                * @param retrieveDutyPeriodByIdentity642
            
          */
        public void startretrieveDutyPeriodByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity643,com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options644,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopsByCriteria
                    * @param retrieveStopsByCriteria647
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria648,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options649)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopsByCriteria
                * @param retrieveStopsByCriteria647
            
          */
        public void startretrieveStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria648,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options649,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                    * @param retrieveDepotSkuAvailabilityByIdentity652
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability retrieveDepotSkuAvailabilityByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity653)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkuAvailabilityByIdentity
                * @param retrieveDepotSkuAvailabilityByIdentity652
            
          */
        public void startretrieveDepotSkuAvailabilityByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityIdentity identity653,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingRouteNetMatrix
                    * @param buildRoutingRouteNetMatrix656
                
         */

         
                     public void buildRoutingRouteNetMatrix(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity657)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingRouteNetMatrix
                * @param buildRoutingRouteNetMatrix656
            
          */
        public void startbuildRoutingRouteNetMatrix(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity657,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                    * @param retrieveRoutingSessionsByCriteria659
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] retrieveRoutingSessionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria660,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options661)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
                * @param retrieveRoutingSessionsByCriteria659
            
          */
        public void startretrieveRoutingSessionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria660,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options661,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamps
                    * @param convertTimestamps664
                
         */

         
                     public java.util.Calendar[] convertTimestamps(

                        java.util.Calendar[] sourceTimestamps665,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions666,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions667)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamps
                * @param convertTimestamps664
            
          */
        public void startconvertTimestamps(

            java.util.Calendar[] sourceTimestamps665,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions666,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions667,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByCriteria
                    * @param retrieveNotificationsByCriteria670
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria671,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions672,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options673)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByCriteria
                * @param retrieveNotificationsByCriteria670
            
          */
        public void startretrieveNotificationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.NotificationCriteria criteria671,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions672,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options673,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReserveOrder
                    * @param schedulerReserveOrder676
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ReserveResult schedulerReserveOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity677,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder678,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow679,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options680)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReserveOrder
                * @param schedulerReserveOrder676
            
          */
        public void startschedulerReserveOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity677,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder678,com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow679,com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options680,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteSurveyResults
                    * @param saveRouteSurveyResults683
                
         */

         
                     public void saveRouteSurveyResults(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity684,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt685,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults686)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteSurveyResults
                * @param saveRouteSurveyResults683
            
          */
        public void startsaveRouteSurveyResults(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity684,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt685,com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] surveyResults686,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                    * @param retrieveRoutingUnassignsByCriteria688
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria689,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options690)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
                * @param retrieveRoutingUnassignsByCriteria688
            
          */
        public void startretrieveRoutingUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria689,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options690,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__PlaceUnassigned
                    * @param placeUnassigned693
                
         */

         
                     public void placeUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop694,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity695,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition696,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime697,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions698)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__PlaceUnassigned
                * @param placeUnassigned693
            
          */
        public void startplaceUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop694,com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity695,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition696,com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime697,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions698,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateTelematicsCachePositions
                    * @param updateTelematicsCachePositions700
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions701)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateTelematicsCachePositions
                * @param updateTelematicsCachePositions700
            
          */
        public void startupdateTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] positions701,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                    * @param retrievePlanningLocationExtensionsByCriteria704
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] retrievePlanningLocationExtensionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria705,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options706)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningLocationExtensionsByCriteria
                * @param retrievePlanningLocationExtensionsByCriteria704
            
          */
        public void startretrievePlanningLocationExtensionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtensionCriteria criteria705,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningLocationExtensionsOptions options706,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BulkArriveDepartStop
                    * @param bulkArriveDepartStop709
                
         */

         
                     public void bulkArriveDepartStop(

                        com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos710)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BulkArriveDepartStop
                * @param bulkArriveDepartStop709
            
          */
        public void startbulkArriveDepartStop(

            com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos710,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SavePlanningLocationExtensions
                    * @param savePlanningLocationExtensions712
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] savePlanningLocationExtensions(

                        java.lang.String regionId713,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions714)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SavePlanningLocationExtensions
                * @param savePlanningLocationExtensions712
            
          */
        public void startsavePlanningLocationExtensions(

            java.lang.String regionId713,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions714,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CleanupTelematicsCachePositions
                    * @param cleanupTelematicsCachePositions717
                
         */

         
                     public void cleanupTelematicsCachePositions(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria718)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CleanupTelematicsCachePositions
                * @param cleanupTelematicsCachePositions717
            
          */
        public void startcleanupTelematicsCachePositions(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria718,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutesByCriteria
                    * @param retrieveRoutesByCriteria720
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route[] retrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria721,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options722)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutesByCriteria
                * @param retrieveRoutesByCriteria720
            
          */
        public void startretrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria721,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options722,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                    * @param schedulerRetrieveRouteByIdentity725
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute schedulerRetrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity726,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options727)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRouteByIdentity
                * @param schedulerRetrieveRouteByIdentity725
            
          */
        public void startschedulerRetrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteIdentity identity726,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options727,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRouteETAs
                    * @param updateRouteETAs730
                
         */

         
                     public void updateRouteETAs(

                        com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info731)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRouteETAs
                * @param updateRouteETAs730
            
          */
        public void startupdateRouteETAs(

            com.freshdirect.routing.proxy.stub.transportation.UpdateRouteETAsInfo info731,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ReturnFault
                    * @param returnFault733
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Fault returnFault(

                        int requestedFaultCode734)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ReturnFault
                * @param returnFault733
            
          */
        public void startreturnFault(

            int requestedFaultCode734,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEmployeeRouteStatsByCriteria
                    * @param retrieveEmployeeRouteStatsByCriteria737
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStats[] retrieveEmployeeRouteStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStatsCriteria criteria738)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEmployeeRouteStatsByCriteria
                * @param retrieveEmployeeRouteStatsByCriteria737
            
          */
        public void startretrieveEmployeeRouteStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeRouteStatsCriteria criteria738,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveyDetails
                    * @param retrieveSurveyDetails741
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyDetails[] retrieveSurveyDetails(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity742)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveyDetails
                * @param retrieveSurveyDetails741
            
          */
        public void startretrieveSurveyDetails(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity742,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AddRICUser
                    * @param addRICUser745
                
         */

         
                     public void addRICUser(

                        com.freshdirect.routing.proxy.stub.transportation.User user746)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AddRICUser
                * @param addRICUser745
            
          */
        public void startaddRICUser(

            com.freshdirect.routing.proxy.stub.transportation.User user746,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveProductsPurchased
                    * @param retrieveProductsPurchased748
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased retrieveProductsPurchased(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity749)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveProductsPurchased
                * @param retrieveProductsPurchased748
            
          */
        public void startretrieveProductsPurchased(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity749,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePermissionsForUser
                    * @param retrievePermissionsForUser752
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserPermissions retrievePermissionsForUser(

                        java.lang.String userID753,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity754)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePermissionsForUser
                * @param retrievePermissionsForUser752
            
          */
        public void startretrievePermissionsForUser(

            java.lang.String userID753,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity754,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteLocations
                    * @param deleteLocations757
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] deleteLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations758)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteLocations
                * @param deleteLocations757
            
          */
        public void startdeleteLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations758,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAssignedEquipment
                    * @param retrieveAssignedEquipment761
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] retrieveAssignedEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity762)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAssignedEquipment
                * @param retrieveAssignedEquipment761
            
          */
        public void startretrieveAssignedEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity762,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRouteReportedDistances
                    * @param saveRouteReportedDistances765
                
         */

         
                     public void saveRouteReportedDistances(

                        com.freshdirect.routing.proxy.stub.transportation.RouteReportedDistance[] reportedDistances766)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRouteReportedDistances
                * @param saveRouteReportedDistances765
            
          */
        public void startsaveRouteReportedDistances(

            com.freshdirect.routing.proxy.stub.transportation.RouteReportedDistance[] reportedDistances766,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopByIdentity
                    * @param retrieveRoutingStopByIdentity768
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop retrieveRoutingStopByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity769,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options770)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopByIdentity
                * @param retrieveRoutingStopByIdentity768
            
          */
        public void startretrieveRoutingStopByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity769,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options770,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__VersionInformation
                    * @param versionInformation773
                
         */

         
                     public java.lang.String versionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__VersionInformation
                * @param versionInformation773
            
          */
        public void startversionInformation(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildDispatchDriverDirections
                    * @param buildDispatchDriverDirections776
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildDispatchDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity777)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildDispatchDriverDirections
                * @param buildDispatchDriverDirections776
            
          */
        public void startbuildDispatchDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity777,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                    * @param retrieveTelematicsCachePositionsByCriteria780
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionInfo[] retrieveTelematicsCachePositionsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria781)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsCachePositionsByCriteria
                * @param retrieveTelematicsCachePositionsByCriteria780
            
          */
        public void startretrieveTelematicsCachePositionsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.TelematicsCachePositionCriteria criteria781,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateStopSignature
                    * @param updateStopSignature784
                
         */

         
                     public void updateStopSignature(

                        com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info785)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateStopSignature
                * @param updateStopSignature784
            
          */
        public void startupdateStopSignature(

            com.freshdirect.routing.proxy.stub.transportation.StopSignatureInfo info785,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                    * @param schedulerSaveDeliveryWaveInstance787
                
         */

         
                     public java.lang.String[] schedulerSaveDeliveryWaveInstance(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity788,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity789,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes790,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options791)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWaveInstance
                * @param schedulerSaveDeliveryWaveInstance787
            
          */
        public void startschedulerSaveDeliveryWaveInstance(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity788,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstanceIdentity waveIdentity789,com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveAttributes attributes790,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWaveInstanceOptions options791,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AllowAdditionOfRICUsers
                    * @param allowAdditionOfRICUsers794
                
         */

         
                     public boolean allowAdditionOfRICUsers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AllowAdditionOfRICUsers
                * @param allowAdditionOfRICUsers794
            
          */
        public void startallowAdditionOfRICUsers(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveGPSProviderOptions
                    * @param retrieveGPSProviderOptions797
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.GPSProviderOptions retrieveGPSProviderOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity798)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveGPSProviderOptions
                * @param retrieveGPSProviderOptions797
            
          */
        public void startretrieveGPSProviderOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity798,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                    * @param retrieveRoutingLocationsWithOrders801
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrders(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity802)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
                * @param retrieveRoutingLocationsWithOrders801
            
          */
        public void startretrieveRoutingLocationsWithOrders(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity802,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteria
                    * @param retrieveRouteNotesByCriteria805
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria806)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteria
                * @param retrieveRouteNotesByCriteria805
            
          */
        public void startretrieveRouteNotesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria806,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveReport
                    * @param saveReport809
                
         */

         
                     public void saveReport(

                        com.freshdirect.routing.proxy.stub.transportation.Report report810)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveReport
                * @param saveReport809
            
          */
        public void startsaveReport(

            com.freshdirect.routing.proxy.stub.transportation.Report report810,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSurveys
                    * @param retrieveSurveys812
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Survey[] retrieveSurveys(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity813)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSurveys
                * @param retrieveSurveys812
            
          */
        public void startretrieveSurveys(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity813,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                    * @param retrieveNotificationsByRecipientIdentity816
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByRecipientIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity817,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions818,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options819)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
                * @param retrieveNotificationsByRecipientIdentity816
            
          */
        public void startretrieveNotificationsByRecipientIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity817,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions818,com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options819,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBalanceRoutes
                    * @param schedulerBalanceRoutes822
                
         */

         
                     public void schedulerBalanceRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity823,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options824)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBalanceRoutes
                * @param schedulerBalanceRoutes822
            
          */
        public void startschedulerBalanceRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity823,com.freshdirect.routing.proxy.stub.transportation.SchedulerBalanceRoutesOptions options824,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                    * @param retrieveUserDefinedDataByCriteria826
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] retrieveUserDefinedDataByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity827,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria828,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions829)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByCriteria
                * @param retrieveUserDefinedDataByCriteria826
            
          */
        public void startretrieveUserDefinedDataByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity827,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataCriteria dataCriteria828,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions829,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveSkusByCriteria
                    * @param retrieveSkusByCriteria832
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Sku[] retrieveSkusByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria833)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveSkusByCriteria
                * @param retrieveSkusByCriteria832
            
          */
        public void startretrieveSkusByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.SkuCriteria criteria833,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                    * @param retrieveCannedTextMessagesByCriteria836
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] retrieveCannedTextMessagesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria837)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
                * @param retrieveCannedTextMessagesByCriteria836
            
          */
        public void startretrieveCannedTextMessagesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria837,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                    * @param retrieveUserDefinedDataByIdentity840
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedData retrieveUserDefinedDataByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity841,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity842,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions843)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedDataByIdentity
                * @param retrieveUserDefinedDataByIdentity840
            
          */
        public void startretrieveUserDefinedDataByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity841,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity dataIdentity842,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions843,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                    * @param retrieveEquipmentTypeByCriteria846
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] retrieveEquipmentTypeByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria847,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options848)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
                * @param retrieveEquipmentTypeByCriteria846
            
          */
        public void startretrieveEquipmentTypeByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria847,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options848,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                    * @param retrieveRoutingOrderByIdentity851
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder retrieveRoutingOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity852,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options853)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingOrderByIdentity
                * @param retrieveRoutingOrderByIdentity851
            
          */
        public void startretrieveRoutingOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity852,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options853,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__TextMessage
                    * @param textMessage856
                
         */

         
                     public void textMessage(

                        com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info857)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__TextMessage
                * @param textMessage856
            
          */
        public void starttextMessage(

            com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info857,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CompleteRoute
                    * @param completeRoute859
                
         */

         
                     public void completeRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info860)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CompleteRoute
                * @param completeRoute859
            
          */
        public void startcompleteRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info860,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                    * @param retrieveLocationsByCriteriaEx862
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria863,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options864)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteriaEx
                * @param retrieveLocationsByCriteriaEx862
            
          */
        public void startretrieveLocationsByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria863,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options864,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveStop
                    * @param arriveStop867
                
         */

         
                     public void arriveStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info868)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveStop
                * @param arriveStop867
            
          */
        public void startarriveStop(

            com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info868,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedTable
                    * @param saveUserDefinedTable870
                
         */

         
                     public void saveUserDefinedTable(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table871)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedTable
                * @param saveUserDefinedTable870
            
          */
        public void startsaveUserDefinedTable(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable table871,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoute
                    * @param saveRoute873
                
         */

         
                     public void saveRoute(

                        com.freshdirect.routing.proxy.stub.transportation.Route route874,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions875,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions876)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoute
                * @param saveRoute873
            
          */
        public void startsaveRoute(

            com.freshdirect.routing.proxy.stub.transportation.Route route874,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions875,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions876,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ArriveDestination
                    * @param arriveDestination878
                
         */

         
                     public void arriveDestination(

                        com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info879)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ArriveDestination
                * @param arriveDestination878
            
          */
        public void startarriveDestination(

            com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info879,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveEquipmentByIdentity
                    * @param retrieveEquipmentByIdentity881
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Equipment retrieveEquipmentByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity882,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options883)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveEquipmentByIdentity
                * @param retrieveEquipmentByIdentity881
            
          */
        public void startretrieveEquipmentByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity882,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options883,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocationsEx
                    * @param saveLocationsEx886
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocationsEx(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations887,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options888)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocationsEx
                * @param saveLocationsEx886
            
          */
        public void startsaveLocationsEx(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations887,com.freshdirect.routing.proxy.stub.transportation.SaveLocationsExOptions options888,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteByIdentity
                    * @param retrieveRouteByIdentity891
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity892,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options893)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteByIdentity
                * @param retrieveRouteByIdentity891
            
          */
        public void startretrieveRouteByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity892,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options893,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserConfig
                    * @param retrieveUserConfig896
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveUserConfig(

                        java.lang.String applicationID897,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity898,java.lang.String configGroupID899)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserConfig
                * @param retrieveUserConfig896
            
          */
        public void startretrieveUserConfig(

            java.lang.String applicationID897,com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity898,java.lang.String configGroupID899,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePositionEx
                    * @param updateRoutePositionEx902
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode[] updateRoutePositionEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos903)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePositionEx
                * @param updateRoutePositionEx902
            
          */
        public void startupdateRoutePositionEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo[] infos903,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentity
                    * @param retrieveLocationByIdentity906
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity907)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentity
                * @param retrieveLocationByIdentity906
            
          */
        public void startretrieveLocationByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity907,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserByUserID
                    * @param retrieveUserByUserID910
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.User retrieveUserByUserID(

                        java.lang.String userID911)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserByUserID
                * @param retrieveUserByUserID910
            
          */
        public void startretrieveUserByUserID(

            java.lang.String userID911,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AssignEquipment
                    * @param assignEquipment914
                
         */

         
                     public void assignEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity915,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment916)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AssignEquipment
                * @param assignEquipment914
            
          */
        public void startassignEquipment(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity915,com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment916,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveGlobalConfig
                    * @param saveGlobalConfig918
                
         */

         
                     public void saveGlobalConfig(

                        java.lang.String applicationID919,java.lang.String configGroupID920,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items921)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveGlobalConfig
                * @param saveGlobalConfig918
            
          */
        public void startsaveGlobalConfig(

            java.lang.String applicationID919,java.lang.String configGroupID920,com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items921,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEquipment
                    * @param saveEquipment923
                
         */

         
                     public void saveEquipment(

                        com.freshdirect.routing.proxy.stub.transportation.Equipment equipment924,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options925)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEquipment
                * @param saveEquipment923
            
          */
        public void startsaveEquipment(

            com.freshdirect.routing.proxy.stub.transportation.Equipment equipment924,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options925,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerConfirmOrder
                    * @param schedulerConfirmOrder927
                
         */

         
                     public void schedulerConfirmOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity928,java.lang.String orderNumberXML929)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerConfirmOrder
                * @param schedulerConfirmOrder927
            
          */
        public void startschedulerConfirmOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity928,java.lang.String orderNumberXML929,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveTelematicsOptions
                    * @param retrieveTelematicsOptions931
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.TelematicsOptions retrieveTelematicsOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity932)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveTelematicsOptions
                * @param retrieveTelematicsOptions931
            
          */
        public void startretrieveTelematicsOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity932,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                    * @param retrieveUserDefinedTableByIdentity935
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable retrieveUserDefinedTableByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity936)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTableByIdentity
                * @param retrieveUserDefinedTableByIdentity935
            
          */
        public void startretrieveUserDefinedTableByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity936,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationsByCriteria
                    * @param retrieveLocationsByCriteria939
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria940)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationsByCriteria
                * @param retrieveLocationsByCriteria939
            
          */
        public void startretrieveLocationsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria940,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreatePlanningSession
                    * @param createPlanningSession943
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity createPlanningSession(

                        java.lang.String regionId944,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties945)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreatePlanningSession
                * @param createPlanningSession943
            
          */
        public void startcreatePlanningSession(

            java.lang.String regionId944,com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties sessionProperties945,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationByIdentityEx
                    * @param retrieveLocationByIdentityEx948
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentityEx(

                        com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity949,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options950)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationByIdentityEx
                * @param retrieveLocationByIdentityEx948
            
          */
        public void startretrieveLocationByIdentityEx(

            com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity949,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options950,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                    * @param retrievePlanningTerritoriesByCriteria953
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningTerritory[] retrievePlanningTerritoriesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria954,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options955)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningTerritoriesByCriteria
                * @param retrievePlanningTerritoriesByCriteria953
            
          */
        public void startretrievePlanningTerritoriesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningTerritoryCriteria criteria954,com.freshdirect.routing.proxy.stub.transportation.RetrievePlanningTerritoriesOptions options955,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ConvertTimestamp
                    * @param convertTimestamp958
                
         */

         
                     public java.util.Calendar convertTimestamp(

                        java.util.Calendar sourceTimestamp959,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions960,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions961)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ConvertTimestamp
                * @param convertTimestamp958
            
          */
        public void startconvertTimestamp(

            java.util.Calendar sourceTimestamp959,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions960,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions961,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                    * @param schedulerSendRoutesToRoadnetEx964
                
         */

         
                     public void schedulerSendRoutesToRoadnetEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity965,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options966)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
                * @param schedulerSendRoutesToRoadnetEx964
            
          */
        public void startschedulerSendRoutesToRoadnetEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity965,com.freshdirect.routing.proxy.stub.transportation.SchedulerSendRoutesToRoadnetExOptions options966,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                    * @param schedulerSendRoutesToRoadnet968
                
         */

         
                     public void schedulerSendRoutesToRoadnet(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity969)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSendRoutesToRoadnet
                * @param schedulerSendRoutesToRoadnet968
            
          */
        public void startschedulerSendRoutesToRoadnet(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity969,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportsByCriteria
                    * @param retrieveReportsByCriteria971
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report[] retrieveReportsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria972)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportsByCriteria
                * @param retrieveReportsByCriteria971
            
          */
        public void startretrieveReportsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria972,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                    * @param retrieveRouteNotesByCriteriaEx975
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteriaEx(

                        com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria976,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options977)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteNotesByCriteriaEx
                * @param retrieveRouteNotesByCriteriaEx975
            
          */
        public void startretrieveRouteNotesByCriteriaEx(

            com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria976,com.freshdirect.routing.proxy.stub.transportation.NoteRetrievalOptions options977,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUserDefinedData
                    * @param saveUserDefinedData980
                
         */

         
                     public void saveUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity981,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input982,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions983)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUserDefinedData
                * @param saveUserDefinedData980
            
          */
        public void startsaveUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity981,com.freshdirect.routing.proxy.stub.transportation.UserDefinedData[] input982,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tmzOptions983,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUnassignsByCriteria
                    * @param retrieveUnassignsByCriteria985
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveUnassignsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria986,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options987)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUnassignsByCriteria
                * @param retrieveUnassignsByCriteria985
            
          */
        public void startretrieveUnassignsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria986,com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options987,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutes
                    * @param schedulerRebuildRoutes990
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity991)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutes
                * @param schedulerRebuildRoutes990
            
          */
        public void startschedulerRebuildRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity991,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeletePlanningLocationExtensions
                    * @param deletePlanningLocationExtensions994
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] deletePlanningLocationExtensions(

                        java.lang.String regionId995,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions996)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeletePlanningLocationExtensions
                * @param deletePlanningLocationExtensions994
            
          */
        public void startdeletePlanningLocationExtensions(

            java.lang.String regionId995,com.freshdirect.routing.proxy.stub.transportation.PlanningLocationExtension[] locationExtensions996,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DescribeAuthenticaitonPolicy
                    * @param describeAuthenticaitonPolicy999
                
         */

         
                     public java.lang.String describeAuthenticaitonPolicy(

                        java.lang.String localeId0)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DescribeAuthenticaitonPolicy
                * @param describeAuthenticaitonPolicy999
            
          */
        public void startdescribeAuthenticaitonPolicy(

            java.lang.String localeId0,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop3
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop3
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCancelOrder
                    * @param schedulerCancelOrder6
                
         */

         
                     public void schedulerCancelOrder(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity7,java.lang.String orderNumberXML8)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCancelOrder
                * @param schedulerCancelOrder6
            
          */
        public void startschedulerCancelOrder(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity7,java.lang.String orderNumberXML8,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UpdateRoutePosition
                    * @param updateRoutePosition10
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateRoutePosition(

                        com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info11)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UpdateRoutePosition
                * @param updateRoutePosition10
            
          */
        public void startupdateRoutePosition(

            com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info11,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerSaveDeliveryWindow
                    * @param schedulerSaveDeliveryWindow14
                
         */

         
                     public boolean schedulerSaveDeliveryWindow(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity15,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options16)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerSaveDeliveryWindow
                * @param schedulerSaveDeliveryWindow14
            
          */
        public void startschedulerSaveDeliveryWindow(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity15,com.freshdirect.routing.proxy.stub.transportation.SchedulerSaveDeliveryWindowOptions options16,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__CreateAdminRoute
                    * @param createAdminRoute19
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteIdentity createAdminRoute(

                        com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity20,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity21)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__CreateAdminRoute
                * @param createAdminRoute19
            
          */
        public void startcreateAdminRoute(

            com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity employeeIdentity20,com.freshdirect.routing.proxy.stub.transportation.LocationIdentity locationIdentity21,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                    * @param retrieveRoutingImportOrdersByCriteria24
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] retrieveRoutingImportOrdersByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria25,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions26)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
                * @param retrieveRoutingImportOrdersByCriteria24
            
          */
        public void startretrieveRoutingImportOrdersByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria25,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions26,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SuggestRoute
                    * @param suggestRoute29
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] suggestRoute(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop30,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options31)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SuggestRoute
                * @param suggestRoute29
            
          */
        public void startsuggestRoute(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop30,com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options31,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__StartRoute
                    * @param startRoute34
                
         */

         
                     public void startRoute(

                        com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info35)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__StartRoute
                * @param startRoute34
            
          */
        public void startstartRoute(

            com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info35,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAccountTypesByCriteria
                    * @param retrieveAccountTypesByCriteria37
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AccountType[] retrieveAccountTypesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.AccountTypeCriteria criteria38)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAccountTypesByCriteria
                * @param retrieveAccountTypesByCriteria37
            
          */
        public void startretrieveAccountTypesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.AccountTypeCriteria criteria38,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerUnload
                    * @param schedulerUnload41
                
         */

         
                     public void schedulerUnload(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity42)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerUnload
                * @param schedulerUnload41
            
          */
        public void startschedulerUnload(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity42,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                    * @param schedulerExcludeCutoffRoutes44
                
         */

         
                     public void schedulerExcludeCutoffRoutes(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity45,boolean excludeXML46)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerExcludeCutoffRoutes
                * @param schedulerExcludeCutoffRoutes44
            
          */
        public void startschedulerExcludeCutoffRoutes(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity45,boolean excludeXML46,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__UnassignStop
                    * @param unassignStop48
                
         */

         
                     public void unassignStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop49)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__UnassignStop
                * @param unassignStop48
            
          */
        public void startunassignStop(

            com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop49,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                    * @param retrieveRouteDailyStatsByCriteria51
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRouteDailyStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria52,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options53)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
                * @param retrieveRouteDailyStatsByCriteria51
            
          */
        public void startretrieveRouteDailyStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria52,com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options53,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                    * @param retrievePlanningSessionPropertiesByIdentity56
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession retrievePlanningSessionPropertiesByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity57)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByIdentity
                * @param retrievePlanningSessionPropertiesByIdentity56
            
          */
        public void startretrievePlanningSessionPropertiesByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionIdentity identity57,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__BuildRoutingDriverDirections
                    * @param buildRoutingDriverDirections60
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DirectionData buildRoutingDriverDirections(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity61)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__BuildRoutingDriverDirections
                * @param buildRoutingDriverDirections60
            
          */
        public void startbuildRoutingDriverDirections(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity routeIdentity61,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveLocations
                    * @param saveLocations64
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocations(

                        com.freshdirect.routing.proxy.stub.transportation.Location[] locations65)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveLocations
                * @param saveLocations64
            
          */
        public void startsaveLocations(

            com.freshdirect.routing.proxy.stub.transportation.Location[] locations65,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveSapShipments
                    * @param saveSapShipments68
                
         */

         
                     public boolean saveSapShipments(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid69,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments70)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveSapShipments
                * @param saveSapShipments68
            
          */
        public void startsaveSapShipments(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity rsid69,com.freshdirect.routing.proxy.stub.transportation.SapShipment[] sapShipments70,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRegionConfig
                    * @param retrieveRegionConfig73
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveRegionConfig(

                        java.lang.String applicationID74,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity75,java.lang.String configGroupID76)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRegionConfig
                * @param retrieveRegionConfig73
            
          */
        public void startretrieveRegionConfig(

            java.lang.String applicationID74,com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity75,java.lang.String configGroupID76,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerReloadWaveInstances
                    * @param schedulerReloadWaveInstances79
                
         */

         
                     public boolean schedulerReloadWaveInstances(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity80,com.freshdirect.routing.proxy.stub.transportation.SchedulerReloadWaveInstancesOptions options81)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerReloadWaveInstances
                * @param schedulerReloadWaveInstances79
            
          */
        public void startschedulerReloadWaveInstances(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity80,com.freshdirect.routing.proxy.stub.transportation.SchedulerReloadWaveInstancesOptions options81,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                    * @param retrieveRoutingLocationsWithOrdersEx84
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrdersEx(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity85,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options86)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
                * @param retrieveRoutingLocationsWithOrdersEx84
            
          */
        public void startretrieveRoutingLocationsWithOrdersEx(

            com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity85,com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options86,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRouteSurveyQuestions
                    * @param retrieveRouteSurveyQuestions89
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SurveyQuestionsResult retrieveRouteSurveyQuestions(

                        com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity90,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt91)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRouteSurveyQuestions
                * @param retrieveRouteSurveyQuestions89
            
          */
        public void startretrieveRouteSurveyQuestions(

            com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity90,com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt91,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRegion
                    * @param saveRegion94
                
         */

         
                     public void saveRegion(

                        com.freshdirect.routing.proxy.stub.transportation.Region region95)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRegion
                * @param saveRegion94
            
          */
        public void startsaveRegion(

            com.freshdirect.routing.proxy.stub.transportation.Region region95,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                    * @param schedulerRetrieveRoutesByCriteria97
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRoute[] schedulerRetrieveRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria98,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options99)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveRoutesByCriteria
                * @param schedulerRetrieveRoutesByCriteria97
            
          */
        public void startschedulerRetrieveRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteCriteria criteria98,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaRouteRetrieveOptions options99,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteSapShipment
                    * @param deleteSapShipment102
                
         */

         
                     public boolean deleteSapShipment(

                        com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid103)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteSapShipment
                * @param deleteSapShipment102
            
          */
        public void startdeleteSapShipment(

            com.freshdirect.routing.proxy.stub.transportation.SapShipmentIdentity ssid103,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveUnassigned
                    * @param saveUnassigned106
                
         */

         
                     public void saveUnassigned(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop107,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options108)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveUnassigned
                * @param saveUnassigned106
            
          */
        public void startsaveUnassigned(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop107,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options108,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrders
                    * @param saveRoutingImportOrders110
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrders(

                        java.lang.String regionId111,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders112,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions113)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrders
                * @param saveRoutingImportOrders110
            
          */
        public void startsaveRoutingImportOrders(

            java.lang.String regionId111,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders112,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions113,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                    * @param retrievePositionHistoryBlocksByCriteria116
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryBlocksByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria117)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePositionHistoryBlocksByCriteria
                * @param retrievePositionHistoryBlocksByCriteria116
            
          */
        public void startretrievePositionHistoryBlocksByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria117,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SequenceStop
                    * @param sequenceStop120
                
         */

         
                     public void sequenceStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info121)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SequenceStop
                * @param sequenceStop120
            
          */
        public void startsequenceStop(

            com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info121,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveReportByIdentity
                    * @param retrieveReportByIdentity123
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.Report retrieveReportByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity124)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveReportByIdentity
                * @param retrieveReportByIdentity123
            
          */
        public void startretrieveReportByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity124,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DeleteUserDefinedData
                    * @param deleteUserDefinedData127
                
         */

         
                     public void deleteUserDefinedData(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity128,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input129)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DeleteUserDefinedData
                * @param deleteUserDefinedData127
            
          */
        public void startdeleteUserDefinedData(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableIdentity tableIdentity128,com.freshdirect.routing.proxy.stub.transportation.UserDefinedDataIdentity[] input129,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveLocationServiceStatsByCriteria
                    * @param retrieveLocationServiceStatsByCriteria131
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.LocationServiceStats[] retrieveLocationServiceStatsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.LocationServiceStatsCriteria criteria132)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveLocationServiceStatsByCriteria
                * @param retrieveLocationServiceStatsByCriteria131
            
          */
        public void startretrieveLocationServiceStatsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.LocationServiceStatsCriteria criteria132,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                    * @param retrieveUserDefinedTablesByCriteria135
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.UserDefinedTable[] retrieveUserDefinedTablesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria136)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveUserDefinedTablesByCriteria
                * @param retrieveUserDefinedTablesByCriteria135
            
          */
        public void startretrieveUserDefinedTablesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.UserDefinedTableCriteria tableCriteria136,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                    * @param retrieveRoutingRoutesByCriteria139
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] retrieveRoutingRoutesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria140,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options141)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
                * @param retrieveRoutingRoutesByCriteria139
            
          */
        public void startretrieveRoutingRoutesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria140,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options141,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                    * @param retrieveRoutingStopsByCriteria144
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingStopsByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria145,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options146)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRoutingStopsByCriteria
                * @param retrieveRoutingStopsByCriteria144
            
          */
        public void startretrieveRoutingStopsByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria145,com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options146,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerBulkReserveOrders
                    * @param schedulerBulkReserveOrders149
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerBulkReserveOrders(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity150,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders151,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options152)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerBulkReserveOrders
                * @param schedulerBulkReserveOrders149
            
          */
        public void startschedulerBulkReserveOrders(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity150,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders151,com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options152,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                    * @param retrieveDepotSkusAvailabilitiesByCriteria155
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] retrieveDepotSkusAvailabilitiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria156)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDepotSkusAvailabilitiesByCriteria
                * @param retrieveDepotSkusAvailabilitiesByCriteria155
            
          */
        public void startretrieveDepotSkusAvailabilitiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailabilityCriteria criteria156,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRICRegionsByUser
                    * @param retrieveRICRegionsByUser159
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RICRegionsWithPurchaseInfo retrieveRICRegionsByUser(

                        java.lang.String userId160)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRICRegionsByUser
                * @param retrieveRICRegionsByUser159
            
          */
        public void startretrieveRICRegionsByUser(

            java.lang.String userId160,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__MassStopSequence
                    * @param massStopSequence163
                
         */

         
                     public void massStopSequence(

                        com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info164)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__MassStopSequence
                * @param massStopSequence163
            
          */
        public void startmassStopSequence(

            com.freshdirect.routing.proxy.stub.transportation.MassStopSequenceInfo info164,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                    * @param retrieveDefaultPlanningSessionProperties166
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSessionProperties retrieveDefaultPlanningSessionProperties(

                        java.lang.String regionId167)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveDefaultPlanningSessionProperties
                * @param retrieveDefaultPlanningSessionProperties166
            
          */
        public void startretrieveDefaultPlanningSessionProperties(

            java.lang.String regionId167,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                    * @param schedulerRetrieveOrderByIdentity170
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder schedulerRetrieveOrderByIdentity(

                        com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity171,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options172)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
                * @param schedulerRetrieveOrderByIdentity170
            
          */
        public void startschedulerRetrieveOrderByIdentity(

            com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity171,com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options172,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                    * @param schedulerCalculateDeliveryWindowMetrics175
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics[] schedulerCalculateDeliveryWindowMetrics(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity176,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options177)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerCalculateDeliveryWindowMetrics
                * @param schedulerCalculateDeliveryWindowMetrics175
            
          */
        public void startschedulerCalculateDeliveryWindowMetrics(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity176,com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetricsOptions options177,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SchedulerRebuildRoutesEx
                    * @param schedulerRebuildRoutesEx180
                
         */

         
                     public java.lang.String[] schedulerRebuildRoutesEx(

                        com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity181,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options182)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SchedulerRebuildRoutesEx
                * @param schedulerRebuildRoutesEx180
            
          */
        public void startschedulerRebuildRoutesEx(

            com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity181,com.freshdirect.routing.proxy.stub.transportation.SchedulerRebuildRoutesExOptions options182,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                    * @param retrievePlanningSessionPropertiesByCriteria185
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.PlanningSession[] retrievePlanningSessionPropertiesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria186)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrievePlanningSessionPropertiesByCriteria
                * @param retrievePlanningSessionPropertiesByCriteria185
            
          */
        public void startretrievePlanningSessionPropertiesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.PlanningSessionCriteria criteria186,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveDepotSkusAvailabilities
                    * @param saveDepotSkusAvailabilities189
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] saveDepotSkusAvailabilities(

                        com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus190)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveDepotSkusAvailabilities
                * @param saveDepotSkusAvailabilities189
            
          */
        public void startsaveDepotSkusAvailabilities(

            com.freshdirect.routing.proxy.stub.transportation.DepotSkusAvailability[] depotSkus190,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__DepartStop
                    * @param departStop193
                
         */

         
                     public void departStop(

                        com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info194)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__DepartStop
                * @param departStop193
            
          */
        public void startdepartStop(

            com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info194,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveEmployee
                    * @param saveEmployee196
                
         */

         
                     public void saveEmployee(

                        com.freshdirect.routing.proxy.stub.transportation.Employee employee197)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveEmployee
                * @param saveEmployee196
            
          */
        public void startsaveEmployee(

            com.freshdirect.routing.proxy.stub.transportation.Employee employee197,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveStop
                    * @param saveStop199
                
         */

         
                     public void saveStop(

                        com.freshdirect.routing.proxy.stub.transportation.Stop stop200,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions201,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options202)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveStop
                * @param saveStop199
            
          */
        public void startsaveStop(

            com.freshdirect.routing.proxy.stub.transportation.Stop stop200,com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions201,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options202,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                    * @param retrieveAutoArriveDepartOptions204
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.AutoArriveDepartOptions retrieveAutoArriveDepartOptions(

                        com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity205)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveAutoArriveDepartOptions
                * @param retrieveAutoArriveDepartOptions204
            
          */
        public void startretrieveAutoArriveDepartOptions(

            com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity205,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__AuthenticateUser
                    * @param authenticateUser208
                
         */

         
                     public int authenticateUser(

                        java.lang.String userID209,java.lang.String password210)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__AuthenticateUser
                * @param authenticateUser208
            
          */
        public void startauthenticateUser(

            java.lang.String userID209,java.lang.String password210,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__SaveRoutingImportOrdersEx
                    * @param saveRoutingImportOrdersEx213
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrdersEx(

                        java.lang.String regionId214,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders215,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions216,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions217)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__SaveRoutingImportOrdersEx
                * @param saveRoutingImportOrdersEx213
            
          */
        public void startsaveRoutingImportOrdersEx(

            java.lang.String regionId214,com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders215,com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions216,com.freshdirect.routing.proxy.stub.transportation.SaveRoutingImportOrdersExOptions importOptions217,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                    * @param retrieveStopCancelCodesByCriteria220
                
         */

         
                     public com.freshdirect.routing.proxy.stub.transportation.StopCancelCode[] retrieveStopCancelCodesByCriteria(

                        com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria221)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveStopCancelCodesByCriteria
                * @param retrieveStopCancelCodesByCriteria220
            
          */
        public void startretrieveStopCancelCodesByCriteria(

            com.freshdirect.routing.proxy.stub.transportation.StopCancelCodeCriteria criteria221,

            final com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    