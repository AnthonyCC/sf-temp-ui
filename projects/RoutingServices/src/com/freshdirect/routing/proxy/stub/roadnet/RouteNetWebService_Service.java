/**
 * RouteNetWebService_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.roadnet;

public interface RouteNetWebService_Service extends javax.xml.rpc.Service {

/**
 * UPS Logistics Technologies RouteNet Web Service
 */
    public java.lang.String getRouteNetWebServiceAddress();

    public com.freshdirect.routing.proxy.stub.roadnet.RouteNetPortType getRouteNetWebService() throws javax.xml.rpc.ServiceException;

    public com.freshdirect.routing.proxy.stub.roadnet.RouteNetPortType getRouteNetWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
