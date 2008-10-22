/**
 * TransportationWebService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public interface TransportationWebService_PortType extends java.rmi.Remote {

    /**
     * Service definition of function ns1__Nop
     */
    public java.lang.Integer nop() throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__VersionInformation
     */
    public java.lang.String versionInformation() throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__AuthenticateUser
     */
    public void authenticateUser(java.lang.String userID, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__ChangeUserPassword
     */
    public void changeUserPassword(java.lang.String userID, java.lang.String oldPassword, java.lang.String newPassword) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__AddRICUser
     */
    public void addRICUser(com.freshdirect.routing.proxy.stub.transportation.User user) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrievePermissionsForUser
     */
    public com.freshdirect.routing.proxy.stub.transportation.UserPermissions retrievePermissionsForUser(java.lang.String userID, com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveUserByUserID
     */
    public com.freshdirect.routing.proxy.stub.transportation.User retrieveUserByUserID(java.lang.String userID) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__AllowAdditionOfRICUsers
     */
    public boolean allowAdditionOfRICUsers() throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRegionByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Region retrieveRegionByIdentity(com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRegionsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.Region[] retrieveRegionsByCriteria(com.freshdirect.routing.proxy.stub.transportation.RegionCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveRegion
     */
    public void saveRegion(com.freshdirect.routing.proxy.stub.transportation.Region region) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRegionOptions
     */
    public com.freshdirect.routing.proxy.stub.transportation.RegionOptions retrieveRegionOptions(com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRouteByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteByIdentity(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity, com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutesByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.Route[] retrieveRoutesByCriteria(com.freshdirect.routing.proxy.stub.transportation.RouteCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRouteForDevice
     */
    public com.freshdirect.routing.proxy.stub.transportation.Route retrieveRouteForDevice(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity, com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options, com.freshdirect.routing.proxy.stub.transportation.WirelessDeviceIdentity wirelessDeviceIdentity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveRoute
     */
    public void saveRoute(com.freshdirect.routing.proxy.stub.transportation.Route route, com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__AssignEquipment
     */
    public void assignEquipment(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity, com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] equipment) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveAssignedEquipment
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity[] retrieveAssignedEquipment(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__AssignDrivers
     */
    public void assignDrivers(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity, com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] drivers) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveAssignedDrivers
     */
    public com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity[] retrieveAssignedDrivers(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RemoveRoute
     */
    public void removeRoute(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SendTextMessageToDriver
     */
    public void sendTextMessageToDriver(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity, java.lang.String message, java.lang.String fromUserID) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveStopByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Stop retrieveStopByIdentity(com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity, com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveStopsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveStopsByCriteria(com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveStop
     */
    public void saveStop(com.freshdirect.routing.proxy.stub.transportation.Stop stop, com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementOptions, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveStopSignature
     */
    public byte[] retrieveStopSignature(com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity, com.freshdirect.routing.proxy.stub.transportation.ImageType imageType) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveStopSignature
     */
    public void saveStopSignature(com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity, byte[] signatureData) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveUnassignsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.Stop[] retrieveUnassignsByCriteria(com.freshdirect.routing.proxy.stub.transportation.StopCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveUnassigned
     */
    public void saveUnassigned(com.freshdirect.routing.proxy.stub.transportation.Stop stop, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SuggestRoute
     */
    public com.freshdirect.routing.proxy.stub.transportation.PlacementCost[] suggestRoute(com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop, com.freshdirect.routing.proxy.stub.transportation.SuggestRouteOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__PlaceUnassigned
     */
    public void placeUnassigned(com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop, com.freshdirect.routing.proxy.stub.transportation.RouteIdentity routeIdentity, com.freshdirect.routing.proxy.stub.transportation.StopPlacementOptions placementPosition, com.freshdirect.routing.proxy.stub.transportation.OptionalDateTime adjustedRouteStartTime, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions timeZoneOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__DeleteUnassigned
     */
    public void deleteUnassigned(com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__UnassignStop
     */
    public void unassignStop(com.freshdirect.routing.proxy.stub.transportation.StopIdentity stop) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveLocations
     */
    public com.freshdirect.routing.proxy.stub.transportation.Location[] saveLocations(com.freshdirect.routing.proxy.stub.transportation.Location[] locations) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveLocationByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentity(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveLocationByIdentityEx
     */
    public com.freshdirect.routing.proxy.stub.transportation.Location retrieveLocationByIdentityEx(com.freshdirect.routing.proxy.stub.transportation.LocationIdentity identity, com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveLocationsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteria(com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveLocationsByCriteriaEx
     */
    public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveLocationsByCriteriaEx(com.freshdirect.routing.proxy.stub.transportation.LocationCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveEquipmentTypeByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentType[] retrieveEquipmentTypeByCriteria(com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveEquipmentTypeByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.EquipmentType retrieveEquipmentTypeByIdentity(com.freshdirect.routing.proxy.stub.transportation.EquipmentTypeIdentity identity, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveEquipmentByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Equipment retrieveEquipmentByIdentity(com.freshdirect.routing.proxy.stub.transportation.EquipmentIdentity identity, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveEquipmentByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.Equipment[] retrieveEquipmentByCriteria(com.freshdirect.routing.proxy.stub.transportation.EquipmentCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveEquipment
     */
    public void saveEquipment(com.freshdirect.routing.proxy.stub.transportation.Equipment equipment, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveEmployeeByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Employee retrieveEmployeeByIdentity(com.freshdirect.routing.proxy.stub.transportation.EmployeeIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveEmployeesByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.Employee[] retrieveEmployeesByCriteria(com.freshdirect.routing.proxy.stub.transportation.EmployeeCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveEmployee
     */
    public void saveEmployee(com.freshdirect.routing.proxy.stub.transportation.Employee employee) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveDutyPeriodByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod retrieveDutyPeriodByIdentity(com.freshdirect.routing.proxy.stub.transportation.DutyPeriodIdentity identity, com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveDutyPeriodsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.DutyPeriod[] retrieveDutyPeriodsByCriteria(com.freshdirect.routing.proxy.stub.transportation.DutyPeriodCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.DutyPeriodRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveProductsPurchased
     */
    public com.freshdirect.routing.proxy.stub.transportation.ProductsPurchased retrieveProductsPurchased(com.freshdirect.routing.proxy.stub.transportation.RegionIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRouteSurveyResults
     */
    public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveRouteSurveyResults(com.freshdirect.routing.proxy.stub.transportation.RouteIdentity identity, com.freshdirect.routing.proxy.stub.transportation.SurveyPerformedAt performedAt) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveStopSurveyResults
     */
    public com.freshdirect.routing.proxy.stub.transportation.SurveyResult[] retrieveStopSurveyResults(com.freshdirect.routing.proxy.stub.transportation.StopIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveSurveys
     */
    public com.freshdirect.routing.proxy.stub.transportation.Survey[] retrieveSurveys(com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrievePositionHistoryByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.PositionHistory[] retrievePositionHistoryByCriteria(com.freshdirect.routing.proxy.stub.transportation.PositionHistoryCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveReportByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Report retrieveReportByIdentity(com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveReportsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.Report[] retrieveReportsByCriteria(com.freshdirect.routing.proxy.stub.transportation.ReportCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveReport
     */
    public void saveReport(com.freshdirect.routing.proxy.stub.transportation.Report report) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__DeleteReport
     */
    public void deleteReport(com.freshdirect.routing.proxy.stub.transportation.ReportIdentity identity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRouteNotesByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteNote[] retrieveRouteNotesByCriteria(com.freshdirect.routing.proxy.stub.transportation.RouteNoteCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveCannedTextMessagesByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.CannedTextMessage[] retrieveCannedTextMessagesByCriteria(com.freshdirect.routing.proxy.stub.transportation.CannedTextMessageCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRouteDailyStatsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRouteDailyStatsByCriteria(com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RouteDailyStatsRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__ConvertTimestamp
     */
    public java.util.Calendar convertTimestamp(java.util.Calendar sourceTimestamp, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__ConvertTimestamps
     */
    public java.util.Calendar[] convertTimestamps(java.util.Calendar[] sourceTimestamps, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions sourceOptions, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions destinationOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__ReturnFault
     */
    public com.freshdirect.routing.proxy.stub.transportation.Fault returnFault(int requestedFaultCode) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__CalculateTimeDist
     */
    public com.freshdirect.routing.proxy.stub.transportation.TimeDistResult calculateTimeDist(com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity, int fromLatitude, int fromLongitude, int toLatitude, int toLongitude) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveRegionConfig
     */
    public void saveRegionConfig(java.lang.String applicationID, com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity, java.lang.String configGroupID, com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRegionConfig
     */
    public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveRegionConfig(java.lang.String applicationID, com.freshdirect.routing.proxy.stub.transportation.RegionIdentity regionIdentity, java.lang.String configGroupID) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveUserConfig
     */
    public void saveUserConfig(java.lang.String applicationID, com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity, java.lang.String configGroupID, com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveUserConfig
     */
    public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveUserConfig(java.lang.String applicationID, com.freshdirect.routing.proxy.stub.transportation.UserIdentity userIdentity, java.lang.String configGroupID) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveGlobalConfig
     */
    public void saveGlobalConfig(java.lang.String applicationID, java.lang.String configGroupID, com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] items) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveGlobalConfig
     */
    public com.freshdirect.routing.proxy.stub.transportation.ConfigurationItem[] retrieveGlobalConfig(java.lang.String applicationID, java.lang.String configGroupID) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingSessionsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingSession[] retrieveRoutingSessionsByCriteria(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingSessionByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingSession retrieveRoutingSessionByIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity identity, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingRoutesByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute[] retrieveRoutingRoutesByCriteria(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingRouteByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingRoute retrieveRoutingRouteByIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteIdentity identity, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingStopByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingStop retrieveRoutingStopByIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingStopIdentity identity, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingUnassignsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingUnassignsByCriteria(com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingLocationsWithOrders
     */
    public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrders(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingLocationsWithOrdersEx
     */
    public com.freshdirect.routing.proxy.stub.transportation.Location[] retrieveRoutingLocationsWithOrdersEx(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity, com.freshdirect.routing.proxy.stub.transportation.LocationRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingOrderByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder retrieveRoutingOrderByIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingOrderIdentity identity, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingSourcedOrdersByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingOrder[] retrieveRoutingSourcedOrdersByCriteria(com.freshdirect.routing.proxy.stub.transportation.RoutingSourcedOrderCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveDefaultRoutingSessionProperties
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties retrieveDefaultRoutingSessionProperties(java.lang.String regionId, java.util.Date sessionDate) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__CreateRoutingSession
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity createRoutingSession(java.lang.String regionId, com.freshdirect.routing.proxy.stub.transportation.RoutingSessionProperties sessionProperties) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__DeleteRoutingSession
     */
    public void deleteRoutingSession(com.freshdirect.routing.proxy.stub.transportation.RoutingSessionIdentity sessionIdentity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SaveRoutingImportOrders
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] saveRoutingImportOrders(java.lang.String regionId, com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] orders, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingImportOrderByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder retrieveRoutingImportOrderByIdentity(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderIdentity identity, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingImportOrdersByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrder[] retrieveRoutingImportOrdersByCriteria(com.freshdirect.routing.proxy.stub.transportation.RoutingImportOrderCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingStopsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RoutingStop[] retrieveRoutingStopsByCriteria(com.freshdirect.routing.proxy.stub.transportation.RoutingStopCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteInfoRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRoutingRouteDailyStatsByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.RouteDailyStats[] retrieveRoutingRouteDailyStatsByCriteria(com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.RoutingRouteDailyStatsRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveNotificationsByRecipientIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.Notification[] retrieveNotificationsByRecipientIdentity(com.freshdirect.routing.proxy.stub.transportation.RecipientIdentity identity, com.freshdirect.routing.proxy.stub.transportation.TimeZoneOptions tzOptions, com.freshdirect.routing.proxy.stub.transportation.NotificationRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__DeleteNotifications
     */
    public void deleteNotifications(com.freshdirect.routing.proxy.stub.transportation.NotificationIdentity[] identities) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__UnlockNotifications
     */
    public void unlockNotifications(com.freshdirect.routing.proxy.stub.transportation.UnlockNotificationsCriteria criteria) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__UpdateDeliveryDetails
     */
    public void updateDeliveryDetails(com.freshdirect.routing.proxy.stub.transportation.DeliveryDetailInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__StartRoute
     */
    public void startRoute(com.freshdirect.routing.proxy.stub.transportation.RouteStartInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__DepartOrigin
     */
    public void departOrigin(com.freshdirect.routing.proxy.stub.transportation.OriginDepartInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__ArriveDestination
     */
    public void arriveDestination(com.freshdirect.routing.proxy.stub.transportation.DestinationArriveInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__CompleteRoute
     */
    public void completeRoute(com.freshdirect.routing.proxy.stub.transportation.RouteCompleteInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__ArriveStop
     */
    public void arriveStop(com.freshdirect.routing.proxy.stub.transportation.StopArriveInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__DepartStop
     */
    public void departStop(com.freshdirect.routing.proxy.stub.transportation.StopDepartInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__BulkArriveDepartStop
     */
    public void bulkArriveDepartStop(com.freshdirect.routing.proxy.stub.transportation.BulkArriveDepartInfo[] arriveDepartInfos) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SequenceStop
     */
    public void sequenceStop(com.freshdirect.routing.proxy.stub.transportation.StopSequenceInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__CancelStop
     */
    public void cancelStop(com.freshdirect.routing.proxy.stub.transportation.StopCancelInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__UpdateRoutePosition
     */
    public com.freshdirect.routing.proxy.stub.transportation.UpdatePositionReturnCode updateRoutePosition(com.freshdirect.routing.proxy.stub.transportation.RoutePositionInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__TextMessage
     */
    public void textMessage(com.freshdirect.routing.proxy.stub.transportation.TextMessageInfo info) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerAnalyzeOrder
     */
    public com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow[] schedulerAnalyzeOrder(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder order, com.freshdirect.routing.proxy.stub.transportation.SchedulerAnalyzeOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerReserveOrder
     */
    public com.freshdirect.routing.proxy.stub.transportation.ReserveResult schedulerReserveOrder(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder deliveryAreaOrder, com.freshdirect.routing.proxy.stub.transportation.DeliveryWindow deliveryWindow, com.freshdirect.routing.proxy.stub.transportation.SchedulerReserveOrderOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerConfirmOrder
     */
    public void schedulerConfirmOrder(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, java.lang.String orderNumberXML) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerCancelOrder
     */
    public void schedulerCancelOrder(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, java.lang.String orderNumberXML) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerPurge
     */
    public void schedulerPurge(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, boolean reloadXML) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerIsExcludingCutoffRoutes
     */
    public com.freshdirect.routing.proxy.stub.transportation.IsExcludingCutoffRoutesResult schedulerIsExcludingCutoffRoutes(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerExcludeCutoffRoutes
     */
    public void schedulerExcludeCutoffRoutes(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, boolean excludeXML) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerOptimizeOrders
     */
    public void schedulerOptimizeOrders(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerExtendOrderReservation
     */
    public void schedulerExtendOrderReservation(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, java.lang.String orderNumberXML, int extendMinutes) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerBulkReserveOrders
     */
    public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerBulkReserveOrders(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] orders, com.freshdirect.routing.proxy.stub.transportation.SchedulerBulkReserveOrdersOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerSendRoutesToRoadnet
     */
    public void schedulerSendRoutesToRoadnet(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerSendRoutesToRoadnetEx
     */
    public void schedulerSendRoutesToRoadnetEx(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, java.lang.String sessionDescription) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerMovableOrders
     */
    public void schedulerMovableOrders(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, java.lang.String[] criteria, com.freshdirect.routing.proxy.stub.transportation.SchedulerMovableOrdersOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerRetrieveFeederRoutes
     */
    public com.freshdirect.routing.proxy.stub.transportation.SchedulerFeederRoute[] schedulerRetrieveFeederRoutes(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, com.freshdirect.routing.proxy.stub.transportation.SchedulerRetrieveFeederRoutesOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerUpdateOrder
     */
    public boolean schedulerUpdateOrder(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity, com.freshdirect.routing.proxy.stub.transportation.SchedulerUpdateOrderOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerRetrieveOrderByIdentity
     */
    public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder schedulerRetrieveOrderByIdentity(com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderIdentity identity, com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__SchedulerRetrieveOrdersByCriteria
     */
    public com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrder[] schedulerRetrieveOrdersByCriteria(com.freshdirect.routing.proxy.stub.transportation.SchedulerIdentity schedulerIdentity, com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderCriteria criteria, com.freshdirect.routing.proxy.stub.transportation.DeliveryAreaOrderRetrieveOptions options) throws java.rmi.RemoteException;
}
