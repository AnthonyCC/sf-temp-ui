
/**
 * TransportationWebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.routing.proxy.stub.transportation;

    /**
     *  TransportationWebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class TransportationWebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public TransportationWebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public TransportationWebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for retrieveQuantityReasonCodesByCriteria method
            * override this method for handling normal response from retrieveQuantityReasonCodesByCriteria operation
            */
           public void receiveResultretrieveQuantityReasonCodesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.QuantityReasonCode[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveQuantityReasonCodesByCriteria operation
           */
            public void receiveErrorretrieveQuantityReasonCodesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingRoutesByCriteria method
            * override this method for handling normal response from retrieveRoutingRoutesByCriteria operation
            */
           public void receiveResultretrieveRoutingRoutesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingRoutesByCriteria operation
           */
            public void receiveErrorretrieveRoutingRoutesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveRoutingImportOrders method
            * override this method for handling normal response from saveRoutingImportOrders operation
            */
           public void receiveResultsaveRoutingImportOrders(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveRoutingImportOrders operation
           */
            public void receiveErrorsaveRoutingImportOrders(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveUnassignsByCriteria method
            * override this method for handling normal response from retrieveUnassignsByCriteria operation
            */
           public void receiveResultretrieveUnassignsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Stop[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveUnassignsByCriteria operation
           */
            public void receiveErrorretrieveUnassignsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateDeliveryDetails method
            * override this method for handling normal response from updateDeliveryDetails operation
            */
           public void receiveResultupdateDeliveryDetails(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateDeliveryDetails operation
           */
            public void receiveErrorupdateDeliveryDetails(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveAssignedDrivers method
            * override this method for handling normal response from retrieveAssignedDrivers operation
            */
           public void receiveResultretrieveAssignedDrivers(
                    com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveAssignedDrivers operation
           */
            public void receiveErrorretrieveAssignedDrivers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrievePositionHistoryByCriteria method
            * override this method for handling normal response from retrievePositionHistoryByCriteria operation
            */
           public void receiveResultretrievePositionHistoryByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrievePositionHistoryByCriteria operation
           */
            public void receiveErrorretrievePositionHistoryByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveStopSignature method
            * override this method for handling normal response from saveStopSignature operation
            */
           public void receiveResultsaveStopSignature(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveStopSignature operation
           */
            public void receiveErrorsaveStopSignature(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerUpdateOrder method
            * override this method for handling normal response from schedulerUpdateOrder operation
            */
           public void receiveResultschedulerUpdateOrder(
                    boolean result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerUpdateOrder operation
           */
            public void receiveErrorschedulerUpdateOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveReportByIdentity method
            * override this method for handling normal response from retrieveReportByIdentity operation
            */
           public void receiveResultretrieveReportByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.Report result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveReportByIdentity operation
           */
            public void receiveErrorretrieveReportByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerDeleteDeliveryWindow method
            * override this method for handling normal response from schedulerDeleteDeliveryWindow operation
            */
           public void receiveResultschedulerDeleteDeliveryWindow(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerDeleteDeliveryWindow operation
           */
            public void receiveErrorschedulerDeleteDeliveryWindow(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelStop method
            * override this method for handling normal response from cancelStop operation
            */
           public void receiveResultcancelStop(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelStop operation
           */
            public void receiveErrorcancelStop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingImportOrderByIdentity method
            * override this method for handling normal response from retrieveRoutingImportOrderByIdentity operation
            */
           public void receiveResultretrieveRoutingImportOrderByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingImportOrderByIdentity operation
           */
            public void receiveErrorretrieveRoutingImportOrderByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerPurge method
            * override this method for handling normal response from schedulerPurge operation
            */
           public void receiveResultschedulerPurge(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerPurge operation
           */
            public void receiveErrorschedulerPurge(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRouteByIdentity method
            * override this method for handling normal response from retrieveRouteByIdentity operation
            */
           public void receiveResultretrieveRouteByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.Route result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRouteByIdentity operation
           */
            public void receiveErrorretrieveRouteByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveLocationByIdentityEx method
            * override this method for handling normal response from retrieveLocationByIdentityEx operation
            */
           public void receiveResultretrieveLocationByIdentityEx(
                    com.freshdirect.routing.proxy.stub.transportation.Location result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveLocationByIdentityEx operation
           */
            public void receiveErrorretrieveLocationByIdentityEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerAnalyzeOrder method
            * override this method for handling normal response from schedulerAnalyzeOrder operation
            */
           public void receiveResultschedulerAnalyzeOrder(
                    com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerAnalyzeOrder operation
           */
            public void receiveErrorschedulerAnalyzeOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRouteNotesByCriteria method
            * override this method for handling normal response from retrieveRouteNotesByCriteria operation
            */
           public void receiveResultretrieveRouteNotesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RouteNote[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRouteNotesByCriteria operation
           */
            public void receiveErrorretrieveRouteNotesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveLocations method
            * override this method for handling normal response from saveLocations operation
            */
           public void receiveResultsaveLocations(
                    com.freshdirect.routing.proxy.stub.transportation.Location[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveLocations operation
           */
            public void receiveErrorsaveLocations(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerRetrieveFeederRoutes method
            * override this method for handling normal response from schedulerRetrieveFeederRoutes operation
            */
           public void receiveResultschedulerRetrieveFeederRoutes(
                    com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerRetrieveFeederRoutes operation
           */
            public void receiveErrorschedulerRetrieveFeederRoutes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for arriveDestination method
            * override this method for handling normal response from arriveDestination operation
            */
           public void receiveResultarriveDestination(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from arriveDestination operation
           */
            public void receiveErrorarriveDestination(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRouteDailyStatsByCriteria method
            * override this method for handling normal response from retrieveRouteDailyStatsByCriteria operation
            */
           public void receiveResultretrieveRouteDailyStatsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRouteDailyStatsByCriteria operation
           */
            public void receiveErrorretrieveRouteDailyStatsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveStopCancelCodesByCriteria method
            * override this method for handling normal response from retrieveStopCancelCodesByCriteria operation
            */
           public void receiveResultretrieveStopCancelCodesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.StopCancelCode[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveStopCancelCodesByCriteria operation
           */
            public void receiveErrorretrieveStopCancelCodesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveReport method
            * override this method for handling normal response from saveReport operation
            */
           public void receiveResultsaveReport(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveReport operation
           */
            public void receiveErrorsaveReport(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRegionsByCriteria method
            * override this method for handling normal response from retrieveRegionsByCriteria operation
            */
           public void receiveResultretrieveRegionsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Region[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRegionsByCriteria operation
           */
            public void receiveErrorretrieveRegionsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveDefaultRoutingSessionProperties method
            * override this method for handling normal response from retrieveDefaultRoutingSessionProperties operation
            */
           public void receiveResultretrieveDefaultRoutingSessionProperties(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveDefaultRoutingSessionProperties operation
           */
            public void receiveErrorretrieveDefaultRoutingSessionProperties(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveRouteSurveyResults method
            * override this method for handling normal response from saveRouteSurveyResults operation
            */
           public void receiveResultsaveRouteSurveyResults(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveRouteSurveyResults operation
           */
            public void receiveErrorsaveRouteSurveyResults(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for buildRoutingRouteNetMatrix method
            * override this method for handling normal response from buildRoutingRouteNetMatrix operation
            */
           public void receiveResultbuildRoutingRouteNetMatrix(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buildRoutingRouteNetMatrix operation
           */
            public void receiveErrorbuildRoutingRouteNetMatrix(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for returnFault method
            * override this method for handling normal response from returnFault operation
            */
           public void receiveResultreturnFault(
                    com.freshdirect.routing.proxy.stub.transportation.Fault result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from returnFault operation
           */
            public void receiveErrorreturnFault(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for suggestRoute method
            * override this method for handling normal response from suggestRoute operation
            */
           public void receiveResultsuggestRoute(
                    com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from suggestRoute operation
           */
            public void receiveErrorsuggestRoute(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveNotificationsByCriteria method
            * override this method for handling normal response from retrieveNotificationsByCriteria operation
            */
           public void receiveResultretrieveNotificationsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Notification[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveNotificationsByCriteria operation
           */
            public void receiveErrorretrieveNotificationsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for calculateTimeDist method
            * override this method for handling normal response from calculateTimeDist operation
            */
           public void receiveResultcalculateTimeDist(
                    com.freshdirect.routing.proxy.stub.transportation.TimeDistResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from calculateTimeDist operation
           */
            public void receiveErrorcalculateTimeDist(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveStopSignature method
            * override this method for handling normal response from retrieveStopSignature operation
            */
           public void receiveResultretrieveStopSignature(
                    javax.activation.DataHandler result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveStopSignature operation
           */
            public void receiveErrorretrieveStopSignature(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerMovableOrders method
            * override this method for handling normal response from schedulerMovableOrders operation
            */
           public void receiveResultschedulerMovableOrders(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerMovableOrders operation
           */
            public void receiveErrorschedulerMovableOrders(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRICRegionsByUser method
            * override this method for handling normal response from retrieveRICRegionsByUser operation
            */
           public void receiveResultretrieveRICRegionsByUser(
                    com.freshdirect.routing.proxy.stub.transportation.RICRegionsWithPurchaseInfo result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRICRegionsByUser operation
           */
            public void receiveErrorretrieveRICRegionsByUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveLocationsByCriteriaEx method
            * override this method for handling normal response from retrieveLocationsByCriteriaEx operation
            */
           public void receiveResultretrieveLocationsByCriteriaEx(
                    com.freshdirect.routing.proxy.stub.transportation.Location[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveLocationsByCriteriaEx operation
           */
            public void receiveErrorretrieveLocationsByCriteriaEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrievePermissionsForUser method
            * override this method for handling normal response from retrievePermissionsForUser operation
            */
           public void receiveResultretrievePermissionsForUser(
                    com.freshdirect.routing.proxy.stub.transportation.UserPermissions result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrievePermissionsForUser operation
           */
            public void receiveErrorretrievePermissionsForUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRouteSurveyQuestions method
            * override this method for handling normal response from retrieveRouteSurveyQuestions operation
            */
           public void receiveResultretrieveRouteSurveyQuestions(
                    com.freshdirect.routing.proxy.stub.transportation.RetrieveRouteSurveyQuestionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRouteSurveyQuestions operation
           */
            public void receiveErrorretrieveRouteSurveyQuestions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveEquipmentByCriteria method
            * override this method for handling normal response from retrieveEquipmentByCriteria operation
            */
           public void receiveResultretrieveEquipmentByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Equipment[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveEquipmentByCriteria operation
           */
            public void receiveErrorretrieveEquipmentByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerBalanceRoutes method
            * override this method for handling normal response from schedulerBalanceRoutes operation
            */
           public void receiveResultschedulerBalanceRoutes(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerBalanceRoutes operation
           */
            public void receiveErrorschedulerBalanceRoutes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveUnassigned method
            * override this method for handling normal response from saveUnassigned operation
            */
           public void receiveResultsaveUnassigned(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveUnassigned operation
           */
            public void receiveErrorsaveUnassigned(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for removeRoute method
            * override this method for handling normal response from removeRoute operation
            */
           public void receiveResultremoveRoute(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removeRoute operation
           */
            public void receiveErrorremoveRoute(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveNotificationsByRecipientIdentity method
            * override this method for handling normal response from retrieveNotificationsByRecipientIdentity operation
            */
           public void receiveResultretrieveNotificationsByRecipientIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.Notification[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveNotificationsByRecipientIdentity operation
           */
            public void receiveErrorretrieveNotificationsByRecipientIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutesByCriteria method
            * override this method for handling normal response from retrieveRoutesByCriteria operation
            */
           public void receiveResultretrieveRoutesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Route[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutesByCriteria operation
           */
            public void receiveErrorretrieveRoutesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerOptimizeOrdersEx method
            * override this method for handling normal response from schedulerOptimizeOrdersEx operation
            */
           public void receiveResultschedulerOptimizeOrdersEx(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerOptimizeOrdersEx operation
           */
            public void receiveErrorschedulerOptimizeOrdersEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveRegion method
            * override this method for handling normal response from saveRegion operation
            */
           public void receiveResultsaveRegion(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveRegion operation
           */
            public void receiveErrorsaveRegion(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRegionByIdentity method
            * override this method for handling normal response from retrieveRegionByIdentity operation
            */
           public void receiveResultretrieveRegionByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.Region result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRegionByIdentity operation
           */
            public void receiveErrorretrieveRegionByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveReportsByCriteria method
            * override this method for handling normal response from retrieveReportsByCriteria operation
            */
           public void receiveResultretrieveReportsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Report[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveReportsByCriteria operation
           */
            public void receiveErrorretrieveReportsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingLocationsWithOrders method
            * override this method for handling normal response from retrieveRoutingLocationsWithOrders operation
            */
           public void receiveResultretrieveRoutingLocationsWithOrders(
                    com.freshdirect.routing.proxy.stub.transportation.Location[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingLocationsWithOrders operation
           */
            public void receiveErrorretrieveRoutingLocationsWithOrders(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveUserByUserID method
            * override this method for handling normal response from retrieveUserByUserID operation
            */
           public void receiveResultretrieveUserByUserID(
                    com.freshdirect.routing.proxy.stub.transportation.User result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveUserByUserID operation
           */
            public void receiveErrorretrieveUserByUserID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveEmployeeByIdentity method
            * override this method for handling normal response from retrieveEmployeeByIdentity operation
            */
           public void receiveResultretrieveEmployeeByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.Employee result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveEmployeeByIdentity operation
           */
            public void receiveErrorretrieveEmployeeByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteUnassigned method
            * override this method for handling normal response from deleteUnassigned operation
            */
           public void receiveResultdeleteUnassigned(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteUnassigned operation
           */
            public void receiveErrordeleteUnassigned(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createRoutingSession method
            * override this method for handling normal response from createRoutingSession operation
            */
           public void receiveResultcreateRoutingSession(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createRoutingSession operation
           */
            public void receiveErrorcreateRoutingSession(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveUndeliverableStopCodesByCriteria method
            * override this method for handling normal response from retrieveUndeliverableStopCodesByCriteria operation
            */
           public void receiveResultretrieveUndeliverableStopCodesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.UndeliverableStopCode[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveUndeliverableStopCodesByCriteria operation
           */
            public void receiveErrorretrieveUndeliverableStopCodesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for authenticateUser method
            * override this method for handling normal response from authenticateUser operation
            */
           public void receiveResultauthenticateUser(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from authenticateUser operation
           */
            public void receiveErrorauthenticateUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for unlockNotifications method
            * override this method for handling normal response from unlockNotifications operation
            */
           public void receiveResultunlockNotifications(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from unlockNotifications operation
           */
            public void receiveErrorunlockNotifications(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteRoutingSession method
            * override this method for handling normal response from deleteRoutingSession operation
            */
           public void receiveResultdeleteRoutingSession(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteRoutingSession operation
           */
            public void receiveErrordeleteRoutingSession(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveUserConfig method
            * override this method for handling normal response from saveUserConfig operation
            */
           public void receiveResultsaveUserConfig(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveUserConfig operation
           */
            public void receiveErrorsaveUserConfig(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerRetrieveOrdersByCriteria method
            * override this method for handling normal response from schedulerRetrieveOrdersByCriteria operation
            */
           public void receiveResultschedulerRetrieveOrdersByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerRetrieveOrdersByCriteria operation
           */
            public void receiveErrorschedulerRetrieveOrdersByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for versionInformation method
            * override this method for handling normal response from versionInformation operation
            */
           public void receiveResultversionInformation(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from versionInformation operation
           */
            public void receiveErrorversionInformation(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveUserConfig method
            * override this method for handling normal response from retrieveUserConfig operation
            */
           public void receiveResultretrieveUserConfig(
                    com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveUserConfig operation
           */
            public void receiveErrorretrieveUserConfig(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveEmployeesByCriteria method
            * override this method for handling normal response from retrieveEmployeesByCriteria operation
            */
           public void receiveResultretrieveEmployeesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Employee[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveEmployeesByCriteria operation
           */
            public void receiveErrorretrieveEmployeesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for departOrigin method
            * override this method for handling normal response from departOrigin operation
            */
           public void receiveResultdepartOrigin(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from departOrigin operation
           */
            public void receiveErrordepartOrigin(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveStop method
            * override this method for handling normal response from saveStop operation
            */
           public void receiveResultsaveStop(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveStop operation
           */
            public void receiveErrorsaveStop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerRemoveFromServer method
            * override this method for handling normal response from schedulerRemoveFromServer operation
            */
           public void receiveResultschedulerRemoveFromServer(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerRemoveFromServer operation
           */
            public void receiveErrorschedulerRemoveFromServer(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for unassignStop method
            * override this method for handling normal response from unassignStop operation
            */
           public void receiveResultunassignStop(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from unassignStop operation
           */
            public void receiveErrorunassignStop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerCancelOrder method
            * override this method for handling normal response from schedulerCancelOrder operation
            */
           public void receiveResultschedulerCancelOrder(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerCancelOrder operation
           */
            public void receiveErrorschedulerCancelOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteNotifications method
            * override this method for handling normal response from deleteNotifications operation
            */
           public void receiveResultdeleteNotifications(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteNotifications operation
           */
            public void receiveErrordeleteNotifications(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingRouteByIdentity method
            * override this method for handling normal response from retrieveRoutingRouteByIdentity operation
            */
           public void receiveResultretrieveRoutingRouteByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingRoute result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingRouteByIdentity operation
           */
            public void receiveErrorretrieveRoutingRouteByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveEmployee method
            * override this method for handling normal response from saveEmployee operation
            */
           public void receiveResultsaveEmployee(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveEmployee operation
           */
            public void receiveErrorsaveEmployee(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveEquipmentTypeByIdentity method
            * override this method for handling normal response from retrieveEquipmentTypeByIdentity operation
            */
           public void receiveResultretrieveEquipmentTypeByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.EquipmentType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveEquipmentTypeByIdentity operation
           */
            public void receiveErrorretrieveEquipmentTypeByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerRetrieveDeliveryWaveInstancesByCriteria method
            * override this method for handling normal response from schedulerRetrieveDeliveryWaveInstancesByCriteria operation
            */
           public void receiveResultschedulerRetrieveDeliveryWaveInstancesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.DeliveryWaveInstance[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerRetrieveDeliveryWaveInstancesByCriteria operation
           */
            public void receiveErrorschedulerRetrieveDeliveryWaveInstancesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for surveyResponse method
            * override this method for handling normal response from surveyResponse operation
            */
           public void receiveResultsurveyResponse(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from surveyResponse operation
           */
            public void receiveErrorsurveyResponse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveStopSurveyQuestions method
            * override this method for handling normal response from retrieveStopSurveyQuestions operation
            */
           public void receiveResultretrieveStopSurveyQuestions(
                    com.freshdirect.routing.proxy.stub.transportation.RetrieveStopSurveyQuestionsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveStopSurveyQuestions operation
           */
            public void receiveErrorretrieveStopSurveyQuestions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveDutyPeriodsByCriteria method
            * override this method for handling normal response from retrieveDutyPeriodsByCriteria operation
            */
           public void receiveResultretrieveDutyPeriodsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveDutyPeriodsByCriteria operation
           */
            public void receiveErrorretrieveDutyPeriodsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerSaveDeliveryWindow method
            * override this method for handling normal response from schedulerSaveDeliveryWindow operation
            */
           public void receiveResultschedulerSaveDeliveryWindow(
                    boolean result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerSaveDeliveryWindow operation
           */
            public void receiveErrorschedulerSaveDeliveryWindow(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveEquipmentByIdentity method
            * override this method for handling normal response from retrieveEquipmentByIdentity operation
            */
           public void receiveResultretrieveEquipmentByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.Equipment result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveEquipmentByIdentity operation
           */
            public void receiveErrorretrieveEquipmentByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendTextMessageToDriver method
            * override this method for handling normal response from sendTextMessageToDriver operation
            */
           public void receiveResultsendTextMessageToDriver(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendTextMessageToDriver operation
           */
            public void receiveErrorsendTextMessageToDriver(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerRetrieveOrderByIdentity method
            * override this method for handling normal response from schedulerRetrieveOrderByIdentity operation
            */
           public void receiveResultschedulerRetrieveOrderByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerRetrieveOrderByIdentity operation
           */
            public void receiveErrorschedulerRetrieveOrderByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveAssignedEquipment method
            * override this method for handling normal response from retrieveAssignedEquipment operation
            */
           public void receiveResultretrieveAssignedEquipment(
                    com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveAssignedEquipment operation
           */
            public void receiveErrorretrieveAssignedEquipment(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveStopSurveyResults method
            * override this method for handling normal response from retrieveStopSurveyResults operation
            */
           public void receiveResultretrieveStopSurveyResults(
                    com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveStopSurveyResults operation
           */
            public void receiveErrorretrieveStopSurveyResults(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveLocationByIdentity method
            * override this method for handling normal response from retrieveLocationByIdentity operation
            */
           public void receiveResultretrieveLocationByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.Location result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveLocationByIdentity operation
           */
            public void receiveErrorretrieveLocationByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveRoutingImportOrdersEx method
            * override this method for handling normal response from saveRoutingImportOrdersEx operation
            */
           public void receiveResultsaveRoutingImportOrdersEx(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveRoutingImportOrdersEx operation
           */
            public void receiveErrorsaveRoutingImportOrdersEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerExtendOrderReservation method
            * override this method for handling normal response from schedulerExtendOrderReservation operation
            */
           public void receiveResultschedulerExtendOrderReservation(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerExtendOrderReservation operation
           */
            public void receiveErrorschedulerExtendOrderReservation(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRegionConfig method
            * override this method for handling normal response from retrieveRegionConfig operation
            */
           public void receiveResultretrieveRegionConfig(
                    com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRegionConfig operation
           */
            public void receiveErrorretrieveRegionConfig(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerSendRoutesToRoadnetEx method
            * override this method for handling normal response from schedulerSendRoutesToRoadnetEx operation
            */
           public void receiveResultschedulerSendRoutesToRoadnetEx(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerSendRoutesToRoadnetEx operation
           */
            public void receiveErrorschedulerSendRoutesToRoadnetEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerOptimizeOrders method
            * override this method for handling normal response from schedulerOptimizeOrders operation
            */
           public void receiveResultschedulerOptimizeOrders(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerOptimizeOrders operation
           */
            public void receiveErrorschedulerOptimizeOrders(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerSendRoutesToRoadnet method
            * override this method for handling normal response from schedulerSendRoutesToRoadnet operation
            */
           public void receiveResultschedulerSendRoutesToRoadnet(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerSendRoutesToRoadnet operation
           */
            public void receiveErrorschedulerSendRoutesToRoadnet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for assignDrivers method
            * override this method for handling normal response from assignDrivers operation
            */
           public void receiveResultassignDrivers(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from assignDrivers operation
           */
            public void receiveErrorassignDrivers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingLocationsWithOrdersEx method
            * override this method for handling normal response from retrieveRoutingLocationsWithOrdersEx operation
            */
           public void receiveResultretrieveRoutingLocationsWithOrdersEx(
                    com.freshdirect.routing.proxy.stub.transportation.Location[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingLocationsWithOrdersEx operation
           */
            public void receiveErrorretrieveRoutingLocationsWithOrdersEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveRoute method
            * override this method for handling normal response from saveRoute operation
            */
           public void receiveResultsaveRoute(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveRoute operation
           */
            public void receiveErrorsaveRoute(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveEquipment method
            * override this method for handling normal response from saveEquipment operation
            */
           public void receiveResultsaveEquipment(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveEquipment operation
           */
            public void receiveErrorsaveEquipment(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveStopByIdentity method
            * override this method for handling normal response from retrieveStopByIdentity operation
            */
           public void receiveResultretrieveStopByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.Stop result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveStopByIdentity operation
           */
            public void receiveErrorretrieveStopByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for bulkArriveDepartStop method
            * override this method for handling normal response from bulkArriveDepartStop operation
            */
           public void receiveResultbulkArriveDepartStop(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from bulkArriveDepartStop operation
           */
            public void receiveErrorbulkArriveDepartStop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingSessionsByCriteria method
            * override this method for handling normal response from retrieveRoutingSessionsByCriteria operation
            */
           public void receiveResultretrieveRoutingSessionsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingSessionsByCriteria operation
           */
            public void receiveErrorretrieveRoutingSessionsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingUnassignsByCriteria method
            * override this method for handling normal response from retrieveRoutingUnassignsByCriteria operation
            */
           public void receiveResultretrieveRoutingUnassignsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingUnassignsByCriteria operation
           */
            public void receiveErrorretrieveRoutingUnassignsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingImportOrdersByCriteria method
            * override this method for handling normal response from retrieveRoutingImportOrdersByCriteria operation
            */
           public void receiveResultretrieveRoutingImportOrdersByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingImportOrdersByCriteria operation
           */
            public void receiveErrorretrieveRoutingImportOrdersByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for nop method
            * override this method for handling normal response from nop operation
            */
           public void receiveResultnop(
                    int result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from nop operation
           */
            public void receiveErrornop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changeUserPassword method
            * override this method for handling normal response from changeUserPassword operation
            */
           public void receiveResultchangeUserPassword(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changeUserPassword operation
           */
            public void receiveErrorchangeUserPassword(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for completeRoute method
            * override this method for handling normal response from completeRoute operation
            */
           public void receiveResultcompleteRoute(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from completeRoute operation
           */
            public void receiveErrorcompleteRoute(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerExcludeCutoffRoutes method
            * override this method for handling normal response from schedulerExcludeCutoffRoutes operation
            */
           public void receiveResultschedulerExcludeCutoffRoutes(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerExcludeCutoffRoutes operation
           */
            public void receiveErrorschedulerExcludeCutoffRoutes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingOrderByIdentity method
            * override this method for handling normal response from retrieveRoutingOrderByIdentity operation
            */
           public void receiveResultretrieveRoutingOrderByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingOrder result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingOrderByIdentity operation
           */
            public void receiveErrorretrieveRoutingOrderByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for textMessage method
            * override this method for handling normal response from textMessage operation
            */
           public void receiveResulttextMessage(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from textMessage operation
           */
            public void receiveErrortextMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingStopsByCriteria method
            * override this method for handling normal response from retrieveRoutingStopsByCriteria operation
            */
           public void receiveResultretrieveRoutingStopsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingStopsByCriteria operation
           */
            public void receiveErrorretrieveRoutingStopsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerCalculateDeliveryWindowMetrics method
            * override this method for handling normal response from schedulerCalculateDeliveryWindowMetrics operation
            */
           public void receiveResultschedulerCalculateDeliveryWindowMetrics(
                    com.freshdirect.routing.proxy.stub.transportation.SchedulerDeliveryWindowMetrics[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerCalculateDeliveryWindowMetrics operation
           */
            public void receiveErrorschedulerCalculateDeliveryWindowMetrics(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteLocations method
            * override this method for handling normal response from deleteLocations operation
            */
           public void receiveResultdeleteLocations(
                    com.freshdirect.routing.proxy.stub.transportation.Location[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteLocations operation
           */
            public void receiveErrordeleteLocations(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveProductsPurchased method
            * override this method for handling normal response from retrieveProductsPurchased operation
            */
           public void receiveResultretrieveProductsPurchased(
                    com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveProductsPurchased operation
           */
            public void receiveErrorretrieveProductsPurchased(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingSourcedOrdersByCriteria method
            * override this method for handling normal response from retrieveRoutingSourcedOrdersByCriteria operation
            */
           public void receiveResultretrieveRoutingSourcedOrdersByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingSourcedOrdersByCriteria operation
           */
            public void receiveErrorretrieveRoutingSourcedOrdersByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveStopSurveyResults method
            * override this method for handling normal response from saveStopSurveyResults operation
            */
           public void receiveResultsaveStopSurveyResults(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveStopSurveyResults operation
           */
            public void receiveErrorsaveStopSurveyResults(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingRouteDailyStatsByCriteria method
            * override this method for handling normal response from retrieveRoutingRouteDailyStatsByCriteria operation
            */
           public void receiveResultretrieveRoutingRouteDailyStatsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingRouteDailyStatsByCriteria operation
           */
            public void receiveErrorretrieveRoutingRouteDailyStatsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateStopSignature method
            * override this method for handling normal response from updateStopSignature operation
            */
           public void receiveResultupdateStopSignature(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateStopSignature operation
           */
            public void receiveErrorupdateStopSignature(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveGlobalConfig method
            * override this method for handling normal response from saveGlobalConfig operation
            */
           public void receiveResultsaveGlobalConfig(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveGlobalConfig operation
           */
            public void receiveErrorsaveGlobalConfig(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerIsExcludingCutoffRoutes method
            * override this method for handling normal response from schedulerIsExcludingCutoffRoutes operation
            */
           public void receiveResultschedulerIsExcludingCutoffRoutes(
                    com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerIsExcludingCutoffRoutes operation
           */
            public void receiveErrorschedulerIsExcludingCutoffRoutes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveCannedTextMessagesByCriteria method
            * override this method for handling normal response from retrieveCannedTextMessagesByCriteria operation
            */
           public void receiveResultretrieveCannedTextMessagesByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveCannedTextMessagesByCriteria operation
           */
            public void receiveErrorretrieveCannedTextMessagesByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerReserveOrder method
            * override this method for handling normal response from schedulerReserveOrder operation
            */
           public void receiveResultschedulerReserveOrder(
                    com.freshdirect.routing.proxy.stub.transportation.ReserveResult result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerReserveOrder operation
           */
            public void receiveErrorschedulerReserveOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveLocationsByCriteria method
            * override this method for handling normal response from retrieveLocationsByCriteria operation
            */
           public void receiveResultretrieveLocationsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Location[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveLocationsByCriteria operation
           */
            public void receiveErrorretrieveLocationsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveStopsByCriteria method
            * override this method for handling normal response from retrieveStopsByCriteria operation
            */
           public void receiveResultretrieveStopsByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.Stop[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveStopsByCriteria operation
           */
            public void receiveErrorretrieveStopsByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRegionOptions method
            * override this method for handling normal response from retrieveRegionOptions operation
            */
           public void receiveResultretrieveRegionOptions(
                    com.freshdirect.routing.proxy.stub.transportation.RegionOptions result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRegionOptions operation
           */
            public void receiveErrorretrieveRegionOptions(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sequenceStop method
            * override this method for handling normal response from sequenceStop operation
            */
           public void receiveResultsequenceStop(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sequenceStop operation
           */
            public void receiveErrorsequenceStop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRouteSurveyResults method
            * override this method for handling normal response from retrieveRouteSurveyResults operation
            */
           public void receiveResultretrieveRouteSurveyResults(
                    com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRouteSurveyResults operation
           */
            public void receiveErrorretrieveRouteSurveyResults(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateRoutePosition method
            * override this method for handling normal response from updateRoutePosition operation
            */
           public void receiveResultupdateRoutePosition(
                    com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateRoutePosition operation
           */
            public void receiveErrorupdateRoutePosition(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for massStopSequence method
            * override this method for handling normal response from massStopSequence operation
            */
           public void receiveResultmassStopSequence(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from massStopSequence operation
           */
            public void receiveErrormassStopSequence(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addRICUser method
            * override this method for handling normal response from addRICUser operation
            */
           public void receiveResultaddRICUser(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addRICUser operation
           */
            public void receiveErroraddRICUser(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingSessionByIdentity method
            * override this method for handling normal response from retrieveRoutingSessionByIdentity operation
            */
           public void receiveResultretrieveRoutingSessionByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingSession result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingSessionByIdentity operation
           */
            public void receiveErrorretrieveRoutingSessionByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for departStop method
            * override this method for handling normal response from departStop operation
            */
           public void receiveResultdepartStop(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from departStop operation
           */
            public void receiveErrordepartStop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerConfirmOrder method
            * override this method for handling normal response from schedulerConfirmOrder operation
            */
           public void receiveResultschedulerConfirmOrder(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerConfirmOrder operation
           */
            public void receiveErrorschedulerConfirmOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for allowAdditionOfRICUsers method
            * override this method for handling normal response from allowAdditionOfRICUsers operation
            */
           public void receiveResultallowAdditionOfRICUsers(
                    boolean result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from allowAdditionOfRICUsers operation
           */
            public void receiveErrorallowAdditionOfRICUsers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveGlobalConfig method
            * override this method for handling normal response from retrieveGlobalConfig operation
            */
           public void receiveResultretrieveGlobalConfig(
                    com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveGlobalConfig operation
           */
            public void receiveErrorretrieveGlobalConfig(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveEquipmentTypeByCriteria method
            * override this method for handling normal response from retrieveEquipmentTypeByCriteria operation
            */
           public void receiveResultretrieveEquipmentTypeByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveEquipmentTypeByCriteria operation
           */
            public void receiveErrorretrieveEquipmentTypeByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrievePositionHistoryBlocksByCriteria method
            * override this method for handling normal response from retrievePositionHistoryBlocksByCriteria operation
            */
           public void receiveResultretrievePositionHistoryBlocksByCriteria(
                    com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrievePositionHistoryBlocksByCriteria operation
           */
            public void receiveErrorretrievePositionHistoryBlocksByCriteria(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerReloadWaveInstances method
            * override this method for handling normal response from schedulerReloadWaveInstances operation
            */
           public void receiveResultschedulerReloadWaveInstances(
                    boolean result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerReloadWaveInstances operation
           */
            public void receiveErrorschedulerReloadWaveInstances(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveSurveyDetails method
            * override this method for handling normal response from retrieveSurveyDetails operation
            */
           public void receiveResultretrieveSurveyDetails(
                    com.freshdirect.routing.proxy.stub.transportation.SurveyDetails[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveSurveyDetails operation
           */
            public void receiveErrorretrieveSurveyDetails(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveLocationsEx method
            * override this method for handling normal response from saveLocationsEx operation
            */
           public void receiveResultsaveLocationsEx(
                    com.freshdirect.routing.proxy.stub.transportation.Location[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveLocationsEx operation
           */
            public void receiveErrorsaveLocationsEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for arriveStop method
            * override this method for handling normal response from arriveStop operation
            */
           public void receiveResultarriveStop(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from arriveStop operation
           */
            public void receiveErrorarriveStop(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerSaveDeliveryWaveInstance method
            * override this method for handling normal response from schedulerSaveDeliveryWaveInstance operation
            */
           public void receiveResultschedulerSaveDeliveryWaveInstance(
                    java.lang.String[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerSaveDeliveryWaveInstance operation
           */
            public void receiveErrorschedulerSaveDeliveryWaveInstance(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRoutingStopByIdentity method
            * override this method for handling normal response from retrieveRoutingStopByIdentity operation
            */
           public void receiveResultretrieveRoutingStopByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.RoutingStop result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRoutingStopByIdentity operation
           */
            public void receiveErrorretrieveRoutingStopByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteReport method
            * override this method for handling normal response from deleteReport operation
            */
           public void receiveResultdeleteReport(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteReport operation
           */
            public void receiveErrordeleteReport(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for saveRegionConfig method
            * override this method for handling normal response from saveRegionConfig operation
            */
           public void receiveResultsaveRegionConfig(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from saveRegionConfig operation
           */
            public void receiveErrorsaveRegionConfig(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for schedulerBulkReserveOrders method
            * override this method for handling normal response from schedulerBulkReserveOrders operation
            */
           public void receiveResultschedulerBulkReserveOrders(
                    com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from schedulerBulkReserveOrders operation
           */
            public void receiveErrorschedulerBulkReserveOrders(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for convertTimestamps method
            * override this method for handling normal response from convertTimestamps operation
            */
           public void receiveResultconvertTimestamps(
                    java.util.Calendar[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from convertTimestamps operation
           */
            public void receiveErrorconvertTimestamps(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRouteForDevice method
            * override this method for handling normal response from retrieveRouteForDevice operation
            */
           public void receiveResultretrieveRouteForDevice(
                    com.freshdirect.routing.proxy.stub.transportation.Route result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRouteForDevice operation
           */
            public void receiveErrorretrieveRouteForDevice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for assignEquipment method
            * override this method for handling normal response from assignEquipment operation
            */
           public void receiveResultassignEquipment(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from assignEquipment operation
           */
            public void receiveErrorassignEquipment(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for convertTimestamp method
            * override this method for handling normal response from convertTimestamp operation
            */
           public void receiveResultconvertTimestamp(
                    java.util.Calendar result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from convertTimestamp operation
           */
            public void receiveErrorconvertTimestamp(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveSurveys method
            * override this method for handling normal response from retrieveSurveys operation
            */
           public void receiveResultretrieveSurveys(
                    com.freshdirect.routing.proxy.stub.transportation.Survey[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveSurveys operation
           */
            public void receiveErrorretrieveSurveys(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveDutyPeriodByIdentity method
            * override this method for handling normal response from retrieveDutyPeriodByIdentity operation
            */
           public void receiveResultretrieveDutyPeriodByIdentity(
                    com.freshdirect.routing.proxy.stub.transportation.DutyPeriod result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveDutyPeriodByIdentity operation
           */
            public void receiveErrorretrieveDutyPeriodByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for placeUnassigned method
            * override this method for handling normal response from placeUnassigned operation
            */
           public void receiveResultplaceUnassigned(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from placeUnassigned operation
           */
            public void receiveErrorplaceUnassigned(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startRoute method
            * override this method for handling normal response from startRoute operation
            */
           public void receiveResultstartRoute(
                    ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startRoute operation
           */
            public void receiveErrorstartRoute(java.lang.Exception e) {
            }
                


    }
    