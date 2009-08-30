package com.freshdirect.routing.service;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Stub;
import org.apache.log4j.Category;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceStub;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceStub;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class RoutingServiceLocator {

	private static RoutingServiceLocator instance = null;

	private BeanFactory factory = null;
	
	private long SERVICE_TIMEOUT = 10 * 60 * 1000;
	
	private boolean useProxy;
	
	private static final Category LOGGER = LoggerFactory.getInstance(RoutingServiceLocator.class);

	private RoutingServiceLocator() {
		try {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
		        new String[] {"com/freshdirect/routing/service/applicationContext-RoutingServices.xml"});
		//	of course, an ApplicationContext is just a BeanFactory
		factory = (BeanFactory) appContext;
		} catch (Error r) {
			r.printStackTrace();
		}
	}

	public static RoutingServiceLocator getInstance() {
	      if(instance == null) {
	         instance = new RoutingServiceLocator();
	      }
	      return instance;
	   }
	
	public Object getService(String beanId) {
		return factory.getBean(beanId);
	}
	
	public IGeographyService getGeographyService() {
		return (IGeographyService)factory.getBean("geographyService");
	}

	public IDeliveryService getDeliveryService() {
		return (IDeliveryService)factory.getBean("deliveryService");
	}

	public IPlantService getPlantService() {
		return (IPlantService)factory.getBean("plantService");
	}
	
	public IUtilityService getUtilityService() {
		return (IUtilityService)factory.getBean("utilityService");
	}
	
	public IRoutingInfoService getRoutingInfoService() {
		return (IRoutingInfoService)factory.getBean("routingInfoService");
	}
	
	public IRoutingEngineService getRoutingEngineService() {
		return (IRoutingEngineService)factory.getBean("routingEngineService");
	}
	
	public RouteNetWebService getRouteNetService() throws AxisFault {
		RouteNetWebServiceStub stub = new RouteNetWebServiceStub(RoutingServicesProperties.getRoadNetProviderURL());
		initStub(stub);
		return stub;
	}
	
	public TransportationWebService getTransportationSuiteService() throws AxisFault {
		TransportationWebServiceStub stub = null;
		if(this.isUseProxy()) {
			stub = new TransportationWebServiceStub(RoutingServicesProperties.getTransportationSuiteProxyURL());
		} else {
			stub = new TransportationWebServiceStub(RoutingServicesProperties.getTransportationSuiteProviderURL());
		}
		initStub(stub);
		return stub;
	}
	
	

	public TransportationWebService getTransportationSuiteProviderService() throws AxisFault {
		TransportationWebServiceStub stub = new TransportationWebServiceStub(RoutingServicesProperties.getTransportationSuiteProviderURL());
		
		initStub(stub);
		return stub;
	}
	
	public TransportationWebService getTransportationSuiteService(String deliveryType) throws AxisFault {
		String url = null;
		if(this.isUseProxy()) {
			url = RoutingServicesProperties.getTransportationSuiteProxyURL();
		} else {
			url = RoutingServicesProperties.getTransportationSuiteProviderURL(deliveryType);
		}
		LOGGER.debug("getTransportationSuiteService:"+ deliveryType +":"+ url);
		
		if(url == null) {			
			url = RoutingServicesProperties.getTransportationSuiteProviderURL();
			LOGGER.debug("getTransportationSuiteService Server not found :"+ deliveryType +":"+ url);
		}
		
		TransportationWebServiceStub stub = new TransportationWebServiceStub(url);
		initStub(stub);
		return stub;
	}
	
	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}
	private void initStub(Stub stub) {
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(SERVICE_TIMEOUT);
	}

}
