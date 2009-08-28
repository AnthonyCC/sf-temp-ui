package com.freshdirect.routing.service;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebServiceStub;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebServiceStub;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class RoutingServiceLocator {

	private static RoutingServiceLocator instance = null;

	private BeanFactory factory = null;
	
	private long SERVICE_TIMEOUT = 10 * 60 * 1000;

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
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(SERVICE_TIMEOUT);
		return stub;
	}
	
	public TransportationWebService getTransportationSuiteService() throws AxisFault {
				
		TransportationWebServiceStub stub = new TransportationWebServiceStub(RoutingServicesProperties.getTransportationSuiteProviderURL());
		//stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.java.JavaTransportSender., property)
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(SERVICE_TIMEOUT);
		return stub;
	}
	
	public TransportationWebService getTransportationSuiteService(String deliveryType) throws AxisFault {
		String url = RoutingServicesProperties.getTransportationSuiteProviderURL(deliveryType);
		if(url == null) {
			url = RoutingServicesProperties.getTransportationSuiteProviderURL();
		}
		
		TransportationWebServiceStub stub = new TransportationWebServiceStub(url);
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(SERVICE_TIMEOUT);
		return stub;
	}

}
