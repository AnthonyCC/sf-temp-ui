

/**
 * InputFormsService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.telargo.proxy.stub.inputformsservices;

    /*
     *  InputFormsService java interface
     */

    public interface InputFormsService {
          

        /**
          * Auto generated method signature
          * 
                    * @param getInputForms1
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_inputforms.ArrayOfInputFormDto getInputForms(

                        org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_inputforms.InputFormsFilter filter2)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getInputForms1
            
          */
        public void startgetInputForms(

            org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_inputforms.InputFormsFilter filter2,

            final com.freshdirect.telargo.proxy.stub.inputformsservices.InputFormsServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getInputFormEntries5
                
         */

         
                     public org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_inputforms.ArrayOfInputFormEntryDto getInputFormEntries(

                        org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_inputforms.InputFormEntriesFilter filter6)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getInputFormEntries5
            
          */
        public void startgetInputFormEntries(

            org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_filters_inputforms.InputFormEntriesFilter filter6,

            final com.freshdirect.telargo.proxy.stub.inputformsservices.InputFormsServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    