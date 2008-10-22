/**
 * ReportsWebService_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.report;

public interface ReportsWebService_Service extends javax.xml.rpc.Service {

/**
 * UPS Logistics Technologies Reports Web Service
 */
    public java.lang.String getReportsWebServiceAddress();

    public com.freshdirect.routing.proxy.stub.report.ReportsWebService_PortType getReportsWebService() throws javax.xml.rpc.ServiceException;

    public com.freshdirect.routing.proxy.stub.report.ReportsWebService_PortType getReportsWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
