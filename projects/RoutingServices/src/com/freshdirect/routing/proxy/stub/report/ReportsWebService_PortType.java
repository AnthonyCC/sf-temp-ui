/**
 * ReportsWebService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.report;

public interface ReportsWebService_PortType extends java.rmi.Remote {

    /**
     * Service definition of function ns1__Nop
     */
    public java.lang.Integer nop() throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveVersionInformation
     */
    public java.lang.String retrieveVersionInformation() throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRICReportByIdentity
     */
    public com.freshdirect.routing.proxy.stub.report.Report retrieveRICReportByIdentity(com.freshdirect.routing.proxy.stub.report.ReportIdentity identity, com.freshdirect.routing.proxy.stub.report.ReportRetrievalOptions retrieveOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__RetrieveRICReports
     */
    public com.freshdirect.routing.proxy.stub.report.Report[] retrieveRICReports(com.freshdirect.routing.proxy.stub.report.ReportRetrievalOptions retrieveOptions) throws java.rmi.RemoteException;

    /**
     * Service definition of function ns1__ExportReportToFile
     */
    public java.lang.String exportReportToFile(com.freshdirect.routing.proxy.stub.report.Report report, com.freshdirect.routing.proxy.stub.report.ReportExportOptions exportOptions) throws java.rmi.RemoteException;
}
