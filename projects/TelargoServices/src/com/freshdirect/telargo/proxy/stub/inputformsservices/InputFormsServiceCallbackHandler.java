
/**
 * InputFormsServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.telargo.proxy.stub.inputformsservices;

    /**
     *  InputFormsServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class InputFormsServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public InputFormsServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public InputFormsServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getInputForms method
            * override this method for handling normal response from getInputForms operation
            */
           public void receiveResultgetInputForms(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_inputforms.ArrayOfInputFormDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getInputForms operation
           */
            public void receiveErrorgetInputForms(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getInputFormEntries method
            * override this method for handling normal response from getInputFormEntries operation
            */
           public void receiveResultgetInputFormEntries(
                    org.datacontract.schemas._2004._07.telargo_generalservices_logic_services_interface_dto_inputforms.ArrayOfInputFormEntryDto result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getInputFormEntries operation
           */
            public void receiveErrorgetInputFormEntries(java.lang.Exception e) {
            }
                


    }
    