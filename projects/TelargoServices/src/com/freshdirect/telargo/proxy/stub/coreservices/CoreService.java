

/**
 * CoreService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.telargo.proxy.stub.coreservices;

    /*
     *  CoreService java interface
     */

    public interface CoreService {
          

        /**
          * Auto generated method signature
          * 
                    * @param getGeofenceEvents4
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfGeofenceEventEntryDto getGeofenceEvents(

                        org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.GeofenceEventFilter filter5)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getGeofenceEvents4
            
          */
        public void startgetGeofenceEvents(

            org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.GeofenceEventFilter filter5,

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getDrivers8
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfDriverDto getDrivers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getDrivers8
            
          */
        public void startgetDrivers(

            

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getEventSourcesAndTypes11
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfEventSourceDto getEventSourcesAndTypes(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getEventSourcesAndTypes11
            
          */
        public void startgetEventSourcesAndTypes(

            

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getAssetsWithLastState14
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetWithStateDto getAssetsWithLastState(

                        org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.CurrentStatusAssetsFilter filter15)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getAssetsWithLastState14
            
          */
        public void startgetAssetsWithLastState(

            org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.CurrentStatusAssetsFilter filter15,

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getAssetsByGroups18
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfGroupItemDto getAssetsByGroups(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getAssetsByGroups18
            
          */
        public void startgetAssetsByGroups(

            

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getSessionPathParts21
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.AssetSessionPathPartsDto getSessionPathParts(

                        com.microsoft.schemas._2003._10.serialization.Guid sessionID22,java.util.Calendar pathPartsFrom23,java.util.Calendar pathPartsTo24)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getSessionPathParts21
            
          */
        public void startgetSessionPathParts(

            com.microsoft.schemas._2003._10.serialization.Guid sessionID22,java.util.Calendar pathPartsFrom23,java.util.Calendar pathPartsTo24,

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getAssetEvents27
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetEventEntryDto getAssetEvents(

                        org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.AssetEventFilter filter28)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getAssetEvents27
            
          */
        public void startgetAssetEvents(

            org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.AssetEventFilter filter28,

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getSessionsForAsset31
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetSessionDto getSessionsForAsset(

                        org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.CurrentStatusRoutesFilter filter32)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getSessionsForAsset31
            
          */
        public void startgetSessionsForAsset(

            org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.CurrentStatusRoutesFilter filter32,

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getAssetStateLog35
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetStateLogEntryDto getAssetStateLog(

                        org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.AssetStateLogFilter filter36)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getAssetStateLog35
            
          */
        public void startgetAssetStateLog(

            org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_core.AssetStateLogFilter filter36,

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getUsers39
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfUserDto getUsers(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getUsers39
            
          */
        public void startgetUsers(

            

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getAssets42
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_core.ArrayOfAssetDto getAssets(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getAssets42
            
          */
        public void startgetAssets(

            

            final com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    