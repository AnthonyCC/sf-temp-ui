package com.freshdirect.fdlogistics.services.impl;

import org.apache.log4j.Category;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.freshdirect.fdlogistics.services.IAirclicService;
import com.freshdirect.fdlogistics.services.ILogisticsService;
import com.freshdirect.framework.util.log.LoggerFactory;

public class LogisticsServiceLocator {

	private static LogisticsServiceLocator instance = null;

	private ApplicationContext factory = null;

	private static final Category LOGGER = LoggerFactory.getInstance(LogisticsServiceLocator.class);

	private LogisticsServiceLocator() {
		try {
		    factory = new ClassPathXmlApplicationContext(
		        new String[] {"com/freshdirect/fdlogistics/services/impl/applicationContext-LogisticsServices.xml"});
		} catch (Error r) {
			r.printStackTrace();
		}
	}

	public static LogisticsServiceLocator getInstance() {
	      if(instance == null) {
	         instance = new LogisticsServiceLocator();
	      }
	      return instance;
	   }

	public Object getService(String beanId) {
		return factory.getBean(beanId);
	}

	public IAirclicService getAirclicService() {
		return (IAirclicService)factory.getBean("airclicService");
	}

	public ILogisticsService getLogisticsService() {
		return (ILogisticsService)factory.getBean("logisticsService");
	}

}
