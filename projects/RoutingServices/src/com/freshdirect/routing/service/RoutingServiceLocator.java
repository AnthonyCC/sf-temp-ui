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
		LOGGER.debug("RSL:getRouteNetService():"+ RoutingServicesProperties.getRoadNetProviderURL());
		initStub(stub);
		return stub;
	}

	public TransportationWebService getTransportationSuiteService() throws AxisFault {
		TransportationWebServiceStub stub = null;
		if(RoutingServicesProperties.isProxyEnabled()) {
			stub = new TransportationWebServiceStub(RoutingServicesProperties.getTransportationSuiteProxyURL());
			LOGGER.debug("RSL:getTransportationSuiteService()Proxy:"+ RoutingServicesProperties.getTransportationSuiteProxyURL());
		} else {
			stub = new TransportationWebServiceStub(RoutingServicesProperties.getTransportationSuiteProviderURL());
			LOGGER.debug("RSL:getTransportationSuiteService()No Proxy:"+ RoutingServicesProperties.getTransportationSuiteProviderURL());
		}
		initStub(stub);
		return stub;
	}



	public TransportationWebService getTransportationSuiteProviderService() throws AxisFault {
		TransportationWebServiceStub stub = new TransportationWebServiceStub(RoutingServicesProperties.getTransportationSuiteProviderURL());
		LOGGER.debug("RSL:getTransportationSuiteProviderService():"+ RoutingServicesProperties.getTransportationSuiteProviderURL());
		initStub(stub);
		return stub;
	}

	public TransportationWebService getTransportationSuiteService(String deliveryType) throws AxisFault {
		String url = null;
		if(RoutingServicesProperties.isProxyEnabled()) {
			url = RoutingServicesProperties.getTransportationSuiteProxyURL();
		} else {
			url = RoutingServicesProperties.getTransportationSuiteProviderURL(deliveryType);
		}
		

		if(url == null) {
			url = RoutingServicesProperties.getTransportationSuiteProviderURL();
			LOGGER.debug("getTransportationSuiteService Server not found :"+ deliveryType +":"+ url);
		} else {
			LOGGER.debug("getTransportationSuiteService:"+ deliveryType +":"+ url);
		}

		TransportationWebServiceStub stub = new TransportationWebServiceStub(url);
		initStub(stub);
		return stub;
	}

	public TransportationWebService getTransportationSuiteBatchProviderService() throws AxisFault {
		TransportationWebServiceStub stub = new TransportationWebServiceStub(RoutingServicesProperties
																.getTransportationSuiteBatchProviderURL());
		LOGGER.debug("RSL:getTransportationSuiteBatchProviderService():"+ RoutingServicesProperties
																	.getTransportationSuiteBatchProviderURL());
		initBatchStub(stub);
		return stub;
	}

	public TransportationWebService getTransportationSuiteDBatchProviderService() throws AxisFault {
		TransportationWebServiceStub stub = new TransportationWebServiceStub(RoutingServicesProperties
																.getTransportationSuiteDBatchProviderURL());
		LOGGER.debug("RSL:getTransportationSuiteDBatchProviderService():"+ RoutingServicesProperties
																		.getTransportationSuiteDBatchProviderURL());
		initBatchStub(stub);
		return stub;
	}

	public RouteNetWebService getRouteNetBatchService() throws AxisFault {
		LOGGER.debug("RSL:getRoadNetBatchProviderURL() >>"+RoutingServicesProperties.getRoadNetBatchProviderURL());
		RouteNetWebServiceStub stub = new RouteNetWebServiceStub(RoutingServicesProperties.getRoadNetBatchProviderURL());
		initBatchStub(stub);
		return stub;
	}

	private void initStub(Stub stub) {
		//System.out.println("RSL:initStub() >>"+stub._getServiceClient().getAxisService().getEndpointURL()+RoutingServicesProperties.getServiceTimeout());
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(RoutingServicesProperties.getServiceTimeout()*1000);
	}
	
	private void initBatchStub(Stub stub) {
		//System.out.println("RSL:initBatchStub() >>"+stub._getServiceClient().getAxisService().getEndpointURL()+RoutingServicesProperties.getBatchServiceTimeout());
		stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(RoutingServicesProperties.getBatchServiceTimeout()*1000);
	}

}
