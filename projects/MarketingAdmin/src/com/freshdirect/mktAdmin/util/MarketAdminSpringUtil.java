package com.freshdirect.mktAdmin.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class MarketAdminSpringUtil implements BeanFactoryAware{

	private static MarketAdminSpringUtil marketAdminSpringUtil = new MarketAdminSpringUtil();
	private BeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory lBeanFactory) throws BeansException {
		this.beanFactory = lBeanFactory;;		
	}

	public BeanFactory getBeanFactory(){
		return beanFactory;
	}
	public static MarketAdminSpringUtil getInstance(){
		return marketAdminSpringUtil;
	}
	
	private MarketAdminSpringUtil(){
		
	}
}
