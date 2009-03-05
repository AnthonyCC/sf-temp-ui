package com.freshdirect.routing.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.freshdirect.routing.proxy.stub.roadnet.RouteNetPortType;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService_Service;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService_ServiceLocator;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_PortType;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_Service;
import com.freshdirect.routing.proxy.stub.transportation.TransportationWebService_ServiceLocator;
import com.freshdirect.routing.util.RoutingServicesProperties;

public class RoutingServiceLocator {

	private static RoutingServiceLocator instance = null;

	private BeanFactory factory = null;

	private RoutingServiceLocator() {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
		        new String[] {"com/freshdirect/routing/service/applicationContext-RoutingServices.xml"});
		//	of course, an ApplicationContext is just a BeanFactory
		factory = (BeanFactory) appContext;
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
	
	public RouteNetPortType getRouteNetService() throws ServiceException, MalformedURLException{
		RouteNetWebService_Service service = new RouteNetWebService_ServiceLocator();
		return service.getRouteNetWebService
							(new URL(RoutingServicesProperties.getRoadNetProviderURL()));
	}
	
	public TransportationWebService_PortType getTransportationSuiteService() throws ServiceException, MalformedURLException{
		TransportationWebService_Service service = new TransportationWebService_ServiceLocator();
		return service.getTransportationWebService(
							(new URL(RoutingServicesProperties.getTransportationSuiteProviderURL())));
	}
	
	public TransportationWebService_PortType getTransportationSuiteService(String deliveryType) throws ServiceException, MalformedURLException{
		TransportationWebService_Service service = new TransportationWebService_ServiceLocator();
		return service.getTransportationWebService(
							(new URL(RoutingServicesProperties.getTransportationSuiteProviderURL(deliveryType))));
	}

}
