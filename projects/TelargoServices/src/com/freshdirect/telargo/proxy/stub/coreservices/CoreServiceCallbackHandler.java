
/**
 * CoreServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.telargo.proxy.stub.coreservices;

    /**
     *  CoreServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class CoreServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public CoreServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public CoreServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getGeofenceEvents method
            * override this method for handling normal response from getGeofenceEvents operation
            */
           public void receiveResultgetGeofenceEvents(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfGeofenceEventEntryDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getGeofenceEvents operation
           */
            public void receiveErrorgetGeofenceEvents(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDrivers method
            * override this method for handling normal response from getDrivers operation
            */
           public void receiveResultgetDrivers(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfDriverDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDrivers operation
           */
            public void receiveErrorgetDrivers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getEventSourcesAndTypes method
            * override this method for handling normal response from getEventSourcesAndTypes operation
            */
           public void receiveResultgetEventSourcesAndTypes(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfEventSourceDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getEventSourcesAndTypes operation
           */
            public void receiveErrorgetEventSourcesAndTypes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAssetsWithLastState method
            * override this method for handling normal response from getAssetsWithLastState operation
            */
           public void receiveResultgetAssetsWithLastState(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetWithStateDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAssetsWithLastState operation
           */
            public void receiveErrorgetAssetsWithLastState(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAssetsByGroups method
            * override this method for handling normal response from getAssetsByGroups operation
            */
           public void receiveResultgetAssetsByGroups(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfGroupItemDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAssetsByGroups operation
           */
            public void receiveErrorgetAssetsByGroups(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSessionPathParts method
            * override this method for handling normal response from getSessionPathParts operation
            */
           public void receiveResultgetSessionPathParts(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetSessionPathPartsDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSessionPathParts operation
           */
            public void receiveErrorgetSessionPathParts(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAssetEvents method
            * override this method for handling normal response from getAssetEvents operation
            */
           public void receiveResultgetAssetEvents(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetEventEntryDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAssetEvents operation
           */
            public void receiveErrorgetAssetEvents(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getSessionsForAsset method
            * override this method for handling normal response from getSessionsForAsset operation
            */
           public void receiveResultgetSessionsForAsset(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetSessionDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getSessionsForAsset operation
           */
            public void receiveErrorgetSessionsForAsset(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAssetStateLog method
            * override this method for handling normal response from getAssetStateLog operation
            */
           public void receiveResultgetAssetStateLog(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetStateLogEntryDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAssetStateLog operation
           */
            public void receiveErrorgetAssetStateLog(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getUsers method
            * override this method for handling normal response from getUsers operation
            */
           public void receiveResultgetUsers(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfUserDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getUsers operation
           */
            public void receiveErrorgetUsers(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAssets method
            * override this method for handling normal response from getAssets operation
            */
           public void receiveResultgetAssets(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAssets operation
           */
            public void receiveErrorgetAssets(java.lang.Exception e) {
            }
                


    }
    