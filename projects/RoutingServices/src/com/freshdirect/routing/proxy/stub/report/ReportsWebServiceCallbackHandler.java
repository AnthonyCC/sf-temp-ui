
/**
 * ReportsWebServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.routing.proxy.stub.report;

    /**
     *  ReportsWebServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ReportsWebServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ReportsWebServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ReportsWebServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for retrieveVersionInformation method
            * override this method for handling normal response from retrieveVersionInformation operation
            */
           public void receiveResultretrieveVersionInformation(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveVersionInformation operation
           */
            public void receiveErrorretrieveVersionInformation(java.lang.Exception e) {
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
            * auto generated Axis2 call back method for retrieveRICReportByIdentity method
            * override this method for handling normal response from retrieveRICReportByIdentity operation
            */
           public void receiveResultretrieveRICReportByIdentity(
                    com.freshdirect.routing.proxy.stub.report.Report result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRICReportByIdentity operation
           */
            public void receiveErrorretrieveRICReportByIdentity(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for exportReportToFile method
            * override this method for handling normal response from exportReportToFile operation
            */
           public void receiveResultexportReportToFile(
                    java.lang.String result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from exportReportToFile operation
           */
            public void receiveErrorexportReportToFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for retrieveRICReports method
            * override this method for handling normal response from retrieveRICReports operation
            */
           public void receiveResultretrieveRICReports(
                    com.freshdirect.routing.proxy.stub.report.Report[] result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from retrieveRICReports operation
           */
            public void receiveErrorretrieveRICReports(java.lang.Exception e) {
            }
                


    }
    