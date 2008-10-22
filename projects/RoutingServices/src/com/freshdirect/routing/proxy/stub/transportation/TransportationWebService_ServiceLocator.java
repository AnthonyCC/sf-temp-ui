/**
 * TransportationWebService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.freshdirect.routing.proxy.stub.transportation;

public class TransportationWebService_ServiceLocator extends org.apache.axis.client.Service implements com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_Service {

/**
 * UPS Logistics Technologies Transportation Web Service
 */

    public TransportationWebService_ServiceLocator() {
    }


    public TransportationWebService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TransportationWebService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TransportationWebService
    private java.lang.String TransportationWebService_address = "http://localhost:81";

    public java.lang.String getTransportationWebServiceAddress() {
        return TransportationWebService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TransportationWebServiceWSDDServiceName = "TransportationWebService";

    public java.lang.String getTransportationWebServiceWSDDServiceName() {
        return TransportationWebServiceWSDDServiceName;
    }

    public void setTransportationWebServiceWSDDServiceName(java.lang.String name) {
        TransportationWebServiceWSDDServiceName = name;
    }

    public com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_PortType getTransportationWebService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TransportationWebService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTransportationWebService(endpoint);
    }

    public com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_PortType getTransportationWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_BindingStub _stub = new com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_BindingStub(portAddress, this);
            _stub.setPortName(getTransportationWebServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTransportationWebServiceEndpointAddress(java.lang.String address) {
        TransportationWebService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_BindingStub _stub = new com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_BindingStub(new java.net.URL(TransportationWebService_address), this);
                _stub.setPortName(getTransportationWebServiceWSDDServiceName());
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
        if ("TransportationWebService".equals(inputPortName)) {
            return getTransportationWebService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TransportationWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.upslogisticstech.com/UPSLT/TransportationSuite/TransportationWebService", "TransportationWebService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TransportationWebService".equals(portName)) {
            setTransportationWebServiceEndpointAddress(address);
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
