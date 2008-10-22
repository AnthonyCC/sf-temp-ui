/**
 * ReportsWebService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.report;

public class ReportsWebService_ServiceLocator extends org.apache.axis.client.Service implements com.freshdirect.routing.proxy.stub.report.ReportsWebService_Service {

/**
 * UPS Logistics Technologies Reports Web Service
 */

    public ReportsWebService_ServiceLocator() {
    }


    public ReportsWebService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ReportsWebService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ReportsWebService
    private java.lang.String ReportsWebService_address = "http://localhost:83";

    public java.lang.String getReportsWebServiceAddress() {
        return ReportsWebService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ReportsWebServiceWSDDServiceName = "ReportsWebService";

    public java.lang.String getReportsWebServiceWSDDServiceName() {
        return ReportsWebServiceWSDDServiceName;
    }

    public void setReportsWebServiceWSDDServiceName(java.lang.String name) {
        ReportsWebServiceWSDDServiceName = name;
    }

    public com.freshdirect.routing.proxy.stub.report.ReportsWebService_PortType getReportsWebService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ReportsWebService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getReportsWebService(endpoint);
    }

    public com.freshdirect.routing.proxy.stub.report.ReportsWebService_PortType getReportsWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.freshdirect.routing.proxy.stub.report.ReportsWebService_BindingStub _stub = new com.freshdirect.routing.proxy.stub.report.ReportsWebService_BindingStub(portAddress, this);
            _stub.setPortName(getReportsWebServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setReportsWebServiceEndpointAddress(java.lang.String address) {
        ReportsWebService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.freshdirect.routing.proxy.stub.report.ReportsWebService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.freshdirect.routing.proxy.stub.report.ReportsWebService_BindingStub _stub = new com.freshdirect.routing.proxy.stub.report.ReportsWebService_BindingStub(new java.net.URL(ReportsWebService_address), this);
                _stub.setPortName(getReportsWebServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ReportsWebService".equals(inputPortName)) {
            return getReportsWebService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportsWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/ReportsWebService", "ReportsWebService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ReportsWebService".equals(portName)) {
            setReportsWebServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
