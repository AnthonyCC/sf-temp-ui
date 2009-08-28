

/**
 * ReportsWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5  Built on : Apr 30, 2009 (06:07:24 EDT)
 */

    package com.freshdirect.routing.proxy.stub.report;

    /*
     *  ReportsWebService java interface
     */

    public interface ReportsWebService {
          

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveVersionInformation
                    * @param retrieveVersionInformation3
                
         */

         
                     public java.lang.String retrieveVersionInformation(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveVersionInformation
                * @param retrieveVersionInformation3
            
          */
        public void startretrieveVersionInformation(

            

            final com.freshdirect.routing.proxy.stub.report.ReportsWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__Nop
                    * @param nop6
                
         */

         
                     public int nop(

                        )
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__Nop
                * @param nop6
            
          */
        public void startnop(

            

            final com.freshdirect.routing.proxy.stub.report.ReportsWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRICReportByIdentity
                    * @param retrieveRICReportByIdentity9
                
         */

         
                     public com.freshdirect.routing.proxy.stub.report.Report retrieveRICReportByIdentity(

                        com.freshdirect.routing.proxy.stub.report.ReportIdentity identity10,com.freshdirect.routing.proxy.stub.report.ReportRetrievalOptions retrieveOptions11)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRICReportByIdentity
                * @param retrieveRICReportByIdentity9
            
          */
        public void startretrieveRICReportByIdentity(

            com.freshdirect.routing.proxy.stub.report.ReportIdentity identity10,com.freshdirect.routing.proxy.stub.report.ReportRetrievalOptions retrieveOptions11,

            final com.freshdirect.routing.proxy.stub.report.ReportsWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__ExportReportToFile
                    * @param exportReportToFile14
                
         */

         
                     public java.lang.String exportReportToFile(

                        com.freshdirect.routing.proxy.stub.report.Report report15,com.freshdirect.routing.proxy.stub.report.ReportExportOptions exportOptions16)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__ExportReportToFile
                * @param exportReportToFile14
            
          */
        public void startexportReportToFile(

            com.freshdirect.routing.proxy.stub.report.Report report15,com.freshdirect.routing.proxy.stub.report.ReportExportOptions exportOptions16,

            final com.freshdirect.routing.proxy.stub.report.ReportsWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * Service definition of function ns1__RetrieveRICReports
                    * @param retrieveRICReports19
                
         */

         
                     public com.freshdirect.routing.proxy.stub.report.Report[] retrieveRICReports(

                        com.freshdirect.routing.proxy.stub.report.ReportRetrievalOptions retrieveOptions20)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Service definition of function ns1__RetrieveRICReports
                * @param retrieveRICReports19
            
          */
        public void startretrieveRICReports(

            com.freshdirect.routing.proxy.stub.report.ReportRetrievalOptions retrieveOptions20,

            final com.freshdirect.routing.proxy.stub.report.ReportsWebServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    