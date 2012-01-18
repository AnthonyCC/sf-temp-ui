package com.freshdirect.telargo.service;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Stub;
import org.apache.axis2.transport.http.HttpTransportProperties;

import com.freshdirect.telargo.proxy.stub.coreservices.CoreService;
import com.freshdirect.telargo.proxy.stub.coreservices.CoreServiceStub;


public class TelargoServiceLocator {
	
	private static TelargoServiceLocator instance = null;

	private long SERVICE_TIMEOUT = 10 * 60 * 1000;
	
	private String TELARGO_CORESERVICE_PROVIDERURL = "https://host.eu.tfc.telargo.com/GeneralServices/CoreService";
	
	private String TELARGO_CORESERVICE_USERNAME = "FDWSaccess";
	
	private String TELARGO_CORESERVICE_PASSWORD = "FD2011access!";
	
	public static TelargoServiceLocator getInstance() {
	      if(instance == null) {
	         instance = new TelargoServiceLocator();
	      }
	      return instance;
	}
	
	public static TelargoServiceLocator getInstance(String url, String username, String password) {
	      if(instance == null) {
	         instance = new TelargoServiceLocator();
	         instance.TELARGO_CORESERVICE_PROVIDERURL = url;
	         instance.TELARGO_CORESERVICE_USERNAME = username;
	         instance.TELARGO_CORESERVICE_PASSWORD = password;
	      }
	      return instance;
	}
	
	public CoreService getTelargoCoreService() throws AxisFault {
		CoreServiceStub stub = new CoreServiceStub(TELARGO_CORESERVICE_PROVIDERURL);
		
		initStub(stub);
		return stub;
	}
	
	private void initStub(Stub stub) {		
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(SERVICE_TIMEOUT);
		
		HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
		
		auth.setUsername(TELARGO_CORESERVICE_USERNAME);
		auth.setPassword(TELARGO_CORESERVICE_PASSWORD);
		
		stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);		
	}
	
	
}
